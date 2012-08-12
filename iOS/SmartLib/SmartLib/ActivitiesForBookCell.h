/*   GNU Public Licence
 *   ActivitiesForBookCell Custom cell for incoming requests(future).
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
 *  @file ActivitiesForBookCell.h
 *  @brief Custom cell for incoming requests(future).
 *
 *  @author Chrystalla Tsoutsouki
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

@interface ActivitiesForBookCell : UITableViewCell 
{
    IBOutlet UITextView *activityTitle;
    IBOutlet UISegmentedControl *ActivityResponse;
    IBOutlet UILabel *answer;
}
@property(nonatomic,retain) IBOutlet UITextView *activityTitle;
@property(nonatomic,retain) IBOutlet UILabel    *answer;
@property(nonatomic,retain) IBOutlet UISegmentedControl *ActivityResponse;



@end
