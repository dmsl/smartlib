/*   GNU Public Licence
 *   LoginViewController View for logging in to system.
 *
 *   Copyright (C) 2012 University of Cyprus
 *   This program is free software: you can redistribute it and/or modify it under
 *   the terms of the GNU General Public License as published by the Free Software
 *   Foundation, either version 3 of the License, or at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful, but
 *   WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *   FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *   details.
 *
 *   You should have received a copy of the GNU General Public License along with
 *   this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *  @file LoginViewController
 *  @brief View for logging in to system.
 *
 *  @author Chrysovalantis Anastasiou, Chrystalla Tsoutsouki
 *  @affiliation
 *      Data Management Systems Laboratory
 *      Dept. of Computer Science
 *      University of Cyprus
 *      P.O. Box 20537
 *      1678 Nicosia, CYPRUS
 *      Web: http://dmsl.cs.ucy.ac.cy/
 *      Email: dmsl@cs.ucy.ac.cy
 *      Tel: +357-22-892755
 *      Fax: +357-22-892701
 *
 *  @bug No known bugs.
 */

#import "LoginViewController.h"
#import "BookActions.h"
#import "PickerViewPopover.h"

@interface LoginViewController ()

-(void)getLibraries;

@end

@implementation LoginViewController
{
    NSArray *librariesList;
    id responder;
    NSString *baseURL;
    UIAlertView *waiting;
    UIView *indicating;
    UIPopoverController *popover;
}

@synthesize username, password, remember, libraries, title, currentLib, baseName, baseURL;

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
}

-(void)refreshTitle
{
    title.text = baseName;
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    [librariesList release];
    [baseURL release];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone) {
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
}

-(void)getLibraries
{
    BookActions *getLibraries = [[BookActions alloc] init];
    librariesList = [[getLibraries getLibraries] retain];
    [getLibraries release];
    [indicating removeFromSuperview];
    if ([[[librariesList objectAtIndex:0] objectForKey:@"result"] integerValue] == 1) {
        [(NSMutableArray*)librariesList removeObjectAtIndex:0];
        [libraries reloadAllComponents];
        [self pickerView:libraries didSelectRow:[libraries selectedRowInComponent:0] inComponent:0];
    }
    else if ([[[librariesList objectAtIndex:0] objectForKey:@"result"] integerValue] == -11) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Database Error" message:@"Error connecting to database" delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Try again", nil];
        alert.tag = 2;
        [alert show];
        [alert release];
    }
}


- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    if ([textField isEqual:username]) {
        [password becomeFirstResponder];
    }
    else {
        [textField resignFirstResponder];
        [self login:self];
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

-(IBAction)login:(id)sender
{   
    if (([username.text isEqualToString:@""]) ||([password.text isEqualToString:@""]) || [baseURL isEqualToString:@""])
    {
        NSString *alertTitle = @"Error";
        NSString *alertMessage = @"You haven't filled all the required fields.";
        UIAlertView *registrationDone = [[UIAlertView alloc] initWithTitle: alertTitle message: alertMessage delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Retry" ,  nil];
        registrationDone.tag = 1;
        [registrationDone show];
        [registrationDone release];
    }
    else 
    {
        waiting = [[UIAlertView alloc] initWithTitle:@"Logging in.." message:@"\n" delegate:nil cancelButtonTitle:nil otherButtonTitles:nil];
        UIActivityIndicatorView *indicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
        indicator.center = waiting.center;
        indicator.frame = CGRectMake(130.0f, 65.0f, 20.0f, 20.0f);
        [waiting addSubview:indicator];
        [indicator startAnimating];
        [waiting show];
        [indicator release];
        
        [self performSelector:@selector(loggingIn) withObject:nil afterDelay:1];
    }

}

-(void)loggingIn
{
    NSUserDefaults *addingUserCred = [NSUserDefaults standardUserDefaults];
    
    //url request to check authentication
    NSString *URL = [NSString stringWithFormat:@"%@/mobile/%@",baseURL,LOGIN_PHP];
    NSMutableURLRequest *login = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    NSString *formData = [[NSString alloc] initWithFormat:@"device=iOS&username=%@&password=%@",username.text,password.text];
    [login setHTTPMethod:@"POST"];
    [login setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [login setHTTPBody:[formData dataUsingEncoding:NSUTF8StringEncoding]];
    
    NSData *response = [NSURLConnection sendSynchronousRequest:login returningResponse:nil error:nil];
    [login release];
    [formData release];
    
    NSString *json = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
    
    JSONParser *parser = [[JSONParser alloc] init];
    NSMutableDictionary *userInfo = (NSMutableDictionary*)[parser parseLoginInfo:json];
    [json release];
    [parser release];
    
    //dismiss the indicator alert
    [waiting dismissWithClickedButtonIndex:-1 animated:YES];
    [waiting release];
    if ([userInfo isEqualToDictionary:nil] || userInfo == nil ) {
        UIAlertView *error = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Sorry this is not a valid library anymore." delegate:self cancelButtonTitle:@"Clear" otherButtonTitles:@"Retry", nil];
        error.tag = 2;
        [error show];
        [error release];
    }
    else if ([[userInfo objectForKey:@"result"] isEqualToString:@"0"]) {
        NSLog(@"Either your username or password is wrong.");
        UIAlertView *error = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Either your username or password is wrong." delegate:self cancelButtonTitle:@"Clear" otherButtonTitles:@"Retry", nil];
        error.tag = 2;
        [error show];
        [error release];
    }
    else if ([[userInfo objectForKey:@"result"] isEqualToString:@"-11"]) {
        NSLog(@"Database internal error!");
        UIAlertView *error = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Database internal error!" delegate:self cancelButtonTitle:@"Clear" otherButtonTitles:@"Retry", nil];
        error.tag = 3;
        [error show];
        [error release];
    }
    else {
        NSInteger level = [[userInfo objectForKey:@"level"] integerValue];
        if (level > 0) {
            NSLog(@"User logged in successfully");
            NSMutableDictionary *userCredentials = [[NSMutableDictionary alloc] initWithCapacity:2];
            [userCredentials setObject:username.text forKey:@"username"];
            [userCredentials setObject:password.text forKey:@"password"];
            
            if (remember.on) {
                NSMutableDictionary *rememberedLibs = [[addingUserCred objectForKey:@"rememberedLibs"] mutableCopy];
                if (rememberedLibs == nil) {
                    rememberedLibs = [[NSMutableDictionary alloc] initWithCapacity:2];
                }
                
                if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
                    [rememberedLibs setObject:userCredentials forKey:baseName];
                }
                else {
                    [rememberedLibs setObject:userCredentials forKey:[[librariesList objectAtIndex:[libraries selectedRowInComponent:0]] objectForKey:@"name"]];
                }
                [addingUserCred setObject:rememberedLibs forKey:@"rememberedLibs"];
                [rememberedLibs release];
            }
            [addingUserCred setObject:userCredentials forKey:@"userCredentials"];
            [userCredentials release];
            [addingUserCred setBool:remember.on forKey:@"rememberThisLib"];
            [addingUserCred setBool:YES forKey:@"session"];
            [addingUserCred setObject:userInfo forKey:@"user"];
            [addingUserCred setObject:baseURL forKey:@"baseURL"];
            if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
                [addingUserCred setObject:currentLib forKey:@"currentLib"];
            }
            else {
                [addingUserCred setObject:[librariesList objectAtIndex:[libraries selectedRowInComponent:0]] forKey:@"currentLib"];
            }
            [addingUserCred synchronize];
            [self performSegueWithIdentifier:@"enterLibrary" sender:self];
        }
        else if (level == 0){
            UIAlertView *notActivated = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Please activate your account and then try logging in again." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [notActivated show];
            [notActivated release];
            if ([addingUserCred objectForKey:@"userCredentials"]!=nil) {
                [addingUserCred removeObjectForKey:@"userCredentials"];
                [addingUserCred setBool:NO forKey:@"rememberThisLib"];
                [addingUserCred synchronize];
            }
            if ([addingUserCred objectForKey:@"user"]!=nil) {
                [addingUserCred removeObjectForKey:@"user"];
                [addingUserCred synchronize];
            }
        }
        else if (level == -1) {
            
        }
        else if (level == -2) {
            UIAlertView *error = [[UIAlertView alloc] initWithTitle:@"Error" message:@"You can't login to this library because you've been banned." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles: nil];
            error.tag = 2;
            [error show];
            [error release];
        }
    }
}

#pragma mark Alert View Delegate

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (alertView.tag == 2) {
        if (buttonIndex == alertView.cancelButtonIndex) {
            username.text = nil;
            password.text = nil;
            remember.on = NO;
            [username becomeFirstResponder];
        }
        else {
            [self performSelector:@selector(getLibraries) withObject:nil afterDelay:0];
        }
    }
    else {
        if (buttonIndex == 0) {
            username.text = nil;
            password.text = nil;
            remember.on = NO;
            [username becomeFirstResponder];
        }
        if (alertView.tag == 3) {
            if (buttonIndex == 1) {
                [self login:self];
            }
            else {
                username.text = nil;
                password.text = nil;
                remember.on = NO;
                [username becomeFirstResponder];
            }
        }
    }
}

#pragma mark Picker View Data source

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
        return [NSString stringWithFormat:@"No Libraries found"];
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
    
    NSUserDefaults *rememberedLibsCheck = [NSUserDefaults standardUserDefaults];
    NSString *libName = [[librariesList objectAtIndex:row] objectForKey:@"name"];
    NSDictionary *rememberedCredentials = [[rememberedLibsCheck objectForKey:@"rememberedLibs"] objectForKey:libName];
    if (rememberedCredentials && ([rememberedCredentials count] != 0)) {
        username.text = [rememberedCredentials objectForKey:@"username"];
        password.text = [rememberedCredentials objectForKey:@"password"];
        remember.on = YES;
    }
    else {
        username.text = nil;
        password.text = nil;
        remember.on = NO;
    }
}

#pragma mark - Seque

-(IBAction)showList:(id)sender
{
    [self performSegueWithIdentifier:@"libList" sender:sender];
}

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([[segue identifier] isEqualToString:@"libList"]) {
        if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
            PickerViewPopover *dvc = [(UIStoryboardPopoverSegue*)segue destinationViewController];
            popover = [(UIStoryboardPopoverSegue *)segue popoverController];
            dvc.popover = popover;
            dvc.delegate = self;
            dvc.saveLib = YES;
        }
    }
}

@end
