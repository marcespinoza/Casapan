package com.casapan.pedidos.Vista;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.casapan.pedidos.R;
import com.casapan.pedidos.Util.Constants;
import com.casapan.pedidos.Util.ProgressDialog;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.Calendar;

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
    @BindView(R.id.kilogramo) RadioGroup kilogramo;
    @BindView(R.id.bizcochuelo) RadioGroup bizcochuelo_radio;
    @BindView(R.id.rellenouno) RadioGroup relleno_uno;
    @BindView(R.id.rellenodos)RadioGroup relleno_dos;
    @BindView(R.id.treskg) RadioButton treskg;
    @BindView(R.id.cuatrokg) RadioButton cuatrokg;
    @BindView(R.id.cincokg) RadioButton cincokg;
    @BindView(R.id.bchocolate) RadioButton bchocolate;
    @BindView(R.id.bvainilla) RadioButton bvainilla;
    //----Relleno1------//
    @BindView(R.id.ddeleche) RadioButton rdleche;
    @BindView(R.id.hojaldre) RadioButton rdlechehojaldre;
    @BindView(R.id.chocolate) RadioButton rdlechechocolate;
    @BindView(R.id.rocklet) RadioButton rdlecherocklet;
    @BindView(R.id.durazno) RadioButton rdlechedurazno;
    @BindView(R.id.chantilly) RadioButton cchantilly;
    @BindView(R.id.americana) RadioButton camericana;
    @BindView(R.id.moca) RadioButton cmoca;
    @BindView(R.id.bombon) RadioButton cbombon;
    @BindView(R.id.chantdurazno) RadioButton chantillydurazno;
    @BindView(R.id.chantillyanana) RadioButton chantillyanana;
    @BindView(R.id.mousefrutilla) RadioButton mousse;
    //----Relleno2------//
    @BindView(R.id.ddeleche2) RadioButton rdleche2;
    @BindView(R.id.hojaldre2) RadioButton rdlechehojaldre2;
    @BindView(R.id.chocolate2) RadioButton rdlechechocolate2;
    @BindView(R.id.rocklet2) RadioButton rdlecherocklet2;
    @BindView(R.id.durazno2) RadioButton rdlechedurazno2;
    @BindView(R.id.chantilly2) RadioButton cchantilly2;
    @BindView(R.id.americana2) RadioButton camericana2;
    @BindView(R.id.moca2) RadioButton cmoca2;
    @BindView(R.id.bombon2) RadioButton cbombon2;
    @BindView(R.id.chantdurazno2) RadioButton chantillydurazno2;
    @BindView(R.id.chantillyanana2) RadioButton chantillyanana2;
    @BindView(R.id.mousefrutilla2) RadioButton mousse2;
    //----Colores-----------//
    @BindView(R.id.blanco) RadioButton rblanco;
    @BindView(R.id.amarillo) RadioButton ramarillo;
    @BindView(R.id.rosado) RadioButton rrosado;
    @BindView(R.id.lila) RadioButton rlila;
    @BindView(R.id.verde) RadioButton rverde;
    @BindView(R.id.celeste) RadioButton rceleste;
    @BindView(R.id.anaranjado) RadioButton ranaranjado;
    //-----Extras-------//
    @BindView(R.id.cereza) RadioButton rcereza;
    @BindView(R.id.mani) RadioButton rmani;
    @BindView(R.id.chipchocolate) RadioButton rchipchocolate;
    @BindView(R.id.baniochocolate) RadioButton rbaniochocolate;
    //---Fin extras------//
    @BindView(R.id.textotorta) EditText textotorta;
    @BindView(R.id.adorno) RadioGroup adornoRadio;
    @BindView(R.id.adornosi) RadioButton adornosi;
    @BindView(R.id.adornono) RadioButton adornono;
    @BindView(R.id.tomopedido) EditText tomopedido;
    String  idpedido, sucursal, kg, bizcochuelo, relleno1, relleno2,  adorno, blanco, amarillo, rosado, lila, verde, celeste, anaranjado, cereza, mani, chipchocolate, baniochocolate;
    ProgressDialog generarPdf;
    ArrayList<String> ptorta;
    public OnAceptarBoton onAceptarBoton;

    public interface OnAceptarBoton{
        void enviarpath(String idpedido, String[] params);
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
               enviarPedido();
            }
        });
        cancelar = rootView.findViewById(R.id.cancelartorta);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        initChecked();
        if(getArguments()!=null){
           ptorta = getArguments().getStringArrayList("pedidotorta");
           cargarPedido(ptorta);
        }
        return rootView;
    }

    public void initChecked(){
        kilogramo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                switch (selectedId){
                    case R.id.treskg:{ kg = "3";break;}
                    case R.id.cuatrokg:{kg = "4";break;}
                    case R.id.cincokg:{kg = "5";break;}
                }
            }
        });
        bizcochuelo_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                switch (selectedId){
                    case R.id.bchocolate:{ bizcochuelo = "chocolate";break;}
                    case R.id.bvainilla:{bizcochuelo = "vainilla";break;}
                }
            }
        });
        relleno_uno.setOnCheckedChangeListener((radioGroup, i) -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            switch (selectedId){
                case R.id.ddeleche:{ relleno1 = "Dulce de leche";break;}
                case R.id.hojaldre:{relleno1 = "D. leche con hojaldre";break;}
                case R.id.chocolate:{relleno1 = "D. leche con chocolate";break;}
                case R.id.rocklet:{relleno1 = "D. leche con rocklet";break;}
                case R.id.durazno:{relleno1 = "D. leche con durazno";break;}
                case R.id.chantilly:{relleno1 = "Crema chantilly";break;}
                case R.id.americana:{relleno1 = "Crema americana";break;}
                case R.id.moca:{relleno1 = "Crema moca";break;}
                case R.id.bombon:{relleno1 = "Crema bombón";}
                case R.id.chantdurazno:{relleno1 = "Chantilly con durazno";break;}
                case R.id.chantillyanana:{relleno1 = "Chantilly con ananá";break;}
                case R.id.mousefrutilla:{relleno1 = "Mousse de frutilla";break;}
                default: relleno1 = "Sin relleno";break;
            }
        });
        relleno_dos.setOnCheckedChangeListener((radioGroup, i) -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            switch (selectedId){
                case R.id.ddeleche2:{ relleno2 = "Dulce de leche";break;}
                case R.id.hojaldre2:{relleno2 = "D. leche con hojaldre";break;}
                case R.id.chocolate2:{relleno2 = "D. leche con chocolate";break;}
                case R.id.rocklet2:{relleno2 = "D. leche con rocklet";break;}
                case R.id.durazno2:{relleno2 = "D. leche con durazno";break;}
                case R.id.chantilly2:{relleno2 = "Crema chantilly";break;}
                case R.id.americana2:{relleno2 = "Crema americana";break;}
                case R.id.moca2:{relleno2 = "Crema moca";break;}
                case R.id.bombon2:{relleno2 = "Crema bombón";break;}
                case R.id.chantdurazno2:{relleno2 = "Chantilly con durazno";break;}
                case R.id.chantillyanana2:{relleno2 = "Chantilly con ananá";break;}
                case R.id.mousefrutilla2:{relleno2 = "Mousse de frutilla";break;}
                default: relleno2 = "Sin relleno";break;
            }
        });
        adornoRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                switch (selectedId){
                    case R.id.adornono:{ adorno = "No"; break;}
                    case R.id.adornosi:{adorno = "Si"; break;}
                    default: adorno = "No"; break;
                }
            }
        });
    }

    public void cargarPedido(ArrayList<String> ptorta){
        idpedido = ptorta.get(0);
        cliente.setText(ptorta.get(1));
        telefono.setText(ptorta.get(2));
        fechatexto.setText(ptorta.get(3));
        horatexto.setText(ptorta.get(4));
        String kg = ptorta.get(5);
        switch (kg){
           case "3": treskg.setChecked(true); break;
           case "4": cuatrokg.setChecked(true); break;
           case "5": cincokg.setChecked(true); break;
        }
        String bz = ptorta.get(6);
        switch (bz){
            case "chocolate": bchocolate.setChecked(true); break;
            case "vainilla": bvainilla.setChecked(true); break;
        }
        String rellenoUno = ptorta.get(7);
        switch (rellenoUno){
            case "Dulce de leche": rdleche.setChecked(true); break;
            case "D. leche con hojaldre": rdlechehojaldre.setChecked(true);break;
            case "D. leche con chocolate": rdlechechocolate.setChecked(true);break;
            case "D. leche con rocklet": rdlecherocklet.setChecked(true);break;
            case "D. leche con durazno": rdlechedurazno.setChecked(true);break;
            case "Crema chantilly": cchantilly.setChecked(true);break;
            case "Crema americana": camericana.setChecked(true);break;
            case "Crema moca": cmoca.setChecked(true);break;
            case "Crema bombon": cbombon.setChecked(true);break;
            case "Chantilly con durazno": chantillydurazno.setChecked(true);break;
            case "Chantilly con ananá": chantillyanana.setChecked(true);break;
            case "Mousse de frutilla": mousse.setChecked(true);break;
        }
        String rellenoDos = ptorta.get(8);
        switch (rellenoDos){
            case "Dulce de leche": rdleche2.setChecked(true);break;
            case "D. leche con hojaldre": rdlechehojaldre2.setChecked(true);break;
            case "D. leche con chocolate": rdlechechocolate2.setChecked(true);break;
            case "D. leche con rocklet": rdlecherocklet2.setChecked(true);break;
            case "D. leche con durazno": rdlechedurazno2.setChecked(true);break;
            case "Crema chantilly": cchantilly2.setChecked(true);break;
            case "Crema americana": camericana2.setChecked(true);break;
            case "Crema moca": cmoca2.setChecked(true);break;
            case "Crema bombon": cbombon2.setChecked(true);break;
            case "Chantilly con durazno": chantillydurazno2.setChecked(true);break;
            case "Chantilly con anana": chantillyanana2.setChecked(true);break;
            case "Mousse de frutilla": mousse2.setChecked(true);break;
        }
        //----Colores----//
        rblanco.setChecked(ptorta.get(9).equals("")?false:true);
        ramarillo.setChecked(ptorta.get(10).equals("")?false:true);
        rrosado.setChecked(ptorta.get(11).equals("")?false:true);
        rlila.setChecked(ptorta.get(12).equals("")?false:true);
        rverde.setChecked(ptorta.get(13).equals("")?false:true);
        rceleste.setChecked(ptorta.get(14).equals("")?false:true);
        ranaranjado.setChecked(ptorta.get(15).equals("")?false:true);
        //-----Extras------//
        rcereza.setChecked(ptorta.get(16).equals("")?false:true);
        rmani.setChecked(ptorta.get(17).equals("")?false:true);
        rchipchocolate.setChecked(ptorta.get(18).equals("")?false:true);
        rbaniochocolate.setChecked(ptorta.get(19).equals("")?false:true);
        textotorta.setText(ptorta.get(20));
        String adorno = ptorta.get(21);
        switch (adorno){
            case "Si": adornosi.setChecked(true); break;
            case "No": adornono.setChecked(true); break;
         }
         tomopedido.setText(ptorta.get(22));
    }

    public void enviarPedido(){
        String client = cliente.getText().toString();
        String tel = telefono.getText().toString();
        String fch =  fechatexto.getText().toString();
        String hora = horatexto.getText().toString();
        String texto_torta = textotorta.getText().toString();
        String tomo_pedido = tomopedido.getText().toString();
        blanco = rblanco.isChecked()? "- Blanco -": "";
        amarillo = ramarillo.isChecked()? "- Amarillo -": "";
        rosado = rrosado.isChecked()? "- Rosado -": "";
        lila = rlila.isChecked()? "- Lila -": "";
        verde = rverde.isChecked()? "- Verde -": "";
        celeste = rceleste.isChecked()? "- Celeste -": "";
        anaranjado = ranaranjado.isChecked()? "- Anaranjado -": "";
        cereza = rcereza.isChecked()? "- Cereza -": "";
        mani = rmani.isChecked()? "- Mani -": "";
        chipchocolate = rchipchocolate.isChecked()? "- Chip de chocolate -": "";
        baniochocolate = rbaniochocolate.isChecked()? "- Baño chocolate cara superior -": "";
        String [] params = {client, tel, fch, hora, kg, bizcochuelo, relleno1, relleno2, blanco, amarillo, rosado, lila, verde, celeste, anaranjado, cereza, mani, chipchocolate, baniochocolate,texto_torta, adorno, tomo_pedido};
        onAceptarBoton.enviarpath(idpedido, params);
        dismiss();
    }

    public static TortaDialog newInstance() {
        TortaDialog frag = new TortaDialog();
        return frag;
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
