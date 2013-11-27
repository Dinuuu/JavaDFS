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
		fich = dfs.open(nom, modo, usuario);

	}

	public int read(byte[] b) throws RemoteException, IOException {

		b = fich.read(b.length, usuario);
		return b.length;
	}

	public void write(byte[] b) throws RemoteException, IOException {

		fich.write(b, usuario);

	}

	public void seek(long p) throws RemoteException, IOException {

		fich.seek(p, usuario);
	}

	public void close() throws RemoteException, IOException {

		fich.close(usuario);
	}
}
