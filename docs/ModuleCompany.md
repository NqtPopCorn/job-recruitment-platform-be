# Company API

## 1. GET LIST PAGINATION COMPANY

### 🧾 Description

Lấy danh sách công ty có phân trang, hỗ trợ tìm kiếm, sắp xếp và lọc theo các điều kiện như vị trí, ngành nghề, hoặc năm thành lập.

### 📌 Endpoint

```http
GET {{baseUrl}}/api/v1/company?page=1&size=10&sort=&search=&location=&primaryIndustry=&foundationDateMin=1900&foundationDateMax=2025
Content-Type: application/json

```

### 📌 Query Parameters

| Field             | Type   | Description                          |
| ----------------- | ------ | ------------------------------------ |
| page              | number | Trang hiện tại                       |
| size              | number | Số lượng bản ghi trên mỗi trang      |
| sort              | string | Trường cần sắp xếp (tùy chọn)        |
| search            | string | Từ khóa tìm kiếm theo tên hoặc email |
| location          | string | Lọc theo địa điểm                    |
| primaryIndustry   | string | Lọc theo ngành nghề chính            |
| foundationDateMin | number | Năm thành lập nhỏ nhất               |
| foundationDateMax | number | Năm thành lập lớn nhất               |

### 📌 Example Response — ✅ Thành công

```json
{
  "statusCode": 200,
  "message": "Lấy danh sách công ty phân trang thành công!",
  "results": [
    {
      "id": "68736afc61942cb6f1e0141c",
      "email": "VNP@company.com",
      "name": "Công ty TNHH Công Nghệ VNP",
      "primaryIndustry": "Infomation Technology",
      "size": "100 - 150",
      "foundedIn": 2015,
      "description": "Công ty chuyên cung cấp giải pháp phần mềm và dịch vụ CNTT.",
      "phone": "0987654321",
      "address": "Quận 2, TP.HCM",
      "jobNumber": 2,
      "logo": "file-1753588732487-4780477.jpg",
      "status": true
    }
  ],
  "meta": {
    "totalItems": 1,
    "currentPage": 1,
    "pageSize": 10,
    "totalPages": 1
  }
}
```

### 📌 Response Schema

| Field                     | Type    | Description           |
| ------------------------- | ------- | --------------------- |
| statusCode                | number  | Mã trạng thái HTTP    |
| message                   | string  | Thông báo kết quả     |
| results                   | array   | Danh sách công ty     |
| results[].id              | string  | ID công ty            |
| results[].name            | string  | Tên công ty           |
| results[].email           | string  | Email công ty         |
| results[].primaryIndustry | string  | Ngành nghề chính      |
| results[].size            | string  | Quy mô công ty        |
| results[].foundedIn       | number  | Năm thành lập         |
| results[].phone           | string  | Số điện thoại         |
| results[].address         | string  | Địa chỉ công ty       |
| results[].jobNumber       | number  | Số lượng công việc    |
| results[].logo            | string  | Ảnh đại diện          |
| results[].status          | boolean | Trạng thái hoạt động  |
| results[].socialMedias    | array   | Danh sách mạng xã hội |
| meta                      | object  | Thông tin phân trang  |
| meta.totalItems           | number  | Tổng số công ty       |
| meta.currentPage          | number  | Trang hiện tại        |
| meta.pageSize             | number  | Số lượng mỗi trang    |
| meta.totalPages           | number  | Tổng số trang         |

## 2. GET LIST COMPANY

### 🧾 Description

Lấy danh sách tất cả công ty (không phân trang).

### 📌 Endpoint

```http
GET {{baseUrl}}/api/v1/company/get-list
Content-Type: application/json
```

### 📌 Example Response — ✅ Thành công

```json
{
  "statusCode": 200,
  "message": "Lấy danh sách công ty thành công!",
  "results": [
    {
      "id": "68736afc61942cb6f1e0141c",
      "email": "VNP@company.com",
      "name": "Công ty TNHH Công Nghệ VNP",
      "primaryIndustry": "Information Technology",
      "size": "100 - 150",
      "foundedIn": 2015,
      "country": "Vietnam",
      "city": "",
      "address": "Quận 2, TP.HCM",
      "website": "ttt220504.github.com",
      "logo": "file-1753588732487-4780477.jpg",
      "status": true
    }
  ]
}
```

### 📌 Response Schema

| Field                     | Type   | Description                         |
| ------------------------- | ------ | ----------------------------------- |
| statusCode                | number | Mã trạng thái HTTP                  |
| message                   | string | Thông báo kết quả trả về            |
| results                   | array  | Danh sách thông tin các công ty     |
| results[].id              | string | ID của công ty                      |
| results[].email           | string | Email liên hệ của công ty           |
| results[].name            | string | Tên công ty                         |
| results[].primaryIndustry | string | Ngành nghề chính của công ty        |
| results[].size            | string | Quy mô công ty (ví dụ: “100 - 150”) |
| results[].foundedIn       | number | Năm thành lập của công ty           |
| results[].country         | string | Quốc gia mà công ty hoạt động       |
| results[].city            | string | Thành phố nơi công ty đặt trụ sở    |
| results[].address         | string | Địa chỉ cụ thể của công ty          |
| results[].website         | string | Trang web chính thức của công ty    |
| results[].logo            | string |                                     |

### 📌 Example Response — ❌ Thất bại

```json
{
  "success": false,
  "statusCode": 409,
  "error": "Hồ sơ ứng viên đã tồn tại!",
  "message": "Hồ sơ ứng viên đã tồn tại"
}
```

## 3. GET DETAIL COMPANY BY ID

### 🧾 Description

Lấy chi tiết thông tin công ty theo companyId.

### 📌 Endpoint

```http
GET {{baseUrl}}/api/v1/company/details/:id
Content-Type: application/json

```

### 📌 Path Parameter

| Field | Type   | Description |
| ----- | ------ | ----------- |
| id    | string | ID công ty  |

### 📌 Example Response — ✅ Thành công

```json
{
  "statusCode": 200,
  "message": "Lấy thông tin công ty thành công!",
  "data": {
    "id": "68736afc61942cb6f1e0141c",
    "name": "Công ty TNHH Công Nghệ VNP",
    "email": "VNP@company.com",
    "phone": "0987654321",
    "primaryIndustry": "Information Technology",
    "size": "100 - 150",
    "foundedIn": 2015,
    "country": "Vietnam",
    "address": "Quận 2, TP.HCM",
    "website": "ttt220504.github.com",
    "status": true
  }
}
```

### 📌 Response Schema

| Field                | Type    | Description                                       |
| -------------------- | ------- | ------------------------------------------------- |
| statusCode           | number  | Mã trạng thái HTTP                                |
| message              | string  | Thông báo kết quả trả về                          |
| data                 | object  | Thông tin chi tiết của công ty                    |
| data.id              | string  | ID của công ty                                    |
| data.name            | string  | Tên công ty                                       |
| data.email           | string  | Email liên hệ của công ty                         |
| data.phone           | string  | Số điện thoại liên hệ của công ty                 |
| data.primaryIndustry | string  | Ngành nghề chính của công ty                      |
| data.size            | string  | Quy mô công ty (ví dụ: “100 - 150”)               |
| data.foundedIn       | number  | Năm thành lập của công ty                         |
| data.country         | string  | Quốc gia mà công ty hoạt động                     |
| data.address         | string  | Địa chỉ cụ thể của công ty                        |
| data.website         | string  | Trang web chính thức của công ty                  |
| data.status          | boolean | Trạng thái hoạt động của công ty (`true`/`false`) |

## 4. GET DETAIL COMPANY BY USER ID

### 🧾 Description

Lấy thông tin công ty theo userId của chủ tài khoản.

### 📌 Endpoint

```http
GET {{baseUrl}}/api/v1/company/details/user/:id
Content-Type: application/json

```

### 📌 Path Parameters

| Field | Type   | Description       |
| ----- | ------ | ----------------- |
| id    | string | ID của người dùng |

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
  "message": "Lấy thông tin công ty thành công!",
  "data": {
    "id": "68736afc61942cb6f1e0141c",
    "name": "Công ty TNHH Công Nghệ VNP",
    "email": "VNP@company.com",
    "phone": "0987654321",
    "primaryIndustry": "Information Technology",
    "size": "100 - 150",
    "foundedIn": 2015,
    "country": "Vietnam",
    "address": "Quận 2, TP.HCM",
    "website": "ttt220504.github.com",
    "status": true
  }
}
```

### 📌 Response Schema

| Field                | Type    | Description                                       |
| -------------------- | ------- | ------------------------------------------------- |
| statusCode           | number  | Mã trạng thái HTTP                                |
| message              | string  | Thông báo kết quả trả về                          |
| data                 | object  | Thông tin chi tiết của công ty                    |
| data.id              | string  | ID của công ty                                    |
| data.name            | string  | Tên công ty                                       |
| data.email           | string  | Địa chỉ email liên hệ của công ty                 |
| data.phone           | string  | Số điện thoại liên hệ                             |
| data.primaryIndustry | string  | Ngành nghề chính của công ty                      |
| data.size            | string  | Quy mô công ty (ví dụ: “100 - 150”)               |
| data.foundedIn       | number  | Năm thành lập của công ty                         |
| data.country         | string  | Quốc gia mà công ty hoạt động                     |
| data.address         | string  | Địa chỉ cụ thể của công ty                        |
| data.website         | string  | Trang web chính thức của công ty                  |
| data.status          | boolean | Trạng thái hoạt động của công ty (`true`/`false`) |

## 5. GET RELATED JOBS BY COMPANY ID

### 🧾 Description

Lấy danh sách công việc liên quan đến công ty dựa trên companyId.

### 📌 Endpoint

```http
GET {{baseUrl}}/api/v1/company/related-jobs/:companyId
Content-Type: application/json

```

### 📌 Example Response — ✅ Thành công

```json
{
  "statusCode": 200,
  "message": "Lấy danh sách công việc liên quan bằng companyId thành công!",
  "results": [
    {
      "id": "689307de1152ccfb7a7d3468",
      "jobTitle": "Intern Backend NestJS",
      "location": "312 Lê Thánh Tông, Quận 1",
      "industry": "Information Technology",
      "salary": {
        "min": 0,
        "max": 3000000,
        "currency": "VND",
        "negotiable": true
      }
    }
  ]
}
```

### 📌 Response Schema

| Field              | Type   | Description                                   |
| ------------------ | ------ | --------------------------------------------- |
| statusCode         | number | Mã trạng thái HTTP                            |
| message            | string | Thông báo kết quả trả về                      |
| results            | array  | Danh sách các công việc liên quan đến công ty |
| results[].id       | string | ID của công việc                              |
| results[].jobTitle | string | Tiêu đề hoặc tên vị trí tuyển dụng            |
| results[].location | string | Địa điểm làm việc của công việc               |
| results[].industry | string | Ngành nghề liên quan đến công việc            |
| results[].salary   | object | Thông tin về mức lương                        |

## 6. GET INDUSTRY LIST OF COMPANY

### 🧾 Description

Lấy danh sách danh mục ngành nghề của các công ty.

### 📌 Endpoint

```http
GET {{baseUrl}}/api/v1/company/industry-list
Content-Type: application/json

```

### 📌 Example Response — ✅ Thành công

```json
{
  "statusCode": 200,
  "message": "Lấy danh sách danh mục công ty thành công!",
  "results": [
    { "label": "Information Technology", "value": "Information Technology" }
  ]
}
```

### 📌 Response Schema

| Field           | Type   | Description                                  |
| --------------- | ------ | -------------------------------------------- |
| statusCode      | number | Mã trạng thái HTTP                           |
| message         | string | Thông báo kết quả trả về                     |
| results         | array  | Danh sách danh mục (ngành nghề) của công ty  |
| results[].label | string | Nhãn hiển thị của ngành nghề (Industry name) |
| results[].value | string | Giá trị thực tế của ngành nghề               |

## 7. POST NEW COMPANY

### 🧾 Description

Tạo mới thông tin công ty.

### 📌 Endpoint

```http
POST {{baseUrl}}/api/v1/company
Content-Type: application/json

```

### 📌 Request Body

```json
{
  "userId": "686f5683f6e123fa2042954f",
  "email": "VNP@company.com",
  "name": "Công ty TNHH Công Nghệ VNP",
  "primaryIndustry": "Information Technology",
  "size": "100 - 150",
  "foundedIn": 2015,
  "description": "Công ty chuyên cung cấp giải pháp phần mềm.",
  "phone": "0987654321",
  "country": "Vietnam",
  "city": "Hồ Chí Minh",
  "address": "Quận 2, TP.HCM",
  "logo": "file-1753588732487-4780477.jpg",
  "website": "https://vnp.com.vn"
}
```

### 📌 Request Body Schema

| Field           | Type   | Required | Description                         |
| --------------- | ------ | -------- | ----------------------------------- |
| userId          | string | ✅       | ID của người dùng sở hữu công ty    |
| email           | string | ✅       | Email của công ty                   |
| name            | string | ✅       | Tên công ty                         |
| primaryIndustry | string | ✅       | Ngành nghề chính của công ty        |
| size            | string | ✅       | Quy mô nhân sự (ví dụ: “100 - 150”) |
| foundedIn       | number | ✅       | Năm thành lập công ty               |
| description     | string | ✅       | Mô tả ngắn gọn về công ty           |
| phone           | string | ✅       | Số điện thoại liên hệ               |
| country         | string | ✅       | Quốc gia của công ty                |
| city            | string | ❌       | Thành phố (có thể để trống)         |
| address         | string | ✅       | Địa chỉ cụ thể                      |
| logo            | string | ❌       | Tên file ảnh logo đã upload         |
| website         | string | ❌       | Website chính thức của công ty      |

### 📌 Example Response — ✅ Thành công

```json

```

### 📌 Response Schema

## 8. PATCH COMPANY

### 🧾 Description

Cập nhật thông tin công ty theo ID.

### 📌 Endpoint

```http
PATCH {{baseUrl}}/api/v1/company/:id
Content-Type: application/json

```

### 📌 Body (cập nhật 1 hoặc nhiều trường)

```json
{
  "name": "Công ty TNHH Công Nghệ VNP",
  "phone": "0123456789",
  "website": "https://vnp.com.vn"
}
```

### 📌 Request Body Schema

| Field   | Type   | Required | Description                    |
| ------- | ------ | -------- | ------------------------------ |
| name    | string | ❌       | Tên công ty mới                |
| phone   | string | ❌       | Số điện thoại liên hệ mới      |
| website | string | ❌       | Website chính thức của công ty |

### 📌 Example Response — ✅ Thành công

```json

```

### 📌 Response Schema

## 9. SOFT DELETE COMPANY

### 🧾 Description

Xóa mềm công ty theo ID (chỉ chuyển trạng thái status thành false).

### 📌 Endpoint

```http
DELETE {{baseUrl}}/api/v1/company/:id
Authorization: Bearer {{token}}
Content-Type: application/json

```

### 📌 Example Response — ✅ Thành công

```json
{
  "statusCode": 200,
  "message": "Xóa mềm công ty thành công!",
  "data": {
    "id": "68736afc61942cb6f1e0141c",
    "name": "Công ty TNHH Công Nghệ VNP",
    "status": false,
    "updatedAt": "2025-10-05T10:32:45.000Z"
  }
}
```

### 📌 Response Schema
