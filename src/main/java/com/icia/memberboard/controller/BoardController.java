package com.icia.memberboard.controller;

import com.icia.memberboard.DTO.*;
import com.icia.memberboard.common.PagingConst;
import com.icia.memberboard.service.BoardService;
import com.icia.memberboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static com.icia.memberboard.common.PagingConst.BLOCK_LIMIT;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService bs;
    private final CommentService cs;

    // 글쓰기 화면요청
    @GetMapping("/save")
    public String saveForm() {
        return "board/save";
    }

    // 글쓰기 처리
    @PostMapping("/save")
    public String save(@ModelAttribute BoardSaveDTO boardSaveDTO) throws IOException {
        System.out.println("boardSaveDTO = " + boardSaveDTO);
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
    public String findById(Model model, @PathVariable("boardId") Long boardId ) {
        log.info("글보기 메서드 호출. 요청글 번호: {}", boardId);
        bs.hits(boardId);
        BoardDetailDTO board = bs.findById(boardId);
        List<CommentDetailDTO> commentList = cs.findAll(boardId);
        model.addAttribute("board", board);
        model.addAttribute("commentList", commentList);
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
        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / BLOCK_LIMIT))) - 1) * BLOCK_LIMIT + 1;
        int endPage = ((startPage + BLOCK_LIMIT - 1) < boardList.getTotalPages()) ? startPage + BLOCK_LIMIT - 1 : boardList.getTotalPages();
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

    // 게시글 검색
    @GetMapping("search")
    public String search(@RequestParam("searchType") String searchType, @RequestParam("keyword") String keyword, Model model) {
        List<BoardDetailDTO>boardList = bs.search(searchType,keyword );
        model.addAttribute("boardList",boardList);
        return "board/findAll";
    }

}
