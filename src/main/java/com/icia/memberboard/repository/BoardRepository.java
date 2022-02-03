package com.icia.memberboard.repository;

import com.icia.memberboard.DTO.BoardDetailDTO;
import com.icia.memberboard.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    List<BoardEntity> findByBoardContentsContaining(String keyword);

    List<BoardEntity> findByBoardWriterContaining(String keyword);

    List<BoardEntity> findByBoardTitleContaining(String keyword);

    @Modifying
    @Query("update BoardEntity as b set b.boardHits = b.boardHits+1 where b.id = :boardId")
    void hits(Long boardId);

}
