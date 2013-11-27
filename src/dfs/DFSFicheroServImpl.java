// Clase de servidor que implementa el API de acceso remoto a un fichero

package dfs;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class DFSFicheroServImpl extends UnicastRemoteObject implements
		DFSFicheroServ {

	private static final long serialVersionUID = 1L;

	private static final String DFSDir = "DFSDir/";

	RandomAccessFile fichero;

	Map<Double, Usuario> posiciones = new HashMap<Double, Usuario>();

	public DFSFicheroServImpl(String nombre, String modo, Double usuario)
			throws IOException {

		this.fichero = new RandomAccessFile(DFSDir + nombre, modo);
		posiciones.put(usuario, new Usuario(fichero.getFilePointer(), modo));

	}

	@Override
	public byte[] read(int read, double usuario) throws RemoteException,
			IOException {

		byte[] resp = new byte[read];
		Usuario usu = posiciones.get(usuario);
		if (usu.getModo().contains("r")) {
			fichero.seek(usu.getPuntero());
			fichero.read(resp, 0, resp.length);
			usu.setPuntero(fichero.getFilePointer());
			return resp;
		} else
			throw new IOException();
	}

	@Override
	public void write(byte[] b, double usuario) throws RemoteException,
			IOException {

		Usuario usu = posiciones.get(usuario);
		if (usu.getModo().contains("w")) {
			fichero.seek(usu.getPuntero());
			fichero.write(b);
			usu.setPuntero(fichero.getFilePointer());
		} else
			throw new IOException();

	}

	@Override
	public void seek(long p, double usuario) throws RemoteException,
			IOException {

		Usuario usu = posiciones.get(usuario);
		usu.setPuntero(p);

	}

	@Override
	public void close(double usuario) throws RemoteException, IOException {

		posiciones.remove(usuario);
		if (posiciones.isEmpty())
			fichero.close();
	}

	@Override
	public void añadirUsuario(Double usuario, String modo)
			throws RemoteException {
		posiciones.put(usuario, new Usuario(new Long(0), modo));

	}

	private class Usuario {
		private long puntero;
		private String modo;

		public Usuario(long puntero, String modo) {
			this.puntero = puntero;
			this.modo = modo;
		}

		long getPuntero() {
			return this.puntero;
		}

		String getModo() {
			return this.modo;
		}

		void setPuntero(long p) {
			this.puntero = p;
		}
	}
}
