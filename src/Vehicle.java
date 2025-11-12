import java.time.LocalDate;

public class Vehicle implements java.io.Serializable {
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vehicle other = (Vehicle) obj;
        return type.equals(other.type) && price.equals(other.price);
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }
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
    public Boolean availabilityCheck(LocalDate requestedStart, LocalDate requestedEnd, java.util.List<Transaction> transactions){
        for (Transaction t : transactions) {
            if (t.vehicleReserved != null && t.vehicleReserved.equals(this)) {
                LocalDate tStart = t.status.startDate;
                LocalDate tEnd = t.status.endDate;
                if (tStart != null && tEnd != null) {
                    // Overlap: not (requestedEnd < tStart or requestedStart > tEnd)
                    if (!(requestedEnd.isBefore(tStart) || requestedStart.isAfter(tEnd))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    @Override
    public String toString(){
        return "Type:"+ this.type + " | Price per day:"+ this.price + " | Available:"+ this.Available;
    }
}
        