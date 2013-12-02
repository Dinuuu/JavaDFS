// Clase de cliente que proporciona el API del servicio DFS

package dfs;

import java.io.*;
import java.rmi.*;

public class DFSFicheroCliente {

	DFSFicheroServ fich;
	Double usuario;

	public DFSFicheroCliente(DFSCliente dfs, String nom, String modo)
			throws RemoteException, IOException, FileNotFoundException {

		usuario = Math.random();
		fich = dfs.getServidor().open(nom, modo, usuario);

	}

	public int read(byte[] b) throws RemoteException, IOException {

		byte[] resp = fich.read(b.length);
		if (resp == null)
			return -1;
		System.arraycopy(resp, 0, b, 0, resp.length);
		return b.length;
	}

	public void write(byte[] b) throws RemoteException, IOException {

		fich.write(b);

	}

	public void seek(long p) throws RemoteException, IOException {

		fich.seek(p);
	}

	public void close() throws RemoteException, IOException {

		fich.close();
	}
}
