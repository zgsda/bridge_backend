package bridge.dto;

import lombok.Data;

@Data
public class ComposerRequestTagDto {

	private int crtIdx;
	private String crtTag;
	private String [] crtTags;
	private int crIdx;
}
