'use strict'

const treeInit = (treeBox,targetTree) => {
	//const treeBox = document.querySelector('.treeBox');
	if(treeBox){
		treeBox.addEventListener('click',(e) => {
			e.preventDefault();
			if (e.target.nodeName === 'SPAN' && e.target.classList.contains('treejs-switcher')) {
				selectSwitch(e.target);
			}
			else if(e.target.nodeName === 'SPAN' && e.target.classList.contains('treejs-checkbox')) {
				selectCheck(e.target);
			}
			else if (e.target.nodeName === 'SPAN' && e.target.classList.contains('treejs-label')) {
				selectNode(e.target);
			}
			else if (e.target.nodeName === 'LI' && e.target.classList.contains('treejs-node')) {
				//selectNode(e.target.querySelector('.treejs-label'));
		    }
		});

		treeBox.addEventListener('dragstart', (e) => {
			e.preventDefault();
			if(targetTree){

			}
	    	//let chkedLis = Array.from(treeBox.querySelector('.treejs').querySelectorAll('.treejs-node__checked'));
	    	/*
			if(chkedLis.length === 0){
				msgCall('노드를 선택하여 이동하세요',false);
				return false;
			}
			*/
	    });
		treeBox.addEventListener('dragover', (e) => {
		    e.preventDefault();
	    });

		treeBox.addEventListener("dragenter", (e) => {
			e.preventDefault();
	    });

		// 드래그한 파일이 dropZone 영역을 벗어났을 때
		treeBox.addEventListener("dragleave", (e) => {
			e.preventDefault();
	    });

		treeBox.addEventListener('drop', (e) => {
			e.preventDefault();
			let src = JSON.parse(e.dataTransfer.getData('text'));
			//if(e.target.classList.contains('treejs-node') ){
			if(e.target.classList.contains('treejs-node') || e.target.classList.contains('treejs-label')){
				let modalTreeBox = document.querySelector('.modalTreeBox');
				if(modalTreeBox){
					let treeLis = modalTreeBox.getElementsByTagName('li');
		    		for (let treeLi of treeLis) {
		    		     if(treeLi.getAttribute('data-id') == src.assetId){
		    		    	 return false;
		    		     }
		    		}
				}

				let targetLi = e.target;
				if(targetLi.tagName === 'SPAN'){
					targetLi = e.target.parentNode;
				}
				let parentSpanSwitcher = targetLi.querySelector('.treejs-switcher');
				if(parentSpanSwitcher && targetLi.parentNode.querySelector('.rootLi')){
					return ;
				}
				if(!parentSpanSwitcher){
				    parentSpanSwitcher = document.createElement("SPAN");
					parentSpanSwitcher.classList.add('treejs-switcher');
					targetLi.insertBefore(parentSpanSwitcher,targetLi.firstChild);
					targetLi.classList.remove('treejs-placeholder');
					targetLi.classList.remove('treejs-node__close')
				}

				let targetUl = targetLi.querySelector('.treejs-nodes');
			    if(!targetUl){
			    	targetUl = createUlEle();
			    	targetLi.appendChild(targetUl);
			    }
		    	let li = document.createElement("LI");
				li.classList.add('treejs-node');
				li.classList.add('treejs-placeholder');
				li.setAttribute("data-id",src.assetId);
				let spanChkBox = document.createElement("SPAN");
				spanChkBox.classList.add('treejs-checkbox');
				let spanLabel = document.createElement("SPAN");
				spanLabel.classList.add('treejs-label');   //contenteditable
				//spanLabel.setAttribute('contenteditable',true);
				//spanLabel.setAttribute('placeholder','입력하세요');
				spanLabel.textContent = src.assetNm;
				//li.appendChild(spanChkBox);
				li.appendChild(spanLabel);
				targetUl.appendChild(li);
				if(modalTreeBox){
					let treeBoxList = document.querySelector('.treeBoxList');
					if(treeBoxList){
						let lis = treeBoxList.getElementsByTagName('li');
			    		for (let li of lis) {
			    		     if(li.getAttribute('data-id') == src.assetId){
			    		    	 li.style.background = '#E5E5E5';
			    		     }
			    		}
					}
				}
			}
            /*
			if(e.target.classList.contains('treejs-node') || e.target.classList.contains('treejs-label')){
				let targetLi = e.target;
				if(e.target.classList.contains('treejs-label')){
					targetLi = e.target.parentNode;
				}
				if(targetLi.classList.contains('treejs-node__checked')){
					msgCall('목적노드가 선택되어 있습니다',false);
					return false;
				}

				let parentSpanSwitcher = targetLi.querySelector('.treejs-switcher');
				if(!parentSpanSwitcher){
				    parentSpanSwitcher = document.createElement("SPAN");
					parentSpanSwitcher.classList.add('treejs-switcher');
					targetLi.insertBefore(parentSpanSwitcher,targetLi.firstChild);
					targetLi.classList.remove('treejs-placeholder');
					targetLi.classList.remove('treejs-node__close')
				}
				let targetUl = targetLi.querySelector('.treejs-nodes');
			    if(!targetUl){
			    	targetUl = createUlEle();
			    	targetLi.appendChild(targetUl);
			    }
			    treeItemMove(treeBox.querySelector('.treejs'),targetUl);
			}*/
			treeBox.querySelectorAll('.treejs-node').forEach((li) => {
				if(li.classList.contains('treejs-node__halfchecked')){
		    		li.classList.remove('treejs-node__halfchecked');
		    	}

	    	});

	    });
	}
}

const treeItemMove = (fromTarget,toTargt) => {

	let chkedList = Array.from(fromTarget.querySelectorAll('.treejs-node__checked'));
	if(chkedList.length > 0){
    	for(let chkedLi of chkedList){
    		chkedLi.classList.remove('treejs-node__checked');
			chkedLi.querySelectorAll('.treejs-node__checked').forEach((childChkedLi) => {
				childChkedLi.classList.remove('treejs-node__checked');
			});
            let hasChild = hasChileItem(chkedLi);
			if(hasChild){
				toTargt.appendChild(chkedLi);
			}
			else{
				toTargt.appendChild(chkedLi);
			}
			break;
    	}
    	chkedMove(fromTarget,toTargt);
	}

}

const chkedMove = (fromTarget,toTargt) => {
	let chkedList = Array.from(fromTarget.querySelectorAll('.treejs-node__checked'));
	if(chkedList.length > 0){
    	for(let chkedLi of chkedList){
    		chkedLi.classList.remove('treejs-node__checked');
			chkedLi.querySelectorAll('.treejs-node__checked').forEach((childChkedLi) => {
				childChkedLi.classList.remove('treejs-node__checked');
			});
            let hasChild = hasChileItem(chkedLi);
			if(hasChild){
				toTargt.appendChild(chkedLi);
			}
			else{
				toTargt.appendChild(chkedLi);
			}
			break;
    	}
    	chkedMove(fromTarget,toTargt);
	}
}


const createInitTree = (treeBox,list,isChk,rootNm) => {
	let _rootNm = rootNm === undefined || rootNm === null?'네트워크 장비':rootNm;
	//let treeBox = document.querySelector('.treeBox');
    let treejs = document.createElement('DIV');
    treejs.classList.add('treejs');
    treeBox.appendChild(treejs);

	if(list === null || list.length === 0){
		let li = document.createElement("LI");

    	li.classList.add('treejs-node','treejs-placeholder','rootLi');
    	li.setAttribute("data-id",'0');
    	if(isChk){

    		let spanChkBox = document.createElement("SPAN");
        	spanChkBox.classList.add('treejs-checkbox');
        	li.appendChild(spanChkBox);
    	}
    	else{
    		li.classList.add('noSelect');
    	}
    	let spanLabel = document.createElement("SPAN");
    	spanLabel.classList.add('treejs-label');
    	spanLabel.append(_rootNm);
    	li.appendChild(spanLabel);
    	treejs.appendChild(li);
	}

	return treejs;
}

const createTree = (soure,target,draggable) => {
	let __draggable = draggable===null||draggable === undefined?false:draggable;
	try{
		for(let itm of soure){
			let li = document.createElement("LI");
        	li.classList.add('treejs-node');
        	li.setAttribute("data-id",itm.id);

        	let spanChkBox = document.createElement("SPAN");
        	spanChkBox.classList.add('treejs-checkbox');
        	let spanLabel = document.createElement("SPAN");
        	spanLabel.classList.add('treejs-label');
        	spanLabel.append(itm.classNm);

			if(itm.id === '0'){
				li.classList.add('rootLi');
			}
			else{
				if(__draggable){
					li.setAttribute("draggable","true");
		        	spanChkBox.setAttribute("draggable","true");
	        	}
	        	spanLabel.setAttribute('contenteditable',true);
	        	spanLabel.setAttribute('placeholder','입력하세요');
	        	spanLabel.setAttribute("draggable","true");

			}
			let ul = target.querySelector('.treejs-nodes');
    		if(ul){
    			ul.appendChild(li);
    			if(itm.children && itm.children.length > 0){
    				let spanSwitch = document.createElement("SPAN");
	        		spanSwitch.classList.add('treejs-switcher');
	            	li.appendChild(spanSwitch);
    			}
    			else{
    				li.classList.add('treejs-placeholder');
    			}
    		}else{
    			if(itm.children && itm.children.length > 0){
    				let spanSwitch = document.createElement("SPAN");
	        		spanSwitch.classList.add('treejs-switcher');
	            	li.appendChild(spanSwitch);
    			}
    			else{
    				li.classList.add('treejs-placeholder');
    			}
    			ul = document.createElement('ul');
         		ul.classList.add('treejs-nodes');
         		ul.appendChild(li);
         		target.appendChild(ul);
    		}

			li.appendChild(spanChkBox);
        	li.appendChild(spanLabel);

        	if(itm.children && itm.children.length > 0){
        		createTree(itm.children,li);
        	}


		}
	}catch(e){console.log(e);}

}


const createTreeNoChk = (soure,target,draggable,drawType) => {
	let __draggable = draggable===null||draggable === undefined?false:draggable;
	let __drawType = drawType===null||drawType === undefined?'':drawType;
	//console.log("createTreeNoChk drawType:",__drawType);
	try{
		for(let itm of soure){
			let li = document.createElement("LI");
        	li.classList.add('treejs-node');
        	//li.setAttribute("data-id",itm.id);
        	li.setAttribute("data-id",itm.assetId);
        	let spanLabel = document.createElement("SPAN");
        	spanLabel.classList.add('treejs-label');
        	spanLabel.append(itm.classNm+' ');
        	if(__drawType == 'asset'){
				let assetStatus = gfn_nullValue(itm.assetStatus);
				if(assetStatus.length >0){
					let font = document.createElement('font');
					if(assetStatus == "미보고") {
						font.setAttribute("color","gray");
					}
					else if(assetStatus == "정상"){
						font.setAttribute("color","green");
					}
					else {
						font.setAttribute("color","red");
					}
					font.append('('+assetStatus+')');
					spanLabel.append(font);

				}

			}
			else if(__drawType == 'threat'){
				if(itm.count > 0){
					let font = document.createElement('font');
					font.setAttribute("color","red");
					font.append('('+itm.count+')');
					spanLabel.append(font);
				}
			}

        	if(itm.id == '1'){
				li.classList.add('rootLi','noSelect');
			}
			else{
				if(__draggable){
					li.setAttribute("draggable","true");
		        	spanLabel.setAttribute("draggable","true");
				}
			}

			let ul = target.querySelector('.treejs-nodes');
    		if(ul){
    			ul.appendChild(li);
    			if(itm.children && itm.children.length > 0){
    				let spanSwitch = document.createElement("SPAN");
	        		spanSwitch.classList.add('treejs-switcher');
	            	li.appendChild(spanSwitch);
    			}
    			else{
    				li.classList.add('treejs-placeholder');
    			}
    		}else{
    			if(itm.children && itm.children.length > 0){
    				let spanSwitch = document.createElement("SPAN");
	        		spanSwitch.classList.add('treejs-switcher');
	            	li.appendChild(spanSwitch);
    			}
    			else{
    				li.classList.add('treejs-placeholder');
    			}
    			ul = document.createElement('ul');
         		ul.classList.add('treejs-nodes');
         		ul.appendChild(li);
         		target.appendChild(ul);
    		}
        	li.appendChild(spanLabel);

        	if(itm.children && itm.children.length > 0){
        		createTreeNoChk(itm.children,li,null,drawType);
        	}

		}
	}catch(e){console.log(e);}

}


const selectCheck = (target) => {
	let li = target.parentNode;

	if(li.classList.contains('treejs-node__halfchecked')){
		li.classList.remove('treejs-node__halfchecked');
	}

	console.log("target:",target.style.cssText);
	let chk = false;
	li.classList.toggle('treejs-node__checked');
   	if(li.classList.contains('treejs-node__checked')){
   		chk = true;
   	}
   	let hasChild = hasChileItem(li);
   	if(hasChild){
   		setChildNode(li,'check',chk);
   	}
	setParentNode(li,chk);
}

const setParentNode = (target,chk) => {
	if(target.classList.contains('rootLi')){
		return false;
	}
	else{
		let ul = target.parentNode;
		let liCnt = 0;
		let liChkCnt = 0;
		let liHalfChkCnt = 0;
        for(let ele of ul.childNodes){
        	if(ele.tagName === 'LI'){
        		liCnt += 1;
        		if(ele.classList.contains('treejs-node__checked')){
        			liChkCnt += 1;
        		}
        		let hasChild = hasChileItem(ele);
            	if(hasChild && ele.classList.contains('treejs-node__halfchecked')){
            		liChkCnt += 1;
            		liHalfChkCnt += 1;
            	}
        	}
        }
        let li = target.parentNode.parentNode;

        if(liCnt === liChkCnt){
        	if(liHalfChkCnt === 0){
        		if(li.classList.contains('treejs-node__halfchecked')){
            		li.classList.remove('treejs-node__halfchecked');
            	}
            	li.classList.add('treejs-node__checked');
        	}
        	else{
        		if(li.classList.contains('treejs-node__checked')){
            		li.classList.remove('treejs-node__checked');
            	}
        		if(!li.classList.contains('treejs-node__halfchecked')){
            		li.classList.add('treejs-node__halfchecked');
            	}
        	}
        }
        else if(liChkCnt > 0 && liCnt != liChkCnt){
           	if(li.classList.contains('treejs-node__checked')){
           		li.classList.remove('treejs-node__checked');
           	}
           	li.classList.add('treejs-node__halfchecked');

        }
        else if(liChkCnt === 0){
        	if(liCnt === 1){
        		if(chk){
            		li.classList.add('treejs-node__checked');
            	}
            	else{
            		if(li.classList.contains('treejs-node__checked')){
                		li.classList.remove('treejs-node__checked');
                	}
            	}
        	}else{
        		if(chk){
            		li.classList.add('treejs-node__halfchecked');
            	}
            	else{
            		if(li.classList.contains('treejs-node__halfchecked')){
                		li.classList.remove('treejs-node__halfchecked');
                	}
            	}
        	}

        }
        setParentNode(li,chk);
	}

}

const setChildNode = (target,mode,isTrue) => {
	for(let ele of target.childNodes){
		if(ele.tagName === 'UL'){
			for(let li of ele.childNodes){
				if(li.tagName === 'LI'){
					if(mode === 'check'){
						if(isTrue){
							if(!li.classList.contains('treejs-node__checked')){
    							li.classList.add('treejs-node__checked');
    						}
    						if(li.classList.contains('treejs-node__halfchecked')){
    		            		li.classList.remove('treejs-node__halfchecked');
    		            	}
						}else{
							if(li.classList.contains('treejs-node__checked')){
    							li.classList.remove('treejs-node__checked');
    						}
    						if(li.classList.contains('treejs-node__halfchecked')){
    		            		li.classList.remove('treejs-node__halfchecked');
    		            	}
						}

						setChildNode(li,mode,isTrue);
					}
					else{
						if(isTrue){
							if(li.classList.contains('treejs-node__close')){
								li.classList.remove('treejs-node__close');
							}
						}
						else{
							li.classList.add('treejs-node__close');
						}
						setChildNode(li,mode,isTrue);
					}
				}
			}
		}
	}

}

const createUlEle =  () => {
    let ul = document.createElement('ul');
    ul.classList.add('treejs-nodes');
    return ul;
}

const onSwitcherClick = (target) => {
	let liEle = target.parentNode;
    let ele = liEle.lastChild;
	let height = ele.scrollHeight;
	if (liEle.classList.contains('treejs-node__close')) {
	    animation(150, {
	      enter: function enter() {
	        ele.style.height = 0;
	        ele.style.opacity = 0;
	      },
	      active: function active() {
	        ele.style.height = "".concat(height, "px");
	        ele.style.opacity = 1;
	      },
	      leave: function leave() {
	        ele.style.height = '';
	        ele.style.opacity = '';
	        liEle.classList.remove('treejs-node__close');
	      }
	    });
	} else {
	    animation(150, {
	      enter: function enter() {
	        ele.style.height = "".concat(height, "px");
	        ele.style.opacity = 1;
	      },
	      active: function active() {
	        ele.style.height = 0;
	        ele.style.opacity = 0;
	      },
	      leave: function leave() {
	        ele.style.height = '';
	        ele.style.opacity = '';
	        liEle.classList.add('treejs-node__close');
	      }
	    });
	}
}


const selectNode = (target) => {
	console.log("selectNode target:",target);
	document.querySelectorAll('.treejs-label').forEach((label) => {
		let li = label.parentNode;
		if(!li.classList.contains('noSelect')){
			if(target === label){
    			target.classList.toggle('on');
    		}else{
    			if(label.classList.contains('on')){
    				label.classList.remove('on');
    			}
    		}
		}
	});
}

const hasChileItem = (liEle) => {
	let hasChild = false;
	for(let ele of liEle.childNodes){
		if(ele.tagName === 'UL'){
			hasChild = true;
			return hasChild;
		}
	}
	return hasChild;
}

const selectSwitch = (target) => {
	let li = target.parentNode;
	li.classList.toggle('treejs-node__close');

	let callpse = true;
	if(li.classList.contains('treejs-node__close')){
		callpse = false;
	}
	let hasChild = hasChileItem(li);
	if(hasChild){
		setChildNode(li,'switch',callpse);
	}
}

const addTree = (target) => {
	document.querySelectorAll('.treejs-node').forEach((liEle) => {
		if(liEle.querySelector('.treejs-label').classList.contains('on')){
			let parentSpanSwitcher = liEle.querySelector('.treejs-switcher');
			if(!parentSpanSwitcher){
				parentSpanSwitcher = document.createElement("SPAN");
				parentSpanSwitcher.classList.add('treejs-switcher');
				liEle.insertBefore(parentSpanSwitcher,liEle.firstChild);
				liEle.classList.remove('treejs-placeholder');
				liEle.classList.remove('treejs-node__close')
			}
			let ul = liEle.querySelector('.treejs-nodes');
			let li = document.createElement("LI");
			li.classList.add('treejs-node');
			li.classList.add('treejs-placeholder');
			let spanChkBox = document.createElement("SPAN");
			spanChkBox.classList.add('treejs-checkbox');
			let spanLabel = document.createElement("SPAN");
			spanLabel.classList.add('treejs-label');   //contenteditable
			spanLabel.setAttribute('contenteditable',true);
			spanLabel.setAttribute('placeholder','입력하세요');
			li.appendChild(spanChkBox);
			li.appendChild(spanLabel);
			if(ul){
				ul.appendChild(li);
			}
			else{
				ul = createUlEle();
				ul.appendChild(li);
				liEle.appendChild(ul);
			}
			let liCnt = 0;
    		let liChkCnt = 0;
            for(let ele of ul.childNodes){
            	if(ele.tagName === 'LI'){
            		liCnt += 1;
            		if(ele.classList.contains('treejs-node__checked')){
            			liChkCnt += 1;
            		}
            	}
            }
            if(liCnt === liChkCnt){
            	if(liEle.classList.contains('treejs-node__halfchecked')){
            		liEle.classList.remove('treejs-node__halfchecked');
            	}
            	liEle.classList.add('treejs-node__checked');
            }
            else if(liChkCnt > 0 && liCnt != liChkCnt){
               	if(liEle.classList.contains('treejs-node__checked')){
               		liEle.classList.remove('treejs-node__checked');
               	}
               	liEle.classList.add('treejs-node__halfchecked');

            }
            else if(liChkCnt === 0){
            	if(liEle.classList.contains('treejs-node__checked')){
            		liEle.classList.remove('treejs-node__checked');
            	}
            	if(liEle.classList.contains('treejs-node__halfchecked')){
            		liEle.classList.remove('treejs-node__halfchecked');
            	}
            }
            li.focus();

		}
	});
}

const deleteTree = (target) => {
	let _target = target === null || target === undefined?document.querySelectorAll('.treejs-node'):target;
	_target.forEach((liEle) => {
		if(liEle.querySelector('.treejs-label').classList.contains('on')){
			if(liEle.classList.contains('rootLi')){
				msgCall('삭제가 불가능합니다.',false);
				return false;
			}
			let ul = liEle.parentNode;
			liEle.remove();
			let existLi = ul.querySelector('.treejs-node');
			if(existLi){
				let liCnt = 0;
	    		let liChkCnt = 0;
	    		let liHalfChkCnt = 0;
	            for(let ele of ul.childNodes){
	            	if(ele.tagName === 'LI'){
	            		liCnt += 1;
	            		if(ele.classList.contains('treejs-node__checked')){
	            			liChkCnt += 1;
	            		}
	            		let hasChild = hasChileItem(ele);
	                	if(hasChild && ele.classList.contains('treejs-node__halfchecked')){
	                		liChkCnt += 1;
	                		liHalfChkCnt += 1;
	                	}
	            	}
	            }
	            //console.log("li:",li);
	            console.log("liCnt:",liCnt,",liChkCnt:",liChkCnt);
	            if(liCnt === liChkCnt){
	            	if(liHalfChkCnt === 0){
	            		if(ul.parentNode.classList.contains('treejs-node__halfchecked')){
	                		ul.parentNode.classList.remove('treejs-node__halfchecked');
	                	}
	            		ul.parentNode.classList.add('treejs-node__checked');
	            	}
	            	else{
	            		if(ul.parentNode.classList.contains('treejs-node__checked')){
	            			ul.parentNode.classList.remove('treejs-node__checked');
	                	}
	            		if(!ul.parentNode.classList.contains('treejs-node__halfchecked')){
	            			ul.parentNode.classList.add('treejs-node__halfchecked');
	                	}
	            	}
	            	setParentNode(ul.parentNode,true);
	            }
	            else if(liChkCnt > 0 && liCnt != liChkCnt){
	               	if(ul.parentNode.classList.contains('treejs-node__checked')){
	               		ul.parentNode.classList.remove('treejs-node__checked');
	               	}
	               	else{
	               		if(!ul.parentNode.classList.contains('treejs-node__halfchecked')){
	               			ul.parentNode.classList.add('treejs-node__halfchecked');
	               		}
	               	}
	               	setParentNode(ul.parentNode,true);
	            }
	            else if(liChkCnt === 0){
	             	if(ul.parentNode.classList.contains('treejs-node__checked')){
	               		ul.parentNode.classList.remove('treejs-node__checked');
	               	}
	             	if(ul.parentNode.classList.contains('treejs-node__halfchecked')){
               			ul.parentNode.classList.remove('treejs-node__halfchecked');
               		}
	             	setParentNode(ul.parentNode,false);
	            }

	           //

			}
			else{
				let parentLi = ul.parentNode;
				let switCherSapn = parentLi.querySelector('.treejs-switcher');
				switCherSapn.remove();
				ul.remove();
				parentLi.classList.add('treejs-placeholder');
			}
		}
	});

	let modalTreeBox = document.querySelector('.modalTreeBox');
	if(modalTreeBox){
		let assets = [];
		let treeLis = modalTreeBox.getElementsByTagName('li');
		for (let treeLi of treeLis) {
			if(treeLi.getAttribute('data-id') && treeLi.getAttribute('data-id') != ''){
				assets.push(treeLi.getAttribute('data-id'));
			}

		}
		let treeBoxList = document.querySelector('.treeBoxList');
		if(treeBoxList){
			let lis = treeBoxList.getElementsByTagName('li');
    		for (let li of lis) {
				//li.style.background = '#FFFFFF';
				li.style.setProperty("background",'rgb(229, 229, 229)');
    		}
    		if(assets.length > 0){
				for (let li of lis) {
					if(!assets.includes(li.getAttribute('data-id'))){
						li.style.removeProperty("background");
					}
				}
			}

		}
	}
}

const getTreeData = (target) => {
	let jsonArray 	= new Array();
	try{
	for(let ele of target.childNodes){
		if(ele.tagName === 'LI'){
			var jsonObj	= new Object();
			jsonObj.assetId = gfn_nullValue(ele.getAttribute('data-id'));
			let spanLabel = ele.querySelector('.treejs-label');
			jsonObj.classNm = spanLabel.innerText.trim();
			if(hasChileItem(ele)){
				let childData = getTreeData(ele.querySelector('.treejs-nodes'),jsonObj.assetId);
				jsonObj.children = childData;
			}
			jsonObj = JSON.stringify(jsonObj);
			jsonArray.push(JSON.parse(jsonObj));
		}
	}
	}catch(e){console.error(e);}
	return jsonArray;
}


