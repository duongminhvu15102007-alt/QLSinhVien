
package view;

import dao.DiemDAO;
import model.Diem;
import model.SinhVien;
import model.MonHoc;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DiemView extends JFrame {

    private final DiemDAO dao = new DiemDAO();

    private JComboBox<SinhVien> cboSinhVien;
    private JComboBox<MonHoc> cboMonHoc;
    private JTextField txtDiem;
    private JTextField txtTimKiem;

    private JTable table;
    private DefaultTableModel tableModel;

    public DiemView() {
        setTitle("QUAN LY DIEM SINH VIEN");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        taoGiaoDien();
        napDanhSachChon();
        taiDanhSachDiem();
    }

    private void taoGiaoDien() {
        setLayout(new BorderLayout(10, 10));

        JLabel tieuDe = new JLabel(
            "QUAN LY DIEM SINH VIEN", SwingConstants.CENTER
        );
        tieuDe.setFont(new Font("Arial", Font.BOLD, 24));
        tieuDe.setBorder(BorderFactory.createEmptyBorder(15, 5, 10, 5));
        add(tieuDe, BorderLayout.NORTH);

        cboSinhVien = new JComboBox<>();
        cboMonHoc = new JComboBox<>();
        txtDiem = new JTextField();
        txtTimKiem = new JTextField(15);

        // Hien thi ma va ten sinh vien trong combobox
        cboSinhVien.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus
                );

                if (value instanceof SinhVien sv) {
                    setText(sv.getMaSV() + " - " + sv.getHoTen());
                }
                return this;
            }
        });

        // Hien thi ma va ten mon hoc trong combobox
        cboMonHoc.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus
                );

                if (value instanceof MonHoc mh) {
                    setText(mh.getMaMH() + " - " + mh.getTenMH());
                }
                return this;
            }
        });

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Nhap thong tin diem"));
        form.add(new JLabel("Sinh vien:"));
        form.add(cboSinhVien);
        form.add(new JLabel("Mon hoc:"));
        form.add(cboMonHoc);
        form.add(new JLabel("Diem (0 - 10):"));
        form.add(txtDiem);

        JButton btnThem = new JButton("Them diem");
        JButton btnSua = new JButton("Sua diem");
        JButton btnXoa = new JButton("Xoa diem");
        JButton btnLamMoi = new JButton("Lam moi");
        JButton btnTim = new JButton("Tim kiem");
        JButton btnTaiLai = new JButton("Tai danh sach");

        JPanel nutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        nutPanel.add(btnThem);
        nutPanel.add(btnSua);
        nutPanel.add(btnXoa);
        nutPanel.add(btnLamMoi);
        nutPanel.add(new JLabel("Tu khoa:"));
        nutPanel.add(txtTimKiem);
        nutPanel.add(btnTim);
        nutPanel.add(btnTaiLai);

        JPanel phanTren = new JPanel(new BorderLayout(5, 10));
        phanTren.add(form, BorderLayout.CENTER);
        phanTren.add(nutPanel, BorderLayout.SOUTH);

        String[] cot = {
            "Ma SV", "Ho ten sinh vien", "Ma mon",
            "Ten mon hoc", "Tin chi", "Diem"
        };

        tableModel = new DefaultTableModel(cot, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);

        JPanel noiDung = new JPanel(new BorderLayout(10, 10));
        noiDung.add(phanTren, BorderLayout.NORTH);
        noiDung.add(new JScrollPane(table), BorderLayout.CENTER);
        add(noiDung, BorderLayout.CENTER);

        btnThem.addActionListener(e -> themDiem());
        btnSua.addActionListener(e -> suaDiem());
        btnXoa.addActionListener(e -> xoaDiem());
        btnLamMoi.addActionListener(e -> xoaForm());
        btnTim.addActionListener(e -> timKiem());
        btnTaiLai.addActionListener(e -> {
            txtTimKiem.setText("");
            taiDanhSachDiem();
            napDanhSachChon();
            xoaForm();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                hienThiDongDuocChon(table.getSelectedRow());
            }
        });
    }

    private void napDanhSachChon() {
        try {
            cboSinhVien.removeAllItems();
            for (SinhVien sv : dao.getSinhVien()) {
                cboSinhVien.addItem(sv);
            }

            cboMonHoc.removeAllItems();
            for (MonHoc mh : dao.getMonHoc()) {
                cboMonHoc.addItem(mh);
            }
        } catch (SQLException ex) {
            xuLyLoi(ex);
        }
    }

    private double layDiemHopLe() {
        String chuoi = txtDiem.getText().trim();

        if (chuoi.isEmpty()) {
            throw new IllegalArgumentException("Vui long nhap diem!");
        }

        double diem;
        try {
            diem = Double.parseDouble(chuoi.replace(',', '.'));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                "Diem phai la so, vi du 8.5!"
            );
        }

        if (!Double.isFinite(diem) || diem < 0 || diem > 10) {
            throw new IllegalArgumentException(
                "Diem phai nam trong khoang tu 0 den 10!"
            );
        }

        return diem;
    }

    private boolean kiemTraLuaChon() {
        if (cboSinhVien.getSelectedItem() == null) {
            thongBao("Chua co sinh vien. Hay them sinh vien truoc!");
            return false;
        }

        if (cboMonHoc.getSelectedItem() == null) {
            thongBao("Chua co mon hoc. Hay them mon hoc truoc!");
            return false;
        }

        return true;
    }

    private void themDiem() {
        try {
            if (!kiemTraLuaChon()) return;

            SinhVien sv = (SinhVien) cboSinhVien.getSelectedItem();
            MonHoc mh = (MonHoc) cboMonHoc.getSelectedItem();
            double diem = layDiemHopLe();

            if (dao.them(sv.getMaSV(), mh.getMaMH(), diem)) {
                thongBao("Them diem thanh cong!");
                taiDanhSachDiem();
                xoaForm();
            }
        } catch (IllegalArgumentException ex) {
            thongBao(ex.getMessage());
        } catch (SQLException ex) {
            xuLyLoi(ex);
        }
    }

    private void suaDiem() {
        int dong = table.getSelectedRow();

        if (dong < 0) {
            thongBao("Hay chon dong diem can sua!");
            return;
        }

        try {
            double diem = layDiemHopLe();

            // Dung khoa chinh tren bang de sua dung ban ghi da chon
            String maSV = tableModel.getValueAt(dong, 0).toString();
            String maMH = tableModel.getValueAt(dong, 2).toString();

            if (dao.sua(maSV, maMH, diem)) {
                thongBao("Cap nhat diem thanh cong!");
                taiDanhSachDiem();
                xoaForm();
            } else {
                thongBao("Khong tim thay ban ghi diem!");
            }
        } catch (IllegalArgumentException ex) {
            thongBao(ex.getMessage());
        } catch (SQLException ex) {
            xuLyLoi(ex);
        }
    }

    private void xoaDiem() {
        int dong = table.getSelectedRow();

        if (dong < 0) {
            thongBao("Hay chon dong diem can xoa!");
            return;
        }

        String maSV = tableModel.getValueAt(dong, 0).toString();
        String maMH = tableModel.getValueAt(dong, 2).toString();

        int luaChon = JOptionPane.showConfirmDialog(
            this,
            "Xoa diem cua sinh vien " + maSV
                + " o mon " + maMH + "?",
            "Xac nhan xoa",
            JOptionPane.YES_NO_OPTION
        );

        if (luaChon != JOptionPane.YES_OPTION) return;

        try {
            if (dao.xoa(maSV, maMH)) {
                thongBao("Xoa diem thanh cong!");
                taiDanhSachDiem();
                xoaForm();
            } else {
                thongBao("Khong tim thay ban ghi diem!");
            }
        } catch (SQLException ex) {
            xuLyLoi(ex);
        }
    }

    private void timKiem() {
        try {
            hienThiDanhSach(
                dao.timKiem(txtTimKiem.getText().trim())
            );
        } catch (SQLException ex) {
            xuLyLoi(ex);
        }
    }

    private void taiDanhSachDiem() {
        try {
            hienThiDanhSach(dao.getAll());
        } catch (SQLException ex) {
            xuLyLoi(ex);
        }
    }

    private void hienThiDanhSach(List<Diem> ds) {
        tableModel.setRowCount(0);

        for (Diem d : ds) {
            tableModel.addRow(new Object[]{
                d.getMaSV(),
                d.getHoTen(),
                d.getMaMH(),
                d.getTenMH(),
                d.getSoTinChi(),
                d.getDiem()
            });
        }
    }

    private void hienThiDongDuocChon(int dong) {
        String maSV = tableModel.getValueAt(dong, 0).toString();
        String maMH = tableModel.getValueAt(dong, 2).toString();
        String diem = tableModel.getValueAt(dong, 5).toString();

        for (int i = 0; i < cboSinhVien.getItemCount(); i++) {
            SinhVien sv = cboSinhVien.getItemAt(i);
            if (sv.getMaSV().equals(maSV)) {
                cboSinhVien.setSelectedIndex(i);
                break;
            }
        }

        for (int i = 0; i < cboMonHoc.getItemCount(); i++) {
            MonHoc mh = cboMonHoc.getItemAt(i);
            if (mh.getMaMH().equals(maMH)) {
                cboMonHoc.setSelectedIndex(i);
                break;
            }
        }

        txtDiem.setText(diem);
    }

    private void xoaForm() {
        table.clearSelection();

        if (cboSinhVien.getItemCount() > 0) {
            cboSinhVien.setSelectedIndex(0);
        }

        if (cboMonHoc.getItemCount() > 0) {
            cboMonHoc.setSelectedIndex(0);
        }

        txtDiem.setText("");
        txtDiem.requestFocus();
    }

    private void xuLyLoi(SQLException ex) {
        ex.printStackTrace();

        if ("23000".equals(ex.getSQLState())) {
            thongBao(
                "Ban ghi diem nay da ton tai, hoac du lieu vi pham "
                + "rang buoc khoa ngoai. Moi sinh vien chi co mot diem "
                + "cho moi mon hoc trong cau truc hien tai."
            );
        } else {
            thongBao("Loi database: " + ex.getMessage());
        }
    }

    private void thongBao(String noiDung) {
        JOptionPane.showMessageDialog(this, noiDung);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DiemView().setVisible(true);
        });
    }
}
