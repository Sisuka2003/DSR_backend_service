package com.iit.dsr.service;

import com.iit.dsr.dto.requests.admin.AdminRequestDto;
import com.iit.dsr.dto.responses.commons.AdminResponseDto;
import com.iit.dsr.entity.AdminEntity;
import com.iit.dsr.repository.AdminRepository;
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
public class AdminOperationsService {

    @Autowired
    private CommonUtils commonUtils;
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AdminResponseDto responseDto;

    public ResponseEntity<?> getAdminControllerDetails(AdminRequestDto requestDto) {
        try{
            log.info("AdminOperationsService =>  getAdminControllerDetails() => "+requestDto);
            AdminEntity adminFromUsernameAndPassword = adminRepository.getAdminFromUsernameAndPassword(requestDto.getUsername(), requestDto.getPassword());

            if(Objects.isNull(adminFromUsernameAndPassword)){
                log.info("AdminOperationsService =>  getAdminControllerDetails() => Empty admins");
                return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_EMPTY,"NO SUCH ADMINS EXISTS",null,null,false);
            }

            log.info("AdminOperationsService =>  getAdminControllerDetails() =>"+adminFromUsernameAndPassword.toString());
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "ADMIN LOGGED IN SUCCESSFULLY", new AdminResponseDto(adminFromUsernameAndPassword.getId(),adminFromUsernameAndPassword.getUsername(),adminFromUsernameAndPassword.getPassword(),adminFromUsernameAndPassword.getStatus().getDescription(), adminFromUsernameAndPassword.getUserRole().getRole(),adminFromUsernameAndPassword.getFirstName(),adminFromUsernameAndPassword.getLastName()), null, false);
        }catch (Exception e){
            e.printStackTrace();
            log.info("AdminOperationsService =>  getAdminControllerDetails() => Failed to process");
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED,"FAILED TO PROCESS",null,null,false);
        }
    }


    public ResponseEntity<?> updateAdminData(AdminRequestDto requestDto) {
        try{
            log.info("AdminOperationsService =>  udpateAdminData() => "+requestDto);
            AdminEntity adminFromId = adminRepository.findById(requestDto.getId()).orElse(null);

            if(Objects.isNull(adminFromId)){
                log.info("AdminOperationsService =>  udpateAdminData() => Empty admins");
                return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_EMPTY,"NO SUCH ADMINS EXISTS",null,null,false);
            }

            log.info("AdminOperationsService => updateAdminData() => Modifying admin details");
            return adminRepository.updateAdminData(requestDto.getFname(), requestDto.getLname(),requestDto.getUsername(),requestDto.getPassword(), requestDto.getId()) > 0 ? commonUtils.generateResponseObject(
                    Constants.RESPONSE_CODE_SUCCESS,
                    "ADMIN DATA UDPATED SUCCESSFULLY",
                    new AdminResponseDto(
                            adminFromId.getId(),
                            adminFromId.getUsername(),
                            adminFromId.getPassword(),
                            adminFromId.getStatus().getDescription(),
                            adminFromId.getUserRole().getRole(),
                            adminFromId.getFirstName(),
                            adminFromId.getLastName()),
                    null,
                    false)
                    :
                    commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED,"ADMIN MODIFICATION FAILED",null,null,false);

        }catch (Exception e){
            e.printStackTrace();
            log.info("AdminOperationsService =>  udpateAdminData() => Failed to process");
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED,"FAILED TO PROCESS",null,null,false);
        }
    }


    public ResponseEntity<?> addNewDataController(AdminRequestDto requestDto){
        try{
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS,"ADDED NEW DATA CONTROLLER",null,null,false);
        }catch (Exception e){
            e.printStackTrace();
            log.info("AdminOperationsService =>  addNewDataController() => Failed to process");
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED,"FAILED TO PROCESS",null,null,false);
        }
    }

}
