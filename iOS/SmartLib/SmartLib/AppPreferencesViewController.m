/*   GNU Public Licence
 *   AppPreferencesViewController.m Application's preferences methods.
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
 *  @file AppPreferencesViewController.m
 *  @brief Application's preferences methods.
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

#import "AppPreferencesViewController.h"

#define ENCODE_SWITCH_VAUE(x) x?@"1":@"0"
#define DECODE_SWITCH_VALUE(x) [x isEqualToString:@"1"]?YES:NO

@interface AppPreferencesViewController ()

@end

@implementation AppPreferencesViewController

@synthesize beep, vibrate, scanning1Donly, popover;

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(IBAction)savePreferences:(id)sender
{
    NSUserDefaults *commitPreferences = [NSUserDefaults standardUserDefaults];
    NSDictionary *appPreferences = [[NSDictionary alloc] 
                                    initWithObjectsAndKeys:ENCODE_SWITCH_VAUE(beep.on),@"beep",ENCODE_SWITCH_VAUE(vibrate.on),@"vibrate",ENCODE_SWITCH_VAUE(scanning1Donly.on),@"scanning1Donly", nil];
    [commitPreferences setObject:appPreferences forKey:@"appPreferences"];
    [commitPreferences synchronize];
    [appPreferences release];
    [self cancel:self];
}


-(IBAction)cancel:(id)sender
{
    if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
        [popover dismissPopoverAnimated:YES];
    }
    else {
          [self.navigationController popViewControllerAnimated:YES];
    }
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.navigationItem.hidesBackButton = YES;
    
    NSDictionary *preferences = [[NSUserDefaults standardUserDefaults] objectForKey:@"appPreferences"];
    if (preferences) {
        beep.on = DECODE_SWITCH_VALUE([preferences objectForKey:@"beep"]);
        vibrate.on = DECODE_SWITCH_VALUE([preferences objectForKey:@"vibrate"]);
        scanning1Donly.on = DECODE_SWITCH_VALUE([preferences objectForKey:@"scanning1Donly"]);
    }

    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
 
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    if ( interfaceOrientation == UIInterfaceOrientationPortraitUpsideDown || interfaceOrientation == UIInterfaceOrientationPortrait) {
        return YES;
    }
	return NO;
}

@end
