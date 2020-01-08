package imooc_spring.test.anotation;

import org.springframework.stereotype.Repository;

//@Repository
@Repository("wyl_repo")
public class UserRepositoryImpl implements IUserRepository {
//模拟持久化层
	@Override
	public void save() {
		// TODO Auto-generated method stub
		System.out.println("\nUserRepositoryImpl.save()...");
	}

}
