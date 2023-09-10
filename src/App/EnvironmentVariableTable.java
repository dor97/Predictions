package App;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;


public class EnvironmentVariableTable {
    private final SimpleStringProperty envVarNameColumn;
    private TextField value;
    private String envVarNameNoType;

    public EnvironmentVariableTable(String i_name, String value, String envVarNameNoType){
        this.envVarNameColumn = new SimpleStringProperty(i_name);
        this.value = new TextField(value);
        this.envVarNameNoType = envVarNameNoType;
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
