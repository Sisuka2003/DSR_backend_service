package com.iit.dsr.controller;

import com.iit.dsr.dto.requests.operations.DataSubjectOperationsRequestDto;
import com.iit.dsr.service.DataSubjectOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/operations/datasubject")
public class DataSubjectOperationsController {

    @Autowired
    private DataSubjectOperationsService dataSubjectOperationsService;

    @PostMapping(value = "/requestOrganizationData")
    public ResponseEntity<?> requestOrganizationData(@RequestBody DataSubjectOperationsRequestDto requestDto){
        return dataSubjectOperationsService.getStoredData(requestDto);
    }

    @PutMapping(value = "/modifyOrganizationData")
    public ResponseEntity<?> requestForDataCorrection(@RequestBody DataSubjectOperationsRequestDto requestDto){
        return dataSubjectOperationsService.updatedStoredData(requestDto);
    }

    @DeleteMapping(value = "/deleteOrganizationData")
    public ResponseEntity<?> requestToDeleteData(@RequestBody DataSubjectOperationsRequestDto requestDto){
        return dataSubjectOperationsService.deleteStoredData(requestDto);
    }

    @PostMapping(value = "/generateDsrReport")
    public ResponseEntity<?> requestToExportData(@RequestBody DataSubjectOperationsRequestDto requestDto){
        return dataSubjectOperationsService.generateDataExportReport(requestDto);
    }

    public ResponseEntity<?> requestWithdrawConsent(@RequestBody DataSubjectOperationsRequestDto requestDto){
        return null;
    }
}
