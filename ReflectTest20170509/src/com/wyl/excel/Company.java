package com.wyl.excel;
/**
 * 公司Bean,用于Excel导出的时候使用
 * @author Wei
 * @time  2017年5月9日 下午3:25:24
 */
public class Company {
	private String companyName;
	private String legalPerson;

	public Company() {
		// TODO Auto-generated constructor stub
	}

	public Company(String companyName, String legalPerson) {
		super();
		this.companyName = companyName;
		this.legalPerson = legalPerson;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

}
