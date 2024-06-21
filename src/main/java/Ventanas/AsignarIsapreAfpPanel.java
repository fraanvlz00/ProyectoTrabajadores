package Ventanas;

import Dominio.Trabajador;
import Dominio.TrabajadorManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AsignarIsapreAfpPanel extends JPanel {
    private TrabajadorManager trabajadorManager;
    private PrincipalFrame parentFrame;
    private JComboBox<String> isapreComboBox;
    private JComboBox<String> afpComboBox;
    private JTextField nombreField;

    public AsignarIsapreAfpPanel(TrabajadorManager trabajadorManager, PrincipalFrame parentFrame) {
        this.trabajadorManager = trabajadorManager;
        this.parentFrame = parentFrame;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2));

        JLabel nombreLabel = new JLabel("Nombre del Trabajador:");
        nombreField = new JTextField();
        formPanel.add(nombreLabel);
        formPanel.add(nombreField);

        JLabel isapreLabel = new JLabel("Seleccionar Isapre:");
        isapreComboBox = new JComboBox<>();
        List<String> isapres = trabajadorManager.getIsapreNames();
        for (String isapre : isapres) {
            isapreComboBox.addItem(isapre);
        }
        formPanel.add(isapreLabel);
        formPanel.add(isapreComboBox);

        JLabel afpLabel = new JLabel("Seleccionar AFP:");
        afpComboBox = new JComboBox<>();
        List<String> afps = trabajadorManager.getAfpNames();
        for (String afp : afps) {
            afpComboBox.addItem(afp);
        }
        formPanel.add(afpLabel);
        formPanel.add(afpComboBox);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton asignarButton = new JButton("Asignar");
        asignarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreField.getText();
                if (!nombre.isEmpty()) {
                    asignarIsapreYAfp(nombre);
                } else {
                    JOptionPane.showMessageDialog(AsignarIsapreAfpPanel.this, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(asignarButton);

        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.showMenu();
            }
        });
        buttonPanel.add(volverButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void asignarIsapreYAfp(String nombre) {
        Trabajador trabajador = trabajadorManager.buscarTrabajadorPorNombre(nombre);
        if (trabajador != null) {
            if (trabajador.getIsapre() != null || trabajador.getAfp() != null) {
                JOptionPane.showMessageDialog(this, "El trabajador ya tiene una Isapre o AFP asignada y no puede cambiarse.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String isapreNombre = (String) isapreComboBox.getSelectedItem();
            String afpNombre = (String) afpComboBox.getSelectedItem();

            trabajadorManager.asignarIsapreYAFP(trabajador, isapreNombre, afpNombre);
            JOptionPane.showMessageDialog(this, "Isapre y AFP asignadas correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "Trabajador no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void clear() {
        nombreField.setText("");
        isapreComboBox.setSelectedIndex(-1);
        afpComboBox.setSelectedIndex(-1);
    }
}
