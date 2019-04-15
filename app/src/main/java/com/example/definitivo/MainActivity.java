package com.example.definitivo;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.definitivo.data.pokeResultInfo;
import com.example.definitivo.model.AdapterDatos;
import com.example.definitivo.utilities.NetworkUtil;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> listDatos = new ArrayList<String>();
    ArrayList<String> listUrl = new ArrayList<String>();
    RecyclerView recycler;
    EditText mSearch;
    Button mBSearch, mAll;
    TextView mNothing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = findViewById(R.id.rv_pokemon);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mBSearch = findViewById(R.id.btn);
        mSearch = findViewById(R.id.et_id_pokemon);
        mAll = findViewById(R.id.btn_all);
        mNothing = findViewById(R.id.tv_nothing);

        if (savedInstanceState == null) {
            new FetchPokemonTask().execute("");
        } else {
            Log.d("Hi", "Holaa");
        }

        mSearch.setFocusableInTouchMode(false);

        mSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSearch.setFocusableInTouchMode(true);
                return false;
            }
        });

        mNothing.setVisibility(View.GONE);
    }

    public class FetchPokemonTask extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading the pokemons");
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }

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
                pDialog.dismiss();

                mBSearch.setOnClickListener(v -> {
                    InputMethodManager imm = (InputMethodManager) getSystemService(MainActivity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mSearch.getWindowToken(), 0);
                    buscarDatos(listDatos, listUrl);
                    mSearch.setFocusable(false);
                });

                mAll.setOnClickListener(v -> {
                    InputMethodManager imm = (InputMethodManager) getSystemService(MainActivity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mSearch.getWindowToken(), 0);
                    recycler.setAdapter(adapter);
                    mSearch.setText("");
                    mNothing.setVisibility(View.GONE);
                    mSearch.setFocusable(false);
                });

                mSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(MainActivity.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mSearch.getWindowToken(), 0);
                            buscarDatos(listDatos, listUrl);
                        }
                    }
                });


            }
        }
    }

    private void buscarDatos(ArrayList<String> listDatos, ArrayList<String> listUrl){
        if (mSearch.getText().toString().equals("")){
            AdapterDatos Vadapter = new AdapterDatos(listDatos, listUrl);
            recycler.setAdapter(Vadapter);
        } else{
            ArrayList<String> Nlistadatos = new ArrayList<String>();
            ArrayList<String> NlistaUrl = new ArrayList<String>();
            for (int i = 0; i < listDatos.size(); i++){
                if (listDatos.get(i).startsWith(mSearch.getText().toString().toLowerCase())){
                    Nlistadatos.add(listDatos.get(i));
                    NlistaUrl.add(listUrl.get(i));
                };
            }
            if (Nlistadatos.size() == 0){
                mNothing.setVisibility(View.VISIBLE);
            } else{
                mNothing.setVisibility(View.GONE);
            }
            AdapterDatos adapterN = new AdapterDatos(Nlistadatos, NlistaUrl);
            recycler.setAdapter(adapterN);
        }
    }
}
