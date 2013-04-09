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

package cy.ac.ucy.paschalis.client.result;

/**
 * @author Sean Owen
 */
public final class AddressBookParsedResult extends ParsedResult {

  private final String[] names;
  private final String pronunciation;
  private final String[] phoneNumbers;
  private final String[] phoneTypes;
  private final String[] emails;
  private final String[] emailTypes;
  private final String instantMessenger;
  private final String note;
  private final String[] addresses;
  private final String[] addressTypes;
  private final String org;
  private final String birthday;
  private final String title;
  private final String url;

  public AddressBookParsedResult(String[] names,
                                 String pronunciation,
                                 String[] phoneNumbers,
                                 String[] phoneTypes,
                                 String[] emails,
                                 String[] emailTypes,
                                 String instantMessenger,
                                 String note,
                                 String[] addresses,
                                 String[] addressTypes,
                                 String org,
                                 String birthday,
                                 String title,
                                 String url) {
    super(ParsedResultType.ADDRESSBOOK);
    this.names = names;
    this.pronunciation = pronunciation;
    this.phoneNumbers = phoneNumbers;
    this.phoneTypes = phoneTypes;
    this.emails = emails;
    this.emailTypes = emailTypes;
    this.instantMessenger = instantMessenger;
    this.note = note;
    this.addresses = addresses;
    this.addressTypes = addressTypes;
    this.org = org;
    this.birthday = birthday;
    this.title = title;
    this.url = url;
  }

  public String[] getNames() {
    return names;
  }

  /**
   * In Japanese, the name is written in kanji, which can have multiple readings. Therefore a hint
   * is often provided, called furigana, which spells the name phonetically.
   *
   * @return The pronunciation of the getNames() field, often in hiragana or katakana.
   */
  public String getPronunciation() {
    return pronunciation;
  }

  public String[] getPhoneNumbers() {
    return phoneNumbers;
  }

  /**
   * @return optional descriptions of the type of each phone number. It could be like "HOME", but,
   *  there is no guaranteed or standard format.
   */
  public String[] getPhoneTypes() {
    return phoneTypes;
  }

  public String[] getEmails() {
    return emails;
  }

  /**
   * @return optional descriptions of the type of each e-mail. It could be like "WORK", but,
   *  there is no guaranteed or standard format.
   */
  public String[] getEmailTypes() {
    return emailTypes;
  }
  
  public String getInstantMessenger() {
    return instantMessenger;
  }

  public String getNote() {
    return note;
  }

  public String[] getAddresses() {
    return addresses;
  }

  /**
   * @return optional descriptions of the type of each e-mail. It could be like "WORK", but,
   *  there is no guaranteed or standard format.
   */
  public String[] getAddressTypes() {
    return addressTypes;
  }

  public String getTitle() {
    return title;
  }

  public String getOrg() {
    return org;
  }

  public String getURL() {
    return url;
  }

  /**
   * @return birthday formatted as yyyyMMdd (e.g. 19780917)
   */
  public String getBirthday() {
    return birthday;
  }

  @Override
  public String getDisplayResult() {
    StringBuilder result = new StringBuilder(100);
    maybeAppend(names, result);
    maybeAppend(pronunciation, result);
    maybeAppend(title, result);
    maybeAppend(org, result);
    maybeAppend(addresses, result);
    maybeAppend(phoneNumbers, result);
    maybeAppend(emails, result);
    maybeAppend(instantMessenger, result);
    maybeAppend(url, result);
    maybeAppend(birthday, result);
    maybeAppend(note, result);
    return result.toString();
  }

}
