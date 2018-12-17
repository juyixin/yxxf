/*
jQuery.msgBox plugi-in 
Copyright 2011, Halil İbrahim Kalyoncu
*/
jQuery.msgBox = msg;
function msg (options) {
    var isShown = false;
    var typeOfValue = typeof options;
    var defaults = {
        content: (typeOfValue == "string" ? options : "Message"),
        title: "消息",
        type: "alert",
        autoClose: false,
        timeOut: 0,
        showButtons: true,
        buttons: [{ value: "Ok"}],
        inputs: [{ type: "text", name:"userName", header: "User Name" }, { type: "password",name:"password", header: "Password"}],
        success: function (result) { },
        beforeShow: function () { },
        afterShow: function () { },
        beforeClose: function () { },
        afterClose: function () { },
        opacity: 0.1
    };
    options = typeOfValue == "string" ? defaults : options;
    if (options.type != null) {
        switch (options.type) {
            case "alert":
                options.title = options.title == null ? "消息" : options.title;
                break;
            case "info":
                options.title = options.title == null ? "提示" : options.title;
                break;
            case "success":
                options.title = options.title == null ? "成功" : options.title;
                break;
            case "error":
                options.title = options.title == null ? "错误" : options.title;
                break;
            case "confirm":
                options.title = options.title == null ? "确认" : options.title;
                options.buttons = options.buttons == null ? [{ value: "Yes" }, { value: "No" }, { value: "Cancel"}] : options.buttons;
                break;
            case "prompt":
                options.title = options.title == null ? "Log In" : options.title;
                options.buttons = options.buttons == null ? [{ value: "Login" }, { value: "Cancel"}] : options.buttons;
                break;
            default:
                image = "alert.png";
        }
    }
    options.timeOut = options.timeOut == null ? (options.content == null ? 500 : options.content.length * 70) : options.timeOut;
    options = $.extend(defaults, options);
    if (options.autoClose) {
        setTimeout(hide, options.timeOut);
    }
    var image = "";
    switch (options.type) {
        case "alert":
            image = "alert.png";
            break;
        case "info":
            image = "info.png";
            break;
        case "success":
            image = "success.jpg";
            break;
        case "error":
            image = "error.png";
            break;
        case "confirm":
            image = "confirm.png";
            break;
        default:
            image = "alert.png";
    }

    var buttons = "";
    $(options.buttons).each(function (index, button) {
        buttons += "<input class=\"msgButton\" type=\"button\" name=\"" + button.value + "\" value=\"" + button.value + "\" />";
    });

    var inputs = "<div class=\"msgInput\"><table>";
    $(options.inputs).each(function (index, input) {
        var type = input.type;
        if (type=="checkbox" || type =="radiobutton") {
        	inputs += "<tr><td class=\"msgInputHeader\"><span>" + input.header + "&nbsp;&nbsp;&nbsp;</td><td><span>" +
            "<input type=\"" + input.type + "\" name=\"" + input.name + "\" "+(input.checked == null ? "" : "checked ='"+input.checked+"'")+" value=\"" + (typeof input.value == "undefined" ? "" : input.value) + "\" onclick=\"" +  (typeof input.onclick == "undefined" ? "" : input.onclick) + "\" /> </td></tr><tr height='5px;'></tr>";
        }else if (type =="radio") {
            inputs +="<input type=\"" + input.type + "\" name=\"" + input.name + "\" "+(input.checked == null ? "" : "checked ='"+input.checked+"'")+" value=\"" + (typeof input.value == "undefined" ? "" : input.value) + "\" />" +
            "&nbsp;<text>"+input.header +"</text>";
        }else if (type =="select") {
            inputs +="<tr><td class=\"msgInputHeader\"><span>" + input.header + "&nbsp;&nbsp;&nbsp;</td><td><span>" +
            "<select name=\"" + input.name + "\" id=\"" + input.name + "\" class='mar-B5'/></td></tr><tr height='5px;'></tr>";
        }else if (type =="hidden") {
        	inputs += "<tr><td class=\"msgInputHeader\">" +
            "<input type=\"" + input.type + "\" name=\"" + input.name + "\" value=\"" + (typeof input.value == "undefined" ? "" : input.value) + "\" class='mar-B5'/>" +
            "</td></tr>";
        } else {
        	var helpText = input.helpText;
        	var helpTextContent = "";
        	var autoCompleteDataList = "";
        	if(helpText != undefined && helpText != ""){
        		helpTextContent = '<span class="style16">' + input.helpText + '<span>';
            }
        	if(input.autocomplete != undefined && input.autocomplete != ""){
             	autoCompleteDataList = "<datalist id=\"" + input.datalistId + "\"> </datalist>";
            }
            inputs += "<tr><td class=\"msgInputHeader\"><span>" + input.header + "&nbsp;&nbsp;&nbsp;</td><td><span>" +
            "<input onblur=\"" + (typeof input.onblur == "undefined" ? "" : input.onblur) + "\"  type=\"" + input.type + "\" name=\"" + input.name + "\" id=\"" +  (typeof input.id == "undefined" ? "" : input.id) + "\"  value=\"" + (typeof input.value == "undefined" ? "" : input.value) + "\" list=\"" + (typeof input.autocomplete == "undefined" ? "" : input.datalistId) + "\" autocomplete=\"" + (typeof input.autocomplete == "undefined" ? "" : "off") + "\" class='mar-B5'/></td></tr><tr height='5px;'><td></td><td>"+helpTextContent+"</td></tr>" +
            autoCompleteDataList ;
        }
    });
    inputs+="</table></div>";
    var divBackGround = "<div class=\"msgBoxBackGround\"></div>"
    var divTitle = "<div class=\"msgBoxTitle\">" + options.title + "</div>";
    var divContainer = "<div class=\"msgBoxContainer\" ";
    if(options.content.length > 1000){
    	divContainer=divContainer+" style=\"height:200px;overflow:auto;\" ";
    }
    divContainer=divContainer+" ><div class=\"msgBoxImage\"><img src=\"images/msgbox/" + image + "\"/></div><div class=\"msgBoxContent\"><p><span>" + options.content + "</span></p></div></div>";
    var divButtons = "<div class=\"msgBoxButtons\">" + buttons + "</div>";
    var divInputs = "<div class=\"msgBoxInputs\">" + inputs + "</div>";

    if ($("div.msgBox").length == 0) {
        if (options.type == "prompt") {
            $("body").append(divBackGround + "<div class=\"msgBox\">" + divTitle + "<div>" + divContainer + (options.showButtons ? divButtons + "</div>" : "</div>") + "</div>");
            $("div.msgBoxImage").remove();
            $("div.msgBoxButtons").css({"text-align":"center","margin-top":"5px"});
            var  height= options.height;
            if(height != undefined){
            	$("div.msgBoxContainer").css({"width":"100%","max-height":height+"px","overflow-y":"auto","overflow-x":"hidden"});
            	 $("div.msgBoxContent").css({"width":"100%","height":"100%"});
            }else{
            	 $("div.msgBoxContent").css({"width":"100%","height":"100%"});
            }
           
            $("div.msgBoxContent").html(divInputs);
        }
        else {
            $("body").append(divBackGround + "<div class=\"msgBox\">" + divTitle + "<div>" + divContainer + (options.showButtons ? divButtons + "</div>" : "</div>") + "</div>");
        }
    }
    else {
        if (options.type == "prompt") {
            $("div.msgBox").html(divTitle + "<div>" + divContainer + (options.showButtons ? divButtons + "</div>" : "</div>"));
            $("div.msgBoxImage").remove();
            $("div.msgBoxContent").css("width", "100%");
            $("div.msgBoxContent").html(divInputs);
        }
        else {
            $("div.msgBox").html(divTitle + "<div>" + divContainer + (options.showButtons ? divButtons + "</div>" : "</div>"));
        }
    }

    var width = $("div.msgBox").width();
    var height = $("div.msgBox").height();
    var windowHeight = $(window).height();
    var windowWidth = $(window).width();

    var top = windowHeight / 2 - height / 2;
    var left = windowWidth / 2 - width / 2;

    show();

    function show() {
        if (isShown) {
            return;
        }
        $("div.msgBox").css({ opacity: 0, top: top - 50, left: left });
        $("div.msgBoxBackGround").css({ opacity: options.opacity });
        options.beforeShow();
        $("div.msgBoxBackGround").css({ "width": $(document).width(), "height": getDocHeight() });
        $("div.msgBox,div.msgBoxBackGround").fadeIn(0);
        $("div.msgBox").animate({ opacity: 1, "top": top, "left": left }, 200);
        setTimeout(options.afterShow, 200);
        isShown = true;
        $(window).bind("resize", function (e) {
            var width = $("div.msgBox").width();
            var height = $("div.msgBox").height();
            var windowHeight = $(window).height();
            var windowWidth = $(window).width();

            var top = windowHeight / 2 - height / 2;
            var left = windowWidth / 2 - width / 2;

            $("div.msgBox").css({ "top": top, "left": left });
        });
    }

    function hide() {
        if (!isShown) {
            return;
        }
        options.beforeClose();
        $("div.msgBox").animate({ opacity: 0, "top": top - 50, "left": left }, 200);
        $("div.msgBoxBackGround").fadeOut(300);
        setTimeout(function () { $("div.msgBox,div.msgBoxBackGround").remove(); }, 300);
        setTimeout(options.afterClose, 300);
        isShown = false;
    }

    function getDocHeight() {
        var D = document;
        return Math.max(
        Math.max(D.body.scrollHeight, D.documentElement.scrollHeight),
        Math.max(D.body.offsetHeight, D.documentElement.offsetHeight),
        Math.max(D.body.clientHeight, D.documentElement.clientHeight));
    }

    function getFocus() {
        $("div.msgBox").fadeOut(200).fadeIn(200);
    }

    $("input.msgButton").click(function (e) {
        e.preventDefault();
        var value = $(this).val();
        if (options.type != "prompt") {
        	var path = window.location.pathname.split( '/' );
        //    var currentURL = document.URL;
        	var pathURL = window.location.hash;
        	var pathName = pathURL.split("/");        	
            // close tab after operate
            	if( (path[1] == 'showTaskFormDetail' || path[1] == 'showTaskFormDetailForReader' || pathName[2] == "saveRederDetails" || pathName[2] == "removeRederDetails") && value =="Ok" ) {
                    options.success(value);
                    if(options.type == "success" && options.content.indexOf("Opinion") != 0 && options.content.indexOf("Bulk Read Success") != 0) {
                        window.close();	
                    } else {
                    	hide();
                    }
            	} else {
                    options.success(value);
                    hide();
            	}
        }
        else {
            var inputValues = [];
            $("div.msgInput input").each(function (index, domEle) {
                var name = $(this).attr("name");
                var value = $(this).val();
                var type = $(this).attr("type");
                if (type == "checkbox" || type == "radiobutton") {
                    inputValues.push({ name: name, value: value,checked: $(this).attr("checked")});
                }
                else {
                    inputValues.push({ name: name, value: value });
                }
            });
            $("div.msgInput select").each(function (index, domEle) {
                var name = $(this).attr("name");
                var id = $(this).attr("id");
                var value = $(this).val();
                var label = $("#"+id+" option[value='"+value+"']").text();
                inputValues.push({ name: name, value: value, label: label});
            });
            options.success(value,inputValues);
            hide();
        }
        
    });

    $("div.msgBoxBackGround").click(function (e) {
        if (!options.showButtons || options.autoClose) {
            hide();
        }
        else {
            getFocus();
        }
    });
};
