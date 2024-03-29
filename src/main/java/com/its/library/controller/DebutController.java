package com.its.library.controller;

import com.its.library.config.auth.PrincipalDetails;
import com.its.library.dto.DebutCommentDTO;
import com.its.library.dto.DebutEpisodeDTO;
import com.its.library.dto.MemberDTO;
import com.its.library.service.DebutCommentService;
import com.its.library.service.DebutService;
import com.its.library.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/debut")
public class DebutController {
    private final DebutService debutService;
    private final DebutCommentService debutCommentService;
    private final MemberService memberService;

    //데뷔글 저장 화면 요청
    @GetMapping("/save-form")
    public String saveForm(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        String loginId = principalDetails.getUsername();
        MemberDTO findDTO = memberService.findByLoginId(loginId);
        model.addAttribute("authentication", findDTO);
        return "debut/save";
    }

    //데뷔글 저장 처리
    @PostMapping("/save")
    public String save(@ModelAttribute DebutEpisodeDTO debutEpisodeDTO) throws IOException {
        debutService.save(debutEpisodeDTO, debutEpisodeDTO.getMemberId());
        return "redirect:/member/myPage/" + debutEpisodeDTO.getMemberId() + "/debut";
    }

    //데뷔글 상세조회
    @GetMapping("/detail/{id}")
    public String detail(@AuthenticationPrincipal PrincipalDetails principalDetails,
                         @PathVariable Long id, Model model) {
        try {
            String loginId = principalDetails.getUsername();
            MemberDTO findDTO = memberService.findByLoginId(loginId);
            model.addAttribute("authentication", findDTO);
        } catch (Exception e) {
            model.addAttribute("authentication", new MemberDTO());
        } finally {
            DebutEpisodeDTO debutEpisodeDTO = debutService.detail(id);
            List<DebutCommentDTO> debutCommentDTOList = debutCommentService.findById(id);
            model.addAttribute("commentList", debutCommentDTOList);
            model.addAttribute("debut", debutEpisodeDTO);
        }
        return "debut/detail";
    }

    //데뷔글 업데이트 폼
    @GetMapping("/update-form/{id}")
    public String updateForm(@AuthenticationPrincipal PrincipalDetails principalDetails,
                             @PathVariable Long id, Model model) {
        String loginId = principalDetails.getUsername();
        MemberDTO findDTO = memberService.findByLoginId(loginId);
        model.addAttribute("authentication", findDTO);

        DebutEpisodeDTO debutEpisodeDTO = debutService.updateForm(id);
        model.addAttribute("debut", debutEpisodeDTO);
        return "debut/update";
    }

    //데뷔글 업데이트 처리
    @PostMapping("/update")
    public String update(@ModelAttribute DebutEpisodeDTO debutEpisodeDTO) throws IOException {
        debutService.update(debutEpisodeDTO);
        return "redirect:/debut/detail/" + debutEpisodeDTO.getId();
    }

    //글 삭제 처리
    @GetMapping("/delete/{id}")
    public String delete(@AuthenticationPrincipal PrincipalDetails principalDetails,
                         @PathVariable Long id) {
        String loginId = principalDetails.getUsername();
        MemberDTO findDTO = memberService.findByLoginId(loginId);

        debutService.delete(id);
        return "redirect:/member/myPage/" + findDTO.getId() + "/debut";
    }

    @GetMapping("/loveCheck/{id}")
    public @ResponseBody String loveCheck(@PathVariable("id") Long id, @RequestParam("memberId") Long memberId) {
        return debutService.loveCheck(id, memberId);

    }

    //데뷔글 좋아요 처리
    @GetMapping("/loveSave/{id}")
    public @ResponseBody int loveSave(@PathVariable Long id, @RequestParam("memberId") Long memberId) {
        return debutService.loveSave(id, memberId);
    }

    @GetMapping("/loveDelete/{id}")
    public @ResponseBody int loveDelete(@PathVariable Long id, @RequestParam("memberId") Long memberId) {
        return debutService.loveDelete(id, memberId);
    }

    //데뷔글 시 리스트
    @GetMapping("/poem/{category}/{addressId}")
    public String poemList(@AuthenticationPrincipal PrincipalDetails principalDetails,
                           @PathVariable("category") Long categoryId, Model model,
                           @PathVariable("addressId") int addressId) {
        try {
            String loginId = principalDetails.getUsername();
            MemberDTO findDTO = memberService.findByLoginId(loginId);
            model.addAttribute("authentication", findDTO);
        } catch (Exception e) {

        } finally {
            List<DebutEpisodeDTO> debutEpisodeDTOList = debutService.categoryList(categoryId, addressId);
            model.addAttribute("poemList", debutEpisodeDTOList);
        }
        return "debut/poemList";
    }

    //데뷔글 에세이 리스트
    @GetMapping("/essay/{category}/{addressId}")
    public String essayList(@AuthenticationPrincipal PrincipalDetails principalDetails,
                            @PathVariable("category") Long categoryId,
                            @PathVariable("addressId") int addressId, Model model) {
        try {
            String loginId = principalDetails.getUsername();
            MemberDTO findDTO = memberService.findByLoginId(loginId);
            model.addAttribute("authentication", findDTO);
        } catch (Exception e) {

        } finally {
            List<DebutEpisodeDTO> debutEpisodeDTOList = debutService.categoryList(categoryId, addressId);
            model.addAttribute("essayList", debutEpisodeDTOList);
        }
        return "debut/essayList";
    }

    //데뷔글 웹소설 리스트
    @GetMapping("/web/{category}/{addressId}")
    public String webList(@AuthenticationPrincipal PrincipalDetails principalDetails,
                          @PathVariable("category") Long categoryId,
                          @PathVariable("addressId") int addressId, Model model) {
        try {
            String loginId = principalDetails.getUsername();
            MemberDTO findDTO = memberService.findByLoginId(loginId);
            model.addAttribute("authentication", findDTO);
        } catch (Exception e) {

        } finally {
            List<DebutEpisodeDTO> debutEpisodeDTOList = debutService.categoryList(categoryId, addressId);
            model.addAttribute("webList", debutEpisodeDTOList);
        }
        return "debut/webList";
    }

    //데뷔글 매인 addressId=0 기본
    @GetMapping("/main/{addressId}")
    public String mainPage(@AuthenticationPrincipal PrincipalDetails principalDetails,
                           @PathVariable("addressId") int addressId, Model model) {
        try {
            String loginId = principalDetails.getUsername();
            MemberDTO findDTO = memberService.findByLoginId(loginId);
            model.addAttribute("authentication", findDTO);
        } catch (Exception e) {

        } finally {
            List<DebutEpisodeDTO> poemList = debutService.categoryList(2L, addressId);
            List<DebutEpisodeDTO> essayList = debutService.categoryList(3L, addressId);
            List<DebutEpisodeDTO> webList = debutService.categoryList(1L, addressId);
            model.addAttribute("poemList", poemList);
            model.addAttribute("essayList", essayList);
            model.addAttribute("webList", webList);
        }
        return "debut/main";
    }
}
