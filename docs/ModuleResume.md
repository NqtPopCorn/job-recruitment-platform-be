# Resume API

## 1. Get Resume List Of Candidate

### Description

Lấy danh sách công việc phân trang

- **Endpoint:**
  GET /api/v1/resume/candidate/:id

- **Headers:**

```http
Authorization: Bearer {{token}}
Content-Type: application/json
```

### 📌 Path Params

| Parameter | Type   | Description                   |
| --------- | ------ | ----------------------------- |
| `id`      | string | ID của ứng viên cần lấy hồ sơ |

### 📌 Response Schema

| Field        | Type                  | Description                       |
| ------------ | --------------------- | --------------------------------- |
| `statusCode` | `number`              | Mã HTTP trả về (ví dụ 200)        |
| `message`    | `string`              | Thông báo kết quả                 |
| `results`    | `ResumeResponseDto[]` | Mảng các đối tượng hồ sơ ứng viên |

### 📌 ResumeResponseDto Object

| Field         | Type             | Description                                  |
| ------------- | ---------------- | -------------------------------------------- |
| `id`          | `string`         | ID của hồ sơ                                 |
| `candidateId` | `string`         | ID của ứng viên                              |
| `fileName`    | `string \| null` | Tên file hồ sơ (có thể null nếu chưa upload) |

### 📌 Example Response

```json
{
  "statusCode": 200,
  "message": "Lấy danh sách hồ sơ xin việc thành công!",
  "results": [
    {
      "id": "64f1a23b5a8f9d1234567890",
      "candidateId": "68be91be9bf7f4178721d9fe",
      "fileName": "Resume_JohnDoe-1699999999999.pdf"
    },
    {
      "id": "64f1a23b5a8f9d1234567891",
      "candidateId": "68be91be9bf7f4178721d9fe",
      "fileName": "Resume_JohnDoe-1699999998888.pdf"
    }
  ]
}
```
