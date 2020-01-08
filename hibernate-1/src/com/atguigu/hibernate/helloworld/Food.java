package com.atguigu.hibernate.helloworld;

/**
 * 食物，用于测试多对一，Food是多，Cat是 一
 * 
 * @author Wei
 * @time 2016 上午11:19:40
 */
public class Food {
	private Cat cat;
	private String name;
	private Integer price;

	public Food() {

	}

	public Food(Cat cat,String name,Integer price){
		this.cat = cat;
		this.name = name;
		this.price = price;
	}

	public Cat getCat() {
		return cat;
	}

	public void setCat(Cat cat) {
		this.cat = cat;
	}

	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

}
