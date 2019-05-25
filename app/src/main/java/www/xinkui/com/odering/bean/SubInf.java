package www.xinkui.com.odering.bean;
/**
*@description 
*@author TONXOK
*@time 2019/4/28 9:53
*/
public class SubInf {
    int[] menu = new int[9];

    public void setMenu(int me1, int me2, int me3, int me4, int me5, int me6, int me7, int me8, int me9) {
        menu[0] = me1;
        menu[1] = me2;
        menu[2] = me3;
        menu[3] = me4;
        menu[4] = me5;
        menu[5] = me6;
        menu[6] = me7;
        menu[7] = me8;
        menu[8] = me9;
    }

    public int[] getMenu() {
        return menu;
    }
}
