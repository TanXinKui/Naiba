package www.xinkui.com.odering.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import www.xinkui.com.odering.R;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    private Context mcontext;
    private ArrayList<String> imgs;
    private ArrayList<String> names;

    public RecycleViewAdapter(Context mcontext, ArrayList<String> imgs, ArrayList<String> names) {
        this.mcontext = mcontext;
        this.imgs = imgs;
        this.names = names;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rvlistitem,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext,"Toast "+position,Toast.LENGTH_LONG).show();
            }
        });
        holder.imageView.setImageDrawable(mcontext.getResources().getDrawable((R.drawable.applogo)));
        holder.textView.setText(names.get(position));

    }

    @Override
    public int getItemCount() {
        return imgs.size();
    }

    class  ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.rv_iv);
            textView=itemView.findViewById(R.id.rv_tv);
        }
    }
}
