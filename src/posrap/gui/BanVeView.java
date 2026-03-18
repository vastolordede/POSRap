package posrap.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import posrap.bus.BanVeBUS;
import posrap.dao.RapChieuDAO;
import posrap.dto.*;
import posrap.util.UiUtil;

public class BanVeView extends JPanel {

    private final JComboBox<PhimDTO> cbPhim = new JComboBox<>();
    private final JComboBox<SuatChieuDTO> cbSuat = new JComboBox<>();
    private final JComboBox<PhongChieuDTO> cbPhong = new JComboBox<>();

    private final JPanel pnlSoDoGheWrapper = new JPanel(new GridBagLayout());

    private final DefaultTableModel modelGioVe = new DefaultTableModel(new Object[]{"Ghe", "Gia"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tblGioVe = new JTable(modelGioVe);

    private final JLabel lblTongTien = new JLabel("Tong: 0");
    private final List<GheDTO> dsGheDaChon = new ArrayList<>();

    private final Map<JToggleButton, GheDTO> mapBtnGhe = new HashMap<>();

    private JToggleButton currentSelectedBtn = null;
    private GheDTO currentSelectedGhe = null;

    private final RapChieuDAO rapDAO = new RapChieuDAO();
    private final BanVeBUS bus = new BanVeBUS();

    public BanVeView() {
        setLayout(new BorderLayout(10, 10));
        add(buildTopFilter(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildBottomPay(), BorderLayout.SOUTH);

        initComboRenderers();
        loadPhim();
    }

    private void initComboRenderers() {
        cbPhim.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                new JLabel(value == null ? "" : value.getTenPhim())
        );

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        cbSuat.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            if (value == null) return new JLabel("");
            String txt = "Suat #" + value.getSuatChieuId() + " | " + f.format(value.getBatDau()) + " | Gia: " + (long) value.getGia();
            return new JLabel(txt);
        });

        cbPhong.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                new JLabel(value == null ? "" : value.getTenPhong())
        );
    }

    private JComponent buildTopFilter() {
        Font filterFont = new Font(Font.SANS_SERIF, Font.PLAIN, 14);

        // Áp font và kích thước cho các combobox
        cbPhim.setFont(filterFont);
        cbPhim.setPreferredSize(new Dimension(200, 36));

        cbSuat.setFont(filterFont);
        cbSuat.setPreferredSize(new Dimension(320, 36)); // dài hơn vì text suat dài

        cbPhong.setFont(filterFont);
        cbPhong.setPreferredSize(new Dimension(120, 36));

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JLabel lblPhim  = new JLabel("Phim:");
        JLabel lblSuat  = new JLabel("Suat:");
        JLabel lblPhong = new JLabel("Phong:");
        for (JLabel lbl : new JLabel[]{lblPhim, lblSuat, lblPhong}) lbl.setFont(filterFont);

        p.add(lblPhim);
        p.add(cbPhim);
        p.add(lblSuat);
        p.add(cbSuat);
        p.add(lblPhong);
        p.add(cbPhong);

        JButton btnTaiGhe = new JButton("Tai so do ghe");
        btnTaiGhe.setFont(filterFont);
        btnTaiGhe.setPreferredSize(new Dimension(160, 36));
        btnTaiGhe.addActionListener(e -> {
            dsGheDaChon.clear();
            modelGioVe.setRowCount(0);
            lblTongTien.setText("Tong: 0");
            sold();
        });
        p.add(btnTaiGhe);

        cbPhim.addActionListener(e -> loadSuatTheoPhimDangChon());
        cbSuat.addActionListener(e -> onSuatChanged());

        return p;
    }

    private JComponent buildCenter() {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.65);

        JPanel left = new JPanel(new BorderLayout(10, 10));
        left.setBorder(BorderFactory.createTitledBorder("So do ghe"));
        pnlSoDoGheWrapper.setBackground(Color.WHITE);
        left.add(new JScrollPane(pnlSoDoGheWrapper), BorderLayout.CENTER);

        JPanel right = new JPanel(new BorderLayout(10, 10));
        right.setBorder(BorderFactory.createTitledBorder("Gio ve"));
        right.add(new JScrollPane(tblGioVe), BorderLayout.CENTER);

        split.setLeftComponent(left);
        split.setRightComponent(right);
        return split;
    }

    private JComponent buildBottomPay() {
        Font filterFont = new Font(Font.SANS_SERIF, Font.PLAIN, 14);

        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JComboBox<String> cbThanhToan = new JComboBox<>(new String[]{"tien_mat", "chuyen_khoan", "the"});
        JButton btnThanhToan = new JButton("Thanh toan va in ve");
        JButton btnXoaGio = new JButton("Xoa gio");

        lblTongTien.setFont(filterFont);
        cbThanhToan.setFont(filterFont);
        cbThanhToan.setPreferredSize(new Dimension(160, 36));
        btnXoaGio.setFont(filterFont);
        btnXoaGio.setPreferredSize(new Dimension(110, 36));
        btnThanhToan.setFont(filterFont);
        btnThanhToan.setPreferredSize(new Dimension(200, 36));

        btnXoaGio.addActionListener(e -> {
            if (!UiUtil.confirm(this, "Xoa tat ca ve trong gio?")) return;
            dsGheDaChon.clear();
            modelGioVe.setRowCount(0);
            lblTongTien.setText("Tong: 0");
            sold();
        });

        btnThanhToan.addActionListener(e -> {
            if (dsGheDaChon.isEmpty()) {
                UiUtil.showInfo(this, "Chua chon ghe.");
                return;
            }
            SuatChieuDTO suat = (SuatChieuDTO) cbSuat.getSelectedItem();
            if (suat == null) {
                UiUtil.showInfo(this, "Chua chon suat chieu.");
                return;
            }

            try {
                bus.lapHoaDonVe(
                        suat.getSuatChieuId(),
                        new ArrayList<>(dsGheDaChon)
                );

                UiUtil.showInfo(this, "Thanh toan thanh cong. Phuong thuc: " + cbThanhToan.getSelectedItem());

                dsGheDaChon.clear();
                modelGioVe.setRowCount(0);
                capNhatTongTien();
                sold();
            } catch (Exception ex) {
                UiUtil.showInfo(this, ex.getMessage());
                sold();
            }
        });

        p.add(lblTongTien);
        p.add(new JLabel("Thanh toan:"));
        p.add(cbThanhToan);
        p.add(btnXoaGio);
        p.add(btnThanhToan);
        return p;
    }

    // ------------------ LOAD DATA THẬT ------------------

    private void loadPhim() {
        cbPhim.removeAllItems();
        try {
            List<PhimDTO> ds = rapDAO.getPhimDangHoatDong();
            for (PhimDTO p : ds) cbPhim.addItem(p);
            if (cbPhim.getItemCount() > 0) cbPhim.setSelectedIndex(0);
            loadSuatTheoPhimDangChon();
        } catch (Exception ex) {
            UiUtil.showInfo(this, "Khong tai duoc danh sach phim: " + ex.getMessage());
        }
    }

    private void loadSuatTheoPhimDangChon() {
        PhimDTO phim = (PhimDTO) cbPhim.getSelectedItem();
        cbSuat.removeAllItems();
        cbPhong.removeAllItems();

        if (phim == null) return;

        try {
            List<SuatChieuDTO> dsSuat = rapDAO.getSuatChieuByPhim(phim.getPhimId());
            for (SuatChieuDTO s : dsSuat) cbSuat.addItem(s);
            if (cbSuat.getItemCount() > 0) cbSuat.setSelectedIndex(0);
            onSuatChanged();
        } catch (Exception ex) {
            UiUtil.showInfo(this, "Khong tai duoc suat chieu: " + ex.getMessage());
        }
    }

    private void onSuatChanged() {
        SuatChieuDTO suat = (SuatChieuDTO) cbSuat.getSelectedItem();
        cbPhong.removeAllItems();

        if (suat == null) {
            pnlSoDoGheWrapper.removeAll();
            pnlSoDoGheWrapper.revalidate();
            pnlSoDoGheWrapper.repaint();
            return;
        }

        try {
            PhongChieuDTO phong = rapDAO.getPhongById(suat.getPhongChieuId());
            if (phong != null) cbPhong.addItem(phong);
        } catch (Exception ex) {
            UiUtil.showInfo(this, "Khong tai duoc phong chieu: " + ex.getMessage());
        }

        dsGheDaChon.clear();
        modelGioVe.setRowCount(0);
        capNhatTongTien();

        sold();
    }

    private void sold() {
        SuatChieuDTO suat = (SuatChieuDTO) cbSuat.getSelectedItem();
        if (suat == null) return;

        try {
            List<GheDTO> ghe = bus.laySoDoGhe(suat.getSuatChieuId());
            Set<Integer> gheDaBan = bus.getGheDaBan(suat.getSuatChieuId());

            buildGhe(ghe, gheDaBan, suat);
        } catch (Exception ex) {
            UiUtil.showInfo(this, "Khong tai duoc so do ghe: " + ex.getMessage());
        }
    }


    private void buildGhe(List<GheDTO> dsGhe, Set<Integer> gheDaBan, SuatChieuDTO suat) {
        pnlSoDoGheWrapper.removeAll();
        mapBtnGhe.clear();
        double gia = suat.getGia();

        if (dsGhe.isEmpty()) {
            pnlSoDoGheWrapper.add(new JLabel("Phòng chiếu này chưa được thiết lập sơ đồ ghế!"));
            pnlSoDoGheWrapper.revalidate();
            pnlSoDoGheWrapper.repaint();
            return;
        }

        JPanel pnlRows = new JPanel();
        pnlRows.setLayout(new BoxLayout(pnlRows, BoxLayout.Y_AXIS));
        pnlRows.setBackground(Color.WHITE);

        Map<String, List<GheDTO>> rowMap = new TreeMap<>();
        for (GheDTO g : dsGhe) {
            String hang = g.getHangGhe();
            if (hang != null && !hang.trim().isEmpty()) {
                rowMap.computeIfAbsent(hang.trim().toUpperCase(), k -> new ArrayList<>()).add(g);
            }
        }

        for (Map.Entry<String, List<GheDTO>> entry : rowMap.entrySet()) {
            JPanel pnlOneRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
            pnlOneRow.setBackground(Color.WHITE);

            List<GheDTO> seatsInRow = entry.getValue();
            seatsInRow.sort(Comparator.comparingInt(GheDTO::getSoGhe));

            int totalSeats = seatsInRow.size();
            int gapLoiDi = 60;

            for (int i = 0; i < totalSeats; i++) {
                GheDTO g = seatsInRow.get(i);
                String ma = g.getHangGhe() + g.getSoGhe();
                JToggleButton btn = new JToggleButton(ma);

                mapBtnGhe.put(btn, g);

                btn.setPreferredSize(new Dimension(95, 95));
                btn.setFont(new Font("Arial", Font.BOLD, 13));
                btn.setMargin(new Insets(2, 2, 2, 2));

                String loai = g.getLoaiGhe();
                boolean isVip = (loai != null && loai.trim().equalsIgnoreCase("vip"));

                if (gheDaBan.contains(g.getGheId())) {
                    btn.setEnabled(false);
                    btn.setBackground(Color.DARK_GRAY);
                    btn.setForeground(Color.WHITE);
                } else {
                    btn.setBackground(isVip ? new Color(241, 196, 15) : new Color(224, 224, 224));
                    btn.setForeground(Color.BLACK);
                    btn.addActionListener(e -> onChonGhe(btn, g, (int) gia));
                }

                pnlOneRow.add(btn);

                if (totalSeats >= 4) {
                    if (totalSeats % 2 == 0) {
                        if (i == (totalSeats / 2) - 1) pnlOneRow.add(Box.createHorizontalStrut(gapLoiDi));
                    } else {
                        if (i == 0 || i == totalSeats - 2) pnlOneRow.add(Box.createHorizontalStrut(gapLoiDi));
                    }
                }
            }

            pnlRows.add(pnlOneRow);
            pnlRows.add(Box.createVerticalStrut(20));
        }

        pnlSoDoGheWrapper.add(pnlRows);
        pnlSoDoGheWrapper.revalidate();
        pnlSoDoGheWrapper.repaint();
    }

    private void onChonGhe(JToggleButton btn, GheDTO ghe, int gia) {
        String loai = ghe.getLoaiGhe();
        boolean isVip = (loai != null && loai.trim().equalsIgnoreCase("vip"));
        Color colorMacDinh = isVip ? new Color(241, 196, 15) : new Color(224, 224, 224);
        Color colorDangChon = new Color(46, 204, 113);

        if (btn == currentSelectedBtn) {
            btn.setSelected(false);
            btn.setBackground(colorMacDinh);
            btn.setForeground(Color.BLACK);

            currentSelectedBtn = null;
            currentSelectedGhe = null;

            dsGheDaChon.clear();
            modelGioVe.setRowCount(0);
            capNhatTongTien();
            return;
        }

        if (currentSelectedBtn != null) {
            GheDTO gheCu = currentSelectedGhe;
            String loaiCu = gheCu.getLoaiGhe();
            boolean isVipCu = (loaiCu != null && loaiCu.trim().equalsIgnoreCase("vip"));
            Color colorCu = isVipCu ? new Color(241, 196, 15) : new Color(224, 224, 224);

            currentSelectedBtn.setSelected(false);
            currentSelectedBtn.setBackground(colorCu);
            currentSelectedBtn.setForeground(Color.BLACK);
        }

        btn.setSelected(true);
        btn.setBackground(colorDangChon);
        btn.setForeground(Color.WHITE);

        currentSelectedBtn = btn;
        currentSelectedGhe = ghe;

        dsGheDaChon.clear();
        dsGheDaChon.add(ghe);

        modelGioVe.setRowCount(0);
        String ma = (ghe.getHangGhe() == null ? "" : ghe.getHangGhe()) + ghe.getSoGhe();
        modelGioVe.addRow(new Object[]{ma, gia});

        capNhatTongTien();
    }

    private void capNhatTongTien() {
        long tong = 0;
        for (int r = 0; r < modelGioVe.getRowCount(); r++) {
            tong += ((Number) modelGioVe.getValueAt(r, 1)).longValue();
        }
        lblTongTien.setText("Tong: " + tong);
    }
}