package bridge.dto;

import lombok.Data;

@Data
public class PartnerContentDto {
	
	private int pcIdx;
	private String pcContent;
	private String pcWriter;
	private int pdIdx;
	private String pcDate;
	private String pcFile;
	
	private String profileImg;
	
	private String cmMusic;
}
