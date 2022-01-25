package com.icia.memberboard.controller;

import com.icia.memberboard.DTO.MemberDetailDTO;
import com.icia.memberboard.DTO.MemberLoginDTO;
import com.icia.memberboard.DTO.MemberSaveDTO;
import com.icia.memberboard.entity.MemberEntity;
import com.icia.memberboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import java.util.List;

import static com.icia.memberboard.common.SessionConst.LOGIN_EMAIL;

@Controller
@RequestMapping("/member/*")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService ms;

    // 회원가입 페이지 이동
    @GetMapping("save")
    public String saveForm() {
        return "member/save";
    }

    // 회원가입 처리
    @PostMapping("save")
    public String save(@ModelAttribute MemberSaveDTO memberSaveDTO){
        Long memberId = ms.save(memberSaveDTO);
        return "index";
    }


    // 로그인 페이지 이동
    @GetMapping("login")
    public String loginForm(Model model){
        model.addAttribute("login",new MemberLoginDTO());
        return "member/login";
    }

    // 로그인 처리
    @PostMapping("login")
    public String login(@ModelAttribute MemberLoginDTO memberLoginDTO, HttpSession session){
        boolean loginResult = ms.login(memberLoginDTO);
        if(loginResult){
            session.setAttribute(LOGIN_EMAIL,memberLoginDTO.getMemberEmail());
            return "/index";
        }else {
            return "member/login";
        }
    }

    // 로그아웃 처리
    @GetMapping("logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

    // 회원목록 요청
    @GetMapping
    public String findAll(Model model) {
        List<MemberDetailDTO> memberList = ms.findAll();
        model.addAttribute("memberList", memberList);
        return "member/findAll";
    }

    // 상세조회
    @GetMapping("{memberId}")
    public String findById(@PathVariable Long memberId, Model model) {
        MemberDetailDTO memberDetailDTO = ms.findById(memberId);
        model.addAttribute("member", memberDetailDTO);
        return "member/findById";
    }

    // 상세조회 ajax
    @PostMapping("{memberId}")
    public @ResponseBody MemberDetailDTO detail(@PathVariable("memberId") Long memberId){
        MemberDetailDTO member = ms.findById(memberId);
        return member;
    }

    // 회원삭제(/member/5) ajax 활용
    @DeleteMapping("{memberId}")
    public ResponseEntity deleteById2(@PathVariable Long memberId) {
        ms.deleteById(memberId);
        return  new ResponseEntity(HttpStatus.OK);
    }

    // 수정화면 요청
    @GetMapping("update")
    public String updateForm(Model model, HttpSession session) {
        String memberEmail = (String) session.getAttribute(LOGIN_EMAIL);
        MemberDetailDTO memberDetailDTO = ms.findByEmail(memberEmail);
        model.addAttribute("member", memberDetailDTO);
        return "member/update";
    }

    // 수정처리 (put) ajax
    @PutMapping("{memberId}")
    public ResponseEntity update2(@RequestBody MemberDetailDTO memberDetailDTO) {
        Long memberId = ms.update(memberDetailDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

}
