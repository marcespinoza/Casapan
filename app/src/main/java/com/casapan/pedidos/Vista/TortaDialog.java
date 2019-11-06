package com.casapan.pedidos.Vista;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.casapan.pedidos.R;

public class TortaDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.torta_dialog,container);
        return rootView;
    }

    public static TortaDialog newInstance(String Pedido) {
        TortaDialog frag = new TortaDialog();
        return frag;
    }

}
