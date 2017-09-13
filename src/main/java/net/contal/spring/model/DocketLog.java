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
@Table(name="efpos_docket_log")
public class DocketLog {
	
	@Id
	@GeneratedValue
	private long id;
	@Column(name="merchantId")
	private String merchantId;
	@Column(name="terminalId")
	private String terminalId;
	@Column(name="dateTime")
	private Date dateTime;
	@Column(name="cardNumber")
	private String cardNumber;
	@Column(name="status")
	private String status;
	@Column(name="purchaseAmount")
	private String purchaseAmount;
	@Column(name="totalAmount")
	private Float totalAmount;
	@Column(name="cardType")
	private String cardType;
	@Column(name="date")
	private String date;
	
	@ManyToOne
	@JoinColumn(name="bank_type_id",nullable=false)
	private BankType bankType;
	
	
	
	public long getId() {
		return id;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public String getStatus() {
		return status;
	}

	public String getPurchaseAmount() {
		return purchaseAmount;
	}

	public Float getTotalAmount() {
		return totalAmount;
	}

	public String getCardType() {
		return cardType;
	}

	public String getDate() {
		return date;
	}



	public void setId(long id) {
		this.id = id;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setPurchaseAmount(String purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}

	public void setTotalAmount(Float totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public BankType getBankType() {
		return bankType;
	}

	public void setBankType(BankType bankType) {
		this.bankType = bankType;
	}

	
	
	
	
}
