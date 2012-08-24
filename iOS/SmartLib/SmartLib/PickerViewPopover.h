//
//  PickerViewPopover.h
//  SmartLib
//
//  Created by Chrysovalantis Anastasiou on 24/08/2012.
//  Copyright (c) 2012 University of Cyprus. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface PickerViewPopover : UIViewController
{
    IBOutlet UIPickerView *libraries;
    id delegate;
}

@property (nonatomic, retain) IBOutlet UIPickerView *libraries;
@property (nonatomic, retain) id delegate;
@property (nonatomic, retain) UIPopoverController *popover;
@property (nonatomic) BOOL saveLib;

-(IBAction)doneChoosing:(id)sender;

@end
