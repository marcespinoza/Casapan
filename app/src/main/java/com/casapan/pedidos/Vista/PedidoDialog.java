package com.casapan.pedidos.Vista;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.casapan.pedidos.Adapter.PedidoAdapter;
import com.casapan.pedidos.Adapter.Utils.ListItem;
import com.casapan.pedidos.Database.DatabaseHelper;
import com.casapan.pedidos.Model.Articulo;
import com.casapan.pedidos.Model.HeaderCategoria;
import com.casapan.pedidos.R;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class PedidoDialog extends DialogFragment {

    com.casapan.pedidos.Model.Pedido Pedido;
    RecyclerView recyclerView;
    PedidoAdapter artAdapter;
    RecyclerView.LayoutManager manager;
    ArrayList<ListItem> aList = new ArrayList<>();
    MaterialButton aceptar, cancelar;
    DatabaseHelper db;

       @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.pedido_dialog,container);
        aceptar = rootView.findViewById(R.id.aceptarpedido);
        cancelar = rootView.findViewById(R.id.cancelarpedido);
        db = new DatabaseHelper(getActivity());
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
         manager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_pedido);
        recyclerView.setLayoutManager(manager);
        cargarArticulos();
        return rootView;
    }


    public void cargarArticulos(){
        ArrayList<Articulo> lArt = db.getArticulosPorCategoria();
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
            aList.add(articulo);
        }

        artAdapter = new PedidoAdapter(aList);
        recyclerView.setAdapter(artAdapter);
    }

    public static PedidoDialog newInstance(String Pedido) {
        PedidoDialog frag = new PedidoDialog();
        return frag;
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getActivity().getResources().getDisplayMetrics().widthPixels;
        int height = getActivity().getResources().getDisplayMetrics().heightPixels;
        Window window = getDialog().getWindow();

        window.setLayout(width-100, height-700
        );
        window.setGravity(Gravity.CENTER);
    }

    private void createPdf(String sometext){
        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle(50, 50, 30, paint);
        paint.setColor(Color.BLACK);
        canvas.drawText(sometext, 80, 50, paint);
        //canvas.drawt
        // finish the page
        document.finishPage(page);
// draw text on the graphics object of the page
        // Create Page 2
        pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 2).create();
        page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawCircle(100, 100, 100, paint);
        document.finishPage(page);
        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"test-2.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
        }
        // close the document
        document.close();
    }


}
