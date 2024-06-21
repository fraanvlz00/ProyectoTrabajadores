package Ventanas;

import Dominio.TrabajadorManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class PrincipalFrame extends JFrame {
    private TrabajadorManager trabajadorManager;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private BuscarTrabajadorPanel buscarTrabajadorPanel;
    private AsignarIsapreAfpPanel asignarIsapreAfpPanel;

    public PrincipalFrame() throws IOException {
        trabajadorManager = new TrabajadorManager("src/main/java/Datos/trabajador.json", "src/main/java/Datos/isapre.json", "src/main/java/Datos/afp.json");
        initUI();
    }

    private void initUI() {
        setTitle("Trabajador Manager");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel menuPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton mostrarDatosButton = new JButton("Mostrar Datos del Trabajador");
        mostrarDatosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "buscarTrabajadorPanel");
            }
        });
        buttonPanel.add(mostrarDatosButton);

        JButton asignarIsapreAfpButton = new JButton("Asignar Isapre y AFP");
        asignarIsapreAfpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "asignarIsapreAfpPanel");
            }
        });
        buttonPanel.add(asignarIsapreAfpButton);

        menuPanel.add(buttonPanel, BorderLayout.NORTH);

        buscarTrabajadorPanel = new BuscarTrabajadorPanel(trabajadorManager, this);
        asignarIsapreAfpPanel = new AsignarIsapreAfpPanel(trabajadorManager, this);

        mainPanel.add(menuPanel, "menuPanel");
        mainPanel.add(buscarTrabajadorPanel, "buscarTrabajadorPanel");
        mainPanel.add(asignarIsapreAfpPanel, "asignarIsapreAfpPanel");

        add(mainPanel);
    }

    public void showMenu() {
        buscarTrabajadorPanel.clear();
        asignarIsapreAfpPanel.clear();
        cardLayout.show(mainPanel, "menuPanel");
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                PrincipalFrame frame = new PrincipalFrame();
                frame.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
