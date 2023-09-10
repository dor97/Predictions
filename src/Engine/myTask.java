package Engine;

import App.EntitiesRunTable;
import DTO.DTORunningSimulationDetails;
import Engine.stopException;
import Engine.world.World;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

//public class BooleanTaskWithMessage extends Task<Boolean> {
//
//    @Override
//    protected Boolean call() throws Exception {
//        // Simulate some time-consuming work
//        for (int i = 1; i <= 10; i++) {
//            Thread.sleep(1000); // Sleep for 1 second (simulating work)
//
//            // Update the message property with progress information
//            updateMessage("Processing step " + i + " of 10");
//        }
//
//        // Return the result of the task (true or false)
//        return true;
//    }
//}

public class myTask extends Task<ObservableMap<String, Integer>> {
    private final Object pauseLock = new Object();
    private boolean paused = false;

    private BooleanProperty pause = new SimpleBooleanProperty();
    private StringProperty m_tick = new SimpleStringProperty();
    private StringProperty m_sec = new SimpleStringProperty();
    private Map<String, Integer> map = new HashMap<>(); // MapProperty == SimpleMapProperty
    private ObservableList<EntitiesRunTable> table;


    private World world;

    public void bindProperties(StringProperty tick, StringProperty sec, ObservableList<EntitiesRunTable> table){//MapProperty<String, Integer> map,
        //map.bind(this.map);
        tick.bind(m_tick);
        sec.bind(m_sec);
        this.table = table;
    }

    public void func(Map<String, Integer> name){
        //name.bind(valueProperty());
        ObservableMap<String, Integer> temp = FXCollections.observableHashMap();
        name.entrySet().stream().forEach(stringIntegerEntry -> temp.put(stringIntegerEntry.getKey(), stringIntegerEntry.getValue()));
        //temp.put("e1", 5);
        updateValue(temp);
    }

    public void loadWorld(World world){
        this.world = world;
    }

    private void updateTable(){
        table.clear();
        map.entrySet().stream().forEach(en -> table.add(new EntitiesRunTable(en.getKey(), en.getValue())));

    }

    @Override
    protected ObservableMap<String, Integer> call() throws Exception {

        while(true){
            DTORunningSimulationDetails runningSimulationDetails = world.getRunningSimulationDTO();
            if(runningSimulationDetails == null){
                updateMessage(world.getException());
                return new SimpleMapProperty<>();
            }
            map = runningSimulationDetails.getEntities();
            Platform.runLater(() -> m_tick.set(runningSimulationDetails.getTick().toString()));
            Platform.runLater(() -> m_sec.set(runningSimulationDetails.getTime().toString()));
            //runningSimulationDetails.getEntities().entrySet().stream().forEach();
            Platform.runLater(() ->updateTable());
            //Platform.runLater(() -> map = runningSimulationDetails.getEntities());
            //mmm.bind(ma);
            //updateValue(runningSimulationDetails.getEntities());

            if(Thread.currentThread().isInterrupted()){
                return new SimpleMapProperty<>();
            }

            Thread.sleep(200);
        }






//        System.out.println("toPause?");
//        if (paused) {
//            synchronized (pauseLock) {
//                System.out.println("toPause, for real!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                try {
//                    pauseLock.wait();
//                } catch (InterruptedException e) {
//                    // Handle interruption if needed
//                }
//            }
//        }

//        for (int i = 2; i < 30; i++) {
//            //System.out.println("update");
//            updateMessage(Integer.toString(i));
//            updateValue(Integer.toString(i));
//            Thread.sleep(100);
//        }
//
//
//        return "100";
        //return new SimpleMapProperty<>();
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
        synchronized (pauseLock) {
            pauseLock.notifyAll();
        }
    }

    public BooleanProperty pauseProperty(){
        return pause;
    }

    public StringProperty tickProperty(){
        return m_tick;
    }

    public StringProperty secProperty(){
        return m_sec;
    }

    public void setTick(Integer tick){
        Platform.runLater(() -> m_tick.set(tick.toString()));
        //m_tick.set(tick);
    }

    public void setSce(Long sec){
        Platform.runLater(() -> m_sec.set(sec.toString()));
    }

    public Boolean getPause() {
        return pause.get();
    }

//    public StringProperty pauseProperty() {
//        return new SimpleStringProperty() {
//            @Override
//            public void set(String newValue) {
//                if (newValue.equals("pause")) {
//                    pause();
//                } else {
//                    resume();
//                }
//                super.set(newValue);
//            }
//        };
//    }
}