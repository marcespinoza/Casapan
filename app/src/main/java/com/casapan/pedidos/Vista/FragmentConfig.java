package com.casapan.pedidos.Vista;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.casapan.pedidos.BuildConfig;
import com.casapan.pedidos.Interface.ConfigInterface;
import com.casapan.pedidos.Presentador.ConfigPresentador;
import com.casapan.pedidos.R;
import com.casapan.pedidos.Util.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class FragmentConfig extends Fragment implements ConfigInterface.Vista {

    private ConfigInterface.Presentador ipresentador;

    @BindView(R.id.guardar) MaterialButton guardar;
    @BindView(R.id.sucursal) EditText sucursal;
    @BindView(R.id.exportar) MaterialButton exportar;
    @BindView(R.id.importar) MaterialButton importar;
    @BindView(R.id.configcoordinator) CoordinatorLayout cl;
    Snackbar mySnackbar;
    String[] permissions= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public static final int MULTIPLE_PERMISSIONS = 10;

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
                hideKeyboard();
            }
        });
        exportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ipresentador.exportarbd();
            }
        });
        importar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilePicker();
            }
        });
        initUi();
        int currentApiVersion = Build.VERSION.SDK_INT;
        if(currentApiVersion >=  Build.VERSION_CODES.M)        {
            checkPermission();
        }
        return view;
    }

    public void showFilePicker(){
        new ChooserDialog(getActivity())
                .withChosenListener(new ChooserDialog.Result() {
                    @Override
                    public void onChoosePath(String path, File pathFile) {
                        ipresentador.importarbd(path);
                    }
                })
                // to handle the back key pressed or clicked outside the dialog:
                .withOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        dialog.cancel(); // MUST have
                    }
                })
                .build()
                .show();
    }

    private void initUi() {
        String suc = Constants.getSPreferences(getContext()).getNombreSucursal();
        sucursal.setText(suc);
        getActivity().getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void guardarSucursal() {
        String suc = sucursal.getText().toString();
        ipresentador.guardarSucursal(suc);
    }

    public void restartApp(){
        Intent mStartActivity = new Intent(getActivity(), MainActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(getActivity(), mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 20, mPendingIntent);
        System.exit(0);
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

    @Override
    public void reiniciarApp() {
        restartApp();
    }

    @Override
    public void mostrartoast() {
        mySnackbar = Snackbar.make(cl, "Guardado en: "+Environment.getExternalStorageDirectory().getPath() + "/Casapan", Snackbar.LENGTH_LONG);
        mySnackbar.setAction("Compartir", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareIntent();
            }
        }).show();
    }

    public void shareIntent(){
        Uri pdfUri;
        File pdfFile = new File(Environment.getExternalStorageDirectory().getPath() + "/Casapan/casapandb.xls");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pdfUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", pdfFile);
        } else {
            pdfUri = Uri.fromFile(pdfFile);
        }
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, pdfUri);
        startActivity(Intent.createChooser(share, "Compartir"));
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

    private void showDialogNotCancelable(String title, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setCancelable(false)
                .create()
                .show();
    }

}
