package gameEngien.rule.action.increase;

import gameEngien.entity.Entity;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

public interface subCondition {
    public boolean getBoolValue(Entity entity) throws InvalidValue;

}
