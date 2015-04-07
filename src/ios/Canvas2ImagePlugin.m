//
//  Canvas2ImagePlugin.m
//  Canvas2ImagePlugin PhoneGap/Cordova plugin
//
//  Created by Tommy-Carlos Williams on 29/03/12.
//  Copyright (c) 2012 Tommy-Carlos Williams. All rights reserved.
//	MIT Licensed
//

#import "Canvas2ImagePlugin.h"
#import <Cordova/CDV.h>
#import <AssetsLibrary/AssetsLibrary.h>


@implementation Canvas2ImagePlugin
@synthesize callbackId;

//-(CDVPlugin*) initWithWebView:(UIWebView*)theWebView
//{
//    self = (Canvas2ImagePlugin*)[super initWithWebView:theWebView];
//    return self;
//}

- (void)saveImageDataToLibrary:(CDVInvokedUrlCommand*)command
{
    NSLog(@"saveImageDataToLibrary.");
    
    self.callbackId = command.callbackId;
	NSData* imageData = [NSData dataFromBase64String:[command.arguments objectAtIndex:0]];
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0]; // Get documents folder
    NSString *fileName1 = [command.arguments objectAtIndex:1];
    NSString *filePath = [documentsDirectory stringByAppendingPathComponent: fileName1];
    NSLog(@"localFilePath.%@",filePath);
    
    NSString *keyword = @".png";
    
    if ([fileName1 rangeOfString:keyword].location != NSNotFound) {
        NSLog(@"PNG: write to album\n");
        
        UIImage* image = [[[UIImage alloc] initWithData:imageData] autorelease];
        UIImageWriteToSavedPhotosAlbum(image, self, @selector(image:didFinishSavingWithError:contextInfo:), nil);
    }
    else
    {
        NSLog(@"BMP: write to document\n");
        
        BOOL isTrue = [imageData writeToFile: filePath atomically: YES];
        
        if (!isTrue)
        {
            NSLog(@"save image file failed");
        }
        else
        {
            NSLog(@"save image file successfully");
            CDVPluginResult* result = [CDVPluginResult resultWithStatus: CDVCommandStatus_OK messageAsString:@"Image saved"];
            [self.webView stringByEvaluatingJavaScriptFromString:[result toSuccessCallbackString: self.callbackId]];
        }
    }

}

- (void)image:(UIImage *)image didFinishSavingWithError:(NSError *)error contextInfo:(void *)contextInfo
{
    // Was there an error?
    if (error != NULL)
    {
        // Show error message...
        NSLog(@"ERROR: %@",error);
		CDVPluginResult* result = [CDVPluginResult resultWithStatus: CDVCommandStatus_ERROR messageAsString:error.description];
		[self.webView stringByEvaluatingJavaScriptFromString:[result toErrorCallbackString: self.callbackId]];
    }
    else  // No errors
    {
        // Show message image successfully saved
        NSLog(@"IMAGE SAVED!");
		CDVPluginResult* result = [CDVPluginResult resultWithStatus: CDVCommandStatus_OK messageAsString:@"Image saved"];
		[self.webView stringByEvaluatingJavaScriptFromString:[result toSuccessCallbackString: self.callbackId]];
    }
}

- (void)dealloc
{	
	[callbackId release];
    [super dealloc];
}


@end
