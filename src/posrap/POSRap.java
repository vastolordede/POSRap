package posrap;

import javax.swing.SwingUtilities;
import posrap.gui.LoginView;

public class POSRap {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}
