package com.hms.appointment.service;

import java.util.List;

import com.hms.appointment.dto.MedicineDTO;

public interface  MedicineService {
    public Long saveMedicine(MedicineDTO request);
    public List<MedicineDTO> saveAllMedicine(List<MedicineDTO> requestList);
    public List<MedicineDTO> getAllMedicinesByPrescription(Long prescriptionId);
}
