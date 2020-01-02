package com.casapan.pedidos.Modelo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import androidx.core.content.ContextCompat;

import com.casapan.pedidos.App.GlobalApplication;
import com.casapan.pedidos.Database.DatabaseHelper;
import com.casapan.pedidos.Interface.ListItem;
import com.casapan.pedidos.Interface.PedidoInterface;
import com.casapan.pedidos.R;
import com.casapan.pedidos.Util.Constants;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
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
    public void obtenerPedidoTorta(String id) {
        ArrayList<String> ptorta = db.getPedidoTortabyId(id);
        presentador.enviarPedidoTorta(ptorta);
    }



    @Override
    public void pedidoTorta(String idpedido, String[] params, Bitmap bitmaptorta) {
        if(idpedido==null){
          guardarTorta(params, bitmaptorta);
        }else{
          actualizarTorta(idpedido, params, bitmaptorta);
        }
    }


    public void guardarTorta(String[] params, Bitmap bitmaptorta){
       long id = db.insertarPedidoTorta(sucursal, params);
       if(id!=-1){
           new GeneraPedidoTorta(id, bitmaptorta).execute(params);
       }else{
           presentador.mostrarError("Error al guardar pedido.");
       }

    }

    public void actualizarTorta(String id, String[] params, Bitmap bitmaptorta){
        long idupdate = db.updatePedidoTorta(id, params);
        if(idupdate!=-1){
            new GeneraPedidoTorta(Long.parseLong(id), bitmaptorta).execute(params);
        }else{
            presentador.mostrarError("Error al actualizar pedido.");
        }
    }

    @Override
    public void updatePedido(String id, String usuario, ArrayList<ListItem> pedidos, String obs) {
        long idupdate = db.updatePedido(id, usuario, obs, pedidos);
        if (idupdate!=-1) {
            String [] parametros = {usuario, id, obs};
            Object [] objects = {parametros, pedidos};
            new GeneraPedido().execute(objects);
        }else{
            presentador.mostrarError("Error al actualizar pedido.");
        }
    }

    public void insertarPedidos(String nombre, ArrayList<ListItem> articulos, String observacion){
        int id = (int) db.insertarPedido(nombre,observacion);
        for(int i = 0; i < articulos.size(); i++){
            int cant = Integer.parseInt(articulos.get(i).getCantidad());
            if(!articulos.get(i).isHeader() && cant >=1){
                int stock = Integer.parseInt(articulos.get(i).getStock());
                db.insertarLineaPedido(id,Integer.parseInt(articulos.get(i).getId()), cant, stock);
            }
        }
        String [] parametros = {nombre, String.valueOf(id), observacion};
        Object [] objects = {parametros, articulos};
        new GeneraPedido().execute(objects);
    }

    @Override
    public void generarPdf(String [] params, ArrayList<ListItem> pedidos) {
        Object [] objects = {params, pedidos};
        new GeneraPedido().execute(objects);
    }

    @Override
    public void pedido(String usuario, ArrayList<ListItem> pedidos, String obs) {
        insertarPedidos(usuario, pedidos, obs);
    }

    private class GeneraPedido extends AsyncTask<Object, Integer, String> {

        @Override
        protected String doInBackground(Object... parameters) {
            String [] params = (String[]) parameters[0];
            ArrayList<ListItem> pedidos = (ArrayList<ListItem>) parameters[1];

            Document document = new Document();
            String path = "/Pedido"+params[1]+"-"+sucursal+"-"+fecha+".pdf";
            String pathfolder = Environment.getExternalStorageDirectory().getPath() + "/Casapan";
            File folder = new File(pathfolder);
            folder.mkdirs();
            File file = new File(pathfolder+path);
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
                Paragraph idpedido = new Paragraph("Nro. Pedido: "+params[1]);
                document.add(idpedido);
                Paragraph nombre =  new Paragraph(params[0]);
                document.add(nombre);
                Paragraph suc = new Paragraph("Sucursal: "+sucursal);
                document.add(suc);
                Paragraph obs = new Paragraph("Observacion: "+params[2]);
                document.add(obs);
                float[] columnWidths = new float[]{ 90f, 20f, 20f};
                //---Encabezado--//
                Font f = new Font(Font.FontFamily.HELVETICA, 19.0f, Font.BOLD, BaseColor.BLACK);
                Font f2 = new Font(Font.FontFamily.HELVETICA, 18.0f, Font.NORMAL, BaseColor.BLACK);
                PdfPTable table = new PdfPTable(3);
                table.setSpacingBefore(20);
                table.setWidthPercentage(100);
                table.setWidths(columnWidths);
                PdfPCell headerproducto= new PdfPCell(new Phrase("Producto",f));
                table.addCell(headerproducto);
                PdfPCell headercantidad = new PdfPCell(new Phrase("Cant.",f));
                table.addCell(headercantidad);
                PdfPCell headerstock = new PdfPCell(new Phrase("Stock",f));
                table.addCell(headerstock);
                document.add(table);
                boolean renglon = false;
                for(int i = 0; i < pedidos.size(); i++){
                    if(!pedidos.get(i).isHeader() && !pedidos.get(i).getCantidad().equals("0")){
                    PdfPTable tablelineapedido = new PdfPTable(3);
                    tablelineapedido.setWidthPercentage(100);
                    tablelineapedido.setWidths(columnWidths);
                    PdfPCell producto= new PdfPCell(new Phrase(pedidos.get(i).getNombre(),f2));
                    producto.setBorder(Rectangle.NO_BORDER);
                    PdfPCell cantidad = new PdfPCell(new Phrase(pedidos.get(i).getCantidad(),f2));
                    cantidad.setBorder(Rectangle.NO_BORDER);
                    PdfPCell stock = new PdfPCell(new Phrase(pedidos.get(i).getStock(),f2));
                    stock.setBorder(Rectangle.NO_BORDER);
                    if(renglon){
                        producto.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cantidad.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        stock.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    }
                    renglon = !renglon;
                    tablelineapedido.addCell(producto);
                    tablelineapedido.addCell(cantidad);
                    tablelineapedido.addCell(stock);
                    document.add(tablelineapedido);
                 }
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
           String path = "/Casapan/Pedido"+result+"-"+sucursal+"-"+fecha+".pdf";
           presentador.mostrarPdf(path);
        }
    }

    class GeneraPedidoTorta extends AsyncTask<String, Integer, String> {

        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        Date todayDate = new Date();
        String fecha = currentDate.format(todayDate);
        long id = 0;
        Bitmap bitmaptorta;
        public GeneraPedidoTorta(long id, Bitmap bitmaptorta) {
            this.id = id;
            this.bitmaptorta = bitmaptorta;
        }
        @Override
        protected String doInBackground(String... params) {

            Document document = new Document();
            String path = "/PedidoTorta"+id+"-"+sucursal+"-"+fecha+".pdf";
            String pathfolder = Environment.getExternalStorageDirectory().getPath() + "/Casapan";
            File folder = new File(pathfolder);
            folder.mkdirs();
            File file = new File(pathfolder+path);
            try {
                Drawable d = ContextCompat.getDrawable(context, R.drawable.logo_casapan);
                BitmapDrawable bitDw = ((BitmapDrawable) d);
                Bitmap bmp = bitDw.getBitmap();
                Font f = new Font(Font.FontFamily.HELVETICA, 17.0f, Font.BOLD, BaseColor.BLACK);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                Image image = Image.getInstance(stream.toByteArray());
                image.scaleToFit(100,50);
                image.setAlignment(Element.ALIGN_CENTER);
                PdfWriter.getInstance(document,new FileOutputStream(file));
                document.open();
                document.add(image);
                Paragraph titulo = new Paragraph("ARMÁ TU TORTA", f);
                titulo.setAlignment(Element.ALIGN_CENTER);
                titulo.setSpacingAfter(20);
                titulo.setSpacingBefore(20);
                document.add(titulo);
                Paragraph idpedido = new Paragraph("Nro. Pedido: "+id, f);
                document.add(idpedido);
                //------Local y fecha-----------//
                float[] columnEncabezado = new float[]{60f, 60f};
                PdfPTable localfecha= new PdfPTable(2);
                localfecha.setWidthPercentage(100);
                localfecha.setWidths(columnEncabezado);
                PdfPCell localentrega= new PdfPCell(new Phrase("Local de entrega: "+sucursal,f));
                localentrega.setBorder(Rectangle.NO_BORDER);
                localfecha.addCell(localentrega);
                PdfPCell fechapedido = new PdfPCell(new Phrase("Fecha pedido: "+ fecha,f));
                fechapedido.setBorder(Rectangle.NO_BORDER);
                localfecha.addCell(fechapedido);
                document.add(localfecha);
                //--------Cliente y telefono-----//
                PdfPTable clientetelefono= new PdfPTable(2);
                clientetelefono.setWidthPercentage(100);
                clientetelefono.setWidths(columnEncabezado);
                PdfPCell cliente= new PdfPCell(new Phrase("Cliente: "+params[0],f));
                cliente.setBorder(Rectangle.NO_BORDER);
                clientetelefono.addCell(cliente);
                PdfPCell telefono = new PdfPCell(new Phrase("Telefono: "+ params[1],f));
                telefono.setBorder(Rectangle.NO_BORDER);
                clientetelefono.addCell(telefono);
                document.add(clientetelefono);
                //--------fecha y hora--------//
                PdfPTable fechahora= new PdfPTable(2);
                fechahora.setWidthPercentage(100);
                fechahora.setWidths(columnEncabezado);
                PdfPCell fecha= new PdfPCell(new Phrase("Fecha de entrega: "+params[2],f));
                fecha.setBorder(Rectangle.NO_BORDER);
                fechahora.addCell(fecha);
                PdfPCell hora = new PdfPCell(new Phrase("Hora de entrega: "+ params[3],f));
                hora.setBorder(Rectangle.NO_BORDER);
                fechahora.addCell(hora);
                document.add(fechahora);
                Paragraph kilogramos = new Paragraph("Kilogramos: "+params[4],f);
                kilogramos.setSpacingBefore(20);
                document.add(kilogramos);
                Paragraph bizcoch = new Paragraph("Bizcochuelo: "+ params[5],f);
                document.add(bizcoch);
                float[] columnWidths = new float[]{ 61f, 61f};
                //---Encabezado--//
                PdfPTable rellenos = new PdfPTable(2);
                rellenos.setWidthPercentage(100);
                rellenos.setWidths(columnWidths);
                rellenos.setSpacingBefore(5);
                PdfPCell rellenoUno= new PdfPCell(new Phrase("Relleno 1: "+params[6],f));
                rellenoUno.setBorder(Rectangle.NO_BORDER);
                rellenos.addCell(rellenoUno);
                PdfPCell rellenoDos = new PdfPCell(new Phrase("Relleno 2: "+params[7],f));
                rellenoDos.setBorder(Rectangle.NO_BORDER);
                rellenos.addCell(rellenoDos);
                document.add(rellenos);
                Paragraph colores = new Paragraph("Colores: "+params[8]+params[9]+params[10]+params[11]+params[12]+params[13]+params[14],f);
                document.add(colores);
                Paragraph extras = new Paragraph("Extras: "+params[15]+params[16]+params[17]+params[18]+params[19],f);
                document.add(extras);
                Paragraph textoTorta = new Paragraph("Texto: "+params[20],f);
                document.add(textoTorta);
                Paragraph espacio_adorno = new Paragraph("Espacio para adorno: "+ params[21],f);
                document.add(espacio_adorno);
                Paragraph observacion = new Paragraph("Observaciones: "+ params[22],f);
                document.add(observacion);
                Paragraph tomoPedido = new Paragraph("Tomó pedido: "+params[23],f);
                document.add(tomoPedido);
                ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                if(bitmaptorta!=null){
                   bitmaptorta.compress(Bitmap.CompressFormat.PNG, 100, stream2);
                   Image image2 = Image.getInstance(stream2.toByteArray());
                   image2.scaleToFit(300,300);
                   image2.setAlignment(Element.ALIGN_CENTER);
                   document.add(image2);
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
