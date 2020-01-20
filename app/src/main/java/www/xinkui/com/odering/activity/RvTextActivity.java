package www.xinkui.com.odering.activity;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import www.xinkui.com.odering.R;
import www.xinkui.com.odering.adapter.RecycleViewAdapter;

public class RvTextActivity extends AppCompatActivity {
    ArrayList<String> names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_text);
        names=new ArrayList<>();
        init();
    }
    public void init(){
        for(int i=0;i<15;i++){
            names.add("123");
            names.add("456");
            names.add("789");
            names.add("101");
        }

        RecycleViewAdapter recycleViewAdapter=new RecycleViewAdapter(this,names,names);
         RecyclerView recyclerView=(RecyclerView)findViewById(R.id.rv_total);
         recyclerView.setAdapter(recycleViewAdapter);
         recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
