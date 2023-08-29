package Engine.world;

public class space {
    private boolean isOccupied = false;
    private int m_x, m_y;

    public space(int x, int y){
        m_x = x;
        m_y = y;
    }
    public void setToOccupied(){
        isOccupied = true;
    }

    public void setToFree(){
        isOccupied = false;
    }

    public boolean isOccupied(){
        return isOccupied;
    }

    public int getX(){
        return m_x;
    }

    public int getY(){
        return m_y;
    }
}
