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
 *  @file EnterBookViewController.m
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

#import "EnterBookViewController.h"
#import <AudioToolbox/AudioToolbox.h>
#import "JSONParser.h"
#import "AppPreferencesViewController.h"
#import "BookActions.h"

#define GET_BOOL(x) [x isEqualToString:@"1"]?YES:NO

@interface EnterBookViewController ()

-(void)takeActionForISBN:(NSString*)isbn;

@end

@implementation EnterBookViewController
{
    ZBarReaderViewController *Zreader;
    UIPopoverController *popover;
    UIActionSheet *option;
    UIAlertView *waiting;
}

@synthesize scannedBooks,readerViewer,flash, duplicates, flag;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(BOOL)checkTwice:(NSString*) isbn
{
    NSInteger count;
    for (count=0; count < [scannedBooks count]; count++) {
        Book *temp = [scannedBooks objectAtIndex:count];
        
        if ([[temp.info objectForKey:@"isbn"] isEqualToString:isbn]) {
            //future use of remember duplicates switch
            //
            //            if (duplicates.on) {
            //                NSNumber *copies = [temp.info objectForKey:@"bookCopies"];
            //                copies = [NSNumber numberWithInt: [copies integerValue] + 1];
            //                [temp.info setValue:copies forKey:@"bookCopies"];
            //                [copies release];
            //            }
            return YES;
        }
    }
    return NO;
}

-(BOOL)searchGoogleBooksWithISBN:(NSString*) isbn
{
    SBJSON *parser = [[SBJSON alloc] init];
    
    // Prepare URL request to download info
    NSString *urlS = [NSString stringWithFormat: @"https://www.googleapis.com/books/v1/volumes?q=+isbn:%@&key=AIzaSyCGV3gOZL3bDjjmSHjpZ-bu1_i1CY6h-Gk",isbn];
    
    NSData *response = [NSData dataWithContentsOfURL:[NSURL URLWithString:urlS]];
    NSString *json_string = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
    
    // parse the JSON response into an object
    // Here we're using NSDictionary since we're parsing an array of JSON status objects
    NSDictionary *volumeInfo = (NSDictionary *)[parser objectWithString:json_string error:nil];
    [parser release];
    [json_string release];
    NSNumber *number = [[NSNumber alloc] initWithInt:0];
    if ([[volumeInfo objectForKey:@"totalItems"] isEqualToNumber: number] )
    {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Warning" message:@"Could not find book." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
        alert.tag = 2;
        [alert show];
        [alert release];
        [number release];
        return NO;
    }
    else
    {
        volumeInfo = [[[volumeInfo objectForKey:@"items"] objectAtIndex:0] objectForKey:@"volumeInfo"];
        Book *book = [[Book alloc] init];
        JSONParser *bookParser = [[JSONParser alloc] init];
        book.info = [NSMutableDictionary dictionaryWithObjects:[bookParser parseGoogleData: volumeInfo] forKeys:[book attributes]];
        [bookParser release];
        [scannedBooks addObject:book];
        [book release];
        [number release];
        return YES;
    }
}

- (void) imagePickerController: (UIImagePickerController*) reader
 didFinishPickingMediaWithInfo: (NSDictionary*) info
{
    //remove viewer when scan finished
    [[Zreader readerView] stop];
    
    if (GET_BOOL([[[NSUserDefaults standardUserDefaults] objectForKey:@"appPreferences"]objectForKey:@"beep"]))
    {
        [self beep];
    }
    if (GET_BOOL([[[NSUserDefaults standardUserDefaults]objectForKey:@"appPreferences"]objectForKey:@"vibrate"]))
    {
        [self vibrate];
    }
    
    // ADD: get the decode results
    id<NSFastEnumeration> results = [info objectForKey: ZBarReaderControllerResults];
    ZBarSymbol *symbol = nil;
    for(symbol in results)
        // EXAMPLE: just grab the first barcode
        break;
    
    [self performSelector:@selector(takeActionForISBN:) withObject:symbol.data afterDelay:0];
}

-(void)takeActionForISBN:(NSString *)isbn
{
    // Do something useful with the barcode data
    if (![self checkTwice:isbn])
    {
        if ([self searchGoogleBooksWithISBN:isbn]) {
            waiting = [[UIAlertView alloc] initWithTitle:@"Please wait..." message:@"\n" delegate:nil cancelButtonTitle:nil otherButtonTitles:nil];
            UIActivityIndicatorView *indicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
            indicator.center = waiting.center;
            indicator.frame = CGRectMake(130.0f, 65.0f, 20.0f, 20.0f);
            [waiting addSubview:indicator];
            [indicator startAnimating];
            [waiting show];
            [indicator release];
            [waiting release];
            
            switch (flag) {
                case 1:
                {
                    [self performSelector:@selector(saveBook:) withObject:isbn afterDelay:0];
                    break;
                }
                case 2:
                {
                    [waiting dismissWithClickedButtonIndex:-1 animated:NO];
                    [self performSegueWithIdentifier:@"scannedBooks" sender:self];
                    break;
                }
                default:
                    break;
            }
        }
        else {
            [[Zreader readerView] start];
        }
    }
    else {
        //future use of remember duplicates switch
        //
        //        if (duplicates.on) {
        //            NSString *msg = [NSString stringWithFormat:@"Book %@ copies increased by 1.",symbol.data];
        //            UIAlertView *increasedCopies = [[UIAlertView alloc] initWithTitle:@"Book Copies" message:msg delegate:nil cancelButtonTitle:@"OK" otherButtonTitles: nil];
        //            [increasedCopies show];
        //            [increasedCopies release];
        //            [self performSegueWithIdentifier:@"scannedBooks" sender:self];
        //            [saving removeFromSuperview];
        //        }
        //        else {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Warning" message:@"Book was already scanned." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
        alert.tag = 2;
        [alert show];
        [alert release];
        //        }
    }
}

-(void)saveBook:(NSString *)isbn
{
    BookActions *addBook = [[BookActions alloc] init];
    NSInteger result = [addBook saveBook:isbn];
    [addBook release];
    [waiting dismissWithClickedButtonIndex:-1 animated:YES];
    NSString *title;
    NSString *message;
    
    switch (result) {
        case 1:
        {
            title = [NSString stringWithFormat:@"Saved"];
            message = [NSString stringWithFormat:@"Book successfully added to your library."];
            break;
        }
        case 0:
        {
            //could not save
            title = [NSString stringWithFormat:@"Error"];
            message = [NSString stringWithFormat:@"Book is already added to your library."];
            break;
        }
        case -2:
        {
            //no info
            title = [NSString stringWithFormat:@"Error"];
            message = [NSString stringWithFormat:@"Could not retrieve book info."];
            break;
        }
        case -11:
        {
            //database error
            title = [NSString stringWithFormat:@"Error"];
            message = [NSString stringWithFormat:@"Database internal error."];
            break;
        }
        case -12:
        {
            //unexpected error
            title = [NSString stringWithFormat:@"Error"];
            message = [NSString stringWithFormat:@"Unexpected Error."];
            break;
        }
        default:
        {
            //unknown error
            title = [NSString stringWithFormat:@"Error"];
            message = [NSString stringWithFormat:@"Unknown error."];
            break;
        }
    }
    
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:title message:message delegate:nil cancelButtonTitle:@"OK" otherButtonTitles: nil];
    [alert show];
    [alert release];
    [self performSegueWithIdentifier:@"scannedBooks" sender:self];
}

-(UIView *)makeViewer
{
    Zreader = [ZBarReaderViewController new];
    Zreader.readerDelegate = self;
    Zreader.supportedOrientationsMask = ZBarOrientationMaskAll;
    Zreader.videoQuality = UIImagePickerControllerQualityTypeIFrame1280x720;
    Zreader.showsZBarControls = NO;
    Zreader.wantsFullScreenLayout = NO;
    Zreader.readerView.zoom = 0;
    Zreader.readerView.frame = [readerViewer bounds];
    
    ZBarImageScanner *scanner2 = Zreader.scanner;
    
    NSUserDefaults *scanning1Donly = [NSUserDefaults standardUserDefaults];
    if ([[[scanning1Donly objectForKey:@"appPreferences"] objectForKey:@"scanning1Donly"] isEqualToString:@"1"]) {
        [scanner2 setSymbology:ZBAR_QRCODE config:ZBAR_CFG_ENABLE to:0];
    }
    
    // EXAMPLE: disable rarely used I2/5 to improve performance
    [scanner2 setSymbology: ZBAR_I25
                    config: ZBAR_CFG_ENABLE
                        to: 0];
    
    return Zreader.view;
}

-(IBAction)toggleFlash:(id)sender
{
    if (flash.on)
    {
        Zreader.cameraFlashMode = UIImagePickerControllerCameraFlashModeOn;
    }
    else {
        Zreader.cameraFlashMode = UIImagePickerControllerCameraFlashModeOff;
    }
}

#pragma mark AlertView Delegate

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (alertView.tag == 1) {
        if (buttonIndex == 1) {
            if (GET_BOOL([[[NSUserDefaults standardUserDefaults] objectForKey:@"appPreferences"]objectForKey:@"beep"]))
            {
                [self beep];
            }
            if (GET_BOOL([[[NSUserDefaults standardUserDefaults]objectForKey:@"appPreferences"]objectForKey:@"vibrate"]))
            {
                [self vibrate];
            }
            NSString *isbn = [alertView textFieldAtIndex:0].text;
            
            [self performSelector:@selector(takeActionForISBN:) withObject:isbn afterDelay:0];
        }
        else {
            [[Zreader readerView] start];
        }
    }
    else if (alertView.tag == 2) {
        [[Zreader readerView] start];
    }
    else if (alertView.tag == 3) {
        if (buttonIndex == 0) {
            [[Zreader readerView] start];
        }
        else if (buttonIndex == 1) {
            [self dismissModalViewControllerAnimated:YES];
        }
    }
}

-(IBAction)extraOptions:(id)sender
{
    [[Zreader readerView] stop];
    if ([popover isPopoverVisible]) {
        [popover dismissPopoverAnimated:YES];
    }
    if (!option) {
        option = [[UIActionSheet alloc] initWithTitle:@"Options" delegate:self cancelButtonTitle:nil destructiveButtonTitle:nil otherButtonTitles:@"Enter ISBN",@"Preferences", nil];
        if ([scannedBooks count]) {
            [option addButtonWithTitle:@"Clipboard last book"];
            [option addButtonWithTitle:@"Save"];
            [option addButtonWithTitle:@"Cancel"];
            [option setCancelButtonIndex:4];
        }
        else {
            [option addButtonWithTitle:@"Cancel"];
            [option setCancelButtonIndex:2];
        }
        if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
            [option showFromBarButtonItem:[self.navigationItem rightBarButtonItem] animated:YES];
        }
        else {
            [option showInView:self.view];
        }
        [option release];
    }
}

-(void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    option = nil;
    if (buttonIndex == actionSheet.cancelButtonIndex) {
        [[Zreader readerView] start];
    }
    else if (buttonIndex == 0) {
        UIAlertView *enterISBN = [[UIAlertView alloc] initWithTitle:@"Enter ISBN" message:nil delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"OK", nil];
        enterISBN.tag = 1;
        enterISBN.alertViewStyle = UIAlertViewStylePlainTextInput;
        [enterISBN textFieldAtIndex:0].keyboardType = UIKeyboardTypeNumberPad;
        [enterISBN show];
        [enterISBN release];
    }
    else if (buttonIndex == 1) {
        [self performSegueWithIdentifier:@"preferences" sender:self];
    }
    else if (buttonIndex == 2) {
        UIPasteboard *clipboard = [UIPasteboard generalPasteboard];
        clipboard.string = [[[scannedBooks lastObject] info] objectForKey:@"isbn"];
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Copied" message:@"Last book's ISBN copied to clipboard." delegate:self cancelButtonTitle:@"OK" otherButtonTitles: nil];
        [alert setTag:2];
        [alert show];
        [alert release];
    }
    else if (buttonIndex == 3) {
        [self performSegueWithIdentifier:@"scannedBooks" sender:self];
    }
}

-(IBAction)mainMenu:(id)sender
{
    if (option) {
        [option dismissWithClickedButtonIndex:-1 animated:NO];
        option = nil;
    }
    if ([popover isPopoverVisible]) {
        [popover dismissPopoverAnimated:NO];
    }
    //    [[Zreader readerView] stop];
    //    UIAlertView *mainMenu = [[UIAlertView alloc] initWithTitle:@"Warning!" message:@"You will lose all unsaved scanned books. Is it OK?" delegate:self cancelButtonTitle:@"Don't Leave" otherButtonTitles:@"Main Menu", nil];
    //    mainMenu.tag = 3;
    //    [mainMenu show];
    //    [mainMenu release];
    [self dismissModalViewControllerAnimated:YES];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    Zreader = nil;
    Zreader.cameraFlashMode = UIImagePickerControllerCameraFlashModeOff;
    [readerViewer addSubview:[self makeViewer]];
    //    [readerViewer sizeToFit];
    flash.on = NO;
    duplicates.on = NO;
    scannedBooks = [[NSMutableArray alloc] initWithCapacity:4];
    
    if (![UIImagePickerController isFlashAvailableForCameraDevice:UIImagePickerControllerCameraDeviceRear]) {
        flash.enabled = NO;
    }
}

-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [[Zreader readerView] start];
    if (flash.on) {
        Zreader.cameraFlashMode = UIImagePickerControllerCameraFlashModeOn;
    }
    else {
        Zreader.cameraFlashMode = UIImagePickerControllerCameraFlashModeOff;
    }
}

-(void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
    [[Zreader readerView] stop];
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    [Zreader release];
    [readerViewer release];
    [scannedBooks release];
    [popover release];
}

//beep manager
-(void)beep
{
    CFURLRef sound = (CFURLRef)[[NSBundle mainBundle] URLForResource:@"Computer Data 01" withExtension:@"caf"];
    SystemSoundID soundID;
    AudioServicesCreateSystemSoundID(sound, &soundID);
    AudioServicesPlaySystemSound(soundID);
    sound = nil;
}

//vibration manager
-(void)vibrate
{
    AudioServicesPlayAlertSound(kSystemSoundID_Vibrate);
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    if ( interfaceOrientation == UIInterfaceOrientationPortraitUpsideDown || interfaceOrientation == UIInterfaceOrientationPortrait) {
        return YES;
    }
	return NO;
}

-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    if ([popover isPopoverVisible]) {
        [popover dismissPopoverAnimated:NO];
    }
}

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([[segue identifier] isEqualToString:@"scannedBooks"]) {
        ScannedBooksViewController *history = [segue destinationViewController];
        history.scannedBooks = self.scannedBooks;
        history.flag = self.flag;
    }
    else if ([[segue identifier] isEqualToString:@"preferences"]) {
        if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
            AppPreferencesViewController *dvc = [(UIStoryboardPopoverSegue*)segue destinationViewController];
            popover = [(UIStoryboardPopoverSegue *)segue popoverController];
            dvc.popover = popover;
        }
    }
    
    if ([popover isPopoverVisible]) {
        [popover dismissPopoverAnimated:NO];
    }
    if (option) {
        [option dismissWithClickedButtonIndex:-1 animated:YES];
        option = nil;
    }
    
}


@end
