
var user = [];
function signUp() {

    var name = document.getElementById('name').value;
    var username = document.getElementById('username').value;
    var password = document.getElementById('password').value;
    var email = document.getElementById('email').value;
    DBS.signUp(name,username, password, email);
    DBS.showToast("Signup success");
}

function redirectToLogin() {
    DBS.redirectToLogin();
}
