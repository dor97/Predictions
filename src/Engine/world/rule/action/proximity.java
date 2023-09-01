package Engine.world.rule.action;

import Engine.InvalidValue;
import Engine.generated.PRDAction;
import Engine.utilites.Utilites;
import Engine.world.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class proximity extends action {

    //private String m_entityName;
    private String targetName;
    private String sourceName;
    private int envDepth;
    private List<ActionInterface> actions;
    private Utilites m_util;
    //private int m_currTick;

    public proximity(PRDAction action, Utilites util, String ruleName) {
        super(action, util, ruleName);
        //m_entityName = action.getEntity();
        sourceName = action.getPRDBetween().getSourceEntity();
        targetName = action.getPRDBetween().getTargetEntity();
        envDepth = action.getPRDEnvDepth().getOf().equals("1") ? 1 : 2;
        m_util = util;
        action.getPRDActions().getPRDAction();

        actions = new ArrayList<>();
        actionFactory factory = new actionFactory();
        for (PRDAction thanAction : action.getPRDActions().getPRDAction()) {
            actions.add(factory.makeAction(thanAction, util, getRuleName()));
        }
    }

    @Override
    public Map<String, List<Entity>> activateAction(Entity entity, int currTick) {
        m_currTick = currTick;
        List<Entity> targets = m_util.getEntitiesByName(targetName);
        //List<Entity> targets = targetAndSecondaryEntities.stream().filter(targetEntity -> targetEntity.getName().equals(targetName)).collect(Collectors.toList());
        //List<Entity> secondaryEntities = secondaryEntities.stream().filter(secondaryEntity -> !secondaryEntity.getName().equals(targetName)).collect(Collectors.toList());
        final List<Entity> secondaryEntities = (getCountForSecondaryEntities() != 0 && !getSecondaryName().equals(sourceName) && !getSecondaryName().equals(targetName)) ? getSecondaryEntities() : null;
//        if(getCountForSecondaryEntities() != 0 && !getSecondaryName().equals(sourceName) && !getSecondaryName().equals(targetName)){
//            secondaryEntities = getSecondaryEntities();
//        }

        Map<String, List<Entity>> killAndCreat = new HashMap<>();
        //if (secondaryEntities == null || secondaryEntities.size() == 0) {
            targets.stream().forEach(targetEntity -> loopThroughEntities(entity, targetEntity, secondaryEntities).forEach((key, value) -> killAndCreat.merge(key, value, (list1, list2) -> {
                list1.addAll(list2);
                return list1;
            })));
        //}else{

        //}




        return killAndCreat;
    }

    private Map<String, List<Entity>> loopThroughEntities(Entity entity, Entity targetEntity, List<Entity> secondaryEntities) {
        Map<String, List<Entity>> killAndCreat = new HashMap<>();
        if(isNear(entity, targetEntity)){
            actions.stream().forEach(action -> activateAction(action, entity, targetEntity, secondaryEntities).forEach((key, value) -> killAndCreat.merge(key, value, (list1, list2) -> {
                list1.addAll(list2);
                return list1;
            })));
        }
        return killAndCreat;
    }

    private Map<String, List<Entity>> activateAction(ActionInterface action, Entity entity, Entity targetEntity, List<Entity> secondaryEntities){
        if(action.getEntityName().equals(entity.getName())){
            return action.activateAction(entity, m_currTick);
        }else if(action.getEntityName().equals(targetEntity.getName())){
            return action.activateAction(targetEntity, m_currTick);
        }else if(secondaryEntities != null && action.getEntityName().equals(getSecondaryName())){
            Map<String, List<Entity>> killAndCreat = new HashMap<>();
            secondaryEntities.stream().forEach(secondaryEntity -> action.activateAction(secondaryEntity, m_currTick).forEach((key, value) -> killAndCreat.merge(key, value, (list1, list2) -> {
                list1.addAll(list2);
                return list1;
            })));
            return killAndCreat;
        }
        throw new InvalidValue("In action proximity, entity in action list is different than source and target(and secondary)");
    }

    private boolean isNear(Entity entity, Entity targetEntity) {

        for (int i = -1 * envDepth; i <= 1 * envDepth; i++) {
            for (int j = -1 * envDepth; j <= 1 * envDepth; j++) {
                if (entity.getPosition().getX() == targetEntity.getPosition().getX() + i && entity.getPosition().getY() == targetEntity.getPosition().getY() + j) {
                    return true;
                }
            }
        }
        return false;
    }
}
