package com.iit.dsr.controller;


import com.iit.dsr.dto.requests.agent.AgentRequestDto;
import com.iit.dsr.service.AgentOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/agent/operations")
@CrossOrigin(origins = "http://localhost:3000")
public class AgentController {

    @Autowired
    private AgentOperationsService agentOperationsService;

    @PostMapping(value = "/loginControllerAgent")
    ResponseEntity<?> loginControllerAdmin(@RequestBody AgentRequestDto requestDto){
       return agentOperationsService.loginControllerAgent(requestDto);
    }

    @PostMapping(value = "/updateAgentRecord")
    ResponseEntity<?> updateAgentRecord(@RequestBody AgentRequestDto requestDto){
       return agentOperationsService.updateAgentRecord(requestDto);
    }

    @PostMapping(value = "/deactivateAgentRecord")
    ResponseEntity<?> deactivateAgentRecord(@RequestBody AgentRequestDto requestDto){
       return agentOperationsService.deleteAgentRecord(requestDto);
    }
    @PostMapping(value = "/fetchAgentInformation")
    ResponseEntity<?> getAgentData(@RequestBody AgentRequestDto requestDto){
       return agentOperationsService.getAgentData(requestDto);
    }
}
