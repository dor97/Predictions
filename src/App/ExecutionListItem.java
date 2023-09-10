package App;

public class ExecutionListItem {
    private Integer id;
    private Boolean isFinished = false;
    public ExecutionListItem(Integer id, Boolean finished){
        isFinished = finished;
        this.id = id;
    }

    public ExecutionListItem(Integer id){
        this.id = id;
    }

    public Integer getID(){
        return id;
    }

    public void setToFinished(){
        isFinished = true;
    }

    @Override
    public String toString(){
        if(isFinished){
            return "Simulation Number: "+ id.toString() + "(finished)";
        }
        return "Id: "+ id.toString();
    }

}
