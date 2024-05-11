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

	public void PrintMsgFormatBitField() {
		//System.out.println("bit 0: " + ((msg_format & 0x0001) != 0));
		//System.out.println("bit 1: " + ((msg_format & 0x0002) != 0));
		//System.out.println("bit 2: " + ((msg_format & 0x0004) != 0));
		//System.out.println("bit 3: " + ((msg_format & 0x0008) != 0));
		//System.out.println("bit 4: " + ((msg_format & 0x0010) != 0));
		//System.out.println("bit 5: " + ((msg_format & 0x0020) != 0));
		//System.out.println("bit 6: " + ((msg_format & 0x0040) != 0));
		//System.out.println("bit 7: " + ((msg_format & 0x0080) != 0));
		//System.out.println("bit 8: " + ((msg_format & 0x0100) != 0));
		//System.out.println("bit 9: " + ((msg_format & 0x0200) != 0));
		//System.out.println("bit 10: " + ((msg_format & 0x0400) != 0));
		//System.out.println("bit 11: " + ((msg_format & 0x0800) != 0));
		//System.out.println("bit 12: " + ((msg_format & 0x1000) != 0));
		//System.out.println("bit 13: " + ((msg_format & 0x2000) != 0));
		//System.out.println("bit 14: " + ((msg_format & 0x4000) != 0));
		//System.out.println("bit 15: " + ((msg_format & 0x8000) != 0));
	}
	/*
	public static void main(String[] args) {

		ClsMsgFormatBitFiled msgformat = new ClsMsgFormatBitFiled();
		msgformat.PrintMsgFormatBitField();
		
		msgformat.setBitFieldValue(1, true);
		
		msgformat.PrintMsgFormatBitField();
		
		msgformat.setBitFieldValue(3, true);
		
		msgformat.PrintMsgFormatBitField();
	}
	*/

}
