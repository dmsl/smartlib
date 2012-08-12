//
//  KeywordsViewController.h
//  SmartLib
//
//  Created by Chrysovalantis Anastasiou on 11/07/2012.
//  Copyright (c) 2012 University of Cyprus. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SaveBooksViewController.h"

@interface KeywordsViewController : UIViewController <UITextFieldDelegate>
{
    IBOutlet UITextField *keywords;
    IBOutlet UITextField *avail;
    IBOutlet UITextField *copies;
    Book *book;
    NSInteger row;
    id delegate;
}

@property (nonatomic, retain) IBOutlet UITextField *keywords;
@property (nonatomic, retain) IBOutlet UITextField *avail;
@property (nonatomic, retain) IBOutlet UITextField *copies;
@property (nonatomic, retain) Book *book;
@property (nonatomic) NSInteger row;
@property (nonatomic, retain) id delegate;

-(IBAction)storeKeywords:(id)sender;

@end
