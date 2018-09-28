var exec = require('cordova/exec');

exports.checkPermission = function (success, error) {
    exec(success, error, 'MediaScanner', 'checkPermission', []);
};

exports.mediaImageScan = function (arg0, success, error) {
    exec(success, error, 'MediaScanner', 'mediaImageScan', [arg0]);
};

exports.mediaVideoScan = function (arg0, success, error) {
    exec(success, error, 'MediaScanner', 'mediaVideoScan', [arg0]);
};