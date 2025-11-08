package com.iit.dsr.controller;

import com.iit.dsr.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "app/v1/commons")
public class CommonController {

    @Autowired
    private CommonService commonService;

    @GetMapping(value = "/getActiveOrganizations")
    public ResponseEntity<?> retrieveAllActiveOrganizations(){
        return commonService.retrieveAllActiveOrganizations();
    }
}
