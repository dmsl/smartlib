//
//  PickerViewPopover.m
//  SmartLib
//
//  Created by Chrysovalantis Anastasiou on 24/08/2012.
//  Copyright (c) 2012 University of Cyprus. All rights reserved.
//

#import "PickerViewPopover.h"
#import "BookActions.h"
#import "RegisterViewController.h"
#import "LoginViewController.h"

@interface PickerViewPopover ()

-(void)getLibraries;

@end

@implementation PickerViewPopover
{
    NSArray *librariesList;
    NSString *baseURL;
    UIView *indicating;
}

@synthesize libraries, delegate, popover, saveLib;

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
    indicating = [[UIView alloc] init];
    indicating.center = libraries.center;
    indicating.frame = CGRectMake([libraries center].x-50,[libraries center].y-50, 100, 100);
    indicating.backgroundColor = [UIColor blackColor];
    indicating.alpha = 0.5;
    [self.view addSubview:indicating];
    UIActivityIndicatorView *indicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
    indicator.center = CGPointMake(50, 50);
    [indicator startAnimating];
    [indicating addSubview:indicator];
    [indicator release];
    
    [self performSelector:@selector(getLibraries) withObject:nil afterDelay:0];
}

-(void)getLibraries
{
    BookActions *getLibraries = [[BookActions alloc] init];
    librariesList = [[getLibraries getLibraries] retain];
    [getLibraries release];
    if ([[[librariesList objectAtIndex:0] objectForKey:@"result"] integerValue] == 1) {
        [(NSMutableArray*)librariesList removeObjectAtIndex:0];
        [indicating removeFromSuperview];
        [libraries reloadAllComponents];
    }
    else if ([[[librariesList objectAtIndex:0] objectForKey:@"result"] integerValue] == -11) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Database Error" message:@"Error connecting to database" delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Try again", nil];
        alert.tag = 2;
        [alert show];
        [alert release];
    }
}

-(void)viewDidAppear:(BOOL)animated {
    
    [super viewDidAppear:animated];
    
    [self pickerView:libraries didSelectRow:0 inComponent:0];
}

-(IBAction)doneChoosing:(id)sender
{
    [self pickerView:libraries didSelectRow:[libraries selectedRowInComponent:0] inComponent:0];
    [delegate setBaseURL:baseURL];
    NSString *baseName = [[librariesList objectAtIndex:[libraries selectedRowInComponent:0]] objectForKey:@"name"];
    [delegate setBaseName:baseName];
    if (saveLib) {
        [delegate setCurrentLib:[librariesList objectAtIndex:[libraries selectedRowInComponent:0]]];
    }
    
    [delegate refreshTitle];
    [popover dismissPopoverAnimated:YES];
}

#pragma mark - picker view data source

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 1;// or the number of vertical "columns" the picker will show...
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    if (librariesList!=nil && ([librariesList count] != 0)) {
        return [librariesList count];
    }
    else {
        return 1;
    }
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row
            forComponent:(NSInteger)component {
    
    if (librariesList==nil || ([librariesList count] == 0)) {
        return @"No Libraries found";
    }
    else {
        return [NSString stringWithFormat:@"%@",[[librariesList objectAtIndex:row] objectForKey:@"name"]];
    }
}

#pragma mark Picker View Delegate

-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
    if (librariesList!=nil && ([librariesList count] != 0)) {
        baseURL = [[librariesList objectAtIndex:row] objectForKey:@"url"];
    }
    else {
        baseURL = @"";
    }
   
    if (saveLib) {
        NSUserDefaults *rememberedLibsCheck = [NSUserDefaults standardUserDefaults];
        NSString *libName = [[librariesList objectAtIndex:row] objectForKey:@"name"];
        NSDictionary *rememberedCredentials = [[rememberedLibsCheck objectForKey:@"rememberedLibs"] objectForKey:libName];
        if (rememberedCredentials && ([rememberedCredentials count] != 0)) {
            [(UITextField*)[delegate username] setText: [rememberedCredentials objectForKey:@"username"]];
            [(UITextField*)[delegate password] setText: [rememberedCredentials objectForKey:@"password"]];
            [(UISwitch*)[delegate remember] setOn:YES];
        }
        else {
            [(UITextField*)[delegate username] setText: nil];
            [(UITextField*)[delegate password] setText: nil];
            [(UISwitch*)[delegate remember] setOn:NO];

        }
    }
}


- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
	return YES;
}

@end
