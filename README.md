☕ Java Swing POS (Aplikasi Kasir Sederhana)
java-swing-pos adalah proyek aplikasi Point-of-Sale (POS) atau kasir sederhana yang dibuat menggunakan Java Swing.

Proyek ini dirancang dengan fokus pada Prinsip Pemrograman Berorientasi Objek (OOP), dengan memisahkan secara jelas antara Data (Model), Tampilan (View), dan Logika (Logic/Controller).

🚀 Fitur Utama
- Daftar Produk: Menampilkan daftar produk (ID, Nama, Harga) di dalam JTable.

- Keranjang Belanja: Menambahkan produk ke keranjang dengan kuantitas tertentu.

- Kalkulasi Otomatis: Total harga dan poin dihitung secara real-time setiap kali item ditambahkan.

- Proses Checkout: Memfinalisasi transaksi dan mencetak struk digital di area teks.

- Cetak Struk: Membuka dialog cetak sistem untuk mencetak struk yang sudah jadi.

🛠️ Arsitektur & Teknologi
Proyek ini dibangun menggunakan:

- Java: Bahasa pemrograman utama.

- Java Swing: Library untuk membangun komponen antarmuka (GUI).

Prinsip OOP (Separation of Concerns):

  - Model: Product.java, CartItem.java (Menyimpan data)
    
  - Logic: Cart.java (Mengelola logika bisnis, "otak" keranjang)
    
  - View/Controller: PointOfSalesGUI.java (Mengelola tampilan dan input pengguna)
  
  - Utility: ReceiptFormatter.java (Kelas bantu untuk memformat struk)
  
  - Entry Point: Main.java (Untuk menjalankan aplikasi)

🏃 Cara Menjalankan
Proyek ini tidak memerlukan library eksternal.

1. Clone repositori ini:
   
  "git clone https://github.com/NAMA_USER_ANDA/java-swing-pos.git"

3. Buka proyek di IDE favorit Anda (NetBeans, IntelliJ, Eclipse, VS Code).

4. Kompilasi semua file .java.
   
  "javac *.java"

6. Jalankan file Main.java.
   
  "java Main"
