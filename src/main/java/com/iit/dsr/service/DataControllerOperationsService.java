package com.iit.dsr.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iit.dsr.dto.requests.operations.DataControllerOperationsRequestDto;
import com.iit.dsr.dto.requests.operations.DataSubjectOperationsRequestDto;
import com.iit.dsr.entity.DataControllerEntity;
import com.iit.dsr.entity.DataSubjectInOrganizationEntity;
import com.iit.dsr.repository.DataControllerRepository;
import com.iit.dsr.repository.DataSubjectInControllerRepository;
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
import java.util.ArrayList;
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

    public ResponseEntity<?> getStoredData(DataControllerOperationsRequestDto requestDto) {
        try {
            log.info("DataControllerOperationsService => getStoredData() => invoked with "+requestDto);
            DataControllerEntity customerRecordFromOrganization = dataControllerRepository.getControllerData(Integer.parseInt(requestDto.getDcCode()), Constants.ACTIVE);
            return Objects.isNull(customerRecordFromOrganization) ? commonUtils.generateResponseObject(Constants.RESPONSE_CODE_EMPTY, "NO DATA EXISTS", null,null,false) : commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "DATA FETCHED SUCCESSFULLY", customerRecordFromOrganization,null,false);
        }catch (Exception e){
            log.info("getStoredData => Failed To Process");
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "FAILED TO PROCESS", null,null,false);

        }
    }

    public ResponseEntity<?> updatedStoredData(DataControllerOperationsRequestDto requestDto) {
        try {

            log.info("DataControllerOperationsService => updatedStoredData() => invoekd with "+requestDto);
            return dataControllerRepository.updateDataController(requestDto.getUsername(), requestDto.getPassword(), Integer.parseInt(requestDto.getDcCode()), Constants.ACTIVE) > 0 ? commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "DATA CORRECTION SUCCESS", null,null,false) : commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "DATA CORRECTION FAILED", null,null,false);
        }catch (Exception e){
            log.info("updatedStoredData => Failed To Process");
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "FAILED TO PROCESS", null,null,false);

        }
    }

    public ResponseEntity<?> deleteStoredData(DataControllerOperationsRequestDto requestDto) {
        try {
            return dataControllerRepository.deleteControllerRecord(Integer.parseInt(requestDto.getDcCode()), Integer.parseInt(requestDto.getStatus())) > 0 ? commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "DATA CORRECTION SUCCESS", null,null,false) : commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "DATA CORRECTION FAILED", null,null,false);
        }catch (Exception e){
            log.info("DataControllerOperationsService => del => Failed To Process");
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "FAILED TO PROCESS", null,null,false);
        }
    }




}
