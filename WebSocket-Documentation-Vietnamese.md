# Hệ Thống Nhắn Tin và Thông Báo WebSocket

Tài liệu này mô tả hệ thống nhắn tin và thông báo dựa trên WebSocket toàn diện được triển khai trong ứng dụng Hope.

## Tổng Quan

Hệ thống cung cấp khả năng giao tiếp thời gian thực thông qua:
- **MessageService & MessageController**: Xử lý nhắn tin trực tiếp, nhắn tin nhóm/phòng, và chỉ báo đang gõ
- **NotificationService & NotificationController**: Quản lý thông báo người dùng, thông báo theo vai trò, và thông báo phát sóng
- **Cấu Hình WebSocket**: Kết nối WebSocket bảo mật với xác thực JWT

## Kiến Trúc

### Các Thành Phần Đã Tạo/Cải Tiến

1. **Cấu Hình WebSocket** (`WebsocketConfig.java`)
2. **Dịch Vụ Tin Nhắn** (`MessageService.java`) - Cải tiến với hỗ trợ WebSocket
3. **Dịch Vụ Thông Báo** (`NotificationService.java`) - Mới
4. **Controller Tin Nhắn** (`MessageController.java`) - Cải tiến với các endpoint WebSocket
5. **Controller Thông Báo** (`NotificationController.java`) - Mới
6. **DTOs**: `NotificationRequest.java`, `NotificationResponse.java`
7. **Repository**: `NotificationRepository.java`
8. **Mapper**: `NotificationMapper.java`
9. **Entity**: Entity `Notification.java` được cải tiến

## Các Endpoint WebSocket

### Kết Nối
- **Endpoint**: `/ws`
- **Giao Thức**: SockJS với STOMP
- **Xác Thực**: JWT token được truyền như tham số `login`

### Các Endpoint Tin Nhắn

#### Nhắn Tin Trực Tiếp
```
@MessageMapping("/message.send")
@SendToUser("/queue/messages")
```
- Gửi tin nhắn giữa các người dùng
- Tự động lưu vào cơ sở dữ liệu
- Giao hàng đến hàng đợi riêng tư của người nhận

#### Tin Nhắn Riêng Tư (Chỉ thời gian thực)
```
@MessageMapping("/message.private")
```
- Gửi tin nhắn thời gian thực mà không lưu cơ sở dữ liệu
- Lý tưởng cho giao tiếp tạm thời

#### Nhắn Tin Phòng/Nhóm
```
@MessageMapping("/message.room.{roomId}")
@SendTo("/topic/room/{roomId}")
```
- Phát sóng tin nhắn đến tất cả người dùng trong phòng
- Người đăng ký phòng nhận tin nhắn thời gian thực

#### Chỉ Báo Đang Gõ
```
@MessageMapping("/message.typing")
```
- Gửi chỉ báo đang gõ đến người dùng khác
- Tính năng thời gian thực không lưu trữ

### Các Endpoint Thông Báo

#### Thông Báo Cá Nhân
```
@MessageMapping("/notification.send")
@SendToUser("/queue/notifications")
```
- Gửi thông báo đến người dùng cụ thể
- Lưu trữ vào cơ sở dữ liệu

#### Thông Báo Thời Gian Thực
```
@MessageMapping("/notification.realtime")
```
- Gửi thông báo mà không lưu cơ sở dữ liệu
- Cho cảnh báo tạm thời

#### Thông Báo Phát Sóng
```
@MessageMapping("/notification.broadcast")
@SendTo("/topic/notifications/broadcast")
```
- Gửi thông báo đến tất cả người dùng đã kết nối
- Thông báo toàn hệ thống

#### Thông Báo Theo Vai Trò
```
@MessageMapping("/notification.role")
```
- Gửi thông báo đến tất cả người dùng có vai trò cụ thể
- Thông báo hàng loạt cho nhóm người dùng

## Các Topic Đăng Ký

### Đăng Ký Riêng Cho Người Dùng
- `/user/queue/messages` - Tin nhắn cá nhân
- `/user/queue/notifications` - Thông báo cá nhân
- `/user/queue/notifications/read-all` - Cập nhật trạng thái đã đọc
- `/user/queue/notifications/deleted` - Thông báo xóa

### Topic Công Khai
- `/topic/notifications/broadcast` - Thông báo phát sóng
- `/topic/room/{roomId}` - Tin nhắn phòng
- `/topic/messages/{userEmail}` - Cập nhật tin nhắn cho cuộc trò chuyện
- `/topic/notifications/{userEmail}` - Cập nhật thông báo

## Các Endpoint REST API

### API Tin Nhắn
- `POST /api/messages/send` - Gửi tin nhắn (REST)
- `GET /api/messages` - Lấy lịch sử cuộc trò chuyện
- `POST /api/messages/mark-read` - Đánh dấu tin nhắn đã đọc

### API Thông Báo
- `POST /api/notifications/create` - Tạo thông báo
- `GET /api/notifications/{userEmail}` - Lấy thông báo người dùng
- `GET /api/notifications/{userEmail}/unread-count` - Lấy số lượng chưa đọc
- `POST /api/notifications/{notificationId}/mark-read` - Đánh dấu đã đọc
- `POST /api/notifications/{userEmail}/mark-all-read` - Đánh dấu tất cả đã đọc
- `DELETE /api/notifications/{notificationId}` - Xóa thông báo
- `POST /api/notifications/send-to-role` - Gửi theo vai trò
- `POST /api/notifications/broadcast` - Phát sóng đến tất cả người dùng

## Bảo Mật

### Xác Thực
- JWT token được yêu cầu cho kết nối WebSocket
- Token được truyền như tham số `login` trong quá trình kết nối
- Danh tính người dùng được trích xuất từ token để ủy quyền

### Ủy Quyền
- Người dùng chỉ có thể gửi tin nhắn với email đã xác thực của họ
- Truy cập theo vai trò cho thông báo quản trị
- Hàng đợi riêng cho người dùng đảm bảo tính riêng tư của tin nhắn

## Luồng Tin Nhắn

### Nhắn Tin Trực Tiếp
1. Client kết nối với JWT token
2. Client đăng ký `/user/queue/messages`
3. Client gửi tin nhắn qua `/app/message.send`
4. Server xác thực danh tính người gửi
5. Tin nhắn được lưu vào cơ sở dữ liệu
6. Tin nhắn được giao đến hàng đợi của người nhận
7. Topic cuộc trò chuyện được cập nhật để làm mới UI

### Thông Báo
1. Client đăng ký `/user/queue/notifications`
2. Các sự kiện phía server kích hoạt thông báo
3. Thông báo được lưu vào cơ sở dữ liệu
4. Giao hàng thời gian thực đến hàng đợi người dùng
5. Phát sóng tùy chọn cho cảnh báo toàn hệ thống

## Lược Đồ Cơ Sở Dữ Liệu

### Entity Thông Báo Được Cải Tiến
```java
@Entity
public class Notification {
    private Long id;
    private User user;
    private String title;        // Trường mới
    private String message;
    private String type;         // Trường mới (info/warning/error)
    private boolean isRead;
    private LocalDateTime createdAt;
}
```

## Ví Dụ Sử Dụng

### Frontend JavaScript (STOMP.js)
```javascript
// Kết nối với JWT
const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);
stompClient.connect({login: jwtToken}, onConnected, onError);

// Đăng ký tin nhắn cá nhân
stompClient.subscribe('/user/queue/messages', handleMessage);

// Gửi tin nhắn
stompClient.send('/app/message.send', {}, JSON.stringify({
    senderEmail: 'user@example.com',
    receiverEmail: 'recipient@example.com',
    content: 'Xin chào!'
}));
```

### Sử Dụng Backend Service
```java
// Gửi thông báo
notificationService.createNotification(
    "user@example.com", 
    "Chào mừng", 
    "Chào mừng bạn đến với nền tảng của chúng tôi!", 
    "info"
);

// Phát sóng theo vai trò
notificationService.sendNotificationToRole(
    "ADMIN", 
    "Cảnh Báo Hệ Thống", 
    "Bảo trì đã được lên lịch", 
    "warning"
);
```

## Kiểm Thử

### HTML Test Client
Một client kiểm thử toàn diện (`websocket-client-example.html`) được cung cấp với các tính năng:
- Quản lý kết nối với xác thực JWT
- Kiểm thử nhắn tin trực tiếp
- Kiểm thử nhắn tin phòng
- Kiểm thử thông báo (cá nhân, theo vai trò, phát sóng)
- Hiển thị tin nhắn và thông báo thời gian thực
- Trạng thái kết nối và ghi log

### Các Bước Kiểm Thử
1. Khởi động ứng dụng Spring Boot
2. Mở `websocket-client-example.html` trong trình duyệt
3. Nhập JWT token hợp lệ
4. Kết nối đến WebSocket
5. Kiểm thử các tính năng nhắn tin và thông báo khác nhau

## Xử Lý Lỗi

### Mã Lỗi Mới
- `NOTIFICATION_NOT_FOUND(506, "Không tìm thấy thông báo", HttpStatus.NOT_FOUND)`

### Xử Lý Lỗi WebSocket
- Lỗi kết nối được ghi log và hiển thị
- Token không hợp lệ dẫn đến từ chối kết nối
- Gửi tin nhắn không được ủy quyền ném SecurityException
- Lỗi cơ sở dữ liệu được truyền đến client qua WebSocket

## Cân Nhắc Hiệu Suất

### Tính Năng Mở Rộng
- Hàng đợi riêng cho người dùng để cô lập tin nhắn
- Phát sóng dựa trên topic để hiệu quả
- Lưu trữ cơ sở dữ liệu với tiềm năng cache
- Message broker có thể cấu hình (Simple in-memory broker)

### Mẹo Tối Ưu
- Cân nhắc message broker bên ngoài (RabbitMQ/Apache Kafka) cho sản xuất
- Triển khai phân trang tin nhắn cho lịch sử chat
- Thêm theo dõi trạng thái tin nhắn (đã gửi/đã giao/đã đọc)
- Cân nhắc chính sách hết hạn tin nhắn

## Cải Tiến Tương Lai

### Cải Tiến Tiềm Năng
1. **Tệp Đính Kèm**: Hỗ trợ tin nhắn đa phương tiện
2. **Phản Ứng Tin Nhắn**: Like/react với tin nhắn
3. **Luồng Tin Nhắn**: Trả lời tin nhắn cụ thể
4. **Trạng Thái Người Dùng**: Trạng thái online/offline
5. **Tìm Kiếm Tin Nhắn**: Tìm kiếm full-text trong lịch sử chat
6. **Push Notifications**: Tích hợp push di động
7. **Mã Hóa Tin Nhắn**: Mã hóa đầu cuối
8. **Kiểm Duyệt Chat**: Kiểm soát admin cho nội dung

### Tính Năng Nâng Cao
- Khởi tạo cuộc gọi thoại/video qua WebSocket
- Phối hợp chia sẻ màn hình
- Chỉnh sửa tài liệu cộng tác
- Chia sẻ vị trí thời gian thực
- Tích hợp với nền tảng giao tiếp bên ngoài

## Khắc Phục Sự Cố

### Vấn Đề Thường Gặp
1. **Kết Nối Thất Bại**: Kiểm tra tính hợp lệ và định dạng JWT token
2. **Tin Nhắn Không Nhận Được**: Xác minh đăng ký topic đúng
3. **Lỗi Xác Thực**: Đảm bảo token được truyền trong tham số login
4. **Vấn Đề CORS**: Cấu hình allowed origins trong WebSocket config

### Mẹo Debug
- Bật STOMP debug logging trong trình duyệt
- Kiểm tra server logs cho các sự kiện kết nối WebSocket
- Xác minh ràng buộc cơ sở dữ liệu cho message/notification entities
- Kiểm thử với HTML client để cô lập

## Tính Năng Chính

### 🔐 **Bảo Mật**
- Xác thực JWT cho tất cả kết nối WebSocket
- Kiểm tra ủy quyền người dùng nghiêm ngặt
- Hàng đợi tin nhắn riêng tư
- Kiểm soát truy cập dựa trên vai trò

### 💬 **Nhắn Tin**
- Nhắn tin 1-on-1 thời gian thực
- Nhắn tin nhóm/phòng
- Chỉ báo đang gõ
- Lưu trữ tin nhắn vĩnh viễn
- Lịch sử cuộc trò chuyện

### 🔔 **Thông Báo**
- Thông báo cá nhân
- Thông báo hàng loạt theo vai trò
- Phát sóng toàn hệ thống
- Tùy chọn thời gian thực và lưu trữ
- Theo dõi số lượng chưa đọc
- Chức năng đánh dấu đã đọc/xóa

### 🚀 **Hiệu Suất**
- Hàng đợi riêng cho người dùng
- Phát sóng dựa trên topic
- Lưu trữ cơ sở dữ liệu
- Message broker có thể cấu hình

### 📱 **Khả Năng Mở Rộng**
- Hỗ trợ nhiều người dùng đồng thời
- Cấu trúc modular dễ mở rộng
- Tích hợp với hệ thống hiện có
- Sẵn sàng cho môi trường sản xuất

Hệ thống WebSocket này cung cấp nền tảng mạnh mẽ cho các tính năng giao tiếp thời gian thực trong ứng dụng Hope, với khả năng tùy chỉnh và cải tiến rộng rãi dựa trên yêu cầu cụ thể.
