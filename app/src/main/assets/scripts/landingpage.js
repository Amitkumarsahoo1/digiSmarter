/*var slideIndex = 1;
showDivs(slideIndex);

function plusDivs(n) {
  showDivs(slideIndex += n);
}

function showDivs(n) {
  var i;
  var x = document.getElementsByClassName("mySlides");
  if (n > x.length) {slideIndex = 1}
  if (n < 1) {slideIndex = x.length}
  for (i = 0; i < x.length; i++) {
     x[i].style.display = "none";
  }
  x[slideIndex-1].style.display = "block";
}*/

function loadAllOptions() {
    DBS.loadAllOptions();

}


function addUserActivity(screen) {
    DBS.addUserActivity(screen);
}

function getUserActivity() {
    return DBS.getUserActivity();
}

function checkForFooterIcons() {

var x = [];
x = getUserActivity();

if(null != x && x.length > 0) {
    x = JSON.parse(x);

    for(var i = 0 ; i < x.length ; i++) {
        var obj = {};
        obj= x[i];

        var icon = "";
        switch(obj.screen) {
            case 'travel':
            icon = "fas fa-road";
            displayName = "Travel";
            break;

            case 'addMoney':
                icon = "fas fa-plus";
                displayName = "Add Money";
                break;

            case 'qrCode':
                icon = "fas fa-qrcode";
                displayName = "QR Code";
            break;

            case 'split':
                icon = "fas fa-columns";
                displayName = "Split";
            break;

            case 'deals':
                icon = "far fa-handshake";
                  displayName = "Deals";
            break;

            case 'payBills':
                icon = "fab fa-amazon-pay";
                 displayName = "Pay";
            break;

            case 'request':
            icon = "fab fa-get-pocket";
               displayName = "Request";
            break;

            case 'shop':
            icon = "fas fa-shopping-cart";
               displayName = "Shop";
            break;

            case 'recharge':
                icon = "fas fa-mobile";
              displayName = "Recharge";
            break;

            case 'transfer':
            icon = "fas fa-exchange-alt";
              displayName = "Transfer";
            break;

            case 'movie':
                icon = "fas fa-film";
               displayName = "Movie";
            break;

            case 'payee':
                icon = "fas fa-user-plus";
              displayName = "payee";
            break;
        }
        document.getElementById("mainFooter").style.display = "block";

        if(i == 0) {

            var footerItem  = document.getElementById("footerItem1");
            footerItem.style.display="block";
            while (footerItem.hasChildNodes()) {
                footerItem.removeChild(footerItem.lastChild);
            }
            footerItem.innerHTML = "<span onclick='la' class='"+ icon + "' style='font-size:65px; border:none' id='span1'></span>";


        } else if(i ==  1) {
        var footerItem  = document.getElementById("footerItem2");
                    footerItem.style.display="block";
                    while (footerItem.hasChildNodes()) {
                        footerItem.removeChild(footerItem.lastChild);
                    }
                    footerItem.innerHTML = "<span class='"+ icon + "' style='font-size:65px; border:none' id='span2'></span>";

        } else if(i==2) {
        var footerItem  = document.getElementById("footerItem3");
                    footerItem.style.display="block";
                    while (footerItem.hasChildNodes()) {
                        footerItem.removeChild(footerItem.lastChild);
                    }
                    footerItem.innerHTML = "<span class='"+ icon + "' style='font-size:65px; border:none' id='span3'></span>";

        } else if(i==3) {
            var footerItem  = document.getElementById("footerItem4");
                        footerItem.style.display="block";
                        while (footerItem.hasChildNodes()) {
                            footerItem.removeChild(footerItem.lastChild);
                        }
                        footerItem.innerHTML = "<span class='"+ icon + "' style='font-size:65px; border:none' id='span4'></span>";

        }
    }


}

}


window.onload = function(){
checkForFooterIcons();
updateOffers();
};

function updateOffers(){
    var response = getDealsOffers();
    var jsonArray = JSON.parse(response);

//document.getElementById('dealsnoffers').innerHTML = '';
    document.getElementById('offer1').innerHTML = jsonArray[0].deals_offers;
    document.getElementById('offer2').innerHTML = jsonArray[1].deals_offers;

    /*for(var i = 0; i < jsonArray.length; i++){

        var divValue = '<div class="item"> <div class="" style="width:100%;height: 100px"> <div class=""> <div class="col-xs-1"></div><div class="col-xs-10" style="margin-top: 35px;">'+jsonArray[i].deals_offers+'</div></div></div></div>';
        alert(divValue);
        document.getElementById('dealsnoffers').innerHTML += divValue;
    }*/
    //console.log(test);
}
function closeChatBox() {
    document.getElementById('chat-container-div').style.display = 'none';
    document.getElementById('mic-content').style.display = 'block';
}

function showChatBox() {
    document.getElementById('chat-container-div').style.display = 'block';
    document.getElementById('mic-content').style.display = 'none';
}
