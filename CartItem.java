public class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // --- Getters ---
    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getSubtotal() {
        return product.getHarga() * quantity;
    }

    // --- Mutator ---
    public void addQuantity(int amount) {
        this.quantity += amount;
    }
}