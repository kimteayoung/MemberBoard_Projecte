package com.icia.memberboard.service;

import com.icia.memberboard.DTO.MemberDetailDTO;
import com.icia.memberboard.DTO.MemberLoginDTO;
import com.icia.memberboard.DTO.MemberSaveDTO;

import java.util.List;

public interface MemberService {
    Long save(MemberSaveDTO memberSaveDTO);

    boolean login(MemberLoginDTO memberLoginDTO);

    List<MemberDetailDTO> findAll();

    MemberDetailDTO findById(Long memberId);

    void deleteById(Long memberId);

    MemberDetailDTO findByEmail(String memberEmail);

    Long update(MemberDetailDTO memberDetailDTO);

    String idDuplicate(String loginEmail);

//    List<MemberDetailDTO> admin();
//
//    MemberDetailDTO myPage(String loginEmail);
}
