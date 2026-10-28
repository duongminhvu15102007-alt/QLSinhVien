# Quản lý sinh viên -- QLSinhVien

## 1. Giới thiệu

`QLSinhVien` là cơ sở dữ liệu quản lý sinh viên được xây dựng bằng
**MySQL** và quản lý thông qua **HeidiSQL**. Cơ sở dữ liệu tổ chức thông
tin khoa, lớp, sinh viên, môn học, điểm và tài khoản người dùng.

## 2. Công nghệ sử dụng

-   **MySQL**: hệ quản trị cơ sở dữ liệu.
-   **HeidiSQL**: công cụ tạo và quản lý bảng, khóa chính, khóa ngoại.
-   **SQL**: ngôn ngữ tạo cấu trúc và ràng buộc dữ liệu.

## 3. Danh sách bảng

  -----------------------------------------------------------------------
  Bảng                    Mục đích                Khóa chính
  ----------------------- ----------------------- -----------------------
  `tblKhoa`               Lưu thông tin khoa      `fldMa_khoa`

  `tblClass`              Lưu thông tin lớp và    `fldMa_lop`
                          khoa trực thuộc         

  `tblSinhVien`           Lưu thông tin sinh viên `fldMSV`

  `tblMon_hoc`            Lưu thông tin môn học   `fldMa_mon`

  `tblDiem`               Lưu điểm tổng kết của   Khóa ghép `fldMa_mon`,
                          sinh viên theo môn học  `fldMSV`

  `tblUser`               Lưu thông tin tài khoản `fldUser`
                          người dùng              
  -----------------------------------------------------------------------

## 4. Các quan hệ giữa bảng

-   `tblClass.fldMa_khoa` tham chiếu `tblKhoa.fldMa_khoa`.
-   `tblSinhVien.fldMa_lop` tham chiếu `tblClass.fldMa_lop`.
-   `tblDiem.fldMSV` tham chiếu `tblSinhVien.fldMSV`.
-   `tblDiem.fldMa_mon` tham chiếu `tblMon_hoc.fldMa_mon`.
-   `tblUser` là bảng độc lập trong thiết kế hiện tại.

Các bảng có quan hệ khóa ngoại nên sử dụng engine `InnoDB` trong MySQL.
Kiểu dữ liệu của cột khóa ngoại phải tương thích với cột được tham
chiếu.

## 5. Cách sử dụng

1.  Cài đặt và mở MySQL Server.
2.  Mở HeidiSQL và kết nối đến MySQL Server.
3.  Tạo cơ sở dữ liệu `quan_ly_sinh_vien` hoặc dùng tên cơ sở dữ liệu
    đang có.
4.  Chạy file SQL xuất từ HeidiSQL để khôi phục cấu trúc bảng và các
    ràng buộc (nếu đã có file `.sql`).
5.  Kiểm tra danh sách bảng và các khóa ngoại trong HeidiSQL.

> Lưu ý: File README này mô tả cấu trúc dự kiến của cơ sở dữ liệu. Hãy
> cập nhật tên cơ sở dữ liệu hoặc danh sách bảng nếu cấu trúc thực tế
> trên máy khác với mô tả.

## 6. Đưa bài lên GitHub

1.  Tạo một repository trên GitHub.
2.  Tải `README.md`, file xuất cơ sở dữ liệu `.sql` và ảnh sơ đồ quan hệ
    (nếu có) lên repository.
3.  Không tải lên mật khẩu thật, thông tin đăng nhập hoặc dữ liệu cá
    nhân nhạy cảm.
4.  Nhấn **Commit changes** để lưu các tệp.

## 7. Tác giả

-   Sinh viên: *Điền họ và tên*
-   Lớp: *Điền lớp*
-   Môn học: *Điền tên môn học*
