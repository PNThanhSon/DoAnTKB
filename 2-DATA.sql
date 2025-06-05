INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, SoTietThucHien, SoTietDuThieu, Email, SDT, MatKhau, GhiChu) VALUES
('ADMIN','Quản trị','viên','Khác','Quản trị hệ thống',NULL,0,0,0,'admin@example.com','0000000000','pass','Tài khoản quản trị viên cao nhất của hệ thống.');

-- Thêm dữ liệu vào bảng CHUCVU
INSERT INTO CHUCVU (MaCV, TenCV) VALUES ('TLHD', 'Tâm lý học đường');
INSERT INTO CHUCVU (MaCV, TenCV) VALUES ('TTCM', 'Tổ trưởng chuyên môn');
INSERT INTO CHUCVU (MaCV, TenCV) VALUES ('TTCĐ', 'Tổ trưởng công đoàn');
INSERT INTO CHUCVU (MaCV, TenCV) VALUES ('TKHĐ', 'Thư ký hội đồng');
INSERT INTO CHUCVU (MaCV, TenCV) VALUES ('TPCM', 'Tổ phó chuyên môn');
INSERT INTO CHUCVU (MaCV, TenCV) VALUES ('PM', 'Phòng máy');
INSERT INTO CHUCVU (MaCV, TenCV) VALUES ('CNTT', 'Công nghệ thông tin');
INSERT INTO CHUCVU (MaCV, TenCV) VALUES ('PCTCĐ', 'Phó chủ tịch công đoàn');
INSERT INTO CHUCVU (MaCV, TenCV) VALUES ('TTrND', 'Thanh tra nhân dân');
INSERT INTO CHUCVU (MaCV, TenCV) VALUES ('CTCĐ', 'Chủ tịch công đoàn');
INSERT INTO CHUCVU (MaCV, TenCV) VALUES ('PBTĐ', 'Phó bí thư đoàn');
INSERT INTO CHUCVU (MaCV, TenCV) VALUES ('BTĐ', 'Bí thư đoàn');
INSERT INTO CHUCVU (MaCV, TenCV) VALUES ('CN', 'Chủ nhiệm');
INSERT INTO CHUCVU (MaCV, TenCV) VALUES ('PTHLY', 'Phòng thực hành Vật lý');
INSERT INTO CHUCVU (MaCV, TenCV) VALUES ('PTHHOA', 'Phòng thực hành Hóa học');
INSERT INTO CHUCVU (MaCV, TenCV) VALUES ('PTHSINH', 'Phòng thực hành Sinh học');
INSERT INTO CHUCVU (MaCV, TenCV) VALUES ('BGH', 'Ban giám hiệu');

-- Thêm dữ liệu vào bảng HOCKY
INSERT INTO HOCKY (MaHK, HocKy, NamHoc) VALUES ('HK1-24-25', 'Học kỳ I', '2024-2025');
INSERT INTO HOCKY (MaHK, HocKy, NamHoc) VALUES ('HK2-24-25', 'Học kỳ II', '2024-2025');

-- Thêm dữ liệu vào bảng TOCHUYENMON
INSERT INTO TOCHUYENMON (MaTCM, TenTCM, ToTruong, ToPho) VALUES
('THT', 'Tổ Toán - Tin học', 'ADMIN', 'ADMIN');
INSERT INTO TOCHUYENMON (MaTCM, TenTCM, ToTruong, ToPho) VALUES
('LYCNKT', 'Tổ Lý - CNKT', 'ADMIN', 'ADMIN');
INSERT INTO TOCHUYENMON (MaTCM, TenTCM, ToTruong, ToPho) VALUES
('HOASINHTD', 'Tổ Hóa - Sinh - TD', 'ADMIN', 'ADMIN');
INSERT INTO TOCHUYENMON (MaTCM, TenTCM, ToTruong, ToPho) VALUES
('NGUVAN', 'Tổ Ngữ văn', 'ADMIN', 'ADMIN');
INSERT INTO TOCHUYENMON (MaTCM, TenTCM, ToTruong, ToPho) VALUES
('SUDIACDQP', 'Tổ Sử - Địa - CD - QP', 'ADMIN', 'ADMIN');
INSERT INTO TOCHUYENMON (MaTCM, TenTCM, ToTruong, ToPho) VALUES
('NGOAINGU', 'Tổ Ngoại ngữ', 'ADMIN', 'ADMIN');

-- Bảng GIAOVIEN
-- Các giáo viên thuộc Ban Giám Hiệu hoặc vai trò đặc biệt (gán MaTCM tạm hoặc NULL nếu chưa rõ)
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau, Email, SDT, GhiChu, SoTietThucHien, SoTietDuThieu) VALUES
('GV001', 'Đỗ Văn', 'Quảng', 'Nam', 'Ths Toán', NULL, 2, 'pass', NULL, NULL, NULL, NULL, NULL);
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau, Email, SDT, GhiChu, SoTietThucHien, SoTietDuThieu) VALUES
('GV002', 'Phan Thị', 'Thảo', 'Nữ', 'ThS QLGD - ĐH Anh văn', NULL, 4, 'pass', NULL, NULL, NULL, NULL, NULL);
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau, Email, SDT, GhiChu, SoTietThucHien, SoTietDuThieu) VALUES
('GV003', 'Triệu Trung', 'Kiên', 'Nam', 'Ths QLGD - ĐH Sử-QP', NULL, 4, 'pass', NULL, NULL, NULL, NULL, NULL);

-- Tổ Toán - Tin học (MaTCM = 'THT')
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV004', 'Nguyễn Văn', 'Tuyến', 'Nam', 'ThS Toán', 'THT', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV005', 'Nguyễn Thị', 'Hà', 'Nữ', 'ĐH Toán', 'THT', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV006', 'Đỗ Thị Thu', 'Hà', 'Nữ', 'ĐH Toán', 'THT', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV007', 'Đinh Minh', 'Hoàng', 'Nam', 'ThS Toán', 'THT', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV008', 'Trần Hữu', 'Phước', 'Nam', 'ĐH Toán', 'THT', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV009', 'Phạm Bích', 'Phượng', 'Nữ', 'ĐH Toán', 'THT', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV010', 'Trần Phạm Thanh', 'Uyên', 'Nữ', 'ThS Toán', 'THT', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV011', 'Nguyễn Thúy', 'Hằng', 'Nữ', 'ĐH Toán-Tin', 'THT', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV012', 'Võ Thị Thái', 'Bình', 'Nữ', 'ThS Toán - ĐH Toán-Tin', 'THT', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV013', 'Trần Thị Phương', 'Lan', 'Nữ', 'ĐH Toán-Tin', 'THT', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV014', 'Nguyễn Thị', 'Thư', 'Nữ', 'ĐH Toán-Tin', 'THT', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV015', 'Vũ Bảo', 'Tuyên', 'Nam', 'ĐH Toán-Tin', 'THT', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV016', 'Phan Xuân', 'Vĩnh', 'Nam', 'ĐH Tin', 'THT', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV017', 'Đinh Anh', 'Tuyên', 'Nam', 'ĐH Tin', 'THT', NULL, 'pass'); -- Số tiết quy định trống
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV018', ' ', 'Thanh', 'Khác', 'ĐH Tin', 'THT', NULL, 'pass'); -- Họ trống, Số tiết quy định trống

-- Tổ Lý - CNKT (MaTCM = 'LYCNKT')
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV019', 'Nguyễn Đình', 'Dũng', 'Nam', 'ĐH Vật lý', 'LYCNKT', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV020', 'Nguyễn Thị Lan', 'Chi', 'Nữ', 'ĐH Vật lý', 'LYCNKT', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV021', 'Đoàn Văn', 'Dương', 'Nam', 'ĐH Vật lý', 'LYCNKT', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV022', 'Lê Thị Cẩm', 'Hường', 'Nữ', 'ĐH Vật lý', 'LYCNKT', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV023', 'Nguyễn Thị Thanh', 'Huyền', 'Nữ', 'ThS Vật lý', 'LYCNKT', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV024', 'Tạ Nữ Hoàng', 'Quyên', 'Nữ', 'ĐH Vật lý', 'LYCNKT', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV025', 'Nguyễn Thanh', 'Thúy', 'Nữ', 'ĐH Vật lý', 'LYCNKT', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV026', 'Vũ Thị Thanh', 'Huyền', 'Nữ', 'ThS Vật lý', 'LYCNKT', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV027', 'Nguyễn Thị', 'Phượng', 'Nữ', 'ĐH Vật lý', 'LYCNKT', 17, 'pass');

-- Tổ Hóa - Sinh - TD (MaTCM = 'HOASINHTD')
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV028', 'Trần Thị Kim', 'Ngọc', 'Nữ', 'ThS Hóa', 'HOASINHTD', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV029', 'Lê Vũ Thùy', 'An', 'Nữ', 'ĐH Hóa', 'HOASINHTD', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV030', 'Võ Thị', 'Hoàn', 'Nữ', 'ThS Hóa', 'HOASINHTD', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV031', 'Bùi Thuỷ', 'Linh', 'Nữ', 'ThS Hóa', 'HOASINHTD', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV032', 'Trần Xuân', 'Hiệp', 'Nam', 'ĐH Hóa', 'HOASINHTD', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV033', 'Đặng Thị Thuý', 'Hà', 'Nữ', 'ĐH Hóa', 'HOASINHTD', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV034', 'Phan Thị Hải', 'Lý', 'Nữ', 'ĐH Sinh', 'HOASINHTD', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV035', 'Nguyễn Thị Hồng', 'Nhung', 'Nữ', 'ThS Sinh', 'HOASINHTD', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV036', 'Nguyễn Thị', 'Sơn', 'Nữ', 'ĐH Sinh', 'HOASINHTD', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV037', 'Nguyễn Tiến', 'Dũng', 'Nam', 'ĐH Thể dục', 'HOASINHTD', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV038', 'Phạm Thị Châu', 'Loan', 'Nữ', 'ĐH Thể dục', 'HOASINHTD', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV039', 'Vũ Văn', 'Minh', 'Nam', 'ĐH Thể dục', 'HOASINHTD', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV040', 'Nguyễn Thành', 'Quang', 'Nam', 'ĐH TC-QP', 'HOASINHTD', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV041', 'Hoàng Nguyễn Trung', 'Hiếu', 'Nam', 'ĐH TC-QP', 'HOASINHTD', 17, 'pass');

-- Tổ Ngữ văn (MaTCM = 'NGUVAN')
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV042', 'Mai Thị', 'Chung', 'Nữ', 'ĐH Ngữ Văn', 'NGUVAN', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV043', 'Nguyễn Xuân Thùy', 'Dương', 'Nữ', 'ĐH Ngữ Văn', 'NGUVAN', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV044', 'Bùi Thị Thu', 'Hiền', 'Nữ', 'ĐH Ngữ Văn', 'NGUVAN', 14, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV045', 'Chu Thị', 'Hoa', 'Nữ', 'ĐH Ngữ Văn', 'NGUVAN', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV046', 'Trần Thị Thanh', 'Huyền', 'Nữ', 'ĐH Ngữ Văn', 'NGUVAN', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV047', 'Phùng Thị', 'Liên', 'Nữ', 'ĐH Ngữ Văn', 'NGUVAN', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV048', 'Nguyễn Thị', 'Nam', 'Nữ', 'ĐH Ngữ Văn', 'NGUVAN', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV049', 'Hoàng Thị', 'Nga', 'Nữ', 'ĐH Ngữ Văn', 'NGUVAN', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV050', 'Trần Thị Thanh', 'Tâm', 'Nữ', 'ĐH Ngữ Văn', 'NGUVAN', 17, 'pass');

-- Tổ Sử - Địa - CD - QP (MaTCM = 'SUDIACDQP')
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV051', 'Lê Thị', 'Đào', 'Nữ', 'ĐH Sử', 'SUDIACDQP', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV052', 'Dương Thị', 'Hương', 'Nữ', 'ĐH Sử', 'SUDIACDQP', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV053', 'Nguyễn Thị', 'Nhung', 'Nữ', 'ĐH Sử', 'SUDIACDQP', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV054', 'Đặng Thị', 'Suốt', 'Nữ', 'ĐH Sử', 'SUDIACDQP', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV055', 'Nguyễn Quốc', 'Toản', 'Nam', 'ĐH SP Sử', 'SUDIACDQP', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV056', 'Nguyễn Thị Trúc', 'Hà', 'Nữ', 'ĐH Sử-QP', 'SUDIACDQP', 14, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV057', 'Nguyễn Thị', 'Dung', 'Nữ', 'ĐH CD', 'SUDIACDQP', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV058', 'Trần Thị', 'Biển', 'Nữ', 'ĐH Địa', 'SUDIACDQP', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV059', 'Huỳnh Thị', 'Thơ', 'Nữ', 'ĐH Địa', 'SUDIACDQP', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV060', 'Bùi Duy', 'Trọng', 'Nam', 'ĐH Địa', 'SUDIACDQP', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV061', 'Ninh Hữu', 'Ngữ', 'Nam', 'ĐH QP', 'SUDIACDQP', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV062', 'Bùi Tiến', 'Hà', 'Nam', 'ĐH tiếng Anh - QP', 'SUDIACDQP', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV063', 'Hoàng Thị Hồng', 'Lý', 'Nữ', 'ĐH NN', 'SUDIACDQP', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV064', 'Nguyễn Thị Hằng', 'Nga', 'Nữ', 'ĐH Toán', 'SUDIACDQP', 17, 'pass');

-- Tổ Ngoại ngữ (MaTCM = 'NGOAINGU')
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV065', 'Vũ Thị Thanh', 'Thảo', 'Nữ', 'ĐH tiếng Anh', 'NGOAINGU', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV066', 'Nguyễn Bảo Di', 'An', 'Nữ', 'ĐH tiếng Anh', 'NGOAINGU', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV067', 'Phạm Thị', 'Hải', 'Nữ', 'ĐH tiếng Anh', 'NGOAINGU', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV068', 'Lưu Thảo', 'Hiền', 'Nữ', 'ĐH tiếng Anh', 'NGOAINGU', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV069', 'Nguyễn Thị', 'Hiền', 'Nữ', 'ĐH tiếng Anh', 'NGOAINGU', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV070', 'Lê Thị', 'Lâm', 'Nữ', 'ĐH tiếng Anh', 'NGOAINGU', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV071', 'Lê Nữ Trà', 'Mi', 'Nữ', 'ĐH tiếng Anh', 'NGOAINGU', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV072', 'Nguyễn Công', 'Trọng', 'Nam', 'ĐH tiếng Anh', 'NGOAINGU', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV073', 'Nhâm Ngọc Tú', 'Vân', 'Nữ', 'ĐH tiếng Anh', 'NGOAINGU', 17, 'pass');
INSERT INTO GIAOVIEN (MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, MatKhau) VALUES
('GV074', 'Võ Nhật Thanh', 'Thảo', 'Nữ', 'ĐH tiếng Anh', 'NGOAINGU', 17, 'pass');

--Cập nhật lại tổ trưởng phó cho TOCHUYENMON
-- Cập nhật Tổ Toán - Tin học
UPDATE TKB.TOCHUYENMON
SET ToTruong = 'GV004',
    ToPho = 'GV011'
WHERE MaTCM = 'THT';
-- Cập nhật Tổ Lý - CNKT
UPDATE TKB.TOCHUYENMON
SET ToTruong = 'GV019',
    ToPho = NULL
WHERE MaTCM = 'LYCNKT';
-- Cập nhật Tổ Hóa - Sinh - TD
UPDATE TKB.TOCHUYENMON
SET ToTruong = 'GV028',
    ToPho = 'GV039'
WHERE MaTCM = 'HOASINHTD';
-- Cập nhật Tổ Ngữ văn
UPDATE TKB.TOCHUYENMON
SET ToTruong = 'GV042',
    ToPho = NULL
WHERE MaTCM = 'NGUVAN';
-- Cập nhật Tổ Sử - Địa - CD - QP
UPDATE TKB.TOCHUYENMON
SET ToTruong = 'GV051',
    ToPho = 'GV056'
WHERE MaTCM = 'SUDIACDQP';
-- Cập nhật Tổ Ngoại ngữ
UPDATE TKB.TOCHUYENMON
SET ToTruong = 'GV065',
    ToPho = NULL
WHERE MaTCM = 'NGOAINGU';

INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV001', 'BGH', 'HK1-24-25', 0, 'Hiệu trưởng');
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV002', 'BGH', 'HK1-24-25', 0, 'Phó hiệu trưởng');
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV003', 'BGH', 'HK1-24-25', 0, NULL);
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV001', 'BGH', 'HK2-24-25', 0, 'Hiệu trưởng');
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV002', 'BGH', 'HK2-24-25', 0, 'Phó hiệu trưởng');
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV003', 'BGH', 'HK2-24-25', 0, NULL);
-- Học kỳ áp dụng: HK1-24-25
-- Ghi chú: Số tiết cho TLHĐ (Tâm lý học đường) sẽ tạm để là 1, bạn có thể điều chỉnh.
-- Ghi chú: PM, CNTT gộp thành một MaCV là 'PMCNTT' với 6 tiết.
-- Ghi chú: P.TH (Phòng thực hành) gộp thành các MaCV 'PTHLY', 'PTHHOA', 'PTHSINH' với 7 tiết.
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV002', 'TLHD', 'HK1-24-25', 1, NULL); -- TLHĐ
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV004', 'TTCM', 'HK1-24-25', 3, NULL); -- TTCM
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV005', 'TTCĐ', 'HK1-24-25', 1, NULL); -- TTCĐ
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV005', 'TKHĐ', 'HK1-24-25', 2, NULL); -- TKHĐ
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV006', 'PBTĐ', 'HK1-24-25', 8.5, NULL); -- PBT Đoàn
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV007', 'BTĐ', 'HK1-24-25', 14.5, NULL); -- BT Đoàn TN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV008', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 12A2'); -- CN 12A2
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV010', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 10A5'); -- CN 10A5
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV011', 'TPCM', 'HK1-24-25', 1, NULL); -- TPCM
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV014', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 12A6'); -- CN 12A6
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV015', 'PM', 'HK1-24-25', 3, NULL); -- PM
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV015', 'CNTT', 'HK1-24-25', 3, NULL); -- CNTT
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV019', 'TTCM', 'HK1-24-25', 3, NULL); -- TTCM
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV020', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 11A1'); -- CN 11A1
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV021', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 10A1'); -- CN 10A1
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV022', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 10A3'); -- CN 10A3
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV022', 'TTCĐ', 'HK1-24-25', 1, NULL); -- TTCĐ
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV023', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 11A6'); -- CN 11A6
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV023', 'PTHLY', 'HK1-24-25', 7, NULL); -- P. TH Lý (MaCV='PTHLY')
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV024', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 11A8'); -- CN 11A8
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV024', 'PCTCĐ', 'HK1-24-25', 3, NULL); -- P.CTCĐ
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV025', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 10A4'); -- CN 10A4
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV026', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 10A6'); -- CN 10A6
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV027', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 10A7'); -- CN 10A7
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV028', 'TTCM', 'HK1-24-25', 3, NULL); -- TTCM
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV029', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 11A9'); -- CN 11A9
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV030', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 11A3'); -- CN 11A3
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV030', 'PTHHOA', 'HK1-24-25', 7, NULL); -- P.TH Hóa (MaCV='PTHHOA')
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV031', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 12A3'); -- CN 12A3
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV032', 'TTrND', 'HK1-24-25', 2, NULL); -- TTrND
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV034', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 12A5'); -- CN 12A5
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV034', 'TTCĐ', 'HK1-24-25', 1, NULL); -- TTCĐ
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV035', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 11A5'); -- CN 11A5
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV035', 'PTHSINH', 'HK1-24-25', 7, NULL); -- P.TH Sinh (MaCV='PTHSINH')
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV036', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 11A4'); -- CN 11A4
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV039', 'CTCĐ', 'HK1-24-25', 3, NULL); -- CTCĐ
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV039', 'TPCM', 'HK1-24-25', 1, NULL); -- TPCM
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV040', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 12A7'); -- CN 12A7
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV042', 'TTCM', 'HK1-24-25', 3, NULL); -- TTCM
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV043', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 11A11'); -- CN 11A11
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV045', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 11A10'); -- CN 11A10
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV046', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 12A8'); -- CN 12A8
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV047', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 12A9'); -- CN 12A9
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV048', 'TTCĐ', 'HK1-24-25', 1, NULL); -- TTCĐ
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV050', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 11A7'); -- CN 11A7
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV051', 'TTCM', 'HK1-24-25', 3, NULL); -- TTCM
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV053', 'TTCĐ', 'HK1-24-25', 1, NULL); -- TTCĐ
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV055', 'PBTĐ', 'HK1-24-25', 8.5, NULL); -- PBT Đoàn
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV056', 'TPCM', 'HK1-24-25', 1, NULL); -- TPCM
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV056', 'TLHD', 'HK1-24-25', 1, NULL); -- TLHĐ
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV063', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 10A8'); -- CN 10A8
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV065', 'TTCM', 'HK1-24-25', 3, NULL); -- TTCM
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV066', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 12A1'); -- CN 12A1
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV067', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 10A11'); -- CN 10A11
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV068', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 10A10'); -- CN 10A10
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV069', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 10A9'); -- CN 10A9
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV069', 'TTCĐ', 'HK1-24-25', 1, NULL); -- TTCĐ
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV070', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 12A4'); -- CN 12A4
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV071', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 12A10'); -- CN 12A10
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV072', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 11A2'); -- CN 11A2
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV073', 'CN', 'HK1-24-25', 4, 'Chủ nhiệm lớp 10A2'); -- CN 10A2
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV074', 'TLHD', 'HK1-24-25', 3, NULL);
-- Học kỳ áp dụng: HK2-24-25
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV002', 'TLHD', 'HK2-24-25', 1, NULL); -- TLHĐ (Tâm lý học đường)
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV004', 'TTCM', 'HK2-24-25', 3, NULL); -- TTCM (Tổ trưởng chuyên môn)
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV005', 'TTCĐ', 'HK2-24-25', 1, NULL); -- TTCĐ (Tổ trưởng công đoàn)
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV005', 'TKHĐ', 'HK2-24-25', 2, NULL); -- TKHĐ (Thư ký hội đồng)
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV006', 'PBTĐ', 'HK2-24-25', 8.5, NULL); -- PBT Đoàn (Phó Bí thư Đoàn)
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV007', 'BTĐ', 'HK2-24-25', 14.5, NULL); -- BT Đoàn TN (Bí thư Đoàn TN)
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV008', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 12A2'); -- CN (Chủ nhiệm lớp)
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV010', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 10A2'); -- CN (Chủ nhiệm lớp)
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV011', 'TPCM', 'HK2-24-25', 1, NULL); -- TPCM (Tổ phó chuyên môn)
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV014', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 12A6'); -- CN (Chủ nhiệm lớp)
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV015', 'PM', 'HK2-24-25', 6, NULL); -- PM (Phòng máy)
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV015', 'CNTT', 'HK2-24-25', 6, NULL); -- CNTT (Công nghệ thông tin)
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV019', 'TTCM', 'HK2-24-25', 3, NULL); -- TTCM
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV020', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 11A1'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV021', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 10A1'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV022', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 10A3'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV022', 'TTCĐ', 'HK2-24-25', 1, NULL); -- TTCĐ
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV023', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 11A6'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV023', 'PTHLY', 'HK2-24-25', 7, NULL); -- P. TH Lý
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV024', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 11A8'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV024', 'PCTCĐ', 'HK2-24-25', 3, NULL); -- P.CTCĐ (Phó Chủ tịch công đoàn)
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV025', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 10A4'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV026', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 10A6'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV027', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 10A7'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV028', 'TTCM', 'HK2-24-25', 3, NULL); -- TTCM
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV029', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 11A9'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV030', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 11A3'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV030', 'PTHHOA', 'HK2-24-25', 7, NULL); -- P.TH Hóa
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV031', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 12A3'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV032', 'TTrND', 'HK2-24-25', 2, NULL);
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV034', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 12A5'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV034', 'TTCĐ', 'HK2-24-25', 1, NULL); -- TTCĐ
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV035', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 11A5'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV035', 'PTHSINH', 'HK2-24-25', 7, NULL); -- P.TH Sinh
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV036', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 11A4'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV039', 'CTCĐ', 'HK2-24-25', 3, NULL); -- CTCĐ (Chủ tịch công đoàn)
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV039', 'TPCM', 'HK2-24-25', 1, NULL); -- TPCM
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV040', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 12A7'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV042', 'TTCM', 'HK2-24-25', 3, NULL); -- TTCM
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV043', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 11A11'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV045', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 11A10'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV046', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 12A8'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV047', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 12A9'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV048', 'TTCĐ', 'HK2-24-25', 1, NULL); -- TTCĐ
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV050', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 11A7'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV051', 'TTCM', 'HK2-24-25', 3, NULL); -- TTCM
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV053', 'TTCĐ', 'HK2-24-25', 1, NULL); -- TTCĐ
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV055', 'PBTĐ', 'HK2-24-25', 8.5, NULL); -- PBT Đoàn
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV056', 'TPCM', 'HK2-24-25', 1, NULL); -- TPCM
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV056', 'TLHD', 'HK2-24-25', 1, NULL); -- TLHĐ
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV063', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 10A8'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV065', 'TTCM', 'HK2-24-25', 3, NULL); -- TTCM
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV066', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 12A1'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV067', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 10A11'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV068', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 10A10'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV069', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 10A9'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV069', 'TTCĐ', 'HK2-24-25', 1, NULL); -- TTCĐ
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV070', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 12A4'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV071', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 12A10'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV072', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 11A2'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV073', 'CN', 'HK2-24-25', 4, 'Chủ nhiệm lớp 10A5'); -- CN
INSERT INTO GIAOVIEN_CHUCVU (MaGV, MaCV, MaHK, SoTiet, GhiChu) VALUES
('GV074', 'TLHD', 'HK2-24-25', 3, NULL); -- TLHĐ

-- Thêm dữ liệu vào bảng LOP (ở dưới là chủ nhiệm của HK2-24-25)
-- Khối 10
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('10A1', 'A1', '10', 'GV021');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('10A2', 'A2', '10', 'GV073');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('10A3', 'A3', '10', 'GV022');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('10A4', 'A4', '10', 'GV025');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('10A5', 'A5', '10', 'GV010');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('10A6', 'A6', '10', 'GV026');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('10A7', 'A7', '10', 'GV027');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('10A8', 'A8', '10', 'GV063');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('10A9', 'A9', '10', 'GV069');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('10A10', 'A10', '10', 'GV068');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('10A11', 'A11', '10', 'GV067');
-- Khối 11
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('11A1', 'A1', '11', 'GV020');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('11A2', 'A2', '11', 'GV072');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('11A3', 'A3', '11', 'GV030');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('11A4', 'A4', '11', 'GV036');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('11A5', 'A5', '11', 'GV035');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('11A6', 'A6', '11', 'GV023');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('11A7', 'A7', '11', 'GV050');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('11A8', 'A8', '11', 'GV024');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('11A9', 'A9', '11', 'GV029');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('11A10', 'A10', '11', 'GV045');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('11A11', 'A11', '11', 'GV043');
-- Khối 12
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('12A1', 'A1', '12', 'GV066');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('12A2', 'A2', '12', 'GV008');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('12A3', 'A3', '12', 'GV031');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('12A4', 'A4', '12', 'GV070');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('12A5', 'A5', '12', 'GV034');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('12A6', 'A6', '12', 'GV014');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('12A7', 'A7', '12', 'GV040');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('12A8', 'A8', '12', 'GV046');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('12A9', 'A9', '12', 'GV047');
INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES ('12A10', 'A10', '12', 'GV071');

-- Thêm dữ liệu vào bảng MONHOC
-- Khối 10
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('TOAN10', 'Toán học', '10', 'THT');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('VATLI10', 'Vật Lí', '10', 'LYCNKT');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('HOAHOC10', 'Hóa học', '10', 'HOASINHTD');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('SINHHOC10', 'Sinh học', '10', 'HOASINHTD');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('NGUVAN10', 'Ngữ Văn', '10', 'NGUVAN');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('LICHSU10', 'Lịch sử', '10', 'SUDIACDQP');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('DIALI10', 'Địa lí', '10', 'SUDIACDQP');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('TIENGANH10', 'Tiếng Anh', '10', 'NGOAINGU');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('KTEPL10', 'Kinh tế và pháp luật', '10', 'SUDIACDQP');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('CONGNGHE10', 'Công nghệ', '10', 'LYCNKT');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('GDTC10', 'Giáo dục thể chất', '10', 'HOASINHTD');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('TINHOC10', 'Tin học', '10', 'THT');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('GDQPAN10', 'Giáo dục quốc phòng và an ninh', '10', 'SUDIACDQP');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('HDTNHN10', 'Hoạt động trải nghiệm hướng nghiệp', '10', NULL);
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('GDDP10', 'Giáo dục địa phương', '10', NULL);
-- Khối 11
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('TOAN11', 'Toán học', '11', 'THT');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('VATLI11', 'Vật Lí', '11', 'LYCNKT');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('HOAHOC11', 'Hóa học', '11', 'HOASINHTD');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('SINHHOC11', 'Sinh học', '11', 'HOASINHTD');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('NGUVAN11', 'Ngữ Văn', '11', 'NGUVAN');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('LICHSU11', 'Lịch sử', '11', 'SUDIACDQP');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('DIALI11', 'Địa lí', '11', 'SUDIACDQP');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('TIENGANH11', 'Tiếng Anh', '11', 'NGOAINGU');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('KTEPL11', 'Kinh tế và pháp luật', '11', 'SUDIACDQP');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('CONGNGHE11', 'Công nghệ', '11', 'LYCNKT');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('GDTC11', 'Giáo dục thể chất', '11', 'HOASINHTD');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('TINHOC11', 'Tin học', '11', 'THT');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('GDQPAN11', 'Giáo dục quốc phòng và an ninh', '11', 'SUDIACDQP');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('HDTNHN11', 'Hoạt động trải nghiệm hướng nghiệp', '11', NULL);
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('GDDP11', 'Giáo dục địa phương', '11', NULL);
-- Khối 12
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('TOAN12', 'Toán học', '12', 'THT');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('VATLI12', 'Vật Lí', '12', 'LYCNKT');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('HOAHOC12', 'Hóa học', '12', 'HOASINHTD');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('SINHHOC12', 'Sinh học', '12', 'HOASINHTD');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('NGUVAN12', 'Ngữ Văn', '12', 'NGUVAN');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('LICHSU12', 'Lịch sử', '12', 'SUDIACDQP');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('DIALI12', 'Địa lí', '12', 'SUDIACDQP');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('TIENGANH12', 'Tiếng Anh', '12', 'NGOAINGU');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('KTEPL12', 'Kinh tế và pháp luật', '12', 'SUDIACDQP');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('CONGNGHE12', 'Công nghệ', '12', 'LYCNKT');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('GDTC12', 'Giáo dục thể chất', '12', 'HOASINHTD');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('TINHOC12', 'Tin học', '12', 'THT');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('GDQPAN12', 'Giáo dục quốc phòng và an ninh', '12', 'SUDIACDQP');
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('HDTNHN12', 'Hoạt động trải nghiệm hướng nghiệp', '12', NULL);
INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES ('GDDP12', 'Giáo dục địa phương', '12', NULL);


INSERT INTO THOIKHOABIEU (MaTKB, NgayLap, NgayApDung, Buoi, NguoiTao, MaHK)
VALUES ('TKB01', TO_DATE('01-01-2025', 'DD-MM-YYYY'), TO_DATE('13-01-2025', 'DD-MM-YYYY'), 'Sáng', 'ADMIN','HK1-24-25');

--Thêm dữ liệu vào CHITIETTKB
--Khối 11
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 1, '11A1', 'GV016', 'TINHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 1, '11A2', 'GV029', 'HOAHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 1, '11A3', 'GV030', 'HOAHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 1, '11A4', 'GV037', 'GDTC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 1, '11A5', 'GV041', 'GDTC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 1, '11A6', 'GV070', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 1, '11A7', 'GV017', 'TINHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 1, '11A8', 'GV060', 'DIALI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 1, '11A9', 'GV069', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 1, '11A10', 'GV051', 'LICHSU11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 1, '11A11', 'GV005', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 2, '11A1', 'GV020', 'VATLI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 2, '11A2', 'GV072', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 2, '11A3', 'GV037', 'GDTC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 2, '11A4', 'GV025', 'VATLI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 2, '11A5', 'GV071', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 2, '11A6', 'GV017', 'TINHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 2, '11A7', 'GV051', 'LICHSU11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 2, '11A8', 'GV057', 'KTEPL11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 2, '11A9', 'GV029', 'HOAHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 2, '11A10', 'GV040', 'GDQPAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 2, '11A11', 'GV005', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 3, '11A1', 'GV029', 'HOAHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 3, '11A2', 'GV037', 'GDTC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 3, '11A3', 'GV025', 'VATLI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 3, '11A4', 'GV036', 'SINHHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 3, '11A5', 'GV035', 'SINHHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 3, '11A6', 'GV066', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 3, '11A7', 'GV040', 'GDQPAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 3, '11A8', 'GV051', 'LICHSU11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 3, '11A9', 'GV009', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 3, '11A10', 'GV020', 'CONGNGHE11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 3, '11A11', 'GV060', 'DIALI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '11A1', 'GV002', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '11A2', 'GV020', 'VATLI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '11A3', 'GV072', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '11A4', 'GV071', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '11A5', 'GV030', 'HOAHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '11A6', 'GV057', 'KTEPL11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '11A7', 'GV070', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '11A8', 'GV069', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '11A9', 'GV009', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '11A10', 'GV074', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '11A11', 'GV038', 'GDTC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 1, '11A1', 'GV002', 'TIENGANH11'); --THIS
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 1, '11A2', 'GV029', 'HOAHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 1, '11A3', 'GV037', 'GDTC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 1, '11A4', 'GV053', 'LICHSU11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 1, '11A5', 'GV006', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 1, '11A6', 'GV023', 'VATLI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 1, '11A7', 'GV012', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 1, '11A8', 'GV057', 'KTEPL11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 1, '11A9', 'GV043', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 1, '11A10', 'GV074', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 1, '11A11', 'GV017', 'TINHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 2, '11A1', 'GV029', 'HOAHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 2, '11A2', 'GV013', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 2, '11A3', 'GV072', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 2, '11A4', 'GV036', 'SINHHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 2, '11A5', 'GV030', 'HOAHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 2, '11A6', 'GV053', 'LICHSU11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 2, '11A7', 'GV041', 'GDTC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 2, '11A8', 'GV038', 'GDTC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 2, '11A9', 'GV032', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 2, '11A10', 'GV060', 'DIALI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 2, '11A11', 'GV043', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 3, '11A1', 'GV044', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 3, '11A2', 'GV013', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 3, '11A3', 'GV030', 'HOAHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 3, '11A4', 'GV037', 'GDTC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 3, '11A5', 'GV041', 'GDTC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 3, '11A6', 'GV048', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 3, '11A7', 'GV057', 'KTEPL11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 3, '11A8', 'GV032', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 3, '11A9', 'GV060', 'DIALI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 3, '11A10', 'GV017', 'TINHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 3, '11A11', 'GV038', 'GDTC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 4, '11A1', 'GV053', 'LICHSU11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 4, '11A2', 'GV072', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 4, '11A3', 'GV044', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 4, '11A4', 'GV025', 'VATLI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 4, '11A5', 'GV059', 'DIALI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 4, '11A6', 'GV017', 'TINHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 4, '11A7', 'GV060', 'DIALI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 4, '11A8', 'GV012', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 4, '11A9', 'GV029', 'HOAHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 4, '11A10', 'GV038', 'GDTC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 4, '11A11', 'GV032', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 5, '11A1', 'GV062', 'GDQPAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 5, '11A2', 'GV053', 'LICHSU11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 5, '11A3', 'GV044', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 5, '11A4', 'GV057', 'KTEPL11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 5, '11A5', 'GV035', 'SINHHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 5, '11A6', 'GV059', 'DIALI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 5, '11A7', 'GV050', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 5, '11A8', 'GV012', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 5, '11A9', 'GV017', 'TINHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 5, '11A10', 'GV032', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 5, '11A11', 'GV074', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 1, '11A1', 'GV002', 'TIENGANH11'); --THIS
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 1, '11A2', 'GV033', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 1, '11A3', 'GV010', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 1, '11A4', 'GV016', 'TINHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 1, '11A5', 'GV030', 'HOAHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 1, '11A6', 'GV013', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 1, '11A7', 'GV070', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 1, '11A8', 'GV060', 'DIALI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 1, '11A9', 'GV029', 'HOAHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 1, '11A10', 'GV032', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 1, '11A11', 'GV074', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 2, '11A1', 'GV037', 'GDTC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 2, '11A2', 'GV013', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 2, '11A3', 'GV033', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 2, '11A4', 'GV010', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 2, '11A5', 'GV044', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 2, '11A6', 'GV070', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 2, '11A7', 'GV041', 'GDTC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 2, '11A8', 'GV012', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 2, '11A9', 'GV032', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 2, '11A10', 'GV038', 'GDTC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 2, '11A11', 'GV060', 'DIALI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 3, '11A1', 'GV033', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 3, '11A2', 'GV016', 'TINHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 3, '11A3', 'GV025', 'VATLI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 3, '11A4', 'GV010', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 3, '11A5', 'GV044', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 3, '11A6', 'GV041', 'GDTC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 3, '11A7', 'GV032', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 3, '11A8', 'GV048', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 3, '11A9', 'GV069', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 3, '11A10', 'GV060', 'DIALI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 3, '11A11', 'GV051', 'LICHSU11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 4, '11A1', 'GV008', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 4, '11A2', 'GV044', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 4, '11A3', 'GV030', 'HOAHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 4, '11A4', 'GV025', 'VATLI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 4, '11A5', 'GV006', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 4, '11A6', 'GV023', 'VATLI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 4, '11A7', 'GV050', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 4, '11A8', 'GV048', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 4, '11A9', 'GV038', 'GDTC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 4, '11A10', 'GV051', 'LICHSU11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 4, '11A11', 'GV032', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 5, '11A1', 'GV008', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 5, '11A2', 'GV044', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 5, '11A3', 'GV059', 'DIALI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 5, '11A4', 'GV071', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 5, '11A5', 'GV006', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 5, '11A6', 'GV048', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 5, '11A7', 'GV050', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 5, '11A8', 'GV069', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 5, '11A9', 'GV051', 'LICHSU11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 5, '11A10', 'GV009', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 5, '11A11', 'GV005', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 1, '11A1', 'GV016', 'TINHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 1, '11A2', 'GV020', 'VATLI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 1, '11A3', 'GV053', 'LICHSU11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 1, '11A4', 'GV040', 'GDQPAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 1, '11A5', 'GV071', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 1, '11A6', 'GV041', 'GDTC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 1, '11A7', 'GV057', 'KTEPL11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 1, '11A8', 'GV069', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 1, '11A9', 'GV051', 'LICHSU11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 1, '11A10', 'GV074', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 1, '11A11', 'GV043', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 2, '11A1', 'GV033', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 2, '11A2', 'GV037', 'GDTC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 2, '11A3', 'GV044', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 2, '11A4', 'GV016', 'TINHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 2, '11A5', 'GV053', 'LICHSU11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 2, '11A6', 'GV013', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 2, '11A7', 'GV012', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 2, '11A8', 'GV040', 'GDQPAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 2, '11A9', 'GV052', 'KTEPL11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 2, '11A10', 'GV020', 'CONGNGHE11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 2, '11A11', 'GV051', 'LICHSU11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 3, '11A1', 'GV020', 'VATLI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 3, '11A2', 'GV057', 'KTEPL11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 3, '11A3', 'GV016', 'TINHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 3, '11A4', 'GV044', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 3, '11A5', 'GV033', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 3, '11A6', 'GV066', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 3, '11A7', 'GV024', 'VATLI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 3, '11A8', 'GV032', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 3, '11A9', 'GV069', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 3, '11A10', 'GV009', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 3, '11A11', 'GV074', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '11A1', 'GV044', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '11A2', 'GV033', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '11A3', 'GV040', 'GDQPAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '11A4', 'GV071', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '11A5', 'GV006', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '11A6', 'GV023', 'VATLI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '11A7', 'GV050', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '11A8', 'GV024', 'CONGNGHE11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '11A9', 'GV032', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '11A10', 'GV052', 'KTEPL11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '11A11', 'GV020', 'CONGNGHE11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 5, '11A1', 'GV044', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 5, '11A2', 'GV013', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 5, '11A3', 'GV010', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 5, '11A4', 'GV033', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 5, '11A5', 'GV018', 'TINHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 5, '11A6', 'GV057', 'KTEPL11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 5, '11A7', 'GV032', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 5, '11A8', 'GV024', 'VATLI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 5, '11A9', 'GV043', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 5, '11A10', 'GV051', 'LICHSU11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 5, '11A11', 'GV040', 'GDQPAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 1, '11A1', 'GV029', 'HOAHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 1, '11A2', 'GV016', 'TINHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 1, '11A3', 'GV053', 'LICHSU11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 1, '11A4', 'GV033', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 1, '11A5', 'GV018', 'TINHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 1, '11A6', 'GV048', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 1, '11A7', 'GV060', 'DIALI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 1, '11A8', 'GV032', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 1, '11A9', 'GV009', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 1, '11A10', 'GV052', 'KTEPL11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 1, '11A11', 'GV017', 'TINHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 2, '11A1', 'GV036', 'SINHHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 2, '11A2', 'GV029', 'HOAHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 2, '11A3', 'GV033', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 2, '11A4', 'GV010', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 2, '11A5', 'GV044', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 2, '11A6', 'GV048', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 2, '11A7', 'GV017', 'TINHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 2, '11A8', 'GV038', 'GDTC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 2, '11A9', 'GV009', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 2, '11A10', 'GV032', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 2, '11A11', 'GV051', 'LICHSU11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 3, '11A1', 'GV053', 'LICHSU11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 3, '11A2', 'GV044', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 3, '11A3', 'GV016', 'TINHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 3, '11A4', 'GV010', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 3, '11A5', 'GV033', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 3, '11A6', 'GV059', 'DIALI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 3, '11A7', 'GV024', 'VATLI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 3, '11A8', 'GV051', 'LICHSU11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 3, '11A9', 'GV060', 'DIALI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 3, '11A10', 'GV017', 'TINHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 3, '11A11', 'GV052', 'KTEPL11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 4, '11A1', 'GV008', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 4, '11A2', 'GV072', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 4, '11A3', 'GV025', 'VATLI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 4, '11A4', 'GV044', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 4, '11A5', 'GV053', 'LICHSU11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 4, '11A6', 'GV013', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 4, '11A7', 'GV032', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 4, '11A8', 'GV024', 'CONGNGHE11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 4, '11A9', 'GV038', 'GDTC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 4, '11A10', 'GV045', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 4, '11A11', 'GV005', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 5, '11A1', 'GV033', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 5, '11A2', 'GV053', 'LICHSU11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 5, '11A3', 'GV072', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 5, '11A4', 'GV044', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 5, '11A5', 'GV059', 'DIALI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 5, '11A6', 'GV013', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 5, '11A7', 'GV051', 'LICHSU11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 5, '11A8', 'GV024', 'VATLI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 5, '11A9', 'GV017', 'TINHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 5, '11A10', 'GV045', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 5, '11A11', 'GV032', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 1, '11A1', 'GV036', 'SINHHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 1, '11A2', 'GV062', 'GDQPAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 1, '11A3', 'GV059', 'DIALI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 1, '11A4', 'GV053', 'LICHSU11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 1, '11A5', 'GV033', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 1, '11A6', 'GV066', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 1, '11A7', 'GV012', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 1, '11A8', 'GV048', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 1, '11A9', 'GV043', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 1, '11A10', 'GV045', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 1, '11A11', 'GV052', 'KTEPL11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 2, '11A1', 'GV008', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 2, '11A2', 'GV057', 'KTEPL11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 2, '11A3', 'GV033', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 2, '11A4', 'GV036', 'SINHHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 2, '11A5', 'GV040', 'GDQPAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 2, '11A6', 'GV053', 'LICHSU11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 2, '11A7', 'GV012', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 2, '11A8', 'GV048', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 2, '11A9', 'GV043', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 2, '11A10', 'GV045', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 2, '11A11', 'GV020', 'CONGNGHE11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 3, '11A1', 'GV037', 'GDTC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 3, '11A2', 'GV020', 'VATLI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 3, '11A3', 'GV010', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 3, '11A4', 'GV033', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 3, '11A5', 'GV071', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 3, '11A6', 'GV070', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 3, '11A7', 'GV024', 'VATLI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 3, '11A8', 'GV012', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 3, '11A9', 'GV040', 'GDQPAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 3, '11A10', 'GV009', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 3, '11A11', 'GV043', 'NGUVAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 4, '11A1', 'GV020', 'VATLI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 4, '11A2', 'GV033', 'HDTNHN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 4, '11A3', 'GV010', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 4, '11A4', 'GV057', 'KTEPL11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 4, '11A5', 'GV035', 'SINHHOC11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 4, '11A6', 'GV040', 'GDQPAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 4, '11A7', 'GV070', 'TIENGANH11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 4, '11A8', 'GV024', 'VATLI11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 4, '11A9', 'GV052', 'KTEPL11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 4, '11A10', 'GV009', 'TOAN11');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 4, '11A11', 'GV043', 'NGUVAN11');


---CHITIETTKB
--12A1
--12A1-Thu 2
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH) 
VALUES('TKB01', 2, 1, '12A1', 'GV039', 'GDTC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 2, '12A1', 'GV054', 'LICHSU12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 3, '12A1', 'GV016', 'TINHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 4, '12A1', 'GV066', 'TIENGANH12');

--12A1 Thu3
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 1, '12A1', 'GV061', 'GDQPAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 2, '12A1', 'GV028', 'HOAHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 3, '12A1', 'GV008', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 4, '12A1', 'GV008', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 5, '12A1', 'GV036', 'SINHHOC12');
--12A1 Thu 4
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 1, '12A1', 'GV046', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 2, '12A1', 'GV046', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 3, '12A1', 'GV019', 'VATLI12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 4, '12A1', 'GV016', 'TINHOC12');

--12A1 Thu 5
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 1, '12A1', 'GV066', 'TIENGANH12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 2, '12A1', 'GV062', 'HDTNHN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 3, '12A1', 'GV039', 'GDTC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 4, '12A1', 'GV019', 'VATLI12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 5, '12A1', 'GV046', 'NGUVAN12');
--12A1 Thu 6
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 1, '12A1', 'GV062', 'HDTNHN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 2, '12A1', 'GV028', 'HOAHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 3, '12A1', 'GV036', 'SINHHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 4, '12A1', 'GV019', 'VATLI12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 5, '12A1', 'GV008', 'TOAN12');

--12A1 Thu 7
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 7, 1, '12A1', 'GV008', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 7, 2, '12A1', 'GV062', 'HDTNHN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 7, 3, '12A1', 'GV066', 'TIENGANH12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 7, 4, '12A1', 'GV028', 'HOAHOC12');

--12A2
--12A2 Thu 2
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 1, '12A2', 'GV042', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 2, '12A2', 'GV066', 'TIENGANH12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 3, '12A2', 'GV008', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 4, '12A2', 'GV008', 'TOAN12');
--12A2 Thu 3
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 1, '12A2', 'GV062', 'HDTNHN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 2, '12A2', 'GV039', 'GDTC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 3, '12A2', 'GV028', 'HOAHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 4, '12A2', 'GV054', 'LICHSU12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 5, '12A2', 'GV008', 'TOAN12');
--12A2 Thu 4
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 1, '12A2', 'GV066', 'TIENGANH12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 2, '12A2', 'GV052', 'KTEPL12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 3, '12A2', 'GV008', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 4, '12A2', 'GV019', 'VATLI12');

--12A2 Thu 5
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 1, '12A2', 'GV028', 'HOAHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 2, '12A2', 'GV039', 'GDTC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 3, '12A2', 'GV052', 'KTEPL12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 4, '12A2', 'GV016', 'TINHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 5, '12A2', 'GV062', 'HDTNHN12');
--12A2 Thu 6
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 1, '12A2', 'GV019', 'VATLI12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 2, '12A2', 'GV042', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 3, '12A2', 'GV042', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 4, '12A2', 'GV028', 'HOAHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 5, '12A2', 'GV016', 'TINHOC12');

--12A2 Thu 7
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 7, 1, '12A2', 'GV061', 'GDQPAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 7, 2, '12A2', 'GV066', 'TIENGANH12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 7, 3, '12A2', 'GV019', 'VATLI12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 7, 4, '12A2', 'GV062', 'HDTNHN12');

--12A3
--12A3 Thu 2
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 1, '12A3', 'GV054', 'LICHSU12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 2, '12A3', 'GV031', 'HOAHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 3, '12A3', 'GV039', 'GDTC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 4, '12A3', 'GV016', 'TINHOC12');

--12A3 Thu 3
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 1, '12A3', 'GV060', 'DIALI12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 2, '12A3', 'GV062', 'HDTNHN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 3, '12A3', 'GV039', 'GDTC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 4, '12A3', 'GV070', 'TIENGANH12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 5, '12A3', 'GV031', 'HOAHOC12');

--12A2 thu 4
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 1, '12A3', 'GV022', 'VATLI12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 2, '12A3', 'GV016', 'TINHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 3, '12A3', 'GV004', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 4, '12A3', 'GV070', 'TIENGANH12');
--12A3 Thu 5
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 1, '12A3', 'GV004', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 2, '12A3', 'GV047', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 3, '12A3', 'GV047', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 4, '12A3', 'GV031', 'HOAHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 5, '12A3', 'GV022', 'VATLI12');

--12A3 Thu 6
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 1, '12A3', 'GV004', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 2, '12A3', 'GV004', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 3, '12A3', 'GV062', 'HDTNHN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 4, '12A3', 'GV060', 'DIALI12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 5, '12A3', 'GV047', 'NGUVAN12');

--12A3 Thu 7
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 7, 1, '12A3', 'GV022', 'VATLI12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 7, 2, '12A3', 'GV070', 'TIENGANH12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 7, 3, '12A3', 'GV062', 'HDTNHN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 7, 4, '12A3', 'GV061', 'GDQPAN12');

--12A4
--12A4 Thu 2
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 1, '12A4', 'GV064', 'HDTNHN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 2, '12A4', 'GV016', 'TINHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 3, '12A4', 'GV070', 'TIENGANH12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 4, '12A4', 'GV061', 'GDQPAN12');
--12A4 Thu 3
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 1, '12A4', 'GV034', 'SINHHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 2, '12A4', 'GV004', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 3, '12A4', 'GV004', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 4, '12A4', 'GV039', 'GDTC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 5, '12A4', 'GV070', 'TIENGANH12');
--12A5 Thu 4
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 1, '12A4', 'GV052', 'KTEPL12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 2, '12A4', 'GV022', 'VATLI12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 3, '12A4', 'GV045', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 4, '12A4', 'GV045', 'NGUVAN12');
--12A4 Thu 5
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 1, '12A4', 'GV039', 'GDTC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 2, '12A4', 'GV004', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 3, '12A4', 'GV034', 'SINHHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 4, '12A4', 'GV022', 'VATLI12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 5, '12A4', 'GV064', 'HDTNHN12');
--12A5 Thu 6
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 1, '12A4', 'GV034', 'SINHHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 2, '12A4', 'GV052', 'KTEPL12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 3, '12A4', 'GV004', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 4, '12A4', 'GV016', 'TINHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 5, '12A4', 'GV052', 'LICHSU12');
--12A4 Thu 7
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 7, 1, '12A4', 'GV070', 'TIENGANH12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 7, 2, '12A4', 'GV022', 'VATLI12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 7, 3, '12A4', 'GV045', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 7, 4, '12A4', 'GV064', 'HDTNHN12');

--12A5
--12A5 Thu 2
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 1, '12A5', 'GV038', 'GDTC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 2, '12A5', 'GV060', 'DIALI12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 3, '12A5', 'GV067', 'TIENGANH12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 4, '12A5', 'GV067', 'HDTNHN12');
--12A5 Thu 3
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 1, '12A5', 'GV031', 'HOAHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 2, '12A5', 'GV034', 'SINHHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 3, '12A5', 'GV050', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 4, '12A5', 'GV050', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 5, '12A5', 'GV015', 'TINHOC12');
--12A5 Thu 4 
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 1, '12A5', 'GV015', 'TINHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 2, '12A5', 'GV031', 'HOAHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 3, '12A5', 'GV014', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 4, '12A5', 'GV060', 'DIALI12');

--12A5 Thu 5
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 1, '12A5', 'GV014', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 2, '12A5', 'GV061', 'GDQPAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 3, '12A5', 'GV031', 'HOAHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 4, '12A5', 'GV067', 'HDTNHN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 5, '12A5', 'GV067', 'TIENGANH12');

--12A5 Thu 6
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 1, '12A5', 'GV038', 'GDTC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 2, '12A5', 'GV050', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 3, '12A5', 'GV034', 'SINHHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 4, '12A5', 'GV067', 'HDTNHN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 5, '12A5', 'GV067', 'TIENGANH12');
---12A5 Thu 7
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 7, 1, '12A5', 'GV034', 'SINHHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 7, 2, '12A5', 'GV052', 'LICHSU12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 7, 3, '12A5', 'GV014', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 7, 4, '12A5', 'GV014', 'TOAN12');

--12A6
--12A6 Thu 2
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 1, '12A6', 'GV067', 'TIENGANH12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 2, '12A6', 'GV038', 'GDTC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 3, '12A6', 'GV064', 'HDTNHN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 4, '12A6', 'GV060', 'DIALI12');
--12A6 Thu 3
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 1, '12A6', 'GV038', 'GDTC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 2, '12A6', 'GV021', 'VATLI12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 3, '12A6', 'GV015', 'TINHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 4, '12A6', 'GV045', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 5, '12A6', 'GV045', 'NGUVAN12');
--12A6 Thu 4
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 1, '12A6', 'GV014', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 2, '12A6', 'GV014', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 3, '12A6', 'GV061', 'GDQPAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 4, '12A6', 'GV067', 'TIENGANH12');
--12A6 Thu 5
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 1, '12A6', 'GV052', 'KTEPL12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 2, '12A6', 'GV014', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 3, '12A6', 'GV067', 'TIENGANH12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 4, '12A6', 'GV064', 'HDTNHN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 5, 5, '12A6', 'GV021', 'VATLI12');
--12A6 Thu 6 
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 1, '12A6', 'GV045', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 2, '12A6', 'GV045', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 3, '12A6', 'GV015', 'TINHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 4, '12A6', 'GV052', 'KTEPL12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 6, 5, '12A6', 'GV060', 'DIALI12');
--12A6 Thu 7
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 7, 1, '12A6', 'GV064', 'HDTNHN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 7, 2, '12A6', 'GV014', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 7, 3, '12A6', 'GV021', 'VATLI12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 7, 4, '12A6', 'GV054', 'LICHSU12');

--12A7
--12A7 Thu 2
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 1, '12A7', 'GV040', 'GDTC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 2, '12A7', 'GV068', 'HDTNHN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 3, '12A7', 'GV057', 'KTEPL12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 2, 4, '12A7', 'GV068', 'TIENGANH12');
--12A7 Thu 3
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 1, '12A7', 'GV068', 'HDTNHN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 2, '12A7', 'GV057', 'KTEPL12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 3, '12A7', 'GV021', 'CONGNGHE12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 4, '12A7', 'GV021', 'VATLI12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 3, 5, '12A7', 'GV005', 'TOAN12');
--12A7 Thu 4
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 1, '12A7', 'GV054', 'LICHSU12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 2, '12A7', 'GV040', 'GDTC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 3, '12A7', 'GV050', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01', 4, 4, '12A7', 'GV005', 'TOAN12');
--12A7 Thu 5
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',5, 1, '12A7', 'GV050', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',5, 2, '12A7', 'GV050', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',5, 3, '12A7', 'GV068', 'TIENGANH12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',5, 4, '12A7', 'GV068', 'HDTNHN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',5, 5, '12A7', 'GV058', 'DIALI12');
--12A7 Thu 6
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',6, 1, '12A7', 'GV050', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',6, 2, '12A7', 'GV058', 'DIALI12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',6, 3, '12A7', 'GV068', 'TIENGANH12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',6, 4, '12A7', 'GV021', 'VATLI12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',6, 5, '12A7', 'GV021', 'CONGNGHE12');
--12A7 Thu 7 
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',7, 1, '12A7', 'GV005', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',7, 2, '12A7', 'GV005', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',7, 3, '12A7', 'GV061', 'GDQPAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',7, 4, '12A7', 'GV021', 'VATLI12');

--12A8
--12A8 Thu 2
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',2, 1, '12A8', 'GV068', 'TIENGANH12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',2, 2, '12A8', 'GV058', 'DIALI12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',2, 3, '12A8', 'GV038', 'GDTC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',2, 4, '12A8', 'GV046', 'NGUVAN12');
--12A8 Thu 3
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',3, 1, '12A8', 'GV015', 'TINHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',3, 2, '12A8', 'GV061', 'GDQPAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',3, 3, '12A8', 'GV054', 'LICHSU12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',3, 4, '12A8', 'GV005', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',3, 5, '12A8', 'GV028', 'HOAHOC12');
--12A8 Thu 4
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',4, 1, '12A8', 'GV038', 'GDTC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',4, 2, '12A8', 'GV064', 'HDTNHN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',4, 3, '12A8', 'GV052', 'KTEPL12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',4, 4, '12A8', 'GV015', 'TINHOC12');
--12A8 Thu 5
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',5, 1, '12A8', 'GV068', 'TIENGANH12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',5, 2, '12A8', 'GV064', 'HDTNHN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',5, 3, '12A8', 'GV028', 'HOAHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',5, 4, '12A8', 'GV046', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',5, 5, '12A8', 'GV052', 'KTEPL12');
--12A8 Thu 6 
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',6, 1, '12A8', 'GV005', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',6, 2, '12A8', 'GV005', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',6, 3, '12A8', 'GV028', 'HOAHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',6, 4, '12A8', 'GV068', 'TIENGANH12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',6, 5, '12A8', 'GV058', 'DIALI12');
--12A8 Thu 7
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',7, 1, '12A8', 'GV046', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',7, 2, '12A8', 'GV046', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',7, 3, '12A8', 'GV064', 'HDTNHN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',7, 4, '12A8', 'GV005', 'TOAN12');

--12A9
--12A9 Thu 2
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',2, 1, '12A9', 'GV025', 'CONGNGHE12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',2, 2, '12A9', 'GV064', 'HDTNHN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',2, 3, '12A9', 'GV061', 'GDQPAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',2, 4, '12A9', 'GV058', 'DIALI12');
--12A9 Thu 3
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',3, 1, '12A9', 'GV047', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',3, 2, '12A9', 'GV047', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',3, 3, '12A9', 'GV011', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',3, 4, '12A9', 'GV011', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',3, 5, '12A9', 'GV054', 'LICHSU12');
--12A9 Thu 4
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',4, 1, '12A9', 'GV064', 'HDTNHN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',4, 2, '12A9', 'GV015', 'TINHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',4, 3, '12A9', 'GV038', 'GDTC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',4, 4, '12A9', 'GV071', 'TIENGANH12');
--12A9 Thu 5
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',5, 1, '12A9', 'GV064', 'HDTNHN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',5, 2, '12A9', 'GV058', 'DIALI12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',5, 3, '12A9', 'GV011', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',5, 4, '12A9', 'GV057', 'KTEPL12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',5, 5, '12A9', 'GV071', 'TIENGANH12');
--12A9 Thu 6
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',6, 1, '12A9', 'GV015', 'TINHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',6, 2, '12A9', 'GV054', 'LICHSU12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',6, 3, '12A9', 'GV038', 'GDTC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',6, 4, '12A9', 'GV011', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',6, 5, '12A9', 'GV025', 'CONGNGHE12');
--12A9 Thu 7
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',7, 1, '12A9', 'GV057', 'KTEPL12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',7, 2, '12A9', 'GV071', 'TIENGANH12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',7, 3, '12A9', 'GV047', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',7, 4, '12A9', 'GV047', 'NGUVAN12');

--12A10
--12A10 Thu 2
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',2, 1, '12A10', 'GV057', 'KTEPL12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',2, 2, '12A10', 'GV042', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',2, 3, '12A10', 'GV071', 'TIENGANH12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',2, 4, '12A10', 'GV064', 'HDTNHN12');
--12A10 Thu 3
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',3, 1, '12A10', 'GV058', 'DIALI12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',3, 2, '12A10', 'GV015', 'TINHOC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',3, 3, '12A10', 'GV023', 'CONGNGHE12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',3, 4, '12A10', 'GV042', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',3, 5, '12A10', 'GV042', 'NGUVAN12');
--12A10 Thu 4
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',4, 1, '12A10', 'GV040', 'GDTC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',4, 2, '12A10', 'GV023', 'CONGNGHE12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',4, 3, '12A10', 'GV064', 'HDTNHN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',4, 4, '12A10', 'GV011', 'TOAN12');

--12A10 Thu 5
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',5, 1, '12A10', 'GV058', 'DIALI12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',5, 2, '12A10', 'GV071', 'TIENGANH12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',5, 3, '12A10', 'GV051', 'LICHSU12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',5, 4, '12A10', 'GV011', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',5, 5, '12A10', 'GV061', 'GDQPAN12');
--12A10 Thu 6
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',6, 1, '12A10', 'GV051', 'LICHSU12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',6, 2, '12A10', 'GV011', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',6, 3, '12A10', 'GV011', 'TOAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',6, 4, '12A10', 'GV042', 'NGUVAN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',6, 5, '12A10', 'GV015', 'TINHOC12');
--12A10 Thu 7
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',7, 1, '12A10', 'GV040', 'GDTC12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',7, 2, '12A10', 'GV064', 'HDTNHN12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',7, 3, '12A10', 'GV057', 'KTEPL12');
INSERT INTO TKB.CHITIETTKB(MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES('TKB01',7, 4, '12A10', 'GV071', 'TIENGANH12');

--Khối 10
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 1, '10A1', 'GV004', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 1, '10A2', 'GV009', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 1, '10A3', 'GV074', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 1, '10A4', 'GV035', 'SINHHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 1, '10A5', 'GV031', 'HOAHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 1, '10A7', 'GV061', 'GDQPAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 1, '10A8', 'GV066', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 1, '10A9', 'GV073', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 1, '10A10', 'GV043', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 1, '10A11', 'GV058', 'DIALI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 2, '10A1', 'GV004', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 2, '10A2', 'GV039', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 2, '10A2', 'GV041', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 2, '10A3', 'GV030', 'HOAHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 2, '10A4', 'GV039', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 2, '10A4', 'GV041', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 2, '10A5', 'GV036', 'SINHHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 2, '10A6', 'GV061', 'GDQPAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 2, '10A7', 'GV073', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 2, '10A8', 'GV046', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 2, '10A9', 'GV069', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 2, '10A10', 'GV043', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 2, '10A11', 'GV067', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 3, '10A1', 'GV074', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 3, '10A2', 'GV073', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 3, '10A3', 'GV004', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 3, '10A4', 'GV065', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 3, '10A5', 'GV041', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 3, '10A6', 'GV011', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 3, '10A7', 'GV027', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 3, '10A8', 'GV046', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 3, '10A9', 'GV058', 'DIALI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 3, '10A10', 'GV012', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 3, '10A11', 'GV054', 'LICHSU10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '10A1', 'GV073', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '10A2', 'GV074', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '10A3', 'GV037', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '10A3', 'GV039', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '10A3', 'GV041', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '10A4', 'GV011', 'VATLI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '10A5', 'GV054', 'LICHSU10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '10A6', 'GV011', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '10A7', 'GV027', 'VATLI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '10A8', 'GV037', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '10A8', 'GV039', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '10A8', 'GV041', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '10A9', 'GV036', 'SINHHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '10A10', 'GV012', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '10A11', 'GV037', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '10A11', 'GV039', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 2, 4, '10A11', 'GV041', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 1, '10A1', 'GV048', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 1, '10A2', 'GV039', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 1, '10A2', 'GV041', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 1, '10A3', 'GV030', 'HOAHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 1, '10A4', 'GV039', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 1, '10A4', 'GV041', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 1, '10A5', 'GV036', 'SINHHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 1, '10A6', 'GV026', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 1, '10A7', 'GV011', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 1, '10A8', 'GV014', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 1, '10A9', 'GV056', 'KTEPL10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 1, '10A10', 'GV063', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 1, '10A11', 'GV042', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 2, '10A1', 'GV048', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 2, '10A2', 'GV023', 'VATLI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 2, '10A3', 'GV059', 'DIALI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 2, '10A4', 'GV063', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 2, '10A5', 'GV031', 'HOAHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 2, '10A6', 'GV026', 'VATLI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 2, '10A7', 'GV011', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 2, '10A8', 'GV014', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 2, '10A9', 'GV058', 'DIALI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 2, '10A11', 'GV042', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 3, '10A1', 'GV062', 'GDQPAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 3, '10A2', 'GV012', 'TINHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 3, '10A3', 'GV074', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 3, '10A4', 'GV065', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 3, '10A5', 'GV063', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 3, '10A6', 'GV056', 'KTEPL10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 3, '10A7', 'GV059', 'DIALI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 3, '10A8', 'GV031', 'HOAHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 3, '10A9', 'GV036', 'SINHHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 3, '10A10', 'GV058', 'DIALI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 3, '10A11', 'GV006', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 4, '10A1', 'GV073', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 4, '10A2', 'GV074', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 4, '10A3', 'GV004', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 4, '10A4', 'GV035', 'SINHHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 4, '10A5', 'GV065', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 4, '10A6', 'GV037', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 4, '10A6', 'GV041', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 4, '10A7', 'GV037', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 4, '10A7', 'GV041', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 4, '10A8', 'GV058', 'DIALI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 4, '10A9', 'GV013', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 4, '10A10', 'GV026', 'CONGNGHE10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 4, '10A11', 'GV056', 'KTEPL10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 5, '10A1', 'GV021', 'VATLI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 5, '10A2', 'GV029', 'HOAHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 5, '10A3', 'GV004', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 5, '10A4', 'GV011', 'VATLI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 5, '10A5', 'GV013', 'TINHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 5, '10A7', 'GV073', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 5, '10A8', 'GV063', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 5, '10A9', 'GV048', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 5, '10A10', 'GV056', 'KTEPL10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 3, 5, '10A11', 'GV026', 'CONGNGHE10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 1, '10A1', 'GV048', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 1, '10A2', 'GV023', 'VATLI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 1, '10A3', 'GV012', 'TINHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 1, '10A4', 'GV063', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 1, '10A5', 'GV041', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 1, '10A6', 'GV018', 'TINHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 1, '10A7', 'GV011', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 1, '10A8', 'GV031', 'HOAHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 1, '10A9', 'GV027', 'CONGNGHE10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 1, '10A10', 'GV037', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 1, '10A11', 'GV061', 'GDQPAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 2, '10A1', 'GV004', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 2, '10A2', 'GV029', 'HOAHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 2, '10A3', 'GV030', 'HOAHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 2, '10A4', 'GV011', 'VATLI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 2, '10A5', 'GV065', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 2, '10A6', 'GV018', 'TINHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 2, '10A7', 'GV011', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 2, '10A8', 'GV066', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 2, '10A9', 'GV061', 'GDQPAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 2, '10A10', 'GV063', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 2, '10A11', 'GV054', 'LICHSU10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 3, '10A1', 'GV015', 'TINHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 3, '10A1', 'GV018', 'TINHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 3, '10A2', 'GV009', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 3, '10A3', 'GV022', 'VATLI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 3, '10A4', 'GV065', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 3, '10A5', 'GV059', 'DIALI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 3, '10A6', 'GV026', 'VATLI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 3, '10A7', 'GV027', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 3, '10A8', 'GV063', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 3, '10A9', 'GV013', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 3, '10A10', 'GV012', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 3, '10A11', 'GV067', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 4, '10A1', 'GV041', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 4, '10A2', 'GV009', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 4, '10A3', 'GV004', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 4, '10A4', 'GV013', 'TINHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 4, '10A5', 'GV063', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 4, '10A6', 'GV026', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 4, '10A7', 'GV059', 'DIALI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 4, '10A8', 'GV014', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 4, '10A9', 'GV069', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 4, '10A10', 'GV018', 'TINHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 4, 4, '10A11', 'GV001', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 1, '10A1', 'GV074', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 1, '10A2', 'GV062', 'GDQPAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 1, '10A3', 'GV012', 'TINHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 1, '10A4', 'GV009', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 1, '10A5', 'GV031', 'HOAHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 1, '10A6', 'GV049', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 1, '10A7', 'GV027', 'VATLI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 1, '10A8', 'GV018', 'TINHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 1, '10A9', 'GV037', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 1, '10A10', 'GV061', 'GDQPAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 1, '10A11', 'GV067', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 2, '10A1', 'GV041', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 2, '10A2', 'GV046', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 2, '10A3', 'GV074', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 2, '10A4', 'GV009', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 2, '10A5', 'GV065', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 2, '10A6', 'GV049', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 2, '10A7', 'GV056', 'KTEPL10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 2, '10A8', 'GV018', 'TINHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 2, '10A9', 'GV069', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 2, '10A10', 'GV043', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 2, '10A11', 'GV001', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 3, '10A1', 'GV021', 'VATLI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 3, '10A2', 'GV012', 'TINHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 3, '10A3', 'GV022', 'VATLI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 3, '10A4', 'GV062', 'GDQPAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 3, '10A5', 'GV061', 'GDQPAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 3, '10A6', 'GV037', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 3, '10A6', 'GV041', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 3, '10A7', 'GV037', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 3, '10A7', 'GV041', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 3, '10A8', 'GV056', 'KTEPL10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 3, '10A9', 'GV013', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 3, '10A10', 'GV043', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 3, '10A11', 'GV018', 'TINHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '10A1', 'GV028', 'HOAHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '10A2', 'GV074', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '10A3', 'GV037', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '10A3', 'GV039', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '10A3', 'GV041', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '10A4', 'GV047', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '10A5', 'GV010', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '10A6', 'GV056', 'KTEPL10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '10A7', 'GV049', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '10A8', 'GV037', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '10A8', 'GV039', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '10A8', 'GV041', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '10A9', 'GV013', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '10A10', 'GV058', 'DIALI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '10A11', 'GV037', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '10A11', 'GV039', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 4, '10A11', 'GV041', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 5, '10A1', 'GV034', 'SINHHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 5, '10A2', 'GV023', 'VATLI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 5, '10A3', 'GV074', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 5, '10A4', 'GV019', 'CONGNGHE10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 5, '10A5', 'GV047', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 5, '10A6', 'GV011', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 5, '10A7', 'GV049', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 5, '10A8', 'GV031', 'HOAHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 5, '10A9', 'GV056', 'KTEPL10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 5, 5, '10A11', 'GV006', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 1, '10A1', 'GV028', 'HOAHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 1, '10A2', 'GV027', 'CONGNGHE10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 1, '10A3', 'GV074', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 1, '10A4', 'GV013', 'TINHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 1, '10A5', 'GV063', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 1, '10A6', 'GV011', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 1, '10A7', 'GV049', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 1, '10A8', 'GV058', 'DIALI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 1, '10A9', 'GV073', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 1, '10A10', 'GV054', 'LICHSU10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 1, '10A11', 'GV056', 'KTEPL10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 2, '10A1', 'GV074', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 2, '10A2', 'GV073', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 2, '10A3', 'GV062', 'GDQPAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 2, '10A4', 'GV063', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 2, '10A5', 'GV013', 'TINHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 2, '10A6', 'GV059', 'DIALI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 2, '10A7', 'GV049', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 2, '10A8', 'GV046', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 2, '10A9', 'GV027', 'CONGNGHE10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 2, '10A10', 'GV056', 'KTEPL10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 2, '10A11', 'GV018', 'TINHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 3, '10A1', 'GV073', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 3, '10A2', 'GV029', 'HOAHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 3, '10A3', 'GV074', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 3, '10A4', 'GV054', 'LICHSU10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 3, '10A5', 'GV047', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 3, '10A7', 'GV019', 'CONGNGHE10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 3, '10A8', 'GV046', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 3, '10A9', 'GV048', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 3, '10A10', 'GV018', 'TINHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 3, '10A11', 'GV058', 'DIALI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 4, '10A1', 'GV015', 'TINHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 4, '10A1', 'GV018', 'TINHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 4, '10A2', 'GV046', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 4, '10A3', 'GV059', 'DIALI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 4, '10A4', 'GV009', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 4, '10A5', 'GV047', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 4, '10A6', 'GV049', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 4, '10A7', 'GV054', 'LICHSU10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 4, '10A8', 'GV056', 'KTEPL10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 4, '10A9', 'GV048', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 4, '10A10', 'GV063', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 4, '10A11', 'GV006', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 5, '10A1', 'GV004', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 5, '10A2', 'GV046', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 5, '10A3', 'GV042', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 5, '10A4', 'GV009', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 5, '10A5', 'GV010', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 5, '10A6', 'GV049', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 5, '10A7', 'GV056', 'KTEPL10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 5, '10A8', 'GV063', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 5, '10A9', 'GV036', 'SINHHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 6, 5, '10A11', 'GV006', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 1, '10A1', 'GV021', 'VATLI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 1, '10A2', 'GV055', 'LICHSU10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 1, '10A3', 'GV042', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 1, '10A4', 'GV047', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 1, '10A5', 'GV010', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 1, '10A6', 'GV054', 'LICHSU10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 1, '10A7', 'GV027', 'VATLI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 1, '10A8', 'GV014', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 1, '10A9', 'GV073', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 1, '10A10', 'GV037', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 1, '10A11', 'GV026', 'CONGNGHE10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 2, '10A1', 'GV034', 'SINHHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 2, '10A2', 'GV009', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 2, '10A3', 'GV042', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 2, '10A4', 'GV047', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 2, '10A5', 'GV010', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 2, '10A6', 'GV026', 'VATLI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 2, '10A7', 'GV019', 'CONGNGHE10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 2, '10A8', 'GV061', 'GDQPAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 2, '10A9', 'GV055', 'LICHSU10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 2, '10A10', 'GV054', 'LICHSU10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 2, '10A11', 'GV001', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 3, '10A1', 'GV028', 'HOAHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 3, '10A2', 'GV073', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 3, '10A3', 'GV055', 'LICHSU10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 3, '10A4', 'GV035', 'SINHHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 3, '10A5', 'GV036', 'SINHHOC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 3, '10A6', 'GV059', 'DIALI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 3, '10A7', 'GV027', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 3, '10A8', 'GV053', 'LICHSU10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 3, '10A9', 'GV048', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 3, '10A10', 'GV026', 'CONGNGHE10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 3, '10A11', 'GV042', 'NGUVAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 4, '10A1', 'GV055', 'LICHSU10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 4, '10A2', 'GV027', 'CONGNGHE10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 4, '10A3', 'GV022', 'VATLI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 4, '10A4', 'GV019', 'CONGNGHE10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 4, '10A5', 'GV059', 'DIALI10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 4, '10A6', 'GV026', 'HDTNHN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 4, '10A7', 'GV073', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 4, '10A8', 'GV066', 'TIENGANH10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 4, '10A9', 'GV037', 'GDTC10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 4, '10A10', 'GV012', 'TOAN10');
INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH)
VALUES ('TKB01', 7, 4, '10A11', 'GV042', 'NGUVAN10');

--Bảng HOC, phân phối theo chương trình, học kì I, 2425
-- Khối 10: Các lớp 10A1, 10A2, 10A3, 10A4, 10A5, 10A6, 10A7, 10A8, 10A9, 10A10, 10A11
-- Môn Toán (TOAN10), 3 tiết
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A1', 'TOAN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A2', 'TOAN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A3', 'TOAN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A4', 'TOAN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A5', 'TOAN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A6', 'TOAN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A7', 'TOAN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A8', 'TOAN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A9', 'TOAN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A10', 'TOAN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A11', 'TOAN10', 'HK1-24-25', 'Bắt buộc', 3);

-- Môn Ngữ Văn (NGUVAN10), 3 tiết
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A1', 'NGUVAN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A2', 'NGUVAN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A3', 'NGUVAN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A4', 'NGUVAN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A5', 'NGUVAN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A6', 'NGUVAN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A7', 'NGUVAN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A8', 'NGUVAN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A9', 'NGUVAN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A10', 'NGUVAN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A11', 'NGUVAN10', 'HK1-24-25', 'Bắt buộc', 3);

-- Môn Tiếng Anh (TIENGANH10), 3 tiết
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A1', 'TIENGANH10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A2', 'TIENGANH10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A3', 'TIENGANH10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A4', 'TIENGANH10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A5', 'TIENGANH10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A6', 'TIENGANH10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A7', 'TIENGANH10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A8', 'TIENGANH10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A9', 'TIENGANH10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A10', 'TIENGANH10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A11', 'TIENGANH10', 'HK1-24-25', 'Bắt buộc', 3);

-- Môn Lịch Sử (LICHSU10), 2 tiết
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A1', 'LICHSU10', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A2', 'LICHSU10', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A3', 'LICHSU10', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A4', 'LICHSU10', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A5', 'LICHSU10', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A6', 'LICHSU10', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A7', 'LICHSU10', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A8', 'LICHSU10', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A9', 'LICHSU10', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A10', 'LICHSU10', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A11', 'LICHSU10', 'HK1-24-25', 'Bắt buộc', 2);

-- Môn GDTC (GDTC10), 2 tiết
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A1', 'GDTC10', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A2', 'GDTC10', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A3', 'GDTC10', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A4', 'GDTC10', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A5', 'GDTC10', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A6', 'GDTC10', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A7', 'GDTC10', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A8', 'GDTC10', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A9', 'GDTC10', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A10', 'GDTC10', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A11', 'GDTC10', 'HK1-24-25', 'Bắt buộc', 2);

-- Môn GDQPAN (GDQPAN10), 1 tiết
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A1', 'GDQPAN10', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A2', 'GDQPAN10', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A3', 'GDQPAN10', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A4', 'GDQPAN10', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A5', 'GDQPAN10', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A6', 'GDQPAN10', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A7', 'GDQPAN10', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A8', 'GDQPAN10', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A9', 'GDQPAN10', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A10', 'GDQPAN10', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A11', 'GDQPAN10', 'HK1-24-25', 'Bắt buộc', 1);

-- Môn HĐ TN-HN (HDTNHN10), 3 tiết
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A1', 'HDTNHN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A2', 'HDTNHN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A3', 'HDTNHN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A4', 'HDTNHN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A5', 'HDTNHN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A6', 'HDTNHN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A7', 'HDTNHN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A8', 'HDTNHN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A9', 'HDTNHN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A10', 'HDTNHN10', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A11', 'HDTNHN10', 'HK1-24-25', 'Bắt buộc', 3);

-- Môn GDĐP (GDDP10), 1 tiết
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A1', 'GDDP10', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A2', 'GDDP10', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A3', 'GDDP10', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A4', 'GDDP10', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A5', 'GDDP10', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A6', 'GDDP10', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A7', 'GDDP10', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A8', 'GDDP10', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A9', 'GDDP10', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A10', 'GDDP10', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A11', 'GDDP10', 'HK1-24-25', 'Bắt buộc', 1);

-- Khối 11: Các lớp 11A1, 11A2, 11A3, 11A4, 11A5, 11A6, 11A7, 11A8, 11A9, 11A10, 11A11
-- Môn Toán (TOAN11), 3 tiết
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A1', 'TOAN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A2', 'TOAN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A3', 'TOAN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A4', 'TOAN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A5', 'TOAN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A6', 'TOAN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A7', 'TOAN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A8', 'TOAN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A9', 'TOAN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A10', 'TOAN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A11', 'TOAN11', 'HK1-24-25', 'Bắt buộc', 3);

-- Môn Ngữ Văn (NGUVAN11), 3 tiết
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A1', 'NGUVAN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A2', 'NGUVAN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A3', 'NGUVAN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A4', 'NGUVAN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A5', 'NGUVAN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A6', 'NGUVAN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A7', 'NGUVAN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A8', 'NGUVAN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A9', 'NGUVAN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A10', 'NGUVAN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A11', 'NGUVAN11', 'HK1-24-25', 'Bắt buộc', 3);

-- Môn Tiếng Anh (TIENGANH11), 3 tiết
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A1', 'TIENGANH11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A2', 'TIENGANH11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A3', 'TIENGANH11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A4', 'TIENGANH11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A5', 'TIENGANH11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A6', 'TIENGANH11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A7', 'TIENGANH11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A8', 'TIENGANH11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A9', 'TIENGANH11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A10', 'TIENGANH11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A11', 'TIENGANH11', 'HK1-24-25', 'Bắt buộc', 3);

-- Môn Lịch Sử (LICHSU11), 2 tiết
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A1', 'LICHSU11', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A2', 'LICHSU11', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A3', 'LICHSU11', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A4', 'LICHSU11', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A5', 'LICHSU11', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A6', 'LICHSU11', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A7', 'LICHSU11', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A8', 'LICHSU11', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A9', 'LICHSU11', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A10', 'LICHSU11', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A11', 'LICHSU11', 'HK1-24-25', 'Bắt buộc', 2);

-- Môn GDTC (GDTC11), 2 tiết
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A1', 'GDTC11', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A2', 'GDTC11', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A3', 'GDTC11', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A4', 'GDTC11', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A5', 'GDTC11', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A6', 'GDTC11', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A7', 'GDTC11', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A8', 'GDTC11', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A9', 'GDTC11', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A10', 'GDTC11', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A11', 'GDTC11', 'HK1-24-25', 'Bắt buộc', 2);

-- Môn GDQPAN (GDQPAN11), 1 tiết
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A1', 'GDQPAN11', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A2', 'GDQPAN11', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A3', 'GDQPAN11', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A4', 'GDQPAN11', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A5', 'GDQPAN11', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A6', 'GDQPAN11', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A7', 'GDQPAN11', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A8', 'GDQPAN11', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A9', 'GDQPAN11', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A10', 'GDQPAN11', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A11', 'GDQPAN11', 'HK1-24-25', 'Bắt buộc', 1);

-- Môn HĐ TN-HN (HDTNHN11), 3 tiết
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A1', 'HDTNHN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A2', 'HDTNHN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A3', 'HDTNHN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A4', 'HDTNHN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A5', 'HDTNHN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A6', 'HDTNHN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A7', 'HDTNHN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A8', 'HDTNHN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A9', 'HDTNHN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A10', 'HDTNHN11', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A11', 'HDTNHN11', 'HK1-24-25', 'Bắt buộc', 3);

-- Môn GDĐP (GDDP11), 1 tiết
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A1', 'GDDP11', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A2', 'GDDP11', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A3', 'GDDP11', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A4', 'GDDP11', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A5', 'GDDP11', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A6', 'GDDP11', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A7', 'GDDP11', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A8', 'GDDP11', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A9', 'GDDP11', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A10', 'GDDP11', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A11', 'GDDP11', 'HK1-24-25', 'Bắt buộc', 1);

-- Khối 12: Các lớp 12A1, 12A2, 12A3, 12A4, 12A5, 12A6, 12A7, 12A8, 12A9, 12A10
-- Môn Toán (TOAN12), 3 tiết
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A1', 'TOAN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A2', 'TOAN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A3', 'TOAN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A4', 'TOAN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A5', 'TOAN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A6', 'TOAN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A7', 'TOAN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A8', 'TOAN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A9', 'TOAN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A10', 'TOAN12', 'HK1-24-25', 'Bắt buộc', 3);

-- Môn Ngữ Văn (NGUVAN12), 3 tiết
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A1', 'NGUVAN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A2', 'NGUVAN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A3', 'NGUVAN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A4', 'NGUVAN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A5', 'NGUVAN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A6', 'NGUVAN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A7', 'NGUVAN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A8', 'NGUVAN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A9', 'NGUVAN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A10', 'NGUVAN12', 'HK1-24-25', 'Bắt buộc', 3);

-- Môn Tiếng Anh (TIENGANH12), 3 tiết
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A1', 'TIENGANH12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A2', 'TIENGANH12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A3', 'TIENGANH12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A4', 'TIENGANH12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A5', 'TIENGANH12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A6', 'TIENGANH12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A7', 'TIENGANH12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A8', 'TIENGANH12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A9', 'TIENGANH12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A10', 'TIENGANH12', 'HK1-24-25', 'Bắt buộc', 3);

-- Môn Lịch Sử (LICHSU12), 2 tiết
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A1', 'LICHSU12', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A2', 'LICHSU12', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A3', 'LICHSU12', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A4', 'LICHSU12', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A5', 'LICHSU12', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A6', 'LICHSU12', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A7', 'LICHSU12', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A8', 'LICHSU12', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A9', 'LICHSU12', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A10', 'LICHSU12', 'HK1-24-25', 'Bắt buộc', 2);

-- Môn GDTC (GDTC12), 2 tiết
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A1', 'GDTC12', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A2', 'GDTC12', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A3', 'GDTC12', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A4', 'GDTC12', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A5', 'GDTC12', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A6', 'GDTC12', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A7', 'GDTC12', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A8', 'GDTC12', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A9', 'GDTC12', 'HK1-24-25', 'Bắt buộc', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A10', 'GDTC12', 'HK1-24-25', 'Bắt buộc', 2);

-- Môn GDQPAN (GDQPAN12), 1 tiết
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A1', 'GDQPAN12', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A2', 'GDQPAN12', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A3', 'GDQPAN12', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A4', 'GDQPAN12', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A5', 'GDQPAN12', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A6', 'GDQPAN12', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A7', 'GDQPAN12', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A8', 'GDQPAN12', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A9', 'GDQPAN12', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A10', 'GDQPAN12', 'HK1-24-25', 'Bắt buộc', 1);

-- Môn HĐ TN-HN (HDTNHN12), 3 tiết
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A1', 'HDTNHN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A2', 'HDTNHN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A3', 'HDTNHN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A4', 'HDTNHN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A5', 'HDTNHN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A6', 'HDTNHN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A7', 'HDTNHN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A8', 'HDTNHN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A9', 'HDTNHN12', 'HK1-24-25', 'Bắt buộc', 3);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A10', 'HDTNHN12', 'HK1-24-25', 'Bắt buộc', 3);

-- Môn GDĐP (GDDP12), 1 tiết
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A1', 'GDDP12', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A2', 'GDDP12', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A3', 'GDDP12', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A4', 'GDDP12', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A5', 'GDDP12', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A6', 'GDDP12', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A7', 'GDDP12', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A8', 'GDDP12', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A9', 'GDDP12', 'HK1-24-25', 'Bắt buộc', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A10', 'GDDP12', 'HK1-24-25', 'Bắt buộc', 1);


-- PHẦN 2: THÊM CÁC MÔN HỌC TỰ CHỌN VÀ CHUYÊN ĐỀ

-- Khối 10
-- Nhóm 1 (10A1): Môn lựa chọn: Lý, Hóa, Sinh, Tin; Chuyên đề: Toán, Lý, Hóa.
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A1', 'VATLI10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A1', 'HOAHOC10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A1', 'SINHHOC10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A1', 'TINHOC10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A1', 'TOAN10', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A1', 'VATLI10', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A1', 'HOAHOC10', 'HK1-24-25', 'Chuyên đề', 1);

-- Nhóm 2 (10A2): Môn lựa chọn: Lý, Hóa, Tin, CN; Chuyên đề: Toán, Lý, Hóa.
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A2', 'VATLI10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A2', 'HOAHOC10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A2', 'TINHOC10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A2', 'CONGNGHE10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A2', 'TOAN10', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A2', 'VATLI10', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A2', 'HOAHOC10', 'HK1-24-25', 'Chuyên đề', 1);

-- Nhóm 3 (10A3): Môn lựa chọn: Lý, Hóa, Địa, Tin; Chuyên đề: Toán, Lý, Hóa.
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A3', 'VATLI10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A3', 'HOAHOC10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A3', 'DIALI10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A3', 'TINHOC10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A3', 'TOAN10', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A3', 'VATLI10', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A3', 'HOAHOC10', 'HK1-24-25', 'Chuyên đề', 1);

-- Nhóm 4 (10A4): Môn lựa chọn: Lý, Sinh, Tin, CN; Chuyên đề: Toán, Lý, Sinh.
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A4', 'VATLI10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A4', 'SINHHOC10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A4', 'TINHOC10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A4', 'CONGNGHE10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A4', 'TOAN10', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A4', 'VATLI10', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A4', 'SINHHOC10', 'HK1-24-25', 'Chuyên đề', 1);

-- Nhóm 5 (10A5): Môn lựa chọn: Hóa, Sinh, Địa, Tin; Chuyên đề: Toán, Hóa, Sinh.
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A5', 'HOAHOC10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A5', 'SINHHOC10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A5', 'DIALI10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A5', 'TINHOC10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A5', 'TOAN10', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A5', 'HOAHOC10', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A5', 'SINHHOC10', 'HK1-24-25', 'Chuyên đề', 1);

-- Nhóm 6 (10A6): Môn lựa chọn: Lý, Địa, KT&PL, Tin; Chuyên đề: Toán, Lý, Văn.
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A6', 'VATLI10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A6', 'DIALI10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A6', 'KTEPL10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A6', 'TINHOC10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A6', 'TOAN10', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A6', 'VATLI10', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A6', 'NGUVAN10', 'HK1-24-25', 'Chuyên đề', 1);

-- Nhóm 7 (10A7): Môn lựa chọn: Lý, Địa, KT&PL, CN; Chuyên đề: Toán, Lý, Văn.
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A7', 'VATLI10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A7', 'DIALI10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A7', 'KTEPL10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A7', 'CONGNGHE10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A7', 'TOAN10', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A7', 'VATLI10', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A7', 'NGUVAN10', 'HK1-24-25', 'Chuyên đề', 1);

-- Nhóm 8 (10A8): Môn lựa chọn: Hóa, Địa, KT&PL, Tin; Chuyên đề: Toán, Hóa, Văn.
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A8', 'HOAHOC10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A8', 'DIALI10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A8', 'KTEPL10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A8', 'TINHOC10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A8', 'TOAN10', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A8', 'HOAHOC10', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A8', 'NGUVAN10', 'HK1-24-25', 'Chuyên đề', 1);

-- Nhóm 9 (10A9): Môn lựa chọn: Sinh, Địa, KT&PL, CN; Chuyên đề: Toán, Sinh, Văn.
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A9', 'SINHHOC10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A9', 'DIALI10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A9', 'KTEPL10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A9', 'CONGNGHE10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A9', 'TOAN10', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A9', 'SINHHOC10', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A9', 'NGUVAN10', 'HK1-24-25', 'Chuyên đề', 1);

-- Nhóm 10 (10A10, 10A11): Môn lựa chọn: Địa, KT&PL, Tin, CN; Chuyên đề: Toán, Văn, Sử.
-- Lớp 10A10
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A10', 'DIALI10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A10', 'KTEPL10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A10', 'TINHOC10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A10', 'CONGNGHE10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A10', 'TOAN10', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A10', 'NGUVAN10', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A10', 'LICHSU10', 'HK1-24-25', 'Chuyên đề', 1);
-- Lớp 10A11
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A11', 'DIALI10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A11', 'KTEPL10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A11', 'TINHOC10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A11', 'CONGNGHE10', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A11', 'TOAN10', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A11', 'NGUVAN10', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('10A11', 'LICHSU10', 'HK1-24-25', 'Chuyên đề', 1);

-- Khối 11
-- Nhóm 1 (11A1): Môn lựa chọn: Lý, Hóa, Sinh, Tin; Chuyên đề: Toán, Lý, Hóa.
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A1', 'VATLI11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A1', 'HOAHOC11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A1', 'SINHHOC11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A1', 'TINHOC11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A1', 'TOAN11', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A1', 'VATLI11', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A1', 'HOAHOC11', 'HK1-24-25', 'Chuyên đề', 1);

-- Nhóm 2 (11A2): Môn lựa chọn: Lý, Hóa, KT&PL, Tin; Chuyên đề: Toán, Lý, Hóa.
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A2', 'VATLI11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A2', 'HOAHOC11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A2', 'KTEPL11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A2', 'TINHOC11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A2', 'TOAN11', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A2', 'VATLI11', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A2', 'HOAHOC11', 'HK1-24-25', 'Chuyên đề', 1);

-- Nhóm 3 (11A3): Môn lựa chọn: Lý, Hóa, Địa, Tin; Chuyên đề: Toán, Lý, Hóa.
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A3', 'VATLI11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A3', 'HOAHOC11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A3', 'DIALI11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A3', 'TINHOC11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A3', 'TOAN11', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A3', 'VATLI11', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A3', 'HOAHOC11', 'HK1-24-25', 'Chuyên đề', 1);

-- Nhóm 4 (11A4): Môn lựa chọn: Lý, Sinh, KT&PL, Tin; Chuyên đề: Toán, Lý, Sinh.
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A4', 'VATLI11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A4', 'SINHHOC11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A4', 'KTEPL11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A4', 'TINHOC11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A4', 'TOAN11', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A4', 'VATLI11', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A4', 'SINHHOC11', 'HK1-24-25', 'Chuyên đề', 1);

-- Nhóm 5 (11A5): Môn lựa chọn: Hóa, Sinh, Địa, Tin; Chuyên đề: Toán, Hóa, Sinh.
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A5', 'HOAHOC11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A5', 'SINHHOC11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A5', 'DIALI11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A5', 'TINHOC11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A5', 'TOAN11', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A5', 'HOAHOC11', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A5', 'SINHHOC11', 'HK1-24-25', 'Chuyên đề', 1);

-- Nhóm 6 (11A6, 11A7): Môn lựa chọn: Lý, Địa, KT&PL, Tin; Chuyên đề: Toán, Lý, Văn.
-- Lớp 11A6
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A6', 'VATLI11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A6', 'DIALI11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A6', 'KTEPL11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A6', 'TINHOC11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A6', 'TOAN11', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A6', 'VATLI11', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A6', 'NGUVAN11', 'HK1-24-25', 'Chuyên đề', 1);
-- Lớp 11A7
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A7', 'VATLI11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A7', 'DIALI11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A7', 'KTEPL11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A7', 'TINHOC11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A7', 'TOAN11', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A7', 'VATLI11', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A7', 'NGUVAN11', 'HK1-24-25', 'Chuyên đề', 1);

-- Nhóm 7 (11A8): Môn lựa chọn: Lý, Địa, KT&PL, CN; Chuyên đề: Toán, Lý, Văn.
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A8', 'VATLI11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A8', 'DIALI11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A8', 'KTEPL11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A8', 'CONGNGHE11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A8', 'TOAN11', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A8', 'VATLI11', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A8', 'NGUVAN11', 'HK1-24-25', 'Chuyên đề', 1);

-- Nhóm 8 (11A9): Môn lựa chọn: Hóa, Địa, KT&PL, Tin; Chuyên đề: Toán, Hóa, Văn.
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A9', 'HOAHOC11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A9', 'DIALI11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A9', 'KTEPL11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A9', 'TINHOC11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A9', 'TOAN11', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A9', 'HOAHOC11', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A9', 'NGUVAN11', 'HK1-24-25', 'Chuyên đề', 1);

-- Nhóm 9 (11A10, 11A11): Môn lựa chọn: Địa, KT&PL, Tin, CN; Chuyên đề: Toán, Văn, Sử.
-- Lớp 11A10
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A10', 'DIALI11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A10', 'KTEPL11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A10', 'TINHOC11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A10', 'CONGNGHE11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A10', 'TOAN11', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A10', 'NGUVAN11', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A10', 'LICHSU11', 'HK1-24-25', 'Chuyên đề', 1);
-- Lớp 11A11
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A11', 'DIALI11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A11', 'KTEPL11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A11', 'TINHOC11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A11', 'CONGNGHE11', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A11', 'TOAN11', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A11', 'NGUVAN11', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('11A11', 'LICHSU11', 'HK1-24-25', 'Chuyên đề', 1);

-- Khối 12
-- Nhóm 1 (12A1): Môn lựa chọn: Lý, Hóa, Sinh, Tin; Chuyên đề: Toán, Lý, Hóa.
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A1', 'VATLI12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A1', 'HOAHOC12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A1', 'SINHHOC12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A1', 'TINHOC12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A1', 'TOAN12', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A1', 'VATLI12', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A1', 'HOAHOC12', 'HK1-24-25', 'Chuyên đề', 1);

-- Nhóm 2 (12A2): Môn lựa chọn: Lý, Hóa, KT&PL, Tin; Chuyên đề: Toán, Lý, Hóa.
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A2', 'VATLI12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A2', 'HOAHOC12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A2', 'KTEPL12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A2', 'TINHOC12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A2', 'TOAN12', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A2', 'VATLI12', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A2', 'HOAHOC12', 'HK1-24-25', 'Chuyên đề', 1);

-- Nhóm 3 (12A3): Môn lựa chọn: Lý, Hóa, Địa, Tin; Chuyên đề: Toán, Lý, Hóa.
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A3', 'VATLI12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A3', 'HOAHOC12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A3', 'DIALI12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A3', 'TINHOC12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A3', 'TOAN12', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A3', 'VATLI12', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A3', 'HOAHOC12', 'HK1-24-25', 'Chuyên đề', 1);

-- Nhóm 4 (12A4): Môn lựa chọn: Lý, Sinh, KT&PL, Tin; Chuyên đề: Toán, Lý, Sinh.
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A4', 'VATLI12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A4', 'SINHHOC12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A4', 'KTEPL12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A4', 'TINHOC12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A4', 'TOAN12', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A4', 'VATLI12', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A4', 'SINHHOC12', 'HK1-24-25', 'Chuyên đề', 1);

-- Nhóm 5 (12A5): Môn lựa chọn: Hóa, Sinh, Địa, Tin; Chuyên đề: Toán, Hóa, Sinh.
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A5', 'HOAHOC12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A5', 'SINHHOC12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A5', 'DIALI12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A5', 'TINHOC12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A5', 'TOAN12', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A5', 'HOAHOC12', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A5', 'SINHHOC12', 'HK1-24-25', 'Chuyên đề', 1);

-- Nhóm 6 (12A6): Môn lựa chọn: Lý, Địa, KT&PL, Tin; Chuyên đề: Toán, Lý, Văn.
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A6', 'VATLI12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A6', 'DIALI12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A6', 'KTEPL12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A6', 'TINHOC12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A6', 'TOAN12', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A6', 'VATLI12', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A6', 'NGUVAN12', 'HK1-24-25', 'Chuyên đề', 1);

-- Nhóm 7 (12A7): Môn lựa chọn: Lý, Địa, KT&PL, CN; Chuyên đề: Toán, Lý, Văn.
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A7', 'VATLI12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A7', 'DIALI12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A7', 'KTEPL12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A7', 'CONGNGHE12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A7', 'TOAN12', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A7', 'VATLI12', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A7', 'NGUVAN12', 'HK1-24-25', 'Chuyên đề', 1);

-- Nhóm 8 (12A8): Môn lựa chọn: Hóa, Địa, KT&PL, Tin; Chuyên đề: Toán, Hóa, Văn.
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A8', 'HOAHOC12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A8', 'DIALI12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A8', 'KTEPL12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A8', 'TINHOC12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A8', 'TOAN12', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A8', 'HOAHOC12', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A8', 'NGUVAN12', 'HK1-24-25', 'Chuyên đề', 1);

-- Nhóm 9 (12A9, 12A10): Môn lựa chọn: Địa, KT&PL, Tin, CN; Chuyên đề: Toán, Văn, Sử.
-- Lớp 12A9
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A9', 'DIALI12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A9', 'KTEPL12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A9', 'TINHOC12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A9', 'CONGNGHE12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A9', 'TOAN12', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A9', 'NGUVAN12', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A9', 'LICHSU12', 'HK1-24-25', 'Chuyên đề', 1);
-- Lớp 12A10
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A10', 'DIALI12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A10', 'KTEPL12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A10', 'TINHOC12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A10', 'CONGNGHE12', 'HK1-24-25', 'Tự chọn', 2);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A10', 'TOAN12', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A10', 'NGUVAN12', 'HK1-24-25', 'Chuyên đề', 1);
INSERT INTO HOC (MaLop, MaMH, MaHK, HinhThuc, PhanPhoiTiet) VALUES ('12A10', 'LICHSU12', 'HK1-24-25', 'Chuyên đề', 1);


COMMIT;