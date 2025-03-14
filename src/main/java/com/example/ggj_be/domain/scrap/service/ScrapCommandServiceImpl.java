package com.example.ggj_be.domain.scrap.service;

import com.example.ggj_be.domain.board.repository.BoardRepository;
import com.example.ggj_be.domain.common.repository.GoodRepository;
import com.example.ggj_be.domain.enums.Type;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.reply.repository.ReplyRepository;
import com.example.ggj_be.domain.scrap.Scrap;
import com.example.ggj_be.domain.scrap.dto.ScrapDto;
import com.example.ggj_be.domain.scrap.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ScrapCommandServiceImpl implements ScrapCommandService {
    private final ScrapRepository scrapRepository;
    private final GoodRepository goodRepository;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    @Override
    public List<ScrapDto> getScraps(Member member) {
        Long userId = member.getUserId();

        List<Scrap> scraps = scrapRepository.findByMember_UserId(userId);

        return scraps.stream()
                .map(scrap -> {
                    Long boardId = scrap.getBoard().getBoardId();
                    int goodsCount = boardRepository.countGoodsByBoardId(boardId).intValue(); // 좋아요 개수 조회
                    int replyCount = boardRepository.countRepliesAndReRepliesByBoardId(boardId).intValue();
                    int goodChk = goodRepository.existsByMember_UserIdAndObjectIdAndType(userId, scrap.getBoard().getBoardId(), Type.board) ? 1 : 0;
                    return new ScrapDto(scrap, goodsCount, replyCount, goodChk);
                })
                .sorted(Comparator.comparing(ScrapDto::getScrap_createdAt).reversed())
                .collect(Collectors.toList());
    }
}
