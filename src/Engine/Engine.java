package Engine;

import DTO.*;
import com.sun.org.apache.xml.internal.security.signature.ReferenceNotInitializedException;
import Engine.world.World;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Engine {
    Map<Integer, World> worldsList = new HashMap<>();
    World cuurentSimuletion;
    Integer simulationNum = 1;

    public void loadSimulation(String fileName) throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException {
        try {
            cuurentSimuletion = new World();
            cuurentSimuletion.loadFile(fileName);
        }catch (Exception e){
            cuurentSimuletion = null;
            throw e;
        }
    }

    public List<DTOEnvironmentVariablesValues> setSimulation()throws InvalidValue{
        return cuurentSimuletion.setSimulation();
    }

    public int activeSimulation()throws InvalidValue, ReferenceNotInitializedException{
        int old_simulationNumber = simulationNum;
        if(cuurentSimuletion == null){
            throw new ReferenceNotInitializedException("Simulation wasn't load");
        }
        cuurentSimuletion.startSimolesan();
        worldsList.put(simulationNum, cuurentSimuletion);
        simulationNum++;
        return old_simulationNumber;
    }

    public void saveSystemState(String simulationName){
        String fileName = simulationName + ".ser";
        try (FileOutputStream fileOut = new FileOutputStream(fileName); //"serializedObject.ser"
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(worldsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadSystemState(String simulationName){
        String fileName = simulationName + ".ser";
        try (FileInputStream fileIn = new FileInputStream(fileName); // serializedObject.ser
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            worldsList = (Map<Integer, World>) in.readObject();
            simulationNum = worldsList.keySet().size();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DTOSimulationDetails getSimulationDetails(){
        return cuurentSimuletion.getSimulationDetails();
    }

    public List<DTOEnvironmentVariables> getEnvironmentDetails(){ return cuurentSimuletion.getEnvironmentDetails();}

    public void addEnvironmentDto(DTOEnvironmentVariables dtoEnvironmentVariables) throws InvalidValue {
        cuurentSimuletion.addEnvironmentDto(dtoEnvironmentVariables);
    }

    public DTOSimulation getSimulationDto() {
//        Map<Integer, String> simulations = new HashMap<>();
//
//        for(Integer id : worldsList.keySet()){
//            simulations.put(id, worldsList.get(id).getSimulationTime());
//        }

        Map<Integer, String> simulations = worldsList.entrySet().stream().filter(worldEntry -> worldEntry.getValue().getSimulationTime() != null).collect(Collectors.toMap(Map.Entry::getKey, worldEntry -> worldEntry.getValue().getSimulationTime()));
        return new DTOSimulation(simulations);
    }

    public DTOSimulationDetailsPostRun getPostRunData(int id) {
        return worldsList.get(id).getPostRunData();
    }


    }
