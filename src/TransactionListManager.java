import java.util.*;
import java.io.*;

public class TransactionListManager {
    public static ArrayList<Transaction> transactionList;
    public static String filename = "transactionList";

    static {
        transactionList = TransactionFileManager.loadTransactionList(filename);
        if (transactionList == null) transactionList = new ArrayList<>();
    }

    public static ArrayList<Transaction> getList() {
        TransactionFileManager.loadTransactionList(filename);
        return transactionList;
    }

    public static void add(Transaction transaction) {
        transactionList.add(transaction);
        TransactionFileManager.saveTransactionList(transactionList, filename);
    }
    public static void remove(Transaction transaction) {
        transactionList.remove(transaction);
        TransactionFileManager.saveTransactionList(transactionList, filename);
    }
    public static void edit(Transaction oldTransaction, Transaction newTransaction) {
        int index = transactionList.indexOf(oldTransaction);
        if (index >= 0) {
            transactionList.set(index, newTransaction);
            TransactionFileManager.saveTransactionList(transactionList, filename);
        }
    }


class TransactionFileManager{
    public static void saveTransactionList(ArrayList<Transaction> transactionList, String filename){
        try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename));
            ObjectOutputStream oos = new ObjectOutputStream(bos);){
            oos.writeObject(transactionList);
            oos.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("readable_"+filename));){
            for(Transaction transaction : transactionList){
                writer.write(transaction.toString());
                writer.newLine();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public static ArrayList<Transaction> loadTransactionList(String filename){
        ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
        java.io.File f = new java.io.File(filename);
        if(!f.exists()){
            return transactionList;
        }
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filename));
            ObjectInputStream ois = new ObjectInputStream(bis);){
            transactionList = (ArrayList<Transaction>) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return transactionList;
    }
}
}
