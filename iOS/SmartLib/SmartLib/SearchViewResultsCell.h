/*   GNU Public Licence
 *   SearchViewResultsCell Custom cell for search results table.
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
 *  @file SearchViewResultsCell.h
 *  @brief Custom cell for search results table.
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

@interface SearchViewResultsCell : UITableViewCell
{
    IBOutlet UILabel *bookTitle;
    IBOutlet UILabel *availability;
    IBOutlet UIImageView *bookCover;
    IBOutlet UISegmentedControl *borrowBook;
    IBOutlet UISegmentedControl *deleteBook;
    IBOutlet UIButton *bookInfo;
}

@property (nonatomic, retain) IBOutlet UILabel *bookTitle;
@property (nonatomic, retain) IBOutlet UILabel *availability;
@property (nonatomic, retain) IBOutlet UIImageView *bookCover;
@property (nonatomic, retain) IBOutlet UISegmentedControl *borrowBook;
@property (nonatomic, retain) IBOutlet UISegmentedControl *deleteBook;
@property (nonatomic, retain) IBOutlet UIButton *bookInfo;

@end
