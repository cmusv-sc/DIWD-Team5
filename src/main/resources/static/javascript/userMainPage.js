/**
 * 
 */

function ReadCookie(cookieName) {
	var theCookie = " " + document.cookie;
	var ind = theCookie.indexOf(" " + cookieName + "=");
	if (ind == -1)
		ind = theCookie.indexOf(";" + cookieName + "=");
	if (ind == -1 || cookieName == "")
		return "";
	var ind1 = theCookie.indexOf(";", ind + 1);
	if (ind1 == -1)
		ind1 = theCookie.length;
	return unescape(theCookie.substring(ind + cookieName.length + 2, ind1));
}

function initial() {
	document.getElementById("myProfile").innerHTML = ("User: " + ReadCookie("email"));
}
