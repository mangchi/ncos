package mil.ln.ncos.user.vo;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mil.ln.ncos.user.vo.UserValidGroups.ChgValid;
import mil.ln.ncos.user.vo.UserValidGroups.LoginValid;
import mil.ln.ncos.user.vo.UserValidGroups.ModValid;
import mil.ln.ncos.user.vo.UserValidGroups.RegValid;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserVo  {

	
	private String accountId;
	
	@NotBlank(groups = {RegValid.class,ChgValid.class,ModValid.class,LoginValid.class},message = "{error.userId.notNull}")
    private String userId;
	
	@NotBlank(groups = {RegValid.class,LoginValid.class},message = "{error.password.notNull}")  
	@Pattern(groups = {RegValid.class,ChgValid.class},regexp="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=\\S+$).{9,20}", 
	         message = "{error.password.invalid1}")
	private String password;
	
	@NotBlank(groups = {RegValid.class,ChgValid.class,LoginValid.class},message = "{error.password.notNull}")  
	/*
	 * @Pattern(groups = {RegValid.class,ChgValid.class},regexp=
	 * "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=\\S+$).{9,20}", message =
	 * "{error.password.invalid1}")
	 */
	private String confirm;

	@NotBlank(groups = {RegValid.class,ChgValid.class,ModValid.class},message = "{error.username.notNull}")
    private String username;
	

	@NotBlank(groups = {RegValid.class,ChgValid.class,ModValid.class} ,message = "{error.classId.notNull}")
	private String classId;
	
	@NotBlank(groups = {RegValid.class,ChgValid.class,ModValid.class} ,message = "{error.affiliationId.notNull}")
	private String affiliationId;
	
	@NotBlank(groups = {RegValid.class,ChgValid.class,ModValid.class} ,message = "{error.authorization.notNull}")
	private String authorization;
	
	
	
	@NotBlank(groups = {RegValid.class,ChgValid.class,ModValid.class} ,message = "{error.phoneNo.notNull}")
	private String phoneNo;
	
	//@NotBlank(groups = {RegValid.class,ChgValid.class,ModValid.class} ,message = "{error.email.notNull}")
	private String email;
	
	//@NotBlank(groups = {RegValid.class,ChgValid.class,ModValid.class} ,message = "{error.militaryServiceNumber.notNull}")
	private String militaryServiceNumber;

	/*
	 * @NotNull(groups = {RegValid.class,ChgValid.class,ModValid.class},message =
	 * "{error.authority.notNull}") private Authority authority;
	 */
    
    private String terminalIp;
    
    private String loginUserId;
    
    private String curPassword;
    
    private int accountActivation = 0 ;
    
    private int alarmStatus = 0 ;
    
    private int warningStatus = 0 ;
    
    private int sessionControlStatus = 0 ;

    private int alarmLevel = 0 ;
    
    private int warningLevel = 0 ;
    
    private Date lastSuccessAccessDate;
    
    private int isLogin = 0;
    
    private String sqlId ;
    
    private String systemId ;
    
//    public String getAccountId() {
//		return accountId;
//	}
//
//	public void setAccountId(String accountId) {
//		this.accountId = accountId;
//	}
//
//	public String getUserId() {
//		return userId;
//	}
//
//	public void setUserId(String userId) {
//		this.userId = userId;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	public String getUsername() {
//		return username;
//	}
//
//	public void setUsername(String username) {
//		this.username = username;
//	}
//
//	public String getClassId() {
//		return classId;
//	}
//
//	public void setClassId(String classId) {
//		this.classId = classId;
//	}
//
//	public String getAffliiationId() {
//		return affliiationId;
//	}
//
//	public void setAffliiationId(String affliiationId) {
//		this.affliiationId = affliiationId;
//	}
//
//	public String getPhoneNo() {
//		return phoneNo;
//	}
//
//	public void setPhoneNo(String phoneNo) {
//		this.phoneNo = phoneNo;
//	}
//
//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
//
//	public String getMilitaryServiceNumber() {
//		return militaryServiceNumber;
//	}
//
//	public void setMilitaryServiceNumber(String militaryServiceNumber) {
//		this.militaryServiceNumber = militaryServiceNumber;
//	}
//
//	public String getTerminalIp() {
//		return terminalIp;
//	}
//
//	public void setTerminalIp(String terminalIp) {
//		this.terminalIp = terminalIp;
//	}
//
//	public String getLoginUserId() {
//		return loginUserId;
//	}
//
//	public void setLoginUserId(String loginUserId) {
//		this.loginUserId = loginUserId;
//	}
//
//	public String getCurPassword() {
//		return curPassword;
//	}
//
//	public void setCurPassword(String curPassword) {
//		this.curPassword = curPassword;
//	}
//
// 
    
}