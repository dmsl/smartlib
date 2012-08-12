//
//  ActivitiesForBookViewController.m
//  SmartLib
//
//  Created by Chrystalla Tsoutsouki on 7/23/12.
//  Copyright (c) 2012 University of Cyprus. All rights reserved.
//

#import "ActivitiesForBookViewController.h"
#import "ActivitiesForBookCell.h"
#import "Book.h"
#import "BookActions.h"

@implementation ActivitiesForBookViewController
{
    NSMutableArray *results;
    NSMutableArray *Returns;
    BookActions *makeAction;
    NSString *username;
    NSString *borrower;
}

//@synthesize  title,isbn;
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
    results= [[NSMutableArray alloc] initWithCapacity:2];
   // self.navigationController.title =self.mytitle;
    [results retain];
    NSString *Reguest = [[NSString alloc]initWithFormat:@"Canast02 wants to borrow your book"];
    [results addObject:Reguest];
    [Reguest release];
    Reguest = [[NSString alloc]initWithFormat:@"Pmpeis wants to borrow your book"];
    [results addObject:Reguest];
    [Reguest release];
    
    Returns = [[NSMutableArray alloc ] initWithCapacity:3];
    Reguest = [[NSString alloc]initWithFormat:@"Dzeina Returns back Your book"];
    [Returns addObject:Reguest];
    [Reguest release];
    Reguest = [[NSString alloc]initWithFormat:@"Costas Returns back Your book"];
    [Returns addObject:Reguest];
    [Reguest release];
    Reguest = [[NSString alloc]initWithFormat:@"Giorgos Return back Your book"];
    [Returns addObject:Reguest];
    [Reguest release];
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
}


- (void)viewDidUnload
{
   [super viewDidUnload];
    
 //   [results release];
  //  [self setbookName:nil];
  
    
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
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    if(section ==0)
    {
    NSLog(@"%d",[results count]);
    return [results count];
    }
    
    else 
    {
        return  [Returns count];
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{   
    
    static NSString *CellIdentifier = @"BookActivity";
    ActivitiesForBookCell *cell = (ActivitiesForBookCell *)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
//    if (cell == nil)
//    {  
//        cell=[[ActivitiesForBookCell alloc]init];
//    }
    

    if (indexPath.section ==0)
        {
            cell.activityTitle.text=[results objectAtIndex:indexPath.row];
              cell.ActivityResponse.enabled=YES;
             [cell.ActivityResponse setTag:indexPath.row];
        }
    else 
        {
            cell.activityTitle.text=[Returns objectAtIndex:indexPath.row];
            cell.ActivityResponse.hidden=YES;
        }
    
   
    return cell;
}

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

#pragma mark - Table view delegate

/*- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Navigation logic may go here. Create and push another view controller.
    
     <#DetailViewController#> *detailViewController = [[<#DetailViewController#> alloc] initWithNibName:@"<#Nib name#>" bundle:nil];
     // ...
     // Pass the selected object to the new view controller.
     [self.navigationController pushViewController:detailViewController animated:YES];
     [detailViewController release];
     */
/*    row = indexPath.row;
    [self performSegueWithIdentifier:@"enterKeywords" sender:self];
}

-(void) prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ( [[segue identifier] isEqualToString:@"enterKeywords"] ) {
        KeywordsViewController *nextView = [segue destinationViewController];
        nextView.book = [scannedBooks objectAtIndex:row];
        nextView.row = row;
        nextView.delegate = self;
    }
    else if ([[segue identifier] isEqualToString:@"booksSaved"]) {
        //        SmartLibNavigationController *menu = [segue destinationViewController];
    }
}
*/


- (IBAction)SendAResponse:(id)sender {
    
    if ([sender selectedSegmentIndex]==0)
    {
        
        NSLog(@"Accept");
        
        NSInteger state;
        makeAction=[[BookActions alloc]init];
        state= [makeAction stateOfBook:isbn user:username];
        
        if (state==0)
         {
            //NSInteger lentResult;
            /*lentResult=[makeAction lentBook:isbn fromUser:username toUser:borrower];
             if(lentResult == 1) 
             {
             UIAlertView *complete = [[UIAlertView alloc]initWithTitle:@"CompletE" message:@"Book Succesfully lented" delegate:self cancelButtonTitle:@"ok" otherButtonTitles:nil, nil];
             [complete show];
             [complete release];
             }
             */
        
            /* UIAlertView *waiting = [[UIAlertView alloc] initWithTitle:@"Sending a Response" message:@"\n" delegate:nil cancelButtonTitle:nil otherButtonTitles:nil];
             UIActivityIndicatorView *indicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
             indicator.center = waiting.center;
             indicator.frame = CGRectMake(130.0f, 65.0f, 20.0f, 20.0f);
             [waiting addSubview:indicator];
             [indicator startAnimating];
             [waiting show];
             [indicator release];
             //number  = [sender tag];
             [self performSelector:@selector(bookLent) withObject:nil afterDelay:3];*/
          }
         else if (state==1)
         {
             UIAlertView *Error = [[UIAlertView alloc]initWithTitle:@"Error" message:@"Book has already lented" delegate:self cancelButtonTitle:@"ok" otherButtonTitles:nil, nil];
             [Error show];
             [Error release];
         }
        else  if (state ==-1)
        {
            UIAlertView *Error = [[UIAlertView alloc]initWithTitle:@"Error" message:@"Book is not for rent" delegate:self cancelButtonTitle:@"ok" otherButtonTitles:nil, nil];
            [Error show];
            [Error release];
        }
        [makeAction release];
    }
    else  if ([sender selectedSegmentIndex]==1) {
        NSLog(@"Ignore");
    
    }
    
    [sender setEnabled:NO];
    
    
  }

- (void)dealloc {
    [super dealloc];
}
@end
