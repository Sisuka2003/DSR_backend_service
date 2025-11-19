package com.iit.dsr.service;

import com.iit.dsr.dto.requests.login.DataSubjectLoginRequestDTO;
import com.iit.dsr.dto.requests.operations.DataSubjectOperationsRequestDto;
import com.iit.dsr.dto.responses.commons.CommonResponseDTO;
import com.iit.dsr.entity.DataSubjectInOrganizationEntity;
import com.iit.dsr.repository.DataSubjectInControllerRepository;
import com.iit.dsr.utils.CommonUtils;
import com.iit.dsr.utils.Constants;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

@Service
@Log4j2
@Transactional
public class DataSubjectOperationsService {

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private DataSubjectInControllerRepository dataSubjectInControllerRepository;

    //Get Data Subjects Collected_data from the organization
    public ResponseEntity<?> getStoredData(DataSubjectOperationsRequestDto requestDto) {
        try {
            DataSubjectInOrganizationEntity customerRecordFromOrganization = dataSubjectInControllerRepository.getCustomerRecordFromOrganization(Integer.parseInt(requestDto.getDsCode()), Integer.parseInt(requestDto.getDcCode()), Constants.ACTIVE);
            return Objects.isNull(customerRecordFromOrganization) ? commonUtils.generateResponseObject(Constants.RESPONSE_CODE_EMPTY, "NO DATA EXISTS", null,null,false) : commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "DATA FETCHED SUCCESSFULLY", customerRecordFromOrganization,null,false);
        }catch (Exception e){
            log.info("getStoredData => Failed To Process");
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "FAILED TO PROCESS", null,null,false);

        }
    }

    //Update Data Subjects Collected_data from the organization
    public ResponseEntity<?> updatedStoredData(DataSubjectOperationsRequestDto requestDto) {
        try {
            return dataSubjectInControllerRepository.updateCollectedDataOfDataSubject(requestDto.getCollectedData()) > 0 ? commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "DATA CORRECTION SUCCESS", null,null,false) : commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "DATA CORRECTION FAILED", null,null,false);
        }catch (Exception e){
            log.info("updatedStoredData => Failed To Process");
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "FAILED TO PROCESS", null,null,false);

        }
    }

    //Delete Data Subjects Collected_data from the organization
    public ResponseEntity<?> deleteStoredData(DataSubjectOperationsRequestDto requestDto) {
        try {
            return dataSubjectInControllerRepository.deleteCollectedDataOfDataSubject(requestDto.getMapId()) > 0 ? commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "DATA CORRECTION SUCCESS", null,null,false) : commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "DATA CORRECTION FAILED", null,null,false);
        }catch (Exception e){
            log.info("deleteStoredData => Failed To Process");
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "FAILED TO PROCESS", null,null,false);
        }
    }


    //Generate Report
    public ResponseEntity<?> generateDataExportReport(DataSubjectOperationsRequestDto requestDto) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            Document document = new Document();
            PdfWriter.getInstance(document, baos);

            document.open();
            document.add(new Paragraph("dfgdgd dgdfgfd dfgdf dgdfgdf"));
            document.close();

            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "REPORT GENERATION SUCCESS", null,baos.toByteArray(),true);
        }catch (Exception e){
            log.info("generateDataExportReport => Failed To Process");
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "FAILED TO PROCESS", null,null,false);
        }
    }

}
