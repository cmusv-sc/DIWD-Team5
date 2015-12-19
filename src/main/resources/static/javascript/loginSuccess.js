/**
 * 
 */
var params = {};
var email;

function SetCookie(cookieName, cookieValue) {
	var today = new Date();
	var expire = new Date();
	var nDays = 1;
	expire.setTime(today.getTime() + 3600000 * 24 * nDays);
	document.cookie = cookieName + "=" + escape(cookieValue) + ";expires="
			+ expire.toGMTString();
}

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
	var param_array = window.location.href.split('?')[1].split('&');
	for ( var i in param_array) {
		x = param_array[i].split('=');
		params[x[0]] = x[1];
	}
	email = params['email'].replace(/%40/, "@");
	console.log(email);
	console.log(params);
	var test = null;
	$.ajax({
		url : "login",
		type : "GET",
		contentType : "applcation/json",
		data : params
	}).done(function(data) {
		var data = JSON.parse(data);
		console.log(data);
		if (data["OK"] == "0") {
			alert("Email or password not correct!");
			window.location.href = "loginAndSignup.html";
		} else {
			SetCookie("email", email);
			console.log(ReadCookie("email"));
			document.getElementById("myProfile").innerHTML = ("User: " + ReadCookie("email"));
			alert("Welcome, " + email);
			window.location.href = "userMainPage.html";
		}
	});
}
