package Engine.world;

import DTO.*;
import Engine.InvalidValue;
import Engine.UnsupportedFileTypeException;
import Engine.allReadyExistsException;
import Engine.world.entity.Entity;
import Engine.world.entity.EntityDifenichan;
import Engine.generated.*;
import Engine.world.entity.property.*;
import Engine.world.entity.property.PropertyInterface;
import Engine.world.expression.expression;
import Engine.world.rule.Rule;
import Engine.world.rule.action.ActionInterface;
import Engine.world.expression.expressionType;
import Engine.utilites.Utilites;
import javafx.application.Platform;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.util.Pair;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class World implements Serializable {
    private List<Rule> m_rules = new ArrayList<>();
    private List<Entity> m_entities = new ArrayList<>();

    private Map<String, EntityDifenichan> m_entitiesDifenichan = new HashMap<>();
    private Map<String, PropertyInterface> m_environments = new HashMap<>();
    private Map<String, EnvironmentDifenichan> m_environmentsDifenichen = new HashMap<>();
    //private Map<String, DTOEnvironmentVariables> m_enviromentsDto = new HashMap<>();
    private expression m_ticks = null;
    private expression m_secondToWork = null;
    private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy | hh.mm.ss");
    private String simulationTime = null;
    private Utilites util;
    private int m_rows, m_cols, currTick = 0;
    private Instant start;
    private Integer numSimulation = 0;
    private Boolean isSimulationEnded = false;
    private javafx.beans.property.BooleanProperty isFines;
    private map map;
    private String exception = "";

    private List<Pair<Integer, Integer>> numOfEntitiesPerTick = new ArrayList<>();

    private final static String JAXB_XML_GAME_PACKAGE_NAME = "Engine.generated";

    public World(){
        isFines.set(false);
    }

    public void setNumSimulation(Integer num){
        numSimulation = num;
    }
    public Integer getNumSimulation(){
        return numSimulation;
    }

    public void setSimulationEnded(){
        Platform.runLater(() -> isFines.set(true));
        isSimulationEnded = true;
    }

    public DTORunningSimulationDetails getRunningSimulationDTO(){
        if(isSimulationEnded){
            return null;
        }
        DTORunningSimulationDetails runningSimulationDetails = new DTORunningSimulationDetails();

        //m_entities.stream().collect(Collectors.groupingBy(Entity::getName, Collectors.summingInt(e -> 1)));
        Map<String, Integer> entitiesMap = m_entities.stream().collect(Collectors.groupingBy(Entity::getName, Collectors.summingInt(e -> 1)));
        MapProperty<String, Integer> entitiesObservableMap = new SimpleMapProperty<>();
        entitiesMap.entrySet().stream().forEach(entityEntrySet -> entitiesObservableMap.put(entityEntrySet.getKey(), entityEntrySet.getValue()));
        runningSimulationDetails.setEntities(entitiesObservableMap);

        runningSimulationDetails.setTick(currTick);
        runningSimulationDetails.setTime(Duration.between(start, Instant.now()).getSeconds());

        return runningSimulationDetails;
    }
    public void startSimolesan(Boolean isPause)throws InvalidValue{
        //Utilites.Init(m_environments, m_entitiesDifenichan);
        List<Entity> toRemove = new ArrayList<>();
        Random random = new Random();

//        for(int i = 0; i < m_ticks.getInt(); ++i){
//            for(Rule r : m_rules) {
//                if(i % r.getTick() == 0 && random.nextDouble() < r.getProbability()) {
//                    for (Entity entity : m_entities) {
//                        if (r.activeRule(entity)) {
//                            toRemove.add(entity);
//                            break;
//                        }
//                    }
//                }
//            }
//            for(Entity entity : toRemove){
//                m_entities.remove(entity);
//            }
//        }

        simulationTime = format.format(new Date());
        currTick = 0;
        start = Instant.now();
//        while ((m_ticks.getType() == null || currTick < m_ticks.getInt()) && (m_secondToWork == null || Duration.between(start, Instant.now()).getSeconds() < m_secondToWork.getInt())){
//            for(Rule r : m_rules) {
//                if(currTick % r.getTick() == 0 && random.nextDouble() < r.getProbability()) {
//                    for (Entity entity : m_entities) {
//                        if (r.activeRule(entity)) {
//                            toRemove.add(entity);
//                            break;
//                        }
//                    }
//                }
//            }
//            for(Entity entity : toRemove){
//                m_entities.remove(entity);
//            }
//            currTick++;
//        }
        map = new map(m_rows, m_cols);  //TODO change
        map.setLocations(m_entities);
        List<ActionInterface> toActive = new ArrayList<>();
        while ((m_ticks.getType() == null || currTick < m_ticks.getInt()) && (m_secondToWork.getType() == null || Duration.between(start, Instant.now()).getSeconds() < m_secondToWork.getInt())) {  //&& currTick < 1000
            numOfEntitiesPerTick.add(new Pair<>(currTick, m_entities.size()));
            toActive.clear();
            for(Rule r : m_rules) {
                toActive.addAll(r.getActionToActive(currTick));
            }

            //m_rules.stream().forEach(rule -> toActive.addAll(rule.getActionToActive(currTick)));
            Map<String, List<Entity>> killAndCreat = new HashMap<>();
            killAndCreat.put("kill", new ArrayList<>());
            killAndCreat.put("creat", new ArrayList<>());
            m_entities.stream().forEach(entity -> toActive.stream().forEach(actionInterface -> (activeatActian(entity, actionInterface)).forEach((key, value) -> killAndCreat.merge(key, value, (list1, list2) -> {
                list1.addAll(list2);
                return list1;
            }))));

            map.moveEntities(m_entities);
            //killAndCreat.forEach((key, value) -> killCreat(key, value));
            map.deleteEntities(killAndCreat.get("kill"));
            map.createEntities(killAndCreat.get("creat"));
            m_entities.removeAll(killAndCreat.get("kill"));
            m_entities.addAll(killAndCreat.get("creat"));


            currTick++;
//            aTask.func(m_entities.stream().collect(Collectors.groupingBy(Entity::getName, Collectors.summingInt(e -> 1))));
//            aTask.setTick(currTick);
//            aTask.setSce(Duration.between(start, Instant.now()).getSeconds());
            if(isPause){
                synchronized (isPause) {
                    System.out.println("pause");
                    try {
                        synchronized (this) {
                            this.notifyAll();
                            isPause.wait();
                        }
                        System.out.println("resume");
                    } catch (InterruptedException e) {
                        System.out.println("leave");
                        isSimulationEnded = true;
                        Platform.runLater(() -> isFines.set(true));
                        isPause = false;
                        return;
                        // Handle interruption if needed
                    }
                }
            }
            if(Thread.currentThread().isInterrupted()){
                System.out.println("leave");
                isSimulationEnded = true;
                Platform.runLater(() -> isFines.set(true));
                return;
            }
            //aTask.run();
            //Thread.currentThread().isInterrupted();
        }
        Platform.runLater(() -> isFines.set(true));
        isSimulationEnded = true;
    }

    public void setException(String exception){
        this.exception = exception;
    }

    public String getException(){
        return this.exception;
    }

    public void killCreat(String killCreat,List<Entity> toKillOrCreat){
        if(killCreat.equals("kill")){
            m_entities.removeAll(toKillOrCreat);
        }else{
            m_entities.addAll(toKillOrCreat);
        }

        //toKill.stream().forEach(entity -> m_entities.remove(entity));
    }

    public Map<String, List<Entity>> activeatActian(Entity entity, ActionInterface actionInterface){
        try {
            if(actionInterface.getEntityName().equals(entity.getName())){
                return actionInterface.activateAction(entity, currTick, new ArrayList<>(Arrays.asList(entity)));
            }
            return new HashMap<>();
        }catch (InvalidValue e){
            throw new InvalidValue(e.getMessage() + ". referred to in rule " + actionInterface.getRuleName());
        }
//        List<Entity> secondary;
//        try {
//            if (actionInterface.getEntityName().equals(entity.getName())) {
//                if (actionInterface.isSecondaryAll()) {
//                    secondary = m_entities.stream().filter(entity1 -> entity1.getName() == actionInterface.getSecondaryName()).collect(Collectors.toList());
//                    return actionInterface.activateAction(entity, secondary);
//                } else {
//                    Random random = new Random();
//                    random.nextInt();
//                    List<Entity> EntitiesOfSecondaryType = m_entities.stream().filter(entity1 -> entity1.getName() == actionInterface.getSecondaryName()).filter(entity1 -> (actionInterface.getCondition()).getBoolValue(entity1)).collect(Collectors.toList());
//                    secondary = IntStream.range(0, actionInterface.getCountForSecondaryEntities()).mapToObj(i -> EntitiesOfSecondaryType.get(random.nextInt(EntitiesOfSecondaryType.size()))).limit(EntitiesOfSecondaryType.size()).collect(Collectors.toList());
//                    //secondary = temp.stream().mapToObj(i -> temp.get(random.nextInt(temp.size()))).limit(actionInterface.getCountForSecondaryEntities())
//                    return actionInterface.activateAction(entity, secondary);
//                }
//            }
//            return actionInterface.activateAction(entity, new ArrayList<>());
//        }catch (InvalidValue e){
//            throw new InvalidValue(e.getMessage() + ". referred to in rule " + actionInterface.getRuleName());
//        }
    }

    public String getSimulationTime(){
//        String res = simulationTime;
//        if(res == null){
//            res = "The simulation haven't ran yet";
//            //throw new RuntimeException("Didn't run the simulation");
//        }
//        return res;
        return simulationTime;
    }

    //public void setEnviroment(String name, )

    public DTOSimulationDetailsPostRun getPostRunData(){
        if(simulationTime == null){
            throw new RuntimeException("The simulation haven't ran yet");
        }

        DTOSimulationDetailsPostRun simulationDetailsPostRun = new DTOSimulationDetailsPostRun();

        final Map<String, Integer> entityAmountPostRun = m_entities.stream().collect(Collectors.groupingBy(entity -> entity.getName(), Collectors.summingInt(e -> 1)));
        List<DTOEntityPostRun> entityPostRuns = m_entitiesDifenichan.values().stream().map(entityDifenichan -> new DTOEntityPostRun(entityDifenichan.getName(), entityDifenichan.getAmount(), entityAmountPostRun.get(entityDifenichan.getName()))).collect(Collectors.toList());
        simulationDetailsPostRun.setEntitiesPostRuns(entityPostRuns);
        Map<String, List<DTOEntityHistogram>>entitiesHistogram = m_entitiesDifenichan.keySet().stream().collect(Collectors.toMap(entityDifenichanName -> entityDifenichanName,
                entityDifenichanName -> m_entities.stream()
                        .filter(entity -> entity.getName().equals(entityDifenichanName))
                        .map(entity -> entity.makeDtoEntity())
                        .collect(Collectors.toList())
        ));
        //Map<String, List<DTOEntityHistogram>>entitiesHistogram = m_entities.stream().map(entity -> entity.makeDtoEntity()).collect(Collectors.groupingBy(entityHistogram -> entityHistogram.getName()));
        simulationDetailsPostRun.setEntitiesHistogram(entitiesHistogram);


        Map<String, DTOEntitysProperties> entitysPropertiesMap = m_entitiesDifenichan.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entity -> entity.getValue().makeDtoEntitysProperties()));
        simulationDetailsPostRun.setEntitysProperties(entitysPropertiesMap);

        Map<String, List<Float>> propertyChangeByTick = new HashMap<>();
        for(Entity entity : m_entities){
            entity.getProperties().values().stream().forEach(propertyInterface -> propertyChangeByTick.put(entity.getName() + "_" + propertyInterface.getName(), addPropertyChangedAv(propertyChangeByTick.get(entity.getName() + "_" + propertyInterface.getName()), propertyInterface.getDeltaTicksChangedValueAve())));
        }
        simulationDetailsPostRun.setPropertyChangeByTick(propertyChangeByTick);

        Map<String, Pair<Float, Integer>> avPropertyValue = new HashMap<>();
        m_entities.stream().forEach(entity -> entity.getProperties().values().stream().filter(propertyInterface -> propertyInterface.getType() == propertyType.FLOAT || propertyInterface.getType() == propertyType.INT).forEach(propertyInterface -> avPropertyValue.put(entity.getName() + "_" + propertyInterface.getName(), makeNewAv(avPropertyValue.get(entity.getName() + "_" + propertyInterface.getName()), (Float)propertyInterface.getValue()))));
        simulationDetailsPostRun.setAvPropertyValue(avPropertyValue);

        simulationDetailsPostRun.setNumOfEntitiesPerTick(numOfEntitiesPerTick);


        return simulationDetailsPostRun;
    }

    public DTOSimulationDetailsPostRun getPostRunData2(){
        DTOSimulationDetailsPostRun simulationDetailsPostRun = new DTOSimulationDetailsPostRun();

        //////////////////////////////////////////////////////////////////////////////////

        Map<String, Integer> entityAmountPostRun = new HashMap<>();

        for(Entity entity : m_entities){
            if(!entityAmountPostRun.containsKey(entity.getName())){
                entityAmountPostRun.put(entity.getName(), 1);
            }
            else{
                entityAmountPostRun.put(entity.getName(), entityAmountPostRun.get(entity.getName()) + 1);
            }
        }

        for(EntityDifenichan entityDifenichan : m_entitiesDifenichan.values()){
            simulationDetailsPostRun.addEntityPostRun(new DTOEntityPostRun(entityDifenichan.getName(), entityDifenichan.getAmount(), entityAmountPostRun.get(entityDifenichan.getName())));
            //entityPostRuns.add(new DTOEntityPostRun(entityDifenichan.getName(), entityDifenichan.getAmount(), entityAmountPostRun.get(entityDifenichan.getName())));
        }

        ////////////////////////////////////////////////////////////////////////
        for(Entity entity : m_entities){
            simulationDetailsPostRun.addEntitiesHistigram(entity.makeDtoEntity());
//            if(!entitiesHistogram.containsKey(entity.getName())){
//                List<DTOEntityHistogram> temp = new ArrayList<>();
//                temp.add(entity.makeDtoEntity());
//                entitiesHistogram.put(entity.getName(), temp);
//            }
//            else{
//                List<DTOEntityHistogram> temp = entitiesHistogram.get(entity.getName());
//                temp.add(entity.makeDtoEntity());
//                //entitiesHistogram.put(entity.getName(), temp);    // ???
//            }
        }


        ////////////////////////////////////
        //DTOHistogram logic
//        String entity = "", property = "";
//        Map<Object, Integer> histogram = new HashMap<>();
//
//        for(DTOEntityHistogram entityHistogram : entitiesHistogram.get(entity)){
//            if(!histogram.containsKey(entityHistogram.getProperty(property).getValue())){
//                histogram.put(entityHistogram.getProperty(property).getValue(), 1);
//            }
//            else{
//                histogram.put(entityHistogram.getProperty(property).getValue(), histogram.get(entityHistogram.getProperty(property).getValue()) + 1);
//            }
//        }

        return simulationDetailsPostRun;
    }

    public DTOSimulationDetails getSimulationDetails(){
        DTOSimulationDetails DTO = new DTOSimulationDetails();

        for(EntityDifenichan entity : m_entitiesDifenichan.values()){
            DTO.addEntity(entity.makeDtoEntity());
        }

        for(Rule rule : m_rules){
            DTO.addRule(rule.makeDtoRule());
        }

        DTO.setEnvironments(getEnvironmentDetails());

        DTO.setGridSize(m_rows, m_cols);

        DTOTerminationData terminationData = new DTOTerminationData();
        if(m_ticks.getType() != null){
            terminationData.putData(terminationType.TICKS.toString(), ((Integer)m_ticks.getInt()).toString());
            //DTO.(terminationType.TICKS, m_ticks.getInt()));
        }

        if(m_secondToWork.getType() != null){
            terminationData.putData(terminationType.SECOND.toString(), ((Integer)m_secondToWork.getInt()).toString());
            //DTO.addTermination(new DTOTerminationData(terminationType.SECOND, m_secondToWork.getInt()));
        }
        DTO.setTermination(terminationData);

        return DTO;
    }

    public List<DTOEnvironmentVariables> getEnvironmentDetails(){
        List<DTOEnvironmentVariables> DTOList = new ArrayList<>();

        for(EnvironmentDifenichan environmentDifenichan : m_environmentsDifenichen.values()){
            DTOList.add(environmentDifenichan.makeDtoEnvironment());
        }

        return DTOList;
    }

    public int loadFile(String xmlFile)throws NoSuchFileException , UnsupportedFileTypeException, allReadyExistsException, InvalidValue, JAXBException, FileNotFoundException {
        PRDWorld xmlWorld = new PRDWorld();

        //load file
        Path path = Paths.get(xmlFile);
        if (Files.exists(path) && Files.isRegularFile(path)) {
            // Check if the file extension is .xml
            String fileName = path.getFileName().toString();
            if (!fileName.endsWith(".xml")) {
                throw new UnsupportedFileTypeException("Not an XML file: " + xmlFile);
            }
        } else {
            throw new NoSuchFileException("File does not exist: " + xmlFile);
        }

        try {
            InputStream inputStream = new FileInputStream(new File(xmlFile));
            xmlWorld = deserializeFrom(inputStream);
        } catch (JAXBException | FileNotFoundException e) {
            throw e;
        }

        m_cols = xmlWorld.getPRDGrid().getColumns();
        m_rows = xmlWorld.getPRDGrid().getRows();
        if(m_rows > 100 || m_rows < 10 || m_cols > 100 || m_cols < 10){
            throw new InvalidValue("Size of grid is not valid");
        }

        //entitys
        for(PRDEntity e : xmlWorld.getPRDEntities().getPRDEntity()){
            m_entitiesDifenichan.put(e.getName(), new EntityDifenichan(e));
        }
        //environment
        for(PRDEnvProperty p : xmlWorld.getPRDEnvironment().getPRDEnvProperty()){
            if(m_environmentsDifenichen.containsKey(p.getPRDName())){
                throw new allReadyExistsException("environment variables " + p.getPRDName() + " all ready exists.");
            }
            m_environmentsDifenichen.put(p.getPRDName(), new EnvironmentDifenichan(p));
        }

        //environment values
//        for(PRDEnvProperty envProperty : xmlWorld.getPRDEvironment().getPRDEnvProperty()){
//            if(m_environments.containsKey(envProperty.getPRDName())){
//                throw new allReadyExistsException("enviroments varuble " + envProperty.getPRDName() + " all ready exists");
//            }
//            if(envProperty.getType().equals("decimal")) {
//                m_environments.put(envProperty.getPRDName(), new DecimalProperty(envProperty));
//            } else if(envProperty.getType().equals("float")){
//                m_environments.put(envProperty.getPRDName(), new FloatProperty(envProperty));
//            } else if (envProperty.getType().equals("string")) {
//                m_environments.put(envProperty.getPRDName(), new StringProperty(envProperty));
//            } else if (envProperty.getType().equals("boolean")) {
//                m_environments.put(envProperty.getPRDName(), new BooleanProperty(envProperty));
//            }
//        }

        util = new Utilites(m_environments, m_entitiesDifenichan, m_environmentsDifenichen, m_entities);
        //util.Init(m_environments, m_entitiesDifenichan, m_environmentsDifenichen);

        //rules
        for(PRDRule rule : xmlWorld.getPRDRules().getPRDRule()){
            m_rules.add(new Rule(rule, util));
        }

        m_ticks = new expression();
        m_secondToWork = new expression();
        Optional<List<Object>> secondOrTicks = Optional.ofNullable((List<Object>) xmlWorld.getPRDTermination().getPRDBySecondOrPRDByTicks());
        secondOrTicks.ifPresent(t -> getTermination(t));

//        m_ticks = new expression();
//        Optional<Integer> time = Optional.ofNullable().ifPresent(t -> t.getCount()))));
//        time.ifPresent((t) -> m_ticks.setValue(t));
//
//        m_secondToWork = new expression();
//        Optional<Integer> second = Optional.ofNullable(((PRDBySecond)xmlWorld.getPRDTermination().getPRDBySecondOrPRDByTicks().get(1)).getCount());
//        second.ifPresent((s) -> m_secondToWork.setValue(s));

        if(m_ticks.getType() == null && m_secondToWork.getType() == null && xmlWorld.getPRDTermination().getPRDByUser() == null){
            throw new InvalidValue("No termination method was added");
        }

        return xmlWorld.getPRDThreadCount();
    }

    private void getTermination(List<Object> secondOrTicks){
        if(secondOrTicks.size() == 0){
            return;
        }
        if(secondOrTicks.size() == 1) {
            if (secondOrTicks.get(0) instanceof PRDByTicks) {
                m_ticks.setValue(((PRDByTicks) (secondOrTicks.get(0))).getCount());
            }
            if (secondOrTicks.get(0) instanceof PRDBySecond) {
                m_secondToWork.setValue(((PRDBySecond) (secondOrTicks.get(0))).getCount());
            }
            return;
        }
        if(secondOrTicks.size() == 2) {
            if (secondOrTicks.get(0) instanceof PRDByTicks) {
                m_ticks.setValue(((PRDByTicks) (secondOrTicks.get(0))).getCount());
                m_secondToWork.setValue(((PRDBySecond) (secondOrTicks.get(1))).getCount());

            }
            if (secondOrTicks.get(0) instanceof PRDBySecond) {
                m_secondToWork.setValue(((PRDBySecond) (secondOrTicks.get(0))).getCount());
                m_ticks.setValue(((PRDByTicks) (secondOrTicks.get(1))).getCount());
            }
        }
    }

    public void bindToWhenFines(javafx.beans.property.BooleanProperty isFines){
        isFines.bind(this.isFines);
    }

    public void addEnvironmentDto(DTOEnvironmentVariables dtoEnvironmentVariables) throws InvalidValue{
        if(m_environmentsDifenichen.containsKey(dtoEnvironmentVariables.getVariableName())){
            m_environmentsDifenichen.get(dtoEnvironmentVariables.getVariableName()).setWithDto(dtoEnvironmentVariables);
        }
        else{
            throw new InvalidValue("Got a non exising environment variables name");
        }
    }

    public void addEnvironmentValue(String name, Object value){
        if(m_environmentsDifenichen.containsKey(name)){
            m_environmentsDifenichen.get(name).setWithString(value.toString());
        }else{
            throw new InvalidValue("Got a non exising environment variables name");
        }
    }

    public void addPopulationToEntity(String entityName, int population){
        if(m_entitiesDifenichan.containsKey(entityName)){
            Integer sumPopulation = 0;
            for(EntityDifenichan entityDifenichan : m_entitiesDifenichan.values()){
                sumPopulation += entityDifenichan.getPopulation();
            }
            if(sumPopulation + population > m_rows * m_cols){
                throw new InvalidValue("Sum of entities population is to big for the size of the grid");
            }
            m_entitiesDifenichan.get(entityName).setPopulation(population);
        }else{
            throw new InvalidValue("Got a non exising entity variables name");
        }
    }

    public void moveOneStep(Boolean isPause){
        synchronized (isPause){
            if(isPause){
                synchronized (this){
                    isPause.notifyAll();
                    try {
                        this.wait(2000);
                    }catch (InterruptedException e){

                    }
                }
            }
            throw new InvalidValue("not pause, can't run manually");
        }
    }

    public DTOMap getMap(Boolean isPause){
        moveOneStep(isPause);
        DTOMap map = new DTOMap();
        map.setMapSize(this.map.getRows(), this.map.getCols());
        map.setMap(this.map.getMap(), this.map.getRows(), this.map.getCols());
        return map;
//        synchronized (isPause){
//            if(isPause){
//                synchronized (this){
//                    isPause.notifyAll();
//                    try {
//                        this.wait(2000);
//                    }catch (InterruptedException e){
//
//                    }
//                }
//                DTOMap map = new DTOMap();
//                map.setMapSize(this.map.getRows(), this.map.getCols());
//                map.setMap(this.map.getMap(), this.map.getRows(), this.map.getCols());
//                return map;
//            }
//            throw new InvalidValue("not pause, can't get map");
//        }
    }

    public List<Float> addPropertyChangedAv(List<Float> listPropertyChange, Float propertyChange){
        if(listPropertyChange != null){
            listPropertyChange.add(propertyChange);
        }else{
            listPropertyChange = new ArrayList<>(Arrays.asList(propertyChange));
        }
        return listPropertyChange;
    }

    public Pair<Float, Integer> makeNewAv(Pair<Float, Integer> p, Float n){
        if(p != null){
            Float av = p.getKey();
            Integer size = p.getValue();
            return new Pair<>((av * size + n) / size + 1, size + 1);
        }else{
            return new Pair<>(n, 1);
        }
    }

    public DTODataForReRun getDataForRerun(){
        DTODataForReRun dataForReRun = new DTODataForReRun();

        dataForReRun.setEntitiesPopulation(m_entitiesDifenichan.values().stream().collect(Collectors.toMap(entityDifenichan -> entityDifenichan.getName(), entityDifenichan -> entityDifenichan.getPopulation())));
        dataForReRun.setEnvironmentsValues(m_environmentsDifenichen.values().stream().filter(environmentDifenichan -> !environmentDifenichan.isRandom()).collect(Collectors.toMap(environmentDifenichan -> environmentDifenichan.getName(), environmentDifenichan -> environmentDifenichan.getInit().getValue())));

        return dataForReRun;
    }

//    public void get(){
//        m_entities.stream();
//        Map<String, List<Float>> propertyChangeByTick = new HashMap<>();
//        for(Entity entity : m_entities){
//            entity.getProperties().values().stream().forEach(propertyInterface -> propertyChangeByTick.put(entity.getName() + "_" + propertyInterface.getName(), addPropertyChangedAv(propertyChangeByTick.get(entity.getName() + "_" + propertyInterface.getName()), propertyInterface.getDeltaTicksChangedValueAve())));
//        }
//
//        Map<String, Pair<Float, Integer>> avPropertyValue = new HashMap<>();
//        m_entities.stream().forEach(entity -> entity.getProperties().values().stream().filter(propertyInterface -> propertyInterface.getType() == propertyType.FLOAT || propertyInterface.getType() == propertyType.INT).forEach(propertyInterface -> avPropertyValue.put(entity.getName() + "_" + propertyInterface.getName(), makeNewAv(avPropertyValue.get(entity.getName() + "_" + propertyInterface.getName()), (Float)propertyInterface.getValue()))));
//
//        numOfEntitiesPerTick;
//
//        //m_entitiesDifenichan.values().stream().collect(Collectors.toMap(entityDifenichan -> entityDifenichan.getName(), entityDifenichan -> entityDifenichan.getPopulation()));
//        //m_environmentsDifenichen.values().stream().filter(environmentDifenichan -> !environmentDifenichan.isRandom()).collect(Collectors.toMap(environmentDifenichan -> environmentDifenichan.getName(), environmentDifenichan -> environmentDifenichan.getInit().getValue()));
//
//
//        for(EntityDifenichan entityDifenichan : m_entitiesDifenichan){
//            for(Entity entity : m_entities){
//                if(entity.equals(entityDifenichan)){
//                    entity.getProperties().values().stream()
//                }
//            }
//        }
//        Map<String, List<DTOEntityHistogram>>entitiesHistogram = m_entitiesDifenichan.values().stream().forEach(ent -> m.put(ent.getPropertys().keySet().stream().map(proper -> ent + "_" + proper).toString(),
//                proper -> m_entities.stream().filter(entity -> entity.getName().equals(ent))
//                        .map(e -> e.getProperties().values().stream()
//                        .filter(pro -> pro.getName().equals(proper))
//                        .map(valueDelta -> valueDelta.getDeltaTicksChangedValueAve()))
//                        .collect(Collectors.toList()))))
//        );
//    }

    public List<DTOEnvironmentVariablesValues> setSimulation()throws InvalidValue{
        for(EnvironmentDifenichan entityDifenichan : m_environmentsDifenichen.values()){
            if(entityDifenichan.getType() == expressionType.INT) {         //.equals("decimal")
                m_environments.put(entityDifenichan.getName(), new DecimalProperty(entityDifenichan));
            } else if(entityDifenichan.getType() == expressionType.FLOAT){     //.equals("float")
                m_environments.put(entityDifenichan.getName(), new FloatProperty(entityDifenichan));
            } else if (entityDifenichan.getType() == expressionType.STRING) {      //.equals("string")
                m_environments.put(entityDifenichan.getName(), new StringProperty(entityDifenichan));
            } else if (entityDifenichan.getType() == expressionType.BOOL) {        //.equals("boolean")
                m_environments.put(entityDifenichan.getName(), new BooleanProperty(entityDifenichan));
            }
        }

        List<DTOEnvironmentVariablesValues> environmentVariablesValues = new ArrayList<>();
        for(PropertyInterface env : m_environments.values()){
            environmentVariablesValues.add(env.makeDtoEnvironment());
        }

        for(EntityDifenichan entityDifenichan : m_entitiesDifenichan.values()){
            for(int i = 0; i < entityDifenichan.getAmount(); i++){
                m_entities.add(new Entity(entityDifenichan));
            }
        }

        return environmentVariablesValues;
    }

    private static PRDWorld deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (PRDWorld) u.unmarshal(in);
    }
}
