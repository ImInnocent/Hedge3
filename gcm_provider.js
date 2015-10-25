var gcm = require('node-gcm');
var fs = require('fs');

var message = new gcm.Message();

var message = new gcm.Message({
    collapseKey: 'demo',
    delayWhileIdle: true,
    timeToLive: 3,
    data: {
        title: 'saltfactory GCM demo',
        message: 'Google Cloud Messaging Å×½ºÆ®',
        custom_key1: 'custom data1',
        custom_key2: 'custom data2'
    }
});

var server_api_key = 'AIzaSyDwipCJy_lTDppme9GJNupB161sEe_x8CM';
var sender = new gcm.Sender(server_api_key);
var registrationIds = [];

var token = 'cWA1YmNyPVk:APA91bFxhMb-KK3fCaOqUIlfHcOx79xPWi-_UjtCPmsyrVvG0qI0hC2sIFcfSuQce4Yug0ax9ihUniJ49jtGyaGCHrhw65Tj_UP3IeN9t0WABvZdZAKI54wibrjAbuboH1L3PFDE36Lx';
registrationIds.push(token);

sender.send(message, registrationIds, 4, function (err, result) {
    console.log(result);
});