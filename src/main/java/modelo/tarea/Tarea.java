package modelo.tarea;

import java.io.Serializable;

public class Tarea implements Serializable {
    private int codigo;
    private String titulo;
    private String descripcion;
    private Prioridad prioridad;
    protected boolean completada;

    public Tarea(){ super(); }

    public Tarea(String titulo, String descripcion, Prioridad prioridad, boolean completada){
        this.codigo = this.hashCode();
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.completada = completada;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getTitulo(){ return titulo;}

    public void setTitulo(String titulo){ this.titulo = titulo; }

    public String getDescripcion(){ return descripcion;}

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Prioridad getPrioridad(){ return prioridad; }

    public void setPrioridad(Prioridad prioridad) { this.prioridad = prioridad; }

    public boolean completada(){ return completada; }

    public void setCompletada(boolean completada) { this.completada = completada; }

    @Override
    public String toString() {
        return  titulo + " (" + descripcion + ") " + prioridad + " prioridad.  " + completada + "\n";
    }
}
