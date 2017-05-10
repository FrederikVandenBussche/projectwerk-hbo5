package be.miras.programs.frederik.model;

/**
 * @author Frederik Vanden Bussche
 * 
 *         return true als bepaalde data van de twee objecten verschillend aan
 *         elkaar zijn. Merk op dat niet alle data van het object gecontrolleerd
 *         wordt op zijnde verschillend.
 *
 */
public interface Ivergelijk {
	/**
	 * @param o
	 *            Object
	 * @param p
	 *            Object
	 * @return boolean
	 * 
	 *         return true als bepaalde data van de twee objecten verschillend
	 *         aan elkaar zijn. Merk op dat niet alle data van het object
	 *         gecontrolleerd wordt op zijnde verschillend.
	 * 
	 */
	public boolean isVerschillend(Object o, Object p);
}
