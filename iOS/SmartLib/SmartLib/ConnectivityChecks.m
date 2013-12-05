/*   GNU Public Licence
 *   ConnectivityChecks.m Continuously checks for connectivity
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
 *  @file ConnectivityChecks.m
 *  @brief Continuously checks for connectivity changes.
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

#import "ConnectivityChecks.h"
#import "Reachability.h"
#import "RootViewController.h"

@interface ConnectivityChecks ()

-(void) sentStatus;

@end


@implementation ConnectivityChecks
{
    BOOL internetActive;
    BOOL hostActive;
    BOOL doneCheckingInternet;
    BOOL doneCheckingHost;
}

@synthesize delegate,connectivityStatus;

-(id)init
{
    self = [super init];
    if (self) {
        
    }
    return self;
}

-(void)checkConnectivity:(id)sender
{
    // check for internet connection
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(checkNetworkStatus:) name:kReachabilityChangedNotification object:nil];
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    internetReachable = [[Reachability reachabilityForInternetConnection] retain];
    [internetReachable startNotifier];
    
    // check if a pathway to a random host exists
    //HOST_NAME => "www.cs.ucy.ac.cy"
    hostReachable = [[Reachability reachabilityWithHostName: @"www.cs.ucy.ac.cy"] retain];
    [hostReachable startNotifier];
    
    NSLog(@"Checking connectivity status...");
    
}

-(void) checkNetworkStatus:(NSNotification *)notice
{
    // called after network status changes
    NetworkStatus internetStatus = [internetReachable currentReachabilityStatus];
    switch (internetStatus)
    {
        case NotReachable:
        {
            NSLog(@"The internet is down.");
            
            internetActive = NO;
            doneCheckingInternet = YES;
            
            break;
        }
        case ReachableViaWiFi:
        {
            NSLog(@"The internet is working via WIFI.");
            
            internetActive = YES;
            doneCheckingInternet = YES;
            
            break;
        }
        case ReachableViaWWAN:
        {
            NSLog(@"The internet is working via WWAN.");
            
            internetActive = YES;
            doneCheckingInternet = YES;
                    
            break;
        }
    }
    
    NetworkStatus hostStatus = [hostReachable currentReachabilityStatus];
    switch (hostStatus)
    {
        case NotReachable:
        {
            NSLog(@"A gateway to the host server is down.");
            
            hostActive = NO;
            doneCheckingHost = YES;
            
            break;
        }
        case ReachableViaWiFi:
        {
            NSLog(@"A gateway to the host server is working via WIFI.");
            
            hostActive = YES;
            doneCheckingHost = YES;
            
            break;
        }
        case ReachableViaWWAN:
        {
            NSLog(@"A gateway to the host server is working via WWAN.");
            
            hostActive = YES;
            doneCheckingHost = YES;
            
            break;
        }
    }
    
    [self sentStatus];
}

-(void)sentStatus
{
    
    if (doneCheckingInternet && doneCheckingHost) {
        [[NSNotificationCenter defaultCenter] removeObserver:self];
        if (internetActive && hostActive) {
            [delegate setConnectivityStatus:3];
        }
        else if (!internetActive && !hostActive) {
            [delegate setConnectivityStatus:0];
        }
        else if (!internetActive) {
            [delegate setConnectivityStatus:1];
        }
        else if (!hostActive) {
            [delegate setConnectivityStatus:2];
        }
        doneCheckingHost = NO;
        doneCheckingInternet = NO;
        [hostReachable release];
        [internetReachable release];
        [delegate performSelector:@selector(receivedResponse) withObject:nil afterDelay:0];
    }
}

-(void)receivedResponse
{
    [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
    NSLog(@"Connectivity Status is: %d",connectivityStatus);
    switch (connectivityStatus) {
        case 3:
        {
            self.delegate = self;
            
            [self performSelector: @selector(checkConnectivity:)
                                  withObject: self
                                  afterDelay: 30];
            break;
        }
        case 2:
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Network Error" message:@"Cannot communicate with SmartLib server." delegate:self cancelButtonTitle:@"OK" otherButtonTitles: nil];
            [alert show];
            [alert release];
            break;
        }
        case 1:
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Network Error" message:@"There is no internet connectivity." delegate:self cancelButtonTitle:@"OK" otherButtonTitles: nil];
            [alert show];
            [alert release];
            break;
        }
        case 0:
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Network Error" message:@"There is no internet connectivity." delegate:self cancelButtonTitle:@"OK" otherButtonTitles: nil];
            [alert show];
            [alert release];
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
        self.delegate = self;
        
        [self performSelector: @selector(checkConnectivity:)
                              withObject: self
                              afterDelay: 0];
    }
}


@end
