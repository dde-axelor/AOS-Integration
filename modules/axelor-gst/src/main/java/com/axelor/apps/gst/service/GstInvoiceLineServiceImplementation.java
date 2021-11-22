package com.axelor.apps.gst.service;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.gst.db.State;
import java.math.BigDecimal;

public class GstInvoiceLineServiceImplementation implements GstInvoiceLineService {

  @Override
  public boolean checkState(State s1, State s2) {

    if (s1.getName().equalsIgnoreCase(s2.getName())) return true;

    return false;
  }

  @Override
  public InvoiceLine getGstAmounts(Invoice invoice, InvoiceLine invoiceLine) {

    BigDecimal netAmount = invoiceLine.getExTaxTotal();

    BigDecimal igst = netAmount.multiply(invoiceLine.getGstRate().divide(BigDecimal.valueOf(100)));

    BigDecimal gst = igst.divide(BigDecimal.valueOf(2));

    if (invoice.getCompany().getAddress().getState() != null
        && invoice.getAddress().getState() != null) {
      if (checkState(
          invoice.getCompany().getAddress().getState(), invoice.getAddress().getState())) {

        invoiceLine.setSgst(gst);

        invoiceLine.setCgst(gst);
      } else invoiceLine.setIgst(igst);
    } else {
      invoiceLine.setSgst(BigDecimal.ZERO);
      invoiceLine.setCgst(BigDecimal.ZERO);
      invoiceLine.setIgst(BigDecimal.ZERO);
    }

    return invoiceLine;
  }

  @Override
  public InvoiceLine getGstAmounts(Invoice invoice, BigDecimal netPrice, BigDecimal gstRate) {

    InvoiceLine invoiceLine = new InvoiceLine();

    BigDecimal igst = netPrice.multiply(gstRate.divide(BigDecimal.valueOf(100)));

    BigDecimal gst = igst.divide(BigDecimal.valueOf(2));

    if (invoice.getCompany().getAddress().getState() != null
        && invoice.getAddress().getState() != null) {
      if (checkState(
          invoice.getCompany().getAddress().getState(), invoice.getAddress().getState())) {

        invoiceLine.setSgst(gst);

        invoiceLine.setCgst(gst);
      } else invoiceLine.setIgst(igst);
    } else {
      invoiceLine.setSgst(BigDecimal.ZERO);
      invoiceLine.setCgst(BigDecimal.ZERO);
      invoiceLine.setIgst(BigDecimal.ZERO);
    }

    return invoiceLine;
  }
}
