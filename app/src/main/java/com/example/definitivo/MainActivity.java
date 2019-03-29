package com.example.definitivo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import com.example.definitivo.data.pokeResultInfo;
import com.example.definitivo.model.AdapterDatos;
import com.example.definitivo.utilities.NetworkUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> listDatos = new ArrayList<String>();
    ArrayList<String> listUrl = new ArrayList<String>();
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = findViewById(R.id.rv_pokemon);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        findViewById(R.id.btn).setOnClickListener(v -> {
            otraVentana();
        });

        new FetchPokemonTask().execute("");
    }



    public class FetchPokemonTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            URL url = NetworkUtil.buiURL("");
            try {
                String result = NetworkUtil.getResponseFromHttpUrl(url);
                Gson gson = new Gson();
                pokeResultInfo element = gson.fromJson (result, pokeResultInfo.class);
                for (int i = 0; i < element.getResults().size(); i++){
                    listDatos.add(element.getResults().get(i).name);
                    listUrl.add(element.getResults().get(i).url);
                }

                return result;
            } catch (IOException e){
                e.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s){
            if(s != null){
                AdapterDatos adapter = new AdapterDatos(listDatos, listUrl);
                recycler.setAdapter(adapter);
            }
        }
    }

    public void otraVentana(){
        Intent mIntent = new Intent(MainActivity.this, pokeInfo.class);
        mIntent.putExtra("Hola", "Hala Juve :(");
        startActivity(mIntent);
    }
}