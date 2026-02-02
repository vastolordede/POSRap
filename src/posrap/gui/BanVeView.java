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

    // giữ UI, chỉ đổi generic type để chứa DTO
    private final JComboBox<PhimDTO> cbPhim = new JComboBox<>();
    private final JComboBox<SuatChieuDTO> cbSuat = new JComboBox<>();
    private final JComboBox<PhongChieuDTO> cbPhong = new JComboBox<>();

    private final JPanel pnlGhe = new JPanel(new GridLayout(5, 10, 6, 6));

    private final DefaultTableModel modelGioVe = new DefaultTableModel(new Object[]{"Ghe", "Gia"}, 0);
    private final JTable tblGioVe = new JTable(modelGioVe);

    private final JLabel lblTongTien = new JLabel("Tong: 0");
    private final List<GheDTO> dsGheDaChon = new ArrayList<>();

    // map button -> GheDTO để xử lý
    private final Map<JToggleButton, GheDTO> mapBtnGhe = new HashMap<>();

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
            String txt = "Suat #" + value.getSuatChieuId() + " | " + f.format(value.getBatDau()) + " | Gia: " + (long)value.getGia();
            return new JLabel(txt);
        });

        cbPhong.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                new JLabel(value == null ? "" : value.getTenPhong())
        );
    }

    private JComponent buildTopFilter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        p.add(new JLabel("Phim:"));
        p.add(cbPhim);
        p.add(new JLabel("Suat:"));
        p.add(cbSuat);
        p.add(new JLabel("Phong:"));
        p.add(cbPhong);

        JButton btnTaiGhe = new JButton("Tai so do ghe");
        btnTaiGhe.addActionListener(e -> {
            dsGheDaChon.clear();
            modelGioVe.setRowCount(0);
            lblTongTien.setText("Tong: 0");
            loadGheTheoSuatDangChon();
        });
        p.add(btnTaiGhe);

        // events khi đổi phim/suất
        cbPhim.addActionListener(e -> loadSuatTheoPhimDangChon());
        cbSuat.addActionListener(e -> onSuatChanged());

        return p;
    }

    private JComponent buildCenter() {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.65);

        JPanel left = new JPanel(new BorderLayout(10, 10));
        left.setBorder(BorderFactory.createTitledBorder("So do ghe"));
        left.add(new JScrollPane(pnlGhe), BorderLayout.CENTER);

        JPanel right = new JPanel(new BorderLayout(10, 10));
        right.setBorder(BorderFactory.createTitledBorder("Gio ve"));
        right.add(new JScrollPane(tblGioVe), BorderLayout.CENTER);

        split.setLeftComponent(left);
        split.setRightComponent(right);
        return split;
    }

    private JComponent buildBottomPay() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JComboBox<String> cbThanhToan = new JComboBox<>(new String[]{"tien_mat", "chuyen_khoan", "the"});
        JButton btnThanhToan = new JButton("Thanh toan va in ve");
        JButton btnXoaGio = new JButton("Xoa gio");

        btnXoaGio.addActionListener(e -> {
            if (!UiUtil.confirm(this, "Xoa tat ca ve trong gio?")) return;
            dsGheDaChon.clear();
            modelGioVe.setRowCount(0);
            lblTongTien.setText("Tong: 0");
            loadGheTheoSuatDangChon();
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
                // bạn muốn login không liên quan nhân viên => dùng 1 nhân viên hệ thống cố định

                bus.lapHoaDonVe(
                        suat.getSuatChieuId(),
                        new ArrayList<>(dsGheDaChon)
                );

                UiUtil.showInfo(this, "Thanh toan thanh cong. Phuong thuc: " + cbThanhToan.getSelectedItem());

                // reset
                dsGheDaChon.clear();
                modelGioVe.setRowCount(0);
                capNhatTongTien();
                loadGheTheoSuatDangChon();
            } catch (Exception ex) {
                UiUtil.showInfo(this, ex.getMessage());
                // reload ghế để cập nhật ghế vừa bị người khác mua
                loadGheTheoSuatDangChon();
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
            pnlGhe.removeAll();
            pnlGhe.revalidate();
            pnlGhe.repaint();
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

        loadGheTheoSuatDangChon();
    }

    private void loadGheTheoSuatDangChon() {
        SuatChieuDTO suat = (SuatChieuDTO) cbSuat.getSelectedItem();
        if (suat == null) return;

        try {
            List<GheDTO> ghe = bus.laySoDoGhe(suat.getSuatChieuId());
            Set<Integer> gheDaBan = bus.getGheDaBan(suat.getSuatChieuId());

            buildSoDoGheFromDb(ghe, gheDaBan, suat);
        } catch (Exception ex) {
            UiUtil.showInfo(this, "Khong tai duoc so do ghe: " + ex.getMessage());
        }
    }

    // bus hiện chưa có hàm gheDaBan => gọi qua DAO thông qua BanVeBUS/VeDAO (tạm: cast dùng reflection không nên)
    // Cách sạch: bạn thêm 1 hàm trong BanVeBUS: getGheDaBan(int suatChieuId) { return veDAO.getGheDaBan(suatChieuId); }
    

    private void buildSoDoGheFromDb(List<GheDTO> dsGhe, Set<Integer> gheDaBan, SuatChieuDTO suat) {
        pnlGhe.removeAll();
        mapBtnGhe.clear();

        // DB có thể không đúng 5x10. Nhưng GUI bạn đang là GridLayout(5,10).
        // Mình vẫn giữ layout 5x10, và fill theo thứ tự ghế.
        // Nếu thiếu ghế -> ô trống. Nếu dư -> phần dư vẫn add nhưng GridLayout sẽ tự mở rộng (GridLayout sẽ tăng hàng).
        double gia = suat.getGia();

        for (GheDTO g : dsGhe) {
            String ma = (g.getHangGhe() == null ? "" : g.getHangGhe()) + g.getSoGhe();
            JToggleButton btn = new JToggleButton(ma);

            mapBtnGhe.put(btn, g);

            if (gheDaBan.contains(g.getGheId())) {
                btn.setEnabled(false);
                btn.setText(ma + " (da ban)");
            }

            btn.addActionListener(e -> onChonGhe(btn, g, (int) gia));
            pnlGhe.add(btn);
        }

        pnlGhe.revalidate();
        pnlGhe.repaint();
    }

    private void onChonGhe(JToggleButton btn, GheDTO ghe, int gia) {
        if (btn.isSelected()) {
            // tránh chọn trùng
            for (GheDTO g : dsGheDaChon) {
                if (g.getGheId() == ghe.getGheId()) return;
            }
            dsGheDaChon.add(ghe);
            String ma = (ghe.getHangGhe() == null ? "" : ghe.getHangGhe()) + ghe.getSoGhe();
            modelGioVe.addRow(new Object[]{ma, gia});
        } else {
            dsGheDaChon.removeIf(g -> g.getGheId() == ghe.getGheId());
            String ma = (ghe.getHangGhe() == null ? "" : ghe.getHangGhe()) + ghe.getSoGhe();
            for (int r = modelGioVe.getRowCount() - 1; r >= 0; r--) {
                if (ma.equals(modelGioVe.getValueAt(r, 0))) {
                    modelGioVe.removeRow(r);
                    break;
                }
            }
        }
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
