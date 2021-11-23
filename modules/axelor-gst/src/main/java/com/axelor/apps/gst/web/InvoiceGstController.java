package com.axelor.apps.gst.web;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.service.invoice.InvoiceService;
import com.axelor.exception.AxelorException;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class InvoiceGstController {
	
	public void updateGst(ActionRequest request, ActionResponse response)
	{
		Invoice invoice = request.getContext().asType(Invoice.class);
		try {
			response.setValue("invoiceLineList",Beans.get(InvoiceService.class).compute(invoice).getInvoiceLineList());
		} catch (AxelorException e) {
			e.printStackTrace();
		}
	}
}
