# Candidate API

## 1. GET LIST PAGINATION CANDIDATE

### 🧾 Description

Lấy danh sách hồ sơ ứng viên có hỗ trợ phân trang, lọc, và tìm kiếm theo nhiều tiêu chí như tên, giới tính, vị trí, ngành nghề, trình độ, kinh nghiệm...

- **Endpoint:**
  GET /api/v1/candidate?page=1&size=10

- **Headers:**

```http
Authorization: Bearer {{token}}
Content-Type: application/json
```

### 📌 Query Parameters

| Field      | Type   | Required | Description                                               |
| ---------- | ------ | -------- | --------------------------------------------------------- |
| page       | number | ✅ Yes   | Số trang hiện tại (bắt đầu từ `1`)                        |
| size       | number | ✅ Yes   | Số lượng hồ sơ trên mỗi trang                             |
| sort       | string | ❌ No    | Trường cần sắp xếp (`createdAt`, `name`, `hourlyRate`...) |
| search     | string | ❌ No    | Từ khóa tìm kiếm (theo tên, vị trí, ngành nghề)           |
| location   | string | ❌ No    | Lọc theo địa điểm làm việc                                |
| industry   | string | ❌ No    | Lọc theo ngành nghề                                       |
| experience | number | ❌ No    | Lọc theo số năm kinh nghiệm                               |
| education  | string | ❌ No    | Lọc theo trình độ học vấn                                 |
| gender     | string | ❌ No    | Lọc theo giới tính (`male`, `female`, `other`)            |

### 📌 Example Response

```json
{
  "statusCode": 200,
  "message": "Lấy danh sách hồ sơ ứng viên phân trang thành công!",
  "results": [
    {
      "id": "686e953e9a1be3274b59919a",
      "avatar": "https://example.com/avatar.jpg",
      "name": "Tăng Thành Trung",
      "designation": "Backend Developer",
      "location": "Hồ Chí Minh",
      "hourlyRate": 30,
      "tags": ["JavaScript", "NestJS", "MongoDB"],
      "category": "Information Technology",
      "gender": "male",
      "createdAt": "09/07/2025",
      "socialMedias": [
        { "platform": "LinkedIn", "url": "https://linkedin.com/in/username" },
        { "platform": "GitHub", "url": "https://github.com/username" }
      ],
      "status": false
    },
    {
      "id": "68be91be9bf7f4178721d9fe",
      "avatar": "file-xxxx.jpg",
      "name": "Nguyễn Văn A",
      "designation": "Frontend Developer",
      "location": "Hà Nội",
      "hourlyRate": 20,
      "tags": ["React", "TypeScript"],
      "category": "Information Technology",
      "gender": "female",
      "createdAt": "08/09/2025",
      "socialMedias": [],
      "status": true
    }
  ],
  "meta": {
    "totalItems": 2,
    "currentPage": 1,
    "pageSize": 10,
    "totalPages": 1
  }
}
```

### 📌 Response Schema

| Field                  | Type    | Description                                    |
| ---------------------- | ------- | ---------------------------------------------- |
| statusCode             | number  | Mã trạng thái HTTP                             |
| message                | string  | Thông báo kết quả trả về                       |
| results                | array   | Danh sách hồ sơ ứng viên                       |
| results[].id           | string  | ID của ứng viên                                |
| results[].avatar       | string  | Ảnh đại diện                                   |
| results[].name         | string  | Họ và tên ứng viên                             |
| results[].designation  | string  | Vị trí hoặc chức danh                          |
| results[].location     | string  | Địa điểm làm việc                              |
| results[].hourlyRate   | number  | Mức lương theo giờ                             |
| results[].tags         | array   | Danh sách kỹ năng của ứng viên                 |
| results[].category     | string  | Ngành nghề hoặc lĩnh vực                       |
| results[].gender       | string  | Giới tính của ứng viên                         |
| results[].createdAt    | string  | Ngày tạo hồ sơ                                 |
| results[].socialMedias | array   | Danh sách mạng xã hội (LinkedIn, GitHub, v.v.) |
| results[].status       | boolean | Trạng thái hiển thị của hồ sơ                  |
| meta                   | object  | Thông tin phân trang                           |
| meta.totalItems        | number  | Tổng số ứng viên                               |
| meta.currentPage       | number  | Trang hiện tại                                 |
| meta.pageSize          | number  | Số lượng ứng viên trên mỗi trang               |
| meta.totalPages        | number  | Tổng số trang                                  |

## 2. POST NEW CANDIDATE

### 🧾 Description

Tạo mới một hồ sơ ứng viên (Candidate) trong hệ thống.
Thông tin hồ sơ bao gồm các trường cơ bản như kỹ năng, vị trí, trình độ học vấn, mức lương mong muốn, v.v.

### 📌 Endpoint

- **Endpoint:**
  POST /api/v1/candidate

- **Headers:**

```http
Authorization: Bearer {{token}}
Content-Type: application/json
```

### 📌 Request Body

| Field          | Type          | Required | Description                                     |
| -------------- | ------------- | -------- | ----------------------------------------------- |
| userId         | string        | ✅ Yes   | ID của người dùng (User) liên kết với ứng viên  |
| name           | string        | ✅ Yes   | Họ và tên ứng viên                              |
| birthday       | string / null | ❌ No    | Ngày sinh (định dạng ISO hoặc `null`)           |
| phone          | string        | ✅ Yes   | Số điện thoại                                   |
| industry       | string        | ✅ Yes   | Ngành nghề làm việc                             |
| skills         | array         | ✅ Yes   | Danh sách kỹ năng của ứng viên                  |
| avatar         | string        | ❌ No    | Ảnh đại diện của ứng viên                       |
| designation    | string        | ✅ Yes   | Vị trí / chức danh hiện tại                     |
| location       | string        | ✅ Yes   | Địa chỉ nơi ở hoặc làm việc                     |
| hourlyRate     | number / null | ❌ No    | Mức lương theo giờ                              |
| description    | string        | ❌ No    | Mô tả ngắn về bản thân, kinh nghiệm, định hướng |
| experience     | string        | ❌ No    | Số năm kinh nghiệm                              |
| currentSalary  | string        | ❌ No    | Mức lương hiện tại                              |
| expectedSalary | string        | ❌ No    | Mức lương mong muốn                             |
| gender         | string        | ✅ Yes   | Giới tính (`male`, `female`, `other`)           |
| language       | array         | ❌ No    | Ngôn ngữ sử dụng                                |
| educationLevel | string        | ❌ No    | Trình độ học vấn cao nhất                       |
| socialMedias   | array         | ❌ No    | Danh sách mạng xã hội (LinkedIn, GitHub, v.v.)  |

### 📌 Example Request Body

```json
{
  "userId": "686cb5b802a159956bb2a370",
  "name": "Tăng Thành Trung",
  "birthday": null,
  "phone": "0773735100",
  "industry": "Infomation Technology",
  "skills": ["JavaScript", "TypeScript", "ReactJS", "NodeJS"],
  "avatar": "",
  "designation": "Backend Developer",
  "location": "63/2 Tân Hóa Phường Phú Lâm",
  "hourlyRate": null,
  "description": "Tôi là một lập trình viên backend với hơn 3 năm kinh nghiệm trong việc phát triển các ứng dụng web và dịch vụ RESTful. Tôi có kỹ năng vững chắc trong việc sử dụng Node.js, Express, và MongoDB để xây dựng các hệ thống hiệu quả và mở rộng được.",
  "experience": "",
  "currentSalary": "",
  "expectedSalary": "",
  "gender": "male",
  "language": ["Tiếng Việt", "English"],
  "educationLevel": "Đại học",
  "socialMedias": []
}
```

### 📌 Example Response — ✅ Thành công

```json
{
  "statusCode": 201,
  "message": "Tạo hồ sơ ứng viên thành công!",
  "data": {
    "userId": "66af1c3a8a5fbd49fbb12345",
    "name": "Tăng Thành Trung",
    "birthday": "1999-07-09",
    "phone": "0987654321",
    "industry": "Information Technology",
    "skills": ["JavaScript", "NestJS", "MongoDB"],
    "avatar": "https://example.com/avatar.jpg",
    "designation": "Backend Developer",
    "country": "Vietnam",
    "city": "Hồ Chí Minh",
    "location": "63/2 Tân Hóa",
    "hourlyRate": 30,
    "description": "Backend developer with 3 years of experience.",
    "experience": "3 năm",
    "currentSalary": "1000 USD",
    "expectedSalary": "2000 USD",
    "gender": "male",
    "language": ["English", "Vietnamese"],
    "educationLevel": "Bachelor",
    "socialMedias": [
      { "platform": "LinkedIn", "url": "https://linkedin.com/in/username" },
      { "platform": "GitHub", "url": "https://github.com/username" }
    ],
    "status": true
  }
}
```

### 📌 Example Response — ❌ Thất bại

```json
{
  "success": false,
  "statusCode": 409,
  "error": "Hồ sơ ứng viên đã tồn tại!",
  "message": "Hồ sơ ứng viên đã tồn tại"
}
```

### 📌 Response Schema

| Field      | Type    | Description                                                 |
| ---------- | ------- | ----------------------------------------------------------- |
| statusCode | number  | Mã trạng thái HTTP                                          |
| message    | string  | Thông báo phản hồi                                          |
| data       | object  | Thông tin chi tiết hồ sơ ứng viên vừa tạo                   |
| success    | boolean | Trạng thái thành công hoặc thất bại (chỉ có ở phản hồi lỗi) |
| error      | string  | Mô tả lỗi (nếu có)                                          |

## 3. GET DETAIL CANDIDATE BY ID

### 🧾 Description

Lấy thông tin chi tiết của một hồ sơ ứng viên (Candidate) theo candidateId.
API này trả về toàn bộ dữ liệu hồ sơ, bao gồm kỹ năng, trình độ, mức lương, mô tả, ngôn ngữ và các mạng xã hội liên kết.

### 📌 Endpoint

```http
GET {{baseUrl}}/api/v1/candidate/details/:id
Authorization: Bearer {{token}}
Content-Type: application/json
```

### 📌 Path Parameter

| Field | Type   | Required | Description                                |
| ----- | ------ | -------- | ------------------------------------------ |
| id    | string | ✅ Yes   | ID của ứng viên cần lấy thông tin chi tiết |

### 📌 Example Request

```json
{
  "userId": "686f5683f6e123fa2042954f",
  "name": "Nguyễn Văn A",
  "birthday": "1998-05-20T00:00:00.000Z",
  "phone": "0987654321",
  "industry": "Information Technology",
  "skills": ["JavaScript", "NestJS", "React"],
  "avatar": "file-1753588732487-4780477.jpg",
  "designation": "Backend Developer",
  "country": "Vietnam",
  "city": "Hồ Chí Minh",
  "location": "Quận 1, TP.HCM",
  "hourlyRate": 15,
  "description": "Lập trình viên backend có 2 năm kinh nghiệm.",
  "experience": "2 năm làm việc với NodeJS/NestJS",
  "currentSalary": "15,000,000 VND",
  "expectedSalary": "20,000,000 VND",
  "gender": "Male",
  "language": ["Tiếng Việt", "Tiếng Anh"],
  "educationLevel": "Đại học",
  "socialMedias": [
    {
      "platform": "linkedin",
      "url": "https://linkedin.com/in/nguyenvana",
      "type": "SocialMedia"
    },
    {
      "platform": "github",
      "url": "https://github.com/nguyenvana",
      "type": "SocialMedia"
    }
  ],
  "status": true,
  "type": "CreateCandidateDto"
}
```

### 📌 Example Response — ✅ Thành công

```json
{
  "statusCode": 200,
  "message": "Lấy hồ sơ ứng viên theo id thành công!",
  "data": {
    "id": "68be91be9bf7f4178721d9fe",
    "userId": "686cb5b802a159956bb2a370",
    "avatar": "file-1757402215941-519300144.jpg",
    "name": "Tăng Thành Trung",
    "designation": "Backend Developer",
    "location": "63/2 Tân Hóa",
    "hourlyRate": 0,
    "tags": ["JavaScript", "NestJS", "MongoDB"],
    "category": "Information Technology",
    "gender": "male",
    "createdAt": "08/09/2025",
    "experience": 0,
    "qualification": "Đại học",
    "currentSalary": "1,000,000 VND",
    "expectedSalary": "5,000,000 VND",
    "description": "Tôi là một lập trình viên backend với hơn 3 năm kinh nghiệm...",
    "language": ["Tiếng Việt", "English"],
    "socialMedias": []
  }
}
```

### 📌 Response Schema

| Field               | Type   | Description                           |
| ------------------- | ------ | ------------------------------------- |
| statusCode          | number | Mã trạng thái HTTP                    |
| message             | string | Thông báo kết quả                     |
| data                | object | Thông tin chi tiết của ứng viên       |
| data.id             | string | ID của ứng viên                       |
| data.userId         | string | ID người dùng liên kết                |
| data.avatar         | string | Ảnh đại diện của ứng viên             |
| data.name           | string | Tên ứng viên                          |
| data.designation    | string | Chức danh hoặc vị trí hiện tại        |
| data.location       | string | Địa điểm làm việc                     |
| data.hourlyRate     | number | Mức lương theo giờ                    |
| data.tags           | array  | Danh sách kỹ năng                     |
| data.category       | string | Ngành nghề hoặc lĩnh vực              |
| data.gender         | string | Giới tính (`male`, `female`, `other`) |
| data.createdAt      | string | Ngày tạo hồ sơ                        |
| data.experience     | number | Số năm kinh nghiệm                    |
| data.qualification  | string | Trình độ học vấn                      |
| data.currentSalary  | string | Mức lương hiện tại                    |
| data.expectedSalary | string | Mức lương mong muốn                   |
| data.description    | string | Mô tả chi tiết về ứng viên            |
| data.language       | array  | Danh sách ngôn ngữ sử dụng            |
| data.socialMedias   | array  | Danh sách mạng xã hội liên kết        |

## 4. GET LIST CANDIDATE

### 🧾 Description

Lấy danh sách hồ sơ ứng viên

### 📌 Endpoint

```http
GET {{baseUrl}}/api/v1/candidate/get-list
```

### 📌 Example Request

```http
GET {{baseUrl}}/api/v1/candidate/get-list
Authorization: Bearer {{token}}
Content-Type: application/json
```

### 📌 Example Response — ✅ Thành công

```json
{
  "statusCode": 200,
  "message": "Lấy danh sách hồ sơ ứng viên thành công!",
  "results": [
    {
      "_id": "68be91be9bf7f4178721d9fe",
      "userId": "686cb5b802a159956bb2a370",
      "name": "Tăng Thành Trung",
      "industry": "Information Technology",
      "skills": ["JavaScript", "NestJS", "MongoDB"],
      "birthday": "2004-05-22T00:00:00.000Z",
      "avatar": "file-1757402215941-519300144.jpg",
      "phone": "0773735100",
      "designation": "Backend Developer",
      "location": "63/2 Tân Hóa",
      "hourlyRate": 0,
      "description": "Tôi là một lập trình viên backend với hơn 3 năm kinh nghiệm...",
      "experience": 0,
      "currentSalary": "1,000,000 VND",
      "expectedSalary": "5,000,000 VND",
      "gender": "male",
      "language": ["Tiếng Việt", "English"],
      "educationLevel": "Đại học",
      "socialMedias": [],
      "city": "HCM",
      "country": "Vietnam",
      "status": false,
      "isDeleted": false,
      "createdAt": "2025-09-08T08:20:14.763Z",
      "updatedAt": "2025-09-15T03:35:40.250Z",
      "__v": 0
    }
  ]
}
```

### 📌 Response Schema

| Field                  | Type    | Description                                |
| ---------------------- | ------- | ------------------------------------------ |
| statusCode             | number  | Mã trạng thái HTTP                         |
| message                | string  | Thông báo kết quả                          |
| results                | array   | Danh sách các hồ sơ ứng viên               |
| results.\_id           | string  | ID của ứng viên                            |
| results.userId         | string  | ID người dùng liên kết                     |
| results.name           | string  | Tên ứng viên                               |
| results.industry       | string  | Ngành nghề hoặc lĩnh vực                   |
| results.skills         | array   | Danh sách kỹ năng của ứng viên             |
| results.birthday       | string  | Ngày sinh của ứng viên                     |
| results.avatar         | string  | Ảnh đại diện của ứng viên                  |
| results.phone          | string  | Số điện thoại liên hệ                      |
| results.designation    | string  | Chức danh hoặc vị trí hiện tại             |
| results.location       | string  | Địa điểm làm việc                          |
| results.hourlyRate     | number  | Mức lương theo giờ                         |
| results.description    | string  | Mô tả chi tiết ngắn gọn về ứng viên        |
| results.experience     | number  | Số năm kinh nghiệm                         |
| results.currentSalary  | string  | Mức lương hiện tại                         |
| results.expectedSalary | string  | Mức lương mong muốn                        |
| results.gender         | string  | Giới tính (`male`, `female`, `other`)      |
| results.language       | array   | Danh sách ngôn ngữ ứng viên sử dụng        |
| results.educationLevel | string  | Trình độ học vấn                           |
| results.socialMedias   | array   | Danh sách mạng xã hội liên kết             |
| results.city           | string  | Thành phố cư trú                           |
| results.country        | string  | Quốc gia                                   |
| results.status         | boolean | Trạng thái hồ sơ (đã kích hoạt hay chưa)   |
| results.isDeleted      | boolean | Đánh dấu đã xóa mềm hay chưa               |
| results.createdAt      | string  | Ngày tạo hồ sơ                             |
| results.updatedAt      | string  | Ngày cập nhật hồ sơ                        |
| results.\_\_v          | number  | Phiên bản dữ liệu trong MongoDB (auto-gen) |

## 5. GET LIST INDUSTRY

### 🧾 Description

Lấy danh sách các ngành nghề (Industry) mà các ứng viên đã đăng ký trong hệ thống.
API này giúp hiển thị danh sách lựa chọn ngành nghề trong bộ lọc hoặc khi tạo hồ sơ ứng viên mới.

### 📌 Endpoint

```http
GET {{baseUrl}}/api/v1/candidate/industry-list
Authorization: Bearer {{token}}
Content-Type: application/json
```

### 📌 Example Request

```http
GET {{baseUrl}}/api/v1/candidate/industry-list
Authorization: Bearer {{token}}
Content-Type: application/json
```

### 📌 Example Response — ✅ Thành công

```json
{
  "statusCode": 200,
  "message": "Lấy danh sách danh mục của các ứng viên thành công!",
  "data": ["Information Technology"]
}
```

### 📌 Response Schema

| Field        | Type   | Description                              |
| ------------ | ------ | ---------------------------------------- |
| statusCode   | number | Mã trạng thái HTTP                       |
| message      | string | Thông báo kết quả                        |
| data         | array  | Danh sách các ngành nghề (Industry list) |
| data[].value | string | Tên ngành nghề của ứng viên              |

## 6. GET DETAIL CANDIDATE BY USER ID

### 🧾 Description

Lấy thông tin chi tiết của hồ sơ ứng viên (Candidate) dựa trên userId.
API này thường được sử dụng sau khi người dùng đăng nhập để lấy hồ sơ cá nhân tương ứng với tài khoản của họ.

### 📌 Endpoint

```http
GET {{baseUrl}}/api/v1/candidate/details/user/:id
Authorization: Bearer {{token}}
Content-Type: application/json
```

### 📌 Path Parameter

| Field | Type   | Required | Description                                  |
| ----- | ------ | -------- | -------------------------------------------- |
| id    | string | ✅ Yes   | ID của người dùng (userId) cần lấy thông tin |

### 📌 Example Request

```http
GET {{baseUrl}}/api/v1/candidate/details/user/686cb5b802a159956bb2a370
Authorization: Bearer {{token}}
Content-Type: application/json
```

### 📌 Example Response — ✅ Thành công

```json
{
  "statusCode": 200,
  "message": "Lấy thông tin ứng viên thành công!",
  "data": {
    "id": "68be91be9bf7f4178721d9fe",
    "userId": "686cb5b802a159956bb2a370",
    "email": "thanhtrung22052004@gmail.com",
    "avatar": "file-1757402215941-519300144.jpg",
    "name": "Tăng Thành Trung",
    "industry": "Information Technology",
    "designation": "Backend Developer",
    "country": "Vietnam",
    "city": "HCM",
    "location": "63/2 Tân Hóa",
    "hourlyRate": 0,
    "tags": ["JavaScript", "NestJS", "MongoDB"],
    "category": "Information Technology",
    "gender": "male",
    "createdAt": "2025-09-08T00:00:00.000Z",
    "experience": 0,
    "qualification": "Đại học",
    "birthday": "2004-05-22T00:00:00.000Z",
    "phone": "0773735100",
    "currentSalary": 1000000,
    "expectedSalary": 5000000,
    "currency": "VND",
    "description": "Tôi là một lập trình viên backend với hơn 3 năm kinh nghiệm trong việc phát triển các ứng dụng web và dịch vụ RESTful. Tôi có kỹ năng vững chắc trong việc sử dụng Node.js, Express, và MongoDB để xây dựng các hệ thống hiệu quả và mở rộng được. Tôi đam mê công nghệ và luôn cập nhật những xu hướng mới nhất trong lĩnh vực phát triển phần mềm tôi muốn đóng góp vào công ty mà tôi tham gia.",
    "language": ["Tiếng Việt", "English"],
    "socialMedias": [
      {
        "platform": "LinkedIn",
        "url": "https://linkedin.com/in/trung"
      },
      {
        "platform": "GitHub",
        "url": "https://github.com/trung"
      }
    ],
    "status": false
  }
}
```

### 📌 Response Schema

| Field               | Type    | Description                           |
| ------------------- | ------- | ------------------------------------- |
| statusCode          | number  | Mã trạng thái HTTP                    |
| message             | string  | Thông báo kết quả                     |
| data                | object  | Thông tin chi tiết của ứng viên       |
| data.id             | string  | ID của hồ sơ ứng viên                 |
| data.userId         | string  | ID người dùng tương ứng               |
| data.email          | string  | Email của ứng viên                    |
| data.avatar         | string  | Ảnh đại diện của ứng viên             |
| data.name           | string  | Họ và tên ứng viên                    |
| data.industry       | string  | Ngành nghề                            |
| data.designation    | string  | Chức danh hoặc vị trí hiện tại        |
| data.country        | string  | Quốc gia sinh sống                    |
| data.city           | string  | Thành phố sinh sống                   |
| data.location       | string  | Địa chỉ cụ thể                        |
| data.hourlyRate     | number  | Mức lương theo giờ                    |
| data.tags           | array   | Danh sách kỹ năng (tags)              |
| data.category       | string  | Lĩnh vực làm việc                     |
| data.gender         | string  | Giới tính (`male`, `female`, `other`) |
| data.createdAt      | string  | Thời điểm tạo hồ sơ                   |
| data.experience     | number  | Số năm kinh nghiệm                    |
| data.qualification  | string  | Trình độ học vấn                      |
| data.birthday       | string  | Ngày sinh                             |
| data.phone          | string  | Số điện thoại liên hệ                 |
| data.currentSalary  | number  | Mức lương hiện tại                    |
| data.expectedSalary | number  | Mức lương mong muốn                   |
| data.currency       | string  | Đơn vị tiền tệ                        |
| data.description    | string  | Giới thiệu chi tiết về ứng viên       |
| data.language       | array   | Danh sách ngôn ngữ ứng viên sử dụng   |
| data.socialMedias   | array   | Danh sách mạng xã hội của ứng viên    |
| data.status         | boolean | Trạng thái hồ sơ ứng viên             |

### ⚠️ Example Response — ❌ Không tìm thấy

```json
{
  "statusCode": 404,
  "message": "Không tìm thấy hồ sơ ứng viên cho userId này!"
}
```

## 7. GET LIST SKILLS

### 🧾 Description

Lấy danh sách các kỹ năng (Skills) mà các ứng viên đã đăng ký trong hệ thống.
API này được sử dụng để hiển thị danh sách kỹ năng trong bộ lọc, autocomplete, hoặc khi tạo hồ sơ ứng viên mới.

### 📌 Endpoint

```http
GET {{baseUrl}}/api/v1/candidate/skill-list
Authorization: Bearer {{token}}
Content-Type: application/json
```

### 📌 Example Request

```http
GET {{baseUrl}}/api/v1/candidate/skill-list
Authorization: Bearer {{token}}
Content-Type: application/json

```

### 📌 Example Response — ✅ Thành công

```json
{
  "statusCode": 200,
  "message": "Lấy danh sách kỹ năng thành công!",
  "data": ["JavaScript", "MongoDB", "NestJS"]
}
```

### 📌 Response Schema

| Field        | Type   | Description                         |
| ------------ | ------ | ----------------------------------- |
| statusCode   | number | Mã trạng thái HTTP                  |
| message      | string | Thông báo kết quả                   |
| data         | array  | Danh sách các kỹ năng (Skills list) |
| data[].value | string | Tên kỹ năng của ứng viên            |

## 8. PATCH CANDIDATE

### 🧾 Description

Cập nhật thông tin hồ sơ ứng viên (Candidate) theo candidateId.
API này cho phép người dùng chỉnh sửa thông tin cá nhân, kỹ năng, mức lương, hoặc các mạng xã hội đã liên kết.

### 📌 Endpoint

```http
PATCH {{baseUrl}}/api/v1/company/:id
Authorization: Bearer {{token}}
Content-Type: application/json
```

### 📌 Path Parameter

| Field | Type   | Required | Description                  |
| ----- | ------ | -------- | ---------------------------- |
| id    | string | ✅ Yes   | ID của ứng viên cần cập nhật |

### 📌 Example Request

```json
{
  "name": "Nguyễn Văn A",
  "birthday": "1998-05-20T00:00:00.000Z",
  "phone": "0987654321",
  "industry": "Information Technology",
  "skills": ["JavaScript", "NestJS", "React"],
  "avatar": "file-1753588732487-4780477.jpg",
  "designation": "Backend Developer",
  "country": "Vietnam",
  "city": "Hồ Chí Minh",
  "location": "Quận 1, TP.HCM",
  "hourlyRate": 15,
  "description": "Lập trình viên backend có 2 năm kinh nghiệm.",
  "experience": "2 năm làm việc với NodeJS/NestJS",
  "currentSalary": "15,000,000 VND",
  "expectedSalary": "20,000,000 VND",
  "gender": "Male",
  "language": ["Tiếng Việt", "Tiếng Anh"],
  "educationLevel": "Đại học",
  "socialMedias": [
    {
      "platform": "linkedin",
      "url": "https://linkedin.com/in/nguyenvana",
      "type": "SocialMedia"
    },
    {
      "platform": "github",
      "url": "https://github.com/nguyenvana",
      "type": "SocialMedia"
    }
  ],
  "status": true,
  "type": "UpdateCandidateDto"
}
```

### 📌 Example Response — ✅ Thành công

```json
{
  "statusCode": 200,
  "message": "Cập nhật hồ sơ ứng viên thành công!",
  "data": {
    "id": "68c2a7b2dfb1f8e2f4a9b8c3",
    "name": "Nguyễn Văn A",
    "designation": "Backend Developer",
    "industry": "Information Technology",
    "skills": ["JavaScript", "NestJS", "React"],
    "city": "Hồ Chí Minh",
    "country": "Vietnam",
    "location": "Quận 1, TP.HCM",
    "hourlyRate": 15,
    "currentSalary": "15,000,000 VND",
    "expectedSalary": "20,000,000 VND",
    "language": ["Tiếng Việt", "Tiếng Anh"],
    "educationLevel": "Đại học",
    "status": true,
    "updatedAt": "2025-10-05T10:20:00.000Z"
  }
}
```

### 📌 Response Schema

| Field               | Type    | Description                               |
| ------------------- | ------- | ----------------------------------------- |
| statusCode          | number  | Mã trạng thái HTTP                        |
| message             | string  | Thông báo kết quả                         |
| data                | object  | Thông tin hồ sơ ứng viên sau khi cập nhật |
| data.id             | string  | ID của hồ sơ ứng viên                     |
| data.name           | string  | Họ và tên ứng viên                        |
| data.designation    | string  | Chức danh hoặc vị trí                     |
| data.industry       | string  | Ngành nghề hoạt động                      |
| data.skills         | array   | Danh sách kỹ năng                         |
| data.city           | string  | Thành phố                                 |
| data.country        | string  | Quốc gia                                  |
| data.location       | string  | Địa chỉ cụ thể                            |
| data.hourlyRate     | number  | Mức lương theo giờ                        |
| data.currentSalary  | string  | Mức lương hiện tại                        |
| data.expectedSalary | string  | Mức lương mong muốn                       |
| data.language       | array   | Danh sách ngôn ngữ sử dụng                |
| data.educationLevel | string  | Trình độ học vấn                          |
| data.status         | boolean | Trạng thái hồ sơ                          |
| data.updatedAt      | string  | Ngày cập nhật hồ sơ                       |

## 9. SOFT DELETE CANDIDATE

### 🧾 Description

Xóa mềm (Soft Delete) hồ sơ ứng viên theo candidateId.
API này không xóa dữ liệu khỏi cơ sở dữ liệu, mà chỉ chuyển trạng thái (status) của hồ sơ thành false để đánh dấu là đã vô hiệu hóa.

### 📌 Endpoint

```http
DELETE {{baseUrl}}/api/v1/candidate/:id
Authorization: Bearer {{token}}
Content-Type: application/json
```

### 📌 Path Parameter

| Field | Type   | Required | Description                       |
| ----- | ------ | -------- | --------------------------------- |
| id    | string | ✅ Yes   | ID của hồ sơ ứng viên cần xóa mềm |

### 📌 Example Request

```http
DELETE {{baseUrl}}/api/v1/candidate/68be91be9bf7f4178721d9fe
Authorization: Bearer {{token}}
Content-Type: application/json
```

### 📌 Example Response — ✅ Thành công

```json
{
  "statusCode": 200,
  "message": "Xóa mềm hồ sơ ứng viên thành công!",
  "data": {
    "id": "68be91be9bf7f4178721d9fe",
    "name": "Tăng Thành Trung",
    "designation": "Backend Developer",
    "status": false,
    "updatedAt": "2025-10-05T10:30:00.000Z"
  }
}
```

### 📌 Response Schema

| Field            | Type    | Description                                    |
| ---------------- | ------- | ---------------------------------------------- |
| statusCode       | number  | Mã trạng thái HTTP                             |
| message          | string  | Thông báo kết quả                              |
| data             | object  | Thông tin hồ sơ sau khi xóa mềm                |
| data.id          | string  | ID của hồ sơ ứng viên                          |
| data.name        | string  | Tên ứng viên                                   |
| data.designation | string  | Chức danh của ứng viên                         |
| data.status      | boolean | Trạng thái hồ sơ (`false` = đã bị vô hiệu hóa) |
| data.updatedAt   | string  | Thời điểm cập nhật trạng thái                  |
