package test.spring.autowired;

public class Person {
	/**
	 * Person要使用自动装配Cat类，那么就必须有Cat属性
	 * <bean id="cat" class="test.spring.autowired.Cat"> <property name="name"
	 * value="我是小喵喵"> </property> </bean>
	 */
	private Cat cat;
	private String name;

	public Person() {

	}

	public Person(Cat cat, String name) {
		this.cat = cat;
		this.name = name;
	}

	public Cat getCat() {
		return cat;
	}

	public void setCat(Cat cat) {
		this.cat = cat;
	}
	public void setccc333(Cat cat) {
		this.cat = cat;
	}
	/**
	 * 如果Person类的Bean设置了autowire="byName"，那么首先第一步：
	 * Spring框架根据Person这个bean找到对应的Person类，比如Person类里有String name以及Cat cat 这两个属性，
	 * 然后依次在IOC容器(Spring配置文件)里的<property name="name" value="小明"></property>这样的
	 * 配置来给Person这个bean进行赋值(通过Person类的setter方法，具体到Person类，是通过setName(String)来实现的
	 * )， 如果在Person 这个bean的配置里没有找到Person类的Cat属性，而Person Bean又是自动装配，那么就在配置文件里找到
	 * class属性为Cat的Bean对象(
	 * <bean id="cat222" class="test.spring.autowired.Cat"> <property name=
	 * "name" value="我是小喵喵"></property></bean>)，然后通过这个Bean的配置id属性值
	 * 在Person类里找到对应的setter()方法，具体到这里就是在Person里找setCat222的setter()方法，
	 * 经过测试，实际上在Person类里写成setcat222(Cat cat)也可以
	 * 
	 */
	public void setCat222(Cat cat) {
		this.cat = cat;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void introduceSelf() {
		if (cat == null) {
			System.out.println("我是" + this.name + ",我还没有小猫,猫：" + this.cat);
			return;
		} else {
			System.out.println("my name is " + this.name + ",i have a Pet," + cat.whoAmI());
		}
	}
}
