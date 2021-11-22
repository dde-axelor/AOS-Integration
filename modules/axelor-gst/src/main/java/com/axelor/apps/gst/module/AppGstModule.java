package com.axelor.apps.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.apps.account.service.AccountManagementServiceAccountImpl;
import com.axelor.apps.account.web.InvoiceLineController;
import com.axelor.apps.businessproject.service.InvoiceLineProjectServiceImpl;
import com.axelor.apps.businessproject.service.PurchaseOrderInvoiceProjectServiceImpl;
import com.axelor.apps.businessproject.service.SaleOrderInvoiceProjectServiceImpl;
import com.axelor.apps.cash.management.service.InvoiceServiceManagementImpl;
import com.axelor.apps.gst.service.GstInvoiceLineService;
import com.axelor.apps.gst.service.GstInvoiceLineServiceImplementation;
import com.axelor.apps.gst.service.GstInvoiceService;
import com.axelor.apps.gst.service.GstInvoiceServiceImplementation;
import com.axelor.apps.gst.service.accountManagement.AccountManagementGstServiceImpl;
import com.axelor.apps.gst.service.invoice.InvoiceGstServiceImpl;
import com.axelor.apps.gst.service.invoice.InvoiceLineGstServiceImpl;
import com.axelor.apps.gst.service.purchaseOrder.GstPurchaseOrderInvoiceServiceImpl;
import com.axelor.apps.gst.service.saleOrder.GstSaleOrderInvoiceServiceImpl;
import com.axelor.apps.gst.web.InvoiceLineGstController;

public class AppGstModule extends AxelorModule {

  @Override
  public void configure() {

    bind(GstInvoiceService.class).to(GstInvoiceServiceImplementation.class);

    bind(GstInvoiceLineService.class).to(GstInvoiceLineServiceImplementation.class);

    bind(SaleOrderInvoiceProjectServiceImpl.class).to(GstSaleOrderInvoiceServiceImpl.class);

    bind(InvoiceLineProjectServiceImpl.class).to(InvoiceLineGstServiceImpl.class);

    bind(PurchaseOrderInvoiceProjectServiceImpl.class).to(GstPurchaseOrderInvoiceServiceImpl.class);

    bind(InvoiceServiceManagementImpl.class).to(InvoiceGstServiceImpl.class);

    bind(AccountManagementServiceAccountImpl.class).to(AccountManagementGstServiceImpl.class);

    bind(InvoiceLineController.class).to(InvoiceLineGstController.class);
  }
}
