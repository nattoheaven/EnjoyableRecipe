function slide(images) {
    var iImage = 0;
    function f(e) {
	document.getElementsByTagName("body")[0].setAttribute("style", "background-image:url(\"" + images[iImage].image + "\");background-position:" + images[iImage].position + ";background-size:" + images[iImage].size + ";background-repeat:no-repeat;background-attachment:fixed;");
	iImage = (iImage + 1) % images.length;
    };
    setInterval(f, 5000);
    window.addEventListener("load", f);
}
