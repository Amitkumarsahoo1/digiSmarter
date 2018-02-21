
function fetchBalance() {
 DBS.fetchBalance();
setTimeout(function(){
document.getElementById("balance").style.display="block";

},3000);

}
function loadBranch() {
document.getElementById("branch").style.display="block";

}


function showAccNumberInput() {
document.getElementById("accNumber").style.display="block";

}

function showSendButton() {
document.getElementById("transfer").style.display="block";

}

function loadBanks() {
document.getElementById("banks").style.display="block";
document.getElementById("amount").style.display="block";

}

function transferMoney() {
var accountNumber = "3376";
           var amount = document.getElementById('amountToSend').value;
  DBS.transferOfflineMoney(accountNumber,amount);

}
