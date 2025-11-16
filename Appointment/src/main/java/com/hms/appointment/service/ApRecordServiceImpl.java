package com.hms.appointment.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hms.appointment.clients.ProfileClient;
import com.hms.appointment.dto.ApRecordDTO;
import com.hms.appointment.dto.RecordDetails;
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
    private final ProfileClient profileClient;

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

    @Override
    public List<RecordDetails> getApRecordsByPatientId(Long patientId) throws HmsException {
        List<ApRecord> records = apRecordRepository.findByPatient_Id(patientId);

        List<RecordDetails> recordDetailsList = records.stream()
                .map(ApRecord::toRecordDetails)
                .toList();

        List<Long> doctorIds = recordDetailsList.stream()
                .map(RecordDetails::getDoctorId)
                .distinct()
                .toList();

        List<DoctorName> doctors = profileClient.getDoctorsByIdsForDropDown(doctorIds);

        Map<Long, String> doctorIdNameMap = doctors.stream()
                .collect(Collectors.toMap(DoctorName::getId, DoctorName::getName));

                recordDetailsList.forEach(recordDetails -> {
            String doctorName = doctorIdNameMap.get(recordDetails.getDoctorId());
            if(doctorName != null) {
                recordDetails.setDoctorName(doctorName);
            }else{
                recordDetails.setDoctorName("Unknown Doctor");
            }
            return recordDetails;
        });

    }

    @Override
    public boolean isRecordExists(Long appointmentId) throws HmsException {
        return apRecordRepository.existsByAppointment_Id(appointmentId);
    }

}
