import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class ChatSystemTest {
    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException, InvocationTargetException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr.addUser(jin.next());
                if ( k == 1 ) cr.removeUser(jin.next());
                if ( k == 2 ) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if ( n == 0 ) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr2.addUser(jin.next());
                if ( k == 1 ) cr2.removeUser(jin.next());
                if ( k == 2 ) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if ( k == 1 ) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while ( true ) {
                String cmd = jin.next();
                if ( cmd.equals("stop") ) break;
                if ( cmd.equals("print") ) {
                    System.out.println(cs.getRoom(jin.next())+"\n");continue;
                }
                for ( Method m : mts ) {
                    if ( m.getName().equals(cmd) ) {
                        String params[] = new String[m.getParameterTypes().length];
                        for ( int i = 0 ; i < params.length ; ++i ) params[i] = jin.next();
                        m.invoke(cs,params);
                    }
                }
            }
        }
    }
}
class ChatRoom implements Comparable {
    private String name;
    private TreeSet<String> users;

    public ChatRoom(String name) {
        this.name=name;
        users = new TreeSet<>();
    }
    public String getName() {
        return name;
    }
    public void addUser(String username) {
        users.add(username);
    }
    public void removeUser(String username) {
        users.remove(username);
    }
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name).append("\n");
        if(users.isEmpty()) stringBuilder.append("EMPTY\n");
        else {
            users.stream().map(i->i+"\n").forEach(stringBuilder::append);;
        }
        return stringBuilder.toString();
    }
    public boolean hasUser(String username) {
        return users.contains(username);
    }
    public int numUsers() {
        return users.size();
    }
    @Override
    public int compareTo(Object object) {
        return Integer.compare(numUsers(), ((ChatRoom)object).numUsers());
    }
}
class ChatSystem {
    private TreeMap<String, ChatRoom> rooms;
    private HashSet<String> users;

    public ChatSystem()  {
        rooms = new TreeMap<>();
        users = new HashSet<>();
    }
    public void addRoom(String roomname) {
        rooms.put(roomname, new ChatRoom(roomname));
    }
    public void removeRoom(String roomname) {
        rooms.remove(roomname);
    }
    public ChatRoom getRoom(String roomname) throws NoSuchRoomException {
        validateRoom(roomname);
        return rooms.get(roomname);
    }
    private void validateRoom(String roomname) throws NoSuchRoomException{
        if(!rooms.containsKey(roomname)) throw new NoSuchRoomException(roomname);
    }
    public void register(String username) {
        users.add(username);
        if(rooms.isEmpty()) return;
        ChatRoom added = rooms.values().stream().min(ChatRoom::compareTo).orElse(null);
        if(added==null) return;
        rooms.get(added.getName()).addUser(username);
    }
    public void registerAndJoin(String username, String roomname) throws NoSuchRoomException {
        validateRoom(roomname);
        users.add(username);
        rooms.get(roomname).addUser(username);
    }
    private void validateUser(String username) throws NoSuchUserException{
        if(!users.contains(username))
            throw new NoSuchUserException(username);
    }
    public void joinRoom(String username, String roomname) throws NoSuchRoomException, NoSuchUserException {
        validateRoom(roomname);
        validateUser(username);
        rooms.get(roomname).addUser(username);
    }
    public void leaveRoom(String username, String roomname) throws NoSuchUserException, NoSuchRoomException {
        validateUser(username);
        validateRoom(roomname);
        rooms.get(roomname).removeUser(username);
    }
    public void followFriend(String username, String friendusername) throws NoSuchUserException {
        validateUser(username);
        validateUser(username);
        rooms.values().stream().forEach(i-> {
            if(i.hasUser(friendusername))
                i.addUser(username);
        });
    }

}
class NoSuchRoomException extends Exception {
    public NoSuchRoomException(String roomname){}
}
class NoSuchUserException extends Exception {
    public NoSuchUserException(String username) {}
}