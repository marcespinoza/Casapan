package com.casapan.pedidos.Interface;

public interface ConfigInterface {

     interface Vista{
         void showToast(boolean b);
    }

    public interface Modelo{
        void enviarSucursal(String suc);
    }

    public interface Presentador{
        void guardarSucursal(String suc);
        void sharedResponse(boolean b);
    }
}
