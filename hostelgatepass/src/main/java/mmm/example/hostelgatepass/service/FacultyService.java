package mmm.example.hostelgatepass.service;

import mmm.example.hostelgatepass.model.Faculty;
import mmm.example.hostelgatepass.model.GatePassRequest;
import mmm.example.hostelgatepass.repository.FacultyRepository;
import mmm.example.hostelgatepass.repository.GatePassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyService {

    @Autowired
    private FacultyRepository facultyRepo;

    @Autowired
    private GatePassRepository gatePassRepo;

    public Faculty validateLogin(String username, String password) {
        Faculty faculty = facultyRepo.findByUsername(username);
        if (faculty != null && faculty.getPassword().equals(password)) {
            return faculty;
        }
        return null;
    }


    public Faculty registerFaculty(Faculty faculty) {
        return facultyRepo.save(faculty);
    }


    public Faculty getFacultyById(Long id) {
        return facultyRepo.findById(id).orElse(null);
    }


    public List<GatePassRequest> getAllRequests() {
        return gatePassRepo.findAll();
    }


    public GatePassRequest approveRequest(Long id, String remarks) {
        GatePassRequest req = gatePassRepo.findById(id).orElse(null);
        if (req != null) {
            req.setFacultyStatus("APPROVED");
            req.setFacultyRemarks(remarks);
            gatePassRepo.save(req);
        }
        return req;
    }


    public GatePassRequest rejectRequest(Long id, String remarks) {
        GatePassRequest req = gatePassRepo.findById(id).orElse(null);
        if (req != null) {
            req.setFacultyStatus("REJECTED");
            req.setFacultyRemarks(remarks);
            gatePassRepo.save(req);
        }
        return req;
    }
}
