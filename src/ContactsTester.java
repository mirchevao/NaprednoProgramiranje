import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;
import java.time.Year;


abstract class Contact {
    private String date;     //YYYY-MM-DD
    private final int day;
    private final int month;
    private final int year;

    public Contact(String date){
        this.date = date;
        year = Integer.parseInt(date.substring(0,4));
        month = Integer.parseInt(date.substring(5,7));
        day = Integer.parseInt(date.substring(8,10));
    }

    public boolean isNewerThan(Contact c){
        if (year > c.year)
            return true;
        if (year < c.year)
            return false;
        if (month > c.month)
            return true;
        if (month < c.month)
            return false;
        if (day > c.day)
            return true;
        return false;
    }

    public abstract String getType();
}

class EmailContact extends Contact {
    private String email;

    public EmailContact(String date, String email) {
        super(date);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getType() {
        return "Email";
    }
}

class PhoneContact extends Contact {
    private String phone;    //Сите телефонски броеви се во формат 07X/YYY-ZZZ каде што X има вредност {0,1,2,5,6,7,8}
    enum Operator {VIP, ONE, TMOBILE}

    public PhoneContact(String date, String phone) {
        super(date);
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public Operator getOperator(){
        //070, 071, 072 – TMOBILE, 075,076 – ONE, 077, 078 – VIP

        if (phone.charAt(2) == '0' || phone.charAt(2) == '1' || phone.charAt(2) == '2')
            return Operator.TMOBILE;
        if (phone.charAt(2) == '5' || phone.charAt(2) == '6')
            return Operator.ONE;
        return Operator.VIP;
    }

    @Override
    public String getType() {
        return "Phone";
    }
}

class Student {
    private String firstName;
    private String lastName;
    private String city;
    private int age;
    private long index;
    Contact[] contacts;
    private int contactsNumber;
    private int emailContacts;
    private int phoneContacts;

    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        contactsNumber = 0;
        emailContacts = 0;
        phoneContacts = 0;
    }

    public void addEmailContact(String date, String email){
        Contact[] tmp = new Contact[contactsNumber+1];
        for (int i=0; i<contactsNumber; i++){
            tmp[i] = contacts[i];
        }
        tmp[contactsNumber++] = new EmailContact(date,email);
        contacts = tmp;
        emailContacts++;
    }

    public void addPhoneContact(String date, String phone){
        Contact[] tmp = new Contact[contactsNumber + 1];
        for (int i=0; i<contactsNumber; i++){
            tmp[i] = contacts[i];
        }
        tmp[contactsNumber++] = new PhoneContact(date,phone);
        contacts = tmp;
        phoneContacts++;
    }

    public Contact[] getEmailContacts(){
        Contact[] tmp = new EmailContact[emailContacts];
        int counter = 0;
        for (Contact c:contacts) {
            if (c.getType().equals("Email")){
                tmp[counter++] = c;
            }
        }
        return tmp;
    }

    public Contact[] getPhoneContacts(){
        Contact[] tmp = new PhoneContact[phoneContacts];
        int counter = 0;
        for (Contact c:contacts) {
            if (c.getType().equals("Phone")){
                tmp[counter++] = c;
            }
        }
        return tmp;
    }

    public Contact getLatestContact(){
        int newestIndex = 0;
        for (int i=1; i<contactsNumber; i++){
            if (contacts[i].isNewerThan(contacts[newestIndex])){
                newestIndex = i;
            }
        }
        return contacts[newestIndex];
    }

    public String getFullName() {
        return (firstName + " " + lastName).toUpperCase();
    }

    public String getCity() {
        return city;
    }

    public long getIndex() {
        return index;
    }

    @Override
    public String toString() {
        //JSON reprezentacija

        StringBuilder sb = new StringBuilder();
        sb.append("{\"ime\":\"").append(firstName).append("\", \"prezime\":\"").append(lastName);
        sb.append("\", \"vozrast\":").append(age).append(", \"grad\":\"").append(city);
        sb.append("\", \"indeks\":").append(index).append(", \"telefonskiKontakti\":[");

        PhoneContact[] pc = (PhoneContact[]) getPhoneContacts();
        for (int i=0; i<phoneContacts-1; i++){
            sb.append("\"").append(pc[i].getPhone()).append("\", ");
        }
        if (phoneContacts>0)
            sb.append("\"").append(pc[phoneContacts-1].getPhone()).append("\"");
        sb.append("], ");

        sb.append("\"emailKontakti\":[");
        EmailContact[] ec = (EmailContact[]) getEmailContacts();
        for (int i=0; i<emailContacts-1; i++){
            sb.append("\"").append(ec[i].getEmail()).append("\", ");
        }
        if (emailContacts>0)
            sb.append("\"").append(ec[emailContacts-1].getEmail()).append("\"");
        sb.append("]}");

        return sb.toString();
    }

    public int getContactsNumber() {
        return contactsNumber;
    }

}

class Faculty {

    private String name;
    private Student[] students;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = students;
    }

    public int countStudentsFromCity(String cityName){
        int count = 0;
        for (Student s:students) {
            if (cityName.equals(s.getCity()))
                count++;
        }
        return count;
    }

    public Student getStudent(long index){
        int ind = -1;
        for (int i=0; i<students.length; i++){
            if (students[i].getIndex() == index){
                ind = i;
                break;
            }
        }
        return students[ind];
    }

    public double getAverageNumberOfContacts(){
        double sum = 0;
        for (Student s:students) {
            sum += s.getContactsNumber();
        }
        return sum/students.length;
    }

    public Student getStudentWithMostContacts(){
        int maxIndex = 0;
        int maxContactsNumber = students[0].contacts.length;
        for (int i=1; i<students.length; i++){
            if (students[i].contacts.length > maxContactsNumber){
                maxContactsNumber = students[i].contacts.length;
                maxIndex = i;
            }
            else if (students[i].contacts.length == maxContactsNumber){
                if (students[i].getIndex() > students[maxIndex].getIndex()){
                    maxContactsNumber = students[i].contacts.length;
                    maxIndex = i;
                }
            }
        }
        return  students[maxIndex];
    }

    @Override
    public String toString() {
        //JSON reprezentacija

        StringBuilder sb = new StringBuilder();
        sb.append("{\"fakultet\":\"").append(name).append("\", \"studenti\":[");
        for (int i=0; i<students.length-1; i++) {
            sb.append(students[i].toString()).append(", ");
        }
        sb.append(students[students.length-1].toString()).append("]}");
        return sb.toString();
    }
}

public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}