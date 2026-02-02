package posrap.dao;

import java.sql.*;
import java.util.*;
import posrap.dto.*;

public class VeDAO extends BaseDAO {

    public List<GheDTO> getGheTheoSuatChieu(int suatChieuId) throws Exception {
        String getPhongSql = "SELECT phong_chieu_id FROM SuatChieu WHERE suat_chieu_id=?";
        int phongId;

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(getPhongSql)) {

            ps.setInt(1, suatChieuId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return new ArrayList<>();
                phongId = rs.getInt("phong_chieu_id");
            }

            String sql = "SELECT ghe_id, phong_chieu_id, hang_ghe, so_ghe, loai_ghe FROM Ghe WHERE phong_chieu_id=? ORDER BY hang_ghe, so_ghe";
            List<GheDTO> list = new ArrayList<>();
            try (PreparedStatement ps2 = c.prepareStatement(sql)) {
                ps2.setInt(1, phongId);
                try (ResultSet rs2 = ps2.executeQuery()) {
                    while (rs2.next()) {
                        GheDTO g = new GheDTO();
                        g.setGheId(rs2.getInt("ghe_id"));
                        g.setPhongChieuId(rs2.getInt("phong_chieu_id"));
                        g.setHangGhe(rs2.getString("hang_ghe"));
                        g.setSoGhe(rs2.getInt("so_ghe"));
                        g.setLoaiGhe(rs2.getString("loai_ghe"));
                        list.add(g);
                    }
                }
            }
            return list;
        }
    }

    public Set<Integer> getGheDaBan(int suatChieuId) throws Exception {
        String sql = "SELECT DISTINCT ghe_id FROM Ve WHERE suat_chieu_id=?";
        Set<Integer> set = new HashSet<>();

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, suatChieuId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) set.add(rs.getInt("ghe_id"));
            }
        }
        return set;
    }

    public HoaDonVeDTO taoHoaDonVe(int suatChieuId, List<GheDTO> dsGhe) throws Exception {
    // POS dùng chung: không phân biệt ai đứng máy
    final int POS_CHUNG_NHANVIEN_ID = 1;

    try (Connection c = getConnection()) {
        c.setAutoCommit(false);

        try {
            double giaSuat = layGiaSuatChieu(c, suatChieuId);
            double tong = giaSuat * dsGhe.size();

            int hoaDonId = insertHoaDonVe(c, POS_CHUNG_NHANVIEN_ID, tong);

            for (GheDTO ghe : dsGhe) {
                if (!isGheTrongTx(c, suatChieuId, ghe.getGheId())) {
                    throw new RuntimeException("Ghe da duoc ban: " + (ghe.getHangGhe() == null ? "" : ghe.getHangGhe()) + ghe.getSoGhe());
                }
                int veId = insertVe(c, suatChieuId, ghe.getGheId(), giaSuat);
                insertChiTietHoaDonVe(c, hoaDonId, veId);
            }

            c.commit();

            HoaDonVeDTO hd = new HoaDonVeDTO();
            hd.setHoaDonVeId(hoaDonId);
            hd.setNhanVienId(POS_CHUNG_NHANVIEN_ID);
            hd.setNgayLap(new java.util.Date());
            hd.setTongTien(tong);
            return hd;

        } catch (Exception ex) {
            c.rollback();
            throw ex;
        } finally {
            c.setAutoCommit(true);
        }
    }
}


    private double layGiaSuatChieu(Connection c, int suatChieuId) throws Exception {
        String sql = "SELECT gia FROM SuatChieu WHERE suat_chieu_id=?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, suatChieuId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new RuntimeException("Khong tim thay suat chieu");
                return rs.getDouble("gia");
            }
        }
    }

    private int insertHoaDonVe(Connection c, int nhanVienId, double tongTien) throws Exception {
        String sql = "INSERT INTO HoaDonVe(nhan_vien_id, ngay_lap, tong_tien) VALUES(?, GETDATE(), ?)";
        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, nhanVienId);
            ps.setDouble(2, tongTien);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    private int insertVe(Connection c, int suatChieuId, int gheId, double giaVe) throws Exception {
        String sql = "INSERT INTO Ve(suat_chieu_id, ghe_id, gia_ve, trang_thai) VALUES(?,?,?,?)";
        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, suatChieuId);
            ps.setInt(2, gheId);
            ps.setDouble(3, giaVe);
            ps.setString(4, "da_ban");
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    private void insertChiTietHoaDonVe(Connection c, int hoaDonVeId, int veId) throws Exception {
        String sql = "INSERT INTO ChiTietHoaDonVe(hoa_don_ve_id, ve_id) VALUES(?,?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, hoaDonVeId);
            ps.setInt(2, veId);
            ps.executeUpdate();
        }
    }

    private boolean isGheTrongTx(Connection c, int suatChieuId, int gheId) throws Exception {
        String sql = "SELECT COUNT(*) FROM Ve WHERE suat_chieu_id=? AND ghe_id=?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, suatChieuId);
            ps.setInt(2, gheId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) == 0;
            }
        }
    }
    public boolean isGheTrong(int suatChieuId, int gheId) throws Exception {
    try (Connection c = getConnection()) {
        return isGheTrongTx(c, suatChieuId, gheId);
    }
}



}
