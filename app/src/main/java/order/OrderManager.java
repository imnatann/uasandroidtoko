package order;

import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private static List<OrderItem> orderItems = new ArrayList<>();

    public static void addOrder(String itemName, int price) {
        OrderItem orderItem = new OrderItem(itemName, price);
        orderItems.add(orderItem);
    }

    public static List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public static void clearOrders() {
        orderItems.clear();
    }
}
