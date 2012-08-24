/*   GNU Public Licence
 *   RegisterViewController View for registering to system.
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
 *  @file RegisterViewController.h
 *  @brief View for registering to system.
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

@interface RegisterViewController : UITableViewController <UIPickerViewDelegate, UIPickerViewDataSource, UITextFieldDelegate, UIAlertViewDelegate>
{
    IBOutlet UITextField *username;
    IBOutlet UITextField *fname;
    IBOutlet UITextField *lname;
    IBOutlet UITextField *email;
    IBOutlet UITextField *password;
    IBOutlet UITextField *confirmPassword;
    IBOutlet UITextField *telephone;
    IBOutlet UISwitch *emailNotifications;
    IBOutlet UISwitch *appNotifications;
    IBOutlet UIPickerView *libraries;
}

@property (nonatomic, retain) IBOutlet UIBarButtonItem *title;
@property (nonatomic, retain) IBOutlet UITextField *username;
@property (nonatomic, retain) IBOutlet UITextField *fname;
@property (nonatomic, retain) IBOutlet UITextField *lname;
@property (nonatomic, retain) IBOutlet UITextField *email;
@property (nonatomic, retain) IBOutlet UITextField *password;
@property (nonatomic, retain) IBOutlet UITextField *confirmPassword;
@property (nonatomic, retain) IBOutlet UITextField *telephone;
@property (nonatomic, retain) IBOutlet UISwitch *emailNotifications;
@property (nonatomic, retain) IBOutlet UISwitch *appNotifications;
@property (nonatomic, retain) IBOutlet UIPickerView *libraries;
@property (nonatomic, retain) NSString *baseURL;
@property (nonatomic, retain) NSString *baseName;

-(IBAction)resetFields:(id)sender;
-(IBAction)submitRegistration:(id)sender;
-(IBAction)showList:(id)sender;
-(void)refreshTitle;

@end
