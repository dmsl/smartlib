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
 *  @file SelectLibraryViewController.m
 *  @brief Show Libraries.
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


#import "SelectLibraryViewController.h"

#import "SmartLibNavigationController.h"
#import "AppPreferencesViewController.h"
#import "NavigationViewController.h"

#import "Library.h"
#import "Book.h"
#import "BookActions.h"
#import "LibraryCell.h"

@interface SelectLibraryViewController ()

-(void)searchForLibraries;

@end

@implementation SelectLibraryViewController
{
    UIPopoverController *popover;
    UIActionSheet *options;
    
    NSInteger row;
    UIAlertView *waiting;
}

@synthesize results, searchField, currentLib;

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
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

-(IBAction)LibraryMenu:(id)sender
{
    row = [sender tag];
    
    currentLib = [[results objectAtIndex:row] info];
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:currentLib forKey:@"currentLib"];
    [defaults synchronize];

    
    [self performSegueWithIdentifier:@"curLib" sender:sender];
}

-(IBAction)mainMenu:(id)sender
{
    [self dismissModalViewControllerAnimated:YES];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
    
    [searchField becomeFirstResponder];
    //[searchField setSelectedScopeButtonIndex:0];
}


- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
    [results release];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    if ( interfaceOrientation == UIInterfaceOrientationPortraitUpsideDown || interfaceOrientation == UIInterfaceOrientationPortrait) {
        return YES;
    }
	return NO;
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    return [results count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"cell";
    LibraryCell *cell = (LibraryCell*) [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    // Configure the cell...
    
    cell.title.text = [[[results objectAtIndex:indexPath.row] info] objectForKey:@"name"];
    cell.owner.text = [[[results objectAtIndex:indexPath.row] info] objectForKey:@"createdby"];

   [cell setTag:indexPath.row];
    
    return cell;
}

-(void)searchBarSearchButtonClicked:(UISearchBar *)searchBar
{
    [searchBar resignFirstResponder];
      
    waiting = [[UIAlertView alloc] initWithTitle:@"Searching.." message:@"\n" delegate:nil cancelButtonTitle:nil otherButtonTitles:nil];
    UIActivityIndicatorView *indicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
    indicator.center = waiting.center;
    indicator.frame = CGRectMake(130.0f, 65.0f, 20.0f, 20.0f);
    [waiting addSubview:indicator];
    [indicator startAnimating];
    [waiting show];
    [indicator release];
    
    [self performSelector:@selector(searchForLibraries) withObject:nil afterDelay:0];
}

-(void)searchForLibraries
{
    BookActions *searching = [[BookActions alloc] init];
    /*NSString *advanced;
    switch ([searchField selectedScopeButtonIndex]) {
        case 0:
        {
            advanced = @"name";
            break;
        }
        case 1:
        {
            advanced = @"";
            break;
        }
    }*/
    
    //NSLog(@"advanced %@",advanced);
    
    NSUserDefaults *userdef = [NSUserDefaults standardUserDefaults];
    /*if ([userdef objectForKey:@"userLib"] == nil) {
        NSLog(@"! exist");
    }else
        NSLog(@"exist");
    */
    //NSLog(@"userLib %d",[userdef boolForKey:@"userLib"]);
    if ([userdef boolForKey:@"userLib"]) {
        //NSLog(@"exist");
        results = (NSMutableArray*)[searching searchForUserLibrariesWithKeywords:searchField.text forUser:[[[NSUserDefaults standardUserDefaults] objectForKey:@"user"] objectForKey:@"username"]]; //advancedSearch:advanced];
    } else {
        //NSLog(@"! exist");
        results = (NSMutableArray*)[searching searchForLibrariesWithKeywords:searchField.text]; //advancedSearch:advanced];
    }
    [searching release];
    
    //NSLog(@"result %@",[results objectAtIndex:0]);
    
    //NSLog(@"search result %d",[[[results objectAtIndex:0] objectForKey:@"result"] integerValue]);
    
    if ([[[results objectAtIndex:0] objectForKey:@"result"] integerValue] == 1) {
        NSLog(@"Found %@ libraries",[[results objectAtIndex:0] objectForKey:@"libsNum"]);
        [results removeObjectAtIndex:0];
        results = (NSMutableArray*)[Library copyFromSmartLib:results];
        
        [waiting dismissWithClickedButtonIndex:-1 animated:YES];
        [self.tableView reloadData];
    }
    else if ([[[results objectAtIndex:0] objectForKey:@"result"] integerValue] == 0) {
        NSLog(@"Didn't find any libraries");
        [waiting dismissWithClickedButtonIndex:-1 animated:NO];
        waiting = [[UIAlertView alloc] initWithTitle:@"No libraries found" message:@"Your keywords didn't match any library in our database." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [waiting show];
        [waiting release];
        [results removeObjectAtIndex:0];
        [self.tableView reloadData];
    }
    else {
        NSLog(@"Error while searching libraries");
        [waiting dismissWithClickedButtonIndex:-1 animated:NO];
        waiting = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Sorry there was an error encountered. Please try again." delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Try again", nil];
        [waiting show];
        [waiting release];
        [results removeObjectAtIndex:0];
        [self.tableView reloadData];
    }
}

#pragma mark Alert View Delegate

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex != alertView.cancelButtonIndex) {
        [self searchBarSearchButtonClicked:searchField];
    }
}

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Navigation logic may go here. Create and push another view controller.
    
    //    SearchViewResultsCell *cell = (SearchViewResultsCell*)[tableView cellForRowAtIndexPath:indexPath];
    //
    //    cell.accessoryType = UITableViewCellAccessoryCheckmark;
    
}

-(void)tableView:(UITableView *)tableView didDeselectRowAtIndexPath:(NSIndexPath *)indexPath
{
    //    SearchViewResultsCell *cell = (SearchViewResultsCell*)[tableView cellForRowAtIndexPath:indexPath];
    //
    //    cell.accessoryType = UITableViewCellAccessoryNone;
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
