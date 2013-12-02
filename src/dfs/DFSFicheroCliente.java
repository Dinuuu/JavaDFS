// Clase de cliente que proporciona el API del servicio DFS

package dfs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;

public class DFSFicheroCliente {

	private DFSFicheroServ fich;
	private Double usuario;
	private DFSCliente dfs;
	private String nombre;
	private long puntero = 0;
	private Cache cache;
	private int tamBloque;

	public DFSFicheroCliente(DFSCliente dfs, String nom, String modo)
			throws RemoteException, IOException, FileNotFoundException {

		usuario = Math.random();
		fich = dfs.open(nom, modo, usuario);
		this.nombre = nom;
		this.dfs = dfs;
		cache = dfs.getCache(nom);
		tamBloque = dfs.getTamBloque();

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
					System.out.println("METO EN LA CACHE BLOQUE"
							+ (bloqueInicial + i));
					System.out.println(new String(leido));
					bloque = new Bloque((bloqueInicial + i), leido);
					cache.putBloque(bloque);
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

		fich.write(b);
		incrementarPuntero(b.length);

	}

	public void seek(long p) throws RemoteException, IOException {

		fich.seek(p);
		setearPuntero(p);
	}

	public void close() throws RemoteException, IOException {

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
		System.out.println("Leo desde " + from + " " + new String(b));
		return b.length;

	}
}
