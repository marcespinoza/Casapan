package com.casapan.pedidos.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.casapan.pedidos.Model.Articulo;
import com.casapan.pedidos.R;

import java.util.ArrayList;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.ArticuloHolder> {

    private ArrayList<Articulo> aList;


    public PedidoAdapter(ArrayList<Articulo> aList) {
        this.aList = aList;
    }

    @Override
    public ArticuloHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_articulo_pedido, parent, false);
        return new ArticuloHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArticuloHolder holder, int position) {
        Articulo articulo = aList.get(position);
        holder.descArticulo.setText(articulo.getNombre());
        holder.agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.check.isChecked()){

                if(holder.cantidad.hasFocus()){
                    String cant = holder.cantidad.getText().toString();
                    int cantArt = Integer.valueOf(cant);
                    cantArt++;
                   holder.cantidad.setText(String.valueOf(cantArt));
                } else if(holder.stock.hasFocus()){
                    String cant = holder.stock.getText().toString();
                    int cantArt = Integer.valueOf(cant);
                    cantArt++;
                    holder.stock.setText(String.valueOf(cantArt));
                }
                }
            }
        });
        holder.quitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.check.isChecked()){
                    if(holder.cantidad.hasFocus()){
                        String cant = holder.cantidad.getText().toString();
                        int cantArt = Integer.valueOf(cant);
                        cantArt--;
                        if(cantArt>=0)
                        holder.cantidad.setText(String.valueOf(cantArt));
                    } else if(holder.stock.hasFocus()){
                        String cant = holder.stock.getText().toString();
                        int cantArt = Integer.valueOf(cant);
                        cantArt--;
                        if(cantArt>=0)
                        holder.stock.setText(String.valueOf(cantArt));
                    }
                }
            }
        });
        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    holder.cantidad.setEnabled(true);
                    holder.stock.setEnabled(true);
                }else{
                    holder.cantidad.setEnabled(false);
                    holder.stock.setEnabled(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return aList.size();
    }

    public class ArticuloHolder extends RecyclerView.ViewHolder {
        public TextView descArticulo;
        public ImageButton agregar, quitar;
        EditText cantidad, stock;
        AppCompatCheckBox check;

        public ArticuloHolder(View view) {
            super(view);
            descArticulo = view.findViewById(R.id.descarticulo);
            agregar = view.findViewById(R.id.agregar);
            quitar = view.findViewById(R.id.quitar);
            cantidad = view.findViewById(R.id.cantidadart);
            stock =  view.findViewById(R.id.stock);
            check = view.findViewById(R.id.check);
        }
    }
    
}
