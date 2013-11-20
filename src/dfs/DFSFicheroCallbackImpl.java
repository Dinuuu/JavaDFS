// Clase de cliente que implementa el servicio de callback de DFS

package dfs;
import java.rmi.*;
import java.rmi.server.*;

public class DFSFicheroCallbackImpl extends UnicastRemoteObject implements DFSFicheroCallback {
    public DFSFicheroCallbackImpl()
      throws RemoteException {

    }
}
