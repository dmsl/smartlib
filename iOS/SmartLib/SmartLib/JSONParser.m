/*   GNU Public Licence
 *   JSONParser Parses json into useful objects.
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
 *  @file JSONParser.m
 *  @brief Parses json into useful objects.
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

#import "JSONParser.h"

@implementation JSONParser

-(NSArray*) parseGoogleData:(NSDictionary *) bookData
{
    //getting book title 
    NSString *title = [bookData objectForKey:@"title"];
    if (title==nil || [title isEqualToString:@""]) {
        title = @"N/A";
    }
    
    //Enumerators for multiple properties
    NSEnumerator *count;
    id thing;
    
    //getting book authors
    NSString *author;
    if ([bookData objectForKey:@"authors"]) {
        count = [[bookData objectForKey:@"authors"] objectEnumerator];
        
        thing = [count nextObject];
        author = [NSString stringWithFormat:@"%@",thing];
        while (thing = [count nextObject]) {
            author = [author stringByAppendingFormat:@", %@",thing];
        }
    }
    else {
        author = @"N/A";
    }
    
    //getting book pages
    NSInteger pageCount = (NSInteger)[bookData objectForKey:@"pageCount"];
    if (!pageCount) {
        
    }
    
    //getting ISBN-13
    NSString *isbn = [[[bookData objectForKey:@"industryIdentifiers"] objectAtIndex:1]objectForKey:@"identifier"];
    
    //getting book-cover thumbnail
    NSString *imageURL = [[bookData objectForKey:@"imageLinks"] objectForKey:@"thumbnail"];
    NSLog(@"%@",imageURL);
    if (imageURL==nil || [imageURL isEqualToString:@""]) {
        imageURL = @"N/A";
    }
    
    //getting publish date
    NSUInteger length = 4;
    NSUInteger start = 0;
    NSString *publishedDate = [bookData objectForKey:@"publishedDate"];
    NSRange sub = NSMakeRange(start, length);
    publishedDate = [publishedDate substringWithRange:sub];
    if (!publishedDate || [publishedDate isEqualToString:@""]) {
        publishedDate = @"N/A";
    }
    
    //getting book rating
    id rating = [bookData objectForKey:@"averageRating"];
    NSString *rate;
    if (rating) {
        rate = [NSString stringWithFormat:@"%@",rating];
    }
    else {
        rate = @"N/A";
    }

    //getting book lang
    NSString *lang = [bookData objectForKey:@"language"];
    
    NSString *today = [NSString stringWithFormat:@"%@",[[NSDate date] description]];
    today = [today stringByPaddingToLength:10 withString:today startingAtIndex:0];
    NSNumber *copAvail = [NSNumber numberWithInt:1];
    NSString *owner = [[[NSUserDefaults standardUserDefaults] objectForKey:@"user"] objectForKey:@"username"];
    NSArray *data = [[NSArray alloc] initWithObjects:isbn,title,author,publishedDate,pageCount,copAvail,copAvail,today,rate,imageURL,lang,owner,nil];
    
    return data;
}

-(NSDictionary*)parseLoginInfo:(NSString *)loginInfo
{
    SBJSON *parser = [[SBJSON alloc] init];
    
    NSDictionary *userInfo = (NSDictionary *)[parser objectWithString:loginInfo error:nil];
    [parser release];
    return userInfo;
}

-(NSDictionary *) parseRegisterResponse: (NSString *) registerResponse
{
    SBJSON *parser = [[SBJSON alloc] init];
    
    NSDictionary *parsedResponse = (NSDictionary *) [parser objectWithString:registerResponse error:nil];
    [parser release];
    return parsedResponse;
}

-(NSDictionary *) parseResponse: (NSString*) response
{
    SBJSON *parser = [[SBJSON alloc] init];
    
    NSDictionary *parsedResponse = (NSDictionary *) [parser objectWithString:response error:nil];
    [parser release];
    return parsedResponse;
}

-(NSArray *) parseResponseToArray: (NSString*) response
{
    SBJSON *parser = [[SBJSON alloc] init];
    
    NSArray *parsedResponse = (NSArray *) [parser objectWithString:response error:nil];
    [parser release];
    return parsedResponse;
}

@end
