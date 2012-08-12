/*   GNU Public Licence
 *   EnterBookViewController Scanner's view.
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
 *  @file EnterBookViewController.h
 *  @brief Scanner's view.
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
#import "ScannedBooksViewController.h"
#import "SmartLibNavigationController.h"
#import "Book.h"
#import "ZBarSDK.h"

@interface EnterBookViewController : UIViewController < ZBarReaderDelegate, UIAlertViewDelegate, UIActionSheetDelegate >
{
    NSMutableArray *scannedBooks;
    IBOutlet ZBarReaderView *readerViewer;
    IBOutlet UISwitch *flash;
    IBOutlet UISwitch *duplicates;
}

@property (nonatomic, retain) NSMutableArray *scannedBooks;
@property (nonatomic, retain) IBOutlet ZBarReaderView *readerViewer;
@property (nonatomic, retain) IBOutlet UISwitch *flash;
@property (nonatomic, retain) IBOutlet UISwitch *duplicates;

@property (nonatomic) unsigned int flag;

-(IBAction)toggleFlash:(id)sender;
-(IBAction)extraOptions:(id)sender;
-(IBAction)mainMenu:(id)sender;

//action methods
-(void)saveBook:(NSString*)isbn;

@end
