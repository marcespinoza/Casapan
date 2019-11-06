package com.casapan.pedidos.Modelo;

import com.casapan.pedidos.App.GlobalApplication;
import com.casapan.pedidos.Interface.ConfigInterface;
import com.casapan.pedidos.Util.Constants;

public class ConfigModelo implements ConfigInterface.Modelo {

    public ConfigInterface.Presentador ipresentador;

    public ConfigModelo(ConfigInterface.Presentador ipresentador) {
        this.ipresentador = ipresentador;
    }

    @Override
    public void enviarSucursal(String suc) {
        boolean b = Constants.getSPreferences(GlobalApplication.getAppContext()).setNombreSucursal(suc);
        ipresentador.sharedResponse(b);
    }
}
