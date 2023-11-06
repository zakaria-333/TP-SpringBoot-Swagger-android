package ma.projet.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ma.projet.entities.Filiere;

import ma.projet.services.FiliereService;

@RestController
@RequestMapping("/api/v1/filieres")

public class FiliereController {
	
	@Autowired
	private FiliereService filiereService;
	

	@GetMapping
	public List<Filiere> findAllFiliere(){
		return filiereService.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> findById(@PathVariable Long id) {
		Filiere filiere = filiereService.findById(id);
		if(filiere == null) {
			return new ResponseEntity<Object>("Filiere with ID " + id + " not found", HttpStatus.BAD_REQUEST);
		}
		else {
			return ResponseEntity.ok(filiere);
		}
	}
	
	@PostMapping
	public Filiere createFiliere(@RequestBody Filiere filiere) {
		filiere.setId(0L);
		return filiereService.create(filiere);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> updateFiliere(@PathVariable Long id,@RequestBody Filiere filiere) {
		if(filiereService.findById(id) == null) {
			return new ResponseEntity<Object>("Filiere with ID " + id + " not found", HttpStatus.BAD_REQUEST);
		}
		else {
			filiereService.update(id, filiere);
			Map<String, String> response = new HashMap<>();
	        response.put("message", "update avec succès");
	        return ResponseEntity.ok(response);
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteFiliere(@PathVariable Long id){
		Filiere filiere = filiereService.findById(id);
		if(filiere == null) {
			return new ResponseEntity<Object>("Filiere with ID " + id + " not found", HttpStatus.BAD_REQUEST);
		}
		else {
			filiereService.delete(filiere);
			return ResponseEntity.ok("Filiere has been deleted");
		}
	}
}
