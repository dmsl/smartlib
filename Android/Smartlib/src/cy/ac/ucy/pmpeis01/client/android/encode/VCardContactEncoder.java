/*
 This file is part of SmartLib Project.

    SmartLib is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    SmartLib is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with SmartLib.  If not, see <http://www.gnu.org/licenses/>.
    
	Author: Paschalis Mpeis

	Affiliation:
	Data Management Systems Laboratory 
	Dept. of Computer Science 
	University of Cyprus 
	P.O. Box 20537 
	1678 Nicosia, CYPRUS 
	Web: http://dmsl.cs.ucy.ac.cy/
	Email: dmsl@cs.ucy.ac.cy
	Tel: +357-22-892755
	Fax: +357-22-892701
	

 */

package cy.ac.ucy.pmpeis01.client.android.encode;

import android.telephony.PhoneNumberUtils;

import java.util.regex.Pattern;

/**
 * Encodes contact information according to the vCard format.
 *
 * @author Sean Owen
 */
final class VCardContactEncoder extends ContactEncoder {

  private static final Pattern RESERVED_VCARD_CHARS = Pattern.compile("([\\\\,;])");
  private static final Pattern NEWLINE = Pattern.compile("\\n");
  private static final Formatter VCARD_FIELD_FORMATTER = new Formatter() {
    @Override
    public String format(String source) {
      return NEWLINE.matcher(RESERVED_VCARD_CHARS.matcher(source).replaceAll("\\\\$1")).replaceAll("");
    }
  };
  private static final char TERMINATOR = '\n';

  @Override
  public String[] encode(Iterable<String> names,
                         String organization,
                         Iterable<String> addresses,
                         Iterable<String> phones,
                         Iterable<String> emails,
                         String url,
                         String note) {
    StringBuilder newContents = new StringBuilder(100);
    StringBuilder newDisplayContents = new StringBuilder(100);
    newContents.append("BEGIN:VCARD").append(TERMINATOR);
    appendUpToUnique(newContents, newDisplayContents, "N", names, 1, null);
    append(newContents, newDisplayContents, "ORG", organization);
    appendUpToUnique(newContents, newDisplayContents, "ADR", addresses, 1, null);
    appendUpToUnique(newContents, newDisplayContents, "TEL", phones, Integer.MAX_VALUE, new Formatter() {
      @Override
      public String format(String source) {
        return PhoneNumberUtils.formatNumber(source);
      }
    });
    appendUpToUnique(newContents, newDisplayContents, "EMAIL", emails, Integer.MAX_VALUE, null);
    append(newContents, newDisplayContents, "URL", url);
    append(newContents, newDisplayContents, "NOTE", note);
    newContents.append("END:VCARD").append(TERMINATOR);
    return new String[] { newContents.toString(), newDisplayContents.toString() };
  }
  
  private static void append(StringBuilder newContents, 
                             StringBuilder newDisplayContents,
                             String prefix, 
                             String value) {
    doAppend(newContents, newDisplayContents, prefix, value, VCARD_FIELD_FORMATTER, TERMINATOR);
  }
  
  private static void appendUpToUnique(StringBuilder newContents, 
                                       StringBuilder newDisplayContents,
                                       String prefix, 
                                       Iterable<String> values, 
                                       int max,
                                       Formatter formatter) {
    doAppendUpToUnique(newContents,
                       newDisplayContents,
                       prefix,
                       values,
                       max,
                       formatter,
                       VCARD_FIELD_FORMATTER,
                       TERMINATOR);
  }

}
