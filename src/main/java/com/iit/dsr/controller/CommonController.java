package com.iit.dsr.controller;

import com.iit.dsr.dto.requests.commons.CommonRequestDto;
import com.iit.dsr.service.CommonService;
import com.iit.dsr.service.EncryptionService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "app/v1/commons")
@Log4j2
public class CommonController {

    @Autowired
    private CommonService commonService;
    @Autowired
    private EncryptionService encryptionService;

    @PostMapping(value = "/keyExchange")
    public ResponseEntity<?> keyExchange(@RequestBody Map<String, String> body,
                                         HttpSession session) {
        try {
            String clientPublicKeyB64 = body.get("publicKey");
            log.info("Key exchange initiated");
            Map<String, String> response = encryptionService.performKeyExchange(
                    clientPublicKeyB64, session);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Key exchange failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Key exchange failed");
        }
    }
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
