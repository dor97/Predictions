package DTO;

public class DTOMapSpace {
    private DTOMapSpaceEnum mapSpaceEnum;
    private String entityName;

    public DTOMapSpace(DTOMapSpaceEnum mapSpaceEnum, String entityName){
        this.mapSpaceEnum = mapSpaceEnum;
        this.entityName = entityName;
    }

    public DTOMapSpaceEnum getMapSpaceEnum(){
        return mapSpaceEnum;
    }

    public String getEntityName(){
        return entityName;
    }
}
