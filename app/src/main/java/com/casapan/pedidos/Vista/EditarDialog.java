package com.casapan.pedidos.Vista;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.casapan.pedidos.R;
import com.google.android.material.button.MaterialButton;

public class EditarDialog extends DialogFragment {

    EditText nombre;
    MaterialButton guardar;
    String descripcion;
    int type;

    public interface Editar{
        public void guardarEdicion(String nombre);
    }

    private Editar editar;

    public void editar(Editar editar){
        this.editar = editar;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.editar_dialog, container, false);
        nombre = v.findViewById(R.id.descripcion);
        nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s.toString().trim().length()==0){
                    guardar.setEnabled(false);
                } else {
                    guardar.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        guardar = v.findViewById(R.id.guardaredicion);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editar.guardarEdicion(nombre.getText().toString());
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        nombre.setText(descripcion);
    }

    public void setType(int type){
        this.type = type;
    }

    public void setDescripcion(String descripcion){
        this.descripcion=descripcion;
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        Window window = getDialog().getWindow();
        window.setLayout(width, height);
        window.setGravity(Gravity.CENTER);
    }

}
