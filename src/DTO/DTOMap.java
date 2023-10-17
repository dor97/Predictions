package DTO;

import Engine.InvalidValue;
import Engine.world.map;
import Engine.world.space;

public class DTOMap {
    private DTOMapSpace[][] map = null;
    private Integer m_rows, m_cols;

    public void setMapSize(Integer rows, Integer cols){
        m_rows = rows;
        m_cols = cols;
        if(m_rows == null || m_cols == null){
            throw new InvalidValue("You mast start simulation before you can get the simulation map");
        }
        map = new DTOMapSpace[m_rows][m_cols];
    }

    public void setMap(space[][] map, Integer rows, Integer cols){
        if(m_rows != rows || m_cols != cols){
            throw new InvalidValue("Wrong size map");
        }
        for(Integer i = 0; i < m_rows; i++){
            for(Integer j = 0; j < m_cols; j++){
                if(map[i][j].isOccupied()){
                    this.map[i][j] = new DTOMapSpace(DTOMapSpaceEnum.OCCUPIED, map[i][j].getEntityName());
                }else{
                    this.map[i][j] = new DTOMapSpace(DTOMapSpaceEnum.FREE, "");
                }
            }
        }
    }

    public DTOMapSpace[][] getMap(){
        return map;
    }

    public Integer getRows(){
        return m_rows;
    }

    public Integer getCols(){
        return m_cols;
    }
}
