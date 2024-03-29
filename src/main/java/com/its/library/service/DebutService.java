package com.its.library.service;

import com.its.library.common.PagingConst;
import com.its.library.dto.DebutEpisodeDTO;
import com.its.library.dto.LoveDTO;
import com.its.library.dto.MemberDTO;
import com.its.library.entity.*;
import com.its.library.repository.DebutCategoryRepository;
import com.its.library.repository.DebutRepository;
import com.its.library.repository.LoveRepository;
import com.its.library.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DebutService {
    private final DebutRepository debutRepository;
    private final MemberRepository memberRepository;
    private final DebutCategoryRepository debutCategoryRepository;
    private final LoveRepository loveRepository;

    //데뷔글 저장 파일 처리
    public void save(DebutEpisodeDTO debutEpisodeDTO, Long id) throws IOException {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if (optionalMemberEntity.isPresent()) {
            MemberEntity memberEntity = optionalMemberEntity.get();
            Optional<DebutCategoryEntity> optionalDebutCategoryEntity = debutCategoryRepository.findById(debutEpisodeDTO.getCategoryId());
            if (optionalDebutCategoryEntity.isPresent()) {
                DebutCategoryEntity debutCategoryEntity = optionalDebutCategoryEntity.get();

                MultipartFile debutImg = debutEpisodeDTO.getDebutImg();
                String debutImgName = debutImg.getOriginalFilename();
                if (!debutImg.isEmpty()) {
                    debutImgName = System.currentTimeMillis() + "_" + debutImgName;
                    String savePath = "C:\\springboot_img\\" + debutImgName;
                    debutImg.transferTo(new File(savePath));
                    debutEpisodeDTO.setDebutImgName(debutImgName);
                }
                DebutEpisodeEntity debutEpisodeEntity = DebutEpisodeEntity.toSave(debutCategoryEntity, debutEpisodeDTO, memberEntity);
                debutRepository.save(debutEpisodeEntity);
            }
        }

    }

    //데뷔글 상세조회
    @Transactional
    public DebutEpisodeDTO detail(Long id) {
        Optional<DebutEpisodeEntity> optionalDebutEpisodeEntity = debutRepository.findById(id);
        if (optionalDebutEpisodeEntity.isPresent()) {
            DebutEpisodeEntity debutEpisodeEntity = optionalDebutEpisodeEntity.get();
            debutRepository.hitsAdd(debutEpisodeEntity.getId());
            DebutEpisodeDTO debutEpisodeDTO = DebutEpisodeDTO.toDTO(debutEpisodeEntity);

            return debutEpisodeDTO;
        } else {
            return null;
        }


    }

    //데뷔글 업데이트 화면 처리
    public DebutEpisodeDTO updateForm(Long id) {
        Optional<DebutEpisodeEntity> optionalDebutEpisodeEntity = debutRepository.findById(id);
        if (optionalDebutEpisodeEntity.isPresent()) {
            DebutEpisodeEntity debutEpisodeEntity = optionalDebutEpisodeEntity.get();
            DebutEpisodeDTO debutEpisodeDTO = DebutEpisodeDTO.toDTO(debutEpisodeEntity);
            return debutEpisodeDTO;
        } else {
            return null;
        }

    }

    // 데뷔글 업데이트 처리
    public void update(DebutEpisodeDTO debutEpisodeDTO) throws IOException {

        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(debutEpisodeDTO.getMemberId());
        Optional<DebutCategoryEntity> optionalDebutCategoryEntity = debutCategoryRepository.findById(debutEpisodeDTO.getCategoryId());
        if (optionalMemberEntity.isPresent()) {
            MemberEntity memberEntity = optionalMemberEntity.get();

            MultipartFile debutImg = debutEpisodeDTO.getDebutImg();
            String debutImgName = debutImg.getOriginalFilename();
            if (!debutImg.isEmpty()) {
                debutImgName = System.currentTimeMillis() + "_" + debutImgName;
                String savePath = "C:\\springboot_img\\" + debutImgName;
                debutImg.transferTo(new File(savePath));
                debutEpisodeDTO.setDebutImgName(debutImgName);
            }
            if (optionalDebutCategoryEntity.isPresent()) {
                DebutCategoryEntity debutCategoryEntity = optionalDebutCategoryEntity.get();
                DebutEpisodeEntity debutEpisodeEntity = DebutEpisodeEntity.toUpdate(debutCategoryEntity, debutEpisodeDTO, memberEntity);
                debutRepository.save(debutEpisodeEntity);

            }

        }

    }

    //데뷔글 삭제 처리
    public void delete(Long id) {
        debutRepository.deleteById(id);
    }


    //데뷔글 좋아요 등록(좋아요 중복체크 좋아요 종합)
    public int loveSave(Long debutId, Long memberId) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(memberId);
        if (optionalMemberEntity.isPresent()) {
            MemberEntity memberEntity = optionalMemberEntity.get();
            LoveDTO loveDTO = new LoveDTO();
            loveDTO.setDebutId(debutId);
            loveDTO.setMemberId(memberId);
            LoveEntity loveEntity = LoveEntity.toSave(loveDTO, memberEntity);
            loveRepository.save(loveEntity);
            int loveNum = loveRepository.countByDebutId(debutId);
            Optional<DebutEpisodeEntity> optionalDebutEpisodeEntity = debutRepository.findById(debutId);
            if (optionalDebutEpisodeEntity.isPresent()) {
                DebutEpisodeEntity debutEpisodeEntity = optionalDebutEpisodeEntity.get();
                Optional<DebutCategoryEntity> optionalDebutCategoryEntity = debutCategoryRepository.findById(debutEpisodeEntity.getDebutCategoryEntity().getId());
                if (optionalDebutCategoryEntity.isPresent()) {
                    DebutCategoryEntity debutCategoryEntity = optionalDebutCategoryEntity.get();
                    debutEpisodeEntity.setMemberEntity(memberEntity);
                    debutEpisodeEntity.setDebutCategoryEntity(debutCategoryEntity);
                    debutEpisodeEntity.setLove(loveNum);
                    debutRepository.save(debutEpisodeEntity);
                }
            }
            return 1;
        } else {
            return 0;
        }
    }

    //좋아요 삭제 기능
    @Transactional
    public int loveDelete(Long debutId, Long memberId) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(memberId);
        if (optionalMemberEntity.isPresent()) {
            MemberEntity memberEntity = optionalMemberEntity.get();
            LoveDTO loveDTO = new LoveDTO();
            loveDTO.setDebutId(debutId);
            loveDTO.setMemberId(memberId);
            loveRepository.deleteByDebutIdAndMemberEntity(debutId, memberEntity);
            int loveNum = loveRepository.countByDebutId(debutId);
            Optional<DebutEpisodeEntity> optionalDebutEpisodeEntity = debutRepository.findById(debutId);
            if (optionalDebutEpisodeEntity.isPresent()) {
                DebutEpisodeEntity debutEpisodeEntity = optionalDebutEpisodeEntity.get();
                Optional<DebutCategoryEntity> optionalDebutCategoryEntity = debutCategoryRepository.findById(debutEpisodeEntity.getDebutCategoryEntity().getId());
                if (optionalDebutCategoryEntity.isPresent()) {
                    DebutCategoryEntity debutCategoryEntity = optionalDebutCategoryEntity.get();
                    debutEpisodeEntity.setMemberEntity(memberEntity);
                    debutEpisodeEntity.setDebutCategoryEntity(debutCategoryEntity);
                    debutEpisodeEntity.setLove(loveNum);
                    debutRepository.save(debutEpisodeEntity);
                }
            }
            return 1;

        } else {
            return 0;
        }
    }

    @Transactional
    public List<DebutEpisodeDTO> categoryList(Long categoryId, int addressId) {
        switch (addressId) {
            case 0:
                Optional<DebutCategoryEntity> optionalDebutCategoryEntity0 = debutCategoryRepository.findById(categoryId);
                if (optionalDebutCategoryEntity0.isPresent()) {
                    DebutCategoryEntity debutCategoryEntity = optionalDebutCategoryEntity0.get();
                    List<DebutEpisodeEntity> debutEpisodeEntityList = debutRepository.findByDebutCategoryEntity(debutCategoryEntity);
                    List<DebutEpisodeDTO> debutEpisodeDTOS = new ArrayList<>();
                    for (DebutEpisodeEntity debutEpisodeEntity : debutEpisodeEntityList) {
                        DebutEpisodeEntity debutEpisodeEntity1 = debutEpisodeEntity;
                        DebutEpisodeDTO debutEpisodeDTO = DebutEpisodeDTO.toDTO(debutEpisodeEntity1);
                        debutEpisodeDTOS.add(debutEpisodeDTO);
                    }
                    return debutEpisodeDTOS;
                }
            case 1:
                Optional<DebutCategoryEntity> optionalDebutCategoryEntity1 = debutCategoryRepository.findById(categoryId);
                if (optionalDebutCategoryEntity1.isPresent()) {
                    DebutCategoryEntity debutCategoryEntity = optionalDebutCategoryEntity1.get();
                    List<DebutEpisodeEntity> debutEpisodeEntityList = debutRepository.oderByNew(debutCategoryEntity.getId());
                    List<DebutEpisodeDTO> debutEpisodeDTOS = new ArrayList<>();
                    for (DebutEpisodeEntity debutEpisodeEntity : debutEpisodeEntityList) {
                        DebutEpisodeEntity debutEpisodeEntity1 = debutEpisodeEntity;
                        DebutEpisodeDTO debutEpisodeDTO = DebutEpisodeDTO.toDTO(debutEpisodeEntity1);
                        debutEpisodeDTOS.add(debutEpisodeDTO);
                    }
                    return debutEpisodeDTOS;
                }
            case 2:
                Optional<DebutCategoryEntity> optionalDebutCategoryEntity2 = debutCategoryRepository.findById(categoryId);
                if (optionalDebutCategoryEntity2.isPresent()) {
                    DebutCategoryEntity debutCategoryEntity = optionalDebutCategoryEntity2.get();
                    List<DebutEpisodeEntity> debutEpisodeEntityList = debutRepository.orderByHits(debutCategoryEntity.getId());
                    List<DebutEpisodeDTO> debutEpisodeDTOS = new ArrayList<>();
                    for (DebutEpisodeEntity debutEpisodeEntity : debutEpisodeEntityList) {
                        DebutEpisodeEntity debutEpisodeEntity1 = debutEpisodeEntity;
                        DebutEpisodeDTO debutEpisodeDTO = DebutEpisodeDTO.toDTO(debutEpisodeEntity1);
                        debutEpisodeDTOS.add(debutEpisodeDTO);
                    }
                    return debutEpisodeDTOS;
                }
            case 3:
                Optional<DebutCategoryEntity> optionalDebutCategoryEntity3 = debutCategoryRepository.findById(categoryId);
                if (optionalDebutCategoryEntity3.isPresent()) {
                    DebutCategoryEntity debutCategoryEntity = optionalDebutCategoryEntity3.get();
                    List<DebutEpisodeEntity> debutEpisodeEntityList = debutRepository.orderByLove(debutCategoryEntity.getId());
                    List<DebutEpisodeDTO> debutEpisodeDTOS = new ArrayList<>();
                    for (DebutEpisodeEntity debutEpisodeEntity : debutEpisodeEntityList) {
                        DebutEpisodeEntity debutEpisodeEntity1 = debutEpisodeEntity;
                        DebutEpisodeDTO debutEpisodeDTO = DebutEpisodeDTO.toDTO(debutEpisodeEntity1);
                        debutEpisodeDTOS.add(debutEpisodeDTO);
                    }
                    return debutEpisodeDTOS;
                }
                break;
        }
        return null;
    }

    @Transactional
    public List<DebutEpisodeDTO> myDebutWrite(Long id) {
        List<DebutEpisodeEntity> debutEpisodeEntityList = debutRepository.findByMemberEntity_Id(id);
        List<DebutEpisodeDTO> debutEpisodeDTOList = new ArrayList<>();
        for (DebutEpisodeEntity debutEpisodeEntity : debutEpisodeEntityList) {
            DebutEpisodeEntity debutEpisodeEntity1 = debutEpisodeEntity;
            debutEpisodeDTOList.add(DebutEpisodeDTO.toDTO(debutEpisodeEntity1));
        }
        return debutEpisodeDTOList;
    }

    public List<DebutEpisodeDTO> indexNewList() {
        List<DebutEpisodeEntity> debutEpisodeEntityList = debutRepository.indexNewList();
        List<DebutEpisodeDTO> debutEpisodeDTOList = new ArrayList<>();

        try {
            for (int i = 0; i < 5; i++) {
                debutEpisodeDTOList.add(DebutEpisodeDTO.toDTO(debutEpisodeEntityList.get(i)));
            }
        } catch (Exception e) {

        }

        return debutEpisodeDTOList;

    }

    public String loveCheck(Long id, Long memberId) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(memberId);
        if (optionalMemberEntity.isPresent()) {
            MemberEntity memberEntity = optionalMemberEntity.get();
            Optional<LoveEntity> optionalLoveEntity = loveRepository.findByDebutIdAndMemberEntity(id, memberEntity);
            if (optionalLoveEntity.isPresent()) {
                return "ok";
            } else {
                return "no";
            }

        }
        return null;
    }

    @Transactional
    public List<DebutEpisodeDTO> hitsTop123() {
        List<DebutEpisodeEntity> top123EntityList = debutRepository.hitsTop123();
        List<DebutEpisodeDTO> top123DTOList = new ArrayList<>();
        for (DebutEpisodeEntity debutEpisodeEntity : top123EntityList) {
            DebutEpisodeEntity top123Entity = debutEpisodeEntity;
            DebutEpisodeDTO debutEpisodeDTO = DebutEpisodeDTO.toDTO(top123Entity);
            top123DTOList.add(debutEpisodeDTO);
        }
        return top123DTOList;
    }

    @Transactional
    public List<DebutEpisodeDTO> hitsTop456() {
        List<DebutEpisodeEntity> top456EntityList = debutRepository.hitsTop456();
        List<DebutEpisodeDTO> top456DTOList = new ArrayList<>();
        for (DebutEpisodeEntity debutEpisodeEntity : top456EntityList) {
            DebutEpisodeEntity top123Entity = debutEpisodeEntity;
            DebutEpisodeDTO debutEpisodeDTO = DebutEpisodeDTO.toDTO(top123Entity);
            top456DTOList.add(debutEpisodeDTO);

        }
        return top456DTOList;
    }
}
