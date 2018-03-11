package tech.corydaniel.hibernatepoc;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import tech.corydaniel.hibernatepoc.model.Person;
import tech.corydaniel.hibernatepoc.model.PersonInfo;

@RestController
public class HPController {
	
	@GetMapping("/person/{personId}/name")
	public String getPersonName(@PathVariable Long personId) {
		if (personId == 1) {
			return "Jimmy";
		} else {
			throw new PersonNotFoundException();
		}		
	}
	
	@PostMapping("/person")
	public PersonInfo createPerson(@RequestBody PersonInfo personInfo) {
		if (personInfo != null) {
			Person p = personInfo.getPerson();
			if (isPersonValid(p)) {
				System.out.println("name: " + p.getName() + " | dob: " + p.getDob());
			} else {
				throw new InvalidPersonException();
			}
		}
		return personInfo;
	}
	
	private boolean isPersonValid(Person p) {
		boolean isValid = false;
		if (p != null) {
			Date dob = p.getDob();
			if (p.getName() != null && dob != null) {
				if (dob.before(new Date())) {
					isValid = true;
				}
			}
		}
		return isValid;
	}
	
	@SuppressWarnings("serial")
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public class PersonNotFoundException extends RuntimeException { }
	
	@SuppressWarnings("serial")
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public class InvalidPersonException extends RuntimeException { }

}
