package posrap.dao;

import java.sql.*;
import posrap.dto.BaoCaoDTO;

public class BaoCaoDAO extends BaseDAO {

    public BaoCaoDTO getBaoCao(Date tu, Date den) throws Exception {
        BaoCaoDTO dto = new BaoCaoDTO();
        dto.setDoanhThuVe(getDoanhThuVe(tu, den));
        dto.setDoanhThuBapNuoc(getDoanhThuBapNuoc(tu, den));
        dto.setTopPhim(getTopPhim(tu, den));
        dto.setTopMon(getTopMon(tu, den));
        return dto;
    }

    public long getDoanhThuVe(Date tu, Date den) throws Exception {
        String sql =
            "SELECT ISNULL(SUM(tong_tien), 0) AS doanh_thu " +
            "FROM HoaDonVe " +
            "WHERE CAST(ngay_lap AS date) BETWEEN ? AND ?";

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDate(1, tu);
            ps.setDate(2, den);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong("doanh_thu");
            }
        }
    }

    public long getDoanhThuBapNuoc(Date tu, Date den) throws Exception {
        String sql =
            "SELECT ISNULL(SUM(tong_tien), 0) AS doanh_thu " +
            "FROM HoaDonBapNuoc " +
            "WHERE CAST(ngay_lap AS date) BETWEEN ? AND ?";

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDate(1, tu);
            ps.setDate(2, den);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong("doanh_thu");
            }
        }
    }

    public String getTopPhim(Date tu, Date den) throws Exception {
        String sql =
            "SELECT TOP 1 p.ten_phim, COUNT(*) AS so_ve " +
            "FROM ChiTietHoaDonVe ct " +
            "JOIN HoaDonVe hd ON hd.hoa_don_ve_id = ct.hoa_don_ve_id " +
            "JOIN Ve v ON v.ve_id = ct.ve_id " +
            "JOIN SuatChieu s ON s.suat_chieu_id = v.suat_chieu_id " +
            "JOIN Phim p ON p.phim_id = s.phim_id " +
            "WHERE CAST(hd.ngay_lap AS date) BETWEEN ? AND ? " +
            "GROUP BY p.ten_phim " +
            "ORDER BY so_ve DESC";

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDate(1, tu);
            ps.setDate(2, den);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("ten_phim");
                return "(khong co du lieu)";
            }
        }
    }

    public String getTopMon(Date tu, Date den) throws Exception {
        // Top món theo tổng số lượng bán trong khoảng ngày
        String sql =
            "SELECT TOP 1 m.ten_mon, SUM(ct.so_luong) AS so_luong " +
            "FROM ChiTietHoaDonBapNuoc ct " +
            "JOIN HoaDonBapNuoc hd ON hd.hoa_don_id = ct.hoa_don_id " +
            "JOIN BienTheMon bt ON bt.bien_the_id = ct.bien_the_id " +
            "JOIN MonBapNuoc m ON m.mon_id = bt.mon_id " +
            "WHERE CAST(hd.ngay_lap AS date) BETWEEN ? AND ? " +
            "GROUP BY m.ten_mon " +
            "ORDER BY so_luong DESC";

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDate(1, tu);
            ps.setDate(2, den);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("ten_mon");
                return "(khong co du lieu)";
            }
        }
    }
}
