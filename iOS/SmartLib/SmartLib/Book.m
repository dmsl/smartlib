/*   GNU Public Licence
 *   Book.m Book struct holds book info
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
 *  @file Book.m
 *  @brief Book struct holds book info
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
 *  @bug Potential leak in method (+copyFromSmartLib).
 */

#import "Book.h"

@implementation Book

@synthesize info, attributes;

-(id) initWithEmptyInfo
{
    self = [super init];
    if (self) {
        NSString *empty = @" ";
        NSMutableArray *emptyArray = [NSMutableArray arrayWithCapacity:12];
        NSInteger count = 0;
        for (count = 0; count<12; count++) {
            [emptyArray addObject:empty];
        }
        
        self.info = [[NSMutableDictionary alloc] initWithObjects:emptyArray forKeys:[self attributes]];
    }
    
    return self;
}

-(NSArray*) attributes
{
    if (!attributes) {
        attributes = [[NSArray alloc] initWithObjects:@"isbn",@"title",@"authors",@"publishedYear",@"pageCount",@"bookCopies",@"bookAvail",@"dateOfInsert",@"status",@"imgURL",@"lang",@"username", nil];
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
    Book *temp;
    int count;
    for (count=0; count<[results count]; count++) {
        temp = [[Book alloc] init];
        temp.info = [[results objectAtIndex:count] mutableCopy];
        temp.attributes = [temp.info allKeys];
        [newResults addObject:temp];
        [temp release];
    }
//    [results release];
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
