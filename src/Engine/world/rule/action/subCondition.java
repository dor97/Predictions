package Engine.world.rule.action;

import Engine.world.entity.Entity;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

public interface subCondition {
    public boolean getBoolValue(Entity entity) throws InvalidValue;

}
