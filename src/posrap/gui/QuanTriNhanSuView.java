package posrap.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import posrap.dao.*;
import posrap.dto.*;
import posrap.util.UiUtil;

public class QuanTriNhanSuView extends JPanel {

    private final NhanVienDAO nhanVienDAO = new NhanVienDAO();
    private final CaLamDAO caLamDAO = new CaLamDAO();
    private final PhanCongCaDAO phanCongDAO = new PhanCongCaDAO();
    private final BangLuongDAO bangLuongDAO = new BangLuongDAO();

    public QuanTriNhanSuView() {
        setLayout(new BorderLayout(10, 10));
        JTabbedPane tab = new JTabbedPane();

        tab.addTab("Nhan vien", buildNhanVienTab());
        tab.addTab("Ca lam", buildCaLamTab());
        tab.addTab("Phan cong ca", buildPhanCongTab());
        tab.addTab("Bang luong", buildLuongTab());

        add(tab, BorderLayout.CENTER);
    }

    /* ======================================================
       TAB 1: NHÂN VIÊN
     ====================================================== */
    private JPanel buildNhanVienTab() {
    DefaultTableModel model = new DefaultTableModel(new Object[]{
            "ID", "Ma", "Ho ten", "SDT", "Email", "Trang thai"
    }, 0);
    JTable table = new JTable(model);
    reloadNhanVien(model);

    return wrapCrudTable("Nhan vien", table, model,
            () -> reloadNhanVien(model),
            () -> UiUtil.showInfo(this, "NhanVienDAO chua co insert()."),
            () -> UiUtil.showInfo(this, "NhanVienDAO chua co update()."),
            () -> UiUtil.showInfo(this, "NhanVienDAO chua co delete().")
    );
}


    private void reloadNhanVien(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            List<NhanVienDTO> list = nhanVienDAO.getAllNhanVien();
            for (NhanVienDTO nv : list) {
                model.addRow(new Object[]{
                        nv.getNhanVienId(),
                        nv.getMaNv(),
                        nv.getHoTen(),
                        nv.getSdt(),
                        nv.getEmail(),
                        nv.getTrangThai()
                });
            }
        } catch (Exception ex) {
            UiUtil.showInfo(this, ex.getMessage());
        }
    }

    /* ======================================================
       TAB 2: CA LÀM
     ====================================================== */
    private JPanel buildCaLamTab() {
        DefaultTableModel model = new DefaultTableModel(new Object[]{
                "ID", "Gio bat dau", "Gio ket thuc"
        }, 0);
        JTable table = new JTable(model);
        reloadCaLam(model);

        return wrapCrudTable("Ca lam", table, model,
                () -> reloadCaLam(model),
                () -> {
                    try {
                        String bd = JOptionPane.showInputDialog(this, "Gio bat dau (HH:mm):");
                        String kt = JOptionPane.showInputDialog(this, "Gio ket thuc (HH:mm):");
                        if (bd == null || kt == null) return;

                        CaLamDTO ca = new CaLamDTO();
                        ca.setGioBatDau(java.sql.Time.valueOf(bd + ":00"));
                        ca.setGioKetThuc(java.sql.Time.valueOf(kt + ":00"));
                        caLamDAO.insert(ca);
                        reloadCaLam(model);
                    } catch (Exception ex) {
                        UiUtil.showInfo(this, ex.getMessage());
                    }
                },
                () -> {
                    int r = table.getSelectedRow();
                    if (r < 0) return;
                    try {
                        int id = (int) model.getValueAt(r, 0);
                        String bd = JOptionPane.showInputDialog(this, "Gio bat dau moi:");
                        String kt = JOptionPane.showInputDialog(this, "Gio ket thuc moi:");
                        if (bd == null || kt == null) return;

                        CaLamDTO ca = new CaLamDTO();
                        ca.setCaLamId(id);
                        ca.setGioBatDau(java.sql.Time.valueOf(bd + ":00"));
                        ca.setGioKetThuc(java.sql.Time.valueOf(kt + ":00"));
                        caLamDAO.update(ca);
                        reloadCaLam(model);
                    } catch (Exception ex) {
                        UiUtil.showInfo(this, ex.getMessage());
                    }
                },
                () -> {
                    int r = table.getSelectedRow();
                    if (r < 0) return;
                    try {
                        int id = (int) model.getValueAt(r, 0);
                        if (!UiUtil.confirm(this, "Xoa ca lam nay?")) return;
                        caLamDAO.delete(id);
                        reloadCaLam(model);
                    } catch (Exception ex) {
                        UiUtil.showInfo(this, ex.getMessage());
                    }
                }
        );
    }

    private void reloadCaLam(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            for (CaLamDTO ca : caLamDAO.getAll()) {
                model.addRow(new Object[]{
                        ca.getCaLamId(),
                        ca.getGioBatDau(),
                        ca.getGioKetThuc()
                });
            }
        } catch (Exception ex) {
            UiUtil.showInfo(this, ex.getMessage());
        }
    }

    /* ======================================================
       TAB 3: PHÂN CÔNG CA
     ====================================================== */
    private JPanel buildPhanCongTab() {
        DefaultTableModel model = new DefaultTableModel(new Object[]{
                "Nhan vien", "Ca", "Ngay"
        }, 0);
        JTable table = new JTable(model);

        int thang = LocalDate.now().getMonthValue();
        int nam = LocalDate.now().getYear();
        reloadPhanCong(model, thang, nam);

        return wrapCrudTable("Phan cong ca", table, model,
                () -> reloadPhanCong(model, thang, nam),
                () -> {
                    try {
                        int nv = Integer.parseInt(JOptionPane.showInputDialog(this, "Nhan vien ID:"));
                        int ca = Integer.parseInt(JOptionPane.showInputDialog(this, "Ca lam ID:"));
                        String ngay = JOptionPane.showInputDialog(this, "Ngay (YYYY-MM-DD):");

                        PhanCongCaDTO p = new PhanCongCaDTO();
                        p.setNhanVienId(nv);
                        p.setCaLamId(ca);
                        p.setNgayLam(ngay);
                        phanCongDAO.insert(p);
                        reloadPhanCong(model, thang, nam);
                    } catch (Exception ex) {
                        UiUtil.showInfo(this, ex.getMessage());
                    }
                },
                null,
                () -> {
                    int r = table.getSelectedRow();
                    if (r < 0) return;
                    try {
                        PhanCongCaDTO p = new PhanCongCaDTO();
                        p.setNhanVienId(Integer.parseInt(model.getValueAt(r, 0).toString()));
                        p.setCaLamId(Integer.parseInt(model.getValueAt(r, 1).toString()));
                        p.setNgayLam(model.getValueAt(r, 2).toString());
                        phanCongDAO.delete(p);
                        reloadPhanCong(model, thang, nam);
                    } catch (Exception ex) {
                        UiUtil.showInfo(this, ex.getMessage());
                    }
                }
        );
    }

    private void reloadPhanCong(DefaultTableModel model, int thang, int nam) {
        model.setRowCount(0);
        try {
            for (PhanCongCaDTO p : phanCongDAO.getByMonth(thang, nam)) {
                model.addRow(new Object[]{
                        p.getNhanVienId(),
                        p.getCaLamId(),
                        p.getNgayLam()
                });
            }
        } catch (Exception ex) {
            UiUtil.showInfo(this, ex.getMessage());
        }
    }

    /* ======================================================
       TAB 4: BẢNG LƯƠNG
     ====================================================== */
    private JPanel buildLuongTab() {
        DefaultTableModel model = new DefaultTableModel(new Object[]{
                "Nhan vien", "Thang", "Tong luong"
        }, 0);
        JTable table = new JTable(model);

        JPanel root = wrapCrudTable("Bang luong", table, model,
                () -> reloadLuong(model),
                null, null
        );

        JButton btnTinhLuong = new JButton("Tinh luong");
        btnTinhLuong.addActionListener(e -> tinhLuong(model));
        ((JPanel) root.getComponent(0)).add(btnTinhLuong);

        return root;
    }

    private void tinhLuong(DefaultTableModel model) {
        try {
            int thang = Integer.parseInt(JOptionPane.showInputDialog(this, "Thang (MM):"));
            int nam = Integer.parseInt(JOptionPane.showInputDialog(this, "Nam (YYYY):"));
            String thangStr = String.format("%04d-%02d", nam, thang);

            Map<Integer, Double> gio = nhanVienDAO.tinhTongGioTheoThang(thang, nam);
            for (Map.Entry<Integer, Double> e : gio.entrySet()) {
                double tongLuong = e.getValue() * 30000; // đơn giá giờ
                bangLuongDAO.upsertBangLuong(e.getKey(), thangStr, tongLuong);
            }

            reloadLuong(model);
            UiUtil.showInfo(this, "Tinh luong thanh cong cho " + thangStr);
        } catch (Exception ex) {
            UiUtil.showInfo(this, ex.getMessage());
        }
    }

    private void reloadLuong(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            String thang = JOptionPane.showInputDialog(this, "Nhap thang (YYYY-MM):");
            if (thang == null) return;
            for (BangLuongDTO b : bangLuongDAO.getByThang(thang)) {
                model.addRow(new Object[]{
                        b.getNhanVienId(),
                        b.getThang(),
                        b.getTongLuong()
                });
            }
        } catch (Exception ex) {
            UiUtil.showInfo(this, ex.getMessage());
        }
    }

    /* ======================================================
       HÀM BAO TABLE CHUNG (KHÔNG ĐỔI UI)
     ====================================================== */
    private JPanel wrapCrudTable(
            String title,
            JTable table,
            DefaultTableModel model,
            Runnable reload,
            Runnable onAdd,
            Runnable onEdit
    ) {
        return wrapCrudTable(title, table, model, reload, onAdd, onEdit, null);
    }

    private JPanel wrapCrudTable(
            String title,
            JTable table,
            DefaultTableModel model,
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

        btnThem.addActionListener(e -> { if (onAdd != null) onAdd.run(); });
        btnSua.addActionListener(e -> { if (onEdit != null) onEdit.run(); });
        btnXoa.addActionListener(e -> { if (onDelete != null) onDelete.run(); });

        top.add(btnThem);
        top.add(btnSua);
        top.add(btnXoa);

        root.add(top, BorderLayout.NORTH);
        root.add(new JScrollPane(table), BorderLayout.CENTER);
        return root;
    }
}
