# cordova-plugin-media-scanner

## 0. Index
1. [Description](#1-description)
2. [Installation](#2-installation)
3. [Usage](#3-usage)

## 1. Description
### Supported platforms
- Android
- iOS

## 2. Installation
### CLI
```
$ cordova plugin add https://github.com/nagygyula21/cordova-plugin-media-scanner.git
```
### PhoneGap Build
Just add the following xml to your `config.xml`
```
<plugin name="cordova-plugin-media-scanner" spec="https://github.com/nagygyula21/cordova-plugin-media-scanner.git" />
```

## 3. Usage
### Plugin provider
```
import { Injectable } from '@angular/core';
import { Plugin, Cordova, CordovaProperty, CordovaInstance, InstanceProperty, IonicNativePlugin } from '@ionic-native/core';

@Plugin({
  pluginName: 'MediaScanner',
  plugin: 'cordova-plugin-media-scanner', 
  pluginRef: 'MediaScanner', 
  repo: 'https://github.com/nagygyula21/cordova-plugin-media-scanner.git', 
  platforms: ['Android', 'iOS'] 
})

@Injectable()
export class MediaScanner extends IonicNativePlugin {

  @Cordova()
  checkPermission(): Promise<any> {
    return; 
  }

  @Cordova()
  mediaImageScan(mediaPath: string): Promise<any> {
    return; 
  }

  @Cordova()
  mediaVideoScan(mediaPath: string): Promise<any> {
    return; 
  }

}
```
### Code
```
import { MediaScanner } from '../providers/media-scanner/media-scanner';
...
constructor(...., mediaScanner: MediaScanner, ...)
...

let mediaFilePath:string = "file:///....";
this.mediaScanner.checkPermission()
.then(() => {
  this.mediaScanner.mediaImageScan(mediaFilePath)
  .then( filePath => {
    console.log("Scanned file: " + filePath);
  })
  .catch(err => {
    console.log(err);
  });
})
.catch(err => {
  console.log(err);
});
```