/*   GNU Public Licence
 *   BooksTaken List of currently taken books.
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
 *  @file BooksTaken.m
 *  @brief List of currently taken books.
 *
 *  @author Chrystalla Tsoutsouki
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

#import "BooksTaken.h"
#import "BookActions.h"

@implementation BooksTaken
{
    NSMutableArray *BooksTable; 
    NSInteger result;
}

@synthesize username;

- (IBAction)mainMenu:(id)sender {
    [self dismissModalViewControllerAnimated:YES];
}

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
    username = [[[NSUserDefaults standardUserDefaults] objectForKey:@"user"] objectForKey:@"username"];
    
    BookActions *action = [[BookActions alloc]init];
    BooksTable =(NSMutableArray*)[action BooksGivenToUser:username];
    [BooksTable retain];
    
    result = [[[BooksTable objectAtIndex:0] objectForKey:@"result"] integerValue];

    [BooksTable removeObjectAtIndex:0];
    [action release];
}

-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    if (result == 0) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"No Books!" message:@"You have not taken any book yet." delegate:self cancelButtonTitle:@"OK" otherButtonTitles: nil];
        [alert setTag:2];
        [alert show];
        [alert release];
    }
    else if (result == -11 || result == -12) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Unexpected error!" delegate:self cancelButtonTitle:@"OK" otherButtonTitles: nil];
        [alert setTag:2];
        [alert show];
        [alert release];
    }
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    [BooksTable release];
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
#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    return ([BooksTable count]);
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"taken";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    cell.textLabel.text =[[BooksTable objectAtIndex:indexPath.row]objectForKey:@"title"];
    // Configure the cell...
    //   cell.textLabel.text = [[[books objectAtIndex:indexPath.row] info] objectForKey:@"title"];
    
    return cell;
}

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{    
    
    NSString *owner = [[BooksTable objectAtIndex:indexPath.row]objectForKey:@"username"];
    NSString *when = [[BooksTable objectAtIndex:indexPath.row]objectForKey:@"date"];
    NSString *msg =[[NSString alloc]initWithFormat:@"From: %@ \n Date: %@ ", owner,when];
    UIAlertView *alert =[[UIAlertView alloc]initWithTitle:@"Details" message:msg delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil , nil];
    [alert show] ;
    [alert release];
    [msg release];
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}

#pragma mark Alert View Delegate

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == alertView.cancelButtonIndex) {
        [self dismissModalViewControllerAnimated:YES];
    }
}

@end
