package com.llt.hope.mapper;

import com.llt.hope.dto.request.CVCreateReq;
import com.llt.hope.dto.request.CVUpdateReq;
import com.llt.hope.dto.response.CVResponse;
import com.llt.hope.dto.response.PostResponse;
import com.llt.hope.entity.CVForm;
import com.llt.hope.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CVMapper {
   public CVResponse toCVResponse(CVForm cvForm){
       if(cvForm==null) return null;
       return CVResponse.builder()
               .id(cvForm.getId())
               .exp(cvForm.getExp())
               .phone(cvForm.getPhone())
               .skill(cvForm.getSkill())
               .dob(cvForm.getDob())
               .email(cvForm.getEmail())
               .address(cvForm.getAddress())
               .userId(cvForm.getUser().getId())
               .name(cvForm.getName())
               .typeOfDisability(cvForm.getTypeOfDisability())
               .typeOfJob(cvForm.getTypeOfJob())
               .education(cvForm.getEducation())
               .build();
   }
    public CVForm toCVForm(CVCreateReq req){
        if(req==null) return null;
        return CVForm.builder()
                .exp(req.getExp())
                .phone(req.getPhone())
                .skill(req.getSkill())
                .dob(req.getDob())
                .email(req.getEmail())
                .address(req.getAddress())
                .name(req.getName())
                .typeOfDisability(req.getTypeOfDisability())
                .typeOfJob(req.getTypeOfJob())
                .education(req.getEducation())
                .build();
    }
    public CVForm toCVForm(CVUpdateReq req){
        if(req==null) return null;
        return CVForm.builder()
                .exp(req.getExp())
                .phone(req.getPhone())
                .skill(req.getSkill())
                .dob(req.getDob())
                .email(req.getEmail())
                .address(req.getAddress())
                .name(req.getName())
                .typeOfDisability(req.getTypeOfDisability())
                .typeOfJob(req.getTypeOfJob())
                .education(req.getEducation())
                .build();
    }
}
