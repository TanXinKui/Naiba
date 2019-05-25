package www.xinkui.com.odering.bean;

public class Transcation {
    private Menu menu;
    private int sumup;
    private String username;
    private int deskNum;
    private int clientNum;

    public Transcation(Menu menu, int sumup, String username, int deskNum, int clientNum) {
        super();
        this.menu = menu;
        this.sumup = sumup;
        this.username = username;
        this.deskNum = deskNum;
        this.clientNum = clientNum;
    }
    public Menu getMenu() {
        return menu;
    }
    public void setMenu(Menu menu) {
        this.menu = menu;
    }
    public int getSumup() {
        return sumup;
    }
    public void setSumup(int sumup) {
        this.sumup = sumup;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public int getDeskNum() {
        return deskNum;
    }
    public void setDeskNum(int deskNum) {
        this.deskNum = deskNum;
    }
    public int getClientNum() {
        return clientNum;
    }
    public void setClientNum(int clientNum) {
        this.clientNum = clientNum;
    }
    @Override
    public String toString() {
        return "Transcation [menu=" + menu + ", sumup=" + sumup + ", username=" + username + ", deskNum=" + deskNum
                + ", clientNum=" + clientNum + "]";
    }


}
