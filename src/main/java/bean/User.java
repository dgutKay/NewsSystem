package bean;

import java.sql.Timestamp;

import tools.WebProperties;

public class User {
	private Integer userId;
	private String name;
	private String type;
	private String email;
	private String password;
	private String salt;
	private String headIconUrl;
	private Timestamp registerDate;
	private String usability;

	public User() {
		headIconUrl = "/" + WebProperties.propertiesMap.get("projectName")
				+ WebProperties.propertiesMap.get("headIconFileDefault").replace("\\", "/");
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHeadIconUrl() {
		return headIconUrl;
	}

	public void setHeadIconUrl(String headIconUrl) {
		this.headIconUrl = headIconUrl;
	}

	public Timestamp getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Timestamp registerDate) {
		this.registerDate = registerDate;
	}

	public String getUsability() {
		return usability;
	}

	public void setUsability(String usability) {
		this.usability = usability;
	}

}
