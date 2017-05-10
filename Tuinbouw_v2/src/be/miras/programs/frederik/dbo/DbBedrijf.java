package be.miras.programs.frederik.dbo;

import be.miras.programs.frederik.model.Ivergelijk;

/**
 * @author Frederik Vanden Bussche
 * 
 *         hibernate object van de tabel bedrijf
 */
public class DbBedrijf extends DbKlant implements Ivergelijk {

	private String bedrijfnaam;
	private String btwNummer;

	/**
	 * @return the bedrijfnaam String
	 */
	public String getBedrijfnaam() {
		return bedrijfnaam;
	}

	/**
	 * @param bedrijfnaam
	 *            the bedrijfnaam to set String
	 */
	public void setBedrijfnaam(String bedrijfnaam) {
		this.bedrijfnaam = bedrijfnaam;
	}

	/**
	 * @return the btwNummer String
	 */
	public String getBtwNummer() {
		return btwNummer;
	}

	/**
	 * @param btwNummer
	 *            the btwNummer to set String
	 */
	public void setBtwNummer(String btwNummer) {
		this.btwNummer = btwNummer;
	}

	@Override
	public boolean isVerschillend(Object o, Object p) {
		boolean isVerschillend = true;
		DbBedrijf b1 = (DbBedrijf) o;
		DbBedrijf b2 = (DbBedrijf) p;
		if (b1.getBedrijfnaam().equals(b2.getBedrijfnaam()) && b1.getBtwNummer().equals(b2.getBtwNummer())) {
			isVerschillend = false;
		}
		return isVerschillend;
	}

	
}
