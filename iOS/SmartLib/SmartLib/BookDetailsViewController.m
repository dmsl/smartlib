/*   GNU Public Licence
 *   BookDetailsViewController View for showing book info.
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
 *  @file BookDetailsViewController.m
 *  @brief View for showing book info.
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

#import "BookDetailsViewController.h"
#import "KeywordsViewController.h"
#import "BookActions.h"

@interface BookDetailsViewController ()

-(void)requestLent;

@end

@implementation BookDetailsViewController
{
    NSString *username;
    BookActions *state;
    UIAlertView *waiting;
    UIActionSheet *actions;
}
@synthesize cover,title,authors,pageCount,isbn,lang,publishYear,owner,availability;
@synthesize bookInfo;

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
    username =[[[NSUserDefaults standardUserDefaults] objectForKey:@"user"] objectForKey:@"username"];
    [self fillBookInfo];
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    [self.navigationItem.rightBarButtonItem setEnabled:NO];
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
}

-(void)fillBookInfo
{   
    cover.image = [UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:[bookInfo.info objectForKey:@"imgURL"]]]];
    title.text = [bookInfo.info objectForKey:@"title"];
    authors.text = [bookInfo.info objectForKey:@"authors"];
    pageCount.text = [NSString stringWithFormat:@"%@",[bookInfo.info objectForKey:@"pageCount"]];
    isbn.text = [bookInfo.info objectForKey:@"isbn"];
    lang.text = [bookInfo.info objectForKey:@"lang"];
    publishYear.text = [bookInfo.info objectForKey:@"publishedYear"];
    if ([[bookInfo.info objectForKey:@"username"] isEqualToString:@""] || ![bookInfo.info objectForKey:@"username"]) {
        owner.text = username;
        [bookInfo.info setValue:username forKey:@"username"];
    }
    else {
        owner.text = [bookInfo.info objectForKey:@"username"];
    }
    state = [[BookActions alloc] init];
    state.delegate = nil;
    NSInteger stateCode = [state stateOfBook:isbn.text user:owner.text];
    switch (stateCode) {
        case 0:
        {
            availability.text = @"YES";
            break;
        }
        case 1:
        case-1:
        case -2:
        {
            availability.text = @"NO";
            break;
        }
        case -11:
        case -12:
        {
            availability.text = @"Unknown book state";
            break;
        }
        default:
        {
            availability.text = @"Unknown book state";
            break;
        }
    }
    [state release];
}


-(IBAction)takeAction:(id)sender
{   
    state =[[BookActions alloc] init];
    NSInteger  bookState =[state stateOfBook:isbn.text user:owner.text];
    
    if (!actions) {
        actions = [[UIActionSheet alloc] initWithTitle:@"Actions" delegate:self cancelButtonTitle:nil destructiveButtonTitle:nil otherButtonTitles:nil];
        if ([username isEqualToString:[bookInfo.info objectForKey:@"username"]]) {
            [actions addButtonWithTitle:@"Delete"];
            [actions setDestructiveButtonIndex:0];
            //        [actions addButtonWithTitle:@"Edit keywords"];
            [actions addButtonWithTitle:@"Cancel"];
            [actions setCancelButtonIndex:1];
            [actions setTag:1];
        }
        else  if ((bookState == 0 ) && (![username isEqualToString:owner.text]))
        {
            [actions addButtonWithTitle:@"Borrow"];
            [actions addButtonWithTitle:@"Cancel"];
            [actions setCancelButtonIndex:1];
            [actions setTag:2];
        }
        else
        {
            [actions addButtonWithTitle:@"Cancel"];
            [actions setCancelButtonIndex:0];
            [actions setTag:3];
        }
        
        if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
            [actions showFromBarButtonItem:[self.navigationItem rightBarButtonItem] animated:YES];
        }
        else {
            [actions showInView:self.view];
        }
        [actions release];
    }
}

-(void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    actions = nil;
    if (actionSheet.tag == 1) {
        if (buttonIndex != actionSheet.cancelButtonIndex) {
            BookActions *delete = [[BookActions alloc] init];
            NSInteger result = [delete deleteBook:[bookInfo.info objectForKey:@"isbn"] ofUser:username];
            [delete release];
            switch (result) {
                case 1:
                {
                    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Success!" message:@"Book has been successfully deleted." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                    [alert show];
                    [alert release];
                    [self.navigationController popViewControllerAnimated:YES];
                    break;
                }
                case -11:
                {
                    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"There was an error while connecting to database. Please try again!" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                    [alert show];
                    [alert release];
                    break;
                }
                case -12:
                {
                    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Unexpected error. Try again!" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                    [alert show];
                    [alert release];
                    break;
                }
                default:
                    break;
            }
        }
    }
    else if (actionSheet.tag == 2) {
        if (buttonIndex != actionSheet.cancelButtonIndex) {
            waiting = [[UIAlertView alloc] initWithTitle:@"Sending Request" message:@"\n" delegate:nil cancelButtonTitle:nil otherButtonTitles:nil];
            UIActivityIndicatorView *indicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
            indicator.center = waiting.center;
            indicator.frame = CGRectMake(130.0f, 65.0f, 20.0f, 20.0f);
            [waiting addSubview:indicator];
            [indicator startAnimating];
            [waiting show];
            [waiting release];
            [indicator release];
            
            [self performSelector:@selector(requestLent) withObject:nil afterDelay:3];
        }
    }
}

-(void)requestLent
{
//    NSString *isbn = [bookInfo.info objectForKey:@"isbn"];
//    NSString *owner = [bookInfo.info objectForKey:@"username"];
    NSString *dest = [[[NSUserDefaults standardUserDefaults] objectForKey:@"user"] objectForKey:@"username"];
    BookActions *request = [[BookActions alloc] init];
    NSInteger result = [request requestBorrowingBook:isbn.text fromUser:owner.text toUser:dest];
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
        waiting = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Unknown error encountered!" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
    }
    [waiting show];
    [waiting release];
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
    [bookInfo release];
    
}

-(void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    
    [actions dismissWithClickedButtonIndex:-1 animated:YES];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    if ( interfaceOrientation == UIInterfaceOrientationPortraitUpsideDown || interfaceOrientation == UIInterfaceOrientationPortrait) {
        return YES;
    }
	return NO;
}

//#pragma mark - Table view data source
//
//- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
//{
//#warning Potentially incomplete method implementation.
//    // Return the number of sections.
//    return 1;
//}
//
//- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
//{
//#warning Incomplete method implementation.
//    // Return the number of rows in the section.
//    if (section == 0) {
//        return 1;
//    }
//    else {
//        return 7;
//    }
//}
//
//- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
//{
//    static NSString *CellIdentifier = @"Cell";
//    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
//    
//    // Configure the cell...
//    
//    return cell;
//}
//
/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationFade];
    }   
    else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }   
}
*/

/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
{
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/
//
//#pragma mark - Table view delegate
//
//- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
//{
//    // Navigation logic may go here. Create and push another view controller.
//    /*
//     <#DetailViewController#> *detailViewController = [[<#DetailViewController#> alloc] initWithNibName:@"<#Nib name#>" bundle:nil];
//     // ...
//     // Pass the selected object to the new view controller.
//     [self.navigationController pushViewController:detailViewController animated:YES];
//     [detailViewController release];
//     */
//}


-(void) prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ( [[segue identifier] isEqualToString:@"enterKeywords"] ) {
        KeywordsViewController *nextView = [segue destinationViewController];
        nextView.book = bookInfo;
        nextView.delegate = self;
    }
}
@end
