package com.example.definitivo.model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.definitivo.R;
import com.example.definitivo.pokeInfo;

import java.util.ArrayList;

public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos> {

    ArrayList<String> listDatos;
    ArrayList<String> listUrl;
    Context context;

    public AdapterDatos(ArrayList<String> listDatos, ArrayList<String> listUrl) {
        this.listDatos = listDatos;
        this.listUrl = listUrl;
    }

    @Override
    public ViewHolderDatos onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokemon, null, false);
        //Con este codigo se configura que el recyclerView acepte parametros de matchParents
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(AdapterDatos.ViewHolderDatos holder, int position) {
        holder.asignarDatos(listDatos.get(position), listUrl.get(position));
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView dato, url;
        Button mostrar, nombre;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.btn_nombre);
            context = itemView.getContext();
        }

        public void asignarDatos(String s, String url) {
            nombre.setText(s);
            nombre.setOnClickListener(v -> {
                Intent intent = new Intent(context, pokeInfo.class);
                intent.putExtra("nombre", s);
                intent.putExtra("url", url);
                context.startActivity(intent);
            });
        }
    }
}
