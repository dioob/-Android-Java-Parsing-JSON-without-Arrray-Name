package com.example.todolistd;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get JSON
    private static String url = "https://jsonplaceholder.typicode.com/todos";

    ArrayList<HashMap<String, String>> myDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDataList = new ArrayList<>();

        lv = findViewById(R.id.list);


        new GetMyDatas().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetMyDatas extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    //JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray mydata = new JSONArray(jsonStr);

                    //JSONArray json = new JSONArray(result);
                    // looping
                    for (int i = 0; i < mydata.length(); i++) {
                        JSONObject c = mydata.getJSONObject(i);

                        String uid = c.getString("userId");
                        String id = c.getString("id");
                        String title = c.getString("title");
                        String completed = c.getString("completed");


                        // tmp hash map for single contact
                        HashMap<String, String> mydatas = new HashMap<>();

                        // adding each child node to HashMap key => value
                        mydatas.put("userId", uid);
                        mydatas.put("id", id);
                        mydatas.put("title", title);
                        mydatas.put("completed", completed);

                        // adding
                        myDataList.add(mydatas);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, myDataList,
                    R.layout.list_item, new String[]{"userId", "id","title","completed"},
                    new int[]{R.id.uid, R.id.id, R.id.title, R.id.compilation});

            lv.setAdapter(adapter);

            //listview on click
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                    Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                    HashMap<String, String> hashMap = myDataList.get(position);
                    Bundle extras = new Bundle();

                    extras.putString("UID", hashMap.get("userId"));
                    extras.putString("ID", hashMap.get("id"));
                    extras.putString("TITLE", hashMap.get("title"));
                    extras.putString("COMPLETED", hashMap.get("completed"));

                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });
        }
        }

    
}