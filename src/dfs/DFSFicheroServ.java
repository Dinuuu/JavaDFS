// Interfaz del API de acceso remoto a un fichero

package dfs;

import java.io.IOException;
import java.rmi.*;

public interface DFSFicheroServ extends Remote {

	public byte[] read(int read) throws RemoteException, IOException;

	public void write(byte[] b) throws RemoteException, IOException;

	public void seek(long p) throws RemoteException, IOException;

	public void close() throws RemoteException, IOException;

}
