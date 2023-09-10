package App;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;


public class EnvironmentVariableTable {
    private final SimpleStringProperty envVarNameColumn;
    private TextField value;

    public EnvironmentVariableTable(String i_name, String value){
        this.envVarNameColumn = new SimpleStringProperty(i_name);
        this.value = new TextField(value);
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

    public TextField getValue(){
        return value;
    }
}
