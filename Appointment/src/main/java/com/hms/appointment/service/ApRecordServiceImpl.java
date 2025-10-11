package com.hms.appointment.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hms.appointment.dto.ApRecordDTO;
import com.hms.appointment.entity.ApRecord;
import com.hms.appointment.exception.HmsException;
import com.hms.appointment.repository.ApRecordRepository;
import com.hms.appointment.utility.StringListConverter;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ApRecordServiceImpl implements ApRecordService {

    private final ApRecordRepository apRecordRepository;
    private final PrescriptionService prescriptionService;

    @Override
    public long createApRecord(ApRecordDTO request) throws HmsException {
        Optional<ApRecord> existingRecord =
                apRecordRepository.findByAppointment_Id(request.getAppointment().getId());

        if (existingRecord.isPresent()) {
            throw new HmsException("appointment_record_already_exists");
        }

        long id =  apRecordRepository.save(request.toEntity()).getId();
        if (request.getPrescription() != null) {
            request.getPrescription().setAppointmentId(id);
            prescriptionService.savePrescription(request.getPrescription());
        }
        return id;
    }

    @Override
    public void updateApRecord(ApRecordDTO request) throws HmsException {
        ApRecord existing = apRecordRepository.findById(request.getId())
                .orElseThrow(() -> new HmsException("appointment_record_not_found"));

        existing.setNotes(request.getNotes());
        existing.setDiagnosis(request.getDiagnosis());
        existing.setFollowUpDate(request.getFollowUpDate());
        existing.setSymptoms(StringListConverter.convertedListToString(request.getSymptoms()));
        existing.setTests(StringListConverter.convertedListToString(request.getTests()));
        existing.setReferral(request.getReferral());

        apRecordRepository.save(existing);
    }

    @Override
    public ApRecordDTO getApRecordByAppointmentId(Long appointmentId) throws HmsException {
        ApRecord record = apRecordRepository.findByAppointment_Id(appointmentId)
                .orElseThrow(() -> new HmsException("appointment_not_found"));
        return record.toDTO();
    }

    @Override
    public ApRecordDTO getApRecordById(Long recordId) throws HmsException {
        ApRecord record = apRecordRepository.findById(recordId)
                .orElseThrow(() -> new HmsException("appointment_not_found"));
        return record.toDTO();
    }


    @Override 
public ApRecordDTO getApRecordDetailsByAppointmentId(Long appointmentId) throws HmsException {
    ApRecord record = apRecordRepository.findByAppointment_Id(appointmentId)
            .orElseThrow(() -> new HmsException("appointment_not_found"));

    ApRecordDTO dto = record.toDTO();
    dto.setPrescription(prescriptionService.getPrescriptionByAppointmentId(appointmentId));

    return dto;
}

}
