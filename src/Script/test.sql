--- cần để test code của t, chạy cái mới của thèn Sơn trc nha
-- Dùng để test Phản hồi ý kiến
CREATE SEQUENCE SEQ_YKIEN
    START WITH 1
    INCREMENT BY 1
    NOCACHE;
CREATE OR REPLACE TRIGGER TRG_YKIEN_ID
BEFORE INSERT ON tkb.YKIEN
FOR EACH ROW
BEGIN
    IF :NEW.MaYK IS NULL THEN
        :NEW.MaYK := 'YK' || LPAD(SEQ_YKIEN.NEXTVAL, 3, '0');
END IF;
END;
/
CREATE SEQUENCE SEQ_GIAOVIEN
    START WITH 80 -- né du lieu cua Son
    INCREMENT BY 1
    NOCACHE;

CREATE OR REPLACE TRIGGER TRG_GIAOVIEN_AUTO_ID
BEFORE INSERT ON tkb.GIAOVIEN
FOR EACH ROW
WHEN (NEW.MaGV IS NULL)
BEGIN
SELECT 'GV' || LPAD(SEQ_GIAOVIEN.NEXTVAL, 3, '0')
INTO :NEW.MaGV
FROM DUAL;
END;
/


-- ADMIN, GV020
INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Cần cải thiện thiết bị phòng học.', SYSDATE, 0, 'GV005');
INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Đề xuất tổ chức hội thảo chuyên môn hàng tháng.', SYSDATE, 0, 'GV012');
INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Nên có thêm hoạt động ngoại khóa.', SYSDATE, 1, 'GV023');
INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Thư viện cần bổ sung sách tham khảo.', SYSDATE, 0, 'GV034');
INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Hệ thống điểm danh online chưa ổn định.', SYSDATE, 1, 'GV045');

INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Đề xuất tăng thời gian nghỉ giữa các tiết.', SYSDATE, 0, 'GV056');
INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Cần thay mới máy chiếu ở phòng 204.', SYSDATE, 1, 'GV067');
INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Đề nghị tổ chức thi thử tốt nghiệp.', SYSDATE, 0, 'GV009');
INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Cần cải thiện chất lượng căng tin.', SYSDATE, 1, 'GV021');
INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Thiết bị âm thanh không hoạt động tốt.', SYSDATE, 0, 'GV033');

INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Nên giảm tải nội dung một số môn học.', SYSDATE, 1, 'GV014');
INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Cần cập nhật phần mềm dạy học.', SYSDATE, 0, 'GV026');
INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Phòng học cần sửa quạt và đèn.', SYSDATE, 1, 'GV037');
INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Nên tổ chức khóa đào tạo cho giáo viên mới.', SYSDATE, 0, 'GV048');
INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Cần có chính sách khen thưởng cho GV tích cực.', SYSDATE, 1, 'GV059');

INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Lịch giảng dạy cần được sắp xếp linh hoạt hơn.', SYSDATE, 0, 'GV002');
INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Phòng máy tính cần kiểm tra định kỳ.', SYSDATE, 1, 'GV018');
INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Thiết bị trình chiếu không đủ cho các lớp.', SYSDATE, 0, 'GV027');
INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Cần mở lớp tập huấn sử dụng LMS.', SYSDATE, 1, 'GV039');
INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Đề xuất thay đổi giờ bắt đầu buổi học sáng.', SYSDATE, 0, 'GV050');

INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Cần rà soát lại chương trình đào tạo.', SYSDATE, 0, 'ADMIN');
INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Đề xuất tổ chức sinh hoạt chuyên môn toàn trường.', SYSDATE, 0, 'GV020');
INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Hệ thống quản lý học tập bị lỗi vào giờ cao điểm.', SYSDATE, 1, 'ADMIN');
INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Đề nghị bổ sung thiết bị dạy học cho tổ Toán.', SYSDATE, 0, 'GV020');
INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Cần cập nhật quy chế mới của Bộ GD.', SYSDATE, 1, 'ADMIN');

INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Đề xuất lập nhóm hỗ trợ chuyên môn theo khối.', SYSDATE, 0, 'GV020');
INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Nên tổ chức họp tổ chuyên môn định kỳ hơn.', SYSDATE, 0, 'GV020');
INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Thư viện chưa cập nhật sách giáo khoa mới.', SYSDATE, 1, 'ADMIN');
INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Cần có chính sách hỗ trợ GV học sau đại học.', SYSDATE, 0, 'ADMIN');
INSERT INTO YKIEN (NoiDung, NgayGui, AnDanh, MaGV) VALUES ('Mong muốn có thêm buổi sinh hoạt công đoàn.', SYSDATE, 1, 'GV020');


COMMIT
