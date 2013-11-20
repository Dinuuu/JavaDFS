// Esta clase representa un "tramo" de un fichero.
// Puede elegir el ID a su criterio (p.e. offset, nº bloque, ...).
// Al ser serializable, además de para la cache, puede usarse en
// las transferencias entre cliente y servidor.

package dfs;
import java.io.*;

public class Bloque implements Serializable {
    private long id;
    private byte[] contenido;
    public Bloque (long i, byte[] c) {
        id = i;
        contenido = c; 
    }
    public long obtenerId() {
        return id;
    }
    public byte [] obtenerContenido() {
        return contenido;
    }
}
