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

package cy.ac.ucy.paschalis.client.android.result.supplement;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.widget.TextView;
import cy.ac.ucy.paschalis.client.android.history.HistoryManager;
import cy.ac.ucy.paschalis.client.result.ISBNParsedResult;
import cy.ac.ucy.paschalis.client.result.ParsedResult;
import cy.ac.ucy.paschalis.client.result.ProductParsedResult;
import cy.ac.ucy.paschalis.client.result.URIParsedResult;

public abstract class SupplementalInfoRetriever implements Callable<Void> {

  private static ExecutorService executorInstance = null;

  private static synchronized ExecutorService getExecutorService() {
    if (executorInstance == null) {
      executorInstance = Executors.newCachedThreadPool(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
          Thread t = new Thread(r);
          t.setDaemon(true);
          return t;
        }
      });
    }
    return executorInstance;
  }

  public static void maybeInvokeRetrieval(TextView textView,
                                          ParsedResult result,
                                          Handler handler,
                                          HistoryManager historyManager,
                                          Context context) {

    Collection<SupplementalInfoRetriever> retrievers = new ArrayList<SupplementalInfoRetriever>(1);

    if (result instanceof URIParsedResult) {
      retrievers.add(new URIResultInfoRetriever(textView, (URIParsedResult) result, handler, historyManager, context));
    } else if (result instanceof ProductParsedResult) {
      String productID = ((ProductParsedResult) result).getProductID();
      retrievers.add(new ProductResultInfoRetriever(textView, productID, handler, historyManager, context));
    } else if (result instanceof ISBNParsedResult) {
      String isbn = ((ISBNParsedResult) result).getISBN();
      retrievers.add(new ProductResultInfoRetriever(textView, isbn, handler, historyManager, context));
      retrievers.add(new BookResultInfoRetriever(textView, isbn, handler, historyManager, context));
    }

    for (SupplementalInfoRetriever retriever : retrievers) {
      ExecutorService executor = getExecutorService();
      Future<?> future = executor.submit(retriever);
      // Make sure it's interrupted after a short time though
      executor.submit(new KillerCallable(future, 10, TimeUnit.SECONDS));
    }
  }

  private final WeakReference<TextView> textViewRef;
  private final Handler handler;
  private final HistoryManager historyManager;

  SupplementalInfoRetriever(TextView textView, Handler handler, HistoryManager historyManager) {
    this.textViewRef = new WeakReference<TextView>(textView);
    this.handler = handler;
    this.historyManager = historyManager;
  }

  @Override
  public final Void call() throws IOException, InterruptedException {
    retrieveSupplementalInfo();
    return null;
  }

  abstract void retrieveSupplementalInfo() throws IOException, InterruptedException;

  final void append(String itemID, String source, String[] newTexts, String linkURL) throws InterruptedException {

    final TextView textView = textViewRef.get();
    if (textView == null) {
      throw new InterruptedException();
    }

    StringBuilder newTextCombined = new StringBuilder();

    if (source != null) {
      newTextCombined.append(source).append(" : ");
    }

    int linkStart = newTextCombined.length();

    boolean first = true;
    for (String newText : newTexts) {
      if (first) {
        newTextCombined.append(newText);
        first = false;
      } else {
        newTextCombined.append(" [");
        newTextCombined.append(newText);
        newTextCombined.append(']');
      }
    }

    int linkEnd = newTextCombined.length();

    String newText = newTextCombined.toString();
    final Spannable content = new SpannableString(newText + "\n\n");
    if (linkURL != null) {
      content.setSpan(new URLSpan(linkURL), linkStart, linkEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    handler.post(new Runnable() {
      @Override
      public void run() {
        textView.append(content);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
      }
    });

    // Add the text to the history.
    historyManager.addHistoryItemDetails(itemID, newText);
  }

}
