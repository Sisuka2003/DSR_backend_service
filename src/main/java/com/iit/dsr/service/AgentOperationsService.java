package com.iit.dsr.service;

import com.iit.dsr.dto.requests.agent.AgentRequestDto;
import com.iit.dsr.entity.DataControllerAgentsEntity;
import com.iit.dsr.repository.DataControllerAgentsRepository;
import com.iit.dsr.repository.DataSubjectInControllerRepository;
import com.iit.dsr.repository.StatusRepository;
import com.iit.dsr.repository.UserRoleRepository;
import com.iit.dsr.utils.CommonUtils;
import com.iit.dsr.utils.Constants;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@Log4j2
public class AgentOperationsService {
    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private DataControllerAgentsRepository agentsRepository;
    @Autowired
    private DataSubjectInControllerRepository dataSubjectInControllerRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private StatusRepository statusRepository;

    public ResponseEntity<?> loginControllerAgent(AgentRequestDto requestDto) {
            log.info("AgentOperationsService =>  loginControllerAgent() =>"+requestDto);
        try{
            DataControllerAgentsEntity dataControllerAgentAvailability = agentsRepository.checkAgentAvailability(requestDto.getUsername(), requestDto.getPassword(), Constants.ACTIVE);
            if(Objects.isNull(dataControllerAgentAvailability)){
                log.info("invoked loginControllerAgent() => no agent exists");
                return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_EMPTY,"NO SUCH AGENT EXISTS",null,null,false);
            }

            if(dataControllerAgentAvailability.getIsLoggedIn() == 1){
                log.info("AgentOperationsService => loginControllerAgent() => agent already logged in ");
                return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED,"AGENT ALREADY LOGGED",null,null,false);
            }

            log.info("AgentOperationsService =>  loginControllerAgent() => validations are passed");
           return agentsRepository.updateAdminsLoggedStatusToTrue(1,dataControllerAgentAvailability.getId()) != 1
                   ?
                   commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED,"LOGIN UPDATING FAILED",null,null,false)
                   :
                   commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS,"agent LOGGED IN SUCCESSFULLY",dataControllerAgentAvailability,null,false);

        }catch (Exception e){
            log.info("AgentOperationsService =>  loginControllerAgent() => Failed to process");
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED,"FAILED TO PROCESS",null,null,false);
        }
    }

    public ResponseEntity<?> updateAgentRecord (AgentRequestDto requestDto){
        try{
            log.info("AgentOperationsService =>  updateAgentRecord() =>"+requestDto);

            DataControllerAgentsEntity dataControllerAgentAvailability = agentsRepository.findById(requestDto.getAgentId()).orElse(null);
            if(Objects.isNull(dataControllerAgentAvailability)){
                log.info("AgentOperationsService =>  updateAgentRecord() => no agent exists");
                return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_EMPTY,"NO SUCH AGENT EXISTS",null,null,false);
            }

            log.info("AgentOperationsService =>  updateAgentRecord() => updating agent record");
            return agentsRepository.updateAgentRecord(requestDto.getUsername(),requestDto.getPassword(), requestDto.getAgentId()) > 0 ?
                    commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS,"AGENT UPDATED SUCCESSFULLY",dataControllerAgentAvailability,null,false)
                    :
                    commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED,"AGENT UPDATING FAILED",null,null,false);

        }catch (Exception e){
            log.info("AgentOperationsService =>  updateAgentRecord() => Failed to process");
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED,"FAILED TO PROCESS",null,null,false);
        }
    }



    public ResponseEntity<?> deleteAgentRecord(AgentRequestDto requestDto){
        try{
            log.info("AgentOperationsService =>  deleteAgentRecord() =>"+requestDto);

            DataControllerAgentsEntity dataControllerAgentAvailability = agentsRepository.findById(requestDto.getAgentId()).orElse(null);
            if(Objects.isNull(dataControllerAgentAvailability)){
                log.info("AgentOperationsService =>  deleteAgentRecord() => no agent exists");
                return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_EMPTY,"NO SUCH AGENT EXISTS",null,null,false);
            }

            log.info("AgentOperationsService =>  deleteAgentRecord() => deactivating agent record");
            return agentsRepository.deactivateAgentRecord(requestDto.getStatus(), requestDto.getAgentId()) > 0 ?
                    commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS,"AGENT DEACTIVATED SUCCESSFULLY",dataControllerAgentAvailability,null,false)
                    :
                    commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED,"AGENT DEACTIVATION FAILED",null,null,false);

        }catch (Exception e){
            log.info("AgentOperationsService =>  deleteAgentRecord() => Failed to process");
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED,"FAILED TO PROCESS",null,null,false);
        }
    }

    public ResponseEntity<?> getAgentData(AgentRequestDto requestDto) {
        log.info("AgentOperationsService =>  getAgentData() =>"+requestDto);
        try{
            DataControllerAgentsEntity dataControllerAgentAvailability = agentsRepository.findById(requestDto.getAgentId()).orElse(null);
            if(Objects.isNull(dataControllerAgentAvailability)){
                log.info("AgentOperationsService =>  getAgentData() => no agent exists");
                return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_EMPTY,"NO SUCH AGENT EXISTS",null,null,false);
            }

            if(!dataControllerAgentAvailability.getStatus().getCode().equals(Constants.ACTIVE)){
                log.info("AgentOperationsService =>  getAgentData() => Agent not active");
                return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED,"AGENT IS INACTIVE",null,null,false);
            }

            log.info("AgentOperationsService =>  getAgentData() => validations are passed");
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS,"AGENT RETRIEVED SUCCESSFULLY",dataControllerAgentAvailability,null,false);

        }catch (Exception e){
            log.info("AgentOperationsService =>  getAgentData() => Failed to process");
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED,"FAILED TO PROCESS",null,null,false);
        }
    }

}
