
# MODULE CHAT WEBSOCKET DOCS

## 🌎 Tổng quan

Module chat này sử dụng **Spring Boot** với **WebSocket** (sử dụng STOMP) để xử lý tin nhắn real-time và **REST API** để quản lý hội thoại (lấy lịch sử, tạo phòng...).

* **Xác thực:** Sử dụng **JWT (JSON Web Token)**.
* **Kết nối Real-time:** `/ws`
* **API REST:** `/api/v1/conversations`

---

## 🔐 1. Xác thực (Quan trọng)

Tất cả các kết nối (cả WebSocket và REST) đều yêu cầu xác thực.

* **REST API:** Gửi JWT qua header `Authorization` như bình thường.

  ```
  Authorization: Bearer <your_jwt_token>

  ```
* **WebSocket (STOMP):** Khi client thực hiện kết nối STOMP, bạn **BẮT BUỘC** phải gửi JWT token qua **native header** của STOMP.

  *Dựa trên file `JwtChannelInterceptor.java`,* hệ thống sẽ chặn mọi kết nối `CONNECT` không có header này.

  **Ví dụ (sử dụng thư viện `stompjs`):**

  JavaScript

  ```js
  import { Client } from '@stomp/stompjs';

  const client = new Client({
    brokerURL: 'ws://your-domain.com/ws', // Endpoint kết nối
    connectHeaders: {
      Authorization: 'Bearer <your_jwt_token>' // Header xác thực
    },
    // ... các cấu hình khác
  });

  client.activate();
  ```

---

## 🔌 2. Kết nối WebSocket & Các Tiền tố

*Dựa trên file `WebSocketConfig.java`.*

* **Endpoint kết nối (Handshake):** `/ws`
* **Tiền tố Gửi (Send):** `/app`

    + Tất cả các tin nhắn client gửi lên server phải có tiền tố này.
    + Ví dụ: Để gửi tin nhắn, bạn sẽ gửi đến destination `/app/chat.sendMessage`.
* **Tiền tố Nhận (Subscribe):** `/topic`

    + Client sẽ lắng nghe (subscribe) các chủ đề bắt đầu bằng `/topic` để nhận tin nhắn.
    + Ví dụ: Để nhận tin nhắn mới, bạn sẽ subscribe vào `/topic/conversation/{conversationId}`.

---

## 📡 3. API WebSocket (Real-time)

Đây là các destination mà client sẽ gửi tin nhắn đến (sử dụng `/app`) và lắng nghe (subscribe) từ (`/topic`). *Dựa trên file `ChatController.java`.*

### 3.1. Gửi tin nhắn

* **Gửi đến (Send):** `/app/chat.sendMessage`
* **Payload (Body):** `SendMessageRequestDTO`

  JSON

  ```json
  {
    "conversationId": 123,
    "content": "Hello, this is a new message!"
  }

  ```
* **Lắng nghe (Subscribe):** `/topic/conversation/{conversationId}`
* **Phản hồi (Payload nhận được):** Server sẽ gửi `ApiDataResponse<MessageDTO>` đến tất cả client đang subscribe chủ đề này.

  JSON

  ```json
  {
    "statusCode": 200,
    "message": "New message sent to /topic/conversation/123",
    "data": {
      "id": "msg_abc",
      "conversationId": 123,
      "senderUserId": 123,
      "content": "Hello, this is a new message!",
      "createAt": "2025-11-01T14:00:00Z"
    }
  }
  ```

### 3.2. Xóa tin nhắn

* **Gửi đến (Send):** `/app/chat.deleteMessage`
* **Payload (Body):** `DeleteMessageDto`

  JSON

  ```json
  {
    "conversationId": 123,
    "messageId": 456
  }
  ```
* **Lắng nghe (Subscribe):** `/topic/conversation/{conversationId}`
* **Phản hồi (Payload nhận được):** Server sẽ gửi một thông báo "đã xóa" để client cập nhật UI.

  JSON

  ```json
  {
    "statusCode": 200,
    "message": "Message deleted",
    "data": {
      "messageId": 456
    }
  }
  ```

### 3.3. Đánh dấu đã đọc

Hành động này được kích hoạt khi người dùng mở một cuộc hội thoại và xem tin nhắn.

* **Gửi đến (Send):** `/app/chat.markAsRead`
* **Payload (Body):** `MarkAsReadRequestDTO`

  JSON

  ```json
  {
    "conversationId": 123
  }
  ```
* **Lắng nghe (Subscribe):** `/topic/conversation/{conversationId}`
* **Phản hồi (Payload nhận được):** Server sẽ gửi `ApiDataResponse<ReadReceiptDTO>` để các client khác (ví dụ: người gửi) cập nhật trạng thái "đã xem".

  JSON

  ```json
  {
    "statusCode": 200,
    "message": "Marked as read",
    "data": {
      "conversationId": 123,
      "userId": 789, 
      "readAt": "2025-11-01T14:05:00Z"
    }
  }
  ```

---

## 📞 4. API RESTful (Quản lý Hội thoại)

Đây là các API HTTP truyền thống dùng để lấy dữ liệu nền, lịch sử, và quản lý hội thoại. *Dựa trên file `ConversationController.java`.*

**Base URL:** `/api/v1/conversations`

### 4.1. Lấy danh sách hội thoại (Inbox)

* **Endpoint:** `GET /api/v1/conversations`
* **Mô tả:** Lấy danh sách các cuộc hội thoại của user hiện tại (đã phân trang).
* **Query Params (Pagination):**

    + `page`: số trang (mặc định 0)
    + `size`: số lượng (mặc định 20)
    + `sort`: sắp xếp (mặc định `updatedAt`)
* **Phản hồi (Success):** `ApiPageResponse<ConversationSummaryDTO>`

### 4.2. Lấy tin nhắn cũ trong hội thoại

* **Endpoint:** `GET /api/v1/conversations/{conversationId}/messages`
* **Mô tả:** Lấy lịch sử tin nhắn của một hội thoại cụ thể (đã phân trang).
* **Query Params (Pagination):**

    + `page`: số trang (mặc định 0)
    + `size`: số lượng (mặc định 20)
    + `sort`: sắp xếp (mặc định `createAt`)
* **Phản hồi (Success):** `ApiPageResponse<MessageDTO>`

### 4.3. Tìm hoặc tạo hội thoại 1-1

* **Endpoint:** `POST /api/v1/conversations/private`
* **Mô tả:** Dùng khi user A muốn chat với user B. Nếu hội thoại đã tồn tại, trả về hội thoại đó. Nếu chưa, tạo hội thoại mới và trả về.
* **Body (Request):** `PrivateConversationRequestDTO`

  JSON

  ```
  {
    "otherUserId": 555 // ID của người dùng kia
  }

  ```
* **Phản hồi (Success):** `ApiDataResponse<ConversationSummaryDTO>`

### 4.4. Lấy chi tiết hội thoại

* **Endpoint:** `GET /api/v1/conversations/{conversationId}`
* **Mô tả:** Lấy thông tin chi tiết của hội thoại, bao gồm danh sách thành viên.
* **Phản hồi (Success):** `ApiDataResponse<ConversationDetailsDTO>`

### 4.5. Rời/Xóa hội thoại

* **Endpoint:** `DELETE /api/v1/conversations/{conversationId}/leave`
* **Mô tả:** User hiện tại rời khỏi cuộc hội thoại.
* **Phản hồi (Success):** `ApiDataResponse<Void>` (thường là HTTP 204 No Content)

---

## 📋 5. Luồng hoạt động gợi ý (Example Flow)

1. **Đăng nhập:** Người dùng đăng nhập (qua API auth riêng), nhận về **JWT Token**.
2. **Khởi tạo Chat:**

    * FE khởi tạo STOMP client, kết nối đến `ws://your-domain.com/ws` và gửi `Authorization: Bearer <token>` trong `connectHeaders`.
3. **Tải Inbox:**

    * FE gọi `GET /api/v1/conversations` để lấy danh sách hội thoại (`ConversationSummaryDTO[]`).
4. **Lắng nghe (Subscribe):**

    * FE lặp qua danh sách hội thoại và **subscribe** vào từng chủ đề: `/topic/conversation/{id}` cho mỗi hội thoại. Điều này để nhận tin nhắn mới hoặc cập nhật trạng thái (đã xem, xóa) cho *tất cả* các hội thoại trong inbox.
5. **Mở một Hội thoại:**

    * Người dùng nhấp vào hội thoại `conversationId = 123`.
    * FE gọi `GET /api/v1/conversations/123/messages` để tải lịch sử tin nhắn (phân trang).
    * Sau khi hiển thị tin nhắn, FE gửi `POST /app/chat.markAsRead` với payload `{"conversationId": 123}`.
    * Các client khác (đang subscribe `/topic/conversation/123`) sẽ nhận được sự kiện `ReadReceiptDTO` và cập nhật UI "Đã xem".
6. **Gửi tin nhắn:**

    * Người dùng nhập "Hi!" và nhấn gửi.
    * FE gửi `POST /app/chat.sendMessage` với payload `{"conversationId": 123, "content": "Hi!"}`.
    * *Chính client này* và các client khác (đang subscribe `/topic/conversation/123`) sẽ nhận được `MessageDTO` mới và thêm vào danh sách tin nhắn.

---
