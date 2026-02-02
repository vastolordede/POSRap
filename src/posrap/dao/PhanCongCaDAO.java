package posrap.dao;

import java.sql.*;
import java.util.*;
import posrap.dto.PhanCongCaDTO;

public class PhanCongCaDAO extends BaseDAO {

    public List<PhanCongCaDTO> getByMonth(int month, int year) throws Exception {
        String sql =
            "SELECT nhan_vien_id, ca_lam_id, ngay_lam " +
            "FROM PhanCongCa " +
            "WHERE MONTH(ngay_lam)=? AND YEAR(ngay_lam)=? " +
            "ORDER BY ngay_lam, nhan_vien_id, ca_lam_id";

        List<PhanCongCaDTO> list = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, month);
            ps.setInt(2, year);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PhanCongCaDTO p = new PhanCongCaDTO();
                    p.setNhanVienId(rs.getInt("nhan_vien_id"));
                    p.setCaLamId(rs.getInt("ca_lam_id"));
                    p.setNgayLam(rs.getDate("ngay_lam").toString()); // DTO đang là String
                    list.add(p);
                }
            }
        }
        return list;
    }

    public void insert(PhanCongCaDTO p) throws Exception {
        String sql = "INSERT INTO PhanCongCa(nhan_vien_id, ca_lam_id, ngay_lam) VALUES(?,?,?)";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, p.getNhanVienId());
            ps.setInt(2, p.getCaLamId());
            ps.setDate(3, java.sql.Date.valueOf(p.getNgayLam())); // 'YYYY-MM-DD'
            ps.executeUpdate();
        }
    }

    public void delete(PhanCongCaDTO p) throws Exception {
        String sql = "DELETE FROM PhanCongCa WHERE nhan_vien_id=? AND ca_lam_id=? AND ngay_lam=?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, p.getNhanVienId());
            ps.setInt(2, p.getCaLamId());
            ps.setDate(3, java.sql.Date.valueOf(p.getNgayLam()));
            ps.executeUpdate();
        }
    }

    // tiện: xóa phân công theo tháng (đỡ làm lại từ đầu)
    public void deleteByMonth(int month, int year) throws Exception {
        String sql = "DELETE FROM PhanCongCa WHERE MONTH(ngay_lam)=? AND YEAR(ngay_lam)=?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, month);
            ps.setInt(2, year);
            ps.executeUpdate();
        }
    }
    public boolean exists(int nhanVienId, int caLamId, String ngayLamYYYYMMDD) throws Exception {
    String sql = "SELECT COUNT(*) FROM PhanCongCa WHERE nhan_vien_id=? AND ca_lam_id=? AND ngay_lam=?";
    try (Connection c = getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setInt(1, nhanVienId);
        ps.setInt(2, caLamId);
        ps.setDate(3, java.sql.Date.valueOf(ngayLamYYYYMMDD));
        try (ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1) > 0;
        }
    }
}

public void insertBatch(List<PhanCongCaDTO> list) throws Exception {
    String sql = "INSERT INTO PhanCongCa(nhan_vien_id, ca_lam_id, ngay_lam) VALUES(?,?,?)";
    try (Connection c = getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        c.setAutoCommit(false);
        try {
            for (PhanCongCaDTO p : list) {
                ps.setInt(1, p.getNhanVienId());
                ps.setInt(2, p.getCaLamId());
                ps.setDate(3, java.sql.Date.valueOf(p.getNgayLam()));
                ps.addBatch();
            }
            ps.executeBatch();
            c.commit();
        } catch (Exception ex) {
            c.rollback();
            throw ex;
        } finally {
            c.setAutoCommit(true);
        }
    }
}

// Update theo kiểu: xóa dòng cũ -> insert dòng mới
public void update(PhanCongCaDTO oldRow, PhanCongCaDTO newRow) throws Exception {
    try (Connection c = getConnection()) {
        c.setAutoCommit(false);
        try {
            String del = "DELETE FROM PhanCongCa WHERE nhan_vien_id=? AND ca_lam_id=? AND ngay_lam=?";
            try (PreparedStatement ps = c.prepareStatement(del)) {
                ps.setInt(1, oldRow.getNhanVienId());
                ps.setInt(2, oldRow.getCaLamId());
                ps.setDate(3, java.sql.Date.valueOf(oldRow.getNgayLam()));
                ps.executeUpdate();
            }
            String ins = "INSERT INTO PhanCongCa(nhan_vien_id, ca_lam_id, ngay_lam) VALUES(?,?,?)";
            try (PreparedStatement ps = c.prepareStatement(ins)) {
                ps.setInt(1, newRow.getNhanVienId());
                ps.setInt(2, newRow.getCaLamId());
                ps.setDate(3, java.sql.Date.valueOf(newRow.getNgayLam()));
                ps.executeUpdate();
            }
            c.commit();
        } catch (Exception ex) {
            c.rollback();
            throw ex;
        } finally {
            c.setAutoCommit(true);
        }
    }
}

}
