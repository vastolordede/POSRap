package posrap.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import posrap.dao.BapNuocDAO;
import posrap.dto.*;
import posrap.util.UiUtil;

public class QuanTriBapNuocView extends JPanel {

    private final BapNuocDAO dao = new BapNuocDAO();

    public QuanTriBapNuocView() {
        setLayout(new BorderLayout(10, 10));
        JTabbedPane tab = new JTabbedPane();

        tab.addTab("Mon", buildMonTab());
        tab.addTab("Size", buildSizeTab());
        tab.addTab("Bien the (mon+size+gia+ton)", buildBienTheTab());

        add(tab, BorderLayout.CENTER);
    }

    /* ================= TAB MON ================= */

    private JPanel buildMonTab() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Ten", "Gia", "Ton", "Trang thai"}, 0
        );
        JTable table = new JTable(model);
        reloadMon(model);

        return wrapCrud(table, model,
                () -> reloadMon(model),
                () -> {
                    try {
                        String ten = JOptionPane.showInputDialog(this, "Ten mon:");
                        int ton = Integer.parseInt(JOptionPane.showInputDialog(this, "Ton kho:"));
                        if (ten == null) return;

                        MonBapNuocDTO m = new MonBapNuocDTO();
                        m.setTenMon(ten);
                        m.setTonKho(ton);
                        dao.insertMon(m);
                        reloadMon(model);
                    } catch (Exception ex) {
                        UiUtil.showInfo(this, ex.getMessage());
                    }
                },
                () -> {
                    int r = table.getSelectedRow();
                    if (r < 0) return;
                    try {
                        int id = (int) model.getValueAt(r, 0);
                        String ten = JOptionPane.showInputDialog(this, "Ten moi:", model.getValueAt(r,1));
                        int ton = Integer.parseInt(JOptionPane.showInputDialog(this, "Ton kho moi:", model.getValueAt(r,3)));

                        MonBapNuocDTO m = new MonBapNuocDTO();
                        m.setMonId(id);
                        m.setTenMon(ten);
                        m.setTonKho(ton);
                        dao.updateMon(m);
                        reloadMon(model);
                    } catch (Exception ex) {
                        UiUtil.showInfo(this, ex.getMessage());
                    }
                },
                () -> {
                    int r = table.getSelectedRow();
                    if (r < 0) return;
                    try {
                        if (!UiUtil.confirm(this, "Xoa mon nay?")) return;
                        dao.deleteMon((int) model.getValueAt(r, 0));
                        reloadMon(model);
                    } catch (Exception ex) {
                        UiUtil.showInfo(this, ex.getMessage());
                    }
                }
        );
    }

    private void reloadMon(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            for (MonBapNuocDTO m : dao.getAllMon()) {
                model.addRow(new Object[]{
                        m.getMonId(), m.getTenMon(), "", m.getTonKho(), "dang_ban"
                });
            }
        } catch (Exception ex) {
            UiUtil.showInfo(this, ex.getMessage());
        }
    }

    /* ================= TAB SIZE ================= */

    private JPanel buildSizeTab() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Ten", "Gia", "Ton", "Trang thai"}, 0
        );
        JTable table = new JTable(model);
        reloadSize(model);

        return wrapCrud(table, model,
                () -> reloadSize(model),
                () -> {
                    try {
                        String ten = JOptionPane.showInputDialog(this, "Ten size:");
                        if (ten == null) return;
                        SizeDTO s = new SizeDTO();
                        s.setTenSize(ten);
                        dao.insertSize(s);
                        reloadSize(model);
                    } catch (Exception ex) {
                        UiUtil.showInfo(this, ex.getMessage());
                    }
                },
                () -> {
                    int r = table.getSelectedRow();
                    if (r < 0) return;
                    try {
                        int id = (int) model.getValueAt(r, 0);
                        String ten = JOptionPane.showInputDialog(this, "Ten moi:", model.getValueAt(r,1));
                        SizeDTO s = new SizeDTO();
                        s.setSizeId(id);
                        s.setTenSize(ten);
                        dao.updateSize(s);
                        reloadSize(model);
                    } catch (Exception ex) {
                        UiUtil.showInfo(this, ex.getMessage());
                    }
                },
                () -> {
                    int r = table.getSelectedRow();
                    if (r < 0) return;
                    try {
                        if (!UiUtil.confirm(this, "Xoa size nay?")) return;
                        dao.deleteSize((int) model.getValueAt(r, 0));
                        reloadSize(model);
                    } catch (Exception ex) {
                        UiUtil.showInfo(this, ex.getMessage());
                    }
                }
        );
    }

    private void reloadSize(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            for (SizeDTO s : dao.getAllSize()) {
                model.addRow(new Object[]{s.getSizeId(), s.getTenSize(), "", "", ""});
            }
        } catch (Exception ex) {
            UiUtil.showInfo(this, ex.getMessage());
        }
    }

    /* ================= TAB BIEN THE ================= */

    private JPanel buildBienTheTab() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Ten", "Gia", "Ton", "Trang thai"}, 0
        );
        JTable table = new JTable(model);
        reloadBienThe(model);

        return wrapCrud(table, model,
                () -> reloadBienThe(model),
                () -> {
                    try {
                        int monId = Integer.parseInt(JOptionPane.showInputDialog(this, "Mon ID:"));
                        int sizeId = Integer.parseInt(JOptionPane.showInputDialog(this, "Size ID:"));
                        double gia = Double.parseDouble(JOptionPane.showInputDialog(this, "Gia:"));
                        dao.insertBienThe(monId, sizeId, gia);
                        reloadBienThe(model);
                    } catch (Exception ex) {
                        UiUtil.showInfo(this, ex.getMessage());
                    }
                },
                () -> {
                    int r = table.getSelectedRow();
                    if (r < 0) return;
                    try {
                        int id = (int) model.getValueAt(r, 0);
                        double gia = Double.parseDouble(JOptionPane.showInputDialog(this, "Gia moi:", model.getValueAt(r,2)));
                        dao.updateBienThe(id, gia);
                        reloadBienThe(model);
                    } catch (Exception ex) {
                        UiUtil.showInfo(this, ex.getMessage());
                    }
                },
                () -> {
                    int r = table.getSelectedRow();
                    if (r < 0) return;
                    try {
                        if (!UiUtil.confirm(this, "Xoa bien the nay?")) return;
                        dao.deleteBienThe((int) model.getValueAt(r, 0));
                        reloadBienThe(model);
                    } catch (Exception ex) {
                        UiUtil.showInfo(this, ex.getMessage());
                    }
                }
        );
    }

    private void reloadBienThe(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            List<Object[]> list = dao.getAllBienTheView();
            for (Object[] r : list) {
                model.addRow(new Object[]{r[0], r[1] + " - " + r[2], r[3], "", "dang_ban"});
            }
        } catch (Exception ex) {
            UiUtil.showInfo(this, ex.getMessage());
        }
    }

    /* ================= WRAP ================= */

    private JPanel wrapCrud(
            JTable table,
            DefaultTableModel model,
            Runnable reload,
            Runnable add,
            Runnable edit,
            Runnable del
    ) {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JButton btnThem = new JButton("Them");
        JButton btnSua = new JButton("Sua");
        JButton btnXoa = new JButton("Xoa");

        btnThem.addActionListener(e -> add.run());
        btnSua.addActionListener(e -> edit.run());
        btnXoa.addActionListener(e -> del.run());

        top.add(btnThem);
        top.add(btnSua);
        top.add(btnXoa);

        root.add(top, BorderLayout.NORTH);
        root.add(new JScrollPane(table), BorderLayout.CENTER);
        return root;
    }
}
