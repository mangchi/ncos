package mil.ln.ncos.cmmn.util;


import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtil {



	/**
	 * 빈 문자열 <code>""</code>.
	 */
	public static final String EMPTY = "";
	
	private static final SecureRandom rnd = new SecureRandom();
	private static final Random rand = new Random();


	/**
	 * <p>Padding을 할 수 있는 최대 수치</p>
	 */
	// private static final int PAD_LIMIT = 8192;
	/**
	 * <p>An array of <code>String</code>s used for padding.</p>
	 * <p>Used for efficient space padding. The length of each String expands as needed.</p>
	 */
	/*
	private static final String[] PADDING = new String[Character.MAX_VALUE];

	static {
		// space padding is most common, start with 64 chars
		PADDING[32] = "                                                                ";
	}
	 */

	/**
	 * 문자열이 지정한 길이를 초과했을때 지정한길이에다가 해당 문자열을 붙여주는 메서드.
	 * @param source 원본 문자열 배열
	 * @param output 더할문자열
	 * @param slength 지정길이
	 * @return 지정길이로 잘라서 더할분자열 합친 문자열
	 */
	public static String cutString(String source, String output, int slength) {
		String returnVal = null;
		if (source != null) {
			if (source.length() > slength) {
				returnVal = source.substring(0, slength) + output;
			} else
				returnVal = source;
		}
		return returnVal;
	}

	/**
	 * 문자열이 지정한 길이를 초과했을때 해당 문자열을 삭제하는 메서드
	 * @param source 원본 문자열 배열
	 * @param slength 지정길이
	 * @return 지정길이로 잘라서 더할분자열 합친 문자열
	 */
	public static String cutString(String source, int slength) {
		String result = null;
		if (source != null) {
			if (source.length() > slength) {
				result = source.substring(0, slength);
			} else
				result = source;
		}
		return result;
	}

	/**
	 * <p>
	 * String이 비었거나("") 혹은 null 인지 검증한다.
	 * </p>
	 *
	 * <pre>
	 *  StringUtil.isEmpty(null)      = true
	 *  StringUtil.isEmpty("")        = true
	 *  StringUtil.isEmpty(" ")       = false
	 *  StringUtil.isEmpty("bob")     = false
	 *  StringUtil.isEmpty("  bob  ") = false
	 * </pre>
	 *
	 * @param str - 체크 대상 스트링오브젝트이며 null을 허용함
	 * @return <code>true</code> - 입력받은 String 이 빈 문자열 또는 null인 경우
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * <p>기준 문자열에 포함된 모든 대상 문자(char)를 제거한다.</p>
	 *
	 * <pre>
	 * StringUtil.remove(null, *)       = null
	 * StringUtil.remove("", *)         = ""
	 * StringUtil.remove("queued", 'u') = "qeed"
	 * StringUtil.remove("queued", 'z') = "queued"
	 * </pre>
	 *
	 * @param str  입력받는 기준 문자열
	 * @param remove  입력받는 문자열에서 제거할 대상 문자열
	 * @return 제거대상 문자열이 제거된 입력문자열. 입력문자열이 null인 경우 출력문자열은 null
	 */
	public static String remove(String str, char remove) {
		if (isEmpty(str) || str.indexOf(remove) == -1) {
			return str;
		}
		char[] chars = str.toCharArray();
		int pos = 0;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] != remove) {
				chars[pos++] = chars[i];
			}
		}
		return new String(chars, 0, pos);
	}

	/**
	 * <p>문자열 내부의 콤마 character(,)를 모두 제거한다.</p>
	 *
	 * <pre>
	 * StringUtil.removeCommaChar(null)       = null
	 * StringUtil.removeCommaChar("")         = ""
	 * StringUtil.removeCommaChar("asdfg,qweqe") = "asdfgqweqe"
	 * </pre>
	 *
	 * @param str 입력받는 기준 문자열
	 * @return " , "가 제거된 입력문자열
	 *  입력문자열이 null인 경우 출력문자열은 null
	 */
	public static String removeCommaChar(String str) {
		return remove(str, ',');
	}

	/**
	 * <p>문자열 내부의 마이너스 character(-)를 모두 제거한다.</p>
	 *
	 * <pre>
	 * StringUtil.removeMinusChar(null)       = null
	 * StringUtil.removeMinusChar("")         = ""
	 * StringUtil.removeMinusChar("a-sdfg-qweqe") = "asdfgqweqe"
	 * </pre>
	 *
	 * @param str  입력받는 기준 문자열
	 * @return " - "가 제거된 입력문자열
	 *  입력문자열이 null인 경우 출력문자열은 null
	 */
	public static String removeMinusChar(String str) {
		return remove(str, '-');
	}

	/**
	 * 원본 문자열의 포함된 특정 문자열을 새로운 문자열로 변환하는 메서드
	 * @param source 원본 문자열
	 * @param subject 원본 문자열에 포함된 특정 문자열
	 * @param object 변환할 문자열
	 * @return sb.toString() 새로운 문자열로 변환된 문자열
	 */
	public static String replace(String source, String subject, String object) {
		StringBuffer rtnStr = new StringBuffer();
		String preStr = "";
		String nextStr = source;
		String srcStr = source;

		while (srcStr.indexOf(subject) >= 0) {
			preStr = srcStr.substring(0, srcStr.indexOf(subject));
			nextStr = srcStr.substring(srcStr.indexOf(subject) + subject.length(), srcStr.length());
			srcStr = nextStr;
			rtnStr.append(preStr).append(object);
		}
		rtnStr.append(nextStr);
		return rtnStr.toString();
	}

	/**
	 * 원본 문자열의 포함된 특정 문자열 첫번째 한개만 새로운 문자열로 변환하는 메서드
	 * @param source 원본 문자열
	 * @param subject 원본 문자열에 포함된 특정 문자열
	 * @param object 변환할 문자열
	 * @return sb.toString() 새로운 문자열로 변환된 문자열 / source 특정문자열이 없는 경우 원본 문자열
	 */
	public static String replaceOnce(String source, String subject, String object) {
		StringBuffer rtnStr = new StringBuffer();
		String preStr = "";
		String nextStr = source;
		if (source.indexOf(subject) >= 0) {
			preStr = source.substring(0, source.indexOf(subject));
			nextStr = source.substring(source.indexOf(subject) + subject.length(), source.length());
			rtnStr.append(preStr).append(object).append(nextStr);
			return rtnStr.toString();
		} else {
			return source;
		}
	}

	/**
	 * <code>subject</code>에 포함된 각각의 문자를 object로 변환한다.
	 *
	 * @param source 원본 문자열
	 * @param subject 원본 문자열에 포함된 특정 문자열
	 * @param object 변환할 문자열
	 * @return sb.toString() 새로운 문자열로 변환된 문자열
	 */
	public static String replaceChar(String source, String subject, String object) {
		StringBuffer rtnStr = new StringBuffer();
		String preStr = "";
		String nextStr = source;
		String srcStr = source;

		char chA;

		for (int i = 0; i < subject.length(); i++) {
			chA = subject.charAt(i);

			if (srcStr.indexOf(chA) >= 0) {
				preStr = srcStr.substring(0, srcStr.indexOf(chA));
				nextStr = srcStr.substring(srcStr.indexOf(chA) + 1, srcStr.length());
				srcStr = rtnStr.append(preStr).append(object).append(nextStr).toString();
			}
		}

		return srcStr;
	}

	/**
	 * <p><code>str</code> 중 <code>searchStr</code>의 시작(index) 위치를 반환.</p>
	 *
	 * <p>입력값 중 <code>null</code>이 있을 경우 <code>-1</code>을 반환.</p>
	 *
	 * <pre>
	 * StringUtil.indexOf(null, *)          = -1
	 * StringUtil.indexOf(*, null)          = -1
	 * StringUtil.indexOf("", "")           = 0
	 * StringUtil.indexOf("aabaabaa", "a")  = 0
	 * StringUtil.indexOf("aabaabaa", "b")  = 2
	 * StringUtil.indexOf("aabaabaa", "ab") = 1
	 * StringUtil.indexOf("aabaabaa", "")   = 0
	 * </pre>
	 *
	 * @param str  검색 문자열
	 * @param searchStr  검색 대상문자열
	 * @return 검색 문자열 중 검색 대상문자열이 있는 시작 위치 검색대상 문자열이 없거나 null인 경우 -1
	 */
	public static int indexOf(String str, String searchStr) {
		if (str == null || searchStr == null) {
			return -1;
		}
		return str.indexOf(searchStr);
	}

	/**
	 * <p>오라클의 decode 함수와 동일한 기능을 가진 메서드이다.
	 * <code>sourStr</code>과 <code>compareStr</code>의 값이 같으면
	 * <code>returStr</code>을 반환하며, 다르면  <code>defaultStr</code>을 반환한다.
	 * </p>
	 *
	 * <pre>
	 * StringUtil.decode(null, null, "foo", "bar")= "foo"
	 * StringUtil.decode("", null, "foo", "bar") = "bar"
	 * StringUtil.decode(null, "", "foo", "bar") = "bar"
	 * StringUtil.decode("하이", "하이", null, "bar") = null
	 * StringUtil.decode("하이", "하이  ", "foo", null) = null
	 * StringUtil.decode("하이", "하이", "foo", "bar") = "foo"
	 * StringUtil.decode("하이", "하이  ", "foo", "bar") = "bar"
	 * </pre>
	 *
	 * @param sourceStr 비교할 문자열
	 * @param compareStr 비교 대상 문자열
	 * @param returnStr sourceStr와 compareStr의 값이 같을 때 반환할 문자열
	 * @param defaultStr sourceStr와 compareStr의 값이 다를 때 반환할 문자열
	 * @return sourceStr과 compareStr의 값이 동일(equal)할 때 returnStr을 반환하며,
	 *         <br/>다르면 defaultStr을 반환한다.
	 */
	public static String decode(String sourceStr, String compareStr, String returnStr, String defaultStr) {
		if (sourceStr == null && compareStr == null) {
			return returnStr;
		}

		if (sourceStr == null && compareStr != null) {
			return defaultStr;
		}

		if (sourceStr != null && sourceStr.trim().equals(compareStr)) {
			return returnStr;
		}

		return defaultStr;
	}

	/**
	 * <p>오라클의 decode 함수와 동일한 기능을 가진 메서드이다.
	 * <code>sourStr</code>과 <code>compareStr</code>의 값이 같으면
	 * <code>returStr</code>을 반환하며, 다르면  <code>sourceStr</code>을 반환한다.
	 * </p>
	 *
	 * <pre>
	 * StringUtil.decode(null, null, "foo") = "foo"
	 * StringUtil.decode("", null, "foo") = ""
	 * StringUtil.decode(null, "", "foo") = null
	 * StringUtil.decode("하이", "하이", "foo") = "foo"
	 * StringUtil.decode("하이", "하이 ", "foo") = "하이"
	 * StringUtil.decode("하이", "바이", "foo") = "하이"
	 * </pre>
	 *
	 * @param sourceStr 비교할 문자열
	 * @param compareStr 비교 대상 문자열
	 * @param returnStr sourceStr와 compareStr의 값이 같을 때 반환할 문자열
	 * @return sourceStr과 compareStr의 값이 동일(equal)할 때 returnStr을 반환하며,
	 *         <br/>다르면 sourceStr을 반환한다.
	 */
	public static String decode(String sourceStr, String compareStr, String returnStr) {
		return decode(sourceStr, compareStr, returnStr, sourceStr);
	}

	/**
	 * 객체가 null인지 확인하고 null인 경우 "" 로 바꾸는 메서드
	 * @param object 원본 객체
	 * @return resultVal 문자열
	 */
	public static String isNullToString(Object object) {
		String string = "";

		if (object != null) {
			string = object.toString().trim();
		}

		return string;
	}

	/**
	 *<pre>
	 * 인자로 받은 String이 null일 경우 &quot;&quot;로 리턴한다.
	 * &#064;param src null값일 가능성이 있는 String 값.
	 * &#064;return 만약 String이 null 값일 경우 &quot;&quot;로 바꾼 String 값.
	 *</pre>
	 */
	public static String nullConvert(Object src) {
		//if (src != null && src.getClass().getName().equals("java.math.BigDecimal")) {
		if (src != null && src instanceof java.math.BigDecimal) {
			return ((BigDecimal) src).toString();
		}

		if (src == null || src.equals("null")) {
			return "";
		} else {
			return ((String) src).trim();
		}
	}

	/**
	 *<pre>
	 * 인자로 받은 String이 null일 경우 &quot;&quot;로 리턴한다.
	 * &#064;param src null값일 가능성이 있는 String 값.
	 * &#064;return 만약 String이 null 값일 경우 &quot;&quot;로 바꾼 String 값.
	 *</pre>
	 */
	public static String nullConvert(String src) {

		if (src == null || src.equals("null") || "".equals(src) || " ".equals(src)) {
			return "";
		} else {
			return src.trim();
		}
	}

	/**
	 *<pre>
	 * 인자로 받은 String이 null일 경우 &quot;0&quot;로 리턴한다.
	 * &#064;param src null값일 가능성이 있는 String 값.
	 * &#064;return 만약 String이 null 값일 경우 &quot;0&quot;로 바꾼 String 값.
	 *</pre>
	 */
	public static int zeroConvert(Object src) {

		if (src == null || src.equals("null")) {
			return 0;
		} else {
			return Integer.parseInt(((String) src).trim());
		}
	}

	/**
	 *<pre>
	 * 인자로 받은 String이 null일 경우 &quot;&quot;로 리턴한다.
	 * &#064;param src null값일 가능성이 있는 String 값.
	 * &#064;return 만약 String이 null 값일 경우 &quot;&quot;로 바꾼 String 값.
	 *</pre>
	 */
	public static int zeroConvert(String src) {

		if (src == null || src.equals("null") || "".equals(src) || " ".equals(src)) {
			return 0;
		} else {
			return Integer.parseInt(src.trim());
		}
	}

	/**
	 * <p>문자열에서 {@link Character#isWhitespace(char)}에 정의된
	 * 모든 공백문자를 제거한다.</p>
	 *
	 * <pre>
	 * StringUtil.removeWhitespace(null)         = null
	 * StringUtil.removeWhitespace("")           = ""
	 * StringUtil.removeWhitespace("abc")        = "abc"
	 * StringUtil.removeWhitespace("   ab  c  ") = "abc"
	 * </pre>
	 *
	 * @param str  공백문자가 제거도어야 할 문자열
	 * @return the 공백문자가 제거된 문자열, null이 입력되면 <code>null</code>이 리턴
	 */
	public static String removeWhitespace(String str) {
		if (isEmpty(str)) {
			return str;
		}
		int sz = str.length();
		char[] chs = new char[sz];
		int count = 0;
		for (int i = 0; i < sz; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				chs[count++] = str.charAt(i);
			}
		}
		if (count == sz) {
			return str;
		}

		return new String(chs, 0, count);
	}

	/**
	 * Html 코드가 들어간 문서를 표시할때 태그에 손상없이 보이기 위한 메서드
	 *
	 * @param strString
	 * @return HTML 태그를 치환한 문자열
	 */
	public static String checkHtmlView(String strString) {
		String strNew = "";

		StringBuffer strTxt = new StringBuffer("");

		char chrBuff;
		int len = strString.length();

		for (int i = 0; i < len; i++) {
			chrBuff = strString.charAt(i);

			switch (chrBuff) {
				case '<':
					strTxt.append("&lt;");
					break;
				case '>':
					strTxt.append("&gt;");
					break;
				case '"':
					strTxt.append("&quot;");
					break;
				case 10:
					strTxt.append("<br>");
					break;
				case ' ':
					strTxt.append("&nbsp;");
					break;
				//case '&' :
				//strTxt.append("&amp;");
				//break;
				default:
					strTxt.append(chrBuff);
			}
		}

		strNew = strTxt.toString();

		return strNew;
	}

	/**
	 * 문자열을 지정한 분리자에 의해 배열로 리턴하는 메서드.
	 * @param source 원본 문자열
	 * @param separator 분리자
	 * @return result 분리자로 나뉘어진 문자열 배열
	 */
	public static String[] split(String source, String separator) throws NullPointerException {
		String[] returnVal = null;
		int cnt = 1;

		int index = source.indexOf(separator);
		int index0 = 0;
		while (index >= 0) {
			cnt++;
			index = source.indexOf(separator, index + 1);
		}
		returnVal = new String[cnt];
		cnt = 0;
		index = source.indexOf(separator);
		while (index >= 0) {
			returnVal[cnt] = source.substring(index0, index);
			index0 = index + 1;
			index = source.indexOf(separator, index + 1);
			cnt++;
		}
		returnVal[cnt] = source.substring(index0);

		return returnVal;
	}

	/**
	 * <p>{@link String#toLowerCase()}를 이용하여 소문자로 변환한다.</p>
	 *
	 * <pre>
	 * StringUtil.lowerCase(null)  = null
	 * StringUtil.lowerCase("")    = ""
	 * StringUtil.lowerCase("aBc") = "abc"
	 * </pre>
	 *
	 * @param str 소문자로 변환되어야 할 문자열
	 * @return 소문자로 변환된 문자열, null이 입력되면 <code>null</code> 리턴
	 */
	public static String lowerCase(String str) {
		if (str == null) {
			return null;
		}

		return str.toLowerCase();
	}

	/**
	 * <p>{@link String#toUpperCase()}를 이용하여 대문자로 변환한다.</p>
	 *
	 * <pre>
	 * StringUtil.upperCase(null)  = null
	 * StringUtil.upperCase("")    = ""
	 * StringUtil.upperCase("aBc") = "ABC"
	 * </pre>
	 *
	 * @param str 대문자로 변환되어야 할 문자열
	 * @return 대문자로 변환된 문자열, null이 입력되면 <code>null</code> 리턴
	 */
	public static String upperCase(String str) {
		if (str == null) {
			return null;
		}

		return str.toUpperCase();
	}

	/**
	 * <p>입력된 String의 앞쪽에서 두번째 인자로 전달된 문자(stripChars)를 모두 제거한다.</p>
	 *
	 * <pre>
	 * StringUtil.stripStart(null, *)          = null
	 * StringUtil.stripStart("", *)            = ""
	 * StringUtil.stripStart("abc", "")        = "abc"
	 * StringUtil.stripStart("abc", null)      = "abc"
	 * StringUtil.stripStart("  abc", null)    = "abc"
	 * StringUtil.stripStart("abc  ", null)    = "abc  "
	 * StringUtil.stripStart(" abc ", null)    = "abc "
	 * StringUtil.stripStart("yxabc  ", "xyz") = "abc  "
	 * </pre>
	 *
	 * @param str 지정된 문자가 제거되어야 할 문자열
	 * @param stripChars 제거대상 문자열
	 * @return 지정된 문자가 제거된 문자열, null이 입력되면 <code>null</code> 리턴
	 */
	public static String stripStart(String str, String stripChars) {
		int strLen;
		if (str == null ) {
			return str;
		}
		strLen = str.length();
		if (strLen  == 0) {
			return str;
		}
		int start = 0;
		if (stripChars == null) {
			while ((start != strLen) && Character.isWhitespace(str.charAt(start))) {
				start++;
			}
		} else if (stripChars.length() == 0) {
			return str;
		} else {
			while ((start != strLen) && (stripChars.indexOf(str.charAt(start)) != -1)) {
				start++;
			}
		}

		return str.substring(start);
	}

	/**
	 * <p>입력된 String의 뒤쪽에서 두번째 인자로 전달된 문자(stripChars)를 모두 제거한다.</p>
	 *
	 * <pre>
	 * StringUtil.stripEnd(null, *)          = null
	 * StringUtil.stripEnd("", *)            = ""
	 * StringUtil.stripEnd("abc", "")        = "abc"
	 * StringUtil.stripEnd("abc", null)      = "abc"
	 * StringUtil.stripEnd("  abc", null)    = "  abc"
	 * StringUtil.stripEnd("abc  ", null)    = "abc"
	 * StringUtil.stripEnd(" abc ", null)    = " abc"
	 * StringUtil.stripEnd("  abcyx", "xyz") = "  abc"
	 * </pre>
	 *
	 * @param str 지정된 문자가 제거되어야 할 문자열
	 * @param stripChars 제거대상 문자열
	 * @return 지정된 문자가 제거된 문자열, null이 입력되면 <code>null</code> 리턴
	 */
	public static String stripEnd(String str, String stripChars) {
		int end;
		if (str == null ) {
			return str;
		}
		end = str.length();
		if (end == 0) {
			return str;
		}

		if (stripChars == null) {
			while ((end != 0) && Character.isWhitespace(str.charAt(end - 1))) {
				end--;
			}
		} else if (stripChars.length() == 0) {
			return str;
		} else {
			while ((end != 0) && (stripChars.indexOf(str.charAt(end - 1)) != -1)) {
				end--;
			}
		}

		return str.substring(0, end);
	}

	/**
	 * <p>입력된 String의 앞, 뒤에서 두번째 인자로 전달된 문자(stripChars)를 모두 제거한다.</p>
	 *
	 * <pre>
	 * StringUtil.strip(null, *)          = null
	 * StringUtil.strip("", *)            = ""
	 * StringUtil.strip("abc", null)      = "abc"
	 * StringUtil.strip("  abc", null)    = "abc"
	 * StringUtil.strip("abc  ", null)    = "abc"
	 * StringUtil.strip(" abc ", null)    = "abc"
	 * StringUtil.strip("  abcyx", "xyz") = "  abc"
	 * </pre>
	 *
	 * @param str 지정된 문자가 제거되어야 할 문자열
	 * @param stripChars 제거대상 문자열
	 * @return 지정된 문자가 제거된 문자열, null이 입력되면 <code>null</code> 리턴
	 */
	public static String strip(String str, String stripChars) {
		if (isEmpty(str)) {
			return str;
		}

		String srcStr = str;
		srcStr = stripStart(srcStr, stripChars);

		return stripEnd(srcStr, stripChars);
	}

	/**
	 * 문자열을 지정한 분리자에 의해 지정된 길이의 배열로 리턴하는 메서드.
	 * @param source 원본 문자열
	 * @param separator 분리자
	 * @param arraylength 배열 길이
	 * @return 분리자로 나뉘어진 문자열 배열
	 */
	public static String[] split(String source, String separator, int arraylength) throws NullPointerException {
		String[] returnVal = new String[arraylength];
		int cnt = 0;
		int index0 = 0;
		int index = source.indexOf(separator);
		while (index >= 0 && cnt < (arraylength - 1)) {
			returnVal[cnt] = source.substring(index0, index);
			index0 = index + 1;
			index = source.indexOf(separator, index + 1);
			cnt++;
		}
		returnVal[cnt] = source.substring(index0);
		if (cnt < (arraylength - 1)) {
			for (int i = cnt + 1; i < arraylength; i++) {
				returnVal[i] = "";
			}
		}

		return returnVal;
	}

	/**
	 * 문자열 A에서 Z사이의 랜덤 문자열을 구하는 기능을 제공 시작문자열과 종료문자열 사이의 랜덤 문자열을 구하는 기능
	 *
	 * @param startChr
	 *            - 첫 문자
	 * @param endChr
	 *            - 마지막문자
	 * @return 랜덤문자
	 * @exception MyException
	 * @see
	 */
	public static String getRandomStr(char startChr, char endChr) {

		int randomInt;
		String randomStr = null;

		// 시작문자 및 종료문자를 아스키숫자로 변환한다.
		int startInt = Integer.valueOf(startChr);
		int endInt = Integer.valueOf(endChr);

		// 시작문자열이 종료문자열보가 클경우
		if (startInt > endInt) {
			throw new IllegalArgumentException("Start String: " + startChr + " End String: " + endChr);
		}

		// 랜덤 객체 생성
		
		do {
			// 시작문자 및 종료문자 중에서 랜덤 숫자를 발생시킨다.
			randomInt = rnd.nextInt(endInt + 1);
		} while (randomInt < startInt); // 입력받은 문자 'A'(65)보다 작으면 다시 랜덤 숫자 발생.

		// 랜덤 숫자를 문자로 변환 후 스트링으로 다시 변환
		randomStr = (char) randomInt + "";

		// 랜덤문자열를 리턴
		return randomStr;
	}

	/**
	 * 문자열을 다양한 문자셋(EUC-KR[KSC5601],UTF-8..)을 사용하여 인코딩하는 기능 역으로 디코딩하여 원래의 문자열을
	 * 복원하는 기능을 제공함 String temp = new String(문자열.getBytes("바꾸기전 인코딩"),"바꿀 인코딩");
	 * String temp = new String(문자열.getBytes("8859_1"),"KSC5601"); => UTF-8 에서
	 * EUC-KR
	 *
	 * @param srcString
	 *            - 문자열
	 * @param srcCharsetNm
	 *            - 원래 CharsetNm
	 * @param charsetNm
	 *            - CharsetNm
	 * @return 인(디)코딩 문자열
	 * @exception MyException
	 * @see
	 */
	public static String getEncdDcd(String srcString, String srcCharsetNm, String cnvrCharsetNm) {

		String rtnStr = null;

		if (srcString == null)
			return null;

		try {
			rtnStr = new String(srcString.getBytes(srcCharsetNm), cnvrCharsetNm);
		} catch (UnsupportedEncodingException e) {
			rtnStr = null;
		}

		return rtnStr;
	}

	/**
	     * 특수문자를 웹 브라우저에서 정상적으로 보이기 위해 특수문자를 처리('<' -> & lT)하는 기능이다
	     * @param 	srcString 		- '<'
	     * @return 	변환문자열('<' -> "&lt"
	     * @exception MyException
	     * @see
	     */
	public static String getSpclStrCnvr(String srcString) {

		String rtnStr = null;

		try {
			StringBuffer strTxt = new StringBuffer("");

			char chrBuff;
			int len = srcString.length();

			for (int i = 0; i < len; i++) {
				chrBuff = srcString.charAt(i);

				switch (chrBuff) {
					case '<':
						strTxt.append("&lt;");
						break;
					case '>':
						strTxt.append("&gt;");
						break;
					case '&':
						strTxt.append("&amp;");
						break;
					default:
						strTxt.append(chrBuff);
				}
			}

			rtnStr = strTxt.toString();

		}
		catch (IndexOutOfBoundsException ne) {
			log.error("IndexOutOfBoundsException:{}", ne.getMessage());
		}

		return rtnStr;
	}

	/**
	 * 응용어플리케이션에서 고유값을 사용하기 위해 시스템에서17자리의TIMESTAMP값을 구하는 기능
	 *
	 * @param
	 * @return Timestamp 값
	 * @exception MyException
	 * @see
	 */
	public static String getTimeStamp() {

		String rtnStr = null;

		// 문자열로 변환하기 위한 패턴 설정(년도-월-일 시:분:초:초(자정이후 초))
		String pattern = "yyyyMMddhhmmssSSS";

		SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.KOREA);
		Timestamp ts = new Timestamp(System.currentTimeMillis());

		rtnStr = sdfCurrent.format(ts.getTime());

		return rtnStr;
	}

	/**
	 * html의 특수문자를 표현하기 위해
	 *
	 * @param srcString
	 * @return String
	 * @exception Exception
	 * @see
	 */
	public static String getHtmlStrCnvr(String srcString) {

		String tmpString = srcString;

		tmpString = tmpString.replaceAll("&lt;", "<");
		tmpString = tmpString.replaceAll("&gt;", ">");
		tmpString = tmpString.replaceAll("&amp;", "&");
		tmpString = tmpString.replaceAll("&nbsp;", " ");
		tmpString = tmpString.replaceAll("&apos;", "\'");
		tmpString = tmpString.replaceAll("&quot;", "\"");

		return tmpString;

	}

	/**
	 * <p>날짜 형식의 문자열 내부에 마이너스 character(-)를 추가한다.</p>
	 *
	 * <pre>
	 *   StringUtil.addMinusChar("20100901") = "2010-09-01"
	 * </pre>
	 *
	 * @param date  입력받는 문자열
	 * @return " - "가 추가된 입력문자열
	 */
	public static String addMinusChar(String date) {
		if (date.length() == 8)
			return date.substring(0, 4).concat("-").concat(date.substring(4, 6)).concat("-").concat(date.substring(6, 8));
		else
			return "";
	}

	/**
	 * 문자열 왼쪽으로 빈값(스페이스)채워 요청 길이 만큼의 문자열을 리턴한다.
	 *
	 * @param String srcStr 대상 문자열
	 * @param int padLen 요청 문자열 길이
	 * @return String padResult 결과 문자열
	 * @exception Exception
	 */
	public static String lpad(String srcStr, int padLen) {
		return lpad(srcStr, padLen, " ");
	}

	/**
	 * 문자열 왼쪽으로 요청 문자로 채워 요청 길이 만큼의 문자열을 리턴한다.
	 *
	 * @param String srcStr 대상 문자열
	 * @param int padLen 요청 문자열 길이
	 * @param String trgtStr 요청 문자
	 * @return String padResult 결과 문자열
	 * @exception Exception
	 */
	public static String lpad(String srcStr, int padLen, String trgtStr) {
		String padResult = "";
		boolean endChk = false;

		if (srcStr != null ) {
			if (trgtStr == null) trgtStr = " ";

			if (srcStr.length() >= padLen) {
				padResult = srcStr.substring(0, padLen);
			} else {
				for (int i = 0; i < padLen - srcStr.length(); i++) {
					if (!endChk) {
						padResult += trgtStr;
						if (padResult.length() + srcStr.length() >= padLen) endChk = true;
					}
				}

				padResult = padResult.substring(0, padLen - srcStr.length()) + srcStr;
			}
		} else {
			padResult = srcStr;
		}

		return padResult;
	}

	/**
	 * 문자열 오른쪽으로 빈값(스페이스)채워 요청 길이 만큼의 문자열을 리턴한다.
	 *
	 * @param String srcStr 대상 문자열
	 * @param int padLen 요청 문자열 길이
	 * @return String padResult 결과 문자열
	 * @exception Exception
	 */
	public static String rpad(String srcStr, int padLen) {
		return rpad(srcStr, padLen, " ");
	}

	/**
	 * 문자열 오른쪽으로 요청 문자로 채워 요청 길이 만큼의 문자열을 리턴한다.
	 *
	 * @param String srcStr 대상 문자열
	 * @param int padLen 요청 문자열 길이
	 * @param String trgtStr 요청 문자
	 * @return String padResult 결과 문자열
	 * @exception Exception
	 */
	public static String rpad(String srcStr, int padLen, String trgtStr) {
		String padResult = "";
		boolean endChk = false;

		if (srcStr != null ) {
			if (trgtStr == null) trgtStr = " ";

			if (srcStr.length() >= padLen) {
				padResult = srcStr.substring(0, padLen);
			} else {
				for (int i = 0; i < padLen - srcStr.length(); i++) {
					if (!endChk) {
						padResult += trgtStr;
						if (padResult.length() + srcStr.length() >= padLen) endChk = true;
					}
				}

				padResult = srcStr + padResult.substring(0, padLen - srcStr.length());
			}
		} else {
			padResult = srcStr;
		}

		return padResult;
	}

	/**
	 * 정수형 문자열 세자리 마다 콤마를 찍어 String으로 리턴한다.
	 *
	 * @param String str 정수형 문자열
	 * @return String 콤마가 추가된 문자열
	 * @exception Exception
	 */
	public static String stringToNumFormat(String str) {
		if(isNullToString(str).equals("")) {
			str = "0";
		}

		int inValues = Integer.parseInt(str);
		DecimalFormat commas = new DecimalFormat("#,###");
		String resultInt = commas.format(inValues);
		return resultInt;
	}

	/**
	 * 숫자 세자리 마다 콤마를 찍어 String으로 리턴한다.
	 *
	 * @param String str 정수형 문자열
	 * @return String 콤마가 추가된 문자열
	 * @exception Exception
	 */
	public static String intToNumFormat(int num) {
		DecimalFormat commas = new DecimalFormat("#,###");
		String resultInt = commas.format(num);
		return resultInt;
	}

	/**
	 *
	  * @Method Name	: lengthLimit
	  * @author			: kdh
	  * @Date			: 2019. 12. 26.
	  * @Description	: 한글 포함 문자열, 특정 길이만큼 자르기
	  * @History		:
	  * @param inputStr
	  * @param limit
	  * @param fixStr
	  * @return
	 */
	public static String lengthLimit(String inputStr, int limit, String fixStr) {
		if (inputStr == null) {return "";}
		if (limit <= 0) { return inputStr;}

		byte[] strbyte = null;
        strbyte = inputStr.getBytes();

        //System.out.println("len:"+strbyte.length);

        if (strbyte.length <= limit) {
            return inputStr;
        }
        char[] charArray = inputStr.toCharArray();
        int checkLimit = limit;
        for ( int i = 0 ; i < charArray.length ; i++ ) {
            if (charArray[i] < 256) {
                checkLimit -= 1;
            }
            else {
                checkLimit -= 2;
            }
            if (checkLimit <= 0) {
                break;
            }
        }
        //대상 문자열 마지막 자리가 2바이트의 중간일 경우 제거함
        byte[] newByte = new byte[limit + checkLimit];
        for ( int i = 0 ; i < newByte.length ; i++ ) {
        	if(i < strbyte.length) {
        		newByte[i] = strbyte[i];
        	}

        }
        if (fixStr == null) {
            return new String(newByte);
        }
        else {
            return new String(newByte) + fixStr;
        }
    }


	public static String subStringBytes(String str, int byteLength, int sizePerLetter) {
		  int retLength = 0;
		  int tempSize = 0;
		  int asc;
		  if (str == null || "".equals(str) || "null".equals(str)) {
		    str = "";
		  }

		  int length = str.length();

		  for (int i = 1; i <= length; i++) {
		    asc = str.charAt(i - 1);
		    if (asc > 127) {
		      if (byteLength >= tempSize + sizePerLetter) {
		        tempSize += sizePerLetter;
		        retLength++;
		      }
		    } else {
		      if (byteLength > tempSize) {
		        tempSize++;
		        retLength++;
		      }
		    }
		  }

		  return str.substring(0, retLength);
	}


	public static String byteCuter(String inputStr, int cutLeng,String append){
		if (inputStr == null) {return "";}
		if(inputStr.toString().getBytes().length > cutLeng){
			StringBuilder stringBuilder = new StringBuilder(cutLeng);
			int nCnt = 0;
			for(char ch:inputStr.toString().toCharArray()){
				nCnt += String.valueOf(ch).getBytes().length;
				if(nCnt > cutLeng) break;
				stringBuilder.append(ch);
			}
			return stringBuilder.toString() + append;
		}else{
			return inputStr;
		}

	}

	/**
	 *
	  * @Method Name	: getRandomStr
	  * @author			: kdh
	  * @Date			: 2019. 10. 30.
	  * @Description	: 무작위 문자열 생성
	  * @History		:
	  * @param len      : 문자열 사이즈
	  * @return
	 */
	public static String getRandomStr(int len)
    {
		String capitalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String smallChars = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String symbols = "!@%~*_=+-/.<>)";

        String values = capitalChars + smallChars + numbers + symbols;

        rand.setSeed(System.currentTimeMillis());
        char[] randomStr = new char[len];

        for (int i = 0; i < len; i++)
        {
        	randomStr[i] = values.charAt(rand.nextInt(values.length()));
        }
        return String.valueOf(randomStr);
    }

	public static String convertUnderScore(String camelStr)
    {

        String result = "";
        char c = camelStr.charAt(0);
        result = result + Character.toLowerCase(c);

        for (int i = 1; i < camelStr.length(); i++) {

            char ch = camelStr.charAt(i);

            if (Character.isUpperCase(ch)) {
                result = result + '_';
                result
                    = result
                    + Character.toLowerCase(ch);
            }

            else {
                result = result + ch;
            }
        }

        return result.toUpperCase();
    }

	public static String convertCamelCase(String underScore) {
		 if (underScore.indexOf('_') < 0 && Character.isLowerCase(underScore.charAt(0))) {
			 return underScore;
		 }
		 StringBuilder result = new StringBuilder();
		 boolean nextUpper = false;
		 int len = underScore.length();
		 for (int i = 0; i < len; i++) {
			 char currentChar = underScore.charAt(i);
			 if (currentChar == '_') {
				 nextUpper = true;
			 } else {
				 if (nextUpper) {
					 result.append(Character.toUpperCase(currentChar));
					 nextUpper = false;
               } else {
              	 result.append(Character.toLowerCase(currentChar));
               }

	         }

	     }
		 return result.toString();

	}

	/**
	 *
	  * @Method Name	: convertPrivacyStr
	  * @author			: mangchi
	  * @Date			: 2022. 2. 4.
	  * @History		:
	  * @param privacyStr 주민번호 포함 문자열
	  * @param idx  변경할려는 문자열의 시작 인덱스
	  * @return
	 */
	public static String convertPrivacyStr(String privacyStr,int idx) {
		if(privacyStr == null || privacyStr.length() == 0){
			return "";
		}
		String switchStr = "******-*******";
		switchStr = switchStr.substring(idx,14);

		int[] validInt = {11,12,13,14,15,21,22,31};
		int[] milInt = {3,4,7,8};
		int[] nonMilInt = {1,2,5,6};

		String patternStr = "\\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])\\-[1-8][0-9]{6}";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(privacyStr);
		while (matcher.find()) {
			String str = matcher.group().replace("-", "");
			int strInt = 0;
			for(int i=0;i<12;i++) {
				strInt += Integer.parseInt(str.substring(i,i+1))*(i%2+1);
			}
			int chkInt = 10 - (strInt % 10);
			if (chkInt == 10) {chkInt = 0;}
			if(!(chkInt == Integer.parseInt(str.substring(12,13)) && IntStream.of(validInt).anyMatch(x -> x == Integer.parseInt(str.substring(4,6))))) {
				if((!str.substring(0,1).equals("0") && IntStream.of(nonMilInt).anyMatch(x -> x == Integer.parseInt(str.substring(6,7))))
						|| (str.substring(0,1).equals("0") && IntStream.of(milInt).anyMatch(x -> x == Integer.parseInt(str.substring(6,7))))){
					privacyStr = privacyStr.replaceAll(matcher.group(), matcher.group().substring(0, idx) + switchStr);
				}
			}

		}
		patternStr = "\\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])\\-[1-8][0-9][*]{6}";
		pattern = Pattern.compile(patternStr);
		matcher = pattern.matcher(privacyStr);
		while (matcher.find()) {
			privacyStr = privacyStr.replace(matcher.group(), matcher.group().substring(0, idx) + switchStr);
		}
		return privacyStr;

	}

	/**
	 *
	  * @Method Name	: checkOrdStr
	  * @author			: mangchi
	  * @Date			: 2022. 4. 26.
	  * @Description	: 연속적인 오름,내림차순 체크 ex)연속 세자리 오름/내림 차순  checkOrdStr("123egw,3)
	  * @History		:
	  * @param str
	  * @param limit
	  * @return
	 */
	public static boolean checkOrdStr(String str,Integer limit) {
		int l = limit==null?3:limit;
		if(l > 3) {
			int o = 0;
			int d = 0;
			int p = 0;
			int n = 0;
			for (int i = 0; i < str.length(); i++ ) {
				char tmpVal = str.charAt(i);
				if (i > 0 && (p = o - tmpVal) > -2 && (n = p == d ? n + 1 : 0 ) > l - 3 ) {
					return true;
				}
				d = p;
				o = tmpVal;
			}
		}else {

			String tmpStr = str.toUpperCase();
			int strLen = tmpStr.length();
			int[] tmpArray = new int[strLen];

			for (int i = 0; i < strLen; i++) {
				tmpArray[i] = tmpStr.charAt(i);
			}
			for (int i = 0; i < strLen - 2; i++) {
				if ((tmpArray[i] > 47 && tmpArray[i + 2] < 58) || (tmpArray[i] > 64 && tmpArray[i + 2] < 91)) {
					if (Math.abs(tmpArray[i + 2] - tmpArray[i + 1]) == 1 && Math.abs(tmpArray[i + 2] - tmpArray[i]) == 2) {
						//char c1 = (char) tmpArray[i]; char c2 = (char) tmpArray[i + 1]; char c3 = (char) tmpArray[i + 2];
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 *
	  * @Method Name	: checkSerialDupStr
	  * @author			: mangchi
	  * @Date			: 2022. 4. 26.
	  * @Description	: 동일한 문자가 연속으로 있는 경우
	  * @History		:
	  * @param str
	  * @param limit
	  * @return
	 */
	public static boolean checkSerialDupStr(String str,Integer limit) {
		try {
			log.debug("str:{}",str);
			log.debug("limit:{}",limit);
		Integer l = limit==null?3:limit;
		String patternStr = "(\\w)";

		for(int i=0;i<l-1;i++) {
			patternStr += "\\1";
		}

		Pattern passPattern = Pattern.compile(patternStr);
		Matcher passMatcher = passPattern.matcher(str);
		if (passMatcher.find()) {
			return true;
		}
		}catch(PatternSyntaxException e) {
			log.error("error:", e );
		}
		return false;
	}

	public static boolean checkPwdStr(String str) {
		String patternStr = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=\\S+$).{9,20}";
		Pattern passPattern = Pattern.compile(patternStr);
		Matcher passMatcher = passPattern.matcher(str);
		if (passMatcher.find()) {
			return true;
		}
		return false;
	}

	public static final long[] IP_RANGE = {16777216L,65536L,256L};

	public static String longToIp(long addr) {
		String ip = "";
		for(int i=0;i<4;i++) {
			if(i<3) {
				ip += (addr/IP_RANGE[i])%256+".";
			}else {
				ip += addr%256;
			}
		}
		return ip;
	}


	 public static long ipToLong(String[] addr) {
		 if(addr == null || addr.length <4) { return 0L; }
		 long ip = 0L;
		 for(int i=0;i<4;i++) {
	     //if(addr[i]) {
	    	 if(i<3) {
	    		 ip += IP_RANGE[i]*Long.parseLong(addr[i]);
	    	 }
	    	 else{
	    		 ip += Long.parseLong(addr[i]);
	    	 }
		 //else {
			 //return 0L;

		// }
         }
		 return ip;
	}

	public static long ipToLong(String ip) {
		 return ipToLong(getBytesByInetAddress(ip));
	}
	public static long ipToLong(byte[] address) {
		if (address == null || address.length < 4) { return 0L; }
		long ip = 0L;
		for (int i = 0; i < 4; ++i) {
			long y = address[i];
		    if (y < 0) { y += 256; }
		    ip += y << ((3 - i) * 8);
		}
		return ip;

	}

	public static byte[] getBytesByInetAddress(String ip) {
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(ip);
		}
		catch (UnknownHostException e) { return new byte[0]; }
		return addr.getAddress();
	}

	public static long ByteArrayToLong(byte[] bytes) {
		ByteBuffer byteBuf = ByteBuffer.allocate(8);
        final byte[] change = new byte[8];

        for (int i = 0; i < 8; i++) {
        	change[i] = (byte) 0x00;
        }
        for (int i = 0; i < bytes.length; i++) {
        	change[7 - i] = bytes[bytes.length - 1 - i];
        }

        byteBuf = ByteBuffer.wrap(change);
        byteBuf.order(ByteOrder.BIG_ENDIAN);

       return byteBuf.getLong();
	}
	public static boolean isOnlyNumber(String s) {
		return s != null && s.matches("\\d*");
	}
	public static boolean isTenIpNumber(String s) {
		if(isOnlyNumber(s)) {
			if(s.length() == 10) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	public static String decryptTenNumberToIp(String s, String key) {
		if(StringUtil.isTenIpNumber(s)) {
			return s;
		}else {
			if(s == null || s.trim().equals("")) {
				return null;
			}else {
				return StringUtil.longToIp(Long.parseLong(ScpDbUtil.scpDec(s, key)));
			}
		}
	}
	
	public static String getBoolean(String boolStr) {
		String rBoolStr = "false";
		if(boolStr == null || boolStr.trim().equals("")) {
			return rBoolStr;
		}
		if(boolStr.trim().equals("1")) {
			rBoolStr = "true";
		}
		return rBoolStr;
	}

}
