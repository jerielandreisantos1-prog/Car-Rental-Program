import java.time.*;
import java.time.format.DateTimeFormatter;

public class Transaction {
    public Status status;
    public int dateStart;
    public int  dateEnd; 
    public double price; 
    public String transactionID;
    public String vehicleType;
    Transaction(String dateStart, String dateEnd,String vehicleType, double price, String transactionID){
        this.status = new Status(dateStart, dateEnd);
        this.price = price;
        this.transactionID = transactionID;
        this.vehicleType = vehicleType;
    }
    
    

    
}
class Status {
    public String state;
    public Boolean successfulPayment;
    public Boolean dateStarted;
    public Boolean dateCompleted;
    public LocalDate startDate;
    public LocalDate endDate;
    public LocalDate currentDate;
    Status(String dateStart, String dateEnd){
        this.state = "Pending";
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate startDate = LocalDate.parse(dateStart, format);
        LocalDate endDate = LocalDate.parse(dateEnd, format);
        LocalDate currentDate = LocalDate.now();
      
    }
    public void updateStatus(){
        LocalDate currentDate = LocalDate.now();
        dateStarted = currentDate.isAfter(startDate) || currentDate.isEqual(startDate);
        dateCompleted = currentDate.isAfter(endDate) || currentDate.isEqual(endDate);
        if(this.successfulPayment == true){
            this.state = "Confirmed";
            if(dateStarted){
                this.state = "Ongoing";
            }
            if(dateCompleted){
                this.state = "Completed";
            }

        } else {
            this.state = "Payment Overdue";
        }
    }
    public String getStatus(){
        updateStatus();
        return this.state;
    }
}
