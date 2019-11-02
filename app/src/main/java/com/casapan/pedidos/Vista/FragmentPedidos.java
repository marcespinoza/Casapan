package com.casapan.pedidos.Vista;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.casapan.pedidos.Adapter.ListaPedidosAdapter;
import com.casapan.pedidos.BuildConfig;
import com.casapan.pedidos.Interface.ListItem;
import com.casapan.pedidos.Database.DatabaseHelper;
import com.casapan.pedidos.Pojo.Pedido;
import com.casapan.pedidos.R;
import com.casapan.pedidos.Util.ProgressDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentPedidos extends Fragment {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    private Boolean isFabOpen = true;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    PedidoDialog aDialog;
    DatabaseHelper db;
    @BindView(R.id.fab_pedido) FloatingActionButton fabPedido;
    @BindView(R.id.fab_armatutorta) FloatingActionButton fabTorta;
    @BindView(R.id.layout_pedido) LinearLayout layoutpedido;
    @BindView(R.id.layout_torta) LinearLayout layouttorta;
    private boolean fabExpanded = false;
    String[] permissions= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final int MULTIPLE_PERMISSIONS = 10;
    ListaPedidosAdapter pAdapter;
    @BindView(R.id.recycler_pedidos) RecyclerView recyclerPedido;
    ProgressDialog generarPdf;
    ArrayList<ListItem> pedidos;
    Snackbar mySnackbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedido, container, false);
        ButterKnife.bind(this, view);
        generarPdf = new ProgressDialog(getContext());
        mySnackbar = Snackbar.make(view, "Pdf generado", Snackbar.LENGTH_LONG);
        db = new DatabaseHelper(getActivity());
        fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_backward);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFAB();
            }
        });
        fabPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                aDialog = PedidoDialog.newInstance("");
                aDialog.setInterface(new PedidoDialog.OnAceptarBoton() {
                    @Override
                    public void clickbutton(String usuario,ArrayList<ListItem> pedidos, String obs) {
                        insertarPedidos(usuario, pedidos, obs);
                    }
                });
                aDialog.show(fm, "Fragment articulo");
            }
        });
        animateFAB();
        db = new DatabaseHelper(getActivity());
        int currentApiVersion = Build.VERSION.SDK_INT;
        if(currentApiVersion >=  Build.VERSION_CODES.M)        {
                checkPermission();
        }
        cargarPedidos();
        return view;
    }


    public void animateFAB(){
        if(isFabOpen){
            fab.startAnimation(rotate_backward);
            layoutpedido.startAnimation(fab_close);
            layoutpedido.setClickable(false);
            layouttorta.startAnimation(fab_close);
            layouttorta.setClickable(false);
            isFabOpen = false;
        } else {
            fab.startAnimation(rotate_forward);
            layoutpedido.startAnimation(fab_open);
            layoutpedido.setClickable(true);
            layouttorta.startAnimation(fab_open);
            layouttorta.setClickable(true);
            isFabOpen = true;

        }
    }


    public void cargarPedidos(){
        ArrayList<Pedido> lPedido = db.getPedidos();
        pAdapter = new ListaPedidosAdapter(lPedido);
        recyclerPedido.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Attach the adapter to the recyclerview to populate items
        recyclerPedido.setAdapter(pAdapter);
    }

    public void insertarPedidos(String nombre, ArrayList<ListItem> articulos, String observacion){

        int id = (int) db.insertarPedido(nombre,observacion);
        for(int i = 0; i < articulos.size(); i++){
            int cant = Integer.parseInt(articulos.get(i).mostrarCantidad());
            int stock = Integer.parseInt(articulos.get(i).mostrarStock());
            db.insertarLineaPedido(id,Integer.parseInt(articulos.get(i).mostrarId()), cant, stock);
        }
        cargarPedidos();
        pedidos = articulos;

        new GeneraPDF().execute();
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
    private void showDialogNotCancelable(String title, String message,
                                         DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setCancelable(false)
                .create()
                .show();
    }



    private class GeneraPDF extends AsyncTask<Void, Integer, Boolean> {

        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        Date todayDate = new Date();
        String fecha = currentDate.format(todayDate);

        @Override
        protected Boolean doInBackground(Void... params) {

            Font largeBold = new Font(Font.FontFamily.COURIER, 32, Font.BOLD);
            Document document = new Document();
            String fpath = Environment.getExternalStorageDirectory().getPath() + "/Pedidos-"+fecha+".pdf";
            File file = new File(fpath);
            try {
                Drawable d = ContextCompat.getDrawable(getActivity(),R.drawable.logo_casapan);
                BitmapDrawable bitDw = ((BitmapDrawable) d);
                Bitmap bmp = bitDw.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = Image.getInstance(stream.toByteArray());
                image.scaleToFit(100,50);
                image.setAlignment(Element.ALIGN_CENTER);
                PdfWriter.getInstance(document,new FileOutputStream(file));
                document.open();
                document.add(image);
                float[] columnWidths = new float[]{ 100f, 10f, 10f};
                //---Encabezado--//
                PdfPTable table = new PdfPTable(3);
                table.setSpacingBefore(20);
                table.setWidthPercentage(100);
                table.setWidths(columnWidths);
                PdfPCell headerproducto= new PdfPCell(new Phrase("Producto"));
                table.addCell(headerproducto);
                PdfPCell headercantidad = new PdfPCell(new Phrase("Cant."));
                table.addCell(headercantidad);
                PdfPCell headerstock = new PdfPCell(new Phrase("Stock"));
                table.addCell(headerstock);
                document.add(table);

                for(int i = 0; i < pedidos.size(); i++){
                    PdfPTable tablelineapedido = new PdfPTable(3);
                    tablelineapedido.setWidthPercentage(100);
                    tablelineapedido.setWidths(columnWidths);
                    PdfPCell producto= new PdfPCell(new Phrase(pedidos.get(i).mostrarNombre()));
                    producto.setBorder(Rectangle.NO_BORDER);
                    tablelineapedido.addCell(producto);
                    PdfPCell cantidad = new PdfPCell(new Phrase(pedidos.get(i).mostrarCantidad()));
                    cantidad.setBorder(Rectangle.NO_BORDER);
                    tablelineapedido.addCell(cantidad);
                    PdfPCell stock = new PdfPCell(new Phrase(pedidos.get(i).mostrarStock()));
                    stock.setBorder(Rectangle.NO_BORDER);
                    tablelineapedido.addCell(stock);
                    document.add(tablelineapedido);
                }
                document.close();
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return true;
        }


        @Override
        protected void onPreExecute() {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            generarPdf.showProgressDialog("Generando PDF");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            generarPdf.finishDialog();

            mySnackbar.setActionTextColor(getResources().getColor(R.color.white))
                    .setAction("Abrir", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Pedidos-"+fecha+".pdf");
                            Uri uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider",file);
                            Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                            pdfOpenintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
                            pdfOpenintent.setDataAndType(uri, "application/pdf");
                            try {
                                startActivity(pdfOpenintent);
                            } catch (ActivityNotFoundException e) {

                            }
                        }
                    })
                    .show();
        }
    }

    public class MyUndoListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            // Code to undo the user's last action
        }
    }

}
