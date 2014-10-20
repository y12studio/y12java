package org.gradle;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "users")
public class User {
	
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", email=" + email
				+ "]";
	}

	@DatabaseField(generatedId = true)
    private int id;
    
    @DatabaseField
    private String username;
    
    @DatabaseField
    private String email;
    
    public User() {
        // ORMLite needs a no-arg constructor 
    }
    
    public User(String username, String email) {
		super();
		this.username = username;
		this.email = email;
	}

	public int getId() {
        return this.id;
    }
    
    public String getUsername() {    
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

}
