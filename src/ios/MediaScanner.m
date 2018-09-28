#import <Cordova/CDV.h>
#import <Photos/Photos.h>

@interface MediaScanner : CDVPlugin {
}
- (void)mediaImageScan:(CDVInvokedUrlCommand*)command;
- (void)mediaVideoScan:(CDVInvokedUrlCommand*)command;
- (void)checkPermission:(CDVInvokedUrlCommand*)command;
@end

@implementation MediaScanner
- (void)mediaImageScan:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString* mediaPath = [command.arguments objectAtIndex:0];
    if (mediaPath != nil && [mediaPath length] > 0) {
        NSString* filteredPath = mediaPath;
        if ([mediaPath hasPrefix:@"file://"]) {
            filteredPath = [mediaPath substringFromIndex:7];
        }

        UIImage* image = [UIImage imageWithContentsOfFile:filteredPath];
        if (image != nil) {
            UIImageWriteToSavedPhotosAlbum(image, nil, nil, nil);
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:filteredPath];
        }else {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Not valid image file!"];
        }
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Media file path error!"];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId]; 
}

- (void)mediaVideoScan:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString* mediaPath = [command.arguments objectAtIndex:0];
    if (mediaPath != nil && [mediaPath length] > 0) {
        NSString* filteredPath = mediaPath;
        if ([mediaPath hasPrefix:@"file://"]) {
            filteredPath = [mediaPath substringFromIndex:7];
        }

        UISaveVideoAtPathToSavedPhotosAlbum(filteredPath, nil, nil, nil);
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:filteredPath];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Media file path error!"];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId]; 
}

- (void)checkPermission:(CDVInvokedUrlCommand*)command 
{
    CDVPluginResult* pluginResult = nil;
    PHAuthorizationStatus status = [PHPhotoLibrary authorizationStatus];
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"No permission!"];

    if (status == PHAuthorizationStatusAuthorized) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    }else if (status == PHAuthorizationStatusNotDetermined) {
        [PHPhotoLibrary requestAuthorization:^(PHAuthorizationStatus status){

        }];  
    }
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId]; 
}
@end
