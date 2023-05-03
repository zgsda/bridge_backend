package bridge.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import bridge.dto.AnnouncementDto;
import bridge.dto.CommentsDto;
import bridge.dto.MusicDto;
import bridge.dto.ReportDto;
import bridge.dto.UserDto;
import bridge.dto.UserProfileDto;
import bridge.mapper.BridgeMapper;

@Service
public class BridgeServiceImpl implements BridgeService {

	@Autowired
	BridgeMapper bridgeMapper;
	
	@Override
	public void insertMusic(MusicDto musicDto) {
		bridgeMapper.insertMusic(musicDto);
	}

	@Override
	public int insertComments(CommentsDto commentsDto) {
		return bridgeMapper.insertComments(commentsDto);	
		
	}

	@Override
	public List<CommentsDto> selectCommentsList(int cIdx) {
		return bridgeMapper.selectCommentsList(cIdx);
	}

	@Override
	public void updateComments(CommentsDto commentsDto) {
		bridgeMapper.updateComments(commentsDto);
		
	}

	@Override
	public void deleteComments(int ccIdx) {
		bridgeMapper.deleteComments(ccIdx);
		
	}

	@Override
	public String selectMusic(String musicUUID) throws Exception {
		return bridgeMapper.selectMusic(musicUUID);
	}

	@Override
	public int insertReport(ReportDto reportDto) {
		return bridgeMapper.insertReport(reportDto);
	}

	@Override
	public List<AnnouncementDto> announcementList() {
		return bridgeMapper.announcementList();
	}

	@Override
	public AnnouncementDto announcementDetail(int aIdx) {
		return bridgeMapper.announcementDetail(aIdx);
	}

	@Override
	public UserDto chargePoint(String userId) {
		return bridgeMapper.chargePoint(userId);
	}

	@Override
	public List<ReportDto> openReportList() {
		return bridgeMapper.openReportList();
	}

	@Override
	public ReportDto openReportDetail(int reportIdx) {
		return bridgeMapper.openReportDetail(reportIdx);
	}

	@Override
	public void handleReport(UserDto userDto) {
		bridgeMapper.handleReport(userDto);	
	}

	@Override
	public int insertProfile(UserProfileDto userProfileDto, MultipartFile[] files) {
		return bridgeMapper.insertProfile(userProfileDto);
	}

//	@Override
//	public int doCharge(UserDto userDto) {
//		return bridgeMapper.doCharge(userDto);
//	}

}