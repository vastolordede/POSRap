    package posrap.dao;

    import java.sql.*;
    import java.util.*;
    import posrap.dto.*;

    public class BapNuocDAO extends BaseDAO {

        /* ================== TRA CỨU ================== */

        public List<MonBapNuocDTO> getAllMon() throws Exception {
            List<MonBapNuocDTO> list = new ArrayList<>();
            String sql = "SELECT mon_id, ten_mon, ton_kho FROM MonBapNuoc ORDER BY mon_id";

            try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    MonBapNuocDTO m = new MonBapNuocDTO();
                    m.setMonId(rs.getInt("mon_id"));
                    m.setTenMon(rs.getString("ten_mon"));
                    m.setTonKho(rs.getInt("ton_kho"));
                    list.add(m);
                }
            }
            return list;
        }

        public List<SizeDTO> getAllSize() throws Exception {
            List<SizeDTO> list = new ArrayList<>();
            String sql = "SELECT size_id, ten_size FROM SizeMon ORDER BY size_id";

            try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    SizeDTO s = new SizeDTO();
                    s.setSizeId(rs.getInt("size_id"));
                    s.setTenSize(rs.getString("ten_size"));
                    list.add(s);
                }
            }
            return list;
        }

        public BienTheMonDTO getBienThe(int monId, int sizeId) throws Exception {
            String sql =
                    "SELECT bien_the_id, mon_id, size_id, gia " +
                    "FROM BienTheMon " +
                    "WHERE mon_id = ? AND size_id = ?";

            try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, monId);
                ps.setInt(2, sizeId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) return null;

                    BienTheMonDTO bt = new BienTheMonDTO();
                    bt.setBienTheId(rs.getInt("bien_the_id"));
                    bt.setMonId(rs.getInt("mon_id"));
                    bt.setSizeId(rs.getInt("size_id"));
                    bt.setGia(rs.getDouble("gia"));
                    return bt;
                }
            }
        }

        /**
         * Dùng cho QuanTriBapNuocView TAB "Biến thể" để hiển thị:
         * [bien_the_id, ten_mon, ten_size, gia]
         */
        public List<Object[]> getAllBienTheView() throws Exception {
            List<Object[]> list = new ArrayList<>();
            String sql =
                "SELECT bt.bien_the_id, m.ten_mon, s.ten_size, bt.gia " +
                "FROM BienTheMon bt " +
                "JOIN MonBapNuoc m ON m.mon_id = bt.mon_id " +
                "JOIN SizeMon s ON s.size_id = bt.size_id " +
                "ORDER BY bt.bien_the_id";

            try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    list.add(new Object[]{
                        rs.getInt("bien_the_id"),
                        rs.getString("ten_mon"),
                        rs.getString("ten_size"),
                        rs.getDouble("gia")
                    });
                }
            }
            return list;
        }

        /* ================== CRUD MON ================== */

        public int insertMon(MonBapNuocDTO m) throws Exception {
            String sql = "INSERT INTO MonBapNuoc(ten_mon, ton_kho) VALUES(?, ?)";
            try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, m.getTenMon());
                ps.setInt(2, m.getTonKho());
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    keys.next();
                    return keys.getInt(1);
                }
            }
        }

        public void updateMon(MonBapNuocDTO m) throws Exception {
            String sql = "UPDATE MonBapNuoc SET ten_mon=?, ton_kho=? WHERE mon_id=?";
            try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, m.getTenMon());
                ps.setInt(2, m.getTonKho());
                ps.setInt(3, m.getMonId());
                ps.executeUpdate();
            }
        }

        public void deleteMon(int monId) throws Exception {
            // nếu DB có FK BienTheMon(mon_id) -> MonBapNuoc(mon_id) thì phải xóa biến thể trước
            String delBienThe = "DELETE FROM BienTheMon WHERE mon_id=?";
            String delMon     = "DELETE FROM MonBapNuoc WHERE mon_id=?";

            try (Connection con = getConnection()) {
                con.setAutoCommit(false);
                try (PreparedStatement ps1 = con.prepareStatement(delBienThe);
                    PreparedStatement ps2 = con.prepareStatement(delMon)) {

                    ps1.setInt(1, monId);
                    ps1.executeUpdate();

                    ps2.setInt(1, monId);
                    ps2.executeUpdate();

                    con.commit();
                } catch (Exception ex) {
                    con.rollback();
                    throw ex;
                } finally {
                    con.setAutoCommit(true);
                }
            }
        }

        /* ================== CRUD SIZE ================== */

        public int insertSize(SizeDTO s) throws Exception {
            String sql = "INSERT INTO SizeMon(ten_size) VALUES(?)";
            try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, s.getTenSize());
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    keys.next();
                    return keys.getInt(1);
                }
            }
        }

        public void updateSize(SizeDTO s) throws Exception {
            String sql = "UPDATE SizeMon SET ten_size=? WHERE size_id=?";
            try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, s.getTenSize());
                ps.setInt(2, s.getSizeId());
                ps.executeUpdate();
            }
        }

        public void deleteSize(int sizeId) throws Exception {
            String delBienThe = "DELETE FROM BienTheMon WHERE size_id=?";
            String delSize    = "DELETE FROM SizeMon WHERE size_id=?";

            try (Connection con = getConnection()) {
                con.setAutoCommit(false);
                try (PreparedStatement ps1 = con.prepareStatement(delBienThe);
                    PreparedStatement ps2 = con.prepareStatement(delSize)) {

                    ps1.setInt(1, sizeId);
                    ps1.executeUpdate();

                    ps2.setInt(1, sizeId);
                    ps2.executeUpdate();

                    con.commit();
                } catch (Exception ex) {
                    con.rollback();
                    throw ex;
                } finally {
                    con.setAutoCommit(true);
                }
            }
        }

        /* ================== CRUD BIẾN THỂ ================== */

        public int insertBienThe(int monId, int sizeId, double gia) throws Exception {
            String sql = "INSERT INTO BienTheMon(mon_id, size_id, gia) VALUES(?,?,?)";
            try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, monId);
                ps.setInt(2, sizeId);
                ps.setDouble(3, gia);
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    keys.next();
                    return keys.getInt(1);
                }
            }
        }

        public void updateBienThe(int bienTheId, double gia) throws Exception {
            String sql = "UPDATE BienTheMon SET gia=? WHERE bien_the_id=?";
            try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setDouble(1, gia);
                ps.setInt(2, bienTheId);
                ps.executeUpdate();
            }
        }

        public void deleteBienThe(int bienTheId) throws Exception {
            String sql = "DELETE FROM BienTheMon WHERE bien_the_id=?";
            try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, bienTheId);
                ps.executeUpdate();
            }
        }

        /* ================== HÓA ĐƠN (GIỮ NGUYÊN LOGIC BẠN ĐÃ SỬA) ================== */

        // --- thay nguyên hàm taoHoaDon bằng bản này ---
public HoaDonBapNuocDTO taoHoaDon(List<ChiTietHoaDonBapNuocDTO> ct) throws Exception {
    if (ct == null || ct.isEmpty()) {
        throw new Exception("Gio hang dang trong.");
    }

    // POS dùng chung: không phân biệt ai đứng máy
    final int POS_CHUNG_NHANVIEN_ID = 1;

    Connection con = getConnection();
    con.setAutoCommit(false);

    try {
        long tong = tinhTongTien(con, ct);

        String sqlHD =
                "INSERT INTO HoaDonBapNuoc(nhan_vien_id, ngay_lap, tong_tien)" +
                "VALUES (?, NOW(), ?) " +
                "RETURNING hoa_don_id";

        int hoaDonId;
        try (PreparedStatement ps = con.prepareStatement(sqlHD)) {
            ps.setInt(1, POS_CHUNG_NHANVIEN_ID);
            ps.setLong(2, tong);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                hoaDonId = rs.getInt(1);
            }
        }

        for (ChiTietHoaDonBapNuocDTO c : ct) {
            checkTonKho(con, c.getBienTheId(), c.getSoLuong());
            insertChiTiet(con, hoaDonId, c);
            truTonKho(con, c.getBienTheId(), c.getSoLuong());
        }

        con.commit();

        HoaDonBapNuocDTO hd = new HoaDonBapNuocDTO();
        hd.setHoaDonId(hoaDonId);
        hd.setNhanVienId(POS_CHUNG_NHANVIEN_ID);
        hd.setTongTien(tong);
        return hd;

    } catch (Exception e) {
        con.rollback();
        throw e;
    } finally {
        try { con.setAutoCommit(true); } catch (Exception ignore) {}
        try { con.close(); } catch (Exception ignore) {}
    }
}


        private long tinhTongTien(Connection con, List<ChiTietHoaDonBapNuocDTO> ct) throws Exception {
            Map<Integer, Integer> slByBienThe = new HashMap<>();
            for (ChiTietHoaDonBapNuocDTO c : ct) {
                slByBienThe.merge(c.getBienTheId(), c.getSoLuong(), Integer::sum);
            }

            long tong = 0;
            String sqlGia = "SELECT gia FROM BienTheMon WHERE bien_the_id = ?";

            try (PreparedStatement ps = con.prepareStatement(sqlGia)) {
                for (Map.Entry<Integer, Integer> e : slByBienThe.entrySet()) {
                    ps.setInt(1, e.getKey());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new Exception("Khong tim thay bien the: " + e.getKey());
                        long gia = (long) rs.getDouble("gia");
                        tong += gia * e.getValue();
                    }
                }
            }
            return tong;
        }

        private void insertChiTiet(Connection con, int hdId, ChiTietHoaDonBapNuocDTO c) throws Exception {
            String sql = "INSERT INTO ChiTietHoaDonBapNuoc(hoa_don_id, bien_the_id, so_luong) VALUES (?, ?, ?)";

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, hdId);
                ps.setInt(2, c.getBienTheId());
                ps.setInt(3, c.getSoLuong());
                ps.executeUpdate();
            }
        }

        private void checkTonKho(Connection con, int bienTheId, int soLuong) throws Exception {
            String sql =
                    "SELECT m.ton_kho " +
                    "FROM BienTheMon bt " +
                    "JOIN MonBapNuoc m ON m.mon_id = bt.mon_id " +
                    "WHERE bt.bien_the_id = ?";

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, bienTheId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) throw new Exception("Khong tim thay mon theo bien the: " + bienTheId);

                    int ton = rs.getInt("ton_kho");
                    if (ton < soLuong) {
                        throw new Exception("Khong du ton kho. Ton: " + ton + ", can: " + soLuong);
                    }
                }
            }
        }

        private void truTonKho(Connection con, int bienTheId, int soLuong) throws Exception {
            String sql =
                    "UPDATE MonBapNuoc " +
                    "SET ton_kho = ton_kho - ? " +
                    "WHERE mon_id = (SELECT mon_id FROM BienTheMon WHERE bien_the_id = ?)";

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, soLuong);
                ps.setInt(2, bienTheId);
                int affected = ps.executeUpdate();
                if (affected <= 0) throw new Exception("Tru ton kho that bai (bien the: " + bienTheId + ")");
            }
        }
        // Thêm vào BapNuocDAO

public MonBapNuocDTO getMonById(int monId) throws Exception {
    String sql = "SELECT mon_id, ten_mon, ton_kho FROM MonBapNuoc WHERE mon_id=?";
    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, monId);
        try (ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) return null;
            MonBapNuocDTO m = new MonBapNuocDTO();
            m.setMonId(rs.getInt("mon_id"));
            m.setTenMon(rs.getString("ten_mon"));
            m.setTonKho(rs.getInt("ton_kho"));
            return m;
        }
    }
}

public SizeDTO getSizeById(int sizeId) throws Exception {
    String sql = "SELECT size_id, ten_size FROM SizeMon WHERE size_id=?";
    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, sizeId);
        try (ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) return null;
            SizeDTO s = new SizeDTO();
            s.setSizeId(rs.getInt("size_id"));
            s.setTenSize(rs.getString("ten_size"));
            return s;
        }
    }
}

// CRUD hóa đơn bắp nước (nếu bạn cần quản trị)
public List<HoaDonBapNuocDTO> getAllHoaDon() throws Exception {
    String sql = "SELECT hoa_don_id, nhan_vien_id, ngay_lap, tong_tien FROM HoaDonBapNuoc ORDER BY ngay_lap DESC";
    List<HoaDonBapNuocDTO> list = new ArrayList<>();
    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            HoaDonBapNuocDTO hd = new HoaDonBapNuocDTO();
            hd.setHoaDonId(rs.getInt("hoa_don_id"));
            hd.setNhanVienId(rs.getInt("nhan_vien_id"));
            hd.setNgayLap(rs.getTimestamp("ngay_lap"));
            hd.setTongTien(rs.getLong("tong_tien"));
            list.add(hd);
        }
    }
    return list;
}

public void deleteHoaDon(int hoaDonId) throws Exception {
    String delCt = "DELETE FROM ChiTietHoaDonBapNuoc WHERE hoa_don_id=?";
    String delHd = "DELETE FROM HoaDonBapNuoc WHERE hoa_don_id=?";
    try (Connection con = getConnection()) {
        con.setAutoCommit(false);
        try (PreparedStatement ps1 = con.prepareStatement(delCt);
             PreparedStatement ps2 = con.prepareStatement(delHd)) {
            ps1.setInt(1, hoaDonId);
            ps1.executeUpdate();

            ps2.setInt(1, hoaDonId);
            ps2.executeUpdate();

            con.commit();
        } catch (Exception ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }
}

    }
