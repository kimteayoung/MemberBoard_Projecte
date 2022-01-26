package com.icia.memberboard.entity;

import com.icia.memberboard.DTO.MemberDetailDTO;
import com.icia.memberboard.DTO.MemberSaveDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "member_table")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_Id")
    private Long id;

    @Column(length = 50, unique = true)
    private String memberEmail;

    @Column(length = 20)
    private String memberPassword;

    @Column
    private String memberName;

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.PERSIST, orphanRemoval = false, fetch = FetchType.LAZY)
    private List<BoardEntity> boardEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.PERSIST, orphanRemoval = false, fetch = FetchType.LAZY)
    private List<CommentEntity> commentEntityList = new ArrayList<>();


    public static MemberEntity saveMember(MemberSaveDTO memberSaveDTO){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberSaveDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberSaveDTO.getMemberPassword());
        memberEntity.setMemberName(memberSaveDTO.getMemberName());
        return memberEntity;
    }

    public static MemberEntity toUpdateMember(MemberDetailDTO memberDetailDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDetailDTO.getMemberId());
        memberEntity.setMemberEmail(memberDetailDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDetailDTO.getMemberPassword());
        memberEntity.setMemberName(memberDetailDTO.getMemberName());
        return memberEntity;
    }

    @PreRemove
    private void preRemove() {
        System.out.println("MemberEntity.preRemove");
        boardEntityList.forEach(board -> board.setMemberEntity(null));
        commentEntityList.forEach(comment -> comment.setMemberEntity(null));
    }
}
