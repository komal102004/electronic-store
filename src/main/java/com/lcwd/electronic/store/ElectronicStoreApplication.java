package com.lcwd.electronic.store;

import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.repositiories.RoleRepository;
import com.lcwd.electronic.store.repositiories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}
	@Autowired
 private RoleRepository roleRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Override
	public void run(String... args) throws Exception {
		Role roleAdmin = roleRepository.findByName("ROLE_ADMIN").orElse(null);
		if(roleAdmin==null) {


			Role role1 = new Role();
			role1.setRoleId(UUID.randomUUID().toString());
			role1.setName("ROLE_ADMIN");
			roleAdmin =  roleRepository.save(role1);
		}
Role roleNormal=roleRepository.findByName("ROLE_NORMAL").orElse(null);
		if(roleNormal==null) {

			Role role2 = new Role();
			role2.setRoleId(UUID.randomUUID().toString());
			role2.setName("ROLE_NORMAL");
			roleRepository.save(role2);
		}
//ek admin user banayenge

User user=userRepository.findByEmail("komalagrahari@gmail.com").orElse(null);
		if(user==null)
		{
			user=new User();
			user.setName("komal");
			user.setEmail("komalagrahari@gmail.com");
			user.setPassword(passwordEncoder.encode("komal"));
			user.setRoles(List.of(roleAdmin));
			user.setUserId(UUID.randomUUID().toString());
			userRepository.save(user);
		}
	}
}
