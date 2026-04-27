package com.iit.dsr.controller;

import com.iit.dsr.dto.requests.admin.AdminRequestDto;
import com.iit.dsr.service.AdminOperationsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/admin")
@Log4j2
public class AdminController {

    @Autowired
    private AdminOperationsService adminOperationsService;

    @PostMapping(value = "/getAdminDetails")
    ResponseEntity<?> getAdminDetails(@RequestBody AdminRequestDto requestDto){
        log.info("invoked getAdminDetails() =>"+ requestDto);
        return adminOperationsService.getAdminControllerDetails(requestDto);
    }
    @PostMapping(value = "/updateAdminData")
    ResponseEntity<?> updateAdminData(@RequestBody AdminRequestDto requestDto){
        log.info("invoked updateAdminData() =>"+ requestDto);
        return adminOperationsService.updateAdminData(requestDto);
    }



}
