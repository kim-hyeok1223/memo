package com.memo.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component //spring bean
public class FileManagerService {

	// ���� ���ε� �� �̹����� ����� ��� (����)
	public static final String FILE_UPLOAD_PATH = "D:\\kimjinhyeok\\6_spring_project\\MEMO\\memo_workspace\\images/";
	// public static final String FILE_UPLOAD_PATH = "D:\\kimjinhyeok\\6_spring_project\\MEMO\\memo_workspace\\images";
	
	// input:File ����, userLoginId(������)    output: �̹��� ��� 
	public String saveFile(String loginId, MultipartFile file) {
		// ����(���丮) ����
		// ��: aaaa_1234123421/sun.png
		String directoryName = loginId + "_" + System.currentTimeMillis();
		String filePath = FILE_UPLOAD_PATH + directoryName; // D:\\kimjinhyeok\\6_spring_project\\MEMO\\memo_workspace\\images/aaaa_1234123421/sun.png"
		
		File directory = new File(filePath);
		if(directory.mkdir() == false) {
			// ���� ���� ���� �� �̹��� ��� null ����
			return null;
		}
		// ���� ���ε� : byte ������ ���ε�
		try {
			byte[] bytes = file.getBytes();
			// �ڡڡڡڡ� �ѱ� �̸� �̹����� �ø� �� �����Ƿ� ���߿� �����ڷ� �ٲ㼭 �ø���
			Path path = Paths.get(filePath + "/" + file.getOriginalFilename());
			Files.write(path, bytes); // ���� ���� ���ε�
		} catch (IOException e) {
			e.printStackTrace();
			return null; // �̹��� ���ε� ���� �� null ����
		}
		
		// ���� ���ε尡 ���������� �� �̹��� url path�� ����
		// �ּҴ� �̷��� �� ���̴�. (����)
		// /images/aaaa_1234123421/sun.png
		return "/images/" + directoryName + "/" + file.getOriginalFilename();
		
	}
	
	//input:imagePath     output :X
	public void deleteFile(String imagePath) {
		// D:\\kimjinhyeok\\6_spring_project\\MEMO\\memo_workspace\\images/
		// �ּҿ� ��ġ�� /images/ �����.
		Path path = Paths.get(FILE_UPLOAD_PATH + imagePath.replace("/images/", ""));
		
		// ������ �̹����� �����ϴ°�?
		if (Files.exists(path)) {
			try {
				Files.delete(path);
			} catch (IOException e) {
				log.info("[���ϸŴ��� ����] �̹��� ���� ����. path:{}", path.toString());
				return;
			}
			// ����(���丮) ����
			path = path.getParent();
			if(Files.exists(path)) {
				try {
					Files.delete(path);
				} catch (IOException e) {
					log.info("[���ϸŴ��� ����] ���� ���� ����. path:{}", path.toString());
				}
			}
		}
	}
}
