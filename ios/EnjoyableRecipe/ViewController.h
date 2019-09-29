//
//  ViewController.h
//  EnjoyableRecipe
//
//  Created by NISHIMURA Ryohei on 2016/10/30.
//  Copyright © 2016年 EnjoyableRecipe. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ViewController : UIViewController

@property (strong, nonatomic) IBOutlet UIWebView *webView;
@property (strong, nonatomic) IBOutlet UIBarButtonItem *backButton;

- (IBAction)goBack:(id)sender;

@end

