package ma.projet.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity

public class Student extends User {
	
	
	private String phone;
	private String firstName;
	private String lastName;

	@ManyToOne
	private Filiere filiere;

	
	public Student() {
		super();
	}
	
	public Student(String username, String password, String phone, String firstName, String lastName, Filiere filiere) {
		super(username, password);
		this.phone = phone;
		this.firstName = firstName;
		this.lastName = lastName;
		this.filiere = filiere;
		
		
	}
	
	public Student(String username, String password, String phone, String firstName, String lastName, Filiere filiere, Role role) {
		super(username, password);
		this.phone = phone;
		this.firstName = firstName;
		this.lastName = lastName;
		this.filiere = filiere;
		this.roles.add(role);
	}
	
	
	
	
	public Student(String phone, String firstName, String lastName, Filiere filiere, Role role) {
		super();
		this.phone = phone;
		this.firstName = firstName;
		this.lastName = lastName;
		this.filiere = filiere;
		this.roles.add(role);
	}

	
	
	
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public Filiere getFiliere() {
		return filiere;
	}

	public void setFiliere(Filiere filiere) {
		this.filiere = filiere;
	}




	public String getFirstName() {
		return firstName;
	}




	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}




	public String getLastName() {
		return lastName;
	}




	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	


}
