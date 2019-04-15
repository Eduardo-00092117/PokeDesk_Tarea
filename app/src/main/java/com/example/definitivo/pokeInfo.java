package com.example.definitivo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.definitivo.data.imageData;
import com.example.definitivo.utilities.NetworkUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class pokeInfo extends AppCompatActivity {

    TextView mNombre, mPeso, mAltura, mExperiencia, mTipo;
    TextView mSP, mS_D, mS_A, mD, mA, mHP;
    TextView mPokeImage, mTouch, mCerrar, mShare;
    String URL;
    JsonArray pokeArray, stats;
    JsonParser pokeParse = new JsonParser();
    JsonObject pokeOb;
    double peso;
    int alto, base;
    JsonArray type;

    ArrayList<imageData> imagenes = new ArrayList<imageData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_info);
        Intent mIntent = getIntent();
        mNombre = findViewById(R.id.tv_nombre_poke);
        mNombre.setText("Name: " + mIntent.getStringExtra("nombre"));
        URL = mIntent.getStringExtra("url");
        mPeso = findViewById(R.id.tv_peso_poke);
        mAltura = findViewById(R.id.tv_altura_poke);
        mExperiencia = findViewById(R.id.tv_experiencia_poke);
        mTipo = findViewById(R.id.tv_tipo);

        mSP = findViewById(R.id.tv_sp);
        mS_D = findViewById(R.id.tv_s_d);
        mS_A = findViewById(R.id.tv_s_a);
        mD = findViewById(R.id.tv_d);
        mA = findViewById(R.id.tv_a);
        mHP = findViewById(R.id.tv_hp);

        mPokeImage = findViewById(R.id.pokeImage);
        mTouch = findViewById(R.id.tv_touch);
        mCerrar = findViewById(R.id.tv_cerrar);
        mShare = findViewById(R.id.tv_share);

        guardarImagenes();

        new FetchPokemonTask().execute("");

        mCerrar.setOnClickListener(v -> {
            onBackPressed();
            this.finish();
        });
    }

    public void guardarImagenes(){
        agregarImagen("poison", R.drawable.poison);
        agregarImagen("grass", R.drawable.grass);
        agregarImagen("bug", R.drawable.bug);
        agregarImagen("dragon", R.drawable.dragon);
        agregarImagen("ice", R.drawable.ice);
        agregarImagen("fighting", R.drawable.fighting);
        agregarImagen("fire", R.drawable.fire);
        agregarImagen("flying", R.drawable.flying);
        agregarImagen("ghost", R.drawable.ghost);
        agregarImagen("ground", R.drawable.ground);
        agregarImagen("electric", R.drawable.electric);
        agregarImagen("psychic", R.drawable.psychic);
        agregarImagen("rock", R.drawable.rock);
        agregarImagen("water", R.drawable.water);
        agregarImagen("dark", R.drawable.dark);
        agregarImagen("steel", R.drawable.steel);
        agregarImagen("fairy", R.drawable.fairy);
        agregarImagen("bird", R.drawable.bird);
        agregarImagen("normal", R.drawable.poke);
    }

    public void agregarImagen(String tipo, int imagen){
        imageData Nimagen = new imageData();
        Nimagen.setTipo(tipo);
        Nimagen.setImagen(imagen);
        imagenes.add(Nimagen);
    }

    public class FetchPokemonTask extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog = new ProgressDialog(pokeInfo.this);
            pDialog.setMessage("Loading data");
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }

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
                    type = pokeOb.get("types").getAsJsonArray();
                    stats = pokeOb.get("stats").getAsJsonArray();
                }

                return result;
            } catch (IOException e){
                e.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s){
            if(s != null) {
                JsonParser pokePa = new JsonParser();
                JsonObject typeOb;
                JsonObject typeObjI;
                String typeIS;
                String catTipo = "";
                mAltura.setText("Height: " + alto + " cm");
                mPeso.setText("Weight: " + peso + " kg");
                mExperiencia.setText("Base experience: " + base);

                ArrayList<Integer> datosE = new ArrayList<>();

                for (int i = 0; i < type.size(); i++) {
                    typeIS = type.get(i).toString();
                    typeOb = pokePa.parse(typeIS).getAsJsonObject();
                    typeObjI = typeOb.get("type").getAsJsonObject();
                    for (int j = 0; j < imagenes.size(); j++) {
                        if (imagenes.get(j).getTipo().equals(typeObjI.get("name").getAsString()) &&
                                !typeObjI.get("name").getAsString().equals("normal")) {
                            datosE.add(imagenes.get(j).getImagen());
                        }
                    }
                    if (type.size() == i + 1) {
                        catTipo += typeObjI.get("name").getAsString();
                    } else {
                        catTipo += typeObjI.get("name").getAsString() + "/";
                    }
                }
                if(datosE.size() != 0){
                    datosE.add(imagenes.get(18).getImagen());
                } else{
                    mTouch.setVisibility(View.INVISIBLE);
                }

                mShare.setOnClickListener(v -> {
                    String datos = mNombre.getText().toString() + "\n" +
                            mPeso.getText().toString() + "\n" +
                            mAltura.getText().toString() + "\n" +
                            mExperiencia.getText().toString() + "\n" +
                            mTipo.getText().toString();

                    Intent mIntentShare = new Intent();
                    mIntentShare.setAction(Intent.ACTION_SEND);
                    mIntentShare.setType("text/plain");
                    mIntentShare.putExtra(Intent.EXTRA_TEXT, datos);
                    startActivity(mIntentShare);
                });

                final int[] i = {0};

                mPokeImage.setOnClickListener(v -> {
                    if (datosE.size() != 0){
                        mPokeImage.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, datosE.get(i[0]));
                        if (datosE.size() == i[0] +1){
                            i[0] = -1;
                        }
                        i[0] += 1;
                    }
                });

                JsonParser statsI = new JsonParser();
                mSP.setText(statsI.parse(stats.get(0).toString()).getAsJsonObject().get("base_stat").toString());
                mS_D.setText(statsI.parse(stats.get(1).toString()).getAsJsonObject().get("base_stat").toString());
                mS_A.setText(statsI.parse(stats.get(2).toString()).getAsJsonObject().get("base_stat").toString());
                mD.setText(statsI.parse(stats.get(3).toString()).getAsJsonObject().get("base_stat").toString());
                mA.setText(statsI.parse(stats.get(4).toString()).getAsJsonObject().get("base_stat").toString());
                mHP.setText(statsI.parse(stats.get(5).toString()).getAsJsonObject().get("base_stat").toString());

                mTipo.setText("Type: "+catTipo);
                pDialog.dismiss();
            }
        }
    }
}
