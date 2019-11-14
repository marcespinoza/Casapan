package com.casapan.pedidos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.casapan.pedidos.App.GlobalApplication;
import com.casapan.pedidos.Pojo.Pedido;
import com.casapan.pedidos.R;
import com.casapan.pedidos.Util.Constants;

import java.util.ArrayList;

public class ListaPedidosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Pedido> listPedidos = null;
    private Context mContext;
    private LayoutInflater mInflater;
    private Pdf pdf;
    String sucursal = "";

    public interface Pdf {
        void ondownload(String id, String f);
    }

    public ListaPedidosAdapter(ArrayList<Pedido> listPedidos, Pdf pdf) {
        this.pdf=pdf;
        this.listPedidos = listPedidos;
        mContext = GlobalApplication.getAppContext();
        sucursal = Constants.getSPreferences(mContext).getNombreSucursal();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido, parent, false);
            return new PedidoHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            PedidoHolder pedidoHolder = (PedidoHolder) holder;
            pedidoHolder.fecha.setText(listPedidos.get(position).getFecha());
            pedidoHolder.usuario.setText(listPedidos.get(position).getUsuario());
            pedidoHolder.observacion.setText(listPedidos.get(position).getObservacion());
            pedidoHolder.descargarpdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String fecha = pedidoHolder.fecha.getText().toString();
                    String id = listPedidos.get(position).getId();
                    String path = "";
                    if(listPedidos.get(position).getTorta()==1){
                        path = "/PedidoTorta"+id+"-"+sucursal+"-"+fecha+".pdf";
                    }else{
                        path = "/Pedido"+id+"-"+sucursal+"-"+fecha+".pdf";
                    }

                    pdf.ondownload(id, path);
                }
            });
            if(listPedidos.get(position).getTorta()==1){
                pedidoHolder.torta.setVisibility(View.VISIBLE);
            }
    }

    @Override
    public int getItemCount() {
        return listPedidos.size();
    }

    public void removeItem(int position) {
        listPedidos.remove(position);
        notifyItemRemoved(position);
    }

    public static class PedidoHolder extends RecyclerView.ViewHolder {

        TextView fecha, usuario, observacion;
        ImageButton descargarpdf;
        ImageView torta;

        public PedidoHolder(@NonNull View itemView) {
            super(itemView);
            fecha = itemView.findViewById(R.id.fecha);
            usuario = itemView.findViewById(R.id.usuario);
            observacion = itemView.findViewById(R.id.observacion);
            descargarpdf = itemView.findViewById(R.id.descargarpdf);
            torta = itemView.findViewById(R.id.torta);
        }
    }

}
