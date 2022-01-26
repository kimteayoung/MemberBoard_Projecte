package com.icia.memberboard.service;

import com.icia.memberboard.DTO.CommentDetailDTO;
import com.icia.memberboard.DTO.CommentSaveDTO;

import java.util.List;

public interface CommentService {
    Long save(CommentSaveDTO commentSaveDTO);

    List<CommentDetailDTO> findAll(Long boardId);
}
