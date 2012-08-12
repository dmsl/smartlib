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
 *  @file ScannedBooksViewController.h
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

#import <UIKit/UIKit.h>
#import "EnterBookViewController.h"
#import "SmartLibNavigationController.h"

@interface ScannedBooksViewController : UIViewController <UITableViewDataSource, UITableViewDelegate, UIAlertViewDelegate, UIActionSheetDelegate>
{
    NSMutableArray *scannedBooks;
    IBOutlet UIImageView *lastBookCover;
    IBOutlet UITableView *scannedBooksList;
}

@property (nonatomic, strong) NSMutableArray *scannedBooks;
@property (nonatomic, retain) IBOutlet UIImageView *lastBookCover;
@property (nonatomic, retain) UITableView *scannedBooksList;

@property (nonatomic) unsigned int flag;

@end
