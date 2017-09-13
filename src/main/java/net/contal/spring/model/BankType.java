package net.contal.spring.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="bank_type")
public class BankType {


	@Id
	@Column(name="bank_type_id")
	 private long id;
	 @Column(name="iname")
	 private String name;
	 @Column
	 private String tradingName;
	 @Column(name="iactive")
	 private boolean active;
	 
	 
	 public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getTradingName() {
		return tradingName;
	}
	public boolean isActive() {
		return active;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setTradingName(String tradingName) {
		this.tradingName = tradingName;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	 
	 
}
