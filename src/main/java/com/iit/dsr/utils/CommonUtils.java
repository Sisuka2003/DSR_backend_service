package com.iit.dsr.utils;

import com.iit.dsr.dto.responses.commons.CommonResponseDTO;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Log4j2
public class CommonUtils {

    @Autowired
    private CommonResponseDTO responseDTO;

    public ResponseEntity<?> generateResponseObject(String responseStatusCode, String responseMessage, Object responseAttachments, byte[] report, Boolean reportGenerationEnabled){
        return reportGenerationEnabled ?
                ResponseEntity.
                        status(responseStatusCode.equals(Constants.RESPONSE_CODE_SUCCESS) ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=DSR-report.pdf")
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(report)
                :
                ResponseEntity.
                status(responseStatusCode.equals(Constants.RESPONSE_CODE_SUCCESS) ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
                .body(new CommonResponseDTO(responseStatusCode,responseMessage,responseAttachments,report));
    }

    public PdfPTable generateReportTable(int numColumns, int widthPercentage, int spacingBefore, ArrayList<PdfPCell> data){
        PdfPTable dRDataTable1 = new PdfPTable(numColumns);
        dRDataTable1.setWidthPercentage(widthPercentage);
        dRDataTable1.setHorizontalAlignment(Element.ALIGN_LEFT);
        dRDataTable1.setSpacingBefore(spacingBefore);

        for(PdfPCell record : data){
            dRDataTable1.addCell(record);
        }

        return dRDataTable1;
    }

    public PdfPCell generateReportTableCell(String cellText){
        PdfPCell dRDataCell = new PdfPCell(new Phrase(cellText));
        dRDataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        dRDataCell.setBorder(Rectangle.NO_BORDER);

        return dRDataCell;
    }
}
