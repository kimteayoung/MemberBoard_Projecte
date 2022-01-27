package com.icia.memberboard.controller;

import com.icia.memberboard.DTO.BoardDetailDTO;
import com.icia.memberboard.DTO.BoardPagingDTO;
import com.icia.memberboard.DTO.BoardSaveDTO;
import com.icia.memberboard.DTO.BoardUpdateDTO;
import com.icia.memberboard.common.PagingConst;
import com.icia.memberboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService bs;

    // 글쓰기 화면요청
    @GetMapping("/save")
    public String saveForm() {
        return "board/save";
    }

    // 글쓰기 처리
    @PostMapping("/save")
    public String save(@ModelAttribute BoardSaveDTO boardSaveDTO){
        Long boardId = bs.save(boardSaveDTO);
        return "index";
    }

    // 글목록 화면 요청
    @GetMapping("/")
    public String findAll(Model model) {
        List<BoardDetailDTO> boardList = bs.findAll();
        model.addAttribute("boardList", boardList);
        log.info("findAll 호출");
        return "board/findAll";
    }

    // 글목록 조회
    @GetMapping("/{boardId}")
    public String findById(Model model, @PathVariable Long boardId) {
        log.info("글보기 메서드 호출. 요청글 번호: {}", boardId);
        BoardDetailDTO board = bs.findById(boardId);
        model.addAttribute("board", board);
        return "board/findById";
    }

    // 글 상세 조회 (ajax)
    @PostMapping("/{boardId}")
    public ResponseEntity findById2(@PathVariable Long boardId) {
        BoardDetailDTO board = bs.findById(boardId);
        return new ResponseEntity<BoardDetailDTO>(board, HttpStatus.OK);
    }

    // 글삭제(/board/숫자) ajax 활용
    @DeleteMapping("/{boardId}")
    public ResponseEntity deleteById2(@PathVariable Long boardId) {
        bs.deleteById(boardId);
        return  new ResponseEntity(HttpStatus.OK);
    }

    // 글 수정 화면 요청
   @GetMapping("/update/{boardId}")
    public String updateForm(@PathVariable Long boardId, Model model) {
        BoardDetailDTO board = bs.findById(boardId);
        model.addAttribute("board", board);
        return "board/update";
    }

    // 글 수정 처리 (post)
    @PostMapping("/update")
    public String update(@ModelAttribute BoardUpdateDTO boardUpdateDTO) {
        bs.update(boardUpdateDTO);
        return "redirect:/board/" + boardUpdateDTO.getBoardId();
    }

    // 글 수정 처리 (put)
    @PutMapping("/{boardId}")
    public ResponseEntity update2(@RequestBody BoardUpdateDTO boardUpdateDTO) {
        bs.update(boardUpdateDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 페이징 처리(/board?page=5)
    // 5번글(/board/5)
    @GetMapping
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model) {
        Page<BoardPagingDTO> boardList = bs.paging(pageable);
        model.addAttribute("boardList", boardList);
        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / PagingConst.BLOCK_LIMIT))) - 1) * PagingConst.BLOCK_LIMIT + 1;
        int endPage = ((startPage + PagingConst.BLOCK_LIMIT - 1) < boardList.getTotalPages()) ? startPage + PagingConst.BLOCK_LIMIT - 1 : boardList.getTotalPages();
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "board/paging";
    }

    // 로그아웃 처리
    @GetMapping("logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

}
