//
//  SaveBooksViewController.h
//  SmartLib
//
//  Created by Chrysovalantis Anastasiou on 10/07/2012.
//  Copyright (c) 2012 University of Cyprus. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Book.h"
#import "SmartLibNavigationController.h"

@interface SaveBooksViewController : UITableViewController
{
    NSMutableArray *scannedBooks;
    BOOL makeReplacement;
    Book *temp;
    NSInteger row;
}

@property (nonatomic, retain) NSMutableArray *scannedBooks;
@property (nonatomic) BOOL makeReplacement;
@property (nonatomic, retain) Book *temp;
@property (nonatomic) NSInteger row;
@property (nonatomic, retain) UIAlertView *saving;

-(IBAction)saveBooks:(id)sender;

@end
