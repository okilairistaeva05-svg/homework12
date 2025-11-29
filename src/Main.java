import java.util.*;

// Абстрактный класс для пользователей
abstract class User {
    protected int id;
    protected String name;
    protected String email;
    protected String address;
    protected String phone;

    public User(int id, String name, String email, String address, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public abstract void login();
    public abstract void updateData(String name, String email, String address, String phone);
    public String getAddress() {
        return address;
    }
}

// Клиент
class Customer extends User {
    private List<Order> orderHistory = new ArrayList<>();
    private int loyaltyPoints;

    public Customer(int id, String name, String email, String address, String phone) {
        super(id, name, email, address, phone);
        this.loyaltyPoints = 0;
    }

    @Override
    public void login() {
        System.out.println(name + " logged in as Customer.");
    }

    @Override
    public void updateData(String name, String email, String address, String phone) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        System.out.println("Customer data updated.");
    }

    public void addOrder(Order order) {
        orderHistory.add(order);
        loyaltyPoints += order.getTotalAmount() / 10; // Начисляем бонусные баллы
    }

    public List<Order> getOrderHistory() {
        return orderHistory;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }
}

// Администратор
class Admin extends User {
    public Admin(int id, String name, String email, String address, String phone) {
        super(id, name, email, address, phone);
    }

    @Override
    public void login() {
        System.out.println(name + " logged in as Admin.");
    }

    @Override
    public void updateData(String name, String email, String address, String phone) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        System.out.println("Admin data updated.");
    }

    public void logAction(String action) {
        System.out.println("Admin action logged: " + action);
    }
}

// Товар
class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String category;

    public Product(int id, String name, String description, double price, int quantity, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    public void update(String name, String description, double price, int quantity, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name + " (" + category + ") - $" + price;
    }
}

// Платеж
class Payment {
    private int id;
    private String type; // карта, кошелек
    private double amount;
    private String status;
    private Date date;

    public Payment(int id, String type, double amount) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.status = "Pending";
        this.date = new Date();
    }

    public void process() {
        status = "Completed";
        System.out.println("Payment of $" + amount + " processed via " + type);
    }

    public void refund() {
        status = "Refunded";
        System.out.println("Payment of $" + amount + " refunded.");
    }
}

// Доставка
class Delivery {
    private int id;
    private String address;
    private String status;
    private String courier;

    public Delivery(int id, String address, String courier) {
        this.id = id;
        this.address = address;
        this.courier = courier;
        this.status = "Preparing";
    }

    public void send() {
        status = "In Transit";
        System.out.println("Delivery sent to " + address);
    }

    public void track() {
        System.out.println("Delivery status: " + status);
    }

    public void complete() {
        status = "Delivered";
        System.out.println("Delivery completed.");
    }
}

// Заказ
class Order {
    private int id;
    private Customer customer;
    private List<Product> products;
    private double totalAmount;
    private String status; // оформлен, доставлен, отменён
    private Payment payment;
    private Delivery delivery;

    public Order(int id, Customer customer) {
        this.id = id;
        this.customer = customer;
        this.products = new ArrayList<>();
        this.totalAmount = 0;
        this.status = "Created";
    }

    public void addProduct(Product product) {
        products.add(product);
        totalAmount += product.getPrice();
    }

    public void removeProduct(Product product) {
        if (products.remove(product)) {
            totalAmount -= product.getPrice();
        }
    }

    public void placeOrder(Payment payment, Delivery delivery) {
        this.payment = payment;
        this.delivery = delivery;
        this.status = "Placed";
        System.out.println("Order #" + id + " placed with total $" + totalAmount);
        payment.process();
        delivery.send();
    }

    public void cancelOrder() {
        status = "Cancelled";
        payment.refund();
        System.out.println("Order #" + id + " cancelled.");
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}

// Главный класс с main
public class ECommerceApp {
    public static void main(String[] args) {
        Customer customer = new Customer(1, "Alice", "alice@mail.com", "123 Street", "555-1234");
        Admin admin = new Admin(2, "Bob", "bob@mail.com", "Admin Street", "555-5678");

        customer.login();
        admin.login();

        Product p1 = new Product(1, "Laptop", "Gaming Laptop", 1200, 10, "Electronics");
        Product p2 = new Product(2, "Phone", "Smartphone", 800, 20, "Electronics");

        Order order = new Order(1, customer);
        order.addProduct(p1);
        order.addProduct(p2);

        Payment payment = new Payment(1, "Card", order.getTotalAmount());
        Delivery delivery = new Delivery(1, customer.getAddress(), "DHL");

        order.placeOrder(payment, delivery);
        delivery.track();
        admin.logAction("Created new order #" + order);
        customer.addOrder(order);
        System.out.println("Customer loyalty points: " + customer.getLoyaltyPoints());
    }
}
