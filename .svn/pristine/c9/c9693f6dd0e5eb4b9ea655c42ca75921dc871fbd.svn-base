///////////////////////////////////////////////////MESSAGE///////////////////////////////////

let msg = {
		   expired : "세션이 만료되었습니다."
		 , fileLimit : "첨부파일은 최대 {}개 까지 <br>첨부 가능합니다."
	     , fileName : "파일명이 {}자 이상인 <br>파일은 제외되었습니다."
	     , fileExt : "확장자가 없는 파일은 <br>제외되었습니다."
	     , fileLength : "최대 파일 용량인 {}를 <br>초과한 파일은 제외되었습니다."
	     , fileType : "첨부가 불가능한 파일은 <br>제외되었습니다."
	     , fileDup : "동일한 파일은 <br>제외되었습니다."
	     , success : "정상 처리되었습니다."
	     , fail : "오류가 발생하였습니다."
	     , del  : "삭제하시겠습니까?"
	     , delChk  : "삭제할 항목을 선택하세요."
	     , blockChk  : "차단할 항목을 선택하세요."
	     , clearChk  : "해제할 항목을 선택하세요."
	     , invalidDay : "시작일이 종료일보다 클 수 없습니다"
	     , invalidTm : "현재시간보다 이전 일 수 없습니다"
	     //, approveChk : "승인할 항목을 선택하세요."
	     , invalidDay : "시작일이 종료일보다 클 수 없습니다"
	     , objChk : "선박종류를 선택하세요."
         , diffChk: "조회 시작일과 종료일을 100일 이내로 해주세요."
         , diffChkWarn: "조회 시작일과 종료일 차이가 100일이\x3cbr\x3e초과할 경우에 시간이 오래 걸릴 수 있습니다.<br>그래도 싫행하시겠습니까?"
         , invalidDelete  : "본인이 생성한 건만 삭제할 수 있습니다."
         , possibleDelete  : "본인이 생성한 건만 삭제됩니다."
         , invalidDelZone  : "수정만 가능합니다."
         , btDayChk1: "시작일과 종료일을 <br>2~6일 이내로 해주세요."
         , btDayChk2: "시작일과 종료일이 동일합니다."
         , invalidPeriodId : "스케쥴 등록시 생성주기가 필수값입니다."
         
}



/*
 * @exp     : 메시지 제공
 * @param   : msg-> msg문자열
 *          : msgArr-> 대체 문자열
 * @return  :
 * @example : getMsg(msg.fileLimit,["1"])
 * 
 */

const getMsg = (msg,msgArr) => {
	if(msg === undefined || msg === ''){
		return msg;
	}
	if(msgArr === undefined || msgArr.length === 0){
		return msg;
	}
	for(let msgIdx in msgArr){
		msg = msg.replace("{}",msgArr[msgIdx]);
	}
	msg = msg.replace(/{}/gi, "");
	return msg;
}