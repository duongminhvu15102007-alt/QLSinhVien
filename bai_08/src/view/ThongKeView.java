
package view;

import dao.ThongKeDAO;
import model.ThongKe;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ThongKeView extends JFrame {

    private final ThongKeDAO dao = new ThongKeDAO();
    private DefaultTableModel model;
    private JLabel lblTongSV;
    private JLabel lblDiemTBChung;

    public ThongKeView() {
        setTitle("THONG KE KET QUA HOC TAP");
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        taoGiaoDien();
        taiDuLieu();
    }

    private void taoGiaoDien() {
        setLayout(new BorderLayout(10, 10));

        JLabel tieuDe = new JLabel(
            "THONG KE KET QUA HOC TAP",
            SwingConstants.CENTER
        );
        tieuDe.setFont(new Font("Arial", Font.BOLD, 23));
        tieuDe.setBorder(BorderFactory.createEmptyBorder(15, 5, 10, 5));
        add(tieuDe, BorderLayout.NORTH);

        String[] cot = {
            "Ma SV", "Ho va ten", "So mon co diem",
            "Diem trung binh", "Xep loai"
        };

        model = new DefaultTableModel(cot, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(26);

        JPanel thongTin = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        lblTongSV = new JLabel("Tong so sinh vien: 0");
        lblDiemTBChung = new JLabel("Diem trung binh chung: 0.00");

        lblTongSV.setFont(new Font("Arial", Font.BOLD, 14));
        lblDiemTBChung.setFont(new Font("Arial", Font.BOLD, 14));

        thongTin.add(lblTongSV);
        thongTin.add(lblDiemTBChung);

        JButton btnTaiLai = new JButton("Cap nhat thong ke");
        btnTaiLai.addActionListener(e -> taiDuLieu());

        JPanel phanDuoi = new JPanel(new BorderLayout());
        phanDuoi.add(thongTin, BorderLayout.CENTER);
        phanDuoi.add(btnTaiLai, BorderLayout.SOUTH);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(phanDuoi, BorderLayout.SOUTH);
    }

    private void taiDuLieu() {
        try {
            List<ThongKe> ds = dao.getThongKe();
            model.setRowCount(0);

            double tongDiemTB = 0;
            int soSVCoDiem = 0;

            for (ThongKe tk : ds) {
                String diemHienThi;

                if (tk.getSoMon() == 0) {
                    diemHienThi = "Chua co diem";
                } else {
                    diemHienThi = String.format(
                        java.util.Locale.US, "%.2f",
                        tk.getDiemTrungBinh()
                    );

                    tongDiemTB += tk.getDiemTrungBinh();
                    soSVCoDiem++;
                }

                model.addRow(new Object[]{
                    tk.getMaSV(),
                    tk.getHoTen(),
                    tk.getSoMon(),
                    diemHienThi,
                    tk.getXepLoai()
                });
            }

            lblTongSV.setText("Tong so sinh vien: " + ds.size());

            double trungBinhChung = soSVCoDiem == 0
                ? 0 : tongDiemTB / soSVCoDiem;

            lblDiemTBChung.setText(
                String.format(
                    java.util.Locale.US,
                    "Diem TB cac sinh vien co diem: %.2f",
                    trungBinhChung
                )
            );

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this, "Khong the tai thong ke: " + ex.getMessage()
            );
        }
    }
}
