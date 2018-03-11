package tech.corydaniel.hibernatepoc.model;

public class PersonInfo {
	
	private Person person;
	
	public PersonInfo() { }
	
	public PersonInfo(Person person) {
		this.person = person;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
}
