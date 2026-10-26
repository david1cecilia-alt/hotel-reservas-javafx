package com.reservas;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ReservasController {

    @FXML private Label lblHotel;
    @FXML private TableView<Habitacion> tablaHabitaciones;
    @FXML private TableView<Reserva> tablaReservas;
    @FXML private TextField txtNumero;
    @FXML private Label lblResultado;

    private FilteredList<Reserva> reservasFiltradas;

    @FXML
    public void initialize() throws Exception {
        // 1. Leer el XML desde src/main/resources
        Element hotel = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(getClass().getResourceAsStream("/reservas.xml"))
                .getDocumentElement();

        lblHotel.setText(texto(hotel, "nombre") + " (" + hotel.getAttribute("categoria") + ") - "
                + texto(hotel, "ciudad"));

        // 2. Cargar las habitaciones en la tabla
        NodeList habitaciones = hotel.getElementsByTagName("habitacion");
        for (int i = 0; i < habitaciones.getLength(); i++) {
            Element h = (Element) habitaciones.item(i);
            tablaHabitaciones.getItems().add(new Habitacion(
                    h.getAttribute("numero"),
                    texto(h, "tipo"),
                    texto(h, "capacidad"),
                    texto(h, "precioNoche", "precio_noche"),
                    h.getAttribute("estado"),
                    h.getAttribute("planta"),
                    texto(h, "vista"),
                    texto(h, "accesible")));
        }

        // 3. Cargar las reservas en la tabla
        NodeList reservas = hotel.getElementsByTagName("reserva");
        for (int i = 0; i < reservas.getLength(); i++) {
            Element r = (Element) reservas.item(i);
            tablaReservas.getItems().add(new Reserva(
                    r.getAttribute("codigo"),
                    texto(r, "nombre") + " " + texto(r, "apellidos"),
                    texto(r, "email"),
                    texto(r, "habitacionReservada", "habitacion_reservada"),
                    texto(r, "entrada"),
                    texto(r, "salida"),
                    texto(r, "personas"),
                    r.getAttribute("estado")));
        }

        reservasFiltradas = new FilteredList<>(tablaReservas.getItems());
        tablaReservas.setItems(reservasFiltradas);
    }

    /** Se ejecuta al pulsar «Buscar» o Enter en el campo de número. */
    @FXML
    private void buscar() {
        String numero = txtNumero.getText().trim();

        if (numero.isEmpty()) {
            mostrar("Introduce un número de habitación.");
            reservasFiltradas.setPredicate(r -> true);
            tablaHabitaciones.getSelectionModel().clearSelection();
            return;
        }
        if (!numero.matches("\\d+")) {
            mostrar("«" + numero + "» no es un número de habitación válido.");
            return;
        }

        Habitacion encontrada = null;
        for (Habitacion h : tablaHabitaciones.getItems()) {
            if (h.getNumero().equals(numero)) {
                encontrada = h;
                break;
            }
        }

        // Mostrar solo las reservas de esa habitación
        reservasFiltradas.setPredicate(r -> r.getHabitacion().equals(numero));

        if (encontrada == null) {
            tablaHabitaciones.getSelectionModel().clearSelection();
            mostrar("La habitación " + numero + " no existe.");
            return;
        }

        tablaHabitaciones.getSelectionModel().select(encontrada);
        tablaHabitaciones.scrollTo(encontrada);

        String datos = " (" + encontrada.getTipo() + ", " + encontrada.getCapacidad() + " pers., "
                + encontrada.getPrecio() + " €/noche)";
        switch (encontrada.getEstado().toLowerCase()) {
            case "disponible", "libre" ->
                    mostrar("La habitación " + numero + " está disponible" + datos);
            case "ocupada" ->
                    mostrar("La habitación " + numero + " está ocupada" + datos);
            case "mantenimiento" ->
                    mostrar("La habitación " + numero + " está en mantenimiento (no disponible)" + datos);
            default ->
                    mostrar("La habitación " + numero + " tiene un estado desconocido: " + encontrada.getEstado());
        }
    }

    private void mostrar(String mensaje) {
        lblResultado.setText(mensaje);
    }

    /** Devuelve el texto de la primera etiqueta que exista (admite nombres alternativos) o "" si no hay ninguna. */
    private String texto(Element padre, String... etiquetas) {
        for (String etiqueta : etiquetas) {
            NodeList nodos = padre.getElementsByTagName(etiqueta);
            if (nodos.getLength() > 0) {
                return nodos.item(0).getTextContent().trim();
            }
        }
        return "";
    }
}
