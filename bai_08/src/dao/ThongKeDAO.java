
package dao;

import database.DBConnection;
import model.ThongKe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThongKeDAO {

    public List<ThongKe> getThongKe() throws SQLException {
        List<ThongKe> ds = new ArrayList<>();

        String sql = """
            SELECT sv.ma_sv, sv.ho_ten,
                   COUNT(d.ma_mh) AS so_mon,
                   AVG(d.diem) AS diem_tb
            FROM sinh_vien sv
            LEFT JOIN diem d ON sv.ma_sv = d.ma_sv
            GROUP BY sv.ma_sv, sv.ho_ten
            ORDER BY sv.ma_sv
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int soMon = rs.getInt("so_mon");
                double diemTB = rs.getDouble("diem_tb");

                if (rs.wasNull()) {
                    diemTB = 0;
                }

                String xepLoai;
                if (soMon == 0) {
                    xepLoai = "Chua co diem";
                } else if (diemTB >= 8.0) {
                    xepLoai = "Gioi";
                } else if (diemTB >= 6.5) {
                    xepLoai = "Kha";
                } else if (diemTB >= 5.0) {
                    xepLoai = "Trung binh";
                } else {
                    xepLoai = "Yeu";
                }

                ds.add(new ThongKe(
                    rs.getString("ma_sv"),
                    rs.getString("ho_ten"),
                    soMon,
                    diemTB,
                    xepLoai
                ));
            }
        }

        return ds;
    }
}
