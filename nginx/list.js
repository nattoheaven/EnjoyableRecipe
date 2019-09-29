function f(json) {
    var li = document.getElementById("list");
    for (var i = 0; i < json.length; ++i) {
	var item = json[i];
	var dta = document.createElement("a");
	dta.setAttribute("href", "./?id=" + json[i].id + "&method=overview");
	dta.textContent = json[i].title;
	var dt = document.createElement("dt");
	dt.appendChild(dta);
	li.appendChild(dt);
	var dda = document.createElement("a");
	dda.setAttribute("href", "https://maps.google.com/maps?hl=en&q=" + json[i].latitude + "," + json[i].longitude);
	dda.textContent = json[i].address;
	var dd = document.createElement("dd");
	dd.appendChild(dda);
	li.appendChild(dd);
    }
}
