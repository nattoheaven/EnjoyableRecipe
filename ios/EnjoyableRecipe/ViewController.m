//
//  ViewController.m
//  EnjoyableRecipe
//
//  Created by NISHIMURA Ryohei on 2016/10/30.
//  Copyright © 2016年 EnjoyableRecipe. All rights reserved.
//

#import "ViewController.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    NSURL *url = [NSURL URLWithString:@"http://www.enjoyablerecipe.com/"];
    NSURLRequest *urlRequest = [NSURLRequest requestWithURL:url];
    [_webView loadRequest:urlRequest];
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (IBAction)goBack:(id)sender {
    if( _webView.canGoBack ) {
        [_webView goBack];
    }
}


@end
