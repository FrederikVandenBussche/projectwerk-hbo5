package be.miras.programs.frederik.dbo;

import java.util.Date;

/**
 * @author Frederik Vanden Bussche
 * 
 *         hibernate object van de tabel werknemer_opdracht_taak
 */
public class DbWerknemerOpdrachtTaak {
	private int id;
	private int werknemerId;
	private int opdrachtTaakOpdrachtId;
	private int opdrachtTaakTaakId;
	private Date beginuur;
	private Date einduur;
	private int aanwezig;

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
	 * @return the werknemerId int
	 */
	public int getWerknemerId() {
		return werknemerId;
	}

	/**
	 * @param werknemerId
	 *            the werknemerId to set int
	 */
	public void setWerknemerId(int werknemerId) {
		this.werknemerId = werknemerId;
	}

	/**
	 * @return the opdrachtTaakOpdrachtId int
	 */
	public int getOpdrachtTaakOpdrachtId() {
		return opdrachtTaakOpdrachtId;
	}

	/**
	 * @param opdrachtTaakOpdrachtId
	 *            the opdrachtTaakOpdrachtId to set int
	 */
	public void setOpdrachtTaakOpdrachtId(int opdrachtTaakOpdrachtId) {
		this.opdrachtTaakOpdrachtId = opdrachtTaakOpdrachtId;
	}

	/**
	 * @return the opdrachtTaakTaakId int
	 */
	public int getOpdrachtTaakTaakId() {
		return opdrachtTaakTaakId;
	}

	/**
	 * @param opdrachtTaakTaakId
	 *            the opdrachtTaakTaakId to set int
	 */
	public void setOpdrachtTaakTaakId(int opdrachtTaakTaakId) {
		this.opdrachtTaakTaakId = opdrachtTaakTaakId;
	}

	/**
	 * @return the beginuur Date
	 */
	public Date getBeginuur() {
		return beginuur;
	}

	/**
	 * @param beginuur
	 *            the beginuur to set Date
	 */
	public void setBeginuur(Date beginuur) {
		this.beginuur = beginuur;
	}

	/**
	 * @return the einduur Date
	 */
	public Date getEinduur() {
		return einduur;
	}

	/**
	 * @param einduur
	 *            the einduur to set Date
	 */
	public void setEinduur(Date einduur) {
		this.einduur = einduur;
	}

	/**
	 * @return the aanwezig int
	 */
	public int getAanwezig() {
		return aanwezig;
	}

	/**
	 * @param aanwezig
	 *            the aanwezig to set int
	 */
	public void setAanwezig(int aanwezig) {
		this.aanwezig = aanwezig;
	}

}
