'use strict'


let ncos = { storageKey : "ncosStorage"
	      , cryptKey   : "ncosAuth"
	      , codeKey   : "codeKey"
	      , interval  :  50000000   //5000
	      , intervalTrans  :  10000000   //1000
	      , diffDay   : -100  
	      , uiMode    : "ncosUiMode"
	      , diffDay   : -30
	      , schMode   : 'auto'
	      , intervalFunc : null
	      , watchFunc : null
	      , intervalHm : 24   //24
	      , initBtn : false 
}

const gfn_formToJson = (form) => {
	try{
		const serializedFormData = Object.fromEntries(new FormData(form));
	    console.log("sendData:::",JSON.stringify(serializedFormData));
		return serializedFormData;
	}catch(e){
		console.log("formToJson:",e)
	}
	
}

const gfn_asyncTranDataCall = async (url,method,jsonParam,callBackFn,msgYn) => {
	try{
        sessionTime = gfn_getStorageItem('sessionTm',false);
		if(sessionTime > -1){
			setTimer();
		}
		let _method = method===undefined?'POST':method;
		const data = await fetch(url, {method: _method
		                               , headers: {'Content-Type': 'application/json', 'X-XSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content} // 'Content-Type':
		                               , body: JSON.stringify(jsonParam)
								 }).then((result) =>{
									 return result.json();
								 })
								 .catch((error) => {
									 console.error("gfn_asyncRemoveCall errorr:",error);
								});
		if(msgYn != undefined){
			if(data["success_msg"] != undefined){
				msgCall(data["success_msg"],true,false,callBackFn);
			}
			if(data["fail_msg"] != undefined){
				msgCall(data["fail_msg"],false,false);
			}
		}
		else{
			if(callBackFn != undefined) callBackFn();
			
		}
		
		return data;
    } catch (err) {
    	console.error("gfn_asyncTranDataCall errorr:", err);
    	gfn_asyncSessionChk().then((data)=> {
			if(data.session === 'off')location.href ='/login';
		});
    }
}


const gfn_asyncTranCall = async (url,method,requiredParams,comp,callBackFn,msgYn) => {
	try{
		sessionTime = gfn_getStorageItem('sessionTm',false);
		if(sessionTime > -1){
			setTimer();
		}
		if(requiredParams != undefined && requiredParams != null){
			if(!gfn_valid(comp,requiredParams)){
				return;
			}
		}
		let _method = method===undefined?'POST':method;
		const data = await fetch(url, {method: _method
		                               , headers: {'Content-Type': 'application/json', 'X-XSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content} // 'Content-Type':
		                               , body: JSON.stringify(gfn_genJsonParam(comp))
								 }).then((result) =>{
									 return result.json();
								 })
								 .catch((error) => {
									 console.error("gfn_asyncTranCall errorr:",error);
								});
		if(msgYn != undefined){
			if(data["success_msg"] != undefined){
				msgCall(data["success_msg"],true,false,callBackFn);
			}
			if(data["fail_msg"] != undefined){
				msgCall(data["fail_msg"],false,false);
			}
			/*
			for(let key in data) {
				
				if(key.indexOf('valid') > -1){
					let valid = key.replace('valid_',''); 
					console.log("::",valid);
					let item = comp.querySelector('#'+valid);
				    if(item.tagName === 'INPUT'){
						let message = item.parentNode.querySelector('.message');
				        if(message){
							item.parentNode.classList.add('error');
						     message.textContent =  data[key];
					    }
					    else{
						   comp.querySelector('#'+key).style.borderColor = 'red'; 
					    }
				       
				    }
				    else if(item.tagName === 'UL'){
						item.parentNode.querySelector('.selected').style.borderColor = 'red'; 
				    }
				}
				
			}*/
		}
		
		return data;
    } catch (err) {
    	console.error("gfn_asyncCall errorr:", err);
    	gfn_asyncSessionChk().then((data)=> {
			if(data.session === 'off')location.href ='/login';
		});
    }
}

const gfn_asyncJsonCall = async (url,method,jsonParam,requiredParams,comp,callBackFn,msgYn) => {
	//console.log("gfn_asyncJsonCall schMode:",ncos.schMode);
	try{
		let _method = method===undefined?'POST':method;
		if(_method.toUpperCase() === 'POST' && (jsonParam === undefined || jsonParam === null || Object.keys(jsonParam).length === 0)){
			console.error("param is null........");
			return false;
		}
		if(requiredParams != undefined && requiredParams != null){
			if(!gfn_validJson(comp,jsonParam,requiredParams)){
				return;
			}
		}
		if(jsonParam != undefined && jsonParam != null) {
			gfn_schParam(jsonParam);
			jsonParam.intervalHm = ncos.intervalHm;
		}
		else{
			jsonParam = {intervalHm: ncos.intervalHm};
		}
		if(ncos.initBtn){
			jsonParam.pageNo = 1;
		}
		let sendData = {method: _method
                      , headers: {'Content-Type': 'application/json', 'X-XSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content} 
	                   }; 
	    
		if(method.toUpperCase() === 'POST'){
			sendData.body = JSON.stringify(jsonParam);
		}
		else{
			sendData.params = JSON.stringify(jsonParam);
		}
		//console.log("sendData:",sendData);
		
		if(jsonParam && jsonParam.pageNo && parseInt(jsonParam.pageNo) > 1 && ncos.schMode === 'auto'){
			clearInterval(ncos.intervalFunc);
		}
		const data = await fetch(url, sendData).then((result) =>{
									 return result.json();
								 });
		if(msgYn != undefined){
			if(data["success_msg"] != undefined){
				msgCall(data["success_msg"],true,false,callBackFn);
			}
			if(data["fail_msg"] != undefined){
				msgCall(data["fail_msg"],false,false);
			}
		}
		return data;
    } catch (err) {
    	console.error("gfn_asyncJsonCall errorr:", err);
        gfn_asyncSessionChk().then((data)=> {
			if(data.session === 'off')location.href ='/login';
		});
	
	}
}


const gfn_ssoCall = () => {
	try{
	    console.log("gfn_ssoCall");
	    let url = "http://localhost:8081/loginTest";
        let option = 'resizable=yes';
        window.open(url, '_blank',option);
	    let myForm = document.sessionFrm;
        myForm.action = url;
        myForm.method = "post";
        myForm.target = "_blank";
        myForm.submit();
      
    } catch (err) {
    	console.error("gfn_ssoCall errorr:", err);
    	
    	
    }
}


const gfn_formCall = (form) => {
	try{
		console.log("gfn_formCall...");      
		let urlPath = document.location.protocol + "//" + document.location.host;  
		let lastIdx = urlPath.lastIndexOf(':');
        const formData = new FormData(form);
        const sessionData = new URLSearchParams(formData);        
	    fetch(urlPath.substring(0,lastIdx)+':8081/apiv1/session', {
               method: 'POST',
               headers: {'Content-Type': 'application/x-www-form-urlencoded', 'X-XSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content},
               body: sessionData,
        });

    } catch (err) {
    	console.error("gfn_formCall errorr:", err);
    }
}

const gfn_ssoLogout = (form) => {
	try{
		console.log("gfn_ssoLogout...");      
		let urlPath = document.location.protocol + "//" + document.location.host;  
		let lastIdx = urlPath.lastIndexOf(':');
        const formData = new FormData(form);
        const sessionData = new URLSearchParams(formData);        
	    fetch(urlPath.substring(0,lastIdx)+':8081/apiv1/logout', {
               method: 'POST',
               headers: {'Content-Type': 'application/x-www-form-urlencoded', 'X-XSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content},
               body: sessionData,
        });

    } catch (err) {
    	console.error("gfn_ssoLogout errorr:", err);
    }
}

const gfn_asyncSessionChk = async () => {
	let sendData = {method: 'GET'
                      , headers: {'Content-Type': 'application/json', 'X-XSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content} 
	                   }; 
	sendData.params = JSON.stringify({});
    const data =  await fetch('/sessionChk', sendData).then((result) =>{
								 return result.json();
							 });
	return data;
}


const gfn_asyncUrlCall = async (url,method,params,modalYn) => {
	try{
		let _modalYn = modalYn===undefined?false:modalYn;
		let _method = method===undefined?'POST':method;
		let sendData = {method: _method
                      , headers: {'Content-Type': 'application/json', 'X-XSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content} 
		              };
		
		if(_method === 'POST'){
			sendData.body = JSON.stringify(params);
		}
		else{
			sendData.params = params;
		}
		console.log("sendData:",sendData);
        /*
		if(!_modalYn){
			let sessionData = gfn_getStorage(ncos.storageKey);
			if(sessionData != null || sessionData != undefined){
				let curUrl = gfn_getStorageItem("curUrl");
				if(curUrl != undefined && curUrl != '' && url != curUrl){
					gfn_setStorageItem("preUrl",curUrl);
				}
				gfn_setStorageItem("curUrl",url);
			}
		}
		*/
		const result = await fetch(url, sendData);
		console.log("result:result");
		const data = await result.text();
		return data;
    } catch (err) {
    	console.error("gfn_asyncUrlCall errorr:", err);
    }
}

const gfn_schParam = (schParam) => {
	schParam.schMode = ncos.schMode;
	let schwrap = document.querySelector('.schwrap');
	if(schwrap){
		let inputs = schwrap.getElementsByTagName('input');
		for (let input  of inputs) {
		        if(input.id != null){
					if(input.type === 'text' || input.type === 'hidden' || input.type === 'datetime-local'){
						if(input.value.trim().length > 0) {
							schParam[input.id] = input.value.trim();
							if(input.type === 'datetime-local'){
								if(input.id === 'frDt'){
									schParam[input.id] = input.value.replace('T',' ')+':00';
								}else if(input.id === 'toDt'){
									schParam[input.id] = input.value.replace('T',' ')+':59';
								}
							}
							
						}
					}
		        }
		    
		}
		let multiselects = schwrap.querySelectorAll('.multiselect');
		multiselects.forEach(multiselect => {
			let checkboxes = multiselect.getElementsByTagName('input');
			let checkboxesId = multiselect.querySelector('.checkboxes').getAttribute('id');
			let chkArray = new Array();
			for (let checkbox  of checkboxes) {
				if(checkbox.checked){
					chkArray.push(checkbox.getAttribute('value'));
				}
			}
			if(chkArray.length > 0) schParam[checkboxesId] = chkArray;
			
		});
		
		let selects = schwrap.querySelectorAll('.select');
		selects.forEach(select => {
			let ulId = select.querySelector('ul').getAttribute('id');
			let selectedValue = select.querySelector('.selected-value');
			if(selectedValue.getAttribute('data-value').trim().length > 0) schParam[ulId] = selectedValue.getAttribute('data-value');
		});
			
	}
}


const gfn_codes =  async () => {
	try{
		if(localStorage.hasOwnProperty(ncos.codeKey)){
			return "";
		}
		const data = await fetch("/codes"
                               , {method: "GET"
		                        , headers: {'Content-Type': 'application/json', 'X-XSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content} // 'Content-Type':
						         }).then((result)=> {return result.json();
						         }).then((codes)=>{
									 codes.codes.push({grpCd:"useFlag",cd:1,cdNm:"사용"});
									 codes.codes.push({grpCd:"useFlag",cd:0,cdNm:"미사용"});
									 codes.codes.push({grpCd:"ACAT",cd:1,cdNm:"활성"});
									 codes.codes.push({grpCd:"ACAT",cd:0,cdNm:"비활성"});
									 codes.codes.push({grpCd:"WKRE",cd:1,cdNm:"성공"});
									 codes.codes.push({grpCd:"WKRE",cd:0,cdNm:"실패"});
									 codes.codes.push({grpCd:"STDE",cd:10,cdNm:"10"});
									 codes.codes.push({grpCd:"STDE",cd:20,cdNm:"20"});
									 codes.codes.push({grpCd:"STDE",cd:30,cdNm:"30"});
									 codes.codes.push({grpCd:"STDE",cd:40,cdNm:"40"});
									 codes.codes.push({grpCd:"STDE",cd:50,cdNm:"50"});
									 codes.codes.push({grpCd:"STDE",cd:60,cdNm:"60"});
									 return codes;
								 }); 
		gfn_setStorage(ncos.codeKey, CryptoJS.AES.encrypt(JSON.stringify(data), ncos.cryptKey));
    } catch (err) {
    	console.error("gfn_codes errorr:", err);
    }
}



const gfn_goBackUrl = () => {
	let params = {};
	let url = gfn_getStorageItem("preUrl");
	//console.log("gfn_goBackUrl preUrl:",url)
	let isMenu = false;
	const menuList = document.querySelector('.menu-list').querySelectorAll("a");
	if(menuList){
		menuList.forEach((menu) => {
		   const menuAnchHtml = menu.innerHTML;
		   if(menuAnchHtml.indexOf(url) > 0){
			   const menuAnch = menu.querySelector('a');
			   isMenu = true;
			   menuAnch.click();
			   
		   }
	    });
	}
	if(!isMenu){
		document.querySelector('.contentBody').innerHTML = '';
		gfn_asyncUrlCall(url,'GET',JSON.stringify(params)).then((data) => {
			dynimicContentCall(data);
		});
	}
}


/**
 * 
 * desc : valid msg innerhtml 제거
 */
const gfn_removeValidHtml = (frm,tag) => {
	try{
		let valids = frm.getElementsByTagName(tag);
	    for (let valid of valids) {
	        if(valid.id.indexOf('valid') > -1){
	        	valid.innerText = '';
	        }
	    }
	}catch(e){
		console.error("gfn_removeValidHtml error:",e);
	}

}

/**
 * 
 * desc : valid msg innerhtml 제거
 */
const gfn_valid = (comp,requiredParams,fileYn) => {
	let isValid = true;
	let _fileYn = (fileYn===undefined || fileYn === null)?false:fileYn;
	try{
	   //let data = gfn_formToJson(frm);
	   let data = gfn_genJsonParam(comp);
	   console.log("data:",data);
	   for(let key in requiredParams){
		   if(!data.hasOwnProperty(key) || data[key] == null || data[key].trim() === ''){  
			   if(comp != undefined){
				   let item = comp.querySelector('#'+key);
				   if(item.tagName === 'INPUT'){
				       let message = item.parentNode.querySelector('.message');
				       if(message){
						   item.parentNode.classList.add('error');
						   message.textContent = '필수 입력입니다';
					   }
					   else{
						   comp.querySelector('#'+key).style.borderColor = 'red'; 
					   }
				       
				   }
				   else if(item.tagName === 'UL'){
				       item.parentNode.querySelector('.selected').style.borderColor = 'red'; 
				   }
			   }else{
				   console.error("gfn_valid error: comp is null");
			   }
			   isValid = false;
		   }
	   }
	   if(_fileYn){
		   let existUploadFile = false;
		   for (var i = 0; i < filesArr.length; i++) {
		        if (!filesArr[i].isDelete) {
		        	existUploadFile = true;
		        	break;
		        } 
		   }
		   if(!existUploadFile){
			   document.querySelector('#valid_file').innerText = "파일을 추가하세요.";
			   isValid = false;
		   }
	   }
	  
	   return isValid;
	}catch(e){
		console.error("gfn_valid error:",e);
	}
}

const gfn_validJson = (comp,jsonParam,requiredParams,fileYn) => {
	let isValid = true;
	let _fileYn = (fileYn===undefined || fileYn === null)?false:fileYn;
	try{
	   for(let key in requiredParams){
		   if(!jsonParam.hasOwnProperty(key) || jsonParam[key] == null ||  jsonParam[key].trim() === ''){  
			   //console.log(key,"is required......",jsonParam[key]);
			   if(comp != undefined){
				   //console.log(comp.querySelector('#'+key).tagName);
				   let item = comp.querySelector('#'+key);
				   if(item.tagName === 'INPUT'){
				       //item.style.borderColor = 'red'; 
				       item.parentNode.classList.add('error');
				       item.parentNode.querySelector('.message').textContent = '필수 입력입니다';
				   }
				   else if(item.tagName === 'UL'){
				       item.parentNode.querySelector('.selected').style.borderColor = 'red'; 
				   }
			   }else{
				   console.error("gfn_validJson error: comp is null");
			   }
			   
			  
			   isValid = false;
		   }
	   }
	   if(_fileYn){
		   let existUploadFile = false;
		   for (var i = 0; i < filesArr.length; i++) {
		        if (!filesArr[i].isDelete) {
		        	existUploadFile = true;
		        	break;
		        } 
		   }
		   if(!existUploadFile){
			   //document.querySelector('#valid_file').innerText = "파일을 추가하세요.";
			   isValid = false;
		   }
	   }
	  
	   return isValid;
	}catch(e){
		console.error("gfn_validJson error:",e);
	}

}

const gfn_initObj = (obj,isValid) => {
	let _isValid = (isValid===undefined || isValid === null)?false:isValid;
	try{
		let inputs = obj.getElementsByTagName('input');
		for (let input of inputs) {
			
			if(input.type === 'radio'){
				
			}
			else if(input.type === 'datetime-local'){
				if(input.id === 'frDt') input.value = gfn_getDiffDt(ncos.diffDay);;
				if(input.id === 'toDt') input.value= gfn_getTodayDt();
			}
			else if(input.type === 'checkbox'){
				if(!_isValid){
					if(input.checked) input.checked = false;
				}

			}
			else {
				if(!_isValid){
					input.value = '';	
				}
				//console.log("input:",input.parentNode);
				if(input.parentNode.classList.contains('error')){
				   // console.log("error");
					input.parentNode.classList.remove('error');
					input.parentNode.querySelector('.message').textContent = '';
				}
				//input.style.borderColor = ''; 	
			}
			
		}
		let selectedValues = obj.querySelectorAll('.selected-value');
		if(selectedValues){
			selectedValues.forEach(selectedValue => {
				if(!_isValid){
					selectedValue.textContent = '선택';
             	    selectedValue.setAttribute('data-value','');
             	}
             	selectedValue.parentNode.style.borderColor = ''; 	
             	if(!_isValid){
	             	let ul = selectedValue.parentNode.parentNode.querySelector('ul');
	             	if(ul){
						 while(ul.hasChildNodes())
				    	 {
				    		ul.removeChild(ul.firstChild );       
				    	 }
					}
				}
				
			});
			
		}
		if(!_isValid){
			let multiChks = obj.querySelectorAll('.multiselect');
			if(multiChks){
				multiChks.forEach(multiChk => {
	             	let checkboxes = multiChk.querySelector('.checkboxes');
	             	if(checkboxes){
						 checkboxes.querySelectorAll('checkbox').forEach(chkbox  => {
							 if(chkbox.checked){
								 chkbox.checked = false;
							 }
						 });
						 /*
						 while(checkboxes.hasChildNodes())
				    	 {
				    		checkboxes.removeChild(checkboxes.firstChild );       
				    	 }
				    	 */
					}
					
				});
				
			}
		
			let selects = obj.getElementsByTagName('select');
			if(selects){
				for(let select of selects){
					select.querySelectorAll('option')[0].selected = true;
				}
			}
		}
		
	}catch(e){
		console.log("gfn_initObj:",e)
	}	
}

const gfn_genJsonParam = (obj) => {
	/*
	let params = {id:document.getElementById('scheduleId').value
     			         ,policyName:document.getElementById('policyName').value
     			         ,reportType:reportTypeSelectedValue.getAttribute('data-value')
     			         ,createHm:document.getElementById('createHm').value
     			         ,periodId:selectedValue.getAttribute('data-value')
     			         ,useFlag: document.getElementById('useFlag').checked?1:0};
     			         */
	let params = {};
	try{
		let inputs = obj.getElementsByTagName('input');
		for (let input of inputs) {
			if(input.type === 'radio'){
				
			}
			else if(input.type === 'checkbox'){
				if(input.checked){
					params[input.getAttribute('id')] = '1';
				}
				else{
					params[input.getAttribute('id')] = '0';
				}
			}
			else {
				params[input.getAttribute('id')] = input.value;
			}
			
		}
	
		let selectedValues = obj.querySelectorAll('.selected-value');
		if(selectedValues){
			selectedValues.forEach(selectedValue => {
				let ul = selectedValue.parentNode.parentNode.querySelector('ul');
				if(ul){
					params[ul.getAttribute('id')] = selectedValue.getAttribute('data-value');
				}else{
					let checkboxes = selectedValue.parentNode.parentNode.parentNode.querySelector('.checkboxes');
					let val = 0;
					let chkboxes = checkboxes.getElementsByTagName('input');
					for (let chkbox of chkboxes) {
						if(chkbox.type === 'checkbox' && chkbox.checked){
							val += parseInt(chkbox.value);
						}
					}
					params[checkboxes.getAttribute('id')] = val+'';
				}
			});
			
		}
		return params;
	}catch(e){
		console.log("gfn_genJsonParam:",e)
	}	
}



const gfn_isMobile = () => {
	let agentKind = "etc";
	let agent = navigator.userAgent;

	if (agent.indexOf("AppleWebKit") != -1 || agent.indexOf("Opera") != -1) {
		if (agent.indexOf("Android") != -1 || agent.indexOf("J2ME/MIDP") != -1) {
			agentKind = "android";
		} else if (agent.indexOf("iPhone") != -1) {
			agentKind = "iphone";
		} else if (agent.indexOf("iPad") != -1) {
			agentKind = "iphone";
		}
	} else {
		agentKind = "etc";
	}
	if (agentKind != "etc") {
		return true; // etc가 아닐경우, 모바일
	} else {
		return false;
	}
}


const gfn_getDate = () => {
	try{
		let date = new Date(); // 현재 날짜(로컬 기준) 가져오기
	    let utc = date.getTime() + (date.getTimezoneOffset() * 60 * 1000); // 
																			// 표준시
																			// 도출
	    let kstGap = 9 * 60 * 60 * 1000; // 한국 kst 기준시간 더하기
	    let today = new Date(utc + kstGap); // 현재 시간
	    return today;
	}catch(e){ console.error("# gfn_getDate error msg:",e);} 
	return true;
};

const gfn_getTodayDt = () => {
	try{
		let date = new Date(); // 현재 날짜(로컬 기준) 가져오기
	    let todayDt = date.getTime() - (date.getTimezoneOffset() * 60 * 1000); 
	    return new Date(todayDt).toISOString().slice(0, 16);
	}catch(e){ console.error("# gfn_getTodayDt error msg:",e);} 
	return true;
};

const gfn_getDiffDt = (day) => {
	try{
		let date = new Date();
		let diffDay = new Date();
		diffDay.setDate(date.getDate()+day);
		let diffDayDt = diffDay.getTime() - (diffDay.getTimezoneOffset() * 60 * 1000); 
		return new Date(diffDayDt).toISOString().slice(0, 16);
	}catch(e){ console.error("# gfn_getDiffDt error msg:",e);} 
	return true;
};


const gfn_getDay = (frmt) => {
	try{
		let _frmt = frmt===undefined?'.':frmt;
		let today = gfn_getDate();
		return today.getFullYear()+_frmt+(parseInt(today.getMonth())+1)+_frmt+today.getDate();
        
	}catch(e){ console.error("# gfn_getDay error msg:",e);} 
	return true;
};

const gfn_getDiffDay = (day,frmt) => {
	try{
		let _frmt = frmt===undefined?'.':frmt;
		let today = gfn_getDate();
		let diffDay = new Date();
		diffDay.setDate(today.getDate()+day);
		return diffDay.getFullYear()+_frmt+(parseInt(diffDay.getMonth())+1)+_frmt+diffDay.getDate();
	}catch(e){ console.error("# gfn_getDiffDay error msg:",e);} 
	return true;
};


const gfn_validDay = (sDay,eDay) => {
	let sDate = sDay.replace('T',' ').substring(0,19);
	let eDate = eDay.replace('T',' ').substring(0,19);
	sDate = new Date(sDate);
	eDate = new Date(eDate);

	try{
		let btMs = eDate.getTime() - sDate.getTime() ;
		let btDay = btMs / (1000*60*60*24) ;
	    if(btDay >= 0){
			
	    	return false;
	    }
	    else{
	    	return true;
	    }
    }catch(e){
		console.error("gfn_validDay error:",e);
	}
};


/**
 * Storage Item 조회
 * 
 * @return ncos Storage
 */
const gfn_getStorage = (key) => {
	let _key = key===undefined?ncos.storageKey:key;
	try{
		if(localStorage.hasOwnProperty(key)){
			return localStorage.getItem(_key);
		}
		else{
			console.log("ncos Storage is not found");
			return null;
		}
		
	}catch(e){ console.error("# gfn_getStorageItem error msg: ",e);} 
	
};

/**
 * Storage Item 조회
 * 
 * @return rtnVal
 */
const gfn_getStorageItem = (key,isClear) => {
	let isClr = isClear === undefined?true:isClear;
	try{
		let ncosStorage = gfn_getStorage(ncos.storageKey);
		if(ncosStorage){
			ncosStorage = JSON.parse(CryptoJS.AES.decrypt(ncosStorage,ncos.cryptKey).toString(CryptoJS.enc.Utf8));
			//console.log("ncosStorage:",ncosStorage);
			if(ncosStorage.hasOwnProperty(key)){
				return ncosStorage[key];
			}
			else{
				//console.log(key," is not exist in the ncos Storage!");
			}
			if(isClr) {
				if(ncosStorage.hasOwnProperty(key)){
					delete ncosStorage.key;
				}
				gfn_setStorage(ncos.storageKey,CryptoJS.AES.encrypt(JSON.stringify(ncosStorage),ncos.cryptKey));
			}
		}
		
	}catch(e){ console.error("# gfn_getStorageItem error msg: ",e);} 
	return '';
};

/**
 * Storage 조회
 * 
 * @return rtnVal
 */
const gfn_setStorage = (key,val) => {
	try{
		localStorage.setItem(key,val);
	}catch(e){ console.error("# gfn_getStorageItem error msg: ",e);} 
};

const gfn_getCodeVal = (grpCd,cd) => {

	try{
		let codeCache = gfn_getStorage(ncos.codeKey);
		if(codeCache){
			codeCache = JSON.parse(CryptoJS.AES.decrypt(codeCache,ncos.cryptKey).toString(CryptoJS.enc.Utf8))["codes"];
			//console.log("codeCache:",codeCache);
			for(let i=0;i<codeCache.length;i++){
				if(grpCd == codeCache[i]["grpCd"] && cd == codeCache[i]["cd"]){
					return codeCache[i]["cdNm"];
				}
			}
	    
			console.error("코드그룹 및 코드에 일치하는 데이타가 없습니다. grpCd:",grpCd,",cd:",cd);
			return cd;
		}
		else {
			return '';
		}
	}catch(e){ console.error("# gfn_getCodeVal error msg: ",e);} 
};

const gfn_getCodeGrp = (grpCd) => {
	let grpCode = new Array();
	try{
		
		const codeCache = JSON.parse(CryptoJS.AES.decrypt(gfn_getStorage(ncos.codeKey),ncos.cryptKey).toString(CryptoJS.enc.Utf8))["codes"];
		if(grpCd === 'COUT'){
			console.log("codeCache:",codeCache);
		}
		//console.log("codeCache:",codeCache);
		for(let i=0;i<codeCache.length;i++){
			
			if(grpCd == codeCache[i]["grpCd"]){
				//console.log("codeGrp:",codeCache[i]["grpCd"],"grpCd:",grpCd);
				grpCode.push(codeCache[i]);
				
			}
		}
		return grpCode;
	}catch(e){ console.error("# gfn_getCodeGrp error msg: ",e);} 
};

const gfn_setCodeGrpObj = (grpCd,comp,select,filter) => {
	if(select){
		let option = document.createElement('option');
		option.append('선택하세요');
		option.setAttribute('value','');
		comp.appendChild(option);
	}
	try{
		const codeCache = JSON.parse(gfn_getStorage(ncos.codeKey))["codes"];
		for(let i=0;i<codeCache.length;i++){
			if(grpCd === codeCache[i]["grpCd"]){
				if(filter && filter === codeCache[i]["cdFilter"]){
					let option = document.createElement('option');
					option.setAttribute('value',codeCache[i]["cd"]);
					option.append(codeCache[i]["cdNm"]);
					comp.appendChild(option);
				}
				if(!filter){
					let option = document.createElement('option');
					option.setAttribute('value',codeCache[i]["cd"]);
					option.append(codeCache[i]["cdNm"]);
					comp.appendChild(option);
				}
				
			}
		}
	}catch(e){ console.error("# gfn_getCodeVal error msg: ",e);} 
};



/**
 * Storage Item 조회
 * 
 * @return rtnVal
 */
const gfn_setStorageItem = (key,val) => {
	try{
		let ncosStorage = JSON.parse(CryptoJS.AES.decrypt(gfn_getStorage(ncos.storageKey),ncos.cryptKey).toString(CryptoJS.enc.Utf8));
		if(ncosStorage.hasOwnProperty(key)){
			console.warn(key," is already exist in the ncos Storage!");
		}
		ncosStorage[key] = val;
		gfn_setStorage(ncos.storageKey,CryptoJS.AES.encrypt(JSON.stringify(ncosStorage),ncos.cryptKey));

	}catch(e){ console.error("# gfn_getStorageItem error msg: ",e);} 
};


/**
 * ncos StorageItem 삭제
 * 
 * @param key
 * @return sessionStorage
 */
const gfn_removeStorageItem = (key) => {
	try{
	
		let ncosStorage = JSON.parse(CryptoJS.AES.decrypt(gfn_getStorage(ncos.storageKey),ncos.cryptKey).toString(CryptoJS.enc.Utf8));
		if(ncosStorage.hasOwnProperty(key)){
			delete ncosStorage.key;
			gfn_setStorage(ncos.storageKey,CryptoJS.AES.encrypt(JSON.stringify(ncosStorage),ncos.cryptKey));
		}
		else{
			console.error(key,"is not exist in the ncos Storage!");
		}
		
	}catch(e){ console.error("# gfn_removeStorageItem error msg: ",e);} 
	return sessionStorage;
};

/**
 * ncos Storage 삭제
 * 
 * @param key
 * @return ncos Storage
 */
const gfn_removeStorage = (key) => {
	try{
		if(localStorage.hasOwnProperty(key)){
			localStorage.removeItem(key);
		}
		else{
			console.error(key,"is not exist in localStorage!");
		}
	}catch(e){ console.error("# gfn_removeStorage error msg: ",e);} 
	return sessionStorage;
};
/**
 * ncos Storage 청소
 * 
 * @return boolean
 */
const gfn_clearStorage = () => {
	try{
		localStorage.removeItem(ncos.storageKey);
		localStorage.removeItem(ncos.codeKey);
	}catch(e){ console.error("# gfn_clearStorage error msg:",e);} 
	return true;
};


const gfn_dateFrmt = (timestamp,frmt) => {
	if(gfn_nullValue(timestamp) === ''){
		return '';
	}
	
	let date = new Date(timestamp);
	
	if(frmt.indexOf('yy') > -1){
		frmt = frmt.replace('yy',date.getFullYear());
	}
	
    if(frmt.indexOf('mm') > -1){
    	frmt = frmt.replace('mm',(date.getMonth()+1)<10?"0".concat(date.getMonth()+1):date.getMonth()+1);
	}
    
    if(frmt.indexOf('dd') > -1){
    	frmt = frmt.replace('dd',(date.getDate())<10?"0".concat(date.getDate()):date.getDate());
	}
    
    if(frmt.indexOf('hh') > -1){
    	frmt = frmt.replace('hh',(date.getHours())<10?"0".concat(date.getHours()):date.getHours());
	}
    
    if(frmt.indexOf('mi') > -1){
    	frmt = frmt.replace('mi',(date.getMinutes())<10?"0".concat(date.getMinutes()):date.getMinutes());
	}
    
    if(frmt.indexOf('ss') > -1){
    	frmt = frmt.replace('ss',(date.getSeconds())<10?"0".concat(date.getSeconds()):date.getSeconds());
	}
    return frmt;
	
    
}

const gfn_dateTmFrmt = (timestamp,frmt) => {
	const regExp = /[-]/g;
	let dateVal = timestamp.split("T")[0];
	if(frmt === undefined || frmt ===''){
		return timestamp.split("T")[0];
	}
	else if(frmt === '-'){
		return timestamp.split("T")[0];
	}else if(frmt === '.'){
		if (regExp.test(dateVal)) {
			dateVal = dateVal.replace(regExp, '.');
		}
		return dateVal;
		
	}else if(frmt === '/'){
		if (regExp.test(dateVal)) {
			dateVal = dateVal.replace(regExp, '/');
		}
		return dateVal;
	}
}

const gfn_nullValue = (val) => {
	if(val === undefined || val === null || val === 'undefined'){
		return '';
	}
	return val;
}


const fadeIn = (target) => {
	target.style.opacity = 0;
	target.style.display = "block";
	let level = 0;
	let inTimer = null;
	inTimer = setInterval( () => {
		level = fadeInAction(target,level,inTimer);
	},50);
}

const fadeInAction = (target,level,inTimer) => {
	level += 0.1;
	changeOpacity(target,level);
	if(level>1) {
		clearInterval(inTimer);
	}
	return level;
}

const fadeOut = (target) => {
	let level = 1;
	let outTimer = null;
	outTimer = setInterval( () => {
		level = fadeOutAction(target,level,outTimer);
	},50);
}

const fadeOutAction = (target,level,outTimer) => {
	level -= 0.1;
	changeOpacity(target,level);
	if(level<0){
		target.style.display = "none";
		clearInterval(outTimer);
	}
	return level;
}

const changeOpacity = (target,level) => {
	let obj = target;
	obj.style.opacity = level;
	obj.style.MozOpacity = level;
	obj.style.KhtmOpacity = level;
	obj.style.MsFilter = "'progid: DXIMageTransform.Microsoft.Alpha(Opacity="+(level * 1) + ")'";
	obj.style.filter = "alpha(opacity="+(level * 100) + ");";
}

const showLayer = (el,x,y)=> {
	document.querySelector(el).style.top = y+'px';
	document.querySelector(el).style.left = x+'px';
	document.querySelector(el).style.display = 'block';
	document.querySelector(el).querySelector('.icon-close').addEventListener('click',() => {
		document.querySelector(el).style.display = 'none';
        
	});
}

const showMsgLayer = (el)=> {
	console.log("el:",el);
	el.style.bottom = 0;
	el.style.right = 0;
	el.style.display = 'block';
	el.querySelector('.icon-close').addEventListener('click',() => {
		el.remove();
        
	});
}

const msgCall = (msg,isSuccess,isCancel,callBackFn) => {
	console.log("dim:",document.querySelector('#messageBox').parentNode);
	document.querySelector('#messageBox').parentNode.style.display = 'block';
	let _isCancel = (isCancel===undefined || isCancel === null)?false:isCancel;
    if(_isCancel){
		document.querySelector('#messageBox').querySelector('.cancelBtn').style.display = '';
	}
	document.querySelector('#messageBox').querySelector('.messageContent').innerHTML = '';
	document.querySelector('#messageBox').querySelector('.messageContent').innerHTML = msg;
	if(isSuccess){
		document.querySelector('#messageBox').querySelector('.messageContent').classList.add('success');
	}
	else{
		document.querySelector('#messageBox').querySelector('.messageContent').classList.add('fail');
	}
	let elWidth = document.querySelector('#messageBox').offsetWidth,
	    elHeight = document.querySelector('#messageBox').offsetHeight,
	    docWidth = document.body.clientWidth,
	    docHeight = document.body.clientHeight;
	
	if (elHeight < docHeight || elWidth < docWidth) {
		document.querySelector('#messageBox').style.top = (docHeight/2 - elHeight/2)+'px';
		document.querySelector('#messageBox').style.left = (docWidth/2 - elWidth/2) + 'px' ;
	} else {
		document.querySelector('#messageBox').style.top = 0;
		document.querySelector('#messageBox').style.left = 0;
	}
/*	document.querySelector('#messageBox').style.top = y+'px';
	document.querySelector('#messageBox').style.left = x+'px';*/
	document.querySelector('#messageBox').style.display = 'block';
	document.querySelector('#messageBox').querySelector('.icon-close').addEventListener('click',() => {
		document.querySelector('#messageBox').parentNode.style.display = 'none';
		document.querySelector('#messageBox').style.display = 'none';
		document.querySelector('#messageBox').querySelector('.cancelBtn').style.display = 'none';
		if(document.querySelector('#messageBox').querySelector('.messageContent').classList.contains('success')){
			document.querySelector('#messageBox').querySelector('.messageContent').classList.remove('success');
		}
		if(document.querySelector('#messageBox').querySelector('.messageContent').classList.contains('fail')){
			document.querySelector('#messageBox').querySelector('.messageContent').classList.remove('fail');
		}
        
	});       	
	document.querySelector('.confirmBtn').addEventListener('click',e => {
		e.preventDefault();
		document.querySelector('#messageBox').parentNode.style.display = 'none';
		document.querySelector('#messageBox').style.display = 'none';
		document.querySelector('#messageBox').querySelector('.cancelBtn').style.display = 'none';
		if(document.querySelector('#messageBox').querySelector('.messageContent').classList.contains('success')){
			document.querySelector('#messageBox').querySelector('.messageContent').classList.remove('success');
		}
		if(document.querySelector('#messageBox').querySelector('.messageContent').classList.contains('fail')){
			document.querySelector('#messageBox').querySelector('.messageContent').classList.remove('fail');
		}
		if(callBackFn != undefined){
			callBackFn();
		}
	});
	
		
		//_callBackFn = callBackFn;
	
}

const layerPopup = (el,x,y) => {
    try{
	let isDim = document.querySelector(el).previousElementSibling.classList.contains('dimBg');   	
	isDim? fadeIn(document.querySelector(el).parentElement):fadeIn(document.querySelector(el));
	let elWidth = document.querySelector(el).offsetWidth,
	    elHeight = document.querySelector(el).offsetHeight,
	    docWidth = document.body.clientWidth,
	    docHeight = document.body.clientHeight;
	if(el.startsWith('#notice')){
	    document.querySelector(el).style.bottom = 0;
		document.querySelector(el).style.right = 0;
	}
	else if(el.startsWith('#testscenario')){
		document.querySelector(el).style.top = y+'px';
		document.querySelector(el).style.left = x+'px';
	}
	else{
		if (elHeight < docHeight || elWidth < docWidth) {
			document.querySelector(el).style.top = (docHeight/2 - elHeight/2)+'px';
		    document.querySelector(el).style.left = (docWidth/2 - elWidth/2) + 'px' ;
		} else {
			document.querySelector(el).style.top = 0;
			document.querySelector(el).style.left = 0;
		}
	}

	document.querySelector(el).parentElement.querySelector('.icon-close').addEventListener('click',(e) => {
		e.preventDefault();
		try{
			if(el == '#notice1'){
				
			}
			else if(el == '#notice2'){
			}
			else if(el == '#notice3'){
				confirmNotice3();
			}
			isDim ? fadeOut(document.querySelector(el).parentElement) : fadeOut(document.querySelector(el)); // 닫기 버튼을 클릭하면 레이어가 닫힌다.
			let tBody = document.querySelector('.tBody');
			if(tBody){
				let trs = tBody.querySelectorAll('tr');
				trs.forEach((tr) => {
					if(tr.classList.contains('select')){
						tr.classList.remove('select');
					}
				});
			}
		}
		catch(e){
			console.error("icon-close error:",e);
		}
	    return false;
	});
	
	let popClose = document.querySelector(el).parentElement.querySelector('.popClose');
	if(popClose){
		popClose.addEventListener('click',() => {
			isDim ? fadeOut(document.querySelector(el).parentElement) : fadeOut(document.querySelector(el)); // 닫기 버튼을 클릭하면 레이어가 닫힌다.
			let tBody = document.querySelector('.tBody');
		    if(tBody){
				let trs = tBody.querySelectorAll('tr');
				trs.forEach((tr) => {
					if(tr.classList.contains('select')){
						tr.classList.remove('select');
					}
				});
			}
			
		    return false;
		});
	}
	}catch(e){
		console.error("e:",e);
	}
}

const showCheckboxes = (selectBox) => {
	let checkboxes = selectBox.parentNode.querySelector(".checkboxes");
    if (checkboxes.style.display === 'none') {
    	checkboxes.style.display = "block";
    } else {
        checkboxes.style.display = "none";
    }
}

function toggleSelectBox(selectBox) {
	selectBox.classList.toggle("active");
}

function selectOption(optionElement) {
	console.log("selectOption");
	const selectBox = optionElement.closest(".select");
    const selectedElement = selectBox.querySelector(".selected-value");
    selectedElement.textContent = optionElement.textContent;
    selectedElement.setAttribute("data-value",optionElement.getAttribute("data-value"));
}


document.addEventListener("click", function (e) {
	try{
		const targetElement = e.target;
		let testscenario = document.querySelector('#testscenario');
		if(testscenario){ 
			if(targetElement === testscenario.querySelector('.icon-close') ||  targetElement.parentElement.classList.contains('ship-layer')){
				return;
			}
			else{
				if(testscenario.style.display === 'block'){
					testscenario.style.display = 'none';
				}
			}
		}
	    const isSelect = targetElement.classList.contains("select") || targetElement.closest(".select");
	    if (isSelect) {
			return;
	    }
	    const isMultiSelect = targetElement.classList.contains("multiselect") || targetElement.closest(".multiselect");
	    if (isMultiSelect) {
			return;
	    }
	    const allSelectBoxElements = document.querySelectorAll(".select");
	    if(allSelectBoxElements){
			allSelectBoxElements.forEach(boxElement => {
				boxElement.classList.remove("active");
		    });
		}
	
		const allMultiSelectBoxElements = document.querySelectorAll(".multiselect");
		if(allMultiSelectBoxElements){
			allMultiSelectBoxElements.forEach(boxElement => {
				let checkboxes = boxElement.querySelector(".checkboxes");
				if(checkboxes.style.display === 'block') {
					showCheckboxes(checkboxes);
			    } 
		    });
		}
	}catch(e){
		console.error("e:",e);
	}
    
});

const gfn_showChkVal = (obj) => {
    if(obj === undefined){
		document.querySelectorAll('.checkboxes').forEach(checkbox => {	
			let selectedValue = checkbox.parentNode.querySelector('.selected-value');
			let inputs = checkbox.getElementsByTagName('input');
			for (let input of inputs) {
				if(input.type === 'checkbox'){
					input.addEventListener('click',(e)=> {
						let labels = e.target.parentNode.parentNode.querySelectorAll('label');
						let selectedVal = '';
						labels.forEach(label => {
							let chk = label.childNodes[0];
							if(chk.checked){
								selectedVal += chk.parentNode.textContent+', ';
							}
						})
						let selectedText = selectedVal.trim();
						selectedValue.textContent = selectedText;
						if(selectedText.lastIndexOf(',') === selectedText.length-1){
							selectedValue.textContent = selectedText.substring(0,selectedText.length-1);
						}
						if(selectedValue.textContent === '')selectedValue.textContent = '전체';
					});
				}
			}
		});
	}
	else{
		let selectedValue = obj.parentNode.querySelector('.selected-value');
		let inputs = obj.getElementsByTagName('input');
		for (let input of inputs) {
			if(input.type === 'checkbox'){
				input.addEventListener('click',(e)=> {
					let labels = e.target.parentNode.parentNode.querySelectorAll('label');
					let selectedVal = '';
					labels.forEach(label => {
						let chk = label.childNodes[0];
						if(chk.checked){
							selectedVal += chk.parentNode.textContent+', ';
						}
					})
					let selectedText = selectedVal.trim();
					selectedValue.textContent = selectedText;
					if(selectedText.lastIndexOf(',') === selectedText.length-1){
						selectedValue.textContent = selectedText.substring(0,selectedText.length-1);
					}
					if(selectedValue.textContent === '')selectedValue.textContent = '전체';
				});
			}
		}
	}
	
}

const gfn_getCodeZone = async() => {
	const data = await fetch("/zoneList"
                           , {method: "GET"
	                        , headers: {'Content-Type': 'application/json', 'X-XSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content} 
					         }).then((result) =>{
								 return result.json();
							 }).catch((error) => {
								 console.error("gfn_getCodeZone errorr:",error);
							 });
	return data;
}

const gfn_getCodeManager = async() => {
	const data = await fetch("/managerList"
                           , {method: "GET"
	                        , headers: {'Content-Type': 'application/json', 'X-XSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content} 
					         }).then((result) =>{
								 return result.json();
							 }).catch((error) => {
								 console.error("gfn_getCodeManager errorr:",error);
							 });
	return data;
}

const createMultiChk = (comp,grpCd,codeData) => {
	let cds = null;
	let html = '';
	if(grpCd === null){
		cds = codeData;
	}
	else if(grpCd === 'ZNLS'){
		cds = codeData;
	}
	else if(grpCd === 'AD'){
		cds = codeData;
	}
	else{
		cds = gfn_getCodeGrp(grpCd);
		
	}
	cds.forEach((cd,index) => {
		//html += '<label for="'+grpCd+'-'+index+'">';
		html += '<label>';
		html += '<input type="checkbox" id="'+grpCd+'-'+index+'"  value="'+cd.cd+'">';
		html += cd.cdNm+'</label>';
	
	});
	comp.innerHTML = html;
}

const createChk = (comp,grpCd,optionLable,codeData) => {
	let cds = null;
	let html = '';
	if(grpCd === 'ZNLS'){
		cds = codeData;
	}else{
		cds = gfn_getCodeGrp(grpCd);
		if(grpCd === 'COUT'){
			console.log("cds:",cds);
		}
		
	}
	if(optionLable != null){
		html += '<li class="option" data-value="">';
		html += optionLable+'</li>';
	}
	cds.forEach((cd) => {
		html += '<li class="option" data-value="'+cd.cd+'">';
		html += cd.cdNm+'</li>';
	});
	
	comp.innerHTML = html;
	
	
}

const setCheckValue = (lis,selectedValue,data) => {
	lis.forEach((li) => {
		if(li.getAttribute('data-value') == data){
			selectedValue.textContent = li.textContent;
        	selectedValue.setAttribute('data-value',data);
        	return false;
		}
	});
}

const setMultiImpCheckValue = (multiObj,labels,data) => {
	let selectedValue = multiObj.parentNode.querySelector('.selected-value');
	labels.forEach((label) => {
		let checkbox = label.querySelector('#'+label.getAttribute('for'));
		if(data === '9'){
			checkbox.checked = true;
			selectedValue.textContent = '하, 중, 상';
		}
		else if(data === '8'){
			selectedValue.textContent = '중, 상';
			if(checkbox.value === '3' || checkbox.value === '5'){
				checkbox.checked = true;
			}
		}
		else if(data === '6'){
			selectedValue.textContent = '하, 상';
			if(checkbox.value === '1' || checkbox.value === '5'){
				checkbox.checked = true;
			}
		}
		else if(data === '5'){
			selectedValue.textContent = '상';
			if(checkbox.value === '5'){
				checkbox.checked = true;
			}
		}
		else if(data === '4'){
			selectedValue.textContent = '하, 중';
			if(checkbox.value === '1' || checkbox.value === '3'){
				checkbox.checked = true;
			}
		}
		else if(data === '3'){
			selectedValue.textContent = '중';
			if(checkbox.value === '3'){
				checkbox.checked = true;
			}
		}
		else if(data === '1'){
			selectedValue.textContent ='하';
			if(checkbox.value === '1'){
				checkbox.checked = true;
			}
		}

	});
}
/* SW접근관리 주기적 호출  */
const gfn_actTouch = async() => {
	let el = document.getElementById("actTouch");
	await fetch(  "/main_touch.jsp"
                , {method: "POST"
	                        , headers: {'Content-Type': 'text/html'} 
					         }).then((result) =>{
								 return result.text()
					         }).then((string) =>{
								 el.innerHTML = string;
							 });
	//fet로 받은 html과 script가 실행안되 추가.
	var scripts = document.getElementById("actTouch").querySelectorAll("script");
	for (var i = 0; i < scripts.length; i++) {
		if (scripts[i].innerText) {
			eval(scripts[i].innerText);
		} else {
			fetch(scripts[i].src).then(function (data) {
				data.text().then(function (r) {
					eval(r);
        		})
      		});
		}			
    // To not repeat the element				 
	scripts[i].parentNode.removeChild(scripts[i]);						 
	}
}
