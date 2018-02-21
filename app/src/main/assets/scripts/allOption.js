
function addUserActivityNow(screen) {
console.log(screen);
    DBS.addUserActivity(screen);
    console.log(screen);
callNewPage(screen);
    }

function addActivity(activity) {
    console.log(activity)
    addUserActivityNow(activity);

}


function callNewPage( screen) {
console.log("YO" +screen);

var nextScreen = '';
if(screen == 'travel') {
nextScreen = 'travelpage';
} else if(screen == 'split') {
        nextScreen = 'split';
} else if(screen == 'flightbooking') {
nextScreen = screen;
} else if(screen == 'transfer') {
 nextScreen = 'payment';
 }

        if(null != nextScreen && nextScreen.length > 0) {
        DBS.launchNextPage(nextScreen,1);
        }

}
