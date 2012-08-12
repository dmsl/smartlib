//
//  KeywordsViewController.m
//  SmartLib
//
//  Created by Chrysovalantis Anastasiou on 11/07/2012.
//  Copyright (c) 2012 University of Cyprus. All rights reserved.
//

#import "KeywordsViewController.h"

@interface KeywordsViewController ()

@end

@implementation KeywordsViewController

@synthesize keywords,avail,copies,book,row,delegate;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(IBAction)storeKeywords:(id)sender
{
    NSInteger copiesInt = [copies.text integerValue];
    NSInteger availInt = [avail.text integerValue];
    
    if (copiesInt < availInt) {
        UIAlertView *error = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Book copies cannot be less than the available books" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles: nil];
        [error show];
        [error release];
    }
    else {
        book.keywords = keywords.text;        
        [book.info setValue:avail.text forKey:@"bookAvail"];
        [book.info setValue:copies.text forKey:@"bookCopies"];
        [delegate setTemp:book];
        [delegate setRow: row];
        [delegate setMakeReplacement:YES];
        [self.navigationController popViewControllerAnimated:YES];
    }
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    self.title = [book.info objectForKey:@"title"];
    if (book.keywords) {
        keywords.text = book.keywords;
    }
    if ( [book.info objectForKey:@"bookAvail"]) {
        avail.text = [NSString stringWithFormat:@"%@",[book.info objectForKey:@"bookAvail"]];
    }
    copies.text = [NSString stringWithFormat:@"%@",[book.info objectForKey:@"bookCopies"]];
//    NSLog(@"%@",book);
    self.navigationItem.hidesBackButton = YES;
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    delegate = nil;
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    if (textField == keywords) {
        [avail becomeFirstResponder];
    }
    else if (textField == avail) {
        [copies becomeFirstResponder];
    }
    else {
        [textField resignFirstResponder];    
    }
    return YES;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    if ( interfaceOrientation == UIInterfaceOrientationPortraitUpsideDown || interfaceOrientation == UIInterfaceOrientationPortrait) {
        return YES;
    }
	return NO;
}


@end
