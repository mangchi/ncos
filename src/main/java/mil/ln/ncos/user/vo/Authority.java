package mil.ln.ncos.user.vo;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Authority {
	
    ROLE_ADMIN("체계관리자"),
    ROLE_MANAGER("지역별관리자"), 
    ROLE_USER("사용자");

	//@JsonValue
    private final String desc;
    

    Authority(String desc) { 
    	this.desc = desc; 
    }
	
	@JsonValue
    public String getDesc() {
        return desc;
    }
	
    @JsonCreator()
    static Authority findDes(String desc){
       return Arrays.stream(Authority.values()).filter(authority -> 
       authority.name().equals(desc)).findFirst().get();
    }
    

}