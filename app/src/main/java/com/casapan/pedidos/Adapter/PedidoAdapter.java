package com.casapan.pedidos.Adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.casapan.pedidos.Interface.ListItem;
import com.casapan.pedidos.R;
import java.util.ArrayList;

public class PedidoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ListItem> aList;
    private ArrayList<ListItem> editPedidos;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public PedidoAdapter(ArrayList<ListItem> aList, ArrayList<ListItem> editPedidos) {
        this.aList = aList;
        this.editPedidos = editPedidos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_categoria, parent, false);
            return new PedidoAdapter.HeaderHolder(layoutView);
        } else if (viewType == TYPE_ITEM) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_articulo_pedido, parent, false);
            return new PedidoAdapter.ArticuloHolder(layoutView, new CantidadTextListener(), new StockTextListener());
        }
        else
            throw new RuntimeException("Could not inflate layout");
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()== TYPE_ITEM){
            ArticuloHolder articuloHolder = (ArticuloHolder) holder;
            articuloHolder.descArticulo.setText(aList.get(position).getNombre());
            articuloHolder.cantidad.setShowSoftInputOnFocus(false);
            articuloHolder.cantidadTextListener.updatePosition(holder.getAdapterPosition());
            articuloHolder.stockTextListener.updatePosition(holder.getAdapterPosition());
            /*articuloHolder.cantidad.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable e) {
                    Log.i("POSITION",""+position);

                    aList.get(position).setCantidad(e.toString());
                }
            });*/
            articuloHolder.stock.setShowSoftInputOnFocus(false);
            /*articuloHolder.stock.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable e) {
                    aList.get(position).setStock(e.toString());
                }
            });*/
            articuloHolder.cantidad.setText(aList.get(holder.getAdapterPosition()).getCantidad());
            articuloHolder.stock.setText(aList.get(holder.getAdapterPosition()).getStock());
            /*if(editPedidos!=null){
            for (ListItem listItem : editPedidos) {
                if (listItem.getNombre()!=null && listItem.getNombre().equalsIgnoreCase(aList.get(position).getNombre()) ) {
                    articuloHolder.cantidad.setText(listItem.getCantidad());
                    articuloHolder.stock.setText(listItem.getStock());
                    aList.get(position).setIdLineaPedido(listItem.getId());
                }
              }
            }*/
            articuloHolder.agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });
            articuloHolder.quitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });
        }else{
            PedidoAdapter.HeaderHolder headerHolder = (PedidoAdapter.HeaderHolder) holder;
            headerHolder.nombreCategoria.setText(aList.get(position).getNombre());
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return aList.size();
    }

    public ArrayList<ListItem> getArticulos(){
        return aList;
    }

    @Override
    public int getItemViewType(int position)
    {
        if(aList.get(position).isHeader()) {
            return TYPE_HEADER;
        }else
            return TYPE_ITEM;
    }


    public class ArticuloHolder extends RecyclerView.ViewHolder {
        public TextView descArticulo;
        public ImageButton agregar, quitar;
        EditText cantidad, stock;
        public StockTextListener stockTextListener;
        public CantidadTextListener cantidadTextListener;

        public ArticuloHolder(View view, CantidadTextListener cantidadTextListener, StockTextListener stockTextListener) {
            super(view);
            this.stockTextListener = stockTextListener;
            this.cantidadTextListener = cantidadTextListener;
            descArticulo = view.findViewById(R.id.descarticulo);
            agregar = view.findViewById(R.id.agregar);
            quitar = view.findViewById(R.id.quitar);
            cantidad = view.findViewById(R.id.cantidadart);
            cantidad.addTextChangedListener(cantidadTextListener);
            stock =  view.findViewById(R.id.stock);
            stock.addTextChangedListener(stockTextListener);
        }
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {

        TextView nombreCategoria;

        public HeaderHolder(@NonNull View itemView) {
            super(itemView);
            nombreCategoria = itemView.findViewById(R.id.nombrecategoria);
        }
    }

    private class StockTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable e) {
            aList.get(position).setStock(e.toString());
        }
    }

    private class CantidadTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable e) {
            aList.get(position).setCantidad(e.toString());
        }
    }

}
