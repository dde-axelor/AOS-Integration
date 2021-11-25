package com.axelor.apps.gst.service;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import java.math.BigDecimal;

public interface GstInvoiceLineService {

  public InvoiceLine getGstAmounts(Invoice invoice, InvoiceLine invoiceLine);

  public InvoiceLine getGstAmounts(Invoice invoice, BigDecimal netPrice, BigDecimal gstRate);
}
