package net.contal.spring.dto;

public class TotalTerminalCardDto {
	
	public String terminalId;

	public Double totalMasterVisa;
	public Double totalOthers;
	public Double total;
	
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public Double getTotalMasterVisa() {
		return totalMasterVisa;
	}
	public void setTotalMasterVisa(Double totalMasterVisa) {
		this.totalMasterVisa = totalMasterVisa;
	}
	public Double getTotalOthers() {
		return totalOthers;
	}
	public void setTotalOthers(Double totalOthers) {
		this.totalOthers = totalOthers;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}

	


}
