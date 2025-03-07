# Banka Uygulaması

Bu proje, bir banka yönetim sistemini simüle eden Java tabanlı bir konsol uygulamasıdır. Kullanıcılar (müşteriler) sisteme giriş yaparak hesaplarını yönetebilir, para transferleri gerçekleştirebilir, kredi başvurusu yapabilir ve kredi ödemelerini gerçekleştirebilirler.

## Özellikler
- **Kullanıcı Yönetimi**: Müşteri girişi, yeni hesap oluşturma, müşteri bilgileri güncelleme.
- **Hesap Yönetimi**: Vadesiz ve vadeli hesap açma, hesapları listeleme.
- **Para Transferleri**: Hesaplar arası para transferi, başka bir hesaba havale işlemi.
- **Kredi İşlemleri**: Kredi başvurusu, kredi görüntüleme, kredi ödemeleri.
- **Hesap Hareketleri**: Hesap işlemlerini görüntüleme ve kayıt altına alma.

## Kurulum ve Çalıştırma

### Gereksinimler
- Java 8 veya daha üstü
- Bir IDE (IntelliJ IDEA, Eclipse veya NetBeans önerilir)

### Derleme ve Çalıştırma

1. **Kaynak kodunu klonlayın:**
   ```sh
   git clone https://github.com/GamzeYarimkulak/BankaUygulamasi.git
   cd BankaUygulamasi
   ```
2. **Projeyi derleyin:**
   ```sh
   javac BankaUygulamasi.java
   ```
3. **Uygulamayı çalıştırın:**
   ```sh
   java BankaUygulamasi
   ```

## Kullanım

### Giriş Menüsü
- **1. Giriş Yap:** Kayıtlı bir müşteri numarası ve şifre ile giriş yapabilirsiniz.
- **2. Yeni Hesap Oluştur:** Yeni müşteri kaydı yaparak hesap açabilirsiniz.
- **3. Çıkış:** Uygulamadan çıkış yapabilirsiniz.

### Ana Menü
- **1. Hesaplarımı Görüntüle:** Açık olan hesaplarınızı ve bakiyenizi görüntüleyebilirsiniz.
- **2. Para Transferi:** Hesaplar arası veya farklı müşterilere para gönderebilirsiniz.
- **3. Yeni Hesap Aç:** Vadesiz veya vadeli hesap açabilirsiniz.
- **4. Kredi İşlemleri:** Kredi başvurusu yapabilir, mevcut kredilerinizi görüntüleyebilir veya kredi ödemesi yapabilirsiniz.
- **5. Hesap Hareketleri:** Hesap hareketlerini görüntüleyebilirsiniz.
- **6. Profil Bilgilerim:** Müşteri bilgilerinizi görüntüleyebilir ve güncelleyebilirsiniz.
- **7. Oturumu Kapat:** Mevcut kullanıcı oturumunu kapatabilirsiniz.
- **8. Çıkış:** Uygulamayı sonlandırabilirsiniz.

## Sınıf Yapısı
- `BankaUygulamasi`: Ana sınıf, uygulamanın başlangıç noktasını içerir.
- `BankaYonetimSistemi`: Kullanıcı etkileşimlerini ve iş mantığını yönetir.
- `Banka`: Bankaya ait müşteri ve hesap verilerini yönetir.
- `Musteri`: Müşteri bilgilerini saklar ve hesaplarını yönetir.
- `Hesap` (Soyut Sınıf): Ortak hesap özelliklerini içerir.
  - `VadesizHesap`: Standart hesap türü.
  - `VadeliHesap`: Belirli bir vade süresi ve faiz oranına sahip hesap türü.
- `KrediHesabi`: Kredileri yöneten sınıf.
- `HesapHareketi`: Hesap hareketlerini kaydeden sınıf.

## Örnek Kullanım

### Yeni Müşteri Kaydı
```
=== YENİ MÜŞTERİ KAYDI ===
Ad: Ahmet
Soyad: Yılmaz
TC Kimlik No: 12345678901
Telefon: 5551234567
Email: ahmet@example.com
Adres: İstanbul, Kadıköy
Şifre: 123456
Müşteri kaydınız oluşturuldu!
Müşteri Numaranız: 98765432
```

### Para Transferi
```
=== PARA TRANSFERİ ===
Kaynak Hesap: 1234567890
Hedef Hesap: 0987654321
Transfer Miktarı: 500 TL
Transfer başarıyla gerçekleştirildi!
```







