/*   GNU Public Licence
 *   MyBooksViewController Shows the books user has in his library.
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
 *  @file Library.m
 *  @brief Library struct holds library info.
 *
 *  @author Aphrodite Christou
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

#import "Library.h"

@implementation Library

@synthesize info, attributes;

-(id) initWithEmptyInfo
{
    self = [super init];
    if (self) {
        NSString *empty = @" ";
        NSMutableArray *emptyArray = [NSMutableArray arrayWithCapacity:3];
        NSInteger count = 0;
        for (count = 0; count<3; count++) {
            [emptyArray addObject:empty];
        }
        
        self.info = [[NSMutableDictionary alloc] initWithObjects:emptyArray forKeys:[self attributes]];
    }
    
    return self;
}

-(NSArray*) attributes
{
    if (!attributes) {
        attributes = [[NSArray alloc] initWithObjects:@"id",@"name",@"owner", nil];
        [attributes retain];
        return attributes;
    }
    else {
        return [info allKeys];
    }
}

-(NSString*) description
{
    NSString *desc = [NSString stringWithFormat:@"%@",info];
    return desc;
}

+(NSArray *) copyFromSmartLib:(NSArray*)results
{
    NSMutableArray *newResults = [[NSMutableArray alloc] initWithCapacity:[results count]];
    Library *temp;
    int count;
    for (count=0; count<[results count]; count++) {
        temp = [[Library alloc] init];
        /*NSLog (@"id %@", [[results objectAtIndex:count] valueForKey:@"id"]);
        NSLog (@"name %@", [[results objectAtIndex:count] valueForKey:@"name"]);
        NSLog (@"createdby %@", [[results objectAtIndex:count] valueForKey:@"createdby"]);*/
        temp.info = [[NSMutableDictionary alloc] init];
        [temp.info setObject:[[results objectAtIndex:count] valueForKey:@"id"] forKey:@"id"];
        [temp.info setObject:[[results objectAtIndex:count] valueForKey:@"name"] forKey:@"name"];
        [temp.info setObject:[[results objectAtIndex:count] valueForKey:@"createdby"] forKey:@"createdby"];
        //temp.info = [[results objectAtIndex:count] mutableCopy];
        temp.attributes = [temp.info allKeys];
        [newResults addObject:temp];
        [temp release];
    }
    //    [results release];
   // NSLog(@"newResults %@",newResults);
    return newResults;
}

-(void)dealloc
{
    [super dealloc];
    [info release];
    [attributes release];
    //    [keywords release];
}

@end
