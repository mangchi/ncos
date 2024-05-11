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
import mil.ln.ncos.user.vo.UserValidGroups.PwdValid;
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
	
	@NotBlank(groups = {RegValid.class,LoginValid.class},message = "{error.password.notNull}")  
	private String confirm;

	@NotBlank(groups = {RegValid.class,ChgValid.class,ModValid.class},message = "{error.username.notNull}")
    private String username;
	

	@NotBlank(groups = {RegValid.class,ChgValid.class,ModValid.class} ,message = "{error.classId.notNull}")
	private String classId;
	
	@NotBlank(groups = {RegValid.class,ChgValid.class,ModValid.class} ,message = "{error.affiliationId.notNull}")
	private String affiliationId;
	
	@NotBlank(groups = {RegValid.class,ModValid.class} ,message = "{error.authorization.notNull}")
	private String authorization;
	
	
	
	@NotBlank(groups = {RegValid.class,ChgValid.class,ModValid.class} ,message = "{error.phoneNo.notNull}")
	private String phoneNo;
	
	private String email;
	
	private String militaryServiceNumber;

    private String terminalIp;
    
    private String loginUserId;
    
    @NotBlank(groups = {PwdValid.class},message = "{error.password.notNull}")  
    private String curPassword;
    
    @NotBlank(groups = {PwdValid.class},message = "{error.password.notNull}")  
    @Pattern(groups = {PwdValid.class},regexp="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=\\S+$).{9,20}", 
    message = "{error.password.invalid1}")
    private String newPassword;
    
    @NotBlank(groups = {PwdValid.class},message = "{error.password.notNull}")  
	private String newConfirm;

    
    
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
    
    private String salt;


    
}