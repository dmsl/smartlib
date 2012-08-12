//
//  RentalViewController.h
//  SmartLib
//
//  Created by Chrysovalantis Anastasiou on 30/07/2012.
//  Copyright (c) 2012 University of Cyprus. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RentalViewController : UIViewController

@property (nonatomic, retain) NSString *isbn;
@property (nonatomic, retain) NSString *bookTitleS;

@property (nonatomic, retain) IBOutlet UILabel *bookTitle;
@property (nonatomic, retain) IBOutlet UITextField *name;
@property (nonatomic, retain) IBOutlet UITextField *surname;
@property (nonatomic, retain) IBOutlet UITextField *telephone;
@property (nonatomic, retain) IBOutlet UITextField *email;

-(IBAction)rentBook:(id)sender;

@end
