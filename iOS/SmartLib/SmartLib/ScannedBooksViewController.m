/*   GNU Public Licence
 *   ScannedBooksViewController Based on flag saves, rents or returns books.
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
 *  @file ScannedBooksViewController.m
 *  @brief Based on flag saves, rents or returns books.
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

#import "ScannedBooksViewController.h"
//#import "SaveBooksViewController.h"
#import "RentalViewController.h"
#import "BookActions.h"

@interface ScannedBooksViewController ()

-(void)goBack;
-(void)customDidAppear;

-(void)returnBook;
-(void)rentBookToUser:(NSString*)user;

@end

@implementation ScannedBooksViewController
{
    NSString *isbn;
    NSString *title;
    NSString *username;
    
    UIAlertView *waiting;
}

@synthesize scannedBooks,scannedBooksList,lastBookCover,flag;

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
    NSLog(@"ScannedBooksViewController");
    [super viewDidLoad];
	// Do any additional setup after loading the view.
//    self.navigationItem.hidesBackButton = YES;
    Book *book = [scannedBooks lastObject];
    NSString *lastBookImage = [book.info objectForKey:@"imgURL"];
    if (![lastBookImage isEqualToString:@"N/A"]) {
        NSData *image = [NSData dataWithContentsOfURL:[NSURL URLWithString:lastBookImage]];
        lastBookCover.image = [UIImage imageWithData:image];
    }

    [[self.toolbarItems objectAtIndex:2] setEnabled:NO];
}


- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    [lastBookCover release];
}

-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    [self customDidAppear];
}

#pragma mark - Controller's methods

-(void)customDidAppear
{
    if (flag == 1) {
        NSTimer *goBack =[NSTimer timerWithTimeInterval:3.5 target:self selector:@selector(goBack) userInfo:nil repeats:NO];
        NSRunLoop *timer = [NSRunLoop currentRunLoop];
        [timer addTimer:goBack forMode:NSRunLoopCommonModes];
    }
    else {
        BookActions *getState = [[BookActions alloc] init];
        isbn = [[[scannedBooks lastObject] info] objectForKey:@"isbn"];
        username = [[[NSUserDefaults standardUserDefaults] objectForKey:@"user"] objectForKey:@"username"];
        NSInteger bookState = [getState stateOfBook:isbn user:username];
        [getState release];
        [scannedBooks removeLastObject];
        
        switch (bookState) {
            case 1:
            {
                //return it
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Return it?" message:@"This book is rented. Do you want to make a return?" delegate:self cancelButtonTitle:@"No" otherButtonTitles:@"Yes", nil];
                alert.tag = 1;
                [alert show];
                [alert release];
                break;
            }
            case 0:
            {
                //rent it
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Rent it?" message:@"This book is available. Do you want to rent it?" delegate:self cancelButtonTitle:@"No" otherButtonTitles:@"Yes", nil];
                alert.tag = 2;
                [alert show];
                [alert release];
                break;
            }
            case -1:
            {
                //no rentals
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Warning!" message:@"This book is not available for rentals." delegate:self cancelButtonTitle:@"OK" otherButtonTitles: nil];
                alert.tag = 100;
                [alert show];
                [alert release];
                break;
            }
            case -2:
            {
                //no rentals
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Warning!" message:@"This book is not available for rentals." delegate:self cancelButtonTitle:@"OK" otherButtonTitles: nil];
                alert.tag = 100;
                [alert show];
                [alert release];
                break;
            }
            case -11:
            {
                //database error
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Internal database error." delegate:self cancelButtonTitle:@"Scan again" otherButtonTitles: nil];
                alert.tag = 100;
                [alert show];
                [alert release];
                break;
            }
            case -12:
            {
                //unexpected error
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Unexpected error." delegate:self cancelButtonTitle:@"Scan again" otherButtonTitles: nil];
                alert.tag = 100;
                [alert show];
                [alert release];
                break;
            }
            default:
            {
                //unknown error
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Unknown error." delegate:self cancelButtonTitle:@"Scan again" otherButtonTitles: nil];
                alert.tag = 100;
                [alert show];
                [alert release];
                break;
            }
        }
    }
}

-(void)goBack
{
    [self.navigationController popViewControllerAnimated:YES];
}

-(void)returnBook
{
    BookActions *action = [[BookActions alloc] init];
    NSInteger result = [action returnBook:isbn toUser:username];
    [action release];
    [waiting dismissWithClickedButtonIndex:-1 animated:YES];
    
    switch (result) {
        case 1:
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Returned" message:@"Book successfully returned to your library" delegate:self cancelButtonTitle:@"OK" otherButtonTitles: nil];
            alert.tag = 100;
            [alert show];
            [alert release];
            break;
        }
        case -11:
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Internal database error." delegate:self cancelButtonTitle:@"Go Back" otherButtonTitles:@"Try again", nil];
            alert.tag = 101;
            [alert show];
            [alert release];
            break;
        }
        case -12:
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Unexpected error." delegate:self cancelButtonTitle:@"Go Back" otherButtonTitles:@"Try again", nil];
            alert.tag = 101;
            [alert show];
            [alert release];
            break;
        }
        default:
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Unknown error." delegate:self cancelButtonTitle:@"Go Back" otherButtonTitles:@"Try again", nil];
            alert.tag = 101;
            [alert show];
            [alert release];
            break;
        }
    }
}

-(void)rentBookToUser:(NSString *)user
{
    BookActions *rent = [[BookActions alloc] init];
    NSInteger result = [rent lentBook:isbn fromUser:username toUser:user];
    [rent release];
    [waiting dismissWithClickedButtonIndex:-1 animated:YES];
    
    switch (result) {
        case 1:
        {
            NSString *msg = [NSString stringWithFormat:@"Book successfully borrowed to %@",user];
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Rented" message:msg delegate:self cancelButtonTitle:@"OK" otherButtonTitles: nil];
            alert.tag = 100;
            [alert show];
            [alert release];
            break;
        }
        case -11:
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Internal database error." delegate:self cancelButtonTitle:@"Go Back" otherButtonTitles:@"Try again", nil];
            alert.tag = 2;
            [alert show];
            [alert release];
            break;
        }
        case -12:
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Unexpected error." delegate:self cancelButtonTitle:@"Go Back" otherButtonTitles:@"Try again", nil];
            alert.tag = 2;
            [alert show];
            [alert release];
            break;
        }
        default:
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Unknown error." delegate:self cancelButtonTitle:@"Go Back" otherButtonTitles:@"Try again", nil];
            alert.tag = 2;
            [alert show];
            [alert release];
            break;
        }
    }

    
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
    
    // Configure the cell...
    Book *book = [scannedBooks objectAtIndex:indexPath.row];
    
    cell.textLabel.text = [book.info objectForKey:@"title"];
    cell.detailTextLabel.text = [book.info objectForKey:@"isbn"];
    
    return cell;
}


// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the specified item to be editable.
    return YES;
}

//
///*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [scannedBooks removeObjectAtIndex:indexPath.row];
        
        [tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationFade];
    }   
    else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }   
}


//// Override to support rearranging the table view.
//- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
//{
//    [scannedBooks exchangeObjectAtIndex:fromIndexPath.row withObjectAtIndex:toIndexPath.row];
//}

// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the item to be re-orderable.
    return NO;
}

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}

#pragma mark - Alert View delegate

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (alertView.tag == 1) {
        if (buttonIndex == alertView.cancelButtonIndex) {
            
        }
        else {
            //return the book
            waiting = [[UIAlertView alloc] initWithTitle:@"Returning book..." message:@"\n" delegate:nil cancelButtonTitle:nil otherButtonTitles:nil];
            UIActivityIndicatorView *indicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
            indicator.center = waiting.center;
            indicator.frame = CGRectMake(130.0f, 65.0f, 20.0f, 20.0f);
            [waiting addSubview:indicator];
            [indicator startAnimating];
            [waiting show];
            [indicator release];
            [waiting release];
            
            [self performSelector:@selector(returnBook) withObject:nil afterDelay:0];
        }
    }
    else if (alertView.tag == 2) {
        if (buttonIndex == alertView.cancelButtonIndex) {
            [self.navigationController popViewControllerAnimated:YES];
        }
        else {
            UIAlertView *rental = [[UIAlertView alloc] initWithTitle:@"Rent Book..." message:@"Who will you rent the book to?" delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Rent", nil];
            [rental setAlertViewStyle:UIAlertViewStylePlainTextInput];
            rental.tag = 21;
            [rental show];
            [rental release];
        }
    }
    else if (alertView.tag == 21) {
        if (buttonIndex == alertView.cancelButtonIndex) {
            
        }
        else {
            //rent the book
            waiting = [[UIAlertView alloc] initWithTitle:@"Renting book..." message:@"\n" delegate:nil cancelButtonTitle:nil otherButtonTitles:nil];
            UIActivityIndicatorView *indicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
            indicator.center = waiting.center;
            indicator.frame = CGRectMake(130.0f, 65.0f, 20.0f, 20.0f);
            [waiting addSubview:indicator];
            [indicator startAnimating];
            [waiting show];
            [indicator release];
            [waiting release];
            
            [self performSelector:@selector(rentBookToUser:) withObject:[alertView textFieldAtIndex:0].text afterDelay:0];
        }
    }
    else if (alertView.tag == 100) {
        [self.navigationController popViewControllerAnimated:YES];
    }
    else if (alertView.tag == 101) {
        if (buttonIndex == alertView.cancelButtonIndex) {
            [self.navigationController popViewControllerAnimated:YES];
        }
        else {
            [self performSelector:@selector(returnBook) withObject:nil afterDelay:0];
        }
    }
}

#pragma mark - Orientations

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    if ( interfaceOrientation == UIInterfaceOrientationPortraitUpsideDown || interfaceOrientation == UIInterfaceOrientationPortrait) {
        return YES;
    }
	return NO;
}

@end
