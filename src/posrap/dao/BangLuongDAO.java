package posrap.dao;

import java.sql.*;
import java.util.*;
import posrap.dto.BangLuongDTO;

public class BangLuongDAO extends BaseDAO {

    public int insert(BangLuongDTO b) throws Exception {
        String sql = "INSERT INTO BangLuong(nhan_vien_id, thang, tong_luong) VALUES(?,?,?)";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, b.getNhanVienId());
            ps.setString(2, b.getThang());
            ps.setDouble(3, b.getTongLuong());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    public void update(BangLuongDTO b) throws Exception {
        String sql = "UPDATE BangLuong SET nhan_vien_id=?, thang=?, tong_luong=? WHERE bang_luong_id=?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, b.getNhanVienId());
            ps.setString(2, b.getThang());
            ps.setDouble(3, b.getTongLuong());
            ps.setInt(4, b.getBangLuongId());
            ps.executeUpdate();
        }
    }

    public void delete(int bangLuongId) throws Exception {
        String sql = "DELETE FROM BangLuong WHERE bang_luong_id=?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, bangLuongId);
            ps.executeUpdate();
        }
    }

    public BangLuongDTO getById(int bangLuongId) throws Exception {
        String sql = "SELECT bang_luong_id, nhan_vien_id, thang, tong_luong FROM BangLuong WHERE bang_luong_id=?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, bangLuongId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                BangLuongDTO b = new BangLuongDTO();
                b.setBangLuongId(rs.getInt("bang_luong_id"));
                b.setNhanVienId(rs.getInt("nhan_vien_id"));
                b.setThang(rs.getString("thang"));
                b.setTongLuong(rs.getDouble("tong_luong"));
                return b;
            }
        }
    }

    public List<BangLuongDTO> getAll() throws Exception {
        String sql = "SELECT bang_luong_id, nhan_vien_id, thang, tong_luong FROM BangLuong ORDER BY thang DESC, nhan_vien_id";
        List<BangLuongDTO> list = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                BangLuongDTO b = new BangLuongDTO();
                b.setBangLuongId(rs.getInt("bang_luong_id"));
                b.setNhanVienId(rs.getInt("nhan_vien_id"));
                b.setThang(rs.getString("thang"));
                b.setTongLuong(rs.getDouble("tong_luong"));
                list.add(b);
            }
        }
        return list;
    }

    // Giữ lại upsert bạn đang có
    public void upsertBangLuong(int nhanVienId, String thangYYYYMM, double tongLuong) throws Exception {
        String sql =
            "IF EXISTS (SELECT 1 FROM BangLuong WHERE nhan_vien_id=? AND thang=?) " +
            "    UPDATE BangLuong SET tong_luong=? WHERE nhan_vien_id=? AND thang=? " +
            "ELSE " +
            "    INSERT INTO BangLuong(nhan_vien_id, thang, tong_luong) VALUES(?,?,?)";

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, nhanVienId);
            ps.setString(2, thangYYYYMM);

            ps.setDouble(3, tongLuong);
            ps.setInt(4, nhanVienId);
            ps.setString(5, thangYYYYMM);

            ps.setInt(6, nhanVienId);
            ps.setString(7, thangYYYYMM);
            ps.setDouble(8, tongLuong);

            ps.executeUpdate();
        }
    }

    public List<BangLuongDTO> getByThang(String thangYYYYMM) throws Exception {
        String sql = "SELECT bang_luong_id, nhan_vien_id, thang, tong_luong FROM BangLuong WHERE thang=? ORDER BY nhan_vien_id";
        List<BangLuongDTO> list = new ArrayList<>();

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, thangYYYYMM);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BangLuongDTO b = new BangLuongDTO();
                    b.setBangLuongId(rs.getInt("bang_luong_id"));
                    b.setNhanVienId(rs.getInt("nhan_vien_id"));
                    b.setThang(rs.getString("thang"));
                    b.setTongLuong(rs.getDouble("tong_luong"));
                    list.add(b);
                }
            }
        }
        return list;
    }
}
