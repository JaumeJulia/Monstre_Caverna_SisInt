/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package robot.vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import robot.control.Agente;

/**
 *
 * @author bertu
 */
public class Recinto extends JPanel implements MouseListener {

    private Vista vista;

    public Recinto(Vista v) {
        this.vista = v;
        this.addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        g.setColor(Color.BLACK);
        for (int i = 0; i < vista.sliderTamañoRecinto.getValue() + 2; i++) {
            for (int j = 0; j < vista.sliderTamañoRecinto.getValue() + 2; j++) {
                int x = vista.matrizCuadros[i][j].getX(), y = vista.matrizCuadros[i][j].getY(),
                        w = vista.matrizCuadros[i][j].getWidth(), h = vista.matrizCuadros[i][j].getHeight();
                g.drawRect(x, y, w, h);
                if (vista.matrizCuadros[i][j].isPared()) {
                    g.fillRect(x, y, w, h);
                }
                if (vista.matrizCuadros[i][j].isAgente()) {

                    g.drawImage(vista.imagen, x, y, w, h, this);
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    //LISTENER PARA CUANDO SE PULSA EL CLICK DEL RATON. CON ESTE METODO CAMBIAMOS
    //LOS ESTADOS DE pared Y agente DE LAS CASILLAS DE LA MATRIZ
    @Override
    public void mousePressed(MouseEvent e) {
        try {
            int i = 0, j = 0;

            for (i = 0; i <= vista.matrizCuadros[0].length; i++) {
                if (i == vista.matrizCuadros[0].length) {
                    if (vista.matrizCuadros[0][i - 1].getY() + vista.matrizCuadros[0][i - 1].getHeight() >= e.getY()) {
                        i--;
                        break;
                    }
                }
                if (vista.matrizCuadros[0][i].getY() > e.getY()) {
                    i--;
                    break;
                }
            }

            for (j = 0; j <= vista.matrizCuadros[0].length; j++) {
                if (j == vista.matrizCuadros[0].length) {
                    if (vista.matrizCuadros[j - 1][0].getX() + vista.matrizCuadros[j - 1][0].getWidth() >= e.getX()) {
                        j--;
                        break;
                    }
                }
                if (vista.matrizCuadros[j][i].getX() > e.getX()) {
                    j--;
                    break;
                }
            }

            if (vista.agente == true && vista.matrizCuadros[j][i].isPared() == false) {
                vista.matrizCuadros[vista.posicionAgente[0]][vista.posicionAgente[1]].setAgente(false);
                vista.matrizCuadros[j][i].setAgente(true);
                vista.posicionAgente[0] = j;
                vista.posicionAgente[1] = i;
                Agente robot = new Agente(vista.posicionAgente, 1, vista);
                vista.control.setAgente(robot);
                try {
                    vista.imagen = ImageIO.read(new File("src/robot/modelo/amogus_OESTE.png"));
                } catch (IOException ex) {
                    System.out.println("Error de imagen");
                }
                vista.agente = false;
            } else if (vista.agente == false && vista.matrizCuadros[j][i].isAgente() == false) {
                if (vista.matrizCuadros[j][i].isPared() == false) {
                    vista.matrizCuadros[j][i].setPared(true);
                } else if (vista.matrizCuadros[j][i].isCentinela() == false) {
                    vista.matrizCuadros[j][i].setPared(false);
                }
            }
            repaint();
        } catch (Exception outOfBounds) {

        }
    }

    @Override
    public void mouseReleased(MouseEvent e
    ) {
    }

    @Override
    public void mouseEntered(MouseEvent e
    ) {
    }

    @Override
    public void mouseExited(MouseEvent e
    ) {
    }
}
