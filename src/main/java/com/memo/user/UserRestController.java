package com.memo.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.memo.common.EncryptUtils;
import com.memo.user.bo.UserBO;
import com.memo.user.entity.UserEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/user")
@RestController
public class UserRestController {
	
	@Autowired
	private UserBO userBO;

	/**
	 * ���̵� �ߺ�Ȯ�� API
	 * @param loginId
	 * @return
	 */
	@RequestMapping("/is-duplicated-id")
	public Map<String, Object> isDuplicatedId(
			@RequestParam("loginId") String loginId) {
		
		// DB ��ȸ - select
		UserEntity user = userBO.getUserEntityByLoginId(loginId);
		
		Map<String, Object> result = new HashMap<>();
		if (user != null) { // �ߺ�
			result.put("code", 200);
			result.put("is_duplicated_id", true);
		} else { // �ߺ� �ƴ�
			result.put("code", 200);
			result.put("is_duplicated_id", false);
		}
		return result;
	}
	
	/**
	 * ȸ������ API
	 * @param loginId
	 * @param password
	 * @param name
	 * @param email
	 * @return
	 */
	@PostMapping("/sign-up")
	public Map<String, Object> signUp(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			@RequestParam("name") String name,
			@RequestParam("email") String email) {
		
		// md5 �˰��� => password hashing
		// aaaa => 74b8733745420d4d33f80c4663dc5e5 
		// aaaa => 74b8733745420d4d33f80c4663dc5e5
		String hashedPassword = EncryptUtils.md5(password);
		
		// db insert
		Integer userId = userBO.addUser(loginId, hashedPassword, name, email);
		
		// ���䰪
		Map<String, Object> result = new HashMap<>();
		if (userId != null) {
			result.put("code", 200);
			result.put("result", "����");
		} else {
			result.put("code", 500);
			result.put("error_message", "ȸ�����Կ� �����߽��ϴ�.");
		}
		
		return result;
	}
	
	@PostMapping("/sign-in")
	public Map<String, Object> signIn(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			HttpServletRequest request) {
		
		// ��й�ȣ hashing - md5
		String hashedPassword = EncryptUtils.md5(password);
		
		// db ��ȸ(loginId, �ؽ̵� ��й�ȣ) => UserEntity
		UserEntity user = userBO.getUserEntityByLoginIdPassword(loginId, hashedPassword);
		
		// ���䰪
		Map<String, Object> result = new HashMap<>();
		if (user != null) { // ����
			// �α��� ó��
			// �α��� ������ ���ǿ� ��´�.(����� ����)
			HttpSession session = request.getSession();
			session.setAttribute("userId", user.getId());
			session.setAttribute("userLoginId", user.getLoginId());
			session.setAttribute("userName", user.getName());
			
			result.put("code", 200);
			result.put("result", "����");
		} else { // �α��� �Ұ�
			result.put("code", 300);
			result.put("error_message", "�������� �ʴ� ������Դϴ�.");
		}
		return result;
	}
	
}