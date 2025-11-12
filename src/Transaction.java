import java.time.*;
import java.time.format.DateTimeFormatter;

public class Transaction implements java.io.Serializable {
    public Vehicle vehicleReserved;
    public Status status;
    public String dateStart;
    public String dateEnd;
    public double price; 
    public String transactionID;
    public User user;

    Transaction(String dateStart, String dateEnd, Vehicle vehicleReserved, double price, String transactionID, User user) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.status = new Status(dateStart, dateEnd);
        this.price = price;
        this.transactionID = transactionID;
        this.user = user;
        this.vehicleReserved = vehicleReserved;
    }
    public User getUser(){
        return this.user;
    }
    public String getDateEnd(){
        return this.dateEnd;
    } 
    public String getTransactionID(){
        return this.transactionID;
    }
    public String getVehicleType(){
        return this.vehicleReserved.getType();
    }
    public String getStatus(){
        return this.status.getStatus();

    }
    public String getPrice(){
        return String.valueOf(this.price);
    }
    public String getDateStart(){
        return this.dateStart;
    }
    
    @Override 
    public String toString(){
        return "Transaction ID:"+ this.transactionID + " | Vehicle Reserved:"+ this.vehicleReserved + " | Start Date:"+ this.dateStart + " | End Date:"+ this.dateEnd + " | Price:"+ this.price + " | Status:"+ this.status.getStatus();
    }
    
    

    
}
class Status implements java.io.Serializable {
    public String state;
    public Boolean successfulPayment;
    public Boolean dateStarted;
    public Boolean dateCompleted;
    public LocalDate startDate;
    public LocalDate endDate;
    public LocalDate currentDate;
    Status(String dateStart, String dateEnd) {
        this.state = "Pending";
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
        this.startDate = LocalDate.parse(dateStart, format);
        this.endDate = LocalDate.parse(dateEnd, format);
        this.currentDate = LocalDate.now();
    }
    public void updateStatus() {
        this.currentDate = LocalDate.now();
        if (startDate != null && endDate != null) {
            dateStarted = (currentDate.isAfter(startDate) || currentDate.isEqual(startDate)) && (currentDate.isBefore(endDate) || currentDate.isEqual(endDate));
            dateCompleted = currentDate.isAfter(endDate) || currentDate.isEqual(endDate);
        } else {
            dateStarted = false;
            dateCompleted = false;
        }
        if (Boolean.TRUE.equals(this.successfulPayment)) {
            this.state = "Confirmed";
            if (dateStarted) {
                this.state = "Ongoing";
            }
            if (dateCompleted) {
                this.state = "Completed";
            }
        } else if (Boolean.FALSE.equals(this.successfulPayment) && startDate != null && currentDate.isAfter(startDate)) {
            this.state = "Payment Overdue";
        }
    }
    public String getStatus(){
        updateStatus();
        return this.state;
    }
}
