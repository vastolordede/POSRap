package posrap.util;

public final class SessionContext {
    private SessionContext() {}

    private static long nhanVienId = -1;
    private static String hoTen = "";
    private static String vaiTro = ""; // "admin" | "staff"

    public static void setSession(long nhanVienIdMoi, String hoTenMoi, String vaiTroMoi) {
        nhanVienId = nhanVienIdMoi;
        hoTen = hoTenMoi;
        vaiTro = vaiTroMoi;
    }

    public static void clear() {
        nhanVienId = -1;
        hoTen = "";
        vaiTro = "";
    }

    public static int getNhanVienId() { return (int) nhanVienId; }
    public static String getHoTen() { return hoTen; }
    public static String getVaiTro() { return vaiTro; }
    public static boolean isAdmin() { return "admin".equalsIgnoreCase(vaiTro); }
    public static boolean isStaff() { return "staff".equalsIgnoreCase(vaiTro); }
}
