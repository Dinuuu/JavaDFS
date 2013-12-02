// Clase de cliente que proporciona acceso al servicio DFS

package dfs;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class DFSCliente {

	DFSServicio serv;
	Cache cache;

	public DFSCliente(int tamBloque, int tamCache)
			throws MalformedURLException, RemoteException, NotBoundException {

		String servidor = System.getenv("SERVIDOR");
		String puerto = System.getenv("PUERTO");
		this.serv = (DFSServicio) Naming.lookup("rmi://" + servidor + ":"
				+ puerto + "/DFS");
		cache = new Cache(tamCache);
	}

	public DFSServicio getServidor() {
		return serv;
	}
}
