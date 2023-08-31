package Engine.world.rule.action;

import Engine.world.entity.Entity;
import Engine.InvalidValue;

import java.util.List;

public interface subCondition {
    public boolean getBoolValue(Entity entity, int currTick) throws InvalidValue;
    public boolean getBoolValue(Entity entity, Entity secondaryEntity, int currTick) throws InvalidValue;

    public boolean shouldIgnore(Entity entity);

}
