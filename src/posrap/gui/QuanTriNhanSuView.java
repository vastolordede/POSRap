package posrap.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import posrap.dao.NhanVienDAO;
import posrap.dto.NhanVienDTO;
import posrap.util.UiUtil;

public class QuanTriNhanSuView extends JPanel {

    private final NhanVienDAO nhanVienDAO = new NhanVienDAO();

    public QuanTriNhanSuView() {
        setLayout(new BorderLayout(10, 10));

        add(buildNhanVienTab(), BorderLayout.CENTER);
    }

    /* ======================================================
       TAB: NHÂN VIÊN (CHỈ XEM)
     ====================================================== */
    private JPanel buildNhanVienTab() {
        DefaultTableModel model = new DefaultTableModel(new Object[]{
                "ID", "Ma", "Ho ten", "SDT", "Email", "Trang thai"
        }, 0);

        JTable table = new JTable(model);

        reloadNhanVien(model);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        root.add(new JScrollPane(table), BorderLayout.CENTER);

        return root;
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
}