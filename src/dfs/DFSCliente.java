// Clase de cliente que proporciona acceso al servicio DFS

package dfs;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class DFSCliente {

	DFSServicio serv;
	Map<String, Cache> caches = new HashMap<String, Cache>();
	int tamCache;
	int tamBloque;

	public DFSCliente(int tamBloque, int tamCache)
			throws MalformedURLException, RemoteException, NotBoundException {

		String servidor = System.getenv("SERVIDOR");
		String puerto = System.getenv("PUERTO");
		this.serv = (DFSServicio) Naming.lookup("rmi://" + servidor + ":"
				+ puerto + "/DFS");
		this.tamCache = tamCache;
		this.tamBloque = tamBloque;
	}

	public int getTamCache() {
		return tamCache;
	}

	public int getTamBloque() {
		return tamBloque;
	}

	public DFSServicio getServidor() {
		return serv;
	}

	public DFSFicheroServ open(String nom, String modo, Double usuario)
			throws IOException {
		DFSFicheroServ resp = serv.open(nom, modo, usuario);

		return resp;

	}

	public Cache getCache(String nom) {
		Cache resp = null;
		if (!caches.containsKey(nom)) {
			resp = new Cache(tamCache);
			caches.put(nom, resp);
		} else
			resp = caches.get(nom);
		return resp;
	}
}
