import java.util.*;

public class User implements java.io.Serializable{ 
    public String name;
    public String contact;
    public String email;
    public String userName;
    public String password;
    public String permission; 
    

    User(String name, String contact, String email, String userName, String password){
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.userName = userName;
        this.password = password;
        
    }
    public String getName() {
        return this.name;
    }
    public String getEmail(){
        return this.email;
    }
    public String getContact(){
        return this.contact;
    }
    @ Override
    public String toString(){
        return "Name:"+ this.name + " | Contact:"+ this.contact + " | Email:"+ this.email + " | Username:"+ this.userName +" | Password "+ this.password +" | Permission:"+ this.permission;
}
}
class Admin extends User {
    Admin(String name, String contact, String email, String userName, String password){
        super(name, contact, email, userName, password);
        this.permission = "Admin";
    }
    

}
class Client extends User{
    public ArrayList<Transaction> transactionHistory;
    Client(String name, String contact, String email, String userName, String password){
        super(name, contact, email, userName, password);
        this.permission = "Client";
        
    }
    
}
