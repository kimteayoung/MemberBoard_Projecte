package com.icia.memberboard.repository;

import com.icia.memberboard.DTO.BoardDetailDTO;
import com.icia.memberboard.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    List<BoardEntity> findByBoardContentsContaining(String keyword);

    List<BoardEntity> findByBoardWriterContaining(String keyword);

    List<BoardEntity> findByBoardTitleContaining(String keyword);
}
