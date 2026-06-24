package com.iit.dsr.schedulers;

import com.iit.dsr.controller.DataSubjectOperationsController;
import com.iit.dsr.entity.DataControllerEntity;
import com.iit.dsr.entity.DataSubjectInOrganizationEntity;
import com.iit.dsr.repository.DataControllerRepository;
import com.iit.dsr.repository.DataSubjectInControllerRepository;
import com.iit.dsr.utils.Constants;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
@Log4j2
@Component
@Transactional
public class NotifyControllerScheduler {

    @Autowired
    private DataControllerRepository dataControllerRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private DataSubjectInControllerRepository dataSubjectInControllerRepository;


    @Scheduled(cron = "${scheduler.midnight.cron}", zone = "${scheduler.operating.zone}")
    public void runAtMidnightOnEveryDay() throws Exception {

        List<DataControllerEntity> allActiveDataControllers = dataControllerRepository.getAllActiveDataControllers(Constants.ACTIVE);

        for (DataControllerEntity dce : allActiveDataControllers) {

            List<DataSubjectInOrganizationEntity> pendingDSRs = dataSubjectInControllerRepository.getPendingDSRsFromDataController(dce.getId(), Constants.PENDING, Constants.ACTIVE);

            StringBuilder tableRows = new StringBuilder();
            int rowNum = 1;

            for (DataSubjectInOrganizationEntity dsr : pendingDSRs) {

                LocalDate createdDate = dsr.getCreatedTime()
                        .toLocalDateTime()
                        .toLocalDate();

                long daysPending = ChronoUnit.DAYS.between(createdDate, LocalDate.now());

                LocalDate dueDate = createdDate.plusDays(Constants.SLA_DAYS);

                // Determine severity
                String severityClass;
                String severityLabel;
                String rowClass;

                if (daysPending >= Constants.URGENT_THRESHOLD_DAYS) {
                    severityClass = "severity-urgent";
                    severityLabel = "Need Immediate Attention";
                    rowClass      = "row-urgent";
                    log.info("request id is :"+ dsr.getId());
                    int i = dataSubjectInControllerRepository.updateRecordToExpiredWithId(
                            dsr.getId());

                    if(i == 1){
                        log.info("Expiration update True");
                    }else{
                        log.info("Expiration update False");
                    }

                } else if (daysPending >= Constants.TENTATIVE_THRESHOLD_DAYS) {
                    severityClass = "severity-tentative";
                    severityLabel = "Tentative";
                    rowClass      = "row-tentative";
                } else {
                    severityClass = "severity-good";
                    severityLabel = "Good";
                    rowClass      = "row-good";
                }

                tableRows.append(String.format("""
                        <tr class="%s">
                            <td>%d</td>
                            <td class="id-cell">DSR-%05d</td>
                            <td class="date-cell">%s</td>
                            <td class="date-cell">%s</td>
                            <td>
                                <span class="severity %s">
                                    <span class="severity-dot"></span>%s
                                </span>
                            </td>
                        </tr>
                        """,
                        rowClass,
                        rowNum++,
                        dsr.getId(),
                        createdDate.format(Constants.DATE_FMT),
                        dueDate.format(Constants.DATE_FMT),
                        severityClass,
                        severityLabel
                ));
            }
            List<DataSubjectInOrganizationEntity> expiredDSRs = dataSubjectInControllerRepository.getPendingDSRsFromDataController(dce.getId(), Constants.EXPIRED, Constants.DEACTIVE);

            for (DataSubjectInOrganizationEntity dsr : expiredDSRs) {

                LocalDate createdDate = dsr.getCreatedTime()
                        .toLocalDateTime()
                        .toLocalDate();

                long daysPending = ChronoUnit.DAYS.between(createdDate, LocalDate.now());

                LocalDate dueDate = createdDate.plusDays(Constants.SLA_DAYS);

                // Determine severity
                String severityClass = null;
                String severityLabel = null;
                String rowClass = null;

                if (daysPending >= Constants.URGENT_THRESHOLD_DAYS) {
                    severityClass = "severity-urgent";
                    severityLabel = "Expired Request";
                    rowClass      = "row-urgent";
                    log.info("request id is :"+ dsr.getId());
                }

                tableRows.append(String.format("""
                        <tr class="%s">
                            <td>%d</td>
                            <td class="id-cell">DSR-%05d</td>
                            <td class="date-cell">%s</td>
                            <td class="date-cell">%s</td>
                            <td>
                                <span class="severity %s">
                                    <span class="severity-dot"></span>%s
                                </span>
                            </td>
                        </tr>
                        """,
                        rowClass,
                        rowNum++,
                        dsr.getId(),
                        createdDate.format(Constants.DATE_FMT),
                        dueDate.format(Constants.DATE_FMT),
                        severityClass,
                        severityLabel
                ));

                pendingDSRs.add(dsr);
            }
            String htmlTemplate;
            try (var inputStream = Objects.requireNonNull(
                    DataSubjectOperationsController.class
                            .getResourceAsStream("/templates/alertTemplate.html"))) {

                htmlTemplate = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }

            htmlTemplate = htmlTemplate
                    .replace("{{ControllerName}}", dce.getOrgName())
                    .replace("{{tableRows}}", tableRows.toString())
                    .replace("{{totalCount}}", String.valueOf(pendingDSRs.size()));

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("sisukaweerasinghe@gmail.com");
            helper.setTo("sisukaweerasinghe@gmail.com");
            helper.setSubject("Action Required: Pending Data Subject Requests — " + dce.getOrgName());
            helper.setText(htmlTemplate, true);

            File logoFile = new File("src/main/resources/static/images/emblem.png");
            helper.addInline("companyLogo", logoFile);

            mailSender.send(message);
        }



    }
}