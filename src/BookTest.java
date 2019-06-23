import java.lang.reflect.Array;
import java.security.PublicKey;
import java.util.*;
import java.util.stream.Collectors;

public class BookTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        BookCollection booksCollection = new BookCollection();
        Set<String> categories = fillCollection(scanner, booksCollection);
        System.out.println("=== PRINT BY CATEGORY ===");
        for (String category : categories) {
            System.out.println("CATEGORY: " + category);
            booksCollection.printByCategory(category);
        }
        System.out.println("=== TOP N BY PRICE ===");
        print(booksCollection.getCheapestN(n));
    }

    static void print(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    static TreeSet<String> fillCollection(Scanner scanner,
                                          BookCollection collection) {
        TreeSet<String> categories = new TreeSet<String>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            Book book = new Book(parts[0], parts[1], Float.parseFloat(parts[2]));
            collection.addBook(book);
            categories.add(parts[1]);
        }
        return categories;
    }
}
class Book implements Comparable<Book>{
    String title;
    String category;
    float price;

    public Book(String title, String category, float price) {
        this.title=title;
        this.category=category;
        this.price=price;
    }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public float getPrice() { return  price; }

    @Override
    public int compareTo(Book o) {
        if(title.equalsIgnoreCase(o.title))
            return Float.compare(price, o.price);
        return title.compareTo(o.title);
    }
    @Override
    public String toString() {
        return String.format("%s (%s) %.2f", title, category, price);
    }
}
class BookCollection {
    List<Book> bookList;
    public BookCollection(List<Book> bookList){
        bookList = new ArrayList<>();
    }

    public BookCollection() {

    }

    public void addBook(Book book) {
        bookList.add(book);
    }
    public void printByCategory(String category) {
        bookList.stream().filter(book -> book.category.equalsIgnoreCase(category)).forEach(System.out::println);
    }
    public List<Book> getCheapestN(int n) {
        return bookList.stream().sorted(Comparator.comparing(book -> book.price)).collect(Collectors.toList()).subList(0,n);
    }
}
