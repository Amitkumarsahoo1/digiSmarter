 function UPIPayment(){
     upiPay("prateek18@dbs", "ANKUR MARCHATTIWAR", "1", "Testing from login");
 }
 function upiPay(vpi, payeeName, amount, note){
     DBS.upiPay(vpi, payeeName, amount, note);
  }

  function loadChatBot() {
    DBS.loadChatBot()
  }