package mil.ln.ncos.cmmn.util;

import java.nio.ByteBuffer;
import java.util.UUID;

public class Uid {
	private final static int LENGTH_20_LONG_RADIX = 9;
	private final static int LENGTH_10_INT_RADIX = 9;
	
	// 
	public static String makeUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	// 10자리의 UUID 생성
	public static String makeShortUUID() {
		UUID uuid = UUID.randomUUID();
		return parseToShortUUID(uuid.toString());
	}
	
	// 20자리의 UUID 생성
	public static String makeLongUUID() {
		UUID uuid = UUID.randomUUID();
		return parseToLongUUID(uuid.toString());
	}

	public static String parseToIntRadixUUID(String uuid, int radix) {
		int l = ByteBuffer.wrap(uuid.getBytes()).getInt();
		return Integer.toString(l, radix);
	}

	public static String parseToLongRadixUUID(String uuid, int radix) {
		long l = ByteBuffer.wrap(uuid.getBytes()).getLong();
		return Long.toString(l, radix);
	}

	// 파라미터로 받은 값을 10자리의 UUID로 변환
	private static String parseToShortUUID(String uuid) {
		int l = ByteBuffer.wrap(uuid.getBytes()).getInt();
		return Integer.toString(l, LENGTH_10_INT_RADIX);
	}

	// 파라미터로 받은 값을 20자리의 UUID로 변환
	private static String parseToLongUUID(String uuid) {
		long l = ByteBuffer.wrap(uuid.getBytes()).getLong();
		return Long.toString(l, LENGTH_20_LONG_RADIX);
	}
	/*
	public static void main(String args[]) {
		//System.out.println(makeUUID());
		//System.out.println(makeLongUUID());
		//28452848562110773285
		//System.out.println(makeShortUUID());
	}
	*/

}
