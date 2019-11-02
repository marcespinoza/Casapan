package com.casapan.pedidos.Adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.casapan.pedidos.Interface.ListItem;
import com.casapan.pedidos.Pojo.ListaPedido;
import com.casapan.pedidos.R;

import java.util.ArrayList;
import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ListItem> aList;
    private ArrayList<ListItem> selArticulos = new ArrayList<>();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public PedidoAdapter(ArrayList<ListItem> aList) {
        this.aList = aList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_categoria, parent, false);
            return new PedidoAdapter.HeaderHolder(layoutView);
        } else if (viewType == TYPE_ITEM) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_articulo_pedido, parent, false);
            return new PedidoAdapter.ArticuloHolder(layoutView);
        }
        else
            throw new RuntimeException("Could not inflate layout");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()== TYPE_ITEM){
            ArticuloHolder articuloHolder = (ArticuloHolder) holder;
            articuloHolder.descArticulo.setText(aList.get(position).mostrarNombre());
            articuloHolder.cantidad.setText("0");
            articuloHolder.stock.setText("0");
            articuloHolder.cantidad.setShowSoftInputOnFocus(false);
            articuloHolder.cantidad.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable e) {
                    int index = getIndex(aList.get(position).mostrarId());
                    selArticulos.get(index).setCantidad(e.toString());
                }
            });
            articuloHolder.stock.setShowSoftInputOnFocus(false);
            articuloHolder.stock.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable e) {
                    int index = getIndex(aList.get(position).mostrarId());
                    selArticulos.get(index).setStock(e.toString());
                }
            });
            articuloHolder.agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(articuloHolder.check.isChecked()){

                if(articuloHolder.cantidad.hasFocus()){
                    String cant = articuloHolder.cantidad.getText().toString();
                    int cantArt = Integer.valueOf(cant);
                    cantArt++;
                    articuloHolder.cantidad.setText(String.valueOf(cantArt));
                } else if(articuloHolder.stock.hasFocus()){
                    String cant = articuloHolder.stock.getText().toString();
                    int cantArt = Integer.valueOf(cant);
                    cantArt++;
                    articuloHolder.stock.setText(String.valueOf(cantArt));
                }
                }
            }
        });
            articuloHolder.quitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(articuloHolder.check.isChecked()){
                    if(articuloHolder.cantidad.hasFocus()){
                        String cant = articuloHolder.cantidad.getText().toString();
                        int cantArt = Integer.valueOf(cant);
                        cantArt--;
                        if(cantArt>=0)
                            articuloHolder.cantidad.setText(String.valueOf(cantArt));
                    } else if(articuloHolder.stock.hasFocus()){
                        String cant = articuloHolder.stock.getText().toString();
                        int cantArt = Integer.valueOf(cant);
                        cantArt--;
                        if(cantArt>=0)
                        articuloHolder.stock.setText(String.valueOf(cantArt));
                    }
                }
            }
        });
            articuloHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ListaPedido listaPedido = new ListaPedido();
                if(b){
                    articuloHolder.cantidad.setEnabled(true);
                    articuloHolder.stock.setEnabled(true);
                    listaPedido.setNombreArticulo(aList.get(position).mostrarNombre());
                    listaPedido.setID(aList.get(position).mostrarId());
                    selArticulos.add(listaPedido);
                }else{
                    remove(aList.get(position).mostrarId());
                    articuloHolder.cantidad.setEnabled(false);
                    articuloHolder.stock.setEnabled(false);
                }
            }
        });
        }else{
            PedidoAdapter.HeaderHolder headerHolder = (PedidoAdapter.HeaderHolder) holder;
            headerHolder.nombreCategoria.setText(aList.get(position).mostrarNombre());
        }
    }

    public void remove(String id){
        for(int i = 0; i < selArticulos.size(); i++){
            if(selArticulos.get(i).mostrarId().equals(id))
                selArticulos.remove(i);
        }
    }

    private int getIndex(String id){
        for(int i = 0; i < selArticulos.size(); i++){
            if(selArticulos.get(i).mostrarId().equals(id))
                return i;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return aList.size();
    }

    public ArrayList<ListItem> getArticulos(){
        return selArticulos;
    }

    @Override
    public int getItemViewType(int position)
    {
        if(aList.get(position).isHeader()) {
            return TYPE_HEADER;
        }else
            return TYPE_ITEM;
    }



    public List<ListItem> getSelectedItems() {

        List<ListItem> selectedItems = new ArrayList<>();

        return selectedItems;
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

    public static class HeaderHolder extends RecyclerView.ViewHolder {

        TextView nombreCategoria;

        public HeaderHolder(@NonNull View itemView) {
            super(itemView);
            nombreCategoria = itemView.findViewById(R.id.nombrecategoria);
        }
    }
    
}
