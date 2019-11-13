package com.casapan.pedidos.Vista;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.casapan.pedidos.Adapter.ListaPedidosAdapter;
import com.casapan.pedidos.BuildConfig;
import com.casapan.pedidos.Interface.ListItem;
import com.casapan.pedidos.Database.DatabaseHelper;
import com.casapan.pedidos.Interface.PedidoInterface;
import com.casapan.pedidos.Pojo.Pedido;
import com.casapan.pedidos.Presentador.PedidoPresentador;
import com.casapan.pedidos.R;
import com.casapan.pedidos.Util.Constants;
import com.casapan.pedidos.Util.ProgressDialog;
import com.casapan.pedidos.Util.SwipeControllerListaPedidos;
import com.casapan.pedidos.Util.ViewAnimation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentPedidos extends Fragment implements PedidoInterface.Vista {

    @BindView(R.id.fab)  FloatingActionButton fab;
    private Boolean isFabOpen = false;
    PedidoDialog pDialog;
    TortaDialog tDialog;
    DatabaseHelper db;
    @BindView(R.id.fab_pedido) FloatingActionButton fabPedido;
    @BindView(R.id.fab_armatutorta) FloatingActionButton fabTorta;
    private boolean fabExpanded = false;
    String[] permissions= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final int MULTIPLE_PERMISSIONS = 10;
    ListaPedidosAdapter pAdapter;
    @BindView(R.id.recycler_pedidos) RecyclerView recyclerPedido;
    ProgressDialog generarPdf;
    ArrayList<ListItem> pedidos = new ArrayList<>();
    Snackbar mySnackbar;
    private PedidoInterface.Presentador presentador;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedido, container, false);
        ButterKnife.bind(this, view);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFAB(view);
            }
        });
        fabPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogPedido("");
            }
        });
        fabTorta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogTorta();
            }
        });
        db = new DatabaseHelper(getActivity());
        int currentApiVersion = Build.VERSION.SDK_INT;
        if(currentApiVersion >=  Build.VERSION_CODES.M)        {
                checkPermission();
        }
        ViewAnimation.init(fabTorta);
        ViewAnimation.init(fabPedido);
        init(view);
        cargarPedidos();
        return view;
    }

    public void showDialogPedido(String id){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        pDialog = PedidoDialog.newInstance("");
        pDialog.setInterface(new PedidoDialog.OnAceptarBoton() {
            @Override
            public void clickbutton(String id, String usuario, ArrayList<ListItem> pedidos, String obs) {
                if(id.equals("")){
                    insertarPedidos(usuario, pedidos, obs);
                }else{
                    db.updatePedido(id, usuario, obs, pedidos);
                }
            }
        });
        if(!id.equals("")){
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        pDialog.setArguments(bundle);
        }
        pDialog.show(fm, "Fragment pedido");
    }

    public void showDialogTorta(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        tDialog = TortaDialog.newInstance();
        tDialog.OnAceptarButton(new TortaDialog.OnAceptarBoton() {
            @Override
            public void enviarpath(String [] params) {
                generarPdf.showProgressDialog("Generando PDF");
                presentador.armarPedidoTorta(params);
            }
        });
        tDialog.show(fm, "Fragment torta");
    }


    public void animateFAB(View v){
        isFabOpen = ViewAnimation.rotateFab(v, !isFabOpen);
        if(isFabOpen){
            ViewAnimation.showIn(fabPedido);
            ViewAnimation.showIn(fabTorta);
        }else{
            ViewAnimation.showOut(fabTorta);
            ViewAnimation.showOut(fabPedido);
        }
    }

    void init(View v){
        generarPdf = new ProgressDialog(getContext());
        mySnackbar = Snackbar.make(v, "Pdf generado", Snackbar.LENGTH_LONG);
        db = new DatabaseHelper(getActivity());
        presentador = new PedidoPresentador(this);

    }


    public void cargarPedidos(){
        ArrayList<Pedido> lPedido = db.getPedidos();
        pAdapter = new ListaPedidosAdapter(lPedido, new ListaPedidosAdapter.Pdf() {
            @Override
            public void ondownload(String id, String f) {
                abrirPdf(id);
            }
        });
        recyclerPedido.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Attach the adapter to the recyclerview to populate items
        recyclerPedido.setAdapter(pAdapter);
        SwipeControllerListaPedidos swipeControllerListaPedidos = new SwipeControllerListaPedidos(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                ArrayList<Pedido>lPedidos = db.getPedidos();
                String id = lPedidos.get(position).getId();
                if(direction == ItemTouchHelper.LEFT)  {
                   int response = db.borrarPedido(id);
                    db.borrarLineaPedido(id);
                    if(response!=-1)
                        Toast.makeText(getActivity(), "Pedido eliminado", Toast.LENGTH_SHORT).show();
                    pAdapter.removeItem(position);
                }
                else if (direction == ItemTouchHelper.RIGHT) {
                    pAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    showDialogPedido(id);
                }
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeControllerListaPedidos);
        itemTouchhelper.attachToRecyclerView(recyclerPedido);
    }

    public void insertarPedidos(String nombre, ArrayList<ListItem> articulos, String observacion){
        int id = (int) db.insertarPedido(nombre,observacion);
        for(int i = 0; i < articulos.size(); i++){
            int cant = Integer.parseInt(articulos.get(i).getCantidad());
            if(!articulos.get(i).isHeader() && cant >=1){
                int stock = Integer.parseInt(articulos.get(i).getStock());
                db.insertarLineaPedido(id,Integer.parseInt(articulos.get(i).getId()), cant, stock);
                pedidos.add(articulos.get(i));
            }
        }
        cargarPedidos();
        String [] parametros = {nombre, String.valueOf(id), observacion};
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
        generarPdf.showProgressDialog("Generando PDF");
        presentador.generarPdf(parametros, pedidos);
    }

    private boolean checkPermission() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(getActivity(),p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
            switch (requestCode) {
                case MULTIPLE_PERMISSIONS:{
                    if (grantResults.length > 0) {
                        String permissionsDenied = "";
                        for (String per : permissions) {
                            if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                                permissionsDenied += "\n" + per;
                                showDialogNotCancelable("Permissions mandatory",
                                        "All the permissions are required for this app",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                checkPermission();
                                            }
                                        });
                            }

                        }

                    }
                    return;
                }

        }
    }
    private void showDialogNotCancelable(String title, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public void mostrarPedidos() {

    }

    @Override
    public void mostrarPdf(String path) {
        generarPdf.finishDialog();
        mySnackbar.setAction("Abrir PDF", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirPdf(path);
            }
        }).show();
    }


    public void abrirPdf(String path){
       File file = new File(Environment.getExternalStorageDirectory().getPath() + path);
       Uri uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider",file);
       Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
       pdfOpenintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
       pdfOpenintent.setDataAndType(uri, "application/pdf");
       try {
           startActivity(pdfOpenintent);
       } catch (ActivityNotFoundException e) {
           Toast.makeText(getContext(), "Archivo no encontrado", Toast.LENGTH_SHORT).show();
       }
   }

}
