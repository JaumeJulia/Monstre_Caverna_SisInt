/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monstrecaverna.vista;

import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author bertu
 */
public class Cuadro {

    Rectangle rectangulo;
    private boolean pared, agente, centinela, hedor, brisa, resplandor, abismo, monstruo;
    private Image imagen;

    public Cuadro(int x, int y, int w, int h, boolean p, boolean a, boolean c) {
        this.rectangulo = new Rectangle(x, y, w, h);
        this.pared = p;
        this.agente = a;
        this.centinela = c;
    }

    public void setPared(boolean p) {
        this.pared = p;
    }

    public void setAgente(boolean a, String directorioImagen) {
        this.agente = a;
        if (a) {
            try {
                this.imagen = ImageIO.read(new File(directorioImagen));
            } catch (IOException ex) {
                Logger.getLogger(Cuadro.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setCentinela(boolean c) {
        this.centinela = c;
    }

    public void setWidth(int w) {
        this.rectangulo.setRect(this.rectangulo.getX(), this.rectangulo.getY(), w, this.rectangulo.getHeight());
    }

    public void setHeight(int h) {
        this.rectangulo.setRect(this.rectangulo.getX(), this.rectangulo.getY(), this.rectangulo.getWidth(), h);
    }

    public boolean isPared() {
        return this.pared;
    }

    public boolean isAgente() {
        return this.agente;
    }

    public boolean isCentinela() {
        return this.centinela;
    }

    public void setHedor(boolean h) {
        this.hedor = h;
        if (h) {
            try {
                this.imagen = ImageIO.read(new File("src/monstrecaverna/modelo/suelo_hedor.png"));
            } catch (IOException ex) {
                Logger.getLogger(Cuadro.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setBrisa(boolean s) {
        this.brisa = s;
        if (s) {
            try {
                this.imagen = ImageIO.read(new File("src/monstrecaverna/modelo/suelo_brisa.png"));
            } catch (IOException ex) {
                Logger.getLogger(Cuadro.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setResplandor(boolean t) {
        this.resplandor = t;
        if (t) {
            try {
                this.imagen = ImageIO.read(new File("src/monstrecaverna/modelo/suelo_tesoro.png"));
            } catch (IOException ex) {
                Logger.getLogger(Cuadro.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setAbismo(boolean a) {
        this.abismo = a;
        if (a) {
            try {
                this.imagen = ImageIO.read(new File("src/monstrecaverna/modelo/suelo_abismo.png"));
            } catch (IOException ex) {
                Logger.getLogger(Cuadro.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setMonstruo(boolean m) {
        this.monstruo = m;
        if (m) {
            try {
                this.imagen = ImageIO.read(new File("src/monstrecaverna/modelo/suelo_monstruo.png"));
            } catch (IOException ex) {
                Logger.getLogger(Cuadro.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setImagen (Image img){
        this.imagen = img;
    }
    
    public boolean[] getEstado() {
        return new boolean[]{this.hedor, this.brisa, this.resplandor};
    }

    public boolean isBrisa() {
        return this.brisa;
    }

    public boolean isHedor() {
        return this.hedor;
    }
    
    public boolean isMonstruo() {
        return this.monstruo;
    }

    public boolean isAbismo() {
        return this.abismo;
    }

    public boolean isTesoro() {
        return this.resplandor;
    }

    public Image getImagen() {
        return this.imagen;
    }

    public int getX() {
        return (int) this.rectangulo.getX();
    }

    public int getY() {
        return (int) this.rectangulo.getY();
    }

    public int getWidth() {
        return (int) this.rectangulo.getWidth();
    }

    public int getHeight() {
        return (int) this.rectangulo.getHeight();
    }

}

/*
@Override
            public void setRect(double x, double y, double w, double h) {}
            
            @Override
            public int outcode(double x, double y) {return 0;}
            
            @Override
            public Rectangle2D createIntersection(Rectangle2D r) {return null;}
            
            @Override
            public Rectangle2D createUnion(Rectangle2D r) {return null;}
            
            @Override
            public double getX() {return 0;}
            
            @Override
            public double getY() {return 0;}
            
            @Override
            public double getWidth() {return 0;}
            
            @Override
            public double getHeight() {return 0;}
            
            @Override
            public boolean isEmpty() {return false;}
 */
