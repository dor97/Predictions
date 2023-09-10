package App;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;

public class EntitiesTable {
    private final SimpleStringProperty entityName;
    private TextField population;

    public EntitiesTable(String i_name, String value){
        this.entityName = new SimpleStringProperty(i_name);
        this.population = new TextField(value);
    }

    public String getEntityName(){
        return entityName.get();
    }

    public SimpleStringProperty entityNameProperty(){
        return entityName;
    }

    public void setPopulation (TextField population){
        this.population = population;
    }

    public TextField getPopulation(){
        return population;
    }
}
