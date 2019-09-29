//
//  AppDelegate.h
//  EnjoyableRecipe
//
//  Created by NISHIMURA Ryohei on 2016/10/30.
//  Copyright © 2016年 EnjoyableRecipe. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreData/CoreData.h>

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (readonly, strong) NSPersistentContainer *persistentContainer;

- (void)saveContext;


@end

