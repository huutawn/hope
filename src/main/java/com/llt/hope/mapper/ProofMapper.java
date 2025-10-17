package com.llt.hope.mapper;

import org.springframework.stereotype.Component;

import com.llt.hope.dto.response.ProofResponse;
import com.llt.hope.entity.Proof;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProofMapper {
    public ProofResponse toProofResponse(Proof proof) {
        return ProofResponse.builder()
                .id(proof.getId())
                .title(proof.getTitle())
                .content(proof.getContent())
                .proofImage(proof.getProofImage())
                .postVolunteerId(
                        proof.getPostVolunteer() != null
                                ? proof.getPostVolunteer().getId()
                                : null)
                .createAt(proof.getCreateAt())
                .updateAt(proof.getUpdateAt())
                .status(proof.getStatus())
                .isActive(proof.isActive())
                .build();
    }
}
