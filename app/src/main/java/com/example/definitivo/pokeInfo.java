package com.example.definitivo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class pokeInfo extends AppCompatActivity {

    TextView mNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_info);
        Intent mIntent = getIntent();
        mNombre = findViewById(R.id.tv_nombre_poke);
        mNombre.setText("Nombre: " + mIntent.getStringExtra("nombre"));
    }
}
