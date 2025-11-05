import java.text.NumberFormat;
import java.util.Locale;

/**
 * Class Utility yang bertanggung jawab HANYA untuk memformat struk.
 */
public class ReceiptFormatter {

    private NumberFormat currencyFormatter;

    public ReceiptFormatter() {
        // Inisialisasi formatter mata uang
        this.currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    }

    public String generateReceiptText(Cart cart) {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("            POIN OFF-SALES            \n");
        sb.append("             Toko: Demo Toko          \n");
        sb.append("========================================\n");
        sb.append(String.format("%-5s %-15s %-3s %-12s\n", "ID", "Nama", "Qty", "Subtotal"));
        sb.append("----------------------------------------\n");

        for (CartItem item : cart.getItems()) {
            sb.append(String.format("%-5s %-15.15s %-3d %-12s\n",
                    item.getProduct().getId(),
                    item.getProduct().getNama(),
                    item.getQuantity(),
                    currencyFormatter.format(item.getSubtotal())));
        }

        double total = cart.getTotal();
        int points = cart.getPoints();

        sb.append("----------------------------------------\n");
        sb.append(String.format("TOTAL: %28s\n", currencyFormatter.format(total)));
        sb.append(String.format("POINTS DIDAPAT: %d (1 point per Rp 1000)\n", points));
        sb.append("========================================\n");
        sb.append("  Terima kasih! Silakan kunjungi kembali. \n");

        return sb.toString();
    }
}