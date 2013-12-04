// Clase de servidor que implementa el API de acceso remoto a un fichero

package dfs;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class DFSFicheroServImpl extends UnicastRemoteObject implements
		DFSFicheroServ {

	private static final long serialVersionUID = 1L;

	private static final String DFSDir = "DFSDir/";
	String nombre;
	RandomAccessFile fichero;
	List<Double> usuariosRead = new ArrayList<Double>();
	List<Double> usuariosWrite = new ArrayList<Double>();

	List<DFSFicheroCallback> usanCache = new ArrayList<DFSFicheroCallback>();
	DFSServicio serv;

	public DFSFicheroServImpl(String nombre, String modo, DFSServicio serv)
			throws IOException {

		File fichero;
		if (!modo.contains("w")) {
			fichero = new File(DFSDir + nombre);
			if (!fichero.exists())
				throw new IOException();
		}
		this.nombre = nombre;
		this.fichero = new RandomAccessFile(DFSDir + nombre, "rw");
		this.serv = serv;
	}

	public void añadirUsuario(Double usuario, String modo,
			DFSFicheroCallback callback) throws IOException {

		if (!modo.contains("w")) {
			if (usuariosWrite.isEmpty()) {
				callback.usarCache();
				agregarCacheCliente(callback);
			} else {
				for (DFSFicheroCallback call : usanCache) {
					call.invalidarCache();
				}
				usanCache.clear();
				callback.invalidarCache();
			}
			usuariosRead.add(usuario);
		} else {
			if (usuariosWrite.isEmpty() && usuariosRead.isEmpty()) {
				callback.usarCache();
				agregarCacheCliente(callback);
			} else {

				for (DFSFicheroCallback call : usanCache) {
					call.invalidarCache();
				}
				usanCache.clear();
				callback.invalidarCache();
			}
			usuariosWrite.add(usuario);
		}

	}

	@Override
	public synchronized byte[] read(int read, Double usuario)
			throws RemoteException, IOException {

		byte[] resp = new byte[read];
		int leidos = fichero.read(resp, 0, resp.length);
		if (leidos == -1)
			return null;

		return resp;

	}

	@Override
	public synchronized void write(byte[] b, Double usuario)
			throws RemoteException, IOException {

		if (usuariosWrite.contains(usuario))
			fichero.write(b);
		else
			throw new IOException();

	}

	@Override
	public synchronized void seek(long p, Double usuario)
			throws RemoteException, IOException {

		fichero.seek(p);
	}

	public synchronized void eliminarUsuario(Double usuario)
			throws RemoteException {
		if (usuariosWrite.contains(usuario))
			usuariosWrite.remove(usuario);
		else
			usuariosRead.remove(usuario);
	}

	public synchronized boolean hayUsuarios() throws RemoteException {

		return !usuariosRead.isEmpty() || !usuariosWrite.isEmpty();
	}

	@Override
	public synchronized void close(Double usuario) throws RemoteException,
			IOException {
		eliminarUsuario(usuario);
		if (!hayUsuarios()) {
			fichero.close();
			serv.close(this.nombre);
		}
	}

	@Override
	public synchronized long lastModified() throws RemoteException {
		File fichero = new File(DFSDir + nombre);
		return fichero.lastModified();
	}

	@Override
	public void agregarCacheCliente(DFSFicheroCallback usaCache)
			throws RemoteException {

		this.usanCache.add(usaCache);

	}

}
