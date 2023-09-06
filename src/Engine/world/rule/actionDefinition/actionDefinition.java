package Engine.world.rule.actionDefinition;

import Engine.generated.PRDAction;
import Engine.utilites.Utilites;
import Engine.world.expression.expression;
import Engine.world.expression.expressionType;
import Engine.world.rule.action.single;

public class actionDefinition{
    private String actionName;
    private String entityName;
    private String m_ruleName;
    private String m_secondaryEntity;
    private Integer countForSecondaryEntities;
    private Boolean isSecondaryAll;
    private setDefinition condition;


    public actionDefinition(PRDAction action, String ruleName){
        actionName = action.getType();
        entityName = action.getEntity();
        if(actionName.equals("proximity")){
            entityName = action.getPRDBetween().getSourceEntity();
        } else if (actionName.equals("replace")) {
            entityName = action.getKill();
        }
        m_ruleName = ruleName;
        if(action.getPRDSecondaryEntity() != null){
            m_secondaryEntity = action.getPRDSecondaryEntity().getEntity();
            expression temp = new expression();
            temp.convertValueInString(action.getPRDSecondaryEntity().getPRDSelection().getCount());
            if(temp.getType() == expressionType.INT){
                countForSecondaryEntities = temp.getInt();
            }
            else{
                isSecondaryAll = true;
                countForSecondaryEntities = -1;
            }
            if(action.getPRDSecondaryEntity().getPRDSelection().getPRDCondition() != null){
                //condition = new single(action.getPRDSecondaryEntity().getPRDSelection().getPRDCondition());
            }
        }
    }
}
