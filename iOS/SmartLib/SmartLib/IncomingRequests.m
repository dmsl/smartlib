/*   GNU Public Licence
 *   IncomingRequests List of incoming requests(future).
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
 *  @file IncomingRequests.m
 *  @brief List of incoming requests(future).
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

#import "IncomingRequests.h"
#import "BookActions.h"
#import "ActivitiesForBookCell.h"

@implementation IncomingRequests
{
    NSMutableArray *BooksArray;
    NSInteger result;
}

@synthesize usename;

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
    BooksArray = [action IncomingRequestsforUser:username];
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
        alert.tag = 2;
        [alert show];
        [alert release];
    }
    else if (result == -11 || result == -12) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Unexpected error. Program will terminate in few minutes!" delegate:self cancelButtonTitle:@"OK" otherButtonTitles: nil];
        alert.tag = 2;
        [alert show];
        [alert release];
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
    
    return [BooksArray count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{ 
    static NSString *CellIdentifier = @"requestIncome";
    ActivitiesForBookCell *cell = (ActivitiesForBookCell *)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    cell.activityTitle.text=[[BooksArray objectAtIndex:indexPath.row] objectForKey:@"title"];
   
    NSInteger acknowledge = [[[BooksArray objectAtIndex:indexPath.row]objectForKey:@"acknowledge"]integerValue];
    
    if (acknowledge==0)
    {
        cell.ActivityResponse.hidden=YES;
        cell.answer.text=@"Negative";
        cell.answer.textColor = [UIColor redColor];
    }
    else if (acknowledge==1)
    {
        cell.ActivityResponse.hidden=YES;
        cell.answer.text=@"Positive";
        cell.answer.textColor = [UIColor colorWithRed:0 green:255 blue:0 alpha:100];
    }

    else if(acknowledge==-1)
    {
        cell.ActivityResponse.hidden=NO;
        cell.answer.text=@"Not Answered";
        cell.answer.textColor = [UIColor redColor];
    }
    
    [cell.ActivityResponse setTag:indexPath.row];
    return cell;
}

#pragma mark Object methods

- (IBAction)SendAResponse:(id)sender {
    
    BookActions * action=[[BookActions alloc]init];
    NSString *isbn = [[BooksArray objectAtIndex:[sender tag]]objectForKey:@"isbn"];
    NSString *destination =[[BooksArray objectAtIndex:[sender tag]]objectForKey:@"username"];
    NSInteger answer=0;
    
    if ([sender selectedSegmentIndex]==0)
    {
        answer =[action ReplyUser:username RequestFrom:destination forBook:isbn withAnswer:1];
        
    }
    else  if ([sender selectedSegmentIndex]==1) {
        answer =[action ReplyUser:username RequestFrom:destination forBook:isbn withAnswer:0];
    }
    
    if(answer==1)
    {
        
        if ([sender selectedSegmentIndex]==0)
        {
            ActivitiesForBookCell *cell = (ActivitiesForBookCell*)[self.tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:[sender tag] inSection:0]];
            cell.answer.text = @"Positive";
        }
        else    if ([sender selectedSegmentIndex]==1)
        {
            ActivitiesForBookCell *cell = (ActivitiesForBookCell*)[self.tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:[sender tag] inSection:0]];
            cell.answer.text = @"Negative";
        }
        [sender  setHidden:YES];
        
        UIAlertView *complete = [[UIAlertView alloc]initWithTitle:@"Complete" message:@"Request Succesfully  replied" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        [complete show];
        [complete release];
    }
    else if(answer ==-11 ||answer == -12)
    {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Unexpected error!" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles: nil];
        [alert show];
        [alert release];
        
    }
    
    [action release];
}

- (void)dealloc
{
    [super dealloc];
}

#pragma mark Alert View Delegate

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if ( buttonIndex == alertView.cancelButtonIndex ) {
        [self dismissModalViewControllerAnimated:YES];
    }
}

@end

