/*   GNU Public Licence
 *   LibraryDetailsViewController Current library's details.
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
 *  @file LibraryDetailsViewController.m
 *  @brief Current library's details.
 *
 *  @author Chrysovalantis Anastasiou, Chrystalla Tsoutsouki
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

#import "LibraryDetailsViewController.h"

@interface LibraryDetailsViewController ()

@end

@implementation LibraryDetailsViewController
{
    NSDictionary *libDetails;
}


@synthesize items;

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
    items = [[NSArray alloc] initWithObjects:@"Library Name",@"Owner", nil ];
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    libDetails = [defaults objectForKey:@"currentLib"];
    //NSLog(@"LIB DETAILS %@",libDetails);
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
}

-(void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
    
    [items release];
}

- (void)viewDidUnload
{   
   
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil
    
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    if ( interfaceOrientation == UIInterfaceOrientationPortraitUpsideDown || interfaceOrientation == UIInterfaceOrientationPortrait) {
        return YES;
    }
	return NO;
}

-(NSString*)getKeyForIndex:(NSInteger)index
{
    NSString *key;
    switch (index) {
        case 0:
        {
            key = [libDetails objectForKey:@"name"];
            if ([key isEqualToString:@""])
                key=@"N/A";
            break;
        }
        case 1:
        {
            key= [libDetails objectForKey:@"createdby"];
            if ([key isEqualToString:@""])
                key=@"N/A";
            break;
        }
        default:
        {
            key = @"N/A";
            break;
        }
    }
    return key;
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
    return [items count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"libDetail";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    // Configure the cell...
    
    cell.textLabel.text = [items objectAtIndex:indexPath.row];
    cell.detailTextLabel.text = [self getKeyForIndex:indexPath.row];
    
    return cell;
    
}

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{

}

- (void)dealloc {
   
    [super dealloc];
}
@end
