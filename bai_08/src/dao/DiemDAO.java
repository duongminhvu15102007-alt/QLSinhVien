
package dao;

import database.DBConnection;
import model.Diem;
import model.SinhVien;
import model.MonHoc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiemDAO {

    // Lay danh sach diem kem ten sinh vien va ten mon hoc
    public List<Diem> getAll() throws SQLException {
        List<Diem> ds = new ArrayList<>();

        String sql = """
            SELECT d.ma_sv, sv.ho_ten, d.ma_mh,
                   mh.ten_mh, mh.so_tin_chi, d.diem
            FROM diem d
            JOIN sinh_vien sv ON d.ma_sv = sv.ma_sv
            JOIN mon_hoc mh ON d.ma_mh = mh.ma_mh
            ORDER BY d.ma_sv, d.ma_mh
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ds.add(docDiem(rs));
            }
        }
        return ds;
    }

    // Tim kiem theo ma SV, ho ten SV, ma MH hoac ten MH
    public List<Diem> timKiem(String tuKhoa) throws SQLException {
        List<Diem> ds = new ArrayList<>();

        String sql = """
            SELECT d.ma_sv, sv.ho_ten, d.ma_mh,
                   mh.ten_mh, mh.so_tin_chi, d.diem
            FROM diem d
            JOIN sinh_vien sv ON d.ma_sv = sv.ma_sv
            JOIN mon_hoc mh ON d.ma_mh = mh.ma_mh
            WHERE d.ma_sv LIKE ?
               OR sv.ho_ten LIKE ?
               OR d.ma_mh LIKE ?
               OR mh.ten_mh LIKE ?
            ORDER BY d.ma_sv, d.ma_mh
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String key = "%" + tuKhoa + "%";
            for (int i = 1; i <= 4; i++) {
                ps.setString(i, key);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(docDiem(rs));
                }
            }
        }
        return ds;
    }

    // Them diem
    public boolean them(String maSV, String maMH, double diem)
            throws SQLException {
        String sql = "INSERT INTO diem (ma_sv, ma_mh, diem) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSV);
            ps.setString(2, maMH);
            ps.setDouble(3, diem);
            return ps.executeUpdate() > 0;
        }
    }

    // Sua diem theo khoa chinh ghep ma SV + ma MH
    public boolean sua(String maSV, String maMH, double diem)
            throws SQLException {
        String sql = """
            UPDATE diem
            SET diem = ?
            WHERE ma_sv = ? AND ma_mh = ?
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, diem);
            ps.setString(2, maSV);
            ps.setString(3, maMH);
            return ps.executeUpdate() > 0;
        }
    }

    // Xoa diem
    public boolean xoa(String maSV, String maMH) throws SQLException {
        String sql = "DELETE FROM diem WHERE ma_sv = ? AND ma_mh = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSV);
            ps.setString(2, maMH);
            return ps.executeUpdate() > 0;
        }
    }

    private Diem docDiem(ResultSet rs) throws SQLException {
        return new Diem(
            rs.getString("ma_sv"),
            rs.getString("ho_ten"),
            rs.getString("ma_mh"),
            rs.getString("ten_mh"),
            rs.getInt("so_tin_chi"),
            rs.getDouble("diem")
        );
    }

    // Danh sach sinh vien de chon tren giao dien
    public List<SinhVien> getSinhVien() throws SQLException {
        return new dao.SinhVienDAO().getAll();
    }

    // Danh sach mon hoc de chon tren giao dien
    public List<MonHoc> getMonHoc() throws SQLException {
        return new dao.MonHocDAO().getAll();
    }
}
