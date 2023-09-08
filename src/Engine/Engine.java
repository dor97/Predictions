package Engine;

import DTO.*;
import Engine.world.worldDifenichan;
import UI.ConsoleUI.myTask;
import com.sun.org.apache.xml.internal.security.signature.ReferenceNotInitializedException;
import Engine.world.World;
import javafx.concurrent.Task;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Engine {
    private Map<Integer, World> worldsList = new HashMap<>();
    private World cuurentSimuletion;
    private Integer simulationNum = 0;
    private int numOfThreads = 1;
    private ExecutorService threadPool;
    private String m_fileName = null;
    private Boolean isFileLoadedInSimulation = false;
    private Map<Integer, simulationsStatus> simStatus = new HashMap<>();
    private worldDifenichan worldDif = null;
    private Map<Integer, String> simulationsExceptions;

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
        m_fileName = fileName;
        threadPool = Executors.newFixedThreadPool(numOfThreads);
    }

    public List<DTOEnvironmentVariablesValues> setSimulation()throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException{
        if(!isFileLoadedInSimulation){
            loadSimulation(m_fileName);
        }
        return cuurentSimuletion.setSimulation();
    }

    public int activeSimulation(myTask aTask)throws InvalidValue, ReferenceNotInitializedException{
        int old_simulationNumber = ++simulationNum;
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
        threadPool.execute(() -> activeSimulationUsingThread(aTask, simulationNum));
        cuurentSimuletion = null;
        //cuurentSimuletion.startSimolesan();
        //worldsList.put(simulationNum, cuurentSimuletion);
        return old_simulationNumber;
    }

    private void activeSimulationUsingThread(myTask aTask, Integer simulationNum){
        Boolean pause;
        World world;
        synchronized (simStatus) {
            world = simStatus.get(simulationNum).getWorld();
            simStatus.get(world.getNumSimulation()).setStatus(Status.RUNNING);
            simStatus.get(world.getNumSimulation()).setRunningThread(Thread.currentThread());
            pause = simStatus.get(world.getNumSimulation()).getIsPause();
        }
        try {
            world.startSimolesan(pause);
        }catch (Exception e){
            world.setException(e.getMessage());
            simulationsExceptions.put(world.getNumSimulation(), e.getMessage());
        }finally {
            world.setSimulationEnded();
        }
        synchronized (simStatus) {
            simStatus.get(world.getNumSimulation()).setStatus(Status.FINISHED);
            simStatus.get(world.getNumSimulation()).setRunningThread(null);
        }
        synchronized (this) {
            worldsList.put(simulationNum, world);
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

    public DTOMap getMap(Integer simulationNum) {
        return simStatus.get(simulationNum).getWorld().getMap(simStatus.get(simStatus).getIsPause());
    }

    public DTODataForReRun getDataForRerun(Integer simulationNum){
        return worldsList.get(simulationNum).getDataForRerun();
    }

    public void getDataUsingTask(myTask task, Integer simulationId){
        World world;
        Thread taskThread = new Thread(task);
        synchronized (simStatus) {
            world = simStatus.get(simulationId).getWorld();
            simStatus.get(simulationId).setTask(task);
            simStatus.get(simulationId).setTaskThread(taskThread);
        }
        task.loadWorld(world);
        taskThread.start();


    }

    public void stopGettingDataUsingTask(Integer simulationNum){
        synchronized (simStatus) {
            simStatus.get(simulationNum).getTaskThread().interrupt();
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
            simulationNum = worldsList.keySet().size();
        }
//        }catch (FileNotFoundException e){
//
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public DTOSimulationDetails getSimulationDetails(){
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
        synchronized (this) {
            return worldsList.get(id).getPostRunData();
        }
    }

    public World getCurrentSimulation (){
        return cuurentSimuletion;
    }
}
