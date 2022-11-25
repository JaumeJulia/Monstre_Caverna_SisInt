package monstrecaverna.vista;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import monstrecaverna.control.Agente;
import monstrecaverna.control.Control;
import monstrecaverna.modelo.Direcciones;
import monstrecaverna.modelo.MovimientoAgenteWrapper;
import monstrecaverna.modelo.PosicionAgente;

/**
 *
 * @author bertu
 */
public class Vista extends JFrame implements ChangeListener, ComponentListener, ItemListener {

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

    //Opciones de tamaño del recinto
    private final JPanel opcionesRecinto = new JPanel();
    private final JLabel tamañoRecintoLabel = new JLabel("Tamaño del recinto: ");
    private JTextField tamañoRecintoText = new JTextField();
    final JSlider sliderTamañoRecinto = new JSlider(JSlider.HORIZONTAL, 5, 20, 10);

    //Opciones para añadir obstaculos o tesoros
    private final JPanel opcionesObstaculo = new JPanel();
    RadioButtonConImagen posicionarAbismo, posicionarMonstruo, posicionarTesoro;
    private JLabel imagenAbismo, imagenMonstruo, imagenTesoro;
    private ButtonGroup grupoBotones;

    //Opciones de inicio para los agentes
    private final JPanel opcionesAgente = new JPanel();
    private JLabel cantidadAgente;
    private JTextField cantidadAgenteText;
    final JSlider sliderCantidadAgentes = new JSlider(JSlider.HORIZONTAL, 1, 4, 1);
    private JToggleButton iniciar;

    //Información general de la vista
    boolean abismo = false, monstruo = false, tesoro = false;
    private int cantidadAgentes = 0, cantidadTesoro = 0;
    private PosicionAgente[] posicionesAgentes = new PosicionAgente[4];
    private boolean avaricioso;
    //private int posicionAgente[] = new int[2];
    //private PosicionAgente posicionesInicialesAgentes[] = new PosicionAgente[4];
    //private int posicionInicialAgente[] = new int[2];

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

    private JPanel panelOpcionesRecinto() {

        opcionesRecinto.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.MatteBorder(null),
                javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED)));

        JPanel texto = new JPanel(new FlowLayout());
        tamañoRecintoText = new JTextField("5");
        tamañoRecintoText.setFont(new Font("calibri", Font.BOLD, 30));
        tamañoRecintoText.setEditable(false);
        tamañoRecintoText.setText(String.valueOf(sliderTamañoRecinto.getValue()));
        tamañoRecintoLabel.setFont(new Font("calibri", Font.BOLD, 30));
        sliderTamañoRecinto.addChangeListener(this);

        opcionesRecinto.setLayout(new GridLayout(2, 1));
        texto.add(tamañoRecintoLabel);
        texto.add(tamañoRecintoText);
        opcionesRecinto.add(texto);
        opcionesRecinto.add(sliderTamañoRecinto);

        return opcionesRecinto;
    }

    private JPanel panelOpcionesObstaculo() {

        opcionesObstaculo.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.MatteBorder(null),
                javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED)));

        posicionarAbismo = new RadioButtonConImagen(new ImageIcon("src/monstrecaverna/modelo/abismo.png"));
        posicionarMonstruo = new RadioButtonConImagen(new ImageIcon("src/monstrecaverna/modelo/monstruo.png"));
        posicionarTesoro = new RadioButtonConImagen(new ImageIcon("src/monstrecaverna/modelo/tesoro.png"));

        grupoBotones = new ButtonGroup();
        posicionarAbismo.addToButtonGroup(grupoBotones);
        posicionarMonstruo.addToButtonGroup(grupoBotones);
        posicionarTesoro.addToButtonGroup(grupoBotones);

        opcionesObstaculo.setLayout(new GridLayout(3, 1));

        opcionesObstaculo.add(posicionarAbismo);
        opcionesObstaculo.add(posicionarMonstruo);
        opcionesObstaculo.add(posicionarTesoro);

        return opcionesObstaculo;
    }

    private JPanel panelopcionesAgente() {

        opcionesAgente.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.MatteBorder(null),
                javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED)));

        JPanel texto = new JPanel(new FlowLayout());
        cantidadAgente = new JLabel("Agentes en la cueva: ");
        cantidadAgente.setFont(new Font("calibri", Font.BOLD, 30));
        cantidadAgenteText = new JTextField("1");
        cantidadAgenteText.setFont(new Font("calibri", Font.BOLD, 30));
        cantidadAgenteText.setEditable(false);

        opcionesAgente.setLayout(new GridLayout(3, 1));
        cantidadAgenteText.setText(String.valueOf(sliderCantidadAgentes.getValue()));
        sliderCantidadAgentes.addChangeListener(this);

        iniciar = new JToggleButton("Iniciar");
        iniciar.setFont(new Font("calibri", Font.BOLD, 30));
        iniciar.addItemListener(this);

        texto.add(cantidadAgente);
        texto.add(cantidadAgenteText);
        opcionesAgente.add(texto);
        opcionesAgente.add(sliderCantidadAgentes);
        opcionesAgente.add(iniciar);

        return opcionesAgente;
    }

    //INICIAMOS LOS COMPONENTES DE LA VENTANA
    private void initComponents() {

        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

        opciones.setBorder(javax.swing.BorderFactory.createMatteBorder(4, 4, 4, 4, new java.awt.Color(0, 0, 0)));
        opciones.setMaximumSize(new Dimension((int) (ancho * 0.2), alto));
        opciones.setBounds(0, 0, opciones.getMaximumSize().width, opciones.getMaximumSize().height);
        recinto.setMaximumSize(new Dimension((int) (ancho * 0.8), alto));

        opciones.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 4;
        constraints.gridheight = 1;
        opciones.add(panelOpcionesRecinto(), constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 4;
        constraints.gridheight = 3;
        opciones.add(panelOpcionesObstaculo(), constraints);
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 4;
        constraints.gridheight = 1;
        opciones.add(panelopcionesAgente(), constraints);

        this.add(recinto);
        this.add(opciones);

        this.addComponentListener(this);

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
        opciones.setMaximumSize(new Dimension((int) (ancho * 0.2), alto));
        recinto.setMaximumSize(new Dimension((int) (ancho * 0.8), alto));
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

                    } else {
                        matrizCuadros[i][j] = new Cuadro(posX, posY, tamañoBase, tamañoBase,
                                auxMatrizCuadros[i][j].isPared(), auxMatrizCuadros[i][j].isAgente(), false);
                    }
                    matrizCuadros[i][j].setAbismo(auxMatrizCuadros[i][j].isAbismo());
                    matrizCuadros[i][j].setMonstruo(auxMatrizCuadros[i][j].isMonstruo());
                    matrizCuadros[i][j].setResplandor(auxMatrizCuadros[i][j].isTesoro());
                    matrizCuadros[i][j].setBrisa(auxMatrizCuadros[i][j].isBrisa());
                    matrizCuadros[i][j].setHedor(auxMatrizCuadros[i][j].isHedor());
                    matrizCuadros[i][j].setImagen(auxMatrizCuadros[i][j].getImagen());
                } else {
                    matrizCuadros[i][j] = new Cuadro(posX, posY, tamañoBase, tamañoBase, false, false, false);
                }
                if ((i == 0) || (j == 0) || (i == (sliderTamañoRecinto.getValue() + 1)) || (j == (sliderTamañoRecinto.getValue() + 1))) {
                    matrizCuadros[i][j].setCentinela(true);
                    matrizCuadros[i][j].setPared(true);
                    matrizCuadros[i][j].setAgente(false, "");
                    matrizCuadros[i][j].setAbismo(false);
                    matrizCuadros[i][j].setMonstruo(false);
                }
                posY += tamañoBase;
            }
            posX += tamañoBase;
            posY = minY;
        }

        repaint();

    }

    public void moverAgente(MovimientoAgenteWrapper movimiento) {
        directorioImagen = movimiento.getDireccion().LINKFOTO;
        int[] posicionAgente = posicionesAgentes[movimiento.getIdentificador()].getPosicionActual();
        
        matrizCuadros[posicionAgente[0]][posicionAgente[1]].setAgente(false, "");
        if (matrizCuadros[posicionAgente[0]][posicionAgente[1]].isBrisa()) {
            matrizCuadros[posicionAgente[0]][posicionAgente[1]].setBrisa(true);
        } else if (matrizCuadros[posicionAgente[0]][posicionAgente[1]].isHedor()) {
            matrizCuadros[posicionAgente[0]][posicionAgente[1]].setHedor(true);
        } else if (matrizCuadros[posicionAgente[0]][posicionAgente[1]].isTesoro()) {
            matrizCuadros[posicionAgente[0]][posicionAgente[1]].setResplandor(false);
        }
        posicionAgente[0] += movimiento.getDireccion().X;
        posicionAgente[1] += movimiento.getDireccion().Y;
        matrizCuadros[posicionAgente[0]][posicionAgente[1]].setAgente(true, directorioImagen);
        repaint();
        try {
            Thread.sleep(250);
        } catch (InterruptedException ex) {
            Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean cogerTesoro(int identificador){
        Cuadro casilla = getCasilla(identificador, posicionesAgentes[identificador].getPosicionActual());
        boolean[] estado = casilla.getEstado();
        if(estado[2]){
            casilla.setResplandor(false);
            cantidadTesoro -= 1;
            return true;
        } else {
            return false;
        }
    }

    public void setCantidadTesoro(int cantidadTesoro) {
        this.cantidadTesoro += cantidadTesoro;
    }
    
    public int getCantidadTesoro(){
        return cantidadTesoro;
    }

    public boolean getAvaricioso(){
        return avaricioso;
    }
    //LISTENER PARA EL SLIDER
    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider src = (JSlider) e.getSource();
        if (!src.getValueIsAdjusting() && e.getSource() == sliderTamañoRecinto) {
            tamañoRecintoText.setText(String.valueOf(src.getValue()));
            reinit();
        } else if (!src.getValueIsAdjusting() && e.getSource() == sliderCantidadAgentes) {
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
    public Cuadro getCasilla(int id, int[] a) {
        int[] posicionInicialAgente = posicionesAgentes[id].getPosicionInicial();
        int resultadoAbsoluto[] = {a[0] + posicionInicialAgente[0], a[1] + posicionInicialAgente[1]};
        System.out.println("Resultado absoluto: " + Arrays.toString(resultadoAbsoluto));
        return matrizCuadros[a[0] + posicionInicialAgente[0]][a[1] + posicionInicialAgente[1]];
    }

    //LISTENER PARA CUANDO SE PULSA EL CLICK DEL RATON. CON ESTE METODO CAMBIAMOS
    //LOS ESTADOS DE pared Y agente DE LAS CASILLAS DE LA MATRIZ
    @Override
    public void itemStateChanged(ItemEvent e) {
        int estado = e.getStateChange();
        if (estado == ItemEvent.SELECTED) {
            if (cantidadAgentes != sliderCantidadAgentes.getValue()) {
                switch (sliderCantidadAgentes.getValue()) {
                    case 4:
                        matrizCuadros[matrizCuadros[1].length - 2][matrizCuadros[1].length - 2].setAgente(true, "src/monstrecaverna/modelo/amogus_OESTE.png");
                        cantidadAgentes = 4;
                    case 3:
                        matrizCuadros[matrizCuadros[1].length - 2][1].setAgente(true, "src/monstrecaverna/modelo/amogus_OESTE.png");
                        if (cantidadAgentes < 3) {
                            cantidadAgentes = 3;
                        }
                    case 2:
                        matrizCuadros[1][matrizCuadros[1].length - 2].setAgente(true, "src/monstrecaverna/modelo/amogus_OESTE.png");
                        if (cantidadAgentes < 2) {
                            cantidadAgentes = 2;
                        }
                    case 1:
                        setAgente(1,1,1);
                }
            }

            repaint();        
            Thread thread = new Thread(control);
            control.setSimulacion(true);
            thread.start();
        } else {
            control.setSimulacion(false);
        }
    }

    private void setAgente(int identificador, int i, int j) {
        matrizCuadros[i][j].setAgente(true, "src/monstrecaverna/modelo/amogus_OESTE.png");
        if (cantidadAgentes < 1) {
            cantidadAgentes = 1;
        }
        int [] pos = {i, j};
        posicionesAgentes[0] = new PosicionAgente(pos);
        setAbismo(i, j, false);
        setMonstruo(i, j, false);
        Agente ag = new Agente(identificador, 1, this);
        control.setAgente(ag);
    }

    public void setAbismo(int i, int j, boolean b) {
        matrizCuadros[i][j].setAbismo(b);
        if (i < matrizCuadros[j].length - 2) {
            matrizCuadros[i + 1][j].setBrisa(b);
        }
        if (j < matrizCuadros[j].length - 2) {
            matrizCuadros[i][j + 1].setBrisa(b);
        }
        if ((i - 1) > 0) {
            matrizCuadros[i - 1][j].setBrisa(b);
        }
        if ((j - 1) > 0) {
            matrizCuadros[i][j - 1].setBrisa(b);
        }
    }

    public void setMonstruo(int i, int j, boolean b) {
        matrizCuadros[i][j].setMonstruo(b);
        if (i < matrizCuadros[j].length - 2) {
            matrizCuadros[i + 1][j].setHedor(b);
        }
        if (j < matrizCuadros[j].length - 2) {
            matrizCuadros[i][j + 1].setHedor(b);
        }
        if ((i - 1) > 0) {
            matrizCuadros[i - 1][j].setHedor(b);
        }
        if ((j - 1) > 0) {
            matrizCuadros[i][j - 1].setHedor(b);
        }
    }
}
