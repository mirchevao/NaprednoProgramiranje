
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CarTest {
}
class Car  {
    String manifacturer;
    String model;
    int price;
    float power;
    public Car(String manifacturer, String model, int price, float power ) {
        this.manifacturer=manifacturer;
        this.model=model;
        this.price=price;
        this.power=power;
    }
    public String getManifacturer() { return manifacturer; }
    public String getModel() { return model; }
    public int getPrice() { return price; }
    public float getPower() { return power; }
    @Override
    public String toString() {
        return String.format("%s %s (%.0fKW) %d", manifacturer, model, power, price);
    }

}
class CarCollection {
    List<Car> cars;
    CarCollection() {
        cars = new ArrayList<>();
    }
    public void addCar (Car car) {
        cars.add(car);
    }
    public List<Car> filterByManufacturer(String manufacutrer) {
        return cars.stream().filter(car->car.manifacturer.equalsIgnoreCase(manufacutrer)).sorted(Comparator.comparing(Car::getModel)).collect(Collectors.toList());
    }
    private Comparator<Car> compareByPrice = (car1,car2) -> {
        if(car1.price == car2.price) {
            return (int)(car1.power-car2.power);
        }
        else return car1.price-car2.price;
    };
    public void sortByPrice(boolean ascending) {
        if(!ascending)
            compareByPrice = compareByPrice.reversed();
        Collections.sort(cars, compareByPrice);
    }
    public List<Car> getList() {
        return cars;
    }
}
