package com.icia.memberboard.service;

import com.icia.memberboard.DTO.BoardDetailDTO;
import com.icia.memberboard.DTO.BoardPagingDTO;
import com.icia.memberboard.DTO.BoardSaveDTO;
import com.icia.memberboard.DTO.BoardUpdateDTO;
import com.icia.memberboard.common.PagingConst;
import com.icia.memberboard.entity.BoardEntity;
import com.icia.memberboard.entity.MemberEntity;
import com.icia.memberboard.repository.BoardRepository;
import com.icia.memberboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
    private final BoardRepository br;
    private final MemberRepository mr;

    @Override
    public Long save(BoardSaveDTO boardSaveDTO) throws IllegalStateException, IOException {
        // 파일처리(파일 가져와서 저장하고, 이름 추출, 파일이름 DTO 담아라)
        MultipartFile boardFile = boardSaveDTO.getBoardFile();
        String boardFileName = boardFile.getOriginalFilename();
        boardFileName = System.currentTimeMillis() + "-" + boardFileName;
        boardSaveDTO.setBoardFileName(boardFileName);

        String savePath = "D:\\wep\\GitHub\\kimteayoung2\\spring_Boot\\stsboot_memberboard\\src\\main\\resources\\uploadfile\\"+boardFileName;
        boardFile.transferTo(new File(savePath));

        MemberEntity memberEntity = mr.findByMemberEmail(boardSaveDTO.getBoardWriter());
        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardSaveDTO, memberEntity);
        Long boardId = br.save(boardEntity).getId();
        return boardId;
    }

    @Override
    public List<BoardDetailDTO> findAll() {
        List<BoardEntity> boardEntityList = br.findAll();
        List<BoardDetailDTO> boardList = new ArrayList<>();
        for (BoardEntity boardEntity: boardEntityList) {
            boardList.add(BoardDetailDTO.toBoardDetailDTO(boardEntity));
        }
        return boardList;
    }

    @Override
    public BoardDetailDTO findById(Long boardId) {
        Optional<BoardEntity> optionalBoardEntity = br.findById(boardId);
        BoardDetailDTO boardDetailDTO = null;
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            boardDetailDTO = BoardDetailDTO.toBoardDetailDTO(boardEntity);
        }
        return boardDetailDTO;
    }

    @Override
    public Long update(BoardUpdateDTO boardUpdateDTO) {
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardUpdateDTO);
        return br.save(boardEntity).getId();
    }

    @Override
    public void deleteById(Long boardId) {
        br.deleteById(boardId);
    }

    @Override
    public Page<BoardPagingDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber();
        page = (page == 1)? 0 : (page-1);
        Page<BoardEntity> boardEntities = br.findAll(PageRequest.of(page, PagingConst.PAGE_LIMIT, Sort.by(Sort.Direction.DESC, "id")));
        Page<BoardPagingDTO> boardList = boardEntities.map(
                board -> new BoardPagingDTO(board.getId(),
                        board.getBoardWriter(),
                        board.getBoardTitle())
        );
        return boardList;
    }

    // 검색

    @Override
    public List<BoardDetailDTO> search(String searchType, String keyword) {

        List<BoardEntity> boardEntity = null;

        if (searchType.equals("boardTitle")){
            boardEntity = br.findByBoardTitleContaining(keyword);
        }else if (searchType.equals("boardWriter")){
            boardEntity = br.findByBoardWriterContaining(keyword);
        }else {
            boardEntity = br.findByBoardContentsContaining(keyword);
        }

        List<BoardDetailDTO> boardDetailDTOSList = new ArrayList<>();
        for (BoardEntity b:boardEntity){
            boardDetailDTOSList.add(BoardDetailDTO.toBoardDetailDTO(b));
        }
        return boardDetailDTOSList;

    }

    @Transactional
    @Override
    public void hits(Long boardId) {
        br.hits(boardId);
    }

}
