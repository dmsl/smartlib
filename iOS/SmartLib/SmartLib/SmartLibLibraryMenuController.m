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
 *  @file SmartLibLibraryMenuController.m
 *  @brief Library Menu.
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


#import "SmartLibLibraryMenuController.h"

#import "SmartLibNavigationController.h"
#import "AppPreferencesViewController.h"
#import "NavigationViewController.h"

//#import "BookActions.h"

@interface SmartLibLibraryMenuController ()

@end

@implementation SmartLibLibraryMenuController
{
    UIPopoverController *popover;
    UIActionSheet *options;
   // NSArray *librariesList; //remove
}

@synthesize welcome; /*, currentLib,baseName, baseURL; */

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
    welcome.text = [NSString stringWithFormat:@"SmartLib: %@", [[defaults objectForKey:@"user"] objectForKey:@"name"]];
    //NSLog(@"%@", [[NSUserDefaults standardUserDefaults] dictionaryRepresentation]);
    
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
        if (![userdef boolForKey:@"rememberThis"]) {
            //            [[userdef objectForKey:@"userCredentials"] removeAllObjects];
            [userdef removeObjectForKey:@"userCredentials"];
            //            [[userdef objectForKey:@"user"] removeAllObjects];
            [userdef removeObjectForKey:@"user"];
            [userdef removeObjectForKey:@"currentLib"];
            //[userdef removeObjectForKey:@"libraries"];
            [userdef setBool:NO forKey:@"rememberThis"];
        }
        //[userdef removeObjectForKey:@"userBooks"];
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

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
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
    } else {
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        if ([[segue identifier]isEqualToString:@"userLib"]) {
            [defaults setBool:YES forKey:@"userLib"];
            //NSLog(@"userLib");
        } else {
            [defaults setBool:NO forKey:@"userLib"];
            //NSLog(@"lib");
        }
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
