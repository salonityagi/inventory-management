package sl.ms.inventorymanagement.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name = "users")
public class Users {

	@Id
	@SequenceGenerator(name = "usersseq",sequenceName = "users_seq",initialValue = 5, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usersseq")
	private int Id;
	private String username;
	@Column(length = 100)
	private String password;
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
