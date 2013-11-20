// Clase de cliente que proporciona el API del servicio DFS

package dfs;

import java.io.*;
import java.rmi.*;


public class DFSFicheroCliente  {
    public DFSFicheroCliente(DFSCliente dfs, String nom, String modo)
      throws RemoteException, IOException, FileNotFoundException {
    }
    public int read(byte[] b) throws RemoteException, IOException {
 	 return 0;
    }
    public void write(byte[] b) throws RemoteException, IOException {
    }
    public void seek(long p) throws RemoteException, IOException {
    }
    public void close() throws RemoteException, IOException {
    }
}
