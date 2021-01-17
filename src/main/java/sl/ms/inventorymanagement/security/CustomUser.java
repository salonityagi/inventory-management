package sl.ms.inventorymanagement.security;

import org.springframework.security.core.userdetails.User;

import sl.ms.inventorymanagement.entity.UserEntity;

public class CustomUser extends User {
	private static final long serialVersionUID = 1L;

	public CustomUser(UserEntity user) {
		super(user.getUsername(), user.getPassword(), user.getGrantedAuthoritiesList());
		System.out.println("User name: " + user.getUsername() + " password: " + user.getPassword() + "auth:" + user.getGrantedAuthoritiesList());
	}
}
