function f(json) {
    var title = document.getElementById("title");
    title.textContent = json[0].title;
    var h1 = document.getElementById("h1");
    h1.textContent = json[0].title;
    var gaiyo = document.getElementById("gaiyo");
    gaiyo.textContent = json[0].gaiyo + " (Click to show details.)";
    var overview = document.getElementById("overview");
    overview.textContent = json[0].overview;
    var googleMapsAnchor = document.createElement("a");
    googleMapsAnchor.setAttribute("href", "https://maps.google.com/maps?hl=en&q=" + json[0].latitude + "," + json[0].longitude);
    googleMapsAnchor.textContent = json[0].address + " (Click to Google Maps.)";
    var googleMaps = document.getElementById("google-maps");
    googleMaps.appendChild(googleMapsAnchor);
    var access = document.getElementById("access");
    var accessTokens = json[0].access.split("\n");
    for (var i = 0; i < accessTokens.length; ++i) {
	var accessItem = document.createElement("li");
	accessItem.textContent = accessTokens[i];
	access.appendChild(accessItem);
    }
    if (json[0].prices) {
	var pricesAnchor = document.createElement("a");
	pricesAnchor.setAttribute("href", "/static/" + json[0].prices);
	pricesAnchor.textContent = "Prices.";
	var pricesParagraph = document.createElement("p");
	pricesParagraph.appendChild(pricesAnchor);
	var prices = document.getElementById("prices");
	prices.appendChild(pricesParagraph);
    }
    if (json[0].coupon) {
	var couponAnchor = document.createElement("a");
	couponAnchor.setAttribute("href", "/static/" + json[0].coupon);
	couponAnchor.textContent = "Advantageous coupon!";
	var couponParagraph = document.createElement("p");
	couponParagraph.appendChild(couponAnchor);
	var coupon = document.getElementById("coupon");
	coupon.appendChild(couponParagraph);
    }
    if (json[0].message) {
	var messageAnchor = document.createElement("a");
	messageAnchor.setAttribute("href", "/static/" + json[0].message);
	messageAnchor.textContent = "Staffs' message for you.";
	var messageParagraph = document.createElement("p");
	messageParagraph.appendChild(messageAnchor);
	var message = document.getElementById("message");
	message.appendChild(messageParagraph);
    }
}
