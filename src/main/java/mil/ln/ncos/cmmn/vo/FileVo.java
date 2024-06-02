package mil.ln.ncos.cmmn.vo;

import lombok.Builder;
import lombok.Data;

@Data
public class FileVo {

	private String name;
	private String url;
	
	@Builder
    public FileVo(String name, String url) {
        this.name = name;
        this.url = url;
	}

}
