public class Vehicle {
    private String type;
    private String price;
    private Boolean Available;

    public Vehicle( String type, String price, Boolean Available){
        this.type = type;
        this.price = price;
        this.Available = Available;
    }
    public Boolean checkAvailability(){
        return Available;
    }
    public Boolean setAvailability(Boolean availability){
        this.Available = availability;
        return Available;
    }
}
