package com.axelor.apps.gst.service;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import java.math.BigDecimal;
import java.util.List;

public interface GstInvoiceService {

  public BigDecimal getAmounts(List<InvoiceLine> il, String str);
  
  public List<InvoiceLine> getInvoiceLineLists(List<InvoiceLine> ilList, Invoice invoice);
}
