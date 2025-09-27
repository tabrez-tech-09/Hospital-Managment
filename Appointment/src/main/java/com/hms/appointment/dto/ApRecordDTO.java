package com.hms.appointment.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.hms.appointment.entity.ApRecord;
import com.hms.appointment.entity.Appointment;
import com.hms.appointment.utility.StringListConverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApRecordDTO {

    private Long id;
    private Long patientId;
    private Long doctorId;
    private Appointment appointment;

    private List<String> symptoms;
    private String diagnosis;
    private List<String> tests;
    private String notes;
    private String referral;
    private LocalDate followUpDate;
    private LocalDateTime createdAt;

    // ✅ Convert DTO → Entity
    public ApRecord toEntity() {
        return new ApRecord(
            id,
            patientId,
            doctorId,
            appointment, // directly using Appointment object
            StringListConverter.convertedListToString(symptoms),
            diagnosis,
            StringListConverter.convertedListToString(tests),
            notes,
            referral,
            followUpDate,
            createdAt
        );
    }
}
