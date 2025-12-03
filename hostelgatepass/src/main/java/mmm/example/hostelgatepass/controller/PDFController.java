package mmm.example.hostelgatepass.controller;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import mmm.example.hostelgatepass.model.GatePassRequest;
import mmm.example.hostelgatepass.repository.GatePassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.OutputStream;

@RestController
@RequestMapping("/pdf")
@CrossOrigin
public class PDFController {

    @Autowired
    private GatePassRepository gatePassRepository;

    @GetMapping("/download/{requestId}")
    public void downloadGatePass(@PathVariable Long requestId, HttpServletResponse response) {
        try {
            GatePassRequest req = gatePassRepository.findById(requestId).orElse(null);
            if (req == null || !"APPROVED".equals(req.getStatus())) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Gate pass not found or not approved yet.");
                return;
            }

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=gatepass_" + requestId + ".pdf");

            Document document = new Document();
            OutputStream out = response.getOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("🏫 HOSTEL GATE PASS"));
            document.add(new Paragraph("--------------------------------------------"));
            document.add(new Paragraph("Request ID: " + req.getRequestId()));
            document.add(new Paragraph("Student ID: " + req.getStudent().getId()));
            document.add(new Paragraph("Student Name: " + req.getStudent().getName()));
            document.add(new Paragraph("Room No: " + req.getStudent().getRoomNo()));
            document.add(new Paragraph("Reason: " + req.getReason()));
            document.add(new Paragraph("Type: " + req.getType()));
            document.add(new Paragraph("Expected Return: " + req.getExpectedReturnDate()));
            document.add(new Paragraph("Status: " + req.getStatus()));
            document.add(new Paragraph("Warden Remarks: " + req.getWardenRemarks()));
            document.add(new Paragraph("--------------------------------------------"));
            document.add(new Paragraph("Approved By: Hostel Warden"));
            document.add(new Paragraph("Generated Automatically by System"));

            document.close();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
