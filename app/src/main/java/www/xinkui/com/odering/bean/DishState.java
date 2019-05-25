package www.xinkui.com.odering.bean;

public class DishState {
    /**
     * 支付状态
     */
    private int payState;
    /**
     * 状态
     */
    private int state;
    /**
     * 确认状态
     */
    private int confirmState;

    public DishState() {
    }

    public DishState(int payState, int state, int confirmState) {
        this.payState = payState;
        this.state = state;
        this.confirmState = confirmState;
    }

    public int getPayState() {
        return payState;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getConfirmState() {
        return confirmState;
    }

    public void setConfirmState(int confirmState) {
        this.confirmState = confirmState;
    }

    @Override
    public String toString() {
        return "DishState{" +
                "payState=" + payState +
                ", state=" + state +
                ", confirmState=" + confirmState +
                '}';
    }
}
