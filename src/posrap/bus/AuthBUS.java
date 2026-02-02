package posrap.bus;

import posrap.dao.TaiKhoanDAO;
import posrap.dto.TaiKhoanDTO;

public class AuthBUS {

    private final TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();

    public TaiKhoanDTO dangNhap(String user, String pass) throws Exception {
        // đảm bảo luôn có 2 account admin/staff trong DB
        taiKhoanDAO.ensureDefaultAccounts();

        TaiKhoanDTO tk = taiKhoanDAO.dangNhap(user, pass);
        if (tk == null) throw new RuntimeException("Sai thong tin dang nhap");
        if (!"dang_hoat_dong".equals(tk.getTrangThai()))
            throw new RuntimeException("Tai khoan bi khoa");
        return tk;
    }
}
