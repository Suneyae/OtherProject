package test.spring.autowired;

/**
 * Cat类，实现Animal接口
 * 
 * @author Wei
 *
 */
public class Cat implements Animal {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Cat(String name) {
		this.name = name;
	}

	public Cat() {
	}

	@Override
	public String whoAmI() {
		// TODO Auto-generated method stub
//		System.out.println("I am an animal,my name is " + this.name);
		return this.name;
	}

}
