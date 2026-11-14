---
name: StuMart
description: Sàn thương mại điện tử sinh viên học đường Việt Nam - Trẻ trung, năng động, táo bạo, giàu tính kết nối và nói không với giao diện card-lồng-card nhàm chán
colors:
  primary: "#0B6E4F"
  primary-container: "#DCF8EC"
  accent-hero: "#FFD21E"
  accent-warm: "#FF5722"
  neutral-bg: "#FAF8F2"
  surface: "#FFFFFF"
  surface-warm: "#FFF6D6"
  text-primary: "#181C24"
  text-muted: "#6B7280"
typography:
  display:
    fontFamily: "Roboto, system-ui, sans-serif"
    fontSize: "2rem"
    fontWeight: 900
    lineHeight: 1.15
    letterSpacing: "-0.02em"
  headline:
    fontFamily: "Roboto, system-ui, sans-serif"
    fontSize: "1.375rem"
    fontWeight: 800
    lineHeight: 1.25
  title:
    fontFamily: "Roboto, system-ui, sans-serif"
    fontSize: "1.0625rem"
    fontWeight: 700
    lineHeight: 1.3
  body:
    fontFamily: "Roboto, system-ui, sans-serif"
    fontSize: "0.9375rem"
    fontWeight: 400
    lineHeight: 1.45
  label:
    fontFamily: "Roboto, system-ui, sans-serif"
    fontSize: "0.75rem"
    fontWeight: 700
    lineHeight: 1.2
rounded:
  sm: "6px"
  md: "12px"
  lg: "16px"
  xl: "24px"
  full: "9999px"
spacing:
  xs: "4px"
  sm: "8px"
  md: "16px"
  lg: "24px"
  xl: "32px"
components:
  button-primary:
    backgroundColor: "{colors.primary}"
    textColor: "{colors.surface}"
    rounded: "{rounded.lg}"
    padding: "16px 24px"
  button-hero-fab:
    backgroundColor: "{colors.accent-hero}"
    textColor: "{colors.text-primary}"
    rounded: "{rounded.full}"
    padding: "6px"
  tile-listing:
    backgroundColor: "{colors.surface}"
    rounded: "{rounded.md}"
    padding: "10px"
  input-field:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.text-primary}"
    rounded: "{rounded.md}"
    padding: "16px"
  nav-bottom:
    backgroundColor: "{colors.surface}"
    padding: "8px 16px"
---

# Design System: StuMart — "The Campus Voltage"

## 1. Triết lý Thiết kế & Định vị Sáng tạo

**Creative North Star: "The Campus Voltage — Nhịp đập Giảng đường"**

StuMart không phải là một sàn thương mại điện tử khô cứng, công nghiệp hay một ứng dụng tài chính xám xịt. Đây là **chợ phiên sinh viên rực rỡ, năng động và hừng hực sức trẻ của thế hệ Gen Z Việt Nam** (từ Bách Khoa, Kinh Tế, Quốc Gia đến Sư Phạm). 

Giao diện tôn vinh tinh thần sinh viên: **thông minh, tiết kiệm, hài hước, phóng khoáng và gắn kết**. Không gian thị giác được xây dựng trên sự đối lập đầy năng lượng giữa nền học viện vững chãi (Collegiate Emerald `#0B6E4F`), sắc vàng rực của chú Vịt Mascot (Voltage Duck Gold `#FFD21E`), và vệt lửa cam săn deal chớp nhoáng (Blaze Tangerine `#FF5722`).

### 4 Nguyên tắc Cốt lõi của Bản sắc Mới:

1. **Tuyệt đối cấm "Card-lồng-Card" (Strict Anti-Card-in-Card):**
   * *Card là vùng chứa lười biếng; card lồng trong card là biểu hiện rõ nhất của thiết kế AI rập khuôn.*
   * Thay vì đóng khung mọi đoạn văn hay thông số vào một chiếc hộp trắng bo tròn có bóng đổ, StuMart sử dụng **sự phân tầng không gian (Spatial Rhythm), dải màu nền tràn cạnh (Edge-to-Edge Color Bands), đường kẻ hairline tinh tế và khoảng thở hào phóng**.
2. **Điểm nhấn Màu sắc Táo bạo & Tương phản Sắc nét:**
   * Màu sắc không rải rác nhạt nhòa kiểu pastel an toàn. Màu xuất hiện theo từng mảng khối tự tin: xanh ngọc lục bảo học đường làm cột trụ, vàng chanh điện tử kích hoạt hành động anh hùng, cam lửa thiêu đốt sự chần chừ khi săn hàng giá rẻ.
3. **Typography Phân cấp Mạnh mẽ, Rõ rệt:**
   * Loại bỏ hoàn toàn kiểu chữ đều đều thiếu điểm nhấn. Sử dụng bước nhảy kích thước và trọng lượng kiểu chữ (Font Weight 800/900 cho tiêu đề, số tiền to bản) để ánh mắt người dùng quét qua màn hình nhận biết ngay thông tin quan trọng nhất trong 0.5 giây.
4. **Bản sắc Sinh viên Việt Nam chân thực:**
   * Gần gũi qua chú Vịt Mascot ("Vịt con StuMart" 🦆), địa điểm giao dịch thực tế (Căn tin H1, Cổng KTX, Thư viện Tạ Quang Bửu), ngôn ngữ trẻ trung, dí dỏm nhưng tuyệt đối minh bạch và đáng tin cậy.

---

## 2. Bảng Màu Táo bạo & Chiến lược Màu sắc

StuMart áp dụng chiến lược **Committed Palette (Bảng màu dấn thân)**: các màu chủ lực sở hữu những vùng diện tích thực thụ, không scatter vụn vặt.

### Primary (Cột trụ Học viện)
* **Collegiate Emerald** (`#0B6E4F`): Xanh ngọc lục bảo đậm, tượng trưng cho uy tín giảng đường, sự bền vững của đồ secondhand và an tâm giao dịch. Làm màu chủ đạo cho Top Bar, nút CTA chính, huy hiệu xác minh thẻ sinh viên.
* **Fresh Mint Tint** (`#DCF8EC`): Sắc xanh bạc hà tươi mới, dùng cho dải khối tin tưởng (Trust Strip), nhãn danh mục đã chọn và container nền thông tin quan trọng.

### Accent & Hero (Điểm nhấn Táo bạo)
* **Voltage Duck Gold** (`#FFD21E`): Sắc vàng năng lượng cao của chú Vịt StuMart. Độc quyền cho nút Floating Action Button trung tâm ("Đăng bán"), huy hiệu ưu đãi cực phẩm và các tương tác gamified (Túi mù, Đấu giá).
* **Blaze Tangerine** (`#FF5722`): Cam lửa rực cháy. Dành riêng cho thẻ giá tiền, nhãn "Giá sốc", đếm ngược Flash Sale và cảnh báo giao dịch. Màu này tạo sức ép hành động và sự phấn khích săn đồ rẻ.
* **Warm Duck Cream** (`#FFF6D6`): Nền placeholder ảnh sản phẩm và vùng ấm áp dành cho mascot.

### Neutrals (Mặt sàn & Chữ Tương phản Cao)
* **Warm Chalkboard Canvas** (`#FAF8F2`): Nền màn hình tổng thể. Khác với màu trắng toát bệnh viện (`#FFFFFF`) gây mỏi mắt khi lướt đêm trong KTX, sắc kem phấn ấm này mang lại cảm giác hữu cơ, dịu mắt dưới ánh nắng sân trường.
* **Pure Surface White** (`#FFFFFF`): Dành riêng cho các khối thẻ sản phẩm, bottom bar và modal sheet nổi lên trên nền Canvas.
* **Ink Charcoal** (`#181C24`): Chữ chính siêu nét, đậm đà, độ tương phản tuyệt đối ≥ 11:1 trên nền trắng, không dùng màu xám xịt khó đọc.
* **Slate Muted Gray** (`#6B7280`): Chữ phụ, thời gian đăng, địa điểm phụ và icon chưa kích hoạt (vẫn đảm bảo độ tương phản ≥ 4.6:1).

### Quy tắc Màu sắc Bất biến:
* **Luật 60-30-10:** 60% mặt sàn kem ấm/trắng, 30% cấu trúc xanh lục bảo vững chãi, 10% điểm nổ vàng chanh điện tử và cam cháy.
* **Cấm Gradient Chữ:** Nhấn mạnh bằng độ đậm (ExtraBold/Black) hoặc kích thước, không dùng gradient màu mè trên văn bản.

---

## 3. Hệ thống Typography Phân cấp Rõ rệt

Không dùng các kiểu font serif già cỗi hay monospace đóng kịch. StuMart sử dụng **Roboto / Material Sans System** nhưng tinh chỉnh nhịp độ và trọng lượng cực kỳ dứt khoát.

### Thang đo Phân cấp (Scale & Roles)

| Role | Size | Weight | Line Height | Tracking | Ứng dụng cụ thể |
|---|---|---|---|---|---|
| **Display Hero** | 30sp – 34sp | Black (900) | 1.15 | -0.02em | Mức giá sản phẩm chi tiết, tiêu đề Onboarding lớn, số đếm ưu đãi |
| **Headline Large** | 22sp – 24sp | ExtraBold (800) | 1.25 | -0.01em | Tiêu đề phân mục chính ("Deal hời KTX", "Sách Bách Khoa") |
| **Headline Medium**| 18sp – 20sp | Bold (700) | 1.30 | 0 | Tiêu đề sản phẩm trong trang chi tiết, tên người bán |
| **Title Medium** | 15sp – 16sp | Bold (700) | 1.35 | 0 | Tiêu đề sản phẩm trên lưới, chữ trên nút bấm hành động |
| **Body Large** | 14sp – 15sp | Regular (400) | 1.50 | 0 | Mô tả sản phẩm, điều khoản giao dịch, nội dung chat |
| **Label Punchy** | 11sp – 12sp | Heavy (800) | 1.20 | +0.02em | Tag tình trạng ("MỚI 99%"), nhãn loại bài ("GIÁ RẺ", "TÚI MÙ") |
| **Caption Mono** | 10sp – 11sp | Medium (500) | 1.20 | 0 | Thời gian đăng ("2 giờ trước"), mã số sinh viên, số lượt xem |

### Quy tắc Typography Bất biến:
* **Cấm Kicker / Eyebrow:** Không đặt nhãn nhỏ li ti nằm ngắc ngoải phía trên tiêu đề chính. Tiêu đề phải tự đứng vững và truyền tải thông điệp trọn vẹn.
* **Khoảng cách Tiêu đề (Heading Asymmetry):** Khoảng trống phía *trên* một tiêu đề luôn phải gấp ít nhất 1.8 lần khoảng trống phía *dưới* nó, giúp tiêu đề gắn chặt thị giác vào nội dung nó bảo hộ.

---

## 4. Kiến trúc Bố cục: Xóa sổ "Card-lồng-Card" (Anti-Card Architecture)

### Vấn đề của AI Slop:
Giao diện AI thường mắc bệnh bọc mọi thứ trong thẻ: Màn hình bọc trong Card → bên trong Card lại bọc Người bán trong Card nhỏ → bên trong Card Người bán lại bọc Avatar và Rating trong Card con. Kết quả là giao diện chật chội, viền chồng viền, bóng đổ chồng bóng đổ, biến app thành một mớ hộp carton hỗn độn.

### Giải pháp Kiến trúc của StuMart:

```
[ SAI - Card lồng Card AI Slop ]         [ ĐÚNG - StuMart Spatial Architecture ]
+--------------------------------+       +------------------------------------+
| Screen Card                    |       | Clean Canvas Background            |
|  +--------------------------+  |       |                                    |
|  | Sub-Card (Seller)        |  |  ==>  | Seller Flow Row (No Card Box)      |
|  |  +--------------------+  |  |       | [Avatar] Name (Bold)  * Verified   |
|  |  | Nested Card (Stat) |  |  |       | ---------------------------------- |
|  |  +--------------------+  |  |       | Edge-to-Edge Mint Trust Strip      |
|  +--------------------------+  |       | 🦆 Safe Meetup at Campus Gate      |
+--------------------------------+       +------------------------------------+
```

1. **Mặt sàn mở (Open-Floor Canvas):**
   * Thông tin người bán, thông số và mô tả sản phẩm được tổ chức thành các **Dòng chảy liền mạch (Flow Rows)** phân tách bằng khoảng trống hoặc đường kẻ mảnh 0.5dp mờ, không nhét vào các hộp con.
2. **Dải màu nhận diện tràn cạnh (Edge-to-Edge Bands):**
   * Khu vực "Cam kết an toàn khuôn viên trường" sử dụng một dải màu xanh bạc hà (`#DCF8EC`) tràn viền với icon lá chắn và vịt mascot, tạo cảm giác như một chiếc băng rôn thể thao học đường uy tín.
3. **Lưới sản phẩm bất đối xứng (Dynamic Asymmetric Grid):**
   * Thẻ bài đăng ngoài trang chủ (`GridPostCard`) là đơn vị chứa DUY NHẤT. Hình ảnh sản phẩm tràn mép trên, thông tin nằm ngay bên dưới mà không có thêm bất kỳ khung viền lồng ghép nào bên trong.
4. **Phân tách bằng Nhịp độ (Pacing by Whitespace):**
   * Cụm giá và tiêu đề đi sát nhau (4dp–6dp).
   * Giữa các phân đoạn nội dung khác nhau là khoảng giãn cách thoáng đãng (16dp–24dp).

---

## 5. Ngôn ngữ Hình khối & Chi tiết (Shapes & Tokens)

* **Hình khối thân thiện nhưng chắc chắn:**
  * **Corner Radius 12dp:** Áp dụng cho các ô ảnh sản phẩm và trường nhập liệu. Đủ mềm mại nhưng không bị biến dạng thành hình tròn quá trớn.
  * **Corner Radius 16dp:** Nút bấm CTA chính, dialog và dải tính năng.
  * **Pill (Full 9999px):** Thanh tìm kiếm (Search Bar), chip danh mục và các nhãn tình trạng ("Trao đổi", "Cho tặng").
* **Đổ bóng & Chiều sâu Tối giản (Tonal Elevation):**
  * Không dùng bóng đen đặc cộp (black block shadow) hay hiệu ứng kính mờ (glassmorphism) rởm.
  * **Thẻ nghỉ (Resting Tile):** `elevation = 1.dp` hoặc viền siêu mảnh `0.5dp` màu `#E5E7EB`.
  * **Thanh Bottom Bar:** Đổ bóng ngược lên trên mềm mại `elevation = 8.dp`.
  * **Hero Action FAB:** Bóng vàng ấm tự nhiên `rgba(255, 210, 30, 0.40)` với bán kính tỏa 12dp.

---

## 6. Thành phần UI Trọng tâm (Core Components)

### 1. Nút Bấm "Đăng Bán" Anh Hùng (Hero Duck FAB)
* Nằm kiêu hãnh ở vị trí trung tâm thanh điều hướng dưới cùng.
* Kích thước 58dp × 58dp, phủ sắc vàng Voltage Gold (`#FFD21E`), bên trong là biểu tượng chú Vịt StuMart 🦆 tinh nghịch hoặc icon máy ảnh tạo bài.
* Nhấp nhô nhẹ nhàng khi có thông báo deal mới.

### 2. Thẻ Đăng Bán Sinh Viên (Listing Tile)
* Không viền dày, không card lồng card.
* Ảnh thực tế chiếm tỷ lệ lớn (tối thiểu 55% chiều cao thẻ).
* Tag loại bài đăng gắn trực tiếp lên góc ảnh (Top-Start) bằng góc bo đồng điệu.
* Giá tiền in đậm bằng màu Cam Lửa (`#FF5722`), cỡ chữ 16sp ExtraBold nổi bật ngay góc nhìn đầu tiên.

### 3. Dải Tin cậy Giảng đường (Campus Trust Strip)
* Dải khối màu bạc hà mát dịu đặt ngay dưới thông tin sản phẩm.
* Chứa địa điểm hẹn gặp đề xuất: *"Sảnh thư viện Bách Khoa / KTX H1"*.
* Huy hiệu *"Vịt con bảo đảm"*: Không chuyển cọc, gặp trực tiếp kiểm tra hàng sinh viên.

### 4. Bảng Đề xuất Giá Nhanh ("Trả Giá" Bottom Sheet)
* Thay vì nhập liệu phức tạp, cung cấp các chip giảm nhanh: `-5k`, `-10k`, `-15%` giúp sinh viên mặc cả vui vẻ, đúng văn hóa sinh viên mà không gây ngượng ngùng.

---

## 7. Bảng Phân định DO's và DON'Ts

### DO (Nên làm):
* **LÀM:** Sử dụng các cặp màu tương phản cao (Xanh ngọc lục bảo kết hợp Vàng chanh rực và Cam cháy) để tạo sức sống trẻ trung, năng động.
* **LÀM:** Giữ cấu trúc màn hình phẳng, thông thoáng; dùng khoảng cách (whitespace) và dải màu thay cho các hộp lồng nhau.
* **LÀM:** Viết micro-copy hóm hỉnh, mang đậm văn hóa ký túc xá, giảng đường Việt Nam ("Deal hời KTX", "Trao đổi giáo trình", "Vịt con đảm bảo").
* **LÀM:** Đảm bảo mọi điểm chạm ngón tay tối thiểu 48dp × 48dp cho thao tác một tay trên điện thoại khi đi bộ quanh trường.

### DON'T (Tuyệt đối tránh):
* **CẤM:** Tuyệt đối không lồng card trong card (Card-in-card AI slop).
* **CẤM:** Không dùng chữ chuyển màu (gradient text) – vừa khó đọc vừa lỗi thời.
* **CẤM:** Không dùng các màu xám nhờ nhờ, pastel nhợt nhạt thiếu sức sống của các template template AI mặc định.
* **CẤM:** Không dùng icon emoji tuỳ tiện thay thế cho hệ thống icon vector chuẩn của Material.
* **CẤM:** Không dùng bóng cứng 90° kiểu neobrutalism vụng về trừ khi cả hệ thống đi theo hướng đó.
