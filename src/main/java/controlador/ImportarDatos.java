package controlador;


import modelo.CambioModelo;
import modelo.GestorTareas;
import vista.ImplementacionVista;
import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ImportarDatos {
    public void cargarDatos(CambioModelo modelo){
        ObjectInputStream ois = null;
        try {
            try {
                FileInputStream fis = new FileInputStream("AgendaTareas.bin");
                ois = new ObjectInputStream(fis);
                //Actualizamos la nueva base de  datos en el modelo MVC
                GestorTareas nueva = (GestorTareas) ois.readObject();
                modelo.setGestorTareas(nueva);
                modelo.setVista(new ImplementacionVista());
                Accion.gestorTareas = nueva;
            } finally {
                if (ois != null) ois.close();
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No se ha encontrado el fichero AgendaTareas.bin",
                    "Error", JOptionPane.WARNING_MESSAGE);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
