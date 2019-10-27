package com.casapan.pedidos.Vista;

import android.os.Bundle;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.editar_dialog, container, false);
        nombre = v.findViewById(R.id.descripcion);
        guardar = v.findViewById(R.id.guardaredicion);
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
