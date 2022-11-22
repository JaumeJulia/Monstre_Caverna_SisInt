package monstrecaverna.vista;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import monstrecaverna.control.Agente;
import monstrecaverna.control.Control;
import monstrecaverna.modelo.Direcciones;
import monstrecaverna.modelo.PosicionInicialAgente;

/**
 *
 * @author bertu
 */
public class Vista extends JFrame implements ChangeListener, ComponentListener, MouseListener, ActionListener {

    Control control;

    private Graphics g;
    private String directorioImagen;
    BufferedImage imagen;

    public Cuadro[][] matrizCuadros;
    private int minX, maxX, minY, maxY;

    int ancho = 1600;
    int alto = 1000;

    private final Recinto recinto;
    private final JPanel opciones = new JPanel();

    JRadioButton posicionarAbismo, posicionarMonstruo, posicionarTesoro;
    private ButtonGroup grupoBotones;
    private JButton posicionarAgente;
    private JToggleButton iniciar;
    boolean abismo = false, monstruo = false, tesoro = false;
    private int cantidadAgentes = 0, cantidadTesoro = 0;
    private int posicionAgente[] = new int[2];
    private PosicionInicialAgente posicionesInicialesAgentes[] = new PosicionInicialAgente[4];
    private int posicionInicialAgente[] = new int[2];
    private final JLabel tamañoRecintoLabel = new JLabel("Tamaño del recinto: ");
    private JTextField tamañoRecintoText = new JTextField();
    private final JLabel cantidadAgente = new JLabel("Agentes en la cueva: ");
    private JTextField cantidadAgenteText = new JTextField();
    final JSlider sliderTamañoRecinto = new JSlider(JSlider.HORIZONTAL, 5, 20, 10);

    //CONSTRUCTOR DE VISTA
    public Vista(String nombre, Control control) {

        super(nombre);
        this.control = control;
        recinto = new Recinto(this);
        initComponents();

    }

    //MOSTRAMOS LA VENTANA
    public void mostrar() {

        this.setPreferredSize(new Dimension((int) ancho, (int) alto));
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    //INICIAMOS LOS COMPONENTES DE LA VENTANA
    private void initComponents() {

        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

        opciones.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        posicionarAbismo = new JRadioButton("Abismo");
        posicionarMonstruo = new JRadioButton("Monstruo");
        posicionarTesoro = new JRadioButton("Tesoro");
        grupoBotones = new ButtonGroup();
        grupoBotones.add(posicionarAbismo);
        grupoBotones.add(posicionarMonstruo);
        grupoBotones.add(posicionarTesoro);

        iniciar = new JToggleButton("Iniciar");
        iniciar.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int estado = e.getStateChange();
                if (estado == ItemEvent.SELECTED) {
                    Thread thread = new Thread(control);
                    control.setSimulacion(true);
                    thread.start();
                } else {
                    control.setSimulacion(false);
                }
            }
        });

        tamañoRecintoText = new JTextField("5");
        tamañoRecintoText.setEditable(false);
        tamañoRecintoText.setText(String.valueOf(sliderTamañoRecinto.getValue()));
        sliderTamañoRecinto.addChangeListener(this);

        JPanel tamaño = new JPanel();
        tamaño.setLayout(new FlowLayout());
        tamaño.add(tamañoRecintoLabel);
        tamaño.add(tamañoRecintoText);

        cantidadAgenteText = new JTextField("1");
        cantidadAgenteText.setEditable(true);
        posicionarAgente = new JButton("Posicionar");
        posicionarAgente.addActionListener(this);

        JPanel agente = new JPanel();
        agente.setLayout(new FlowLayout());
        agente.add(cantidadAgente);
        agente.add(cantidadAgenteText);

        c.gridwidth = 1;
        c.weighty = .2;
        c.gridx = 0;
        c.gridy = 1;
        opciones.add(tamaño, c);
        c.gridy = 2;
        opciones.add(sliderTamañoRecinto, c);
        c.gridy = 3;
        opciones.add(agente, c);
        c.gridy = 4;
        opciones.add(posicionarAgente, c);
        c.gridy = 5;
        opciones.add(posicionarAbismo, c);
        c.gridy = 6;
        opciones.add(posicionarMonstruo, c);
        c.gridy = 7;
        opciones.add(posicionarTesoro, c);
        c.gridy = 8;
        opciones.add(iniciar, c);
        opciones.setMaximumSize(new Dimension((int) (ancho * 0.25), alto));

        recinto.setMaximumSize(new Dimension((int) (ancho * 0.75), alto));

        this.add(recinto);
        this.add(opciones);
        this.addComponentListener(this);
        this.addMouseListener(this);

        //CALCULAMOS LO QUE OCUPA LA CUADRICULA Y LO QUE OCUPA CADA CUADRO
        double auxAncho = recinto.getWidth(), auxAlto = recinto.getHeight();
        int tamañoBase;
        if ((auxAncho / auxAlto) >= 1) {
            minX = (int) (((auxAncho - auxAlto) / 2) + 10);
            minY = 10;
            maxX = (int) (auxAncho - (((auxAncho - auxAlto) / 2) - 50));
            maxY = alto - 50;
            tamañoBase = (maxY - minY) / (sliderTamañoRecinto.getValue() + 2);
        } else {
            minX = 10;
            minY = (int) (((auxAlto - auxAncho) / 2) + 10);
            maxX = (int) (auxAncho - 50);
            maxY = (int) (auxAlto - (((auxAlto - auxAncho) / 2) - 50));
            tamañoBase = (maxX - minX) / (sliderTamañoRecinto.getValue() + 2);
        }
        int posX = minX, posY = minY;

        //INICIALIZAMOS LA MATRIZ DE CUADROS CON EL TAMAÑO INDICADO EN EL SLIDER
        matrizCuadros = new Cuadro[sliderTamañoRecinto.getValue() + 2][sliderTamañoRecinto.getValue() + 2];
        for (int i = 0; i < sliderTamañoRecinto.getValue() + 2; i++) {
            for (int j = 0; j < sliderTamañoRecinto.getValue() + 2; j++) {
                if ((i == 0) || (j == 0) || (i == (sliderTamañoRecinto.getValue() + 1)) || (j == (sliderTamañoRecinto.getValue() + 1))) {
                    matrizCuadros[i][j] = new Cuadro(posX, posY, tamañoBase, tamañoBase, true, false, true);
                } else {
                    matrizCuadros[i][j] = new Cuadro(posX, posY, tamañoBase, tamañoBase, false, false, false);
                }

                posY += tamañoBase;
            }
            posX += tamañoBase;
            posY = minY;
        }
        repaint();
    }

    //METODO QUE SE EJECUTA CUANDO LA VENTANA SUFRE UNA MODIFICACIÓN DE TAMAÑO
    private void reinit() {
        //RECALCULAMOS LOS ESPACIOS QUE OCUPAN LOS PANELES Y EL TAMAÑO DE LOS
        //CUADROS DE LA MATRIZ
        opciones.setMaximumSize(new Dimension((int) (ancho * 0.25), alto));
        recinto.setMaximumSize(new Dimension((int) (ancho * 0.75), alto));
        this.add(recinto);
        this.add(opciones);
        double auxAncho = recinto.getWidth(), auxAlto = recinto.getHeight();
        int tamañoBase;
        if ((auxAncho / auxAlto) >= 1) {
            minX = (int) (((auxAncho - auxAlto) / 2) + 10);
            minY = 10;
            maxX = (int) (auxAncho - (((auxAncho - auxAlto) / 2) - 50));
            maxY = alto - 50;
            tamañoBase = (maxY - minY) / (sliderTamañoRecinto.getValue() + 2);
        } else {
            minX = 10;
            minY = (int) (((auxAlto - auxAncho) / 2) + 10);
            maxX = (int) (auxAncho - 50);
            maxY = (int) (auxAlto - (((auxAlto - auxAncho) / 2) - 50));
            tamañoBase = (maxX - minX) / (sliderTamañoRecinto.getValue() + 2);
        }
        int posX = minX, posY = minY;

        Cuadro auxMatrizCuadros[][] = matrizCuadros;
        matrizCuadros = new Cuadro[sliderTamañoRecinto.getValue() + 2][sliderTamañoRecinto.getValue() + 2];
        //CREAMOS UNA NUEVA MATRIZ DEL TAMAÑO INDICADO POR EL SLIDER Y COPIAMOS
        //EL CONTENIDO DE LA MATRIZ ANTERIOR
        for (int i = 0; i < sliderTamañoRecinto.getValue() + 2; i++) {
            for (int j = 0; j < sliderTamañoRecinto.getValue() + 2; j++) {
                if (auxMatrizCuadros[0].length > i && auxMatrizCuadros[0].length > j) {
                    if (auxMatrizCuadros[i][j].isCentinela() == true) {
                        matrizCuadros[i][j] = new Cuadro(posX, posY, tamañoBase, tamañoBase,
                                false, auxMatrizCuadros[i][j].isAgente(), false);
                        matrizCuadros[i][j].setAbismo(auxMatrizCuadros[i][j].isAbismo());
                        matrizCuadros[i][j].setMonstruo(auxMatrizCuadros[i][j].isMonstruo());
                        matrizCuadros[i][j].setResplandor(auxMatrizCuadros[i][j].isTesoro());
                        matrizCuadros[i][j].setImagen(auxMatrizCuadros[i][j].getImagen());
                    } else {
                        matrizCuadros[i][j] = new Cuadro(posX, posY, tamañoBase, tamañoBase,
                                auxMatrizCuadros[i][j].isPared(), auxMatrizCuadros[i][j].isAgente(), false);
                        matrizCuadros[i][j].setAbismo(auxMatrizCuadros[i][j].isAbismo());
                        matrizCuadros[i][j].setMonstruo(auxMatrizCuadros[i][j].isMonstruo());
                        matrizCuadros[i][j].setResplandor(auxMatrizCuadros[i][j].isTesoro());
                        matrizCuadros[i][j].setImagen(auxMatrizCuadros[i][j].getImagen());
                    }
                } else {
                    matrizCuadros[i][j] = new Cuadro(posX, posY, tamañoBase, tamañoBase, false, false, false);
                }
                if ((i == 0) || (j == 0) || (i == (sliderTamañoRecinto.getValue() + 1)) || (j == (sliderTamañoRecinto.getValue() + 1))) {
                    matrizCuadros[i][j].setCentinela(true);
                    matrizCuadros[i][j].setPared(true);
                    matrizCuadros[i][j].setAgente(false, "");
                }
                posY += tamañoBase;
            }
            posX += tamañoBase;
            posY = minY;
        }

        repaint();

    }

    public void moverAgente(Direcciones direccion) {
        directorioImagen = direccion.LINKFOTO;
        try {
            imagen = ImageIO.read(new File(directorioImagen));
        } catch (IOException ex) {
            System.out.println("Error de imagen");
        }
        matrizCuadros[posicionAgente[0]][posicionAgente[1]].setAgente(false, directorioImagen);
        posicionAgente[0] += direccion.X;
        posicionAgente[1] += direccion.Y;
        matrizCuadros[posicionAgente[0]][posicionAgente[1]].setAgente(true, directorioImagen);
        repaint();
    }

    //LISTENER PARA EL SLIDER
    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider src = (JSlider) e.getSource();
        if (!src.getValueIsAdjusting() && e.getSource() == sliderTamañoRecinto) {
            tamañoRecintoText.setText(String.valueOf(src.getValue()));
            reinit();
        } else if (!src.getValueIsAdjusting() && e.getSource() == posicionarAgente) {
            cantidadAgenteText.setText(String.valueOf(src.getValue()));
            reinit();
        }

    }

    //LISTENER PARA EL RESIZE DE LA VENTANA
    @Override
    public void componentResized(ComponentEvent e) {
        Dimension nuevoTamaño = e.getComponent().getBounds().getSize();
        ancho = nuevoTamaño.width;
        alto = nuevoTamaño.height;
        reinit();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    //METODO QUE DEVUELVE EL CUADRO INDICADO POR a[]
    public Cuadro getCasilla(int[] a) {
        int resultadoAbsoluto[] = {a[0] + posicionInicialAgente[0],a[1] + posicionInicialAgente[1]}; 
        System.out.println("Resultado absoluto: " + Arrays.toString(resultadoAbsoluto));
        return matrizCuadros[a[0] + posicionInicialAgente[0]][a[1] + posicionInicialAgente[1]];
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

            for (i = 0; i <= matrizCuadros[0].length; i++) {
                if (i == matrizCuadros[0].length) {
                    if (matrizCuadros[0][i - 1].getY() + matrizCuadros[0][i - 1].getHeight() >= e.getY()) {
                        i--;
                        break;
                    }
                }
                if (matrizCuadros[0][i].getY() > e.getY()) {
                    i--;
                    break;
                }
            }

            for (j = 0; j <= matrizCuadros[0].length; j++) {
                if (j == matrizCuadros[0].length) {
                    if (matrizCuadros[j - 1][0].getX() + matrizCuadros[j - 1][0].getWidth() >= e.getX()) {
                        j--;
                        break;
                    }
                }
                if (matrizCuadros[j][i].getX() > e.getX()) {
                    j--;
                    break;
                }
            }

            if (posicionarAbismo.isSelected() == true && matrizCuadros[j][i].isPared() == false) {
                matrizCuadros[j][i].setAbismo(true);
                matrizCuadros[j + 1][i].setBrisa(true);
                matrizCuadros[j][i + 1].setBrisa(true);
                matrizCuadros[j - 1][i].setBrisa(true);
                matrizCuadros[j][i - 1].setBrisa(true);
                try {
                    imagen = ImageIO.read(new File("src/monstrecaverna/modelo/abismo.png"));
                } catch (IOException ex) {
                    System.out.println("Error de imagen");
                }
            } else if (posicionarMonstruo.isSelected() == true && matrizCuadros[j][i].isPared() == false) {
                matrizCuadros[j][i].setMonstruo(true);
                matrizCuadros[j + 1][i].setHedor(true);
                matrizCuadros[j][i + 1].setHedor(true);
                matrizCuadros[j - 1][i].setHedor(true);
                matrizCuadros[j][i - 1].setHedor(true);
                try {
                    imagen = ImageIO.read(new File("src/monstrecaverna/modelo/monstruo.png"));
                } catch (IOException ex) {
                    System.out.println("Error de imagen");
                }
            } else if (posicionarTesoro.isSelected() == true && matrizCuadros[j][i].isPared() == false) {
                matrizCuadros[j][i].setResplandor(true);
                try {
                    imagen = ImageIO.read(new File("src/monstrecaverna/modelo/tesoro.png"));
                } catch (IOException ex) {
                    System.out.println("Error de imagen");
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

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == posicionarAgente) {
            if ((Integer.parseInt(cantidadAgenteText.getText())) > 4) {
                cantidadAgenteText.setText("4");
            }
            switch (Integer.parseInt(cantidadAgenteText.getText())) {
                case 4:
                    matrizCuadros[matrizCuadros[1].length - 2][matrizCuadros[1].length - 2].setAgente(true, "src/monstrecaverna/modelo/amogus_OESTE.png");
                case 3:
                    matrizCuadros[matrizCuadros[1].length - 2][1].setAgente(true, "src/monstrecaverna/modelo/amogus_OESTE.png");
                case 2:
                    matrizCuadros[1][matrizCuadros[1].length - 2].setAgente(true, "src/monstrecaverna/modelo/amogus_OESTE.png");
                case 1:
                    matrizCuadros[1][1].setAgente(true, "src/monstrecaverna/modelo/amogus_OESTE.png");
                    posicionInicialAgente[0] = 1;
                    posicionInicialAgente[1] = 1;
                    Agente ag = new Agente(1, this);
                    control.setAgente(ag);
            }
            repaint();
        }

    }
}
