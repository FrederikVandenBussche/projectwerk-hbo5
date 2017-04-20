package be.miras.programs.frederik.dbo;

import java.util.Date;

public class DbWerknemer {
	private int id;
	private double loon;
	private Date aanwervingsdatum;
	private int persoonId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getLoon() {
		return loon;
	}
	public void setLoon(double loon) {
		this.loon = loon;
	}
	public Date getAanwervingsdatum() {
		return aanwervingsdatum;
	}
	public void setAanwervingsdatum(Date aanwervingsdatum) {
		this.aanwervingsdatum = aanwervingsdatum;
	}
	public int getPersoonId() {
		return persoonId;
	}
	public void setPersoonId(int persoonId) {
		this.persoonId = persoonId;
	}

	

}
