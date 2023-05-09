package bridge.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import bridge.dto.ReviewDto;
import bridge.dto.TagDto;
import bridge.dto.UserProfileDto;
import bridge.service.BridgeService;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@Slf4j
public class UserProfileController {

	@Autowired
	BridgeService bridgeService;

	// 프로필 작성
	@PostMapping("/api/insertProfile/{userId}")
	public ResponseEntity<Map<String, Object>> insertProfile(@PathVariable("userId") String userId,
			@RequestPart(value = "data", required = false) UserProfileDto userProfileDto,
			@RequestPart(value = "files", required = false) MultipartFile[] files,
			@RequestPart(value = "music", required = false) MultipartFile[] files1,
			@RequestPart(value = "tag", required = false) TagDto tag) throws Exception {
		System.out.println(tag);
		String UPLOAD_PATH = "C:\\Temp\\";
		int insertedCount = 0;
		try {
			for (MultipartFile mf : files) {
				String uuid = UUID.randomUUID().toString();
//				String originFileName = mf.getOriginalFilename(); // 원본 파일 명
//				String safeFile = System.currentTimeMillis() + originFileName;
				userProfileDto.setProfileImg(uuid);
//				fileNames = fileNames + "," + safeFile;
				try {
					File f1 = new File(UPLOAD_PATH + uuid + ".jpg");
					mf.transferTo(f1);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
			}
			for (MultipartFile mf : files1) {
				String uuid = UUID.randomUUID().toString();
					userProfileDto.setUserMusic(uuid);
				try {
					File f1 = new File(UPLOAD_PATH + uuid + ".mp3");
					mf.transferTo(f1);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
			}
			tag.setUserId(userId);
			bridgeService.insertTag(tag);
			insertedCount = bridgeService.insertProfile(userProfileDto);

			if (insertedCount > 0) {
				Map<String, Object> result = new HashMap<>();
				result.put("message", "정상적으로 등록되었습니다.");
				result.put("count", insertedCount);
				result.put("userId", userProfileDto.getUserId());
				return ResponseEntity.status(HttpStatus.OK).body(result);
			} else {
				Map<String, Object> result = new HashMap<>();
				result.put("message", "등록된 내용이 없습니다.");
				result.put("count", insertedCount);
				return ResponseEntity.status(HttpStatus.OK).body(result);
			}
		} catch (Exception e) {
			Map<String, Object> result = new HashMap<>();
			System.out.println(e);
			result.put("message", "등록 중 오류가 발생했습니다.");
			result.put("count", insertedCount);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}
	
	@GetMapping("/api/profile/{userId}")
	public ResponseEntity<Map<String, Object>> getPorfile(@PathVariable("userId") String userId) throws Exception {
		Map<String, Object> result = new HashMap<>();
		List<UserProfileDto> list = bridgeService.getPorfile(userId);
		List<TagDto> taglist = bridgeService.getTaglist(userId);
		List<ReviewDto> reviewDto = bridgeService.getReview(userId);
		
		result.put("profile", list);
		result.put("taglist", taglist);
		
		if (list == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(result);
		}
	}

}
