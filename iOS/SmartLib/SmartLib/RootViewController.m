/*   GNU Public Licence
 *   RootViewController Root View controller.
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
 *  @file RootViewController.m
 *  @brief Application's root view.
 *
 *  @author Chrysovalantis Anastasiou
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

#import "RootViewController.h"
#import "AppDelegate.h"

@interface RootViewController ()

@end

@implementation RootViewController
{
    UIAlertView *connectivityAlert;
    ConnectivityChecks *checkConnection;
    BOOL connectivityCheckStarted;
}

@synthesize connectivityStatus,dismiss;

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
    /*
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"baseURL"];
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"libraries"];
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"rememberThis"];
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"currentLib"];
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"userCredentials"];
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"user"];
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"userBooks"];
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"session"];
    */
    //NSLog(@"%@", [[NSUserDefaults standardUserDefaults] dictionaryRepresentation]);

    self.view.hidden = YES;
    connectivityCheckStarted = NO;
    
    connectivityAlert = [[UIAlertView alloc] initWithTitle:@"Checking Connectivity" message:@"\n" delegate:nil cancelButtonTitle:nil otherButtonTitles: nil];
    UIActivityIndicatorView *indicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
    indicator.center = connectivityAlert.center;
    indicator.frame = CGRectMake(130.0f, 65.0f, 20.0f, 20.0f);
    [connectivityAlert addSubview:indicator];
    [indicator startAnimating];
    [connectivityAlert show];
    [connectivityAlert release];
    [indicator release];
}

-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    checkConnection = [(AppDelegate*)[UIApplication sharedApplication].delegate checkConnection];
    
    if (checkConnection==nil) {
        checkConnection = [[ConnectivityChecks alloc] init];
        checkConnection.delegate = self;
        [(AppDelegate*)[UIApplication sharedApplication].delegate setCheckConnection:checkConnection];
        [checkConnection performSelector: @selector(checkConnectivity:)
                              withObject: self
                              afterDelay: 0];
        //now i'm waiting for a responses
    }
    else {
        if (dismiss) {
            [connectivityAlert dismissWithClickedButtonIndex:-1 animated:YES];
        }
        
        self.view.hidden = NO;
    }
}

-(void)receivedResponse
{
    //NSLog(@"%d",[connectivityAlert retainCount]);
    [connectivityAlert dismissWithClickedButtonIndex:-1 animated:YES];
    [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
    NSLog(@"Connectivity Status is: %d",connectivityStatus);
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    switch (connectivityStatus) {
        case 3:
        {
            checkConnection.delegate = checkConnection;
            
            [checkConnection performSelector: @selector(checkConnectivity:)
                                  withObject: self
                                  afterDelay: 15];
            if ([defaults boolForKey:@"session"]) {
                NSLog(@"Recovering Session");
                [self performSegueWithIdentifier:@"recoverSession" sender:self];
            }
            else {
                self.view.hidden = NO;
            }
            break;
        }
        case 2:
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Network Error" message:@"Cannot communicate with SmartLib server." delegate:self cancelButtonTitle:@"OK" otherButtonTitles: nil];
            [alert show];
            [alert release];
            self.view.hidden = NO;
            [defaults setBool:NO forKey:@"session"];
            break;
        }
        case 1:
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Network Error" message:@"There is no internet connectivity." delegate:self cancelButtonTitle:@"OK" otherButtonTitles: nil];
            [alert show];
            [alert release];
            self.view.hidden = NO;
            [defaults setBool:NO forKey:@"session"];
            break;
        }
        case 0:
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Network Error" message:@"There is no internet connectivity." delegate:self cancelButtonTitle:@"OK" otherButtonTitles: nil];
            [alert show];
            [alert release];
            self.view.hidden = NO;
            [defaults setBool:NO forKey:@"session"];
            break;
        }
        default:
            break;
    }
}

#pragma mark Alert View Delegate

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == alertView.cancelButtonIndex) {
        checkConnection.delegate = self;
        
        [checkConnection performSelector: @selector(checkConnectivity:)
                              withObject: self
                              afterDelay: 0];
    }
}

-(void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
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
@end
