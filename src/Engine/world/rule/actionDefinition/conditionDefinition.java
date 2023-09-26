package Engine.world.rule.actionDefinition;

import Engine.InvalidValue;
import Engine.generated2.PRDAction;

public class conditionDefinition extends actionDefinition{
    public conditionDefinition(PRDAction action, String ruleName) throws InvalidValue {
        super(action, ruleName);
    }
}
