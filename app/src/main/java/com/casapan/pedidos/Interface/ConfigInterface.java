package com.casapan.pedidos.Interface;

public interface ConfigInterface {

     interface Vista{
         void showToast(boolean b);
         void reiniciarApp();
         void mostrartoast();
    }

     interface Modelo{
        void enviarSucursal(String suc);
        void importarbd(String path);
        void exportarbd();
    }

    interface Presentador{
        void guardarSucursal(String suc);
        void sharedResponse(boolean b);
        void importarbd(String path);
        void exportarbd();
        void reiniciarApp();
        void mostrartoast();
    }
}
