package com.iit.dsr.controller;

import com.iit.dsr.dto.requests.operations.DataSubjectOperationsRequestDto;
import com.iit.dsr.service.DataSubjectOperationsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/operations/datasubject")
@CrossOrigin(origins = "http://localhost:3000")
@Log4j2
public class DataSubjectOperationsController {

    @Autowired
    private DataSubjectOperationsService dataSubjectOperationsService;

    @PostMapping(value = "/requestDataSubjectData")
    public ResponseEntity<?> requestOrganizationData(@RequestBody DataSubjectOperationsRequestDto requestDto){
        return dataSubjectOperationsService.getStoredData(requestDto);
    }

    @PutMapping(value = "/modifyDataSubjectData")
    public ResponseEntity<?> requestForDataCorrection(@RequestBody DataSubjectOperationsRequestDto requestDto){
            log.info("DataSubjectOperationsController => requestForDataCorrection => Invoked with :"+requestDto);
            return dataSubjectOperationsService.updatedStoredData(requestDto);
    }

    @DeleteMapping(value = "/deleteDataSubjectData")
    public ResponseEntity<?> requestToDeleteData(@RequestBody DataSubjectOperationsRequestDto requestDto){
        log.info("DataSubjectOperationsController => requestToDeleteData => Invoked with :"+requestDto);
        return dataSubjectOperationsService.deleteStoredData(requestDto);
    }

    @PostMapping(value = "/generateDsrReport")
    public ResponseEntity<?> requestToExportData(@RequestBody DataSubjectOperationsRequestDto requestDto){
        return dataSubjectOperationsService.generateDataExportReport(requestDto);
    }

    public ResponseEntity<?> requestWithdrawConsent(@RequestBody DataSubjectOperationsRequestDto requestDto){
        return null;
    }


    @PostMapping(value = "/generateOtpCode")
    public ResponseEntity<?> generateLoginOTP(@RequestBody DataSubjectOperationsRequestDto requestDto){
        return dataSubjectOperationsService.sendOtpEmail(requestDto);
    }

    @PostMapping(value = "/verifyOtpCode")
    public ResponseEntity<?> verifyLoginOTP(@RequestBody DataSubjectOperationsRequestDto requestDto){
        return dataSubjectOperationsService.verifyOtpCode(requestDto);
    }
}
