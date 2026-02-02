package posrap.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

import posrap.bus.BanBapNuocBUS;
import posrap.dto.*;
import posrap.util.UiUtil;

public class BanBapNuocView extends JPanel {

    // GIỮ UI: vẫn JComboBox<String> như bạn đang có
    private final JComboBox<String> cbMon = new JComboBox<>();
    private final JComboBox<String> cbSize = new JComboBox<>();
    private final JSpinner spSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));

    private final DefaultTableModel modelGio = new DefaultTableModel(
            new Object[]{"Mon", "Size", "So luong", "Don gia", "Thanh tien"}, 0
    );
    private final JTable tblGio = new JTable(modelGio);

    private final JLabel lblTong = new JLabel("Tong: 0");

    // BUS
    private final BanBapNuocBUS bus = new BanBapNuocBUS();

    // map tên -> id để GUI gọi BUS theo DB
    private final Map<String, Integer> monIdByTen = new HashMap<>();
    private final Map<String, Integer> sizeIdByTen = new HashMap<>();

    // giỏ hàng thật (giữ bienTheId để thanh toán)
    private static class GioItem {
        int bienTheId;
        String monTen;
        String sizeTen;
        int soLuong;
        int donGia;

        GioItem(int bienTheId, String monTen, String sizeTen, int soLuong, int donGia) {
            this.bienTheId = bienTheId;
            this.monTen = monTen;
            this.sizeTen = sizeTen;
            this.soLuong = soLuong;
            this.donGia = donGia;
        }
    }
    private final List<GioItem> gioHang = new ArrayList<>();

    public BanBapNuocView() {
        setLayout(new BorderLayout(10, 10));
        add(buildTopAdd(), BorderLayout.NORTH);
        add(new JScrollPane(tblGio), BorderLayout.CENTER);
        add(buildBottomPay(), BorderLayout.SOUTH);

        // nạp combo từ DB thật
        loadCombosFromDb();
    }

    private void loadCombosFromDb() {
        cbMon.removeAllItems();
        cbSize.removeAllItems();
        monIdByTen.clear();
        sizeIdByTen.clear();

        try {
            List<MonBapNuocDTO> mons = bus.layDanhSachMon();
            for (MonBapNuocDTO m : mons) {
                cbMon.addItem(m.getTenMon());
                monIdByTen.put(m.getTenMon(), m.getMonId());
            }

            List<SizeDTO> sizes = bus.layDanhSachSize();
            for (SizeDTO s : sizes) {
                cbSize.addItem(s.getTenSize());
                sizeIdByTen.put(s.getTenSize(), s.getSizeId());
            }

            if (cbMon.getItemCount() > 0) cbMon.setSelectedIndex(0);
            if (cbSize.getItemCount() > 0) cbSize.setSelectedIndex(0);

        } catch (Exception ex) {
            UiUtil.showInfo(this, "Khong tai duoc mon/size: " + ex.getMessage());
        }
    }

    private JComponent buildTopAdd() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton btnThem = new JButton("Them vao gio");
        JButton btnXoaDong = new JButton("Xoa dong");

        btnThem.addActionListener(e -> {
            String monTen = (String) cbMon.getSelectedItem();
            String sizeTen = (String) cbSize.getSelectedItem();
            if (monTen == null || sizeTen == null) {
                UiUtil.showInfo(this, "Chua co du lieu mon/size.");
                return;
            }

            int soLuong = (int) spSoLuong.getValue();

            try {
                BienTheMonDTO bt = layBienTheTheoTen(monTen, sizeTen);
                if (bt == null) {
                    UiUtil.showInfo(this, "Khong tim thay bien the (mon+size) trong DB.");
                    return;
                }

                int donGia = (int) bt.getGia();
                long thanhTien = (long) soLuong * donGia;

                // add vào table (UI giữ nguyên)
                modelGio.addRow(new Object[]{monTen, sizeTen, soLuong, donGia, thanhTien});

                // add vào giỏ thật để thanh toán
                gioHang.add(new GioItem(bt.getBienTheId(), monTen, sizeTen, soLuong, donGia));

                capNhatTong();

            } catch (Exception ex) {
                UiUtil.showInfo(this, "Loi tinh gia: " + ex.getMessage());
            }
        });

        btnXoaDong.addActionListener(e -> {
            int row = tblGio.getSelectedRow();
            if (row < 0) { UiUtil.showInfo(this, "Chon 1 dong de xoa."); return; }
            if (!UiUtil.confirm(this, "Xoa dong da chon?")) return;

            // xóa đúng item trong gioHang theo index dòng
            if (row >= 0 && row < gioHang.size()) {
                GioItem it = gioHang.get(row);

        // ✅ DÙNG field để IDE hết warning + log nghiệp vụ
        System.out.println("Xoa mon: " + it.monTen + " size " + it.sizeTen);
                gioHang.remove(row);
            }

            modelGio.removeRow(row);
            capNhatTong();
        });

        p.add(new JLabel("Mon:"));
        p.add(cbMon);
        p.add(new JLabel("Size:"));
        p.add(cbSize);
        p.add(new JLabel("So luong:"));
        p.add(spSoLuong);
        p.add(btnThem);
        p.add(btnXoaDong);
        return p;
    }

    private JComponent buildBottomPay() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JComboBox<String> cbThanhToan = new JComboBox<>(new String[]{"tien_mat", "chuyen_khoan", "the"});
        JButton btnThanhToan = new JButton("Thanh toan");

        btnThanhToan.addActionListener(e -> {
            if (modelGio.getRowCount() == 0 || gioHang.isEmpty()) {
                UiUtil.showInfo(this, "Gio hang dang trong.");
                return;
            }

            try {
                // giống bên bán vé: dùng nhân viên hệ thống cố định
                

                List<ChiTietHoaDonBapNuocDTO> ct = new ArrayList<>();
                for (GioItem it : gioHang) {
                      System.out.println(it.monTen + " - " + it.sizeTen + " x " + it.soLuong + " | donGia=" + it.donGia);
                    ChiTietHoaDonBapNuocDTO c = new ChiTietHoaDonBapNuocDTO();
                    c.setBienTheId(it.bienTheId);
                    c.setSoLuong(it.soLuong);
                    ct.add(c);
                }

                HoaDonBapNuocDTO hd = bus.lapHoaDon(ct);

                UiUtil.showInfo(this,
                        "Thanh toan bap nuoc thanh cong. " +
                        "HD#" + hd.getHoaDonId() +
                        " | Tong: " + (long) hd.getTongTien() +
                        " | Phuong thuc: " + cbThanhToan.getSelectedItem()
                );

                // reset
                gioHang.clear();
                modelGio.setRowCount(0);
                capNhatTong();

            } catch (Exception ex) {
                UiUtil.showInfo(this, "Thanh toan that bai: " + ex.getMessage());
            }
        });

        p.add(lblTong);
        p.add(new JLabel("Thanh toan:"));
        p.add(cbThanhToan);
        p.add(btnThanhToan);
        return p;
    }

    private BienTheMonDTO layBienTheTheoTen(String monTen, String sizeTen) throws Exception {
        Integer monId = monIdByTen.get(monTen);
        Integer sizeId = sizeIdByTen.get(sizeTen);
        if (monId == null || sizeId == null) return null;
        return bus.layBienThe(monId, sizeId);
    }

    private void capNhatTong() {
        long tong = 0;
        for (int r = 0; r < modelGio.getRowCount(); r++) {
            tong += ((Number) modelGio.getValueAt(r, 4)).longValue();
        }
        lblTong.setText("Tong: " + tong);
    }
}
