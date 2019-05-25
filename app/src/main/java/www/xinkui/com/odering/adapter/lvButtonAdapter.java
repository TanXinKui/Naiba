package www.xinkui.com.odering.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import www.xinkui.com.odering.R;

/**
 * @author TONXOK
 * @description
 * @time 2019/4/28 9:53
 */
public class lvButtonAdapter extends BaseAdapter {
    private class buttonViewHolder {
        ImageView appIcon;
        TextView appName;
        ImageButton buttonClose;
    }

    private ArrayList<HashMap<String, Object>> mAppList;
    private LayoutInflater mInflater;
    private Context mContext;
    private String[] keyString;
    private int[] valueViewID;
    private buttonViewHolder holder;

    public lvButtonAdapter(Context c, ArrayList<HashMap<String, Object>> appList, int resource,
                           String[] from, int[] to) {
        mAppList = appList;
        mContext = c;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        keyString = new String[from.length];
        valueViewID = new int[to.length];
        System.arraycopy(from, 0, keyString, 0, from.length);
        System.arraycopy(to, 0, valueViewID, 0, to.length);
    }

    @Override
    public int getCount() {
        return mAppList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void removeItem(int position) {
        mAppList.remove(position);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            holder = (buttonViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.forlist, null);
            holder = new buttonViewHolder();
            holder.appIcon = (ImageView) convertView.findViewById(valueViewID[0]);
            holder.appName = (TextView) convertView.findViewById(valueViewID[1]);
            holder.buttonClose = (ImageButton) convertView.findViewById(valueViewID[2]);
            convertView.setTag(holder);
        }

        HashMap<String, Object> appInfo = mAppList.get(position);
        if (appInfo != null) {
            String aname = (String) appInfo.get(keyString[1]);
            int mid = (Integer) appInfo.get(keyString[0]);
            int bid = (Integer) appInfo.get(keyString[2]);
            holder.appName.setText(aname);
            holder.appIcon.setImageDrawable(holder.appIcon.getResources().getDrawable(mid));
            holder.buttonClose.setImageDrawable(holder.buttonClose.getResources().getDrawable(bid));
            holder.buttonClose.setOnClickListener(new lvButtonListener(position));
        }
        return convertView;
    }

    class lvButtonListener implements View.OnClickListener {
        private int position;

        lvButtonListener(int pos) {
            position = pos;
        }

        @Override
        public void onClick(View v) {
            int vid = v.getId();
            if (vid == holder.buttonClose.getId()) ;
            String[] Itemlist = new String[]{"碳烤松茸", "酥油煎松茸", "油焖冬笋", "柳州酸笋", "黄豆酸笋小黄鱼",
                    "柳州酸笋", "黄豆酸笋小黄鱼", "柳州酸笋", "黄豆酸笋小黄鱼"};
            removeItem(position);
            Toast toast = Toast.makeText(mContext, "自定义位置 的Toast", Toast.LENGTH_LONG);//显示时间较长
            toast.setGravity(Gravity.CENTER, 0, 0);// 居中显示
            toast.show();

            //Toast.makeText(Main.this, "successfully remove" + position, Toast.LENGTH_SHORT).show();

            switch (position) {
                case 0:
                    Toast.makeText(mContext, Itemlist[0] + "已加入购物车", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(mContext, Itemlist[1] + "已加入购物车", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(mContext, Itemlist[2] + "已加入购物车", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(mContext, Itemlist[3] + "已加入购物车", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(mContext, Itemlist[4] + "已加入购物车", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(mContext, Itemlist[5] + "已加入购物车", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    Toast.makeText(mContext, Itemlist[6] + "已加入购物车", Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    Toast.makeText(mContext, Itemlist[7] + "已加入购物车", Toast.LENGTH_SHORT).show();
                    break;
                case 8:
                    Toast.makeText(mContext, Itemlist[8] + "已加入购物车", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
}
