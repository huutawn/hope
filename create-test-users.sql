-- Script tạo users test cho real-time messaging
-- Chạy script này trong database để tạo các user test

-- Tạo roles nếu chưa có
INSERT IGNORE INTO role (name, description) VALUES 
('USER', 'Standard user role'),
('ADMIN', 'Administrator role');

-- Tạo test users với password đã hash (password123)
-- BCrypt hash của "password123": $2a$10$5K8yF6Q1YmjK7rE.WLr/OOaM9L.xJ8qB3vH1sG9dP7cN2fE4tR6wS

INSERT IGNORE INTO user (id, email, password, created_at, updated_at) VALUES
('test-user-1', 'user1@example.com', '$2a$10$5K8yF6Q1YmjK7rE.WLr/OOaM9L.xJ8qB3vH1sG9dP7cN2fE4tR6wS', NOW(), NOW()),
('test-user-2', 'user2@example.com', '$2a$10$5K8yF6Q1YmjK7rE.WLr/OOaM9L.xJ8qB3vH1sG9dP7cN2fE4tR6wS', NOW(), NOW()),
('test-user-3', 'user3@example.com', '$2a$10$5K8yF6Q1YmjK7rE.WLr/OOaM9L.xJ8qB3vH1sG9dP7cN2fE4tR6wS', NOW(), NOW()),
('test-admin', 'admin@example.com', '$2a$10$5K8yF6Q1YmjK7rE.WLr/OOaM9L.xJ8qB3vH1sG9dP7cN2fE4tR6wS', NOW(), NOW());

-- Gán roles cho users
INSERT IGNORE INTO user_roles (user_id, roles_name) VALUES
('test-user-1', 'USER'),
('test-user-2', 'USER'), 
('test-user-3', 'USER'),
('test-admin', 'USER'),
('test-admin', 'ADMIN');

-- Tạo profiles cho users
INSERT IGNORE INTO media_file (id, url, created_at) VALUES
('profile-pic-1', 'https://via.placeholder.com/150/0000FF/808080?Text=User1', NOW()),
('profile-pic-2', 'https://via.placeholder.com/150/FF0000/FFFFFF?Text=User2', NOW()),
('profile-pic-3', 'https://via.placeholder.com/150/00FF00/000000?Text=User3', NOW()),
('profile-pic-admin', 'https://via.placeholder.com/150/FFD700/000000?Text=Admin', NOW());

INSERT IGNORE INTO profile (id, full_name, user_id, profile_picture_id) VALUES
('profile-1', 'Test User One', 'test-user-1', 'profile-pic-1'),
('profile-2', 'Test User Two', 'test-user-2', 'profile-pic-2'),
('profile-3', 'Test User Three', 'test-user-3', 'profile-pic-3'),
('profile-admin', 'Administrator', 'test-admin', 'profile-pic-admin');

-- Cập nhật user table để link với profiles
UPDATE user SET profile_id = 'profile-1' WHERE id = 'test-user-1';
UPDATE user SET profile_id = 'profile-2' WHERE id = 'test-user-2';
UPDATE user SET profile_id = 'profile-3' WHERE id = 'test-user-3';
UPDATE user SET profile_id = 'profile-admin' WHERE id = 'test-admin';

-- Tạo một số tin nhắn test để có dữ liệu
INSERT IGNORE INTO message (sender_id, receiver_id, content, is_read, sent_at) VALUES
('test-user-1', 'test-user-2', 'Xin chào! Đây là tin nhắn test đầu tiên.', true, NOW() - INTERVAL 1 HOUR),
('test-user-2', 'test-user-1', 'Chào bạn! Tôi đã nhận được tin nhắn của bạn.', true, NOW() - INTERVAL 50 MINUTE),
('test-user-1', 'test-user-2', 'Rất tuyệt! Hệ thống messaging hoạt động tốt.', false, NOW() - INTERVAL 30 MINUTE);

-- Hiển thị thông tin các user đã tạo
SELECT 
    u.id,
    u.email,
    p.full_name,
    GROUP_CONCAT(ur.roles_name) as roles
FROM user u
LEFT JOIN profile p ON u.profile_id = p.id
LEFT JOIN user_roles ur ON u.id = ur.user_id
WHERE u.email LIKE '%@example.com'
GROUP BY u.id, u.email, p.full_name;

-- Hiển thị tin nhắn test
SELECT 
    m.id,
    sender.email as sender_email,
    receiver.email as receiver_email,
    m.content,
    m.is_read,
    m.sent_at
FROM message m
JOIN user sender ON m.sender_id = sender.id
JOIN user receiver ON m.receiver_id = receiver.id
ORDER BY m.sent_at DESC;

-- Lưu ý: 
-- Password cho tất cả users test: password123
-- Các email có thể login:
-- - user1@example.com
-- - user2@example.com  
-- - user3@example.com
-- - admin@example.com
