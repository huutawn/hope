# 📱 Hướng Dẫn Sử Dụng Trang Test Real-time Messaging

## 🎯 Tổng Quan

Trang test `test-realtime-messaging.html` là một công cụ toàn diện để kiểm tra hệ thống tin nhắn thời gian thực của dự án Hope. Trang này cho phép bạn:

- 🔐 Đăng nhập bằng email/password thông qua API `/api/auth/token`
- 🌐 Kết nối WebSocket an toàn với JWT token
- 💬 Gửi và nhận tin nhắn real-time
- 👥 Test tin nhắn nhóm/phòng chat
- 📊 Theo dõi logs hệ thống chi tiết

---

## 🚀 Cách Khởi Chạy

### Bước 1: Khởi động Backend
```bash
# Trong thư mục dự án Hope
mvn spring-boot:run
```
Hoặc chạy từ IDE với main class `HopeApplication.java`

### Bước 2: Mở Trang Test
1. Mở file `test-realtime-messaging.html` bằng trình duyệt web
2. Hoặc mở qua địa chỉ: `file:///đường-dẫn-đến-file/test-realtime-messaging.html`

⚠️ **Lưu ý**: Đảm bảo backend đang chạy trên `http://localhost:8080`

---

## 🔐 Hướng Dẫn Đăng Nhập

### Thông Tin Đăng Nhập Mặc Định
Khi trang load, các trường sẽ được điền sẵn:
- **Email**: `user1@example.com`
- **Mật khẩu**: `password123`

### Cách Đăng Nhập
1. **Nhập thông tin**: Điền email và mật khẩu của người dùng đã tồn tại trong hệ thống
2. **Nhấn "Đăng Nhập"**: Hệ thống sẽ gửi request đến `/api/auth/token`
3. **Kiểm tra trạng thái**: 
   - ✅ **Thành công**: Màn hình sẽ hiển thị thông tin người dùng và JWT token
   - ❌ **Thất bại**: Thông báo lỗi sẽ xuất hiện

### Phản Hồi API Login
```json
{
  "code": 1000,
  "result": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9..."
  }
}
```

---

## 🌐 Kết Nối WebSocket

### Sau Khi Đăng Nhập Thành Công:
1. **Hiển thị Section WebSocket**: Phần "Kết Nối WebSocket" sẽ xuất hiện
2. **Nhấn "Kết Nối WebSocket"**: Hệ thống sẽ thiết lập kết nối an toàn
3. **Xác thực JWT**: Token được truyền qua parameter `login`

### Các Subscription Tự Động:
- `/user/queue/messages` - Tin nhắn cá nhân
- `/user/queue/notifications` - Thông báo cá nhân

### Trạng Thái Kết Nối:
- 🟢 **Thành công**: "Đã kết nối WebSocket" (màu xanh)
- 🔴 **Thất bại**: "Kết nối thất bại" (màu đỏ)

---

## 💬 Gửi và Nhận Tin Nhắn

### 1. Tin Nhắn Cá Nhân (1-on-1)

#### Cách Gửi:
1. **Nhập email người nhận**: Ví dụ `user2@example.com`
2. **Nhập nội dung**: Tin nhắn muốn gửi
3. **Chọn loại tin nhắn**:
   - **"Gửi Tin Nhắn"**: Lưu vào database + realtime
   - **"Gửi Tin Nhắn Riêng"**: Chỉ realtime, không lưu

#### Endpoint WebSocket:
- **Tin nhắn thường**: `/app/message.send`
- **Tin nhắn riêng**: `/app/message.private`

#### Format Request:
```json
{
  "senderEmail": "user1@example.com",
  "receiverEmail": "user2@example.com", 
  "content": "Xin chào! Đây là tin nhắn test."
}
```

### 2. Tin Nhắn Nhóm/Phòng Chat

#### Cách Gửi:
1. **Nhập ID phòng**: Ví dụ `room1`, `meeting-room-A`
2. **Nhập nội dung tin nhắn**
3. **Nhấn "Gửi Tin Nhắn Nhóm"**

#### Endpoint WebSocket:
- **Gửi**: `/app/message.room.{roomId}`
- **Nhận**: `/topic/room/{roomId}`

### 3. Hiển Thị Tin Nhắn

#### Tin Nhắn Gửi (Màu Xanh):
```
┌─────────────────────────────┐
│ Bạn                    [Sent]│
│ Nội dung tin nhắn           │
│ 14:30:25                    │
└─────────────────────────────┘
```

#### Tin Nhắn Nhận (Màu Xám):
```
┌─────────────────────────────┐
│[Received] user2@example.com │
│ Nội dung tin nhắn           │
│ 14:30:25                    │
└─────────────────────────────┘
```

---

## 🛠 Các Tính Năng Khác

### 1. Phím Tắt
- **Enter trong mật khẩu**: Đăng nhập tự động
- **Enter trong tin nhắn**: Gửi tin nhắn (Shift+Enter để xuống dòng)

### 2. Quản Lý Tin Nhắn
- **Tự động scroll**: Tin nhắn mới tự động cuộn xuống
- **Xóa tin nhắn**: Nút "Xóa Tất Cả Tin Nhắn"
- **Hiển thị thời gian**: Mỗi tin nhắn có timestamp

### 3. Logs Hệ Thống
- **Màu sắc phân loại**:
  - 🟢 Xanh: Thành công
  - 🔴 Đỏ: Lỗi
  - 🔵 Xanh dương: Thông tin
- **Thời gian**: Mỗi log có timestamp chi tiết
- **Auto-scroll**: Tự động cuộn đến log mới nhất

---

## 📊 Kịch Bản Test Thực Tế

### Scenario 1: Test Tin Nhắn 1-on-1
1. **Mở 2 tab trình duyệt**
2. **Tab 1**: Đăng nhập `user1@example.com`
3. **Tab 2**: Đăng nhập `user2@example.com` 
4. **Kết nối WebSocket** ở cả 2 tab
5. **Gửi tin nhắn** từ User1 đến User2
6. **Kiểm tra** tin nhắn xuất hiện real-time ở Tab 2

### Scenario 2: Test Tin Nhắn Nhóm
1. **Mở 3+ tab** với các user khác nhau
2. **Tất cả join** cùng một room (ví dụ: `room1`)
3. **Gửi tin nhắn** từ một user
4. **Xác nhận** tất cả user khác nhận được

### Scenario 3: Test Error Handling
1. **Không đăng nhập** → Thử kết nối WebSocket
2. **Token hết hạn** → Xem xử lý lỗi
3. **Ngắt mạng** → Test reconnection

---

## 🔧 Troubleshooting

### Lỗi Thường Gặp

#### 1. "Lỗi kết nối: Failed to fetch"
**Nguyên nhân**: Backend chưa khởi động hoặc sai port
**Giải pháp**: 
```bash
# Kiểm tra backend đang chạy
curl http://localhost:8080/api/auth/token
```

#### 2. "Đăng nhập thất bại"
**Nguyên nhân**: Email/password không tồn tại
**Giải pháp**: Tạo user trong database hoặc dùng user có sẵn

#### 3. "Kết nối WebSocket thất bại"
**Nguyên nhân**: 
- JWT token không hợp lệ
- WebSocket endpoint không accessible
**Giải pháp**: Kiểm tra logs backend và JWT token

#### 4. "Tin nhắn không nhận được"
**Nguyên nhân**: 
- Chưa subscribe đúng topic
- Email người nhận không tồn tại
**Giải pháp**: Kiểm tra logs và database

### Debug Tips

#### 1. Kiểm tra Backend Logs
```bash
# Xem logs WebSocket connection
tail -f application.log | grep WebSocket
```

#### 2. Kiểm tra Browser Developer Tools
- **Network tab**: Xem HTTP requests
- **Console**: Xem JavaScript errors
- **WebSocket tab**: Monitor WebSocket traffic

#### 3. Test API Riêng Lẻ
```bash
# Test login API
curl -X POST http://localhost:8080/api/auth/token \
  -H "Content-Type: application/json" \
  -d '{"email":"user1@example.com","password":"password123"}'
```

---

## 🎯 Best Practices

### 1. Quản Lý Connection
- **Luôn logout** trước khi đóng trang
- **Không mở quá nhiều tab** cùng lúc với cùng user
- **Monitor connection status** thường xuyên

### 2. Test Data
- **Tạo nhiều user** với email/password khác nhau
- **Sử dụng room ID** có ý nghĩa (meeting-room-1, team-chat)
- **Test với nội dung đa dạng** (emoji, special chars, long text)

### 3. Performance
- **Xóa logs** định kỳ để tránh lag browser
- **Clear messages** khi test nhiều
- **Refresh trang** nếu có memory leak

---

## 📚 Tài Liệu Kham Khảo

### API Endpoints
- `POST /api/auth/token` - Đăng nhập
- `POST /api/messages/send` - Gửi tin nhắn (REST)
- `GET /api/messages?email1=...&email2=...` - Lịch sử tin nhắn

### WebSocket Endpoints
- `/ws` - Connection endpoint
- `/app/message.send` - Gửi tin nhắn cá nhân
- `/app/message.private` - Gửi tin nhắn riêng
- `/app/message.room.{roomId}` - Gửi tin nhắn nhóm

### Subscription Topics
- `/user/queue/messages` - Tin nhắn cá nhân
- `/user/queue/notifications` - Thông báo cá nhân  
- `/topic/room/{roomId}` - Tin nhắn nhóm

---

## 🆘 Liên Hệ Support

Nếu gặp vấn đề không thể giải quyết:

1. **Check documentation**: `WebSocket-Documentation-Vietnamese.md`
2. **Review backend logs**: Tìm error messages chi tiết
3. **Test với Postman**: Verify API endpoints riêng lẻ
4. **Browser compatibility**: Test trên Chrome/Firefox/Edge

---

## 🎉 Kết Luận

Trang test này cung cấp một công cụ mạnh mẽ và trực quan để:
- ✅ Validate toàn bộ flow authentication
- ✅ Test real-time messaging functionality  
- ✅ Debug WebSocket connections
- ✅ Monitor system performance
- ✅ Simulate real-world usage scenarios

**Happy Testing!** 🚀

---

*Tài liệu này được cập nhật lần cuối: {{current_date}}*
