
package view;

import dao.SinhVienDAO;
import model.SinhVien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class SinhVienView extends JFrame {

    private final SinhVienDAO dao = new SinhVienDAO();

    private JTextField txtMaSV;
    private JTextField txtHoTen;
    private JTextField txtNgaySinh;
    private JComboBox<String> cboGioiTinh;
    private JTextField txtEmail;
    private JTextField txtSDT;
    private JTextField txtDiaChi;
    private JTextField txtLop;
    private JTextField txtTimKiem;

    private JTable table;
    private DefaultTableModel tableModel;

    public SinhVienView() {
        setTitle("PHAN MEM QUAN LY SINH VIEN");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        taoGiaoDien();
        taiDanhSach();
    }

    private void taoGiaoDien() {
        setLayout(new BorderLayout(10, 10));

        JLabel tieuDe = new JLabel(
            "QUAN LY SINH VIEN", SwingConstants.CENTER
        );
        tieuDe.setFont(new Font("Arial", Font.BOLD, 26));
        tieuDe.setBorder(BorderFactory.createEmptyBorder(15, 5, 10, 5));
        add(tieuDe, BorderLayout.NORTH);

        // Khu vuc nhap thong tin
        JPanel form = new JPanel(new GridLayout(4, 4, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder(
            "Thong tin sinh vien"
        ));

        txtMaSV = new JTextField();
        txtHoTen = new JTextField();
        txtNgaySinh = new JTextField();
        cboGioiTinh = new JComboBox<>(
            new String[]{"", "Nam", "Nu", "Khac"}
        );
        txtEmail = new JTextField();
        txtSDT = new JTextField();
        txtDiaChi = new JTextField();
        txtLop = new JTextField();

        form.add(new JLabel("Ma sinh vien:"));
        form.add(txtMaSV);
        form.add(new JLabel("Ho va ten:"));
        form.add(txtHoTen);

        form.add(new JLabel("Ngay sinh (yyyy-MM-dd):"));
        form.add(txtNgaySinh);
        form.add(new JLabel("Gioi tinh:"));
        form.add(cboGioiTinh);

        form.add(new JLabel("Email:"));
        form.add(txtEmail);
        form.add(new JLabel("So dien thoai:"));
        form.add(txtSDT);

        form.add(new JLabel("Dia chi:"));
        form.add(txtDiaChi);
        form.add(new JLabel("Lop:"));
        form.add(txtLop);

        JPanel khuVucTren = new JPanel(new BorderLayout(5, 10));
        khuVucTren.add(form, BorderLayout.CENTER);

        // Cac nut chuc nang
        JPanel nutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));

        JButton btnThem = new JButton("Them");
        JButton btnSua = new JButton("Sua");
        JButton btnXoa = new JButton("Xoa");
        JButton btnLamMoi = new JButton("Lam moi");

        txtTimKiem = new JTextField(18);
        JButton btnTimKiem = new JButton("Tim kiem");
        JButton btnTaiLai = new JButton("Tai danh sach");

        nutPanel.add(btnThem);
        nutPanel.add(btnSua);
        nutPanel.add(btnXoa);
        nutPanel.add(btnLamMoi);
        nutPanel.add(new JLabel("Tu khoa:"));
        nutPanel.add(txtTimKiem);
        nutPanel.add(btnTimKiem);
        nutPanel.add(btnTaiLai);

        khuVucTren.add(nutPanel, BorderLayout.SOUTH);
        add(khuVucTren, BorderLayout.CENTER);

        // Bang danh sach sinh vien
        String[] cot = {
            "Ma SV", "Ho va ten", "Ngay sinh", "Gioi tinh",
            "Email", "So dien thoai", "Dia chi", "Lop"
        };

        tableModel = new DefaultTableModel(cot, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setRowHeight(25);

        int[] doRong = {90, 160, 100, 80, 180, 120, 200, 100};
        for (int i = 0; i < doRong.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(doRong[i]);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder(
            "Danh sach sinh vien"
        ));
        add(scroll, BorderLayout.SOUTH);

        // Xu ly su kien cac nut
        btnThem.addActionListener(e -> themSinhVien());
        btnSua.addActionListener(e -> suaSinhVien());
        btnXoa.addActionListener(e -> xoaSinhVien());
        btnLamMoi.addActionListener(e -> xoaForm());
        btnTimKiem.addActionListener(e -> timKiem());
        btnTaiLai.addActionListener(e -> {
            txtTimKiem.setText("");
            taiDanhSach();
            xoaForm();
        });

        // Click mot dong tren bang de hien thong tin len form
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                hienThiLenForm(table.getSelectedRow());
            }
        });
    }

    private SinhVien layDuLieuForm() {
        String maSV = txtMaSV.getText().trim();
        String hoTen = txtHoTen.getText().trim();

        if (maSV.isEmpty() || hoTen.isEmpty()) {
            throw new IllegalArgumentException(
                "Ma sinh vien va ho ten khong duoc de trong!"
            );
        }

        String ngaySinh = txtNgaySinh.getText().trim();
        if (!ngaySinh.isEmpty()
                && !ngaySinh.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException(
                "Ngay sinh phai co dang yyyy-MM-dd, vi du 2005-08-20."
            );
        }

        return new SinhVien(
            maSV,
            hoTen,
            ngaySinh,
            (String) cboGioiTinh.getSelectedItem(),
            txtEmail.getText().trim(),
            txtSDT.getText().trim(),
            txtDiaChi.getText().trim(),
            txtLop.getText().trim()
        );
    }

    private void themSinhVien() {
        try {
            SinhVien sv = layDuLieuForm();

            if (dao.them(sv)) {
                thongBao("Them sinh vien thanh cong!");
                taiDanhSach();
                xoaForm();
            }
        } catch (IllegalArgumentException ex) {
            thongBao(ex.getMessage());
        } catch (SQLException ex) {
            xuLyLoiDatabase(ex);
        }
    }

    private void suaSinhVien() {
        try {
            if (table.getSelectedRow() < 0) {
                thongBao("Hay chon sinh vien can sua trong bang!");
                return;
            }

            SinhVien sv = layDuLieuForm();

            if (dao.sua(sv)) {
                thongBao("Cap nhat sinh vien thanh cong!");
                taiDanhSach();
                xoaForm();
            } else {
                thongBao("Khong tim thay sinh vien can sua!");
            }
        } catch (IllegalArgumentException ex) {
            thongBao(ex.getMessage());
        } catch (SQLException ex) {
            xuLyLoiDatabase(ex);
        }
    }

    private void xoaSinhVien() {
        int dong = table.getSelectedRow();

        if (dong < 0) {
            thongBao("Hay chon sinh vien can xoa trong bang!");
            return;
        }

        String maSV = tableModel.getValueAt(dong, 0).toString();

        int luaChon = JOptionPane.showConfirmDialog(
            this,
            "Ban co chac muon xoa sinh vien " + maSV + " khong?",
            "Xac nhan xoa",
            JOptionPane.YES_NO_OPTION
        );

        if (luaChon != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            if (dao.xoa(maSV)) {
                thongBao("Xoa sinh vien thanh cong!");
                taiDanhSach();
                xoaForm();
            } else {
                thongBao("Khong tim thay sinh vien can xoa!");
            }
        } catch (SQLException ex) {
            xuLyLoiDatabase(ex);
        }
    }

    private void timKiem() {
        try {
            List<SinhVien> danhSach =
                dao.timKiem(txtTimKiem.getText().trim());
            hienThiDanhSach(danhSach);
        } catch (SQLException ex) {
            xuLyLoiDatabase(ex);
        }
    }

    private void taiDanhSach() {
        try {
            hienThiDanhSach(dao.getAll());
        } catch (SQLException ex) {
            xuLyLoiDatabase(ex);
        }
    }

    private void hienThiDanhSach(List<SinhVien> danhSach) {
        tableModel.setRowCount(0);

        for (SinhVien sv : danhSach) {
            tableModel.addRow(new Object[]{
                sv.getMaSV(),
                sv.getHoTen(),
                sv.getNgaySinh(),
                sv.getGioiTinh(),
                sv.getEmail(),
                sv.getSoDienThoai(),
                sv.getDiaChi(),
                sv.getLop()
            });
        }
    }

    private void hienThiLenForm(int dong) {
        txtMaSV.setText(giaTriO(dong, 0));
        txtHoTen.setText(giaTriO(dong, 1));
        txtNgaySinh.setText(giaTriO(dong, 2));

        String gioiTinh = giaTriO(dong, 3);
        cboGioiTinh.setSelectedItem(
            gioiTinh.isEmpty() ? "" : gioiTinh
        );

        txtEmail.setText(giaTriO(dong, 4));
        txtSDT.setText(giaTriO(dong, 5));
        txtDiaChi.setText(giaTriO(dong, 6));
        txtLop.setText(giaTriO(dong, 7));

        // Khong cho sua ma sinh vien sau khi chon dong
        txtMaSV.setEditable(false);
    }

    private String giaTriO(int dong, int cot) {
        Object value = tableModel.getValueAt(dong, cot);
        return value == null ? "" : value.toString();
    }

    private void xoaForm() {
        table.clearSelection();
        txtMaSV.setEditable(true);

        txtMaSV.setText("");
        txtHoTen.setText("");
        txtNgaySinh.setText("");
        cboGioiTinh.setSelectedIndex(0);
        txtEmail.setText("");
        txtSDT.setText("");
        txtDiaChi.setText("");
        txtLop.setText("");

        txtMaSV.requestFocus(); 
    }

    private void xuLyLoiDatabase(SQLException ex) {
        ex.printStackTrace();

        String thongBao;
        String loi = ex.getMessage() == null ? "" : ex.getMessage();

        if ("23000".equals(ex.getSQLState())) {
            thongBao = "Ma sinh vien da ton tai hoac email bi trung. "
                     + "Hay kiem tra lai du lieu.";
        } else if (loi.contains("Data too long")) {
            thongBao = "Co truong du lieu qua dai. Hay kiem tra lai.";
        } else {
            thongBao = "Loi database: " + loi;
        }

        thongBao(thongBao);
    }

    private void thongBao(String noiDung) {
        JOptionPane.showMessageDialog(this, noiDung);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SinhVienView().setVisible(true);
        });
    }
}
