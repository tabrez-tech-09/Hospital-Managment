package com.hms.appointment.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hms.appointment.clients.ProfileClient;
import com.hms.appointment.dto.AppointmentDTO;
import com.hms.appointment.dto.AppointmentDetails;
import com.hms.appointment.dto.DoctorDTO;
import com.hms.appointment.dto.PatientDTO;
import com.hms.appointment.dto.Status;
import com.hms.appointment.entity.Appointment;
import com.hms.appointment.exception.HmsException;
import com.hms.appointment.repository.AppointmentRepository;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ApiService apiService;

    @Autowired
    private ProfileClient profileClient;

    @Override
    public Long scheduleAppointment(AppointmentDTO appointmentDTO) throws HmsException {
        boolean doctorExists = profileClient.doctorExists(appointmentDTO.getDoctorId());
        if (!doctorExists) {
            throw new HmsException("Doctor not found", 404);
        }

        boolean patientExists = profileClient.patientExists(appointmentDTO.getPatientId());
        if (!patientExists) {
            throw new HmsException("Patient not found", 404);
        }

        appointmentDTO.setStatus(Status.SCHEDULED);
        return appointmentRepository.save(appointmentDTO.toEntity()).getId();
    }

    @Override
    public void cancelAppointment(Long appointmentId) throws HmsException {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new HmsException("Appointment not found", 404));

        if (appointment.getStatus() == Status.CANCELLED) {
            throw new HmsException("Appointment is already cancelled", 400);
        }

        appointment.setStatus(Status.CANCELLED);
        appointmentRepository.save(appointment);
    }

    @Override
    public void completeAppointment(Long appointmentId) {
        // Future implementation
    }

    @Override
    public void rescheduleAppointment(Long appointmentId, String newDateTime) {
        // Future implementation
    }

    @Override
    public AppointmentDTO getAppointmentDetails(Long appointmentId) throws HmsException {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new HmsException("Appointment not found", 404))
                .toDTO();
    }



 @Override
public List<AppointmentDetails> getAllAppointmentsByPatientId(Long patientId) throws HmsException {
    return appointmentRepository.findByPatientId(patientId)
            .stream()
            .map(appointment -> {
                DoctorDTO doctorDTO = profileClient.getDoctorById(appointment.getDoctorId());
                if (doctorDTO != null) {
                    appointment.setDoctorName(doctorDTO.getName());
                } else {
                    appointment.setDoctorName("Unknown Doctor");
                }
                return appointment;
            })
            .collect(Collectors.toList());
        }

@Override
public List<AppointmentDetails> getAllAppointmentsByDoctorId(Long doctorId) throws HmsException {
    return appointmentRepository.findByDoctorId(doctorId)
            .stream()
            .map(appointment -> {
                // here you probably want patient info, not doctor info again
                PatientDTO patientDTO = profileClient.getPatientById(appointment.getPatientId());
                if (patientDTO != null) {
                    appointment.setPatientName(patientDTO.getName());
                    appointment.setPatientEmail(patientDTO.getEmail());
                    appointment.setPatientPhone(patientDTO.getPhone());
                } else {
                    appointment.setPatientName("Unknown Patient");
                }
                return appointment;
            })
            .collect(Collectors.toList());
}

@Override
public AppointmentDetails getAppointmentDetailsWithName(Long appointmentId) throws HmsException {
    Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new HmsException("Appointment not found with id: " + appointmentId, 404));

    DoctorDTO doctorDTO = profileClient.getDoctorById(appointment.getDoctorId());
    PatientDTO patientDTO = profileClient.getPatientById(appointment.getPatientId());

    AppointmentDetails details = new AppointmentDetails();
    details.setId(appointment.getId());
    details.setPatientId(appointment.getPatientId());
    details.setDoctorId(appointment.getDoctorId());
    details.setAppointmentTime(appointment.getAppointmentTime());
    details.setStatus(appointment.getStatus());
    details.setReason(appointment.getReason());
    details.setNotes(appointment.getNotes());

    if (patientDTO != null) {
        details.setPatientName(patientDTO.getName());
        details.setPatientEmail(patientDTO.getEmail());
        details.setPatientPhone(patientDTO.getPhone());
    } else {
        details.setPatientName("Unknown Patient");
    }

    if (doctorDTO != null) {
        details.setDoctorName(doctorDTO.getName());
    } else {
        details.setDoctorName("Unknown Doctor");
    }
    return details;
}
}

