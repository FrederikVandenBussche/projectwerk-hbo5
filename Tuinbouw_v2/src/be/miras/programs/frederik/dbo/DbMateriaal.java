package be.miras.programs.frederik.dbo;

public class DbMateriaal {
	private int id;
	private String naam;
	private int typeMateriaalId;
	private double eenheidsprijs;
	private String eenheid;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public int getTypeMateriaalId() {
		return typeMateriaalId;
	}

	public void setTypeMateriaalId(int typeMateriaalId) {
		this.typeMateriaalId = typeMateriaalId;
	}

	public double getEenheidsprijs() {
		return eenheidsprijs;
	}

	public void setEenheidsprijs(double eenheidsprijs) {
		this.eenheidsprijs = eenheidsprijs;
	}

	public String getEenheid() {
		return eenheid;
	}

	public void setEenheid(String eenheid) {
		this.eenheid = eenheid;
	}

}
