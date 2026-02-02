package posrap.gui;

import javax.swing.*;
import posrap.util.UiUtil;

public abstract class BaseFrame extends JFrame {

    protected BaseFrame(String title) {
        super(title);
        UiUtil.setSystemLookAndFeel();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Khong cho resize
        setResizable(false);

        // Full screen theo man hinh
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Tranh Windows nho trang thai cu
        setUndecorated(false);
    }
}
