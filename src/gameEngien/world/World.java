package gameEngien.world;

import gameEngien.entity.Entity;
import gameEngien.entity.EntityDifenichan;
import gameEngien.generated.*;
import gameEngien.property.DecimalProperty;
import gameEngien.property.propertyInterface.PropertyInterface;
import gameEngien.rule.Rule;
import gameEngien.rule.action.actionInterface.ActionInterface;
import gameEngien.rule.action.increase.Increase;
import gameEngien.rule.action.increase.calculation;
import gameEngien.utilites.Utilites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World {
    private List<Rule> m_rules = new ArrayList<>();
    private List<Entity> m_entities = new ArrayList<>();

    private List<EntityDifenichan> m_entitiesDifenichan = new ArrayList<>();
    private Map<String, PropertyInterface> m_environments = new HashMap<>();
    private int m_time = 0;

    public void start(){
        PropertyInterface p = new DecimalProperty("pro", 100, 0, 200);
        PropertyInterface p2 = new DecimalProperty("p2", 100, 0, 10000);
        ActionInterface a = new Increase("entity", "pro", "3");
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
        Utilites.Init(m_environments);


    }

    public void startSimolesan(){
        for(int i = 0; i < 100; ++i){
            for(Entity entity : m_entities){
                for(Rule r : m_rules){
                    if (r.activeRule(entity, i)){
                        m_entities.remove(entity);
                    }
                }
            }
        }
    }

    //public void setEnviroment(String name, )

    public void loadFile(PRDWorld w){
        for(PRDEntity e : w.getPRDEntities().getPRDEntity()){
            m_entitiesDifenichan.add(new EntityDifenichan(e));
        }
        for(PRDRule rule : w.getPRDRules().getPRDRule()){
            m_rules.add(new Rule(rule));
        }

        for(PRDEnvProperty envProperty : w.getPRDEvironment().getPRDEnvProperty()){
            if(envProperty.getType() == "decimal") {
                m_environments.put(envProperty.getPRDName(), new DecimalProperty(envProperty));
            }
            else{

            }
        }

        Utilites.Init(m_environments);
    }

    public void setSimulation(){
        for(EntityDifenichan entityDifenichan : m_entitiesDifenichan){
            for(int i = 0; i < entityDifenichan.getAmount(); i++){
                m_entities.add(new Entity(entityDifenichan));
            }
        }
    }

}
