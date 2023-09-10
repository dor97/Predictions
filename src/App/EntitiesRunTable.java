package App;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;

public class EntitiesRunTable {
    private final SimpleStringProperty entityName;
    private SimpleIntegerProperty population;

    public EntitiesRunTable(String i_name, int value){
        this.entityName = new SimpleStringProperty(i_name);
        this.population = new SimpleIntegerProperty(value);
    }

    public String getEntityName(){
        return entityName.get();
    }

    public SimpleStringProperty entityNameProperty(){
        return entityName;
    }

    public SimpleIntegerProperty populationProperty(){return population;}

    public void setPopulation (SimpleIntegerProperty population){
        this.population = population;
    }

    public int getPopulation(){
        return population.get();
    }
}
