package mil.ln.ncos.cmmn.vo;

import lombok.Data;

@Data
public class LogVo {
	private String actionId;

    private String accountId;

	private String workUiId;

	private String workCodeId;

	private String workContent;
	
	private String workEncContent;

	private String collectionTime;

	private String integrity;
	
	private String terminalIp;

	private int result;


}
