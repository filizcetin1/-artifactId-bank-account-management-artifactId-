# Banka Hesap Yönetim Sistemi

Bu proje, temel banka hesap işlemlerini yönetmek için geliştirilmiş bir Spring Boot uygulamasıdır. Hesap oluşturma, para yatırma/çekme ve hesap hareketlerini takip etme gibi temel bankacılık işlemlerini destekler.

## Özellikler

- Hesap yönetimi (oluşturma, güncelleme, silme, listeleme)
- Para yatırma ve çekme işlemleri
- Hesap hareketlerinin kaydı ve takibi
- Detaylı loglama sistemi
- RESTful API arayüzü
- Swagger UI ile API dokümantasyonu

## Teknolojiler

- Java 17
- Spring Boot 3.2.3
- PostgreSQL
- Liquibase (Veritabanı versiyon yönetimi)
- Swagger/OpenAPI
- Lombok
- JUnit 5

### Ön Koşullar

- JDK 17
- PostgreSQL
- Maven

### Veritabanı Kurulumu

PostgreSQL'de aşağıdaki komutları çalıştırın:
-- Ana veritabanı
DROP DATABASE IF EXISTS bankdb;
CREATE DATABASE bankdb;
-- Test veritabanı
DROP DATABASE IF EXISTS bankdb_test;
CREATE DATABASE bankdb_test;

### Projeyi Çalıştırma

1. Projeyi klonlayın
2. Veritabanı bağlantı bilgilerini `application.properties` dosyasında güncelleyin
3. Projeyi derleyin ve çalıştırın:
mvn clean install
mvn spring-boot:run

### Hesap İşlemleri

- `POST /api/hesaplar` - Yeni hesap oluşturma
- `GET /api/hesaplar` - Tüm hesapları listeleme
- `GET /api/hesaplar/{id}` - Hesap detaylarını getirme
- `PUT /api/hesaplar/{id}` - Hesap bilgilerini güncelleme
- `DELETE /api/hesaplar/{id}` - Hesap silme

### Para İşlemleri

- `POST /api/hesaplar/{id}/para-yatir` - Para yatırma
- `POST /api/hesaplar/{id}/para-cek` - Para çekme

## API Dokümantasyonu

Swagger UI'a erişmek için: `http://localhost:8080/swagger-ui.html`

## Loglama

Uygulama logları `logs/application.log` dosyasında tutulur. Log seviyeleri:

- INFO: Genel işlem bilgileri
- DEBUG: Detaylı işlem bilgileri
- ERROR: Hata durumları
  ## Test

Testleri çalıştırmak için:
mvn test
## Veritabanı Şeması

### Hesap Tablosu
- id (UUID)
- hesap_sahip_kimlik_no (VARCHAR)
- hesap_sahip_ad (VARCHAR)
- hesap_sahip_soyad (VARCHAR)
- hesap_turu (ENUM: VADELI/VADESIZ)
- bakiye (NUMERIC)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)

### Hesap Hareketi Tablosu
- id (BIGINT)
- hesap_id (UUID, FK)
- hareket_turu (ENUM: PARA_YATIRMA/PARA_CEKME)
- miktar (NUMERIC)
- islem_tarihi (TIMESTAMP)
- aciklama (VARCHAR)
- bakiye (NUMERIC)
  
