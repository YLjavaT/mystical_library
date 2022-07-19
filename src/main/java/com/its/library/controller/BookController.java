package com.its.library.controller;

import com.its.library.common.PagingConst;
import com.its.library.dto.BookDTO;
import com.its.library.dto.EpisodeDTO;
import com.its.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    // 책 저장페이지 요청
    @GetMapping("/book-save-form")
    public String bookSaveForm(){

        return "book/save";
    }

    // 책 저장처리
    @PostMapping("/req-book-save")
    public String reqBookSave(@ModelAttribute BookDTO bookDTO) throws IOException {

        BookDTO saveDTO = bookService.reqBookSave(bookDTO);
//      return "redirect:/book?category=" + saveDTO.getCategoryId()  + "/book/" + saveDTO.getId();
        return "redirect:/member/myPage/" + saveDTO.getMemberId();
    }

    // 회차 저장페이지 출력
    @GetMapping("/episode-save-form/{id}")
    public String episodeSaveForm(@PathVariable("id") Long bookId, Model model){
        model.addAttribute("bookId",bookId);
        return "book/episodeSave";
    }

    // 회차 저장처리
    @PostMapping("/req-episode-save")
    public String reqEpisodeSave(@ModelAttribute EpisodeDTO episodeDTO) throws IOException {
        Long id = bookService.reqEpisodeSave(episodeDTO);
        BookDTO bookDTO = bookService.findById(episodeDTO.getBookId());
        return "redirect:/book?category="+ bookDTO.getCategoryId() + "/book/" + id;
    }

    // 책 상세조회
    @GetMapping("/book/{id}")
    public String bookDetail(@PageableDefault(page = 1) Pageable pageable,
                             @RequestParam("categoryId") Long categoryId,
                             @PathVariable("id") Long id, Model model){
        BookDTO bookDTO = bookService.findById(id);
        model.addAttribute("book", bookDTO);
        Page<EpisodeDTO> episodeDTOList = bookService.episodeFindAll(id, pageable);
        model.addAttribute("episodeList", episodeDTOList);
        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / PagingConst.BLOCK_LIMIT))) - 1) * PagingConst.BLOCK_LIMIT + 1;
        int endPage = ((startPage + PagingConst.BLOCK_LIMIT - 1) < episodeDTOList.getTotalPages()) ? startPage + PagingConst.BLOCK_LIMIT - 1 : episodeDTOList.getTotalPages();
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "book/detail";
    }

    // 회차 상세조회
    @GetMapping("/episode/{id}")
    public String episodeDetail(@RequestParam("bookId") Long bookId, @PathVariable("id") Long id,
                                Model model) {
        EpisodeDTO episodeDTO = bookService.episodeFindById(id);
        BookDTO bookDTO = bookService.findById(bookId);
        model.addAttribute("book", bookDTO);
        model.addAttribute("episode", episodeDTO);
        return "book/episodeDetail";
    }



}
