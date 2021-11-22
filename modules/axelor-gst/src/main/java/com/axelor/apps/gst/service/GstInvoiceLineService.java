package com.axelor.apps.gst.service;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.gst.db.State;
import java.math.BigDecimal;

public interface GstInvoiceLineService {

  public boolean checkState(State s1, State s2);

  public InvoiceLine getGstAmounts(Invoice invoice, InvoiceLine invoiceLine);

  public InvoiceLine getGstAmounts(Invoice invoice, BigDecimal netPrice, BigDecimal gstRate);
}
