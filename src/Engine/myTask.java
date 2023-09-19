package Engine;

import App.EntitiesRunTable;
import DTO.DTORunningSimulationDetails;
import Engine.stopException;
import Engine.world.World;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;

import javax.xml.ws.Binding;
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
    private StringProperty m_tick = new SimpleStringProperty("0");
    private StringProperty m_sec = new SimpleStringProperty("0");
    private Map<String, Integer> map = new HashMap<>(); // MapProperty == SimpleMapProperty
    private ObservableList<EntitiesRunTable> table;
    private StringProperty text;
    private StringProperty exception = new SimpleStringProperty();
    private BooleanProperty endSimulation;


    private World world;

    public void bindProperties(StringProperty tick, StringProperty sec, StringProperty exception, ObservableList<EntitiesRunTable> table, BooleanProperty endSimulation){//MapProperty<String, Integer> map,
        //map.bind(this.map);
        //tick.bind(m_tick);
        //sec.bind(m_sec);
        Bindings.bindBidirectional(sec, m_sec);
        Bindings.bindBidirectional(tick, m_tick);
        Bindings.bindBidirectional(exception, this.exception);
        this.endSimulation = endSimulation;
        //tick.bindBidirectional(m_tick);
        //text = tick;
        this.table = table;
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

        while(!isCancelled()){
            synchronized (world) {
                if (Thread.currentThread().isInterrupted()) {
                    return new SimpleMapProperty<>();
                }
                DTORunningSimulationDetails runningSimulationDetails = world.getRunningSimulationDTO();
                if (runningSimulationDetails == null) {
                    DTORunningSimulationDetails finishedRunning = world.getSimulationRunningDetailsDTO();
                    Platform.runLater(() -> m_tick.set(finishedRunning.getTick().toString()));
                    Platform.runLater(() -> m_sec.set(finishedRunning.getTime().toString()));
                    //runningSimulationDetails.getEntities().entrySet().stream().forEach();
                    Platform.runLater(() -> {
                        map.clear();
                        map.putAll(finishedRunning.getEntities());
                        updateTable();
                    });
                    Platform.runLater(() -> exception.set(world.getException()));
                    Platform.runLater(() -> endSimulation.set(!endSimulation.get()));
                    return new SimpleMapProperty<>();
                }

                //map = runningSimulationDetails.getEntities();
                Platform.runLater(() -> m_tick.set(runningSimulationDetails.getTick().toString()));
                Platform.runLater(() -> m_sec.set(runningSimulationDetails.getTime().toString()));
                //runningSimulationDetails.getEntities().entrySet().stream().forEach();
                Platform.runLater(() -> {
                    map.clear();
                    map.putAll(runningSimulationDetails.getEntities());
                    updateTable();
                });
                //Platform.runLater(() -> map = runningSimulationDetails.getEntities());
                //mmm.bind(ma);
                //updateValue(runningSimulationDetails.getEntities());
            }

            try {
                Thread.sleep(200);
            }catch (InterruptedException e){
                return new SimpleMapProperty<>();
            }
        }

        return new SimpleMapProperty<>();




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