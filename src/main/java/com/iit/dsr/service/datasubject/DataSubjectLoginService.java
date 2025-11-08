package com.iit.dsr.service.datasubject;

import com.iit.dsr.dto.requests.dataSubject.DataSubjectLoginRequestDTO;
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

import java.util.Objects;

@Service
@Log4j2
public class DataSubjectLoginService {


    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private DataControllerRepository dataControllerRepository;
    @Autowired
    private DataSubjectRepository dataSubjectRepository;

    @Autowired
    private DataSubjectInControllerRepository dataSubjectInControllerRepository;


    public ResponseEntity<CommonResponseDTO> dataSubjectLogin(DataSubjectLoginRequestDTO dsLoginDto) {
        try{
            int dataControllerId = Integer.parseInt(dsLoginDto.getOrganizationID());
            DataControllerEntity dataControllerRecord = dataControllerRepository.findById(dataControllerId).orElse(null);

            if(Objects.isNull(dataControllerRecord)){
                return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_EMPTY, "ORGANIZATION DOES NOT EXIST", null);
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
                return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_EMPTY, "SUBJECT DOES NOT EXISTS", null);
            }

            DataSubjectInOrganizationEntity dataSubjectInOrganizationRecord = dataSubjectInControllerRepository.getCustomerRecordFromOrganization(dataSubjectsEntity.getId(),dataControllerId, Constants.ACTIVE);
            if(Objects.isNull(dataSubjectInOrganizationRecord)){
                return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_EMPTY, "SUBJECT DOES NOT EXISTS WITHIN THE ORGANIZATION", null);
            }



            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "SUCCESS", dataSubjectInOrganizationRecord);
        }catch (Exception e) {
            e.printStackTrace();
           return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "FAILED TO PROCESS", null);
        }
    }
}
