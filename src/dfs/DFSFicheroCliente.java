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

	public DFSFicheroCliente(DFSCliente dfs, String nom, String modo)
			throws RemoteException, IOException, FileNotFoundException {

		usuario = Math.random();
		fich = dfs.open(nom, modo, usuario);
		cache = dfs.getCache(nom);
		if (cache.obtenerFecha() < fich.lastModified())
			cache.vaciar();
		tamBloque = dfs.getTamBloque();
		this.modo = modo;

	}

	public int read(byte[] b) throws RemoteException, IOException {

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
			if (leidos != -1) {
				byte[] info = bloque.obtenerContenido();
				System.arraycopy(info, 0, b, i * tamBloque, info.length);
			}
		}

		incrementarPuntero(i * tamBloque);

		return b.length;

	}

	public void write(byte[] b) throws RemoteException, IOException {

		if (!modo.contains("w"))
			throw new IOException();

		int cantBloques = b.length / tamBloque;
		int bloqueInicial = (int) (puntero / tamBloque);
		int i = 0;
		byte[] cacheInfo = new byte[tamBloque];
		for (; i < cantBloques; i++) {
			System.arraycopy(b, i * tamBloque, cacheInfo, 0, tamBloque);
			Bloque bloque = new Bloque(bloqueInicial + i, cacheInfo);
			Bloque aux = cache.putBloque(bloque);
			cache.activarMod(bloque);
			if (aux != null && cache.preguntarYDesactivarMod(aux)) {
				writeInter(aux.obtenerContenido(), aux.obtenerId() * tamBloque);
			}
		}
		incrementarPuntero(b.length);

	}

	private void writeInter(byte[] info, long from) throws RemoteException,
			IOException {

		fich.seek(from);
		fich.write(info);
	}

	public void seek(long p) throws RemoteException, IOException {

		fich.seek(p);
		setearPuntero(p);
	}

	public void close() throws RemoteException, IOException {

		for (Bloque b : cache.listaMod()) {
			writeInter(b.obtenerContenido(), b.obtenerId() * tamBloque);
			cache.desactivarMod(b);
		}

		cache.fijarFecha(fich.lastModified());

		fich.close();
	}

	private void incrementarPuntero(long length) {
		this.puntero += length;
	}

	private void setearPuntero(long p) {
		this.puntero = p;
	}

	private int readInter(byte[] b, long from) throws RemoteException,
			IOException {

		fich.seek(from);
		byte[] resp = fich.read(tamBloque);
		if (resp == null) {
			return -1;
		}
		System.arraycopy(resp, 0, b, 0, tamBloque);
		return b.length;

	}
}
