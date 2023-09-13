package App;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;

public class QueueManagement {
    private SimpleStringProperty status;
    private SimpleIntegerProperty amount;

    public QueueManagement(String status, Integer amount){
        this.status = new SimpleStringProperty(status);
        this.amount = new SimpleIntegerProperty(amount);
    }

    public String getStatus(){
        return status.get();
    }

    public SimpleStringProperty statusProperty(){
        return status;
    }

    public void setAmount (SimpleIntegerProperty amount){
        this.amount = amount;
    }

    public Integer getAmount(){
        return amount.get();
    }

    public SimpleIntegerProperty amountProperty(){
        return amount;
    }
}
