import java.awt.*;
import java.awt.print.PrinterException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Class View (GUI) utama.
 * Bertanggung jawab untuk menampilkan UI dan menangani input pengguna.
 * Menggunakan class Cart, Product, dan ReceiptFormatter.
 */
public class PointOfSalesGUI extends JFrame {

    // --- Komponen UI ---
    private JTable productTable;
    private DefaultTableModel productModel;
    private JLabel selectedProductLabel;
    private JTextField qtyField;
    private JButton addToCartButton;

    private JTable cartTable;
    private DefaultTableModel cartModel;
    private JLabel totalLabel;
    private JLabel pointsLabel;
    private JButton checkoutButton;
    private JButton printButton;
    private JTextArea receiptArea;

    // --- Data & Logika ---
    private List<Product> productDatabase;
    private Cart cart;
    private ReceiptFormatter receiptFormatter;
    private NumberFormat currencyFormatter;

    // Menyimpan produk yang sedang dipilih di tabel produk
    private Product currentlySelectedProduct;

    public PointOfSalesGUI() {
        this.productDatabase = new ArrayList<>();
        this.cart = new Cart();
        this.receiptFormatter = new ReceiptFormatter();
        this.currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        loadProductDatabase(); // Memuat data produk "dummy"
        initUI(); // Membangun antarmuka
        populateProductTable(); // Mengisi tabel produk

        // Set pilihan awal agar sesuai gambar (P004 - Snack Keripik)
        productTable.setRowSelectionInterval(3, 3);
    }

    /**
     * Mengisi database produk (hardcoded).
     */
    private void loadProductDatabase() {
        productDatabase.add(new Product("P001", "Air Mineral 600ml", 3000));
        productDatabase.add(new Product("P002", "Kopi Sachet", 5000));
        productDatabase.add(new Product("P003", "Roti Isi", 8000));
        productDatabase.add(new Product("P004", "Snack Keripik", 6000));
        productDatabase.add(new Product("P005", "Minuman Botol", 12000));
    }

    /**
     * Membangun semua komponen Swing.
     */
    private void initUI() {
        setTitle("POIN Off-Sales - Java Swing");
        setSize(950, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu helpMenu = new JMenu("Help");
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        // Main Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createLeftPanel(), createRightPanel());
        splitPane.setDividerLocation(420);
        add(splitPane, BorderLayout.CENTER);
    }

    /**
     * Mengisi tabel produk dari productDatabase.
     */
    private void populateProductTable() {
        productModel.setRowCount(0); // Kosongkan tabel dulu
        for (Product p : productDatabase) {
            productModel.addRow(new Object[] {
                    p.getId(),
                    p.getNama(),
                    p.getHarga()
            });
        }
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        leftPanel.add(new JLabel("Produk"), BorderLayout.NORTH);

        String[] productColumns = { "ID", "Nama Produk", "Harga (Rp)" };
        productModel = new DefaultTableModel(null, productColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(productModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leftPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);

        // Panel Bawah (Info Pilihan & Tombol Add)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        selectedProductLabel = new JLabel("Dipilih: ");
        bottomPanel.add(selectedProductLabel, BorderLayout.NORTH);

        JPanel addToCartPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addToCartPanel.add(new JLabel("Qty:"));
        qtyField = new JTextField("1", 3);
        addToCartPanel.add(qtyField);
        addToCartButton = new JButton("Add To Cart");
        addToCartPanel.add(addToCartButton);
        bottomPanel.add(addToCartPanel, BorderLayout.CENTER);

        leftPanel.add(bottomPanel, BorderLayout.SOUTH);

        // --- Event Listener Tabel Produk ---
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && productTable.getSelectedRow() != -1) {
                // Ambil produk dari database berdasarkan baris yang dipilih
                int selectedRow = productTable.getSelectedRow();
                currentlySelectedProduct = productDatabase.get(selectedRow);
                updateSelectedProductLabel();
            }
        });

        // --- Event Listener Tombol "Add To Cart" ---
        addToCartButton.addActionListener(e -> addProductToCart());

        return leftPanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel.setMinimumSize(new Dimension(450, 500));

        // Panel Keranjang (Atas)
        JPanel cartPanel = new JPanel(new BorderLayout(5, 5));
        cartPanel.add(new JLabel("Keranjang"), BorderLayout.NORTH);

        String[] cartColumns = { "ID", "Nama Produk", "Qty", "Harga", "Subtotal" };
        cartModel = new DefaultTableModel(null, cartColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        cartTable = new JTable(cartModel);
        cartPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);
        cartPanel.setPreferredSize(new Dimension(450, 200));

        // Panel Checkout (Total, Poin, Tombol)
        JPanel checkoutPanel = new JPanel();
        checkoutPanel.setLayout(new BoxLayout(checkoutPanel, BoxLayout.Y_AXIS));
        checkoutPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        totalLabel = new JLabel("Total: Rp0,00");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        pointsLabel = new JLabel("Points: 0");
        pointsLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        pointsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        checkoutButton = new JButton("Checkout");
        printButton = new JButton("Cetak");
        buttonPanel.add(checkoutButton);
        buttonPanel.add(printButton);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        checkoutPanel.add(totalLabel);
        checkoutPanel.add(pointsLabel);
        checkoutPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spasi
        checkoutPanel.add(buttonPanel);

        cartPanel.add(checkoutPanel, BorderLayout.SOUTH);

        // Panel Struk (Bawah)
        JPanel receiptPanel = new JPanel(new BorderLayout(5, 5));
        receiptPanel.add(new JLabel("Struk:"), BorderLayout.NORTH);
        receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        receiptArea.setBorder(BorderFactory.createEtchedBorder());
        receiptPanel.add(new JScrollPane(receiptArea), BorderLayout.CENTER);

        // Gabungkan Panel Keranjang dan Struk
        JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, cartPanel, receiptPanel);
        rightSplitPane.setDividerLocation(320);
        rightPanel.add(rightSplitPane, BorderLayout.CENTER);

        // --- Event Listener Tombol Kanan ---
        checkoutButton.addActionListener(e -> performCheckout());
        printButton.addActionListener(e -> performPrint());

        return rightPanel;
    }

    // --- METODE LOGIKA VIEW ---

    private void updateSelectedProductLabel() {
        if (currentlySelectedProduct != null) {
            selectedProductLabel.setText("Dipilih: " + currentlySelectedProduct.getId() +
                    " - " + currentlySelectedProduct.getNama() +
                    " (" + currentlySelectedProduct.getFormattedHarga() + ")");
        } else {
            selectedProductLabel.setText("Dipilih: ");
        }
    }

    /**
     * MENGGUNAKAN class Cart untuk menambah item.
     * Lalu memperbarui tampilan.
     */
    private void addProductToCart() {
        if (currentlySelectedProduct == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih produk terlebih dahulu.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int qty = Integer.parseInt(qtyField.getText());
            if (qty <= 0)
                throw new NumberFormatException();

            // Delegasikan logika penambahan ke class Cart
            cart.addItem(currentlySelectedProduct, qty);

            // Perbarui tampilan berdasarkan data dari Cart
            updateCartTable();
            updateTotalAndPoints();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Kuantitas (Qty) harus berupa angka positif.", "Error Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Menggambar ulang tabel keranjang berdasarkan data terbaru dari
     * cart.getItems().
     */
    private void updateCartTable() {
        cartModel.setRowCount(0); // Kosongkan tabel

        for (CartItem item : cart.getItems()) {
            Product p = item.getProduct();
            cartModel.addRow(new Object[] {
                    p.getId(),
                    p.getNama(),
                    item.getQuantity(),
                    p.getHarga(),
                    item.getSubtotal()
            });
        }
    }

    /**
     * MENGGUNAKAN class Cart untuk mendapatkan total & poin terbaru.
     */
    private void updateTotalAndPoints() {
        double currentTotal = cart.getTotal();
        int currentPoints = cart.getPoints();

        totalLabel.setText("Total: " + currencyFormatter.format(currentTotal));
        pointsLabel.setText("Points: " + currentPoints);
    }

    /**
     * MENGGUNAKAN ReceiptFormatter dan Cart untuk checkout.
     */
    private void performCheckout() {
        if (cart.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang masih kosong.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 1. Delegasikan pembuatan struk ke ReceiptFormatter
        String receiptText = receiptFormatter.generateReceiptText(cart);

        // 2. Tampilkan struk
        receiptArea.setText(receiptText);

        // 3. Delegasikan pengosongan keranjang ke Cart
        cart.clearCart();

        // 4. Perbarui tampilan
        updateCartTable();
        updateTotalAndPoints();
    }

    /**
     * Logika untuk tombol "Cetak".
     */
    private void performPrint() {
        try {
            if (receiptArea.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tidak ada struk untuk dicetak.", "Info",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            // Ini akan memunculkan dialog print sistem
            receiptArea.print();
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(this, "Error saat mencetak: " + ex.getMessage(), "Print Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}