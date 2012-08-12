//
//  RentalViewController.m
//  SmartLib
//
//  Created by Chrysovalantis Anastasiou on 30/07/2012.
//  Copyright (c) 2012 University of Cyprus. All rights reserved.
//

#import "RentalViewController.h"
#import "BookActions.h"

@interface RentalViewController ()

-(void)startRentalProcess;

@end

@implementation RentalViewController
{
    UIAlertView *waiting;
}

@synthesize bookTitle,bookTitleS,isbn,name,surname,telephone,email;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
}

-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    NSLog(@"rental");
    self.navigationItem.hidesBackButton = YES;
    bookTitle.text = bookTitleS;
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
	if ( interfaceOrientation == UIInterfaceOrientationPortraitUpsideDown || interfaceOrientation == UIInterfaceOrientationPortrait) {
        return YES;
    }
	return NO;
}

#pragma mark Renting method

-(IBAction)rentBook:(id)sender
{
    waiting = [[UIAlertView alloc] initWithTitle:@"Renting.." message:@"\n" delegate:nil cancelButtonTitle:nil otherButtonTitles:nil];
    UIActivityIndicatorView *indicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
    indicator.center = waiting.center;
    indicator.frame = CGRectMake(130.0f, 65.0f, 20.0f, 20.0f);
    [waiting addSubview:indicator];
    [indicator startAnimating];
    [waiting show];
    [indicator release];
    
    [self performSelector:@selector(startRentalProcess) withObject:nil afterDelay:0.2];
}

-(void)startRentalProcess
{
    BookActions *rentBooks = [[BookActions alloc] init];
    NSString *owner = [[[NSUserDefaults standardUserDefaults] objectForKey:@"user"] objectForKey:@"username"];
    NSInteger rentalStatus = [rentBooks lentBook:isbn fromUser:owner toUser:name.text];
    [rentBooks release];
    [waiting dismissWithClickedButtonIndex:-1 animated:NO];
    
    //TODO: something with rental status
}

@end
