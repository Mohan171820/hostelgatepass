package mmm.example.hostelgatepass.controller;

import mmm.example.hostelgatepass.model.GatePassRequest;
import mmm.example.hostelgatepass.model.Parent;
import mmm.example.hostelgatepass.repository.GatePassRepository;
import mmm.example.hostelgatepass.repository.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/student")
@CrossOrigin
public class GatePassController {


    @Autowired
    private GatePassRepository gatePassRepo;

    @Autowired
    private ParentRepository parentRepo;

    // ====== Student raises request ======
    @PostMapping("/raise")
    public GatePassRequest raiseGatePass(@RequestBody GatePassRequest request) {
        // Parent code provided by student
        String parentCode = request.getParent().getParentCode();

        Parent parent = parentRepo.findByParentCode(parentCode)
                .orElseThrow(() -> new RuntimeException("Invalid parent code! Request cannot be raised."));

        request.setParent(parent);
        request.setParentStatus("PENDING");
        request.setFacultyStatus("PENDING");
        request.setAdminStatus("PENDING");
        request.setStatus("PendingParent");
        request.setRequestDate(LocalDateTime.now());

        return gatePassRepo.save(request);
    }


    // ====== Parent approves/rejects ======
    @PutMapping("/parentApproval/{requestId}")
    public GatePassRequest parentApproval(@PathVariable Long requestId,
                                          @RequestParam boolean approve,
                                          @RequestParam(required = false) String remarks) {
        GatePassRequest request = gatePassRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setParentRemarks(remarks);

        if (approve) {
            request.setParentStatus("APPROVED");
            request.setStatus("PendingFaculty");
        } else {
            request.setParentStatus("REJECTED");
            request.setStatus("Rejected");
        }

        return gatePassRepo.save(request);
    }

    // ====== Faculty approves/rejects ======
    @PutMapping("/facultyApproval/{requestId}")
    public GatePassRequest facultyApproval(@PathVariable Long requestId,
                                           @RequestParam boolean approve,
                                           @RequestParam(required = false) String remarks) {
        GatePassRequest request = gatePassRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!"APPROVED".equalsIgnoreCase(request.getParentStatus())) {
            throw new RuntimeException("Parent approval required first!");
        }

        request.setFacultyRemarks(remarks);
        if (approve) {
            request.setFacultyStatus("APPROVED");
            request.setStatus("PendingAdmin");
        } else {
            request.setFacultyStatus("REJECTED");
            request.setStatus("Rejected");
        }

        return gatePassRepo.save(request);
    }

    // ====== Admin approves/rejects ======
    @PutMapping("/adminApproval/{requestId}")
    public GatePassRequest adminApproval(@PathVariable Long requestId,
                                         @RequestParam boolean approve,
                                         @RequestParam(required = false) String remarks) {
        GatePassRequest request = gatePassRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!"APPROVED".equalsIgnoreCase(request.getFacultyStatus())) {
            throw new RuntimeException("Faculty approval required first!");
        }

        request.setAdminRemarks(remarks);
        if (approve) {
            request.setAdminStatus("APPROVED");
            request.setStatus("APPROVED");
        } else {
            request.setAdminStatus("REJECTED");
            request.setStatus("Rejected");
        }

        return gatePassRepo.save(request);
    }

    // ====== Fetch requests for parent dashboard ======
    @GetMapping("/forParent/{parentId}")
    public List<GatePassRequest> getParentRequests(@PathVariable Long parentId) {
        return gatePassRepo.findByParent_IdAndParentStatus(parentId, "PENDING");
    }

    // ====== Fetch requests for faculty dashboard ======
    @GetMapping("/forFaculty/{studentId}")
    public List<GatePassRequest> getFacultyRequests(@PathVariable String studentId) {
        return gatePassRepo.findByStudent_IdAndFacultyStatus(studentId, "PENDING");
    }

    // ====== Fetch requests for admin dashboard ======
    @GetMapping("/forAdmin/{studentId}")
    public List<GatePassRequest> getAdminRequests(@PathVariable String studentId) {
        return gatePassRepo.findByStudent_IdAndAdminStatus(studentId, "PENDING");
    }

    // ====== Download PDF (only if fully approved) ======
    @GetMapping("/download/{requestId}")
    public ResponseEntity<byte[]> downloadGatePass(@PathVariable Long requestId) {
        try {
            GatePassRequest req = gatePassRepo.findById(requestId)
                    .orElseThrow(() -> new RuntimeException("Gate pass not found"));

            boolean fullyApproved = "APPROVED".equalsIgnoreCase(req.getParentStatus()) &&
                    "APPROVED".equalsIgnoreCase(req.getFacultyStatus()) &&
                    "APPROVED".equalsIgnoreCase(req.getAdminStatus());

            if (!fullyApproved) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Gate pass not fully approved yet!".getBytes());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document doc = new Document();
            PdfWriter.getInstance(doc, out);
            doc.open();

            Paragraph header = new Paragraph("🏫 Hostel Gate Pass", new Font(Font.HELVETICA, 18, Font.BOLD));
            header.setAlignment(Element.ALIGN_CENTER);
            doc.add(header);
            doc.add(new Paragraph("--------------------------------------------------"));
            doc.add(Chunk.NEWLINE);

            doc.add(new Paragraph("Student ID      : " + req.getStudent().getId()));
            doc.add(new Paragraph("Name            : " + req.getStudent().getName()));
            doc.add(new Paragraph("Room No         : " + req.getStudent().getRoomNo()));
            doc.add(new Paragraph("Reason          : " + req.getReason()));
            doc.add(new Paragraph("Type            : " + req.getType()));
            doc.add(new Paragraph("Requested On    : " + req.getRequestDate()));
            doc.add(new Paragraph("Expected Return : " + req.getExpectedReturnDate()));
            doc.add(Chunk.NEWLINE);

            doc.add(new Paragraph("Parent Approval  : " + req.getParentStatus() +
                    (req.getParentRemarks() != null ? " (" + req.getParentRemarks() + ")" : "")));
            doc.add(new Paragraph("Faculty Approval : " + req.getFacultyStatus() +
                    (req.getFacultyRemarks() != null ? " (" + req.getFacultyRemarks() + ")" : "")));
            doc.add(new Paragraph("Admin Approval   : " + req.getAdminStatus() +
                    (req.getAdminRemarks() != null ? " (" + req.getAdminRemarks() + ")" : "")));

            doc.add(Chunk.NEWLINE);
            doc.add(new Paragraph("Final Status     : APPROVED ✅"));
            doc.add(new Paragraph("Generated Date   : " + LocalDateTime.now()));

            doc.close();

            byte[] pdfBytes = out.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment",
                    "GatePass_" + req.getRequestId() + ".pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error generating PDF: " + e.getMessage()).getBytes());
        }
    }


}
