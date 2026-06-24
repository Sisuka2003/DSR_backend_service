package com.iit.dsr.service;

import com.iit.dsr.dto.requests.commons.CommonRequestDto;
import com.iit.dsr.dto.responses.commons.DashboardResponseDTO;
import com.iit.dsr.entity.DataControllerEntity;
import com.iit.dsr.entity.IdentificationKeyTypesEntity;
import com.iit.dsr.repository.*;
import com.iit.dsr.utils.CommonUtils;
import com.iit.dsr.utils.Constants;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonService {

    @Autowired
    private CommonUtils commonUtils;
    @Autowired
    private DataControllerRepository dataControllerRepository;
    @Autowired
    private KeyIdenticationKeyRepository keyIdenticationKeyRepository;

    @Autowired
    private DataSubjectRepository dataSubjectRepository;

    @Autowired
    private DataSubjectInControllerRepository dataSubjectInControllerRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DataControllerAgentsRepository agentsRepository;

    public ResponseEntity<?> retrieveAllActiveOrganizations(){
        try{
            List<DataControllerEntity> allActiveDataControllers = dataControllerRepository.getAllActiveDataControllers(Constants.ACTIVE);
            return allActiveDataControllers == null ?
                    commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED,"FAILED TO PROCESS",null,null,false)
                    :
                    commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS,"SUCCESS",allActiveDataControllers,null,false);

        }catch (Exception e){
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED,"FAILED TO PROCESS",null,null,false);
        }
    }

    public ResponseEntity<?> retrieveKeyIdentifiers() {
        try{
            List<IdentificationKeyTypesEntity> allActiveIdentifiers = keyIdenticationKeyRepository.getAllActiveIdentifiers();
            return allActiveIdentifiers == null ?
                    commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED,"FAILED TO PROCESS",null,null,false)
                    :
                    commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS,"SUCCESS",allActiveIdentifiers,null,false);

        }catch (Exception e){
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED,"FAILED TO PROCESS",null,null,false);
        }
    }

    public ResponseEntity<?> getDataCountsForDashboards(CommonRequestDto requestDto) {
        try{
            DashboardResponseDTO dashboardResponseDTO = new DashboardResponseDTO();
            if (requestDto.getIsAdminDashboard()){
                dashboardResponseDTO.setDataControllerCount(dataControllerRepository.getAllActiveDataControllerCount(Constants.ACTIVE));
                dashboardResponseDTO.setDataSubjectCount(dataSubjectRepository.getAllActiveDataSubjectCount(Constants.ACTIVE));
                dashboardResponseDTO.setTotalDataRecordsCount(dataSubjectInControllerRepository.getAllRecordsCount(Constants.ACTIVE));
                dashboardResponseDTO.setTotalAdminCount(adminRepository.getAllRecordsCount(Constants.ACTIVE));
                dashboardResponseDTO.setPendingRecordsCount(dataSubjectInControllerRepository.getAllPendingRecordsCount(Constants.PENDING));
                dashboardResponseDTO.setApprovedRecordsCount(dataSubjectInControllerRepository.getAllApprovedRecordsCount(Constants.APPROVED));
                dashboardResponseDTO.setQueuedRecordsCount(dataSubjectInControllerRepository.getAllQueuedRecordsCount(Constants.QUEUED));
                dashboardResponseDTO.setRejectedRecordsCount(dataSubjectInControllerRepository.getAllRejectedRecordsCount(Constants.REJECTED));
                dashboardResponseDTO.setExpiredRecordsCount(dataSubjectInControllerRepository.getAllRejectedRecordsCount(Constants.EXPIRED));
            }
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_SUCCESS,"SUCCESSFULLY PROCESSED",dashboardResponseDTO,null,false);
        }catch (Exception e){
            e.printStackTrace();
            return commonUtils.generateResponseObject(Constants.RESPONSE_CODE_FAILED,"FAILED TO PROCESS",null,null,false);
        }
    }
}
