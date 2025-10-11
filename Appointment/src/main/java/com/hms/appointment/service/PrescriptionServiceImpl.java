package com.hms.appointment.service;

import org.springframework.stereotype.Service;

import com.hms.appointment.dto.PrescriptionDTO;
import com.hms.appointment.exception.HmsException;
import com.hms.appointment.repository.PrescriptionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final MedicineService medicineService;

    @Override
    public Long savePrescription(PrescriptionDTO request) {
        // Save prescription entity and get generated ID
        Long prescriptionId = prescriptionRepository.save(request.toEntity()).getId();

        // Save medicines if present
        if (request.getMedicines() != null && !request.getMedicines().isEmpty()) {
            request.getMedicines().forEach(medicine -> {
                medicine.setPrescriptionId(prescriptionId);
            });
            medicineService.saveAllMedicine(request.getMedicines());
        }

        return prescriptionId;
    }

    @Override
    public PrescriptionDTO getPrescriptionByAppointmentId(Long appointmentId) throws HmsException {
        // Find prescription by appointment ID
        var prescription = prescriptionRepository.findByAppointment_Id(appointmentId)
                .orElseThrow(() -> new HmsException("prescription_not_found"));

        PrescriptionDTO dto = prescription.toDto();

        // Attach medicines
        dto.setMedicines(medicineService.getAllMedicinesByPrescription(dto.getId()));

        return dto;
    }

    @Override
    public PrescriptionDTO getPrescriptionById(Long prescriptionId) throws HmsException {
        var prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new HmsException("prescription_not_found"));

        PrescriptionDTO dto = prescription.toDto();
        dto.setMedicines(medicineService.getAllMedicinesByPrescription(dto.getId()));

        return dto;
    }
}