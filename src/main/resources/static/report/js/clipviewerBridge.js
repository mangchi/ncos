_clip_eForm_shallow_extend = function(obj,extendBy) {
	for( i in extendBy ) {
		obj[i] = extendBy[i];
	}
	return obj;
}
if (typeof ClipHybrid != 'undefined') {
	//기존 ClipHybrid 객체를 생성하는 경우 ClipHybrid객체를 그대로 사용. 
	//android webview에 ClipJsImp적용. ios jscontext에 ClipHybrid init 한경우 
	//필요할경우 아래와 같이 커스트마이징 가능 
//	ClipHybrid = _clip_eForm_shallow_extend(ClipHybrid,{
//		//네이티브 컨트롤을 미사용 
//		callNativeControl:undefined
//	});
} else if(typeof window.webkit != 'undefined' && window.webkit.messageHandlers.ClipHybrid!='undefined'){
	//iOS용 wkWebview에 addScriptManagerHandler 에  ClipHybrid init 
	ClipHybrid={};
	ClipHybrid = _clip_eForm_shallow_extend(ClipHybrid,{
		getDeviceType:function(){
			//디바이스 type을 리턴한다. android,ios			
			var message = {
	                'fn': 'getDeviceType'
	            };
			window.webkit.messageHandlers.ClipHybrid.postMessage(message);
		},
		getCameraPicture:function(targetId,width,height){
			//카메라 이미지 가져오기. base64로 리턴.
			//상황에 따라 해상도 등을 설정해야할수도 있음.
			_targetId=targetId;
			var message = {
	                'fn': 'getCameraPicture',
	                'targetId': targetId,
	                'width': width,
	                'height': height
	            };
			window.webkit.messageHandlers.ClipHybrid.postMessage(message);
		},
		getBarcode:function(time){
			var message = {
	                'fn': 'getBarcode',
	                'time': time
	            };
			window.webkit.messageHandlers.ClipHybrid.postMessage(message);
		},
		goPrintOut:function(fileUrl){
			//메모리에서 바로 ???
			//서버에서 생성된 파일을 로컬에 저장하지 않고 처리 .....
			var message = {
	                'fn': 'goPrintOut',
	                'mUrl': fileUrl
	            };
			window.webkit.messageHandlers.ClipHybrid.postMessage(message);
		},
		getCalendarDate:function(year,month,day){
			var message = {
	                'fn': 'getCalendarDate',
	                'nYear': year,
	                'nMonth': month,
	                'nDay': day
	            };
			window.webkit.messageHandlers.ClipHybrid.postMessage(message);
		},
		callExternalFunction:function(cmd, param, handler){
			var message = {
	                'fn': 'callExternalFunction',
	                'cmd': cmd,
	                'param': param,
	                'handler': handler
	            };
			window.webkit.messageHandlers.ClipHybrid.postMessage(message);
		},
		showProgressbar:function(){
			var message = {
	                'fn': 'showProgressbar'
	            };
			window.webkit.messageHandlers.ClipHybrid.postMessage(message);
		},
		hideProgressbar:function(){
			var message = {
	                'fn': 'hideProgressbar'
	            };
			window.webkit.messageHandlers.ClipHybrid.postMessage(message);
		},
		callMirraring:function(cmd, msg){
			var message = {
	                'fn': 'callMirraring',
	                'cmd': cmd,
	                'msg': msg
	            };
			window.webkit.messageHandlers.ClipHybrid.postMessage(message);
		},
		callNativeControl:function(cmd, param) {
			var message = {
	                'fn': 'callNativeControl',
	                'cmd': cmd,
	                'param': param
	            };
			window.webkit.messageHandlers.ClipHybrid.postMessage(message);
		},
		closeNativeControl:function(cmd){
			var message = {
	                'fn': 'closeNativeControl',
	                'cmd': cmd
	            };
			window.webkit.messageHandlers.ClipHybrid.postMessage(message);
		},
		fileDownLoad:function(fileUrl) {
			var message = {
	                'fn': 'fileDownLoad',
	                'mUrl': fileUrl
	            };
			window.webkit.messageHandlers.ClipHybrid.postMessage(message);
		},
		resetSignNativeControl:function(ctrlID) {
			var message = {
	                'fn': 'resetNativeControl',
	                'type': 'sign',
	                'ctrlID': ctrlID
	            };
			window.webkit.messageHandlers.ClipHybrid.postMessage(message);
		},
		resetKeyboardNativeControl:function(){
			window.console.log(111);
			var message = {
	                'fn': 'resetNativeControl',
	                'type': 'keyboard'
	            };
			window.webkit.messageHandlers.ClipHybrid.postMessage(message);
		},
		okSignNativeControl:function() {
			var message = {
	                'fn': 'okNativeControl',
	                'type': 'sign'
	            };
			window.webkit.messageHandlers.ClipHybrid.postMessage(message);
		},
		okKeyboardNativeControl:function() {
			var message = {
	                'fn': 'okNativeControl',
	                'type': 'keyboard'
	            };
			window.webkit.messageHandlers.ClipHybrid.postMessage(message);
		},
		okCalendarNativeControl:function() {
			var message = {
	                'fn': 'okNativeControl',
	                'type': 'calendar'
	            };
			window.webkit.messageHandlers.ClipHybrid.postMessage(message);
		},
		okSelectboxNativeControl:function() {
			var message = {
	                'fn': 'okNativeControl',
	                'type': 'selectbox'
	            };
			window.webkit.messageHandlers.ClipHybrid.postMessage(message);
		}
	});
} else if(typeof TestHybrid != 'undefined'){
	//웹뷰에 clip라이브러리 미설정시.  
	ClipHybrid={};
	ClipHybrid = _clip_eForm_shallow_extend(ClipHybrid,{
		getDeviceType:function(){
			//디바이스 type을 리턴한다. android,ios
			return TestHybrid.getDeviceType();
		},
		getCameraPicture:function(targetId,width,height){
			TestHybrid.getCameraPicture(targetId,width,height);
		},
		getBarcode:function(time){
			TestHybrid.getBarcode(time);
		},
		goPrintOut:function(fileUrl){
			//메모리에서 바로 ???
			//서버에서 생성된 파일을 로컬에 저장하지 않고 처리 .....
			TestHybrid.goPrintOut(fileUrl);

		},
		getCalendarDate:function(year,month,day){
			//날짜 선택 
			TestHybrid.getCalendarDate(year,month,day);

		},
		callExternalFunction:function(cmd, param, handler){
			TestHybrid.callExternalFunction(cmd, param, handler);
		},
		showProgressbar:function(){
			TestHybrid.showProgressbar();
		},
		hideProgressbar:function(){
			TestHybrid.hideProgressbar();
		},
		callMirraring:function(cmd, msg){
			TestHybrid.callMirraring(cmd, msg);
		},
		callNativeControl:function(cmd,param) {
			TestHybrid.callNativeControl(cmd, param);
		},
		closeNativeControl:function(cmd){
			TestHybrid.closeNativeControl(cmd);
		},
		fileDownload:function(url){
			TestHybrid.fileDownLoad(url);
		}
	});
}


