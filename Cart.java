import java.util.ArrayList;
import java.util.List;

/**
 * Class Logika Bisnis untuk mengelola keranjang belanja.
 * Bertanggung jawab untuk menambah item, menghitung total, dan poin.
 */
public class Cart {

    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    /**
     * Mencari item di keranjang berdasarkan produk.
     */
    private CartItem findItem(Product product) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                return item;
            }
        }
        return null;
    }

    /**
     * Menambahkan item ke keranjang.
     * Jika item sudah ada, tambahkan kuantitasnya.
     * Jika belum ada, tambahkan sebagai item baru.
     */
    public void addItem(Product product, int quantity) {
        CartItem existingItem = findItem(product);

        if (existingItem != null) {
            existingItem.addQuantity(quantity);
        } else {
            items.add(new CartItem(product, quantity));
        }
    }

    /**
     * Menghitung total harga dari semua item di keranjang.
     */
    public double getTotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    /**
     * Menghitung poin yang didapat.
     * (1 poin per Rp 1000)
     */
    public int getPoints() {
        return (int) (getTotal() / 1000);
    }

    /**
     * Mengosongkan keranjang.
     */
    public void clearCart() {
        items.clear();
    }

    /**
     * Mendapatkan daftar item untuk ditampilkan di GUI.
     */
    public List<CartItem> getItems() {
        return items;
    }
}