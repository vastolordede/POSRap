package posrap.dao;

import java.sql.*;
import java.util.*;
import posrap.dto.*;

public class RapChieuDAO extends BaseDAO {

    // ---------- PHIM ----------
    public List<PhimDTO> getAllPhim() throws Exception {
        String sql = "SELECT phim_id, ten_phim, the_loai, thoi_luong, do_tuoi, trang_thai FROM Phim";
        List<PhimDTO> list = new ArrayList<>();

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PhimDTO p = new PhimDTO();
                p.setPhimId(rs.getInt("phim_id"));
                p.setTenPhim(rs.getString("ten_phim"));
                p.setTheLoai(rs.getString("the_loai"));
                p.setThoiLuong(rs.getInt("thoi_luong"));
                p.setDoTuoi(rs.getString("do_tuoi"));
                p.setTrangThai(rs.getString("trang_thai"));
                list.add(p);
            }
        }
        return list;
    }

    public void insertPhim(PhimDTO p) throws Exception {
        String sql = "INSERT INTO Phim(ten_phim, the_loai, thoi_luong, do_tuoi, trang_thai) VALUES(?,?,?,?,?)";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getTenPhim());
            ps.setString(2, p.getTheLoai());
            ps.setInt(3, p.getThoiLuong());
            ps.setString(4, p.getDoTuoi());
            ps.setString(5, p.getTrangThai());
            ps.executeUpdate();
        }
    }

    public void updatePhim(PhimDTO p) throws Exception {
        String sql = "UPDATE Phim SET ten_phim=?, the_loai=?, thoi_luong=?, do_tuoi=?, trang_thai=? WHERE phim_id=?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getTenPhim());
            ps.setString(2, p.getTheLoai());
            ps.setInt(3, p.getThoiLuong());
            ps.setString(4, p.getDoTuoi());
            ps.setString(5, p.getTrangThai());
            ps.setInt(6, p.getPhimId());
            ps.executeUpdate();
        }
    }

    public void deletePhim(int id) throws Exception {
        String sql = "DELETE FROM Phim WHERE phim_id=?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // ---------- PHONG ----------
    public List<PhongChieuDTO> getAllPhong() throws Exception {
        String sql = "SELECT phong_chieu_id, ten_phong, suc_chua, trang_thai FROM PhongChieu";
        List<PhongChieuDTO> list = new ArrayList<>();

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PhongChieuDTO pc = new PhongChieuDTO();
                pc.setPhongChieuId(rs.getInt("phong_chieu_id"));
                pc.setTenPhong(rs.getString("ten_phong"));
                pc.setSucChua(rs.getInt("suc_chua"));
                pc.setTrangThai(rs.getString("trang_thai"));
                list.add(pc);
            }
        }
        return list;
    }

    // ---------- GHE ----------
    public List<GheDTO> getAllGhe() throws Exception {
        String sql = "SELECT ghe_id, phong_chieu_id, hang_ghe, so_ghe, loai_ghe FROM Ghe ORDER BY phong_chieu_id, hang_ghe, so_ghe";
        List<GheDTO> list = new ArrayList<>();

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                GheDTO g = new GheDTO();
                g.setGheId(rs.getInt("ghe_id"));
                g.setPhongChieuId(rs.getInt("phong_chieu_id"));
                g.setHangGhe(rs.getString("hang_ghe"));
                g.setSoGhe(rs.getInt("so_ghe"));
                g.setLoaiGhe(rs.getString("loai_ghe"));
                list.add(g);
            }
        }
        return list;
    }

    // ---------- SUAT CHIEU ----------
    public List<SuatChieuDTO> getAllSuatChieu() throws Exception {
        String sql = "SELECT suat_chieu_id, phim_id, phong_chieu_id, thoi_gian_bat_dau, thoi_gian_ket_thuc, gia FROM SuatChieu ORDER BY thoi_gian_bat_dau";
        List<SuatChieuDTO> list = new ArrayList<>();

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SuatChieuDTO s = new SuatChieuDTO();
                s.setSuatChieuId(rs.getInt("suat_chieu_id"));
                s.setPhimId(rs.getInt("phim_id"));
                s.setPhongChieuId(rs.getInt("phong_chieu_id"));
                s.setBatDau(rs.getTimestamp("thoi_gian_bat_dau"));
                s.setKetThuc(rs.getTimestamp("thoi_gian_ket_thuc"));
                s.setGia(rs.getDouble("gia"));
                list.add(s);
            }
        }
        return list;
    }

    // Phục vụ BanVeView
    public List<PhimDTO> getPhimDangHoatDong() throws Exception {
        String sql = "SELECT phim_id, ten_phim, the_loai, thoi_luong, do_tuoi, trang_thai FROM Phim WHERE trang_thai='dang_hoat_dong'";
        List<PhimDTO> list = new ArrayList<>();

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PhimDTO p = new PhimDTO();
                p.setPhimId(rs.getInt("phim_id"));
                p.setTenPhim(rs.getString("ten_phim"));
                p.setTheLoai(rs.getString("the_loai"));
                p.setThoiLuong(rs.getInt("thoi_luong"));
                p.setDoTuoi(rs.getString("do_tuoi"));
                p.setTrangThai(rs.getString("trang_thai"));
                list.add(p);
            }
        }
        return list;
    }

    public List<SuatChieuDTO> getSuatChieuByPhim(int phimId) throws Exception {
        String sql = "SELECT suat_chieu_id, phim_id, phong_chieu_id, thoi_gian_bat_dau, thoi_gian_ket_thuc, gia FROM SuatChieu WHERE phim_id=? ORDER BY thoi_gian_bat_dau";
        List<SuatChieuDTO> list = new ArrayList<>();

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, phimId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SuatChieuDTO s = new SuatChieuDTO();
                    s.setSuatChieuId(rs.getInt("suat_chieu_id"));
                    s.setPhimId(rs.getInt("phim_id"));
                    s.setPhongChieuId(rs.getInt("phong_chieu_id"));
                    s.setBatDau(rs.getTimestamp("thoi_gian_bat_dau"));
                    s.setKetThuc(rs.getTimestamp("thoi_gian_ket_thuc"));
                    s.setGia(rs.getDouble("gia"));
                    list.add(s);
                }
            }
        }
        return list;
    }

    public PhongChieuDTO getPhongById(int phongId) throws Exception {
        String sql = "SELECT phong_chieu_id, ten_phong, suc_chua, trang_thai FROM PhongChieu WHERE phong_chieu_id=?";

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, phongId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                PhongChieuDTO pc = new PhongChieuDTO();
                pc.setPhongChieuId(rs.getInt("phong_chieu_id"));
                pc.setTenPhong(rs.getString("ten_phong"));
                pc.setSucChua(rs.getInt("suc_chua"));
                pc.setTrangThai(rs.getString("trang_thai"));
                return pc;
            }
        }
    }

    public List<GheDTO> getGheByPhong(int phongId) throws Exception {
        String sql = "SELECT ghe_id, phong_chieu_id, hang_ghe, so_ghe, loai_ghe FROM Ghe WHERE phong_chieu_id=? ORDER BY hang_ghe, so_ghe";
        List<GheDTO> list = new ArrayList<>();

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, phongId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    GheDTO g = new GheDTO();
                    g.setGheId(rs.getInt("ghe_id"));
                    g.setPhongChieuId(rs.getInt("phong_chieu_id"));
                    g.setHangGhe(rs.getString("hang_ghe"));
                    g.setSoGhe(rs.getInt("so_ghe"));
                    g.setLoaiGhe(rs.getString("loai_ghe"));
                    list.add(g);
                }
            }
        }
        return list;
    }
    public void insertPhong(PhongChieuDTO p) throws Exception {
    String sql = "INSERT INTO PhongChieu(ten_phong, suc_chua, trang_thai) VALUES(?,?,?)";
    try (Connection c = getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setString(1, p.getTenPhong());
        ps.setInt(2, p.getSucChua());
        ps.setString(3, p.getTrangThai());
        ps.executeUpdate();
    }
}

public void updatePhong(PhongChieuDTO p) throws Exception {
    String sql = "UPDATE PhongChieu SET ten_phong=?, suc_chua=?, trang_thai=? WHERE phong_chieu_id=?";
    try (Connection c = getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setString(1, p.getTenPhong());
        ps.setInt(2, p.getSucChua());
        ps.setString(3, p.getTrangThai());
        ps.setInt(4, p.getPhongChieuId());
        ps.executeUpdate();
    }
}

public void deletePhong(int phongId) throws Exception {
    // nếu có FK: Ghe(phong_chieu_id), SuatChieu(phong_chieu_id) -> xóa con trước
    String delGhe = "DELETE FROM Ghe WHERE phong_chieu_id=?";
    String delSuat = "DELETE FROM SuatChieu WHERE phong_chieu_id=?";
    String delPhong = "DELETE FROM PhongChieu WHERE phong_chieu_id=?";

    try (Connection c = getConnection()) {
        c.setAutoCommit(false);
        try (PreparedStatement ps1 = c.prepareStatement(delGhe);
             PreparedStatement ps2 = c.prepareStatement(delSuat);
             PreparedStatement ps3 = c.prepareStatement(delPhong)) {

            ps1.setInt(1, phongId); ps1.executeUpdate();
            ps2.setInt(1, phongId); ps2.executeUpdate();
            ps3.setInt(1, phongId); ps3.executeUpdate();

            c.commit();
        } catch (Exception ex) {
            c.rollback();
            throw ex;
        } finally {
            c.setAutoCommit(true);
        }
    }
}
public int insertGhe(GheDTO g) throws Exception {
    String sql = "INSERT INTO Ghe(phong_chieu_id, hang_ghe, so_ghe, loai_ghe) VALUES(?,?,?,?)";
    try (Connection c = getConnection();
         PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        ps.setInt(1, g.getPhongChieuId());
        ps.setString(2, g.getHangGhe());
        ps.setInt(3, g.getSoGhe());
        ps.setString(4, g.getLoaiGhe());
        ps.executeUpdate();
        try (ResultSet keys = ps.getGeneratedKeys()) {
            keys.next();
            return keys.getInt(1);
        }
    }
}

public void updateGhe(GheDTO g) throws Exception {
    String sql = "UPDATE Ghe SET phong_chieu_id=?, hang_ghe=?, so_ghe=?, loai_ghe=? WHERE ghe_id=?";
    try (Connection c = getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setInt(1, g.getPhongChieuId());
        ps.setString(2, g.getHangGhe());
        ps.setInt(3, g.getSoGhe());
        ps.setString(4, g.getLoaiGhe());
        ps.setInt(5, g.getGheId());
        ps.executeUpdate();
    }
}

public void deleteGhe(int gheId) throws Exception {
    // nếu có FK: Ve(ghe_id) -> phải xóa vé/chi tiết trước hoặc chặn xóa
    String sql = "DELETE FROM Ghe WHERE ghe_id=?";
    try (Connection c = getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setInt(1, gheId);
        ps.executeUpdate();
    }
}
public int insertSuatChieu(SuatChieuDTO s) throws Exception {
    String sql =
        "INSERT INTO SuatChieu(phim_id, phong_chieu_id, thoi_gian_bat_dau, thoi_gian_ket_thuc, gia) " +
        "VALUES(?,?,?,?,?)";
    try (Connection c = getConnection();
         PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        ps.setInt(1, s.getPhimId());
        ps.setInt(2, s.getPhongChieuId());
        ps.setTimestamp(3, s.getBatDau());
        ps.setTimestamp(4, s.getKetThuc());
        ps.setDouble(5, s.getGia());
        ps.executeUpdate();
        try (ResultSet keys = ps.getGeneratedKeys()) {
            keys.next();
            return keys.getInt(1);
        }
    }
}

public void updateSuatChieu(SuatChieuDTO s) throws Exception {
    String sql =
        "UPDATE SuatChieu SET phim_id=?, phong_chieu_id=?, thoi_gian_bat_dau=?, thoi_gian_ket_thuc=?, gia=? " +
        "WHERE suat_chieu_id=?";
    try (Connection c = getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setInt(1, s.getPhimId());
        ps.setInt(2, s.getPhongChieuId());
        ps.setTimestamp(3, s.getBatDau());
        ps.setTimestamp(4, s.getKetThuc());
        ps.setDouble(5, s.getGia());
        ps.setInt(6, s.getSuatChieuId());
        ps.executeUpdate();
    }
}

public void deleteSuatChieu(int suatChieuId) throws Exception {
    // FK: Ve(suat_chieu_id) -> phải xóa vé/chi tiết trước hoặc chặn xóa
    String sql = "DELETE FROM SuatChieu WHERE suat_chieu_id=?";
    try (Connection c = getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setInt(1, suatChieuId);
        ps.executeUpdate();
    }
}

}
