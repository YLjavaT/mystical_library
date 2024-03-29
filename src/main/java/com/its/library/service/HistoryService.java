package com.its.library.service;

import com.its.library.dto.BookDTO;
import com.its.library.dto.EpisodeDTO;
import com.its.library.dto.HistoryDTO;
import com.its.library.entity.*;
import com.its.library.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final EpisodeRepository episodeRepository;
    private final BoxRepository boxRepository;

    public String historyUpdate(HistoryDTO historyDTO) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(historyDTO.getMemberId());
        Optional<EpisodeEntity> optionalEpisodeEntity = episodeRepository.findById(historyDTO.getEpisodeId());
        MemberEntity memberEntity = new MemberEntity();
        HistoryEntity historyEntity = new HistoryEntity();
        EpisodeEntity episodeEntity = new EpisodeEntity();
        if (optionalMemberEntity.isPresent() && optionalEpisodeEntity.isPresent()) {
            memberEntity = optionalMemberEntity.get();
            episodeEntity = optionalEpisodeEntity.get();
            historyEntity = HistoryEntity.updateEntity(historyDTO, memberEntity, episodeEntity);
            historyRepository.save(historyEntity);
            return "저장완료";
        } else {
            return null;
        }

    }

    public List<BookDTO> list(Long id) {
        List<HistoryEntity> historyEntityList = new ArrayList<>();
        BookEntity bookEntity = new BookEntity();
        List<HistoryDTO> historyDTOList = new ArrayList<>();
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        MemberEntity memberEntity = new MemberEntity();
        if (optionalMemberEntity.isPresent()) {
            memberEntity = optionalMemberEntity.get();
        }
        List<BookDTO> bookDTOList = new ArrayList<>();
        List<Long> list = historyRepository.findByMemberId(memberEntity.getId());
        for (int i = 0; i < list.size(); i++) {
            Optional<BookEntity> optionalBookEntity = bookRepository.findById(list.get(i));
            if (optionalBookEntity.isPresent()) {
                bookEntity = optionalBookEntity.get();
                bookDTOList.add(BookDTO.findDTO(bookEntity));
            }
        }
        return bookDTOList;
    }

    public String hidden(HistoryDTO historyDTO) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(historyDTO.getMemberId());
        Optional<BookEntity> optionalBookEntity = bookRepository.findById(historyDTO.getBookId());
        MemberEntity memberEntity = new MemberEntity();
        BookEntity bookEntity = new BookEntity();
        HistoryEntity historyEntity = new HistoryEntity();
        if (optionalMemberEntity.isPresent() && optionalBookEntity.isPresent()) {
            memberEntity = optionalMemberEntity.get();
            bookEntity = optionalBookEntity.get();

            List<HistoryEntity> historyEntityList = historyRepository.findByMemberEntityAndBooKId(memberEntity.getId(), bookEntity.getId());
            for (int i = 0; i < historyEntityList.size(); i++) {
                historyRepository.deleteById(historyEntityList.get(i).getId());
            }
            return "숨기기";
        } else {
            return null;
        }
    }

    public Long findByHistory(HistoryDTO historyDTO) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(historyDTO.getMemberId());
        Optional<EpisodeEntity> optionalEpisodeEntity = episodeRepository.findById(historyDTO.getEpisodeId());
        MemberEntity memberEntity = new MemberEntity();
        EpisodeEntity episodeEntity = new EpisodeEntity();
        HistoryEntity historyEntity = new HistoryEntity();
        if (optionalMemberEntity.isPresent() && optionalEpisodeEntity.isPresent()) {
            memberEntity = optionalMemberEntity.get();
            episodeEntity = optionalEpisodeEntity.get();
            Optional<HistoryEntity> optionalHistoryEntity = historyRepository.findByMemberEntityAndEpisodeEntity(memberEntity, episodeEntity);
            if (optionalHistoryEntity.isPresent()) {
                historyEntity = optionalHistoryEntity.get();
                if (historyEntity != null) {
                    Long historyId = historyEntity.getId();
                    return historyId;
                }
            }
        } else {
            return null;
        }
        return null;
    }

    public List<HistoryDTO> findByBookId(Long bookId, Long memberId) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(memberId);
        MemberEntity memberEntity = new MemberEntity();
        List<HistoryDTO> historyDTOList = new ArrayList<>();
        if (optionalMemberEntity.isPresent()) {
            memberEntity = optionalMemberEntity.get();
            List<HistoryEntity> historyEntityList = historyRepository.findByMemberEntityAndBooKId(memberEntity.getId(), bookId);
            for (HistoryEntity history: historyEntityList) {
                historyDTOList.add(HistoryDTO.findDTO(history));
            }
            return historyDTOList;
        } else {
            return null;
        }
    }
    // 이어보기
    public Long findByAgain(Long bookId, Long memberId) {
        List<HistoryEntity> historyEntityList = historyRepository.findByAgain(memberId, bookId);
        if (historyEntityList.get(0).getEpisodeEntity().getId() != null) {
            return historyEntityList.get(0).getEpisodeEntity().getId();
        } else {
            return null;
        }
    }

    public String episodeCheck(HistoryDTO historyDTO) {
        System.out.println("historyDTO.getEpisodeId() = " + historyDTO.getEpisodeId());
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(historyDTO.getMemberId());
        Optional<EpisodeEntity> optionalEpisodeEntity = episodeRepository.findById(historyDTO.getEpisodeId());
        if (optionalMemberEntity.isPresent() && optionalEpisodeEntity.isPresent()) {
            MemberEntity memberEntity = optionalMemberEntity.get();
            EpisodeEntity episodeEntity = optionalEpisodeEntity.get();
            System.out.println("episodeEntity = " + episodeEntity.getId());
            System.out.println("memberEntity = " + memberEntity.getId());
            List<BoxEntity> boxEntityList = boxRepository.findByMemberEntityAndEpisodeId(memberEntity, historyDTO.getEpisodeId());
            Optional<HistoryEntity> optionalHistoryEntity = historyRepository.findByMemberEntityAndEpisodeEntity(memberEntity, episodeEntity);
            if (optionalHistoryEntity.isPresent()) {
                System.out.println("히스토리있음");
                return "내역있음";
            }
            if (boxEntityList.size() != 0) {
                System.out.println("박스에만있음");
                historyRepository.save(HistoryEntity.saveEntity(historyDTO, memberEntity, episodeEntity));
                return "내역있음";
            }
        }
        return "내역없음";
    }
}
