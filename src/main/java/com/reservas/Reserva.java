package com.reservas;

public class Reserva {

    private final String codigo, cliente, email, habitacion, entrada, salida, personas, estado;

    public Reserva(String codigo, String cliente, String email, String habitacion,
                   String entrada, String salida, String personas, String estado) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.email = email;
        this.habitacion = habitacion;
        this.entrada = entrada;
        this.salida = salida;
        this.personas = personas;
        this.estado = estado;
    }

    public String getCodigo() { return codigo; }
    public String getCliente() { return cliente; }
    public String getEmail() { return email; }
    public String getHabitacion() { return habitacion; }
    public String getEntrada() { return entrada; }
    public String getSalida() { return salida; }
    public String getPersonas() { return personas; }
    public String getEstado() { return estado; }
}
