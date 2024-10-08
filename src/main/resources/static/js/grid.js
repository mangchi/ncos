'use strict'

const grid = {
		
}

let _gridClass = "";

const gridInit = (comp,headColumns,gridHeight,gridClass) => {
	let _comp = comp === null?document.querySelector('.tablewrap'):comp;
	let thead = _comp.querySelector('.tHead');
	while(thead.hasChildNodes())
	{
		thead.removeChild( thead.firstChild );       
	}
	let trHead = document.createElement("TR");
	thead.appendChild(trHead);
	if(headColumns != undefined && headColumns != null){
		for(let i=0;i<headColumns.length;i++){
			let thCol = document.createElement("TH");
			for(let key in headColumns[i]){
				//thCol.classList.add("headCol");
				if(key === 'data_id'){
					thCol.setAttribute('data-id',headColumns[i][key]);	
					if(headColumns[i][key] === 'chk'){
						let chkbox = document.createElement("INPUT");
						chkbox.setAttribute("type","checkbox");
						chkbox.setAttribute("name","chkAll");
						thCol.appendChild(chkbox);
						chkbox.addEventListener('click',(e) => {
							const tBody = _comp.querySelector('.tBody');
							for(let row of tBody.children){
								const tds = row.querySelectorAll('td');
								tds.forEach((td,index) => {
									if(i === index){
										td.children[0].checked = e.target.checked;
									}
								});
							}
						});
					}
						
				}
				if(key === 'label'){
					thCol.append(headColumns[i][key]);
				}
				if(key === 'width'){
					if(headColumns[i][key] == '0px'){
						thCol.style.display = 'none';
					}
				}
				if(key === 'data_dateFrmt'){
					thCol.setAttribute('data-dateFrmt',headColumns[i][key]);
				}
				if(key === 'data_class'){
					thCol.setAttribute('data-class',headColumns[i][key]);
				}
				if(key === 'data_btnNm'){
					thCol.setAttribute('data-btnNm',headColumns[i][key]);
				}
				if(key === 'btnSpan'){
					thCol.classList.add('btnSpan');
				}
				if(key === 'data_grpCd'){
					thCol.setAttribute('data-grpCd',headColumns[i][key]);
				}
				if(key === 'data_type'){
					thCol.setAttribute('data-type',headColumns[i][key]);
				}
				/*if(key === 'row_span'){
					thCol.setAttribute('row-span',headColumns[i][key]);
				}
				if(key === 'col_span'){
					thCol.setAttribute('col-span',headColumns[i][key]);
				}*/
					
			}	
			trHead.appendChild(thCol);
		}
	}
}	

const gridBind = (list,gridComp) => {
	try{
	let tBody = gridComp.querySelector('.tBody');
	while(tBody.hasChildNodes())
	{
		tBody.removeChild( tBody.firstChild );       
	}
	let modBtn = document.querySelector('.modBtn');
	let popModBtn = document.querySelector('.popModBtn');
	for(let key in list){
		let item = list[key];
		let thCols = gridComp.querySelector('.tHead').querySelectorAll("th");
		let tr = document.createElement('TR');
		if(modBtn || popModBtn){
			tr.addEventListener('click', (e) =>{ 
				console.log("target:",e.target);
				let trs = tBody.querySelectorAll('tr');
				trs.forEach((trItm) => {
					if(trItm === e.target || trItm === e.target.parentNode || trItm === e.target.parentNode.parentNode){
						if(!trItm.classList.contains('trSelect')){
							trItm.classList.add('trSelect');
						}
		
					}
					else{
						if(trItm.classList.contains('trSelect')){
							trItm.classList.remove('trSelect');
						}
					}
					
				});
				if(document.getElementById('assetdisplay')){
					selectTr();
				}
				
			});
		}
		
		thCols.forEach((thCol,thIdx) => {
			let td = document.createElement('TD');
    		if(thCol.getAttribute("data-id") === 'chk'){
				let chkbox = document.createElement('input');
				chkbox.setAttribute("type", "checkbox");
				td.appendChild(chkbox);
  				chkbox.addEventListener('click', (e) =>{ 
   					if(e.target.checked){
					    let checkCnt = 0;
					    for(let row of tBody.children){
							const tds = row.querySelectorAll('td');
							tds.forEach((td,index) => {
								if(thIdx === index){
									if(td.children[0].checked){
										checkCnt += 1;
									}
								}
							});
						}
						if(tBody.children.length === checkCnt){
							thCol.children[0].checked = e.target.checked;
						}
					}else{
						if(thCol.children[0].checked){
							thCol.children[0].checked = e.target.checked;
						}
					}
				});
			}
			else if(thCol.getAttribute("data-dateFrmt") != undefined && thCol.getAttribute("data-dateFrmt") != ''){
				let dateFrmt = gfn_dateFrmt(gfn_nullValue(item[thCol.getAttribute("data-id")]),thCol.getAttribute("data-dateFrmt"));
				td.append(dateFrmt);
				td.setAttribute("data-value",dateFrmt);
				td.setAttribute("data-id",thCol.getAttribute("data-id"));
				
			}
			else if(thCol.getAttribute("data-grpCd") != undefined && thCol.getAttribute("data-grpCd") != '' && thCol.getAttribute("data-type") === null){
				if(thCol.getAttribute("data-class") != undefined && thCol.getAttribute("data-class") ){
					if(thCol.getAttribute("data-grpCd") === 'IMP'){
						const tdSpan = document.createElement('SPAN');
					    tdSpan.append(gfn_getCodeVal(thCol.getAttribute("data-grpCd"),gfn_nullValue(item[thCol.getAttribute("data-id")])));
						if(item[thCol.getAttribute("data-id")] == "5"){
							tdSpan.classList.add("threat-high");
						}else if(item[thCol.getAttribute("data-id")] == "3"){
							tdSpan.classList.add("threat-middle");
					    }
					    else {
							tdSpan.classList.add("threat-low");
						}
						td.appendChild(tdSpan);
					}
					else if(thCol.getAttribute("data-grpCd") === 'IMPS'){
						const tdSpan = document.createElement('SPAN');
					    tdSpan.append(gfn_nullValue(item[thCol.getAttribute("data-id")]));
						if(item[thCol.getAttribute("data-id")] == "상"){
							tdSpan.classList.add("threat-high");
						}else if(item[thCol.getAttribute("data-id")] == "중"){
							tdSpan.classList.add("threat-middle");
					    
					    }else if(item[thCol.getAttribute("data-id")] == "정상"){
							tdSpan.classList.add("threat-middle");
					    
					    }else if(item[thCol.getAttribute("data-id")] == "비정상"){
							tdSpan.classList.add("threat-high");
					    }
					    else {
							tdSpan.classList.add("threat-low");
						}
						td.appendChild(tdSpan);
					}
					else if(thCol.getAttribute("data-grpCd") === 'COIM' || thCol.getAttribute("data-grpCd") === 'THIM'){
						const tdSpan = document.createElement('SPAN');
					    tdSpan.append(gfn_getCodeVal(thCol.getAttribute("data-grpCd"),gfn_nullValue(item[thCol.getAttribute("data-id")])));
						if(item[thCol.getAttribute("data-id")] == "5"){
							tdSpan.classList.add("threat-high");
						}else if(item[thCol.getAttribute("data-id")] == "3"){
							tdSpan.classList.add("threat-middle");
					    }
					    else if(item[thCol.getAttribute("data-id")] == "1"){
							tdSpan.classList.add("threat-low");
						}
						if(item[thCol.getAttribute("data-id")] != "0"){
							td.appendChild(tdSpan);
						}
						
					}
					else if(thCol.getAttribute("data-grpCd") === 'COIMS'){
						if(item[thCol.getAttribute("data-id")] == "9"){
							const tdHighSpan = document.createElement('SPAN');
					        tdHighSpan.append(gfn_getCodeVal('COIM','5'));
							tdHighSpan.classList.add("threat-high");
							td.appendChild(tdHighSpan);
							const tdMiddleSpan = document.createElement('SPAN');
					        tdMiddleSpan.append(gfn_getCodeVal('COIM','3'));
							tdMiddleSpan.classList.add("threat-middle");
							td.appendChild(tdMiddleSpan);
							const tdLowSpan = document.createElement('SPAN');
					        tdLowSpan.append(gfn_getCodeVal('COIM','1'));
							tdLowSpan.classList.add("threat-low");
							td.appendChild(tdLowSpan);
						}
						else if(item[thCol.getAttribute("data-id")] == "8"){
							const tdHighSpan = document.createElement('SPAN');
					        tdHighSpan.append(gfn_getCodeVal('COIM','5'));
							tdHighSpan.classList.add("threat-high");
							td.appendChild(tdHighSpan);
							const tdMiddleSpan = document.createElement('SPAN');
					        tdMiddleSpan.append(gfn_getCodeVal('COIM','3'));
							tdMiddleSpan.classList.add("threat-middle");
							td.appendChild(tdMiddleSpan);
						}
						else if(item[thCol.getAttribute("data-id")] == "6"){
							const tdHighSpan = document.createElement('SPAN');
					        tdHighSpan.append(gfn_getCodeVal('COIM','5'));
							tdHighSpan.classList.add("threat-high");
							td.appendChild(tdHighSpan);
							const tdLowSpan = document.createElement('SPAN');
					        tdLowSpan.append(gfn_getCodeVal('COIM','1'));
							tdLowSpan.classList.add("threat-low");
							td.appendChild(tdLowSpan);
						}
						else if(item[thCol.getAttribute("data-id")] == "5"){
							const tdHighSpan = document.createElement('SPAN');
					        tdHighSpan.append(gfn_getCodeVal('COIM','5'));
							tdHighSpan.classList.add("threat-high");
							td.appendChild(tdHighSpan);
						}else if(item[thCol.getAttribute("data-id")] == "4"){
							const tdMiddleSpan = document.createElement('SPAN');
					        tdMiddleSpan.append(gfn_getCodeVal('COIM','3'));
							tdMiddleSpan.classList.add("threat-middle");
							td.appendChild(tdMiddleSpan);
							const tdLowSpan = document.createElement('SPAN');
					        tdLowSpan.append(gfn_getCodeVal('COIM','1'));
							tdLowSpan.classList.add("threat-low");
							td.appendChild(tdLowSpan);
					    }
					    else if(item[thCol.getAttribute("data-id")] == "3"){
							const tdMiddleSpan = document.createElement('SPAN');
					        tdMiddleSpan.append(gfn_getCodeVal('COIM','3'));
							tdMiddleSpan.classList.add("threat-middle");
							td.appendChild(tdMiddleSpan);
					    }
					    else {
							const tdLowSpan = document.createElement('SPAN');
					        tdLowSpan.append(gfn_getCodeVal('COIM','1'));
							tdLowSpan.classList.add("threat-low");
					     	td.appendChild(tdLowSpan);
						}
					}
					td.setAttribute("data-value",gfn_nullValue(item[thCol.getAttribute("data-id")]));
					td.setAttribute("data-id",thCol.getAttribute("data-id"));
				}
				else{
					
					td.setAttribute("data-value",gfn_nullValue(item[thCol.getAttribute("data-id")]));
					td.setAttribute("data-id",thCol.getAttribute("data-id"));
					if(gfn_nullValue(item[thCol.getAttribute("data-id")]) != ''){
						td.append(gfn_getCodeVal(thCol.getAttribute("data-grpCd"),gfn_nullValue(item[thCol.getAttribute("data-id")])));
					}
					
				}
			}
			
			else if(thCol.getAttribute("data-btnNm") != undefined && thCol.getAttribute("data-btnNm") != ''){
				const tdAnch = document.createElement('a');
				tdAnch.classList.add("btn","small","darkblue");
				//tdAnch.setAttribute("title", "상세보기");
				tdAnch.setAttribute("title", thCol.getAttribute("data-btnNm"));
				tdAnch.setAttribute("href", "");
				if(thCol.classList.contains('btnSpan')){
					const tdSpan = document.createElement('span');
					tdSpan.classList.add('iconBtn','icon_btn_06');
					const tdStrong = document.createElement('strong');
					tdStrong.append(thCol.getAttribute("data-btnNm"));
					tdAnch.append(tdSpan);
					tdAnch.append(tdStrong);
				}
				else{
					tdAnch.append(thCol.getAttribute("data-btnNm"));
				}
				tdAnch.addEventListener('click',(e) => {
					e.preventDefault();
				    gridCheckedRow(thCols,tr);
				});
				td.setAttribute("data-value",gfn_nullValue(item[thCol.getAttribute("data-id")]));
				td.setAttribute("data-id",thCol.getAttribute("data-id"));
			    td.appendChild(tdAnch);
			}
			else if(thCol.getAttribute("data-type") != undefined){
				if(thCol.getAttribute("data-type") === 'chk'){
					let chkbox = document.createElement('input');
					chkbox.setAttribute("type", "checkbox");
					if(gfn_nullValue(item[thCol.getAttribute("data-id")]) == '1'){
						chkbox.checked = true;
					}
					//td.setAttribute("data-id",thCol.getAttribute("data-id"));
					td.appendChild(chkbox);
				}
				else if(thCol.getAttribute("data-type") === 'select'){
					let select = document.createElement('select');
					select.classList.add('form-select');
					let selectCol = gfn_getCodeGrp(thCol.getAttribute("data-grpCd"))
					selectCol.forEach((cd) => {
						if(cd.cd != null){
							let option = document.createElement('option');
							option.setAttribute("value",cd.cd);
							if(cd.cd == gfn_nullValue(item[thCol.getAttribute("data-id")])){
								option.setAttribute("selected",true);
							}
							option.append(cd.cdNm);
							select.appendChild(option);
						}
					});
                    td.setAttribute("data-value",gfn_nullValue(item[thCol.getAttribute("data-id")]));
                    td.setAttribute("data-id",thCol.getAttribute("data-id"));
					td.appendChild(select);
				}
				else if(thCol.getAttribute("data-type") === 'input'){
				     let input = document.createElement('input');
				    
					 input.setAttribute("type","text");
					 input.setAttribute("value",gfn_nullValue(item[thCol.getAttribute("data-id")]));
					 td.setAttribute("data-id",thCol.getAttribute("data-id"));
					 td.appendChild(input);
				}
				else if(thCol.getAttribute("data-type") === 'int'){
				    td.setAttribute("data-value",gfn_nullValue(item[thCol.getAttribute("data-id")]));
				    td.setAttribute("data-id",thCol.getAttribute("data-id"));
				    td.append(gfn_nullValue(item[thCol.getAttribute("data-id")]).toFixed(0));
				}
			}
			else{
				td.setAttribute("data-value",gfn_nullValue(item[thCol.getAttribute("data-id")]));
				td.setAttribute("data-id",thCol.getAttribute("data-id"));
				td.append(gfn_nullValue(item[thCol.getAttribute("data-id")]));
			}
			
			if(thCol.style.display == 'none'){
				td.style.display = 'none';
			}
			tr.appendChild(td);
		});
		
		tBody.appendChild(tr);
		
	}
	}catch(e){
		console.error(e);
	}
	
}	

const gridCheckedRow = (thCols,gridRow) => {
	let jsonParam = {};
	let tds = gridRow.querySelectorAll('td');
	thCols.forEach((th,index) => {
		if(!(th.getAttribute("data-id") === 'chk' || th.getAttribute("data-btnNm") != undefined)){
			if(tds[index].children[0] && tds[index].children[0].type === 'checkbox'){
				if(tds[index].children[0].checked){
					jsonParam[th.getAttribute("data-id")] = 'Y';
				}
				else{
					jsonParam[th.getAttribute("data-id")] = 'N';
				}
			}
			else{
				jsonParam[th.getAttribute("data-id")] = tds[index].getAttribute("data-value");
			}	
			
		}
	});
	detailFunc(jsonParam);
}

const gridCheckedData = (gridComp) => {
	let jsonParam = {};
	let checkedData = new Array();
	let tBody = gridComp.querySelector('.tBody');
	let thCols = gridComp.querySelector('.tHead').querySelectorAll("th");
	let chkIdx = 0;
	thCols.forEach((thCol,thIdx) => {
		if(thCol.getAttribute("data-id") === 'chk'){
			chkIdx = thIdx;
		}
	});
	tBody.querySelectorAll('tr').forEach((tr) => {
		let rowData = {};
		const tds = tr.querySelectorAll('td');
		if(tds[chkIdx].children[0].checked){
			tds.forEach((td,i) => {
				if(thCols[i].getAttribute("data-id") != 'chk'){
					
					if(td.children[0] && td.children[0].type === 'checkbox'){
						if(td.children[0].checked){
							rowData[thCols[i].getAttribute("data-id")] = 'Y';
						}
						else{
							rowData[thCols[i].getAttribute("data-id")] = 'N';
						}
					}
					else{
						rowData[thCols[i].getAttribute("data-id")] = td.getAttribute("data-value");
					}
				}
				
			});
			checkedData.push(rowData);
		}
	});
	jsonParam["list"] = checkedData;
	return jsonParam;
}	

const gridClickedData = (gridComp) => {
	let jsonParam = {};
	let clickedData = {};
	let thCols = gridComp.querySelector('.tHead').querySelectorAll("th");
	let trs = gridComp.querySelector('.tBody').querySelectorAll('tr');
	trs.forEach((tr) => {
		if(tr.classList.contains('trSelect')){
			const tds = tr.querySelectorAll('td');
			tds.forEach((td,i) => {
				if(thCols[i].getAttribute("data-id") != 'chk'){
					if(td.children[0] && td.children[0].type === 'checkbox'){
						if(td.children[0].checked){
							clickedData[thCols[i].getAttribute("data-id")] = 'Y';
						}
						else{
							clickedData[thCols[i].getAttribute("data-id")] = 'N';
						}
					}
					else{
						clickedData[thCols[i].getAttribute("data-id")] = td.getAttribute("data-value");
					}
				}
				
			});
		}
	});
	
	jsonParam.row = clickedData;
	return jsonParam;
}
		