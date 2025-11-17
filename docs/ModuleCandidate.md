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
      "skills": ["JavaScript", "NestJS", "MongoDB"],
      "category": "Information Technology",
      "gender": "male",
      "createdAt": "09/07/2025",
      "status": false
    },
    {
      "id": "68be91be9bf7f4178721d9fe",
      "avatar": "file-xxxx.jpg",
      "name": "Nguyễn Văn A",
      "designation": "Frontend Developer",
      "location": "Hà Nội",
      "hourlyRate": 20,
      "skills": ["React", "TypeScript"],
      "category": "Information Technology",
      "gender": "female",
      "createdAt": "08/09/2025",
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

| Field                 | Type    | Description                      |
| --------------------- | ------- | -------------------------------- |
| statusCode            | number  | Mã trạng thái HTTP               |
| message               | string  | Thông báo kết quả trả về         |
| results               | array   | Danh sách hồ sơ ứng viên         |
| results[].id          | string  | ID của ứng viên                  |
| results[].avatar      | string  | Ảnh đại diện                     |
| results[].name        | string  | Họ và tên ứng viên               |
| results[].designation | string  | Vị trí hoặc chức danh            |
| results[].location    | string  | Địa điểm làm việc                |
| results[].hourlyRate  | number  | Mức lương theo giờ               |
| results[].skills      | array   | Danh sách kỹ năng của ứng viên   |
| results[].category    | string  | Ngành nghề hoặc lĩnh vực         |
| results[].gender      | string  | Giới tính của ứng viên           |
| results[].createdAt   | string  | Ngày tạo hồ sơ                   |
| results[].status      | boolean | Trạng thái hiển thị của hồ sơ    |
| meta                  | object  | Thông tin phân trang             |
| meta.totalItems       | number  | Tổng số ứng viên                 |
| meta.currentPage      | number  | Trang hiện tại                   |
| meta.pageSize         | number  | Số lượng ứng viên trên mỗi trang |
| meta.totalPages       | number  | Tổng số trang                    |

## 2. GET DETAIL CANDIDATE BY ID

### 🧾 Description

Lấy thông tin chi tiết của một hồ sơ ứng viên (Candidate) theo candidateId.
API này trả về toàn bộ dữ liệu hồ sơ, bao gồm kỹ năng, trình độ, mức lương, mô tả, ngôn ngữ và các mạng xã hội liên kết.

### 📌 Endpoint

- **Endpoint:**
  GET /api/v1/candidate/details/:id

- **Headers:**

```http
Authorization: Bearer {{token}}
Content-Type: application/json
```

### 📌 Path Parameter

| Field | Type            | Required | Description                                |
| ----- | --------------- | -------- | ------------------------------------------ |
| id    | string / number | ✅ Yes   | ID của ứng viên cần lấy thông tin chi tiết |

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
    "birthday": "2004-05-22T00:00:00.000Z",
    "designation": "Backend Developer",
    "location": "63/2 Tân Hóa",
    "hourlyRate": 0,
    "skills": ["JavaScript", "NestJS", "MongoDB"],
    "category": "Information Technology",
    "gender": "male",
    "createdAt": "2025-09-08T08:20:14.763Z",
    "experience": 0,
    "qualification": "Đại học",
    "currentSalary": "1,000,000 VND",
    "expectedSalary": "5,000,000 VND",
    "description": "Tôi là một lập trình viên backend với hơn 3 năm kinh nghiệm...",
    "languages": ["Tiếng Việt", "English"],
    "socialMedias": []
  }
}
```

### 📌 Response Schema

| Field               | Type         | Description                           |
| ------------------- | ------------ | ------------------------------------- |
| statusCode          | number       | Mã trạng thái HTTP                    |
| message             | string       | Thông báo kết quả                     |
| data                | object       | Thông tin chi tiết của ứng viên       |
| data.id             | string       | ID của ứng viên                       |
| data.userId         | string       | ID người dùng liên kết                |
| data.avatar         | string       | Ảnh đại diện của ứng viên             |
| data.name           | string       | Tên ứng viên                          |
| data.birthday       | Date or null | Ngày sinh nhật                        |
| data.designation    | string       | Chức danh hoặc vị trí hiện tại        |
| data.location       | string       | Địa điểm làm việc                     |
| data.hourlyRate     | number       | Mức lương theo giờ                    |
| data.skills         | array        | Danh sách kỹ năng                     |
| data.category       | string       | Ngành nghề hoặc lĩnh vực              |
| data.gender         | string       | Giới tính (`male`, `female`, `other`) |
| data.createdAt      | Date or null | Ngày tạo hồ sơ                        |
| data.experience     | number       | Số năm kinh nghiệm                    |
| data.qualification  | string       | Trình độ học vấn                      |
| data.currentSalary  | string       | Mức lương hiện tại                    |
| data.expectedSalary | string       | Mức lương mong muốn                   |
| data.description    | string       | Mô tả chi tiết về ứng viên            |
| data.languages      | array        | Danh sách ngôn ngữ sử dụng            |
| data.socialMedias   | array        | Danh sách mạng xã hội liên kết        |

## 3. GET LIST INDUSTRY

### 🧾 Description

Lấy danh sách các ngành nghề (Industry) mà các ứng viên đã đăng ký trong hệ thống.
API này giúp hiển thị danh sách lựa chọn ngành nghề trong bộ lọc hoặc khi tạo hồ sơ ứng viên mới.

### 📌 Endpoint

- **Endpoint:**
  GET /api/v1/candidate/industry-list

- **Headers:**

```http
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

## 4. GET DETAIL CANDIDATE BY USER ID

### 🧾 Description

Lấy thông tin chi tiết của hồ sơ ứng viên (Candidate) dựa trên userId.
API này thường được sử dụng sau khi người dùng đăng nhập để lấy hồ sơ cá nhân tương ứng với tài khoản của họ.

### 📌 Endpoint

- **Endpoint:**
  GET /api/v1/candidate/details/user/:id

- **Headers:**

```http
Authorization: Bearer {{token}}
Content-Type: application/json
```

### 📌 Path Parameter

| Field | Type   | Required | Description                                  |
| ----- | ------ | -------- | -------------------------------------------- |
| id    | string | ✅ Yes   | ID của người dùng (userId) cần lấy thông tin |

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
    "birthday": "2004-05-22T00:00:00.000Z",
    "industry": "Information Technology",
    "designation": "Backend Developer",
    "country": "Vietnam",
    "city": "HCM",
    "location": "63/2 Tân Hóa",
    "hourlyRate": 0,
    "skills": ["JavaScript", "NestJS", "MongoDB"],
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
    "languages": ["Tiếng Việt", "English"],
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

| Field               | Type         | Description                           |
| ------------------- | ------------ | ------------------------------------- |
| statusCode          | number       | Mã trạng thái HTTP                    |
| message             | string       | Thông báo kết quả                     |
| data                | object       | Thông tin chi tiết của ứng viên       |
| data.id             | string       | ID của hồ sơ ứng viên                 |
| data.userId         | string       | ID người dùng tương ứng               |
| data.email          | string       | Email của ứng viên                    |
| data.avatar         | string       | Ảnh đại diện của ứng viên             |
| data.name           | string       | Họ và tên ứng viên                    |
| data.birthday       | Date or null | Ngày sinh nhật                        |
| data.industry       | string       | Ngành nghề                            |
| data.designation    | string       | Chức danh hoặc vị trí hiện tại        |
| data.country        | string       | Quốc gia sinh sống                    |
| data.city           | string       | Thành phố sinh sống                   |
| data.location       | string       | Địa chỉ cụ thể                        |
| data.hourlyRate     | number       | Mức lương theo giờ                    |
| data.skills         | array        | Danh sách kỹ năng (skills)            |
| data.category       | string       | Lĩnh vực làm việc                     |
| data.gender         | string       | Giới tính (`male`, `female`, `other`) |
| data.createdAt      | Date or null | Thời điểm tạo hồ sơ                   |
| data.experience     | number       | Số năm kinh nghiệm                    |
| data.qualification  | string       | Trình độ học vấn                      |
| data.birthday       | string       | Ngày sinh                             |
| data.phone          | string       | Số điện thoại liên hệ                 |
| data.currentSalary  | number       | Mức lương hiện tại                    |
| data.expectedSalary | number       | Mức lương mong muốn                   |
| data.currency       | string       | Đơn vị tiền tệ                        |
| data.description    | string       | Giới thiệu chi tiết về ứng viên       |
| data.languages      | array        | Danh sách ngôn ngữ ứng viên sử dụng   |
| data.socialMedias   | array        | Danh sách mạng xã hội của ứng viên    |
| data.status         | boolean      | Trạng thái hồ sơ ứng viên             |

### ⚠️ Example Response — ❌ Không tìm thấy

```json
{
  "statusCode": 404,
  "message": "Không tìm thấy hồ sơ ứng viên cho userId này!"
}
```

## 5. GET LIST SKILLS

### 🧾 Description

Lấy danh sách các kỹ năng (Skills) mà các ứng viên đã đăng ký trong hệ thống.
API này được sử dụng để hiển thị danh sách kỹ năng trong bộ lọc, autocomplete, hoặc khi tạo hồ sơ ứng viên mới.

### 📌 Endpoint

- **Endpoint:**
  GET /api/v1/candidate/skill-list

- **Headers:**

```http
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

## 6. PATCH CANDIDATE

### 🧾 Description

Cập nhật thông tin hồ sơ ứng viên (Candidate) theo candidateId.
API này cho phép người dùng chỉnh sửa thông tin cá nhân, kỹ năng, mức lương, hoặc các mạng xã hội đã liên kết.

### 📌 Endpoint

- **Endpoint:**
  GET /api/v1/candidate/:id

- **Headers:**

```http
Authorization: Bearer {{token}}
Content-Type: application/json
```

### 📌 Path Parameter

| Field | Type   | Required | Description                  |
| ----- | ------ | -------- | ---------------------------- |
| id    | string | ✅ Yes   | ID của ứng viên cần cập nhật |

### 📌 Request Body

| **Field**        | **Type**       | **Required** | **Description**                                                            |
| ---------------- | -------------- | ------------ | -------------------------------------------------------------------------- |
| `name`           | `string`       | ❌ Tùy chọn  | Họ và tên của ứng viên (tối đa 100 ký tự).                                 |
| `birthday`       | `Date`         | ❌ Tùy chọn  | Ngày sinh của ứng viên (phải đúng định dạng ngày).                         |
| `phone`          | `string`       | ❌ Tùy chọn  | Số điện thoại của ứng viên (tối đa 15 ký tự).                              |
| `industry`       | `string`       | ❌ Tùy chọn  | Ngành nghề hoặc lĩnh vực làm việc của ứng viên (tương đương `category`).   |
| `skills`         | `string[]`     | ❌ Tùy chọn  | Danh sách các kỹ năng của ứng viên.                                        |
| `avatar`         | `string`       | ❌ Tùy chọn  | Đường dẫn ảnh đại diện của ứng viên.                                       |
| `designation`    | `string`       | ❌ Tùy chọn  | Chức danh hoặc vị trí hiện tại của ứng viên.                               |
| `country`        | `string`       | ❌ Tùy chọn  | Quốc gia nơi ứng viên sinh sống.                                           |
| `city`           | `string`       | ❌ Tùy chọn  | Thành phố nơi ứng viên sinh sống.                                          |
| `location`       | `string`       | ❌ Tùy chọn  | Địa điểm chi tiết của ứng viên (ví dụ: địa chỉ cụ thể).                    |
| `hourlyRate`     | `number`       | ❌ Tùy chọn  | Mức lương theo giờ (phải là số).                                           |
| `description`    | `string`       | ❌ Tùy chọn  | Mô tả bản thân, giới thiệu ứng viên.                                       |
| `experience`     | `string`       | ❌ Tùy chọn  | Kinh nghiệm làm việc (mô tả bằng chuỗi).                                   |
| `currentSalary`  | `string`       | ❌ Tùy chọn  | Mức lương hiện tại của ứng viên.                                           |
| `expectedSalary` | `string`       | ❌ Tùy chọn  | Mức lương mong muốn của ứng viên.                                          |
| `gender`         | `string`       | ❌ Tùy chọn  | Giới tính của ứng viên.                                                    |
| `languages`      | `string[]`     | ❌ Tùy chọn  | Danh sách ngôn ngữ mà ứng viên sử dụng.                                    |
| `educationLevel` | `string`       | ❌ Tùy chọn  | Trình độ học vấn cao nhất của ứng viên.                                    |
| `socialMedias`   | `SocilMedia[]` | ❌ Tùy chọn  | Danh sách mạng xã hội của ứng viên (ví dụ: LinkedIn, GitHub, Facebook...). |

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
    "currentSalary": "15,000,000",
    "expectedSalary": "20,000,000",
    "currency": "VND",
    "languages": ["Tiếng Việt", "Tiếng Anh"],
    "educationLevel": "Đại học",
    "status": true,
    "updatedAt": "2025-10-05T10:20:00.000Z"
  }
}
```

## 9. SOFT DELETE CANDIDATE

### 🧾 Description

Xóa mềm (Soft Delete) hồ sơ ứng viên theo candidateId.
API này không xóa dữ liệu khỏi cơ sở dữ liệu, mà chỉ chuyển trạng thái (status) của hồ sơ thành false để đánh dấu là đã vô hiệu hóa.

### 📌 Endpoint

- **Endpoint:**
  DELETE /api/v1/candidate/:id

- **Headers:**

```http
Authorization: Bearer {{token}}
Content-Type: application/json
```

### 📌 Path Parameter

| Field | Type   | Required | Description                       |
| ----- | ------ | -------- | --------------------------------- |
| id    | string | ✅ Yes   | ID của hồ sơ ứng viên cần xóa mềm |

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
    "languages": ["Tiếng Việt", "Tiếng Anh"],
    "educationLevel": "Đại học",
    "status": false,
    "updatedAt": "2025-10-05T10:20:00.000Z"
  }
}
```
