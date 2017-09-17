package com.nestedmango.myjson;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> actorsList;
    ListView listview;
    EditText searchView;
    String name;
    String item;
    // ArrayAdapter<CharSequence> adapterSpinner;
    Spinner Cspinner;
    ArrayAdapter<CharSequence> adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Cspinner = (Spinner) findViewById(R.id.spinnerActors);
        adapter1 = ArrayAdapter.createFromResource(this, R.array.country_names, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Cspinner.setAdapter(adapter1);
        Cspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getBaseContext(), item + "selected", Toast.LENGTH_LONG).show();
                if (item.equalsIgnoreCase(name)) {
                    Toast.makeText(getApplicationContext(), item, Toast.LENGTH_LONG);
                    final List<String> filteredList = new ArrayList<>();
                    for (int i = 0; i < actorsList.size(); i++) {
                        //final String text = actorsList.get(i).toLowerCase();


                        filteredList.add(actorsList.get(i).toLowerCase());

                    }
                    ArrayAdapter adapter = new ArrayAdapter(getApplication(), android.R.layout.simple_list_item_1, filteredList);

                    listview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        actorsList = new ArrayList<>();
        new ActorsAsyntask().execute("http://microblogging.wingnity.com/JSONParsingTutorial/jsonActors");

        listview = (ListView) findViewById(R.id.list);
        // adapter = new ActorAdapter(getApplicationContext(), R.layout.row, actorsList);
        ArrayAdapter adapter = new ArrayAdapter(getApplication(), android.R.layout.simple_list_item_1, actorsList);
        listview.setAdapter(adapter);
       // searchView = (EditText) findViewById(R.id.searchView);

        // listview.setAdapter(adapter);
        // searchView.
     //   addTextListener();


    }


    public class ActorsAsyntask extends AsyncTask<String, String, List<Actors>> {

        @Override
        protected  List<Actors> doInBackground(String... params) {

                HttpURLConnection connection = null;
                BufferedReader reader = null;

                URL url = null;
                try {
                    url = new URL((String) params[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    InputStream stream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);

                    }
                    String data = buffer.toString();
                    JSONObject jsono = new JSONObject(data);
                    JSONArray jarray = jsono.getJSONArray("actors");

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        Actors actor = new Actors();

                       name=object.getString("name");
                      //  actor.setDescription(object.getString("description"));
                        //actor.setDob(object.getString("dob"));
                        //actor.setCountry(object.getString("country"));
                        //actor.setHeight(object.getString("height"));
                        //actor.setSpouse(object.getString("spouse"));
                        //actor.setChildren(object.getString("children"));
                        //actor.setImage(object.getString("image"));

                        actorsList.add(name);
                    }






                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finally {
                    if (connection != null)
                        connection.disconnect();
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (connection != null) {
                            connection.disconnect();
                        }
                    }
                    try {
                        if (reader != null) {
                            reader.close();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                    return null;
        }


        @Override
        protected void onPostExecute(List<Actors> result) {
            super.onPostExecute(result);
            ArrayAdapter adapter=new ArrayAdapter(getApplication(),android.R.layout.simple_list_item_1,actorsList);
            listview.setAdapter(adapter);
        }
    }
}
