
package model;

public class Diem {
    private String maSV;
    private String hoTen;
    private String maMH;
    private String tenMH;
    private int soTinChi;
    private double diem;

    public Diem() {
    }

    public Diem(String maSV, String hoTen, String maMH,
                String tenMH, int soTinChi, double diem) {
        this.maSV = maSV;
        this.hoTen = hoTen;
        this.maMH = maMH;
        this.tenMH = tenMH;
        this.soTinChi = soTinChi;
        this.diem = diem;
    }

    public String getMaSV() { return maSV; }
    public void setMaSV(String maSV) { this.maSV = maSV; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getMaMH() { return maMH; }
    public void setMaMH(String maMH) { this.maMH = maMH; }

    public String getTenMH() { return tenMH; }
    public void setTenMH(String tenMH) { this.tenMH = tenMH; }

    public int getSoTinChi() { return soTinChi; }
    public void setSoTinChi(int soTinChi) { this.soTinChi = soTinChi; }

    public double getDiem() { return diem; }
    public void setDiem(double diem) { this.diem = diem; }
}
