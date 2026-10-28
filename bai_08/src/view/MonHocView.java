
package view;

import dao.MonHocDAO;
import model.MonHoc;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class MonHocView extends JFrame {

    private final MonHocDAO dao = new MonHocDAO();

    private JTextField txtMaMH;
    private JTextField txtTenMH;
    private JTextField txtTinChi;
    private JTextField txtTimKiem;

    private JTable table;
    private DefaultTableModel tableModel;

    public MonHocView() {
        setTitle("QUAN LY MON HOC");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        taoGiaoDien();
        taiDanhSach();
    }

    private void taoGiaoDien() {
        setLayout(new BorderLayout(10, 10));

        JLabel tieuDe = new JLabel(
            "QUAN LY MON HOC", SwingConstants.CENTER
        );
        tieuDe.setFont(new Font("Arial", Font.BOLD, 24));
        tieuDe.setBorder(BorderFactory.createEmptyBorder(15, 5, 10, 5));
        add(tieuDe, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Thong tin mon hoc"));

        txtMaMH = new JTextField();
        txtTenMH = new JTextField();
        txtTinChi = new JTextField();

        form.add(new JLabel("Ma mon hoc:"));
        form.add(txtMaMH);
        form.add(new JLabel("Ten mon hoc:"));
        form.add(txtTenMH);
        form.add(new JLabel("So tin chi:"));
        form.add(txtTinChi);

        JButton btnThem = new JButton("Them");
        JButton btnSua = new JButton("Sua");
        JButton btnXoa = new JButton("Xoa");
        JButton btnLamMoi = new JButton("Lam moi");
        JButton btnTim = new JButton("Tim kiem");
        JButton btnTaiLai = new JButton("Tai danh sach");

        txtTimKiem = new JTextField(15);

        JPanel nutPanel = new JPanel(new FlowLayout());
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

        String[] cot = {"Ma mon hoc", "Ten mon hoc", "So tin chi"};

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

        btnThem.addActionListener(e -> themMonHoc());
        btnSua.addActionListener(e -> suaMonHoc());
        btnXoa.addActionListener(e -> xoaMonHoc());
        btnLamMoi.addActionListener(e -> xoaForm());
        btnTim.addActionListener(e -> timKiem());
        btnTaiLai.addActionListener(e -> {
            txtTimKiem.setText("");
            taiDanhSach();
            xoaForm();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int dong = table.getSelectedRow();

                txtMaMH.setText(tableModel.getValueAt(dong, 0).toString());
                txtTenMH.setText(tableModel.getValueAt(dong, 1).toString());
                txtTinChi.setText(tableModel.getValueAt(dong, 2).toString());

                txtMaMH.setEditable(false);
            }
        });
    }

    private MonHoc layDuLieuForm() {
        String ma = txtMaMH.getText().trim();
        String ten = txtTenMH.getText().trim();
        String tinChiText = txtTinChi.getText().trim();

        if (ma.isEmpty() || ten.isEmpty() || tinChiText.isEmpty()) {
            throw new IllegalArgumentException(
                "Vui long nhap day du ma, ten mon hoc va so tin chi!"
            );
        }

        int tinChi;
        try {
            tinChi = Integer.parseInt(tinChiText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "So tin chi phai la so nguyen!"
            );
        }

        if (tinChi <= 0) {
            throw new IllegalArgumentException(
                "So tin chi phai lon hon 0!"
            );
        }

        return new MonHoc(ma, ten, tinChi);
    }

    private void themMonHoc() {
        try {
            if (dao.them(layDuLieuForm())) {
                thongBao("Them mon hoc thanh cong!");
                taiDanhSach();
                xoaForm();
            }
        } catch (IllegalArgumentException ex) {
            thongBao(ex.getMessage());
        } catch (SQLException ex) {
            xuLyLoi(ex);
        }
    }

    private void suaMonHoc() {
        try {
            if (table.getSelectedRow() < 0) {
                thongBao("Hay chon mon hoc can sua trong bang!");
                return;
            }

            if (dao.sua(layDuLieuForm())) {
                thongBao("Cap nhat mon hoc thanh cong!");
                taiDanhSach();
                xoaForm();
            } else {
                thongBao("Khong tim thay mon hoc!");
            }
        } catch (IllegalArgumentException ex) {
            thongBao(ex.getMessage());
        } catch (SQLException ex) {
            xuLyLoi(ex);
        }
    }

    private void xoaMonHoc() {
        int dong = table.getSelectedRow();

        if (dong < 0) {
            thongBao("Hay chon mon hoc can xoa!");
            return;
        }

        String ma = tableModel.getValueAt(dong, 0).toString();

        int chon = JOptionPane.showConfirmDialog(
            this,
            "Ban co chac muon xoa mon hoc " + ma + " khong?",
            "Xac nhan",
            JOptionPane.YES_NO_OPTION
        );

        if (chon != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            if (dao.xoa(ma)) {
                thongBao("Xoa mon hoc thanh cong!");
                taiDanhSach();
                xoaForm();
            } else {
                thongBao("Khong tim thay mon hoc!");
            }
        } catch (SQLException ex) {
            xuLyLoi(ex);
        }
    }

    private void timKiem() {
        try {
            hienThiDanhSach(dao.timKiem(txtTimKiem.getText().trim()));
        } catch (SQLException ex) {
            xuLyLoi(ex);
        }
    }

    private void taiDanhSach() {
        try {
            hienThiDanhSach(dao.getAll());
        } catch (SQLException ex) {
            xuLyLoi(ex);
        }
    }

    private void hienThiDanhSach(List<MonHoc> danhSach) {
        tableModel.setRowCount(0);

        for (MonHoc mh : danhSach) {
            tableModel.addRow(new Object[]{
                mh.getMaMH(),
                mh.getTenMH(),
                mh.getSoTinChi()
            });
        }
    }

    private void xoaForm() {
        table.clearSelection();
        txtMaMH.setEditable(true);
        txtMaMH.setText("");
        txtTenMH.setText("");
        txtTinChi.setText("");
        txtMaMH.requestFocus();
    }

    private void xuLyLoi(SQLException ex) {
        ex.printStackTrace();

        if ("23000".equals(ex.getSQLState())) {
            thongBao(
                "Ma mon hoc da ton tai hoac mon hoc dang duoc su dung "
                + "trong bang diem. Hay kiem tra lai."
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
            new MonHocView().setVisible(true);
        });
    }
}
