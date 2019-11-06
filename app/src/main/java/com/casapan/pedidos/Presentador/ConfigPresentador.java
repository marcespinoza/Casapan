package com.casapan.pedidos.Presentador;

import com.casapan.pedidos.Interface.ConfigInterface;
import com.casapan.pedidos.Modelo.ConfigModelo;

public class ConfigPresentador implements ConfigInterface.Presentador {

    private ConfigInterface.Vista ivista;
    private ConfigInterface.Modelo imodelo;

    public ConfigPresentador(ConfigInterface.Vista ivista) {
        this.ivista = ivista;
        imodelo = new ConfigModelo(this);
    }

    @Override
    public void guardarSucursal(String suc) {
        imodelo.enviarSucursal(suc);
    }

    @Override
    public void sharedResponse(boolean b) {
        ivista.showToast(b);
    }
}
