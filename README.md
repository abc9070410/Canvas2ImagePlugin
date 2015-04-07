# Canvas2ImagePlugin

Forked from https://github.com/devgeeks/Canvas2ImagePlugin

### Some changes as following:

1. The user could decide the stored image name. 
2. The user has to extract and encode the image data from the canvas by himself.
3. remove the support of windows phone 8 cause there was the related feature in WinJS. 

## Installation

### For Cordova 3.0.x:

1. To add this plugin just type: `cordova plugin add https://github.com/devgeeks/Canvas2ImagePlugin.git` or `phonegap local plugin add https://github.com/devgeeks/Canvas2ImagePlugin.git`
2. To remove this plugin type: `cordova plugin remove org.devgeeks.Canvas2ImagePlugin` or `phonegap local plugin remove org.devgeeks.Canvas2ImagePlugin`

### NOTE: For older versions of Cordova (You will probably have to use tag 0.2.0)

## Usage:

Call the `window.canvas2ImagePlugin.saveImageDataToLibrary()` method using success and error callbacks and the image data and the image name :

### Example
```html
<canvas id="myCanvas" width="165px" height="145px"></canvas>
```

```javascript
function onDeviceReady()
{
    var canvas = document.getElementById("myCanvas");
    var imageData = canvas.toDataURL("image/png");
    var imageFileName = "myImage.png";

	window.canvas2ImagePlugin.saveImageDataToLibrary(
        function(msg){
            console.log(msg);
        },
        function(err){
            console.log(err);
        },
        imageData, // the image data encoded as base64
        imageFileName // the iamge name for storage
    );
}
```

## License

The MIT License

Copyright (c) 2011 Tommy-Carlos Williams (http://github.com/devgeeks)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
