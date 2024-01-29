package com.memo.post;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.memo.post.bo.PostBO;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/post")
@RestController
public class PostRestController {

	@Autowired
	private PostBO postBO;
	
	/**
	 * �۾��� API
	 * @param subject
	 * @param content
	 * @param file
	 * @param session
	 * @return
	 */
	@PostMapping("/create")
	public Map<String, Object> create(
			@RequestParam("subject") String subject,
			@RequestParam("content") String content,
			@RequestParam(value = "file", required = false) MultipartFile file,
			HttpSession session) {
		
		// �۾��� ��ȣ - session�� �ִ� userId�� ������.
		int userId = (int)session.getAttribute("userId");
		String userLoginId = (String)session.getAttribute("userLoginId");
		
		// DB Insert
		postBO.addPost(userId, userLoginId, subject, content, file);
		
		// ���䰪
		Map<String, Object> result = new HashMap<>();
		result.put("code", 200);
		result.put("result", "����");
		return result;
	}
	/**
	 * �� ���� API
	 * @param postId
	 * @param subject
	 * @param content
	 * @param file
	 * @param session
	 * @return
	 */
	@PutMapping("/update")
	public Map<String, Object> update(
			@RequestParam("postId") int postId,
			@RequestParam("subject") String subject,
			@RequestParam("content") String content,
			@RequestParam(value = "file" , required = false) MultipartFile file,
			HttpSession session) {
		
		int userId = (int)session.getAttribute("userId");
		String userLoginId = (String)session.getAttribute("userLoginId");
		
		// db update
		postBO.updatePostById(userId, userLoginId, postId, subject, content, file);
		// ���� 
		Map<String, Object> result = new HashMap<>();
		result.put("code", 200);
		result.put("result", "����");
		return result;
		
	}
	
	@PostMapping("/delete")
	public Map<String, Object> delete(
			@RequestParam("postId") int postId,
			HttpSession session) {
		
		// �α��� ���� Ȯ��
		int userId = (int)session.getAttribute("userId");
		Map<String, Object> result = new HashMap<>();
		
		
		postBO.deletePostByPostIdUserId(postId, userId);
		
		result.put("code", 200);
		result.put("result", "����");
		
		return result;
	}
	
}