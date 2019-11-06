package com.casapan.pedidos.Vista;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.casapan.pedidos.Interface.ConfigInterface;
import com.casapan.pedidos.Presentador.ConfigPresentador;
import com.casapan.pedidos.R;
import com.casapan.pedidos.Util.Constants;
import com.google.android.material.button.MaterialButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentConfig extends Fragment implements ConfigInterface.Vista {

    private ConfigInterface.Presentador ipresentador;

    @BindView(R.id.guardar) MaterialButton guardar;
    @BindView(R.id.sucursal) EditText sucursal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_config, container, false);
        ButterKnife.bind(this, view);
        view.findViewById(R.id.guardar);
        ipresentador = new ConfigPresentador(this);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarSucursal();
            }
        });
        initUi();
        return view;
    }

    private void initUi() {
        String suc = Constants.getSPreferences(getContext()).getNombreSucursal();
        sucursal.setText(suc);
        getActivity().getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void guardarSucursal() {
        String suc = sucursal.getText().toString();
        ipresentador.guardarSucursal(suc);
    }

    @Override
    public void showToast(boolean b) {
        String mensaje = "";
        if(b){
            mensaje = "Se ha guardado la sucursal";
        }else{
            mensaje = "Error al guardar";
        }
        Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
    }
}
