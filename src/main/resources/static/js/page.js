'use strict'

const page = {
		rowPerPage:10
	  , halfRowPerPage:5
	  , halfDoubleRowPerPage:15
	  , doubleRowPerPage:20
	
}

/*
<a class="arrow pprev" href="#"></a> <a class="arrow prev" href="#"></a> 
							<a href="#" class="active">1</a> <a href="#">2</a> <a href="#">3</a> <a href="#">4</a> <a href="#">5</a> 
							<a href="#">6</a> <a href="#">7</a> <a href="#">8</a> <a href="#">9</a> <a href="#">10</a> 
							<a class="arrow next" href="#"></a> <a class="arrow nnext" href="#"></a> 
*/

const pageInit = (comp,pageFunc,perPage) => {
	const compPageNum = document.createElement('INPUT'); 
	compPageNum.setAttribute("type","hidden");
	compPageNum.setAttribute("name","compPageNum");
	compPageNum.classList.add("compPageNum");
	compPageNum.setAttribute("value",1);
	
	comp.appendChild(compPageNum);
	
	const pprev = document.createElement('a');
	pprev.classList.add("arrow","pprev","hidden");
	pprev.setAttribute("title", "이전");
	pprev.setAttribute("href", "");
	pprev.addEventListener('click', (e) => {
		e.preventDefault();
		pprevFunc(comp);
		if(typeof pageFunc === 'function'){
			pageFunc();
		}
		else{
			new Function(pageFunc)();
		}
	});
	
	const prev = document.createElement('a');
	prev.classList.add("arrow","prev","hidden");
	prev.setAttribute("title", "이전");
	prev.setAttribute("href", "");
	prev.addEventListener('click', (e) => {
		e.preventDefault();
		prevFunc(comp);
		if(typeof pageFunc === 'function'){
			pageFunc();
		}
		else{
			new Function(pageFunc)();
		}
	});
	
	const next = document.createElement('a');
	next.classList.add("arrow","next","hidden");
	next.setAttribute("title", "이후");
	next.setAttribute("href", "");
	next.addEventListener('click', (e) => {
		e.preventDefault();
		nextFunc(comp);
		if(typeof pageFunc === 'function'){
			pageFunc();
		}
		else{
			new Function(pageFunc)();
		}		
	});
	
	const nnext = document.createElement('a');
	nnext.classList.add("arrow","nnext","hidden");
	nnext.setAttribute("title", "이후");
	nnext.setAttribute("href", "");
	nnext.addEventListener('click', (e) => {
		e.preventDefault();
		nnextFunc(comp);
		if(typeof pageFunc === 'function'){
			pageFunc();
		}
		else{
			new Function(pageFunc)();
		}		
	});
	comp.appendChild(pprev);
	comp.appendChild(prev);
	comp.appendChild(next);
	comp.appendChild(nnext);
}

const pageBind = (comp,pageInfo,pageFunc) => {
	const pprev = comp.querySelector(".pprev");
	const prev = comp.querySelector(".prev");
	const next = comp.querySelector(".next");
	const nnext = comp.querySelector(".nnext");
	
	//let perPage = comp.querySelector('.rowPerPage').getAttribute('value');
	let pageGrp = Math.ceil(pageInfo.pageNo/10);
	
	let last = pageGrp * 10;
    
	let first = last - (10 - 1) <= 0 ? 1 : last - (10 - 1);
	
	if (last > pageInfo.totPage) last = pageInfo.totPage;
	/*
	console.log("first:",first);
	console.log("last:",last);
    */
    const pageIdxs = document.querySelectorAll('.pageIdx');
	if(pageIdxs){
		pageIdxs.forEach((pageIdx) => {
			comp.removeChild(pageIdx);
		})
	}
	
	for(let i=first;i<=last;i++){
		const pageIdx = document.createElement('a');
	    pageIdx.classList.add("pageIdx");
	    pageIdx.setAttribute("title", i);
	    pageIdx.append(i);
	    if(i === pageInfo.pageNo){
			pageIdx.classList.add("active");
		}
		else{
			pageIdx.setAttribute("href", "");
			pageIdx.addEventListener('click', (e) => {
				e.preventDefault();
				ncos.initBtn = false;
				const pageNum = comp.querySelector('.compPageNum'); 
	            pageNum.setAttribute("value",parseInt(i));
				if(typeof pageFunc === 'function'){
					pageFunc();
				}
				else{
					new Function(pageFunc)();
				}
			});
		}
		
		comp.insertBefore(pageIdx,next);
	}
	
	

	if(pageInfo.totCount === 0){
		if(!pprev.classList.contains("hidden")){
			pprev.classList.add("hidden");
		}
		if(!prev.classList.contains("hidden")){
			prev.classList.add("hidden");
		}
		if(!next.classList.contains("hidden")){
			next.classList.add("hidden");
		}
		if(!nnext.classList.contains("hidden")){
			nnext.classList.add("hidden");
		}
	}
	else{
		if(pageInfo.isFirstPerPage){
			if(pprev.classList.contains("hidden")){
				pprev.classList.remove("hidden");
			}		
		}
		else{
			if(!pprev.classList.contains("hidden")){
				pprev.classList.add("hidden");
			}		
		}
		if(pageInfo.isFirstPage){
			if(!prev.classList.contains("hidden")){
				prev.classList.add("hidden");
			}		
		}
		else{
			if(prev.classList.contains("hidden")){
				prev.classList.remove("hidden");
			}		
		}
		if(pageInfo.isLastPage){
			if(!next.classList.contains("hidden")){
				next.classList.add("hidden");
			}		
		}
		else{
			if(next.classList.contains("hidden")){
				next.classList.remove("hidden");
			}		
		}
		if(pageInfo.totPage != last){
			if(nnext.classList.contains("hidden")){
				nnext.classList.remove("hidden");
			}		
		}
		else{
			if(!nnext.classList.contains("hidden")){
				nnext.classList.add("hidden");
			}		
		}
	}
}

const prevFunc = (comp) => {
	ncos.initBtn = false;
	const pageNum = comp.querySelector('.compPageNum'); 
    pageNum.setAttribute("value",parseInt(pageNum.getAttribute("value"))-1);
}

const nextFunc = (comp) => {
	ncos.initBtn = false;
	const pageNum = comp.querySelector('.compPageNum'); 
    pageNum.setAttribute("value",parseInt(pageNum.getAttribute("value"))+1);
}

const pprevFunc = (comp) => {
	//console.log("pprevFunc");
	ncos.initBtn = false;
	const pageNum = comp.querySelector('.compPageNum'); 
    //pageNum.setAttribute("value",parseInt(pageNum.getAttribute("value"))-1);
    pageNum.setAttribute("value",parseInt(pageNum.getAttribute("value")/10)*10-10+1);
}

const nnextFunc = (comp) => {
	const pageNum = comp.querySelector('.compPageNum'); 
	//console.log("curPage:",pageNum.getAttribute("value"));
	//console.log("나머지:",parseInt(pageNum.getAttribute("value"))%10);
	//console.log("몫:",parseInt(pageNum.getAttribute("value")/10));

    pageNum.setAttribute("value",parseInt(pageNum.getAttribute("value")/10)*10+10+1);
}

const pageSearch = (url,gridComp,pageComp,invoker,pageFunc,callBackFn) => {
	if(ncos.schMode != 'auto' && document.getElementById('frDt') && gfn_validDay(document.getElementById('frDt').value,document.getElementById('toDt').value)){
		msgCall(msg.invalidDay,false,false,() => {
			document.getElementById('frDt').focus();
		});
	}
	const compPageNum = pageComp.querySelector('.compPageNum'); 
	if(invoker != undefined && invoker != null){
		compPageNum.setAttribute("value",1);
	}
	let rowPerPage = 10;
	if(pageComp.querySelector(".rowPerPage")){
		rowPerPage = parseInt(pageComp.querySelector(".rowPerPage").getAttribute("value"));
	}
	let param = { "pageNo" : compPageNum.getAttribute("value")
	            , rowPerPage
    			};
	gfn_asyncJsonCall(url,'POST',param).then((data) => {
		pageGridSet(data,gridComp,pageComp,pageFunc,callBackFn);
	}); 
}

const pageGridSet = (data,gridComp,pageComp,pageFunc,callBackFn) => {
	let list = null;
	let appendData = {};
	let pageInfo = null;
	for(let key in data) {
		if(key.indexOf('success_msg') > -1){
			
		}
		if(key.indexOf('fail_msg') > -1){
			msgCall(data[key],false);
			break;
		}
		if(key.indexOf("list") > -1){
			list = data[key];
 		}
		if(key.indexOf("appendData") > -1){
			appendData = data[key];
 		}
        if(key.indexOf('pageInfo') > -1){
          	pageInfo = data[key];
  		}
      
	}
	
	gridBind(list,gridComp);
	pageBind(pageComp,pageInfo,pageFunc);
	if(callBackFn != undefined){
		callBackFn(appendData);
	}
}

