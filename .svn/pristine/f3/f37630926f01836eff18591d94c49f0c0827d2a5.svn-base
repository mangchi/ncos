<%@page import="com.clipsoft.clipreport.server.service.viewer.MemoUpdate"%>
<%@page import="com.clipsoft.clipreport.server.service.viewer.MakeDocumentJSON"%>
<%@page import="com.clipsoft.org.apache.commons.codec.binary.Base64"%>
<%@page import="com.clipsoft.clipreport.server.service.viewer.ExportViewImage"%>
<%@page import="com.clipsoft.clipreport.server.service.eform.EFormThumbnailPage"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.File"%>
<%@ page import="java.io.OutputStream"%>
<%@ page import="com.clipsoft.clipreport.server.service.*" %>
<%@ page import="com.clipsoft.clipreport.server.service.export.*" %>
<%@ page import="com.clipsoft.clipreport.server.service.export.save.SAVEReport" %>
<%@ page import="com.clipsoft.clipreport.server.service.export.save.SAVEReportToFileDownload"%>
<%@ page import="com.clipsoft.clipreport.server.service.export.save.SAVEReportToFileCheck"%>
<%@ page import="com.clipsoft.clipreport.server.service.export.save.SAVEReportToFile"%>
<%@ page import="com.clipsoft.clipreport.server.service.html.PrintHTML" %>
<%@ page import="com.clipsoft.clipreport.server.service.reporteservice.*" %>
<%@ page import="com.clipsoft.clipreport.server.service.eform.EFormUpdateDocument"%>
<%@ page import="com.clipsoft.clipreport.server.service.eform.EFormPage"%>
<%@ include file="Property.jsp"%>
<%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
//response.setHeader("Cache-Control", "max-age=0");
out.clear();
out=pageContext.pushBody();

//크로스 도메인 설정
//response.setHeader("Access-Control-Allow-Origin", "*");

//세션을 활용하여 리포트키들을 관리하지 않는 옵션
//request.getSession().setAttribute("ClipReport-SessionList-Allow", false);

String passName = request.getParameter("ClipID");

if(null != passName){
	if("R01".equals(passName)){
		NewReport newReport = new NewReport();
		newReport.doPost(request, response, propertyPath);
		//String responseValue = newReport.doPostToString(request, response, propertyPath);
		
		//리포트의 특정 사용자 ID를 부여합니다.
		//clipreport5.properties 의 useuserid 옵션이 true 일 때만 적용됩니다. 
		//clipreport5.properties 의 useuserid 옵션이 true 이고 기본 예제[newReport.doPost(request, response, propertyPath);] 사용 했을 때 세션ID가 userID로 사용 됩니다.
		//newReport.doPost(request, response, propertyPath, "userID");
		
		//리포트key의 사용자문자열을 추가합니다.(문자숫자만 가능합니다.)
		//newReport.doPost(request, response, propertyPath, "userID", "userKey");
	}
	else if("R02".equals(passName)){
		Page page1 = new Page();
		page1.doPost(request, response, propertyPath);
		//String pageString = page1.doPostToString(request, response, propertyPath);
		//리포트의 특정 사용자 ID를 부여합니다.
		//clipreport5.properties 의 useuserid 옵션이 true 일 때만 적용됩니다. 
		//page1.doPost(request, response, propertyPath, "userID");
	}
	else if("R03".equals(passName)){
		PageCount pageCount = new PageCount();
		pageCount.doPost(request, response, propertyPath);
		//String responseValue = pageCount.doPostToString(request, response, propertyPath);
	}
	else if("R04".equals(passName)){
		DeleteReport deleteReport = new DeleteReport();
		deleteReport.doPost(request, response);
		//String responseValue = deleteReport.doPostToString(request, response, propertyPath);
	}
	
	else if("R07".equals(passName)){
		HWPReport hwpReport = new HWPReport();
		hwpReport.doPost(request, response);
		
		//리포트의 특정 사용자 ID를 부여합니다.
		//clipreport5.properties 의 useuserid 옵션이 true 일 때만 적용됩니다. 
		//hwpReport.doPost(request, response, "userID");
	}
	else if("R08".equals(passName)){
		PDFReport pdfReport = new PDFReport();
		pdfReport.doPost(request, response);
		
		//리포트의 특정 사용자 ID를 부여합니다.
		//clipreport5.properties 의 useuserid 옵션이 true 일 때만 적용됩니다. 
		//pdfReport.doPost(request, response, "userID");
	} 
	else if("R09".equals(passName)){
		SAVEReport saveReport = new SAVEReport();
		saveReport.doPost(request, response);
		
		//리포트의 특정 사용자 ID를 부여합니다.
		//clipreport5.properties 의 useuserid 옵션이 true 일 때만 적용됩니다. 
		//saveReport.doPost(request, response, "userID");

		
		//문서 암호화를 처리하기 위해서는 jsp forward 사용 합니다.
		//RequestDispatcher dispatcher = request.getRequestDispatcher("export/exportForEncryption.jsp");
		//dispatcher.forward(request, response);
		
	} 
	else if("R09S1".equals(passName)){
		SAVEReportToFile localSaveReport = new SAVEReportToFile();
		localSaveReport.doPost(request, response);
		
		//리포트의 특정 사용자 ID를 부여합니다.
		//clipreport5.properties 의 useuserid 옵션이 true 일 때만 적용됩니다. 
		//saveReport.doPost(request, response, "userID");

		
		//문서 암호화를 처리하기 위해서는 jsp forward 사용 합니다.
		//RequestDispatcher dispatcher = request.getRequestDispatcher("export/exportForEncryption.jsp");
		//dispatcher.forward(request, response);
		
	}
	else if("R09S2".equals(passName)){
		SAVEReportToFileCheck saveFileCheck = new SAVEReportToFileCheck();
		saveFileCheck.doPost(request, response);
	}
	
	else if("R09S3".equals(passName)){
		SAVEReportToFileDownload saveReport = new SAVEReportToFileDownload();
		saveReport.doPost(request, response);
	}
	
	else if("R10".equals(passName)){
		PagePrint page1 = new PagePrint();
		page1.doPost(request, response);
		
		//리포트의 특정 사용자 ID를 부여합니다.
		//clipreport5.properties 의 useuserid 옵션이 true 일 때만 적용됩니다. 
		//page1.doPost(request, response, "userID");
	}
	else if("R11".equals(passName)){
		UpDatePage page1 = new UpDatePage();
		page1.doPost(request, response);
		
		//리포트의 특정 사용자 ID를 부여합니다.
		//clipreport5.properties 의 useuserid 옵션이 true 일 때만 적용됩니다. 
		//page1.doPost(request, response, "userID");
	}
	
	else if("R15".equals(passName)){
		PrintHTML printHTML = new PrintHTML();
		printHTML.doPost(request, response);
	}
	
	else if ("R16".equals(passName)) {
		FileDownLoadCheck fileCheck = new FileDownLoadCheck();
		fileCheck.doPost(request, response);
		//String responseValue = fileCheck.doPostToString(request, response, propertyPath);
	}
	else if ("R17".equals(passName)) {
		PageImage pageImage = new PageImage();
		pageImage.doPost(request, response);
	}
	else if("R30".equals(passName)){
		EFormPage eformPage = new EFormPage();
		eformPage.doPost(request, response, propertyPath);
		
		//리포트의 특정 사용자 ID를 부여합니다.
		//clipreport5.properties 의 useuserid 옵션이 true 일 때만 적용됩니다. 
		//eformPage.doPost(request, response, propertyPath, "userID");
	}
	else if("R31".equals(passName)){
		EFormUpdateDocument eformUpDate = new EFormUpdateDocument();
		eformUpDate.doPost(request, response, propertyPath);
	}
	
	else if("R32".equals(passName)){
		EFormThumbnailPage thumbnailPage = new EFormThumbnailPage();
		String responseValue = thumbnailPage.doPost(request,  propertyPath);
		thumbnailPage.setOutPutText(response, responseValue);
	}

	else if ("R50".equals(passName)) {
		PrintLicense printLicense = new PrintLicense();
		printLicense.doPost(request, response, propertyPath);
	}
	else if ("R51".equals(passName)) {
		UpdateLicense updateLicense = new UpdateLicense();
		updateLicense.doPost(request, response, propertyPath);
	}
	else if ("R52".equals(passName)) {
		ConfigurationAuthorization authorization = new ConfigurationAuthorization();
		authorization.doPost(request, response, propertyPath);
	}
	else if("R97".equals(passName)){
		SVGExport svgExport = new SVGExport();
		svgExport.doZipPost(request, response);
	}
	else if("R98".equals(passName)){
		ImageBase64Export imageExport = new ImageBase64Export();
		imageExport.doPost(request, response);
	}
	else if ("S01".equals(passName)){
		ExePrintInfo exePrintInfo = new ExePrintInfo();
		String strInfo = exePrintInfo.doPost(request);
		exePrintInfo.setOutPutText(response, strInfo);
	}
	
	// Report
}

String ClipType = request.getParameter("ClipType");
if(null != ClipType){
	if("exportViewImage".equals(ClipType)){
		response.setHeader("Cache-Control", "max-age=1800");
		ExportViewImage viewImage = new ExportViewImage();
		viewImage.doPost(request, response,  propertyPath);
	}
	else if("exportViewImageBase64".equals(ClipType)){
		ExportViewImage viewImage = new ExportViewImage();
		viewImage.doPostBase64(request, response,  propertyPath);
	}
	else if("DocumentPageView".equals(ClipType)){
		MakeDocumentJSON documentPage = new MakeDocumentJSON();
		documentPage.doPost(request, response,  propertyPath);
	}
	else if("memoUpdate".equals(ClipType)){
		MemoUpdate memoUpdate = new MemoUpdate();
		String responseValue = memoUpdate.doPost(request,  propertyPath);
		memoUpdate.setOutPutText(response, responseValue);
	}
}
%>