/*   GNU Public Licence
 *   BookActions.h SmartLib API
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
 *  @file BookActions.h
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

#import <Foundation/Foundation.h>
#import "JSONParser.h"
#import "Book.h"

@interface BookActions : NSObject
{
    id delegate;
}

@property (nonatomic, retain) id delegate;

-(NSArray*)getLibraries;

-(NSInteger)saveBook:(NSString*)isbn;
-(NSInteger)stateOfBook:(NSString*)isbn user:(NSString*)username;
-(NSInteger)lentBook:(NSString*)isbn fromUser:(NSString*)owner toUser:(NSString*)borrower;
-(NSInteger)returnBook:(NSString*)isbn toUser:(NSString*)owner;
-(NSMutableArray*)getBooksForUser:(NSString*)user;
-(NSInteger)changeStatusForBook:(NSString*)isbn ofUser:(NSString*)user newStatus:(NSInteger)newStatus;
-(NSInteger)deleteBook:(NSString*)isbn ofUser:(NSString*)user;
-(NSArray*)searchForBooksWithKeywords:(NSString*)keywords forUser:(NSString*)user advancedSearch:(NSString*)extras;
-(NSInteger)requestBorrowingBook:(NSString*)isbn fromUser:(NSString*)owner toUser:(NSString*)borrower;
-(NSMutableArray *) IncomingRequestsforUser:(NSString *)username;
-(NSMutableArray *) OutGoingRequestsforUser:(NSString *)username;
-(NSInteger)deleteRequestForBook:(NSString*)isbn sentFromUser:(NSString*)username toUser:(NSString*)owner;
-(NSMutableArray *) BooksGivenFromUser:(NSString *) username;
-(NSMutableArray *) BooksGivenToUser:(NSString *)username;
-(NSInteger) ReplyUser:(NSString *)owner RequestFrom:(NSString *)destination forBook:(NSString *)isbn withAnswer:(NSInteger)answer;
-(NSArray *)popularBooksForUser:(NSString*)username;


//does not belong to book actions but never mind
-(NSInteger)sentMessage:(NSString*)message fromUser:(NSString*)sender toUser:(NSString*)receiver;

@end
