package com.memo.post.bo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.domain.Post;
import com.memo.post.mapper.PostMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PostBO {

//	private Logger logger = LoggerFactory.getLogger(PostBO.class);
	//private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private FileManagerService fileManagerService;
	
	// input: userId(�α��� �� ���)  output:List<Post>
	public List<Post> getPostListByUserId(int userId) {
		return postMapper.selectPostListByUserId(userId);
	}
	
	// input: params     output: X
	public void addPost(int userId, String userLoginId, 
			String subject, String content, MultipartFile file) {
		
		String imagePath = null;
		
		// ���ε��� �̹����� ���� �� ���ε�
		if (file != null) {
			imagePath = fileManagerService.saveFile(userLoginId, file);
		}
		
		postMapper.insertPost(userId, subject, content, imagePath);
	}
	
	// input: �۹�ȣ, userId    output: Post
	public Post getPostByPostIdUserId(int postId, int userId) {
		return postMapper.selectPostByPostIdUserId(postId, userId);
	}
	
	// input : �Ķ���͵�    output:X
	public void updatePostById(int userId, String userLoginId, int postId, String subject, String content, MultipartFile file) {
		
		// �������� �����´�. (1. �̹��� ��ü �� �����ϱ� ���� 2. ������Ʈ ��� �ִ��� Ȯ��)
		Post post = postMapper.selectPostByPostIdUserId(postId, userId);
		if (post == null) {
			log.info("[�� ����] post is null. postId:{}, userId:{}", postId, userId);
			return;
		}
		
		// ������ �ִٸ� 
		// 1) �� �̹����� ���ε� �Ѵ�.
		// 2) 1�� �ܰ谡 �����ϸ� ���� �̹��� ����(���� �̹����� �ִٸ�)
		String imagePath = null;
		if(file != null) {
			// ���ε�
			imagePath = fileManagerService.saveFile(userLoginId, file);
			
			// ���ε� ���� �� �����̹����� ������ ����
			if (imagePath != null && post.getImagePath() != null) {
				// ���ε� �����ϰ� ���� �̹��� ������ ������ ���� ����
				fileManagerService.deleteFile(post.getImagePath());
			}
		}
		// db ������Ʈ
		postMapper.updatePostByPostId(postId, subject, content, imagePath);
	}
	// input : postId, userId    output : x
	public void deletePostByPostIdUserId (int postId, int userId) {
		// �������� �ִ��� Ȯ��
		Post post = postMapper.selectPostByPostIdUserId(postId, userId);
		if(post == null) {
			log.info("[�� ����] post is null. postId:{}, userId:{}", postId, userId);
			return;
		}
		// DB ����
		int deleteRowCount = postMapper.deletePostByPostId(postId);
		
		// �̹����� �����ϸ� ���� && DB ������ ����		
		if (deleteRowCount > 0 && post.getImagePath() != null) {
			fileManagerService.deleteFile(post.getImagePath());
		}
	}
}