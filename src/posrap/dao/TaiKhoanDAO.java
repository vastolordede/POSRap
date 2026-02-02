package posrap.dao;

import java.sql.*;
import posrap.dto.TaiKhoanDTO;

public class TaiKhoanDAO extends BaseDAO {

    public void ensureDefaultAccounts() {
        String checkSql = "SELECT COUNT(*) FROM TaiKhoan WHERE ten_dang_nhap = ?";
        String insertSql = "INSERT INTO TaiKhoan(ten_dang_nhap, mat_khau, vai_tro, trang_thai) VALUES(?,?,?,?)";

        try (Connection c = getConnection()) {
            ensureOne(c, checkSql, insertSql, "admin", "admin", "admin", "dang_hoat_dong");
            ensureOne(c, checkSql, insertSql, "staff", "staff", "staff", "dang_hoat_dong");
        } catch (Exception ignored) {
            // Nếu DB chưa tạo bảng TaiKhoan hoặc lỗi kết nối, bỏ qua để app không crash.
        }
    }

    private void ensureOne(Connection c, String checkSql, String insertSql,
                           String user, String pass, String role, String status) throws Exception {
        try (PreparedStatement ps = c.prepareStatement(checkSql)) {
            ps.setString(1, user);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                int cnt = rs.getInt(1);
                if (cnt > 0) return;
            }
        }
        try (PreparedStatement ins = c.prepareStatement(insertSql)) {
            ins.setString(1, user);
            ins.setString(2, pass);
            ins.setString(3, role);
            ins.setString(4, status);
            ins.executeUpdate();
        }
    }

    public TaiKhoanDTO dangNhap(String user, String pass) throws Exception {
        String sql = "SELECT tai_khoan_id, ten_dang_nhap, vai_tro, trang_thai FROM TaiKhoan WHERE ten_dang_nhap=? AND mat_khau=?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, user);
            ps.setString(2, pass);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TaiKhoanDTO tk = new TaiKhoanDTO();
                    tk.setTaiKhoanId(rs.getInt("tai_khoan_id"));
                    tk.setTenDangNhap(rs.getString("ten_dang_nhap"));
                    tk.setVaiTro(rs.getString("vai_tro"));
                    tk.setTrangThai(rs.getString("trang_thai"));
                    return tk;
                }
            }
        }
        return null;
    }
}
