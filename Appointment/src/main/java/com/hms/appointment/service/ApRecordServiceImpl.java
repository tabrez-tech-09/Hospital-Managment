package com.hms.appointment.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hms.appointment.dto.ApRecordDTO;
import com.hms.appointment.entity.ApRecord;
import com.hms.appointment.repository.ApRecordRepository;
import com.hms.appointment.utility.StringListConverter;
import com.hms.appointment.exception.HmsException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApRecordServiceImpl implements ApRecordService {

    private final ApRecordRepository apRecordRepository;

    @Override
    public long createApRecord(ApRecordDTO request) {
        // Check if record already exists for this appointment
        Optional<ApRecord> existingRecord = apRecordRepository
                .findByAppointment_Id(request.getAppointment().getId());

        if (existingRecord.isPresent()) {
            throw new HmsException("appointment_record_already_exists");
        }

        return apRecordRepository.save(request.toEntity()).getId();
    }

    @Override
    public void updateApRecord(ApRecordDTO request) throws HmsException {
        ApRecord exiting = apRecordRepository.deleteById(request.getId()).orElseThrow(() -> new HmsException(Appointmnet_Record_no_found));
        exiting.setNotes(request.getNotes());
        exiting.setDiagnosis(request.getDiagnosis());
        exiting.setFollowUpDate(request.getFollowUpDate());
        exiting.setSymptoms(StringListConverter.convertedListToString(request.getSymptoms()));
        exiting.setTests(StringListConverter.convertedListToString(request.getTests()));
        exiting.setReferral(request.getReferral());
        apRecordRepository.save(exiting);
    }

    @Override
    public ApRecordDTO getApRecordByAppointmentId(Long appointmentId) throws HmsException {
        return apRecordRepository.findByAppointment_Id(appointmentId).orElseThrow(() -> new HmsException("appointmnet_no_found")).toDto();
    }

    @Override
    public ApRecordDTO getApRecordById(Long recordId) throws HmsException {
        return apRecordRepository.findById(recordId).orElseThrow(() -> new HmsException("appointmnet_no_found")).toDto();
    }
}
