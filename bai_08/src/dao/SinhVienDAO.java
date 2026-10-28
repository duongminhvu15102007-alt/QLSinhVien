
package dao;

import database.DBConnection;
import model.SinhVien;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SinhVienDAO {

    // Lay toan bo danh sach sinh vien
    public List<SinhVien> getAll() throws SQLException {
        List<SinhVien> danhSach = new ArrayList<>();

        String sql = "SELECT * FROM sinh_vien ORDER BY ma_sv";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                danhSach.add(docSinhVien(rs));
            }
        }

        return danhSach;
    }

    // Tim sinh vien theo ma hoac ho ten
    public List<SinhVien> timKiem(String tuKhoa) throws SQLException {
        List<SinhVien> danhSach = new ArrayList<>();

        String sql = """
            SELECT * FROM sinh_vien
            WHERE ma_sv LIKE ? OR ho_ten LIKE ?
            ORDER BY ma_sv
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String key = "%" + tuKhoa + "%";
            ps.setString(1, key);
            ps.setString(2, key);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    danhSach.add(docSinhVien(rs));
                }
            }
        }

        return danhSach;
    }

    // Them sinh vien
    public boolean them(SinhVien sv) throws SQLException {
        String sql = """
            INSERT INTO sinh_vien
            (ma_sv, ho_ten, ngay_sinh, gioi_tinh,
             email, so_dien_thoai, dia_chi, lop)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ganDuLieu(ps, sv);
            return ps.executeUpdate() > 0;
        }
    }

    // Cap nhat thong tin sinh vien theo ma sinh vien
    public boolean sua(SinhVien sv) throws SQLException {
        String sql = """
            UPDATE sinh_vien
            SET ho_ten = ?, ngay_sinh = ?, gioi_tinh = ?,
                email = ?, so_dien_thoai = ?, dia_chi = ?, lop = ?
            WHERE ma_sv = ?
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sv.getHoTen());
            setNgaySinh(ps, 2, sv.getNgaySinh());
            setNullableString(ps, 3, sv.getGioiTinh());
            setNullableString(ps, 4, sv.getEmail());
            setNullableString(ps, 5, sv.getSoDienThoai());
            setNullableString(ps, 6, sv.getDiaChi());
            setNullableString(ps, 7, sv.getLop());
            ps.setString(8, sv.getMaSV());

            return ps.executeUpdate() > 0;
        }
    }

    // Xoa sinh vien
    public boolean xoa(String maSV) throws SQLException {
        String sql = "DELETE FROM sinh_vien WHERE ma_sv = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maSV);
            return ps.executeUpdate() > 0;
        }
    }

    private SinhVien docSinhVien(ResultSet rs) throws SQLException {
        Date ngay = rs.getDate("ngay_sinh");

        return new SinhVien(
            rs.getString("ma_sv"),
            rs.getString("ho_ten"),
            ngay == null ? "" : ngay.toString(),
            rs.getString("gioi_tinh"),
            rs.getString("email"),
            rs.getString("so_dien_thoai"),
            rs.getString("dia_chi"),
            rs.getString("lop")
        );
    }

    private void ganDuLieu(PreparedStatement ps, SinhVien sv)
            throws SQLException {
        ps.setString(1, sv.getMaSV());
        ps.setString(2, sv.getHoTen());
        setNgaySinh(ps, 3, sv.getNgaySinh());
        setNullableString(ps, 4, sv.getGioiTinh());
        setNullableString(ps, 5, sv.getEmail());
        setNullableString(ps, 6, sv.getSoDienThoai());
        setNullableString(ps, 7, sv.getDiaChi());
        setNullableString(ps, 8, sv.getLop());
    }

    private void setNgaySinh(PreparedStatement ps, int viTri, String ngay)
            throws SQLException {
        if (ngay == null || ngay.isBlank()) {
            ps.setNull(viTri, Types.DATE);
        } else {
            // Ngay sinh nhap theo dinh dang yyyy-MM-dd
            ps.setDate(viTri, Date.valueOf(ngay.trim()));
        }
    }

    private void setNullableString(PreparedStatement ps, int viTri,
                                   String giaTri) throws SQLException {
        if (giaTri == null || giaTri.isBlank()) {
            ps.setNull(viTri, Types.VARCHAR);
        } else {
            ps.setString(viTri, giaTri.trim());
        }
    }
}
