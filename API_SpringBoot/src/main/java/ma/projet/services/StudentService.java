package ma.projet.services;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import ma.projet.dao.IDao;
import ma.projet.entities.Filiere;
import ma.projet.entities.Student;
import ma.projet.repository.FiliereRepository;
import ma.projet.repository.StudentRepository;

@Service
public class StudentService implements IDao<Student>{
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private FiliereRepository filiereRepository;
	

	@Override
	public Student create(Student o) {
		return studentRepository.save(o);
	}

	@Override
	public boolean delete(Student o) {
		try {
			studentRepository.delete(o);
			return true;
		}
		catch(Exception ex) {
			return false;
		}
	}

	@Override
    public Student update(Long id, Student o) {
        Student existingStudent = studentRepository.findById(id).orElse(null);

        if (existingStudent != null) {
            try {
                existingStudent.setFirstName(o.getFirstName());
                existingStudent.setPhone(o.getPhone());
                existingStudent.setLastName(o.getLastName());
                existingStudent.setFiliere(o.getFiliere());
                return studentRepository.save(existingStudent);
            } catch (DataAccessException e) {
                e.printStackTrace(); 
            }
        }
        return null;
    }

	@Override
	public List<Student> findAll() {
		
		return studentRepository.findAll();
	}

	@Override
	public Student findById(Long id) {
		return studentRepository.findById(id).orElse(null);
	}
	
	
	public List<Student> findStudentsByFiliere(Long id) {
	    Filiere filiere = filiereRepository.findById(id).orElse(null); 

	    if (filiere != null) {
	        return studentRepository.findStudentsByFiliere(filiere);
	    } else {
	        return Collections.emptyList();
	    }
	}

	

}
