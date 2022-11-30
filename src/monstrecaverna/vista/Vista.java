package monstrecaverna.vista;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JButton;
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
public class Vista extends JFrame implements ChangeListener, ComponentListener, ItemListener, ActionListener {

    JFrame ventanaMapa = new JFrame("Mapa del agente");

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
    private final JLabel obstaculos = new JLabel("Obstaculos");
    RadioButtonConImagen posicionarAbismo, posicionarMonstruo, posicionarTesoro;
    private ButtonGroup grupoBotonesPosicionar;

    //Opciones de inicio para los agentes
    private final JPanel opcionesAgente = new JPanel();
    private JLabel cantidadAgente, velocidadAgente;
    private JTextField cantidadAgenteText, velocidadAgenteText;
    final JSlider sliderCantidadAgentes = new JSlider(JSlider.HORIZONTAL, 1, 4, 1);
    final JSlider sliderVelocidadAgentes = new JSlider(JSlider.HORIZONTAL, 100, 1000, 250);
    public JToggleButton iniciar;
    public JButton verMapa = new JButton("Ver mapa");

    //Opciones para seleccionar el modo de juego
    private final JPanel opcionesGamemode = new JPanel();
    private final JLabel gamemodes = new JLabel("Modo de juego");
    RadioButtonConImagen busquedaRapida, busquedaAvariciosa;
    private ButtonGroup grupoBotonesGamemode;

    //Información general de la vista
    boolean abismo = false, monstruo = false, tesoro = false;
    private int cantidadAgentes = 0, cantidadTesoro = 0, velocidad = 250;
    private PosicionAgente[] posicionesAgentes = new PosicionAgente[4];
    public boolean simulacion;
    private Thread[] controlThreads = new Thread[4];
    private Control[] controles = new Control[4];
    private int posicionLlegada = 0;
    private boolean avaricioso;
    //private int posicionAgente[] = new int[2];
    //private PosicionAgente posicionesInicialesAgentes[] = new PosicionAgente[4];
    //private int posicionInicialAgente[] = new int[2];

    //CONSTRUCTOR DE VISTA
    public Vista(String nombre) {

        super(nombre);
        for (int i = 0; i < controlThreads.length; i++) {
            controles[i] = new Control(this);
        }
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

        ventanaMapa.setPreferredSize(new Dimension((int) 512, (int) 532));
        ventanaMapa.pack();
        ventanaMapa.setLocationRelativeTo(null);
        ventanaMapa.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

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

        obstaculos.setFont(new Font("calibri", Font.BOLD, 30));

        JPanel obstaculosPanel = new JPanel(new FlowLayout());
        obstaculosPanel.add(obstaculos);
        posicionarAbismo = new RadioButtonConImagen(new ImageIcon("src/monstrecaverna/modelo/abismo.png"));
        posicionarMonstruo = new RadioButtonConImagen(new ImageIcon("src/monstrecaverna/modelo/monstruo.png"));
        posicionarTesoro = new RadioButtonConImagen(new ImageIcon("src/monstrecaverna/modelo/tesoro.png"));

        grupoBotonesPosicionar = new ButtonGroup();
        posicionarAbismo.addToButtonGroup(grupoBotonesPosicionar);
        posicionarMonstruo.addToButtonGroup(grupoBotonesPosicionar);
        posicionarTesoro.addToButtonGroup(grupoBotonesPosicionar);

        opcionesObstaculo.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        opcionesObstaculo.add(obstaculosPanel, c);
        c.gridy = 1;
        opcionesObstaculo.add(posicionarAbismo, c);
        c.gridy = 2;
        opcionesObstaculo.add(posicionarMonstruo, c);
        c.gridy = 3;
        opcionesObstaculo.add(posicionarTesoro, c);

        return opcionesObstaculo;
    }

    private JPanel panelOpcionesAgente() {

        opcionesAgente.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.MatteBorder(null),
                javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED)));

        JPanel texto = new JPanel(new FlowLayout());
        cantidadAgente = new JLabel("Agentes en la cueva: ");
        cantidadAgente.setFont(new Font("calibri", Font.BOLD, 30));
        cantidadAgenteText = new JTextField("1");
        cantidadAgenteText.setFont(new Font("calibri", Font.BOLD, 30));
        cantidadAgenteText.setEditable(false);

        cantidadAgenteText.setText(String.valueOf(sliderCantidadAgentes.getValue()));
        sliderCantidadAgentes.addChangeListener(this);

        iniciar = new JToggleButton("Iniciar");
        iniciar.setFont(new Font("calibri", Font.BOLD, 30));
        iniciar.addItemListener(this);

        verMapa.addActionListener(this);

        JPanel texto_2 = new JPanel(new FlowLayout());
        velocidadAgente = new JLabel("Sleep agente: ");
        velocidadAgente.setFont(new Font("calibri", Font.BOLD, 20));
        velocidadAgenteText = new JTextField("250ms");
        velocidadAgenteText.setColumns(4);
        velocidadAgenteText.setFont(new Font("calibri", Font.BOLD, 20));
        velocidadAgenteText.setEditable(false);

        velocidadAgenteText.setText(String.valueOf(sliderVelocidadAgentes.getValue()) + "ms");
        sliderVelocidadAgentes.addChangeListener(this);

        opcionesAgente.setLayout(new GridLayout(6, 1));

        texto.add(cantidadAgente);
        texto.add(cantidadAgenteText);
        opcionesAgente.add(texto);
        opcionesAgente.add(sliderCantidadAgentes);
        opcionesAgente.add(iniciar);
        opcionesAgente.add(verMapa);
        texto_2.add(velocidadAgente);
        texto_2.add(velocidadAgenteText);
        opcionesAgente.add(texto_2);
        opcionesAgente.add(sliderVelocidadAgentes);

        return opcionesAgente;
    }

    private JPanel panelOpcionesJuego() {

        opcionesGamemode.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.MatteBorder(null),
                javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED)));

        gamemodes.setFont(new Font("calibri", Font.BOLD, 30));
        JPanel gamemodesPanel = new JPanel(new FlowLayout());
        gamemodesPanel.add(gamemodes);
        busquedaRapida = new RadioButtonConImagen(new ImageIcon("src/monstrecaverna/modelo/tesoro.png"));
        busquedaRapida.setSelected(true);
        busquedaAvariciosa = new RadioButtonConImagen(new ImageIcon("src/monstrecaverna/modelo/tesoros.png"));

        grupoBotonesGamemode = new ButtonGroup();
        busquedaRapida.addToButtonGroup(grupoBotonesGamemode);
        busquedaAvariciosa.addToButtonGroup(grupoBotonesGamemode);

        opcionesGamemode.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.gridheight = 1;
        opcionesGamemode.add(gamemodesPanel, c);
        c.gridy = 1;
        c.gridwidth = 1;
        opcionesGamemode.add(busquedaRapida, c);
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        opcionesGamemode.add(busquedaAvariciosa, c);

        return opcionesGamemode;
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
        constraints.gridwidth = 4;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridheight = 1;
        opciones.add(panelOpcionesRecinto(), constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridheight = 3;
        opciones.add(panelOpcionesObstaculo(), constraints);
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridheight = 1;
        opciones.add(panelOpcionesAgente(), constraints);
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridheight = 1;
        opciones.add(panelOpcionesJuego(), constraints);

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

    public synchronized void moverAgente(MovimientoAgenteWrapper movimiento) {
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
        ventanaMapa.repaint();
        repaint();
    }

    public synchronized boolean cogerTesoro(int identificador, Cuadro casilla) {
        //Cuadro casilla = getCasilla(identificador, posicionesAgentes[identificador].getPosicionActual());
        boolean[] estado = casilla.getEstado();
        if (estado[2]) {
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

    public int getCantidadTesoro() {
        return cantidadTesoro;
    }

    public boolean getAvaricioso() {
        return busquedaAvariciosa.getIsSelected();
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
        } else if (!src.getValueIsAdjusting() && e.getSource() == sliderVelocidadAgentes) {
            velocidadAgenteText.setText(String.valueOf(src.getValue()) + "ms");
            System.out.println(velocidadAgenteText.getText());
            velocidad = src.getValue();
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

    @Override
    public void itemStateChanged(ItemEvent e) {
        int estado = e.getStateChange();
        if (estado == ItemEvent.SELECTED) {
            if (cantidadAgentes != sliderCantidadAgentes.getValue()) {
                switch (sliderCantidadAgentes.getValue()) {
                    case 1:
                        if (cantidadAgentes < 1) {
                            controles[3].stop();
                            controles[2].stop();
                            controles[1].stop();
                            cantidadAgentes = 1;
                        }break;
                    case 2:
                        if (cantidadAgentes < 2) {
                            controles[3].stop();
                            controles[2].stop();
                            cantidadAgentes = 2;
                        }break;
                    case 3:
                        if (cantidadAgentes < 3) {
                            controles[3].stop();
                            cantidadAgentes = 3;
                        }break;
                    case 4:
                        if (cantidadAgentes < 4) {
                            cantidadAgentes = 4;
                        }break;
                    //setAgente(0, 1, 1);
                }
            

            for (int i = 0; i < sliderCantidadAgentes.getValue(); i++) {
                switch (i) {
                    case 0:
                        setAgente(i, 1, 1);
                        break;
                    case 1:
                        setAgente(i, 1, matrizCuadros[1].length - 2);
                        break;
                    case 2:
                        setAgente(i, matrizCuadros[1].length - 2, 1);
                        break;
                    case 3:
                        setAgente(i, matrizCuadros[1].length - 2, matrizCuadros[1].length - 2);
                        break;
                    default:
                        break;
                }
            }
            }
            System.out.println("cantidad agentes: " + cantidadAgentes);

            repaint();
            //Thread thread = new Thread(control);
            simulacion = true;
            int i = 0;
            for (Control control : controles) {
                System.out.println("Start del agente " + i);
                //thread.start();
                Thread thread = new Thread(control);
                controlThreads[i] = thread;
                thread.start();
                i++;
            }
            //control.setSimulacion(true);
            //thread.start();
        } else {
            //control.setSimulacion(false);
            simulacion = false;
        }
    }

    private void setAgente(int identificador, int i, int j) {
        matrizCuadros[i][j].setAgente(true, "src/monstrecaverna/modelo/amogus_OESTE.png");
        if (cantidadAgentes < 1) {
            cantidadAgentes = 1;
        }
        int[] pos = {i, j};
        posicionesAgentes[identificador] = new PosicionAgente(pos);
        if (matrizCuadros[i][j].isAbismo() || matrizCuadros[i][j].isMonstruo()) {
            setAbismo(i, j, false);
            setMonstruo(i, j, false);
        }
        System.out.println("Agente creado " + identificador);
        Agente ag = new Agente(identificador, 1, this);
        controles[identificador].setAgente(ag);
        ventanaMapa.add(ag.getMapaAgente());
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

    public int getVelocidad() {
        return velocidad;
    }
    
    public synchronized void salir(int identificador, int tesoros){
        int[] posicionInicial = posicionesAgentes[identificador].getPosicionInicial();
        matrizCuadros[posicionInicial[0]][posicionInicial[1]].setAgente(false, "");
        if(getAvaricioso()){
            //poner el número de tesoros en la casilla de salida del agente
        } else {
            if(tesoros == 0){
                //un cerapio por lento
            } else{
                posicionLlegada += 1;
                //poner el orden de llegada
            }
        }
        repaint();
    }        

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == verMapa) {
            ventanaMapa.setVisible(true);
        }
    }
}
