function setLeft(itemId, itemWidth, itemIndex, noOfColumns){
	var left=parseInt(itemWidth)*(parseInt(itemIndex)%parseInt(noOfColumns));//+(parseInt(itemIndex)*10);
	document.getElementById(itemId).style.left=left+'px';
}

function setTop(itemId, itemHeight, itemIndex, noOfColumns){	
	var top = parseInt(itemHeight)*(parseInt(parseInt(itemIndex)/parseInt(noOfColumns)))+(parseInt(parseInt(itemIndex)/parseInt(noOfColumns))*10);
	document.getElementById(itemId).style.top=top+'px';
}

function setHeight(itemId, itemHeight, itemIndex){	
	var height = parseInt(itemHeight)*parseInt(itemIndex)+10;
	document.getElementById(itemId).style.height=height+'px';
}