package com.app.usearth.controller;

import com.app.usearth.domain.CommentDTO;
import com.app.usearth.domain.PostDTO;
import com.app.usearth.domain.UserDTO;
import com.app.usearth.service.RecyclingAgentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/recycling-agent/*")
@RequiredArgsConstructor
@Slf4j
public class RecyclingAgentController {

    private final RecyclingAgentService recyclingAgentService;

    @GetMapping("recycling-agent")
    public void getByRecycling() {;}

    @GetMapping("recycling-agentread/{id}")
    public String getByRecyclingRead(@PathVariable Long id, Model model) {
        Optional<PostDTO> foundPost = recyclingAgentService.getByRecyclingReadId(id);

        recyclingAgentService.updateViewCount(id);

        if (foundPost.isPresent()) model.addAttribute("post", foundPost.get());
        return "recycling-agent/recycling-agentread";
    }
//   Model을 사용하는 이유 : (Key, Value 형식을 가지고) 화면으로 data값을 가져가기 위해 사용



    // 재활용대행 신청
    @GetMapping("recycling-agentwrite")
    public String goToRecyclingWrite(Model model) {

        PostDTO postDTO = new PostDTO();
        model.addAttribute("post", postDTO);
        return "recycling-agent/recycling-agentwrite";
    }

    // 재활용대행 신청 후 저장
    @PostMapping("recycling-agentwrite")
    public RedirectView writePost(PostDTO postDTO, HttpSession session){
//      UserDTO 타입에 session을 담아서 userDTO 담음(user: key값)
        UserDTO userDTO = ((UserDTO)session.getAttribute("user"));
//      user의 모든 값이 userDTO에 담김
//      userDTO의 id를 가져옴(getId())
        postDTO.setUserId(userDTO.getId());
        recyclingAgentService.addPost(postDTO);
        Long postId = postDTO.getId();
        return new RedirectView("/recycling-agent/recycling-agentread/" + postId);
    }

    // 재활용대행 수정하기
    @GetMapping("recycling-agentwrite/{id}")
    public String goToRecyclingModify(@PathVariable Long id, Model model) {
        PostDTO postDTO = recyclingAgentService.getPostById(id);
        model.addAttribute("post", postDTO);
        log.info("--------------{}", model);
        return "recycling-agent/recycling-agentwrite";
    }

    // 게시글 수정 후 저장
    @PostMapping("recycling-agentwrite/{id}")
    public RedirectView updatePost(@PathVariable Long id, PostDTO postDTO, HttpSession session) {
        UserDTO userDTO = ((UserDTO)session.getAttribute("user"));
        postDTO.setUserId(userDTO.getId());
        postDTO.setId(id); // 게시글 ID 설정

        // 게시글 정보를 수정하는 서비스 메소드를 호출
        recyclingAgentService.updatePost(postDTO);
        Long modifyPostId = postDTO.getId();
        return new RedirectView("/recycling-agent/recycling-agentread/" + modifyPostId);
    }



}

