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
 * @since version 1.0.0.9
 * @since version 5.0.1
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
		case 10: ReportWebLog("It is during the generation of reports.!!"); break;
		case 11: ReportWebLog("You do not have permission for the license of the report4..!!"); break;
		case 12: ReportWebLog("You do not have permission for the license of the EForm4..!!"); break;
		case 13: ReportWebLog("XSS attack is suspected...!!"); break;
		case 14: ReportWebLog("You do not have permission for the license of the report5..!!"); break;
		case 15: ReportWebLog("You do not have permission for the license of the EForm5..!!"); break;
		case 20: ReportWebLog("Target and become TAG (div or body) is NULL.!!"); break;
		case 30: ReportWebLog("There is a problem with the creation of the report.!!");break;
		case 31: ReportWebLog("There is a problem with the dynamic attribute script of the report..!!");break;
		case 32: ReportWebLog("not Found Report Page!!");break;
		case 35: ReportWebLog("Another use is ID access.");break;
		case 40: ReportWebLog("It failed to connect to the report server.!!");break;
		case 41: ReportWebLog("Server communication problem with output.!!");break;
		case 50: ReportWebLog("Failed to delete the report.!!");break;
		case 60: ReportWebLog("Session of the report server has ended.!!");break;
		case 70: ReportWebLog("There was a problem OOF document.!!");break;
		case 100: ReportWebLog("not found pdf reader ."); break;
		case 101: ReportWebLog("not found hwp viewer."); break;
		case 102: ReportWebLog("There was a problem to html Print."); break;
		case 103: ReportWebLog("not found exe Print."); break;
		case 104: ReportWebLog("There was a problem EXE Print."); break;
		case 105: ReportWebLog("There was a problem Print Service."); break;
		case 106: ReportWebLog("finish EXE Print.."); break;
		case 107: ReportWebLog("cancel EXE Print.."); break;
		case 140: ReportWebLog("page zero"); break;
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
 * @since version 1.0.0.9
 * @since version 5.0.1
 */
function ReportWebLog(message){
	if (is_exeMirroring) {
		if (is_exeMirroringMain) {
			window.external.MainWebBrowserCtrl_DebugLog(message);
		} 
		else if (is_exeMirroringSub) {
			window.external.SubWebBrowserCtrl_DebugLog(message);
		}
		if(typeof window.console != 'undefined'){
			window.console.log(message);
		}
	}
	else if(typeof window.console != 'undefined'){
		window.console.log(message);
	}
	else{
		alert(message);
	}
}

/**
 * eForm-Viewer 사용할 때 필수항목에 대한 체크 메시지를 변경합니다.
 * @method UserConfig
 * @param message {Array} 값
 * @returns {String}
 * @since version 1.0.0.67
 * @since version 5.0.1
 */
function EFormNecessaryCheckMessage(arrMessage, callBack){
	var message = new Array();
	for(var i=0; i<arrMessage.length; i++){
		message.push((i+1));
		message.push(". ");
		message.push(arrMessage[i]);
		message.push("<br>");
	}
	ClipAlertUI("필수 항목이 모두 체크되지 않았습니다.", message.join(""), callBack);
}

window.m_clipAlert = null;
/** 
 * Alert UI를 사용합니다.
 * @method UserConfig
 * @param title {String} 제목
 * @param text {String} 내용
 * @param callBack {function} 콜백함수
 * @since version 1.0.0.342
 * @since version 5.0.105
 */
function ClipAlertUI(title, text, callBack, type){
	if(null != window.m_clipAlert){
		document.body.removeChild(window.m_clipAlert);
		window.m_clipAlert = null;
	}
	if(typeof type == 'undefined'){
		type = 1;
	}
	var  backgroundDiv = document.createElement("div");
	backgroundDiv.addEventListener('contextmenu', function(event){
		event.preventDefault()
	}, false );
	window.m_clipAlert = backgroundDiv;
	if(is_smartphone){
		backgroundDiv.className = "m_eform_neterror_background";
	}
	else{
		backgroundDiv.className = "eform_neterror_background";
	}
	backgroundDiv.style.top = "0px";
	backgroundDiv.style.width = "auto";
	backgroundDiv.style.left = "0px";
	backgroundDiv.style.right = "0px";
	var  alertLayoutDiv = document.createElement("div");
	alertLayoutDiv.className = "eform_neterror_layout";
	var  alertLayoutHeadDiv = document.createElement("div");
	alertLayoutHeadDiv.className ="eform_neterror_font eform_neterror_layout_head";
	alertLayoutHeadDiv.innerHTML = title;
	var  alertLayoutBodyDiv = document.createElement("div");
	alertLayoutBodyDiv.className ="eform_neterror_font eform_neterror_layout_body";
	alertLayoutBodyDiv.innerHTML =text ;
	var  alertLayoutBottonDiv = document.createElement("div");
	alertLayoutBottonDiv.className ="eform_neterror_font eform_neterror_layout_bottom";
	if(type > 0){
		var  okButton = document.createElement("button");
		okButton.onclick = function(){
			document.body.removeChild(backgroundDiv);
			window.m_clipAlert = null;
			if(typeof callBack != 'undefined'){
				callBack(1);
			}
		};
		okButton.className = "eform_neterror_button";
		okButton.innerHTML = "확인";
		alertLayoutBottonDiv.appendChild(okButton);
	}
	if(type > 1){
		var  cancelButton = document.createElement("button");
		cancelButton.onclick = function(){
			document.body.removeChild(backgroundDiv);
			window.m_clipAlert = null;
			if(typeof callBack != 'undefined'){
				callBack(2);
			}
		};
		cancelButton.className = "eform_neterror_button";
		cancelButton.style.marginLeft = "20px";
		cancelButton.innerHTML = "취소";
		
		alertLayoutBottonDiv.appendChild(cancelButton);
	}
	
	
	
	alertLayoutDiv.appendChild(alertLayoutHeadDiv);
	alertLayoutDiv.appendChild(alertLayoutBodyDiv);
	alertLayoutDiv.appendChild(alertLayoutBottonDiv);
	backgroundDiv.appendChild(alertLayoutDiv);
	document.body.appendChild(backgroundDiv);
	
	
	var textHeight = alertLayoutBodyDiv.offsetHeight;
	if(textHeight > 300){
		alertLayoutBodyDiv.style.height =  "300px";
		alertLayoutBodyDiv.style.overflow = "auto";
		alertLayoutDiv.style.height =  "460px";
	}
	else{
		alertLayoutDiv.style.height = (textHeight + 160) + "px";
	}
	
	var bgWidth = backgroundDiv.offsetWidth;
	var bgHeight = backgroundDiv.offsetHeight;
	
	alertLayoutDiv.style.left = ((bgWidth-alertLayoutDiv.offsetWidth)/2) + "px";
	alertLayoutDiv.style.top =  (((bgHeight-alertLayoutDiv.offsetHeight)/2)-50) + "px";
} 

/** 
 * Alert UI를 사용합니다.
 * @method UserConfig
 * @param title {String} 제목
 * @param text {String} 내용
 * @param type {Number} 0=없음,1=확인,2=없음(메인) 확인(서브),3=확인(메인) 없음(서브)
 * @since version 1.0.0.342
 * @since version 5.0.105
 */
function ClipMirroringAlertUI(title, text, type){
	if(typeof type == 'undefined'){
		type = 3;
	}
	
	var alertType = type;
	if(type == 2 || type == 3){
		alertType = 1;
	}	
		
	if(null != window.m_clipEFormSocket){
		if(!window.m_clipEFormSocket.isMain){
			if(type== 3){
				alertType = 0; 
			}
		}
		else{
			if(type == 2){
				alertType = 0; 
			}
		}
	}
	
	ClipAlertUI(title, text, function(value){
		try{
			if(null != window.m_clipEFormSocket){
				window.m_clipEFormSocket.sendCloseClipAlert();
			}
		}catch(e){
			
		}
	}, alertType);
	
	try{
		if(null != window.m_clipEFormSocket){
			window.m_clipEFormSocket.sendOpenClipAlert(title, text, type);
		}
	}catch(e){
		
	}
}



/**
 * 뷰어에서 프린트 선택 메뉴의 순서를 지정합니다.
 * 기본적으로 pdf, html, hwp(hwp 인쇄 사용 일때 표시), exe(미구현)
 * 
  * @method UserConfig
  * @since version 1.0.0.62
  * @since version 5.0.1
 */
function ReportPrintMenuOrder(){
	var MenuOrder =[
            "pdf",
            "html",
			"hwp",
            "exe"
	];
	return MenuOrder;
	
}

/**
 * 뷰어가 호출 하기 전 기본 뷰어의 디자인을 표현할 수 있도록 제공하는 함수입니다.
 * 
 * @method UserConfig
 * @param targetId {String} 리포트를 표현할 Tag의 id
 * @since version 1.0.0.17
 * @since version 5.0.1
 */
function ReportViewForm(targetId){
	var targetDom = document.getElementById(targetId);
	if(null != targetDom){
		targetDom.innerHTML = "<div style=\"position:absolute;left:0px;right:0px;top:0px;bottom:0px;border:0px solid;overflow:hidden;\"><div id=\"re_menud94a5a8a3b4f4f58bc851c1a9d171cb6\" class=\"report_menu_div\" style=\"display:;\"><table class=\"report_menu_table\" vertical-align=\"middle\"><tbody><tr><td class=\"report_menu_table_td\"><div class=\"report_menu_table_td_div\"><nobr><button title=\"저장\" id=\"re_saved94a5a8a3b4f4f58bc851c1a9d171cb6\" class=\"report_menu_button report_menu_save_button report_menu_save_button_svg\" style=\"\"></button><button title=\"pdf 저장\" id=\"re_pdfd94a5a8a3b4f4f58bc851c1a9d171cb6\" class=\"report_menu_button report_menu_pdf_button report_menu_pdf_button_svg\" style=\"\"></button><button title=\"엑셀 저장\" id=\"re_exceld94a5a8a3b4f4f58bc851c1a9d171cb6\" class=\"report_menu_button report_menu_excel_button report_menu_excel_button_svg\" style=\"\"></button><button title=\"한글 저장\" id=\"re_hwpd94a5a8a3b4f4f58bc851c1a9d171cb6\" class=\"report_menu_button report_menu_hwp_button report_menu_hwp_button_svg\" style=\"\"></button><button title=\"프린트\" id=\"re_printd94a5a8a3b4f4f58bc851c1a9d171cb6\" class=\"report_menu_button report_menu_print_button report_menu_print_button_svg\"></button><button title=\"첫 페이지\" id=\"re_firstd94a5a8a3b4f4f58bc851c1a9d171cb6\" class=\"report_menu_button report_menu_leftEnd_button report_menu_leftEnd_button_svg\" style=\"\"></button><button title=\"이전 페이지\" id=\"re_prevd94a5a8a3b4f4f58bc851c1a9d171cb6\" class=\"report_menu_button report_menu_left_button report_menu_left_button_svg\" style=\"\"></button><input title=\"페이지\" id=\"re_inputd94a5a8a3b4f4f58bc851c1a9d171cb6\" type=\"text\" value=\"1\" class=\"report_menu_pageCount_input\"><input title=\"전체 페이지\" id=\"re_totalCountNumberd94a5a8a3b4f4f58bc851c1a9d171cb6\" value=\"/&nbsp;0\" class=\"report_menu_pageCount_span\" style=\"\" readonly=\"\"><span id=\"re_totalCountd94a5a8a3b4f4f58bc851c1a9d171cb6\" class=\"report_menu_pageCount_span\" style=\"border:0px;\"></span><button title=\"다음 페이지\" id=\"re_nextd94a5a8a3b4f4f58bc851c1a9d171cb6\" class=\"report_menu_button report_menu_right_button report_menu_right_button_svg\" style=\"\"></button><button title=\"마지막 페이지\" id=\"re_lastd94a5a8a3b4f4f58bc851c1a9d171cb6\" class=\"report_menu_button report_menu_rightEnd_button report_menu_rightEnd_button_svg\" style=\"\"></button><select title=\"화면 비율 선택\" onchange=\"m_reportHashMap['d94a5a8a3b4f4f58bc851c1a9d171cb6'].zoomIn(this)\" name=\"zoomSelect\" id=\"re_zoomSelectd94a5a8a3b4f4f58bc851c1a9d171cb6\" class=\"report_menu_zoom_combo\" style=\"\">;<option name=\"p50\" value=\"0.5\" class=\"report_menu_zoom_combo_option\">50%</option><option name=\"p75\" value=\"0.75\" class=\"report_menu_zoom_combo_option\">75%</option><option name=\"p100\" value=\"1\" class=\"report_menu_zoom_combo_option\" selected=\"\">100%</option><option name=\"p125\" value=\"1.25\" class=\"report_menu_zoom_combo_option\">125%</option><option name=\"p150\" value=\"1.5\" class=\"report_menu_zoom_combo_option\">150%</option><option name=\"p200\" value=\"2\" class=\"report_menu_zoom_combo_option\">200%</option><option name=\"p300\" value=\"3\" class=\"report_menu_zoom_combo_option\">300%</option><option name=\"p400\" value=\"4\" class=\"report_menu_zoom_combo_option\">400%</option><option name=\"pWidth\" value=\"PageWidth\" class=\"report_menu_zoom_combo_option\">PageWidth</option><option name=\"pWholePage\" value=\"WholePage\" class=\"report_menu_zoom_combo_option\">WholePage</option></select><button title=\"등록 정보\" id=\"re_reportInfod94a5a8a3b4f4f58bc851c1a9d171cb6\" class=\"report_menu_button report_menu_reportInfo_button report_menu_reportInfo_button_svg\" style=\"\"></button><button title=\"닫기\" id=\"re_closed94a5a8a3b4f4f58bc851c1a9d171cb6\" class=\"report_menu_button report_menu_close_button report_menu_close_button_svg\" style=\"\" onclick=\"document.getElementById('"+targetId+"').innerHTML='';return false;\"></button></nobr></div></td></tr></tbody></table></div><div id=\"re_paintDivd94a5a8a3b4f4f58bc851c1a9d171cb6\" class=\"report_paint_div\" style=\"overflow: auto;\"></div></div>";
	}
}


/**
 * 리포트 전체에 저장 선택부분에서 기본 타입을 지정합니다.<br>
 * @example
 *{String} excel, excelx, pdf, hwp, rtf, ppt, html5, hancell, doc, jpg, txt, tif, multiTif, hanshow
 * 
 * @method UserConfig
 * @since version 1.0.0.18
 * @since version 5.0.1
 */
function getDefaultSaveOption(){
	return "excel";
}


/**
 * 뷰어에서 저장 선택 메뉴의 순서를 지정합니다.
 * 기본적으로 excel, excelx, pdf, hwp, rtf, ppt, html5, hancell, doc, jpg, txt, tif, multiTif, excelData, hanshow
 * 
  * @method UserConfig
  * @since version 1.0.0.136
  * @since version 5.0.1
 */
function ReportSaveMenuOrder(){
	var MenuOrder =[
		"excel",
        "excelx",
		"pdf",
        "hwp",
        "rtf",
        "ppt",
        "pptx",
        "html",
        "html5",
        "doc",
        "jpg",
        "txt",
        "tif",
        "multiTif",
        "excelData",
        "docx",
        "brl",
		"hancell",
		"hanshow"
	];
	return MenuOrder;
	
}

/**
 * 전체 리포트에 대한 Excel 저장 옵션 기본값을 설정합니다.<br>
 * @example
 * exportMethod {Integer} <br>
 * 1 = 페이지 마다 <br>
 * 2 = 하나의 시트 <br>
 * 3 = 하나의 시트(페이지영역 무시)<br>
 * 4 = 리포트 마다(페이지영역 무시)<br>
 * 5 = 리포트 마다<br>
 * mergeCell {Boolean} 셀 합치기<br>
 * mergeEmptyCell {Boolean} 공백 셀일 경우 합치기<br>
 * splitCellAtPageSize {Boolean} 페이지 크기로 셀 분리<br>
 * rightToLeft {Boolean} 열이 오른쪽에서 왼쪽으로 진행<br>
 * widthRate {Integer} 가로 비율<br>
 * heightRate {Integer} 세로 비율<br>
 * coordinateErrorLimit {Integer} 좌표 오차 범위<br>
 * processGerenalFormat {Integer}<br>
 * 1 = 텍스트<br>
 * 2 = 일반<br>
 * printingMagnification {Integer} 인쇄 확대/축소 비율<br>
 * fitToPageWhenPrinting {Boolean} 출력시 페이지 맞춤<br> 
 * removeHyperlink {Boolean} 하이퍼링크 제거<br> 
 * repeatPageSectionWhenAllSectionInOneSheet {Boolean} 페이지 머리글/바닥글 반복<br> 
 * useSubreportNameAsSheetName {Boolean} 리포트명을 시트명으로 사용<br> 
 * defaultPageSheetName {String} 기본 페이지 시트명<br> 
 * clearEmptySubSection {Boolean} 비어있는 영역 무시<br> 
 * clearCrosstabHeader {Boolean} 반복되는 크로스탭 헤더 무시<br> 
 * 
 * @method UserConfig
 * @since version 1.0.0.18
 * @since version 5.0.1
 */
function getDefaultSaveExcelOption(){
	var option = {
			exportMethod : "1",
			mergeCell : true,
			mergeEmptyCell : false,
			splitCellAtPageSize : true,
			rightToLeft : false,
			widthRate : 100,
			heightRate : 100, 
			coordinateErrorLimit : 10,
			processGerenalFormat : 1,
			printingMagnification : 100,
			fitToPageWhenPrinting : false,
			removeHyperlink : false,
			repeatPageSectionWhenAllSectionInOneSheet:false,
			userPassword : "",
			useSubreportNameAsSheetName : false,
			defaultPageSheetName : "",
			clearEmptySubSection : false,
			clearCrosstabHeader : false
	};
	return option;
}


/**
 * 전체 리포트에 대한 Excel(xlsx) 저장 옵션 기본값을 설정합니다.<br>
 * @example
 * exportMethod {Integer} <br>
 * 1 = 페이지 마다 <br>
 * 2 = 하나의 시트 <br>
 * 3 = 하나의 시트(페이지영역 무시)<br>
 * 4 = 리포트 마다(페이지영역 무시)<br>
 * 5 = 리포트 마다<br>
 * mergeCell {Boolean} 셀 합치기<br>
 * mergeEmptyCell {Boolean} 공백 셀일 경우 합치기<br>
 * splitCellAtPageSize {Boolean} 페이지 크기로 셀 분리<br>
 * rightToLeft {Boolean} 열이 오른쪽에서 왼쪽으로 진행<br>
 * widthRate {Integer} 가로 비율<br>
 * heightRate {Integer} 세로 비율<br>
 * coordinateErrorLimit {Integer} 좌표 오차 범위<br>
 * processGerenalFormat {Integer}<br>
 * 1 = 텍스트<br>
 * 2 = 일반<br>
 * printingMagnification {Integer} 인쇄 확대/축소 비율<br>
 * fitToPageWhenPrinting {Boolean} 출력시 페이지 맞춤<br>
 * removeHyperlink {Boolean} 하이퍼링크 제거<br> 
  * repeatPageSectionWhenAllSectionInOneSheet {Boolean} 페이지 머리글/바닥글 반복<br> 
  * userPassword {String} 문서 암호<br> 
  * useSubreportNameAsSheetName {Boolean} 리포트명을 시트명으로 사용<br> 
   * defaultPageSheetName {String} 기본 페이지 시트명<br> 
  * clearEmptySubSection {Boolean} 비어있는 영역 무시<br> 
  * clearCrosstabHeader {Boolean} 반복되는 크로스탭 헤더 무시<br> 
  *  
 * @method UserConfig
 * @since version 1.0.0.54
 * @since version 5.0.1
 */
function getDefaultSaveExcelxOption(){
	var option = {
			exportMethod : "1",
			mergeCell : true,
			mergeEmptyCell : false,
			splitCellAtPageSize : true,
			rightToLeft : false,
			widthRate : 100,
			heightRate : 100, 
			coordinateErrorLimit : 10,
			processGerenalFormat : 1,
			printingMagnification : 100,
			fitToPageWhenPrinting : false,
			removeHyperlink : false,
			repeatPageSectionWhenAllSectionInOneSheet:false,
			userPassword : "",
			is2Dot5VersionConverted : false,
			useSubreportNameAsSheetName : false,
			defaultPageSheetName : "",
			clearEmptySubSection : false,
			clearCrosstabHeader : false
	};
	return option;
}

/**
 * 전체 리포트에 대한 HanCell 저장 옵션 기본값을 설정합니다.<br>
 * @example
 * exportMethod {Integer} <br>
  * 1 = 페이지 마다 <br>
 * 2 = 하나의 시트 <br>
 * 3 = 하나의 시트(페이지영역 무시)<br>
 * 4 = 리포트 마다(페이지영역 무시)<br>
 * 5 = 리포트 마다<br>
 * mergeCell {Boolean} 셀 합치기<br>
 * mergeEmptyCell {Boolean} 공백 셀일 경우 합치기<br>
 * splitCellAtPageSize {Boolean} 페이지 크기로 셀 분리<br>
 * rightToLeft {Boolean} 열이 오른쪽에서 왼쪽으로 진행<br>
 * widthRate {Integer} 가로 비율<br>
 * heightRate {Integer} 세로 비율<br>
 * coordinateErrorLimit {Integer} 좌표 오차 범위<br>
 * processGerenalFormat {Integer}<br>
 * 1 = 텍스트<br>
 * 2 = 일반<br>
 * printingMagnification {Integer} 인쇄 확대/축소 비율<br>
 * fitToPageWhenPrinting {Boolean} 출력시 페이지 맞춤<br>
 * removeHyperlink {Boolean} 하이퍼링크 제거<br> 
 * repeatPageSectionWhenAllSectionInOneSheet {Boolean} 페이지 머리글/바닥글 반복<br> 
 * userPassword {String} 문서 암호<br> 
 * useSubreportNameAsSheetName {Boolean} 리포트명을 시트명으로 사용<br> 
  * defaultPageSheetName {String} 기본 페이지 시트명<br> 
 * clearEmptySubSection {Boolean} 비어있는 영역 무시<br> 
 * clearCrosstabHeader {Boolean} 반복되는 크로스탭 헤더 무시<br> 
 
 * @method UserConfig
 * @since version 1.0.0.18
 * @since version 5.0.1
 * 
 */


function getDefaultSaveHanCellOption(){
	var option = {
			exportMethod : "1",
			mergeCell : true,
			mergeEmptyCell : false,
			splitCellAtPageSize : true,
			rightToLeft : false,
			widthRate : 100,
			heightRate : 100, 
			coordinateErrorLimit : 10,
			processGerenalFormat : 1,
			printingMagnification : 100,
			fitToPageWhenPrinting : false,
			removeHyperlink : false,
			repeatPageSectionWhenAllSectionInOneSheet:false,
			userPassword : "",
			useSubreportNameAsSheetName : false,
			defaultPageSheetName : "",
			clearEmptySubSection : false,
			clearCrosstabHeader : false
	};
	return option;
}

/**
 * 전체 리포트에 대한 HWP 저장 옵션 기본값을 설정합니다.<br>
* @example
* fixSize {Boolean} 크기 고정<br>
* allowOverlay {Boolean} 겹침 허용<br>
* setPageBottomMarginToZero {Boolean} 페이지 바닥 여백을 0으로 설정<br>
* outputLikeWord {Boolean} 글자처럼 출력<br>
* tableSplitMethod {Integer}<br>
* 1 = 나눔 <br>
* 2 = 셀 단위로 나눔 <br>
* 3 = 나누지 않음 <br>
* defaultCharGap {Integer} 기본 자간<br>
* charRatio {Integer} 장평<br>
* putCheckboxIntoCell {Boolean} 셀 안에 체크박스 넣기(version 1.0.0.22)<br>
* splitTextByLine {Boolean} 텍스트를 라인별로 나눠서 표현(version 1.0.0.23)<br>
* mergeTable{Boolean} 이웃한 테이블과 병합할 것인지 여부(version 1.0.0.55)<br>
* lineSpaceRate{Integer} 줄간격 비율(version 1.0.0.58)<br>
* positionRelTo{Integer} 좌표 기준(version 1.0.0.63)<br>
* 1 = 종이 <br>
* 2 = 문단 <br>
* hwpExportMethod{Integer} 저장 방식(version 1.0.0.99)<br>
* 1 = 일반 <br>
* 2 = 표만 나열하기 <br>
* 표만 나열하기 옵션 사용 시[크기고정(false), 겸침 허용(false), 표 합치기(false), 페이지 바닥 여백 0로 설정(false), <br>
* 글자처럼(true), 테이블셀에 표 넣기(true), 페이지영역에서 표 나눔(셀 단위로 나눔) , 위치 기준(문단) 으로 고정]<br> 
* splitPageOnListTableOnly{Boolean}  “표만 나열하기” 시 페이지 나눔 (version 1.0.0.102)<br>
* pageBottomMarginApplicationRate {Integer}  “페이지 바닥 여백을 0으로 설정” 이 false일때 사용가능하며 페이지 아래쪽 여백 적용비율(1~100) (version 1.0.0.107)<br>
* insertPageNumber {Boolean}  페이지 넘버 삽입 (version 1.0.0.173)<br>
* mergeAllTablesOnListTableOnly {Boolean}  표만 나열하기 일떄 모든 테이블 병합하기 (version 1.0.0.212)<br>
* insertLabelWithTextOnListTableOnly {Boolean}  표만 나열하기 일떄 글상자를 텍스트로 삽입 (version 1.0.0.212)<br>
* isTransparentMode {Boolean}  표의 배경 투명으로 표시  (version 1.0.0.291)<br>
* isSectionClear {Boolean}  배경색상을 설정했을경우 무시  (version 1.0.0.367)<br>
 
 
* @method UserConfig
* @since version 1.0.0.18
* @since version 5.0.1
* 
*/
function getDefaultSaveHWPOption(){
	var option = {
			fixSize : true,
			allowOverlay : true,
			setPageBottomMarginToZero : true,
			outputLikeWord : false,
			tableSplitMethod : 2,
			defaultCharGap : -8,
			charRatio : 100,
			putCheckboxIntoCell : true,
			splitTextByLine : true,
			mergeTable : false,
			lineSpaceRate : 100,
			positionRelTo : 1,
			hwpExportMethod : 1,
			splitPageOnListTableOnly : true,
			pageBottomMarginApplicationRate : 100,
			insertPageNumber : false,
			mergeAllTablesOnListTableOnly : false,
			insertLabelWithTextOnListTableOnly : false,
			isTransparentMode : false,
			isSectionClear : false
	};
	return option;
}

/**
 * 전체 리포트에 대한 RTF 저장 옵션 기본값을 설정합니다.<br>
 * @example
 * splitTextLine {Boolean} 문자열을 여러 줄로 나누어 표현<br>
 * processAsUnicode {Boolean} 유니코드로 문자열을 처리 <br>
 * processEqualAlign {Integer} <br>
 * 1 = 왼쪽 정렬 <br>
 * 2 = 가운데 정렬 <br>
 * 3 = 오른쪽 정렬 <br>
 * 4 = 양쪽 정렬 <br>
 * defaultCharSpace {Double} 기본 자간 <br>
 * tableWrapperBottomGap {Integer} 표를 감싸는 객체의 아래쪽 여백 <br>
 * insertTableWrapper {Boolean} 표를 감싸는 객체의 아래쪽 여백 <br>
 * mergeTable {Boolean} 이웃한 테이블과 병합할 것인지 여부 <br>
 * fitShapeToText  {Boolean} 도형을 텍스트 크기에 맟춤 <br>
 * TableRowHeightSort  {Integer} 표의 행높이 설정<br>
 * 1 = 고정 <br>
 * 2 = 최소(워드에서 표 셀의 글자를 넣으면 셀이 늘어남) <br>
 * @method UserConfig
 * @since version 1.0.0.18
 * @since version 5.0.1
 */
function getDefaultSaveRTFOption(){
	var option = {
		splitTextLine : true,
		processAsUnicode : true,
	    processEqualAlign : 4,
	    defaultCharSpace : -0.5,
	    tableWrapperBottomGap : 0,
	    insertTableWrapper : true,
	    mergeTable : false,
	    fitShapeToText : false,
		TableRowHeightSort:1
	};
	return option;
}

/**
 * 전체 리포트에 대한 DOC 저장 옵션 기본값을 설정합니다.<br>
 * @example
 * splitTextLine {Boolean} 문자열을 여러 줄로 나누어 표현<br>
 * processAsUnicode {Boolean} 유니코드로 문자열을 처리 <br>
 * processEqualAlign {Integer} <br>
 * 1 = 왼쪽 정렬 <br>
 * 2 = 가운데 정렬 <br>
 * 3 = 오른쪽 정렬 <br>
 * 4 = 양쪽 정렬 <br>
 * defaultCharSpace {Double} 기본 자간 <br>
 * tableWrapperBottomGap {Integer} 표를 감싸는 객체의 아래쪽 여백 <br>
  * insertTableWrapper {Boolean} 표를 감싸는 객체의 아래쪽 여백 <br>
 * mergeTable {Boolean} 이웃한 테이블과 병합할 것인지 여부 <br>
 * fitShapeToText  {Boolean} 도형을 텍스트 크기에 맟춤 <br>
 * TableRowHeightSort  {Integer} 표의 행높이 설정<br>
 * 1 = 고정 <br>
 * 2 = 최소(워드에서 표 셀의 글자를 넣으면 셀이 늘어남) <br>
 * @method UserConfig
 * @since version 1.0.0.23
 * @since version 5.0.1
 */
function getDefaultSaveDOCOption(){
	var option = {
		splitTextLine : true,
		processAsUnicode : true,
	    processEqualAlign : 4,
	    defaultCharSpace : -0.5,
	    tableWrapperBottomGap : 0,
	    insertTableWrapper : true,
	    mergeTable : false,
	    fitShapeToText : false,
		TableRowHeightSort:1
	};
	return option;
}

/**
 * 전체 리포트에 대한 DOCX 저장 옵션 기본값을 설정합니다.<br>
 * @example
 * splitTextLine {Boolean} 문자열을 여러 줄로 나누어 표현<br>
 * defaultCharSpace {Double} 기본 자간 <br>
 * tableWrapperBottomGap {Integer} 표를 감싸는 객체의 아래쪽 여백 <br>
  * insertTableWrapper {Boolean} 표를 감싸는 객체의 아래쪽 여백 <br>
 * mergeTable {Boolean} 이웃한 테이블과 병합할 것인지 여부 <br>
 * lineSpaceRate {Number} 줄간격 비율 <br>
 * userPassword {String} 문서 암호  <br>
 * 
 * @method UserConfig
 * @since version 1.0.0.163
 * @since version 5.0.1
 */
function getDefaultSaveDOCXOption(){
	var option = {
		splitTextLine : 	true,
	    defaultCharSpace : -0.5,
	    tableWrapperBottomGap : 0,
	    insertTableWrapper : true,
	    mergeTable : false,
	    lineSpaceRate : 100,
	    easyEditable : false,
	    userPassword : ""
	};
	return option;
}

/**
 * 전체 리포트에 대한 HTML 저장 옵션 기본값을 설정합니다.<br>
 * @example
 * processCellLikeShape {Boolean} 테이블 셀을 라벨처럼 저정 <br>
 * displayPageLine {Boolean} 페이지 분리선 표시 <br>
 * keepPageHeight {Boolean} 페이지 높이 유지 <br>
 * applyWordBreak {Boolean} 글자 감싸기 적용 <br>
 * setTextProperiesToEmptyCell {Boolean} 빈 셀에 택스트 속성 설정 <br>
 * putTagIntoEmptyCell {Boolean} 빈 셀에 공백 삽입 <br>
 * textOverflowHidden {Boolean} overflow=hidden 스타일 적용 <br>
 * coordinateRateForX {Double} X축 비율 <br>
 * coordinateRateForY {Double} Y축 비율 <br>
 * encodingType {Integer} 인코딩 <br>
 * 1 = UTF=8 <br>
 * 2 = Unicode <br>
 * 3 = EUC-KR <br>
 * 
 * @method UserConfig
 * @since version 1.0.0.18
 * @since version 5.0.1
 */
function getDefaultSaveHTMLOption(){
	var option = {
		processCellLikeShape : false,
		displayPageLine : true,
		keepPageHeight : true,
		applyWordBreak : false,
		setTextProperiesToEmptyCell : false,
		putTagIntoEmptyCell : false,
		textOverflowHidden : false,
		coordinateRateForX : 2.6,
		coordinateRateForY : 2.6,
		encodingType : 1
	};
	return option;
}

/**
 * 전체 리포트에 대한 PDF 저장 옵션 기본값을 설정합니다.<br>
 * @example
 *  isSplit {Boolean} 분할 저장 선택<br>
 *  pageCount {Integer} 페이지 수<br>
 *  userpw {String} 문서암호 (version 1.0.0.25) <br>
 *  textToImage {Boolean} pdf에 문자를 이미지로 표현 (version 1.0.0.29) <br>
 *  importOriginImage {Boolean} 원본 이미지 사용 (version 1.0.0.31) <br> 
 *  removeHyperlink {Boolean} 하이퍼 링크 제거 (version 1.0.0.141) <br> 
 * 
 * @method UserConfig
 * @since version 1.0.0.18
 * @since version 5.0.1
 * 
 */
function getDefaultSavePDFOption(){
	var option = {
		isSplite : false,
		spliteValue : 1,
		userpw : "",
		textToImage : false,
		importOriginImage : false,
		removeHyperlink : false
	};
	return option;
}


/**
 * 전체 리포트에 대한 html5 저장 옵션 기본값을 설정합니다.<br>
 * @example
 * isSplite {Boolean} 페이지 구분<br>
 * dpiValue {Integer} DPI 값<br>
 * 
 * @method UserConfig
 * @since version 1.0.0.22
 * @since version 5.0.1
 * 
 */
function getDefaultSaveHTML5Option(){
	var option = {
			isSplite : true,
			dpiValue : 96
	};
	return option;
}

/**
 * 전체 리포트에 대한 JPG 저장 옵션 기본값을 설정합니다.<br>
 * @example
 * rotate90 {Boolean} 90도 회전<br>
 * dpiX {Integer} DPI X<br>
 * dpiY {Integer} DPI X<br>
 * quality {Integer} 품질<br>
 * 
 * @method UserConfig
 * @since version 1.0.0.60
 * @since version 5.0.1
 * 
 */
function getDefaultSaveJPGOption(){
	var option = {
			rotate90 : false,
			dpiX : 96,
			dpiY : 96,
			quality : 100
	};
	return option;
}


/**
 * 전체 리포트에 대한 TIF 저장 옵션 기본값을 설정합니다.<br>
 * @example
 * rotate90 {Boolean} 90도 회전<br>
 * dpiX {Integer} DPI X<br>
 * dpiY {Integer} DPI X<br>
 * blackWhite {Boolean} blackWhite<br>
 * gray {Boolean} gray(version 1.0.0.140)<br>
 * 
 * @method UserConfig
 * @since version 1.0.0.128
 * @since version 5.0.1
 * 
 */
function getDefaultSaveTIFOption(){
	var option = {
			rotate90 : false,
			blackWhite : true,
			gray : false,
			dpiX : 96,
			dpiY : 96
	};
	return option;
}

/**
 * 전체 리포트에 대한 Multi TIF 저장 옵션 기본값을 설정합니다.<br>
 * @example
 * rotate90 {Boolean} 90도 회전<br>
 * dpiX {Integer} DPI X<br>
 * dpiY {Integer} DPI X<br>
 * blackWhite {Boolean} blackWhite<br>
 * gray {Boolean} gray(version 1.0.0.140)<br>
 * 
 * @method UserConfig
 * @since version 1.0.0.130
 * @since version 5.0.1
 * 
 */
function getDefaultSaveMultiTIFOption(){
	var option = {
			rotate90 : false,
			blackWhite : true,
			gray : false,
			dpiX : 96,
			dpiY : 96
	};
	return option;
}


/**
 * 전체 리포트에 대한 EXCEL DATA 저장 옵션 기본값을 설정합니다.<br>
 * @example
  outputPageSectionMethod {Integer} 페이지 머리글/바닥글 반복 <br>
 * 0 = 저장안함 <br>
 * 1 = 한번만 <br>
 * 2 = 페이지 마다 <br>
 * 
 * @method UserConfig
 * @since version 1.0.0.140
 * @since version 5.0.1
 * 
 */
function getDefaultSaveEXCELDATAOption(){
	var option = {
			outputPageSectionMethod : 0
	};
	return option;
}
/**
 * 전체 리포트에 대한 PPT 저장 옵션 기본값을 설정합니다.<br>
 * @example
 * mergeTable {Boolean} 이웃한 표 병합<br>
 * ignoreLineSpace {Boolean} 행간 무시<br>
 * removeHyperlink {Boolean} 하이퍼 링크 제거(ver 1.0.0.141)<br>
 * 
 * @method UserConfig
 * @since version 1.0.0.63
 * @since version 5.0.1
 * 
 */
function getDefaultSavePPTOption(){
	var option = {
			mergeTable : false,
			ignoreLineSpace : true,
			removeHyperlink : false
	};
	return option;
}

/**
 * 전체 리포트에 대한 PPTX 저장 옵션 기본값을 설정합니다.<br>
 * @example
 * mergeTable {Boolean} 이웃한 표 병합<br>
 * splitTextByLine {Boolean} 라인별로 나눠서 텍스트 출력<br>
 * removeHyperlink {Boolean} 하이퍼 링크 제거<br>
 * defaultCharGap {Number} 자간 <br>
 * lineSpaceRate {Number} 행간 <br>
 * putCheckboxIntoCell {Boolean} 테이블 셀 안에 체크박스 넣기 <br>
 
 * @method UserConfig
 * @since version 1.0.0.219
 * @since version 5.0.1
 * 
 */
function getDefaultSavePPTXOption(){
	var option = {
			mergeTable : false,
			splitTextByLine : false,
			removeHyperlink : false,
			defaultCharGap : 0,
			lineSpaceRate: 0,
			putCheckboxIntoCell : false
	};
	return option;
}


/**
 * 빠른 PDF 저장 대한 옵션 기본값을 설정합니다.<br>
 * @example
 *  fileName{String} 문서를 저장 할 파일 이름<br>
 *  isSplit {Boolean} 분할 저장 선택<br>
 *  pageCount {Integer} 페이지 수<br>
 *  userpw {String} 문서암호 (version 1.0.0.25) <br>
 *  textToImage {Boolean} pdf에 문자를 이미지로 표현 (version 1.0.0.29) <br>
 *  importOriginImage {Boolean} 원본 이미지 사용 (version 1.0.0.31) <br> 
 *  removeHyperlink {Boolean} 하이퍼 링크 제거 (version 1.0.0.141) <br> 
 * 
 * @method UserConfig
 * @since version 1.0.0.20
 * @since version 5.0.1
 * 
 */
function getDirectSavePDFOption(){
	var option = {
			fileName : "report",
			isSplite : false,
			spliteValue : 1,
			userpw : "",
			textToImage : false,
			importOriginImage : false,
			removeHyperlink : false
		};
		return option;
}

/**
 * 빠른 HWP 저장 대한 옵션 기본값을 설정합니다.<br>
* @example
* fileName{String} 문서를 저장 할 파일 이름<br>
* fixSize {Boolean} 크기 고정<br>
* allowOverlay {Boolean} 겹침 허용<br>
* setPageBottomMarginToZero {Boolean} 페이지 바닥 여백을 0으로 설정<br>
* outputLikeWord {Boolean} 글자처럼 출력<br>
* tableSplitMethod {Integer}<br>
* 1 = 나눔 <br>
* 2 = 셀 단위로 나눔 <br>
* 3 = 나누지 않음 <br>
* defaultCharGap {Integer} 기본 자간<br>
* charRatio {Integer} 장평<br>
* putCheckboxIntoCell {Boolean} 셀 안에 체크박스 넣기(version 1.0.0.22)<br>
* splitTextByLine {Boolean} 텍스트를 라인별로 나눠서 표현(version 1.0.0.23)<br>
* mergeTable{Boolean} 이웃한 테이블과 병합할 것인지 여부(version 1.0.0.55)<br>
* lineSpaceRate{Integer} 줄간격 비율(version 1.0.0.58)<br>
* positionRelTo{Integer} 좌표 기준(version 1.0.0.63)<br>
* 1 = 종이 <br>
* 2 = 문단 <br>
* hwpExportMethod{Integer} 저장 방식(version 1.0.0.99)<br>
* 1 = 일반 <br>
* 2 = 표만 나열하기 <br>
* 표만 나열하기 옵션 사용 시[크기고정(false), 겸침 허용(false), 표 합치기(false), 페이지 바닥 여백 0로 설정(false), <br>
* 글자처럼(true), 테이블셀에 표 넣기(true), 페이지영역에서 표 나눔(셀 단위로 나눔) , 위치 기준(문단) 으로 고정]<br> 
* splitPageOnListTableOnly{Boolean}  “표만 나열하기” 시 페이지 나눔 (version 1.0.0.102) <br>
* pageBottomMarginApplicationRate {Integer}  “페이지 바닥 여백을 0으로 설정” 이 false일때 사용가능하며 페이지 아래쪽 여백 적용비율(1~100) (version 1.0.0.107)<br>
* insertPageNumber {Boolean}  페이지 넘버 삽입 (version 1.0.0.173)<br>
 * mergeAllTablesOnListTableOnly {Boolean}  표만 나열하기 일떄 모든 테이블 병합하기 (version 1.0.0.212)<br>
* insertLabelWithTextOnListTableOnly {Boolean}  표만 나열하기 일떄 글상자를 텍스트로 삽입 (version 1.0.0.212)<br>
* isTransparentMode {Boolean}  표의 배경 투명으로 표시  (version 1.0.0.291)<br>
* isSectionClear {Boolean}  배경색상을 설정했을경우 무시  (version 1.0.0.367)<br>
* 
* @method UserConfig
* @since version 1.0.0.20
* @since version 5.0.1
* 
*/
function getDirectSaveHWPOption(){
	var option = {
			fileName : "report",
			fixSize : true,
			allowOverlay : true,
			setPageBottomMarginToZero : true,
			outputLikeWord : false,
			tableSplitMethod : 2,
			defaultCharGap : -8,
			charRatio : 100,
			putCheckboxIntoCell : true,
			splitTextByLine : true,
			mergeTable : false,
			lineSpaceRate : 100,
			positionRelTo : 1,
			hwpExportMethod : 1,
			splitPageOnListTableOnly : true,
			pageBottomMarginApplicationRate : 100,
			insertPageNumber : false,
			mergeAllTablesOnListTableOnly : false,
			insertLabelWithTextOnListTableOnly : false,
			isTransparentMode : false,
			isSectionClear : false
			
	};
	return option;
}

/**
 * 빠른 EXCEL 저장 대한 옵션 기본값을 설정합니다.<br>
 * @example
 * fileName{String} 문서를 저장 할 파일 이름<br>
 * exportMethod {Integer} <br>
 * 1 = 페이지 마다 <br>
 * 2 = 하나의 시트 <br>
 * 3 = 하나의 시트(페이지영역 무시)<br>
 * 4 = 리포트 마다(페이지영역 무시)<br>
 * 5 = 리포트 마다<br>
 * mergeCell {Boolean} 셀 합치기<br>
 * mergeEmptyCell {Boolean} 공백 셀일 경우 합치기<br>
 * splitCellAtPageSize {Boolean} 페이지 크기로 셀 분리<br>
 * rightToLeft {Boolean} 열이 오른쪽에서 왼쪽으로 진행<br>
 * widthRate {Integer} 가로 비율<br>
 * heightRate {Integer} 세로 비율<br>
 * coordinateErrorLimit {Integer} 좌표 오차 범위<br>
 * processGerenalFormat {Integer}<br>
 * 1 = 텍스트<br>
 * 2 = 일반<br>
 * printingMagnification {Integer} 인쇄 확대/축소 비율<br>
 * removeHyperlink {Boolean} 하이퍼링크 제거<br> 
 * repeatPageSectionWhenAllSectionInOneSheet {Boolean} 페이지 머리글/바닥글 반복<br> 
 * useSubreportNameAsSheetName {Boolean} 리포트명을 시트명으로 사용<br> 
 * clearEmptySubSection {Boolean} 비어있는 영역 무시<br> 
 * clearCrosstabHeader {Boolean} 반복되는 크로스탭 헤더 무시<br> 
 * 
 * @method UserConfig
 * @since version 1.0.0.20
 * @since version 5.0.1
 * 
 */
function getDirectSaveEXCELOption(){
	var option = {
			fileName : "report",
			exportMethod : "1",
			mergeCell : true,
			mergeEmptyCell : false,
			splitCellAtPageSize : true,
			rightToLeft : false,
			widthRate : 100,
			heightRate : 100, 
			coordinateErrorLimit : 10,
			processGerenalFormat : 1,
			printingMagnification : 100,
			fitToPageWhenPrinting : false,
			removeHyperlink : false,
			repeatPageSectionWhenAllSectionInOneSheet : false,
			useSubreportNameAsSheetName : false,
			clearEmptySubSection : false,
			clearCrosstabHeader : false
	};
	return option;
}

/**
 * 빠른 DOC 저장 대한 옵션 기본값을 설정합니다.<br>
 * @example
 * fileName{String} 문서를 저장 할 파일 이름<br>
 * splitTextLine {Boolean} 문자열을 여러 줄로 나누어 표현
 * processAsUnicode {Boolean} 유니코드로 문자열을 처리
 * processEqualAlign {Integer} <br>
 * 1 = 왼쪽 정렬 <br>
 * 2 = 가운데 정렬 <br>
 * 3 = 오른쪽 정렬 <br>
 * 4 = 양쪽 정렬 <br>
 * defaultCharSpace {Double} 기본 자간
 * tableWrapperBottomGap {Integer} 표를 감싸는 객체의 아래쪽 여백
 * insertTableWrapper {Boolean} 테이블을 감싸는 객체를 삽입할 것인지 여부
 * mergeTable {Boolean} 이웃한 테이블과 병합할 것인지 여부
* @method UserConfig
* @since version 1.0.0.62
* @since version 5.0.1
* 
*/
function getDirectSaveDOCOption(){
	var option = {
			fileName : "report",
			type : "doc",
			splitTextLine : true,
			processAsUnicode : true,
			processEqualAlign : 4,
			defaultCharSpace : -0.5,
			tableWrapperBottomGap : 0,
			insertTableWrapper : true,
			mergeTable : false
	};
	return option;
}

/**
 * 리포트서버와의 통신으로 리포트상태를 체크하는 함수 
 * @example 리포트 키를 사용하여 리포트서버에 리포트 생성 진행 상태를 체크합니다.<br>
 * var report_status = ReportStatus('http://localhost:8080/ClipReport4/Clip.jsp', '49368ee12e5a4ca9b35f315994c81bd2'); <br>
 *if(report_status.status) // 리포트 생성중에 상태값 (true :정상적으로 생성중, false: 리포트생성중 오류) 
 *{
 *	if(report_status.endReport) //리포트 생성이 완료되었는지에 대한 상태값 
 *	{
 *		alert('리포트 생성이 완료되었습니다');
 *	}	
 *}
 * 
 * @param clipServerPath 리포트서버와 연결하는 주소 ex)http://localhost:8080/ClipReport4/Clip.jsp
 * @param reportKey 리포트에서 사용하는 리포트 키 ex)49368ee12e5a4ca9b35f315994c81bd2
 * @returns json object 
 * @since version 1.0.0.78
 * @since version 5.0.1
 */
function ReportStatus(clipServerPath, reportKey){
	var s_time = "t" + new Date().getMilliseconds();
	var sendData = "ClipID=R03&uid=" + reportKey + "clipUID=" + reportKey + "&s_time=" + s_time;
	sendData = mRe_onBeforeSend(sendData);
	var objHttpClient = new HttpClient();
	try{
		var resultText = objHttpClient.send(clipServerPath, sendData, false, null);
		resultText = mRe_onAfterSend(resultText);
		
		var data = objectCall(ClipStrTrim(resultText));
		if(typeof data == "string"){
			data = objectCall(data);
		}
		return data;
	}catch(e){
		ReportWebLog(e);
		return {status:false, endReport:false};
	}
}

/**
 * EForm 팔레트의 기본색상을 설정합니다.<br>
* @method UserConfig
* @since version 1.0.0.72
* @since version 5.0.1
* 
*/
function getPalletColor(index){
	switch(index){
		case 0:return "RGB(237, 68, 61)";break;
		case 1:return "RGB(247, 176, 52)";break;
		case 2:return "RGB(252, 227, 72)";break;
		case 3:return "RGB(120, 230, 107)";break;
		case 4:return "RGB(92, 26, 124)";break;
		case 5:return "RGB(0, 0, 0)";break;
		case 6:return "RGB(184, 184, 186)";break;
		case 7:return "RGB(230, 230, 230)";break;
		case 8:return "RGB(31, 144, 250)";break;
		case 9:return "RGB(111, 222, 252)";break;
		default:break;
	}
}

/**
 * EForm 팔레트의 기본두께(px)을 설정합니다.<br>
* @method UserConfig
* @since version 1.0.0.72
* @since version 5.0.1
* 
*/
function getPalletPenWidth(index){
	switch(index){
		case 0:return 4;break;
		case 1:return 8;break;
		case 2:return 12;break;
		case 3:return 16;break;
		case 4:return 24;break;
		default:break;
	}
}

/**
 * EForm 팔레트의 기본투명도(0~1)을 설정합니다.<br>
* @method UserConfig
* @since version 1.0.0.72
* @since version 5.0.1
* 
*/
function getPalletOpacity(index){
	switch(index){
		case 0:return 0.1;break;
		case 1:return 0.2;break;
		case 2:return 0.3;break;
		case 3:return 0.5;break;
		case 4:return 1;break;
		default:break;
	}
}

/**
 * Report exe print service로 호출시 ajax method를 설정합니다.<br>
* @method UserConfig
* @since version 1.0.0.159
* @since version 5.0.1
* 
*/
function getExePrintAjaxType(){
	return "POST";
}

/**
 * Report exe print service로 호출시 쿠키사용 여부를 설정합니다.<br>
* @method UserConfig
* @since version 1.0.0.159
* @since version 5.0.1
* 
*/
function getExePrintUseCookie(){
	return true;
}

/* 클라이언트 페인트 사용 시 통신 설정*/
window.objClipClientPaintConnection = "post";

/* 웹 전용 인감 버튼 라벨*/
window.objClipStampName = "인감";

/**
 *  text에 관하여 서버로 데이터를 전송이 일어나기전에 호출하는 함수입니다.<br>
 *  서버로 text/html 통신이 일어나기전에 아래 선언된 함수를 거쳐서 통신합니다.<br>
 *
 * @param strData {String} 서버로 전송할 데이터 
 * @returns 
 * @since version 1.0.0.72
 * @since version 5.0.1
 */
function onBeforeSend (strData){
	//strData ="report_param=" + mCR_Base64.encode(strData);
	//ReportWebLog("sendData:" + strData);
	return strData;
}


/**
 *  text에 관하여 서버에서 내려온 데이터를 뷰어에 적용하기전에 호출하는 함수입니다.<br>
 *  서버에서 text/html  형태의 데이터가 내려왔을 때 뷰어에 데이터를 적용하기 전에 함수를 거쳐서 적용합니다.<br>
 *
 * @param strData {String} 서버에서 받은 데이터 
 * @returns 
 * @since version 1.0.0.72 
 * @since version 5.0.1
 */
function onAfterSend (strData){
	//strData = mCR_Base64.decode(strData);
	//ReportWebLog("afterData:" + strData);
	return strData;
}

function onSaveBeforeSend(report, strData, printType){
	return true;
	
//	if("pdf" == printType){ //R08
//		report.mRe_printView_demon();
//	}
//	else if("mobile_pdf" == printType){ //R09
//		report.mRe_mobilePDF_demon();
//	}
//	else if("save" == printType){//R09
//		report.mRe_saveFile_demon();
//	}
//	
//	return false;
}



/**
 *  기본적인 리포트서버와의 통신 대신 다른 통신 모듈을 연결 할 수 있도록 지원하는 함수입니다<br>
 *  이폼 뷰어에서만 사용가능하며 서버에서의 나온 결과를 리포트 객체의 eformReceiveData 함수로 받아서 사용합니다.<br>
 *  서버에 연결 jsp는 소스코드의 주석된 부분을 참고하시면 됩니다. 
 *  
 *  
 *
 * @param eform {Obeject} 리포트&이폼 객체
 * @param jsonData {Obejct} 서버 보내는 json 데이터
 * @returns 
 * @since version 1.0.0.121 
 * @since version 5.0.1
 */
function onBrokenSendData(eform, jsonData){
	//기존 통신 사용 여부 (true:사용, false:사용안함)
	return true;
	
//서버와 통신할 데이터 (json 데이터를 문자열 변환 및 URI 인코딩)
//	var strData = "ClipData=" + encodeURIComponent(JSON.stringify(jsonData));
//	
//	$.ajax({
//		type : "POST",
//		url : "./ClipReport/eform_server.jsp",
//		data : strData,
//		success : function(strResult) {
//			if (null != strResult) {
//				//통신에 대한 콜백 함수
//				eform.eformReceiveData(strResult);
//			}
//		},
//		error : function(strErr) {
//			alert("error : " + strErr);
//		}
//	});
//	return false;
}

/**
 *  이폼 입력 컨트롤의 출력양식을 직접 만들어 사용하도록 함수 제공
 *  
 *  
 *
 * @param strData {String} 입력 문자열
 * @param strFormat {String} 사용자 출력양식
 * @returns 출력양식이 적용된 문자열
 * @since version 1.0.0.345 
 * @since version 5.0.108
 */

function userCustomFormat(strData, strFormat){
	return "";
}


/* exe 전용 메인화면 저장 버튼 라벨*/
window.objClipMainSaveButtonName = "저장";
/* exe 전용 메인화면 인감 버튼 라벨*/
window.objClipMainStampButtonName = "인감";
/* exe 전용 메인화면 전환 버튼 라벨*/
window.objClipMainChangeButtonName = "전환";
/* exe 전용 메인화면 초기화 버튼 라벨*/
window.objClipMainResetButtonName = "초기화";

