import com.sun.javafx.collections.MappingChange;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
public class CalendarTest {
}
class Event implements Comparable<Event> {
    String name;
    String location;
    Date date;

    public Event(String name, String location, Date date) {
        this.name=name;
        this.location=location;
        this.date=date;
    }

    @Override
    public int compareTo(Event o) {
        int value = Long.compare(date.getTime(), o.date.getTime());
        if(value == 0 )
            value=name.compareTo(o.name);
        return value;
    }
    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("dd MMM, YYY HH:mm");
        String dateFormat = df.format(date);
        return String.format("%s at %s, %s", dateFormat, location, name);
    }
}
class EventCalendar {
  int year;
  Map<Integer,Integer> monthlyEvents;
  Map<String, TreeSet<Event>> events;

  public EventCalendar(int year) {
      this.year=year;
      monthlyEvents = new HashMap<>();
      events = new HashMap<>();
  }
  public void addEvent(String name, String location, Date date) throws WrongDateException {
      if(date.getYear() != (year-1900))
          throw new WrongDateException(date);
      monthlyEvents.computeIfPresent(date.getMonth(), (key,value) -> value++);
      monthlyEvents.putIfAbsent(date.getMonth(),1);
      String time = String.format("%d %d", date.getDate(), date.getMonth());
      events.computeIfPresent(time, (key,value)-> {
          value.add(new Event(name,location, date));
          return value;
      });
      events.computeIfAbsent(time, (key) -> {
          TreeSet<Event> eventTreeSet = new TreeSet<>();
          eventTreeSet.add(new Event(name, location, date));
          return eventTreeSet;
      });
  }
  public void listEvents(Date date) {
      String time = String.format("%d %d", date.getDate(), date.getMonth());
      TreeSet<Event> eventTreeSet = events.get(time);
      if(eventTreeSet==null)
          System.out.println("No events on this day!");
      else {
          eventTreeSet.stream().forEach(System.out::println);
      }
  }
  public void lisByMonth() {
      for (int i=0; i<12; i++ ){
          if(monthlyEvents.containsKey(i))
              System.out.printf("%d : %d\n", i+1, monthlyEvents.get(i));
          else System.out.printf("%d : %d\n", i+1, 0);
      }
  }

}
class WrongDateException extends Exception {
    public WrongDateException(Date date){
        super(String.format("Wrong date: %s", date.toString().replaceAll("GMT","UTC")));
    }
}
