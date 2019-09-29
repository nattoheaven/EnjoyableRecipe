(function() {
    function resizeEvent(e) {
	var screenWidth = window.innerWidth || document.documentElement.clientWidth;
	var screenHeight = window.innerHeight || document.documentElement.clientHeight;
	var fontSize = Math.max(Math.min(screenWidth / 24, screenHeight / 24), 9);
	var resizeStyle = document.getElementById("resize-style");
	if (!resizeStyle) {
	    resizeStyle = document.createElement("style");
	    resizeStyle.setAttribute("id", "resize-style");
	    document.getElementsByTagName("head")[0].appendChild(resizeStyle);
	}
	resizeStyle.textContent = "body,table,input,select,textarea,button{font-size:" + fontSize + "px;}";
    }
    window.addEventListener("load", resizeEvent);
    window.addEventListener("resize", resizeEvent);
})();
