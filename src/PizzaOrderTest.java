import java.util.Arrays;
import java.util.Scanner;

class InvalidExtraTypeException extends Exception {
    InvalidExtraTypeException(){}
}
class InvalidPizzaTypeException extends Exception {
    InvalidPizzaTypeException(){}
}
class ItemOutOfStockException extends Exception {
    ItemOutOfStockException(Item item) {
        super(item.toString());
    }
}
class OrderLockedException extends Exception {
    OrderLockedException(){}
}
class ArrayIndexOutOfBoundsException extends Exception {
    ArrayIndexOutOfBoundsException(int index) {
        super(Integer.toString(index));
    }
}
class EmptyOrder extends Exception {
    EmptyOrder() {
        super("EmptyOrder");
    }
}

public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}
interface Item {
    int getPrice();
    String getType();

}
class ExtraItem implements Item {
    String type; // "coke or ketchup"
    public ExtraItem(String type) throws InvalidExtraTypeException {
        if(!(type.equals("Coke") || type.equals("Ketchup")))
            throw new InvalidExtraTypeException();
        else this.type=type;
    }
    public int getPrice() {
        if(type.equals("Ketchup")) return 3;
        else if(type.equals("Coke")) return  5;
        else return 0;
    }
    public String getType() { return type; }
}
class PizzaItem implements Item {
    String type; // standard, pepperoni, vegetarian
    public PizzaItem(String type) throws InvalidPizzaTypeException{
        if(!(type.equals("Standard") || type.equals("Pepperoni") || type.equals("Vegetarian")))
            throw new InvalidPizzaTypeException();
        else this.type=type;
    }
    public int getPrice() {
        if(type.equals("Standard")) return 10;
        else if(type.equals("Pepperoni")) return 12;
        else if(type.equals("Vegetarian")) return 8;
        else return 0;
    }
    public String getType() { return type; }
}
class Order {
    Item [] items;
    int [] amount;
    boolean locked;

    public Order() {
        items=null;
        amount=null;
    }
    public void addItem(Item item, int count) throws ItemOutOfStockException, OrderLockedException {
        if(locked)
            throw new OrderLockedException();
        if(count > 10)
            throw new ItemOutOfStockException(item);
        boolean flag = false;
        int index=0;
        if(items!=null) {
            for(int i = 0; i<items.length; i++) {
                if(items[i].getType().equals(item.getType())) {
                    index=i;
                    flag=true;
                    break;
                }
            }
            if(flag)
                amount[index] = count;
            else {
                items = Arrays.copyOf(items,items.length+1);
                items[items.length-1]=item;
                amount=Arrays.copyOf(amount,amount.length+1);
                amount[amount.length-1]=count;
            }
        }
        else {
            items = new Item[1];
            amount=new int[1];
            items[0] = item;
            amount[0] = count;
        }
    }
    public int getPrice() {
        int sum=0;
        for(int i=0; i<items.length; i++) {
            sum+=items[i].getPrice()*amount[i];
        }
        return sum;
    }
    public void displayOrder() {
        String print = new String();
        for(int i = 0; i<items.length; i++) {
            print+=(String.format("%3d.%-15sx%2d%5d$\n", i+1, items[i].getType(), amount[i],items[i].getPrice()*amount[i]));

        }
        print+=(String.format("%-22s%5d$\n","Total:", getPrice()));
        System.out.println(print.toString());
    }
    public void removeItem(int index) throws ArrayIndexOutOfBoundsException, OrderLockedException {
        if(locked)
            throw new OrderLockedException();
        else {
            if(index < 0 || index > (items.length-1))
                throw new ArrayIndexOutOfBoundsException(index);
            else {
                if(items.length!=0) {
                    Item[] tmp = new Item[items.length-1];
                    int [] tmpInt = new int[amount.length-1];
                    for(int i=0; i<index; i++) {
                        tmp[i] = items[i];
                        tmpInt[i] = amount[i];
                    }
                    for(int i=0; i<items.length-1; i++) {
                        tmp[i]=items[i+1];
                        tmpInt[i]=amount[i];
                    }
                    items=tmp;
                    amount=tmpInt;
                }
            }
        }
    }
    public void lock() throws EmptyOrder {
        if(items==null) throw new EmptyOrder();
        else locked = true;
    }
}