package bridge.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import bridge.dto.PartnerContentDto;
import bridge.dto.PartnerDetailCommentDto;
import bridge.dto.PartnerDetailDto;
import bridge.dto.PayListDto;
import bridge.security.JwtTokenUtil;
import bridge.service.BridgeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class bridgeApiController {

	@Autowired
	private BridgeService bridgeService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
//	
	private PayListDto payListDto;
	private PartnerContentDto partnerContentDto;

	// 2. 파트너 협업창 게시글 조회
	@GetMapping("/api/bridge/partnerdetail/{pdIdx}")
	public ResponseEntity<List<PartnerContentDto>> openPartnerContentList(@PathVariable("pdIdx") int pdIdx)
			throws Exception {
		List<PartnerContentDto> list = bridgeService.selectPartnerContent(pdIdx);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}

	// 3. 파트너 협업창 게시글 상세조회
	@GetMapping("api/bridge/partnerdetail/content/{pcIdx}")
	@ResponseBody
	public ResponseEntity<PartnerContentDto> openPartnerContentDetail(@PathVariable("pcIdx") int pcIdx)
			throws Exception {
		PartnerContentDto partnerContentDto = bridgeService.selectPartnerContentDetail(pcIdx);

		return ResponseEntity.status(HttpStatus.OK).body(partnerContentDto);
	}

	// 4. 파트너 협업창 게시글 등록
	@PostMapping("/api/bridge/partnerdetail/write/{pdIdx}")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Map<String, Object>> insertPartnetContent(@PathVariable int pdIdx,
			@RequestPart(value = "Data", required = false) PartnerContentDto partnerContentDto,
			@RequestPart(value = "files", required = false) MultipartFile[] pcFile) throws Exception {
		String UPLOAD_PATH = "C:/temp/";
//		/upload/
	
		String uuid = UUID.randomUUID().toString();
		List<String> fileNames = new ArrayList<>();

		System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaas" + pcFile);
//			PartnerContentDto partnerContentDto = new PartnerContentDto();
		int insertedCount;
		try {
			if (pcFile != null) {
				for (MultipartFile mf : pcFile) {
					String originFileName = mf.getOriginalFilename();

					// 파일이 음악 파일인지 확인
					if (isAudioFile(originFileName)) {
						// 음악 파일인 경우 처리
						try {
							File f = new File(UPLOAD_PATH + File.separator + uuid + ".mp3");
							mf.transferTo(f);
						} catch (IllegalStateException e) {
							e.printStackTrace();
						}
						partnerContentDto.setCmMusic(uuid);
					} else {
						// 일반 파일인 경우 처리
						try {
							File f = new File(UPLOAD_PATH + originFileName);
							mf.transferTo(f);
						} catch (IllegalStateException e) {
							e.printStackTrace();
						}
						partnerContentDto.setPcFile(originFileName);
					}

				}
			}
			System.out.println("aaaaaaaaaaaaaaaaaaaasssssssssssssssssss" + partnerContentDto);
			if (partnerContentDto.getPcContent() == "") {
				insertedCount = 0;
			} else {
				insertedCount = bridgeService.insertPartnerContent(partnerContentDto);
			}
			if (insertedCount > 0) {
				Map<String, Object> result = new HashMap<>();
				result.put("message", "정상적으로 등록되었습니다.");
				result.put("count", insertedCount);
				result.put("pcIdx", partnerContentDto.getPcIdx());
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
			System.out.println("111111111111111" + result);
			System.out.println("222222222222" + pcFile);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}

	// 4-1. pcFile이 음악 파일인지 여부를 확인하는 메서드
	private boolean isAudioFile(String filename) {
		// 파일 이름에서 확장자 추출
		String extension = StringUtils.getFilenameExtension(filename);

		// 음악 파일 확장자 목록
		String[] audioExtensions = { "mp3", "wav", "flac" };

		// 확장자가 음악 파일 확장자 목록에 포함되어 있는지 확인
		if (extension != null) {
			for (String audioExtension : audioExtensions) {
				if (extension.equalsIgnoreCase(audioExtension)) {
					return true;
				}
			}
		}

		return false;
	}

	// 5. 파트너 협업창 게시글 수정
	@PutMapping("/api/bridge/partnerdetail/update/{pcIdx}")
	public ResponseEntity<Map<String, Object>> updatePartnerContent(@PathVariable("pcIdx") int pcIdx,
			@RequestPart(value = "Data", required = false) PartnerContentDto partnerContentDto,
			@RequestPart(value = "files", required = false) MultipartFile[] pcImg) throws Exception {
		String UPLOAD_PATH = "C:/temp/";
//		upload/
		String uuid = UUID.randomUUID().toString();
		int insertedCount;
		partnerContentDto.setPcIdx(pcIdx);
		try {
			if (pcImg != null) {
				for (MultipartFile mf : pcImg) {
					String originFileName = mf.getOriginalFilename();

					// 파일이 음악 파일인지 확인
					if (isAudioFile(originFileName)) {
						// 음악 파일인 경우 처리
						try {
							File f = new File(UPLOAD_PATH + File.separator + uuid + ".mp3");
							mf.transferTo(f);
						} catch (IllegalStateException e) {
							e.printStackTrace();
						}
						partnerContentDto.setCmMusic(uuid);
					} else {
						// 일반 파일인 경우 처리
						try {
							File f = new File(UPLOAD_PATH + originFileName);
							mf.transferTo(f);
						} catch (IllegalStateException e) {
							e.printStackTrace();
						}
						partnerContentDto.setPcFile(originFileName);
					}
				}
				insertedCount = bridgeService.updatePartnerContent(partnerContentDto);

			} else if (partnerContentDto.getPcContent() == "" && pcImg == null) {
				insertedCount = 0;
			} else {
				insertedCount = bridgeService.updatePartnerContent(partnerContentDto);
			}
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>" + partnerContentDto);

			if (insertedCount > 0) {
				Map<String, Object> result = new HashMap<>();
				result.put("message", "정상적으로 등록되었습니다.");
//							result.put("count", insertedCount);
				result.put("pcContent", partnerContentDto.getPcContent());
				result.put("pcIdx", partnerContentDto.getPcIdx());
				result.put("pcImg", partnerContentDto.getPcFile());
				System.out.println("1111111111111111111111111");
				return ResponseEntity.status(HttpStatus.OK).body(result);
			} else {
				Map<String, Object> result = new HashMap<>();
				result.put("message", "등록된 내용이 없습니다.");
				result.put("count", insertedCount);
				System.out.println("222222222222222");
				return ResponseEntity.status(HttpStatus.OK).body(result);
			}
		} catch (Exception e) {
			Map<String, Object> result = new HashMap<>();
			System.out.println(e);
			result.put("message", "등록 중 오류가 발생했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}

	// 6. 파트너 협업창 게시글 삭제
	@DeleteMapping("/api/bridge/partnerdetail/delete/{pcIdx}")
	public ResponseEntity<Object> deletePartnerContent(@PathVariable("pcIdx") int pcIdx) throws Exception {
		try {
			bridgeService.deletePartnerContent(pcIdx);
			return ResponseEntity.status(HttpStatus.OK).body('Y');
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
		}
	}

	// 7. 파트너 협업창 결제 내역
	@GetMapping("/api/bridge/partnerdetail/paylist/{userId1}/{userId2}")
	public ResponseEntity<PayListDto> openPayList(@PathVariable("userId1") String userId1,
			@PathVariable("userId2") String userId2) throws Exception {
		PayListDto payListDto = new PayListDto();

		payListDto.setUserId1(userId1);
		payListDto.setUserId2(userId2);

		PayListDto payListDto1 = bridgeService.selectPayList(payListDto);

		if (payListDto1 != null) {
			return ResponseEntity.status(HttpStatus.OK).body(payListDto1);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	// 8. 파트너 협업창 작업목록 조회
	@GetMapping("/api/bridge/partnerdetail/projectList/{userId1}")
	public ResponseEntity<List<PartnerDetailDto>> openProjectList(@PathVariable("userId1") String userId1) {
		try {

			List<PartnerDetailDto> list1 = bridgeService.selectProjectList(userId1);

			if (list1.size() != 0) {
				return ResponseEntity.status(HttpStatus.OK).body(list1);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	// 8-1. 작업 목록 추가
	@PostMapping("/api/bridge/partnerdetail/projectList/insert")
	public ResponseEntity<Integer> addProjectList(@RequestBody PartnerDetailDto partnerDetailDto) throws Exception {
		
		int insertProjectListCount = bridgeService.insertProjectList(partnerDetailDto);
		
		return ResponseEntity.status(HttpStatus.OK).body(insertProjectListCount);
		
	}
	
	// 8-2. 작업 목록 삭제
	@DeleteMapping("/api/bridge/partnerdetail/projectList/delete/{pdIdx}")
	public ResponseEntity<Integer> deleteProjectList(@PathVariable("pdIdx") int pdIdx) {
		
		int deleteProjectListCount;
		try {
			deleteProjectListCount = bridgeService.deleteProjectList(pdIdx);
			return ResponseEntity.status(HttpStatus.OK).body(deleteProjectListCount);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		 
	}

	// 9. 파트너 협업창 게시글의 댓글 조회
	@GetMapping("/api/bridge/partnerdetail/comment/{pcIdx}")
	public ResponseEntity<List<PartnerDetailCommentDto>> openPartnerCommentList(@PathVariable("pcIdx") int pcIdx)
			throws Exception {

		try {

			List<PartnerDetailCommentDto> list = bridgeService.selectPartnerComment(pcIdx);

			if (list.size() != 0) {
				return ResponseEntity.status(HttpStatus.OK).body(list);
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(list);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	// 10. 파트너 협업창 게시글의 댓글 작성
	@PostMapping("/api/bridge/partnerdetail/comment/write/{pcIdx}")
	public ResponseEntity<Integer> addComment(@RequestBody PartnerDetailCommentDto partnerDetailCommentDto)
			throws Exception {

		int insertCommentCount;

		if (partnerDetailCommentDto.getPdcComment() == "") {
			insertCommentCount = 0;
		} else {
			insertCommentCount = bridgeService.insertPartnerComment(partnerDetailCommentDto);
		}

		return ResponseEntity.status(HttpStatus.OK).body(insertCommentCount);

//		if (insertCommentCount != 1) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(insertCommentCount);
//		} else {
//			return ResponseEntity.status(HttpStatus.OK).body(insertCommentCount);
//		}

	}

	// 11. 파트너 협업창 게시글의 댓글 삭제
	@DeleteMapping("/api/bridge/partnerdetail/comment/delete/{pdcIdx}")
	public ResponseEntity<Integer> deleteComment(@PathVariable("pdcIdx") int pdcIdx) throws Exception {

		int deletedCount = bridgeService.deletePartnerComment(pdcIdx);

		if (deletedCount != 1) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(deletedCount);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(deletedCount);
		}
	}

	// 12. 파트너 협업창 작업 진행 상황
	@PutMapping("/api/bridge/partnerdetail/complete/{pdIdx}")
	public ResponseEntity<Integer> projectComplete(@PathVariable("pdIdx") int pdIdx) throws Exception {

		int completeCount = bridgeService.partnerComplete(pdIdx);
		bridgeService.partnerMoney(pdIdx);

		if (completeCount != 1) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(completeCount);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(completeCount);
		}
	}

	// 13. 파트너 협업창 게시글 파일 클릭 시 다운로드
	@GetMapping("/api/bridge/partnerdetail/download/{fileName}")
	public void downloadFile(@PathVariable("fileName") String fileName, HttpServletResponse response) throws Exception {
		String filePath = "C:\\Temp\\" + fileName;
//		upload\\
		File file = new File(filePath);
		if (file.exists()) {
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			try (FileInputStream inputStream = new FileInputStream(file);
					ServletOutputStream outputStream = response.getOutputStream()) {
				byte[] buffer = new byte[1024];
				int length;
				while ((length = inputStream.read(buffer)) > 0) {
					outputStream.write(buffer, 0, length);
				}
				outputStream.flush();
			}
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

}
