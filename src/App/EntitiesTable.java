package App;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.util.Duration;

public class EntitiesTable {
    private final SimpleStringProperty entityName;
    private TextField population;
    private static ParallelTransition pt;
    private static TextField lastTextField = null;

    public EntitiesTable(String i_name, String value){
        this.entityName = new SimpleStringProperty(i_name);
        this.population = new TextField(value);
        //this.population.setBackground(new Background(new BackgroundFill()));
        FadeTransition ft = new FadeTransition(Duration.millis(1000));//Duration.INDEFINITE
        ft.setFromValue(1);
        ft.setToValue(0.25);
        ft.setAutoReverse(true);
        ft.setCycleCount(Animation.INDEFINITE);
        this.population.setStyle("-fx-background-color: #00d0ff;");

        this.population.setOnMouseClicked( param -> {
            if(lastTextField != null && pt != null){
                pt.stop();
                lastTextField.setOpacity(1);
                //pt.stop();

            }
            lastTextField = this.population;
            this.pt = new ParallelTransition(this.population, ft);
            pt.play();
        });

        //this.population.addEventHandler();
        //pt.stop();
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
