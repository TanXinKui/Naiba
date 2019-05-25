package www.xinkui.com.odering.bean;

public class SellState {
    private int id;
    private int state;

    public SellState(int id, int state) {
        super();
        this.id = id;
        this.state = state;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }

}
