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
 *  @author Chrysovalantis Anastasiou, Chrystalla Tsoutsouki, Aphrodite Christou
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

-(NSMutableArray*)getLibraries
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
    
    //NSLog(@"json %@",json);
    
    [formData release];
    [request release];
    
    JSONParser *parser = [[JSONParser alloc] init];
    NSMutableArray*result = (NSMutableArray *) [parser parseResponseToArray:json];
    [parser release];
    [json release];
    
    //NSLog(@"all %@",result);
    return result;
}

-(NSMutableArray*)getUserLibraries
{
    NSLog(@"Getting User libraries..");
    
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",MASTER_URL,GET_USER_LIBRARIES_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    NSString *formData = [[NSString alloc] initWithFormat:@"device=iOS&username=%@",[[userDefaults objectForKey:@"user"] objectForKey:@"username"]];
    [request setHTTPMethod:@"POST"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:[formData dataUsingEncoding:NSUTF8StringEncoding]];
    
    NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    NSString *json = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
    
    //NSLog(@"json %@",json);
    
    [formData release];
    [request release];
    
    JSONParser *parser = [[JSONParser alloc] init];
    
    NSMutableArray*result = (NSMutableArray *) [parser parseResponseToArray:json];
    [parser release];
    [json release];
    
    //NSLog(@"user %@",result);
    return result;
}

/*-(NSArray*)getLibraries
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
*/

-(NSArray*)searchForLibrariesWithKeywords:(NSString*)keywords // advancedSearch:(NSString*)extras
{
    NSLog(@"Searching Libraries with keywords: %@",keywords);
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",MASTER_URL,SEARCH_LIBRARIES_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    //NSString *formData=[[NSString alloc]initWithFormat:@"device=iOS&username=%@&column=%@&keyword=%@",user,extras,keywords];
    
    //NSLog(@"extras %@ keywords %@",extras,keywords);
    
    NSString *formData;
    //search Lib with contains specific keyword
 //   if ([extras isEqualToString:@"name"]) {
        //NSLog(@"name");
        formData =[[NSString alloc] initWithFormat:@"device=iOS&keyword=%@",keywords];
  /*  }
    //return all libraries
    else {
        //NSLog(@"all");
        formData =[[NSString alloc] initWithFormat:@"device=iOS"];
    }
    */
    
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

-(NSArray*)searchForUserLibrariesWithKeywords:(NSString*)keywords forUser:(NSString*)user; //advancedSearch:(NSString*)extras
{
    NSLog(@"Searching User Libraries with keywords: %@",keywords);
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",MASTER_URL,SEARCH_USER_LIBRARIES_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    //NSString *formData=[[NSString alloc]initWithFormat:@"device=iOS&username=%@&column=%@&keyword=%@",user,extras,keywords];
    
    NSString *formData;
    //search Lib with contains specific keyword
  //  if ([extras isEqualToString:@"name"]) {
        formData =[[NSString alloc] initWithFormat:@"device=iOS&username=%@&keyword=%@",user,keywords];
   /* }
    //return all libraries
    else {
        formData =[[NSString alloc] initWithFormat:@"device=iOS&username=%@",user];
    }
 */
    //search a book that belong to specific library
    //NSString *formData =[[NSString alloc] initWithFormat:@"device=iOS&usernane=%@&keyword=%@",user,keywords];
    
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
    
    //NSLog(@"%@",result);
    
    return result;
}


-(NSInteger)saveBook:(NSString*)isbn
{
    NSLog(@"Adding Book with isbn: %@",isbn);
    
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",MASTER_URL,INSERT_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    //NSString *formData =[[NSString alloc] initWithFormat:@"device=iOS&isbn=%@&username=%@",isbn,[[userDefaults objectForKey:@"user"] objectForKey:@"username"]];
    
    NSLog(@"id %@",[[userDefaults objectForKey:@"currentLib"] objectForKey:@"id"]);
    
    //Save to specific library
    NSString *formData =[[NSString alloc] initWithFormat:@"device=iOS&isbn=%@&username=%@&libid=%@",isbn,[[userDefaults objectForKey:@"user"] objectForKey:@"username"],[[userDefaults objectForKey:@"currentLib"] objectForKey:@"id"]];
    
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
    
    //[userDefaults removeObjectForKey:@"userBooks"]; //Add new Book -> update userBooks list
    //[userDefaults synchronize];
    
    return [result integerValue];
}

-(NSInteger)stateOfBook:(NSString *)isbn user:(NSString *)username
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Looking for book state.");
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",MASTER_URL,BOOK_STATE_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    //NSString *formData = [[NSString alloc] initWithFormat:@"device=iOS&isbn=%@&username=%@",isbn,username];
    
    //Return status of book that belong to specific library
    NSString *formData =[[NSString alloc] initWithFormat:@"device=iOS&isbn=%@&username=%@&libid=%@",isbn,[[userDefaults objectForKey:@"user"] objectForKey:@"username"],[[userDefaults objectForKey:@"currentLib"] objectForKey:@"id"]];
    
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
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",MASTER_URL,LENT_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    //NSString *formData= [[NSString alloc] initWithFormat:@"device=iOS&owner=%@&destination=%@&isbn=%@",owner,borrower,isbn];
    
    //Lent a book that belong to specific library
    NSString *formData =[[NSString alloc] initWithFormat:@"device=iOS&owner=%@&destination=%@&isbn=%@&libid=%@",owner,borrower,isbn,[[userDefaults objectForKey:@"currentLib"] objectForKey:@"id"]];
    
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
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",MASTER_URL,RETURN_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    //NSString *formData= [[NSString alloc] initWithFormat:@"device=iOS&owner=%@&isbn=%@",owner,isbn];
    
    //Lent a book that belong to specific library
    NSString *formData =[[NSString alloc] initWithFormat:@"device=iOS&owner=%@&isbn=%@&libid=%@",owner,isbn,[[userDefaults objectForKey:@"currentLib"] objectForKey:@"id"]];
    
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
    
    //NSLog(@"username %@ libid %@",user,[[userDefaults objectForKey:@"currentLib"] objectForKey:@"id"]);
    
    //NSLog(@"lib id %@",[[userDefaults objectForKey:@"currentLib"] objectForKey:@"id"]);
    
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",MASTER_URL,USER_BOOKS_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    
    //Get books that belong to library
    NSString *formData=[[NSString alloc] initWithFormat:@"device=iOS&username=%@&libid=%@",user,[[userDefaults objectForKey:@"currentLib"] objectForKey:@"id"]];
    [request setHTTPMethod:@"POST"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:[formData dataUsingEncoding:NSUTF8StringEncoding]];
    
    NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    NSString *json = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
    
   // NSLog(@"json %@",json);
    
    [formData release];
    [request release];
    
    JSONParser *parser = [[JSONParser alloc] init];
    NSMutableArray*result = (NSMutableArray *) [parser parseResponseToArray:json];
    
    [parser release];
    [json release];
    
    //NSLog(@"getUser result %@",result);
    
    return result;
}

-(NSInteger)changeStatusForBook:(NSString *)isbn ofUser:(NSString *)user newStatus:(NSInteger)newStatus
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Change Status For Book.");
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",MASTER_URL,CHANGE_BOOK_STATUS_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    //NSString *formData=[[NSString alloc] initWithFormat:@"device=iOS&isbn=%@&username=%@&newstatus=%d",isbn,user,newStatus];
    
    //Change status of book that belong to specific library
    NSString *formData =[[NSString alloc] initWithFormat:@"device=iOS&isbn=%@&username=%@&newstatus=%d&libid=%@",isbn,user,newStatus,[[userDefaults objectForKey:@"currentLib"] objectForKey:@"id"]];
    
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
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",MASTER_URL,DELETE_BOOK_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    //NSString *formData= [[NSString alloc] initWithFormat:@"device=iOS&isbn=%@&username=%@",isbn,user];
   
    //delete a book that belong to specific library
    NSString *formData =[[NSString alloc] initWithFormat:@"device=iOS&isbn=%@&username=%@&libid=%@",isbn,user,[[userDefaults objectForKey:@"currentLib"] objectForKey:@"id"]];
    
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
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",MASTER_URL,MOBILE_SEARCH_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    //NSString *formData=[[NSString alloc]initWithFormat:@"device=iOS&username=%@&column=%@&keyword=%@",user,extras,keywords];
    
    //NSLog(@"0. %@ 1. %@ 2. %@ 3. %@",user,extras,keywords,[[userDefaults objectForKey:@"currentLib"] objectForKey:@"id"]);
    
    //search a book that belong to specific library
    NSString *formData =[[NSString alloc] initWithFormat:@"device=iOS&username=%@&column=%@&keyword=%@&libid=%@",user,extras,keywords,[[userDefaults objectForKey:@"currentLib"] objectForKey:@"id"]];
    
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
    
    //NSLog(@"%@",result);
    
    return result;
}

-(NSInteger)requestBorrowingBook:(NSString *)isbn fromUser:(NSString *)owner toUser:(NSString *)borrower
{
 //   NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Request a Book.");
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",MASTER_URL,REQUEST_BOOK_PHP];
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
    
 //   NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Incoming Requests.");
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",MASTER_URL,INCOMING_REQUESTS_PHP];
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
 //   NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Outgoing Requests.");
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",MASTER_URL,OUTGOING_REQUESTS_PHP];
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
//    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Delete Request");
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",MASTER_URL,DELETE_REQUEST_PHP];
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
 //   NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Books Given.");
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",MASTER_URL,BOOKS_GIVEN_PHP];
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
  //  NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Books Taken.");
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",MASTER_URL,BOOKS_TAKEN_PHP];
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
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",MASTER_URL,POPULAR_BOOKS_PHP];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL]];
    //NSString *formData= [[NSString alloc] initWithFormat:@"device=iOS&username=%@",username];
    
    //search a book that belong to specific library
    NSString *formData =[[NSString alloc] initWithFormat:@"device=iOS&username=%@&libid=%@",username,[[userDefaults objectForKey:@"currentLib"] objectForKey:@"id"]];
    
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
    
    //NSLog(@"%@",result);
    
    return result;
}

-(NSInteger) ReplyUser:(NSString *)owner RequestFrom:(NSString *)destination forBook:(NSString *)isbn withAnswer:(NSInteger)answer;
{
//    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Reply Request");
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",MASTER_URL,REPLY_REQUEST_PHP];
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
 //   NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSLog(@"Send Message");
    NSString *URL = [NSString stringWithFormat:@"%@/mobile%@",MASTER_URL,SEND_MESSAGE_PHP];
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
