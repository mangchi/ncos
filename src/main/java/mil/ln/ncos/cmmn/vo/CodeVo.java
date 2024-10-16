package mil.ln.ncos.cmmn.vo;

import lombok.Builder;
import lombok.Data;

@Data
public class CodeVo {

	private String grpCd;
	
	/*
	 * private String grpCdNm;
	 * 
	 * private String grpCdDesc;
	 */
	
	private String cd;
	
	private String cdNm;

	
	public CodeVo() {

	}
	
	@Builder
    public CodeVo(String grpCd, String cd) {
        this.grpCd = grpCd;
        this.cd = cd;
	}

}
