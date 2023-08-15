package gameEngien.world;

import DTO.*;
import gameEngien.UnsupportedFileTypeException;
import gameEngien.allReadyExistsException;
import gameEngien.entity.Entity;
import gameEngien.entity.EntityDifenichan;
import gameEngien.generated.*;
import gameEngien.property.*;
import gameEngien.property.propertyInterface.PropertyInterface;
import gameEngien.rule.Rule;
import gameEngien.rule.action.actionInterface.ActionInterface;
import gameEngien.rule.action.increase.addValue;
import gameEngien.rule.action.increase.calculation;
import gameEngien.rule.action.increase.exprecn;
import gameEngien.rule.action.increase.exprecnType;
import gameEngien.utilites.Utilites;
import org.omg.CORBA.DynAnyPackage.InvalidValue;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import javax.naming.NoInitialContextException;
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
    private exprecn m_ticks = null;
    private exprecn m_secondToWork = null;
    private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy | hh.mm.ss");
    private String simulationTime = null;

    private final static String JAXB_XML_GAME_PACKAGE_NAME = "gameEngien.generated";


    public void start(){
        PropertyInterface p = new DecimalProperty("pro", 100, 0, 200);
        PropertyInterface p2 = new DecimalProperty("p2", 100, 0, 10000);
        ActionInterface a = new addValue("entity", "pro", "3");
        ActionInterface a2 = new calculation("entity", "p2", "environment(ep1)",
               "environment(ep2)");
        Entity e =new Entity("entity");

        m_rules.add(new Rule("rule", 1, 1));
        m_rules.add(new Rule("rule", 70, 1));
        m_entities.add(e);
        m_entities.get(0).addProperty(p);
        m_entities.get(0).addProperty(p2);
        m_rules.get(0).addAction(a);
        m_rules.get(1).addAction(a2);

        PropertyInterface env = new DecimalProperty("ep1", 50, 0, 200);
        PropertyInterface env2 = new DecimalProperty("ep2", 4, 0, 200);
        m_environments.put(env.getName(), env);
        m_environments.put(env2.getName(), env2);
        Utilites.Init(m_environments, m_entitiesDifenichan, m_environmentsDifenichen);


    }

    public void startSimolesan()throws InvalidValue{
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
        int currTick = 0, currTime = 0;
        Instant start = Instant.now();
        while ((m_ticks.getType() == null || currTick < m_ticks.getInt()) && (m_secondToWork == null || Duration.between(start, Instant.now()).getSeconds() < m_secondToWork.getInt())){
            for(Rule r : m_rules) {
                if(currTick % r.getTick() == 0 && random.nextDouble() < r.getProbability()) {
                    for (Entity entity : m_entities) {
                        if (r.activeRule(entity)) {
                            toRemove.add(entity);
                            break;
                        }
                    }
                }
            }
            for(Entity entity : toRemove){
                m_entities.remove(entity);
            }
            currTick++;
        }
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

        if(m_ticks.getType() != null){
            DTO.addTermination(new DTOTerminationData(terminationType.TICKS, m_ticks.getInt()));
        }

        if(m_secondToWork.getType() != null){
            DTO.addTermination(new DTOTerminationData(terminationType.SECOND, m_secondToWork.getInt()));
        }

        return DTO;
    }

    public List<DTOEnvironmentVariables> getEnvironmentDetails(){
        List<DTOEnvironmentVariables> DTOList = new ArrayList<>();

        for(EnvironmentDifenichan environmentDifenichan : m_environmentsDifenichen.values()){
            DTOList.add(environmentDifenichan.makeDtoEnvironment());
        }

        return DTOList;
    }

    public void loadFile(String xmlFile)throws NoSuchFileException , UnsupportedFileTypeException, allReadyExistsException, InvalidValue, JAXBException, FileNotFoundException {
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

        //entitys
        for(PRDEntity e : xmlWorld.getPRDEntities().getPRDEntity()){
            m_entitiesDifenichan.put(e.getName(), new EntityDifenichan(e));
        }
        //environment
        for(PRDEnvProperty p : xmlWorld.getPRDEvironment().getPRDEnvProperty()){
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

        Utilites.Init(m_environments, m_entitiesDifenichan, m_environmentsDifenichen);

        //rules
        for(PRDRule rule : xmlWorld.getPRDRules().getPRDRule()){
            m_rules.add(new Rule(rule));
        }

        m_ticks = new exprecn();
        Optional<Integer> time = Optional.ofNullable(((PRDByTicks)(xmlWorld.getPRDTermination().getPRDByTicksOrPRDBySecond().get(0))).getCount());
        time.ifPresent((t) -> m_ticks.setValue(t));

        m_secondToWork = new exprecn();
        Optional<Integer> second = Optional.ofNullable(((PRDBySecond)xmlWorld.getPRDTermination().getPRDByTicksOrPRDBySecond().get(1)).getCount());
        second.ifPresent((s) -> m_secondToWork.setValue(s));

        if(m_ticks.getType() == null && m_secondToWork.getType() == null){
            throw new InvalidValue("No termination method was added");
        }
    }

    public void addEnvironmentDto(DTOEnvironmentVariables dtoEnvironmentVariables) throws InvalidValue{
        if(m_environmentsDifenichen.containsKey(dtoEnvironmentVariables.getVariableName())){
            m_environmentsDifenichen.get(dtoEnvironmentVariables.getVariableName()).setWithDto(dtoEnvironmentVariables);
        }
        else{
            throw new InvalidValue("Got a non exising environment variables name");
        }
    }

    public List<DTOEnvironmentVariablesValues> setSimulation()throws InvalidValue{
        for(EnvironmentDifenichan entityDifenichan : m_environmentsDifenichen.values()){
            if(entityDifenichan.getType() == exprecnType.INT) {         //.equals("decimal")
                m_environments.put(entityDifenichan.getName(), new DecimalProperty(entityDifenichan));
            } else if(entityDifenichan.getType() == exprecnType.FLOAT){     //.equals("float")
                m_environments.put(entityDifenichan.getName(), new FloatProperty(entityDifenichan));
            } else if (entityDifenichan.getType() == exprecnType.STRING) {      //.equals("string")
                m_environments.put(entityDifenichan.getName(), new StringProperty(entityDifenichan));
            } else if (entityDifenichan.getType() == exprecnType.BOOL) {        //.equals("boolean")
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
