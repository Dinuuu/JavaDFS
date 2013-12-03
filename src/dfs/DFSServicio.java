// Interfaz del servicio DFS

package dfs;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DFSServicio extends Remote {

	DFSFicheroServ open(String nombre, String modo, Double usuario)
			throws IOException;

	void close(String nombre) throws RemoteException;

}
