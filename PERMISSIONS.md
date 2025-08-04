# Phân quyền trong Hệ thống Quản lý Khách sạn

## Tổng quan
Hệ thống có 2 role chính: **STAFF** và **MANAGER**

## Chi tiết phân quyền

### 1. STAFF (Nhân viên lễ tân)

#### ✅ Được phép:
- **Xem danh sách phòng** - `GET /room`
- **Tìm kiếm phòng** - `GET /room?number=&roomType=&roomStatus=`
- **Tạo đặt phòng** - `POST /reservation/create`
- **Xem/Sửa đặt phòng** (chỉ đặt phòng mình tạo)
- **Check-in/Check-out** - `PUT /reservation/checkin/{id}`, `PUT /reservation/checkout/{id}`
- **Cập nhật trạng thái phòng** - `PUT /room/updateStatus/{id}`
- **Quản lý khách hàng** - `GET/POST/PUT /customer`
- **Xem danh sách dịch vụ** - `GET /service`
- **Gán dịch vụ cho đặt phòng** - `POST /used-service`

#### ❌ Không được phép:
- Thêm/Sửa/Xóa phòng
- Thay đổi giá phòng
- Thêm/Sửa/Xóa dịch vụ
- Xem báo cáo doanh thu
- Quản lý nhân sự

### 2. MANAGER (Quản lý)

#### ✅ Được phép:
- **Tất cả quyền của STAFF**
- **Thêm phòng mới** - `POST /room/create`
- **Sửa thông tin phòng** - `PUT /room/update/{id}`
- **Xóa phòng** - `DELETE /room/delete/{id}`
- **Thêm dịch vụ** - `POST /service/create`
- **Xóa dịch vụ** - `DELETE /service/delete/{id}`
- **Xem tất cả báo cáo** - `GET /report/*`
- **Quản lý nhân sự** - `GET/POST/PUT /staff`

## Cách sử dụng trong code (Spring Security Method Security)

### 1. Annotation cơ bản:
```java
@PreAuthorize("hasRole('MANAGER')")
public ResponseEntity<?> createSomething() {
    // Chỉ MANAGER mới được truy cập
}
```

### 2. Kiểm tra nhiều role:
```java
@PreAuthorize("hasAnyRole('STAFF', 'MANAGER')")
public ResponseEntity<?> updateSomething() {
    // STAFF hoặc MANAGER đều được truy cập
}
```

### 3. Expression phức tạp:
```java
@PreAuthorize("hasRole('MANAGER') or (hasRole('STAFF') and #id == authentication.principal.id)")
public ResponseEntity<?> updateOwnData(@PathVariable Long id) {
    // MANAGER hoặc STAFF chỉ được sửa dữ liệu của mình
}
```

## API Endpoints theo quyền

### STAFF có thể truy cập:
- `GET /room` - Xem danh sách phòng
- `PUT /room/updateStatus/{id}` - Cập nhật trạng thái phòng
- `GET /customer` - Xem danh sách khách hàng
- `POST /customer` - Thêm khách hàng
- `PUT /customer/{id}` - Sửa khách hàng
- `GET /reservation` - Xem đặt phòng
- `POST /reservation/create` - Tạo đặt phòng
- `PUT /reservation/checkin/{id}` - Check-in
- `PUT /reservation/checkout/{id}` - Check-out
- `GET /service` - Xem dịch vụ
- `POST /used-service` - Sử dụng dịch vụ

### MANAGER có thể truy cập:
- **Tất cả endpoints của STAFF**
- `POST /room/create` - Tạo phòng
- `PUT /room/update/{id}` - Sửa phòng
- `DELETE /room/delete/{id}` - Xóa phòng
- `POST /service/create` - Tạo dịch vụ
- `DELETE /service/delete/{id}` - Xóa dịch vụ
- `GET /report/*` - Xem báo cáo
- `GET /staff` - Xem nhân sự
- `POST /staff` - Thêm nhân sự
- `PUT /staff/{id}` - Sửa nhân sự

## Các annotation Spring Security phổ biến

- `@PreAuthorize("hasRole('MANAGER')")` - Chỉ MANAGER
- `@PreAuthorize("hasRole('STAFF')")` - Chỉ STAFF  
- `@PreAuthorize("hasAnyRole('STAFF', 'MANAGER')")` - STAFF hoặc MANAGER
- `@PreAuthorize("isAuthenticated()")` - Bất kỳ user đã đăng nhập
- `@PreAuthorize("permitAll()")` - Cho phép tất cả (không cần đăng nhập) 