package com.multi.culture_link.board.controller;

import com.multi.culture_link.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board/view")
public class BoardViewController {
    private final BoardService boardService;

    @GetMapping()
    public String board() {
        return "/board/board";
    }

    @GetMapping("/{boardId}")
    public String detailBoard() {
        return "/board/boardDetail";

    }



    @GetMapping("/create")
    public String createBoard() {
        return "/board/createBoard";

    }



    @GetMapping("/update/{boardId}")
    public String updateBoard() {
        return "/board/updateBoard";
    }

}
