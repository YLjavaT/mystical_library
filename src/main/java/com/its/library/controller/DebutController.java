package com.its.library.controller;

import com.its.library.common.PagingConst;
import com.its.library.dto.DebutCommentDTO;
import com.its.library.dto.DebutEpisodeDTO;
import com.its.library.service.DebutCommentService;
import com.its.library.service.DebutService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/debut")
public class DebutController {
    private final DebutService debutService;
    private final DebutCommentService debutCommentService;

    //데뷔글 화면 요청처리
    @GetMapping("/save-form")
    public String saveForm() {
        return "debut/save";
    }

    //데뷔글 저장처리
    @PostMapping("/save")
    public String save(@ModelAttribute DebutEpisodeDTO debutEpisodeDTO, HttpSession session) throws IOException {
        Long id = (Long) session.getAttribute("id");
        debutService.save(debutEpisodeDTO, id);
        return "index";
    }

    //데뷔글 상세조회
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        DebutEpisodeDTO debutEpisodeDTO = debutService.detail(id);
        List<DebutCommentDTO> debutCommentDTOList = debutCommentService.findById(id);
        model.addAttribute("commentList",debutCommentDTOList);
        model.addAttribute("debut", debutEpisodeDTO);
        return "debut/detail";
    }

    //데뷔글 업데이트 폼
    @GetMapping("/update-form/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        DebutEpisodeDTO debutEpisodeDTO = debutService.updateForm(id);
        model.addAttribute("debut", debutEpisodeDTO);
        return "debut/update";
    }

    //데뷔글 업데이트 처리
    @PostMapping("/update")
    public String update(@ModelAttribute DebutEpisodeDTO debutEpisodeDTO) throws IOException {
        debutService.update(debutEpisodeDTO);
        return "index";
    }

    //글 삭제 처리
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        debutService.delete(id);
        return "index";
    }

    //데뷔글 좋아요 처리
    @GetMapping("/love/{id}")
    public @ResponseBody int love(@PathVariable Long id, @RequestParam("memberId") Long memberId, @RequestParam("num") int num) {
        if (num == 1) {
            int resultNum = debutService.loveSave(id, memberId);
            return resultNum;
        } else {
            int resultNum = debutService.loveDelete(id, memberId);
            return resultNum;
        }
    }

    //데뷔글 페이징 리스트
    //
//    @GetMapping("/poem/{category}")
//    public String poemPageingList(@PathVariable("category")Long categoryId,@PageableDefault(page = 1) Pageable pageable, Model model) {
//        Page<DebutEpisodeDTO> debutEpisodeDTOPage = debutService.poemList(categoryId,pageable);
//        model.addAttribute("debutEpisodePage", debutEpisodeDTOPage);
//        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / PagingConst.BLOCK_LIMIT))) - 1) * PagingConst.BLOCK_LIMIT + 1;
//        int endPage = ((startPage + PagingConst.BLOCK_LIMIT - 1) < debutEpisodeDTOPage.getTotalPages()) ? startPage + PagingConst.BLOCK_LIMIT - 1 : debutEpisodeDTOPage.getTotalPages();
//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", endPage);
//        return "debut/poemPagingList";
//    }
    @GetMapping("/poem/{category}/{addressId}")
    public String poemList(@PathVariable("category")Long categoryId,Model model,@PathVariable("addressId")int addressId) {

        List<DebutEpisodeDTO> debutEpisodeDTOList = debutService.categoryList(categoryId,addressId);
        model.addAttribute("poemList", debutEpisodeDTOList);
        System.out.println("poemList = " + debutEpisodeDTOList);
        return "debut/poemList";

    }
    //데뷔글 에세이 리스트
    @GetMapping("/essay/{category}/{addressId}")
    public String essayList(@PathVariable("category")Long categoryId,Model model ,@PathVariable("addressId")int addressId) {
        List<DebutEpisodeDTO> debutEpisodeDTOList = debutService.categoryList(categoryId,addressId);
        model.addAttribute("essayList", debutEpisodeDTOList);
        System.out.println("essayList = " + debutEpisodeDTOList);
        return "debut/essayList";

    }
    //데뷔글 웹소설 리스트
    @GetMapping("/web/{category}/{addressId}")
    public String webList(@PathVariable("category")Long categoryId,Model model ,@PathVariable("addressId")int addressId) {
        List<DebutEpisodeDTO> debutEpisodeDTOList = debutService.categoryList(categoryId,addressId);
        model.addAttribute("webList", debutEpisodeDTOList);
        System.out.println("webList = " + debutEpisodeDTOList);
        return "debut/webList";

    }
    //데뷔글 매인 addressId=0 기본
    @GetMapping("/main/{addressId}")
    public String mainPage(Model model ,@PathVariable("addressId")int addressId){
       List<DebutEpisodeDTO> poemList = debutService.categoryList(1L,addressId);
        List<DebutEpisodeDTO> essayList = debutService.categoryList(2L,addressId);
        List<DebutEpisodeDTO> webList = debutService.categoryList(3L,addressId);
        model.addAttribute("poemList",poemList);
        model.addAttribute("essayList",essayList);
        model.addAttribute("webList",webList);
        return "debut/main";
    }
}
