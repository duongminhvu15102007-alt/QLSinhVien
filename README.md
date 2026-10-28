Quản lý sinh viên -- quan_ly_sinh_vien
1. Giới thiệu
Cơ sở dữ liệu quản lý sinh viên được xây dựng bằng MariaDB/MySQL và quản
lý qua HeidiSQL. Theo file SQL đã cung cấp, cơ sở dữ liệu có ba bảng:
sinh_vien, mon_hoc và diem.
2. Công nghệ sử dụng
- MariaDB 10.4.32: hệ quản trị cơ sở dữ liệu được ghi trong file
  SQL.
- HeidiSQL 12.21: công cụ quản lý cơ sở dữ liệu.
- SQL: ngôn ngữ định nghĩa bảng và các ràng buộc.
3. Cấu trúc bảng
Bảng sinh_vien
Lưu thông tin sinh viên.

  Cột               Kiểu dữ liệu     Ràng buộc
  ma_sv           VARCHAR(15)    Khóa chính, NOT NULL
  ho_ten          VARCHAR(100)   NOT NULL
  ngay_sinh       DATE           Có thể để trống
  gioi_tinh       VARCHAR(10)    CHECK: Nam, Nu, Khac hoặc NULL
  email           VARCHAR(100)   UNIQUE
  so_dien_thoai   VARCHAR(15)    Có thể để trống
  dia_chi         VARCHAR(255)   Có thể để trống
  lop             VARCHAR(50)    Có thể để trống
Bảng mon_hoc
Lưu thông tin môn học.

  Cột            Kiểu dữ liệu     Ràng buộc
  ma_mh        VARCHAR(15)    Khóa chính, NOT NULL
  ten_mh       VARCHAR(100)   NOT NULL
  so_tin_chi   INT            NOT NULL, CHECK lớn hơn 0
Bảng diem
Lưu điểm của sinh viên theo từng môn học.

  Cột       Kiểu dữ liệu     Ràng buộc
  ma_sv   VARCHAR(15)    Thuộc khóa chính ghép, khóa ngoại
  ma_mh   VARCHAR(15)    Thuộc khóa chính ghép, khóa ngoại
  diem    DECIMAL(4,2)   NOT NULL, CHECK từ 0 đến 10

Khóa chính của bảng diem gồm ma_sv và ma_mh.
4. Quan hệ và ràng buộc
- diem.ma_sv tham chiếu sinh_vien.ma_sv.
- diem.ma_mh tham chiếu mon_hoc.ma_mh.
- Hai khóa ngoại của bảng diem sử dụng ON DELETE CASCADE và
  ON UPDATE CASCADE.
- Email sinh viên có ràng buộc duy nhất (UNIQUE).
- Giới tính chỉ nhận Nam, Nu, Khac hoặc NULL.
- Số tín chỉ phải lớn hơn 0.
- Điểm phải nằm trong khoảng từ 0 đến 10.
- Các bảng sử dụng engine InnoDB.
5. Cách sử dụng
1. Mở HeidiSQL và kết nối đến máy chủ MariaDB/MySQL.
2. Mở tab Query.
3. Mở hoặc dán nội dung file quan_ly_sv.sql.
4. Chạy SQL để tạo cơ sở dữ liệu quan_ly_sinh_vien và ba bảng.
5. Kiểm tra các bảng và khóa ngoại trong HeidiSQL.
Lưu ý: File SQL được cung cấp chỉ chứa cấu trúc bảng, không có dữ
liệu mẫu.
6. Tệp dự án
- README.md: giới thiệu dự án và mô tả cơ sở dữ liệu.
- quan_ly_sv.sql: file SQL xuất từ HeidiSQL.
7. Đưa dự án lên GitHub
1. Mở repository GitHub của dự án.
2. Chọn Add file → Upload files.
3. Tải lên file README và quan_ly_sv.sql.
4. Nhấn Commit changes.
Không tải mật khẩu thật hoặc dữ liệu cá nhân nhạy cảm lên repository
công khai.
8. Thông tin sinh viên
- Họ và tên: Điền họ và tên
- Lớp: Điền lớp
- Môn học: Điền tên môn học
