package posrap.gui;

import javax.swing.*;
import java.awt.*;

public class QuanLyHoaDonContainerView extends JPanel {

    public QuanLyHoaDonContainerView() {

        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Hóa đơn vé", new QuanLyHoaDonView());
        tabs.addTab("Hóa đơn bắp nước", new QuanLyHoaDonBapNuocView());

        add(tabs, BorderLayout.CENTER);
    }
}