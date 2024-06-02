package mil.ln.ncos.user.vo;

public interface UserValidGroups {
	
	interface RegValid {}  //사용자 등록 검증
	interface ChgValid {}  //사용자 수정 검증
	interface PwdValid {}  //비밀번호변경 검증
	interface ModValid {}  //사용자 수정 검증  패스워드 체크 안함
	interface LoginValid {}  //사용자 로그 검증

}
