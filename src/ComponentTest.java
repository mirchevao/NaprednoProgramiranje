import java.util.*;

public class ComponentTest {
}
class Component implements Comparable<Component> {
    String color;
    int weight;
    Set<Component> componentSet;
    public Component(String color, int weight) {
        super();
        this.color=color;
        this.weight=weight;
        componentSet = new TreeSet<>();
    }
    public String getColor(){ return color; }
    public int getWeight(){ return weight; }
    void addComponent(Component component) {
        componentSet.add(component);
    }
    public void changeColor(int weight, String color) {
        if(this.weight < weight) {
            this.color=color;
        }
        componentSet.forEach(component -> component.changeColor(weight,color));

    }


    @Override
    public int compareTo(Component o) {
        return Comparator.comparing(Component::getWeight).thenComparing(Component::getColor).compare(this,o);
    }
    public String format(String crti) {
        String s = String.format("%s%d:%s\n", crti, weight, color);
        for (Component component : componentSet) {
            s+=component.format(crti+"---");
        }
        return s;
    }
    @Override
    public String toString() {
        return format("");
    }
}
class Window {
    String name;
    Map<Integer, Component>componentMap;
    public Window(String name) {
        this.name=name;
        componentMap = new TreeMap<>();
    }
    public void addComponent(int position, Component component) throws InvalidPositionException {
        if(componentMap.containsKey(position))
            throw new InvalidPositionException(position);
        componentMap.put(position,component);
    }
    public void changeColor(int weight, String color) {
        componentMap.values().forEach(component -> component.changeColor(weight,color));
    }
    public void switchComponents(int position1, int position2) {
        Component tmp = componentMap.get(position1);
        componentMap.put(position1, componentMap.get(position2));
        componentMap.put(position2,tmp);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("WINDOW "+name+"\n");
        componentMap.entrySet().forEach(entry -> sb.append(entry.getKey()+ ":" + entry.getValue()));
        return sb.toString();
    }
}
class InvalidPositionException extends Exception{
    public InvalidPositionException(int pos) {
        super("Invalid position "+pos+", alredy taken!");
    }
}