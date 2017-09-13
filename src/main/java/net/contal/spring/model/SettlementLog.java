package net.contal.spring.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="efpos_settlement_log")
public class SettlementLog {
	
	

	@Id
	@GeneratedValue
	private long id;
	@Column
	private String merchantId;

	@Column
	private Date date;
	@Column
	private String purchCount;
	@Column
	private Float purchAmount;
	@Column
	private String terminalId;
	@Column
	private Double total;
	
	@ManyToOne
	@JoinColumn(name="bank_typ_id",nullable=false)
	private BankType bankType;
	
	
	
	public long getId() {
		return id;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public Date getDate() {
		return date;
	}
	public String getPurchCount() {
		return purchCount;
	}
	public Float getPurchAmount() {
		return purchAmount;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public Double getTotal() {
		return total;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public void setPurchCount(String purchCount) {
		this.purchCount = purchCount;
	}
	public void setPurchAmount(Float purchAmount) {
		this.purchAmount = purchAmount;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public BankType getBankType() {
		return bankType;
	}
	public void setBankType(BankType bankType) {
		this.bankType = bankType;
	}

	
	
}
