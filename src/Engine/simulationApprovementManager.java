package Engine;

public class simulationApprovementManager {
    private approvementStatus status;
    private String simulationName;
    private String userName;
    private Integer amountToRun;
    private Integer ticks;
    private Integer sec;

    public simulationApprovementManager(String simulationName, String userName, Integer amountToRun, Integer ticks, Integer sec){
        this.simulationName = simulationName;
        this.userName = userName;
        this.amountToRun = amountToRun;
        this.ticks = ticks;
        this.sec = sec;
        status = approvementStatus.WAITING;
    }

    public void setStatus(approvementStatus status){
        this.status = status;
    }

    public Integer getTicks(){
        return ticks;
    }

    public Integer getSec(){
        return sec;
    }

    public approvementStatus getStatus(){
        return status;
    }

    public String getSimulationName(){
        return simulationName;
    }

    public String getUserName(){
        return userName;
    }

    public Integer getAmountToRun(){
        return amountToRun;
    }

    public void addToAmountToRun(Integer add){
        amountToRun += add;
    }

    public void decreasedAmount(){
        amountToRun -= 1;
    }

}
