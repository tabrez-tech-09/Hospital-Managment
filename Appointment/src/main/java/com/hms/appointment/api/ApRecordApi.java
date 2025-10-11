package com.hms.appointment.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.hms.appointment.dto.ApRecordDTO;
import com.hms.appointment.exception.HmsException;
import com.hms.appointment.service.ApRecordService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequestMapping("/appointment/records")
@Validated
@RequiredArgsConstructor
public class ApRecordApi {

    private final ApRecordService apRecordService;

    @PostMapping("/create")
    public ResponseEntity<Long> createApRecord(@RequestBody ApRecordDTO request) throws HmsException {
        return new ResponseEntity<>(apRecordService.createApRecord(request), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateApRecord(@RequestBody ApRecordDTO request) throws HmsException {
        apRecordService.updateApRecord(request);
        return new ResponseEntity<>("Record updated successfully", HttpStatus.OK);
    }

    @GetMapping("/getByAppointmentId/{appointmentId}")
    public ResponseEntity<ApRecordDTO> getByAppointmentId(@PathVariable Long appointmentId) throws HmsException {
        return new ResponseEntity<>(apRecordService.getApRecordByAppointmentId(appointmentId), HttpStatus.OK);
    }

    @GetMapping("/getApRecordDetailsByAppointmentId/{appointmentId}")
    public ResponseEntity<ApRecordDTO> getApRecordDetailsByAppointmentId(@PathVariable Long appointmentId) throws HmsException {
        return new ResponseEntity<>(apRecordService.getApRecordDetailsByAppointmentId(appointmentId), HttpStatus.OK);
    }

    @GetMapping("/getById/{recordId}")
    public ResponseEntity<ApRecordDTO> getById(@PathVariable Long recordId) throws HmsException {
        return new ResponseEntity<>(apRecordService.getApRecordById(recordId), HttpStatus.OK);
    }
}