# Backend API - Spring Boot (Java 19 + PostgreSQL)

Project ini merupakan implementasi backend dengan menggunakan **Spring Boot** versi Java 19 yang terdiri dari 3 modul utama API:

1. **Item API**
2. **Inventory API**
3. **Order API**

Semua data disimpan dalam **PostgreSQL Database** dan dapat diuji melalui dokumentasi Postman yang telah disediakan.

---

## 🔧 Teknologi yang Digunakan

- **Java 19**
- **Spring Boot**
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok**
- **Postman (untuk testing dan dokumentasi API)**

---

## 🧩 Modul API

### 1. 📦 Item API
Menyediakan endpoint untuk mengelola item/barang yang tersedia.

Contoh endpoint:
- `GET /item`
- `POST /item/save`
- `PUT /item/update`
- `GET /item/{id}`
- `DELETE /item/{id}`

### 2. 🏪 Inventory API
Menyediakan endpoint untuk pengelolaan stok barang yang tersimpan.

Contoh endpoint:
- `GET /inventory`
- `POST /inventory/save`
- `PUT /inventory/update`
- `GET /inventory/{id}`
- `DELETE /inventory/{id}`

### 3. 🧾 Order API
Menyediakan endpoint untuk membuat pesanan (order) berdasarkan ketersediaan item dan inventory.

Contoh endpoint:
- `GET /order`
- `POST /order/save`
- `PUT /order/update`
- `GET /order/{id}`
- `DELETE /order/{id}`

---

## 🗂 Dokumentasi API (Postman)

Dokumentasi API tersedia dalam bentuk file Postman Collection yang bisa di-import ke aplikasi Postman.

🔗 [Download Postman Collection](https://drive.google.com/file/d/179UbRYYNvBPD9oTymDCKFw3tHEQ8IYWw/view?usp=sharing)

---

## 📦 Setup dan Menjalankan Aplikasi

### Prasyarat
- Java 19
- PostgreSQL (dengan konfigurasi database yang sesuai di `application.properties` atau `application.yml`)
- Maven atau Gradle

### Langkah Menjalankan

```bash
# Clone repository
git clone https://github.com/rizkyyananda/test-obs.git
cd <project-folder>

# Jalankan aplikasi (menggunakan Maven)
./mvnw spring-boot:run
