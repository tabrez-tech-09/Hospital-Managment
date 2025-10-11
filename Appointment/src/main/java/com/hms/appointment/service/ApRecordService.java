package com.hms.appointment.service;

import com.hms.appointment.dto.ApRecordDTO;
import com.hms.appointment.exception.HmsException;

public interface ApRecordService {
    long createApRecord(ApRecordDTO request) throws HmsException;
    void updateApRecord(ApRecordDTO request) throws HmsException;
    ApRecordDTO getApRecordByAppointmentId(Long appointmentId) throws HmsException;
    ApRecordDTO getApRecordDetailsByAppointmentId(Long appointmentId) throws HmsException;
    ApRecordDTO getApRecordById(Long recordId) throws HmsException;
}

