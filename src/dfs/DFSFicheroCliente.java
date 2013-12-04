// Clase de cliente que proporciona el API del servicio DFS

package dfs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;

public class DFSFicheroCliente {

	private DFSFicheroServ fich;
	private Double usuario;
	private String modo;
	private long puntero = 0;
	private Cache cache;
	private int tamBloque;
	private boolean abierto;
	private DFSFicheroCallback usoCache;
	private DFSCliente dfs;
	private String nombre;
	private boolean usarCache;

	public DFSFicheroCliente(DFSCliente dfs, String nom, String modo)
			throws RemoteException, IOException, FileNotFoundException {

		this.dfs = dfs;
		this.usuario = Math.random();
		this.nombre = nom;
		this.tamBloque = dfs.getTamBloque();
		this.modo = modo;
		this.abierto = true;
		this.usoCache = new DFSFicheroCallbackImpl(this);
		this.fich = dfs.open(nom, modo, usuario);
		this.fich.añadirUsuario(usuario, modo, usoCache);

	}

	public synchronized void usarCache() throws RemoteException {
		this.cache = this.dfs.getCache(nombre);
		if (cache.obtenerFecha() < fich.lastModified()) {
			cache.vaciar();
		}
		usarCache = true;
	}

	public synchronized void invalidarCache() throws RemoteException,
			IOException {
		if (cache != null) {
			if (modo.contains("w"))
				volcarCache();

			usarCache = false;
		}
	}

	public int read(byte[] b) throws RemoteException, IOException {

		if (!estaAbierto())
			throw new IOException();
		int resp;
		if (usarCache)
			resp = readConCache(b);
		else
			resp = readSinCache(b);

		if (resp != -1)
			incrementarPuntero(resp);

		return resp;

	}

	private int readSinCache(byte[] b) throws RemoteException, IOException {

		fich.seek(puntero, usuario);
		byte[] resp = fich.read(b.length, usuario);
		if (resp == null)
			return -1;

		System.arraycopy(resp, 0, b, 0, resp.length);
		return b.length;

	}

	private int readConCache(byte[] b) throws RemoteException, IOException {

		int cantBloques = b.length / tamBloque;
		int bloqueInicial = (int) (puntero / tamBloque);
		int leidos = 0;
		int i = 0;
		Bloque bloque;
		for (; i < cantBloques && leidos != -1; i++) {
			byte leido[] = new byte[tamBloque];
			if ((bloque = cache.getBloque(bloqueInicial + i)) == null) {
				leidos = readInter(leido, puntero + (i * tamBloque));
				if (leidos != -1) {
					bloque = new Bloque((bloqueInicial + i), leido);
					Bloque aux = cache.putBloque(bloque);
					if (aux != null && cache.preguntarYDesactivarMod(aux)) {
						writeInter(aux.obtenerContenido(), aux.obtenerId()
								* tamBloque);
					}

				}
			}

			if (i == 0 && leidos == -1) {
				return -1;
			}
			if (leidos != -1) {
				byte[] info = bloque.obtenerContenido();
				System.arraycopy(info, 0, b, i * tamBloque, info.length);
			}
		}
		return b.length;
	}

	public void write(byte[] b) throws RemoteException, IOException {

		if (!estaAbierto() || !puedeEscribir())
			throw new IOException();

		if (usarCache)
			writeConCache(b);
		else
			writeSinCache(b);
		incrementarPuntero(b.length);

	}

	private void writeSinCache(byte[] b) throws RemoteException, IOException {
		fich.seek(puntero, usuario);
		fich.write(b, usuario);
	}

	private void writeConCache(byte[] b) throws RemoteException, IOException {
		int cantBloques = b.length / tamBloque;
		int bloqueInicial = (int) (puntero / tamBloque);
		int i = 0;
		for (; i < cantBloques; i++) {
			byte[] cacheInfo = new byte[tamBloque];
			System.arraycopy(b, i * tamBloque, cacheInfo, 0, tamBloque);
			Bloque bloque = new Bloque(bloqueInicial + i, cacheInfo);
			Bloque aux = cache.putBloque(bloque);
			cache.activarMod(bloque);
			if (aux != null && cache.preguntarYDesactivarMod(aux)) {
				writeInter(aux.obtenerContenido(), aux.obtenerId() * tamBloque);
			}
		}
	}

	private boolean puedeEscribir() {
		return modo.contains("w");
	}

	private void writeInter(byte[] info, long from) throws RemoteException,
			IOException {

		fich.seek(from, usuario);
		fich.write(info, usuario);
	}

	public void seek(long p) throws RemoteException, IOException {

		if (!estaAbierto())
			throw new IOException();

		fich.seek(p, usuario);
		setearPuntero(p);
	}

	public void close() throws RemoteException, IOException {

		if (!estaAbierto())
			throw new IOException();

		if (usarCache) {
			volcarCache();
			cache.fijarFecha(fich.lastModified());
		}

		fich.close(usuario);
		abierto = false;
	}

	private void incrementarPuntero(long length) {
		this.puntero += length;
	}

	private void setearPuntero(long p) {
		this.puntero = p;
	}

	private int readInter(byte[] b, long from) throws RemoteException,
			IOException {

		fich.seek(from, usuario);
		byte[] resp = fich.read(tamBloque, usuario);
		if (resp == null) {
			return -1;
		}
		System.arraycopy(resp, 0, b, 0, tamBloque);
		return b.length;

	}

	private boolean estaAbierto() {
		return abierto;
	}

	private void volcarCache() throws RemoteException, IOException {
		if (usarCache) {
			for (Bloque b : cache.listaMod()) {
				writeInter(b.obtenerContenido(), b.obtenerId() * tamBloque);
				cache.desactivarMod(b);
			}
		}
	}
}
