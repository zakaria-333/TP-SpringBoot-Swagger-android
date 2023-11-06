package ma.projet.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.projet.entities.Filiere;
import ma.projet.entities.Student;

import ma.projet.services.StudentService;

@RestController
@RequestMapping("/api/student")
public class StudentController {
	@Autowired
	private StudentService studentservice;

	@GetMapping("")
	public List<Student> getAllStudent() {
		return studentservice.findAll();
	}
	
	
	@GetMapping("filiere/{id}")
	public List<Student> getStudentsByFiliere(@PathVariable Long id) {
	    return studentservice.findStudentsByFiliere(id);
	}

	@GetMapping("/{id}")
	public Student getById(@PathVariable Long id) {
		return studentservice.findById(id);

	}

	@PostMapping("")
	public Student createStudent(@RequestBody Student student) {
		return studentservice.create(student);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> updateStudent(@PathVariable Long id, @RequestBody Student student) {

	    if (studentservice.findById(id) == null) {
	        return new ResponseEntity<Object>("student avec ID " + id + " n'existe pas", HttpStatus.BAD_REQUEST);
	    } else {
	        studentservice.update(id, student);
	        Map<String, String> response = new HashMap<>();
	        response.put("message", "update avec succès");
	        return ResponseEntity.ok(response);
	    }
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> delete(@PathVariable Long id) {
		Student student = studentservice.findById(id);
		
		if (student == null) {
			return new ResponseEntity<Object>("student avec ID " + id + " n exite pas", HttpStatus.BAD_REQUEST);
		} else {
			studentservice.delete(student);
			return ResponseEntity.ok(" supression avec succes ");

		}
	}
}
