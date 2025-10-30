# API Documentation - Upload Module

## Candidate Uploads (`/api/v1/candidate-upload`)

### Lấy danh sách CV của ứng viên

GET
/{candidateId}/resume

#### Parameters

| Type | Name | Data Type | Description |
| --- | --- | --- | --- |
| Path Variable | `candidateId` | `Long` | ID của ứng viên |

#### Success Response (200 OK)

```
{
    "statusCode": 200,
    "message": "Success",
    "data": [
        {
            "id": 1,
            "candidateId": 123,
            "fileName": "my_cv.pdf",
            "status": true,
            "url": "https://storage.googleapis.com/..."
        }
    ]
}
```

### Tải lên CV mới

POST
/{candidateId}/resume

Upload CV mới. Content-Type phải là `multipart/form-data`.

#### Parameters

| Type | Name | Data Type | Description |
| --- | --- | --- | --- |
| Path Variable | `candidateId` | `Long` | ID của ứng viên |

#### Form Data

| Name | Data Type | Description | Required |
| --- | --- | --- | --- |
| `file` | `File` | File CV (PDF, DOCX, v.v.) | **Yes** |
| `status` | `boolean` | Trạng thái (CV chính) (Default: `true`) | No |

#### Success Response (200 OK)

```
{
    "statusCode": 200,
    "message": "Success",
    "data": {
        "id": 2,
        "candidateId": 123,
        "fileName": "new_cv.pdf",
        "status": true,
        "url": "https://storage.googleapis.com/..."
    }
}
```

### Lấy URL của avatar

GET
/{candidateId}/avatar

#### Parameters

| Type | Name | Data Type | Description |
| --- | --- | --- | --- |
| Path Variable | `candidateId` | `Long` | ID của ứng viên |

#### Success Response (200 OK)

```
{
    "statusCode": 200,
    "message": "Success",
    "data": {
        "url": "https://storage.googleapis.com/..."
    }
}
```

### Upload avatar mới

POST
/{candidateId}/avatar

Upload avatar mới (hoặc thay thế avatar cũ). Content-Type phải là `multipart/form-data`.

**Lưu ý bảo mật:** Cần kiểm tra xem người dùng đã xác thực có quyền sửa đổi `candidateId` này hay không.

#### Parameters

| Type | Name | Data Type | Description |
| --- | --- | --- | --- |
| Path Variable | `candidateId` | `Long` | ID của ứng viên |

#### Form Data

| Name | Data Type | Description | Required |
| --- | --- | --- | --- |
| `file` | `File` | File ảnh (JPG, PNG, v.v.) | **Yes** |

#### Success Response (200 OK)

```
{
    "statusCode": 200,
    "message": "Success",
    "data": {
        "url": "https://storage.googleapis.com/..."
    }
}
```

### Xóa avatar

DELETE
/{candidateId}/avatar

**Lưu ý bảo mật:** Cần kiểm tra xem người dùng đã xác thực có quyền sửa đổi `candidateId` này hay không.

#### Parameters

| Type | Name | Data Type | Description |
| --- | --- | --- | --- |
| Path Variable | `candidateId` | `Long` | ID của ứng viên |

#### Success Response (200 OK)

```
{
    "statusCode": 200,
    "message": "Delete successfully"
}
```

### Lấy danh sách ảnh gallery

GET
/{candidateId}/gallery

#### Parameters

| Type | Name | Data Type | Description |
| --- | --- | --- | --- |
| Path Variable | `candidateId` | `Long` | ID của ứng viên |

#### Success Response (200 OK)

```
{
    "statusCode": 200,
    "data": [
        {
            "id": 10,
            "url": "https://storage.googleapis.com/..."
        },
        {
            "id": 11,
            "url": "https://storage.googleapis.com/..."
        }
    ]
}
```

### Upload ảnh mới vào gallery

POST
/{candidateId}/gallery

Content-Type phải là `multipart/form-data`.

**Lưu ý bảo mật:** Cần kiểm tra xem người dùng đã xác thực có quyền sửa đổi `candidateId` này hay không.

#### Parameters

| Type | Name | Data Type | Description |
| --- | --- | --- | --- |
| Path Variable | `candidateId` | `Long` | ID của ứng viên |

#### Form Data

| Name | Data Type | Description | Required |
| --- | --- | --- | --- |
| `file` | `File` | File ảnh (JPG, PNG, v.v.) | **Yes** |

#### Success Response (200 OK)

```
{
    "statusCode": 200,
    "data": {
        "id": 12,
        "url": "https://storage.googleapis.com/..."
    }
}
```

### Xóa một ảnh khỏi gallery

DELETE
/gallery/{imageId}

**Lưu ý bảo mật:** Cần kiểm tra xem người dùng đã xác thực có quyền xóa `imageId` này hay không (ví dụ: kiểm tra xem ảnh này có thuộc về ứng viên đang đăng nhập không).

#### Parameters

| Type | Name | Data Type | Description |
| --- | --- | --- | --- |
| Path Variable | `imageId` | `Long` | ID của ảnh cần xóa (lấy từ `UploadDataResponse`) |

#### Success Response (200 OK)

```
{
    "statusCode": 200,
    "message": "Delete successfully"
}
```

## Company Uploads (`/api/v1/company-upload`)

### Lấy logo công ty

GET
/{companyId}/logo

#### Parameters

| Type | Name | Data Type | Description |
| --- | --- | --- | --- |
| Path Variable | `companyId` | `Long` | ID của công ty |

#### Success Response (200 OK)

```
{
    "statusCode": 200,
    "message": "Lấy logo công ty thành công!",
    "data": {
        "url": "https://storage.googleapis.com/..."
    }
}
```

### Lấy ảnh gallery của công ty

GET
/{companyId}/gallery

#### Parameters

| Type | Name | Data Type | Description |
| --- | --- | --- | --- |
| Path Variable | `companyId` | `Long` | ID của công ty |

#### Success Response (200 OK)

```
{
    "statusCode": 200,
    "message": "Lấy ảnh công ty thành công!",
    "data": [
        {
            "id": 20,
            "url": "https://storage.googleapis.com/..."
        }
    ]
}
```

### Upload ảnh gallery công ty

POST
/{companyId}/gallery

Content-Type phải là `multipart/form-data`.

**Lưu ý bảo mật:** Cần kiểm tra xem người dùng đã xác thực (ví dụ: nhà tuyển dụng) có quyền sửa đổi `companyId` này hay không.

#### Parameters

| Type | Name | Data Type | Description |
| --- | --- | --- | --- |
| Path Variable | `companyId` | `Long` | ID của công ty |

#### Form Data

| Name | Data Type | Description | Required |
| --- | --- | --- | --- |
| `file` | `File` | File ảnh (JPG, PNG, v.v.) | **Yes** |

#### Success Response (200 OK)

```
{
    "statusCode": 200,
    "message": "Upload ảnh công ty thành công!",
    "data": {
        "id": 21,
        "url": "https://storage.googleapis.com/..."
    }
}
```

### Upload logo công ty

POST
/{companyId}/logo

Content-Type phải là `multipart/form-data`.

**Lưu ý bảo mật:** Cần kiểm tra xem người dùng đã xác thực (ví dụ: nhà tuyển dụng) có quyền sửa đổi `companyId` này hay không.

#### Parameters

| Type | Name | Data Type | Description |
| --- | --- | --- | --- |
| Path Variable | `companyId` | `Long` | ID của công ty |

#### Form Data

| Name | Data Type | Description | Required |
| --- | --- | --- | --- |
| `file` | `File` | File ảnh logo (JPG, PNG, SVG v.v.) | **Yes** |

#### Success Response (200 OK)

```
{
    "statusCode": 200,
    "message": "Upload logo công ty thành công!",
    "data": {
        "url": "https://storage.googleapis.com/..."
    }
}
```

### Xóa ảnh gallery công ty

DELETE
/gallery/{imageId}

**Lưu ý bảo mật:** Cần kiểm tra xem người dùng đã xác thực có quyền xóa `imageId` này hay không.

#### Parameters

| Type | Name | Data Type | Description |
| --- | --- | --- | --- |
| Path Variable | `imageId` | `Long` | ID của ảnh cần xóa |

#### Success Response (200 OK)

```
{
    "statusCode": 200,
    "message": "Xóa ảnh công ty thành công!"
}
```

### Xóa logo công ty

DELETE
/{companyId}/logo

**Lưu ý bảo mật:** Cần kiểm tra xem người dùng đã xác thực có quyền sửa đổi `companyId` này hay không.

#### Parameters

| Type | Name | Data Type | Description |
| --- | --- | --- | --- |
| Path Variable | `companyId` | `Long` | ID của công ty |

#### Success Response (200 OK)

```
{
    "statusCode": 200,
    "message": "Xóa logo công ty thành công!"
}
```