package posrap.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

import posrap.dao.BapNuocDAO;
import posrap.dto.HoaDonBapNuocDTO;
import posrap.util.UiUtil;

public class QuanLyHoaDonBapNuocView extends JPanel {

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Mã Hóa Đơn", "Mã Nhân Viên", "Ngày Lập", "Tổng Tiền (VNĐ)", "Đã Hủy"}, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(model);
    private final BapNuocDAO bapNuocDAO = new BapNuocDAO();

    public QuanLyHoaDonBapNuocView() {
        setLayout(new BorderLayout(10, 10));

        table.setRowHeight(26);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                boolean daHuy = (boolean) table.getModel().getValueAt(row, 4);

                if (daHuy) {
                    c.setBackground(Color.LIGHT_GRAY);
                    c.setForeground(Color.RED);
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        });

        add(buildTopPanel(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();
    }

    private JPanel buildTopPanel() {
        Font btnFont = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
        Dimension btnSize = new Dimension(150, 36);

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton btnRefresh = new JButton("Làm mới");
        JButton btnDetail  = new JButton("Xem chi tiết");
        JButton btnCancel  = new JButton("Hủy hóa đơn");

        for (JButton btn : new JButton[]{btnRefresh, btnDetail, btnCancel}) {
            btn.setFont(btnFont);
            btn.setPreferredSize(btnSize);
        }

        btnRefresh.addActionListener(e -> loadData());
        btnDetail.addActionListener(e -> showInvoiceDetail());
        btnCancel.addActionListener(e -> cancelSelectedInvoice());

        p.add(btnRefresh);
        p.add(btnDetail);
        p.add(btnCancel);

        return p;
    }

    private void loadData() {
        model.setRowCount(0);

        try {
            List<HoaDonBapNuocDTO> list = bapNuocDAO.getAllHoaDon();

            for (HoaDonBapNuocDTO hd : list) {
                model.addRow(new Object[]{
                        hd.getHoaDonId(),
                        hd.getNhanVienId(),
                        hd.getNgayLap(),
                        (long) hd.getTongTien(),
                        hd.isDaHuy()
                });
            }

        } catch (Exception ex) {
            UiUtil.showInfo(this, "Không thể tải danh sách: " + ex.getMessage());
        }
    }

    private void showInvoiceDetail() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow < 0) {
            UiUtil.showInfo(this, "Vui lòng chọn một hóa đơn!");
            return;
        }

        int hoaDonId = (int) model.getValueAt(selectedRow, 0);

        try {
            String details = bapNuocDAO.getChiTietHoaDonString(hoaDonId);

            JOptionPane.showMessageDialog(
                    this,
                    details,
                    "Chi tiết Hóa Đơn #" + hoaDonId,
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            UiUtil.showInfo(this, "Lỗi khi lấy chi tiết: " + ex.getMessage());
        }
    }

    private void cancelSelectedInvoice() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow < 0) {
            UiUtil.showInfo(this, "Vui lòng chọn một hóa đơn!");
            return;
        }

        int hoaDonId = (int) model.getValueAt(selectedRow, 0);
        boolean daHuy = (boolean) model.getValueAt(selectedRow, 4);

        if (daHuy) {
            UiUtil.showInfo(this, "Hóa đơn này đã bị hủy!");
            return;
        }

        if (UiUtil.confirm(this,
                "Bạn chắc chắn muốn HỦY hóa đơn #" + hoaDonId +
                " ?\nHành động này KHÔNG thể khôi phục!")) {

            try {
                bapNuocDAO.huyHoaDon(hoaDonId);
                UiUtil.showInfo(this, "Hủy hóa đơn thành công!");
                loadData();

            } catch (Exception ex) {
                UiUtil.showInfo(this, "Lỗi khi hủy: " + ex.getMessage());
            }
        }
    }
}