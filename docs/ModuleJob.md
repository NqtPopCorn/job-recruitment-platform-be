# Job API

## 1. GET LIST PAGINATION JOB

### Description

Lấy danh sách công việc phân trang

- **Endpoint:**
  GET /api/v1/job?page=1&size=10

- **Headers:**

```http
Authorization: Bearer {{token}}
Content-Type: application/json
```

### Query Params

| Tên          | Kiểu   | Mô tả               |
| ------------ | ------ | ------------------- |
| `search`     | string | Từ khóa tìm kiếm    |
| `location`   | string | Địa điểm            |
| `category`   | string | Ngành nghề          |
| `type`       | string | Loại công việc      |
| `datePosted` | string | Ngày đăng           |
| `experience` | string | Kinh nghiệm yêu cầu |
| `min`        | number | Lương tối thiểu     |
| `max`        | number | Lương tối đa        |

### 📌 Response Schema

| Field      | Type     | Description              |
| ---------- | -------- | ------------------------ |
| statusCode | number   | Mã trạng thái HTTP       |
| message    | string   | Thông báo kết quả        |
| results    | object[] | Danh sách công việc      |
| page       | number   | Trang hiện tại           |
| size       | number   | Số item trên mỗi trang   |
| totalPage  | number   | Tổng số trang            |
| totalItem  | number   | Tổng số item (công việc) |

### 📌 Job Object

| Field              | Type            | Description                                   |
| ------------------ | --------------- | --------------------------------------------- |
| id                 | number          | ID công việc                                  |
| logo               | string          | Logo công việc (file path hoặc URL)           |
| jobTitle           | string          | Tiêu đề công việc                             |
| company            | Company Object  | Thông tin công ty đăng tuyển                  |
| location           | string          | Địa điểm làm việc                             |
| description        | string          | Mô tả công việc                               |
| responsibilities   | string[]        | Danh sách trách nhiệm                         |
| skillAndExperience | string[]        | Danh sách kỹ năng & kinh nghiệm yêu cầu       |
| salary             | Salary Object   | Thông tin mức lương                           |
| workTime           | WorkTime Object | Thời gian làm việc                            |
| industry           | string          | Ngành nghề                                    |
| quantity           | number          | Số lượng tuyển dụng                           |
| country            | string          | Quốc gia                                      |
| city               | string          | Thành phố                                     |
| jobType            | JobType[]       | Hình thức công việc (Full Time, Part Time, …) |
| destination        | string          | null                                          |
| datePosted         | string (date)   | Ngày đăng tuyển                               |
| expireDate         | string (date)   | Ngày hết hạn                                  |

### 📌 Example Response

```json
{
  "statusCode": 200,
  "message": "Lấy danh sách công việc phân trang thành công!",
  "results": [
    {
      "id": "689307de1152ccfb7a7d3468",
      "logo": "file-1753588732487-4780477.jpg",
      "jobTitle": "Intern Backend NestJS",
      "company": {
        "id": "68736afc61942cb6f1e0141c",
        "email": "VNP@company.com",
        "name": "Công ty TNHH Công Nghệ VNP",
        "userId": "686f5683f6e123fa2042954f",
        "primaryIndustry": "Infomation Technology",
        "size": "100 - 150",
        "foundedIn": 2015,
        "description": "Công ty chuyên cung cấp giải pháp phần mềm và dịch vụ CNTT.",
        "phone": "0987654321",
        "address": "Quận 2, TP.HCM",
        "logo": "file-1753588732487-4780477.jpg",
        "socialMedias": [
          { "platform": "facebook", "url": "https://facebook.com/congtyabc" },
          { "platform": "twitter", "url": "https://twitter.com/congtyabc" },
          {
            "platform": "linkedin",
            "url": "https://linkedin.com/company/congtyabc"
          },
          {
            "platform": "googlePlus",
            "url": "https://googleplus.com/company/congtyabc"
          }
        ],
        "isDeleted": false,
        "createdAt": "2025-07-13T08:14:52.413Z",
        "updatedAt": "2025-07-30T07:07:22.379Z"
      },
      "location": "312 Lê Thánh Tông, Quận 1",
      "description": "We are hiring intern for internship program.",
      "responsibilities": [
        "Chịu trách nhiệm và hoàn thành nhiệm vụ được giao."
      ],
      "skillAndExperience": [
        "Có kiến thức về NestJS và NodeJS.",
        "Có kiến thức về cơ sở dữ liệu."
      ],
      "salary": {
        "min": 0,
        "max": 3000000,
        "currency": "VND",
        "negotiable": true
      },
      "workTime": {
        "from": "09:00",
        "to": "18:00"
      },
      "industry": "Infomation Technology",
      "quantity": 1,
      "country": "Vietnam",
      "city": "Hồ Chí Minh",
      "jobType": [
        { "styleClass": "time", "type": "Full Time" },
        { "styleClass": "level", "type": "Intern" }
      ],
      "destination": null,
      "datePosted": "6/8/2025",
      "expireDate": "30/8/2025"
    }
  ]
}
```

## 2. GET LIST JOB FOR CLIENT OF COMPANY

### Endpoint

GET {{baseUrl}}/api/v1/job/get-list

**Headers**

```http
Content-Type: application/json

```

### 📌 Response Schema

| Field      | Type     | Description         |
| ---------- | -------- | ------------------- |
| statusCode | number   | Mã trạng thái HTTP  |
| message    | string   | Thông báo kết quả   |
| results    | object[] | Danh sách công việc |

### 📌 Job Object

| Field              | Type        | Description                               |
| ------------------ | ----------- | ----------------------------------------- |
| id                 | number      | ID công việc                              |
| logo               | string      | Logo công việc (file path hoặc URL)       |
| jobTitle           | string      | Tên công việc                             |
| company            | object      | Thông tin công ty                         |
| location           | string      | Địa chỉ làm việc                          |
| description        | string      | Mô tả công việc                           |
| responsibilities   | string[]    | Trách nhiệm                               |
| skillAndExperience | string[]    | Kỹ năng & kinh nghiệm yêu cầu             |
| salary             | object      | Thông tin lương                           |
| workTime           | object      | Thời gian làm việc                        |
| industry           | string      | Ngành nghề                                |
| quantity           | number      | Số lượng tuyển dụng                       |
| country            | string      | Quốc gia                                  |
| city               | string      | Thành phố                                 |
| jobType            | object[]    | Loại hình công việc (Fulltime, Intern, …) |
| destination        | string/null | Điểm đến (nếu có)                         |
| datePosted         | string      | Ngày đăng (dd/MM/yyyy)                    |
| expireDate         | string      | Ngày hết hạn (dd/MM/yyyy)                 |

```json
{
  "statusCode": 200,
  "message": "Lấy danh sách công việc thành công!",
  "results": [
    {
      "id": "689307de1152ccfb7a7d3468",
      "logo": "file-1753588732487-4780477.jpg",
      "jobTitle": "Intern Backend NestJS",
      "company": {
        "id": "68736afc61942cb6f1e0141c",
        "email": "VNP@company.com",
        "name": "Công ty TNHH Công Nghệ VNP",
        "userId": "686f5683f6e123fa2042954f",
        "primaryIndustry": "Infomation Technology",
        "size": "100 - 150",
        "foundedIn": 2015,
        "description": "Công ty chuyên cung cấp giải pháp phần mềm và dịch vụ CNTT.",
        "phone": "0987654321",
        "address": "Quận 2, TP.HCM",
        "logo": "file-1753588732487-4780477.jpg",
        "socialMedias": [
          { "platform": "facebook", "url": "https://facebook.com/congtyabc" },
          { "platform": "twitter", "url": "https://twitter.com/congtyabc" },
          {
            "platform": "linkedin",
            "url": "https://linkedin.com/company/congtyabc"
          },
          {
            "platform": "googlePlus",
            "url": "https://googleplus.com/company/congtyabc"
          }
        ],
        "isDeleted": false,
        "createdAt": "2025-07-13T08:14:52.413Z",
        "updatedAt": "2025-07-30T07:07:22.379Z"
      },
      "location": "312 Lê Thánh Tông, Quận 1",
      "description": "We are hiring intern for internship program.",
      "responsibilities": [
        "Chịu trách nhiệm và hoàn thành nhiệm vụ được giao."
      ],
      "skillAndExperience": [
        "Có kiến thức về NestJS và NodeJS.",
        "Có kiến thức về cơ sở dữ liệu."
      ],
      "salary": {
        "min": 0,
        "max": 3000000,
        "currency": "VND",
        "negotiable": true
      },
      "workTime": { "from": "09:00", "to": "18:00" },
      "industry": "Infomation Technology",
      "quantity": 1,
      "country": "Vietnam",
      "city": "Hồ Chí Minh",
      "jobType": [
        { "styleClass": "time", "type": "Full Time" },
        { "styleClass": "level", "type": "Intern" }
      ],
      "destination": null,
      "datePosted": "6/8/2025",
      "expireDate": "30/8/2025"
    }
  ]
}
```

## 3. GET Detail by Job ID

### Endpoint

```http
GET {{baseUrl}}/api/v1/job/detail/:id
Content-Type: application/json
```

### 📌 Response Object

| Field      | Type   | Description                  |
| ---------- | ------ | ---------------------------- |
| statusCode | number | Mã trạng thái HTTP trả về    |
| message    | string | Thông báo kết quả            |
| data       | object | Thông tin chi tiết công việc |

### 📌 Job Object

| Field              | Type          | Description                            |
| ------------------ | ------------- | -------------------------------------- |
| id                 | string        | ID công việc                           |
| logo               | string        | Logo công việc (file path hoặc URL)    |
| jobTitle           | string        | Tiêu đề công việc                      |
| company            | object        | Thông tin công ty                      |
| location           | string        | Địa điểm làm việc                      |
| description        | string        | Mô tả công việc                        |
| responsibilities   | string[]      | Danh sách trách nhiệm                  |
| skillAndExperience | string[]      | Kỹ năng và kinh nghiệm yêu cầu         |
| salary             | object        | Thông tin lương                        |
| workTime           | object        | Thời gian làm việc                     |
| industry           | string        | Ngành nghề                             |
| quantity           | number        | Số lượng tuyển dụng                    |
| country            | string        | Quốc gia                               |
| city               | string        | Thành phố                              |
| jobType            | object[]      | Hình thức làm việc (Full Time, ...)    |
| destination        | string/null   | Địa điểm cụ thể hoặc null nếu không có |
| datePosted         | string (date) | Ngày đăng tuyển                        |
| expireDate         | string (date) | Ngày hết hạn                           |

### 📌 Company Object

| Field           | Type     | Description           |
| --------------- | -------- | --------------------- |
| id              | string   | ID công ty            |
| email           | string   | Email công ty         |
| name            | string   | Tên công ty           |
| userId          | string   | ID user tạo công ty   |
| primaryIndustry | string   | Ngành nghề chính      |
| size            | string   | Quy mô công ty        |
| foundedIn       | number   | Năm thành lập         |
| description     | string   | Giới thiệu công ty    |
| phone           | string   | Số điện thoại         |
| address         | string   | Địa chỉ               |
| logo            | string   | Logo công ty          |
| socialMedias    | object[] | Danh sách mạng xã hội |
| isDeleted       | boolean  | Đã xóa hay chưa       |
| createdAt       | string   | Ngày tạo              |
| updatedAt       | string   | Ngày cập nhật         |

### 📌 SocialMedia Object

| Field    | Type   | Description                  |
| -------- | ------ | ---------------------------- |
| platform | string | Tên nền tảng (facebook, ...) |
| url      | string | Liên kết mạng xã hội         |

### 📌 Salary Object

| Field      | Type    | Description                |
| ---------- | ------- | -------------------------- |
| min        | number  | Lương tối thiểu            |
| max        | number  | Lương tối đa               |
| currency   | string  | Loại tiền tệ (VND, USD, …) |
| negotiable | boolean | Có thể thương lượng không  |

### 📌 WorkTime Object

| Field | Type   | Description           |
| ----- | ------ | --------------------- |
| from  | string | Giờ bắt đầu làm việc  |
| to    | string | Giờ kết thúc làm việc |

### 📌 JobType Object

| Field      | Type   | Description                     |
| ---------- | ------ | ------------------------------- |
| styleClass | string | Loại phân loại (time, level, …) |
| type       | string | Giá trị (Full Time, Intern, …)  |

## 4. GET List Category (Industry of Job)

### Endpoint

```http
GET {{baseUrl}}/api/v1/job/category-list
Content-Type: application/json
```

### 📌 Response Object

| Field      | Type     | Description                |
| ---------- | -------- | -------------------------- |
| statusCode | number   | Mã trạng thái HTTP của API |
| message    | string   | Thông điệp phản hồi        |
| results    | string[] | Danh sách tên danh mục     |

### 📌 Response Object

```json
{
  "statusCode": 200,
  "message": "Lấy danh sách danh mục thành công!",
  "results": ["Information Technology", "Finance", "Education", "Healthcare"]
}
```

## 5. GET List Primary Industry of Company

### Endpoint

```http
GET {{baseUrl}}/api/v1/job/category-list/company/:id
Content-Type: application/json
```

### 📌 Response Object

| Field      | Type     | Description                         |
| ---------- | -------- | ----------------------------------- |
| statusCode | number   | Mã trạng thái HTTP của API          |
| message    | string   | Thông điệp phản hồi                 |
| results    | string[] | Danh sách tên danh mục (ngành nghề) |

### 📌 Example Response

```json
{
  "statusCode": 200,
  "message": "Lấy danh sách danh mục thành công!",
  "results": ["Information Technology", "Finance", "Healthcare"]
}
```

## 6. GET List Skills

### Endpoint

```http
GET {{baseUrl}}/api/v1/job/skill-list
Content-Type: application/json
```

### 📌 Response Schema

| Field      | Type     | Description                             |
| ---------- | -------- | --------------------------------------- |
| statusCode | number   | Mã trạng thái HTTP                      |
| message    | string   | Thông báo kết quả                       |
| results    | string[] | Danh sách kỹ năng (mỗi phần tử là text) |

### 📌 Example Response

```json
{
  "statusCode": 200,
  "message": "Lấy danh sách kỹ năng thành công!",
  "results": ["MongoDB", "MySQL", "NestJS", "NextJS", "NodeJs"]
}
```

## 7. GET List Cities

### Endpoint

```http
GET {{baseUrl}}/api/v1/job/city-list
Content-Type: application/json
```

### 📌 Response Schema

| Field      | Type     | Description                     |
| ---------- | -------- | ------------------------------- |
| statusCode | number   | Mã trạng thái HTTP              |
| message    | string   | Thông báo kết quả               |
| results    | string[] | Danh sách các thành phố (chuỗi) |

### 📌 Example Response

```json
{
  "statusCode": 200,
  "message": "Lấy danh sách thành phố thành công!",
  "results": ["Hà Nội", "Hồ Chí Minh", "Đà Nẵng", "Cần Thơ"]
}
```

## 8. GET Max Salary

### Endpoint

```http
GET {{baseUrl}}/api/v1/job/max-salary
Content-Type: application/json
```

### 📌 Response Schema

| Field      | Type   | Description                     |
| ---------- | ------ | ------------------------------- |
| statusCode | number | Mã trạng thái HTTP              |
| message    | string | Thông báo kết quả               |
| data       | number | Mức lương cao nhất (đơn vị VND) |

### 📌 Example Response

```json
{
  "statusCode": 200,
  "message": "Lấy mức lương cao nhất thành công!",
  "data": 50000000
}
```

## 9. GET Related Jobs By ID

### Description

Lấy danh sách các công việc liên quan đến job hiện tại.  
Nếu không truyền `industry`, `country`, `city` thì hệ thống sẽ dựa vào **industry của job hiện tại** để trả về kết quả.

---

### Endpoint

```http
GET {{baseUrl}}/api/v1/job/related-jobs/:id
Content-Type: application/json
```

### Query Params (optional)

| Param    | Type   | Description        |
| -------- | ------ | ------------------ |
| industry | string | Ngành nghề cần lọc |
| country  | string | Quốc gia cần lọc   |
| city     | string | Thành phố cần lọc  |

### 📌 Response Schema

| Field      | Type   | Description                   |
| ---------- | ------ | ----------------------------- |
| statusCode | number | Mã trạng thái HTTP            |
| message    | string | Thông báo kết quả             |
| results    | Job[]  | Danh sách công việc liên quan |

### 📌 Job Object

| Field              | Type        | Description                    |
| ------------------ | ----------- | ------------------------------ |
| id                 | string      | ID công việc                   |
| logo               | string      | Logo công việc                 |
| jobTitle           | string      | Tiêu đề công việc              |
| company            | Company     | Thông tin công ty              |
| location           | string      | Địa chỉ chi tiết               |
| description        | string      | Mô tả công việc                |
| responsibilities   | string[]    | Danh sách trách nhiệm          |
| skillAndExperience | string[]    | Kỹ năng và kinh nghiệm yêu cầu |
| salary             | Salary      | Thông tin lương                |
| workTime           | WorkTime    | Thời gian làm việc             |
| industry           | string      | Ngành nghề                     |
| quantity           | number      | Số lượng tuyển                 |
| country            | string      | Quốc gia                       |
| city               | string      | Thành phố                      |
| jobType            | JobType[]   | Hình thức & cấp độ công việc   |
| destination        | string/null | Địa điểm khác (nếu có)         |
| datePosted         | string      | Ngày đăng (dd/MM/yyyy)         |
| expireDate         | string      | Ngày hết hạn (dd/MM/yyyy)      |

### 📌 Company Object

| Field           | Type          | Description             |
| --------------- | ------------- | ----------------------- |
| id              | string        | ID công ty              |
| email           | string        | Email công ty           |
| name            | string        | Tên công ty             |
| userId          | string        | ID user sở hữu công ty  |
| primaryIndustry | string        | Ngành chính             |
| size            | string        | Quy mô công ty          |
| foundedIn       | number        | Năm thành lập           |
| description     | string        | Giới thiệu công ty      |
| phone           | string        | Số điện thoại           |
| address         | string        | Địa chỉ                 |
| logo            | string        | Logo công ty            |
| socialMedias    | SocialMedia[] | Danh sách mạng xã hội   |
| isDeleted       | boolean       | Trạng thái xóa          |
| createdAt       | string        | Ngày tạo (ISODate)      |
| updatedAt       | string        | Ngày cập nhật (ISODate) |

### 📌 SocialMedia Object

| Field    | Type   | Description                          |
| -------- | ------ | ------------------------------------ |
| platform | string | Tên mạng xã hội (facebook, twitter…) |
| url      | string | Liên kết đến trang công ty           |

### 📌 Salary Object

| Field      | Type    | Description                  |
| ---------- | ------- | ---------------------------- |
| min        | number  | Lương tối thiểu              |
| max        | number  | Lương tối đa                 |
| currency   | string  | Đơn vị tiền tệ (VND, USD, …) |
| negotiable | boolean | Có thể thương lượng không    |

### 📌 WorkTime Object

| Field | Type   | Description          |
| ----- | ------ | -------------------- |
| from  | string | Giờ bắt đầu (HH:mm)  |
| to    | string | Giờ kết thúc (HH:mm) |

### 📌 JobType Object

| Field      | Type   | Description                   |
| ---------- | ------ | ----------------------------- |
| styleClass | string | Loại phân loại (time, level…) |
| type       | string | Giá trị (Full Time, Intern…)  |

### 📌 Example Response

```json
{
  "statusCode": 200,
  "message": "Lấy danh sách công việc liên quan thành công!",
  "results": [
    {
      "id": "689307de1152ccfb7a7d3468",
      "logo": "file-1753588732487-4780477.jpg",
      "jobTitle": "Intern Backend NestJS",
      "company": {
        "id": "68736afc61942cb6f1e0141c",
        "email": "VNP@company.com",
        "name": "Công ty TNHH Công Nghệ VNP",
        "userId": "686f5683f6e123fa2042954f",
        "primaryIndustry": "Infomation Technology",
        "size": "100 - 150",
        "foundedIn": 2015,
        "description": "Công ty chuyên cung cấp giải pháp phần mềm và dịch vụ CNTT.",
        "phone": "0987654321",
        "address": "Quận 2, TP.HCM",
        "logo": "file-1753588732487-4780477.jpg",
        "socialMedias": [
          {
            "platform": "facebook",
            "url": "https://facebook.com/congtyabc"
          }
        ],
        "isDeleted": false,
        "createdAt": "2025-07-13T08:14:52.413Z",
        "updatedAt": "2025-07-30T07:07:22.379Z"
      },
      "location": "312 Lê Thánh Tông, Quận 1",
      "description": "We are hiring intern for internship program.",
      "responsibilities": [
        "Chịu trách nhiệm và hoàn thành nhiệm vụ được giao."
      ],
      "skillAndExperience": [
        "Có kiến thức về NestJS và NodeJS.",
        "Có kiến thức về cơ sở dữ liệu."
      ],
      "salary": {
        "min": 0,
        "max": 3000000,
        "currency": "VND",
        "negotiable": true
      },
      "workTime": {
        "from": "09:00",
        "to": "18:00"
      },
      "industry": "Infomation Technology",
      "quantity": 1,
      "country": "Vietnam",
      "city": "Hồ Chí Minh",
      "jobType": [
        {
          "styleClass": "time",
          "type": "Full Time"
        },
        {
          "styleClass": "level",
          "type": "Intern"
        }
      ],
      "destination": null,
      "datePosted": "6/8/2025",
      "expireDate": "30/8/2025"
    }
  ]
}
```

## 10. GET List Job Dashboard of Company by ID

### Description

Lấy danh sách công việc của một công ty trên dashboard.  
Kết quả bao gồm thông tin cơ bản của job, trạng thái active/inactive, số lượng ứng viên nộp đơn.

---

### Endpoint

```http
GET {{baseUrl}}/api/v1/job/get-list/dashboard/company/:id
Content-Type: application/json
```

### Path Params

| Param | Type   | Description |
| ----- | ------ | ----------- |
| id    | number | ID công ty  |

### 📌 Response Schema

| Field      | Type   | Description         |
| ---------- | ------ | ------------------- |
| statusCode | number | Mã trạng thái HTTP  |
| message    | string | Thông báo kết quả   |
| results    | Job[]  | Danh sách công việc |

### 📌 Job Object

| Field              | Type        | Description                                        |
| ------------------ | ----------- | -------------------------------------------------- |
| id                 | number      | ID công việc                                       |
| logo               | string      | Logo công việc                                     |
| jobTitle           | string      | Tiêu đề công việc                                  |
| company            | Company     | Thông tin công ty                                  |
| location           | string      | Địa chỉ chi tiết                                   |
| description        | string      | Mô tả công việc                                    |
| responsibilities   | string[]    | Danh sách trách nhiệm                              |
| skillAndExperience | string[]    | Kỹ năng và kinh nghiệm yêu cầu                     |
| salary             | Salary      | Thông tin lương                                    |
| workTime           | WorkTime    | Thời gian làm việc                                 |
| industry           | string      | Ngành nghề                                         |
| quantity           | number      | Số lượng tuyển                                     |
| country            | string      | Quốc gia                                           |
| city               | string      | Thành phố                                          |
| jobType            | JobType[]   | Hình thức & cấp độ công việc                       |
| destination        | string/null | Địa điểm khác (nếu có)                             |
| datePosted         | string      | Ngày đăng (dd/MM/yyyy)                             |
| expireDate         | string      | Ngày hết hạn (dd/MM/yyyy)                          |
| applications       | number      | Số lượng ứng viên đã nộp đơn                       |
| status             | boolean     | Trạng thái công việc (true=active, false=inactive) |

### 📌 Company Object

| Field           | Type          | Description             |
| --------------- | ------------- | ----------------------- |
| id              | string        | ID công ty              |
| email           | string        | Email công ty           |
| name            | string        | Tên công ty             |
| userId          | string        | ID user sở hữu công ty  |
| primaryIndustry | string        | Ngành chính             |
| size            | string        | Quy mô công ty          |
| foundedIn       | number        | Năm thành lập           |
| description     | string        | Giới thiệu công ty      |
| phone           | string        | Số điện thoại           |
| address         | string        | Địa chỉ                 |
| logo            | string        | Logo công ty            |
| socialMedias    | SocialMedia[] | Danh sách mạng xã hội   |
| isDeleted       | boolean       | Trạng thái xóa          |
| createdAt       | string        | Ngày tạo (ISODate)      |
| updatedAt       | string        | Ngày cập nhật (ISODate) |

### 📌 SocialMedia Object

| Field    | Type   | Description                          |
| -------- | ------ | ------------------------------------ |
| platform | string | Tên mạng xã hội (facebook, twitter…) |
| url      | string | Liên kết đến trang công ty           |

### 📌 Salary Object

| Field      | Type    | Description                |
| ---------- | ------- | -------------------------- |
| min        | number  | Lương tối thiểu            |
| max        | number  | Lương tối đa               |
| currency   | string  | Đơn vị tiền tệ (VND, USD…) |
| negotiable | boolean | Có thể thương lượng không  |

### 📌 WorkTime Object

| Field | Type   | Description          |
| ----- | ------ | -------------------- |
| from  | string | Giờ bắt đầu (HH:mm)  |
| to    | string | Giờ kết thúc (HH:mm) |

### 📌 JobType Object

| Field      | Type   | Description                   |
| ---------- | ------ | ----------------------------- |
| styleClass | string | Loại phân loại (time, level…) |
| type       | string | Giá trị (Full Time, Intern…)  |

### 📌 Example Response

```json
{
  "statusCode": 200,
  "message": "Lấy danh sách công việc thành công!",
  "results": [
    {
      "id": "689307de1152ccfb7a7d3468",
      "logo": "file-1753588732487-4780477.jpg",
      "jobTitle": "Intern Backend NestJS",
      "company": {
        /* Company Object */
      },
      "location": "312 Lê Thánh Tông, Quận 1",
      "description": "We are hiring intern for internship program.",
      "responsibilities": [
        "Chịu trách nhiệm và hoàn thành nhiệm vụ được giao."
      ],
      "skillAndExperience": [
        "Có kiến thức về NestJS và NodeJS.",
        "Có kiến thức về cơ sở dữ liệu."
      ],
      "salary": {
        "min": 0,
        "max": 3000000,
        "currency": "VND",
        "negotiable": true
      },
      "workTime": { "from": "09:00", "to": "18:00" },
      "industry": "Infomation Technology",
      "quantity": 1,
      "country": "Vietnam",
      "city": "Hồ Chí Minh",
      "jobType": [
        { "styleClass": "time", "type": "Full Time" },
        { "styleClass": "level", "type": "Intern" }
      ],
      "destination": null,
      "datePosted": "6/8/2025",
      "expireDate": "30/8/2025",
      "applications": 0,
      "status": true
    }
  ]
}
```
