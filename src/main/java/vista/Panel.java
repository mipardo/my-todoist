package vista;

import controlador.Controlador;
import modelo.GestorTareas;
import modelo.InterrogaModelo;
import modelo.TareaNoExistenteException;
import modelo.tarea.Prioridad;
import modelo.tarea.Tarea;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.LinkedList;

/**
 * En esta clase se implementa el diseño del panel principal de la ventana.
 * He decidido separarlo de la clase ImplementacionVista para que no quede
 * una clase con tanto código.
 * Implementa la interfaz InterrogaVista ya que esta define los métodos
 * que devuelven los datos introducidos por el usuario.
 * */

public class Panel extends JPanel implements InterrogaVista{
    private Controlador controlador;
    private InterrogaModelo modelo;
    private InformaVista vista;
    private ModeloTabla modeloTabla;
    private Tabla tabla;
    private String[] columnas;
    private JTextField jTextFieldTitulo;
    private JTextArea jTextAreaDescripcion;
    private JCheckBox jCheckBoxCompletada;
    private String tipoTarea;
    private String tipoFiltroPrioridad;
    private String tipoFiltroCompletado;
    private JRadioButton jRButtonAlta2;
    private JRadioButton jRButtonNormal2;
    private JRadioButton jRButtonBaja2;


    public Panel(){
        super();
        creaPanel();
    }

    public void setModelo(InterrogaModelo modelo) { this.modelo = modelo; }

    public void setControlador(Controlador controlador) { this.controlador = controlador; }

    public void setVista(InformaVista vista) { this.vista = vista; }

    private void creaPanel(){
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        //Escuchador de los botones:
        ActionListener escuchadorBoton = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String comando = actionEvent.getActionCommand();
                try {
                    switch (comando){
                        case "NUEVO":
                            nuevo();
                            break;
                        case "BORRA":
                            borrar();
                            break;
                        case "ACTUALIZA":
                            actualiza();
                            break;
                        default:
                            aplicarFiltros();
                    }
                } catch (TareaNoExistenteException | IndexOutOfBoundsException exception){
                    vista.accionDenegada("No se ha seleccionado ninguna tarea de la tabla");
                }
            }
        };

        //SECCION FILTROS

        JPanel jPanelSeccionArriba = new JPanel();
        jPanelSeccionArriba.setLayout(new BoxLayout(jPanelSeccionArriba, BoxLayout.X_AXIS));
        jPanelSeccionArriba.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));



        //Prioridad
        JRadioButton jRButtonAlta = new JRadioButton("Alta");
        jRButtonAlta.setActionCommand("FILTRO ALTA");
        JRadioButton jRButtonNormal = new JRadioButton("Normal");
        jRButtonNormal.setActionCommand("FILTRO NORMAL");
        JRadioButton jRButtonBaja = new JRadioButton("Baja");
        jRButtonBaja.setActionCommand("FILTRO BAJA");
        JRadioButton jRButtonTodas = new JRadioButton("Todas", true);
        jRButtonTodas.setActionCommand("FILTRO TODAS");

        ActionListener escuchadorTipoFiltroPrioridad = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                tipoFiltroPrioridad = actionEvent.getActionCommand();
            }
        };
        jRButtonAlta.addActionListener(escuchadorTipoFiltroPrioridad);
        jRButtonNormal.addActionListener(escuchadorTipoFiltroPrioridad);
        jRButtonBaja.addActionListener(escuchadorTipoFiltroPrioridad);
        jRButtonTodas.addActionListener(escuchadorTipoFiltroPrioridad);

        JPanel jPanelPrioridad = new JPanel();
        jPanelPrioridad.setLayout(new GridLayout(5, 1));
        jPanelPrioridad.add(new JLabel("Prioridad"));
        jPanelPrioridad.add(jRButtonAlta);
        jPanelPrioridad.add(jRButtonNormal);
        jPanelPrioridad.add(jRButtonBaja);
        jPanelPrioridad.add(jRButtonTodas);

        ButtonGroup grupoPrioridad = new ButtonGroup();
        grupoPrioridad.add(jRButtonAlta);
        grupoPrioridad.add(jRButtonNormal);
        grupoPrioridad.add(jRButtonBaja);
        grupoPrioridad.add(jRButtonTodas);

        jPanelSeccionArriba.add(jPanelPrioridad);

        //Completadas
        JRadioButton jRButtonCompletada = new JRadioButton("Completada");
        jRButtonCompletada.setActionCommand("FILTRO COMPLETADAS");
        JRadioButton jRButtonNoCompletada = new JRadioButton("No completada");
        jRButtonNoCompletada.setActionCommand("FILTRO NO COMPLETADAS");
        JRadioButton jRButtonTodasB = new JRadioButton("Todas", true);
        jRButtonTodasB.setActionCommand("FILTRO TODAS");

        ActionListener escuchadorFiltroCompletado = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                tipoFiltroCompletado = actionEvent.getActionCommand();
            }
        };
        jRButtonCompletada.addActionListener(escuchadorFiltroCompletado);
        jRButtonNoCompletada.addActionListener(escuchadorFiltroCompletado);
        jRButtonTodasB.addActionListener(escuchadorFiltroCompletado);

        JPanel jPanelCompletadas = new JPanel();
        jPanelCompletadas.setLayout(new GridLayout(4, 1));

        jPanelCompletadas.add(new JLabel("Completadas"));
        jPanelCompletadas.add(jRButtonCompletada);
        jPanelCompletadas.add(jRButtonNoCompletada);
        jPanelCompletadas.add(jRButtonTodasB);

        ButtonGroup grupoCompletadas = new ButtonGroup();
        grupoCompletadas.add(jRButtonCompletada);
        grupoCompletadas.add(jRButtonNoCompletada);
        grupoCompletadas.add(jRButtonTodasB);


        jPanelSeccionArriba.add(jPanelCompletadas);

        //Botón filtra:
        JButton jButtonAplicarFiltros = new JButton("Aplicar filtros");
        jButtonAplicarFiltros.setActionCommand("APLICARFILTROS");
        jPanelSeccionArriba.add(jButtonAplicarFiltros);

        this.add(jPanelSeccionArriba);


        //SECCION LISTA DE TAREAS

        JPanel jPanelSeccionMedia = new JPanel();
        jPanelSeccionMedia.setLayout(new BoxLayout(jPanelSeccionMedia, BoxLayout.PAGE_AXIS));
        jPanelSeccionMedia.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel nombreSeccionListaTareas = new JLabel("Lista de tareas");
        jPanelSeccionMedia.add(nombreSeccionListaTareas);

        columnas = new String[]{"Tarea", "Descripcion", "Terminada", "Prioridad"};
        Collection<Tarea> tareas = new LinkedList<>();
        modeloTabla = new ModeloTabla(columnas, tareas);
        tabla = new Tabla(modeloTabla);

        //crea el escuchador de la tabla
        ListSelectionListener escuchadorTabla = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //si se ejecuta este metodo porque se da al boton de actualizar o borrar
                //y no se selecciona ninguna fila
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if(lsm.getMinSelectionIndex() == -1) return;

                if (!e.getValueIsAdjusting()) {
                    int fila = tabla.convertRowIndexToModel(tabla.getSelectedRow());
                    int codTarea = (Integer) modeloTabla.getValueAt(fila, -1);
                    detallesTarea(codTarea);
                }
            }
        };

        jPanelSeccionMedia.add(tabla);
        Scroll scroll = new Scroll();
        jPanelSeccionMedia = scroll.ejecuta(tabla, jPanelSeccionMedia, escuchadorTabla);
        this.add(jPanelSeccionMedia);


        //SECCION DETALLE DE LA TAREA
        JPanel jPanelseccionBaja = new JPanel();
        jPanelseccionBaja.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        jPanelseccionBaja.setLayout(new BoxLayout(jPanelseccionBaja, BoxLayout.Y_AXIS));

        //Titulo:
        JLabel jLabelTituloSeccionBaja = new JLabel("Detalle de la tarea ");
        jPanelseccionBaja.add(jLabelTituloSeccionBaja);
        JPanel titulo = new JPanel();
        titulo.setLayout(new BoxLayout(titulo, BoxLayout.X_AXIS));

        titulo.add(new JLabel("Título: "));
        jTextFieldTitulo = new JTextField();
        titulo.add(jTextFieldTitulo);

        jPanelseccionBaja.add(titulo);

        //Descripcion:
        JPanel descripcion = new JPanel();
        descripcion.setLayout(new BoxLayout(descripcion, BoxLayout.X_AXIS));
        descripcion.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));

        descripcion.add(new JLabel("Descripcion: "));
        jTextAreaDescripcion = new JTextArea(3,1);
        descripcion.add(jTextAreaDescripcion);

        jPanelseccionBaja.add(descripcion);

        //Estado
        jCheckBoxCompletada = new JCheckBox("Completada");
        jPanelseccionBaja.add(jCheckBoxCompletada);

        //Prioridad
        JPanel prioridad = new JPanel();
        prioridad.setLayout(new BoxLayout(prioridad, BoxLayout.X_AXIS));
        prioridad.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        JLabel jLabelTituloPrioridad = new JLabel("Prioridad: ");
        prioridad.add(jLabelTituloPrioridad);

        jRButtonAlta2 = new JRadioButton("Alta");
        jRButtonAlta2.setActionCommand("ALTA");
        jRButtonNormal2 = new JRadioButton("Normal");
        jRButtonNormal2.setActionCommand("NORMAL");
        jRButtonBaja2 = new JRadioButton("Baja", true);
        jRButtonBaja2.setActionCommand("BAJA");

        ActionListener escuchadorTipoTarea = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                tipoTarea = actionEvent.getActionCommand();
            }
        };

        jRButtonAlta2.addActionListener(escuchadorTipoTarea);
        jRButtonNormal2.addActionListener(escuchadorTipoTarea);
        jRButtonBaja2.addActionListener(escuchadorTipoTarea);


        prioridad.add(jRButtonAlta2);
        prioridad.add(jRButtonNormal2);
        prioridad.add(jRButtonBaja2);

        ButtonGroup grupoPrioridad2 = new ButtonGroup();
        grupoPrioridad2.add(jRButtonAlta2);
        grupoPrioridad2.add(jRButtonNormal2);
        grupoPrioridad2.add(jRButtonBaja2);

        jPanelseccionBaja.add(prioridad);

        //Botones:
        JPanel botones = new JPanel();
        botones.setLayout(new BoxLayout(botones, BoxLayout.X_AXIS));

        JButton jButtonNuevo = new JButton("Nuevo");
        jButtonNuevo.setActionCommand("NUEVO");
        JButton jButtonActualiza = new JButton("Actualiza");
        jButtonActualiza.setActionCommand("ACTUALIZA");
        JButton jButtonBorra = new JButton("Borra");
        jButtonBorra.setActionCommand("BORRA");


        jButtonAplicarFiltros.addActionListener(escuchadorBoton);
        jButtonNuevo.addActionListener(escuchadorBoton);
        jButtonBorra.addActionListener(escuchadorBoton);
        jButtonActualiza.addActionListener(escuchadorBoton);

        botones.add(jButtonNuevo);
        botones.add(jButtonActualiza);
        botones.add(jButtonBorra);

        jPanelseccionBaja.add(botones);

        this.add(jPanelseccionBaja);
    }

    private void borrar() throws TareaNoExistenteException {
        controlador.borrarTarea();
        cargarDatosTabla();
        aplicarFiltros();
    }

    private void nuevo() {
        controlador.anadirTarea();
        cargarDatosTabla();
        aplicarFiltros();
    }

    private void aplicarFiltros(){
        controlador.aplicarFiltros();
    }

    private void actualiza() throws TareaNoExistenteException{
        controlador.actualizarTarea();
        cargarDatosTabla();
        aplicarFiltros();
    }

    private void detallesTarea(int codigoTarea){
        Tarea tareaSeleccionada = modelo.getGestorTareas().devolverTarea(codigoTarea);
        jTextFieldTitulo.setText(tareaSeleccionada.getTitulo());
        jTextAreaDescripcion.setText(tareaSeleccionada.getDescripcion());
        //Modificamos los botones segun estado de la tarea seleccionada:
        jCheckBoxCompletada.setSelected(tareaSeleccionada.completada());
        //Modificamos los botones segun prioridad
        if(tareaSeleccionada.getPrioridad().equals(Prioridad.ALTA)) jRButtonAlta2.setSelected(true);
        else if(tareaSeleccionada.getPrioridad().equals(Prioridad.NORMAL)) jRButtonNormal2.setSelected(true);
        else jRButtonBaja2.setSelected(true);
    }

    public void cargarDatosTabla(){
        String[] columnas = {"Tarea","Descripcion", "Terminada", "Prioridad"};
        GestorTareas gestorTareas = modelo.getGestorTareas();
        Collection<Tarea> tareas = gestorTareas.devolverTareas();
        tabla.setModel(modeloTabla = new ModeloTabla(columnas, tareas));
    }


    @Override
    public void mostrarTareasFiltradas(Collection<Tarea> tareasFiltradas){
        tabla.setModel(modeloTabla = new ModeloTabla(columnas, tareasFiltradas));
    }

    @Override
    public Panel getPanel() {
        return this;
    }

    @Override
    public String getFiltroPrioridad() {
        if(tipoFiltroPrioridad != null ) return tipoFiltroPrioridad;
        return "TODAS";
    }

    @Override
    public String getFiltroCompletado() {
        if(tipoFiltroCompletado != null) return tipoFiltroCompletado;
        else return "TODAS";
    }

    @Override
    public String getTitulo() {
        return jTextFieldTitulo.getText();
    }

    @Override
    public String getDescripcion() {
        return jTextAreaDescripcion.getText();
    }

    @Override
    public String getPrioridad() {
        if(tipoTarea != null) return tipoTarea;
        else return "BAJA";
    }

    @Override
    public boolean getCompletado() {
        return jCheckBoxCompletada.isSelected();
    }

    @Override
    public int getCodigo() {
        int fila = tabla.convertRowIndexToModel(tabla.getSelectedRow());
        //Se indica la columna -1 para que se obtenga el codigo de la tarea
        int codigoTarea = (int) modeloTabla.getValueAt(fila, -1);
        return codigoTarea;
    }

}
