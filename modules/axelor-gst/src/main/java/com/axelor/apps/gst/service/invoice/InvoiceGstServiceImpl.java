package com.axelor.apps.gst.service.invoice;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.repo.InvoiceLineRepository;
import com.axelor.apps.account.db.repo.InvoiceRepository;
import com.axelor.apps.account.service.app.AppAccountService;
import com.axelor.apps.account.service.config.AccountConfigService;
import com.axelor.apps.account.service.invoice.InvoiceLineService;
import com.axelor.apps.account.service.invoice.factory.CancelFactory;
import com.axelor.apps.account.service.invoice.factory.ValidateFactory;
import com.axelor.apps.account.service.invoice.factory.VentilateFactory;
import com.axelor.apps.account.service.move.MoveToolService;
import com.axelor.apps.base.service.PartnerService;
import com.axelor.apps.base.service.alarm.AlarmEngineService;
import com.axelor.apps.base.service.app.AppService;
import com.axelor.apps.cash.management.service.InvoiceEstimatedPaymentService;
import com.axelor.apps.cash.management.service.InvoiceServiceManagementImpl;
import com.axelor.apps.gst.service.GstInvoiceService;
import com.axelor.exception.AxelorException;
import com.axelor.inject.Beans;
import com.google.inject.Inject;

public class InvoiceGstServiceImpl extends InvoiceServiceManagementImpl {

  @Inject
  public InvoiceGstServiceImpl(
      ValidateFactory validateFactory,
      VentilateFactory ventilateFactory,
      CancelFactory cancelFactory,
      AlarmEngineService<Invoice> alarmEngineService,
      InvoiceRepository invoiceRepo,
      AppAccountService appAccountService,
      PartnerService partnerService,
      InvoiceLineService invoiceLineService,
      AccountConfigService accountConfigService,
      MoveToolService moveToolService,
      InvoiceLineRepository invoiceLineRepo,
      InvoiceEstimatedPaymentService invoiceEstimatedPaymentService) {
    super(
        validateFactory,
        ventilateFactory,
        cancelFactory,
        alarmEngineService,
        invoiceRepo,
        appAccountService,
        partnerService,
        invoiceLineService,
        accountConfigService,
        moveToolService,
        invoiceLineRepo,
        invoiceEstimatedPaymentService);
  }

  @Inject private GstInvoiceService service;

  @Override
  public Invoice compute(final Invoice invoice) throws AxelorException {
    if (!Beans.get(AppService.class).isApp("gst")) {
      return super.compute(invoice);
    }

    invoice.setInvoiceLineList(service.getInvoiceLineLists(invoice.getInvoiceLineList(), invoice));
    Invoice invoice1 = super.compute(invoice);
    invoice1.setNetCgst(service.getAmounts(invoice.getInvoiceLineList(), "cgst"));
    invoice1.setNetIgst(service.getAmounts(invoice.getInvoiceLineList(), "igst"));
    invoice1.setNetSgst(service.getAmounts(invoice.getInvoiceLineList(), "sgst"));

    return invoice1;
  }
}
