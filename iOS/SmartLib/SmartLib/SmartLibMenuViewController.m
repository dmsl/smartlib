/*   GNU Public Licence
 *   SmartLibMenuViewController Application's main menu.
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
 *  @file SmartLibMenuViewController.m
 *  @brief Application's main menu.
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

#import "SmartLibMenuViewController.h"
#import "SmartLibNavigationController.h"
#import "AppPreferencesViewController.h"
#import "NavigationViewController.h"

@interface SmartLibMenuViewController ()

@end

@implementation SmartLibMenuViewController
{
    UIPopoverController *popover;
    UIActionSheet *options;
}

@synthesize welcome;

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
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    welcome.text = [NSString stringWithFormat:@"Welcome to SmartLib, %@", [[defaults objectForKey:@"user"]               objectForKey:@"name"]];
	// Do any additional setup after loading the view.
}

-(void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    welcome.text = nil;
    [popover release];
}

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
        if (![userdef boolForKey:@"rememberThisLib"]) {
//            [[userdef objectForKey:@"userCredentials"] removeAllObjects];
            [userdef removeObjectForKey:@"userCredentials"];
//            [[userdef objectForKey:@"user"] removeAllObjects];
            [userdef removeObjectForKey:@"user"];
            [userdef removeObjectForKey:@"baseURL"];
            NSString *curLibName = [[userdef objectForKey:@"currentLib"] objectForKey:@"name"];
            if ([userdef objectForKey:@"rememberedLibs"] && [[userdef objectForKey:@"rememberedLibs"] objectForKey:curLibName]) {
                NSMutableDictionary *rememberedLibs = [[userdef objectForKey:@"rememberedLibs"] mutableCopy];
                [rememberedLibs removeObjectForKey:curLibName];
                [userdef setObject:rememberedLibs forKey:@"rememberedLibs"];
                [rememberedLibs release];
            }
            [userdef removeObjectForKey:@"currentLib"];
            [userdef setBool:NO forKey:@"rememberThisLib"];
        }
        [userdef removeObjectForKey:@"userBooks"];
        [userdef setBool:NO forKey:@"session"];
        [userdef synchronize];
        [self performSegueWithIdentifier:@"logout" sender:self];
//        [self dismissModalViewControllerAnimated:YES];
    }
    else if (buttonIndex == 1) {
            [self performSegueWithIdentifier:@"preferences" sender:self];
    }
    
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    if ( interfaceOrientation == UIInterfaceOrientationPortraitUpsideDown || interfaceOrientation == UIInterfaceOrientationPortrait) {
        return YES;
    }
	return NO;
}

-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    if ([popover isPopoverVisible]) {
        [popover dismissPopoverAnimated:NO];
    }
}

-(IBAction)scanner:(id)sender
{
//    flag =(unsigned int)[sender tag];
    [self performSegueWithIdentifier:@"scanBook" sender:sender];
}

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([[segue identifier] isEqualToString:@"preferences"]) {
        if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
            AppPreferencesViewController *dvc = [(UIStoryboardPopoverSegue*)segue destinationViewController];
            popover = [(UIStoryboardPopoverSegue *)segue popoverController];
            dvc.popover = popover;
        }
    }
    else if ([[segue identifier] isEqualToString:@"scanBook"]) {
        SmartLibNavigationController *scanner = [segue destinationViewController];
        scanner.identifier = @"scan";
        if ([sender tag] == 1) {
            scanner.flag = 1;
        }
        else {
            scanner.flag = 2;
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

@end
