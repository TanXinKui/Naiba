package www.xinkui.com.odering.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import www.xinkui.com.odering.R;

public class VirtualKeyBoardView extends RelativeLayout implements View.OnClickListener {
    private Context mContext;
    private RelativeLayout mRelativeLayout;
    private GridView mGridView;
    private ImageView imgBack;
    private RelativeLayout mWholeKeyBoard;
    private ArrayList<Map<String, String>> maps;
    private View view;
    private boolean visibleStatus = true;

    public VirtualKeyBoardView(Context context) {
        super(context);
    }

    public VirtualKeyBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        view = View.inflate(context, R.layout.wechat_alike_ui, null);
        maps = new ArrayList<>();
        mRelativeLayout = view.findViewById(R.id.layoutBack);
        mRelativeLayout.setOnClickListener(this);
        mGridView = view.findViewById(R.id.gv_keybord);
        imgBack=view.findViewById(R.id.imgBack);
        mWholeKeyBoard = view.findViewById(R.id.whole_key_board);
        imgBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                mWholeKeyBoard.setVisibility(GONE);
                view.setVisibility(GONE);
                visibleStatus = false;
            }
        });
        setView();
        addView(view);
    }
//    public void switchKeyBoard(){
////        mWholeKeyBoard.setVisibility(VISIBLE);
//        view.setVisibility(VISIBLE);
//    }


    public void setOnVisible() {
        view.setVisibility(VISIBLE);
        visibleStatus = true;
    }
    public void setOffVisible(){
        view.setVisibility(GONE);
        visibleStatus = false;
    }

    public boolean isVisibleStatus() {
        return visibleStatus;
    }

    private void setView() {
        for (int i = 1; i < 13; i++) {
            Map<String, String> map = new HashMap<>(1);
            if (i < 10) {
                map.put("name", String.valueOf(i));
            } else if (i == 10) {
                map.put("name", ".");
            } else if (i == 11) {
                map.put("name", "0");
            } else if (i == 12) {
                map.put("name", "");
            }
            maps.add(map);
        }
        GridViewAdapter gridViewAdapter=new GridViewAdapter(mContext,maps);
        mGridView.setAdapter(gridViewAdapter);
    }

    @Override
    public void onClick(View view) {

    }

    public RelativeLayout getmRelativeLayout() {
        return mRelativeLayout;
    }

    public GridView getmGridView() {
        return mGridView;
    }

    public ArrayList<Map<String, String>> getMaps() {
        return maps;
    }

    class GridViewAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<Map<String, String>> mArrayList;

        public GridViewAdapter(Context context, ArrayList<Map<String, String>> ArrayList) {
            mContext = context;
            mArrayList = ArrayList;
        }

        @Override
        public int getCount() {
            return mArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return mArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = View.inflate(mContext, R.layout.item, null);
                viewHolder = new ViewHolder();
                viewHolder.btnKey = view.findViewById(R.id.btn_keys);
                viewHolder.imgDelete = view.findViewById(R.id.imgDelete);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            if(i==9){
                viewHolder.imgDelete.setVisibility(INVISIBLE);
                viewHolder.btnKey.setVisibility(VISIBLE);
                viewHolder.btnKey.setText(maps.get(i).get("name"));
//                viewHolder.btnKey.setBackgroundColor(Color.parseColor("#F5F5F5"));
            }else if(i==11){
                viewHolder.imgDelete.setVisibility(VISIBLE);
                viewHolder.btnKey.setVisibility(INVISIBLE);
//                viewHolder.btnKey.setBackgroundColor(Color.parseColor("#DCDCDC"));
                viewHolder.btnKey.setBackgroundResource(R.drawable.delete);
            }else {
                viewHolder.imgDelete.setVisibility(INVISIBLE);
                viewHolder.btnKey.setVisibility(VISIBLE);
                viewHolder.btnKey.setText(maps.get(i).get("name"));
            }
            return view;
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount();
        }

        class ViewHolder {
            private TextView btnKey;
            private RelativeLayout imgDelete;
        }
    }
}

