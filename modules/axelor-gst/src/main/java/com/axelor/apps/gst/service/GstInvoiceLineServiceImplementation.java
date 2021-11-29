package com.axelor.apps.gst.service;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import java.math.BigDecimal;

public class GstInvoiceLineServiceImplementation implements GstInvoiceLineService {

  @Override
  public InvoiceLine getGstAmounts(Invoice invoice, InvoiceLine invoiceLine) {

    BigDecimal netAmount = invoiceLine.getExTaxTotal();

    BigDecimal igst = netAmount.multiply(invoiceLine.getGstRate().divide(BigDecimal.valueOf(100)));

    BigDecimal gst = igst.divide(BigDecimal.valueOf(2));

    if (invoice.getCompany().getAddress().getState() != null
        && invoice.getAddress() != null) {
      if(invoice.getAddress().getState() != null) {
    	  if (invoice
    	          .getCompany()
    	          .getAddress()
    	          .getState()
    	          .getName()
    	          .equalsIgnoreCase(invoice.getAddress().getState().getName())) {

    	        invoiceLine.setSgst(gst);

    	        invoiceLine.setCgst(gst);
    	      } else invoiceLine.setIgst(igst);
    	  return invoiceLine;
      }
      
    }
    invoiceLine.setSgst(BigDecimal.ZERO);
    invoiceLine.setCgst(BigDecimal.ZERO);
    invoiceLine.setIgst(BigDecimal.ZERO);

    return invoiceLine;
  }

  @Override
  public InvoiceLine getGstAmounts(Invoice invoice, BigDecimal netPrice, BigDecimal gstRate) {

    InvoiceLine invoiceLine = new InvoiceLine();

    BigDecimal igst = netPrice.multiply(gstRate.divide(BigDecimal.valueOf(100)));

    BigDecimal gst = igst.divide(BigDecimal.valueOf(2));

    if (invoice.getCompany().getAddress().getState() != null
        && invoice.getAddress().getState() != null) {
      if (invoice
          .getCompany()
          .getAddress()
          .getState()
          .getName()
          .equalsIgnoreCase(invoice.getAddress().getState().getName())) {

        invoiceLine.setSgst(gst);

        invoiceLine.setCgst(gst);
      } else invoiceLine.setIgst(igst);
      return invoiceLine;
    }
    invoiceLine.setSgst(BigDecimal.ZERO);
    invoiceLine.setCgst(BigDecimal.ZERO);
    invoiceLine.setIgst(BigDecimal.ZERO);

    return invoiceLine;
  }
}
