package Engine.utilites;

import Engine.InvalidValue;
import Engine.world.entity.Entity;
import Engine.world.entity.EntityDifenichan;
import Engine.world.entity.property.EnvironmentDifenichan;
import Engine.world.entity.property.PropertyInterface;
import Engine.world.expression.expressionType;
import Engine.world.map;

import java.util.*;
import java.util.stream.Collectors;

public class Utilites {
    //private static List<Entity> m_entities;
    private Map<String, EnvironmentDifenichan> m_environmentsDifenichan;
    private Map<String, PropertyInterface> m_environments;
    private Map<String, EntityDifenichan> m_entityDifenichan;
    private List<Entity> m_entities;
    private map m_simulationMap;

    public Utilites(Map<String, PropertyInterface> environments, Map<String, EntityDifenichan> entityDifenichan, Map<String, EnvironmentDifenichan> environmentsDifenichan, List<Entity> entities){
        m_environments = environments;
        m_entityDifenichan = entityDifenichan;
        m_environmentsDifenichan = environmentsDifenichan;
        //m_simulationMap = simulationMap;
        m_entities = entities;
    }

    public void Init(Map<String, PropertyInterface> environments, Map<String, EntityDifenichan> entityDifenichan, Map<String, EnvironmentDifenichan> environmentsDifenichan){
        m_environments = environments;
        m_entityDifenichan = entityDifenichan;
        m_environmentsDifenichan = environmentsDifenichan;
    }

    public final Object environment(String name) {
        if(!m_environments.containsKey(name)){
            throw new InvalidValue("bad arg in function environment");
        }
        return m_environments.get(name).getValue();
    }

    public boolean isEnvironmentExist(String name) {
        return m_environmentsDifenichan.containsKey(name);// != null ? true : false;
    }

    public expressionType getEnvironmentType(String name){
        return m_environmentsDifenichan.get(name).getType();
    }

    public int random(int num) {
        Random random = new Random();
        return random.nextInt(num + 1);
    }
    public int random(String numString) {
        Random random = new Random();
        int num = 1;
        try {
            num = Integer.parseInt(numString);
        } catch (NumberFormatException e) {
            // Not an int
        }
        return random.nextInt(num);
    }

    public final EntityDifenichan getEntityDifenichan(String name){
        return m_entityDifenichan.get(name);
    }

    public boolean isEntityDifenichanExists(String name){
        return m_entityDifenichan.containsKey(name);
    }

    public List<Entity> getEntitiesByName(String entityName){
        return m_entities.stream().filter(entity -> entity.getName().equals(entityName)).collect(Collectors.toList());
    }

    public boolean isPropertyExists(String entityName, String propertyName){
        if(!m_entityDifenichan.containsKey(entityName)){
            return false;
        }
        return m_entityDifenichan.get(entityName).getPropertys().containsKey(propertyName);
    }

    public float percent(float hole, float percent){
        return hole * percent / 100f;
    }

}
