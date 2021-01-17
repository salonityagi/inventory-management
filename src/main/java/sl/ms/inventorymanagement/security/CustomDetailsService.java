package sl.ms.inventorymanagement.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import sl.ms.inventorymanagement.entity.UserEntity;

@Service
public class CustomDetailsService implements UserDetailsService {
	@Autowired
	OAuthDao oauthDao;

	@Override
	public CustomUser loadUserByUsername(final String username) throws UsernameNotFoundException {
		UserEntity userEntity = null;
		try {
			System.out.println("User requested in loadUserByUsername : "+ username);
			userEntity = oauthDao.getUserDetails(username);
			CustomUser customUser = new CustomUser(userEntity);
			return customUser;
		} catch (Exception e) {
			e.printStackTrace();
			throw new UsernameNotFoundException("User " + username + " was not found in the database");
		}
	}
}
