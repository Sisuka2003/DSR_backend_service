package com.iit.dsr.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iit.dsr.dto.requests.operations.DataControllerOperationsRequestDto;
import com.iit.dsr.dto.requests.operations.DataSubjectOperationsRequestDto;
import com.iit.dsr.entity.DataControllerEntity;
import com.iit.dsr.entity.DataSubjectInOrganizationEntity;
import com.iit.dsr.entity.StatusEntity;
import com.iit.dsr.repository.DataControllerRepository;
import com.iit.dsr.repository.DataSubjectInControllerRepository;
import com.iit.dsr.repository.KeyIdenticationKeyRepository;
import com.iit.dsr.repository.StatusRepository;
import com.iit.dsr.utils.CommonUtils;
import com.iit.dsr.utils.Constants;
import com.lowagie.text.*;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Log4j2
@Transactional
public class DataControllerOperationsService {

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private DataControllerRepository dataControllerRepository;

    @Autowired
    private DataSubjectInControllerRepository dataSubjectInControllerRepository;

    @Autowired
    private KeyIdenticationKeyRepository keyIdenticationKeyRepository;

    @Autowired
    private StatusRepository statusRepository;

    public ResponseEntity<?> getStoredData(DataControllerOperationsRequestDto requestDto) {
        try {
            log.info("DataControllerOperationsService => getStoredData() => invoked with "+requestDto);
            DataControllerEntity customerRecordFromOrganization = dataControllerRepository.getControllerData(Integer.parseInt(requestDto.getDcCode()), Constants.ACTIVE);
            return Objects.isNull(customerRecordFromOrganization) ? commonUtils.generateResponseObject(Constants.RESPONSE_CODE_EMPTY, "NO DATA EXISTS", null,null,false) : commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "DATA FETCHED SUCCESSFULLY", customerRecordFromOrganization,null,false);
        }catch (Exception e){
            log.info("DataControllerOperationsService => getStoredData() => Failed To Process");
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "FAILED TO PROCESS", null,null,false);

        }
    }

    public ResponseEntity<?> updatedStoredData(DataControllerOperationsRequestDto requestDto) {
        try {
            log.info("DataControllerOperationsService => updatedStoredData() => invoekd with "+requestDto);
            return dataControllerRepository.updateDataController(requestDto.getUsername(), requestDto.getPassword(), Integer.parseInt(requestDto.getDcCode()), Constants.ACTIVE) > 0 ? commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "DATA CORRECTION SUCCESS", null,null,false) : commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "DATA CORRECTION FAILED", null,null,false);
        }catch (Exception e){
            log.info("DataControllerOperationsService => updatedStoredData() => Failed To Process");
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "FAILED TO PROCESS", null,null,false);

        }
    }

    public ResponseEntity<?> deleteStoredData(DataControllerOperationsRequestDto requestDto) {
        try {
            log.info("DataControllerOperationsService => deleteStoredData() => invoked with "+requestDto);
            return dataControllerRepository.deleteControllerRecord(Integer.parseInt(requestDto.getDcCode()), Integer.parseInt(requestDto.getStatus())) > 0 ? commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "DATA CORRECTION SUCCESS", null,null,false) : commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "DATA CORRECTION FAILED", null,null,false);
        }catch (Exception e){
            log.info("DataControllerOperationsService => deleteStoredData() => Failed To Process");
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "FAILED TO PROCESS", null,null,false);
        }
    }

    public ResponseEntity<?> getDataSubjectDataOfController(DataControllerOperationsRequestDto requestDto) {
        try {
            log.info("DataControllerOperationsService => getDataSubjectDataOfController() => invoked with "+requestDto);
            List<DataSubjectInOrganizationEntity> customerRecordFromOrganization = null;
            if(!requestDto.getIsTaskAssign()){
                customerRecordFromOrganization = requestDto.getIsAgentAlert() ?
                        dataSubjectInControllerRepository.getCustomerRecordFromAgentMapped(Integer.parseInt(requestDto.getAgentCode()), Integer.parseInt(requestDto.getDcCode()), Constants.ACTIVE)
                        :
                        dataSubjectInControllerRepository.getCustomerRecordFromOrganizationMapped(Integer.parseInt(requestDto.getDcCode()), Constants.ACTIVE);
            }else {
                log.info("Retreiving only the pending records");
                customerRecordFromOrganization = requestDto.getIsAgentAlert() ?
                        dataSubjectInControllerRepository.getCustomerRecordFromAgentMappedTask(
                                Integer.parseInt(requestDto.getAgentCode()),
                                Integer.parseInt(requestDto.getDcCode()),
                                Constants.ACTIVE,
                                Constants.PENDING)
                        :
                        dataSubjectInControllerRepository.getCustomerRecordFromOrganizationMappedTask(
                                Integer.parseInt(requestDto.getDcCode()),
                                Constants.ACTIVE,
                                Constants.PENDING);
            }
            return Objects.isNull(customerRecordFromOrganization) ? commonUtils.generateResponseObject(Constants.RESPONSE_CODE_EMPTY, "NO DATA EXISTS", null,null,false) : commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "DATA FETCHED SUCCESSFULLY", customerRecordFromOrganization,null,false);
        }catch (Exception e){
            log.info("DataControllerOperationsService => getDataSubjectDataOfController() => Failed To Process");
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "FAILED TO PROCESS", null,null,false);

        }
    }

    public ResponseEntity<?> rejectDataSubjectDataModificationOrDeletionRequest(DataControllerOperationsRequestDto requestDto){
        try {
            log.info("DataControllerOperationsService => rejectDataSubjectDataModificationOrDeletionRequest() => invoked with "+requestDto);
            if(requestDto.getIsUserProfile()){
                return dataSubjectInControllerRepository.rejectDataSubjectDataModificationOrDeletionRequestForUserOnly(
                        statusRepository.getStatusRecordFromCode(Constants.REJECTED).getId(),
                        statusRepository.getStatusRecordFromCode(Constants.REJECTED).getId(),
                        statusRepository.getStatusRecordFromCode(Constants.REJECTED).getId(),
                        Integer.parseInt(requestDto.getId())) > 0 ?
                        commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "DATA CORRECTION SUCCESS", null,null,false)
                        :
                        commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "DATA CORRECTION FAILED", null,null,false);
            }
            return dataSubjectInControllerRepository.rejectDataSubjectDataModificationOrDeletionRequest(
                    statusRepository.getStatusRecordFromCode(Constants.REJECTED).getId(),
                    statusRepository.getStatusRecordFromCode(Constants.REJECTED).getId(),
                    Integer.parseInt(requestDto.getId()),
                    Integer.parseInt(requestDto.getStatus())) > 0 ?

                    commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "DATA CORRECTION SUCCESS", null,null,false)
                    :
                    commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "DATA CORRECTION FAILED", null,null,false);
        }catch (Exception e){
            log.info("DataControllerOperationsService => rejectDataSubjectDataModificationOrDeletionRequest() => Failed To Process");
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "FAILED TO PROCESS", null,null,false);
        }
    }

    public ResponseEntity<?> addNewDataControllerRequestDto(DataControllerOperationsRequestDto requestDto) {
        try {
            log.info("DataControllerOperationsService => addnewDataControllerRequestDto() => invoked with "+requestDto);
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            DataControllerEntity dce =new DataControllerEntity();
            dce.setOrgName(requestDto.getOrgName());
            dce.setOrgStatus(statusRepository.getStatusRecordFromCode(Constants.ACTIVE));
            dce.setAgents(Integer.parseInt(requestDto.getAgentCount()));
            dce.setNotifications(0);
            dce.setEmailAddress(requestDto.getOrgEmailAddress());
            dce.setLastUpdatedTime(currentTime);
            dce.setCreatedTime(currentTime);
            dce.setIdentificationKey(keyIdenticationKeyRepository.findById(Integer.parseInt(requestDto.getIdKey())).orElse(null));
            dce.setOrgUsername(requestDto.getUsername());
            dce.setOrgPassword(requestDto.getPassword());

            dataControllerRepository.save(dce);
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "SUCCESSFULLY SAVED", null,null,false);
        }catch (Exception e){
            log.info("DataControllerOperationsService => addnewDataControllerRequestDto() => Failed To Process");
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "FAILED TO PROCESS", null,null,false);
        }
    }
}
