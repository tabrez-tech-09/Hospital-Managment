package com.hms.appointment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hms.appointment.dto.AppointmentDetails;
import com.hms.appointment.entity.Appointment;

@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, Long> {

    @Query("""
        SELECT new com.hms.appointment.dto.AppointmentDetails(
            a.id,
            a.patientId,
            null,
            null,
            null,
            a.doctorId,
            null,
            a.appointmentTime,
            a.status,
            a.reason,
            a.notes
        )
        FROM Appointment a
        WHERE a.patientId = :patientId
    """)
    List<AppointmentDetails> findByPatientId(Long patientId);

    @Query("""
        SELECT new com.hms.appointment.dto.AppointmentDetails(
            a.id,
            a.patientId,
            null,
            null,
            null,
            a.doctorId,
            null,
            a.appointmentTime,
            a.status,
            a.reason,
            a.notes
        )
        FROM Appointment a
        WHERE a.doctorId = :doctorId
    """)
    List<AppointmentDetails> findByDoctorId(Long doctorId);
}

