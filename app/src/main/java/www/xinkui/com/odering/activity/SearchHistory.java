package www.xinkui.com.odering.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import www.xinkui.com.odering.R;

/**
 * Created by lenovo on 2017/11/4.
 */
/**
*@description
*@author TONXOK
*@time 2019/4/28 10:28
*/
public class SearchHistory extends Activity {
    private SQLiteDatabase db;
    String databaseName = "user.bd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forsearch);
        //创建出具库
        //final SQLiteDatabase db;
        db = SQLiteDatabase.openOrCreateDatabase(getFilesDir() + "/" + databaseName, null);

        //监听事件，清除历史记录
        Button clear_h_btn = (Button) findViewById(R.id.clear_history);
        clear_h_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear_search_history();
                refrech_search_history("");
            }
        });

        //监听事件，搜索与输入所匹配的

        Button btn = (Button) findViewById(R.id.main_next);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ed1 = (EditText) findViewById(R.id.search_board);
                String strp = ed1.getText().toString();
                if (!strp.equals("")) {
                    refrech_search_history(strp);
                    try {
                        create(db);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //String sql="select * from user";
                    String sql = "select * from user where password like '%%" + strp + "%%'";
                    Cursor cursor = db.rawQuery(sql, null);
                    int i = 0;
                    while (cursor.moveToNext()) {
                        i++;
                    }
                    //Toast.makeText(SearchHistory.this,"hhhh"+i,Toast.LENGTH_SHORT).show();
                    if (i == 0) {
                        Toast.makeText(SearchHistory.this, "无搜查结果！", Toast.LENGTH_SHORT).show();
                        ListView list = (ListView) findViewById(R.id.listview2);
                        ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("ItemText", "");
                        map.put("ItemTitle", "");
                        map.put("ItemText1", "");
                        mylist.add(map);
                        SimpleAdapter simpleA = new SimpleAdapter(
                                SearchHistory.this,
                                mylist,
                                R.layout.forlist,
                                new String[]{"", "", ""},
                                new int[]{R.id.forlistiv1, R.id.forlisttv1, R.id.forlisttv2}
                        );
                        list.setAdapter(simpleA);
                    } else {
                        final int j = i;
                        String[] ItemTitle = new String[j];
                        int l = 0;
                        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                            int nameColumnIndex = cursor.getColumnIndex("_id");
                            int nameColumnIndex1 = cursor.getColumnIndex("password");
                            int nameColumnIndex2 = cursor.getColumnIndex("password1");
                            ItemTitle[l++] = cursor.getString(nameColumnIndex1);
                            String strValue1 = cursor.getString(nameColumnIndex1);
                            String strValue2 = cursor.getString(nameColumnIndex2);
                            ListView list = (ListView) findViewById(R.id.listview2);
                            ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
                            for (int k = 0; k < ItemTitle.length; k++) {
                                HashMap<String, Object> map = new HashMap<String, Object>();
                                map.put("forlistim1", R.drawable.dish1);
                                map.put("ItemTitle", ItemTitle[k]);
                                map.put("ItemText1", R.drawable.addtocar);
                                mylist.add(map);
                            }
                            SimpleAdapter simpleA = new SimpleAdapter(
                                    SearchHistory.this,
                                    mylist,
                                    R.layout.forlist,
                                    new String[]{"forlistim1", "ItemText", "ItemText1"},
                                    new int[]{R.id.forlistiv1, R.id.forlisttv1, R.id.forlisttv2}
                            );
                            list.setAdapter(simpleA);
                        }

                    }
                    // create(db);
                    // e.printStackTrace();
                    //Toast.makeText(SearchHistory.this,"报错了！", Toast.LENGTH_SHORT).show();

                    //Cursor cursor=db.rawQuery(sql,null);
                    //cursor.close();

                } else if (strp.equals("")) {
                    Toast.makeText(SearchHistory.this, "请输入查询菜名！", Toast.LENGTH_SHORT).show();
                    ListView list = (ListView) findViewById(R.id.listview2);
                    ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("ItemText", "");
                    map.put("ItemTitle", "");
                    map.put("ItemText1", "");
                    mylist.add(map);
                    SimpleAdapter simpleA = new SimpleAdapter(
                            SearchHistory.this,
                            mylist,
                            R.layout.forlist,
                            new String[]{"", "", ""},
                            new int[]{R.id.forlistiv1, R.id.forlisttv1, R.id.forlisttv2}
                    );
                    list.setAdapter(simpleA);
                }


            }
            //Toast.makeText(this,"insert successfully",Toast.LENGTH_SHORT).show();
        });
    }

    //历史记录功能
    String load[] = {"", ""};

    public void refrech_search_history(String stra) {
        TextView tv1 = (TextView) findViewById(R.id.history1);
        TextView tv2 = (TextView) findViewById(R.id.history2);
        if (!stra.equals(load[0])) {
            load[1] = load[0];
            load[0] = stra;
            tv1.setText(load[0]);
            tv2.setText(load[1]);
        }
    }

    public void clear_search_history() {
        load[0] = "";
        load[1] = "";
    }

    //创建表并且插入数据
    public void create(SQLiteDatabase db) {
        try {
            db.execSQL("DELETE  FROM user ");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql = "create table user(_id integer primary key autoincrement,name text ,password text,password1 text)";
        String str1 = "R.drawable.ClientInfo";
        String str2 = "红烧鸡腿";
        String str3 = "R.drawable.addtocar";
        insert(db, str1, str2, str3);
        str1 = "R.drawable.ClientInfo";
        str2 = "碳烤松茸";
        str3 = "R.drawable.addtocar";
        insert(db, str1, str2, str3);
        str1 = "R.drawable.ClientInfo";
        str2 = "油焖冬笋";
        str3 = "R.drawable.addtocar";
        insert(db, str1, str2, str3);
        str1 = "R.drawable.ClientInfo";
        str2 = "柳州酸笋";
        str3 = "R.drawable.addtocar";
        insert(db, str1, str2, str3);
        str1 = "R.drawable.ClientInfo";
        str2 = "黄豆酸笋小黄鱼";
        str3 = "R.drawable.addtocar";
        insert(db, str1, str2, str3);
        str1 = "R.drawable.ClientInfo";
        str2 = "瓜丝儿";
        str3 = "R.drawable.addtocar";
        insert(db, str1, str2, str3);
        str1 = "R.drawable.ClientInfo";
        str2 = "糖焖莲子";
        str3 = "R.drawable.addtocar";
        insert(db, str1, str2, str3);
        str1 = "R.drawable.ClientInfo";
        str2 = "扒燕窝";
        str3 = "R.drawable.addtocar";
        insert(db, str1, str2, str3);
        db.execSQL(sql);
    }

    //插入数据库
    public void insert(SQLiteDatabase db, String name, String password, String pass1) {
        String sql = "insert into user(name,password,password1) values(?,?,?)";
        db.execSQL(sql, new String[]{name, password, pass1});
    }
}
