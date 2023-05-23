package bridge.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import bridge.dto.AnnouncementDto;
import bridge.dto.CommentsDto;
import bridge.dto.ComposerRequestDto;
import bridge.dto.ComposerRequestTagDto;
import bridge.dto.MusicDto;
import bridge.dto.PartnerContentDto;
import bridge.dto.PartnerDetailCommentDto;
import bridge.dto.PartnerDetailDto;
import bridge.dto.PayListDto;
import bridge.dto.ReportDto;
import bridge.dto.ReviewDto;
import bridge.dto.TagDto;
import bridge.dto.UserDto;
import bridge.dto.UserProfileDto;

public interface BridgeService {

	public void insertMusic(MusicDto musicDto);

	public String selectMusic(String musicUUID) throws Exception;

	public int insertComments(CommentsDto commentsDto);

	public List<CommentsDto> selectCommentsList(int cIdx);

	public void updateComments(CommentsDto commentsDto);

	public void deleteComments(int ccIdx);

	// 신고
	public int insertReport(ReportDto reportDto);

	public List<ReportDto> openReportList();

	// 공지
	public List<AnnouncementDto> announcementList();

	public AnnouncementDto announcementDetail(int aIdx);

	// 포인트 충전
	public UserDto chargePoint(String userId);

	public ReportDto openReportDetail(int reportIdx);

	public void handleReport(UserDto userDto);
	
	// 파트너 구인 작성
	public int insertPartnerWrite(ComposerRequestDto composerRequestDto, MultipartFile[] files);

	public int insertProfile(UserProfileDto userProfileDto);

//	public int doCharge(UserDto userDto);
	
	// 1. 파트너 협업창 조회

	// 2. 파트너 협업창 게시글 조회
	public List<PartnerContentDto> selectPartnerContent(int pdIdx) throws Exception;

	// 3. 파트너 협업창 게시글 상세조회
	public PartnerContentDto selectPartnerContentDetail(int pcIdx) throws Exception;

	// 4. 파트너 협업창 게시글 등록
	public int insertPartnerContent(PartnerContentDto partnerContentDto) throws Exception;

	// 5. 파트너 협업창 게시글 수정
	public int updatePartnerContent(PartnerContentDto partnerContentDto) throws Exception;

	// 6. 파트너 협업창 게시글 삭제
	public int deletePartnerContent(int pcIdx) throws Exception;

	// 7. 파트너 협업창 결제 내역
	public PayListDto selectPayList(PayListDto payListDto) throws Exception;

	// 8. 파트너 협업창 작업목록 조회
	public List<PartnerDetailDto> selectProjectList(String userId1) throws Exception;
	
	// 8-1. 작업 목록 추가 
	public int insertProjectList(PartnerDetailDto partnerDetailDto) throws Exception;
	
	// 8-2. 작업 목록 삭제
	public int deleteProjectList(int pdIdx) throws Exception;

	// 9. 파트너 협업창 게시글의 댓글 조회
	public List<PartnerDetailCommentDto> selectPartnerComment(int pcIdx) throws Exception;

	// 10. 파트너 협업창 게시글의 댓글 작성
	public int insertPartnerComment(PartnerDetailCommentDto partnerDetailCommentDto) throws Exception;


	// 11. 파트너 협업창 게시글의 댓글 삭제
	public int deletePartnerComment(int pdcIdx) throws Exception;
	
	// 12, 파트너 협업창 작업 진행 상황
	public int partnerComplete(int pdIdx) throws Exception;
	

	public void insertTag(TagDto tag);

	public List<UserProfileDto> getPorfile(int idx);

	public List<TagDto> getTaglist(int idx);

	public List<ReviewDto> getReview(String userId);

	//파트너 작업 태그 작성
	public void insertCrtTag(ComposerRequestTagDto crtTag);

	public List<ComposerRequestDto> openPartnerList();

	public List<ComposerRequestTagDto> partnerTagList();
	public UserDto getUserDto(String userId);

	public void partnerMoney(int pdIdx);


}
