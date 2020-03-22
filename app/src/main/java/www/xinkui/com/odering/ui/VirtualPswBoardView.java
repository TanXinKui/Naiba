package www.xinkui.com.odering.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import www.xinkui.com.odering.R;

public class VirtualPswBoardView extends LinearLayout {
    GridView mGridView;
    View mView;
    PswAdapter pswAdapter;
    Context mContext;
    TextView textView;
    ImageView  closebtn;
    public VirtualPswBoardView(Context context) {
        super(context);
    }

    public VirtualPswBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        mView= View.inflate(context,R.layout.password_input,null);
        mGridView=mView.findViewById(R.id.my_psw_board);
        textView = mView.findViewById(R.id.amount);
        closebtn =mView.findViewById(R.id.closebtn);
        pswAdapter=new PswAdapter(context,6,0);
        mGridView.setAdapter(pswAdapter);
        addView(mView);
    }

    public ImageView getClosebtn() {
        return closebtn;
    }

    public TextView getAmountTextView() {
        return textView;
    }

    public void setRoundNum(int num){
        pswAdapter=new PswAdapter(mContext,6,num);
        mGridView.setAdapter(pswAdapter);
    }
    public GridView getmGridView(){
        return mGridView;
    }
    class PswAdapter extends BaseAdapter {
        private int num;
        private int needNum;
        private Context mContext;
        public void ChangeData(){
            notifyDataSetChanged();
        }
        public PswAdapter(Context context, int num, int needNum) {
            super();
            mContext=context;
            this.num=num;
            this.needNum=needNum;
        }

        @Override
        public int getCount() {
            return num;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder=new ViewHolder();
            if(view==null){
             view= View.inflate(mContext,R.layout.psw_item,null );
             viewHolder.imageView=view.findViewById(R.id.round_point);
             if(needNum!=num){
                 //i : 0-5  need:0-6
                 if(i<needNum){
                     viewHolder.imageView.setVisibility(VISIBLE);
                 }else {
                     viewHolder.imageView.setVisibility(INVISIBLE);
                 }
             }
            }
            return view;
        }
        class ViewHolder{
            ImageView imageView;
        }
    }
}
