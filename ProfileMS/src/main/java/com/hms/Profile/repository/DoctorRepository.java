package com.hms.Profile.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.hms.Profile.dto.DoctorDropDown;
import com.hms.Profile.entity.Doctor;

public interface DoctorRepository extends CrudRepository<Doctor, Long> {

    // find doctor by email
    Optional<Doctor> findByEmail(String email);

    // find doctor by license number
    Optional<Doctor> findByLicenseNO(String licenseNO);

    @Query("SELECT d.id AS id, d.name AS name FROM Doctor d")
    List<DoctorDropDown> findAllDoctorsForDropDown();

    @Query("SELECT d.id AS id , d.name FROM Doctor d WHERE d.id in ?1")
    List<DoctorDropDown> findDoctorsByIdsForDropDown(List<Long> doctorIds);
}


