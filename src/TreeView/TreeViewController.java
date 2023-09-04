package TreeView;

import App.AppController;
import DTO.*;
import Engine.Engine;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;

import java.util.Map;

public class TreeViewController {
    private AppController mainController;
    @FXML private TreeView<DTOSimulationDetailsItem> detailsTreeView;
    @FXML private BorderPane borderPaneTreeView;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void displayFileDetails(Engine engine, String absolutePath) {

        try {
            engine.loadSimulation(absolutePath);
            DTOSimulationDetails details = engine.getSimulationDetails();
            TreeItem<DTOSimulationDetailsItem> rootItem = new TreeItem<>(details);
            System.out.println("adding item: ");
            for(DTOEntityData entityData : details.getEntitysList()){
                TreeItem<DTOSimulationDetailsItem> entityItem = new TreeItem<>(entityData);
                rootItem.getChildren().add(entityItem);
                for(DTOPropertyData propertyData : entityData.getPropertList()){
                    entityItem.getChildren().add(new TreeItem<>(propertyData));
                }
                System.out.println("Item: ");
            }
            for(DTORuleData ruleData : details.getRulesList()){
                TreeItem<DTOSimulationDetailsItem> ruleItem = new TreeItem<>(ruleData);
                rootItem.getChildren().add(ruleItem);
                for(DTOActionData actionData : ruleData.getActions()){
                    ruleItem.getChildren().add(new TreeItem<>(actionData));
                }
            }

            detailsTreeView.setRoot(rootItem);
            mainController.getTreeViewComponent().setLeft(detailsTreeView);

            detailsTreeView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
                if (newValue !=null && newValue.getChildren().isEmpty()){
                    TreeItem<DTOSimulationDetailsItem> selectedTreeItem = newValue;
                    DTOSimulationDetailsItem selectedValue = selectedTreeItem.getValue();
                    mainController.displayTreeItemsDetails(selectedValue);
                }
            }));
        }catch (Exception e){
            System.out.println(e.getMessage());

        }
    }
    public void selectItem(){
        TreeItem<DTOSimulationDetailsItem> item = detailsTreeView.getSelectionModel().getSelectedItem();

        if(item != null){
            item.getValue().getData().ifPresent(putData -> putData(putData));
        }
    }

    private void putData(Map<String, String> data){
        for(String d : data.values()){
            System.out.println(d);
        }
    }
}
