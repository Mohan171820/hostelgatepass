package mmm.example.hostelgatepass.service;
import mmm.example.hostelgatepass.model.Student;
import mmm.example.hostelgatepass.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repo;

    public Student registerStudent(Student s) {
        return repo.save(s);
    }

    public Student validateLogin(String id, String password) {
        return repo.findByIdAndPassword(id, password);
    }

    public List<Student> getAllStudents() {
        return repo.findAll();
    }
}