function setSwipeUrl(left, right) {
    var startX = null;
    var startY = null;
    var currentX = null;
    var currentY = null;
    function startF(e) {
	if (startX == null) {
	    startX = e.screenX;
	}
	if (startY == null) {
	    startY = e.screenY;
	}
    }
    document.addEventListener("touchstart", function(e) {
	startF(e.changedTouches[0]);
    });
    document.addEventListener("mousedown", function(e) {
	startF(e);
    });
    function moveF(e) {
	if (startX != null) {
	    currentX = e.screenX;
	}
	if (startY != null) {
	    currentY = e.screenY;
	}
    }
    document.addEventListener("touchmove", function(e) {
	moveF(e.changedTouches[0]);
    });
    document.addEventListener("mousemove", function(e) {
	moveF(e);
    });
    function endF() {
	var sx = startX;
	var sy = startY;
	var cx = currentX;
	var cy = currentY;
	startX = null;
	startY = null;
	currentX = null;
	currentY = null;
	if (sx != null && sy != null && cx != null && cy != null) {
	    var diffX = cx - sx;
	    var diffY = cy - sy;
	    if (diffX * diffX > 3 * diffY * diffY) {
		if (left && diffX < 0) {
		    document.location.href = left;
		}
		if (right && diffX > 0) {
		    document.location.href = right;
		}
	    }
	}
    }
    document.addEventListener("touchend", function(e) {
	endF();
    });
    document.addEventListener("mouseup", function(e) {
	endF();
    });
}
