package be.miras.programs.frederik.dbo;

/**
 * @author Frederik Vanden Bussche
 * 
 *         hibernate object van de tabel vooruitgang
 */
public class DbVooruitgang {
	private int id;
	private int percentage;
	private int statusId;

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
	 * @return the percentage int
	 */
	public int getPercentage() {
		return percentage;
	}

	/**
	 * @param percentage
	 *            the percentage to set int
	 */
	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	/**
	 * @return the statusId int
	 */
	public int getStatusId() {
		return statusId;
	}

	/**
	 * @param statusId
	 *            the statusId to set int
	 */
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	
}
