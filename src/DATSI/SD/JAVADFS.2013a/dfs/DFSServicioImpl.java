// Clase de servidor que implementa el servicio DFS

package dfs;

import java.rmi.*;
import java.rmi.server.*;

public class DFSServicioImpl extends UnicastRemoteObject implements DFSServicio {
    public DFSServicioImpl() throws RemoteException {
    }
}
