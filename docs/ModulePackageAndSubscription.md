#  API Documentation - Gói Dịch Vụ (Service Packages) _(COMING SOON)_

Tài liệu này định nghĩa các
endpoint REST API để quản lý, mua, gia hạn, và nâng cấp các gói dịch vụ cho Doanh nghiệp (Employer) và Ứng viên (Candidate).

**Base API Path**: `/api/v1`

## 🔐 Xác Thực (Authentication)

Tất cả các endpoint (ngoại trừ các endpoint `PUBLIC`) đều yêu cầu xác thực. Hệ thống sử dụng `Bearer Token` (JWT) trong header `Authorization`.

* **Header**: `Authorization: Bearer <your-jwt-token>`
* Các endpoint yêu cầu quyền `ADMIN`, `EMPLOYER`, hoặc `CANDIDATE` sẽ được ghi chú rõ.

---

## I. Quản lý Gói Dịch Vụ (Admin Only)

Các endpoint này dùng để ADMIN tạo và quản lý các gói dịch vụ "thô" (sản phẩm).

**Yêu cầu**: Quyền `ADMIN`.

### 1.1. Employer Service Package

Quản lý các gói cho Doanh nghiệp.

#### `POST /admin/employer-packages`

Tạo một gói dịch vụ mới cho doanh nghiệp.

* **Request Body**: `EmployerPackageDTO` (xem Phụ lục)
    ```json
    {
      "name": "Gói Đăng Tuyển Premium",
      "description": "Đăng 10 job, làm nổi bật 5 job.",
      "price": 5000000.00,
      "durationDay": 30,
      "isLifetime": false,
      "jobPostLimit": 10,
      "highlightJobLimit": 5
    }
    ```
* **Response (201 Created)**: `EmployerPackageDTO` (đã tạo, kèm ID)
    ```json
    {
      "id": 1,
      "name": "Gói Đăng Tuyển Premium",
      "description": "Đăng 10 job, làm nổi bật 5 job.",
      "price": 5000000.00,
      "durationDay": 30,
      "isLifetime": false,
      "jobPostLimit": 10,
      "highlightJobLimit": 5,
      "createdAt": "2025-10-29T10:00:00Z"
    }
    ```

#### `GET /admin/employer-packages`

Lấy danh sách tất cả các gói dịch vụ của doanh nghiệp.

* **Response (200 OK)**: `List<EmployerPackageDTO>`
    ```json
    [
      {
        "id": 1,
        "name": "Gói Đăng Tuyển Premium",
        "price": 5000000.00,
        "durationDay": 30,
        "jobPostLimit": 10
      }
    ]
    ```

#### `GET /admin/employer-packages/{packageId}`

Lấy chi tiết một gói dịch vụ của doanh nghiệp.

* **Response (200 OK)**: `EmployerPackageDTO`
* **Response (404 Not Found)**: Nếu `packageId` không tồn tại.

#### `PUT /admin/employer-packages/{packageId}`

Cập nhật thông tin gói dịch vụ. (Lưu ý: Việc này nên tạo version mới, các subscription cũ không bị ảnh hưởng).

* **Request Body**: `EmployerPackageDTO`
* **Response (200 OK)**: `EmployerPackageDTO` (đã cập nhật)
* **Response (404 Not Found)**: Nếu `packageId` không tồn tại.

---

### 1.2. Candidate Service Package

Quản lý các gói cho Ứng viên (tương tự Employer).

#### `POST /admin/candidate-packages`

Tạo gói mới cho ứng viên.

* **Request Body**: `CandidatePackageDTO`
    ```json
    {
      "name": "Hồ Sơ Nổi Bật",
      "description": "Nổi bật hồ sơ trong 7 ngày, ứng tuyển 50 job.",
      "price": 150000.00,
      "durationDay": 30,
      "isLifetime": false,
      "highlightProfileDays": 7,
      "jobApplyLimit": 50,
      "canViewOtherCandidates": false
    }
    ```
* **Response (201 Created)**: `CandidatePackageDTO`

#### `GET /admin/candidate-packages`

Lấy danh sách tất cả các gói của ứng viên.

* **Response (200 OK)**: `List<CandidatePackageDTO>`

#### `GET /admin/candidate-packages/{packageId}`

Lấy chi tiết một gói của ứng viên.

* **Response (200 OK)**: `CandidatePackageDTO`

#### `PUT /admin/candidate-packages/{packageId}`

Cập nhật gói của ứng viên.

* **Request Body**: `CandidatePackageDTO`
* **Response (200 OK)**: `CandidatePackageDTO` (đã cập nhật)

---

### 1.3. Employer Add-on Package

Quản lý các gói bổ sung (mua lẻ) cho Doanh nghiệp.

#### `POST /admin/employer-add-ons`

Tạo một gói add-on mới.

* **Request Body**: `EmployerAddOnPackageDTO`
    ```json
    {
      "name": "Gói 5 Job Nổi Bật",
      "description": "Mua thêm 5 lượt làm nổi bật job",
      "price": 500000.00,
      "type": "HIGHLIGHT",
      "quantity": 5,
      "durationDay": 30
    }
    ```
* **Response (201 Created)**: `EmployerAddOnPackageDTO`

#### `GET /admin/employer-add-ons`

Lấy danh sách tất cả các gói add-on.

* **Response (200 OK)**: `List<EmployerAddOnPackageDTO>`

#### `GET /admin/employer-add-ons/{addOnId}`

Lấy chi tiết một gói add-on.

* **Response (200 OK)**: `EmployerAddOnPackageDTO`

#### `PUT /admin/employer-add-ons/{addOnId}`

Cập nhật một gói add-on.

* **Request Body**: `EmployerAddOnPackageDTO`
* **Response (200 OK)**: `EmployerAddOnPackageDTO` (đã cập nhật)

---

## II. Nghiệp Vụ Doanh Nghiệp (Employer)

Các endpoint cho doanh nghiệp đã đăng nhập.

**Yêu cầu**: Quyền `EMPLOYER`.

### 2.1. Xem Gói Dịch Vụ

#### `GET /employer/packages`

Lấy danh sách các gói dịch vụ (EmployerServicePackage) mà doanh nghiệp CÓ THỂ MUA.

* **Response (200 OK)**: `List<EmployerPackageDTO>`

#### `GET /employer/add-on-packages`

Lấy danh sách các gói add-on (EmployerAddOnPackage) mà doanh nghiệp CÓ THỂ MUA.

* **Response (200 OK)**: `List<EmployerAddOnPackageDTO>`

### 2.2. Quản Lý Đăng Ký (Subscription)

#### `GET /employer/subscriptions/me`

Lấy (các) gói subscription HIỆN TẠI (`ACTIVE` hoặc `QUEUED`) của doanh nghiệp.

* **Response (200 OK)**: `EmployerSubscriptionDTO` (hoặc list nếu cho phép nhiều gói)
    ```json
    {
      "id": 101,
      "employerId": 50,
      "packageId": 1,
      "packageName": "Gói Đăng Tuyển Premium",
      "startDate": "2025-10-01",
      "endDate": "2025-10-31",
      "status": "ACTIVE",
      "isLifetime": false,
      "usedJobCount": 5,
      "usedHighlightCount": 2,
      "totalJobCount": 10,
      "totalHighlightCount": 5
    }
    ```
* **Response (404 Not Found)**: Nếu doanh nghiệp không có gói nào.

#### `GET /employer/subscriptions/history`

Lấy lịch sử tất cả các gói đã mua (bao gồm `EXPIRED`).

* **Response (200 OK)**: `List<EmployerSubscriptionDTO>`

#### `POST /employer/subscriptions`

Mua mới hoặc gia hạn một gói dịch vụ.

* **Logic**:
    * Nếu chưa có gói: Tạo mới.
    * Nếu đang có gói `ACTIVE`: Thực hiện logic gia hạn (cộng dồn, hoặc xếp hàng `QUEUED` tùy nghiệp vụ).
    * Nếu có gói `EXPIRED`: Tạo mới.
* **Request Body**:
    ```json
    {
      "packageId": 1,
      "paymentToken": "pm_123xyz_abc"
    }
    ```
* **Response (201 Created)**: `EmployerSubscriptionDTO` (subscription mới được tạo/gia hạn)

### 2.3. Mua Gói Bổ Sung (Add-on)

#### `POST /employer/add-ons/purchase`

Mua một gói add-on (ví dụ: thêm lượt highlight).

* **Logic**: Tạo một bản ghi `EmployerAddOnUsage` và cộng dồn quyền lợi vào `EmployerSubscription` hiện tại (nếu có) hoặc cho phép dùng độc lập.
* **Request Body**:
    ```json
    {
      "addOnPackageId": 3,
      "paymentToken": "pm_456abc_def"
    }
    ```
* **Response (201 Created)**: `EmployerAddOnUsageDTO` (chi tiết add-on đã mua)

### 2.4. Nâng Cấp Gói (Upgrade)

#### `POST /employer/subscriptions/upgrade/quote`

Lấy báo giá (tính toán khấu trừ) khi muốn nâng cấp từ gói hiện tại lên gói mới.

* **Logic**: Tính toán dựa trên logic "Nghiệp Vụ Khi Nâng Cấp Gói".
* **Request Body**:
    ```json
    {
      "newPackageId": 2
    }
    ```
* **Response (200 OK)**: `UpgradeQuoteDTO`
    ```json
    {
      "oldPackageId": 1,
      "newPackageId": 2,
      "oldPackagePrice": 1000000.00,
      "newPackagePrice": 3000000.00,
      "refundPercent": 0.54,
      "refundValue": 540000.00,
      "finalPrice": 2460000.00
    }
    ```
* **Response (400 Bad Request)**: Nếu không đủ điều kiện nâng cấp (ví dụ: gói vĩnh viễn, gói đã hết hạn).

#### `POST /employer/subscriptions/upgrade`

Xác nhận thực hiện nâng cấp gói.

* **Logic**: Hủy gói cũ, tạo gói mới với ngày bắt đầu là hôm nay và khấu trừ chi phí. Lưu lịch sử vào `SubscriptionUpgradeHistory`.
* **Request Body**:
    ```json
    {
      "newPackageId": 2,
      "paymentToken": "pm_789jkl_ghi"
    }
    ```
* **Response (201 Created)**: `EmployerSubscriptionDTO` (gói subscription mới sau khi nâng cấp)

---

## III. Nghiệp Vụ Ứng Viên (Candidate)

Các endpoint cho ứng viên đã đăng nhập.

**Yêu cầu**: Quyền `CANDIDATE`.

### 3.1. Xem Gói Dịch Vụ

#### `GET /candidate/packages`

Lấy danh sách các gói dịch vụ (CandidateServicePackage) mà ứng viên CÓ THỂ MUA.

* **Response (200 OK)**: `List<CandidatePackageDTO>`

### 3.2. Quản Lý Đăng Ký (Subscription)

#### `GET /candidate/subscriptions/me`

Lấy gói subscription HIỆN TẠI (`ACTIVE` hoặc `QUEUED`) của ứng viên.

* **Response (200 OK)**: `CandidateSubscriptionDTO`
    ```json
    {
      "id": 201,
      "candidateId": 80,
      "packageId": 5,
      "packageName": "Hồ Sơ Nổi Bật",
      "subscriptionCode": "SUB-9A8B7C",
      "startDate": "2025-10-15",
      "endDate": "2025-11-14",
      "status": "ACTIVE",
      "isLifetime": false,
      "usedApplyCount": 10,
      "usedHighlightDays": 3,
      "totalApplyLimit": 50,
      "totalHighlightDays": 7
    }
    ```
* **Response (404 Not Found)**: Nếu ứng viên không có gói nào.

#### `GET /candidate/subscriptions/history`

Lấy lịch sử tất cả các gói đã mua (bao gồm `EXPIRED`).

* **Response (200 OK)**: `List<CandidateSubscriptionDTO>`

#### `POST /candidate/subscriptions`

Mua mới hoặc gia hạn một gói dịch vụ.

* **Request Body**:
    ```json
    {
      "packageId": 5,
      "paymentToken": "pm_abc123_xyz"
    }
    ```
* **Response (201 Created)**: `CandidateSubscriptionDTO` (subscription mới được tạo/gia hạn)

### 3.3. Nâng Cấp Gói (Upgrade)

#### `POST /candidate/subscriptions/upgrade/quote`

Lấy báo giá (tính toán khấu trừ) khi muốn nâng cấp.

* **Logic**: Tương tự employer, nhưng tính trên `jobApplyLimit`, `highlightProfileDays`, `durationDay`.
* **Request Body**:
    ```json
    {
      "newPackageId": 6
    }
    ```
* **Response (200 OK)**: `UpgradeQuoteDTO`
    ```json
    {
      "oldPackageId": 5,
      "newPackageId": 6,
      "oldPackagePrice": 150000.00,
      "newPackagePrice": 500000.00,
      "refundPercent": 0.30,
      "refundValue": 45000.00,
      "finalPrice": 455000.00
    }
    ```
* **Response (400 Bad Request)**: Nếu không đủ điều kiện.

#### `POST /candidate/subscriptions/upgrade`

Xác nhận thực hiện nâng cấp gói.

* **Request Body**:
    ```json
    {
      "newPackageId": 6,
      "paymentToken": "pm_def456_uvw"
    }
    ```
* **Response (201 Created)**: `CandidateSubscriptionDTO` (gói subscription mới sau khi nâng cấp)

---

## IV. Tác Vụ Hệ Thống (Internal / Cron Job)

Đây không phải là API public, mà là mô tả các tác vụ nền cần chạy.

### 4.1. Cron Job: Hết Hạn Gói

* **Tần suất**: Chạy hàng ngày (ví dụ: 00:00).
* **Logic**:
    1.  Quét bảng `EmployerSubscription` và `CandidateSubscription`.
    2.  Tìm các bản ghi có `status = 'ACTIVE'` và `endDate < NOW()`.
    3.  Cập nhật `status = 'EXPIRED'`.

---

## V. Phụ Lục: Cấu Trúc Dữ Liệu (DTOs)

```ts 
// ----------------------------------------------------------------
// I. Kiểu dữ liệu (Enums/Literal Types) dùng chung
// ----------------------------------------------------------------

/**
 * Trạng thái của một gói đăng ký (subscription).
 * - ACTIVE: Đang hoạt động, còn hiệu lực.
 * - EXPIRED: Đã hết hạn.
 * - SUSPENDED: Bị tạm ngưng (do vi phạm hoặc admin khóa).
 * - QUEUED: Đã thanh toán nhưng đang chờ gói ACTIVE hết hạn để kích hoạt (xếp hàng).
 */
export type SubscriptionStatus = 'ACTIVE' | 'EXPIRED' | 'SUSPENDED' | 'QUEUED';

/**
 * Loại gói add-on mà doanh nghiệp có thể mua.
 * - JOB_POST: Mua thêm lượt đăng tin.
 * - HIGHLIGHT: Mua thêm lượt làm nổi bật tin.
 * - CV_VIEW: Mua thêm lượt xem CV.
 */
export type EmployerAddOnType = 'JOB_POST' | 'HIGHLIGHT' | 'CV_VIEW';

/**
 * Trạng thái của một gói add-on đã mua.
 */
export type AddOnStatus = 'ACTIVE' | 'EXPIRED';

// ----------------------------------------------------------------
// II. DTOs cho các Gói "Thô" (Sản phẩm do Admin quản lý)
// ----------------------------------------------------------------

/**
 * DTO cho Gói Dịch Vụ Doanh Nghiệp (Employer Service Package).
 * Đây là sản phẩm gốc mà Admin tạo ra.
 */
export interface EmployerPackageDTO {
  id: number;
  name: string;
  description: string;
  price: number; // Đại diện cho DECIMAL(10,2)
  durationDay: number | null;
  isLifetime: boolean;
  jobPostLimit: number;
  highlightJobLimit: number;
  createdAt: string; // ISO 8601 string
  updatedAt: string; // ISO 8601 string
}

/**
 * DTO cho Gói Dịch Vụ Ứng Viên (Candidate Service Package).
 * Đây là sản phẩm gốc mà Admin tạo ra.
 */
export interface CandidatePackageDTO {
  id: number;
  name: string;
  description: string;
  price: number; // Đại diện cho DECIMAL(10,2)
  durationDay: number | null;
  isLifetime: boolean;
  highlightProfileDays: number;
  jobApplyLimit: number;
  canViewOtherCandidates: boolean;
  createdAt: string; // ISO 8601 string
}

/**
 * DTO cho Gói Bổ Sung (Add-on) của Doanh Nghiệp.
 * Dùng để mua lẻ quyền lợi.
 */
export interface EmployerAddOnPackageDTO {
  id: number;
  name: string;
  description: string;
  price: number; // Đại diện cho DECIMAL(10,2)
  type: EmployerAddOnType;
  quantity: number;
  durationDay: number | null;
  isLifetime: boolean;
}

// ----------------------------------------------------------------
// III. DTOs cho các Gói Đã Mua (Subscription của User)
// ----------------------------------------------------------------

/**
 * DTO cho Gói Đăng Ký (Subscription) của Doanh Nghiệp.
 * Đây là bản ghi khi doanh nghiệp đã mua một EmployerPackage.
 */
export interface EmployerSubscriptionDTO {
  id: number;
  employerId: number;
  packageId: number;
  packageName: string; // Tên gói tại thời điểm mua
  startDate: string; // ISO 8601 Date (YYYY-MM-DD)
  endDate: string | null; // ISO 8601 Date (YYYY-MM-DD), null nếu vĩnh viễn
  status: SubscriptionStatus;
  isLifetime: boolean;
  usedJobCount: number;
  usedHighlightCount: number;
  totalJobCount: number; // Tổng quyền lợi tại thời điểm mua
  totalHighlightCount: number; // Tổng quyền lợi tại thời điểm mua
  renewCount: number;
}

/**
 * DTO cho Gói Đăng Ký (Subscription) của Ứng Viên.
 * Đây là bản ghi khi ứng viên đã mua một CandidatePackage.
 */
export interface CandidateSubscriptionDTO {
  id: number;
  candidateId: number;
  packageId: number;
  packageName: string; // Tên gói tại thời điểm mua
  subscriptionCode: string;
  startDate: string; // ISO 8601 Date (YYYY-MM-DD)
  endDate: string | null; // ISO 8601 Date (YYYY-MM-DD), null nếu vĩnh viễn
  status: SubscriptionStatus;
  isLifetime: boolean;
  usedApplyCount: number;
  usedHighlightDays: number;
  totalApplyLimit: number; // Tổng quyền lợi tại thời điểm mua
  totalHighlightDays: number; // Tổng quyền lợi tại thời điểm mua
  renewCount: number;
}

/**
 * DTO cho Gói Bổ Sung (Add-on) mà Doanh Nghiệp đã mua.
 * Ghi lại việc sử dụng một add-on cụ thể.
 */
export interface EmployerAddOnUsageDTO {
  id: number;
  employerId: number;
  addOnPackageId: number;
  addOnPackageName: string; // Tên add-on tại thời điểm mua
  usedCount: number;
  totalQuantity: number; // Tổng số lượng của add-on này
  startDate: string; // ISO 8601 Date (YYYY-MM-DD)
  endDate: string | null; // ISO 8601 Date (YYYY-MM-DD), null nếu vĩnh viễn
  status: AddOnStatus;
}

// ----------------------------------------------------------------
// IV. DTOs cho các Nghiệp Vụ Khác (Nâng cấp, Lỗi)
// ----------------------------------------------------------------

/**
 * DTO chứa thông tin báo giá khi nâng cấp gói.
 */
export interface UpgradeQuoteDTO {
  oldPackageId: number;
  newPackageId: number;
  oldPackagePrice: number;
  newPackagePrice: number;
  refundPercent: number; // Tỷ lệ khấu trừ, ví dụ: 0.54 (cho 54%)
  refundValue: number; // Số tiền được khấu trừ
  finalPrice: number; // Số tiền cuối cùng phải trả
}

/**
 * DTO chuẩn cho các phản hồi lỗi (Error Response).
 */
export interface ErrorResponse {
  timestamp: string; // ISO 8601 string
  status: number; // Mã trạng thái HTTP, ví dụ: 400, 404
  error: string; // Tên trạng thái HTTP, ví dụ: "Bad Request"
  message: string; // Thông báo lỗi chi tiết cho client
  path: string; // Endpoint đã gây ra lỗi
}

// ----------------------------------------------------------------
// V. DTOs cho Request Body (Payload)
// ----------------------------------------------------------------

/**
 * Dữ liệu cần thiết khi mua/gia hạn gói.
 */
export interface PurchaseSubscriptionRequest {
  packageId: number;
  paymentToken: string; // Hoặc một cấu trúc payment method ID
}

/**
 * Dữ liệu cần thiết khi mua gói add-on.
 */
export interface PurchaseAddOnRequest {
  addOnPackageId: number;
  paymentToken: string;
}

/**
 * Dữ liệu cần thiết để lấy báo giá nâng cấp.
 */
export interface UpgradeQuoteRequest {
  newPackageId: number;
}

/**
 * Dữ liệu cần thiết để xác nhận nâng cấp.
 */
export interface UpgradeSubscriptionRequest {
  newPackageId: number;
  paymentToken: string;
}
```