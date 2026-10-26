package com.reservas;

public class Habitacion {

    private final String numero, tipo, capacidad, precio, estado, planta, vista, accesible;

    public Habitacion(String numero, String tipo, String capacidad, String precio, String estado,
                      String planta, String vista, String accesible) {
        this.numero = numero;
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.precio = precio;
        this.estado = estado;
        this.planta = planta;
        this.vista = vista;
        this.accesible = accesible;
    }

    public String getNumero() { return numero; }
    public String getTipo() { return tipo; }
    public String getCapacidad() { return capacidad; }
    public String getPrecio() { return precio; }
    public String getEstado() { return estado; }
    public String getPlanta() { return planta; }
    public String getVista() { return vista; }
    public String getAccesible() { return accesible; }
}
