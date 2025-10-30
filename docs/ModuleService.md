# 📘 Subscription Service API Documentation

## Table of Contents
- [Employer Subscription APIs](#employer-subscription-apis)
- [Candidate Subscription APIs](#candidate-subscription-apis)
- [Common Error Responses](#common-error-responses)

---

# Employer Subscription APIs

## 1. Get All Employer Packages

### `GET /api/v1/employer/subscriptions/packages`

Lấy danh sách tất cả các gói dịch vụ cho Employer, sắp xếp theo giá tăng dần.

**Authentication:** Not required (Public endpoint)

**Request Headers:**
```
Content-Type: application/json
```

**Response Success (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Basic Package",
    "description": "Gói cơ bản cho doanh nghiệp nhỏ",
    "price": 500000,
    "durationDay": 30,
    "isLifetime": false,
    "jobPostLimit": 10,
    "highlightJobLimit": 3
  },
  {
    "id": 2,
    "name": "Premium Package",
    "description": "Gói cao cấp với nhiều tính năng",
    "price": 1500000,
    "durationDay": 90,
    "isLifetime": false,
    "jobPostLimit": 50,
    "highlightJobLimit": 20
  },
  {
    "id": 3,
    "name": "Lifetime Package",
    "description": "Gói vĩnh viễn không giới hạn thời gian",
    "price": 5000000,
    "durationDay": null,
    "isLifetime": true,
    "jobPostLimit": 9999,
    "highlightJobLimit": 9999
  }
]
```

---

## 2. Get Package By ID

### `GET /api/v1/employer/subscriptions/packages/{packageId}`

Lấy thông tin chi tiết của một gói dịch vụ cụ thể.

**Authentication:** Not required

**Path Parameters:**
- `packageId` (Long, required) - ID của gói cần xem

**Request Example:**
```
GET /api/v1/employer/subscriptions/packages/1
```

**Response Success (200 OK):**
```json
{
  "id": 1,
  "name": "Basic Package",
  "description": "Gói cơ bản cho doanh nghiệp nhỏ",
  "price": 500000,
  "durationDay": 30,
  "isLifetime": false,
  "jobPostLimit": 10,
  "highlightJobLimit": 3
}
```

**Response Error (404 Not Found):**
```json
{
  "statusCode": 404,
  "message": "Package not found",
  "error": "Not Found"
}
```

---

## 3. Create Employer Package (Admin Only)

### `POST /api/v1/employer/subscriptions/packages`

Tạo gói dịch vụ mới cho Employer. Chỉ Admin mới có quyền.

**Authentication:** Required (Admin role)

**Request Headers:**
```
Authorization: Bearer {access_token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Enterprise Package",
  "description": "Gói dành cho doanh nghiệp lớn",
  "price": 3000000,
  "durationDay": 180,
  "isLifetime": false,
  "jobPostLimit": 100,
  "highlightJobLimit": 50
}
```

**Request Body (Lifetime Package):**
```json
{
  "name": "Unlimited Package",
  "description": "Gói không giới hạn",
  "price": 10000000,
  "durationDay": null,
  "isLifetime": true,
  "jobPostLimit": 9999,
  "highlightJobLimit": 9999
}
```

**Validation Rules:**
- `name`: Required, not blank
- `price`: Required, >= 0
- `durationDay`: Min 1 (hoặc null nếu lifetime)
- `isLifetime`: Required (true/false)
- `jobPostLimit`: Required, >= 0
- `highlightJobLimit`: Required, >= 0

**Response Success (201 Created):**
```json
{
  "id": 4,
  "name": "Enterprise Package",
  "description": "Gói dành cho doanh nghiệp lớn",
  "price": 3000000,
  "durationDay": 180,
  "isLifetime": false,
  "jobPostLimit": 100,
  "highlightJobLimit": 50
}
```

**Response Error (400 Bad Request):**
```json
{
  "statusCode": 400,
  "message": [
    "name should not be empty",
    "price must be greater than or equal to 0"
  ],
  "error": "Bad Request"
}
```

**Response Error (403 Forbidden):**
```json
{
  "statusCode": 403,
  "message": "Access denied. Required roles: admin",
  "error": "Forbidden"
}
```

---

## 4. Purchase Package

### `POST /api/v1/employer/subscriptions/purchase`

Mua gói dịch vụ. Employer phải đăng nhập và chưa có subscription active.

**Authentication:** Required (Employer role)

**Request Headers:**
```
Authorization: Bearer {access_token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "packageId": 1
}
```

**Response Success (200 OK):**
```json
{
  "id": 101,
  "employerId": 25,
  "packageInfo": {
    "id": 1,
    "name": "Basic Package",
    "description": "Gói cơ bản cho doanh nghiệp nhỏ",
    "price": 500000,
    "durationDay": 30,
    "isLifetime": false,
    "jobPostLimit": 10,
    "highlightJobLimit": 3
  },
  "startDate": "2024-10-30",
  "endDate": "2024-11-29",
  "status": "ACTIVE",
  "isLifetime": false,
  "usedJobCount": 0,
  "usedHighlightCount": 0,
  "totalJobCount": 10,
  "totalHighlightCount": 3,
  "remainingJobCount": 10,
  "remainingHighlightCount": 3,
  "renewCount": 0
}
```

**Response Error (400 Bad Request):**
```json
{
  "statusCode": 400,
  "message": "You already have an active subscription. Please upgrade or wait for it to expire.",
  "error": "Bad Request"
}
```

**Response Error (404 Not Found):**
```json
{
  "statusCode": 404,
  "message": "Package not found",
  "error": "Not Found"
}
```

---

## 5. Get Active Subscription

### `GET /api/v1/employer/subscriptions/active`

Lấy thông tin subscription đang active của employer.

**Authentication:** Required (Employer role)

**Request Headers:**
```
Authorization: Bearer {access_token}
```

**Response Success (200 OK):**
```json
{
  "id": 101,
  "employerId": 25,
  "packageInfo": {
    "id": 1,
    "name": "Basic Package",
    "description": "Gói cơ bản cho doanh nghiệp nhỏ",
    "price": 500000,
    "durationDay": 30,
    "isLifetime": false,
    "jobPostLimit": 10,
    "highlightJobLimit": 3
  },
  "startDate": "2024-10-30",
  "endDate": "2024-11-29",
  "status": "ACTIVE",
  "isLifetime": false,
  "usedJobCount": 5,
  "usedHighlightCount": 1,
  "totalJobCount": 10,
  "totalHighlightCount": 3,
  "remainingJobCount": 5,
  "remainingHighlightCount": 2,
  "renewCount": 0
}
```

**Response Error (404 Not Found):**
```json
{
  "statusCode": 404,
  "message": "No active subscription found",
  "error": "Not Found"
}
```

---

## 6. Get Subscription History

### `GET /api/v1/employer/subscriptions/history`

Lấy lịch sử tất cả các subscription của employer (bao gồm cả expired).

**Authentication:** Required (Employer role)

**Request Headers:**
```
Authorization: Bearer {access_token}
```

**Response Success (200 OK):**
```json
[
  {
    "id": 100,
    "employerId": 25,
    "packageInfo": {
      "id": 1,
      "name": "Basic Package",
      "description": "Gói cơ bản cho doanh nghiệp nhỏ",
      "price": 500000,
      "durationDay": 30,
      "isLifetime": false,
      "jobPostLimit": 10,
      "highlightJobLimit": 3
    },
    "startDate": "2024-09-01",
    "endDate": "2024-10-01",
    "status": "EXPIRED",
    "isLifetime": false,
    "usedJobCount": 10,
    "usedHighlightCount": 3,
    "totalJobCount": 10,
    "totalHighlightCount": 3,
    "remainingJobCount": 0,
    "remainingHighlightCount": 0,
    "renewCount": 0
  },
  {
    "id": 101,
    "employerId": 25,
    "packageInfo": {
      "id": 2,
      "name": "Premium Package",
      "description": "Gói cao cấp với nhiều tính năng",
      "price": 1500000,
      "durationDay": 90,
      "isLifetime": false,
      "jobPostLimit": 50,
      "highlightJobLimit": 20
    },
    "startDate": "2024-10-30",
    "endDate": "2025-01-28",
    "status": "ACTIVE",
    "isLifetime": false,
    "usedJobCount": 5,
    "usedHighlightCount": 1,
    "totalJobCount": 50,
    "totalHighlightCount": 20,
    "remainingJobCount": 45,
    "remainingHighlightCount": 19,
    "renewCount": 0
  }
]
```

---

## 7. Check Usage Status

### `GET /api/v1/employer/subscriptions/usage`

Kiểm tra trạng thái sử dụng subscription hiện tại.

**Authentication:** Required (Employer role)

**Request Headers:**
```
Authorization: Bearer {access_token}
```

**Response Success (200 OK):**
```json
{
  "canPostJob": true,
  "canHighlightJob": true,
  "canApplyJob": null,
  "canHighlightProfile": null,
  "remainingJobPosts": 45,
  "remainingHighlights": 19,
  "remainingApplies": null,
  "remainingHighlightDays": null,
  "message": "Subscription active"
}
```

**Response (No Active Subscription):**
```json
{
  "canPostJob": false,
  "canHighlightJob": false,
  "canApplyJob": null,
  "canHighlightProfile": null,
  "remainingJobPosts": 0,
  "remainingHighlights": 0,
  "remainingApplies": null,
  "remainingHighlightDays": null,
  "message": "No active subscription"
}
```

---

## 8. Calculate Upgrade Cost

### `POST /api/v1/employer/subscriptions/calculate-upgrade`

Tính toán chi phí nâng cấp gói dựa trên phần chưa sử dụng của gói hiện tại.

**Authentication:** Required (Employer role)

**Request Headers:**
```
Authorization: Bearer {access_token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "newPackageId": 2
}
```

**Response Success (200 OK):**
```json
{
  "oldPackageName": "Basic Package",
  "newPackageName": "Premium Package",
  "oldPackagePrice": 500000,
  "newPackagePrice": 1500000,
  "refundPercent": 54.00,
  "refundValue": 270000,
  "finalPrice": 1230000,
  "message": "You will be refunded 54.0% of your current package"
}
```

**Giải thích tính toán:**
- Gói cũ: 500,000 VND
- Đã dùng: 5/10 job (50%), 1/3 highlight (33%), còn 10/30 ngày (33%)
- Trung bình còn lại: (50% + 67% + 33%) / 3 = 50%
- Hoàn: 500,000 × 50% = 250,000
- Phải trả: 1,500,000 - 250,000 = 1,250,000

**Response Error (400 Bad Request):**
```json
{
  "statusCode": 400,
  "message": "Cannot upgrade from lifetime package",
  "error": "Bad Request"
}
```

**Response Error (404 Not Found):**
```json
{
  "statusCode": 404,
  "message": "No active subscription found",
  "error": "Not Found"
}
```

---

## 9. Upgrade Package

### `POST /api/v1/employer/subscriptions/upgrade`

Thực hiện nâng cấp gói subscription.

**Authentication:** Required (Employer role)

**Request Headers:**
```
Authorization: Bearer {access_token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "newPackageId": 2
}
```

**Response Success (200 OK):**
```json
{
  "id": 101,
  "employerId": 25,
  "packageInfo": {
    "id": 2,
    "name": "Premium Package",
    "description": "Gói cao cấp với nhiều tính năng",
    "price": 1500000,
    "durationDay": 90,
    "isLifetime": false,
    "jobPostLimit": 50,
    "highlightJobLimit": 20
  },
  "startDate": "2024-10-30",
  "endDate": "2025-01-28",
  "status": "ACTIVE",
  "isLifetime": false,
  "usedJobCount": 0,
  "usedHighlightCount": 0,
  "totalJobCount": 50,
  "totalHighlightCount": 20,
  "remainingJobCount": 50,
  "remainingHighlightCount": 20,
  "renewCount": 0
}
```

**Note:** Sau khi upgrade, quota sẽ được reset về 0.

---

## 10. Create Add-on Package (Admin Only)

### `POST /api/v1/employer/subscriptions/addons`

Tạo gói add-on để bán thêm quota (job post, highlight, CV view).

**Authentication:** Required (Admin role)

**Request Headers:**
```
Authorization: Bearer {access_token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Extra 10 Highlights",
  "description": "Thêm 10 lượt làm nổi bật job",
  "price": 200000,
  "type": "HIGHLIGHT",
  "quantity": 10,
  "durationDay": 30,
  "isLifetime": false
}
```

**Add-on Types:**
- `JOB_POST` - Thêm lượt đăng job
- `HIGHLIGHT` - Thêm lượt làm nổi bật
- `CV_VIEW` - Thêm lượt xem CV

**Response Success (201 Created):**
```json
{
  "id": 1,
  "name": "Extra 10 Highlights",
  "description": "Thêm 10 lượt làm nổi bật job",
  "price": 200000,
  "type": "HIGHLIGHT",
  "quantity": 10,
  "durationDay": 30,
  "isLifetime": false
}
```

---

## 11. Purchase Add-on

### `POST /api/v1/employer/subscriptions/addons/{addOnId}/purchase`

Mua gói add-on để tăng quota cho subscription hiện tại.

**Authentication:** Required (Employer role)

**Path Parameters:**
- `addOnId` (Long, required) - ID của add-on cần mua

**Request Headers:**
```
Authorization: Bearer {access_token}
```

**Request Example:**
```
POST /api/v1/employer/subscriptions/addons/1/purchase
```

**Response Success (200 OK):**
```json
{
  "success": true,
  "statusCode": 200,
  "message": "Add-on purchased successfully"
}
```

**Note:** Sau khi mua, quota sẽ được cộng vào subscription chính:
- `HIGHLIGHT` type → `totalHighlightCount` += quantity
- `JOB_POST` type → `totalJobCount` += quantity

**Response Error (404 Not Found):**
```json
{
  "statusCode": 404,
  "message": "No active subscription found",
  "error": "Not Found"
}
```

---

# Candidate Subscription APIs

## 1. Get All Candidate Packages

### `GET /api/v1/candidate/subscriptions/packages`

Lấy danh sách tất cả các gói dịch vụ cho Candidate.

**Authentication:** Not required

**Response Success (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Basic Candidate Package",
    "description": "Gói cơ bản cho ứng viên",
    "price": 200000,
    "durationDay": 30,
    "isLifetime": false,
    "highlightProfileDays": 7,
    "jobApplyLimit": 20,
    "canViewOtherCandidates": false
  },
  {
    "id": 2,
    "name": "Premium Candidate Package",
    "description": "Gói cao cấp với nhiều tính năng",
    "price": 500000,
    "durationDay": 90,
    "isLifetime": false,
    "highlightProfileDays": 30,
    "jobApplyLimit": 100,
    "canViewOtherCandidates": true
  }
]
```

---

## 2. Get Candidate Package By ID

### `GET /api/v1/candidate/subscriptions/packages/{packageId}`

Lấy chi tiết một gói candidate.

**Path Parameters:**
- `packageId` (Long, required)

**Response Success (200 OK):**
```json
{
  "id": 1,
  "name": "Basic Candidate Package",
  "description": "Gói cơ bản cho ứng viên",
  "price": 200000,
  "durationDay": 30,
  "isLifetime": false,
  "highlightProfileDays": 7,
  "jobApplyLimit": 20,
  "canViewOtherCandidates": false
}
```

---

## 3. Create Candidate Package (Admin Only)

### `POST /api/v1/candidate/subscriptions/packages`

Tạo gói dịch vụ mới cho Candidate.

**Authentication:** Required (Admin role)

**Request Body:**
```json
{
  "name": "VIP Candidate Package",
  "description": "Gói VIP với tất cả tính năng",
  "price": 1000000,
  "durationDay": 180,
  "isLifetime": false,
  "highlightProfileDays": 60,
  "jobApplyLimit": 500,
  "canViewOtherCandidates": true
}
```

**Response Success (201 Created):**
```json
{
  "id": 3,
  "name": "VIP Candidate Package",
  "description": "Gói VIP với tất cả tính năng",
  "price": 1000000,
  "durationDay": 180,
  "isLifetime": false,
  "highlightProfileDays": 60,
  "jobApplyLimit": 500,
  "canViewOtherCandidates": true
}
```

---

## 4. Purchase Candidate Package

### `POST /api/v1/candidate/subscriptions/purchase`

Mua gói dịch vụ cho candidate.

**Authentication:** Required (Candidate role)

**Request Body:**
```json
{
  "packageId": 1
}
```

**Response Success (200 OK):**
```json
{
  "id": 201,
  "candidateId": 15,
  "packageInfo": {
    "id": 1,
    "name": "Basic Candidate Package",
    "description": "Gói cơ bản cho ứng viên",
    "price": 200000,
    "durationDay": 30,
    "isLifetime": false,
    "highlightProfileDays": 7,
    "jobApplyLimit": 20,
    "canViewOtherCandidates": false
  },
  "subscriptionCode": "SUB-A7B3C9D2",
  "startDate": "2024-10-30",
  "endDate": "2024-11-29",
  "status": "ACTIVE",
  "isLifetime": false,
  "usedApplyCount": 0,
  "usedHighlightDays": 0,
  "totalApplyLimit": 20,
  "totalHighlightDays": 7,
  "remainingApplyCount": 20,
  "remainingHighlightDays": 7,
  "renewCount": 0
}
```

**Note:** Mỗi subscription được tạo với `subscriptionCode` unique để tracking.

---

## 5. Get Active Candidate Subscription

### `GET /api/v1/candidate/subscriptions/active`

Lấy subscription đang active của candidate.

**Authentication:** Required (Candidate role)

**Response Success (200 OK):**
```json
{
  "id": 201,
  "candidateId": 15,
  "packageInfo": {
    "id": 1,
    "name": "Basic Candidate Package",
    "description": "Gói cơ bản cho ứng viên",
    "price": 200000,
    "durationDay": 30,
    "isLifetime": false,
    "highlightProfileDays": 7,
    "jobApplyLimit": 20,
    "canViewOtherCandidates": false
  },
  "subscriptionCode": "SUB-A7B3C9D2",
  "startDate": "2024-10-30",
  "endDate": "2024-11-29",
  "status": "ACTIVE",
  "isLifetime": false,
  "usedApplyCount": 8,
  "usedHighlightDays": 2,
  "totalApplyLimit": 20,
  "totalHighlightDays": 7,
  "remainingApplyCount": 12,
  "remainingHighlightDays": 5,
  "renewCount": 0
}
```

---

## 6. Get Candidate Subscription History

### `GET /api/v1/candidate/subscriptions/history`

Lấy lịch sử tất cả subscription của candidate.

**Authentication:** Required (Candidate role)

**Response Success (200 OK):**
```json
[
  {
    "id": 200,
    "candidateId": 15,
    "packageInfo": {
      "id": 1,
      "name": "Basic Candidate Package",
      "description": "Gói cơ bản cho ứng viên",
      "price": 200000,
      "durationDay": 30,
      "isLifetime": false,
      "highlightProfileDays": 7,
      "jobApplyLimit": 20,
      "canViewOtherCandidates": false
    },
    "subscriptionCode": "SUB-X1Y2Z3W4",
    "startDate": "2024-09-01",
    "endDate": "2024-10-01",
    "status": "EXPIRED",
    "isLifetime": false,
    "usedApplyCount": 20,
    "usedHighlightDays": 7,
    "totalApplyLimit": 20,
    "totalHighlightDays": 7,
    "remainingApplyCount": 0,
    "remainingHighlightDays": 0,
    "renewCount": 0
  },
  {
    "id": 201,
    "candidateId": 15,
    "packageInfo": {
      "id": 2,
      "name": "Premium Candidate Package",
      "description": "Gói cao cấp với nhiều tính năng",
      "price": 500000,
      "durationDay": 90,
      "isLifetime": false,
      "highlightProfileDays": 30,
      "jobApplyLimit": 100,
      "canViewOtherCandidates": true
    },
    "subscriptionCode": "SUB-A7B3C9D2",
    "startDate": "2024-10-30",
    "endDate": "2025-01-28",
    "status": "ACTIVE",
    "isLifetime": false,
    "usedApplyCount": 8,
    "usedHighlightDays": 2,
    "totalApplyLimit": 100,
    "totalHighlightDays": 30,
    "remainingApplyCount": 92,
    "remainingHighlightDays": 28,
    "renewCount": 0
  }
]
```

---

## 7. Check Candidate Usage

### `GET /api/v1/candidate/subscriptions/usage`

Kiểm tra trạng thái sử dụng của candidate.

**Authentication:** Required (Candidate role)

**Response Success (200 OK):**
```json
{
  "canPostJob": null,
  "canHighlightJob": null,
  "canApplyJob": true,
  "canHighlightProfile": true,
  "remainingJobPosts": null,
  "remainingHighlights": null,
  "remainingApplies": 92,
  "remainingHighlightDays": 28,
  "message": "Subscription active"
}
```

---

## 8. Check Can View Other Candidates

### `GET /api/v1/candidate/subscriptions/can-view-others`

Kiểm tra candidate có quyền xem profile của candidate khác không.

**Authentication:** Required (Candidate role)

**Response Success (200 OK) - Can View:**
```json
{
  "success": true,
  "statusCode": 200,
  "message": "You can view other candidates"
}
```

**Response Success (200 OK) - Cannot View:**
```json
{
  "success": false,
  "statusCode": 200,
  "message": "Upgrade to view other candidates"
}
```

---

## 9. Calculate Candidate Upgrade

### `POST /api/v1/candidate/subscriptions/calculate-upgrade`

Tính toán chi phí nâng cấp cho candidate.

**Authentication:** Required (Candidate role)

**Request Body:**
```json
{
  "newPackageId": 2
}
```

**Response Success (200 OK):**
```json
{
  "oldPackageName": "Basic Candidate Package",
  "newPackageName": "Premium Candidate Package",
  "oldPackagePrice": 200000,
  "newPackagePrice": 500000,
  "refundPercent": 60.00,
  "refundValue": 120000,
  "finalPrice": 380000,
  "message": "You will be refunded 60.0% of your current package"
}
```

**Giải thích:**
- Đã apply: 8/20 (60% còn lại)
- Đã highlight: 2/7 ngày (71% còn lại)
- Thời gian: 20/30 ngày còn lại (67%)
- Trung bình: (60% + 71% + 67%) / 3 = 66%
- Refund: 200,000 × 66% = 132,000
- Phải trả: 500,000 - 132,000 = 368,000

---

## 10. Upgrade Candidate Package

### `POST /api/v1/candidate/subscriptions/upgrade`

Thực hiện nâng cấp gói cho candidate.

**Authentication:** Required (Candidate role)

**Request Body:**
```json
{
  "newPackageId": 2
}
```

**Response Success (200 OK):**
```json
{
  "id": 201,
  "candidateId": 15,
  "packageInfo": {
    "id": 2,
    "name": "Premium Candidate Package",
    "description": "Gói cao cấp với nhiều tính năng",
    "price": 500000,
    "durationDay": 90,
    "isLifetime": false,
    "highlightProfileDays": 30,
    "jobApplyLimit": 100,
    "canViewOtherCandidates": true
  },
  "subscriptionCode": "SUB-A7B3C9D2",
  "startDate": "2024-10-30",
  "endDate": "2025-01-28",
  "status": "ACTIVE",
  "isLifetime": false,
  "usedApplyCount": 0,
  "usedHighlightDays": 0,
  "totalApplyLimit": 100,
  "totalHighlightDays": 30,
  "remainingApplyCount": 100,
  "remainingHighlightDays": 30,
  "renewCount": 0
}
```

---

# Common Error Responses

## Authentication Errors

**401 Unauthorized - No Token:**
```json
{
  "statusCode": 401,
  "message": "Not authenticated",
  "error": "Unauthorized"
}
```

**401 Unauthorized - Invalid Token:**
```json
{
  "statusCode": 401,
  "message": "Invalid token",
  "error": "Unauthorized"
}
```

**401 Unauthorized - Token Expired:**
```json
{
  "statusCode": 401,
  "message": "Token has expired",
  "error": "Unauthorized"
}
```

## Authorization Errors

**403 Forbidden - Wrong Role:**
```json
{
  "statusCode": 403,
  "message": "Access denied. Required roles: admin",
  "error": "Forbidden"
}
```

**403 Forbidden - No Active Subscription:**
```json
{
  "statusCode": 403,
  "message": "No active subscription found",
  "error": "Forbidden"
}
```

**403 Forbidden - Quota Exceeded:**
```json
{
  "statusCode": 403,
  "message": "You have reached your job post limit. Please upgrade your package.",
  "error": "Forbidden"
}
```

## Validation Errors

**400 Bad Request - Validation Failed:**
```json
{
  "statusCode": 400,
  "message": [
    "name should not be empty",
    "price must be greater than or equal to 0",
    "Package ID is required"
  ],
  "error": "Bad Request"
}
```

**400 Bad Request - Business Logic:**
```json
{
  "statusCode": 400,
  "message": "You already have an active subscription. Please upgrade or wait for it to expire.",
  "error": "Bad Request"
}
```

**400 Bad Request - Cannot Upgrade Lifetime:**
```json
{
  "statusCode": 400,
  "message": "Cannot upgrade from lifetime package",
  "error": "Bad Request"
}
```

## Not Found Errors

**404 Not Found - Package:**
```json
{
  "statusCode": 404,
  "message": "Package not found",
  "error": "Not Found"
}
```

**404 Not Found - Subscription:**
```json
{
  "statusCode": 404,
  "message": "No active subscription found",
  "error": "Not Found"
}
```

**404 Not Found - Add-on:**
```json
{
  "statusCode": 404,
  "message": "Add-on not found",
  "error": "Not Found"
}
```

## Server Errors

**500 Internal Server Error:**
```json
{
  "statusCode": 500,
  "message": "EmployerSubscriptionService.purchasePackage: Unexpected error occurred",
  "error": "Internal Server Error",
  "details": "Database connection timeout"
}
```

---

# Usage Flow Examples

## Flow 1: Employer Mua Gói Lần Đầu

1. **Xem danh sách gói:**
   ```
   GET /api/v1/employer/subscriptions/packages
   ```

2. **Xem chi tiết gói:**
   ```
   GET /api/v1/employer/subscriptions/packages/1
   ```

3. **Mua gói:**
   ```
   POST /api/v1/employer/subscriptions/purchase
   Body: { "packageId": 1 }
   ```

4. **Kiểm tra subscription:**
   ```
   GET /api/v1/employer/subscriptions/active
   ```

5. **Kiểm tra usage:**
   ```
   GET /api/v1/employer/subscriptions/usage
   ```

## Flow 2: Employer Nâng Cấp Gói

1. **Kiểm tra gói hiện tại:**
   ```
   GET /api/v1/employer/subscriptions/active
   ```

2. **Tính toán chi phí upgrade:**
   ```
   POST /api/v1/employer/subscriptions/calculate-upgrade
   Body: { "newPackageId": 2 }
   ```

3. **Xác nhận upgrade:**
   ```
   POST /api/v1/employer/subscriptions/upgrade
   Body: { "newPackageId": 2 }
   ```

## Flow 3: Employer Hết Quota Highlight

1. **Kiểm tra usage:**
   ```
   GET /api/v1/employer/subscriptions/usage
   Response: { "remainingHighlights": 0 }
   ```

2. **Mua add-on:**
   ```
   POST /api/v1/employer/subscriptions/addons/1/purchase
   ```

3. **Kiểm tra lại usage:**
   ```
   GET /api/v1/employer/subscriptions/usage
   Response: { "remainingHighlights": 10 }
   ```

## Flow 4: Candidate Đăng Ký & Sử Dụng

1. **Xem các gói:**
   ```
   GET /api/v1/candidate/subscriptions/packages
   ```

2. **Mua gói:**
   ```
   POST /api/v1/candidate/subscriptions/purchase
   Body: { "packageId": 1 }
   ```

3. **Ứng tuyển job (tự động trừ quota):**
   ```
   Internally calls: candidateSubscriptionService.useJobApply(candidateId)
   ```

4. **Kiểm tra còn lại:**
   ```
   GET /api/v1/candidate/subscriptions/usage
   Response: { "remainingApplies": 19 }
   ```

5. **Kiểm tra quyền xem candidate khác:**
   ```
   GET /api/v1/candidate/subscriptions/can-view-others
   ```

---

# Integration Notes

## Tích hợp với Job Service

Khi employer đăng job, cần gọi:

```java
@PostMapping("/jobs")
@RequireRole({"employer"})
public ResponseEntity<?> createJob(Authentication auth) {
    Long employerId = Long.parseLong(auth.getName());

    // Kiểm tra và trừ quota
    employerSubscriptionService.useJobPost(employerId);

    // Tạo job
    Job job = jobService.createJob();

    return ResponseEntity.ok(job);
}
```

## Tích hợp với Application Service

Khi candidate ứng tuyển:

```java
@PostMapping("/applications")
@RequireRole({"candidate"})
public ResponseEntity<?> applyJob(Authentication auth) {
    Long candidateId = Long.parseLong(auth.getName());
    
    // Kiểm tra và trừ quota
    candidateSubscriptionService.useJobApply(candidateId);
    
    // Tạo application
    Application app = applicationService.apply();
    
    return ResponseEntity.ok(app);
}
```

## Tích hợp với Highlight Service

Khi employer làm nổi bật job:

```java
@PostMapping("/jobs/{jobId}/highlight")
@RequireRole({"employer"})
public ResponseEntity<?> highlightJob(
        @PathVariable Long jobId,
        Authentication auth) {
    Long employerId = Long.parseLong(auth.getName());
    
    // Kiểm tra và trừ quota
    employerSubscriptionService.useHighlight(employerId);
    
    // Highlight job
    jobService.highlightJob(jobId);
    
    return ResponseEntity.ok("Job highlighted");
}
```

## Tích hợp với Profile Service

Khi candidate kích hoạt highlight profile:

```java
@PostMapping("/profile/highlight")
@RequireRole({"candidate"})
public ResponseEntity<?> highlightProfile(Authentication auth) {
    Long candidateId = Long.parseLong(auth.getName());
    
    // Kiểm tra và trừ quota
    candidateSubscriptionService.useHighlightDay(candidateId);
    
    // Highlight profile
    profileService.activateHighlight(candidateId);
    
    return ResponseEntity.ok("Profile highlighted");
}
```

---

# Testing Examples

## cURL Examples

### Get all employer packages:
```bash
curl -X GET "http://localhost:8080/api/v1/employer/subscriptions/packages" \
  -H "Content-Type: application/json"
```

### Purchase package (Employer):
```bash
curl -X POST "http://localhost:8080/api/v1/employer/subscriptions/purchase" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"packageId": 1}'
```

### Calculate upgrade cost:
```bash
curl -X POST "http://localhost:8080/api/v1/employer/subscriptions/calculate-upgrade" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"newPackageId": 2}'
```

### Check usage:
```bash
curl -X GET "http://localhost:8080/api/v1/employer/subscriptions/usage" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### Create package (Admin):
```bash
curl -X POST "http://localhost:8080/api/v1/employer/subscriptions/packages" \
  -H "Authorization: Bearer ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Enterprise Package",
    "description": "Gói doanh nghiệp",
    "price": 3000000,
    "durationDay": 180,
    "isLifetime": false,
    "jobPostLimit": 100,
    "highlightJobLimit": 50
  }'
```

## Postman Collection Structure

```
Subscription APIs/
├── Employer/
│   ├── Packages/
│   │   ├── Get All Packages
│   │   ├── Get Package By ID
│   │   └── Create Package (Admin)
│   ├── Subscriptions/
│   │   ├── Purchase Package
│   │   ├── Get Active Subscription
│   │   ├── Get History
│   │   └── Check Usage
│   ├── Upgrade/
│   │   ├── Calculate Upgrade
│   │   └── Upgrade Package
│   └── Add-ons/
│       ├── Create Add-on (Admin)
│       └── Purchase Add-on
└── Candidate/
    ├── Packages/
    │   ├── Get All Packages
    │   ├── Get Package By ID
    │   └── Create Package (Admin)
    ├── Subscriptions/
    │   ├── Purchase Package
    │   ├── Get Active Subscription
    │   ├── Get History
    │   ├── Check Usage
    │   └── Can View Others
    └── Upgrade/
        ├── Calculate Upgrade
        └── Upgrade Package
```

---

# Business Rules Summary

## Employer Rules

1. ✅ Một employer chỉ có thể có **1 subscription ACTIVE** tại một thời điểm
2. ✅ Không thể mua gói mới khi đang có subscription active (phải upgrade)
3. ✅ Khi upgrade, quota sẽ được **reset về 0**
4. ✅ Add-on sẽ **cộng dồn** vào quota hiện tại
5. ✅ Lifetime package **không thể upgrade**
6. ✅ Khi hết quota, employer có thể:
    - Mua add-on để tăng quota
    - Upgrade lên gói cao hơn
7. ✅ Subscription tự động expire khi `endDate < currentDate`

## Candidate Rules

1. ✅ Một candidate chỉ có thể có **1 subscription ACTIVE**
2. ✅ Mỗi subscription có **subscriptionCode** unique
3. ✅ Khi upgrade, quota được **reset về 0**
4. ✅ Quyền `canViewOtherCandidates` chỉ có ở gói cao cấp
5. ✅ Lifetime package **không thể upgrade**
6. ✅ Khi hết quota apply, candidate phải upgrade
7. ✅ Highlight profile sử dụng theo **ngày** (usedHighlightDays)

## Upgrade Calculation Rules

**Công thức refund:**
```
remainingPercent = (
  (1 - used/total) for each feature +
  remainingDays/totalDays
) / numberOfFeatures

refundValue = oldPrice × remainingPercent
finalPrice = newPrice - refundValue (min 0)
```

**Example cho Employer:**
- Features: jobPostLimit, highlightJobLimit, durationDay
- NumberOfFeatures = 3

**Example cho Candidate:**
- Features: jobApplyLimit, highlightProfileDays, durationDay
- NumberOfFeatures = 3

---

# Status Definitions

## Subscription Status

- **ACTIVE**: Subscription đang hoạt động
- **EXPIRED**: Đã hết hạn (tự động bởi cron job)
- **SUSPENDED**: Bị tạm ngưng (admin action)
- **QUEUED**: Đang chờ xử lý (dự phòng cho future feature)

## Add-on Status

- **ACTIVE**: Add-on đang sử dụng được
- **EXPIRED**: Add-on đã hết hạn

---

# Database Schema Reference

## Key Tables

### employer_subscriptions
```sql
id, employer_id, package_id, start_date, end_date, 
status, is_lifetime, used_job_count, used_highlight_count,
total_job_count, total_highlight_count, renew_count
```

### candidate_subscriptions
```sql
id, candidate_id, package_id, subscription_code,
start_date, end_date, status, is_lifetime,
used_apply_count, used_highlight_days,
total_apply_limit, total_highlight_days, renew_count
```

### subscription_upgrade_history
```sql
id, subscription_id, subscription_type,
old_package_id, new_package_id,
refund_percent, refund_value, final_price
```

---

# Scheduled Jobs

## Cron Schedule

- **00:00 daily**: Expire subscriptions & add-ons
- **09:00 daily**: Send expiration warnings (7 days before)

## Enable Scheduling

Add to main application class:
```java
@SpringBootApplication
@EnableScheduling
public class JrpApplication {
    // ...
}
```

---

# Security & Permissions

## Role Matrix

| Endpoint | admin | employer | candidate | Public |
|----------|-------|----------|-----------|--------|
| GET packages | ✅ | ✅ | ✅ | ✅ |
| POST packages | ✅ | ❌ | ❌ | ❌ |
| POST purchase | ✅ | ✅ (employer only) | ✅ (candidate only) | ❌ |
| GET active | ✅ | ✅ | ✅ | ❌ |
| POST upgrade | ✅ | ✅ | ✅ | ❌ |
| POST create addon | ✅ | ❌ | ❌ | ❌ |

---

# FAQ

**Q: Employer có thể có nhiều subscription cùng lúc không?**
A: Không. Chỉ có 1 subscription ACTIVE tại một thời điểm.

**Q: Khi upgrade, quota cũ có được giữ lại không?**
A: Không. Quota sẽ reset về 0 theo gói mới.

**Q: Add-on khác gì với upgrade?**
A: Add-on chỉ tăng quota mà không đổi gói. Upgrade đổi sang gói khác.

**Q: Lifetime package có hết quota không?**
A: Không. Lifetime package có quota rất lớn (9999).

**Q: Candidate có thể gia hạn gói cũ không?**
A: Hiện tại chưa có tính năng renew. Phải mua gói mới sau khi hết hạn.

**Q: Refund được tính như thế nào?**
A: Dựa trên trung bình % chưa sử dụng của các features và thời gian còn lại.

---

# Version History

- **v1.0** (2024-10-30): Initial release
    - Employer subscription management
    - Candidate subscription management
    - Upgrade calculation
    - Add-on support
    - Automatic expiration