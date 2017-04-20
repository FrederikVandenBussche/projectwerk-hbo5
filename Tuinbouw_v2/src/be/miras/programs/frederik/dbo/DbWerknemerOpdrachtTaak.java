package be.miras.programs.frederik.dbo;

import java.util.Date;

public class DbWerknemerOpdrachtTaak {
	private int id;
	private int werknemerId;
	private int opdrachtTaakOpdrachtId;
	private int opdrachtTaakTaakId;
	private Date beginuur;
	private Date einduur;
	private int aanwezig;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWerknemerId() {
		return werknemerId;
	}

	public void setWerknemerId(int werknemerId) {
		this.werknemerId = werknemerId;
	}

	public int getOpdrachtTaakOpdrachtId() {
		return opdrachtTaakOpdrachtId;
	}

	public void setOpdrachtTaakOpdrachtId(int opdrachtTaakOpdrachtId) {
		this.opdrachtTaakOpdrachtId = opdrachtTaakOpdrachtId;
	}

	public int getOpdrachtTaakTaakId() {
		return opdrachtTaakTaakId;
	}

	public void setOpdrachtTaakTaakId(int opdrachtTaakTaakId) {
		this.opdrachtTaakTaakId = opdrachtTaakTaakId;
	}

	public Date getBeginuur() {
		return beginuur;
	}

	public void setBeginuur(Date beginuur) {
		this.beginuur = beginuur;
	}

	public Date getEinduur() {
		return einduur;
	}

	public void setEinduur(Date einduur) {
		this.einduur = einduur;
	}

	public int getAanwezig() {
		return aanwezig;
	}

	public void setAanwezig(int aanwezig) {
		this.aanwezig = aanwezig;
	}

}
