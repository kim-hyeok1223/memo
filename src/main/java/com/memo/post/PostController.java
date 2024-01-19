package com.memo.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.memo.post.bo.PostBO;
import com.memo.post.domain.Post;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/post")
@Controller
public class PostController {
	
	@Autowired
	private PostBO postBO;
	
	@GetMapping("/post-list-view")
	public String postListView(Model model, HttpSession session) {
		// �α��� ���� ��ȸ
		Integer userId = (Integer)session.getAttribute("userId");
		if (userId == null) {
			// ��α����̸� �α��� �������� �̵�
			return "redirect:/user/sign-in-view";
		}
		
		// DB �� ��� ��ȸ
		List<Post> postList = postBO.getPostListByUserId(userId);
		model.addAttribute("postList", postList);
		model.addAttribute("viewName", "post/postList");
		return "template/layout";
	}
	
	/**
	 * �۾��� ȭ��
	 * @param model
	 * @return
	 */
	@GetMapping("/post-create-view")
	public String postCreateView(Model model) {
		model.addAttribute("viewName", "post/postCreate");
		return "template/layout";
	}
	
	@GetMapping("/post-detail-view")
	public String postDetailView(
			@RequestParam("postId") int postId,
			Model model,
			HttpSession session) {
		
		int userId = (int)session.getAttribute("userId");
		// DB ��ȸ - postId + userId
		Post post = postBO.getPostByPostIdUserId(postId, userId);
		
		model.addAttribute("post", post);
		model.addAttribute("viewName", "post/postDetail");
		return "template/layout";
	}
}