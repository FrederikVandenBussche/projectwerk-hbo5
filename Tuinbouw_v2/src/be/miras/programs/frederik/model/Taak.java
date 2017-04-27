package be.miras.programs.frederik.model;

import java.util.List;

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

	public String getTaakNaam() {
		return taakNaam;
	}

	public void setTaakNaam(String taakNaam) {
		this.taakNaam = taakNaam;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public String getOpmerking() {
		return opmerking;
	}

	public void setOpmerking(String opmerking) {
		this.opmerking = opmerking;
	}

	public int getVooruitgangPercentage() {
		return vooruitgangPercentage;
	}

	public void setVooruitgangPercentage(int vooruitgangPercentage) {
		this.vooruitgangPercentage = vooruitgangPercentage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public List<Planning> getPlanningLijst() {
		return planningLijst;
	}

	public void setPlanningLijst(List<Planning> planningLijst) {
		this.planningLijst = planningLijst;
	}
	
	/**
	 * @return the gewerkteUrenLijst
	 */
	public List<Planning> getGewerkteUrenLijst() {
		return gewerkteUrenLijst;
	}

	/**
	 * @param gewerkteUrenLijst the gewerkteUrenLijst to set
	 */
	public void setGewerkteUrenLijst(List<Planning> gewerkteUrenLijst) {
		this.gewerkteUrenLijst = gewerkteUrenLijst;
	}

	@Override
	public boolean isVerschillend(Object o, Object p) {
		// TODO Auto-generated method stub
		return false;
	}

}
