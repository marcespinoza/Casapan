package com.casapan.pedidos.Vista;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.casapan.pedidos.Adapter.PedidoAdapter;
import com.casapan.pedidos.Adapter.Utils.ListItem;
import com.casapan.pedidos.Database.DatabaseHelper;
import com.casapan.pedidos.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentPedidos extends Fragment {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    private Boolean isFabOpen = true;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    PedidoDialog aDialog;
    DatabaseHelper db;
    @BindView(R.id.fab2)
    LinearLayout fab2;
    private boolean fabExpanded = false;
    String[] permissions= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final int MULTIPLE_PERMISSIONS = 10;
    PedidoAdapter pAdapter;
    RecyclerView recyclerView;
    ArrayList<ListItem> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedido, container, false);
        ButterKnife.bind(this, view);
        db = new DatabaseHelper(getActivity());
        fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_backward);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFAB();
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                aDialog = PedidoDialog.newInstance("");
                aDialog.show(fm, "Fragment articulo");
            }
        });
        animateFAB();
        db = new DatabaseHelper(getActivity());
        int currentApiVersion = Build.VERSION.SDK_INT;
        if(currentApiVersion >=  Build.VERSION_CODES.M)        {
                checkPermission();
        }
        return view;
    }


    public void animateFAB(){
        if(isFabOpen){
            fab.startAnimation(rotate_backward);
            fab2.startAnimation(fab_close);
            fab2.setClickable(false);
            isFabOpen = false;
        } else {
            fab.startAnimation(rotate_forward);
            fab2.startAnimation(fab_open);
            fab2.setClickable(true);
            isFabOpen = true;

        }
    }

    private boolean checkPermission() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(getActivity(),p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
            switch (requestCode) {
                case MULTIPLE_PERMISSIONS:{
                    if (grantResults.length > 0) {
                        String permissionsDenied = "";
                        for (String per : permissions) {
                            if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                                permissionsDenied += "\n" + per;
                                showDialogNotCancelable("Permissions mandatory",
                                        "All the permissions are required for this app",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                checkPermission();
                                            }
                                        });
                            }

                        }

                    }
                    return;
                }

        }
    }
    private void showDialogNotCancelable(String title, String message,
                                         DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setCancelable(false)
                .create()
                .show();
    }


}
