package be.miras.programs.frederik.dbo;

public class DbOpdrachtTaak{
	
	private int id;
	private int opdrachtId;
	private int taakId;
	private int vooruitgangId;
	private String opmerking;
	

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

	public int getTaakId() {
		return taakId;
	}

	public void setTaakId(int taakId) {
		this.taakId = taakId;
	}

	public int getVooruitgangId() {
		return vooruitgangId;
	}

	public void setVooruitgangId(int vooruitgangId) {
		this.vooruitgangId = vooruitgangId;
	}

	public String getOpmerking() {
		return opmerking;
	}

	public void setOpmerking(String opmerking) {
		this.opmerking = opmerking;
	}
	
	class DbOpdrachtTaakId{
		private int TaakId;
		private int oprachtId;

		public DbOpdrachtTaakId(int opdrachtId, int TaakId){
			this.oprachtId = opdrachtId;
			this.TaakId = taakId;
		}

		public int getTaakId() {
			return TaakId;
		}

		public void setTaakId(int taakId) {
			TaakId = taakId;
		}

		public int getOprachtId() {
			return oprachtId;
		}

		public void setOprachtId(int oprachtId) {
			this.oprachtId = oprachtId;
		}
		
		
	}

}
