package com.hms.Profile.service;

import java.util.List;

import com.hms.Profile.dto.DoctorDTO;
import com.hms.Profile.dto.DoctorDropDown;
import com.hms.Profile.exception.HmsException;

public interface DoctorService {

    Long addDoctor(DoctorDTO doctorDTO) throws HmsException;

    DoctorDTO getDoctorById(Long id) throws HmsException;

    DoctorDTO updateDoctor(Long id, DoctorDTO doctorDTO) throws HmsException;

    boolean doctorExists(Long id); // no need to throw exception here

    public List<DoctorDropDown> getAllDoctorsForDropDown() throws HmsException;
}

