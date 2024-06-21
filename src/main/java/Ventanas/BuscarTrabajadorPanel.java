package Ventanas;

import Dominio.Trabajador;
import Dominio.TrabajadorManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BuscarTrabajadorPanel extends JPanel {
    private TrabajadorManager trabajadorManager;
    private JTextArea textArea;
    private JTextField nombreField;
    private PrincipalFrame parentFrame;

    public BuscarTrabajadorPanel(TrabajadorManager trabajadorManager, PrincipalFrame parentFrame) {
        this.trabajadorManager = trabajadorManager;
        this.parentFrame = parentFrame;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout());

        JLabel nombreLabel = new JLabel("Ingrese el nombre del trabajador:");
        nombreField = new JTextField(20);
        JButton buscarButton = new JButton("Buscar");

        inputPanel.add(nombreLabel);
        inputPanel.add(nombreField);
        inputPanel.add(buscarButton);

        add(inputPanel, BorderLayout.NORTH);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.showMenu();
            }
        });
        buttonPanel.add(volverButton);

        add(buttonPanel, BorderLayout.SOUTH);

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreField.getText();
                mostrarDatosTrabajador(nombre);
            }
        });
    }

    private void mostrarDatosTrabajador(String nombre) {
        if (nombre != null && !nombre.isEmpty()) {
            Trabajador trabajador = trabajadorManager.buscarTrabajadorPorNombre(nombre);
            if (trabajador != null) {
                StringBuilder datosTrabajador = new StringBuilder();
                datosTrabajador.append("Datos del Trabajador:\n");
                datosTrabajador.append("Nombre: ").append(trabajador.getNombre()).append("\n");
                datosTrabajador.append("Apellido: ").append(trabajador.getApellido()).append("\n");
                datosTrabajador.append("RUT: ").append(trabajador.getRut()).append("\n");
                datosTrabajador.append("Isapre: ").append(trabajador.getIsapre() != null ? trabajador.getIsapre().getNombre() : "No asignado").append("\n");
                datosTrabajador.append("AFP: ").append(trabajador.getAfp() != null ? trabajador.getAfp().getNombre() : "No asignado").append("\n");
                textArea.setText(datosTrabajador.toString());
            } else {
                textArea.setText("Trabajador no encontrado.");
            }
        } else {
            textArea.setText("El campo de nombre no puede estar vacío.");
        }
    }

    public void clear() {
        nombreField.setText("");
        textArea.setText("");
    }
}
