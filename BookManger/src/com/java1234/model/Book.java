package com.java1234.model;

public class Book {

	private int id;
	private String bookName;
	private String author;
	private String sex;
	private float price;
	private String bookDesc;
	private int bookTypeId=-1;
	private String BookTypeName;
	
	
	public Book() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Book(String bookName, String author, String sex, float price,
			String bookDesc, int bookTypeId) {
		super();
		this.bookName = bookName;
		this.author = author;
		this.sex = sex;
		this.price = price;
		this.bookDesc = bookDesc;
		this.bookTypeId = bookTypeId;
	}
	
	
	public Book(int id, String bookName, String author, String sex,
			float price, String bookDesc, int bookTypeId) {
		super();
		this.id = id;
		this.bookName = bookName;
		this.author = author;
		this.sex = sex;
		this.price = price;
		this.bookDesc = bookDesc;
		this.bookTypeId = bookTypeId;
	}
	public Book(String bookName, String author, String sex, int bookTypeId) {
		super();
		this.bookName = bookName;
		this.author = author;
		this.sex = sex;
		this.bookTypeId = bookTypeId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getBookDesc() {
		return bookDesc;
	}
	public void setBookDesc(String bookDesc) {
		this.bookDesc = bookDesc;
	}
	public int getBookTypeId() {
		return bookTypeId;
	}
	public void setBookTypeId(int bookTypeId) {
		this.bookTypeId = bookTypeId;
	}
	public String getBookTypeName() {
		return BookTypeName;
	}
	public void setBookTypeName(String bookTypeName) {
		BookTypeName = bookTypeName;
	}
	
	
	
	
}
