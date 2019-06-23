import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BookCollection2 {

}
class Book2 implements Comparable<Book2> {
    private String title;
    private String category;
    private float price;
    public Book2(String title, String category, float price) {
        this.title=title;
        this.price=price;
        this.category=category;
    }
    public String getTitle() { return title;}
    @Override
    public String toString() {
        return String.format("%s %s %.02f", title, category, price);

    }
    public float getPrice() { return price; }

    @Override
    public int compareTo(Book2 o) {
        if(title.equalsIgnoreCase(o.title))
            return Float.compare(price,o.price);
        return title.compareTo(o.title);
    }
}
class BookCollections2 {
    Set<Book> books;
    BookCollections2(Set<Book> books) {
        books = new HashSet<>();
    }
    public void addBook(Book book) {
        books.add(book);
    }
    public void printByCategory(String category) {
        books.stream().filter(book->book.category.equalsIgnoreCase(category)).forEach(System.out::println);
    }
    public List<Book> getCheapestN(int n) {
        return books.stream().sorted(Comparator.comparing(book -> book.price)).collect(Collectors.toList()).subList(0,n);
    }
}