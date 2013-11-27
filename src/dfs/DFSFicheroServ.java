// Interfaz del API de acceso remoto a un fichero

package dfs;

import java.io.IOException;
import java.rmi.*;

public interface DFSFicheroServ extends Remote {

	public byte[] read(int read, double usuario) throws RemoteException,
			IOException;

	public void write(byte[] b, double usuario) throws RemoteException,
			IOException;

	public void seek(long p, double usuario) throws RemoteException,
			IOException;

	public void close(double usuario) throws RemoteException, IOException;

	public void añadirUsuario(Double usuario, String modo)
			throws RemoteException;

}
