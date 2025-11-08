public class Vehicle implements java.io.Serializable {
    private String type;
    private String price;
    private Boolean Available;

    public Vehicle( String type, String price, Boolean Available){
        this.type = type;
        this.price = price;
        this.Available = Available;
    }
    public String getType(){
        return type;
    }
    public String getPrice(){
        return price;
    }
    public Boolean getAvailability(){
        return Available;
    }
    public Boolean setAvailability(Boolean availability){
        this.Available = availability;
        return Available;
    }
    @Override
    public String toString(){
        return "Type:"+ this.type + " | Price per day:"+ this.price + " | Available:"+ this.Available;
    }
}
