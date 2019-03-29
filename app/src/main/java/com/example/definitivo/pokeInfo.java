package com.example.definitivo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.definitivo.data.pokeResultInfo;
import com.example.definitivo.model.AdapterDatos;
import com.example.definitivo.utilities.NetworkUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;

public class pokeInfo extends AppCompatActivity {

    TextView mNombre, mPeso, mAltura, mExperiencia;
    String URL;
    JsonArray pokeArray;
    JsonParser pokeParse = new JsonParser();
    JsonObject pokeOb;
    double peso;
    int alto, base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_info);
        Intent mIntent = getIntent();
        mNombre = findViewById(R.id.tv_nombre_poke);
        mNombre.setText("Nombre: " + mIntent.getStringExtra("nombre"));
        URL = mIntent.getStringExtra("url");
        mPeso = findViewById(R.id.tv_peso_poke);
        mAltura = findViewById(R.id.tv_altura_poke);
        mExperiencia = findViewById(R.id.tv_experiencia_poke);

        new FetchPokemonTask().execute("");
    }

    public class FetchPokemonTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            URL url = NetworkUtil.getURL(URL);
            try {
                String result = NetworkUtil.getResponseFromHttpUrl(url);
                result = "[" + result + "]";
                pokeArray = pokeParse.parse(result).getAsJsonArray();
                for(JsonElement pokeElement: pokeArray){
                    pokeOb = pokeElement.getAsJsonObject();
                    peso = pokeOb.get("weight").getAsDouble()/10;
                    alto = pokeOb.get("height").getAsInt()*10;
                    base = pokeOb.get("base_experience").getAsInt();
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
                mAltura.setText("Altura: " + alto + " cm");
                mPeso.setText("Peso: " + peso + " kg");
                mExperiencia.setText("Experiencia base: " + base);
            }
        }
    }
}
