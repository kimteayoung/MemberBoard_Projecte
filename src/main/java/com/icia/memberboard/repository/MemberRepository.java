package com.icia.memberboard.repository;

import com.icia.memberboard.DTO.MemberDetailDTO;
import com.icia.memberboard.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    MemberEntity findByMemberEmail(String memberEmail);

//    List<MemberDetailDTO> admin();
//
//    MemberDetailDTO myPage(String loginEmail);
}
