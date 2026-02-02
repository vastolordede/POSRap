package posrap.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import posrap.bus.BaoCaoBUS;
import posrap.dto.BaoCaoDTO;
import posrap.util.UiUtil;

public class BaoCaoView extends JPanel {

    private final JTextField txtTuNgay = new JTextField("2026-01-01", 10);
    private final JTextField txtDenNgay = new JTextField("2026-01-31", 10);

    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"Chi so", "Gia tri"}, 0);
    private final JTable t = new JTable(model);

    private final BaoCaoBUS bus = new BaoCaoBUS();

    public BaoCaoView() {
        setLayout(new BorderLayout(10, 10));
        add(buildTopFilter(), BorderLayout.NORTH);
        add(new JScrollPane(t), BorderLayout.CENTER);
    }

    private JComponent buildTopFilter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        p.add(new JLabel("Tu ngay:"));
        p.add(txtTuNgay);
        p.add(new JLabel("Den ngay:"));
        p.add(txtDenNgay);

        JButton btn = new JButton("Xem bao cao");
        btn.addActionListener(e -> loadBaoCaoThat());

        p.add(btn);
        return p;
    }

    private void loadBaoCaoThat() {
        model.setRowCount(0);

        String tu = txtTuNgay.getText().trim();
        String den = txtDenNgay.getText().trim();

        try {
            BaoCaoDTO bc = bus.xemBaoCao(tu, den);

            model.addRow(new Object[]{"Doanh thu ve", bc.getDoanhThuVe()});
            model.addRow(new Object[]{"Doanh thu bap nuoc", bc.getDoanhThuBapNuoc()});
            model.addRow(new Object[]{"Top phim", bc.getTopPhim()});
            model.addRow(new Object[]{"Top mon", bc.getTopMon()});

        } catch (Exception ex) {
            UiUtil.showInfo(this, "Khong tai duoc bao cao: " + ex.getMessage());
        }
    }
}
