package com.hms.appointment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hms.appointment.dto.ApRecordDTO;
import com.hms.appointment.utility.StringListConverter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ApRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patientId;
    private Long doctorId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    private String symptoms;
    private String diagnosis;
    private String tests;
    private String notes;
    private String referral;
    private LocalDate followUpDate;
    private LocalDateTime createdAt;

    // ✅ Convert Entity → DTO
    public ApRecordDTO toDTO() {
        return new ApRecordDTO(
            id,
            patientId,
            doctorId,
            appointment,
            StringListConverter.convertedStringToList(symptoms),
            diagnosis,
            StringListConverter.convertedStringToList(tests),
            notes,
            referral,
            null,
            followUpDate,
            createdAt
        );
    }
}