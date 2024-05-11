package mil.ln.ncos.cmmn.vo;

public class ClsMsgFormatBitFiled {

	// 모든 bit를 0으로 초기화.
	public short msgFormat = 0x0000;

	// (bitFieldNum) 비트 번호와 (value) 값을 전달 받아 대상 필드 값을 변경한다.
	// 위성통신 메시지 포멧 엑셀 시트 참조.
	// 예) 위성 전송 환경 설정 화면에서 송신 IP 항목이 선택되있으면 setBitFieldValue(1,true);
	// => (엑셀 시트의 msg format 항목의 1번째 비트를 1로 설정함.
	public void setBitFieldValue(int bitFieldNum, boolean value) {
		if (bitFieldNum < 0 || bitFieldNum > 15) {
			throw new IllegalArgumentException("bitFieldNum must be between 0 and 15");
		}
		short mask = (short) (1 << bitFieldNum);
		if (value) {
			msgFormat |= mask;
		} else {
			msgFormat &= ~mask;
		}
	}


}
