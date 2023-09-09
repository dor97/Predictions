package SimulationDetailsTable;

import App.AppController;
import DTO.DTOEntityData;
import DTO.DTOEnvironmentVariables;
import DTO.DTOSimulationDetails;
import Engine.Engine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SimulationDetailsTableController implements Initializable {

    @FXML private TableView<EnvironmentVariableTable> environmentVariableTable;
    @FXML public TableColumn<EnvironmentVariableTable, String> envVarNameColumn;
    @FXML public TableColumn<EnvironmentVariableTable,String> envVarValueColumn;
    @FXML public TableColumn<String, String> entityNameColumn;
    @FXML public TableColumn <String, Integer> populationColumn;
    @FXML private TableView<String> entitiesTable;
    @FXML private AppController mainController;
    private ObservableList<EnvironmentVariableTable> environmentVariablesData = FXCollections.observableArrayList();
    public void setMainController(AppController appController) {
        this.mainController = mainController;
    }

    public void updateTablesWithDetails(Engine engine) {

        DTOSimulationDetails simulationDetails = engine.getSimulationDetails();

        for (DTOEnvironmentVariables environmentVariable : simulationDetails.getEnvironmentVariables()){
//          environmentVariableTable.getItems().add(new EnvironmentVariableTable("ori", "2"));
            environmentVariablesData.add(new EnvironmentVariableTable(environmentVariable.getVariableName(), "0"));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//      envVarNameColumn.setCellValueFactory(new PropertyValueFactory<EnvironmentVariableTable, String>("EnvVarNameColumn"));
        envVarNameColumn.setCellValueFactory(cellData -> cellData.getValue().getEnvVarName());
        envVarValueColumn.setCellValueFactory(cellData -> cellData.getValue().getValue());
//      envVarValueColumn.setCellValueFactory(new PropertyValueFactory<EnvironmentVariableTable, String>("EnvVarValueColumn"));
        environmentVariablesData.add(new EnvironmentVariableTable("ori", "2"));
        environmentVariableTable.setItems(environmentVariablesData);
//      environmentVariableTable.getItems().add(new EnvironmentVariableTable("ori", "2"));
    }
}
