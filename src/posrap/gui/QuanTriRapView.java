package posrap.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import posrap.dao.RapChieuDAO;
import posrap.dto.*;
import posrap.util.UiUtil;

public class QuanTriRapView extends JPanel {

    private final RapChieuDAO dao = new RapChieuDAO();

    public QuanTriRapView() {
        setLayout(new BorderLayout(10, 10));
        JTabbedPane tab = new JTabbedPane();

        tab.addTab("Phim", buildCrudPanel("phim"));
        tab.addTab("Phong chieu", buildCrudPanel("phong_chieu"));
        tab.addTab("Ghe", buildCrudPanel("ghe"));
        tab.addTab("Suat chieu", buildCrudPanel("suat_chieu"));

        add(tab, BorderLayout.CENTER);
    }

    private JPanel buildCrudPanel(String ten) {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        // giữ UI cột ID/Ten/Trang thai như bạn đang có
        model.addColumn("ID");
        model.addColumn("Ten");
        model.addColumn("Trang thai");

        // load DB thật vào bảng
        reloadTable(ten, model);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JButton btnThem = new JButton("Them");
        JButton btnSua = new JButton("Sua");
        JButton btnXoa = new JButton("Xoa");

        btnThem.addActionListener(e -> {
            // Không thay UI => dùng JOptionPane nhập nhanh
            try {
                if ("phim".equals(ten)) {
                    String tenPhim = JOptionPane.showInputDialog(this, "Ten phim:");
                    if (tenPhim == null || tenPhim.trim().isEmpty()) return;

                    PhimDTO p = new PhimDTO();
                    p.setTenPhim(tenPhim.trim());
                    p.setTheLoai("khac");
                    p.setThoiLuong(90);
                    p.setDoTuoi("P");
                    p.setTrangThai("dang_hoat_dong");

                    dao.insertPhim(p);
                    reloadTable(ten, model);
                    return;
                }

                UiUtil.showInfo(this, "Tab '" + ten + "' chua co CRUD day du (can them DAO insert/update/delete).");
            } catch (Exception ex) {
                UiUtil.showInfo(this, ex.getMessage());
            }
        });

        btnSua.addActionListener(e -> {
            if (table.getSelectedRow() < 0) { UiUtil.showInfo(this, "Chon dong de sua."); return; }
            try {
                if ("phim".equals(ten)) {
                    int row = table.getSelectedRow();
                    int id = ((Number) model.getValueAt(row, 0)).intValue();
                    String oldTen = String.valueOf(model.getValueAt(row, 1));
                    String oldTrangThai = String.valueOf(model.getValueAt(row, 2));

                    String newTen = JOptionPane.showInputDialog(this, "Ten phim moi:", oldTen);
                    if (newTen == null || newTen.trim().isEmpty()) return;

                    String newTrangThai = JOptionPane.showInputDialog(this, "Trang thai:", oldTrangThai);
                    if (newTrangThai == null || newTrangThai.trim().isEmpty()) return;

                    PhimDTO p = new PhimDTO();
                    p.setPhimId(id);
                    p.setTenPhim(newTen.trim());
                    // giữ các field khác tạm default nếu bạn chưa sửa UI nhập đủ
                    p.setTheLoai("khac");
                    p.setThoiLuong(90);
                    p.setDoTuoi("P");
                    p.setTrangThai(newTrangThai.trim());

                    dao.updatePhim(p);
                    reloadTable(ten, model);
                    return;
                }

                UiUtil.showInfo(this, "Tab '" + ten + "' chua co CRUD day du (can them DAO insert/update/delete).");
            } catch (Exception ex) {
                UiUtil.showInfo(this, ex.getMessage());
            }
        });

        btnXoa.addActionListener(e -> {
            if (table.getSelectedRow() < 0) { UiUtil.showInfo(this, "Chon dong de xoa."); return; }
            if (!UiUtil.confirm(this, "Xoa dong da chon?")) return;

            try {
                int row = table.getSelectedRow();
                int id = ((Number) model.getValueAt(row, 0)).intValue();

                if ("phim".equals(ten)) {
                    dao.deletePhim(id);
                    reloadTable(ten, model);
                    return;
                }

                UiUtil.showInfo(this, "Tab '" + ten + "' chua co CRUD day du (can them DAO delete).");
            } catch (Exception ex) {
                UiUtil.showInfo(this, ex.getMessage());
            }
        });

        top.add(btnThem); top.add(btnSua); top.add(btnXoa);
        root.add(top, BorderLayout.NORTH);
        root.add(new JScrollPane(table), BorderLayout.CENTER);
        return root;
    }

    private void reloadTable(String ten, DefaultTableModel model) {
        model.setRowCount(0);

        try {
            if ("phim".equals(ten)) {
                List<PhimDTO> ds = dao.getAllPhim();
                for (PhimDTO p : ds) {
                    model.addRow(new Object[]{p.getPhimId(), p.getTenPhim(), p.getTrangThai()});
                }
                return;
            }

            if ("phong_chieu".equals(ten)) {
                List<PhongChieuDTO> ds = dao.getAllPhong();
                for (PhongChieuDTO pc : ds) {
                    model.addRow(new Object[]{pc.getPhongChieuId(), pc.getTenPhong(), pc.getTrangThai()});
                }
                return;
            }

            if ("ghe".equals(ten)) {
                List<GheDTO> ds = dao.getAllGhe();
                for (GheDTO g : ds) {
                    String tenGhe = (g.getHangGhe() == null ? "" : g.getHangGhe()) + g.getSoGhe()
                            + " | phong:" + g.getPhongChieuId()
                            + " | loai:" + g.getLoaiGhe();
                    model.addRow(new Object[]{g.getGheId(), tenGhe, "ok"});
                }
                return;
            }

            if ("suat_chieu".equals(ten)) {
                List<SuatChieuDTO> ds = dao.getAllSuatChieu();
                for (SuatChieuDTO s : ds) {
                    String tenSuat = "phim:" + s.getPhimId() + " | phong:" + s.getPhongChieuId()
                            + " | " + s.getBatDau();
                    model.addRow(new Object[]{s.getSuatChieuId(), tenSuat, "gia:" + (long)s.getGia()});
                }
                return;
            }

        } catch (Exception ex) {
            // nếu lỗi DB thì giữ bảng trống và show message
            UiUtil.showInfo(this, "Khong tai duoc du lieu '" + ten + "': " + ex.getMessage());
        }
    }
}
