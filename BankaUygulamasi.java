import java.util.*;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.io.*;

// Ana uygulama sınıfı
public class BankaUygulamasi {
    public static void main(String[] args) {
        BankaYonetimSistemi sistem = new BankaYonetimSistemi();
        sistem.basla();
    }
}

// Banka yönetim sistemi sınıfı
class BankaYonetimSistemi {
    private Scanner scanner;
    private Banka banka;
    private Musteri aktifMusteri;
    private boolean calisma;
    
    public BankaYonetimSistemi() {
        scanner = new Scanner(System.in);
        banka = new Banka("TürkBank", "1234567890");
        calisma = true;
    }
    
    public void basla() {
        System.out.println("Hoş Geldiniz - " + banka.getAd() + " Banka Uygulaması");
        
        while(calisma) {
            if (aktifMusteri == null) {
                girisMenusu();
            } else {
                anaMenu();
            }
        }
    }
    
    private void girisMenusu() {
        System.out.println("\n=== GİRİŞ MENÜSÜ ===");
        System.out.println("1. Giriş Yap");
        System.out.println("2. Yeni Hesap Oluştur");
        System.out.println("3. Çıkış");
        System.out.print("Seçiminiz: ");
        
        int secim = scanner.nextInt();
        scanner.nextLine(); // Buffer temizleme
        
        switch(secim) {
            case 1:
                girisYap();
                break;
            case 2:
                yeniHesapOlustur();
                break;
            case 3:
                cikis();
                break;
            default:
                System.out.println("Geçersiz seçim!");
        }
    }
    
    private void anaMenu() {
        System.out.println("\n=== ANA MENÜ ===");
        System.out.println("Hoş geldiniz, " + aktifMusteri.getAd() + " " + aktifMusteri.getSoyad());
        System.out.println("1. Hesaplarımı Görüntüle");
        System.out.println("2. Para Transferi");
        System.out.println("3. Yeni Hesap Aç");
        System.out.println("4. Kredi İşlemleri");
        System.out.println("5. Hesap Hareketleri");
        System.out.println("6. Profil Bilgilerim");
        System.out.println("7. Oturumu Kapat");
        System.out.println("8. Çıkış");
        System.out.print("Seçiminiz: ");
        
        int secim = scanner.nextInt();
        scanner.nextLine(); // Buffer temizleme
        
        switch(secim) {
            case 1:
                hesaplariGoruntule();
                break;
            case 2:
                paraTransferi();
                break;
            case 3:
                yeniHesapAc();
                break;
            case 4:
                krediIslemleri();
                break;
            case 5:
                hesapHareketleri();
                break;
            case 6:
                profilBilgileri();
                break;
            case 7:
                oturumuKapat();
                break;
            case 8:
                cikis();
                break;
            default:
                System.out.println("Geçersiz seçim!");
        }
    }
    
    private void girisYap() {
        System.out.print("Müşteri Numarası: ");
        String musteriNo = scanner.nextLine();
        System.out.print("Şifre: ");
        String sifre = scanner.nextLine();
        
        Musteri musteri = banka.musteriGiris(musteriNo, sifre);
        
        if (musteri != null) {
            aktifMusteri = musteri;
            System.out.println("Giriş başarılı!");
        } else {
            System.out.println("Müşteri numarası veya şifre hatalı!");
        }
    }
    
    private void yeniHesapOlustur() {
        System.out.println("\n=== YENİ MÜŞTERİ KAYDI ===");
        System.out.print("Ad: ");
        String ad = scanner.nextLine();
        System.out.print("Soyad: ");
        String soyad = scanner.nextLine();
        System.out.print("TC Kimlik No: ");
        String tcKimlikNo = scanner.nextLine();
        System.out.print("Telefon: ");
        String telefon = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Adres: ");
        String adres = scanner.nextLine();
        System.out.print("Şifre: ");
        String sifre = scanner.nextLine();
        
        Musteri yeniMusteri = new Musteri(ad, soyad, tcKimlikNo, telefon, email, adres, sifre);
        banka.musteriEkle(yeniMusteri);
        
        System.out.println("Müşteri kaydınız oluşturuldu!");
        System.out.println("Müşteri Numaranız: " + yeniMusteri.getMusteriNo());
        System.out.println("Lütfen müşteri numaranızı not ediniz.");
    }
    
    private void hesaplariGoruntule() {
        List<Hesap> hesaplar = aktifMusteri.getTumHesaplar();
        System.out.println("\n=== HESAPLARIM ===");
        
        if (hesaplar.isEmpty()) {
            System.out.println("Henüz bir hesabınız bulunmamaktadır.");
            return;
        }
        
        DecimalFormat df = new DecimalFormat("#,##0.00");
        for (Hesap hesap : hesaplar) {
            System.out.println("-----------------------------");
            System.out.println("Hesap No: " + hesap.getHesapNo());
            System.out.println("Hesap Türü: " + hesap.getHesapTuru());
            System.out.println("Bakiye: " + df.format(hesap.getBakiye()) + " TL");
            
            if (hesap instanceof VadeliHesap) {
                VadeliHesap vadeliHesap = (VadeliHesap) hesap;
                System.out.println("Vade Süresi: " + vadeliHesap.getVadeSuresi() + " ay");
                System.out.println("Faiz Oranı: %" + vadeliHesap.getFaizOrani());
                System.out.println("Vadeli Bakiye: " + df.format(vadeliHesap.getVadeliBakiye()) + " TL");
                System.out.println("Vade Bitiş Tarihi: " + vadeliHesap.getVadeBitisTarihi());
            }
        }
    }
    
    private void paraTransferi() {
        List<Hesap> hesaplar = aktifMusteri.getTumHesaplar();
        
        if (hesaplar.isEmpty()) {
            System.out.println("Transfer için hesabınız bulunmamaktadır.");
            return;
        }
        
        System.out.println("\n=== PARA TRANSFERİ ===");
        System.out.println("1. Hesaplarım Arası Transfer");
        System.out.println("2. Başka Hesaba Transfer");
        System.out.print("Seçiminiz: ");
        
        int secim = scanner.nextInt();
        scanner.nextLine(); // Buffer temizleme
        
        switch(secim) {
            case 1:
                hesaplarArasiTransfer();
                break;
            case 2:
                baskaHesabaTransfer();
                break;
            default:
                System.out.println("Geçersiz seçim!");
        }
    }
    
    private void hesaplarArasiTransfer() {
        List<Hesap> hesaplar = aktifMusteri.getTumHesaplar();
        
        if (hesaplar.size() < 2) {
            System.out.println("Hesaplar arası transfer için en az 2 hesabınız olmalıdır.");
            return;
        }
        
        System.out.println("\nKaynak Hesap Seçin:");
        for (int i = 0; i < hesaplar.size(); i++) {
            Hesap hesap = hesaplar.get(i);
            System.out.println((i+1) + ". " + hesap.getHesapNo() + " - " + hesap.getHesapTuru() + " - " + hesap.getBakiye() + " TL");
        }
        
        System.out.print("Seçiminiz: ");
        int kaynakIndex = scanner.nextInt() - 1;
        
        if (kaynakIndex < 0 || kaynakIndex >= hesaplar.size()) {
            System.out.println("Geçersiz hesap seçimi!");
            return;
        }
        
        if (hesaplar.get(kaynakIndex) instanceof VadeliHesap) {
            VadeliHesap vadeliHesap = (VadeliHesap) hesaplar.get(kaynakIndex);
            if (!vadeliHesap.vadeBittiMi()) {
                System.out.println("Vadeli hesaptan vade süresi dolmadan para çekemezsiniz!");
                return;
            }
        }
        
        System.out.println("\nHedef Hesap Seçin:");
        for (int i = 0; i < hesaplar.size(); i++) {
            if (i != kaynakIndex) {
                Hesap hesap = hesaplar.get(i);
                System.out.println((i+1) + ". " + hesap.getHesapNo() + " - " + hesap.getHesapTuru() + " - " + hesap.getBakiye() + " TL");
            }
        }
        
        System.out.print("Seçiminiz: ");
        int hedefIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Buffer temizleme
        
        if (hedefIndex < 0 || hedefIndex >= hesaplar.size() || hedefIndex == kaynakIndex) {
            System.out.println("Geçersiz hesap seçimi!");
            return;
        }
        
        System.out.print("Transfer Miktarı (TL): ");
        double miktar = scanner.nextDouble();
        scanner.nextLine(); // Buffer temizleme
        
        Hesap kaynakHesap = hesaplar.get(kaynakIndex);
        Hesap hedefHesap = hesaplar.get(hedefIndex);
        
        if (kaynakHesap.getBakiye() < miktar) {
            System.out.println("Yetersiz bakiye!");
            return;
        }
        
        kaynakHesap.paraCek(miktar);
        hedefHesap.paraYatir(miktar);
        
        // İşlem kaydını oluştur
        String aciklama = "Hesaplar arası transfer: " + kaynakHesap.getHesapNo() + " -> " + hedefHesap.getHesapNo();
        HesapHareketi kaynakHareket = new HesapHareketi(kaynakHesap.getHesapNo(), "Para Çekme", miktar, aciklama);
        HesapHareketi hedefHareket = new HesapHareketi(hedefHesap.getHesapNo(), "Para Yatırma", miktar, aciklama);
        
        aktifMusteri.hareketEkle(kaynakHareket);
        aktifMusteri.hareketEkle(hedefHareket);
        
        System.out.println("Transfer başarıyla gerçekleştirildi!");
        System.out.println("Kaynak Hesap Bakiye: " + kaynakHesap.getBakiye() + " TL");
        System.out.println("Hedef Hesap Bakiye: " + hedefHesap.getBakiye() + " TL");
    }
    
    private void baskaHesabaTransfer() {
        List<Hesap> hesaplar = aktifMusteri.getTumHesaplar();
        
        System.out.println("\nKaynak Hesap Seçin:");
        for (int i = 0; i < hesaplar.size(); i++) {
            Hesap hesap = hesaplar.get(i);
            System.out.println((i+1) + ". " + hesap.getHesapNo() + " - " + hesap.getHesapTuru() + " - " + hesap.getBakiye() + " TL");
        }
        
        System.out.print("Seçiminiz: ");
        int kaynakIndex = scanner.nextInt() - 1;
        
        if (kaynakIndex < 0 || kaynakIndex >= hesaplar.size()) {
            System.out.println("Geçersiz hesap seçimi!");
            return;
        }
        
        if (hesaplar.get(kaynakIndex) instanceof VadeliHesap) {
            VadeliHesap vadeliHesap = (VadeliHesap) hesaplar.get(kaynakIndex);
            if (!vadeliHesap.vadeBittiMi()) {
                System.out.println("Vadeli hesaptan vade süresi dolmadan para çekemezsiniz!");
                return;
            }
        }
        
        scanner.nextLine(); // Buffer temizleme
        System.out.print("Alıcı Hesap Numarası: ");
        String hedefHesapNo = scanner.nextLine();
        
        Hesap hedefHesap = banka.hesapBul(hedefHesapNo);
        if (hedefHesap == null) {
            System.out.println("Belirtilen hesap numarası bulunamadı!");
            return;
        }
        
        System.out.print("Transfer Miktarı (TL): ");
        double miktar = scanner.nextDouble();
        scanner.nextLine(); // Buffer temizleme
        
        Hesap kaynakHesap = hesaplar.get(kaynakIndex);
        
        if (kaynakHesap.getBakiye() < miktar) {
            System.out.println("Yetersiz bakiye!");
            return;
        }
        
        System.out.print("Açıklama: ");
        String aciklama = scanner.nextLine();
        
        kaynakHesap.paraCek(miktar);
        hedefHesap.paraYatir(miktar);
        
        // İşlem kaydını oluştur
        String transferAciklama = aciklama.isEmpty() ? 
                                "Havale: " + kaynakHesap.getHesapNo() + " -> " + hedefHesap.getHesapNo() : 
                                aciklama;
                                
        HesapHareketi kaynakHareket = new HesapHareketi(kaynakHesap.getHesapNo(), "Havale Gönderimi", miktar, transferAciklama);
        HesapHareketi hedefHareket = new HesapHareketi(hedefHesap.getHesapNo(), "Havale Alımı", miktar, transferAciklama);
        
        aktifMusteri.hareketEkle(kaynakHareket);
        
        // Hedef hesabın sahibini bul ve işlem kaydını ekle
        Musteri hedefMusteri = banka.hesapSahibiBul(hedefHesapNo);
        if (hedefMusteri != null) {
            hedefMusteri.hareketEkle(hedefHareket);
        }
        
        System.out.println("Transfer başarıyla gerçekleştirildi!");
        System.out.println("Güncel Bakiye: " + kaynakHesap.getBakiye() + " TL");
    }
    
    private void yeniHesapAc() {
        System.out.println("\n=== YENİ HESAP AÇ ===");
        System.out.println("1. Vadesiz Hesap");
        System.out.println("2. Vadeli Hesap");
        System.out.print("Seçiminiz: ");
        
        int secim = scanner.nextInt();
        scanner.nextLine(); // Buffer temizleme
        
        switch(secim) {
            case 1:
                vadesizHesapAc();
                break;
            case 2:
                vadeliHesapAc();
                break;
            default:
                System.out.println("Geçersiz seçim!");
        }
    }
    
    private void vadesizHesapAc() {
        System.out.print("Başlangıç Bakiyesi (TL): ");
        double bakiye = scanner.nextDouble();
        scanner.nextLine(); // Buffer temizleme
        
        VadesizHesap yeniHesap = new VadesizHesap(bakiye);
        aktifMusteri.hesapEkle(yeniHesap);
        
        // İşlem kaydını oluştur
        HesapHareketi hareket = new HesapHareketi(yeniHesap.getHesapNo(), "Hesap Açılışı", bakiye, "Yeni vadesiz hesap açılışı");
        aktifMusteri.hareketEkle(hareket);
        
        System.out.println("Vadesiz hesap başarıyla açıldı!");
        System.out.println("Hesap Numarası: " + yeniHesap.getHesapNo());
        System.out.println("Bakiye: " + yeniHesap.getBakiye() + " TL");
    }
    
    private void vadeliHesapAc() {
        System.out.print("Başlangıç Bakiyesi (TL): ");
        double bakiye = scanner.nextDouble();
        
        System.out.print("Vade Süresi (ay): ");
        int vadeSuresi = scanner.nextInt();
        scanner.nextLine(); // Buffer temizleme
        
        // Faiz oranını vade süresine göre belirle
        double faizOrani;
        if (vadeSuresi <= 3) {
            faizOrani = 15.0; // %15
        } else if (vadeSuresi <= 6) {
            faizOrani = 17.5; // %17.5
        } else if (vadeSuresi <= 12) {
            faizOrani = 20.0; // %20
        } else {
            faizOrani = 22.5; // %22.5
        }
        
        VadeliHesap yeniHesap = new VadeliHesap(bakiye, vadeSuresi, faizOrani);
        aktifMusteri.hesapEkle(yeniHesap);
        
        // İşlem kaydını oluştur
        HesapHareketi hareket = new HesapHareketi(yeniHesap.getHesapNo(), "Hesap Açılışı", bakiye, 
                                                 "Yeni vadeli hesap açılışı - Vade: " + vadeSuresi + " ay, Faiz: %" + faizOrani);
        aktifMusteri.hareketEkle(hareket);
        
        System.out.println("Vadeli hesap başarıyla açıldı!");
        System.out.println("Hesap Numarası: " + yeniHesap.getHesapNo());
        System.out.println("Bakiye: " + yeniHesap.getBakiye() + " TL");
        System.out.println("Vade Süresi: " + vadeSuresi + " ay");
        System.out.println("Faiz Oranı: %" + faizOrani);
        System.out.println("Vade Sonunda Tahmini Bakiye: " + yeniHesap.getVadeliBakiye() + " TL");
        System.out.println("Vade Bitiş Tarihi: " + yeniHesap.getVadeBitisTarihi());
    }
    
    private void krediIslemleri() {
        System.out.println("\n=== KREDİ İŞLEMLERİ ===");
        System.out.println("1. Kredi Başvurusu");
        System.out.println("2. Kredilerim");
        System.out.println("3. Kredi Ödemesi");
        System.out.print("Seçiminiz: ");
        
        int secim = scanner.nextInt();
        scanner.nextLine(); // Buffer temizleme
        
        switch(secim) {
            case 1:
                krediBasvurusu();
                break;
            case 2:
                kredileriGoruntule();
                break;
            case 3:
                krediOdemesi();
                break;
            default:
                System.out.println("Geçersiz seçim!");
        }
    }
    
    private void krediBasvurusu() {
        System.out.println("\n=== KREDİ BAŞVURUSU ===");
        System.out.println("Kredi Türleri:");
        System.out.println("1. İhtiyaç Kredisi (Faiz: %24.99)");
        System.out.println("2. Konut Kredisi (Faiz: %19.99)");
        System.out.println("3. Taşıt Kredisi (Faiz: %22.99)");
        System.out.print("Seçiminiz: ");
        
        int krediTuruSecim = scanner.nextInt();
        
        String krediTuru;
        double faizOrani;
        
        switch(krediTuruSecim) {
            case 1:
                krediTuru = "İhtiyaç Kredisi";
                faizOrani = 24.99;
                break;
            case 2:
                krediTuru = "Konut Kredisi";
                faizOrani = 19.99;
                break;
            case 3:
                krediTuru = "Taşıt Kredisi";
                faizOrani = 22.99;
                break;
            default:
                System.out.println("Geçersiz seçim!");
                return;
        }
        
        System.out.print("Kredi Miktarı (TL): ");
        double miktar = scanner.nextDouble();
        
        System.out.print("Vade (ay): ");
        int vade = scanner.nextInt();
        scanner.nextLine(); // Buffer temizleme
        
        if (miktar <= 0 || vade <= 0) {
            System.out.println("Geçersiz miktar veya vade!");
            return;
        }
        
        // Kredi başvurusunu değerlendir
        boolean krediOnay = banka.krediBasvurusuDegerlendir(aktifMusteri, miktar);
        
        if (krediOnay) {
            // Aylık taksit hesaplama (basit hesaplama)
            double aylikFaizOrani = faizOrani / 100 / 12; // Aylık faiz oranı
            double aylikTaksit = (miktar * aylikFaizOrani * Math.pow(1 + aylikFaizOrani, vade)) / 
                            (Math.pow(1 + aylikFaizOrani, vade) - 1);
            
            KrediHesabi yeniKredi = new KrediHesabi(krediTuru, miktar, vade, faizOrani, aylikTaksit);
            aktifMusteri.krediEkle(yeniKredi);
            
            // Kredi tutarını vadesiz hesaba aktarma
            List<Hesap> vadesizHesaplar = aktifMusteri.getVadesizHesaplar();
            
            if (!vadesizHesaplar.isEmpty()) {
                VadesizHesap hedefHesap = (VadesizHesap) vadesizHesaplar.get(0);
                hedefHesap.paraYatir(miktar);
                
                // İşlem kayıtlarını oluştur
                HesapHareketi krediHareket = new HesapHareketi(yeniKredi.getKrediNo(), "Kredi Kullanımı", miktar, 
                                                             krediTuru + " kullanımı - Vade: " + vade + " ay, Faiz: %" + faizOrani);
                HesapHareketi hesapHareket = new HesapHareketi(hedefHesap.getHesapNo(), "Kredi Tutarı", miktar, 
                                                             "Kredi tutarı hesaba aktarıldı - Kredi No: " + yeniKredi.getKrediNo());
                
                aktifMusteri.hareketEkle(krediHareket);
                aktifMusteri.hareketEkle(hesapHareket);
                
                System.out.println("\nKredi başvurunuz onaylandı!");
                System.out.println("Kredi Numarası: " + yeniKredi.getKrediNo());
                System.out.println("Kredi Tutarı: " + miktar + " TL");
                System.out.println("Vade: " + vade + " ay");
                System.out.println("Faiz Oranı: %" + faizOrani);
                System.out.println("Aylık Taksit: " + String.format("%.2f", aylikTaksit) + " TL");
                System.out.println("Toplam Geri Ödeme: " + String.format("%.2f", aylikTaksit * vade) + " TL");
                System.out.println("Kredi tutarı " + hedefHesap.getHesapNo() + " numaralı hesabınıza aktarıldı.");
            } else {
                System.out.println("\nKredi başvurunuz onaylandı, ancak vadesiz hesabınız olmadığı için kredi tutarı aktarılamadı.");
                System.out.println("Lütfen bir vadesiz hesap açın ve müşteri hizmetleriyle iletişime geçin.");
            }
        } else {
            System.out.println("\nKredi başvurunuz reddedildi.");
            System.out.println("Nedeni: Kredi skoru yetersiz veya mevcut borç yükü fazla.");
        }
    }
    
    private void kredileriGoruntule() {
        List<KrediHesabi> krediler = aktifMusteri.getKrediler();
        System.out.println("\n=== KREDİLERİM ===");
        
        if (krediler.isEmpty()) {
            System.out.println("Aktif krediniz bulunmamaktadır.");
            return;
        }
        
        DecimalFormat df = new DecimalFormat("#,##0.00");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for (KrediHesabi kredi : krediler) {
            System.out.println("-----------------------------");
            System.out.println("Kredi No: " + kredi.getKrediNo());
            System.out.println("Kredi Türü: " + kredi.getKrediTuru());
            System.out.println("Kredi Tutarı: " + df.format(kredi.getKrediTutari()) + " TL");
            System.out.println("Kalan Borç: " + df.format(kredi.getKalanBorc()) + " TL");
            System.out.println("Vade: " + kredi.getVade() + " ay");
            System.out.println("Kalan Taksit: " + kredi.getKalanTaksit());
            System.out.println("Faiz Oranı: %" + kredi.getFaizOrani());
            System.out.println("Aylık Taksit: " + df.format(kredi.getAylikTaksit()) + " TL");
            System.out.println("Son Ödeme Tarihi: " + sdf.format(kredi.getSonOdemeTarihi()));
            System.out.println("Kredi Durumu: " + (kredi.isAktif() ? "Aktif" : "Kapalı"));
        }
    }
    
    private void krediOdemesi() {
        List<KrediHesabi> krediler = aktifMusteri.getAktifKrediler();
        
        if (krediler.isEmpty()) {
            System.out.println("Ödeme yapılacak aktif krediniz bulunmamaktadır.");
            return;
        }
        
        System.out.println("\n=== KREDİ ÖDEMESİ ===");
        System.out.println("Ödeme Yapılacak Kredi Seçin:");
        
        for (int i = 0; i < krediler.size(); i++) {
            KrediHesabi kredi = krediler.get(i);
            System.out.println((i+1) + ". " + kredi.getKrediNo() + " - " + kredi.getKrediTuru() + 
                             " - Kalan Borç: " + String.format("%.2f", kredi.getKalanBorc()) + " TL");
        }
        
        System.out.print("Seçiminiz: ");
        int krediIndex = scanner.nextInt() - 1;
        
        if (krediIndex < 0 || krediIndex >= krediler.size()) {
            System.out.println("Geçersiz seçim!");
            return;
        }
        KrediHesabi secilenKredi = krediler.get(krediIndex);
        
        System.out.println("\nÖdeme Yöntemi Seçin:");
        System.out.println("1. Hesaptan Ödeme");
        System.out.println("2. Nakit Ödeme");
        System.out.print("Seçiminiz: ");
        
        int odemeYontemi = scanner.nextInt();
        scanner.nextLine(); // Buffer temizleme
        
        switch(odemeYontemi) {
            case 1:
                hesaptanKrediOdemesi(secilenKredi);
                break;
            case 2:
                nakitKrediOdemesi(secilenKredi);
                break;
            default:
                System.out.println("Geçersiz seçim!");
        }
    }
    
    private void hesaptanKrediOdemesi(KrediHesabi kredi) {
        List<Hesap> vadesizHesaplar = aktifMusteri.getVadesizHesaplar();
        
        if (vadesizHesaplar.isEmpty()) {
            System.out.println("Ödeme için vadesiz hesabınız bulunmamaktadır.");
            return;
        }
        
        System.out.println("\nÖdeme Yapılacak Hesap Seçin:");
        for (int i = 0; i < vadesizHesaplar.size(); i++) {
            Hesap hesap = vadesizHesaplar.get(i);
            System.out.println((i+1) + ". " + hesap.getHesapNo() + " - Bakiye: " + hesap.getBakiye() + " TL");
        }
        
        System.out.print("Seçiminiz: ");
        int hesapIndex = scanner.nextInt() - 1;
        
        if (hesapIndex < 0 || hesapIndex >= vadesizHesaplar.size()) {
            System.out.println("Geçersiz seçim!");
            return;
        }
        
        System.out.println("\nÖdeme Tutarı Seçin:");
        System.out.println("1. Taksit Tutarı (" + String.format("%.2f", kredi.getAylikTaksit()) + " TL)");
        System.out.println("2. Farklı Bir Tutar");
        System.out.println("3. Kalan Tüm Borç (" + String.format("%.2f", kredi.getKalanBorc()) + " TL)");
        System.out.print("Seçiminiz: ");
        
        int tutarSecimi = scanner.nextInt();
        scanner.nextLine(); // Buffer temizleme
        
        double odemeTutari;
        
        switch(tutarSecimi) {
            case 1:
                odemeTutari = kredi.getAylikTaksit();
                break;
            case 2:
                System.out.print("Ödeme tutarını girin (TL): ");
                odemeTutari = scanner.nextDouble();
                scanner.nextLine(); // Buffer temizleme
                break;
            case 3:
                odemeTutari = kredi.getKalanBorc();
                break;
            default:
                System.out.println("Geçersiz seçim!");
                return;
        }
        
        if (odemeTutari <= 0 || odemeTutari > kredi.getKalanBorc()) {
            System.out.println("Geçersiz ödeme tutarı!");
            return;
        }
        
        Hesap odemeyeHesap = vadesizHesaplar.get(hesapIndex);
        
        if (odemeyeHesap.getBakiye() < odemeTutari) {
            System.out.println("Hesapta yeterli bakiye bulunmamaktadır!");
            return;
        }
        
        odemeyeHesap.paraCek(odemeTutari);
        boolean tamOdeme = kredi.odemeYap(odemeTutari);
        
        // İşlem kaydını oluştur
        HesapHareketi hesapHareket = new HesapHareketi(odemeyeHesap.getHesapNo(), "Kredi Ödemesi", odemeTutari, 
                                                      "Kredi ödemesi - Kredi No: " + kredi.getKrediNo());
        HesapHareketi krediHareket = new HesapHareketi(kredi.getKrediNo(), "Kredi Ödemesi", odemeTutari, 
                                                      "Hesaptan ödeme - Hesap No: " + odemeyeHesap.getHesapNo());
        
        aktifMusteri.hareketEkle(hesapHareket);
        aktifMusteri.hareketEkle(krediHareket);
        
        System.out.println("\nÖdeme başarıyla gerçekleştirildi!");
        System.out.println("Ödenen Tutar: " + odemeTutari + " TL");
        System.out.println("Kalan Borç: " + kredi.getKalanBorc() + " TL");
        
        if (tamOdeme) {
            System.out.println("Krediniz tamamen kapanmıştır. Teşekkür ederiz!");
        }
    }
    
    private void nakitKrediOdemesi(KrediHesabi kredi) {
        System.out.println("\nÖdeme Tutarı Seçin:");
        System.out.println("1. Taksit Tutarı (" + String.format("%.2f", kredi.getAylikTaksit()) + " TL)");
        System.out.println("2. Farklı Bir Tutar");
        System.out.println("3. Kalan Tüm Borç (" + String.format("%.2f", kredi.getKalanBorc()) + " TL)");
        System.out.print("Seçiminiz: ");
        
        int tutarSecimi = scanner.nextInt();
        scanner.nextLine(); // Buffer temizleme
        
        double odemeTutari;
        
        switch(tutarSecimi) {
            case 1:
                odemeTutari = kredi.getAylikTaksit();
                break;
            case 2:
                System.out.print("Ödeme tutarını girin (TL): ");
                odemeTutari = scanner.nextDouble();
                scanner.nextLine(); // Buffer temizleme
                break;
            case 3:
                odemeTutari = kredi.getKalanBorc();
                break;
            default:
                System.out.println("Geçersiz seçim!");
                return;
        }
        
        if (odemeTutari <= 0 || odemeTutari > kredi.getKalanBorc()) {
            System.out.println("Geçersiz ödeme tutarı!");
            return;
        }
        
        boolean tamOdeme = kredi.odemeYap(odemeTutari);
        
        // İşlem kaydını oluştur
        HesapHareketi krediHareket = new HesapHareketi(kredi.getKrediNo(), "Kredi Ödemesi", odemeTutari, "Nakit ödeme");
        aktifMusteri.hareketEkle(krediHareket);
        
        System.out.println("\nÖdeme başarıyla gerçekleştirildi!");
        System.out.println("Ödenen Tutar: " + odemeTutari + " TL");
        System.out.println("Kalan Borç: " + kredi.getKalanBorc() + " TL");
        
        if (tamOdeme) {
            System.out.println("Krediniz tamamen kapanmıştır. Teşekkür ederiz!");
        }
    }
    
    private void hesapHareketleri() {
        List<Hesap> hesaplar = aktifMusteri.getTumHesaplar();
        List<KrediHesabi> krediler = aktifMusteri.getKrediler();
        
        System.out.println("\n=== HESAP HAREKETLERİ ===");
        System.out.println("1. Tüm Hesap Hareketleri");
        System.out.println("2. Belirli Bir Hesabın Hareketleri");
        System.out.print("Seçiminiz: ");
        
        int secim = scanner.nextInt();
        scanner.nextLine(); // Buffer temizleme
        
        if (secim == 1) {
            tumHesapHareketleri();
        } else if (secim == 2) {
            System.out.println("\nHesap Seçin:");
            int index = 1;
            
            // Banka hesaplarını listele
            for (Hesap hesap : hesaplar) {
                System.out.println(index + ". " + hesap.getHesapNo() + " - " + hesap.getHesapTuru());
                index++;
            }
            
            // Kredi hesaplarını listele
            for (KrediHesabi kredi : krediler) {
                System.out.println(index + ". " + kredi.getKrediNo() + " - " + kredi.getKrediTuru());
                index++;
            }
            
            System.out.print("Seçiminiz: ");
            int hesapIndex = scanner.nextInt();
            scanner.nextLine(); // Buffer temizleme
            
            String hesapNo;
            if (hesapIndex <= hesaplar.size()) {
                hesapNo = hesaplar.get(hesapIndex - 1).getHesapNo();
            } else {
                int krediIndex = hesapIndex - hesaplar.size() - 1;
                if (krediIndex >= 0 && krediIndex < krediler.size()) {
                    hesapNo = krediler.get(krediIndex).getKrediNo();
                } else {
                    System.out.println("Geçersiz seçim!");
                    return;
                }
            }
            
            belirliHesapHareketleri(hesapNo);
        } else {
            System.out.println("Geçersiz seçim!");
        }
    }
    
    private void tumHesapHareketleri() {
        List<HesapHareketi> hareketler = aktifMusteri.getTumHareketler();
        
        if (hareketler.isEmpty()) {
            System.out.println("Henüz hesap hareketi bulunmamaktadır.");
            return;
        }
        
        System.out.println("\n=== TÜM HESAP HAREKETLERİ ===");
        hareketleriGoster(hareketler);
    }
    
    private void belirliHesapHareketleri(String hesapNo) {
        List<HesapHareketi> hareketler = aktifMusteri.getHesapHareketleri(hesapNo);
        
        if (hareketler.isEmpty()) {
            System.out.println("Bu hesap için hareket bulunmamaktadır.");
            return;
        }
        
        System.out.println("\n=== HESAP HAREKETLERİ: " + hesapNo + " ===");
        hareketleriGoster(hareketler);
    }
    
    private void hareketleriGoster(List<HesapHareketi> hareketler) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DecimalFormat df = new DecimalFormat("#,##0.00");
        
        // Hareketleri tarihe göre tersine sırala (en yeni en üstte)
        hareketler.sort((h1, h2) -> h2.getTarih().compareTo(h1.getTarih()));
        
        for (HesapHareketi hareket : hareketler) {
            System.out.println("-----------------------------");
            System.out.println("Tarih: " + sdf.format(hareket.getTarih()));
            System.out.println("Hesap/Kredi No: " + hareket.getHesapNo());
            System.out.println("İşlem Türü: " + hareket.getIslemTuru());
            System.out.println("Tutar: " + df.format(hareket.getTutar()) + " TL");
            System.out.println("Açıklama: " + hareket.getAciklama());
        }
    }
    
    private void profilBilgileri() {
        System.out.println("\n=== PROFİL BİLGİLERİM ===");
        System.out.println("Müşteri Numarası: " + aktifMusteri.getMusteriNo());
        System.out.println("Ad: " + aktifMusteri.getAd());
        System.out.println("Soyad: " + aktifMusteri.getSoyad());
        System.out.println("TC Kimlik No: " + aktifMusteri.getTcKimlikNo());
        System.out.println("Telefon: " + aktifMusteri.getTelefon());
        System.out.println("Email: " + aktifMusteri.getEmail());
        System.out.println("Adres: " + aktifMusteri.getAdres());
        
        System.out.println("\n1. Profil Bilgilerini Güncelle");
        System.out.println("2. Şifre Değiştir");
        System.out.println("3. Ana Menüye Dön");
        System.out.print("Seçiminiz: ");
        
        int secim = scanner.nextInt();
        scanner.nextLine(); // Buffer temizleme
        
        switch(secim) {
            case 1:
                profilGuncelle();
                break;
            case 2:
                sifreDegistir();
                break;
            case 3:
                return;
            default:
                System.out.println("Geçersiz seçim!");
        }
    }
    
    private void profilGuncelle() {
        System.out.println("\n=== PROFİL BİLGİLERİNİ GÜNCELLE ===");
        System.out.println("Güncellemek istediğiniz bilgiyi seçin:");
        System.out.println("1. Telefon");
        System.out.println("2. Email");
        System.out.println("3. Adres");
        System.out.print("Seçiminiz: ");
        
        int secim = scanner.nextInt();
        scanner.nextLine(); // Buffer temizleme
        
        switch(secim) {
            case 1:
                System.out.print("Yeni Telefon: ");
                String yeniTelefon = scanner.nextLine();
                aktifMusteri.setTelefon(yeniTelefon);
                System.out.println("Telefon numarası güncellendi!");
                break;
            case 2:
                System.out.print("Yeni Email: ");
                String yeniEmail = scanner.nextLine();
                aktifMusteri.setEmail(yeniEmail);
                System.out.println("Email adresi güncellendi!");
                break;
            case 3:
                System.out.print("Yeni Adres: ");
                String yeniAdres = scanner.nextLine();
                aktifMusteri.setAdres(yeniAdres);
                System.out.println("Adres güncellendi!");
                break;
            default:
                System.out.println("Geçersiz seçim!");
        }
    }
    
    private void sifreDegistir() {
        System.out.println("\n=== ŞİFRE DEĞİŞTİR ===");
        System.out.print("Mevcut Şifre: ");
        String mevcutSifre = scanner.nextLine();
        
        if (!aktifMusteri.getSifre().equals(mevcutSifre)) {
            System.out.println("Mevcut şifre hatalı!");
            return;
        }
        
        System.out.print("Yeni Şifre: ");
        String yeniSifre = scanner.nextLine();
        
        System.out.print("Yeni Şifre (Tekrar): ");
        String yeniSifreTekrar = scanner.nextLine();
        
        if (!yeniSifre.equals(yeniSifreTekrar)) {
            System.out.println("Yeni şifreler eşleşmiyor!");
            return;
        }
        
        aktifMusteri.setSifre(yeniSifre);
        System.out.println("Şifre başarıyla değiştirildi!");
    }
    
    private void oturumuKapat() {
        aktifMusteri = null;
        System.out.println("Oturum kapatıldı. İyi günler!");
    }
    
    private void cikis() {
        System.out.println("Banka uygulamasından çıkılıyor. İyi günler!");
        calisma = false;
        scanner.close();
    }
}

// Banka sınıfı
class Banka {
    private String ad;
    private String bankaKodu;
    private List<Musteri> musteriler;
    
    public Banka(String ad, String bankaKodu) {
        this.ad = ad;
        this.bankaKodu = bankaKodu;
        this.musteriler = new ArrayList<>();
        
        // Örnek müşteriler ekle
        ornekMusterilerEkle();
    }
    
    private void ornekMusterilerEkle() {
        // Örnek müşteri 1
        Musteri musteri1 = new Musteri("Ahmet", "Yılmaz", "12345678901", "5551234567", 
                                      "ahmet@example.com", "İstanbul, Kadıköy", "123456");
        
        VadesizHesap vadesizHesap1 = new VadesizHesap(5000);
        VadeliHesap vadeliHesap1 = new VadeliHesap(10000, 6, 18.5);
        
        musteri1.hesapEkle(vadesizHesap1);
        musteri1.hesapEkle(vadeliHesap1);
        
        musteriler.add(musteri1);
        
        // Örnek müşteri 2
        Musteri musteri2 = new Musteri("Ayşe", "Demir", "98765432109", "5559876543", 
                                      "ayse@example.com", "Ankara, Çankaya", "654321");
        
        VadesizHesap vadesizHesap2 = new VadesizHesap(7500);
        
        musteri2.hesapEkle(vadesizHesap2);
        
        KrediHesabi kredi1 = new KrediHesabi("İhtiyaç Kredisi", 20000, 12, 24.99, 1866.58);
        musteri2.krediEkle(kredi1);
        
        musteriler.add(musteri2);
    }
    
    public void musteriEkle(Musteri musteri) {
        musteriler.add(musteri);
    }
    
    public Musteri musteriGiris(String musteriNo, String sifre) {
        for (Musteri musteri : musteriler) {
            if (musteri.getMusteriNo().equals(musteriNo) && musteri.getSifre().equals(sifre)) {
                return musteri;
            }
        }
        return null;
    }
    
    public Hesap hesapBul(String hesapNo) {
        for (Musteri musteri : musteriler) {
            Hesap hesap = musteri.hesapBul(hesapNo);
            if (hesap != null) {
                return hesap;
            }
        }
        return null;
    }
    
    public Musteri hesapSahibiBul(String hesapNo) {
        for (Musteri musteri : musteriler) {
            Hesap hesap = musteri.hesapBul(hesapNo);
            if (hesap != null) {
                return musteri;
            }
        }
        return null;
    }
    
    public boolean krediBasvurusuDegerlendir(Musteri musteri, double miktar) {
        // Basit bir kredi değerlendirme algoritması
        // Gerçek bir uygulamada çok daha karmaşık olacaktır
        
        // Mevcut toplam kredi borcu hesapla
        double toplamKrediBorcu = 0;
        for (KrediHesabi kredi : musteri.getAktifKrediler()) {
            toplamKrediBorcu += kredi.getKalanBorc();
        }
        
        // Toplam varlık hesapla
        double toplamVarlik = 0;
        for (Hesap hesap : musteri.getTumHesaplar()) {
            toplamVarlik += hesap.getBakiye();
        }
        
        // Kredi skoru hesapla (0-100 arası)
        int krediSkoru = (int) (Math.random() * 40) + 60; // 60-100 arası rastgele
        
        // Kredi değerlendirme kriterleri
        boolean yeterliSkor = krediSkoru >= 70;
        boolean yeterliBorcOrani = (toplamKrediBorcu + miktar) <= (toplamVarlik * 5);
        
        return yeterliSkor && yeterliBorcOrani;
    }
    
    public String getAd() {
        return ad;
    }
    
    public String getBankaKodu() {
        return bankaKodu;
    }
}

// Müşteri sınıfı
class Musteri {
    private String ad;
    private String soyad;
    private String tcKimlikNo;
    private String telefon;
    private String email;
    private String adres;
    private String musteriNo;
    private String sifre;
    private List<Hesap> hesaplar;
    private List<KrediHesabi> krediler;
    private List<HesapHareketi> hesapHareketleri;
    
    public Musteri(String ad, String soyad, String tcKimlikNo, String telefon, String email, String adres, String sifre) {
        this.ad = ad;
        this.soyad = soyad;
        this.tcKimlikNo = tcKimlikNo;
        this.telefon = telefon;
        this.email = email;
        this.adres = adres;
        this.sifre = sifre;
        this.musteriNo = musteriNoUret();
        this.hesaplar = new ArrayList<>();
        this.krediler = new ArrayList<>();
        this.hesapHareketleri = new ArrayList<>();
    }
    
    private String musteriNoUret() {
        // 8 haneli rastgele bir müşteri numarası üret
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < 8; i++) {
            sb.append(random.nextInt(10));
        }
        
        return sb.toString();
    }
    
    public void hesapEkle(Hesap hesap) {
        hesaplar.add(hesap);
    }
    
    public void krediEkle(KrediHesabi kredi) {
        krediler.add(kredi);
    }
    
    public void hareketEkle(HesapHareketi hareket) {
        hesapHareketleri.add(hareket);
    }
    
    public Hesap hesapBul(String hesapNo) {
        for (Hesap hesap : hesaplar) {
            if (hesap.getHesapNo().equals(hesapNo)) {
                return hesap;
            }
        }
        return null;
    }
    
    public List<Hesap> getTumHesaplar() {
        return new ArrayList<>(hesaplar);
    }
    
    public List<Hesap> getVadesizHesaplar() {
        List<Hesap> vadesizHesaplar = new ArrayList<>();
        for (Hesap hesap : hesaplar) {
            if (hesap instanceof VadesizHesap) {
                vadesizHesaplar.add(hesap);
            }
        }
        return vadesizHesaplar;
    }
    
    public List<KrediHesabi> getKrediler() {
        return new ArrayList<>(krediler);
    }
    
    public List<KrediHesabi> getAktifKrediler() {
        List<KrediHesabi> aktifKrediler = new ArrayList<>();
        for (KrediHesabi kredi : krediler) {
            if (kredi.isAktif()) {
                aktifKrediler.add(kredi);
            }
        }
        return aktifKrediler;
    }
    
    public List<HesapHareketi> getTumHareketler() {
        return new ArrayList<>(hesapHareketleri);
    }
    
    public List<HesapHareketi> getHesapHareketleri(String hesapNo) {
        List<HesapHareketi> filtreliHareketler = new ArrayList<>();
        for (HesapHareketi hareket : hesapHareketleri) {
            if (hareket.getHesapNo().equals(hesapNo)) {
                filtreliHareketler.add(hareket);
            }
        }
        return filtreliHareketler;
    }
    
    // Getter ve Setter'lar
    public String getAd() {
        return ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public String getTcKimlikNo() {
        return tcKimlikNo;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getMusteriNo() {
        return musteriNo;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }
}

// Hesap sınıfı (abstract)
abstract class Hesap {
    protected String hesapNo;
    protected double bakiye;
    protected Date acilisTarihi;
    
    public Hesap(double bakiye) {
        this.bakiye = bakiye;
        this.hesapNo = hesapNoUret();
        this.acilisTarihi = new Date();
    }
    
    private String hesapNoUret() {
        // 10 haneli rastgele bir hesap numarası üret
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        
        return sb.toString();
    }
    
    public void paraYatir(double miktar) {
        if (miktar > 0) {
            bakiye += miktar;
        }
    }
    
    public boolean paraCek(double miktar) {
        if (miktar > 0 && bakiye >= miktar) {
            bakiye -= miktar;
            return true;
        }
        return false;
    }
    
    public String getHesapNo() {
        return hesapNo;
    }
    
    public double getBakiye() {
        return bakiye;
    }
    
    public Date getAcilisTarihi() {
        return acilisTarihi;
    }
    
    public abstract String getHesapTuru();
}

// Vadesiz Hesap sınıfı
class VadesizHesap extends Hesap {
    public VadesizHesap(double bakiye) {
        super(bakiye);
    }
    
    @Override
    public String getHesapTuru() {
        return "Vadesiz Hesap";
    }
}

// Vadeli Hesap sınıfı
class VadeliHesap extends Hesap {
    private int vadeSuresi; // ay cinsinden
    private double faizOrani; // yıllık yüzde
    private Date vadeBitisTarihi;
    
    public VadeliHesap(double bakiye, int vadeSuresi, double faizOrani) {
        super(bakiye);
        this.vadeSuresi = vadeSuresi;
        this.faizOrani = faizOrani;
        this.vadeBitisTarihi = hesaplaVadeBitisTarihi();
    }
    
    private Date hesaplaVadeBitisTarihi() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(acilisTarihi);
        calendar.add(Calendar.MONTH, vadeSuresi);
        return calendar.getTime();
    }
    
    public boolean vadeBittiMi() {
        return new Date().after(vadeBitisTarihi);
    }
    
    public double getVadeliBakiye() {
        // Basit faiz hesabı: Anapara * (1 + (Faiz Oranı * Vade Süresi / 12))
        return bakiye * (1 + (faizOrani / 100 * vadeSuresi / 12));
    }
    
    @Override
    public boolean paraCek(double miktar) {
        if (!vadeBittiMi()) {
            return false; // Vade dolmadan para çekilemez
        }
        return super.paraCek(miktar);
    }
    
    @Override
    public String getHesapTuru() {
        return "Vadeli Hesap";
    }
    
    public int getVadeSuresi() {
        return vadeSuresi;
    }
    
    public double getFaizOrani() {
        return faizOrani;
    }
        