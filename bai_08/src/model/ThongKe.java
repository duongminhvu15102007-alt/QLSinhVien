
package model;

public class ThongKe {
    private String maSV;
    private String hoTen;
    private int soMon;
    private double diemTrungBinh;
    private String xepLoai;

    public ThongKe(String maSV, String hoTen, int soMon,
                   double diemTrungBinh, String xepLoai) {
        this.maSV = maSV;
        this.hoTen = hoTen;
        this.soMon = soMon;
        this.diemTrungBinh = diemTrungBinh;
        this.xepLoai = xepLoai;
    }

    public String getMaSV() { return maSV; }
    public String getHoTen() { return hoTen; }
    public int getSoMon() { return soMon; }
    public double getDiemTrungBinh() { return diemTrungBinh; }
    public String getXepLoai() { return xepLoai; }
}
