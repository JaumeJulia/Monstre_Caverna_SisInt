/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monstrecaverna.vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author bertu
 */
public class Recinto extends JPanel implements MouseListener {

    private Vista vista;
    private Image img;

    public Recinto(Vista v) {
        this.vista = v;
        this.addMouseListener(this);
        try {
            this.img = ImageIO.read(new File("src/monstrecaverna/modelo/Hotpot_2.png"));
        } catch (IOException ex) {
            Logger.getLogger(Recinto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.setColor(Color.WHITE);
        g.drawImage(this.img, this.getX(), this.getY(), this.getWidth(), this.getHeight(), this);
//        g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
//        System.out.println(this.getBounds());
        
        for (int i = 0; i < vista.sliderTamañoRecinto.getValue() + 2; i++) {
            for (int j = 0; j < vista.sliderTamañoRecinto.getValue() + 2; j++) {
                int x = vista.matrizCuadros[i][j].getX(), y = vista.matrizCuadros[i][j].getY(),
                        w = vista.matrizCuadros[i][j].getWidth(), h = vista.matrizCuadros[i][j].getHeight();

                if (!vista.matrizCuadros[i][j].isPared()) {
//                    g.setColor(Color.BLACK);
//                    g.fillRect(x, y, w, h);
//                    g.setColor(Color.WHITE);
//                    g.fillRect(x, y, w, h);
                    g.setColor(Color.WHITE);
                    g.drawRect(x, y, w, h);
                }
                if (vista.matrizCuadros[i][j].isAgente() || vista.matrizCuadros[i][j].isAbismo()
                        || vista.matrizCuadros[i][j].isMonstruo() || vista.matrizCuadros[i][j].isTesoro()
                        || vista.matrizCuadros[i][j].isBrisa() || vista.matrizCuadros[i][j].isHedor()) {
                    System.out.println("Imagen en la casilla [" + i + "][" + j + "]:" + vista.matrizCuadros[i][j].getImagen());
                    System.out.println("Estado de la casilla [" + i + "][" + j + "]: "
                            + "\nAgente -> " + vista.matrizCuadros[i][j].isAgente()
                            + "\nAbismo -> " + vista.matrizCuadros[i][j].isAbismo()
                            + "\nMonstruo -> " + vista.matrizCuadros[i][j].isMonstruo()
                            + "\nTesoro -> " + vista.matrizCuadros[i][j].isTesoro());
                    System.out.println("Posicion absoluta de la casilla[" + i + "][" + j + "]:" + vista.matrizCuadros[i][j].getX() + "," + vista.matrizCuadros[i][j].getY());
                    g.drawImage(vista.matrizCuadros[i][j].getImagen(), x, y, w, h, this);
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

            if (vista.posicionarAbismo.getIsSelected() == true && vista.matrizCuadros[j][i].isAbismo() == true) {
                System.out.println("Aqui");
                vista.matrizCuadros[j][i].setAbismo(false);
                vista.matrizCuadros[j + 1][i].setBrisa(false);
                vista.matrizCuadros[j][i + 1].setBrisa(false);
                vista.matrizCuadros[j - 1][i].setBrisa(false);
                vista.matrizCuadros[j][i - 1].setBrisa(false);

            } else if (vista.posicionarAbismo.getIsSelected() == true && vista.matrizCuadros[j][i].isMonstruo() == false
                    && vista.matrizCuadros[j][i].isTesoro() == false && vista.matrizCuadros[j][i].isAgente() == false) {
                vista.matrizCuadros[j][i].setAbismo(true);
                if (j < vista.matrizCuadros[j].length - 2) {
                    vista.matrizCuadros[j + 1][i].setBrisa(true);
                }
                if (i < vista.matrizCuadros[j].length - 2) {
                    vista.matrizCuadros[j][i + 1].setBrisa(true);
                }
                if ((j - 1) > 0) {
                    vista.matrizCuadros[j - 1][i].setBrisa(true);
                }
                if ((i - 1) > 0) {
                    vista.matrizCuadros[j][i - 1].setBrisa(true);
                }

            } else if (vista.posicionarMonstruo.getIsSelected() == true && vista.matrizCuadros[j][i].isMonstruo() == true) {
                vista.matrizCuadros[j][i].setMonstruo(false);
                vista.matrizCuadros[j + 1][i].setHedor(false);
                vista.matrizCuadros[j][i + 1].setHedor(false);
                vista.matrizCuadros[j - 1][i].setHedor(false);
                vista.matrizCuadros[j][i - 1].setHedor(false);

            } else if (vista.posicionarMonstruo.getIsSelected() == true && vista.matrizCuadros[j][i].isAbismo() == false
                    && vista.matrizCuadros[j][i].isTesoro() == false && vista.matrizCuadros[j][i].isAgente() == false) {
                vista.matrizCuadros[j][i].setMonstruo(true);
                if (j < vista.matrizCuadros[j].length - 2) {
                    vista.matrizCuadros[j + 1][i].setHedor(true);
                }
                if (i < vista.matrizCuadros[j].length - 2) {
                    vista.matrizCuadros[j][i + 1].setHedor(true);
                }
                if ((j - 1) > 0) {
                    vista.matrizCuadros[j - 1][i].setHedor(true);
                }
                if ((i - 1) > 0) {
                    vista.matrizCuadros[j][i - 1].setHedor(true);
                }

            } else if (vista.posicionarTesoro.getIsSelected() == true && vista.matrizCuadros[j][i].isTesoro() == true) {
                vista.matrizCuadros[j][i].setResplandor(false);
            } else if (vista.posicionarTesoro.getIsSelected() == true && vista.matrizCuadros[j][i].isMonstruo() == false
                    && vista.matrizCuadros[j][i].isAbismo() == false && vista.matrizCuadros[j][i].isAgente() == false) {
                vista.matrizCuadros[j][i].setResplandor(true);
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
