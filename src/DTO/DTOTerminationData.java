package DTO;

public class DTOTerminationData {
    private terminationType m_type;
    private int m_count;

    public DTOTerminationData(terminationType type, int count){
        m_type = type;
        m_count = count;
    }

    public terminationType getType(){
        return m_type;
    }

    public int getCount(){
        return m_count;
    }
}
