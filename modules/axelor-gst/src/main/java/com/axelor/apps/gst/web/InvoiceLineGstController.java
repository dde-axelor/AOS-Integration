package com.axelor.apps.gst.web;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.account.web.InvoiceLineController;
import com.axelor.apps.gst.service.GstInvoiceLineService;
import com.axelor.exception.AxelorException;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.rpc.Context;
import com.google.inject.Inject;

public class InvoiceLineGstController extends InvoiceLineController {

  @Inject private GstInvoiceLineService service;

  @Override
  public void compute(ActionRequest request, ActionResponse response) throws AxelorException {

    super.compute(request, response);

    Context context = request.getContext();

    InvoiceLine invoiceLine = context.asType(InvoiceLine.class);
    Invoice invoice = this.getInvoice(context);

    InvoiceLine il =
        service.getGstAmounts(
            invoice,
            invoiceLine.getQty().multiply(invoiceLine.getPrice()),
            invoiceLine.getProduct().getGstRate());
    response.setValue("igst", il.getIgst());
    response.setValue("sgst", il.getSgst());
    response.setValue("cgst", il.getCgst());
  }
}
