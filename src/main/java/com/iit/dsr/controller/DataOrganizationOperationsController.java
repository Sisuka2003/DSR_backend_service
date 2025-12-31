package com.iit.dsr.controller;

import com.iit.dsr.dto.requests.operations.DataControllerOperationsRequestDto;
import com.iit.dsr.dto.requests.operations.DataSubjectOperationsRequestDto;
import com.iit.dsr.service.DataControllerOperationsService;
import com.iit.dsr.service.DataSubjectOperationsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/operations/datacontroller")
@CrossOrigin(origins = "http://localhost:3000")
@Log4j2
public class DataOrganizationOperationsController {

    @Autowired
    private DataControllerOperationsService dataControllerOperationsService;

    @PostMapping(value = "/requestDataControllerData")
    public ResponseEntity<?> requestOrganizationData(@RequestBody DataControllerOperationsRequestDto requestDto){
        log.info("DataOrganizationOperationsController => requestOrganizationData => Invoked with :"+requestDto);
        return dataControllerOperationsService.getStoredData(requestDto);
    }

    @PutMapping(value = "/modifyDataControllerData")
    public ResponseEntity<?> requestForDataCorrection(@RequestBody DataControllerOperationsRequestDto requestDto){
        log.info("DataOrganizationOperationsController => requestForDataCorrection => Invoked with :"+requestDto);
        return dataControllerOperationsService.updatedStoredData(requestDto);
    }

    @DeleteMapping(value = "/deleteDataControllerData")
    public ResponseEntity<?> requestToDeleteData(@RequestBody DataControllerOperationsRequestDto requestDto){
        log.info("DataOrganizationOperationsController => requestToDeleteData => Invoked with :"+requestDto);
        return dataControllerOperationsService.deleteStoredData(requestDto);
    }

}
