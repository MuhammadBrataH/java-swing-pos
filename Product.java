import java.text.NumberFormat;
import java.util.Locale;

/**
 * Class Model untuk menyimpan data produk.
 * Ini adalah "blueprint" untuk sebuah produk.
 */
public class Product {
    private String id;
    private String nama;
    private double harga;

    public Product(String id, String nama, double harga) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
    }

    // --- Getters ---
    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public double getHarga() {
        return harga;
    }

    public String getFormattedHarga() {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return currencyFormatter.format(this.harga);
    }
}