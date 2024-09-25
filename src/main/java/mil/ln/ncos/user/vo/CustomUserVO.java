package mil.gdl.nis.user.vo;

import java.sql.Timestamp;
import java.util.Collection;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mil.gdl.nis.user.vo.UserValidGroups.ChgValid;
import mil.gdl.nis.user.vo.UserValidGroups.ModValid;
import mil.gdl.nis.user.vo.UserValidGroups.PwdValid;
import mil.gdl.nis.user.vo.UserValidGroups.RegValid;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CustomUserVO implements UserDetails {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//private final MessageSourceAccessor messageSource; 
	
	private String id;
	
	@NotBlank(groups = {RegValid.class,ChgValid.class,ModValid.class},message = "{error.userId.notNull}")
    private String userId;
	
	@NotBlank(groups = {RegValid.class,ChgValid.class},message = "{error.userPw.notNull}")  
	@Pattern(groups = {RegValid.class,ChgValid.class},regexp="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=\\S+$).{9,20}", 
	         message = "{error.userPw.invalid1}")
	private String userPw;

	@NotBlank(groups = {RegValid.class,ChgValid.class,ModValid.class},message = "{error.userNm.notNull}")
    private String userNm;
	

	@NotBlank(groups = {RegValid.class,ChgValid.class,ModValid.class} ,message = "{error.deptNm.notNull}")
	private String deptNm;
	
	@NotBlank(groups = {RegValid.class,ChgValid.class,ModValid.class} ,message = "{error.specNm.notNull}")
	private String specNm;
	
	
	@NotBlank(groups = {RegValid.class,ChgValid.class,ModValid.class} ,message = "{error.unitNm.notNull}")
	private String unitId;
	
	@NotBlank(groups = {RegValid.class,ChgValid.class,ModValid.class} ,message = "{error.classCd.notNull}")
	private String rankCd;

	
    @NotNull(groups = {RegValid.class,ChgValid.class,ModValid.class},message = "{error.authority.notNull}")
    private Authority authority;
    
    private String unitNm;
    private String ipAddr;
    private Timestamp createTs;
    private Timestamp updateTS;
    private Timestamp pwRegTs;
    private Timestamp lastVisitedTs;
    private Timestamp blockTs;
    private String lastConnectedIp;
    
    private String pwdInitYn;
    
    private String readonly;
    
    private String hiddenPwd;

   // private boolean enabled;
    private boolean accountExpired = false ;
    //private boolean blockYn = false ;
    private Collection <? extends GrantedAuthority> authorities;
    
    
    @NotBlank(groups = PwdValid.class,message = "{error.oldPw.notNull}")
	private String oldPw;
	
	@NotBlank(groups = PwdValid.class,message = "{error.newPw.notNull}")
	@Pattern(groups = PwdValid.class,regexp="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=\\S+$).{9,20}", 
	         message = "{error.userPw.invalid1}")
	private String newPw;
	
	@NotBlank(groups = PwdValid.class,message = "{error.newPwConfirm.notNull}")
	private String newPwConfirm;
    

    

    @Builder
    public UserVo(String userId, String userPw,String userNm,Authority authority) {
        this.userId = userId;
        this.userPw = userPw;
        this.userNm = userNm;
        this.authority = authority;
    }

	@Override
	public Collection <? extends GrantedAuthority>  getAuthorities() {
		return this.authorities;
	}

	/*
	 * @Override public Collection<? extends GrantedAuthority> getAuthorities() {
	 * return Collections.singletonList(new SimpleGrantedAuthority(this.userAuth));
	 * }
	 */

    @Override
    public String getPassword() {
        return this.userPw;
    }

    // 시큐리티의 userName
    // -> 따라서 얘는 인증할 때 id를 봄
    @Override
    public String getUsername() {
        return this.userId;
    }

    // Vo의 userName !
	/*
	 * public String getUserNm(){ return this.userNm; }
	 */

    @Override
    public boolean isAccountNonExpired() {
    	//계정 만료
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
    	//계정 잠금
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
    	//비밀번호 만료 관리
        return true;
    }

    @Override
    public boolean isEnabled() {
    	//비활성화 계정 처리
        return true;
    }

	@Override
	public int hashCode() {
		return this.getUsername().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof UserVo) {
			return this.getUsername().equals(((UserVo)obj).getUsername());
		}
		return false;
	}
    
    
}
