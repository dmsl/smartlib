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
 *  @file MyBooksViewController.h
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



#import <UIKit/UIKit.h>

#import "SmartLibNavigationController.h"
#import "Book.h"
#import "MyBookResultsCell.h"

@interface MyBooksViewController : UITableViewController <UITableViewDataSource,UITableViewDelegate,UIActionSheetDelegate,UIAlertViewDelegate >
{
    NSString *username;
    Book *temp;
    MyBookResultsCell *myCell;
}

@property(nonatomic,retain) UITableView *list;

@property(nonatomic,retain) NSString *username;
@property(nonatomic,retain)  Book *temp;
@property(nonatomic,retain) MyBookResultsCell *myCell;


-(IBAction)mainMenu:(id)sender;
-(IBAction)reloadBooks:(id)sender;

@end
