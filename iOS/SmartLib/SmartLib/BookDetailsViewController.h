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
 *  @file BookDetailsViewController.h
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

#import <UIKit/UIKit.h>
@class Book;

@interface BookDetailsViewController : UITableViewController <UIActionSheetDelegate>
{
    IBOutlet UIImageView *cover;
    IBOutlet UITextView *title;
    IBOutlet UITextView *authors;
    IBOutlet UITextView *pageCount;
    IBOutlet UITextView *publishYear;
    IBOutlet UITextView *isbn;
    IBOutlet UITextView *lang;
    IBOutlet UITextView *owner;
    IBOutlet UITextView *availability;
    IBOutlet UITextView *borrower;
     IBOutlet UITextView *library_name;
    
    Book *bookInfo;
}

@property (nonatomic, retain) IBOutlet UIImageView *cover;
@property (nonatomic, retain) IBOutlet UITextView *title;
@property (nonatomic, retain) IBOutlet UITextView *authors;
@property (nonatomic, retain) IBOutlet UITextView *pageCount;
@property (nonatomic, retain) IBOutlet UITextView *publishYear;
@property (nonatomic, retain) IBOutlet UITextView *isbn;
@property (nonatomic, retain) IBOutlet UITextView *lang;
@property (nonatomic, retain) IBOutlet UITextView *owner;
@property (nonatomic, retain) IBOutlet UITextView *availability;
@property (nonatomic, retain) IBOutlet UITextView *borrower;
@property (nonatomic, retain) IBOutlet UITextView *library_name;
@property (nonatomic, retain) Book *bookInfo;

-(IBAction)takeAction:(id)sender;

@end
