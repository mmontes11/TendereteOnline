package es.udc.pa.pa013.practicapa.model.userservice;

public class UserProfileDetails {

	private String firstName;
	private String lastName;
	private String email;
	private String street;
	private int number;
	private String door;
	private int zipCode;

	public UserProfileDetails(String firstName, String lastName, String email,
			String street, int number, String door, int zipCode) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.street = street;
		this.number = number;
		this.door = door;
		this.zipCode = zipCode;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getDoor() {
		return door;
	}

	public void setDoor(String door) {
		this.door = door;
	}

	public int getZipCode() {
		return zipCode;
	}

	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "UserProfileDetails [firstName=" + firstName + ", lastName="
				+ lastName + ", email=" + email + ", street=" + street
				+ ", number=" + number + ", door=" + door + ", zipCode="
				+ zipCode + "]";
	}

}
