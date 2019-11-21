package com.casapan.pedidos.Vista;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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

import com.casapan.pedidos.Database.DatabaseHelper;
import com.casapan.pedidos.R;
import com.google.android.material.button.MaterialButton;

public class CategoriaDialog extends DialogFragment {

    EditText nombre;
    MaterialButton aceptar;
    DatabaseHelper db;
    private DissmissListener dissmissListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.categoria_dialog,container);
        db = new DatabaseHelper(getContext());
        aceptar = rootView.findViewById(R.id.agregarcategoria);
        aceptar.setEnabled(false);
        nombre = rootView.findViewById(R.id.nombrecategoria);
        nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                aceptar.setEnabled(!TextUtils.isEmpty(charSequence.toString().trim()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarArticulo(nombre.getText().toString());
            }
        });
        return rootView;
    }

    public interface DissmissListener {
        void onDismiss(CategoriaDialog categoriaDialog);
    }

    public void setDissmissListener(DissmissListener dissmissListener) {
        this.dissmissListener = dissmissListener;
    }

    public static CategoriaDialog newInstance(String Pedido) {
        CategoriaDialog categoriaDialog = new CategoriaDialog();
        return categoriaDialog;
    }

    public void agregarArticulo(String nombre){
        db.insertarCategoria(nombre);
        dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        dissmissListener.onDismiss(this);
    }
}
