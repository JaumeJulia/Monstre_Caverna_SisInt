/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monstrecaverna.vista;

/**
 *
 * @author bertu
 */
import java.awt.Image;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeListener;

public class RadioButtonConImagen extends JPanel {

    private JRadioButton radio = new JRadioButton();
    private JLabel image;

    public RadioButtonConImagen(ImageIcon icon) {
        Image imagen = icon.getImage(); // transform it 
        Image newimg = imagen.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        icon = new ImageIcon(newimg);
        image = new JLabel(icon);
        add(radio);
        add(image);
    }

    public void addToButtonGroup(ButtonGroup group) {
        group.add(radio);
    }

    public void addActionListener(ActionListener listener) {
        radio.addActionListener(listener);
    }

    public void addChangeListener(ChangeListener listener) {
        radio.addChangeListener(listener);
    }

    public Icon getImage() {
        return image.getIcon();
    }

    public void setImage(Icon icon) {
        image.setIcon(icon);
    }

    public boolean getIsSelected() {
        return radio.isSelected();
    }

}
