//
//  SaveBooksViewController.m
//  SmartLib
//
//  Created by Chrysovalantis Anastasiou on 10/07/2012.
//  Copyright (c) 2012 University of Cyprus. All rights reserved.
//

#import "SaveBooksViewController.h"
#import "KeywordsViewController.h"
#import "BookActions.h"
//#import "JSONParser.h"

@interface SaveBooksViewController ()

-(void)startSaving;

@end

@implementation SaveBooksViewController

@synthesize scannedBooks,row;
@synthesize temp,makeReplacement;
@synthesize saving;

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
}

-(void)viewDidAppear:(BOOL)animated
{
    if (makeReplacement) {
        [scannedBooks replaceObjectAtIndex:row withObject:temp];
        makeReplacement = NO;
    }
    if ([self checkKeywords]) {
        self.navigationItem.rightBarButtonItem.enabled = YES;
    }
    [super viewDidAppear:animated];
}

-(BOOL) checkKeywords
{
    for (NSInteger count = 0; count < [scannedBooks count]; count++) {
        Book *check = [scannedBooks objectAtIndex:count];
        if (check.keywords == nil || [check.keywords isEqualToString:@""])
            return NO;
    }
    return YES;
}

-(IBAction)saveBooks:(id)sender
{
//    saving = [[UIAlertView alloc] initWithTitle:@"Saving.." message:@"\n" delegate:nil cancelButtonTitle:nil otherButtonTitles:nil];
//    UIActivityIndicatorView *indicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
//    indicator.center = saving.center;
//    indicator.frame = CGRectMake(130.0f, 65.0f, 20.0f, 20.0f);
//    [saving addSubview:indicator];
//    [indicator startAnimating];
//    [saving show];
//    [indicator release];
//    
//    [self performSelector:@selector(startSaving) withObject:nil afterDelay:1];
//    
////    [saving dismissWithClickedButtonIndex:-1 animated:NO];
    [self dismissModalViewControllerAnimated:YES];
}

-(void)startSaving
{
//    BookActions *save = [[BookActions alloc] init];
//    save.delegate = saving;
//    [save saveBooks:scannedBooks];
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
    [scannedBooks release];
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
    return [scannedBooks count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"scannedBook";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];

//    if (cell == nil)
//    {
//        cell=[[UITableViewCell alloc]initWithStyle:UITableViewCellStyleValue2 reuseIdentifier:CellIdentifier];
//    }

    // Configure the cell...
    Book *book = [scannedBooks objectAtIndex:indexPath.row];

    cell.textLabel.text = [book.info objectForKey:@"title"];
    cell.detailTextLabel.text = [book.info objectForKey:@"isbn"];
    
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

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    row = indexPath.row;
//    [self performSegueWithIdentifier:@"enterKeywords" sender:self];
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
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

@end
