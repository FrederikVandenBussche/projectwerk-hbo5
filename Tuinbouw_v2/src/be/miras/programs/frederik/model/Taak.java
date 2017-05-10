package be.miras.programs.frederik.model;

import java.util.List;

/**
 * @author Frederik Vanden Bussche
 * 
 *         view model van een taak
 *
 */
public class Taak implements Ivergelijk {
	private int id;
	private int opdrachtId;
	private String taakNaam;
	private boolean isVisible;
	private String opmerking;
	private int vooruitgangPercentage;
	private String status;
	private List<Planning> planningLijst;
	private List<Planning> gewerkteUrenLijst;

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
	 * @return the opdrachtId int
	 */
	public int getOpdrachtId() {
		return opdrachtId;
	}

	/**
	 * @param opdrachtId
	 *            the opdrachtId to set int
	 */
	public void setOpdrachtId(int opdrachtId) {
		this.opdrachtId = opdrachtId;
	}

	/**
	 * @return the taakNaam String
	 */
	public String getTaakNaam() {
		return taakNaam;
	}

	/**
	 * @param taakNaam
	 *            the taakNaam to set String
	 */
	public void setTaakNaam(String taakNaam) {
		this.taakNaam = taakNaam;
	}

	/**
	 * @return the isVisible boolean
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * @param isVisible
	 *            the isVisible to set boolean
	 */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	/**
	 * @return the opmerking String
	 */
	public String getOpmerking() {
		return opmerking;
	}

	/**
	 * @param opmerking
	 *            the opmerking to set String
	 */
	public void setOpmerking(String opmerking) {
		this.opmerking = opmerking;
	}

	/**
	 * @return the vooruitgangPercentage int
	 */
	public int getVooruitgangPercentage() {
		return vooruitgangPercentage;
	}

	/**
	 * @param vooruitgangPercentage
	 *            the vooruitgangPercentage to set int
	 */
	public void setVooruitgangPercentage(int vooruitgangPercentage) {
		this.vooruitgangPercentage = vooruitgangPercentage;
	}

	/**
	 * @return the status String
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set String
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the planningLijst List<Planning>
	 */
	public List<Planning> getPlanningLijst() {
		return planningLijst;
	}

	/**
	 * @param planningLijst
	 *            the planningLijst to set List<Planning>
	 */
	public void setPlanningLijst(List<Planning> planningLijst) {
		this.planningLijst = planningLijst;
	}

	/**
	 * @return the gewerkteUrenLijst List<Planning>
	 */
	public List<Planning> getGewerkteUrenLijst() {
		return gewerkteUrenLijst;
	}

	/**
	 * @param gewerkteUrenLijst
	 *            the gewerkteUrenLijst to set List<Planning>
	 */
	public void setGewerkteUrenLijst(List<Planning> gewerkteUrenLijst) {
		this.gewerkteUrenLijst = gewerkteUrenLijst;
	}

	@Override
	public boolean isVerschillend(Object o, Object p) {
		return false;
	}

	
}
