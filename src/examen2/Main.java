package examen2;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class Main {
    private PSNUsers usuarios;
    private JFrame ventana;
    private JTextField campoUsuario, campoJuego, campoTrofeo;
    private JTextArea campoDescripcion;
    private JComboBox<Trophy> comboTrofeo;
    private JTextArea informacionUsuario;

    public Main() throws IOException {
        usuarios = new PSNUsers();
        ventana = new JFrame("PLAY STATION NETWORK");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(900, 700); 
        ventana.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));
        ventana.add(panel);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(15, 15, 15, 15); 
        c.fill = GridBagConstraints.HORIZONTAL;

        Font fuenteLabel = new Font("Arial", Font.BOLD, 18);
        Font fuenteBoton = new Font("Arial", Font.PLAIN, 18); 
        Color colorBoton = Color.BLACK;

        JLabel etiquetaUsuario = new JLabel("Usuario:");
        etiquetaUsuario.setFont(fuenteLabel);
        c.gridx = 0;
        c.gridy = 0;
        panel.add(etiquetaUsuario, c);

        campoUsuario = new JTextField(20);
        campoUsuario.setFont(fuenteBoton);
        c.gridx = 1;
        c.gridy = 0;
        panel.add(campoUsuario, c);

        JLabel etiquetaJuego = new JLabel("Juego:");
        etiquetaJuego.setFont(fuenteLabel);
        c.gridx = 0;
        c.gridy = 1;
        panel.add(etiquetaJuego, c);

        campoJuego = new JTextField(20);
        campoJuego.setFont(fuenteBoton);
        c.gridx = 1;
        c.gridy = 1;
        panel.add(campoJuego, c);

        JLabel etiquetaTrofeo = new JLabel("Nombre del Trofeo:");
        etiquetaTrofeo.setFont(fuenteLabel);
        c.gridx = 0;
        c.gridy = 2;
        panel.add(etiquetaTrofeo, c);

        campoTrofeo = new JTextField(20);
        campoTrofeo.setFont(fuenteBoton);
        c.gridx = 1;
        c.gridy = 2;
        panel.add(campoTrofeo, c);

        JLabel etiquetaTipoTrofeo = new JLabel("Tipo de Trofeo:");
        etiquetaTipoTrofeo.setFont(fuenteLabel);
        c.gridx = 0;
        c.gridy = 3;
        panel.add(etiquetaTipoTrofeo, c);

        comboTrofeo = new JComboBox<>(Trophy.values());
        comboTrofeo.setFont(fuenteBoton);
        c.gridx = 1;
        c.gridy = 3;
        panel.add(comboTrofeo, c);

        JLabel etiquetaDescripcion = new JLabel("Descripcion:");
        etiquetaDescripcion.setFont(fuenteLabel);
        c.gridx = 0;
        c.gridy = 4;
        panel.add(etiquetaDescripcion, c);

        campoDescripcion = new JTextArea(5, 20);
        campoDescripcion.setFont(fuenteBoton);
        JScrollPane scrollDescripcion = new JScrollPane(campoDescripcion);
        c.gridx = 1;
        c.gridy = 4;
        panel.add(scrollDescripcion, c);

        c.gridx = 2;
        c.gridy = 0;
        c.gridheight = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;

        Dimension dimensionBoton = new Dimension(200, 40); 

        JButton botonAgregar = new JButton("Agregar Usuario");
        botonAgregar.setPreferredSize(dimensionBoton);
        botonAgregar.setFont(fuenteBoton);
        botonAgregar.setBackground(colorBoton);
        botonAgregar.setForeground(Color.WHITE);
        botonAgregar.setFocusPainted(false);
        botonAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = campoUsuario.getText();
                try {
                    if (usuarios.buscarUsername(username)) {
                        JOptionPane.showMessageDialog(null, "El usuario ya existe", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        usuarios.addUser(username);
                        JOptionPane.showMessageDialog(null, "Usuario creado con exito", "Creacion", 1);
                        limpiarCampos();
                        actualizarInfo();
                    }
                } catch (IOException ex) {
                    mostrarError(ex, "Error al agregar usuario");
                }
            }
        });
        panel.add(botonAgregar, c);

        c.gridy = 1;
        JButton botonDesactivar = new JButton("Desactivar Usuario");
        botonDesactivar.setPreferredSize(dimensionBoton);
        botonDesactivar.setFont(fuenteBoton);
        botonDesactivar.setBackground(colorBoton);
        botonDesactivar.setForeground(Color.WHITE);
        botonDesactivar.setFocusPainted(false);
        botonDesactivar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = campoUsuario.getText();
                try {
                    if (!usuarios.buscarUsername(username)) {
                        JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        usuarios.deactivateUser(username);
                        JOptionPane.showMessageDialog(null, "El usuario ha sido desactivado", "Desactivacion", 1);
                        limpiarCampos();
                        actualizarInfo();
                    }
                } catch (IOException ex) {
                    mostrarError(ex, "Error al desactivar usuario");
                }
            }
        });
        panel.add(botonDesactivar, c);

        c.gridy = 2;
        JButton botonBuscar = new JButton("Buscar Usuario");
        botonBuscar.setPreferredSize(dimensionBoton);
        botonBuscar.setFont(fuenteBoton);
        botonBuscar.setBackground(colorBoton);
        botonBuscar.setForeground(Color.WHITE);
        botonBuscar.setFocusPainted(false);
        botonBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = campoUsuario.getText();
                try {
                    if (!usuarios.buscarUsername(username)) {
                        JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        String info = usuarios.playerInfo(username);
                        informacionUsuario.setText(info);
                        if (info.contains("Activo: No")) {
                            JOptionPane.showMessageDialog(null, "El usuario esta inactivo", "Info", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } catch (IOException ex) {
                    mostrarError(ex, "Error al buscar usuario");
                }
            }
        });
        panel.add(botonBuscar, c);

        c.gridy = 3;
        JButton botonAgregarTrofeo = new JButton("Agregar Trofeo");
        botonAgregarTrofeo.setPreferredSize(dimensionBoton);
        botonAgregarTrofeo.setFont(fuenteBoton);
        botonAgregarTrofeo.setBackground(colorBoton);
        botonAgregarTrofeo.setForeground(Color.WHITE);
        botonAgregarTrofeo.setFocusPainted(false);
        botonAgregarTrofeo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = campoUsuario.getText();
                String juego = campoJuego.getText();
                String nombreTrofeo = campoTrofeo.getText();
                String descripcion = campoDescripcion.getText();
                Trophy tipo = (Trophy) comboTrofeo.getSelectedItem();
                try {
                    if (!usuarios.buscarUsername(username)) {
                        JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (!usuarios.isActive(username)) {
                        JOptionPane.showMessageDialog(null, "El usuario esta inactivo", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        usuarios.addTrophieTo(username, juego, nombreTrofeo, tipo, descripcion);
                        JOptionPane.showMessageDialog(null, "Trofeo agregado correctamente", "Creacion", 1);
                        limpiarCampos();
                        actualizarInfo();
                    }
                } catch (IOException ex) {
                    mostrarError(ex, "Error al agregar trofeo");
                }
            }
        });
        panel.add(botonAgregarTrofeo, c);

        informacionUsuario = new JTextArea();
        informacionUsuario.setFont(fuenteBoton);
        informacionUsuario.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(informacionUsuario);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Informacion del Usuario"));
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 3;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        panel.add(scrollPane, c);

        ventana.setVisible(true);
    }

    private void limpiarCampos() {
        campoUsuario.setText("");
        campoJuego.setText("");
        campoTrofeo.setText("");
        campoDescripcion.setText("");
        comboTrofeo.setSelectedIndex(0);
    }

    private void actualizarInfo() {
        informacionUsuario.setText("");
    }

    private void mostrarError(Exception ex, String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje + "\nDetalles: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }

    public static void main(String[] args) {
        try {
            new Main();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
