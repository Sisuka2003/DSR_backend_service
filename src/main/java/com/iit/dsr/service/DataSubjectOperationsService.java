package com.iit.dsr.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iit.dsr.controller.DataSubjectOperationsController;
import com.iit.dsr.dto.requests.login.DataSubjectLoginRequestDTO;
import com.iit.dsr.dto.requests.operations.DataSubjectOperationsRequestDto;
import com.iit.dsr.dto.responses.commons.CommonResponseDTO;
import com.iit.dsr.entity.DataSubjectInOrganizationEntity;
import com.iit.dsr.entity.DataSubjectsEntity;
import com.iit.dsr.repository.DataSubjectInControllerRepository;
import com.iit.dsr.repository.DataSubjectRepository;
import com.iit.dsr.repository.StatusRepository;
import com.iit.dsr.utils.CommonUtils;
import com.iit.dsr.utils.Constants;
import com.lowagie.text.*;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

@Service
@Log4j2
@Transactional
public class DataSubjectOperationsService {

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private DataSubjectInControllerRepository dataSubjectInControllerRepository;

    @Autowired
    private DataSubjectRepository dataSubjectRepository;
    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private JavaMailSender mailSender;

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
            log.info("DataSubjectOperationsService => updatedStoredData() => invoked with "+requestDto);

            if(requestDto.isAnAdmin()){
                log.info("DataSubjectOperationsService => updatedStoredData() => Is an admin");
                DataSubjectInOrganizationEntity dataSubjectInOrganizationEntity = dataSubjectInControllerRepository.findById(Integer.parseInt(requestDto.getRecordId())).orElse(null);

                if(Objects.isNull(dataSubjectInOrganizationEntity)){
                    log.info("DataSubjectOperationsService => updatedStoredData() => Data empty");
                    return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_EMPTY, "NO SUCH RECORD", null,null,false);
                }

                if(dataSubjectInOrganizationEntity.getActivityStatus().getCode().equals(Constants.REJECTED) || dataSubjectInOrganizationEntity.getActivityStatus().getCode().equals(Constants.QUEUED) ||
                        dataSubjectInOrganizationEntity.getSubjectActivityStatus().getCode().equals(Constants.CANCELLED)){

                    log.info("DataSubjectOperationsService => updatedStoredData() => a;ready cancelled / rejected");
                    return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "UNABLE TO PROCEED DUE TO AGENT / SUBJECT MODIFICATION", null,null,false);
                }

                log.info("DataSubjectOperationsService => updatedStoredData => controller requesting to accept or reject request");
                return dataSubjectInOrganizationEntity.getActivityStatus().getCode().equals(Constants.APPROVED) || dataSubjectInOrganizationEntity.getActivityStatus().getCode().equals(Constants.REJECTED) ?
                        dataSubjectInControllerRepository.updateDataSubjectDataOnApproval(requestDto.getCollectedData(),statusRepository.getStatusRecordFromCode(Constants.APPROVED).getId(), Integer.parseInt(requestDto.getRecordId()), Integer.parseInt(requestDto.getStatus())) > 0 ? commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "DATA CORRECTION SUCCESS", null,null,false) : commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "DATA CORRECTION FAILED", null,null,false)
                :
                        dataSubjectInControllerRepository.updateDataSubjectDataOnApprovalSkipped(requestDto.getCollectedData(),statusRepository.getStatusRecordFromCode(Constants.APPROVED).getId(),statusRepository.getStatusRecordFromCode(Constants.SKIPPED).getId(), Integer.parseInt(requestDto.getRecordId()), Integer.parseInt(requestDto.getStatus())) > 0 ? commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "DATA CORRECTION SUCCESS", null,null,false) : commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "DATA CORRECTION FAILED", null,null,false);
            }
            return dataSubjectInControllerRepository.updateCollectedDataOfDataSubject(requestDto.getCollectedData(), Integer.parseInt(requestDto.getDsCode()), Integer.parseInt(requestDto.getDcCode()), Integer.parseInt(requestDto.getStatus())) > 0 ? commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "DATA CORRECTION SUCCESS", null,null,false) : commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "DATA CORRECTION FAILED", null,null,false);
        }catch (Exception e){
            log.info("DataSubjectOperationsService => updatedStoredData => Failed To Process");
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


    public ResponseEntity<?> sendOtpEmail(DataSubjectOperationsRequestDto requestDto) {

        try {
            String code = CommonUtils.generateOtp();

            int modifiedOtpCodeStatus = dataSubjectRepository.updateTheOtpCodeColumnIfUserVerificationIsFalse(code, Integer.parseInt(requestDto.getDsCode()), Constants.ACTIVE);

            if(modifiedOtpCodeStatus == 0){
                return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "VERIFICATION PROCEDURE FAILED", null, null, false);
            }

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("sisukaweerasinghe@gmail.com");
            helper.setTo(requestDto.getRecipientEmail());
            helper.setSubject("OTP Verification");

            try (var inputStream = Objects.requireNonNull(
                    DataSubjectOperationsController.class.getResourceAsStream("/templates/OtpEmailTemplate.html"))) {

                String htmlTemplate = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

                htmlTemplate = htmlTemplate.replace("{{userName}}", "Sisuka Weerasinghe")
                        .replace("{{otp}}", code)
                        .replace("{{expiryMinutes}}", "5");


                helper.setText(htmlTemplate, true);
            }
            File logoFile = new File("src/main/resources/static/images/emblem.png"); // path to your logo
            helper.addInline("companyLogo", logoFile);

            mailSender.send(message);
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "SUCCESS TO PROCESS", code, null, false);
        } catch (Exception e) {
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "FAILED TO PROCESS", null, null, false);
        }
    }


    public ResponseEntity<?> verifyOtpCode(DataSubjectOperationsRequestDto requestDto){
        try{
            return !requestDto.getOtpCode().equals(dataSubjectRepository.findDataSubjectByIdAndActiveStatus(Integer.parseInt(requestDto.getDsCode()), Constants.ACTIVE).getOtpCode()) ? commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "OTP CODE MISMATCH", null, null, false) : dataSubjectRepository.updateTheOtpCodeColumnIfUserVerificationIsFalse("", Integer.parseInt(requestDto.getDsCode()), Constants.ACTIVE) == 0 ? commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "VERIFICATION PROCEDURE FAILED", null, null, false) : commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "OTP VERIFIED SUCCESSFULLY", null, null, false);
        }catch (Exception e){
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "FAILED TO PROCESS", null, null, false);
        }
    }

    public ResponseEntity<?> requestDataSubjectRelatedDataFromOrganization(DataSubjectOperationsRequestDto requestDto) {
        try{
            log.info("DataSubjectOperationsService => requestDataSubjectRelatedDataFromOrganization() => invoked with "+requestDto);
            List<DataSubjectInOrganizationEntity> customerRecordFromOrganization = dataSubjectInControllerRepository.getCustomerRecordFromCustomerMapped(Integer.parseInt(requestDto.getDsCode()), Constants.ACTIVE);
            return Objects.isNull(customerRecordFromOrganization) ? commonUtils.generateResponseObject(Constants.RESPONSE_CODE_EMPTY, "NO DATA EXISTS", null,null,false) : commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS, "DATA FETCHED SUCCESSFULLY", customerRecordFromOrganization,null,false);
        }catch (Exception e){
            log.info("DataSubjectOperationsService => requestDataSubjectRelatedDataFromOrganization() => Failed To process");
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED, "FAILED TO PROCESS", null, null, false);
        }
    }
}
