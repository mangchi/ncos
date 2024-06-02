package mil.ln.ncos.cmmn.vo;

import javax.validation.constraints.Min;

import org.apache.ibatis.session.RowBounds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({ "offset", "limit" })
public class PageInfo extends RowBounds {
    
	private Boolean isFirstPage = false;
	
	private Boolean isFirstPerPage = false;
	
	private Boolean isLastPage = false;
	
	private Boolean isLastPerPage = false;
	
	private Integer pageNo;
	
	private long startRow = -1L;

	@Min(1)
	private Integer rowPerPage;
	
	private Integer totPage;

	private Long totCount = -1L; // set이 되지않았다는 의미로 -1
	
	private String orderBy;

	@Builder
    public PageInfo(int pageNo, int rowPerPage,String orderBy) {
		this.pageNo = pageNo;
		this.rowPerPage = rowPerPage;
		this.orderBy = orderBy;
	}
}
