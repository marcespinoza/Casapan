package com.casapan.pedidos.Modelo;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.casapan.pedidos.App.GlobalApplication;
import com.casapan.pedidos.BuildConfig;
import com.casapan.pedidos.Interface.ListItem;
import com.casapan.pedidos.Interface.PedidoInterface;
import com.casapan.pedidos.R;
import com.casapan.pedidos.Util.Constants;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
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

public class PedidoModelo implements PedidoInterface.Modelo {
    
    private PedidoInterface.Presentador presentador;
    Context context;
    String sucursal = "";
    String fecha = "";

    public PedidoModelo(PedidoInterface.Presentador presentador) {
        this.presentador = presentador;
        context = GlobalApplication.getAppContext();
        sucursal = Constants.getSPreferences(context).getNombreSucursal();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        Date todayDate = new Date();
        fecha = currentDate.format(todayDate);
    }

    @Override
    public void getPedidos() {

    }

    @Override
    public void generarPdf(String [] params, ArrayList<ListItem> pedidos) {
        Object [] objects = {params, pedidos};
        new GeneraPDF().execute(objects);
    }

    private class GeneraPDF extends AsyncTask<Object, Integer, String> {

        @Override
        protected String doInBackground(Object... parameters) {
            String [] params = (String[]) parameters[0];
            ArrayList<ListItem> pedidos = (ArrayList<ListItem>) parameters[1];

            Document document = new Document();
            String fpath = Environment.getExternalStorageDirectory().getPath() + "/Pedido"+params[1]+"-"+sucursal+"-"+fecha+".pdf";
            File file = new File(fpath);
            try {
                Drawable d = ContextCompat.getDrawable(context, R.drawable.logo_casapan);
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
                Paragraph fch = new Paragraph(fecha);
                fch.setAlignment(Element.ALIGN_RIGHT);
                document.add(fch);
                Paragraph nombre =  new Paragraph(params[0]);
                document.add(nombre);
                Paragraph suc = new Paragraph("Sucursal: "+sucursal);
                document.add(suc);
                Paragraph obs = new Paragraph("Observacion: "+params[2]);
                document.add(obs);
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
                    PdfPCell producto= new PdfPCell(new Phrase(pedidos.get(i).getNombre()));
                    producto.setBorder(Rectangle.NO_BORDER);
                    tablelineapedido.addCell(producto);
                    PdfPCell cantidad = new PdfPCell(new Phrase(pedidos.get(i).getCantidad()));
                    cantidad.setBorder(Rectangle.NO_BORDER);
                    tablelineapedido.addCell(cantidad);
                    PdfPCell stock = new PdfPCell(new Phrase(pedidos.get(i).getStock()));
                    stock.setBorder(Rectangle.NO_BORDER);
                    tablelineapedido.addCell(stock);
                    document.add(tablelineapedido);
                }
                pedidos.clear();
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


            return params[1];
        }


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String result) {
           String path = "/Pedido"+result+"-"+sucursal+"-"+fecha+".pdf";
           presentador.mostrarPdf(path);
        }
    }
    
}
