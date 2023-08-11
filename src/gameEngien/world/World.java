package gameEngien.world;

import DTO.DTOEnvironmentVariables;
import DTO.DTOSimulationDetails;
import DTO.DTOTerminationData;
import DTO.terminationType;
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
import gameEngien.utilites.Utilites;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class World implements Serializable {
    private List<Rule> m_rules = new ArrayList<>();
    private List<Entity> m_entities = new ArrayList<>();

    private Map<String, EntityDifenichan> m_entitiesDifenichan = new HashMap<>();
    private Map<String, PropertyInterface> m_environments = new HashMap<>();
    private Map<String, EnvironmentDifenichan> m_environmentsDifenichen = new HashMap<>();
    private exprecn m_ticks = null;
    private exprecn m_secondToWork = null;

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
        Utilites.Init(m_environments, m_entitiesDifenichan);


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

    //public void setEnviroment(String name, )

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

        for(PRDEnvProperty p : xmlWorld.getPRDEvironment().getPRDEnvProperty()){
            if(m_environmentsDifenichen.containsKey(p.getPRDName())){
                throw new allReadyExistsException("environment varuble " + p.getPRDName() + " all ready exists.");
            }
            m_environmentsDifenichen.put(p.getPRDName(), new EnvironmentDifenichan(p));
        }

        //environment values
        for(PRDEnvProperty envProperty : xmlWorld.getPRDEvironment().getPRDEnvProperty()){
            if(m_environments.containsKey(envProperty.getPRDName())){
                throw new allReadyExistsException("enviroments varuble " + envProperty.getPRDName() + " all ready exists");
            }
            if(envProperty.getType().equals("decimal")) {
                m_environments.put(envProperty.getPRDName(), new DecimalProperty(envProperty));
            } else if(envProperty.getType().equals("float")){
                m_environments.put(envProperty.getPRDName(), new FloatProperty(envProperty));
            } else if (envProperty.getType().equals("string")) {
                m_environments.put(envProperty.getPRDName(), new StringProperty(envProperty));
            } else if (envProperty.getType().equals("boolean")) {
                m_environments.put(envProperty.getPRDName(), new BooleanProperty(envProperty));
            }
        }

        Utilites.Init(m_environments, m_entitiesDifenichan);

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

    public void setSimulation(){
        for(EntityDifenichan entityDifenichan : m_entitiesDifenichan.values()){
            for(int i = 0; i < entityDifenichan.getAmount(); i++){
                m_entities.add(new Entity(entityDifenichan));
            }
        }
    }

    private static PRDWorld deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (PRDWorld) u.unmarshal(in);
    }

    public void checkIfEnvVariableIsValid(DTOEnvironmentVariables env_variable) {

        exprecn exp = new exprecn();
        String value = env_variable.getValue();
    }
}
