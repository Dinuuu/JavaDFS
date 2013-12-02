// Clase de servidor que implementa el servicio DFS

package dfs;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class DFSServicioImpl extends UnicastRemoteObject implements DFSServicio {

	private static final long serialVersionUID = 1L;
	private Map<String, DFSFicheroServ> ficheros = new HashMap<String, DFSFicheroServ>();
	private int puerto;

	public DFSServicioImpl(Integer puerto) throws RemoteException {
		this.puerto = puerto;
	}

	@Override
	public DFSFicheroServ open(String nombre, String modo, Double usuario)
			throws IOException {
		DFSFicheroServ fichero = null;

		fichero = new DFSFicheroServImpl(nombre, modo, usuario, this);
		ficheros.put(nombre + usuario.toString(), fichero);
		Naming.rebind(
				"rmi://localhost:" + puerto + "/DFS/" + nombre
						+ usuario.toString(), fichero);

		return fichero;

	}

	@Override
	public void close(String nombre, Double usuario) throws RemoteException {

		ficheros.remove(nombre + usuario.toString());
	}

}
