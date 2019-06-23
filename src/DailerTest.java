import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DailerTest {
}
class Contact1 {
    public long moment;
    String name;
    String vreme;
    String tip;
List<Integer> telephoneNumbers;
    public Contact1(String name, List<Integer> telephoneNumbers) {
        this.name=name;
        this.telephoneNumbers=telephoneNumbers;
    }
public long getMoment(){return moment;}
public String getVreme() { return vreme;}

    public String getName() {
        return name;
    }
    public Contact1(String name, long moment, String vreme, String tip ){
        this.name=name;
        this.moment=moment;
        this.vreme=vreme;
        this.tip=tip;
    }
    public Contact1(String name) {
        this.name=name;
    }
    @Override
    public String toString() {
        return String.format("%s %d", name, telephoneNumbers);
    }
    public String getTip() {
        if(tip.equalsIgnoreCase("D"))
            return "D";
        else if(tip.equalsIgnoreCase("M"))
            return "M";
        else if(tip.equalsIgnoreCase("R"))
            return "R";
        return null;
    }
    static Contact1 fromString(String line) {
        String[] parts = line.split("\\s+");
        List<Integer> list = IntStream.range(1, parts.length)
                .mapToObj(i->telFromString(parts[i])).collect(Collectors.toList());
        return new Contact1(parts[0], list);
    }
    static int telFromString(String num) {
        if((num.startsWith("07")))
            return Integer.parseInt(num);
        return 0;
    }
public String getNmae() { return  name; }

}
class Dailer {
    TreeSet<Contact1> contacts;
public Dailer(List<Contact1> contacts) {
    contacts = new ArrayList<>();
}
public void readCalls(IntStream intStream) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream) intStream));
    contacts = (TreeSet<Contact1>) reader.lines().map(Contact1::fromString).collect(Collectors.toList());
}
public void writeContactCalls(OutputStream outputStream) {
    PrintWriter writer = new PrintWriter(outputStream);
    contacts.stream().sorted(Comparator.comparing(Contact::).reversed()).forEach(i->writer.println(String.format()));
    writer.flush();
}

public void writeCalls (OutputStream outputStream, String type) {
    contacts.stream().filter(i->i.tip.equalsIgnoreCase(type)).forEach(System.out::println);
}
public void writeContactCallsDetails(OutputStream outputStream, Contact1 contact) {
    PrintWriter writer = new PrintWriter(outputStream);
    contacts.stream().sorted(Comparator.comparing(Contact1::getMoment).reversed()).forEach(i->writer.println(String.format("%-20s%-15d%-10d%-10s", contact.getName(), contact.telephoneNumbers, contact.vreme, contact.vreme)));
}
public void makeCall(String phoneNumber, Long timeStamp, String duration, String type) {
  // Contact1 contact1 = new Contact1(phoneNumber,timeStamp, duration, type);
   boolean ifmatch1 = contacts.stream().anyMatch(i->i.name.equalsIgnoreCase(phoneNumber));
   boolean ifmatch2 = contacts.stream().anyMatch(i->i.vreme.equalsIgnoreCase(duration));
   boolean ifmatch3 = contacts.stream().anyMatch(i->i.tip.equalsIgnoreCase(type));
   if(ifmatch1 && ifmatch2 && ifmatch3)
       makeCall(phoneNumber,timeStamp,duration,type);
   Contact1 Unknown = new Contact1("Unknown");
    }
}





class WrongCallFormat extends Exception  {
    WrongCallFormat(){}
}
