package com.casapan.pedidos.Vista;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import com.casapan.pedidos.Interface.ListItem;
import com.casapan.pedidos.R;
import com.casapan.pedidos.Util.Constants;
import com.casapan.pedidos.Util.ProgressDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
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
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TortaDialog extends DialogFragment {

    public final Calendar c = Calendar.getInstance();
    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);
    private static final String BARRA = "/";
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    @BindView(R.id.aceptartorta) MaterialButton aceptar;
    @BindView(R.id.cancelartorta) MaterialButton cancelar;
    @BindView(R.id.cliente) EditText cliente;
    @BindView(R.id.telefono) EditText telefono;
    @BindView(R.id.fechaentrega) EditText fechatexto;
    @BindView(R.id.horaentrega) EditText horatexto;
    @BindView(R.id.fechapicker) ImageButton fechapick;
    @BindView(R.id.horapicker) ImageButton horapick;
    @BindView(R.id.treskg) AppCompatCheckBox treskg;
    @BindView(R.id.cuatrokg) AppCompatCheckBox cuatrokg;
    @BindView(R.id.cincokg) AppCompatCheckBox cincokg;
    @BindView(R.id.bchocolate) AppCompatCheckBox bchocolate;
    @BindView(R.id.bvainilla) AppCompatCheckBox bvainilla;
    @BindView(R.id.ddeleche)AppCompatCheckBox ddeleche;
    @BindView(R.id.hojaldre) AppCompatCheckBox hojaldre;
    @BindView(R.id.chocolate) AppCompatCheckBox chocolate;
    @BindView(R.id.rocklet) AppCompatCheckBox rocklet;
    @BindView(R.id.durazno)AppCompatCheckBox durazno;
    @BindView(R.id.chantilly) AppCompatCheckBox chantilly;
    @BindView(R.id.americana) AppCompatCheckBox ameriacana;
    @BindView(R.id.moca) AppCompatCheckBox moca;
    @BindView(R.id.bombon)AppCompatCheckBox bombon;
    @BindView(R.id.chantdurazno) AppCompatCheckBox chantydurazno;
    @BindView(R.id.chantillyanana) AppCompatCheckBox chantyanana;
    @BindView(R.id.mousefrutilla) AppCompatCheckBox mousefrutilla;
    @BindView(R.id.blanco) AppCompatCheckBox blanco;
    @BindView(R.id.amarillo) AppCompatCheckBox amarillo;
    @BindView(R.id.rosado) AppCompatCheckBox rosado;
    @BindView(R.id.lila) AppCompatCheckBox lila;
    @BindView(R.id.verde) AppCompatCheckBox verde;
    @BindView(R.id.celeste) AppCompatCheckBox celeste;
    @BindView(R.id.anaranjado) AppCompatCheckBox anaranjado;
    @BindView(R.id.cereza) AppCompatCheckBox cereza;
    @BindView(R.id.mani) AppCompatCheckBox mani;
    @BindView(R.id.chocolaterallado) AppCompatCheckBox chocolaterallado;
    @BindView(R.id.baniochocolate) AppCompatCheckBox baniochocolate;
    @BindView(R.id.textotorta) EditText textotorta;
    @BindView(R.id.adornosi) AppCompatCheckBox adornosi;
    @BindView(R.id.adornono) AppCompatCheckBox adornono;
    String sucursal = "";
    ProgressDialog generarPdf;
    public OnAceptarBoton onAceptarBoton;

    public interface OnAceptarBoton{
        void enviarpath(String usuario);
    }

    public void OnAceptarButton(OnAceptarBoton onAceptarBoton){
        this.onAceptarBoton = onAceptarBoton;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.torta_dialog,container);
        ButterKnife.bind(this, rootView);
        sucursal = Constants.getSPreferences(getContext()).getNombreSucursal();
        generarPdf = new ProgressDialog(getContext());
        fechapick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        horapick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerHora();
            }
        });
        aceptar = rootView.findViewById(R.id.aceptartorta);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String [] params = {cliente.getText().toString(), telefono.getText().toString(), fechatexto.getText().toString(), horatexto.getText().toString()};
                new GeneraPedidoTorta().execute(params);
            }
        });
        cancelar = rootView.findViewById(R.id.cancelartorta);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return rootView;
    }

    public static TortaDialog newInstance(String Pedido) {
        TortaDialog frag = new TortaDialog();
        return frag;
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
                    PdfPCell localentrega= new PdfPCell(new Phrase("LOCAL DE ENTREGA"));
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
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                generarPdf.showProgressDialog("Generando PDF");
            }

            @Override
            protected void onPostExecute(String result) {
                generarPdf.finishDialog();
                dismiss();
                onAceptarBoton.enviarpath(result);
            }
        }


    private void showDatePickerDialog() {
        DatePickerDialog recogerFecha = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                fechatexto.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
            }
        },anio, mes, dia);
        recogerFecha.show();
    }

    private void obtenerHora(){
        TimePickerDialog recogerHora = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                //Muestro la hora con el formato deseado
                horatexto.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, hora, minuto, false);

        recogerHora.show();
    }


    @Override
    public void onResume() {
        super.onResume();
        int width = getActivity().getResources().getDisplayMetrics().widthPixels;
        int height = getActivity().getResources().getDisplayMetrics().heightPixels;
        Window window = getDialog().getWindow();
        window.setLayout(width-70, TableRow.LayoutParams.WRAP_CONTENT  );
        window.setGravity(Gravity.CENTER);
    }

}
