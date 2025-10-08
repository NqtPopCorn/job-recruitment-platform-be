# Candidate Section API

## 1. POST New Candidate Section

### 🧾 Description

Tạo một mục thông tin mới cho ứng viên (Candidate Section).  
Ví dụ: Work & Experience, Education, Projects…

---

### 📌Endpoint

```http
POST {{baseUrl}}/api/v1/candidate-about
Authorization: Bearer {{token}}
Content-Type: application/json
```

### 📌 Request Body

| Field        | Type   | Description                                 |
| ------------ | ------ | ------------------------------------------- |
| candidateId  | string | ID của ứng viên                             |
| category     | string | Loại mục (ví dụ: "Work & Experience")       |
| title        | string | Tiêu đề của mục                             |
| organization | string | Tổ chức/công ty liên quan                   |
| startTime    | string | Thời gian bắt đầu (ISODate)                 |
| endTime      | string | Thời gian kết thúc (ISODate)                |
| text         | string | Mô tả chi tiết                              |
| type         | string | DTO type (ví dụ: "CreateCandidateAboutDto") |

### 📌 Example Request Body

```json
{
  "candidateId": "68a123efc98b2cd3f1a0457a",
  "category": "Work & Experience",
  "title": "Backend Developer",
  "organization": "Công ty TNHH Công Nghệ VNP",
  "startTime": "2022-01-01T00:00:00.000Z",
  "endTime": "2024-06-01T00:00:00.000Z",
  "text": "Tham gia phát triển hệ thống quản lý người dùng bằng NestJS và PostgreSQL.",
  "type": "CreateCandidateAboutDto"
}
```

### 📌 Response Schema

| Field      | Type   | Description                 |
| ---------- | ------ | --------------------------- |
| statusCode | number | Mã trạng thái HTTP          |
| message    | string | Thông báo kết quả           |
| data       | object | Thông tin candidate vừa tạo |

### 📌 Example Response

```json
{
  "statusCode": 201,
  "message": "Tạo candidate section thành công!",
  "data": {
    "id": "68b456efc98b2cd3f1a0567b",
    "candidateId": "68a123efc98b2cd3f1a0457a",
    "category": "Work & Experience",
    "title": "Backend Developer",
    "organization": "Công ty TNHH Công Nghệ VNP",
    "startTime": "2022-01-01T00:00:00.000Z",
    "endTime": "2024-06-01T00:00:00.000Z",
    "text": "Tham gia phát triển hệ thống quản lý người dùng bằng NestJS và PostgreSQL.",
    "createdAt": "2025-10-03T08:00:00.000Z",
    "updatedAt": "2025-10-03T08:00:00.000Z"
  }
}
```

## 2. PATCH Candidate Section

### 🧾 Description

Cập nhật thông tin một mục (section) của ứng viên đã tồn tại.
Ví dụ: Work & Experience, Education, Projects…

### 📌 Endpoint

```http
PATCH {{baseUrl}}/api/v1/candidate-about/:id
Authorization: Bearer {{token}}
Content-Type: application/json
```

### 📌 Request Body

| Field        | Type   | Description                                 |
| ------------ | ------ | ------------------------------------------- |
| category     | string | Loại mục (ví dụ: "Work & Experience")       |
| title        | string | Tiêu đề của mục                             |
| organization | string | Tổ chức/công ty liên quan                   |
| startTime    | string | Thời gian bắt đầu (ISODate)                 |
| endTime      | string | Thời gian kết thúc (ISODate)                |
| text         | string | Mô tả chi tiết                              |
| type         | string | DTO type (ví dụ: "CreateCandidateAboutDto") |

### 📌 Example Request Body

{
"category": "Work & Experience",
"title": "Backend Developer",
"organization": "Công ty TNHH Công Nghệ VNP",
"startTime": "2022-01-01T00:00:00.000Z",
"endTime": "2024-06-01T00:00:00.000Z",
"text": "Tham gia phát triển hệ thống quản lý người dùng bằng NestJS và PostgreSQL.",
"type": "CreateCandidateAboutDto"
}

### 📌 Response Schema

| Field      | Type   | Description                          |
| ---------- | ------ | ------------------------------------ |
| statusCode | number | Mã trạng thái HTTP                   |
| message    | string | Thông báo kết quả                    |
| data       | object | Thông tin candidate đã được cập nhật |

### 📌 Example Response

```json
{
  "statusCode": 200,
  "message": "Cập nhật candidate section thành công!",
  "data": {
    "id": "68b456efc98b2cd3f1a0567b",
    "candidateId": "68a123efc98b2cd3f1a0457a",
    "category": "Work & Experience",
    "title": "Backend Developer",
    "organization": "Công ty TNHH Công Nghệ VNP",
    "startTime": "2022-01-01T00:00:00.000Z",
    "endTime": "2024-06-01T00:00:00.000Z",
    "text": "Tham gia phát triển hệ thống quản lý người dùng bằng NestJS và PostgreSQL.",
    "createdAt": "2025-10-03T08:00:00.000Z",
    "updatedAt": "2025-10-04T09:00:00.000Z"
  }
}
```

## 3. DELETE Candidate Section

### 🧾 Description

Xóa một mục (section) của ứng viên đã tồn tại.
Ví dụ: Work & Experience, Education, Projects…

### Endpoint

```json
DELETE {{baseUrl}}/api/v1/candidate-about/:id
Authorization: Bearer {{token}}
Content-Type: application/json
```

### 📌 Request Body

Không cần body. Chỉ cần cung cấp id của mục trong URL.

### 📌 Example Request

DELETE {{baseUrl}}/api/v1/candidate-about/68b456efc98b2cd3f1a0567b
Authorization: Bearer {{token}}

### 📌 Response Schema

| Field      | Type   | Description        |
| ---------- | ------ | ------------------ |
| statusCode | number | Mã trạng thái HTTP |
| message    | string | Thông báo kết quả  |

### 📌 Example Response

{
"statusCode": 200,
"message": "Xóa candidate section thành công!"
}

## 4. GET SECTIONS OF CANDIDATE BY ID

### 🧾 Description

Lấy danh mục các section (Educations, Works, Certificates, v.v.) của một ứng viên dựa theo ID.

### 📌 Endpoint

```http
GET {{baseUrl}}/api/v1/candidate-about/details/candidate/:candidateId
```

### 📌 Example Request

```http
GET {{baseUrl}}/api/v1/candidate-about/details/candidate/68be91be9bf7f4178721d9fe
Authorization: Bearer {{token}}
Content-Type: application/json
```

### 📌 Response Schema

| Field                                | Type     | Description                                                   |
| ------------------------------------ | -------- | ------------------------------------------------------------- |
| `statusCode`                         | `number` | Mã trạng thái HTTP trả về                                     |
| `message`                            | `string` | Thông báo phản hồi từ server                                  |
| `results`                            | `array`  | Danh sách các section của ứng viên                            |
| `results[].category`                 | `string` | Tên loại section (ví dụ: `Educations`, `Works & Experiences`) |
| `results[].themeColor`               | `string` | Màu chủ đề của section                                        |
| `results[].blockList`                | `array`  | Danh sách các khối nội dung trong section                     |
| `results[].blockList[].id`           | `string` | ID của block                                                  |
| `results[].blockList[].meta`         | `string` | Mã meta của block                                             |
| `results[].blockList[].title`        | `string` | Tiêu đề nội dung (ví dụ: tên ngành học, công việc, v.v.)      |
| `results[].blockList[].organization` | `string` | Tên tổ chức hoặc công ty                                      |
| `results[].blockList[].time`         | `string` | Khoảng thời gian thực hiện                                    |
| `results[].blockList[].text`         | `string` | Mô tả chi tiết nội dung                                       |

### 📌 Example Response

```json
{
  "statusCode": 200,
  "message": "Lấy danh mục chứng chỉ của ứng viên theo id thành công!",
  "results": [
    {
      "category": "Educations",
      "themeColor": "",
      "blockList": [
        {
          "id": "68d80254a7e42d8e6797c032",
          "meta": "I",
          "title": "Information Technology",
          "organization": "Đại học Sài Gòn",
          "time": "Sep 01, 2022 - Jan 01, 2027",
          "text": "Tôi là 1 sinh viên và mong muốn trở thành kỹ sư phần mềm trong tương lai."
        },
        {
          "id": "68d804cba7e42d8e6797c039",
          "meta": "I",
          "title": "Intern Backend Developer",
          "organization": "Google Global Enterprise",
          "time": "Sep 01, 2025 - Present",
          "text": "Tôi có cơ hội tham gia vào dự án của công ty trên môi trường thực tế."
        }
      ]
    },
    {
      "category": "Works & Experiences",
      "themeColor": "theme-blue",
      "blockList": [
        {
          "id": "68d8f64961e929e4409bd864",
          "meta": "I",
          "title": "Intern Frontend Developer",
          "organization": "CTY TNHH 1 Thành Viên",
          "time": "Sep 01, 2025 - Present",
          "text": ""
        }
      ]
    }
  ]
}
```
