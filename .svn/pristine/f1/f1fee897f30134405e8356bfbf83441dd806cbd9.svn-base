package mil.ln.ncos.cmmn.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mil.ln.ncos.cmmn.EnumModel;

@RequiredArgsConstructor
@Getter
@JsonFormat(shape = Shape.OBJECT)
public enum ErrorCode implements EnumModel {

	// COMMON
	INVALID_CODE(001, "E001", "Invalid Code"), 
	RESOURCE_NOT_FOUND(002, "E002", "Resource not found"),
	USER_NOT_FOUND(003, "E003", "User not found"), 
	BAD_CRYPTO_FOUND(004, "E004", "Bad Credentials Exception"),
	TEMPORARY_SERVER_ERROR(005, "E005", "temporary server error happend"),
	EXPIRED_CODE(006, "E006", "Expired Code"),
	PWD_QUARTER_CHG(007, "E007", "Password change per quarter"),
	PWD_ERROR_FIFTH_TIMES(Long.parseLong("008"), "E008", "Password error with 5 times"),
	NOT_EXIST_MSG(Long.parseLong("009"), "E009", "Mandatory message is not exist"),
	USER_BlOCK(Long.parseLong("010"), "E010", "user is blocked"),
	USER_DUPLOGIN(Long.parseLong("011"), "E011", "user duplicatied login"),
	USER_NOT_ACTIVATED(Long.parseLong("012"), "E012", "user is not activated"),
	CONF_LOAD_FAIL(Long.parseLong("013"), "E013", "user is not activated"),
	EVENT_HANDLE_TYPE_IS_NULL(Long.parseLong("014"), "E014", "eventHandler type is null"),
	EVENT_HANDLE_TYPE_IS_NOT_VALID(Long.parseLong("015"), "E015", "eventHandler type is not valid"),
	
	
	UNDEFINED_ERROR(Long.parseLong("999"), "E999", "undefined error");

	private long status;
	private String code;
	private String message;
	private String detail;

	ErrorCode(long status, String code, String message) {
		this.status = status;
		this.message = message;
		this.code = code;

	}

	@Override
	public String getKey() {
		return this.code;
	}

	@Override
	public String getValue() {
		return this.message;
	}
}