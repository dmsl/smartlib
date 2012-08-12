/*   GNU Public Licence
 *   RegisterViewController View for registering to system.
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
 *  @file RegisterViewController.m
 *  @brief View for registering to system.
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

#import "RegisterViewController.h"
#import "BookActions.h"

#define switchValue(x) x?@"on":@"off"

@interface RegisterViewController ()

-(void)getLibraries;

@end

@implementation RegisterViewController
{
    NSArray *librariesList;
    NSArray *keys;
    NSString *baseURL;
    id nextResponder;
    id isFirstResponser;
    UIView *indicating;
}

@synthesize username,fname,lname,email,password, confirmPassword, telephone, emailNotifications, appNotifications,libraries;

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(void)textFieldDidBeginEditing:(UITextField *)textField
{
    isFirstResponser = textField;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone) {
        // create a toolbar that has two buttons in the right
        UIToolbar* tools = [[UIToolbar alloc] initWithFrame:CGRectMake(0, 0, 96, 44.01)];
        NSMutableArray* buttons = [[NSMutableArray alloc] initWithCapacity:2];
        
        // create a standard "refresh" button to reset the fields
        UIBarButtonItem* bi = [[UIBarButtonItem alloc]
                               initWithBarButtonSystemItem:UIBarButtonSystemItemRefresh target:self action:@selector(resetFields:)];
        bi.style = UIBarButtonItemStyleBordered;
        [buttons addObject:bi];
        [bi release];
        
        // create a submit button
        bi = [[UIBarButtonItem alloc]
              initWithBarButtonSystemItem:UIBarButtonSystemItemDone target:self action:@selector(submitRegistration:)];
        bi.title = @"Submit";
        bi.style = UIBarButtonItemStyleBordered;
        [buttons addObject:bi];
        [bi release];
        
        [tools setItems:buttons animated:NO];
        [buttons release];
        
        // and put the toolbar in the nav bar
        UIBarButtonItem *right = [[UIBarButtonItem alloc] initWithCustomView:tools];
        [tools release];

        self.navigationItem.rightBarButtonItem = right;
        [right release];
    }
}

-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
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
    [indicating removeFromSuperview];
    if ([[[librariesList objectAtIndex:0] objectForKey:@"result"] integerValue] == 1) {
        [(NSMutableArray*)librariesList removeObjectAtIndex:0];
        [libraries reloadAllComponents];
    }
    else if ([[[librariesList objectAtIndex:0] objectForKey:@"result"] integerValue] == -11) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Database Error" message:@"Error connecting to database" delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Try again", nil];
        alert.tag = 2;
        [alert show];
        [alert release];
    }
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
    [librariesList release];
    [keys release];
    [nextResponder release];
    [isFirstResponser release];
    [baseURL release];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];    
    return YES;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    if ( interfaceOrientation == UIInterfaceOrientationPortraitUpsideDown || interfaceOrientation == UIInterfaceOrientationPortrait) {
        return YES;
    }
	return NO;
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (alertView.tag == 2) {
        if (buttonIndex == alertView.cancelButtonIndex) {
            [self.navigationController popViewControllerAnimated:YES];
        }
        else {
//            indicating = [[UIView alloc] init];
//            indicating.center = libraries.center;
//            indicating.frame = CGRectMake([libraries center].x-50,[libraries center].y-50, 100, 100);
//            indicating.backgroundColor = [UIColor blackColor];
//            indicating.alpha = 0.5;
            [self.view addSubview:indicating];
//            UIActivityIndicatorView *indicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
//            indicator.center = CGPointMake(50, 50);
//            [indicator startAnimating];
//            [indicating addSubview:indicator];
            
            [self performSelector:@selector(getLibraries) withObject:nil afterDelay:0];
        }
    }
    else {
        if (buttonIndex == 0) {
            [self.navigationController popViewControllerAnimated:YES];
        }
        else {
            [self getNextResponder];
        }
    }
}

-(void)getNextResponder
{
    [nextResponder becomeFirstResponder];
}

-(IBAction)resetFields:(id)sender
{
    username.text = nil;
    fname.text = nil;
    lname.text = nil;
    email.text = nil;
    password.text = nil;
    confirmPassword.text = nil;
    telephone.text = nil;
    emailNotifications.on = NO;
    appNotifications.on = NO;
}

-(BOOL) NSStringIsValidEmail:(NSString*)checkString
{  
    BOOL stricterFilter = YES; // Discussion
	NSString *stricterFilterString = @"[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}"; 
	NSString *laxString = @".+@.+\\.[A-Za-z]{2}[A-Za-z]*";
	NSString *emailRegex = stricterFilter ? stricterFilterString : laxString;
	NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", emailRegex];
	return [emailTest evaluateWithObject:checkString];
}



-(IBAction)submitRegistration:(id)sender
{
    [isFirstResponser resignFirstResponder];
   //some input checks must be implemented here
    if (([username.text isEqualToString:@""])||([fname.text isEqualToString:@""])||([lname.text isEqualToString:@""])||([email.text isEqualToString:@""])||([password.text isEqualToString:@""])||([confirmPassword.text isEqualToString:@""]) || [telephone.text isEqualToString:@""] || [baseURL isEqualToString:@""] || !baseURL)
    {
        NSString *alertTitle = @"Error";
        NSString *alertMessage = @"You haven't filled all the required fields.";
        UIAlertView *registrationDone = [[UIAlertView alloc] initWithTitle: alertTitle message: alertMessage delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Retry",  nil];
        [registrationDone show];
        [registrationDone release];
    }
   else  if (!([password.text isEqualToString: confirmPassword.text]))
    {   
        
        NSString *alertTitle = @"Registration Unsuccessfull";
        NSString *alertMessage = @"The Password field and Confirm Password one are not same ";
        UIAlertView *registrationDone = [[UIAlertView alloc] initWithTitle: alertTitle message: alertMessage delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Retry",  nil];
        [registrationDone show];
        [registrationDone release];
        password.text =@"";
        confirmPassword.text =@"";
        nextResponder = password;
    }
   else  if([self NSStringIsValidEmail:email.text] == NO)
    {    
       
        NSString *alertTitle = @"Error";
        NSString *alertMessage = @"The Email is not valid.";
        UIAlertView *registrationDone = [[UIAlertView alloc] initWithTitle: alertTitle message: alertMessage delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Retry" ,  nil];
        
        [registrationDone show];
        [registrationDone release];
        email.text=@"";
        nextResponder = email;
    }
    else 
    {
        //url request to register
        NSString *URL = [NSString stringWithFormat:@"%@/mobile/%@",baseURL,REGISTER_PHP];
        NSMutableURLRequest *registerForm = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
        NSString *formData = [[NSString alloc] initWithFormat:@"device=iOS&username=%@&password=%@&confPassword=%@&name=%@&surname=%@&email=%@&telephone=%@&appNotif=%@&emailNotif=%@",username.text,password.text,confirmPassword.text,fname.text,lname.text,email.text,telephone.text,switchValue(appNotifications.on),switchValue(emailNotifications.on)];
        [registerForm setHTTPMethod:@"POST"];
        [registerForm setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
        [registerForm setHTTPBody:[formData dataUsingEncoding:NSUTF8StringEncoding]];
        
        NSData *response = [NSURLConnection sendSynchronousRequest:registerForm returningResponse:nil error:nil];
        NSString *json = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
        
        JSONParser *parser = [[JSONParser alloc] init];
        NSDictionary *registerStatus = [parser parseRegisterResponse:json];
        NSLog(@"%@",registerStatus);
        
        [formData release];
        [registerForm release];
        [json release];
        [parser release];
        
        //make checks
        if ([[registerStatus objectForKey:@"result"] isEqualToString:@"0"]) {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:[registerStatus objectForKey:@"message"] delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Retry", nil];
            [alert show];
            [alert release];
            nextResponder = username;
        }
        //if everything is ok then popView and inform user
        else {
            NSString *alertTitle = @"Registration Successfull";
            NSString *alertMessage = @"An activation link has been sent to your email. You will be able to log in once you activate you account.";
            UIAlertView *registrationDone = [[UIAlertView alloc] initWithTitle: alertTitle message: alertMessage delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
            [registrationDone show];
            [registrationDone release];
            [self.navigationController popViewControllerAnimated:YES];
        }
    }
    
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
}

@end
