/*   GNU Public Licence
 *   SearchBooksViewController View for searching through library's books.
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
 *  @file SearchBooksViewController.m
 *  @brief View for searching through library's books.
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

#import "SearchBooksViewController.h"
#import "BookDetailsViewController.h"
#import "Book.h"
#import "BookActions.h"
#import "SearchViewResultsCell.h"

#define CONFIRM_ALERT_TAG 100

@interface SearchBooksViewController ()

-(void)beginDeleting;
-(void)requestLent;
-(void)searchForBooks;
-(void)getBookCoversAsynchronous;

@end

@implementation SearchBooksViewController
{
    NSInteger row;
    UIAlertView *waiting;
    NSMutableArray *bookCovers;
}

@synthesize results, searchField;

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
    [super viewDidLoad];
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
    
    [searchField becomeFirstResponder];
    [searchField setSelectedScopeButtonIndex:3];
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
    static NSString *CellIdentifier = @"book";
    SearchViewResultsCell *cell = (SearchViewResultsCell*) [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    // Configure the cell...
    
    cell.bookTitle.text = [[[results objectAtIndex:indexPath.row] info] objectForKey:@"title"];
    
//    if ([bookCovers count] > indexPath.row) {
        cell.bookCover.image = [bookCovers objectAtIndex:indexPath.row];
//    }
//    else {
//        cell.bookCover.image = [UIImage imageNamed:@"96-book.png"];
//    }
    
    [cell.borrowBook removeSegmentAtIndex:1 animated:NO];
    [cell.deleteBook removeSegmentAtIndex:1 animated:NO];
    [cell.borrowBook setTag:indexPath.row];
    [cell.deleteBook setTag:indexPath.row];
    [cell.bookInfo setTag:indexPath.row];
    [cell setTag:indexPath.row];
    
    NSInteger stateCode = [[[[results objectAtIndex:indexPath.row] info] objectForKey:@"status"] integerValue];
    if (stateCode == 0) {
        cell.availability.text = @"Available!";
        cell.availability.textColor = [UIColor colorWithRed:0 green:255 blue:0 alpha:100];
    }
    else if (stateCode == 1) {
        cell.availability.text = @"Rented";
        cell.availability.textColor = [UIColor redColor];
    }
    else if (stateCode == -1 || stateCode == -2) {
        cell.availability.text = @"Not available!";
        cell.availability.textColor = [UIColor redColor];
    }
    else {
        cell.availability.text = @"Could not retrieve availability!";
        cell.availability.textColor = [UIColor redColor];
    }
    
    //if the book belongs to the user show delete button
    NSString *username =[[[NSUserDefaults standardUserDefaults] objectForKey:@"user"] objectForKey:@"username"];
    if (![[[[results objectAtIndex:indexPath.row] info] objectForKey:@"username"] isEqualToString:username]) {
        [cell.deleteBook removeFromSuperview];
        [cell addSubview:cell.borrowBook];
        if (stateCode!=0) {
            cell.borrowBook.enabled = NO;
        }
        else {
            cell.borrowBook.enabled = YES;
        }

    }
    //if the book doesn't belong to user show borrow button
    else {
        [cell.borrowBook removeFromSuperview];
        [cell addSubview:cell.deleteBook];
        switch (stateCode) {
            case 1:
            case -11:
            case -12:
            {
                cell.deleteBook.enabled = NO;
                break;
            }
            case 0:
            case -1:
            case -2:
            {
                cell.deleteBook.enabled = YES;
                break;
            }
            default:
                break;
        }
     }

    return cell;
}

#pragma mark Controller's methods

-(IBAction)refreshResults:(id)sender
{
    [self searchBarSearchButtonClicked:searchField];
}

-(IBAction)bookInfo:(id)sender
{
    row = [sender tag];
    [self performSegueWithIdentifier:@"bookInfo" sender:sender];
}

-(IBAction)deleteBook:(id)sender
{
    NSLog(@"Delete");
    
    row = [sender tag];
    
    UIAlertView *confirm = [[UIAlertView alloc] initWithTitle:@"Confirm" message:@"Are you sure you want to delete this book?" delegate:self cancelButtonTitle:@"No" otherButtonTitles:@"Yes", nil];
    confirm.tag = CONFIRM_ALERT_TAG;
    [confirm show];
    [confirm release];
}

-(void)beginDeleting
{
    BookActions *delete = [[BookActions alloc] init];
    NSInteger result = [delete deleteBook:[[[results objectAtIndex:row] info] objectForKey:@"isbn"] ofUser:[[[NSUserDefaults standardUserDefaults] objectForKey:@"user"] objectForKey:@"username"]];
    [delete release];
    [waiting dismissWithClickedButtonIndex:-1 animated:YES];
    switch (result) {
        case 1:
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Success" message:@"Book successfully deleted from database." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles: nil];
            [alert show];
            [alert release];
            break;
        }
        case -11:
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Error while connecting to database." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles: nil];
            [alert show];
            [alert release];
            break;
        }
        case -12:
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Unexpected error." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles: nil];
            [alert show];
            [alert release];
            break;
        }
        default:
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Unknown error." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles: nil];
            [alert show];
            [alert release];
            break;
        }
    }

}

-(IBAction)borrowBook:(id)sender
{
    waiting = [[UIAlertView alloc] initWithTitle:@"Sending Request" message:@"\n" delegate:nil cancelButtonTitle:nil otherButtonTitles:nil];
    UIActivityIndicatorView *indicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
    indicator.center = waiting.center;
    indicator.frame = CGRectMake(130.0f, 65.0f, 20.0f, 20.0f);
    [waiting addSubview:indicator];
    [indicator startAnimating];
    [waiting show];
    [waiting release];
    [indicator release];
    
    row = [sender tag];
    
    [self performSelector:@selector(requestLent) withObject:nil afterDelay:0];
    
}

-(void)requestLent
{
    NSString *isbn = [[[results objectAtIndex:row] info] objectForKey:@"isbn"];
    NSString *owner = [[[results objectAtIndex:row] info] objectForKey:@"username"];
    NSString *dest = [[[NSUserDefaults standardUserDefaults] objectForKey:@"user"] objectForKey:@"username"];
    BookActions *request = [[BookActions alloc] init];
    NSInteger result = [request requestBorrowingBook:isbn fromUser:owner toUser:dest];
    [request release];
    [waiting dismissWithClickedButtonIndex:-1 animated:YES];
    if (result == 1) {
        waiting = [[UIAlertView alloc] initWithTitle:@"Success" message:@"Request successfully sent!" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
    }
    else if (result == 0) {
        waiting = [[UIAlertView alloc] initWithTitle:@"Error" message:@"The owner of this book doesn't accept requests." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
    }
    else if (result == -1) {
        waiting = [[UIAlertView alloc] initWithTitle:@"Error" message:@"You have already sent a request for this book!" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
    }
    else {
        NSLog(@"%d",result);
        waiting = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Unknown error encountered!" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
    }
    [waiting show];
    [waiting release];
}

-(void)searchBarSearchButtonClicked:(UISearchBar *)searchBar
{
    [searchBar resignFirstResponder];
//    if (bookCovers!=nil || [bookCovers count]!=0) {
//        [bookCovers release];
//    }
//    if (results!=nil || [results count]!=0) {
//        [results release];
//    }
    
    waiting = [[UIAlertView alloc] initWithTitle:@"Searching.." message:@"\n" delegate:nil cancelButtonTitle:nil otherButtonTitles:nil];
    UIActivityIndicatorView *indicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
    indicator.center = waiting.center;
    indicator.frame = CGRectMake(130.0f, 65.0f, 20.0f, 20.0f);
    [waiting addSubview:indicator];
    [indicator startAnimating];
    [waiting show];
    [indicator release];
    
    [self performSelector:@selector(searchForBooks) withObject:nil afterDelay:0];
}

-(void)searchForBooks
{
    BookActions *searching = [[BookActions alloc] init];
    NSString *advanced;
    switch ([searchField selectedScopeButtonIndex]) {
        case 0:
        {
            advanced = @"authors";
            break;
        }
        case 1:
        {
            advanced = @"isbn";
            break;
        }
        case 2:
        {
            advanced = @"title";
            break;
        }
        default:
        {
            advanced = @"";
            break;
        }
    }
    
    results = (NSMutableArray*)[searching searchForBooksWithKeywords:searchField.text forUser:[[[NSUserDefaults standardUserDefaults] objectForKey:@"user"] objectForKey:@"username"] advancedSearch:advanced];
    [searching release];
    
    if ([[[results objectAtIndex:0] objectForKey:@"result"] integerValue] == 1) {
        NSLog(@"Found %@ books",[[results objectAtIndex:0] objectForKey:@"booksNum"]);
        [results removeObjectAtIndex:0];
        results = (NSMutableArray*)[Book copyFromSmartLib:results];
        
        bookCovers = [[NSMutableArray alloc] initWithCapacity:1];

        int count;
        for (count = 0; count<[results count]; count++) {
            NSData *image = [NSData dataWithContentsOfURL:[NSURL URLWithString:[[[results objectAtIndex:count] info] objectForKey:@"imgURL"]]];
            UIImage *cover;
            if (image == nil) {
                cover = [UIImage imageNamed:@"nocover.png"];
            }
            else {
                cover = [UIImage imageWithData:image];
            }
            [bookCovers addObject:cover];
        }
        [waiting dismissWithClickedButtonIndex:-1 animated:YES];
        [bookCovers retain];
        [self.tableView reloadData];
    }
    else if ([[[results objectAtIndex:0] objectForKey:@"result"] integerValue] == 0) {
        NSLog(@"Didn't find any books");
        [waiting dismissWithClickedButtonIndex:-1 animated:NO];
        waiting = [[UIAlertView alloc] initWithTitle:@"No books found" message:@"Your keywords didn't match any books in our database." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [waiting show];
        [waiting release];
        [results removeObjectAtIndex:0];
        [self.tableView reloadData];
    }
    else {
        NSLog(@"Error while searching books");
        [waiting dismissWithClickedButtonIndex:-1 animated:NO];
        waiting = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Sorry there was an error encountered. Please try again." delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Try again", nil];
        [waiting show];
        [waiting release];
        [results removeObjectAtIndex:0];
        [self.tableView reloadData];
    }
}

-(void)getBookCoversAsynchronous
{
    bookCovers = [[NSMutableArray alloc] initWithCapacity:1];
    int count;
    NSLog(@"%d",[results count]);
    for (count = 0; count<[results count]; count++) {
        NSLog(@"%d. %@",count,[[[results objectAtIndex:count] info] objectForKey:@"imgURL"]);
        NSData *image = [NSData dataWithContentsOfURL:[NSURL URLWithString:[[[results objectAtIndex:count] info] objectForKey:@"imgURL"]]];
        UIImage *cover;
        if (image == nil) {
            cover = [UIImage imageNamed:@"nocover.png"];
        }
        else {
            cover = [UIImage imageWithData:image];
        }
        [bookCovers addObject:cover];
    }
    [waiting dismissWithClickedButtonIndex:-1 animated:YES];
    [bookCovers retain];
    NSLog(@"%d",[bookCovers retainCount]);
    [self.tableView reloadData];
}

-(IBAction)mainMenu:(id)sender
{
    [self dismissModalViewControllerAnimated:YES];
}

#pragma mark Alert View Delegate

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (alertView.tag == CONFIRM_ALERT_TAG) {
        if (buttonIndex != alertView.cancelButtonIndex) {
            
            waiting = [[UIAlertView alloc] initWithTitle:@"Deleting book..." message:@"\n" delegate:nil cancelButtonTitle:nil otherButtonTitles:nil];
            UIActivityIndicatorView *indicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
            indicator.center = waiting.center;
            indicator.frame = CGRectMake(130.0f, 65.0f, 20.0f, 20.0f);
            [waiting addSubview:indicator];
            [indicator startAnimating];
            [waiting show];
            [waiting release];
            [indicator release];
            
            [self performSelector:@selector(beginDeleting) withObject:nil afterDelay:0];
        }
    }
    else {
        if (buttonIndex != alertView.cancelButtonIndex) {
            [self searchBarSearchButtonClicked:searchField];
        }
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
    if ([[segue identifier] isEqualToString:@"bookInfo"]) {
        BookDetailsViewController *bookInfo = [segue destinationViewController];
        bookInfo.bookInfo = [results objectAtIndex: row];
    }
}
@end
