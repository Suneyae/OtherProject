package com.wyl.reflect2;
/**
 * 明星接口的实现类
 * @author Wei
 * @time  2017年5月9日 下午2:04:48
 */
public class RealStar implements Star{
	String name ;
	public RealStar() {
		
	}
	
	public RealStar(String name){
		this.name = name;
	}

	@Override
	public void sing(String program) {
		System.out.println(name+"正在表演:"+program);
	}

	@Override
	public void act(String movie) {
		System.out.println(name+"正在表演:"+movie);
	}

}
