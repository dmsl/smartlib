/*   GNU Public Licence
 *   SendMessageViewController Internal messaging system.
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
 *  @file SendMessageViewController.m
 *  @brief Internal messaging system.
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

#import "SendMessageViewController.h"
#import "BookActions.h"

@interface SendMessageViewController ()

-(void)startSendingMessage;

@end

@implementation SendMessageViewController
{
    UIAlertView *sending;
}

@synthesize receiver,message;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    UIToolbar *accessory = [[UIToolbar alloc] initWithFrame:CGRectMake(100, 100, 100, 50)];
    [accessory setBarStyle:UIBarStyleBlackTranslucent];
    NSMutableArray *items = [[NSMutableArray alloc] initWithCapacity:2];
    UIBarButtonItem *button = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:self action:nil];
    [items addObject:button];
    [button release];
    button = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonItemStylePlain target:message action:@selector(resignFirstResponder)];
    button.title = @"Done";
    button.tintColor = [UIColor blueColor];
    [items addObject:button];
    [button release];
    
    [accessory setItems:items];
    [items release];
    
    message.inputAccessoryView = accessory;
    [accessory release];
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    if ( interfaceOrientation == UIInterfaceOrientationPortraitUpsideDown || interfaceOrientation == UIInterfaceOrientationPortrait) {
        return YES;
    }
	return NO;
}

#pragma mark Send Message Methods

-(IBAction)sendMessage:(id)sender
{
    [message resignFirstResponder];
    sending = [[UIAlertView alloc] initWithTitle:@"Sending.." message:@"\n" delegate:nil cancelButtonTitle:nil otherButtonTitles:nil];
    UIActivityIndicatorView *indicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
    indicator.center = sending.center;
    indicator.frame = CGRectMake(130.0f, 65.0f, 20.0f, 20.0f);
    [sending addSubview:indicator];
    [indicator startAnimating];
    [sending show];
    [indicator release];
    
    if ([receiver.text isEqualToString:@""] || [message.text isEqualToString:@""]) {
        [sending dismissWithClickedButtonIndex:-1 animated:NO];
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Warning!" message:@"Please fill all the fields." delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Try again", nil];
        [alert show];
        [alert release];
    }
    else {
        [self performSelector:@selector(startSendingMessage) withObject:nil afterDelay:0.1];
    }
}


-(void)startSendingMessage
{
    BookActions *sendMessage = [[BookActions alloc] init];
    NSString *username = [[[NSUserDefaults standardUserDefaults] objectForKey:@"user"] objectForKey:@"username"];
    NSInteger result = [sendMessage sentMessage:message.text fromUser:username toUser:receiver.text];
    [sendMessage release];
    
    [sending dismissWithClickedButtonIndex:-1 animated:NO];
    switch (result) {
        case 1:
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Message Sent" message:@"Your message was successfully sent." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles: nil];
            [alert show];
            [alert release];
            [self.navigationController popViewControllerAnimated:YES];
            break;
        }
        case 0:
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"User doesn't accept messages. Do you want to send it to someone else?" delegate:self cancelButtonTitle:@"NO" otherButtonTitles:@"YES", nil];
            [alert show];
            [alert release];
            break;
        }
        case -1:
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"This library doen't support messaging." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles: nil];
            [alert show];
            [alert release];
            [self.navigationController popViewControllerAnimated:YES];
            break;
        }
        default:
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"An unknown error occured." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles: nil];
            [alert show];
            [alert release];
            [self.navigationController popViewControllerAnimated:YES];
            break;
        }
    }
}

#pragma mark Alert View Delegate

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == alertView.cancelButtonIndex) {
        [self.navigationController popViewControllerAnimated:YES];
    }
    else {
        receiver.text = @"";
        [receiver becomeFirstResponder];
    }
}

#pragma mark text delegates

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    if ([textField isEqual:receiver]) {
        [message becomeFirstResponder];
    }
    else {
        [textField resignFirstResponder];
        [self sendMessage:self];
    }
    return NO;
}

-(void)textViewDidEndEditing:(UITextView *)textView
{
    [textView resignFirstResponder];
    [self sendMessage:self];
}

@end
