package com.bravesoft.riumachi.bean;

public class CarlendarBean {
	
	private boolean hasNormalSchedule;
	private boolean hasSeeTheDocterSchedule;
	private boolean hasBiologicalAgentSchedule;
	private boolean hasAntibioticSchedule;
	private boolean hasOtherMedicineSchedule;
	private String date;
	private long dateLong;
	
	public boolean isHasNormalSchedule() {
		return hasNormalSchedule;
	}
	public void setHasNormalSchedule(boolean hasNormalSchedule) {
		this.hasNormalSchedule = hasNormalSchedule;
	}
	public boolean isHasSeeTheDocterSchedule() {
		return hasSeeTheDocterSchedule;
	}
	public void setHasSeeTheDocterSchedule(boolean hasSeeTheDocterSchedule) {
		this.hasSeeTheDocterSchedule = hasSeeTheDocterSchedule;
	}
	public boolean isHasBiologicalAgentSchedule() {
		return hasBiologicalAgentSchedule;
	}
	public void setHasBiologicalAgentSchedule(boolean hasBiologicalAgentSchedule) {
		this.hasBiologicalAgentSchedule = hasBiologicalAgentSchedule;
	}
	public boolean isHasAntibioticSchedule() {
		return hasAntibioticSchedule;
	}
	public void setHasAntibioticSchedule(boolean hasAntibioticSchedule) {
		this.hasAntibioticSchedule = hasAntibioticSchedule;
	}
	public boolean isHasOtherMedicineSchedule() {
		return hasOtherMedicineSchedule;
	}
	public void setHasOtherMedicineSchedule(boolean hasOtherMedicineSchedule) {
		this.hasOtherMedicineSchedule = hasOtherMedicineSchedule;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public long getDateLong() {
		return dateLong;
	}
	public void setDateLong(long dateLong) {
		this.dateLong = dateLong;
	}
	
	
}
