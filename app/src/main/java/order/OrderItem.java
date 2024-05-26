package order;

public class OrderItem {
    private String itemName;
    private int price;

    public OrderItem(String itemName, int price) {
        this.itemName = itemName;
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public int getPrice() {
        return price;
    }
}
