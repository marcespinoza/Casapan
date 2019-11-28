package com.casapan.pedidos.Vista;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.casapan.pedidos.Adapter.PedidoAdapter;
import com.casapan.pedidos.Interface.ListItem;
import com.casapan.pedidos.Database.DatabaseHelper;
import com.casapan.pedidos.Pojo.Articulo;
import com.casapan.pedidos.Pojo.HeaderCategoria;
import com.casapan.pedidos.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;


public class PedidoDialog extends DialogFragment {

    RecyclerView recyclerView;
    PedidoAdapter artAdapter;
    RecyclerView.LayoutManager manager;
    ArrayList<ListItem> aList = new ArrayList<>();
    MaterialButton aceptar, cancelar;
    DatabaseHelper db;
    EditText usuario, obser;
    private OnAceptarBoton onAceptarBoton;
    String idedicion;
    String id = "";

    public interface OnAceptarBoton{
        void clickbutton(String id, String usuario, ArrayList<ListItem> pedidos, String obser);
    }

       @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.pedido_dialog,container);
        if (getArguments() != null)
           idedicion = getArguments().getString("id");
           aceptar = rootView.findViewById(R.id.aceptarpedido);
           cancelar = rootView.findViewById(R.id.cancelarpedido);
           usuario = rootView.findViewById(R.id.usuario);
           usuario.setOnFocusChangeListener(new View.OnFocusChangeListener() {
               @Override
               public void onFocusChange(View view, boolean b) {
                   if(!b)
                       hideKeyboard();
               }
           });
           obser = rootView.findViewById(R.id.observacion);
           obser.setOnFocusChangeListener(new View.OnFocusChangeListener() {
               @Override
               public void onFocusChange(View view, boolean b) {
                   if(!b)
                       hideKeyboard();
               }
           });
           db = new DatabaseHelper(getActivity());
           aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ListItem> art = artAdapter.getArticulos();
                onAceptarBoton.clickbutton(id, usuario.getText().toString(), art, obser.getText().toString());
                dismiss();
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        manager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_linea_pedido);
        recyclerView.setLayoutManager(manager);
        cargarArticulos();
        return rootView;
    }

    public void setInterface(OnAceptarBoton onAceptarBoton){
        this.onAceptarBoton=onAceptarBoton;
    }

    public void cargarArticulos(){
        ArrayList<Articulo> lArt = db.getArticulosPorCategoria();
        ArrayList<ListItem> editPedidos = null;
        if(idedicion!=null ){
            editPedidos = db.getPedidosbyId(idedicion);
            if(editPedidos!=null){
                usuario.setText(editPedidos.get(0).getNombre());
                obser.setText(editPedidos.get(0).getObservacion());
                id = editPedidos.get(0).getId();
            }
        }
        String categoria = "";
        for(int i = 0; i < lArt.size(); i++){
            if(!categoria.equals(lArt.get(i).getCategoria())){
                categoria = lArt.get(i).getCategoria();
                HeaderCategoria headerCategoria = new HeaderCategoria();
                headerCategoria.setId(lArt.get(i).getIdCategoria());
                headerCategoria.setNombre(lArt.get(i).getCategoria());
                aList.add(headerCategoria);
            }
            Articulo articulo = new Articulo();
            articulo.setNombre(lArt.get(i).getNombre());
            articulo.setId(lArt.get(i).getId());
            articulo.setCantidad("0");
            articulo.setStock("0");
            aList.add(articulo);
            if(editPedidos!=null){
                for (ListItem listItem : editPedidos) {
                    if (listItem.getNombre()!=null && listItem.getNombre().equalsIgnoreCase(articulo.getNombre()) ) {
                        articulo.setCantidad(listItem.getCantidad());
                        articulo.setStock(listItem.getStock());
                        articulo.setIdLineaPedido(listItem.getId());
                    }
                }
            }
        }

        artAdapter = new PedidoAdapter(aList, editPedidos);
        recyclerView.setAdapter(artAdapter);
    }

    public static PedidoDialog newInstance(String Pedido) {
        PedidoDialog frag = new PedidoDialog();
        return frag;
    }

    public void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }


    @Override
    public void onResume() {
        super.onResume();
        int width = getActivity().getResources().getDisplayMetrics().widthPixels;
        int height = getActivity().getResources().getDisplayMetrics().heightPixels;
        Window window = getDialog().getWindow();
        window.setLayout(width-50, height-200);
        window.setGravity(Gravity.CENTER);
    }

}
