package be.miras.programs.frederik.dbo;

public class DbAdres {
	private int id;
	private int straatId;
	private int gemeenteId;
	private int huisnummer;
	private String bus;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStraatId() {
		return straatId;
	}

	public void setStraatId(int straatId) {
		this.straatId = straatId;
	}

	public int getGemeenteId() {
		return gemeenteId;
	}

	public void setGemeenteId(int gemeenteId) {
		this.gemeenteId = gemeenteId;
	}

	public int getHuisnummer() {
		return huisnummer;
	}

	public void setHuisnummer(int huisnummer) {
		this.huisnummer = huisnummer;
	}

	public String getBus() {
		return bus;
	}

	public void setBus(String bus) {
		this.bus = bus;
	}

}
