// Clase de servidor que implementa el servicio DFS

package dfs;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class DFSServicioImpl extends UnicastRemoteObject implements DFSServicio {

	private static final long serialVersionUID = 1L;
	private Map<String, DFSFicheroServ> ficheros = new HashMap<String, DFSFicheroServ>();

	public DFSServicioImpl() throws RemoteException {
	}

	@Override
	public synchronized DFSFicheroServ open(String nombre, String modo,
			Double usuario) throws IOException {
		DFSFicheroServ fichero = null;

		if (ficheros.containsKey(nombre)) {
			fichero = ficheros.get(nombre);
		} else {

			fichero = new DFSFicheroServImpl(nombre, modo, this);
			ficheros.put(nombre, fichero);
		}

		return fichero;

	}

	@Override
	public synchronized void close(String nombre) throws RemoteException {

		ficheros.remove(nombre);
	}

}
