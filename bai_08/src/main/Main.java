
package main;
import view.ThongKeView;
import view.SinhVienView;
import view.MonHocView;
import view.DiemView;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public Main() {
        setTitle("HE THONG QUAN LY SINH VIEN");
        setSize(650, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        taoGiaoDien();
    }

    private void taoGiaoDien() {
        setLayout(new BorderLayout(15, 15));

        JLabel tieuDe = new JLabel(
            "PHAN MEM QUAN LY SINH VIEN",
            SwingConstants.CENTER
        );
        tieuDe.setFont(new Font("Arial", Font.BOLD, 25));
        tieuDe.setBorder(BorderFactory.createEmptyBorder(25, 10, 15, 10));
        add(tieuDe, BorderLayout.NORTH);

        JButton btnSinhVien = new JButton("QUAN LY SINH VIEN");
        JButton btnMonHoc = new JButton("QUAN LY MON HOC");
        JButton btnDiem = new JButton("QUAN LY DIEM");
        JButton btnThongKe = new JButton("THONG KE KET QUA HOC TAP");
        btnThongKe.setFont(new Font("Arial", Font.BOLD, 16));
        btnThongKe.setPreferredSize(new Dimension(250, 65));
        

        Font fontNut = new Font("Arial", Font.BOLD, 16);
        btnSinhVien.setFont(fontNut);
        btnMonHoc.setFont(fontNut);
        btnDiem.setFont(fontNut);

        btnSinhVien.setPreferredSize(new Dimension(250, 65));
        btnMonHoc.setPreferredSize(new Dimension(250, 65));
        btnDiem.setPreferredSize(new Dimension(250, 65));

        JPanel nutPanel = new JPanel(
            new GridLayout(4, 1, 15, 15)
        );
        nutPanel.setBorder(BorderFactory.createEmptyBorder(10, 100, 25, 100));

        nutPanel.add(btnSinhVien);
        nutPanel.add(btnMonHoc);
        nutPanel.add(btnDiem);
        nutPanel.add(btnThongKe);

        add(nutPanel, BorderLayout.CENTER);

        JLabel chanTrang = new JLabel(
            "Java Swing - MinhVu",
            SwingConstants.CENTER
        );
        chanTrang.setBorder(BorderFactory.createEmptyBorder(5, 5, 15, 5));
        add(chanTrang, BorderLayout.SOUTH);

        btnSinhVien.addActionListener(e ->
            new SinhVienView().setVisible(true)
        );

        btnMonHoc.addActionListener(e ->
            new MonHocView().setVisible(true)
        );

        btnDiem.addActionListener(e ->
            new DiemView().setVisible(true)
        );
        btnThongKe.addActionListener(e ->
        new ThongKeView().setVisible(true)
    );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}
