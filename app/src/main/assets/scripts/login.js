function login(){
    DBS.callFingerPrint();
}

function launchNextPage(screen){
    DBS.launchNextPage(screen, 1);
}

function readSms(){
    return DBS.readSms();
}

function launchCamera(){
    DBS.launchCamera();
}

function showAlert(message){
    DBS.showToast(message);
}

function loadSignUpPage() {
    DBS.loadSignUpPage();
    /*DBS.addUserTxn('Deccan Pavilion', '11232131', '2000.00', 'DINE');
    DBS.addUserTxn('Barbeque Nation', '10090113', '1000.00', 'DINE');
    DBS.addUserTxn('The Link@Sheraton Cafe', '2313123', '1500.00', 'DINE');*/
}

function loginDBSUser() {
    var username = document.getElementById('userName').value;
    var password = document.getElementById('password').value;

    if(null != username && username.length >0 && null != password && password.length >0) {
        var message = DBS.loginUserWithPassword(username,password);
    } else {
        alert('Please provide username and password');
    }

    }

function callJSFunction() {
    DBS.callJavaScriptFunction('showAlert', 'Success');
}

function addUserActivity(screen) {
    DBS.addUserActivity(screen);
}

function getUserActivity() {
    return DBS.getUserActivity();
}

function addUserTxn(payeeName, payeeAcc, amount, category) {
    DBS.addUserTxn(payeeName, payeeAcc, amount, category);
}

function getUserTxns() {
    return DBS.getUserTxns();
}

function getDealsOffers() {
    return DBS.getDealsOffers();
}

function sendDigiBankRequest() {
    sendSMS('7207244657', 'digibank by DBS by DBS Bank LTD. https://itunes.apple.com/in/app/digibank-by-dbs/id1057836974?mt=8');
    showAlert("An invite has been sent to Amar");
}

function sendSMS(number, message) {
    DBS.sendSMS(number, message);
}

function upiPay(payeeName, amount) {
    DBS.upiPay(payeeName, amount);
}

function getBeneficiaryVPA(beneficiary) {
    return DBS.getBeneficiaryVPA(beneficiary);
}


