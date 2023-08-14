package DTO;

public class DTOEntityPostRun {
    private String m_name;
    private int m_amountPreRun, m_amountPostRun;

    public DTOEntityPostRun(String name, int amountPreRun, Integer amountPostRun){
        m_name = name;
        m_amountPreRun = amountPreRun;
        m_amountPostRun = amountPostRun != null ? amountPostRun : 0;
    }

    public DTOEntityPostRun(String name, int amountPreRun){
        m_name = name;
        m_amountPreRun = amountPreRun;
        m_amountPostRun = 0;
    }

    public String getName(){
        return m_name;
    }

    public int getAmountPreRun(){
        return m_amountPreRun;
    }

    public int getAmountPostRun(){
        return m_amountPostRun;
    }

    public void IncreaseAmountPostRunByOne(){
        m_amountPostRun++;
    }
}
