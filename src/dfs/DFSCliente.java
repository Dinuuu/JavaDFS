// Clase de cliente que proporciona acceso al servicio DFS

package dfs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;

public class DFSCliente {

	DFSServicio serv;

	public DFSCliente(int tamBloque, int tamCache) {

		// TODO BUSCAR EN EL SERVIDOR EL SERV
	}

	DFSFicheroServ open(String nombre, String modo, Double usuario)
			throws IOException {

		return serv.open(nombre, modo, usuario);

	}
}
