package project.itcs6166.com.expirynotify.main.list;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.itcs6166.com.expirynotify.R;
import project.itcs6166.com.expirynotify.main.common.ItemData;

public class ShowListActivity extends AppCompatActivity {
    private static final String TAG = "ShowListActivity";


    private RecyclerView recyclerView;
    private ViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Map<String, Object>>items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate : called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        recyclerView = findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        mAdapter = new ViewAdapter(ItemData.getItemNames(), ItemData.getExpiryDates(), this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);
        initListViewData();
        //initialize Recycler View


        /*Intent intent = new Intent(ShowListActivity.this, NotificationService.class);
        intent.putExtra("message","'hello");
        startService(intent);*/

       // saveData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveData();
    }

    //initializing data for recycler view
    private void initListViewData() {
        Log.d(TAG, "initListViewData : called");

            //items = (ArrayList<Map<String, Object>>)getIntent().getSerializableExtra("items");
            loadData();
                Collections.sort(items, new Comparator<Map<String, Object>>() {
                    @Override
                    public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                        Map<String, Double> timesMapO1 = (LinkedTreeMap<String, Double>)o1.get("exp_date");
                        Map<String, Double> timesMapO2 = (LinkedTreeMap<String, Double>)o2.get("exp_date");


                        double secondsO1 = timesMapO1.get("seconds");
                        double nanosecondsO1 = timesMapO1.get("nanoseconds");
                        Timestamp timestamp1 = new Timestamp((long)secondsO1, (int)nanosecondsO1);

                        double secondsO2 = timesMapO2.get("seconds");
                        double nanosecondsO2 = timesMapO2.get("nanoseconds");
                        Timestamp timestamp2 = new Timestamp((long)secondsO2, (int)nanosecondsO2);


                        return timestamp1.toDate().compareTo(timestamp2.toDate());
                    }
                });
            Log.d(TAG, "initListViewData : data received : " + items);
            for (Map<String, Object> item : items) {
                Map<String, Double> timesMap = (LinkedTreeMap<String, Double>)item.get("exp_date");
                double seconds = timesMap.get("seconds");
                double nanoseconds = timesMap.get("nanoseconds");
                Timestamp timestamp = new Timestamp((long)seconds, (int) nanoseconds);

                String label = (String) item.get("label");
                String exp_date = DateFormat.getDateInstance().format(timestamp.toDate());
                Log.d(TAG, "initListViewData : label: " + label);
                Log.d(TAG, "initListViewData : expiry date: " + exp_date);
                if (!ItemData.getItemNames().contains(label) || !ItemData.getExpiryDates().contains(exp_date)) {
                    // ToDo : replace this line with local lists .
                    ItemData.getItemNames().add(label);
                    ItemData.getExpiryDates().add(exp_date);
                }

            }
            Log.d(TAG, "initListViewData : labels: " + ItemData.getItemNames());
            Log.d(TAG, "initListViewData : expiry dates: " + ItemData.getExpiryDates());



            mAdapter.setOnItemClickListener(new ViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {

                }

                @Override
                public void onDeleteClick(int position) {
                    removeItem(position);
                }
            });


    }

    private void removeItem(int position){
            ItemData.getItemNames().remove(position);
            ItemData.getExpiryDates().remove(position);
            //ItemData.removeItemData(position);
            items.remove(position);

            mAdapter.notifyItemRemoved(position);
    }

    // for saving activity data into shared preference
    private void saveData(){
        SharedPreferences sf = getSharedPreferences("sharedList", MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        Gson gson = new Gson();
        String json = gson.toJson(items);
        editor.putString("savedItems", json);
        editor.apply();
    }

    public void loadData(){
        SharedPreferences sf = getSharedPreferences("sharedList", MODE_PRIVATE);
        Gson gson = new Gson();
        String itemList = sf.getString("savedItems", null);
        Type type = new TypeToken<ArrayList<Map<String, Object>>>(){}.getType();

        items = gson.fromJson(itemList, type);
        if(items == null){
            items = new ArrayList<>();
        }
        Log.d(TAG, "loadData : item null check : " + items);

        //Log.d(TAG, "loadData : item null check : " + items.equals(null));
    }

}
