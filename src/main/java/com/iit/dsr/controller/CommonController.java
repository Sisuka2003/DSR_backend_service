package com.iit.dsr.controller;

import com.iit.dsr.dto.requests.commons.CommonRequestDto;
import com.iit.dsr.service.CommonService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "app/v1/commons")
@CrossOrigin(origins = "http://localhost:3000")
@Log4j2
public class CommonController {

    @Autowired
    private CommonService commonService;

    @GetMapping(value = "/getActiveOrganizations")
    public ResponseEntity<?> retrieveAllActiveOrganizations(){
        return commonService.retrieveAllActiveOrganizations();
    }

    @GetMapping(value = "/getKeyIdentifiers")
    public ResponseEntity<?> retrieveKeyIdentifiers(){
        return commonService.retrieveKeyIdentifiers();
    }

    @PostMapping(value = "/getAllCounts")
    public ResponseEntity<?> getCountsForDashboards(@RequestBody CommonRequestDto requestDto){
        log.info("Invoked data count fetching :"+requestDto);
        return commonService.getDataCountsForDashboards(requestDto);
    }
}
