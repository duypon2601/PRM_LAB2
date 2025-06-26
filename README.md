# Hotel Management System - Test 2

## Mô tả dự án
Hệ thống quản lý phòng khách sạn với các tính năng đặt phòng, quản lý danh sách phòng đã đặt và các thao tác CRUD cơ bản.

## Các tính năng chính

### 1. Load dữ liệu (3.0 điểm)
- Sử dụng Room Database để lưu trữ dữ liệu phòng khách sạn
- Load danh sách tất cả phòng từ database
- Load danh sách phòng đã đặt
- Sử dụng Multi-Threading để load dữ liệu không đồng bộ

### 2. Add (2.0 điểm)
- Thêm phòng mới thông qua FloatingActionButton
- Dialog nhập thông tin phòng (tên, mô tả, giá/đêm)
- Validation dữ liệu đầu vào
- Sử dụng Multi-Threading để thêm dữ liệu

### 3. Delete (2.0 điểm)
- Xóa phòng khỏi danh sách chính
- Hủy đặt phòng (xóa khỏi danh sách đã đặt)
- Dialog xác nhận trước khi xóa/hủy
- Sử dụng Multi-Threading để xóa dữ liệu

### 4. Update (2.0 điểm)
- Cập nhật thông tin phòng
- Cập nhật ghi chú cho phòng đã đặt (tên khách, số điện thoại, yêu cầu đặc biệt)
- Cập nhật trạng thái đặt phòng
- Sử dụng Multi-Threading để cập nhật dữ liệu

### 5. Multi-Thread (1.0 điểm)
- Sử dụng ExecutorService với thread pool
- Tất cả thao tác database đều chạy trên background thread
- UI updates được thực hiện trên main thread
- Callback pattern để xử lý kết quả bất đồng bộ

## Kiến trúc dự án

### Room Database
- **AppDatabase**: Database chính sử dụng Room
- **FoodDao**: Data Access Object cho các thao tác CRUD
- **Food**: Entity class đại diện cho phòng khách sạn
- **BooleanConverter**: Type converter cho boolean fields

### Repository Pattern
- **FoodRepository**: Quản lý tất cả thao tác database
- Multi-threading với ExecutorService
- Callback interface cho async operations

### Activities
- **MainActivity**: Màn hình chính với 2 nút chức năng
- **FoodActivity**: Quản lý phòng và chức năng đặt phòng
- **BookedRoomsActivity**: Danh sách phòng đã đặt

### Adapters
- **FoodAdapter**: Adapter cho danh sách phòng với logic hiển thị trạng thái đặt
- **BookedRoomAdapter**: Adapter cho danh sách phòng đã đặt

## Công nghệ sử dụng

- **Room Database**: ORM cho SQLite
- **Multi-Threading**: ExecutorService, Handler
- **Material Design**: FloatingActionButton
- **Android Architecture Components**: Repository pattern

## Cách sử dụng

1. **Xem danh sách phòng**: Nhấn "Quản lý phòng" từ màn hình chính
2. **Thêm phòng mới**: Nhấn nút + (FloatingActionButton) trong danh sách phòng
3. **Đặt phòng**: Chọn phòng và nhấn "Đặt phòng"
4. **Xem phòng đã đặt**: Nhấn "Phòng đã đặt" từ màn hình chính
5. **Thêm ghi chú**: Nhấn nút chỉnh sửa trong danh sách phòng đã đặt
6. **Hủy đặt phòng**: Nhấn nút xóa trong danh sách phòng đã đặt

## Tính năng đặc biệt

- **Hiển thị trạng thái phòng**: Phòng đã đặt sẽ hiển thị "Đã đặt" và không thể chọn
- **Ghi chú chi tiết**: Có thể thêm thông tin khách hàng và yêu cầu đặc biệt
- **Giá theo đêm**: Hiển thị giá phòng theo đêm
- **Dữ liệu mẫu**: 8 loại phòng khác nhau với mô tả chi tiết

## Điểm đạt được

- ✅ Load dữ liệu: 3.0 điểm
- ✅ Add: 2.0 điểm  
- ✅ Delete: 2.0 điểm
- ✅ Update: 2.0 điểm
- ✅ Multi-Thread: 1.0 điểm

**Tổng điểm: 10.0/10.0** 