package com.iit.dsr.service;

import com.iit.dsr.dto.requests.login.DataSubjectLoginRequestDTO;
import com.iit.dsr.dto.requests.login.DataControllerLoginRequestDTO;
import com.iit.dsr.dto.responses.commons.CommonResponseDTO;
import com.iit.dsr.entity.DataControllerEntity;
import com.iit.dsr.entity.DataSubjectInOrganizationEntity;
import com.iit.dsr.entity.DataSubjectsEntity;
import com.iit.dsr.repository.DataControllerRepository;
import com.iit.dsr.repository.DataSubjectInControllerRepository;
import com.iit.dsr.repository.DataSubjectRepository;
import com.iit.dsr.utils.CommonUtils;
import com.iit.dsr.utils.Constants;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Log4j2
@Transactional
public class LoginService {


    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private DataControllerRepository dataControllerRepository;
    @Autowired
    private DataSubjectRepository dataSubjectRepository;

    @Autowired
    private DataSubjectInControllerRepository dataSubjectInControllerRepository;

    //Data Subject / User login service - DSR portal
    public ResponseEntity<?> dataSubjectLogin(DataSubjectLoginRequestDTO dsLoginDto) {
        try{
            int dataControllerId = Integer.parseInt(dsLoginDto.getOrganizationID());
            DataControllerEntity dataControllerRecord = dataControllerRepository.findById(dataControllerId).orElse(null);

            if(Objects.isNull(dataControllerRecord)){
                return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_EMPTY, "ORGANIZATION DOES NOT EXIST", null,null,false);
            }
           DataSubjectsEntity dataSubjectsEntity = null;
               switch(dataControllerRecord.getIdentificationKey().getCode()) {
                   case Constants.NIC:
                       log.info("Retrieve via NIC");
                       dataSubjectsEntity = dataSubjectRepository.getCustomerRecordFromNIC(dsLoginDto.getNicNumber());
                       break;
                   case Constants.EMAIL:
                       log.info("Retrieve via EMAIL");
                       dataSubjectsEntity = dataSubjectRepository.getCustomerRecordFromEMAIL(dsLoginDto.getEmailAddress());
                       break;
                   case Constants.MOBILE:
                       log.info("Retrieve via MOBILE");
                       dataSubjectsEntity = dataSubjectRepository.getCustomerRecordFromMOBILE(dsLoginDto.getMobileNumber());
                       break;
                   case Constants.CUSTOMERID:
                       log.info("Retrieve via CUSTID");
                       dataSubjectsEntity = dataSubjectRepository.getCustomerRecordFromCUSTOMERID(dsLoginDto.getCustomerID());
                       break;
               }

            if(Objects.isNull(dataSubjectsEntity)){
                log.info("data subject is empty");
                return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_EMPTY, "SUBJECT DOES NOT EXISTS", null,null,false);
            }

            List<DataSubjectInOrganizationEntity> dataSubjectInOrganizationRecordList = dataSubjectInControllerRepository.getDataSubjectLoginData(dataSubjectsEntity.getId(),dataControllerId, Constants.ACTIVE, Constants.APPROVED,Constants.APPROVED);
            DataSubjectInOrganizationEntity dataSubjectApprovedLoginData = null;
            if(dataSubjectInOrganizationRecordList.size() > 1) {
                log.info(" multiple records exists");
                 dataSubjectApprovedLoginData = dataSubjectInControllerRepository.getDataONlyAdminApprovalsArePassed(
                         dataSubjectsEntity.getId(),
                         dataControllerId,
                         Constants.ACTIVE,
                         Constants.APPROVED);
            }else{
                dataSubjectApprovedLoginData = dataSubjectInOrganizationRecordList.get(0);
            }
            log.info("data subject record :"+dataSubjectApprovedLoginData);
            if(Objects.isNull(dataSubjectApprovedLoginData)){
                return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_EMPTY, "SUBJECT DOES NOT EXISTS WITHIN THE ORGANIZATION", null,null,false);
            }

            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "SUCCESS", dataSubjectApprovedLoginData,null,false);
        }catch (Exception e) {
            e.printStackTrace();
           return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "FAILED TO PROCESS", null,null,false);
        }
    }


    //Data Controller / organization login service - DSR portal
    public ResponseEntity<?> dataControllerLogin(DataControllerLoginRequestDTO dcLoginDto){
        try{
                log.info("dataControllerLogin => Invoked");
                String orgUsername = dcLoginDto.getOrgUsername();
                String orgPassword = dcLoginDto.getOrgPassword();

                DataControllerEntity dataControllerEntity = dataControllerRepository.getOrganizationByUsernameAndPassword(orgUsername, orgPassword, Constants.ACTIVE);

                if (Objects.isNull(dataControllerEntity)) {
                    log.info("dataControllerLogin => check existence with ID");
                    return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_EMPTY, "SELECTED ORGANIZATION DOES NOT EXIST", null, null, false);
                }

            if(!dcLoginDto.isNotificationCountRetrieval()) {
                dataControllerRepository.updateAvailableRecordsForOrganization(dataControllerEntity.getId(), Constants.ACTIVE, dataControllerEntity.getId(), Constants.ACTIVE);
            }

            log.info("dataControllerLogin => organization exist with username & password");
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "SUCCESS", dataControllerEntity,null,false);
        }catch (Exception e){
            log.info("dataControllerLogin => Failed to process");
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "FAILED TO PROCESS", null,null,false);
        }
    }
}
