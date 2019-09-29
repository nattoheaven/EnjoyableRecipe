function f(json) {
    var title = document.getElementById("title");
    title.textContent = json[0].title;
    var h1 = document.getElementById("h1");
    h1.textContent = json[0].title;
    var num_seats = document.getElementById("num_seats");
    num_seats.textContent = json[0].num_seats;
    var smoke = document.getElementById("smoke");
    smoke.textContent = json[0].smoke;
    var wifi = document.getElementById("wifi");
    wifi.textContent = json[0].wifi;
    var english_menu = document.getElementById("english_menu");
    if (json[0].english_menu != null) {
	if (json[0].english_menu != '') {
	    var english_menuAnchor = document.createElement("a");
	    english_menuAnchor.setAttribute("href", "/static/" + json[0].english_menu);
	    english_menuAnchor.textContent = "Yes.";
	    english_menu.appendChild(english_menuAnchor);
	} else {
	    english_menu.textContent = "Yes.";
	}
    } else {
	english_menu.textContent = "No.";
    }
    var credit_card = document.getElementById("credit_card");
    credit_card.textContent = json[0].credit_card;
    var genre = document.getElementById("genre");
    genre.textContent = json[0].genre;
    var hours = document.getElementById("hours");
    var hoursTokens = json[0].hours.split("\n");
    for (var i = 0; i < hoursTokens.length; ++i) {
	if (i != 0) {
	    hours.appendChild(document.createElement("br"));
	}
	hours.appendChild(document.createTextNode(hoursTokens[i]));
    }
}
