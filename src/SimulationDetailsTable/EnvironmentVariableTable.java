package SimulationDetailsTable;

import Engine.world.entity.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class EnvironmentVariableTable {
    private final SimpleStringProperty envVarNameColumn;
    private final SimpleStringProperty envVarValueColumn;

    public EnvironmentVariableTable(String i_name, String i_value){
        this.envVarNameColumn = new SimpleStringProperty(i_name);
        this.envVarValueColumn = new SimpleStringProperty(i_value);
    }

    public SimpleStringProperty getEnvVarName(){
        return envVarNameColumn;
    }

    public SimpleStringProperty getValue(){
        return envVarValueColumn;
    }

}
