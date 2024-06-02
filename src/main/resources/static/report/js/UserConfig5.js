/**
 * 리포트 결과에 따른 event의 값으로 함수를 호출합니다. <br>
 * 
 * @example 사용자는 아래에 기본 설정된 함수가 아닌 다른 기능으로 대체하여 사용하실 수 있습니다.
 *  case 8:ReportWebLog("리포트 서버가 정상적으로 설치되지 않았습니다.!!"); break;
 *  //위에 코드 대신
 *  case 8:alert("관리자에게 문의 하세요"); break;
 * 
 * @method UserConfig
 * @param event {Integer} 값
 * @since version 2.0.0.1
 */
function ReportEventHandler(event){
	switch(event){
		case 0: 
		case 1:
		case 2:
		case 3:
		case 4:
		case 5: 
		case 6: 
		case 7: 
		case 8: ReportWebLog("Report Server is not installed properly.!!"); break;
		case 9: ReportWebLog("There is a problem with the report generation key.!!"); break;
		case 10: ReportWebLog("It is during the generation of reports.!!"); break;
		case 11: ReportWebLog("You do not have permission for the license of the report4..!!"); break;
		case 12: ReportWebLog("You do not have permission for the license of the EForm4..!!"); break;
		case 13: ReportWebLog("XSS attack is suspected...!!"); break;
		case 14: ReportWebLog("You do not have permission for the license of the report5..!!"); break;
		case 15: ReportWebLog("You do not have permission for the license of the EForm5..!!"); break;
		case 20: ReportWebLog("Target and become TAG (div or body) is NULL.!!"); break;
		case 30: ReportWebLog("There is a problem with the creation of the report.!!");break;
		case 31: ReportWebLog("There is a problem with the dynamic attribute script of the report..!!");break;
		case 40: ReportWebLog("It failed to connect to the report server.!!");break;
		case 50: ReportWebLog("Failed to delete the report.!!");break;
		case 60: ReportWebLog("Sesstion of the report server has ended.!!");break;
		case 70: ReportWebLog("There was a problem OOF document.!!");break;
		case 100: ReportWebLog("not found pdf reader ."); break;
		case 101: ReportWebLog("not found hwp viewer."); break;
		case 102: ReportWebLog("There was a problem to html Print."); break;
		case 103: ReportWebLog("not found exe Print."); break;
		case 104: ReportWebLog("There was a problem EXE Print."); break;
		case 105: ReportWebLog("There was a problem Print Service."); break;
		case 106: ReportWebLog("finish EXE Print.."); break;
		case 107: ReportWebLog("cancel EXE Print.."); break;
		case 150: ReportWebLog("pdf reader version is low.."); break;
		case 200: ReportWebLog("The report was completed successfully.");break;
		case 300: ReportWebLog("It was saved.");break;
		case 301: ReportWebLog("There was a problem when you save.");break;
		case 1000:
		case 1001:
		case 1002:
		case 1003: ReportWebLog("In connection with the license problem occurred.");break;
		case 1004: ReportWebLog("You do not have permission for the license");break;
		default:break;
	}
}

/**
 * console이 존재할 경우 메시지를 console에 출력합니다.<br>
 * console이 없는 브라우져는 alert 창으로 메시지를 출력합니다.
 * 
 * @method UserConfig
 * @param message {String} 값
 * @since version 2.0.0.1
 */
function ReportWebLog(message){
	if(typeof window.console != 'undefined'){
		window.console.log(message);
	}
	else{
		alert(message);
	}
}

/**
 * Report exe print service로 호출시 ajax method를 설정합니다.<br>
* @method UserConfig
* @since version 5.0.53
* 
*/
function getExePrintAjaxType(){
	return "POST";
}

/**
 * Report exe print service로 호출시 쿠키사용 여부를 설정합니다.<br>
* @method UserConfig
* @since version 5.0.53
* 
*/
function getExePrintUseCookie(){
	return true;
}


/* 클라이언트 페인트 사용 시 통신 설정*/
window.objClipClientPaintConnection = "get";