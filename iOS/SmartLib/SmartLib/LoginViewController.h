/*   GNU Public Licence
 *   LoginViewController View for logging in to system.
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
 *  @file LoginViewController
 *  @brief View for logging in to system.
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

#import <UIKit/UIKit.h>

@interface LoginViewController : UITableViewController <UIPickerViewDelegate, UIPickerViewDataSource, UITextFieldDelegate,UIAlertViewDelegate>
{
    IBOutlet UITextField *username;
    IBOutlet UITextField *password;
    IBOutlet UISwitch *remember;
    IBOutlet UIPickerView *libraries;
}

@property (nonatomic, retain) IBOutlet UITextField *username;
@property (nonatomic, retain) IBOutlet UITextField *password;
@property (nonatomic, retain) IBOutlet UISwitch *remember;
@property (nonatomic, retain) IBOutlet UIPickerView *libraries;

@property (nonatomic, retain) IBOutlet UILabel *title;
@property (nonatomic, retain) NSDictionary *currentLib;
@property (nonatomic, retain) NSString *baseURL;
@property (nonatomic, retain) NSString *baseName;

-(IBAction)login:(id)sender;
-(IBAction)showList:(id)sender;
-(void)refreshTitle;

@end
