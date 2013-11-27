// Clase de cliente que proporciona acceso al servicio DFS

package dfs;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class DFSCliente {

	DFSServicio serv;

	public DFSCliente(int tamBloque, int tamCache)
			throws MalformedURLException, RemoteException, NotBoundException {

		String servidor = System.getenv("SERVIDOR");
		String puerto = System.getenv("PUERTO");
		this.serv = (DFSServicio) Naming.lookup("rmi://" + servidor + ":"
				+ puerto + "/DFS");
	}

	DFSFicheroServ open(String nombre, String modo, Double usuario)
			throws IOException {

		return serv.open(nombre, modo, usuario);

	}
}
