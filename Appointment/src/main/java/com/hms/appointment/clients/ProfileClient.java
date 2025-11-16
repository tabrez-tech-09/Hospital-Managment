package com.hms.appointment.clients;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.hms.appointment.dto.DoctorDTO;
import com.hms.appointment.dto.DoctorName;
import com.hms.appointment.dto.PatientDTO;


@FeignClient(name = "ProfileMS")
public interface ProfileClient {

    @GetMapping("/profile/doctors/exists/{id}")
    Boolean doctorExists(@PathVariable Long id);

    @GetMapping("/profile/patients/exists/{id}")
    Boolean patientExists(@PathVariable Long id);

    @GetMapping("/profile/patients/get/{id}")
    PatientDTO getPatientById(@PathVariable Long id);

    @GetMapping("/profile/doctors/get/{id}")
    DoctorDTO getDoctorById(@PathVariable Long id);

    @GetMapping("/profile/doctors/getDoctorsByIdsForDropDown")
    List<DoctorName> getDoctorsByIdsForDropDown(@RequestParam List<Long> doctorIds);

}
