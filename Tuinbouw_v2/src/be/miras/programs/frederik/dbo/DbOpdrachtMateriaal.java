package be.miras.programs.frederik.dbo;

public class DbOpdrachtMateriaal {
	private int id;
	private int opdrachtId;
	private int materiaalId;
	private double verbruik;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOpdrachtId() {
		return opdrachtId;
	}

	public void setOpdrachtId(int opdrachtId) {
		this.opdrachtId = opdrachtId;
	}

	public int getMateriaalId() {
		return materiaalId;
	}

	public void setMateriaalId(int materiaalId) {
		this.materiaalId = materiaalId;
	}

	public double getVerbruik() {
		return verbruik;
	}

	public void setVerbruik(double verbruik) {
		this.verbruik = verbruik;
	}

}
