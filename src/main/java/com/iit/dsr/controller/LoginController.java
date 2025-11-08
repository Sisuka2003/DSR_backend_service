package com.iit.dsr.controller;

import com.iit.dsr.dto.requests.dataSubject.DataSubjectLoginRequestDTO;
import com.iit.dsr.dto.responses.commons.CommonResponseDTO;
import com.iit.dsr.service.datasubject.DataSubjectLoginService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/v1/login")
@Log4j2
public class LoginController {

    @Autowired
    private DataSubjectLoginService datSubjectLoginService;


    @PostMapping("/datasubject")
    public ResponseEntity<?> dataSubjectLogin(@RequestBody DataSubjectLoginRequestDTO dsLoginDto){
        log.info(dsLoginDto.toString());
        return datSubjectLoginService.dataSubjectLogin(dsLoginDto);
    }

    @PostMapping("/datacontroller")
    public ResponseEntity<?> dataControllerLogin(@RequestBody String req){
        return ResponseEntity.ok("Success");
    }
}
