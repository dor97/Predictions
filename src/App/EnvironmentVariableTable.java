package App;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;
import javafx.util.Duration;


public class EnvironmentVariableTable {
    private final SimpleStringProperty envVarNameColumn;
    private TextField value;
    private String envVarNameNoType;
    private ParallelTransition pt;

    public EnvironmentVariableTable(String i_name, String value, String envVarNameNoType){
        this.envVarNameColumn = new SimpleStringProperty(i_name);
        this.value = new TextField(value);
        this.envVarNameNoType = envVarNameNoType;

        FadeTransition ft = new FadeTransition(Duration.millis(1000));//Duration.INDEFINITE
        ft.setFromValue(1);
        ft.setToValue(0.25);
        ft.setAutoReverse(true);
        ft.setCycleCount(Animation.INDEFINITE);
        this.value.setStyle("-fx-background-color: #00d0ff;");
        this.pt = new ParallelTransition(this.value, ft);

        //this.population.setOnTouchPressed();

        this.value.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Platform.runLater(() -> pt.play());
            } else {
                Platform.runLater(() -> {if(pt != null){
                    pt.stop();
                    this.value.setOpacity(1);
                }});

            }
        });
    }

    public String getEnvVarName(){
        return envVarNameColumn.get();
    }

    public SimpleStringProperty envVarNameColumnProperty(){
        return envVarNameColumn;
    }

    public void setValue (TextField value){
        this.value = value;
    }

    public void setValueInText(String value){
        this.value.setText(value);
    }

    public TextField getValue(){
        return value;
    }

    public String getEnvVarNameNoType(){
        return envVarNameNoType;
    }
}
