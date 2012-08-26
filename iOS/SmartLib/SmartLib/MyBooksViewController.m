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
 *  @file MyBooksViewController.m
 *  @brief Shows the books user has in his library.
 *
 *  @author Chrystalla Tsoutsouki, Chrysovalantis Anastasiou
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

#import "MyBooksViewController.h"
#import "Book.h"
#import "BookDetailsViewController.h"
#import "MyBookResultsCell.h"
#import "ActivitiesForBookViewController.h"
#import "BookActions.h"


@interface MyBooksViewController ()

-(void)getBooks;
-(void)readFromPhone;
-(void)rentBookToUser:(NSString *)user;

@end

@implementation MyBooksViewController
{
    NSInteger number;
    NSMutableArray *scannedBooks;
    UIAlertView *loading;
    NSInteger NextStatus;
}

@synthesize username,temp,list,myCell;

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(IBAction)mainMenu:(id)sender
{
    [self dismissModalViewControllerAnimated:YES];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    // create a toolbar that has two buttons in the right
    UIToolbar* tools = [[UIToolbar alloc] initWithFrame:CGRectMake(0, 0, 96, 44.01)];
    NSMutableArray* buttons = [[NSMutableArray alloc] initWithCapacity:2];
    
    // create a standard "refresh" button to reset the fields
    UIBarButtonItem* bi = [[UIBarButtonItem alloc]
                           initWithBarButtonSystemItem:UIBarButtonSystemItemRefresh target:self action:@selector(reloadBooks:)];
    bi.style = UIBarButtonItemStyleBordered;
    [buttons addObject:bi];
    [bi release];
    
    //edit button
    [buttons addObject:[self editButtonItem]];
    
    [tools setItems:buttons animated:NO];
    [buttons release];
    
    // and put the toolbar in the nav bar
    UIBarButtonItem *right = [[UIBarButtonItem alloc] initWithCustomView:tools];
    [tools release];
    
    self.navigationItem.rightBarButtonItem = right;
    [right release];

    NSUserDefaults *userDef = [NSUserDefaults standardUserDefaults];
    username =[[userDef objectForKey:@"user"] objectForKey:@"username"];
    if (![userDef objectForKey:@"userBooks"]) {
        [self reloadBooks:self];
    }
    else {
        loading = [[UIAlertView alloc] initWithTitle:@"Reading from device.." message:@"\n" delegate:nil cancelButtonTitle:nil otherButtonTitles:nil];
        UIActivityIndicatorView *indicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
        indicator.center = loading.center;
        indicator.frame = CGRectMake(130.0f, 65.0f, 20.0f, 20.0f);
        [loading addSubview:indicator];
        [indicator startAnimating];
        [loading show];
        [loading release];
        [indicator release];
        
        [self performSelector:@selector(readFromPhone) withObject:nil afterDelay:0];
    }
}

-(void)readFromPhone
{
    NSUserDefaults *userDef = [NSUserDefaults standardUserDefaults];
    NSMutableArray *tempDicts = [userDef objectForKey:@"userBooks"];
    scannedBooks = [[NSMutableArray alloc] initWithCapacity:[tempDicts count]];
    Book *tempBook;
    for (NSInteger count=0; count < [tempDicts count]; count++) {
        tempBook = [[Book alloc] init];
        tempBook.info = [tempDicts objectAtIndex:count];
        [scannedBooks addObject:tempBook];
        [tempBook release];
    }
    [loading dismissWithClickedButtonIndex:-1 animated:YES];
    [self.tableView reloadData];
}

-(IBAction)reloadBooks:(id)sender
{
    loading = [[UIAlertView alloc] initWithTitle:@"Getting list.." message:@"\n" delegate:nil cancelButtonTitle:nil otherButtonTitles:nil];
    UIActivityIndicatorView *indicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
    indicator.center = loading.center;
    indicator.frame = CGRectMake(130.0f, 65.0f, 20.0f, 20.0f);
    [loading addSubview:indicator];
    [indicator startAnimating];
    [loading show];
    [loading release];
    [indicator release];
    
    [self performSelector:@selector(getBooks) withObject:nil afterDelay:0];
}

-(void)getBooks
{
    BookActions *GetBooks=[[BookActions alloc]init];
    scannedBooks=[GetBooks getBooksForUser:username];
    [scannedBooks retain];
    
    [loading dismissWithClickedButtonIndex:-1 animated:YES];
    
    //Check the result that script returns
    //if result ==1 (User Books have been returned sucessfully) then we delete first row of the scannedBooks because is unused.
    if ([[[scannedBooks objectAtIndex:0] objectForKey:@"result"] integerValue] == 1) {
        [(NSMutableArray*) scannedBooks removeObjectAtIndex:0];
        scannedBooks = (NSMutableArray*)[Book copyFromSmartLib:scannedBooks];
        NSUserDefaults *myBooks = [NSUserDefaults standardUserDefaults];
        NSMutableArray *dicts = [[NSMutableArray alloc] initWithCapacity:[scannedBooks count]];
        for (NSInteger count=0; count<[scannedBooks count]; count++) {
            [dicts addObject:[[scannedBooks objectAtIndex:count] info]];
        }
        [myBooks setObject:dicts forKey:@"userBooks"];
        [myBooks synchronize];
        [dicts release];
        [self.tableView reloadData];
    }
    else if ([[[scannedBooks objectAtIndex:0] objectForKey:@"result"] integerValue] == 0)
    {
        UIAlertView *warning = [[UIAlertView alloc]initWithTitle:@"Warning"  message:@"You don't have any books" delegate:self  cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        [warning setTag:2];
        [warning show];
        [warning release];
    }
    else
    {
        UIAlertView *error =[[UIAlertView alloc] initWithTitle:@"Error" message:@"Unexpected error appeared!" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        [error setTag:2];
        [error show];
        [error release];
    }
    
    [GetBooks release];

}

-(void)rentBookToUser:(NSString *)user
{
    BookActions *rent = [[BookActions alloc] init];
    NSString *isbn = [[[scannedBooks objectAtIndex:number] info] objectForKey:@"isbn"];
    NSInteger result = [rent lentBook:isbn fromUser:username toUser:user];
    [rent release];
    [loading dismissWithClickedButtonIndex:-1 animated:YES];
    
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
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Internal database error." delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Try again", nil];
            alert.tag = 3;
            [alert show];
            [alert release];
            break;
        }
        case -12:
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Unexpected error." delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Try again", nil];
            alert.tag = 3;
            [alert show];
            [alert release];
            break;
        }
        default:
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Unknown error." delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Try again", nil];
            alert.tag = 3;
            [alert show];
            [alert release];
            break;
        }
    }
}

-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
}


- (void)viewDidUnload
{
    [super viewDidUnload];
    [scannedBooks release];
//    [loading release];
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
    
    return [scannedBooks count];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    static NSString *CellIdentifier = @"cell";
    MyBookResultsCell *cell= (MyBookResultsCell*) [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    number=indexPath.row;
    
    cell.title.text = [[[scannedBooks objectAtIndex:indexPath.row] info] objectForKey:@"title"];
    NSString *BookImage =[[[scannedBooks objectAtIndex:indexPath.row] info] objectForKey:@"imgURL"];
    NSData *imageURL=[[NSData alloc] initWithContentsOfURL:[NSURL URLWithString:BookImage]];
    
    if (imageURL !=nil)
    {
        cell.bookCover.image=[UIImage imageWithData:imageURL];
    }
    else {
        cell.bookCover.image = [UIImage imageNamed:@"nocover.png"];
    }
    [imageURL release];
    
    [cell setTag:indexPath.row];
    
    return cell;
}

-(BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
{
    return NO;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    number = indexPath.row;
    
    NSString *isbn= [[[scannedBooks objectAtIndex:number] info] objectForKey:@"isbn"];
    
    BookActions * action = [[BookActions alloc]init];
    NSInteger result = [action stateOfBook:isbn user:username];
    
    //result ==1 Rented
    if(result ==1)
    {
        UIActionSheet* myOptions = [[UIActionSheet alloc] initWithTitle:@"Options" delegate:self cancelButtonTitle:@"Cancel"destructiveButtonTitle:nil otherButtonTitles:@"Book Information",nil];
        myOptions.tag = 1;
        [myOptions showInView:self.view];
        [myOptions release];
    }
    //not rental
    else if(result ==-1 || result ==-2)
    {
        UIActionSheet* myOptions = [[UIActionSheet alloc] initWithTitle:@"Options" delegate:self cancelButtonTitle:@"Cancel"destructiveButtonTitle:nil otherButtonTitles:@"Book Information",@"Change Book Status" ,nil];
        myOptions.tag = 2;
        [myOptions showInView:self.view];
        [myOptions release];
        
    }
    //available
    else if(result==0)
    {
        UIActionSheet* myOptions = [[UIActionSheet alloc] initWithTitle:@"Options" delegate:self cancelButtonTitle:@"Cancel"destructiveButtonTitle:nil otherButtonTitles:@"Book Information",@"Change Book Status",@"Lent the book" ,nil];
        myOptions.tag = 3;
        [myOptions showInView:self.view];
        [myOptions release];
    }
    
    //Error appears
    else if (result ==-11 || result ==-12)
    {
        UIAlertView *Error =[[UIAlertView alloc] initWithTitle:@"Error" message:@"Unexpected error appeared!" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        [Error show];
        [Error release];
    }
    
    //    [isbn release];
    [action release];
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}

-(void) tableView:(UITableView *)tableView accessoryButtonTappedForRowWithIndexPath:(NSIndexPath *)indexPath
{
    number = indexPath.row;
    
    [self performSegueWithIdentifier:@"bookInfo" sender:self];
}


-(BOOL) tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ([[[[scannedBooks objectAtIndex:indexPath.row] info] objectForKey:@"status"] integerValue] != 1) {
        return YES;
    }
    else {
        return NO;
    }
}


- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    
    if (alertView.tag==1)
    {
        
        if(buttonIndex == alertView.cancelButtonIndex)
        {
            BookActions * action = [[BookActions alloc]init];
            NSString *isbn= [[[scannedBooks objectAtIndex:number] info] objectForKey:@"isbn"];
            
            NSInteger statusChange = [action changeStatusForBook:isbn ofUser:username newStatus:NextStatus];
            [action release];
            
            if (statusChange)
            {
                UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Book Status "  message:@"Book status succesfully changed" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
                [alert show];
                [alert release];
            }
        }
    }
    else if (alertView.tag == 2) {
        if (buttonIndex == alertView.cancelButtonIndex) {
            [self dismissModalViewControllerAnimated:YES];
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
    else if (alertView.tag == 3) {
        if (buttonIndex == alertView.cancelButtonIndex) {
            
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
            loading = [[UIAlertView alloc] initWithTitle:@"Renting book..." message:@"\n" delegate:nil cancelButtonTitle:nil otherButtonTitles:nil];
            UIActivityIndicatorView *indicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
            indicator.center = loading.center;
            indicator.frame = CGRectMake(130.0f, 65.0f, 20.0f, 20.0f);
            [loading addSubview:indicator];
            [indicator startAnimating];
            [loading show];
            [indicator release];
            [loading release];
            
            [self performSelector:@selector(rentBookToUser:) withObject:[alertView textFieldAtIndex:0].text afterDelay:0];
        }
 
    }
    else if (alertView.tag == 100) {
        if (buttonIndex != alertView.cancelButtonIndex) {
            
            NSInteger del;
            BookActions *Book = [[BookActions alloc] init];
            NSString *isbn= [[[scannedBooks objectAtIndex:number] info] objectForKey:@"isbn"];
            del= [Book deleteBook:isbn ofUser:username];
            [Book release];
            
            if(del==1)
            {
                UIAlertView *Complete =[[UIAlertView alloc] initWithTitle:@"Delete" message:@"Book Succesfully deleted" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
                [Complete show];
                
                [scannedBooks removeObjectAtIndex:number];
                [self.tableView reloadData];
                [Complete release];
            }
        }
    }
}


-(void) tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(editingStyle ==UITableViewCellEditingStyleDelete)
    {
        BookActions *Book = [[BookActions alloc]init];
        NSString *isbn= [[[scannedBooks objectAtIndex:indexPath.row] info] objectForKey:@"isbn"];
        
        //check book's state.
        NSInteger state= [Book stateOfBook:isbn user:username];
        number = indexPath.row;
        // STATE -> AVAILABLE ->NO RENTAL  so user can delete it
        if (state==0 || state==-1 || state== -2) {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Confirm" message:@"Are you sure you want to delete this book?" delegate:self cancelButtonTitle:@"No" otherButtonTitles:@"Yes",nil];
            alert.tag = 100;
            [alert show];
            [alert release];
        }
        //STATE->RENTED
        else if (state ==1)
        {
            UIAlertView *Error =[[UIAlertView alloc] initWithTitle:@"Error" message:@"Book is rented." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
            [Error show];
            [Error release];
        }
        
        else
        {
            UIAlertView *Error =[[UIAlertView alloc] initWithTitle:@"Error" message:@"Unexpected error appeared!" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
            [Error show];
            [Error release];
        }
        [Book release];
    }
}


-(void) tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)sourceIndexPath toIndexPath:(NSIndexPath *)destinationIndexPath
{
    [scannedBooks exchangeObjectAtIndex:[sourceIndexPath row] withObjectAtIndex:[destinationIndexPath row]];
}


-(void)actionSheet:(UIActionSheet *) actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == actionSheet.cancelButtonIndex)
    {
        NSLog(@"Cancel");
    }
    
    else if (buttonIndex == 0)
    {
        NSLog(@"Book info");
        [self performSegueWithIdentifier:@"bookInfo" sender:self];
    }
    
    else if(buttonIndex == 1)
    {
        
        NSLog(@"Change Book Status");
        NSString *isbn= [[[scannedBooks objectAtIndex:number] info] objectForKey:@"isbn"];
        
        BookActions * action = [[BookActions alloc]init];
        NSInteger result = [action stateOfBook:isbn user:username];
        
        NSString *Currentstatus=nil;
        NSString *NewStatus=nil;
        
        if (result==-1 || result==-2)
        {
            Currentstatus=[[NSString alloc]initWithFormat:@"No Rental"];
            NewStatus=[[NSString alloc]initWithFormat:@"Available"];
            NextStatus=0;
        }
        
        else if (!result)
        {
            Currentstatus=[[NSString alloc]initWithFormat:@"Available"];
            NewStatus=[[NSString alloc]initWithFormat:@"No Rental"];
            NextStatus =-1;
        }
        
        else
        {
            UIAlertView *Error =[[UIAlertView alloc] initWithTitle:@"Error" message:@"Unexpected error appeared!" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
            [Error show];
            [Error release];
            [self viewDidDisappear:YES];
        }
        
        NSString *msg = [NSString stringWithFormat:@"Book status is going from %@ to %@",Currentstatus ,NewStatus];
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Book Status "  message:msg delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        alert.tag =1;
        [ alert show];
        [alert release];
        
        // [msg release];
        [NewStatus release];
        [Currentstatus release];
        [action release];
    }
    else
    {
        NSLog(@"Lent");
        
        UIAlertView *rental = [[UIAlertView alloc] initWithTitle:@"Rent Book..." message:@"Who will you rent the book to?" delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Rent", nil];
        [rental setAlertViewStyle:UIAlertViewStylePlainTextInput];
        rental.tag = 21;
        [rental show];
        [rental release];
    }
}

-(void) prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if([[segue identifier] isEqualToString:@"bookInfo"])
    {
        BookDetailsViewController *mybooks = [segue destinationViewController];
        mybooks.bookInfo = [scannedBooks objectAtIndex:number];
    }
    
    
}

@end
