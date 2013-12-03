// Interfaz del servicio de callback de DFS

package dfs;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DFSFicheroCallback extends Remote {

	public void invalidarCache() throws RemoteException, IOException;

	public void usarCache() throws RemoteException;
}
