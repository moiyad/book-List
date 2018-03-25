package com.example.moiyad.booklist;

import android.app.ProgressDialog;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.zip.Inflater;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private EditText searchBar;
    private Button searchButton;
    private ListView bookListView;
    private Book[] books;
    private BookAdapter adapter;
    private TextView tv;
    private LinearLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBar = (EditText) findViewById(R.id.searchBar);
        searchButton = (Button) findViewById(R.id.searchButton);

        bookListView = new ListView(this);


        layout = (LinearLayout) findViewById(R.id.layout);


        layout.addView(bookListView);



        tv = new TextView(this);
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tv.setTextSize(40);
        tv.setGravity(Gravity.CENTER);
        tv.setText("there are no books");

        adapter = new BookAdapter(this);

        bookListView.setAdapter(adapter);


        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isconnected(MainActivity.this)) {
                    JsonGetter jg = new JsonGetter();
                    jg.execute(searchBar.getText().toString());
                } else {
                    Toast.makeText(MainActivity.this, "No Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public boolean isconnected(Context mContext) {
        ConnectivityManager connectivity =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {
                if (info.getState() == NetworkInfo.State.CONNECTED)
                    return true;
            }
        }
        return false;
    }

    class JsonGetter extends AsyncTask<String, Void, JSONObject> {
        private URL url;

        public JsonGetter() {
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            HttpsURLConnection h = null;
            try {
                url = new URL("https://www.googleapis.com/books/v1/volumes?q=" + params[0] + "&maxResults=20");
                h = (HttpsURLConnection) url.openConnection();
                h.setRequestMethod("GET");
                h.setDoInput(true);
                //responseCode = to know if your connection is working or not Example:200 connect ,404 page not found
                int responseCode = h.getResponseCode();
                Log.d("responseCode", responseCode + "");
                String tmp;
                String result = "";
                BufferedReader br = new BufferedReader(new InputStreamReader(h.getInputStream()));
                while ((tmp = br.readLine()) != null) {
                    result += tmp + "\n";
                }
                JSONObject jo = new JSONObject(result);
                return jo;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                h.disconnect();
            }
            return null;
        }

        ProgressDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(MainActivity.this);
            p.setMessage("loading...");
            p.show();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
               if(adapter.getCount()==0) {
                   Toast.makeText(MainActivity.this,"result",Toast.LENGTH_SHORT).show();
                   super.onPostExecute(jsonObject);

                p.dismiss();
                try {
                    JSONArray arr = jsonObject.getJSONArray("items");
                    books = new Book[arr.length()];
                    for (int i = 0; i < arr.length(); i++) {
                        books[i] = new Book();
                        JSONObject volumeInfo = ((JSONObject) arr.get(i)).getJSONObject("volumeInfo");
                        books[i].setTitle(volumeInfo.getString("title"));
                        if(volumeInfo.has("authors")) {
                            books[i].setAuthors(volumeInfo.getJSONArray("authors"));
                        }
                        books[i].display();
                    }
                    adapter.update(books);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
               }else{
                   Toast.makeText(MainActivity.this,"No result",Toast.LENGTH_SHORT).show();
                   p.dismiss();
               }


        }


    }
}


