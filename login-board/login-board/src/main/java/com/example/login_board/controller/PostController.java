// src/main/java/com/example/loginboard/controller/PostController.java
package com.example.login_board.controller;

import com.example.login_board.entity.Post;
import com.example.login_board.entity.User;
import com.example.login_board.repository.PostRepository;
import com.example.login_board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.login_board.entity.Comment;
import com.example.login_board.repository.CommentRepository;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;  // 여기에 추가

    @GetMapping("/posts")
    public String listPosts(Model model) {
        List<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "posts";
    }

    @GetMapping("/posts/new")
    public String newPostForm() {
        return "new-post";
    }

    @PostMapping("/posts")
    public String createPost(@RequestParam String title,
                             @RequestParam String content,
                             @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(user);
        postRepository.save(post);

        return "redirect:/posts";
    }

    @GetMapping("/posts/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        model.addAttribute("post", post);
        return "post-detail";
    }

    @GetMapping("/posts/{id}/edit")
    public String editPost(@PathVariable Long id,
                           Model model,
                           @AuthenticationPrincipal UserDetails userDetails) {
        Post post = postRepository.findById(id).orElseThrow();
        if (!post.getAuthor().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/posts";
        }
        model.addAttribute("post", post);
        return "edit-post";
    }

    @PostMapping("/posts/{id}/edit")
    public String updatePost(@PathVariable Long id,
                             @RequestParam String title,
                             @RequestParam String content,
                             @AuthenticationPrincipal UserDetails userDetails) {
        Post post = postRepository.findById(id).orElseThrow();
        if (post.getAuthor().getUsername().equals(userDetails.getUsername())) {
            post.setTitle(title);
            post.setContent(content);
            postRepository.save(post);
        }
        return "redirect:/posts/" + id;
    }

    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Long id,
                             @AuthenticationPrincipal UserDetails userDetails) {
        Post post = postRepository.findById(id).orElseThrow();
        if (post.getAuthor().getUsername().equals(userDetails.getUsername())) {
            postRepository.delete(post);
        }
        return "redirect:/posts";
    }

    @PostMapping("/posts/{id}/comments")
    public String addComment(@PathVariable Long id,
                             @RequestParam String content,
                             @AuthenticationPrincipal UserDetails userDetails) {

        Post post = postRepository.findById(id).orElseThrow();
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post);
        comment.setAuthor(user);

        commentRepository.save(comment);
        return "redirect:/posts/" + id;
    }


}
