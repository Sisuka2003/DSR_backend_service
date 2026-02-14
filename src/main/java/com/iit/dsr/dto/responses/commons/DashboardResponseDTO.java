package com.iit.dsr.dto.responses.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
public class DashboardResponseDTO {

    int dataControllerCount;
    int dataSubjectCount;
    int totalDataRecordsCount;
    int totalAdminCount;
    int totalAgentCount;

    int pendingRecordsCount;
    int rejectedRecordsCount;
    int queuedRecordsCount;
    int approvedRecordsCount;
}
