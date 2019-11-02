package com.casapan.pedidos.Vista;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.casapan.pedidos.Adapter.ArticuloAdapter;
import com.casapan.pedidos.Interface.ListItem;
import com.casapan.pedidos.Pojo.HeaderCategoria;
import com.casapan.pedidos.Database.DatabaseHelper;
import com.casapan.pedidos.Pojo.Articulo;
import com.casapan.pedidos.Pojo.Categoria;
import com.casapan.pedidos.R;
import com.casapan.pedidos.Util.SwipeController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentArticulo extends Fragment {

    @BindView(R.id.agregarcategoria) ImageButton agregarCat;
    @BindView(R.id.agregararticulo) ImageButton agregarArt;
    @BindView(R.id.spinnercategoria) AppCompatSpinner spinnercategorias;
    @BindView(R.id.recycler_articulo) RecyclerView recyclerArticulo;
    @BindView(R.id.artnombre) EditText nombreArt;
    ArrayAdapter<Categoria> adapterSpinner;
    ArticuloAdapter adapter;
    CategoriaDialog catDialog;
    DatabaseHelper dbh;
    ArrayList<ListItem> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_articulo, container, false);
        ButterKnife.bind(this, view);
        dbh = new DatabaseHelper(getActivity());
        // Create adapter passing in the sample user data

        agregarCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                catDialog = CategoriaDialog.newInstance("");
                catDialog.setDissmissListener(new CategoriaDialog.DissmissListener() {
                    @Override
                    public void onDismiss(CategoriaDialog categoriaDialog) {
                        cargarCategorias();
                    }
                });
                catDialog.show(fm, "Fragment articulo");
            }
        });
        agregarArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
                agregarArticulo();
            }
        });
        spinnercategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Categoria categoria = (Categoria) adapterView.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        cargarCategorias();
        cargarArticulos();
        return view;
    }

    public void agregarArticulo(){
        Categoria categoria = (Categoria) spinnercategorias.getSelectedItem();
        if(spinnercategorias.getSelectedItemPosition()!=0){
        if(!nombreArt.getText().toString().isEmpty()){
        boolean bol = dbh.insertarArticulo(nombreArt.getText().toString(), categoria.getId());
        if(bol){
            nombreArt.setText("");
            cargarArticulos();
        }
        }else{
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });

        }
        }else{
            Toast.makeText(getActivity(),"Seleccione una categoria", Toast.LENGTH_SHORT).show();
        }
    }

    public void cargarCategorias(){
        ArrayList<Categoria> lCat = dbh.getCategorias();
        List<String> list = new ArrayList<>();
        for(int i = 0; i < lCat.size(); i++){
            list.add(lCat.get(i).getNombre());
        }
        adapterSpinner = new ArrayAdapter<Categoria>(getActivity(), android.R.layout.simple_spinner_item, lCat);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnercategorias.setAdapter(adapterSpinner);
    }

    public void clear () {
        list.clear();
        adapter.notifyDataSetChanged();
    }

    public void cargarArticulos(){
        ArrayList<Articulo> lArt = dbh.getArticulosPorCategoria();
        String categoria = "";
        for(int i = 0; i < lArt.size(); i++){
            if(!categoria.equals(lArt.get(i).getCategoria())){
                categoria = lArt.get(i).getCategoria();
                HeaderCategoria headerCategoria = new HeaderCategoria();
                headerCategoria.setId(lArt.get(i).getIdCategoria());
                headerCategoria.setNombre(lArt.get(i).getCategoria());
                list.add(headerCategoria);
            }
            Articulo articulo = new Articulo();
            articulo.setNombre(lArt.get(i).getNombre());
            articulo.setId(lArt.get(i).getId());
            list.add(articulo);
        }

        adapter = new ArticuloAdapter(list);
        recyclerArticulo.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Attach the adapter to the recyclerview to populate items
        recyclerArticulo.setAdapter(adapter);
        // Set layout manager to position the items
        SwipeController swipeController = new SwipeController(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                int viewType = viewHolder.getItemViewType();
                String id = adapter.getId(position);
                String descripcion = adapter.getNombre(position);
                if(direction == ItemTouchHelper.LEFT)
                {
                    int response = dbh.borrarArticulo(id);
                    if(response!=-1)
                      Toast.makeText(getActivity(), "Articulo eliminado", Toast.LENGTH_SHORT).show();
                      adapter.removeItem(position);
                }
                else if (direction == ItemTouchHelper.RIGHT)
                {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    EditarDialog eDialog = new EditarDialog();
                    eDialog.editar(new EditarDialog.Editar() {
                        @Override
                        public void guardarEdicion(String nombre) {
                            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                            dbh.updateArticulo(id,nombre);
                            eDialog.dismiss();
                            clear();
                            cargarArticulos();
                        }
                    });
                    eDialog.show(getActivity().getSupportFragmentManager(), "EDITAR DIALOGO");
                    eDialog.setDescripcion(descripcion);
                    eDialog.setType(viewType);
                }
             }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerArticulo);
    }

}
