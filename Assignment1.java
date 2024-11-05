           Simple E-commerce cart system

Step 1: Define the Product Base Class and Prototype

// Product base class with Prototype pattern
abstract class Product implements Cloneable {
    protected String name;
    protected double price;
    protected boolean available;

    public Product(String name, double price, boolean available) {
        this.name = name;
        this.price = price;
        this.available = available;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public boolean isAvailable() { return available; }

    @Override
    public Product clone() {
        try {
            return (Product) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}

// Example subclass for Electronic products
class ElectronicProduct extends Product {
    public ElectronicProduct(String name, double price, boolean available) {
        super(name, price, available);
    }
}


Step 2: Define the CartItem Class

class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; 
}

    public double getTotalPrice() { return product.getPrice() * quantity; 
}
}

Step 3: Define the Cart Class

import java.util.HashMap;
import java.util.Map;

class Cart {
    private Map<String, CartItem> items = new HashMap<>();

    public void addProduct(Product product, int quantity) {
        if (!product.isAvailable()) {
            System.out.println(product.getName() + " is not available.");
            return;
        }
        CartItem item = items.get(product.getName());
        if (item == null) {
            items.put(product.getName(), new CartItem(product.clone(), quantity));
        } else {
            item.setQuantity(item.getQuantity() + quantity);
        }
    }

    public void updateQuantity(String productName, int quantity) {
        CartItem item = items.get(productName);
        if (item != null) {
            item.setQuantity(quantity);
        }
    }

    public void removeProduct(String productName) {
        items.remove(productName);
    }

    public void displayCart() {
        items.values().forEach(item ->
            System.out.println("You have " + item.getQuantity() + " " + item.getProduct().getName() + "(s) in your cart.")
        );
    }

    public double calculateTotal(DiscountStrategy discountStrategy) {
        double total = items.values().stream().mapToDouble(CartItem::getTotalPrice).sum();
        return discountStrategy.applyDiscount(total);
    }
}

Step 4: Define the Discount Strategy Interface and Implementations

interface DiscountStrategy {
    double applyDiscount(double totalAmount);
}

class PercentageDiscount implements DiscountStrategy {
    private double percentage;

    public PercentageDiscount(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public double applyDiscount(double totalAmount) {
        return totalAmount - (totalAmount * percentage / 100);
    }
}

class BuyOneGetOneFree implements DiscountStrategy {
    @Override
    public double applyDiscount(double totalAmount) {
        // This example assumes each CartItem quantity is eligible for BOGO, for simplicity
        return totalAmount / 2;
    }
}

Step 5: Demonstrate the Use Case with Main Application


public class ECommerceApp {
    public static void main(String[] args) {
        Product laptop = new ElectronicProduct("Laptop", 1000, true);
        Product headphones = new ElectronicProduct("Headphones", 50, true);

        Cart cart = new Cart();

        // Adding products to the cart
        cart.addProduct(laptop, 1);
        cart.addProduct(headphones, 2);

        // Displaying cart items
        cart.displayCart();

        // Updating quantity of an item
        cart.updateQuantity("Headphones", 3);
        cart.displayCart();

        // Calculating total with Percentage Discount
        DiscountStrategy percentageDiscount = new PercentageDiscount(10); // 10% discount
        System.out.println("Total Bill with Percentage Discount: $" + cart.calculateTotal(percentageDiscount));

        // Calculating total with BOGO Discount
        DiscountStrategy bogoDiscount = new BuyOneGetOneFree();
        System.out.println("Total Bill with BOGO Discount: $" + cart.calculateTotal(bogoDiscount));
    }
}



























