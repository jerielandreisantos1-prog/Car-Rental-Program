import java.util.*;
import java.io.*;


public class VehicleListManager {
    public static ArrayList<Vehicle> vehicleList;
    public static String fileName = "vehicleList";

    public static ArrayList<Vehicle> main() {
        ArrayList<Vehicle> vehicles = VehicleFileManager.loadVehicleList(fileName);
        if(vehicles == null || vehicles.size() == 0){
            vehicles = new ArrayList<Vehicle>();
            VehicleFileManager.saveVehicleList(vehicles, fileName);
        
        }
        return vehicles;
       
      
    }
    public static void add( Vehicle vehicle){
        vehicleList.add(vehicle);
        VehicleFileManager.saveVehicleList(vehicleList, "vehicleList");
    }
    public static void remove( Vehicle vehicle){
        vehicleList.remove(vehicle);
        VehicleFileManager.saveVehicleList(vehicleList, "vehicleList");
    }
    public static void edit ( Vehicle oldVehicle, Vehicle newVehicle){
        int index = vehicleList.indexOf(oldVehicle);
        vehicleList.set(index, newVehicle);
        VehicleFileManager.saveVehicleList(vehicleList, "vehicleList");
    }

}

class VehicleFileManager { 

    
    public static void saveVehicleList(ArrayList<Vehicle> vehicleList, String filename){
        try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename));
            ObjectOutputStream oos = new ObjectOutputStream(bos);){
            oos.writeObject(vehicleList);
            oos.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("readable_"+filename));){
            for(Vehicle vehicle : vehicleList){
                writer.write(vehicle.toString());
                writer.newLine();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public static ArrayList<Vehicle> loadVehicleList(String filename){
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        java.io.File f = new java.io.File(filename);
        if(!f.exists()){
            return vehicleList;
        }
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
            ObjectInputStream ois = new ObjectInputStream(bis);){
            vehicleList = (ArrayList<Vehicle>) ois.readObject();
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return vehicleList;
    }
}