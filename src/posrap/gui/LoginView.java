package posrap.gui;

import javax.swing.*;
import java.awt.*;

import posrap.bus.AuthBUS;
import posrap.dao.TaiKhoanDAO;
import posrap.dto.TaiKhoanDTO;
import posrap.util.SessionContext;
import posrap.util.UiUtil;

public class LoginView extends BaseFrame {

    private final JTextField txtTenDangNhap = new JTextField(20);
    private final JPasswordField txtMatKhau = new JPasswordField(20);
    private final JButton btnDangNhap = new JButton("Dang nhap");

    public LoginView() {
        super("Dang nhap - POS rap chieu phim");
        // đảm bảo 2 tài khoản mặc định luôn tồn tại trong DB
        new TaiKhoanDAO().ensureDefaultAccounts();
        setContentPane(buildUi());
    }

    private JPanel buildUi() {
        JPanel root = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("POS rap chieu phim", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));

        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        root.add(title, g);

        g.gridwidth = 1;
        g.gridx = 0; g.gridy = 1;
        root.add(new JLabel("Ten dang nhap:"), g);

        g.gridx = 1; g.gridy = 1;
        root.add(txtTenDangNhap, g);

        g.gridx = 0; g.gridy = 2;
        root.add(new JLabel("Mat khau:"), g);

        g.gridx = 1; g.gridy = 2;
        root.add(txtMatKhau, g);

        g.gridx = 0; g.gridy = 3; g.gridwidth = 2;
        root.add(btnDangNhap, g);

        JLabel hint = new JLabel("Mock login: admin/admin hoac staff/staff", SwingConstants.CENTER);
        g.gridx = 0; g.gridy = 4; g.gridwidth = 2;
        root.add(hint, g);

        btnDangNhap.addActionListener(e -> onDangNhap());

        return root;
    }

    private void onDangNhap() {
        try {
            AuthBUS bus = new AuthBUS();
            TaiKhoanDTO tk = bus.dangNhap(
                    txtTenDangNhap.getText().trim(),
                    new String(txtMatKhau.getPassword()).trim()
            );

            // Vì bạn muốn login KHÔNG liên quan NhanVien:
            // setSession dùng 3 thông tin: taiKhoanId, vaiTro, tenDangNhap
            SessionContext.setSession(
                    tk.getTaiKhoanId(),
                    tk.getVaiTro(),
                    tk.getTenDangNhap()
            );

            openMain();
        } catch (Exception ex) {
            UiUtil.showInfo(this, ex.getMessage());
        }
    }

    private void openMain() {
        MainView main = new MainView();
        main.setVisible(true);
        dispose();
    }
}
