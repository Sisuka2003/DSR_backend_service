package com.iit.dsr.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iit.dsr.dto.requests.login.DataSubjectLoginRequestDTO;
import com.iit.dsr.dto.requests.operations.DataSubjectOperationsRequestDto;
import com.iit.dsr.dto.responses.commons.CommonResponseDTO;
import com.iit.dsr.entity.DataSubjectInOrganizationEntity;
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
public class DataSubjectOperationsService {

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private DataSubjectInControllerRepository dataSubjectInControllerRepository;

    //Get Data Subjects Collected_data from the organization
    public ResponseEntity<?> getStoredData(DataSubjectOperationsRequestDto requestDto) {
        try {
            log.info("DataSubjectOperationsService => getStoredData() => invoked with "+requestDto);
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

            log.info("DataSubjectOperationsService => updatedStoredData() => invoekd with "+requestDto);
            return dataSubjectInControllerRepository.updateCollectedDataOfDataSubject(requestDto.getCollectedData(), Integer.parseInt(requestDto.getDsCode()), Integer.parseInt(requestDto.getDcCode()), Integer.parseInt(requestDto.getStatus())) > 0 ? commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "DATA CORRECTION SUCCESS", null,null,false) : commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "DATA CORRECTION FAILED", null,null,false);
        }catch (Exception e){
            log.info("updatedStoredData => Failed To Process");
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "FAILED TO PROCESS", null,null,false);

        }
    }

    //Delete Data Subjects Collected_data from the organization
    public ResponseEntity<?> deleteStoredData(DataSubjectOperationsRequestDto requestDto) {
        try {
            return dataSubjectInControllerRepository.deleteCollectedDataOfDataSubject(Integer.parseInt(requestDto.getDsCode()), Integer.parseInt(requestDto.getDcCode()), Integer.parseInt(requestDto.getStatus())) > 0 ? commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "DATA CORRECTION SUCCESS", null,null,false) : commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "DATA CORRECTION FAILED", null,null,false);
        }catch (Exception e){
            log.info("deleteStoredData => Failed To Process");
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "FAILED TO PROCESS", null,null,false);
        }
    }


    //Generate Report
    public ResponseEntity<?> generateDataExportReport(DataSubjectOperationsRequestDto requestDto) {
        try {

            DataSubjectInOrganizationEntity customerRecordFromOrganization = dataSubjectInControllerRepository.getCustomerRecordFromOrganization(Integer.parseInt(requestDto.getDsCode()), Integer.parseInt(requestDto.getDcCode()), Constants.ACTIVE);
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> collectedData = objectMapper.readValue(
                    customerRecordFromOrganization.getCollectedData(),
                    new TypeReference<Map<String, Object>>() {}
            );

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            // header - START ----------------------------------------------
            String imagePath = "src/main/resources/static/images/emblem.png";
            Image emblem = Image.getInstance(imagePath);
            emblem.scaleToFit(60, 60);
            emblem.setAlignment(Element.ALIGN_CENTER);


            Paragraph propertyOfSriLankaTitle = new Paragraph("This is a Property of the Sri Lanka Government");
            propertyOfSriLankaTitle.setAlignment(Element.ALIGN_CENTER);
            propertyOfSriLankaTitle.setSpacingBefore(5);

            Paragraph DSRTitle = new Paragraph("Data Subject Request Portal");
            DSRTitle.setAlignment(Element.ALIGN_CENTER);

            Paragraph YearTitle = new Paragraph("2025/2026");
            YearTitle.setAlignment(Element.ALIGN_CENTER);
            // header - END ----------------------------------------------

            //Body - START -----------------------------------------------

            PdfPTable dRInformationTable = new PdfPTable(1);
            dRInformationTable.setWidthPercentage(100);
            dRInformationTable.setSpacingBefore(14);

            PdfPCell dRInformationTableCell = new PdfPCell(new Phrase("DATA SUBJECT REQUEST DETAILS"));
            dRInformationTableCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            dRInformationTableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            dRInformationTableCell.setPadding(10);
            dRInformationTableCell.setBackgroundColor(Color.pink);
            dRInformationTableCell.setBorder(Rectangle.NO_BORDER);

            dRInformationTable.addCell(dRInformationTableCell);



            Paragraph subjectNameText = new Paragraph("   Subject Name : "+customerRecordFromOrganization.getDsCode().getFirstName()+customerRecordFromOrganization.getDsCode().getLastName());
            subjectNameText.setSpacingBefore(15);
            Paragraph subjectNicText = new Paragraph("   Subject NIC     : "+customerRecordFromOrganization.getDsCode().getNicNumber());
            Paragraph organizationText = new Paragraph("   Request Code : ");
            organizationText.setSpacingAfter(15);



            PdfPTable dRInformationDataTitle = new PdfPTable(1);
            dRInformationDataTitle.setWidthPercentage(100);
            dRInformationDataTitle.setSpacingBefore(15);
            dRInformationDataTitle.setSpacingAfter(15);
            PdfPCell dRInformationDataCell = new PdfPCell(new Phrase("Data Subject Information"));
            dRInformationDataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            dRInformationDataCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            dRInformationDataCell.setPadding(10);
            dRInformationDataCell.setBackgroundColor(Color.pink);
            dRInformationDataCell.setBorder(Rectangle.NO_BORDER);
            dRInformationDataTitle.addCell(dRInformationDataCell);





            document.add(emblem);
            document.add(propertyOfSriLankaTitle);
            document.add(DSRTitle);
            document.add(YearTitle);
            document.add(dRInformationTable);
            document.add(subjectNameText);
            document.add(subjectNicText);
            document.add(organizationText);
            document.add(dRInformationDataTitle);


            for (Map.Entry<String, Object> entry : collectedData.entrySet()) {
                Object value = entry.getValue();
                ArrayList<PdfPCell> pdfPCells = new ArrayList<>();

                PdfPCell titleCell = new PdfPCell(new Phrase(commonUtils.titleText(entry.getKey().toUpperCase())));
                titleCell.setBackgroundColor(Color.LIGHT_GRAY);
                titleCell.setPadding(5);
                titleCell.setPaddingBottom(8);
                titleCell.setColspan(2); // Span across two columns
                PdfPTable titleTable = new PdfPTable(2);
                titleTable.setWidthPercentage(100);
                titleTable.addCell(titleCell);
                document.add(titleTable);

                if (value instanceof Map) {
                    // It's already a nested Map
                    Map<String, Object> nestedMap = (Map<String, Object>) value;
                    for (Map.Entry<String, Object> nestedEntry : nestedMap.entrySet()) {
                        pdfPCells.add(commonUtils.generateReportTableCell("   " + nestedEntry.getKey() + " :"));
                        pdfPCells.add(commonUtils.generateReportTableCell(String.valueOf(nestedEntry.getValue())));
                    }
                } else if (value instanceof String) {

                    // Sometimes JSON libraries return Strings; parse them if necessary
                    Map<String, Object> nestedMap = objectMapper.readValue((String) value, new TypeReference<Map<String, Object>>() {});
                    for (Map.Entry<String, Object> nestedEntry : nestedMap.entrySet()) {
                        pdfPCells.add(commonUtils.generateReportTableCell("   " + nestedEntry.getKey() + " :"));
                        pdfPCells.add(commonUtils.generateReportTableCell(String.valueOf(nestedEntry.getValue())));
                    }
                } else {

                    // Primitive value
                    pdfPCells.add(commonUtils.generateReportTableCell("   " + entry.getKey() + " :"));
                    pdfPCells.add(commonUtils.generateReportTableCell(String.valueOf(entry.getValue())));
                }


                PdfPTable table = commonUtils.generateReportTable(2, 50, 15, pdfPCells);
                document.add(table);
            }
            //Body - END -----------------------------------------------
            document.close();

            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "REPORT GENERATION SUCCESS", null,baos.toByteArray(),true);
        }catch (Exception e){
            log.info("generateDataExportReport => Failed To Process");
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "FAILED TO PROCESS", null,null,false);
        }
    }



}
