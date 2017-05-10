package be.miras.programs.frederik.dbo;

/**
 * @author Frederik Vanden Bussche
 * 
 *         hibernate object van de tabel bevoegdheid
 */
public class DbBevoegdheid {
	private int id;
	private String rol;

	/**
	 * @return the id int
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set int
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the rol String
	 */
	public String getRol() {
		return rol;
	}

	/**
	 * @param rol
	 *            the rol to set String
	 */
	public void setRol(String rol) {
		this.rol = rol;
	}

	
}
