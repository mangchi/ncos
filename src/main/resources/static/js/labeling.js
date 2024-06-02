'use strict'

let labelingMain = {};
let labeling = {};
let labelTarget = 'main';
let zoneArray = [];
let assetArray = [];
let choosedColor = 'rgba(255, 255, 0, 0.4)',
    otherColor = 'rgba(255,0,255, 0.4)',
    redColor = 'rgba(255,0,0, 0.4)',
    greenColor = 'rgba(0,255,0, 0.4)',
    lineWidth = 0.05;

const labelInit = () => {
	labelingMain = {
			mode:'select'	
		  , imageId: ''
		  , img : null             //이미지 객체
		  , scaleX : 1
		  , scaleY : 1
		  , xOffset : 0
		  , yOffset : 0
		  , active : false
		  , rectIndex : -1
		  , currentX : 0
		  , currentY : 0
		  , objCnt : 0   // object count
		  , drawing : false // 그리고 있는 중인가
		  , sx : 0
		  , sy : 0
		  , ex : 0
		  , ey : 0
		  , initialX : 0
		  , initialY : 0
		  , arRectangle : new Array()
	      , cssTxt : document.querySelector('.canvasWrapMain').style.cssText
	      , layerX : 0
	      , layerY : 0

	};
	
	labeling = {
			mode:'select'	
		  , imageId: ''
		  , img : null             //이미지 객체
		  , scaleX : 1
		  , scaleY : 1
		  , xOffset : 0
		  , yOffset : 0
		  , active : false
		  , rectIndex : -1
		  , currentX : 0
		  , currentY : 0
		  , objCnt : 0   // object count
		  , drawing : false // 그리고 있는 중인가
		  , sx : 0
		  , sy : 0
		  , ex : 0
		  , ey : 0
		  , initialX : 0
		  , initialY : 0
		  , arRectangle : new Array()
	      , cssTxt : document.querySelector('.canvasWrap').style.cssText
	      , layerX : 0
	      , layerY : 0

	};
	zoneArray = new Array();
	assetArray = new Array();
}


//var arRectangle = new Array();

 // 사각형 생성자
function Rectangle(sx, sy, ex, ey, color,objIdx,zoneId,zoneName,delYn,isMod) {
	try{
		let _delYn = delYn===undefined?'Y':delYn;
		let _isMod = isMod===undefined?'':isMod;
		this.sx = sx;
		this.sy = sy;
		this.ex = ex;
		this.ey = ey;
		this.color = color;
		this.objIdx = objIdx;
		this.zoneId = zoneId;
		this.zoneName = zoneName;
		this.delYn = _delYn;
		this.isMod = _isMod;
	}catch(e){console.error(e);}
}

const canvasX = (clientX) => {
	let canvas = null;//document.querySelector('#drawLayerMain');
	if(labelTarget == 'main'){
		canvas = document.querySelector('#drawLayerMain');
	}
	else{
		canvas = document.querySelector('#drawLayer');
	}
	var bound = canvas.getBoundingClientRect();
    var bw = 0;
   // console.log("left:",bound.left);
    return (clientX - bound.left) * (canvas.width / (bound.width));
    //return (clientX - bound.left - bw) * (canvas.width / (bound.width - bw * 2));
}

const canvasY = (clientY) => {
	let canvas = null;
	if(labelTarget == 'main'){
		canvas = document.querySelector('#drawLayerMain');
	}
	else{
		canvas = document.querySelector('#drawLayer');
	}
    var bound = canvas.getBoundingClientRect();
    var bw = 0;
    return (clientY - bound.top) * (canvas.height / (bound.height));
    //return (clientY - bound.top - bw) * (canvas.height / (bound.height - bw * 2));
}



// x, y 위치의 사각형 찾음. 없으면 -1
const getRectangle = (x, y) => {
	let rectangles = labelingMain.arRectangle;
	if(labelTarget != 'main'){
		rectangles = labeling.arRectangle;
	}
	for (let i = 0;i < rectangles.length;i++) {
		let r = rectangles[i];
		if (x > r.sx && x < r.ex && y > r.sy && y < r.ey) {
			return r.objIdx;
	    }
	}
	return -1;
}    

// 화면 지우고 모든 도형을 순서대로 다 그림
const drawRects = (x,y,isRect) => {
	//console.log("drawRects");
	let canvas = document.querySelector('.container-wide').querySelector('#drawLayerMain');
	let labelImgWith = labelingMain.img.width;
	let labelImgHeight = labelingMain.img.height;
	let rectangles = labelingMain.arRectangle;
	let currentX = labelingMain.currentX;
	let currentY = labelingMain.currentY;
	let scaleX = labelingMain.scaleX;
	let scaleY = labelingMain.scaleY;
	let layerX = labelingMain.layerX;
	let layerY = labelingMain.layerY;
	if(labelTarget != 'main'){
		canvas = document.querySelector('#assetdisplay').querySelector('#drawLayer');
		labelImgWith = labeling.img.width;
		labelImgHeight = labeling.img.height;
		rectangles = labeling.arRectangle;
		currentX = labeling.currentX;
		currentY = labeling.currentY;
		scaleX = labeling.scaleX;
	    scaleY = labeling.scaleY;
	    layerX = labeling.layerX;
	    layerY = labeling.layerY;
	}
	let ctx = canvas.getContext('2d');
	ctx.clearRect(0,0, labelImgWith, labelImgHeight);
    for (var i = 0;i < rectangles.length;i++) {
          let r = rectangles[i];
          if(r.isMod === ''){ //수정중인 것 제외
        	  r.color = otherColor;
              if(isRect){
            	  if (x > r.sx && x < r.ex && y > r.sy && y < r.ey) {
                	  r.color = choosedColor;
                  }   
              }
              else{
            	  if (x-currentX > r.sx && x-currentX < r.ex && y-currentY > r.sy && y-currentY < r.ey) {
            		  r.color = choosedColor;
            	  } 
              }  
              ctx.fillStyle = r.color;
              ctx.fillRect(r.sx*scaleX+(layerX*scaleX), r.sy*scaleY+(layerY*scaleY), (r.ex-r.sx)*scaleX, (r.ey-r.sy)*scaleY);
              ctx.strokeRect(r.sx*scaleX+(layerX*scaleX), r.sy*scaleY+(layerY*scaleY), (r.ex-r.sx)*scaleX, (r.ey-r.sy)*scaleY);
          }    
    }
}

//화면 지우고 모든 도형을 순서대로 다 그림
const drawRectsNormal= () => {
	//console.log("drawRectsNormal");
	//let canvas = document.getElementById('drawLayer');
	let canvas = document.querySelector('.container-wide').querySelector('#drawLayerMain');
	let labelImgWith = labelingMain.img.width;
	let labelImgHeight = labelingMain.img.height;
	let scaleX = labelingMain.scaleX;
	let scaleY = labelingMain.scaleY;
	let layerX = labelingMain.layerX;
	let layerY = labelingMain.layerY;
	let rectangles = labelingMain.arRectangle;
	if(labelTarget != 'main'){
		canvas = document.querySelector('#assetdisplay').querySelector('#drawLayer');
		labelImgWith = labeling.img.width;
		labelImgHeight = labeling.img.height;
		scaleX = labeling.scaleX;
	    scaleY = labeling.scaleY;
	    layerX = labeling.layerX;
	    layerY = labeling.layerY;
	    rectangles = labeling.arRectangle;
	}
	let ctx = canvas.getContext('2d');
	//ctx.clearRect(0,0, labeling.img.width/labeling.scaleX, labeling.img.height/labeling.scaleY);
	ctx.clearRect(0,0, labelImgWith, labelImgHeight);
    for (var i = 0;i < rectangles.length;i++) {
          let r = rectangles[i];
          if(r.isMod === ''){ //수정중인 것 제외
          //ctx.fillStyle = otherColor;
          ctx.fillStyle = r.color;
          ctx.fillRect(r.sx*scaleX+(layerX*scaleX), r.sy*scaleY+(layerY*scaleY), (r.ex-r.sx)*scaleX, (r.ey-r.sy)*scaleY);
          ctx.strokeRect(r.sx*scaleX+(layerX*scaleX), r.sy*scaleY+(layerY*scaleY), (r.ex-r.sx)*scaleX, (r.ey-r.sy)*scaleY);
          //ctx.fillRect(r.sx+labeling.layerX*labeling.scaleX, r.sy+labeling.layerY*labeling.scaleY, r.ex-r.sx, r.ey-r.sy);
          //ctx.strokeRect(r.sx+labeling.layerX*labeling.scaleX, r.sy+labeling.layerY*labeling.scaleY, r.ex-r.sx, r.ey-r.sy);
          }
    }
}

//화면 지우고 모든 도형을 순서대로 다 그림
const drawRectsByIdx= (idx) => {

    let canvas = document.querySelector('#assetdisplay').querySelector('#drawLayer');
	let rectangels = labeling.arRectangle;
	let	labelImgWith = labeling.img.width;
	let	labelImgHeight = labeling.img.height;
	let	scaleX = labeling.scaleX;
	let scaleY = labeling.scaleY;
	let layerX = labeling.layerX;
	let layerY = labeling.layerY;

	let ctx = canvas.getContext('2d');
	ctx.clearRect(0,0, labelImgWith, labelImgHeight);
    for (var i = 0;i < rectangels.length;i++) {
          let r = rectangels[i];
          if(r.isMod === ''){ //수정중인 것 제외
          r.color = otherColor;
          if(r.objIdx === idx){
        	  r.color = choosedColor;
          }
          ctx.fillStyle = r.color;
          //console.log("color:",r.color);
          //console.log("sx:",r.sx*scaleX+(layerX*scaleX),", sy:",r.sy*scaleY+(layerY*scaleY),",ex:",(r.ex-r.sx)*scaleX,",ey:",(r.ey-r.sy)*scaleY);
          ctx.fillRect(r.sx*scaleX+(layerX*scaleX), r.sy*scaleY+(layerY*scaleY), (r.ex-r.sx)*scaleX, (r.ey-r.sy)*scaleY);
          ctx.strokeRect(r.sx*scaleX+(layerX*scaleX), r.sy*scaleY+(layerY*scaleY), (r.ex-r.sx)*scaleX, (r.ey-r.sy)*scaleY);
          //ctx.fillRect(r.sx+labeling.layerX*labeling.scaleX, r.sy+labeling.layerY*labeling.scaleY, r.ex-r.sx, r.ey-r.sy);
          //ctx.strokeRect(r.sx+labeling.layerX*labeling.scaleX, r.sy+labeling.layerY*labeling.scaleY, r.ex-r.sx, r.ey-r.sy);
          }
    }
}

const selectImg = () => {
	changeOn(document.querySelector('.icon_select'));
	labeling.mode = 'select';
	let canvasWrap = document.querySelector('#assetdisplay').querySelector('.canvasWrap');
	canvasWrap.style.cssText = '';
	canvasWrap.style.cssText = labeling.cssTxt + 'cursor:default;';
	if(labeling.scaleX !=  1 || labeling.currentX != 0){
		console.log("scaleX:",labeling.scaleX,",currentX:",labeling.currentX);
		labeling.scaleX = 1;
		labeling.scaleY = 1;
		labeling.currentX = 0;
		labeling.currentY = 0;
		labeling.layerX = 0;
		labeling.layerY = 0;
		let imgCvs = document.querySelector('#assetdisplay').querySelector('#imgLayer');
    	let imgCtx = imgCvs.getContext('2d');
    	imgCtx.clearRect(0,0, imgCvs.width, imgCvs.height);
    	imgCvs.width = labeling.img.width;
    	imgCvs.height = labeling.img.height;
	    imgCtx.scale(labeling.scaleX,labeling.scaleY);
	    imgCtx.drawImage(labeling.img,labeling.layerX,labeling.layerY,labeling.img.width,labeling.img.height);

	    for (let i = 0;i < labeling.arRectangle.length;i++) {
    		let r = labeling.arRectangle[i];
    		if(r.color === choosedColor){
    			drawRectsByIdx(r.objIdx);
    			break;
    		}    		
	    }
	}
}

const selectParentImg = () => {
	changeParentOn(document.querySelector('.icon_select2'));
	labelingMain.mode = 'select';
	let canvasWrapMain = document.querySelector('.container-wide').querySelector('.canvasWrapMain');
	canvasWrapMain.style.cssText = '';
	canvasWrapMain.style.cssText = labelingMain.cssTxt + 'cursor:default;';
	if(labelingMain.scaleX !=  1 || labelingMain.currentX != 0){
		console.log("scaleX:",labelingMain.scaleX,",currentX:",labelingMain.currentX);
		labelingMain.scaleX = 1;
		labelingMain.scaleY = 1;
		labelingMain.currentX = 0;
		labelingMain.currentY = 0;
		labelingMain.layerX = 0;
		labelingMain.layerY = 0;
		let imgCvs = document.querySelector('.container-wide').querySelector('#imgLayerMain');
    	let imgCtx = imgCvs.getContext('2d');
    	imgCtx.clearRect(0,0, imgCvs.width, imgCvs.height);
    	imgCvs.width = labelingMain.img.width;
    	imgCvs.height = labelingMain.img.height;
	    imgCtx.scale(labelingMain.scaleX,labelingMain.scaleY);
	    imgCtx.drawImage(labelingMain.img,labelingMain.layerX,labelingMain.layerY,labelingMain.img.width,labelingMain.img.height);
	    drawRectsNormal();
        /*
	    for (let i = 0;i < labelingMain.arRectangle.length;i++) {
    		let r = labelingMain.arRectangle[i];
    		if(r.color === choosedColor){
    			drawRectsByIdx(r.objIdx);
    			break;
    		}    		
	    }
	    */
	}
}




const drawImg = (item,callbackFn) => {
	labeling.scaleX = 1;
	labeling.scaleY = 1;
	let canvasWrap = document.querySelector('#assetdisplay').querySelector('.canvasWrap');
	let canvas = canvasWrap.querySelector('#imgLayer');
	let canvas1 = canvasWrap.querySelector('#drawLayer');
	
	if(labelTarget == 'main'){
		canvasWrap = document.querySelector('.container-wide').querySelector('.canvasWrapMain');
	    canvas = canvasWrap.querySelector('#imgLayerMain');
	    canvas1 = canvasWrap.querySelector('#drawLayerMain');
	    labelingMain.img = new Image();
	    labelingMain.fileNm = item.fileNm;
	    labelingMain.filePath = item.filePath;
	}
	else{
		labeling.img = new Image();
	    labeling.fileNm = item.fileNm;
	    labeling.filePath = item.filePath;
	}
	let ctx = canvas.getContext('2d');
	let ctx1 = canvas1.getContext('2d');
	if(labelTarget == 'main'){
		labelingMain.img.addEventListener("load", () => {
			ctx.clearRect(0,0, canvasWrap.width, canvasWrap.height);
			ctx1.clearRect(0,0, canvasWrap.width, canvasWrap.height);
			//labeling.arRectangle.length = 0;
		    canvas.width = labelingMain.img.width;
		    canvas.height = labelingMain.img.height;
		    canvas1.width = labelingMain.img.width;
		    canvas1.height = labelingMain.img.height;
		    ctx.drawImage(labelingMain.img,labelingMain.currentX,labelingMain.currentY,labelingMain.img.width,labelingMain.img.height);
		    labelingMain.xOffset = canvasWrap.querySelector("#imgLayerMain").offsetLeft;
			labelingMain.yOffset = canvasWrap.querySelector("#imgLayerMain").offsetTop;
		    if(callbackFn != undefined){
				if(typeof callbackFn === 'function'){
					callbackFn();
				}
				else{
					new Function(callbackFn)();
				}
			}
	    });
	    labelingMain.img.setAttribute("src", "../img/@img_051.png");
	}
	else{
		labeling.img.addEventListener("load", () => {
			ctx.clearRect(0,0, canvasWrap.width, canvasWrap.height);
			ctx1.clearRect(0,0, canvasWrap.width, canvasWrap.height);
			//labeling.arRectangle.length = 0;
		    canvas.width = labeling.img.width;
		    canvas.height = labeling.img.height;
		    canvas1.width = labeling.img.width;
		    canvas1.height = labeling.img.height;
		    ctx.drawImage(labeling.img,labeling.currentX,labeling.currentY,labeling.img.width,labeling.img.height);
		    labeling.xOffset = canvasWrap.querySelector("#imgLayer").offsetLeft;
			labeling.yOffset = canvasWrap.querySelector("#imgLayer").offsetTop;
		    if(callbackFn != undefined){
				if(typeof callbackFn === 'function'){
					callbackFn();
				}
				else{
					new Function(callbackFn)();
				}
			}
	    });
		labeling.img.setAttribute("src", "../img/@img_051.png");
		labeling.mode = 'select';
		
	}
	canvasWrap.style.cssText = '';
	canvasWrap.style.cssText = labeling.cssTxt + 'cursor:default;';
}

const moveImg = () => {
	changeOn(document.querySelector('.icon_movement'));
	labeling.mode = 'move';
	let canvasWrap = document.querySelector('#assetdisplay').querySelector('.canvasWrap');
	canvasWrap.style.cssText = '';
	canvasWrap.style.cssText = labeling.cssTxt + 'cursor:move;';
}

const moveParentImg = () => {
	changeParentOn(document.querySelector('.icon_movement2'));
	labelingMain.mode = 'move';
	let canvasWrapMain = document.querySelector('.canvasWrapMain');
	canvasWrapMain.style.cssText = '';
	canvasWrapMain.style.cssText = labelingMain.cssTxt + 'cursor:move;';
}

const zoominImg = () => {
	if(labeling.mode != 'zoomin'){
    	changeOn(document.querySelector('.icon_enlargement'));
    	labeling.mode = 'zoomin';
    	let canvasWrap = document.querySelector('.canvasWrap');
	    canvasWrap.style.cssText = '';
    	canvasWrap.style.cssText = labeling.cssTxt + 'cursor:zoom-in;';
	}

}
const zoominParentImg = () => {
	//console.log("zoominParentImg............");
	//if(labelingMain.mode != 'zoomin'){
    	changeParentOn(document.querySelector('.icon_enlargement2'));
    	labelingMain.mode = 'zoomin';
    	let canvasWrapMain = document.querySelector('.canvasWrapMain');
    	//console.log("canvasWrapMain:",canvasWrapMain);
    	canvasWrapMain.style.cursor = "zoom-in";
	    //canvasWrapMain.style.cssText = '';
    	//canvasWrapMain.style.cssText = labelingMain.cssTxt + ' cursor:zoom-in;';
	//}

}

const zoomoutImg = () => {
	if(labeling.mode != 'zoomout'){
    	changeOn(document.querySelector('.icon_reduction'));
    	labeling.mode = 'zoomout';
    	let canvasWrap = document.querySelector('.canvasWrap');
    	canvasWrap.style.cursor = "zoom-out";
	    //canvasWrap.style.cssText = '';
    	//canvasWrap.style.cssText = labeling.cssTxt + 'cursor:zoom-out;';
	}
}

const zoomoutParentImg = () => {
	//if(labelingMain.mode != 'zoomout'){
    	changeParentOn(document.querySelector('.icon_reduction2'));
    	labelingMain.mode = 'zoomout';
    	let canvasWrapMain = document.querySelector('.canvasWrapMain');
    	canvasWrapMain.style.cursor = "zoom-out";
	    //canvasWrapMain.style.cssText = '';
    	//canvasWrapMain.style.cssText = labelingMain.cssTxt + 'cursor:zoom-out;';
	//}
}



const drawRect = () => {
	changeOn(document.querySelector('.icon_addition'));
	labeling.mode = 'draw';
	let canvasWrap = document.querySelector('#assetdisplay').querySelector('.canvasWrap');
	canvasWrap.style.cssText = '';
	canvasWrap.style.cssText = labeling.cssTxt + 'cursor:crosshair;';
}

const editRect = () => {
	changeOn(document.querySelector('.icon_correction'));
	labeling.mode = 'edit';
	let canvasWrap = document.querySelector('#assetdisplay').querySelector('.canvasWrap');
	canvasWrap.style.cssText = '';
	canvasWrap.style.cssText = labeling.cssTxt + 'cursor:crosshair;';
}


const deleteRect = () => {
	changeOn(document.querySelector('.icon_delete'));
	labeling.mode = 'delete';
	let canvasWrap = document.querySelector('#assetdisplay').querySelector('.canvasWrap');
	canvasWrap.style.cssText = '';
	canvasWrap.style.cssText = labeling.cssTxt + 'cursor:not-allowed;';
}

const changeOn = (obj) => {
	if(obj.tagName === 'SPAN'){
		obj = obj.parentNode.parentNode;
	}
	else if(obj.tagName === 'A'){
		obj = obj.parentNode;
	}
	const lis = document.querySelector('.clfix').querySelectorAll('li');
	lis.forEach((li) => {
		let anch = li.getElementsByTagName('a');
		
	    anch[0].classList.remove('active');
		if(li === obj){
		    anch[0].classList.add('active');
		}
	});

}

const changeParentOn = (obj) => {
	if(obj.tagName === 'SPAN'){
		obj = obj.parentNode;
	}
    else if(obj.parentNode.tagName === 'SPAN'){
		obj = obj.parentNode.parentNode;
	}
	const anchs = document.querySelector('.container-wide').querySelector('.right').querySelectorAll('a');
	anchs.forEach((anch) => {
	    anch.classList.remove('active');
		if(anch=== obj){
		    anch.classList.add('active');
		}
	});

}

const drawDbRect = () => {
	//console.log("labeling.arRectangle:",labeling.arRectangle);
	
	let imgCvs = document.querySelector('.container-wide').querySelector('#imgLayerMain');
	let drawCvs = document.querySelector('.container-wide').querySelector('#drawLayerMain');
	let rectangels = labelingMain.arRectangle;
	
	let labelImgWith = labelingMain.img.width;
	console.log(labelImgWith);
	let labelImgHeight = labelingMain.img.height;
	let scaleX = labelingMain.scaleX;
	let scaleY = labelingMain.scaleY;
	let layerX = labelingMain.layerX;
	let layerY = labelingMain.layerY;
	let currentX = labelingMain.currentX;
	let currentY = labelingMain.currentY;
	let img = labelingMain.img;
	if(labelTarget != 'main'){
		
		imgCvs = document.querySelector('#assetdisplay').querySelector('#imgLayer');
		drawCvs = document.querySelector('#assetdisplay').querySelector('#drawLayer');
		rectangels = labeling.arRectangle;
		labelImgWith = labeling.img.width;
		labelImgHeight = labeling.img.height;
		scaleX = labeling.scaleX;
	    scaleY = labeling.scaleY;
	    layerX = labeling.layerX;
	    layerY = labeling.layerY;
	    currentX = labeling.currentX;
	    currentY = labeling.currentY;
	    img = labeling.img;
	    
	}

	let imgCtx = imgCvs.getContext('2d');	
	// 좌표 정규화해서 새로운 도형을 배열에 추가
	imgCvs.width = labelImgWith;
	imgCvs.height = labelImgHeight;
	console.log("labelTarget:",labelTarget);
	imgCtx.scale(scaleX,scaleY);
	imgCtx.clearRect(0,0, imgCvs.width, imgCvs.height);
	imgCtx.drawImage(img,currentX,currentY,labelImgWith,labelImgHeight);
    
   
    let drawCtx = drawCvs.getContext('2d');
    drawCvs.width = labelImgWith;
    drawCvs.height = labelImgHeight;
    console.log("drawCvs.width:",drawCvs.width);
    //drawCtx.clearRect(0,0, drawCvs.width, drawCvs.height);
    drawCtx.strokeStyle = "black";
    drawCtx.lineWidth = lineWidth;
    drawCtx.scale(scaleX,scaleY);
    drawCtx.clearRect(0,0, drawCvs.width, drawCvs.height);
    drawRectsNormal();
   
}



const removeObject = (tr) => {
	let trVal = tr.getAttribute('data-value');
	for (let i = 0;i < labeling.arRectangle.length;i++) {
   		let rect = labeling.arRectangle[i];
   		if(rect.objIdx == trVal){
   			labeling.arRectangle.splice(i, 1);
   		}
	}
	tr.remove();
	drawRectsNormal();
}


const selectZoneName = (selectObj) => {
	let trs = document.getElementById('assetdisplay').querySelector('.tBody').querySelectorAll('tr');
	trs.forEach((tr) => {
		if(tr == selectObj){
			if(!tr.classList.contains('trSelect')){
				tr.classList.add('trSelect');
			}
		}
		else{
			if(tr.classList.contains('trSelect')){
				tr.classList.remove('trSelect');
			}
		}
		
	});
	selectTr();

}	


const selectTr = () => {
	console.log("selectTr");
	let trs = document.getElementById('assetdisplay').querySelector('.tBody').querySelectorAll('tr');
	trs.forEach((tr,index) => {
		if(tr.classList.contains('trSelect')){
			for (let i = 0;i < labeling.arRectangle.length;i++) {
		   		let rect = labeling.arRectangle[i];
		   		if(rect.objIdx == tr.getAttribute('data-value')){
					   rect.color = choosedColor; 
		   		}
		   		else{
					   rect.color = otherColor; 
				}
			}
			console.log(index," labeling.arRectangle:",labeling.arRectangle)
			drawRectsNormal();
		}
	});
}
const selectZone = (objIdx) => {
	let trs = document.getElementById('assetdisplay').querySelector('.tBody').querySelectorAll('tr');
	trs.forEach((tr) => {
		if(tr.getAttribute('data-value') == objIdx){
			if(!tr.classList.contains('trSelect')){
				tr.classList.add('trSelect');
			}
		}
		else{
			if(tr.classList.contains('trSelect')){
				tr.classList.remove('trSelect');
			}
		}
		
	});
	selectTr();

}

const editXy = () => {
	console.log("editXy");
	let trs = document.getElementById('assetdisplay').querySelector('.tBody').querySelectorAll('tr');
	if(trs.length > 0){
		trs.forEach((tr) =>{
			if(tr.getAttribute('data-value') == labeling.rectIndex){
				tr.classList.add('trSelect');
				let rect = null;
				for(let i=0;i<labeling.arRectangle.length;i++){
					rect =  labeling.arRectangle[i];
					if(rect.objIdx == labeling.rectIndex){
						break;
					}
				}
				/*
				let x1 = Math.min(labeling.sx, labeling.ex);
	        	let y1 = Math.min(labeling.sy, labeling.ey);
	        	let x2 = Math.max(labeling.sx, labeling.ex);
	        	let y2 = Math.max(labeling.sy, labeling.ey);
	            */
                let tds = tr.querySelectorAll('td');
                tds.forEach((td) =>{
					if(td.getAttribute('data-id') === 'startX'){
						td.textContent = rect.sx.toFixed(0);
						td.setAttribute('data-value',rect.sx);
					}
					else if(td.getAttribute('data-id') === 'startY'){
						td.textContent = rect.sy.toFixed(0);
						td.setAttribute('data-value',rect.sy);
				    }
				    else if(td.getAttribute('data-id') === 'endX'){
						td.textContent = rect.ex.toFixed(0);
						td.setAttribute('data-value',rect.ex);
					}
					else if(td.getAttribute('data-id') === 'endY'){
						td.textContent = rect.ey.toFixed(0);
						td.setAttribute('data-value',rect.ey);
					}
			    });
			}
			else{
				tr.classList.remove('trSelect');
			}
		});
	}
	
}


//x, y 위치의 사각형 찾음. 없으면 -1
const collisionDetection = () => {
	let x1 = Math.min(labeling.sx, labeling.ex);
	let y1 = Math.min(labeling.sy, labeling.ey);
	let x2 = Math.max(labeling.sx, labeling.ex);
	let y2 = Math.max(labeling.sy, labeling.ey);
	for (let i = 0;i < labeling.arRectangle.length;i++) {
		let r = labeling.arRectangle[i];
		if (labeling.ex > r.sx && labeling.ex < r.ex && labeling.ey > r.sy && labeling.ey < r.ey) {
			return true;
	    }
		if (labeling.ex > r.sx && labeling.ex < r.ex && labeling.sy > r.sy && labeling.sy < r.ey) {
			return true;
	    }
		if (r.sx > x1  && r.ex < x2 && r.sy > y1 && r.ey < y2) {
			return true;
	    }
		if (r.sx > x1  && r.sx < x2 && r.sy > y1 && r.sy < y2) {
			return true;
	    }		
		if (r.ex > x1  && r.ex < x2 && r.ey > y1 && r.ey < y2) {
			return true;
	    }
	}
	return false;
}    

