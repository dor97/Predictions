package Engine;

import App.ExecutionListItem;
import App.QueueManagement;
import DTO.*;
import Engine.world.worldDifenichan;
import com.sun.org.apache.xml.internal.security.signature.ReferenceNotInitializedException;
import Engine.world.World;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.omg.CORBA.DynAnyPackage.InvalidValue;
import javafx.collections.FXCollections;


import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Engine {
    private Map<Integer, World> worldsList = new HashMap<>();
    private World cuurentSimuletion;
    private Integer simulationNum = 0;
    private int numOfThreads = 1;
    private ExecutorService threadPool = null;
    private Integer poolSize = 0;
    private String m_fileName = null;
    private Boolean isFileLoadedInSimulation = false;
    private Map<Integer, simulationsStatus> simStatus = new HashMap<>();
    private worldDifenichan worldDif = null;
    //private Map<Integer, String> simulationsExceptions;
    private List<Integer> newlyFinishedSimulationIds = new ArrayList<>();
    private Thread taskThread = null;
    private Boolean isTreadPoolShoutDown = false;
    private Map<String, worldDifenichan> worldDifenichanCollecen = new HashMap<>();
    private Map<String, Map<applicationDetails, simulationApprovementManager>> approvementManager = new HashMap<>();
    private Map<String, World> userCurrentSimulation= new HashMap<>();

    public final Set<String> _getWorldDifenichanCollecen(){
        return worldDifenichanCollecen.keySet();
    }

    public void _askToRunASimulation(String simulationName, String userName, Integer amountToRun, Integer ticks, Integer sec){
        synchronized (approvementManager) {
            applicationDetails details = new applicationDetails(simulationName, approvementStatus.WAITING, ticks, sec);
            if (approvementManager.containsKey(userName)) {
                if (approvementManager.get(userName).containsKey(details)) {
                    approvementManager.get(userName).get(details).addToAmountToRun(amountToRun);
                } else {
                    approvementManager.get(userName).put(details, new simulationApprovementManager(simulationName, userName, amountToRun, ticks, sec));
                }
            } else {
                Map<applicationDetails, simulationApprovementManager> temp = new HashMap<>();
                temp.put(details, new simulationApprovementManager(simulationName, userName, amountToRun, ticks, sec));
                approvementManager.put(userName, temp);
            }
        }
    }

    public Boolean _approveSimulation(String userName, String simulationName, Integer ticks, Integer sec){
        if(approvementManager.containsKey(userName)) {
            applicationDetails details = new applicationDetails(simulationName, approvementStatus.WAITING, ticks, sec);
            if (approvementManager.get(userName).containsKey(details)) {
                simulationApprovementManager manager = approvementManager.get(userName).get(details);
                approvementManager.get(userName).remove(details);
                applicationDetails temp = new applicationDetails(simulationName, approvementStatus.APPROVED, ticks, sec);
                manager.setStatus(approvementStatus.APPROVED);
                approvementManager.get(userName).put(temp, manager);
                return true;
            }
        }
        return false;
    }

    public Boolean _denySimulation(String userName, String simulationName, Integer ticks, Integer sec){
        if(approvementManager.containsKey(userName)) {
            applicationDetails details = new applicationDetails(simulationName, approvementStatus.WAITING, ticks, sec);
            if (approvementManager.get(userName).containsKey(simulationName)) {
                simulationApprovementManager manager = approvementManager.get(userName).get(details);
                approvementManager.get(userName).remove(details);
                applicationDetails temp = new applicationDetails(simulationName, approvementStatus.DENIED, ticks, sec);
                manager.setStatus(approvementStatus.DENIED);
                approvementManager.get(userName).put(temp, manager);
                //approvementManager.get(userName).get(simulationName).setStatus(approvementStatus.DENIED);
                return true;
            }
        }
        return false;
    }

    public final Map<String, Map<applicationDetails, simulationApprovementManager>> _getApprovementManager(){
        return approvementManager;
    }

    public final Map<applicationDetails, simulationApprovementManager> _getApprovementManager(String userName){
        return approvementManager.get(userName);
    }

    public void startThreadPool(Integer numberOFThreads){
        if(threadPool != null && !threadPool.isShutdown()){
            isTreadPoolShoutDown = true;
            threadPool.shutdownNow();
        }

        threadPool = Executors.newFixedThreadPool(numberOFThreads);
        isTreadPoolShoutDown = false;
    }

    public void _loadSimulationDefinition(String fileName) throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException {
        worldDifenichan worldDif = new worldDifenichan();
        worldDif.loadFile(fileName);
        if(worldDifenichanCollecen.containsKey(worldDif.getName())){
            throw new allReadyExistsException("simulation name all ready exists");
        }
        worldDifenichanCollecen.put(worldDif.getName(), worldDif);
    }

    public void loadSimulationFromDefinition(World world, String simulationName, String userName){
        world = new World();
        simulationApprovementManager manager = approvementManager.get(userName).get(simulationName);
        world.loadSimulation(worldDifenichanCollecen.get(simulationName), manager.getTicks(), manager.getSec());
        //numOfThreads = 3;
    }

    public void loadSimulation(String fileName) throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException {
        try {
            cuurentSimuletion = new World();
            numOfThreads = cuurentSimuletion.loadFile(fileName);
            isFileLoadedInSimulation = true;
            if(m_fileName == null || !m_fileName.equals(fileName)){
                loadNewFile(fileName);
            }
        }catch (Exception e){
            m_fileName = null;
            cuurentSimuletion = null;
            throw e;
        }
    }

    private void loadNewFile(String fileName)throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException{
        if(threadPool != null && !threadPool.isShutdown()){
            isTreadPoolShoutDown = true;
            threadPool.shutdownNow();
        }
        m_fileName = fileName;
        synchronized (simStatus) {
            simStatus.clear();
        }
        synchronized (newlyFinishedSimulationIds){
            newlyFinishedSimulationIds.clear(); //TODO maybe not (i.e delete this line)
        }
        threadPool = Executors.newFixedThreadPool(numOfThreads);
        isTreadPoolShoutDown = false;
    }

    public List<DTOEnvironmentVariablesValues> setSimulation()throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException{
        if(!isFileLoadedInSimulation){
            loadSimulation(m_fileName);
        }
        return cuurentSimuletion.setSimulation();
    }

    public void updateNewlyFinishedSimulationInLoop(ObservableList<ExecutionListItem> simulations){
        Thread thread = new Thread(() -> {  while(true)
                                            {updateNewlyFinishedSimulation(simulations);
                                            try{Thread.sleep(200);}catch (InterruptedException e){}}
                                         });
        thread.setDaemon(true);
        thread.start();
    }

    public List<Integer> updateNewlyFinishedSimulation(ObservableList<ExecutionListItem> simulations){
        synchronized (newlyFinishedSimulationIds){
            ObservableList<ExecutionListItem> toRemove = FXCollections.observableArrayList();
            for(Integer id : newlyFinishedSimulationIds){
                for(ExecutionListItem executionListItem : simulations){
                    if(executionListItem.getID().equals(id)){
                        toRemove.add(executionListItem);
                    }
                }
            }

            Platform.runLater(() -> {for(ExecutionListItem executionListItem : toRemove){//TODO make logic when simulation ended
                simulations.remove(executionListItem);
                simulations.add(new ExecutionListItem(executionListItem.getID(), true));

            }});
            List<Integer> res = new ArrayList<>();
            newlyFinishedSimulationIds.stream().forEach(id -> res.add(id));
            newlyFinishedSimulationIds.clear();
            return res;
        }
    }

    public Status getSimulationStatus(Integer simulationNum){
        synchronized (simStatus){
            return simStatus.get(simulationNum).getStatus();
        }
    }

    public void bindAndGetThreadPoolDetails2(ObservableList<QueueManagement> threadPoolList){
        Thread thread = new Thread(() -> {  while(true)
        {threadPoolDetails2(threadPoolList);
            try{Thread.sleep(200);}catch (InterruptedException e){}}
        });
        thread.setDaemon(true);
        thread.start();
    }
    public void threadPoolDetails2(ObservableList<QueueManagement> threadPoolList){
        if(threadPool != null && !threadPool.isTerminated()){
            Integer poolSize = 0;
            Integer finedSimulation = 0;
            Integer witting = 0;
//            synchronized (this.poolSize){
//                poolSize = this.poolSize;
//            }
//            synchronized (this) {
//                finedSimulation = worldsList.size();
//            }
            synchronized(simStatus) {
                for (simulationsStatus simulationsStatus : simStatus.values()) {
                    if (simulationsStatus.getStatus() == Status.WAITINGTORUN) {
                        witting++;
                    } else if (simulationsStatus.getStatus() == Status.RUNNING) {
                        poolSize++;
                    } else if (simulationsStatus.getStatus() == Status.FINISHED) {
                        finedSimulation++;
                    }
                }
            }
            Platform.runLater(() -> threadPoolList.clear());
            setThreadPoolProperties2(threadPoolList, witting, "Waiting");
            setThreadPoolProperties2(threadPoolList, poolSize, "Running");
            setThreadPoolProperties2(threadPoolList, finedSimulation, "Finished");
        }
    }

    private void setThreadPoolProperties2(ObservableList<QueueManagement> threadPoolList, Integer value, String status){
        Platform.runLater(() -> threadPoolList.add(new QueueManagement(status, value)));
    }

    public void bindAndGetThreadPoolDetails(IntegerProperty wit, IntegerProperty run, IntegerProperty fin){
        Thread thread = new Thread(() -> {  while(true)
                                            {threadPoolDetails(wit, run, fin);
                                            try{Thread.sleep(200);}catch (InterruptedException e){}}
                                             });
        thread.setDaemon(true);
        thread.start();
    }
    public void threadPoolDetails(IntegerProperty wit, IntegerProperty run, IntegerProperty fin){
        if(threadPool != null && !threadPool.isTerminated()){
            Integer poolSize = 0;
            Integer finedSimulation = 0;
            Integer witting = 0;
//            synchronized (this.poolSize){
//                poolSize = this.poolSize;
//            }
//            synchronized (this) {
//                finedSimulation = worldsList.size();
//            }
            synchronized(simStatus) {
                for (simulationsStatus simulationsStatus : simStatus.values()) {
                    if (simulationsStatus.getStatus() == Status.WAITINGTORUN) {
                        witting++;
                    } else if (simulationsStatus.getStatus() == Status.RUNNING) {
                        poolSize++;
                    } else if (simulationsStatus.getStatus() == Status.FINISHED) {
                        finedSimulation++;
                    }
                }
            }
            setThreadPoolProperties(wit, witting);
            setThreadPoolProperties(run, poolSize);
            setThreadPoolProperties(fin, finedSimulation);
        }
    }

    private void setThreadPoolProperties(IntegerProperty prop, Integer value){
        Platform.runLater(() -> prop.set(value));
    }

    public void disposeOfThreadPool(){
        if(threadPool != null && !threadPool.isTerminated()){
            isTreadPoolShoutDown = true;
            threadPool.shutdownNow();
        }
    }

    public void bindToWhenFines(BooleanProperty isFines){
        cuurentSimuletion.bindToWhenFines(isFines);
    }

    public List<DTOEnvironmentVariablesValues> _prepareSimulation(String userName, String simulationName, myTask aTask, Map<String, String> environmentsValues, Map<String, Integer> entitiesPopulation, Integer ticks, Integer sec) throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException,  InvalidValue, ReferenceNotInitializedException{
//        if(approvementManager.containsKey(userName) || approvementManager.get(userName).containsKey(simulationName) || !approvementManager.get(userName).get(simulationName).getStatus().equals(approvementStatus.APPROVED)){
//            return null;
//        }
        applicationDetails details = new applicationDetails(simulationName, approvementStatus.APPROVED, ticks, sec);
        synchronized (approvementManager) {
            if (!approvementManager.containsKey(userName) || !approvementManager.get(userName).containsKey(details)) {
                return null;
            }
            approvementManager.get(userName).get(details).decreasedAmount();
            applicationDetails temp = new applicationDetails(simulationName, approvementStatus.USED, ticks, sec);
            if (approvementManager.get(userName).containsKey(temp)) {
                approvementManager.get(userName).get(temp).addToAmountToRun(1);
            } else {
                approvementManager.get(userName).put(temp, new simulationApprovementManager(simulationName, userName, 1, ticks, sec));
            }
        }

        World world = new World();
        loadSimulationFromDefinition(world, simulationName, userName);

        for(String name : environmentsValues.keySet()){
            addEnvironmentVariableValue(world, name, environmentsValues.get(name));
        }

        for(String name : entitiesPopulation.keySet()){
            addPopulationToEntity(world, name, entitiesPopulation.get(name));
        }

        List<DTOEnvironmentVariablesValues> environmentVariablesValues = world.setSimulation();
        synchronized (userCurrentSimulation) {
            userCurrentSimulation.put(userName, world);
        }

        return environmentVariablesValues;
    }

    public int _startSimulation(String userName, String simulationName, myTask aTask)throws InvalidValue, ReferenceNotInitializedException{
        int simulationNum;
        synchronized (userCurrentSimulation) {
            simulationNum = activeSimulation(userCurrentSimulation.get(userName), aTask);
            userCurrentSimulation.remove(userName);
        }
        return simulationNum;
    }

//    public DTOSimulationStartDetails startSimulation(String userName, String simulationName, myTask aTask, Map<String, String> environmentsValues, Map<String, Integer> entitiesPopulation) throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException,  InvalidValue, ReferenceNotInitializedException{
//        if(approvementManager.containsKey(userName) || approvementManager.get(userName).containsKey(simulationName) || !approvementManager.get(userName).get(simulationName).getStatus().equals(simulationApprovementManager.approvementStatus.APPROVED)){
//            return null;
//        }
//        World world = new World();
//        loadSimulationFromDefinition(world, simulationName);
//
//        for(String name : environmentsValues.keySet()){
//            addEnvironmentVariableValue(world, name, environmentsValues.get(name));
//        }
//
//        for(String name : entitiesPopulation.keySet()){
//            addPopulationToEntity(world, name, entitiesPopulation.get(name));
//        }
//
//        List<DTOEnvironmentVariablesValues> environmentVariablesValues = world.setSimulation();
//        int simulationNum = activeSimulation(world, aTask);
//        DTOSimulationStartDetails simulationStartDetails = new DTOSimulationStartDetails(simulationNum, environmentVariablesValues);
//
//        return simulationStartDetails;
//
//    }

    public int activeSimulation(World world, myTask aTask)throws InvalidValue, ReferenceNotInitializedException{
        int simulationNum;
        synchronized (this.simulationNum) {
            simulationNum = ++this.simulationNum;
        }
        if(world == null){
            throw new ReferenceNotInitializedException("Simulation wasn't load");
        }
        //simulationNum++;
        world.setNumSimulation(simulationNum);
        simulationsStatus temp = new simulationsStatus();
        temp.setSimulationId(simulationNum);
        temp.setTask(aTask);
        temp.setWorld(world);
        synchronized (simStatus) {
            simStatus.put(simulationNum, temp);
        }
        isFileLoadedInSimulation = false;
        synchronized (poolSize){
            poolSize++;
        }
        threadPool.execute(() -> activeSimulationUsingThread(aTask, simulationNum));
        //world = null;
        //cuurentSimuletion.startSimolesan();
        //worldsList.put(simulationNum, cuurentSimuletion);
        return simulationNum;
    }

    public int activeSimulation(myTask aTask)throws InvalidValue, ReferenceNotInitializedException{
        int simulationNum;
        synchronized (this.simulationNum) {
            simulationNum = ++this.simulationNum;
        }
        if(cuurentSimuletion == null){
            throw new ReferenceNotInitializedException("Simulation wasn't load");
        }
        //simulationNum++;
        cuurentSimuletion.setNumSimulation(simulationNum);
        simulationsStatus temp = new simulationsStatus();
        temp.setSimulationId(simulationNum);
        temp.setTask(aTask);
        temp.setWorld(cuurentSimuletion);
        synchronized (simStatus) {
            simStatus.put(simulationNum, temp);
        }
        isFileLoadedInSimulation = false;
        synchronized (poolSize){
            poolSize++;
        }
        threadPool.execute(() -> activeSimulationUsingThread(aTask, simulationNum));
        cuurentSimuletion = null;
        //cuurentSimuletion.startSimolesan();
        //worldsList.put(simulationNum, cuurentSimuletion);
        return simulationNum;
    }

    private void activeSimulationUsingThread(myTask aTask, Integer simulationNum){
        Boolean pause;
        World world;
        synchronized (poolSize){
            poolSize--;
        }
        synchronized (simStatus) {
            world = simStatus.get(simulationNum).getWorld();
            simStatus.get(world.getNumSimulation()).setStatus(Status.RUNNING);
            simStatus.get(world.getNumSimulation()).setRunningThread(Thread.currentThread());
            //pause = simStatus.get(world.getNumSimulation()).getIsPause();
        }
        try {
            world.startSimolesan(simStatus.get(world.getNumSimulation()).getIsPause());
        }catch (Exception e){
            world.setException(e.getMessage());
            //simulationsExceptions.put(world.getNumSimulation(), e.getMessage());
        }finally {
            world.setSimulationEnded();
        }
        synchronized (simStatus) {
            if(simStatus != null && simStatus.size() != 0) {
                simStatus.get(world.getNumSimulation()).setStatus(Status.FINISHED);
                simStatus.get(world.getNumSimulation()).setRunningThread(null);
            }
        }
        synchronized (this) {
            if(worldsList != null) { // && worldsList.size() == 0
                worldsList.put(simulationNum, world);
            }
        }
        synchronized (newlyFinishedSimulationIds){
            if(newlyFinishedSimulationIds != null) { // && newlyFinishedSimulationIds.size() == 0
                newlyFinishedSimulationIds.add(world.getNumSimulation());
            }
        }
    }

    public Boolean isSimulationGotError(Integer simulationNum){
        synchronized (simStatus){
            return !simStatus.get(simulationNum).getWorld().getException().equals("");
        }
    }

    public void pauseSimulation(Integer numSimulation){
        synchronized (simStatus.get(numSimulation).getIsPause()) {
            simStatus.get(numSimulation).setIsPause(true);
        }
    }

    public void resumeSimulation(Integer numSimulation){
        synchronized (simStatus.get(numSimulation).getIsPause()) {
            simStatus.get(numSimulation).setIsPause(false);
            simStatus.get(numSimulation).getIsPause().notify();
        }
    }

    public void stopSimulation(Integer numSimulation){
        synchronized (simStatus.get(numSimulation).getRunningThread()) {
            simStatus.get(numSimulation).getRunningThread().interrupt();
        }
    }

    public void moveOneStep(Integer simulationNum){
        simStatus.get(simulationNum).getWorld().moveOneStep(simStatus.get(simulationNum).getIsPause());
    }

    public DTOMap getMap(Integer simulationNum) {
        return simStatus.get(simulationNum).getWorld().getMap(simStatus.get(simulationNum).getIsPause());
    }

    public DTODataForReRun getDataForRerun(Integer simulationNum){
        if(worldsList.containsKey(simulationNum)) {
            return worldsList.get(simulationNum).getDataForRerun();
        }
        return null;
    }

    public void getDataUsingTask(myTask task, Integer simulationId){
        World world;
        Thread taskThread = new Thread(task);
        taskThread.setDaemon(true);
//        synchronized (this.taskThread) {
//            this.taskThread = taskThread;
//        }
        taskThread.setName("TaskThread");
        synchronized (simStatus) {
            world = simStatus.get(simulationId).getWorld();
            simStatus.get(simulationId).setTask(task);
            simStatus.get(simulationId).setTaskThread(taskThread);
        }
        task.loadWorld(world);
        taskThread.start();


    }

    public void stopGettingDataUsingTask(myTask task, Integer simulationNum){
        synchronized (simStatus) {
//            synchronized (this.taskThread) {
//            if(taskThread != null) {
//                taskThread.interrupt();
//                task.cancel();
//                }
//            }
            synchronized (simStatus.get(simulationNum).getWorld()) {
                simStatus.get(simulationNum).getTaskThread().interrupt();
            }
        }

    }

    public void saveSystemState(String simulationName) throws FileNotFoundException, IOException{
        String fileName = simulationName + ".ser";
//        try (FileOutputStream fileOut = new FileOutputStream(fileName); //"serializedObject.ser"
//             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
//        }
        FileOutputStream fileOut = new FileOutputStream(fileName); //"serializedObject.ser"
        ObjectOutputStream out = new ObjectOutputStream(fileOut);

        synchronized (this) {
            out.writeObject(worldsList);
        }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void loadSystemState(String simulationName) throws FileNotFoundException, IOException, ClassNotFoundException{
        String fileName = simulationName + ".ser";
//        try (FileInputStream fileIn = new FileInputStream(fileName); // serializedObject.ser
//             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            FileInputStream fileIn = new FileInputStream(fileName); // serializedObject.ser
            ObjectInputStream in = new ObjectInputStream(fileIn);

        synchronized (this) {
            worldsList = (Map<Integer, World>) in.readObject();
            synchronized (simulationName) {
                simulationNum = worldsList.keySet().size();
            }
        }
//        }catch (FileNotFoundException e){
//
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public DTOSimulationDetails getSimulationDetails(){
        if(cuurentSimuletion == null){
            return null;
        }
        return cuurentSimuletion.getSimulationDetails();
    }

    public List<DTOEnvironmentVariables> getEnvironmentDetails(){ return cuurentSimuletion.getEnvironmentDetails();}

    public void addEnvironmentDto(DTOEnvironmentVariables dtoEnvironmentVariables) throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException  {
        if(!isFileLoadedInSimulation){
            loadSimulation(m_fileName);
        }
        cuurentSimuletion.addEnvironmentDto(dtoEnvironmentVariables);
    }

    public void addPopulationToEntity(String entityName, int population) throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException {
        if(!isFileLoadedInSimulation){
            loadSimulation(m_fileName);
        }
        cuurentSimuletion.addPopulationToEntity(entityName, population);
    }

    public void addPopulationToEntity(World world, String entityName, int population) throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException {
        if(!isFileLoadedInSimulation){
            loadSimulation(m_fileName);
        }
        world.addPopulationToEntity(entityName, population);
    }

    public void addEnvironmentVariableValue(String name, String value) throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException {
        if(!isFileLoadedInSimulation){
            loadSimulation(m_fileName);
        }
        cuurentSimuletion.addEnvironmentValue(name, value);
    }

    public void addEnvironmentVariableValue(World world, String name, String value) throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException {
        if(!isFileLoadedInSimulation){
            loadSimulation(m_fileName);
        }
        world.addEnvironmentValue(name, value);
    }

    public DTOSimulation getSimulationDto() {
//        Map<Integer, String> simulations = new HashMap<>();
//
//        for(Integer id : worldsList.keySet()){
//            simulations.put(id, worldsList.get(id).getSimulationTime());
//        }
        synchronized (this) {
            Map<Integer, String> simulations = worldsList.entrySet().stream().filter(worldEntry -> worldEntry.getValue().getSimulationTime() != null).collect(Collectors.toMap(Map.Entry::getKey, worldEntry -> worldEntry.getValue().getSimulationTime()));
            return new DTOSimulation(simulations);
        }
        //return new DTOSimulation(simulations);
    }

    public DTOSimulationDetailsPostRun getPostRunData(int id) {
        synchronized (simStatus) {
            if(!simStatus.containsKey(id)){
                return null;
            }
            //return worldsList.get(id).getPostRunData();
            return simStatus.get(id).getWorld().getPostRunData();
        }
    }

    public World getCurrentSimulation (){
        return cuurentSimuletion;
    }
}
