<%@page import="com.clipsoft.clipreport.server.service.viewer.*"%>
<%@page import="com.clipsoft.clipreport.server.service.DeleteReport"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="Property.jsp"%><%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
//response.setHeader("Cache-Control", "max-age=0");

//크로스 도메인 설정
response.setHeader("Access-Control-Allow-Origin", "*");

out.clear();
out=pageContext.pushBody();

String ClipType = request.getParameter("ClipType");
if(null != ClipType){
	if("newReport".equals(ClipType)){
		OOFToNewReport newReport = new OOFToNewReport();
		String responseValue = newReport.doPost(request, propertyPath);
		//리포트의 특정 사용자 ID를 부여합니다.
		//clipreport5.properties 의 useuserid 옵션이 true 일 때만 적용됩니다. 
		//clipreport5.properties 의 useuserid 옵션이 true 이고 기본 예제[newReport.doPost(request, response, propertyPath);] 사용 했을 때 세션ID가 userID로 사용 됩니다.
		//newReport.doPost(request, response, propertyPath, "userID");
		
		//리포트key의 사용자문자열을 추가합니다.(문자숫자만 가능합니다.)
		//newReport.doPost(request, response, propertyPath, "userID", "userKey");
		newReport.setOutPutText(response, responseValue);
	}
	else if("pageCheck".equals(ClipType)){
		PageCountCheck pageCountCheck = new PageCountCheck();
		String responseValue = pageCountCheck.doPost(request,  propertyPath);
		pageCountCheck.setOutPutText(response, responseValue);
	}
	else if("makePage".equals(ClipType)){
		PageMaker pageMake = new PageMaker();
		String responseValue = pageMake.doPost(request,  propertyPath);
		
		//리포트의 특정 사용자 ID를 부여합니다. 
		//clipreport5.properties 의 useuserid 옵션이 true 일 때만 적용됩니다. 
		//pageMake.doPost(request, propertyPath, "userID");
		pageMake.setOutPutText(response, responseValue);
	}
	else if("thumbnailPage".equals(ClipType)){
		ThumbnailPage thumbnailPage = new ThumbnailPage();
		String responseValue = thumbnailPage.doPost(request,  propertyPath);
		
		//리포트의 특정 사용자 ID를 부여합니다. 
		//clipreport5.properties 의 useuserid 옵션이 true 일 때만 적용됩니다. 
		//thumbnailPage.doPost(request, propertyPath, "userID");
		thumbnailPage.setOutPutText(response, responseValue);
	}
	else if("TOCReport".equals(ClipType)){
		TOCReport tocReport = new TOCReport();
		String responseValue = tocReport.doPost(request,  propertyPath);
		
		//리포트의 특정 사용자 ID를 부여합니다. 
		//clipreport5.properties 의 useuserid 옵션이 true 일 때만 적용됩니다. 
		//tocReport.doPost(request, propertyPath, "userID");
		tocReport.setOutPutText(response, responseValue);
	}
	else if("makeImagePage".equals(ClipType)){
		ImagePageMaker imagePageMake = new ImagePageMaker();
		imagePageMake.doPost(request, response,  propertyPath);
		//리포트의 특정 사용자 ID를 부여합니다. 
		//clipreport5.properties 의 useuserid 옵션이 true 일 때만 적용됩니다. 
		//imagePageMake.doPost(request, response, propertyPath, "userID");
	}
	else if("exportViewImage".equals(ClipType)){
		response.setHeader("Cache-Control", "max-age=1800");
		ExportViewImage viewImage = new ExportViewImage();
		viewImage.doPost(request, response,  propertyPath);
	}
	else if("exportViewImageBase64".equals(ClipType)){
		ExportViewImage viewImage = new ExportViewImage();
		viewImage.doPostBase64(request, response,  propertyPath);
	}
	else if ("exportBarcodeImage".equals(ClipType)){
		ExportBarcodeImage barcodeImage = new ExportBarcodeImage();
		barcodeImage.doPost(request, response, propertyPath);
	}
	else if ("exportChartImage".equals(ClipType)){
		ExportChartImage chartImage = new ExportChartImage();
		chartImage.doPost(request, response, propertyPath);
	}
	else if ("exportDocumentCheckboxAndRadioImage".equals(ClipType)){
		ExportDocumentCheckAndRadioImage image = new ExportDocumentCheckAndRadioImage();
		image.doPost(request, response, propertyPath);
	}
	else if("deleteReport".equals(ClipType)){
		DeleteReport deleteReport = new DeleteReport();
		deleteReport.doPost(request);
	}
	else if("drilingUpdate".equals(ClipType)){
		DrilingUpdate drilingUpdate = new DrilingUpdate();
		String responseValue = drilingUpdate.doPost(request,  propertyPath);
		drilingUpdate.setOutPutText(response, responseValue);
	}
	
	else if("editableUpdate".equals(ClipType)){
		EditableUpdate editableUpdate = new EditableUpdate();
		String responseValue = editableUpdate.doPost(request,  propertyPath);
		editableUpdate.setOutPutText(response, responseValue);
	}
	else if("editableDocUpdate".equals(ClipType)){
		EditableDocUpdate editableDocUpdate = new EditableDocUpdate();
		String responseValue = editableDocUpdate.doPost(request,  propertyPath);
		editableDocUpdate.setOutPutText(response, responseValue);
	}
	else if("memoUpdate".equals(ClipType)){
		MemoUpdate memoUpdate = new MemoUpdate();
		String responseValue = memoUpdate.doPost(request,  propertyPath);
		memoUpdate.setOutPutText(response, responseValue);
	}
	
	else if("fileDownloadCheck".equals(ClipType)){
		FileDownLoadCheck fileCheck = new FileDownLoadCheck();
		String responseValue = fileCheck.doPostToString(request, propertyPath);
		fileCheck.setOutPutText(response, responseValue);
	}
	else if("PDFPrint".equals(ClipType)){
		PDFPrint pdfPrint = new PDFPrint();
		pdfPrint.doPost(request, response,  propertyPath);
		//리포트의 특정 사용자 ID를 부여합니다. 
		//clipreport5.properties 의 useuserid 옵션이 true 일 때만 적용됩니다. 
		//pdfPrint.doPost(request, response, propertyPath, "userID");
	}
	else if("PDFPrintDownload".equals(ClipType)){
		PDFPrintDownload pdfPrintDownload = new PDFPrintDownload();
		pdfPrintDownload.doPost(request, response,  propertyPath);
		//리포트의 특정 사용자 ID를 부여합니다. 
		//clipreport5.properties 의 useuserid 옵션이 true 일 때만 적용됩니다. 
		//pdfPrintDownload.doPost(request, response, propertyPath, "userID");
	}
	else if("PDFPrintFrame".equals(ClipType)){
		PDFPrintFrame pdfPrintFrame = new PDFPrintFrame();
		pdfPrintFrame.doPost(request, response,  propertyPath);
		//리포트의 특정 사용자 ID를 부여합니다. 
		//clipreport5.properties 의 useuserid 옵션이 true 일 때만 적용됩니다. 
		//pdfPrintDownload.doPost(request, response, propertyPath, "userID");
	}
	else if("HTMLPrint".equals(ClipType)){
		HTMLPrint htmlPrint = new HTMLPrint();
		htmlPrint.doPost(request, response,  propertyPath);
		//리포트의 특정 사용자 ID를 부여합니다. 
		//clipreport5.properties 의 useuserid 옵션이 true 일 때만 적용됩니다. 
		//htmlPrint.doPost(request, response, propertyPath, "userID");
	}
	else if("HTMLPrintDownload".equals(ClipType)){
		HTMLPrintDownload htmlPrintDownload = new HTMLPrintDownload();
		htmlPrintDownload.doPost(request, response,  propertyPath);
		//리포트의 특정 사용자 ID를 부여합니다. 
		//clipreport5.properties 의 useuserid 옵션이 true 일 때만 적용됩니다. 
		//htmlPrintDownload.doPost(request, response, propertyPath, "userID");
	}
	else if("saveExport".equals(ClipType)){
		SaveExport saveExport = new SaveExport();
		saveExport.doPost(request, response,  propertyPath);
		//리포트의 특정 사용자 ID를 부여합니다. 
		//clipreport5.properties 의 useuserid 옵션이 true 일 때만 적용됩니다. 
		//saveExport.doPost(request, response, propertyPath, "userID");
	}
	else if("saveExportDownload".equals(ClipType)){
		SaveExportDownload saveExportDownload = new SaveExportDownload();
		saveExportDownload.doPost(request, response,  propertyPath);
		//리포트의 특정 사용자 ID를 부여합니다. 
		//clipreport5.properties 의 useuserid 옵션이 true 일 때만 적용됩니다. 
		//saveExportDownload.doPost(request, response, propertyPath, "userID");
	}
	else if("makeDocumentPage".equals(ClipType)){
		MageDocumentPage documentPage = new MageDocumentPage();
		documentPage.doPost(request, response,  propertyPath);
	}
	else if("DocumentPageView".equals(ClipType)){
		MakeDocumentJSON documentPage = new MakeDocumentJSON();
		documentPage.doPost(request, response,  propertyPath);
	}
	else if("DocumentPagePrint".equals(ClipType)){
		MakeDocumentJSON documentPage = new MakeDocumentJSON(12);
		documentPage.doPost(request, response,  propertyPath);
	}
	else if("DocumentPageThumbnail".equals(ClipType)){
		MakeDocumentJSON documentPage = new MakeDocumentJSON(13);
		documentPage.doPost(request, response,  propertyPath);
	}
	else if("DocumentPageOnly".equals(ClipType)){
		MakeDocumentJSON documentPage = new MakeDocumentJSON(14);
		documentPage.doPost(request, response,  propertyPath);
	}
	else if("docDownload".equals(ClipType)){
		DocDownload downloadFile = new DocDownload();
		downloadFile.doPost(request, response, propertyPath);
	}
	else if("PageSizeInfo".equals(ClipType)){
		PageSizeInfo pageSizeInfo = new PageSizeInfo();
		String responseValue = pageSizeInfo.doPost(request, propertyPath);
		pageSizeInfo.setOutPutText(response, responseValue);
	}
	else if ("currentCookie".equals(ClipType)){
		CurrentCookie currentCookie = new CurrentCookie();
		String outPutText = currentCookie.doPost(request);
		currentCookie.setOutPutText(response, outPutText);
	}
}
%>