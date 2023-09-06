package Engine.world.rule.action;

import DTO.DTOActionData;
import Engine.InvalidValue;
import Engine.generated.PRDAction;
import Engine.utilites.Utilites;
import Engine.world.entity.Entity;
import org.omg.CORBA.OBJECT_NOT_EXIST;

import java.util.*;
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
        checkEntityAndPropertyExist();
    }

    private void checkEntityAndPropertyExist(){
        if(!m_util.isEntityDifenichanExists(sourceName)){
            throw new OBJECT_NOT_EXIST("In action set the entity " + sourceName + " does not exist.");
        }
        if(!m_util.isEntityDifenichanExists(targetName)){
            throw new OBJECT_NOT_EXIST("In action set the entity " + targetName + " does not exist.");
        }
        if(getCountForSecondaryEntities() != 0 && !m_util.isEntityDifenichanExists(getSecondaryName())){
            throw new OBJECT_NOT_EXIST("In action " + getActionName() + " the entity " + getSecondaryName() + " does not exist.");
        }
    }

    @Override
    public Map<String, List<Entity>> activateAction(Entity entity, int currTick, List<Entity> paramsForFuncs) {
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
        try {
            if (action.getEntityName().equals(entity.getName())) {
                if (secondaryEntities == null || secondaryEntities.size() == 0) { //TODO remove secondaryEntities.size() == 0 so not active if didnt find secondaries
                    return action.activateAction(entity, m_currTick, new ArrayList<>(Arrays.asList(entity, targetEntity)));
                } else {
                    Map<String, List<Entity>> killAndCreat = new HashMap<>();
                    secondaryEntities.stream().forEach(secondaryEntity -> action.activateAction(entity, m_currTick, new ArrayList<>(Arrays.asList(entity, targetEntity, secondaryEntity))).forEach((key, value) -> killAndCreat.merge(key, value, (list1, list2) -> {
                        list1.addAll(list2);
                        return list1;
                    })));
                    return killAndCreat;
                }
            } else if (action.getEntityName().equals(targetEntity.getName())) {
                if (secondaryEntities == null || secondaryEntities.size() == 0) { //TODO remove secondaryEntities.size() == 0 so not active if didnt find secondaries
                    return action.activateAction(targetEntity, m_currTick, new ArrayList<>(Arrays.asList(targetEntity, entity)));
                } else {
                    Map<String, List<Entity>> killAndCreat = new HashMap<>();
                    secondaryEntities.stream().forEach(secondaryEntity -> action.activateAction(entity, m_currTick, new ArrayList<>(Arrays.asList(targetEntity, entity, secondaryEntity))).forEach((key, value) -> killAndCreat.merge(key, value, (list1, list2) -> {
                        list1.addAll(list2);
                        return list1;
                    })));
                    return killAndCreat;
                }
            } else if (secondaryEntities != null && action.getEntityName().equals(getSecondaryName())) {
                Map<String, List<Entity>> killAndCreat = new HashMap<>();
                secondaryEntities.stream().forEach(secondaryEntity -> action.activateAction(secondaryEntity, m_currTick, new ArrayList<>(Arrays.asList(secondaryEntity, entity, targetEntity))).forEach((key, value) -> killAndCreat.merge(key, value, (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                })));
                return killAndCreat;
            }
            throw new InvalidValue("In action proximity, entity in action list is different than source and target(and secondary)");
        }catch (InvalidValue e){
            if(e.getEntityName() != null){
                if(!e.getEntityName().equals(sourceName) && !e.getEntityName().equals(targetName) && !e.getEntityName().equals(getSecondaryName())) {
                    throw e;
                }
            }
        }
        return new HashMap<>();
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

    @Override
    public DTOActionData makeActionDto(){
        DTOActionData actionData = new DTOActionData(getActionName());
        actionData.putData("entity", sourceName);
        actionData.putData("target", targetName);
        actionData.putData("envDepth", ((Integer)(envDepth)).toString());
        actionData.putData("actions", ((Integer)(actions.size())).toString());
        actionData.putData("secondary", getSecondaryName());

        return actionData;
    }
}
