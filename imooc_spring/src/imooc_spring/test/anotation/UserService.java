package imooc_spring.test.anotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	@Autowired(required=false)
	public UserRepositoryImpl rep ;
	
	@Autowired(required=false)
//	@Qualifier("xxxxxxxxx")
	public IUserRepository repo;
	
	public void add(){
		System.out.println("\nUserService.add()...");
	}
}
