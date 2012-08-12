/*   GNU Public Licence
 *   OutgoingRequests List of outgoing requests(future).
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
 *  @file OutgoingRequests.m
 *  @brief List of outgoing requests(future).
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

#import "OutgoingRequests.h"
#import "BookActions.h"


@implementation OutgoingRequests
{
    NSMutableArray *BooksArray;
    NSInteger result;
}
@synthesize username;

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (IBAction)mainMenu:(id)sender {
    [self dismissModalViewControllerAnimated:YES];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    username =[[[NSUserDefaults standardUserDefaults] objectForKey:@"user"] objectForKey:@"username"];
    
    BookActions *action = [[BookActions alloc]init];
    BooksArray = [action OutGoingRequestsforUser:username];
    [BooksArray retain];
        
    result = [[[BooksArray objectAtIndex:0] objectForKey:@"result"] integerValue];    
    [BooksArray removeObjectAtIndex:0];

    [action release];
}


- (void)viewDidUnload
{   
    [super viewDidUnload];
    [BooksArray release];
}

-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    if (result == 0) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"No requests!" message:@"You don't have any requests." delegate:self cancelButtonTitle:@"OK" otherButtonTitles: nil];
        [alert show];
        [alert release];
    }
    else if (result == -11 || result == -12) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Unexpected error!" delegate:self cancelButtonTitle:@"OK" otherButtonTitles: nil];
        [alert show];
        [alert release];
    }
    else
    {
        [[self navigationItem] setRightBarButtonItem:[self editButtonItem]];
    }
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
    //return ([scannedBooks count]-1);
    
    return [BooksArray count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{ 
    static NSString *CellIdentifier = @"requestOutgoing";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell ==nil)
    {  
        //
    }
    
    
    cell.textLabel.text  =[[BooksArray objectAtIndex:indexPath.row]objectForKey:@"title"];
    
    return cell;
    
}

#pragma mark Table View Delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{    
    
    NSInteger acknowledge  =[[[BooksArray objectAtIndex:indexPath.row]objectForKey:@"acknowledge"] integerValue];
    
    NSString *owner =[[BooksArray objectAtIndex:indexPath.row]objectForKey:@"username"];
    NSString *date= [[BooksArray objectAtIndex:indexPath.row]objectForKey:@"date"];
    NSString *answer ;
    if(acknowledge==1)
    {
        answer = [[NSString alloc]initWithFormat:@"Positive"];
    }
    else if (acknowledge==0)
    {
         answer = [[NSString alloc]initWithFormat:@"Negative"]; 
    }
    else if (acknowledge==-1)
    {
        answer = [[NSString alloc]initWithFormat:@"No Reply yet"]; 
    }
    else 
    {
         answer = [[NSString alloc]initWithFormat:@"unknown"]; 
    }
        
    NSString *msg =[[NSString alloc]initWithFormat:@"From: %@ \n Date: %@ \n Answer: %@", owner,date,answer];
    [answer release];
    UIAlertView *alert =[[UIAlertView alloc]initWithTitle:@"Details" message:msg delegate:nil cancelButtonTitle:@"ok" otherButtonTitles:nil , nil];
    [alert show] ;
    [alert release];
    [msg release];
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}

-(BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ([[[BooksArray objectAtIndex:indexPath.row] objectForKey:@"acknowledge"] integerValue] < 1 ) {
        return YES;
    }
    else {
        return NO;
    }
}

-(void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        BookActions *deleteRequest = [[BookActions alloc]init];
        NSString *isbn = [[BooksArray objectAtIndex:indexPath.row] objectForKey:@"isbn"];
        NSString *owner = [[BooksArray objectAtIndex:indexPath.row] objectForKey:@"username"];
        result = [deleteRequest deleteRequestForBook:isbn sentFromUser:username toUser:owner];
        [deleteRequest release];
        
        if (result!=1) {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Database error." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles: nil];
            [alert show];
            [alert release];
        }
        else {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Success" message:@"Request deleted successfully." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles: nil];
            [alert show];
            [alert release];
            
            [BooksArray removeObjectAtIndex:indexPath.row];
            [tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationFade];
        }
    }
}

#pragma mark Alert View Delegate

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == alertView.cancelButtonIndex) {
        [self dismissModalViewControllerAnimated:YES];
    }
}

@end
