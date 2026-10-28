
package dao;

import database.DBConnection;
import model.MonHoc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonHocDAO {

    // Lay danh sach mon hoc
    public List<MonHoc> getAll() throws SQLException {
        List<MonHoc> danhSach = new ArrayList<>();

        String sql = "SELECT * FROM mon_hoc ORDER BY ma_mh";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                danhSach.add(new MonHoc(
                    rs.getString("ma_mh"),
                    rs.getString("ten_mh"),
                    rs.getInt("so_tin_chi")
                ));
            }
        }

        return danhSach;
    }

    // Tim kiem theo ma hoac ten mon hoc
    public List<MonHoc> timKiem(String tuKhoa) throws SQLException {
        List<MonHoc> danhSach = new ArrayList<>();

        String sql = """
            SELECT * FROM mon_hoc
            WHERE ma_mh LIKE ? OR ten_mh LIKE ?
            ORDER BY ma_mh
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String key = "%" + tuKhoa + "%";
            ps.setString(1, key);
            ps.setString(2, key);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    danhSach.add(new MonHoc(
                        rs.getString("ma_mh"),
                        rs.getString("ten_mh"),
                        rs.getInt("so_tin_chi")
                    ));
                }
            }
        }

        return danhSach;
    }

    // Them mon hoc
    public boolean them(MonHoc mh) throws SQLException {
        String sql = """
            INSERT INTO mon_hoc (ma_mh, ten_mh, so_tin_chi)
            VALUES (?, ?, ?)
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, mh.getMaMH());
            ps.setString(2, mh.getTenMH());
            ps.setInt(3, mh.getSoTinChi());

            return ps.executeUpdate() > 0;
        }
    }

    // Sua mon hoc
    public boolean sua(MonHoc mh) throws SQLException {
        String sql = """
            UPDATE mon_hoc
            SET ten_mh = ?, so_tin_chi = ?
            WHERE ma_mh = ?
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, mh.getTenMH());
            ps.setInt(2, mh.getSoTinChi());
            ps.setString(3, mh.getMaMH());

            return ps.executeUpdate() > 0;
        }
    }

    // Xoa mon hoc
    public boolean xoa(String maMH) throws SQLException {
        String sql = "DELETE FROM mon_hoc WHERE ma_mh = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maMH);
            return ps.executeUpdate() > 0;
        }
    }
}
