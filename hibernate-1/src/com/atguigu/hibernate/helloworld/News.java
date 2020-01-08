package com.atguigu.hibernate.helloworld;

import java.util.Date;

public class News {

	private Integer id; // field
	private String title;
	private String author;
	private Date datee;
	private String descr;

	// 使用 title + "," + content 可以来描述当前的 News 记录.
	// 即 title + "," + content 可以作为 News 的 desc 属性值

	private String content;

	public News(String title, String author, Date datee) {
		super();
		this.title = title;
		this.author = author;
		this.datee = datee;
	}
	
	public News(String title, String author, Date datee,String descr) {
		super();
		this.title = title;
		this.author = author;
		this.datee = datee;
		this.descr = descr;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getDatee() {
		return datee;
	}

	public void setDatee(Date datee) {
		this.datee = datee;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public News() {
		// TODO Auto-generated constructor stub
	}
	
	

}
