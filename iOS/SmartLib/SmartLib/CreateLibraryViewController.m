/*   GNU Public Licence
 *   MyBooksViewController Shows the books user has in his library.
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
 *  @file CreateLibraryViewController.m
 *  @brief View for creating new library to system.
 *
 *  @author Aphrodite Christou
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


#import "CreateLibraryViewController.h"
#import "BookActions.h"

#import "SmartLibNavigationController.h"
#import "AppPreferencesViewController.h"
#import "NavigationViewController.h"

@interface CreateLibraryViewController ()

@end

@implementation CreateLibraryViewController
{
  /*  UIPopoverController *popover;
    UIActionSheet *options;
    */
    
    NSArray *keys;
    id nextResponder;
    id isFirstResponser;
}

@synthesize name;

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

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
    [keys release];
    [nextResponder release];
    [isFirstResponser release];
    //[baseURL release]; //remove
}

/*
-(IBAction)appOptions:(id)sender
{
    if ([popover isPopoverVisible]) {
        [popover dismissPopoverAnimated:NO];
    }
    if (!options) {
        options = [[UIActionSheet alloc] initWithTitle:@"Options" delegate:self cancelButtonTitle:@"Cancel" destructiveButtonTitle:@"Log Out" otherButtonTitles:@"Preferences", nil];
        if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
            [options showFromBarButtonItem:[self.navigationItem rightBarButtonItem] animated:YES];
        }
        else {
            [options showInView:self.view];
        }
        [options release];
    }
}

-(void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    options = nil;
    if (buttonIndex == actionSheet.destructiveButtonIndex) {
        NSUserDefaults *userdef = [NSUserDefaults standardUserDefaults];
        if (![userdef boolForKey:@"rememberThis"]) {
            //            [[userdef objectForKey:@"userCredentials"] removeAllObjects];
            [userdef removeObjectForKey:@"userCredentials"];
            //            [[userdef objectForKey:@"user"] removeAllObjects];
            [userdef removeObjectForKey:@"user"];
            [userdef removeObjectForKey:@"currentLib"];
            [userdef removeObjectForKey:@"libraries"];
            [userdef setBool:NO forKey:@"rememberThis"];
        }
        [userdef removeObjectForKey:@"userBooks"];
        [userdef removeObjectForKey:@"userLib"];
        [userdef setBool:NO forKey:@"session"];
        [userdef synchronize];
        [self performSegueWithIdentifier:@"logout" sender:self];
        //        [self dismissModalViewControllerAnimated:YES];
    }
    else if (buttonIndex == 1) {
        [self performSegueWithIdentifier:@"preferences" sender:self];
    }
}

-(void) prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([[segue identifier] isEqualToString:@"preferences"]) {
        if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
            AppPreferencesViewController *dvc = [(UIStoryboardPopoverSegue*)segue destinationViewController];
            popover = [(UIStoryboardPopoverSegue *)segue popoverController];
            dvc.popover = popover;
        }
    }
    else if ([[segue identifier] isEqualToString:@"logout"]) {
        NavigationViewController *dvc = [segue destinationViewController];
        dvc.dissmiss = YES;
    }
    
    if ([popover isPopoverVisible]) {
        [popover dismissPopoverAnimated:NO];
    }
    if (options) {
        [options dismissWithClickedButtonIndex:-1 animated:YES];
        options = nil;
    }
}
*/
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
            //            [self.view addSubview:indicating];
            //            UIActivityIndicatorView *indicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
            //            indicator.center = CGPointMake(50, 50);
            //            [indicator startAnimating];
            //            [indicating addSubview:indicator];
            
            //[self performSelector:@selector(getLibraries) withObject:nil afterDelay:0];
        }
    }
    else {
        if (buttonIndex == 0) {
            [self resetFields:self];
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
    name.text = nil;
}

-(IBAction)submitRegistration:(id)sender
{
    [isFirstResponser resignFirstResponder];
    if ([name.text isEqualToString:@""])
    {
        NSString *alertTitle = @"Error";
        NSString *alertMessage = @"You haven't filled all the required fields.";
        UIAlertView *registrationDone = [[UIAlertView alloc] initWithTitle: alertTitle message: alertMessage delegate:self cancelButtonTitle:@"Clear" otherButtonTitles:@"Retry",  nil];
        [registrationDone show];
        [registrationDone release];
    }
    else
    {
        //url request to register
        NSString *URL = [NSString stringWithFormat:@"%@/mobile/%@",MASTER_URL,CREATE_LIB_PHP];
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        NSString *username = [[defaults objectForKey:@"user"] objectForKey:@"name"];
        NSMutableURLRequest *registerForm = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
        NSString *formData = [[NSString alloc] initWithFormat:@"device=iOS&name=%@&username=%@&url=%@&email=%@&tel=%@&town=%@&country=%@",name.text,username,@"",@"",@"",@"",@""];
        
        [registerForm setHTTPMethod:@"POST"];
        [registerForm setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
        [registerForm setHTTPBody:[formData dataUsingEncoding:NSUTF8StringEncoding]];
        
        NSData *response = [NSURLConnection sendSynchronousRequest:registerForm returningResponse:nil error:nil];
        NSString *json = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
        
        JSONParser *parser = [[JSONParser alloc] init];
        NSDictionary *registerStatus = [parser parseRegisterResponse:json];
        //NSLog(@"%@",registerStatus);
        
        [formData release];
        [registerForm release];
        [json release];
        [parser release];
        
        //make checks
        if ([[registerStatus objectForKey:@"result"] isEqualToString:@"0"]) {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:[registerStatus objectForKey:@"message"] delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Retry", nil];
            [alert show];
            [alert release];
            nextResponder = name;
        }
        //if everything is ok then popView and inform user
        else {
            NSString *alertTitle = @"Creation Successfull";
            NSString *alertMessage = @"Now you can select your library.";
            UIAlertView *registrationDone = [[UIAlertView alloc] initWithTitle: alertTitle message: alertMessage delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
            //UIAlertView *registrationDone = [[UIAlertView alloc] initWithTitle: alertTitle message:nil delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
            [registrationDone show];
            [registrationDone release];
            
            //[defaults removeObjectForKey:@"libraries"]; //Add new Library -> update libraries list
            //[defaults synchronize];
            
            [self performSelector:@selector(dismissController) withObject:nil afterDelay:0.3];
            //[self.navigationController popViewControllerAnimated:YES];
        }
    }
}

- (void) dismissController {
    [[self navigationController] popViewControllerAnimated:YES];
}
@end

