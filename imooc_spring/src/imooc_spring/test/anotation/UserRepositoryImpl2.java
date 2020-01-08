package imooc_spring.test.anotation;

import org.springframework.stereotype.Repository;

@Repository("repo")
public class UserRepositoryImpl2 implements IUserRepository {

	@Override
	public void save() {
		// TODO Auto-generated method stub
		System.out.println("UserRepositoryImpl2.save()....");
	}
	
	
	
}
