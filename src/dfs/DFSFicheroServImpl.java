// Clase de servidor que implementa el API de acceso remoto a un fichero

package dfs;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DFSFicheroServImpl extends UnicastRemoteObject implements
		DFSFicheroServ {

	private static final long serialVersionUID = 1L;

	private static final String DFSDir = "DFSDir/";
	String nombre;
	RandomAccessFile fichero;
	Double usuario;
	DFSServicio serv;

	public DFSFicheroServImpl(String nombre, String modo, Double usuario,
			DFSServicio serv) throws IOException {

		this.nombre = nombre;
		this.fichero = new RandomAccessFile(DFSDir + nombre, modo);
		this.usuario = usuario;
		this.serv = serv;
	}

	@Override
	public synchronized byte[] read(int read) throws RemoteException,
			IOException {

		byte[] resp = new byte[read];
		int leidos = fichero.read(resp, 0, resp.length);
		if (leidos == -1)
			return null;
		return resp;

	}

	@Override
	public synchronized void write(byte[] b) throws RemoteException,
			IOException {

		fichero.write(b);

	}

	@Override
	public synchronized void seek(long p) throws RemoteException, IOException {

		fichero.seek(p);
	}

	@Override
	public synchronized void close() throws RemoteException, IOException {

		fichero.close();
		serv.close(this.nombre, this.usuario);

	}
}
