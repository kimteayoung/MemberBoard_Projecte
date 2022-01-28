package com.icia.memberboard.service;

import com.icia.memberboard.DTO.BoardDetailDTO;
import com.icia.memberboard.DTO.BoardPagingDTO;
import com.icia.memberboard.DTO.BoardSaveDTO;
import com.icia.memberboard.DTO.BoardUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardService {
    Long save(BoardSaveDTO boardSaveDTO);

    List<BoardDetailDTO> findAll();

    BoardDetailDTO findById(Long boardId);

    Long update(BoardUpdateDTO boardUpdateDTO);

    void deleteById(Long boardId);

    Page<BoardPagingDTO> paging(Pageable pageable);


    List<BoardDetailDTO> search(String searchType, String keyword);
}
