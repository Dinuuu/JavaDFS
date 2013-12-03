// Interfaz del API de acceso remoto a un fichero

package dfs;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DFSFicheroServ extends Remote {

	public byte[] read(int read, Double usuario) throws RemoteException,
			IOException;

	public void write(byte[] b, Double usuario) throws RemoteException,
			IOException;

	public void seek(long p, Double usuario) throws RemoteException,
			IOException;

	public void close(Double usuario) throws RemoteException, IOException;

	public long lastModified() throws RemoteException;

	public void añadirUsuario(Double usuario, String modo,
			DFSFicheroCallback usaCache) throws RemoteException, IOException;

	public boolean hayUsuarios() throws RemoteException;

	public void eliminarUsuario(Double usuario) throws RemoteException;

	public void agregarCacheCliente(DFSFicheroCallback usaCache)
			throws RemoteException;

}
