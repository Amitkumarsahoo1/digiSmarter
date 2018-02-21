function getFlightBookingList(){
    //alert('test');
    var source = document.getElementById('source').value;
    var dest = document.getElementById('destination').value;
    var date = document.getElementById('dayofjourney').value;
    var e = document.getElementById("monthofjourney");
    var month = e.options[e.selectedIndex].value;
    e = document.getElementById("yearofjourney");
    var year = e.options[e.selectedIndex].value;
    var text = {source:''+ source +'',destination:'' + dest + '',date:''+ date +'',month:'' + month + '',year:''+ year +''};
    var obj = JSON.parse('{"source":"Kolkata","dest":"Hyderabad","date":"25","month":"February","year":"2018"}');
    //alert(document.getElementById('source'));
    $.ajax({
        url: 'http://100.0.1.125:5002/getflights',
        type: 'POST',
        data: JSON.stringify(text),
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        async: false,
        success: function(msg) {
            showBotMessage(msg);
        }
    });
}

function showBotMessage(response) {
    var text = response;
    //var jsonData = JSON.parse(response);
    //alert(response);
    for (var i = 0; i < response.length; i++) {
        var div = '<div class="panel panel-primary" style="width:100%"><div class="panel-heading">Flight Detail</div><div class="panel-body"><div class="row"> <div class="airline-detail"> <div class="col-xs-6"> Airline </div><div class="col-xs-6"> '+response[i].airline+' </div></div><div class="arrival-detail"> <div class="col-xs-6"> Arrival </div><div class="col-xs-6"> '+ response[i].arrdate +' </div></div><div class="arrival-detail"> <div class="col-xs-6"> Departure </div><div class="col-xs-6"> '+response[i].depdate+' </div></div><div class="arrival-detail"> <div class="col-xs-6">Gross Charge </div><div class="col-xs-6"> Rs '+response[i].grosscharge+' </div></div></div></div></div></div>';
         document.getElementById('airlineresult').innerHTML += div;
    }
    //alert(response);
}

function clickme(){
    //alert('test');
}