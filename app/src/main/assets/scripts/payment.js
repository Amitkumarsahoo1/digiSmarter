function pay() {
    //alert(getDealsOffers());
    payee = document.getElementById('name').value;
    account = document.getElementById('accountnumber').value;
    amount = document.getElementById('amount').value;
    category = document.getElementById('category').value;
    addUserTxn(payee, account, amount, category);
    //alert(getDealsOffers());
    DBS.showToast("Payment successful");
}