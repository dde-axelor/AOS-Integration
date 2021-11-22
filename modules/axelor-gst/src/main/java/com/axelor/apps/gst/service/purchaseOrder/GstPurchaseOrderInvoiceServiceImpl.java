package com.axelor.apps.gst.service.purchaseOrder;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.account.db.repo.InvoiceRepository;
import com.axelor.apps.base.service.app.AppService;
import com.axelor.apps.businessproject.service.PurchaseOrderInvoiceProjectServiceImpl;
import com.axelor.apps.gst.service.GstInvoiceLineService;
import com.axelor.apps.gst.service.GstInvoiceService;
import com.axelor.apps.purchase.db.PurchaseOrder;
import com.axelor.apps.purchase.db.PurchaseOrderLine;
import com.axelor.exception.AxelorException;
import com.axelor.inject.Beans;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.util.List;

public class GstPurchaseOrderInvoiceServiceImpl extends PurchaseOrderInvoiceProjectServiceImpl {

  @Inject private GstInvoiceService serviceInvoice;

  @Inject private GstInvoiceLineService serviceInvoiceLine;

  @Override
  @Transactional(rollbackOn = {Exception.class})
  public Invoice generateInvoice(PurchaseOrder purchaseOrder) throws AxelorException {
	  
	  if (!Beans.get(AppService.class).isApp("gst")) {
	      return super.generateInvoice(purchaseOrder);
	    }

    Invoice invoice = super.generateInvoice(purchaseOrder);
    invoice.setNetCgst(serviceInvoice.getAmounts(invoice.getInvoiceLineList(), "cgst"));
    invoice.setNetSgst(serviceInvoice.getAmounts(invoice.getInvoiceLineList(), "sgst"));
    invoice.setNetIgst(serviceInvoice.getAmounts(invoice.getInvoiceLineList(), "igst"));

    invoice = Beans.get(InvoiceRepository.class).save(invoice);
    return invoice;
  }

  @Override
  public List<InvoiceLine> createInvoiceLine(Invoice invoice, PurchaseOrderLine purchaseOrderLine)
      throws AxelorException {
	  
	  if (!Beans.get(AppService.class).isApp("gst")) {
	      return super.createInvoiceLine(invoice, purchaseOrderLine);
	    }
	  
    List<InvoiceLine> invoiceLines = super.createInvoiceLine(invoice, purchaseOrderLine);

    for (InvoiceLine invoiceLine : invoiceLines) {
    	
      InvoiceLine il = serviceInvoiceLine.getGstAmounts(invoice, invoiceLine.getExTaxTotal(),invoiceLine.getProduct().getGstRate());
      if (purchaseOrderLine != null) {
        invoiceLine.setGstRate(purchaseOrderLine.getProduct().getGstRate());
        invoiceLine.setIgst(il.getIgst());
        invoiceLine.setSgst(il.getSgst());
        invoiceLine.setCgst(il.getCgst());

      }
    }
    return invoiceLines;
  }
}
