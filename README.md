# PROGRAMLAMA DİLLERİ PRENSİPLERİ DÖNEM ÖDEVİ
## Java Dosya Analiz Programı
Bu proje, kullanıcının girdiği GitHub repo linkindeki klasörü klonlayarak, içerisindeki .java uzantılı dosyaları analiz eder. Program, Java sınıflarını tespit eder ve bu sınıflar hakkında temel bilgileri (Javadoc satır sayısı, yorum satır sayısı, kod satır sayısı, LOC, fonksiyon sayısı ve yorum sapma yüzdesi) çıkarır.

### Projenin Amacı
#Projenin temel amacı, bir GitHub reposundaki Java dosyalarını otomatik olarak analiz etmek ve bu dosyaların içerdiği sınıflar hakkında detaylı bilgi sunmaktır. Bu, büyük projelerdeki Java sınıflarını hızlıca incelemek veya belirli bir sınıfın yapısını analiz etmek için kullanılabilir.

### Nasıl Çalışır?
1. **Repo Klonlama**: Program, kullanıcının girdiği GitHub repo linkini kullanarak repoyu yerel bir dizine klonlar.

2. **Java Dosyalarını Bulma**: Klonlanan repoda .java uzantılı dosyaları arar.

3. **Sınıf Analizi**: Her bir .java dosyasını tarayarak içerisindeki sınıfları tespit eder ve bu sınıflar hakkında aşağıdaki bilgileri çıkarır:

- **Javadoc satır sayısı**

- **Yorum satır sayısı**

- **Kod satır sayısı**

- **LOC (Lines of Code)**

- **Fonksiyon sayısı**

- **Yorum sapma yüzdesi**

4. **Sonuçları Görüntüleme**: Analiz sonuçları kullanıcıya konsol üzerinde gösterilir.
