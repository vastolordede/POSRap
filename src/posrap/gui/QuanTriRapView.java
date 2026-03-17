package posrap.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Timestamp;
import java.util.List;

import posrap.dao.RapChieuDAO;
import posrap.dto.*;
import posrap.util.UiUtil;

public class QuanTriRapView extends JPanel {

    private final RapChieuDAO dao = new RapChieuDAO();

    public QuanTriRapView() {
        setLayout(new BorderLayout(10, 10));
        JTabbedPane tab = new JTabbedPane();

        tab.addTab("Phim", buildPhimTab());
        tab.addTab("Phong chieu", buildPhongTab());
        tab.addTab("Ghe", buildGheTab());
        tab.addTab("Suat chieu", buildSuatChieuTab());

        add(tab, BorderLayout.CENTER);
    }

    /* ===================== TAB PHIM ===================== */
    private JPanel buildPhimTab() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Ten phim", "The loai", "Thoi luong", "Do tuoi", "Trang thai"}, 0
        );
        JTable table = new JTable(model);
        reloadPhim(model);

        return wrapCrudTable(table,
                () -> reloadPhim(model),
                () -> {
                    JTextField txtTen = new JTextField();
                    JTextField txtTheLoai = new JTextField();
                    JTextField txtThoiLuong = new JTextField();
                    JTextField txtDoTuoi = new JTextField();
                    JComboBox<String> cbTrangThai = new JComboBox<>(new String[]{"Dang chieu", "Ngung chieu", "Sap chieu"});

                    JPanel p = new JPanel(new GridLayout(5, 2, 10, 10));
                    p.add(new JLabel("Tên phim:")); p.add(txtTen);
                    p.add(new JLabel("Thể loại:")); p.add(txtTheLoai);
                    p.add(new JLabel("Thời lượng (phút):")); p.add(txtThoiLuong);
                    p.add(new JLabel("Độ tuổi:")); p.add(txtDoTuoi);
                    p.add(new JLabel("Trạng thái:")); p.add(cbTrangThai);

                    if (JOptionPane.showConfirmDialog(this, p, "Thêm Phim Mới", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
                        try {
                            PhimDTO f = new PhimDTO();
                            f.setTenPhim(txtTen.getText().trim());
                            f.setTheLoai(txtTheLoai.getText().trim());
                            f.setThoiLuong(Integer.parseInt(txtThoiLuong.getText().trim()));
                            f.setDoTuoi(txtDoTuoi.getText().trim());
                            f.setTrangThai((String) cbTrangThai.getSelectedItem());
                            dao.insertPhim(f);
                            reloadPhim(model);
                        } catch (Exception ex) { UiUtil.showInfo(this, "Lỗi: Kiểm tra lại số liệu nhập!"); }
                    }
                },
                () -> {
                    int r = table.getSelectedRow();
                    if (r < 0) { UiUtil.showInfo(this, "Chọn 1 dòng để sửa."); return; }
                    
                    JTextField txtTen = new JTextField(model.getValueAt(r, 1).toString());
                    JTextField txtTheLoai = new JTextField(model.getValueAt(r, 2).toString());
                    JTextField txtThoiLuong = new JTextField(model.getValueAt(r, 3).toString());
                    JTextField txtDoTuoi = new JTextField(model.getValueAt(r, 4).toString());
                    JComboBox<String> cbTrangThai = new JComboBox<>(new String[]{"Dang chieu", "Ngung chieu", "Sap chieu"});
                    cbTrangThai.setSelectedItem(model.getValueAt(r, 5).toString());

                    JPanel p = new JPanel(new GridLayout(5, 2, 10, 10));
                    p.add(new JLabel("Tên phim:")); p.add(txtTen);
                    p.add(new JLabel("Thể loại:")); p.add(txtTheLoai);
                    p.add(new JLabel("Thời lượng (phút):")); p.add(txtThoiLuong);
                    p.add(new JLabel("Độ tuổi:")); p.add(txtDoTuoi);
                    p.add(new JLabel("Trạng thái:")); p.add(cbTrangThai);

                    if (JOptionPane.showConfirmDialog(this, p, "Sửa Phim", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
                        try {
                            int id = ((Number) model.getValueAt(r, 0)).intValue();
                            PhimDTO f = new PhimDTO();
                            f.setPhimId(id);
                            f.setTenPhim(txtTen.getText().trim());
                            f.setTheLoai(txtTheLoai.getText().trim());
                            f.setThoiLuong(Integer.parseInt(txtThoiLuong.getText().trim()));
                            f.setDoTuoi(txtDoTuoi.getText().trim());
                            f.setTrangThai((String) cbTrangThai.getSelectedItem());
                            dao.updatePhim(f);
                            reloadPhim(model);
                        } catch (Exception ex) { UiUtil.showInfo(this, "Lỗi: Kiểm tra lại số liệu nhập!"); }
                    }
                },
                () -> {
                    int[] rows = table.getSelectedRows();
                    if (rows.length == 0) { UiUtil.showInfo(this, "Kéo chuột chọn nhiều dòng để xóa."); return; }
                    try {
                        if (!UiUtil.confirm(this, "Xóa " + rows.length + " phim đã chọn?")) return;
                        for (int i = 0; i < rows.length; i++) {
                            int id = ((Number) model.getValueAt(rows[i], 0)).intValue();
                            dao.deletePhim(id);
                        }
                        reloadPhim(model);
                    } catch (Exception ex) { UiUtil.showInfo(this, "Lỗi: " + ex.getMessage()); }
                }
        );
    }

    private void reloadPhim(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            List<PhimDTO> ds = dao.getAllPhim();
            for (PhimDTO p : ds) {
                model.addRow(new Object[]{
                        p.getPhimId(),
                        p.getTenPhim(),
                        p.getTheLoai(),
                        p.getThoiLuong(),
                        p.getDoTuoi(),
                        p.getTrangThai()
                });
            }
        } catch (Exception ex) {
            UiUtil.showInfo(this, ex.getMessage());
        }
    }

    /* ===================== TAB PHONG ===================== */
    private JPanel buildPhongTab() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Ten phong", "Suc chua"}, 0
        );
        JTable table = new JTable(model);
        reloadPhong(model);

        return wrapCrudTable(table,
                () -> reloadPhong(model),
                () -> {
                   JTextField txtTen = new JTextField();
                    JTextField txtSucChua = new JTextField("20"); 

                    JPanel p = new JPanel(new GridLayout(3, 2, 10, 10));
                    p.add(new JLabel("Tên phòng:")); p.add(txtTen);
                    p.add(new JLabel("Sức chứa:")); p.add(txtSucChua);
                    
                    if (JOptionPane.showConfirmDialog(this, p, "Thêm Phòng", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
                        try {
                            PhongChieuDTO pc = new PhongChieuDTO();
                            pc.setTenPhong(txtTen.getText().trim());
                            pc.setSucChua(Integer.parseInt(txtSucChua.getText().trim()));
                            
                            dao.insertPhong(pc);
    
                            reloadPhong(model);
                            updatelistPhong();
                        } catch (Exception ex) { UiUtil.showInfo(this, "Lỗi nhập liệu: " + ex.getMessage()); }
                    }
                },
                () -> {
                    int r = table.getSelectedRow();
                    if (r < 0) { UiUtil.showInfo(this, "Chọn 1 dòng để sửa."); return; }
                    
                    JTextField txtTen = new JTextField(model.getValueAt(r, 1).toString());
                    String oldSucChua = model.getColumnCount() > 2 ? model.getValueAt(r, 2).toString() : "50";
                    JTextField txtSucChua = new JTextField(oldSucChua);


                    JPanel p = new JPanel(new GridLayout(3, 2, 10, 10));
                    p.add(new JLabel("Tên phòng:")); p.add(txtTen);
                    p.add(new JLabel("Sức chứa:")); p.add(txtSucChua);
                    
                    if (JOptionPane.showConfirmDialog(this, p, "Sửa Phòng", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
                        try {
                            int id = ((Number) model.getValueAt(r, 0)).intValue();
                            PhongChieuDTO pc = new PhongChieuDTO();
                            pc.setPhongChieuId(id);
                            pc.setTenPhong(txtTen.getText().trim());
                            pc.setSucChua(Integer.parseInt(txtSucChua.getText().trim()));
                            
                            dao.updatePhong(pc);
                            
                            reloadPhong(model);
                            updatelistPhong();
                        } catch (Exception ex) { UiUtil.showInfo(this, "Lỗi nhập liệu: " + ex.getMessage()); }
                    }
                },
                () -> {
                    int[] rows = table.getSelectedRows();
                    if (rows.length == 0) { UiUtil.showInfo(this, "Chọn nhiều dòng để xóa."); return; }
                    try {
                        if (!UiUtil.confirm(this, "Xóa " + rows.length + " phòng đã chọn?")) return;
                        for (int i = 0; i < rows.length; i++) {
                            int id = ((Number) model.getValueAt(rows[i], 0)).intValue();
                            dao.deletePhong(id); 
                        }
                        reloadPhong(model);
                        updatelistPhong();
                    } catch (Exception ex) { UiUtil.showInfo(this, "Lỗi: " + ex.getMessage()); }
                }
        );
    }

    private void reloadPhong(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            for (PhongChieuDTO pc : dao.getAllPhong()) {
                model.addRow(new Object[]{
                        pc.getPhongChieuId(),
                        pc.getTenPhong(),
                        pc.getSucChua()
                });
            }
        } catch (Exception ex) {
            UiUtil.showInfo(this, ex.getMessage());
        }
    }

    private JPanel pnlSoDoGheWrapper = new JPanel(new GridBagLayout()); 
    private JComboBox<String> cbLPhongChieu = new JComboBox<>();
    private java.util.List<PhongChieuDTO> listPhongCache = new java.util.ArrayList<>();

    // ===================== TAB GHE ===================== 
    private JPanel buildGheTab() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Phong ID", "Hang", "So", "Loai ghe"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        JTable table = new JTable(model);
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        
        pnlTop.add(new JLabel("Xem phòng:"));
        
        cbLPhongChieu.addActionListener(e -> taoGhe(model));
        pnlTop.add(cbLPhongChieu);
        
        pnlTop.add(new JLabel(" | "));

        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnTaiLai = new JButton("Tải lại");

        pnlTop.add(btnThem);
        pnlTop.add(btnSua);
        pnlTop.add(btnXoa);
        pnlTop.add(btnTaiLai);

        root.add(pnlTop, BorderLayout.NORTH);
        btnThem.addActionListener(e -> DialogThem(model));

        btnSua.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r < 0) { UiUtil.showInfo(this, "Chọn 1 dòng để sửa."); return; }
            try {
                int gheId = ((Number) model.getValueAt(r, 0)).intValue();
                String phongIdStr = JOptionPane.showInputDialog(this, "Phong ID:", model.getValueAt(r, 1));
                if (phongIdStr == null || phongIdStr.trim().isEmpty()) return;
                String hang = JOptionPane.showInputDialog(this, "Hang:", model.getValueAt(r, 2));
                if (hang == null || hang.trim().isEmpty()) return;
                String soStr = JOptionPane.showInputDialog(this, "So:", model.getValueAt(r, 3));
                if (soStr == null || soStr.trim().isEmpty()) return;
                String loai = JOptionPane.showInputDialog(this, "Loai ghe:", model.getValueAt(r, 4));
                if (loai == null || loai.trim().isEmpty()) return;

                GheDTO g = new GheDTO();
                g.setGheId(gheId);
                g.setPhongChieuId(Integer.parseInt(phongIdStr.trim()));
                g.setHangGhe(hang.trim());
                g.setSoGhe(Integer.parseInt(soStr.trim()));
                g.setLoaiGhe(loai.trim());

                dao.updateGhe(g);
                taoGhe(model);
            } catch (Exception ex) { UiUtil.showInfo(this, ex.getMessage()); }
        });

        btnXoa.addActionListener(e -> {
            int[] rows = table.getSelectedRows();
            if (rows.length == 0) { UiUtil.showInfo(this, "Kéo chuột chọn nhiều dòng trong bảng để xóa."); return; }
            try {
                if (!UiUtil.confirm(this, "Xóa " + rows.length + " ghế đã chọn?")) return;
                
                for (int i = 0; i < rows.length; i++) {
                    int gheId = ((Number) model.getValueAt(rows[i], 0)).intValue();
                    dao.deleteGhe(gheId); 
                }
                taoGhe(model); 
            } catch (Exception ex) { UiUtil.showInfo(this, ex.getMessage()); }
        });

        btnTaiLai.addActionListener(e -> {
            updatelistPhong();
            taoGhe(model);
        });

        updatelistPhong();
        taoGhe(model);

        pnlSoDoGheWrapper.setBackground(Color.WHITE);
        JScrollPane scrollSoDo = new JScrollPane(pnlSoDoGheWrapper);
        scrollSoDo.setBorder(BorderFactory.createTitledBorder("Sơ đồ trực quan"));
        
        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(BorderFactory.createTitledBorder("Dữ liệu bảng"));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTable, scrollSoDo);
        split.setResizeWeight(0.4); 
        root.add(split, BorderLayout.CENTER);

        reloadGhe(model);
        return root;
    }

    private void reloadGhe(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            List<GheDTO> ds = dao.getAllGhe();
            for (GheDTO g : ds) {
                model.addRow(new Object[]{
                        g.getGheId(),
                        g.getPhongChieuId(),
                        g.getHangGhe(),
                        g.getSoGhe(),
                        g.getLoaiGhe()
                });
            }
        } catch (Exception ex) {
            UiUtil.showInfo(this, ex.getMessage());
        }
    }

    /* ===================== TAB SUAT CHIEU ===================== */
    private JPanel buildSuatChieuTab() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Phim ID", "Phong ID", "Bat dau", "Ket thuc", "Gia"}, 0
        );
        JTable table = new JTable(model);
        reloadSuatChieu(model);

        return wrapCrudTable(table,
                () -> reloadSuatChieu(model),
                () -> {
                    JTextField txtPhimId = new JTextField();
                    JTextField txtPhongId = new JTextField();
                    JTextField txtBatDau = new JTextField("2026-03-15 18:00:00");
                    JTextField txtKetThuc = new JTextField("2026-03-15 20:00:00");
                    JTextField txtGia = new JTextField("60000");

                    JPanel p = new JPanel(new GridLayout(5, 2, 10, 10));
                    p.add(new JLabel("Phim ID:")); p.add(txtPhimId);
                    p.add(new JLabel("Phòng ID:")); p.add(txtPhongId);
                    p.add(new JLabel("Bắt đầu (N-T-N G:P:S):")); p.add(txtBatDau);
                    p.add(new JLabel("Kết thúc (N-T-N G:P:S):")); p.add(txtKetThuc);
                    p.add(new JLabel("Giá vé (VNĐ):")); p.add(txtGia);

                    if (JOptionPane.showConfirmDialog(this, p, "Thêm Suất Chiếu", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
                        try {
                            SuatChieuDTO sc = new SuatChieuDTO();
                            sc.setPhimId(Integer.parseInt(txtPhimId.getText().trim()));
                            sc.setPhongChieuId(Integer.parseInt(txtPhongId.getText().trim()));
                            sc.setBatDau(java.sql.Timestamp.valueOf(txtBatDau.getText().trim()));
                            sc.setKetThuc(java.sql.Timestamp.valueOf(txtKetThuc.getText().trim()));
                            sc.setGia(Double.parseDouble(txtGia.getText().trim()));
                            dao.insertSuatChieu(sc);
                            reloadSuatChieu(model);
                        } catch (Exception ex) { UiUtil.showInfo(this, "Lỗi nhập liệu: Sai định dạng ngày giờ hoặc số!"); }
                    }
                },
                () -> {
                    int r = table.getSelectedRow();
                    if (r < 0) { UiUtil.showInfo(this, "Chọn 1 dòng để sửa."); return; }
                    
                    JTextField txtPhimId = new JTextField(model.getValueAt(r, 1).toString());
                    JTextField txtPhongId = new JTextField(model.getValueAt(r, 2).toString());
                    JTextField txtBatDau = new JTextField(model.getValueAt(r, 3).toString());
                    JTextField txtKetThuc = new JTextField(model.getValueAt(r, 4).toString());
                    JTextField txtGia = new JTextField(model.getValueAt(r, 5).toString());

                    JPanel p = new JPanel(new GridLayout(5, 2, 10, 10));
                    p.add(new JLabel("Phim ID:")); p.add(txtPhimId);
                    p.add(new JLabel("Phòng ID:")); p.add(txtPhongId);
                    p.add(new JLabel("Bắt đầu:")); p.add(txtBatDau);
                    p.add(new JLabel("Kết thúc:")); p.add(txtKetThuc);
                    p.add(new JLabel("Giá vé:")); p.add(txtGia);

                    if (JOptionPane.showConfirmDialog(this, p, "Sửa Suất Chiếu", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
                        try {
                            int id = ((Number) model.getValueAt(r, 0)).intValue();
                            SuatChieuDTO sc = new SuatChieuDTO();
                            sc.setSuatChieuId(id);
                            sc.setPhimId(Integer.parseInt(txtPhimId.getText().trim()));
                            sc.setPhongChieuId(Integer.parseInt(txtPhongId.getText().trim()));
                            sc.setBatDau(java.sql.Timestamp.valueOf(txtBatDau.getText().trim()));
                            sc.setKetThuc(java.sql.Timestamp.valueOf(txtKetThuc.getText().trim()));
                            sc.setGia(Double.parseDouble(txtGia.getText().trim()));
                            dao.updateSuatChieu(sc);
                            reloadSuatChieu(model);
                        } catch (Exception ex) { UiUtil.showInfo(this, "Lỗi nhập liệu: Sai định dạng ngày giờ hoặc số!"); }
                    }
                },
                () -> {
                    int[] rows = table.getSelectedRows();
                    if (rows.length == 0) { UiUtil.showInfo(this, "Kéo chuột chọn nhiều dòng để xóa."); return; }
                    try {
                        if (!UiUtil.confirm(this, "Xóa " + rows.length + " suất chiếu đã chọn?")) return;
                        for (int i = 0; i < rows.length; i++) {
                            int id = ((Number) model.getValueAt(rows[i], 0)).intValue();
                            dao.deleteSuatChieu(id);
                        }
                        reloadSuatChieu(model);
                    } catch (Exception ex) { UiUtil.showInfo(this, "Lỗi: " + ex.getMessage()); }
                }
        );
    }

    private void reloadSuatChieu(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            for (SuatChieuDTO s : dao.getAllSuatChieu()) {
                model.addRow(new Object[]{
                        s.getSuatChieuId(),
                        s.getPhimId(),
                        s.getPhongChieuId(),
                        s.getBatDau(),
                        s.getKetThuc(),
                        s.getGia()
                });
            }
        } catch (Exception ex) {
            UiUtil.showInfo(this, ex.getMessage());
        }
    }

    /* ===================== WRAP CRUD ===================== */
    private JPanel wrapCrudTable(
            JTable table,
            Runnable reload,
            Runnable onAdd,
            Runnable onEdit,
            Runnable onDelete
    ) {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JButton btnThem = new JButton("Them");
        JButton btnSua = new JButton("Sua");
        JButton btnXoa = new JButton("Xoa");
        JButton btnTaiLai = new JButton("Tai lai");

        btnThem.addActionListener(e -> { if (onAdd != null) onAdd.run(); });
        btnSua.addActionListener(e -> { if (onEdit != null) onEdit.run(); });
        btnXoa.addActionListener(e -> { if (onDelete != null) onDelete.run(); });
        btnTaiLai.addActionListener(e -> reload.run());

        top.add(btnThem);
        top.add(btnSua);
        top.add(btnXoa);
        top.add(btnTaiLai);

        root.add(top, BorderLayout.NORTH);
        root.add(new JScrollPane(table), BorderLayout.CENTER);
        return root;
    }

    private void updatelistPhong() {
        try {
            java.awt.event.ActionListener[] listeners = cbLPhongChieu.getActionListeners();
            for (java.awt.event.ActionListener l : listeners) cbLPhongChieu.removeActionListener(l);
            
            listPhongCache = dao.getAllPhongChieu();
            cbLPhongChieu.removeAllItems();
            for (PhongChieuDTO p : listPhongCache) {
                cbLPhongChieu.addItem(p.getTenPhong());
            }
            if (!listPhongCache.isEmpty()) cbLPhongChieu.setSelectedIndex(0);

            for (java.awt.event.ActionListener l : listeners) cbLPhongChieu.addActionListener(l);
        } catch (Exception ex) { }
    }

    private void taoGhe(DefaultTableModel model) {
        int idx = cbLPhongChieu.getSelectedIndex();
        if (idx < 0 || listPhongCache.isEmpty()) {
            model.setRowCount(0);
            pnlSoDoGheWrapper.removeAll();
            pnlSoDoGheWrapper.repaint();
            return;
        }

        int phongId = listPhongCache.get(idx).getPhongChieuId();
        pnlSoDoGheWrapper.removeAll(); 

        pnlSoDoGheWrapper.setLayout(new GridBagLayout());

        try {
            java.util.List<GheDTO> dsGhe = dao.getGheByPhong(phongId);
            
            model.setRowCount(0);
            for (GheDTO g : dsGhe) {
                model.addRow(new Object[]{
                    g.getGheId(), g.getPhongChieuId(), g.getHangGhe(), g.getSoGhe(), g.getLoaiGhe()
                });
            }

            if (dsGhe.isEmpty()) {
                pnlSoDoGheWrapper.add(new JLabel("Phòng này chưa có sơ đồ. Bấm 'Tạo sơ đồ' nhé!"));
            } else {
                JPanel pnlRows = new JPanel();
                pnlRows.setLayout(new BoxLayout(pnlRows, BoxLayout.Y_AXIS)); 
                pnlRows.setBackground(Color.WHITE);

                java.util.Map<String, java.util.List<GheDTO>> rowMap = new java.util.TreeMap<>();
                for (GheDTO g : dsGhe) {
                    String hang = g.getHangGhe();
                    if (hang != null && !hang.trim().isEmpty()) {
                        rowMap.computeIfAbsent(hang.trim().toUpperCase(), k -> new java.util.ArrayList<>()).add(g);
                    }
                }

                for (java.util.Map.Entry<String, java.util.List<GheDTO>> entry : rowMap.entrySet()) {
                    JPanel pnlOneRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0)); 
                    pnlOneRow.setBackground(Color.WHITE);
                    
                    java.util.List<GheDTO> seatsInRow = entry.getValue();
                    seatsInRow.sort(java.util.Comparator.comparingInt(GheDTO::getSoGhe));

                    int totalSeats = seatsInRow.size();
                    int gapLoiDi = 60; 

                    for (int i = 0; i < totalSeats; i++) {
                        GheDTO g = seatsInRow.get(i);
                        JToggleButton btn = new JToggleButton(g.getHangGhe() + g.getSoGhe()); 
                        
                        
                        btn.setPreferredSize(new Dimension(95, 95));
                        btn.setFont(new Font("Arial", Font.BOLD, 13)); 
                        btn.setMargin(new Insets(2, 2, 2, 2));
                        
                        String loai = g.getLoaiGhe();
                        boolean isVip = (loai != null && loai.trim().equalsIgnoreCase("vip"));
                        btn.setBackground(isVip ? new Color(241, 196, 15) : new Color(224, 224, 224));
                        
                        pnlOneRow.add(btn);

                        if (totalSeats >= 4) { 
                            if (totalSeats % 2 == 0) { 
                                if (i == (totalSeats / 2) - 1) {
                                    pnlOneRow.add(Box.createHorizontalStrut(gapLoiDi));
                                }
                            } else { 
                                if (i == 0 || i == totalSeats - 2) {
                                    pnlOneRow.add(Box.createHorizontalStrut(gapLoiDi));
                                }
                            }
                        }
                    }
                    
                    pnlRows.add(pnlOneRow); 
                    pnlRows.add(Box.createVerticalStrut(20)); 
                }
                
               pnlSoDoGheWrapper.add(pnlRows);
            }
            
            pnlSoDoGheWrapper.revalidate();
            pnlSoDoGheWrapper.repaint();
            
        } catch (Exception ex) { 
            ex.printStackTrace();
            UiUtil.showInfo(this, "Lỗi tải sơ đồ: " + ex.getMessage());
        }
    }

    private void DialogThem(DefaultTableModel model) {
        try {
            if (listPhongCache.isEmpty()) {
                UiUtil.showInfo(this, "Chưa có phòng chiếu nào! Vui lòng tạo phòng trước."); 
                return;
            }

            JComboBox<String> cbPhong = new JComboBox<>();
            for (PhongChieuDTO p : listPhongCache) {
                cbPhong.addItem(p.getTenPhong());
            }

            int currentSelected = cbLPhongChieu.getSelectedIndex();
            if(currentSelected >= 0 && currentSelected < listPhongCache.size()) {
                cbPhong.setSelectedIndex(currentSelected);
            }

            JTextField txtSoHang = new JTextField("5");
            JTextField txtTongGhe = new JTextField("20");
            JComboBox<String> cbLoaiGhe = new JComboBox<>(new String[]{"thuong", "vip"});

            JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
            panel.add(new JLabel("Chọn phòng chiếu:"));
            panel.add(cbPhong);
            panel.add(new JLabel("Số lượng hàng (VD: 4):"));
            panel.add(txtSoHang);
            panel.add(new JLabel("Tổng số ghế (VD: 20):"));
            panel.add(txtTongGhe);
            panel.add(new JLabel("Loại ghế mặc định:"));
            panel.add(cbLoaiGhe);

            int result = JOptionPane.showConfirmDialog(this, panel, "Tạo sơ đồ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                int selectedIndex = cbPhong.getSelectedIndex();
                int phongChieuId = listPhongCache.get(selectedIndex).getPhongChieuId();
                
                int soHang = Integer.parseInt(txtSoHang.getText().trim());
                int tongGhe = Integer.parseInt(txtTongGhe.getText().trim());
                String loaiGhe = (String) cbLoaiGhe.getSelectedItem();

                dao.buildGhe(phongChieuId, soHang, tongGhe, loaiGhe);
                
                UiUtil.showInfo(this, "Tạo sơ đồ thành công!");
                
                cbLPhongChieu.setSelectedIndex(selectedIndex); 
                taoGhe(model);
            }
        } catch (NumberFormatException ex) {
            UiUtil.showInfo(this, "Lỗi: Số hàng và tổng ghế bắt buộc phải là số!");
        } catch (Exception ex) {
            ex.printStackTrace(); 
            UiUtil.showInfo(this, "Lỗi hệ thống thao tác Ghế: \n" + ex.getMessage());
        }
    }
}
