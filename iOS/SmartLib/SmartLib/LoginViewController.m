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
 *  @author Chrysovalantis Anastasiou, Chrystalla Tsoutsouki, Aphrodite Christou
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

@implementation LoginViewController
{
    id responder;
    UIAlertView *waiting;
}

@synthesize username, password, remember ;

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
    
/*
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"baseURL"];
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"currentLib"];
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"new"];
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"new2"];
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"rememberedLibs"];
    [[NSUserDefaults standardUserDefaults]synchronize ];
    NSLog(@"empty %@", [[NSUserDefaults standardUserDefaults] dictionaryRepresentation]);  */

}

- (void)viewDidUnload
{
    [super viewDidUnload];
    
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
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
    if (([username.text isEqualToString:@""]) ||([password.text isEqualToString:@""]))
    {
        NSString *alertTitle = @"Error";
        NSString *alertMessage = @"You haven't filled all the required fields.";
        UIAlertView *registrationDone = [[UIAlertView alloc] initWithTitle: alertTitle message: alertMessage delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Retry" ,  nil];
        //registrationDone.tag = 1;
        registrationDone.tag=2; //ME
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
    NSString *URL = [NSString stringWithFormat:@"%@/mobile/%@",MASTER_URL,LOGIN_PHP]; //ME
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
        UIAlertView *error = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Sorry something going wrong." delegate:self cancelButtonTitle:@"Clear" otherButtonTitles:@"Retry", nil];
        error.tag = 2;
        [error show];
        [error release];
    } else if ([[userInfo objectForKey:@"result"] isEqualToString:@"0"]) {
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
            
            [addingUserCred setObject:userCredentials forKey:@"userCredentials"];
            [userCredentials release];
            
            [addingUserCred setBool:remember.on forKey:@"rememberThis"];

            [addingUserCred setBool:YES forKey:@"session"];
            [addingUserCred setObject:userInfo forKey:@"user"];
            [addingUserCred synchronize];
            
            [self performSegueWithIdentifier:@"Liblogin" sender:self]; //ME
        }
        else if (level == 0){
            UIAlertView *notActivated = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Please activate your account and then try logging in again." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [notActivated show];
            [notActivated release];
            if ([addingUserCred objectForKey:@"userCredentials"]!=nil) {
                [addingUserCred removeObjectForKey:@"userCredentials"];
                [addingUserCred setBool:NO forKey:@"rememberThis"];
                [addingUserCred synchronize];
            }
            if ([addingUserCred objectForKey:@"user"]!=nil) {
                [addingUserCred removeObjectForKey:@"user"];
                [addingUserCred synchronize];
            }
        }
        else if (level == -1) {
            
        } /*
           else if (level == -2) {
           UIAlertView *error = [[UIAlertView alloc] initWithTitle:@"Error" message:@"You can't login to this library because you've been banned." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles: nil];
           error.tag = 2;
           [error show];
           [error release];
           }*/
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

@end
