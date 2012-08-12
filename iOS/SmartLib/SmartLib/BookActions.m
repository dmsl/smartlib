/*   GNU Public Licence
 *   BookActions.m SmartLib API
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
 *  @file BookActions.m
 *  @brief SmartLib API
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

#import "BookActions.h"

@implementation BookActions

@synthesize delegate;

-(id)init
{
    self = [super init];
    if (self) {
        //Custom init
    }
    return self;
}

-(NSArray*)getLibraries
{
    NSLog(@"Getting libraries..");
    NSString *URL = [NSString stringWithFormat:@"%@%@",MASTER_URL,GET_LIBRARIES_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    NSString *formData = [[NSString alloc] initWithFormat:@"device=iOS"];
    [request setHTTPMethod:@"POST"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:[formData dataUsingEncoding:NSUTF8StringEncoding]];
    
    NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    NSString *json = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
    
    [formData release];
    [request release];
    
    JSONParser *parser = [[JSONParser alloc] init];
    NSArray *result = [parser parseResponseToArray:json];
    [parser release];
    [json release];
    
    return result;
}

-(NSInteger)saveBook:(NSString*)isbn
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Adding Book with isbn: %@",isbn);
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",[userDefaults objectForKey:@"baseURL"],INSERT_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    NSString *formData =[[NSString alloc] initWithFormat:@"device=iOS&isbn=%@&username=%@",isbn,[[userDefaults objectForKey:@"user"] objectForKey:@"username"]];
    [request setHTTPMethod:@"POST"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:[formData dataUsingEncoding:NSUTF8StringEncoding]];
    
    NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    NSString *json = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
    
    [formData release];
    [request release];
    
    JSONParser *parser = [[JSONParser alloc] init];
    NSString *result = [[parser parseResponse:json] objectForKey:@"result"];
    [parser release];
    [json release];
    
    return [result integerValue];
}

-(NSInteger)stateOfBook:(NSString *)isbn user:(NSString *)username
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Looking for book state.");
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",[userDefaults objectForKey:@"baseURL"],BOOK_STATE_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    NSString *formData = [[NSString alloc] initWithFormat:@"device=iOS&isbn=%@&username=%@",isbn,username];
    [request setHTTPMethod:@"POST"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:[formData dataUsingEncoding:NSUTF8StringEncoding]];
    
    NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    NSString *json = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
    
    [formData release];
    [request release];
    
    JSONParser *parser = [[JSONParser alloc] init];
    NSString *result = [[parser parseResponse:json] objectForKey:@"result"];
    [parser release];
    [json release];
    
    return [result integerValue];
}

-(NSInteger)lentBook:(NSString*)isbn fromUser:(NSString*)owner toUser:(NSString*)borrower
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Lenting Book.");
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",[userDefaults objectForKey:@"baseURL"],LENT_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    NSString *formData= [[NSString alloc] initWithFormat:@"device=iOS&owner=%@&destination=%@&isbn=%@",owner,borrower,isbn];
    [request setHTTPMethod:@"POST"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:[formData dataUsingEncoding:NSUTF8StringEncoding]];
    
    NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    NSString *json = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
    
    [formData release];
    [request release];
    
    JSONParser *parser = [[JSONParser alloc] init];
    NSString *result = [[parser parseResponse:json] objectForKey:@"result"];
    [parser release];
    [json release];
    
    [delegate dismissWithClickedButtonIndex:-1 animated:NO];
    return [result integerValue];
}

-(NSInteger)returnBook:(NSString*)isbn toUser:(NSString*)owner
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Return a Book.");
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",[userDefaults objectForKey:@"baseURL"],RETURN_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    NSString *formData= [[NSString alloc] initWithFormat:@"device=iOS&owner=%@&isbn=%@",owner,isbn];
    [request setHTTPMethod:@"POST"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:[formData dataUsingEncoding:NSUTF8StringEncoding]];
    
    NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    NSString *json = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
    
    [formData release];
    [request release];
    
    JSONParser *parser = [[JSONParser alloc] init];
    NSString *result = [[parser parseResponse:json] objectForKey:@"result"];
    [parser release];
    [json release];
    
    return [result integerValue];
}

-(NSMutableArray*)getBooksForUser:(NSString *)user
{
    //Chrystalla implement this
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    NSLog(@"Get Books For User");
    
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",[userDefaults objectForKey:@"baseURL"],USER_BOOKS_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    NSString *formData=[[NSString alloc] initWithFormat:@"device=iOS&username=%@",user];
    [request setHTTPMethod:@"POST"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:[formData dataUsingEncoding:NSUTF8StringEncoding]];
    
    NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    NSString *json = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
    
    [formData release];
    [request release];
    
    JSONParser *parser = [[JSONParser alloc] init];
    NSMutableArray*result = (NSMutableArray *) [parser parseResponseToArray:json];
    [parser release];
    [json release];
    
    return result;
    
}

-(NSInteger)changeStatusForBook:(NSString *)isbn ofUser:(NSString *)user newStatus:(NSInteger)newStatus
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Change Status For Book.");
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",[userDefaults objectForKey:@"baseURL"],CHANGE_BOOK_STATUS_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    NSString *formData=[[NSString alloc] initWithFormat:@"device=iOS&isbn=%@&username=%@&newstatus=%d",isbn,user,newStatus];
    [request setHTTPMethod:@"POST"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:[formData dataUsingEncoding:NSUTF8StringEncoding]];
    
    NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    NSString *json = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
    
    [formData release];
    [request release];
    
    JSONParser *parser = [[JSONParser alloc] init];
    NSString *result = [[parser parseResponse:json] objectForKey:@"result"];
    [parser release];
    [json release];
    
    return [result integerValue];
}

-(NSInteger)deleteBook:(NSString *)isbn ofUser:(NSString *)user
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Deleting Book.");
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",[userDefaults objectForKey:@"baseURL"],DELETE_BOOK_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    NSString *formData= [[NSString alloc] initWithFormat:@"device=iOS&isbn=%@&username=%@",isbn,user];
    [request setHTTPMethod:@"POST"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:[formData dataUsingEncoding:NSUTF8StringEncoding]];
    
    NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    NSString *json = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
    
    [formData release];
    [request release];
    
    JSONParser *parser = [[JSONParser alloc] init];
    NSString *result = [[parser parseResponse:json] objectForKey:@"result"];
    [parser release];
    [json release];
    
    return [result integerValue]; 
}

-(NSArray*)searchForBooksWithKeywords:(NSString*)keywords forUser:(NSString*)user advancedSearch:(NSString*)extras
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Searching Books with keywords: %@",keywords);
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",[userDefaults objectForKey:@"baseURL"],MOBILE_SEARCH_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    NSString *formData=[[NSString alloc]initWithFormat:@"device=iOS&username=%@&column=%@&keyword=%@",user,extras,keywords];
    [request setHTTPMethod:@"POST"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:[formData dataUsingEncoding:NSUTF8StringEncoding]];
    
    NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    NSString *json = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
    
    [formData release];
    [request release];
    
    JSONParser *parser = [[JSONParser alloc] init];
    NSArray *result = [parser parseResponseToArray:json];
    [parser release];
    [json release];
    
    return result;
}

-(NSInteger)requestBorrowingBook:(NSString *)isbn fromUser:(NSString *)owner toUser:(NSString *)borrower
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Request a Book.");
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",[userDefaults objectForKey:@"baseURL"],REQUEST_BOOK_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    NSString *formData= [[NSString alloc] initWithFormat:@"device=iOS&owner=%@&username=%@&isbn=%@",owner,borrower,isbn];
    
    [request setHTTPMethod:@"POST"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:[formData dataUsingEncoding:NSUTF8StringEncoding]];
    
    NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    NSString *json = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
    
    [formData release];
    [request release];
    
    JSONParser *parser = [[JSONParser alloc] init];
    NSString *result = [[parser parseResponse:json] objectForKey:@"result"];
    [parser release];
    [json release];
    
    return [result integerValue];
}

-(NSMutableArray *) IncomingRequestsforUser:(NSString *)username
{
    
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Incoming Requests.");
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",[userDefaults objectForKey:@"baseURL"],INCOMING_REQUESTS_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    NSString *formData= [[NSString alloc] initWithFormat:@"device=iOS&username=%@",username];
    [request setHTTPMethod:@"POST"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:[formData dataUsingEncoding:NSUTF8StringEncoding]];

    NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    NSString *json = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
    
    [formData release];
    [request release];
    
    JSONParser *parser = [[JSONParser alloc] init];
    NSMutableArray*result = (NSMutableArray *) [parser parseResponseToArray:json];
    [parser release];
    [json release];
    
    return result;


}
-(NSMutableArray *) OutGoingRequestsforUser:(NSString *)username;
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Outgoing Requests.");
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",[userDefaults objectForKey:@"baseURL"],OUTGOING_REQUESTS_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    NSString *formData= [[NSString alloc] initWithFormat:@"device=iOS&username=%@",username];
    [request setHTTPMethod:@"POST"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:[formData dataUsingEncoding:NSUTF8StringEncoding]];
    
    NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    NSString *json = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
    
    [formData release];
    [request release];
    
    JSONParser *parser = [[JSONParser alloc] init];
    NSMutableArray*result = (NSMutableArray *) [parser parseResponseToArray:json];
    [parser release];
    [json release];
    
    return result;
}

-(NSInteger)deleteRequestForBook:(NSString*)isbn sentFromUser:(NSString*)username toUser:(NSString*)owner
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Delete Request");
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",[userDefaults objectForKey:@"baseURL"],DELETE_REQUEST_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    NSString *formData= [[NSString alloc] initWithFormat:@"device=iOS&owner=%@&username=%@&isbn=%@",owner,username,isbn];
    
    [request setHTTPMethod:@"POST"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:[formData dataUsingEncoding:NSUTF8StringEncoding]];
    
    NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    NSString *json = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
    
    [formData release];
    [request release];
    
    JSONParser *parser = [[JSONParser alloc] init];
    NSString *result = [[parser parseResponse:json] objectForKey:@"result"];
    [parser release];
    [json release];
    
    return [result integerValue];
}

-(NSMutableArray *) BooksGivenFromUser:(NSString *) username
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Books Given.");
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",[userDefaults objectForKey:@"baseURL"],BOOKS_GIVEN_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    NSString *formData= [[NSString alloc] initWithFormat:@"device=iOS&username=%@",username];
    [request setHTTPMethod:@"POST"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:[formData dataUsingEncoding:NSUTF8StringEncoding]];
    
    NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    NSString *json = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
    
    [formData release];
    [request release];
    
    JSONParser *parser = [[JSONParser alloc] init];
    NSMutableArray*result = (NSMutableArray *) [parser parseResponseToArray:json];
    [parser release];
    [json release];
    
    return result;
}
-(NSMutableArray *) BooksGivenToUser:(NSString *)username
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Books Taken.");
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",[userDefaults objectForKey:@"baseURL"],BOOKS_TAKEN_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    NSString *formData= [[NSString alloc] initWithFormat:@"device=iOS&username=%@",username];
    [request setHTTPMethod:@"POST"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:[formData dataUsingEncoding:NSUTF8StringEncoding]];
    
    NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    NSString *json = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
    
    [formData release];
    [request release];
    
    JSONParser *parser = [[JSONParser alloc] init];
    NSMutableArray*result = (NSMutableArray *) [parser parseResponseToArray:json];
    [parser release];
    [json release];
    
    return result;
}


-(NSArray *)popularBooksForUser:(NSString*)username
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Top Books.");
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",[userDefaults objectForKey:@"baseURL"],POPULAR_BOOKS_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    NSString *formData= [[NSString alloc] initWithFormat:@"device=iOS&username=%@",username];
    [request setHTTPMethod:@"POST"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:[formData dataUsingEncoding:NSUTF8StringEncoding]];
    
    NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    NSString *json = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
    
    [formData release];
    [request release];
    
    JSONParser *parser = [[JSONParser alloc] init];
    NSArray *result = [parser parseResponseToArray:json];
    [parser release];
    [json release];
    
    return result;
}

-(NSInteger) ReplyUser:(NSString *)owner RequestFrom:(NSString *)destination forBook:(NSString *)isbn withAnswer:(NSInteger)answer;
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Reply Request");
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",[userDefaults objectForKey:@"baseURL"],REPLY_REQUEST_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    NSString *formData= [[NSString alloc] initWithFormat:@"device=iOS&owner=%@&destination=%@&isbn=%@&answer=%d",owner,destination,isbn,answer];
    
    [request setHTTPMethod:@"POST"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:[formData dataUsingEncoding:NSUTF8StringEncoding]];
    
    NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    NSString *json = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
    
    [formData release];
    [request release];
    
    JSONParser *parser = [[JSONParser alloc] init];
    NSString *result = [[parser parseResponse:json] objectForKey:@"result"];
    [parser release];
    [json release];
    
    return [result integerValue];
}

-(NSInteger)sentMessage:(NSString*)message fromUser:(NSString*)sender toUser:(NSString*)receiver
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Send Message");
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",[userDefaults objectForKey:@"baseURL"],SEND_MESSAGE_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    NSString *formData= [[NSString alloc] initWithFormat:@"device=iOS&username=%@&destination=%@&message=%@",sender,receiver,message];
    [request setHTTPMethod:@"POST"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:[formData dataUsingEncoding:NSUTF8StringEncoding]];
    
    NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    NSString *json = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
    
    [formData release];
    [request release];
    
    JSONParser *parser = [[JSONParser alloc] init];
    NSString *result = [[parser parseResponse:json] objectForKey:@"result"];
    [parser release];
    [json release];
    
    return [result integerValue];
}

@end
