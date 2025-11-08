import java.util.*;
import java.io.*;


public class UserListManager {
    public static ArrayList<User> userList;
    public static String filename = "userList";

    public static ArrayList<User> main() {
        ArrayList<User> users = FileManager.loadUserList(filename);
        if(users == null || users.size() == 0){
            users = new ArrayList<User>();
            users.add(new Admin("Admin", "000", "Admin" , "Admin" ,"Admin"));
            FileManager.saveUserList(users, filename);
        
        }
      
        return users;
   
    }
    public static void add( User user){
        FileManager.loadUserList(filename);
        if (userList.contains(user)) {
            throw new IllegalArgumentException("User already exists in the list");
        }
        
        userList.add(user);
        FileManager.saveUserList(userList, "userList");
    }
    public static void remove( User user){
        userList.remove(user);
        FileManager.saveUserList(userList, "userList");
    }
    public static void edit ( int indexInt, User newUser){
    
        userList.set(indexInt, newUser);
        FileManager.saveUserList(userList, "userList");
    }

}

class FileManager { 

    
    public static void saveUserList(ArrayList<User> userList, String filename){
        try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename));
            ObjectOutputStream oos = new ObjectOutputStream(bos);){
            oos.writeObject(userList);
            oos.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("readable_"+filename));){
            for(User user : userList){
                writer.write(user.toString());
                writer.newLine();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public static ArrayList<User> loadUserList(String filename){
        ArrayList<User> userList = new ArrayList<User>();
        java.io.File f = new java.io.File(filename);
        // If the serialized file doesn't exist yet, return an empty list instead of throwing
        if(!f.exists()){
            return userList;
        }
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
            ObjectInputStream ois = new ObjectInputStream(bis);){
            userList = (ArrayList<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return userList;
    }
    
}
