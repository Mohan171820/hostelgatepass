package mmm.example.hostelgatepass.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GatePassRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @ManyToOne
    private Student student;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;  // link to the parent

    private String reason;
    private String type; // SHORT_OUTING / HOME_VISIT
    private LocalDateTime requestDate;
    private LocalDateTime expectedReturnDate;

    private String status; // PENDING / APPROVED / REJECTED (overall)

    private String parentStatus;    // PENDING / APPROVED / REJECTED
    private String facultyStatus;   // PENDING / APPROVED / REJECTED
    private String adminStatus;     // PENDING / APPROVED / REJECTED

    private String parentRemarks;
    private String facultyRemarks;
    private String adminRemarks;

    private String wardenRemarks; // optional
}
