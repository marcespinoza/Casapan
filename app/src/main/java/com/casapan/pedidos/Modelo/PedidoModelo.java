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
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.casapan.pedidos.App.GlobalApplication;
import com.casapan.pedidos.BuildConfig;
import com.casapan.pedidos.Database.DatabaseHelper;
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
    DatabaseHelper db;

    public PedidoModelo(PedidoInterface.Presentador presentador) {
        this.presentador = presentador;
        context = GlobalApplication.getAppContext();
        sucursal = Constants.getSPreferences(context).getNombreSucursal();
        db = new DatabaseHelper(context);
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        Date todayDate = new Date();
        fecha = currentDate.format(todayDate);
    }

    @Override
    public void getPedidos() {

    }

    @Override
    public void pedidoTorta(String [] params) {
        guardarTorta(params);
        new GeneraPedidoTorta().execute(params);
    }

    public void guardarTorta(String [] params){
        db.insertarPedidoTorta(sucursal, params);
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

    class GeneraPedidoTorta extends AsyncTask<String, Integer, String> {

        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        Date todayDate = new Date();
        String fecha = currentDate.format(todayDate);

        @Override
        protected String doInBackground(String... params) {

            Document document = new Document();
            String path = "/PedidoTorta"+"-"+sucursal+"-"+fecha+".pdf";
            String fpath = Environment.getExternalStorageDirectory().getPath() + path;
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
                Paragraph titulo = new Paragraph("ARMÁ TU TORTA");
                titulo.setAlignment(Element.ALIGN_CENTER);
                titulo.setSpacingAfter(20);
                titulo.setSpacingBefore(20);
                document.add(titulo);
                float[] columnEncabezado = new float[]{60f, 60f};
                //------Local y fecha-----------///
                PdfPTable localfecha= new PdfPTable(2);
                localfecha.setWidthPercentage(100);
                localfecha.setWidths(columnEncabezado);
                PdfPCell localentrega= new PdfPCell(new Phrase("LOCAL DE ENTREGA: "+sucursal));
                localentrega.setBorder(Rectangle.NO_BORDER);
                localfecha.addCell(localentrega);
                PdfPCell fechapedido = new PdfPCell(new Phrase("FECHA PEDIDO: "+ fecha));
                fechapedido.setBorder(Rectangle.NO_BORDER);
                localfecha.addCell(fechapedido);
                document.add(localfecha);
                //--------Cliente y telefono-----//
                PdfPTable clientetelefono= new PdfPTable(2);
                clientetelefono.setWidthPercentage(100);
                clientetelefono.setWidths(columnEncabezado);
                PdfPCell cliente= new PdfPCell(new Phrase("CLIENTE: "+params[0]));
                cliente.setBorder(Rectangle.NO_BORDER);
                clientetelefono.addCell(cliente);
                PdfPCell telefono = new PdfPCell(new Phrase("TELEFONO: "+ params[1]));
                telefono.setBorder(Rectangle.NO_BORDER);
                clientetelefono.addCell(telefono);
                document.add(clientetelefono);
                //--------fecha y hora--------//
                PdfPTable fechahora= new PdfPTable(2);
                fechahora.setWidthPercentage(100);
                fechahora.setWidths(columnEncabezado);
                PdfPCell fecha= new PdfPCell(new Phrase("FECHA DE ENTREGA: "+params[2]));
                fecha.setBorder(Rectangle.NO_BORDER);
                fechahora.addCell(fecha);
                PdfPCell hora = new PdfPCell(new Phrase("HORA DE ENTREGA: "+ params[3]));
                hora.setBorder(Rectangle.NO_BORDER);
                fechahora.addCell(hora);
                document.add(fechahora);
                Paragraph kilogramos = new Paragraph("Kilogramos: "+params[6]);
                kilogramos.setSpacingBefore(20);
                document.add(kilogramos);
                Paragraph bizcoch = new Paragraph("Bizcochuelo: "+ params[7]);
                document.add(bizcoch);
                float[] columnWidths = new float[]{ 61f, 61f};
                //---Encabezado--//
                PdfPTable rellenos = new PdfPTable(2);
                rellenos.setWidthPercentage(100);
                rellenos.setWidths(columnWidths);
                rellenos.setSpacingBefore(5);
                PdfPCell rellenoUno= new PdfPCell(new Phrase("Relleno 1: "+params[6]));
                rellenoUno.setBorder(Rectangle.NO_BORDER);
                rellenos.addCell(rellenoUno);
                PdfPCell rellenoDos = new PdfPCell(new Phrase("Relleno 2: "+params[7]));
                rellenoDos.setBorder(Rectangle.NO_BORDER);
                rellenos.addCell(rellenoDos);
                document.add(rellenos);
                Paragraph colores = new Paragraph("Colores: "+params[8]+params[9]+params[10]+params[11]+params[12]+params[13]+params[14]);
                document.add(colores);
                Paragraph extras = new Paragraph("Extras: "+params[15]+params[16]+params[17]+params[18]);
                document.add(extras);
                Paragraph textoTorta = new Paragraph("Texto: "+params[19]);
                document.add(textoTorta);
                Paragraph espacio_adorno = new Paragraph("Espacio para adorno: "+ params[20]);
                document.add(espacio_adorno);
                Paragraph tomoPedido = new Paragraph("Tomó pedido: "+params[21]);
                document.add(tomoPedido);
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
            return path;
        }


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String result) {
            presentador.mostrarPdf(result);
        }
    }
    
}
