import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Avtomobili {
}
class Car2 {
    String proizvoditel;
    String model;
    int cena;
    float mokjnost;
    Car2(String proizvoditel, String model, int cena, float mokjnost) {
        this.proizvoditel=proizvoditel;
        this.model=model;
        this.cena=cena;
        this.mokjnost=mokjnost;
    }
    public String getModel() { return model;}
    Car2(){}
    @Override
    public String toString() {
        return String.format("%s %s %d %f", proizvoditel, model, cena, mokjnost);
    }
}
class CarCollection2 {
    List<Car2> cars;
    CarCollection2(List<Car2> cars) {
        cars = new ArrayList<>();
    }
    public void addCar(Car2 car) {
        cars.add(car);
    }
    public void sortByPrice(boolean ascending) {
      if(!ascending) {
          compareByPrice = compareByPrice.reversed();
      }
      Collections.sort(cars, compareByPrice);
    }
    private Comparator<Car2> compareByPrice = (car1, car2)-> {
        if(car1.cena == car2.cena)
            return ((int)car1.mokjnost-(int)car2.mokjnost);
        else return car1.cena-car2.cena;
    };
    public List<Car2> filterByManufacturer(String manufacturer) {
     return cars.stream().filter(i->i.proizvoditel.equalsIgnoreCase(manufacturer)).sorted(Comparator.comparing(Car2::getModel)).collect(Collectors.toList());
    }
    public List<Car2> getList() {
        return cars;
    }
}
