package ma.projet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ma.projet.entities.Filiere;
import ma.projet.entities.Student;
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

	@Query("SELECT s FROM Student s WHERE s.filiere = :filiere")
    public List<Student> findStudentsByFiliere(@Param("filiere") Filiere filiere);
}
