'use strict'

let input = {
		
}
window.addEventListener('DOMContentLoaded', function(){
	inputInit();
	//tabInit();
});

const inputInit = () => {
	/**
	 * class에 number 속성 추가
	 * 숫자만 가능
	 */
	const numbers = document.querySelectorAll('.number');
	if(numbers){
		numbers.forEach((number) => {
			number.addEventListener('keyup', (e) => {
				e.preventDefault();
				const regExp = /[^0-9]/g;
				const ele = e.target;
				if (regExp.test(ele.value)) {
				  ele.value = ele.value.replace(regExp, '');
				}
				
			});
		})
	}
	
	const numberDays = document.querySelectorAll('.numberDay');
	if(numberDays){
		numberDays.forEach((number) => {
			number.addEventListener('keyup', (e) => {
				e.preventDefault();
				const regExp = /[^1-6]/g;
				const ele = e.target;
				if (regExp.test(ele.value)) {
				  ele.value = ele.value.replace(regExp, '');
				}
				
			});
		})
	}
	
	const numberWeeks = document.querySelectorAll('.numberWeek');
	if(numberWeeks){
		numberWeeks.forEach((number) => {
			number.addEventListener('keyup', (e) => {
				e.preventDefault();
				const regExp = /[^1-4]/g;
				const ele = e.target;
				if (regExp.test(ele.value)) {
				  ele.value = ele.value.replace(regExp, '');
				}
				
			});
		})
	}
	

	/**
	 * class에 number comma 속성 추가
	 * 숫자,콤마만 가능
	 */
	const numCommas = document.querySelectorAll('.numComma');
	if(numCommas){
		numCommas.forEach((numComma) => {
			numComma.addEventListener('keyup', (e) => {
				e.preventDefault();
				const regExp = /[^0-9|.]/g;
				const ele = e.target;
				if (regExp.test(ele.value)) {
				  ele.value = ele.value.replace(regExp, '');
				}
				
			});
		})
	}

	/**
	 * class에 string 속성 추가
	 * 문자만 가능
	 */
	const strings = document.querySelectorAll('.string');
	if(strings){
		strings.forEach((string) => {
			string.addEventListener('keyup', (e) => {
				e.preventDefault();
				const regExp = /[0-9]/g;
				const ele = e.target;
				if (regExp.test(ele.value)) {
				  ele.value = ele.value.replace(regExp, '');
				}
				
			});
		})
	}
	
	/**
	 * class에 eng 속성 추가
	 * 영문 가능
	 */
	const engs = document.querySelectorAll('.eng');
	if(engs){
		engs.forEach((eng) => {
			eng.addEventListener('keyup', (e) => {
				const regExp = /[^a-zA-Z]/g;
				if (regExp.test(e.target.value)) {
					e.preventDefault();
					e.target.value = e.target.value.replace(regExp, '');
					return;
				}
                
				
			});
		})
	}
	
	
	/**
	 * class에 engNum 속성 추가
	 * 영문,숫자 가능
	 */
	const engNums = document.querySelectorAll('.engNum');
	if(engNums){
		engNums.forEach((engNum) => {
			engNum.addEventListener('keyup', (e) => {
				e.preventDefault();
				const regExp = /[^0-9a-zA-Z]/g;
				const ele = e.target;
				if (regExp.test(ele.value)) {
				  ele.value = ele.value.replace(regExp, '');
				}
				
			});
		})
	}
	/**
	 * class에 kor 속성 추가
	 * 한글 가능
	 */
	const kors = document.querySelectorAll('.kor');
	if(kors){
		kors.forEach((kor) => {
			kor.addEventListener('keyup', (e) => {
				e.preventDefault();
				const regExp = /[a-z0-9]|[ \[\]{}()<>?|`~!@#$%^&*-_+=,.;:\"'\\]/g;
				const ele = e.target;
				if (regExp.test(ele.value)) {
				  ele.value = ele.value.replace(regExp, '');
				}
				
			});
		})
	}
	
	/**
	 * class에 korNum 속성 추가
	 * 한글,숫자 가능
	 */
	const korNums = document.querySelectorAll('.korNum');
	if(korNums){
		korNums.forEach((kor) => {
			kor.addEventListener('keyup', (e) => {
				e.preventDefault();
				//const regExp = /[^0-9a-z]|[ \[\]{}()<>?|`~!@#$%^&*-_+=,.;:\"'\\]/g;
				const regExp = /[^0-9|ㄱ-ㅎ|가-힣]/g;
				const ele = e.target;
				if (regExp.test(ele.value)) {
				  ele.value = ele.value.replace(regExp, '');
				}
				
			});
		})
	}
	
	/**
	 * class에 currency 속성 추가
	 * 금액 가능
	 */
	const currencys = document.querySelectorAll('.currency');
	if(currencys){
		currencys.forEach((currency) => {
			let a = 3212244;
			console.log(a.toLocaleString('ko-KR', {maximumFractionDigits: 2}));
			currency.addEventListener('keyup', (e) => {
				e.preventDefault();
				const ele = e.target;
				//ele.value = ele.value.replace(/^[0]|[^0-9]/g, '').replace(/\B(?=(\d{3})+(?!\d))/g, ",");
				ele.value = Number(ele.value.replace(/^[0]|[^0-9]/g, '')).toLocaleString('ko-KR');

			});
			
		})
	}
	
	/**
	 * class에 currency2 속성 추가
	 * 소수점2자리 가능
	 */
	const currencys2 = document.querySelectorAll('.currency2');
	if(currencys2){
		currencys2.forEach((currency) => {
			currency.addEventListener('keyup', (e) => {
				let ele = e.target;
				ele.value = ele.value.replace(/^[0]|^[.]|[^0-9.]/g, '');

			});
			currency.addEventListener('blur', (e) => {
				let ele = e.target;
				ele.value = Number(ele.value).toLocaleString('ko-KR', {maximumFractionDigits: 2});

			});

		})
	}
	
	
	const unitSch = document.querySelector('.unitChk');
	let unitNm = null;
	if(unitSch) {
		unitNm = unitSch.querySelector('#schUnitNm');
	}
	
	if(unitNm){
		unitNm.addEventListener('click',(e) => {
			console.log("unitNm click");
		    let list = null;
	   		gfn_asyncJsonCall("/unitInfo",'GET',{}).then((data) => {
	   			for(let key in data) {
	                   if(key.indexOf('list') > -1){
	                      	list = data[key];
	   				}
	   			}
	   			const treeBox = document.querySelector('.treeBox');
	   			treeBox.innerHTML = '';
	   			let treejs = createInitTree(treeBox,list,false);
	   			if(list != null && list.length > 0){
	   				createTreeNoChk(list,treejs);
	   			}

	   		}).then(() => {
	   			document.querySelector('.treeBox').querySelectorAll('.treejs-label').forEach((label) => {
	   				let li = label.parentNode;
	   				if(li.getAttribute('data-id') === document.querySelector('.unitChk').querySelector('#schUnitId').value){
	   					label.classList.add('on');
	   					return false;
	   	    		}
	   			});  	
	   			
	   		});
	   		let popLayer = document.querySelector('.unitPop');
	   		popLayer.style.display = "block";
	   });
	   const btnUnitClose = document.querySelector('.btnUnitClose');
	   if(btnUnitClose){
		   btnUnitClose.addEventListener('click', (e) => {
	       		e.preventDefault();
	       		document.querySelector('.unitPop').style.display = "none";
	       });
	   }
	   
	   const btnUnitOpen = document.querySelector('.btnUnitOpen');
	   if(btnUnitOpen){
		   btnUnitOpen.addEventListener('click', (e) => {
	   			e.preventDefault();
	  			let popLayer = document.querySelector('.unitPop'); 
	  			let chkObj = false;
	  			document.querySelectorAll('.treejs-label').forEach((label) => {
	  				if(label.classList.contains('on')){
	  					chkObj = true;
	  					console.log(label.parentNode.getAttribute('data-id'));
	  					document.querySelector('.unitChk').querySelector('#schUnitId').value = label.parentNode.getAttribute('data-id');
	  					document.querySelector('#schUnitNm').value = label.innerText;
	  					unitAfterFunc();
	  					return false;
	    		    }
	  				
	  			});  	
	  			if(!chkObj){
	  				msgCall('부대정보를 선택하세요.',false);
	  				return;
	  			}
	  			popLayer.style.display = "none";
	       		
	   		});
	   }	
	}
	
	
}

const isIpValid = (ipAddr) => {
	const regExp = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
	if (regExp.test(ipAddr)) {
		return true;
	}
	else{
		return false;
	}
}

/*
 * tab
 */
const tabInit = () => {
    let tabHtml = null;
    const tabPanes = document.querySelectorAll('.tabPane');
	if(tabPanes){
		const tabParent = tabPanes[0].parentNode;
		let parser = new DOMParser();
		let doc = parser.parseFromString(tabParent.innerHTML, "text/html");
		tabHtml = doc.querySelectorAll('.tabPane');
	
		tabPanes.forEach((tabPane,index) => {
			let tabPaneChilds = [...tabPane.children];
			tabPaneChilds.forEach((child,index) => {
				
			});
		});
		
		
	}
	const tabBtns = document.querySelectorAll('.btn-tab01');
    tabBtns.forEach((tabBtn,index) => {
 	   tabBtn.addEventListener('click', (e) => {
 		  e.preventDefault();
	      if(!e.target.classList.contains('on')){
	    	  tabFunc(e.target,index);
	      }
 		   
 	   });
    });
    
    const tabFunc = (tab,idx) => {
  	   tabBtns.forEach((tabBtn) => {
      	  tabBtn.classList.remove('on');
         });
  	   tab.classList.add('on');
  	   
  	   const tabPanes = document.querySelectorAll('.tabPane');
  	   if(tabPanes){
  		   tabPanes.forEach((tabPane,index) => {
         	  if(index === idx){
         		  //tabPane.classList.remove('displayNone'); 
	         	  tabHtml.forEach((tab,tabIndex) => {
	         		 if(index === tabIndex){
	         			tabPane.innerHTML = tab.innerHTML;
	         		 }
	         	  });
         		 
         	  }else{
         		  //tabPane.classList.add('displayNone');
         		  
         	  }
           });
  	   }
  	   
  	  
     }

}


