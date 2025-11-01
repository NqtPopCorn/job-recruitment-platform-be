# 📖 Resume API

Tài liệu này định nghĩa các endpoint để quản lý hồ sơ (CV) của ứng viên.

## 📌 DTOs (Data Transfer Objects)

Cấu trúc `data` sẽ sử dụng các đối tượng sau.

### ResumeResponseDto Object

| Field | Type      | Description |
| --- |-----------| --- |
| `id` | `number`  | ID của hồ sơ |
| `candidateId` | `number`  | ID của ứng viên sở hữu |
| `fileName` | `string`  | Tên file gốc do người dùng đặt (ví dụ: "My\_CV\_2025.pdf") |
| `status` | `boolean` | Trạng thái (ví dụ: `true` là CV chính/active) |
| `url` | `string`  | Đường dẫn (URL) để tải/xem file |

### UpdateResumeDto Object

| Field | Type | Description |
| --- | --- | --- |
| `fileName` | `string` | (Optional) Tên file gốc mới |
| `status` | `boolean` | (Optional) Trạng thái active mới |

## 1. Lấy danh sách CV của ứng viên

### Description

Lấy danh sách tất cả các hồ sơ CV thuộc về một ứng viên.

GET /api/v1/resume/candidate/:id

### Headers:

```
Authorization: Bearer {{token}}
Content-Type: application/json
```

### 📌 Path Params

| Parameter | Type     | Description |
| --- |----------| --- |
| `id` | `number` | ID của ứng viên cần lấy hồ sơ |

### 📌 Response Schema

| Field | Type | Description |
| --- | --- | --- |
| `statusCode` | `number` | Mã HTTP trả về (ví dụ 200) |
| `message` | `string` | Thông báo kết quả |
| `data` | `ResumeResponseDto[]` | Mảng các đối tượng hồ sơ ứng viên |

### 📌 Example Response

```
{
  "statusCode": 200,
  "message": "Lấy danh sách hồ sơ xin việc thành công!",
  "data": [
    {
      "id": 8787878787
      "candidateId": 988989898,
      "fileName": "CV_Backend_Developer_2025.pdf",
      "status": true,
      "url": "/api/v1/files/resumes/uuid-abc-123.pdf"
    },
    {
      "id": 4254353,
      "candidateId": 5345345,
      "fileName": "CV_Data_Analyst.pdf",
      "status": false,
      "url": "/api/v1/files/resumes/uuid-xyz-789.pdf"
    }
  ]
}
```

## 2. Tải lên CV mới

### Description

Tải lên một file CV mới cho ứng viên.

POST /api/v1/resume/candidate/:id

### Headers:

```
Authorization: Bearer {{token}}
Content-Type: multipart/form-data
```

### 📌 Path Params

| Parameter | Type     | Description |
| --- |----------| --- |
| `id` | `number` | ID của ứng viên |

### 📌 Form Data

| Parameter | Type | Description |
| --- | --- | --- |
| `file` | `File` | (Required) File CV (.pdf, .doc, .docx) |
| `fileName` | `string` | (Optional) Tên file gốc. Nếu bỏ trống, sẽ dùng tên của file. |
| `status` | `boolean` | (Optional) Đặt làm CV chính. Mặc định: `true`. |

### 📌 Response Schema

| Field | Type | Description |
| --- | --- | --- |
| `statusCode` | `number` | Mã HTTP trả về (ví dụ 201) |
| `message` | `string` | Thông báo kết quả |
| `data` | `ResumeResponseDto` | Đối tượng hồ sơ vừa được tạo |

### 📌 Example Response

```
{
  "statusCode": 201,
  "message": "Tải lên hồ sơ thành công!",
  "data": {
    "id": 34324,
    "candidateId": 2342342,
    "fileName": "CV_Moi_Nhat.pdf",
    "status": true,
    "url": "/api/v1/files/resumes/uuid-new-456.pdf"
  }
}
```

## 3. Lấy chi tiết một CV

### Description

Lấy thông tin chi tiết của một hồ sơ CV cụ thể bằng ID của nó.

GET /api/v1/resume/:id

### Headers:

```
Authorization: Bearer {{token}}
Content-Type: application/json
```

### 📌 Path Params

| Parameter | Type     | Description |
| --- |----------| --- |
| `id` | `number` | ID của hồ sơ (`ResumeEntity`) |

### 📌 Response Schema

| Field | Type | Description |
| --- | --- | --- |
| `statusCode` | `number` | Mã HTTP trả về (ví dụ 200) |
| `message` | `string` | Thông báo kết quả |
| `data` | `ResumeResponseDto` | Đối tượng hồ sơ |

### 📌 Example Response

```
{
  "statusCode": 200,
  "message": "Lấy chi tiết hồ sơ thành công!",
  "data": {
    "id": 1212212,
    "candidateId": 8787674,
    "fileName": "CV_Backend_Developer_2025.pdf",
    "status": true,
    "url": "/api/v1/files/resumes/uuid-abc-123.pdf"
  }
}
```

## 4. Cập nhật thông tin CV

### Description

Cập nhật thông tin của CV (ví dụ: đổi tên file, hoặc thay đổi trạng thái `status` để đặt làm CV chính).

PUT /api/v1/resume/:id

### Headers:

```
Authorization: Bearer {{token}}
Content-Type: application/json
```

### 📌 Path Params

| Parameter | Type     | Description |
| --- |----------| --- |
| `id` | `number` | ID của hồ sơ (`ResumeEntity`) |

### 📌 Request Body (JSON)

Sử dụng `UpdateResumeDto`.

```
{
  "fileName": "CV_Backend_Chinh_Thuc.pdf",
  "status": true
}
```

### 📌 Response Schema

| Field | Type | Description |
| --- | --- | --- |
| `statusCode` | `number` | Mã HTTP trả về (ví dụ 200) |
| `message` | `string` | Thông báo kết quả |
| `data` | `ResumeResponseDto` | Đối tượng hồ sơ đã được cập nhật |

### 📌 Example Response

```
{
  "statusCode": 200,
  "message": "Cập nhật hồ sơ thành công!",
  "data": {
    "id": 6499877,
    "candidateId": 98989797987,
    "fileName": "CV_Backend_Chinh_Thuc.pdf",
    "status": true,
    "url": "/api/v1/files/resumes/uuid-abc-123.pdf"
  }
}
```

## 5. Xóa một CV

### Description

Xóa vĩnh viễn một hồ sơ CV (xóa file và record trong DB).

DELETE /api/v1/resume/:id

### Headers:

```
Authorization: Bearer {{token}}
```

### 📌 Path Params

| Parameter | Type     | Description |
| --- |----------| --- |
| `id` | `number` | ID của hồ sơ (`ResumeEntity`) |

### 📌 Response Schema

| Field | Type | Description |
| --- | --- | --- |
| `statusCode` | `number` | Mã HTTP trả về (ví dụ 200) |
| `message` | `string` | Thông báo kết quả |
| `data` | `null` | Không có dữ liệu trả về |

### 📌 Example Response

```
{
  "statusCode": 200,
  "message": "Xóa hồ sơ thành công!",
  "data": null
}
```