package net.contal.spring.model;

import java.text.ParseException;
import java.util.Date;

import net.contal.spring.utils.CustomHelper;

public class DatesDto {
	
	
	private String dateFrom;
	private String dateTo;
	

	public String getDateFrom() {
		return dateFrom;
	}
	public String getDateTo() {
		return dateTo;
	}
	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}
	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	
	public Date getFtomDate() {
		try {
			return CustomHelper.stringToDate(this.dateFrom);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	public Date getToDate() {
		try {
			return CustomHelper.stringToDate(this.dateTo);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
