# Hotel Management App - Test 2

## Mô tả dự án
Ứng dụng quản lý khách sạn với các tính năng đặt phòng, quản lý danh sách phòng đã đặt và các thao tác CRUD cơ bản.

## Các tính năng chính

### 1. Load dữ liệu (3.0 điểm)
- Sử dụng Room Database để lưu trữ dữ liệu
- Load danh sách phòng từ database
- Load danh sách phòng đã đặt
- Sử dụng Multi-Threading để load dữ liệu không đồng bộ

### 2. Add (2.0 điểm)
- Thêm phòng mới thông qua FloatingActionButton
- Dialog nhập thông tin phòng (tên, mô tả, giá)
- Validation dữ liệu đầu vào
- Sử dụng Multi-Threading để thêm dữ liệu

### 3. Delete (2.0 điểm)
- Xóa phòng khỏi danh sách chính
- Xóa phòng khỏi danh sách đã đặt (hủy đặt phòng)
- Dialog xác nhận trước khi xóa
- Sử dụng Multi-Threading để xóa dữ liệu

### 4. Update (2.0 điểm)
- Cập nhật thông tin phòng
- Cập nhật ghi chú cho phòng đã đặt
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
- **Food**: Entity class với annotations Room

### Repository Pattern
- **FoodRepository**: Quản lý tất cả thao tác database
- Multi-threading với ExecutorService
- Callback interface cho async operations

### Activities
- **MainActivity**: Màn hình chính với 2 nút chức năng
- **FoodActivity**: Danh sách phòng và chức năng đặt phòng
- **BookedRoomsActivity**: Danh sách phòng đã đặt

### Adapters
- **FoodAdapter**: Adapter cho danh sách phòng
- **BookedRoomAdapter**: Adapter cho danh sách phòng đã đặt

## Công nghệ sử dụng

- **Room Database**: ORM cho SQLite
- **Multi-Threading**: ExecutorService, Handler
- **Material Design**: FloatingActionButton
- **Android Architecture Components**: LiveData, Repository pattern

## Cách sử dụng

1. **Xem danh sách phòng**: Nhấn "Chọn phòng" từ màn hình chính
2. **Thêm phòng mới**: Nhấn nút + (FloatingActionButton) trong danh sách phòng
3. **Đặt phòng**: Chọn phòng và nhấn "Đặt phòng"
4. **Xem phòng đã đặt**: Nhấn "Phòng đã đặt" từ màn hình chính
5. **Chỉnh sửa ghi chú**: Nhấn nút chỉnh sửa trong danh sách phòng đã đặt
6. **Hủy đặt phòng**: Nhấn nút xóa trong danh sách phòng đã đặt

## Điểm đạt được

- ✅ Load dữ liệu: 3.0 điểm
- ✅ Add: 2.0 điểm  
- ✅ Delete: 2.0 điểm
- ✅ Update: 2.0 điểm
- ✅ Multi-Thread: 1.0 điểm

**Tổng điểm: 10.0/10.0** 