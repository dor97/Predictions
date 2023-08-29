package Engine.world.rule.action;

import Engine.generated.PRDAction;
import Engine.utilites.Utilites;
import Engine.world.entity.Entity;
import Engine.world.entity.EntityDifenichan;
import Engine.world.expression.expressionWithFunc;

import java.util.*;
import java.util.stream.Collectors;

public class replace extends action{
    private String m_entityName;
    private String m_propertyName;
    private String toCreate;
    private Entity m_entity = null;
    private String mode;
    private Utilites m_util;

    public replace(PRDAction action, Utilites util, String ruleName){
        super(action, util, ruleName);
        m_entityName = action.getEntity();
        toCreate = action.getCreate();
        //actionName = action.getType();
        mode = action.getMode();
    }

    private Entity creatEntity(Entity entity){
        if(mode.equals("scratch")){
            return new Entity(m_util.getEntityDifenichan(toCreate));
        }
        else{
            return new Entity(m_util.getEntityDifenichan(toCreate), entity.getProperties());
        }
    }

    @Override
    public Map<String, List<Entity>> activateAction(Entity entity, int m_currTick){
        List<Entity> secondaryEntities = null;
        if(getCountForSecondaryEntities() != 0 && !getSecondaryName().equals(m_entityName)){
            secondaryEntities = getSecondaryEntities();
        }
        Map<String, List<Entity>> killAndCreat = new HashMap<>();
        if(secondaryEntities == null || secondaryEntities.size() == 0){
            killAndCreat.put("kill", new ArrayList<>(Arrays.asList(entity)));
            killAndCreat.put("creat", new ArrayList<>(Arrays.asList(creatEntity(entity))));
            //loopThroughEntities(entity);
        }else{
            if(m_entityName.equals(entity.getName())){
                killAndCreat.put("kill", new ArrayList<>(Arrays.asList(entity)));
                killAndCreat.put("creat", new ArrayList<>(Arrays.asList(creatEntity(entity))));
            }else {
                killAndCreat.put("kill", secondaryEntities);
                killAndCreat.put("creat", secondaryEntities.stream().map(secondaryEntity -> creatEntity(secondaryEntity)).collect(Collectors.toList()));

            }
        }
        return killAndCreat;
    }
}
