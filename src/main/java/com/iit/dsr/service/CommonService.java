package com.iit.dsr.service;

import com.iit.dsr.dto.responses.commons.ActiveDataControllerResponseDTO;
import com.iit.dsr.dto.responses.commons.CommonResponseDTO;
import com.iit.dsr.entity.DataControllerEntity;
import com.iit.dsr.repository.DataControllerRepository;
import com.iit.dsr.utils.CommonUtils;
import com.iit.dsr.utils.Constants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CommonService {

    @Autowired
    private CommonUtils commonUtils;
    @Autowired
    private DataControllerRepository dataControllerRepository;


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
}
