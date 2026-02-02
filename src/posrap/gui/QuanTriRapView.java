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
                    try {
                        String ten = JOptionPane.showInputDialog(this, "Ten phim:");
                        if (ten == null || ten.trim().isEmpty()) return;

                        String theLoai = JOptionPane.showInputDialog(this, "The loai:");
                        if (theLoai == null || theLoai.trim().isEmpty()) return;

                        String tlStr = JOptionPane.showInputDialog(this, "Thoi luong (phut):");
                        if (tlStr == null || tlStr.trim().isEmpty()) return;

                        String doTuoi = JOptionPane.showInputDialog(this, "Do tuoi (P/C13/C16/C18...):");
                        if (doTuoi == null || doTuoi.trim().isEmpty()) return;

                        String trangThai = JOptionPane.showInputDialog(this, "Trang thai (dang_hoat_dong/..):");
                        if (trangThai == null || trangThai.trim().isEmpty()) return;

                        PhimDTO p = new PhimDTO();
                        p.setTenPhim(ten.trim());
                        p.setTheLoai(theLoai.trim());
                        p.setThoiLuong(Integer.parseInt(tlStr.trim()));
                        p.setDoTuoi(doTuoi.trim());
                        p.setTrangThai(trangThai.trim());

                        dao.insertPhim(p);
                        reloadPhim(model);
                    } catch (Exception ex) { UiUtil.showInfo(this, ex.getMessage()); }
                },
                () -> {
                    int r = table.getSelectedRow();
                    if (r < 0) { UiUtil.showInfo(this, "Chon dong de sua."); return; }

                    try {
                        int id = ((Number) model.getValueAt(r, 0)).intValue();

                        String ten = JOptionPane.showInputDialog(this, "Ten phim:", model.getValueAt(r, 1));
                        if (ten == null || ten.trim().isEmpty()) return;

                        String theLoai = JOptionPane.showInputDialog(this, "The loai:", model.getValueAt(r, 2));
                        if (theLoai == null || theLoai.trim().isEmpty()) return;

                        String tlStr = JOptionPane.showInputDialog(this, "Thoi luong (phut):", model.getValueAt(r, 3));
                        if (tlStr == null || tlStr.trim().isEmpty()) return;

                        String doTuoi = JOptionPane.showInputDialog(this, "Do tuoi:", model.getValueAt(r, 4));
                        if (doTuoi == null || doTuoi.trim().isEmpty()) return;

                        String trangThai = JOptionPane.showInputDialog(this, "Trang thai:", model.getValueAt(r, 5));
                        if (trangThai == null || trangThai.trim().isEmpty()) return;

                        PhimDTO p = new PhimDTO();
                        p.setPhimId(id);
                        p.setTenPhim(ten.trim());
                        p.setTheLoai(theLoai.trim());
                        p.setThoiLuong(Integer.parseInt(tlStr.trim()));
                        p.setDoTuoi(doTuoi.trim());
                        p.setTrangThai(trangThai.trim());

                        dao.updatePhim(p);
                        reloadPhim(model);
                    } catch (Exception ex) { UiUtil.showInfo(this, ex.getMessage()); }
                },
                () -> {
                    int r = table.getSelectedRow();
                    if (r < 0) { UiUtil.showInfo(this, "Chon dong de xoa."); return; }
                    try {
                        if (!UiUtil.confirm(this, "Xoa phim nay?")) return;
                        int id = ((Number) model.getValueAt(r, 0)).intValue();
                        dao.deletePhim(id);
                        reloadPhim(model);
                    } catch (Exception ex) { UiUtil.showInfo(this, ex.getMessage()); }
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
        // Theo diagram: PhongChieu chỉ có (id, ten_phong, suc_chua)
        // Nếu DB bạn có thêm trang_thai thì thêm cột "Trang thai" ở đây + addRow tương ứng.
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Ten phong", "Suc chua"}, 0
        );
        JTable table = new JTable(model);
        reloadPhong(model);

        return wrapCrudTable(table,
                () -> reloadPhong(model),
                () -> {
                    try {
                        String tenPhong = JOptionPane.showInputDialog(this, "Ten phong:");
                        if (tenPhong == null || tenPhong.trim().isEmpty()) return;

                        String sucChuaStr = JOptionPane.showInputDialog(this, "Suc chua:");
                        if (sucChuaStr == null || sucChuaStr.trim().isEmpty()) return;

                        PhongChieuDTO p = new PhongChieuDTO();
                        p.setTenPhong(tenPhong.trim());
                        p.setSucChua(Integer.parseInt(sucChuaStr.trim()));

                        dao.insertPhong(p);
                        reloadPhong(model);
                    } catch (Exception ex) { UiUtil.showInfo(this, ex.getMessage()); }
                },
                () -> {
                    int r = table.getSelectedRow();
                    if (r < 0) { UiUtil.showInfo(this, "Chon dong de sua."); return; }
                    try {
                        int id = ((Number) model.getValueAt(r, 0)).intValue();

                        String tenPhong = JOptionPane.showInputDialog(this, "Ten phong:", model.getValueAt(r, 1));
                        if (tenPhong == null || tenPhong.trim().isEmpty()) return;

                        String sucChuaStr = JOptionPane.showInputDialog(this, "Suc chua:", model.getValueAt(r, 2));
                        if (sucChuaStr == null || sucChuaStr.trim().isEmpty()) return;

                        PhongChieuDTO p = new PhongChieuDTO();
                        p.setPhongChieuId(id);
                        p.setTenPhong(tenPhong.trim());
                        p.setSucChua(Integer.parseInt(sucChuaStr.trim()));

                        dao.updatePhong(p);
                        reloadPhong(model);
                    } catch (Exception ex) { UiUtil.showInfo(this, ex.getMessage()); }
                },
                () -> {
                    int r = table.getSelectedRow();
                    if (r < 0) { UiUtil.showInfo(this, "Chon dong de xoa."); return; }
                    try {
                        if (!UiUtil.confirm(this, "Xoa phong nay? (co the anh huong ghe/suat chieu)")) return;
                        int id = ((Number) model.getValueAt(r, 0)).intValue();
                        dao.deletePhong(id);
                        reloadPhong(model);
                    } catch (Exception ex) { UiUtil.showInfo(this, ex.getMessage()); }
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

    /* ===================== TAB GHE ===================== */
    private JPanel buildGheTab() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Phong ID", "Hang", "So", "Loai ghe"}, 0
        );
        JTable table = new JTable(model);
        reloadGhe(model);

        return wrapCrudTable(table,
                () -> reloadGhe(model),
                () -> {
                    try {
                        int phongId = Integer.parseInt(JOptionPane.showInputDialog(this, "Phong ID:"));
                        String hang = JOptionPane.showInputDialog(this, "Hang ghe (A/B/C...):");
                        if (hang == null || hang.trim().isEmpty()) return;

                        int so = Integer.parseInt(JOptionPane.showInputDialog(this, "So ghe:"));
                        String loai = JOptionPane.showInputDialog(this, "Loai ghe:");
                        if (loai == null || loai.trim().isEmpty()) return;

                        GheDTO g = new GheDTO();
                        g.setPhongChieuId(phongId);
                        g.setHangGhe(hang.trim());
                        g.setSoGhe(so);
                        g.setLoaiGhe(loai.trim());

                        dao.insertGhe(g);
                        reloadGhe(model);
                    } catch (Exception ex) { UiUtil.showInfo(this, ex.getMessage()); }
                },
                () -> {
                    int r = table.getSelectedRow();
                    if (r < 0) { UiUtil.showInfo(this, "Chon dong de sua."); return; }
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
                        reloadGhe(model);
                    } catch (Exception ex) { UiUtil.showInfo(this, ex.getMessage()); }
                },
                () -> {
                    int r = table.getSelectedRow();
                    if (r < 0) { UiUtil.showInfo(this, "Chon dong de xoa."); return; }
                    try {
                        if (!UiUtil.confirm(this, "Xoa ghe nay?")) return;
                        int gheId = ((Number) model.getValueAt(r, 0)).intValue();
                        dao.deleteGhe(gheId);
                        reloadGhe(model);
                    } catch (Exception ex) { UiUtil.showInfo(this, ex.getMessage()); }
                }
        );
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
                    try {
                        int phimId = Integer.parseInt(JOptionPane.showInputDialog(this, "Phim ID:"));
                        int phongId = Integer.parseInt(JOptionPane.showInputDialog(this, "Phong ID:"));

                        String bd = JOptionPane.showInputDialog(this, "Bat dau (YYYY-MM-DD HH:mm:ss):");
                        if (bd == null || bd.trim().isEmpty()) return;

                        String kt = JOptionPane.showInputDialog(this, "Ket thuc (YYYY-MM-DD HH:mm:ss):");
                        if (kt == null || kt.trim().isEmpty()) return;

                        double gia = Double.parseDouble(JOptionPane.showInputDialog(this, "Gia:"));

                        SuatChieuDTO s = new SuatChieuDTO();
                        s.setPhimId(phimId);
                        s.setPhongChieuId(phongId);
                        s.setBatDau(Timestamp.valueOf(bd.trim()));
                        s.setKetThuc(Timestamp.valueOf(kt.trim()));
                        s.setGia(gia);

                        dao.insertSuatChieu(s);
                        reloadSuatChieu(model);
                    } catch (Exception ex) { UiUtil.showInfo(this, ex.getMessage()); }
                },
                () -> {
                    int r = table.getSelectedRow();
                    if (r < 0) { UiUtil.showInfo(this, "Chon dong de sua."); return; }
                    try {
                        int id = ((Number) model.getValueAt(r, 0)).intValue();

                        String phimIdStr = JOptionPane.showInputDialog(this, "Phim ID:", model.getValueAt(r, 1));
                        if (phimIdStr == null || phimIdStr.trim().isEmpty()) return;

                        String phongIdStr = JOptionPane.showInputDialog(this, "Phong ID:", model.getValueAt(r, 2));
                        if (phongIdStr == null || phongIdStr.trim().isEmpty()) return;

                        String bd = JOptionPane.showInputDialog(this, "Bat dau (YYYY-MM-DD HH:mm:ss):", model.getValueAt(r, 3));
                        if (bd == null || bd.trim().isEmpty()) return;

                        String kt = JOptionPane.showInputDialog(this, "Ket thuc (YYYY-MM-DD HH:mm:ss):", model.getValueAt(r, 4));
                        if (kt == null || kt.trim().isEmpty()) return;

                        String giaStr = JOptionPane.showInputDialog(this, "Gia:", model.getValueAt(r, 5));
                        if (giaStr == null || giaStr.trim().isEmpty()) return;

                        SuatChieuDTO s = new SuatChieuDTO();
                        s.setSuatChieuId(id);
                        s.setPhimId(Integer.parseInt(phimIdStr.trim()));
                        s.setPhongChieuId(Integer.parseInt(phongIdStr.trim()));
                        s.setBatDau(Timestamp.valueOf(bd.trim()));
                        s.setKetThuc(Timestamp.valueOf(kt.trim()));
                        s.setGia(Double.parseDouble(giaStr.trim()));

                        dao.updateSuatChieu(s);
                        reloadSuatChieu(model);
                    } catch (Exception ex) { UiUtil.showInfo(this, ex.getMessage()); }
                },
                () -> {
                    int r = table.getSelectedRow();
                    if (r < 0) { UiUtil.showInfo(this, "Chon dong de xoa."); return; }
                    try {
                        if (!UiUtil.confirm(this, "Xoa suat chieu nay?")) return;
                        int id = ((Number) model.getValueAt(r, 0)).intValue();
                        dao.deleteSuatChieu(id);
                        reloadSuatChieu(model);
                    } catch (Exception ex) { UiUtil.showInfo(this, ex.getMessage()); }
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
}
