package TreeDetails;

import App.AppController;
import DTO.DTOSimulationDetailsItem;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import sun.reflect.generics.tree.Tree;

import java.util.Map;
import java.util.Optional;

public class TreeDetailsController {

    @FXML private TreeView <String> detailsTreeView;
    private AppController mainController;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void displayTreeItemsDetails(DTOSimulationDetailsItem selectedValue) {

        TreeItem<String> rootItem = new TreeItem<>("Data");

        for (Map.Entry<String, String> entry : selectedValue.getData().get().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            TreeItem<String> item = new TreeItem<>(key + ": " + value);
            rootItem.getChildren().add(item);
        }

        detailsTreeView.setRoot(rootItem);
        mainController.getTreeViewComponent().setCenter(detailsTreeView);
    }
}
