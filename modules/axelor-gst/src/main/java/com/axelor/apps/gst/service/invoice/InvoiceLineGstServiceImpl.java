package com.axelor.apps.gst.service.invoice;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.account.db.repo.InvoiceLineRepository;
import com.axelor.apps.account.service.AccountManagementAccountService;
import com.axelor.apps.account.service.AnalyticMoveLineService;
import com.axelor.apps.account.service.app.AppAccountService;
import com.axelor.apps.base.db.Product;
import com.axelor.apps.base.service.CurrencyService;
import com.axelor.apps.base.service.PriceListService;
import com.axelor.apps.base.service.ProductCompanyService;
import com.axelor.apps.base.service.app.AppBaseService;
import com.axelor.apps.base.service.app.AppServiceImpl;
import com.axelor.apps.businessproject.service.InvoiceLineProjectServiceImpl;
import com.axelor.apps.gst.service.GstInvoiceLineService;
import com.axelor.apps.purchase.service.PurchaseProductService;
import com.axelor.exception.AxelorException;
import com.axelor.inject.Beans;
import com.google.inject.Inject;
import java.util.Map;

public class InvoiceLineGstServiceImpl extends InvoiceLineProjectServiceImpl {

  @Inject
  public InvoiceLineGstServiceImpl(
      CurrencyService currencyService,
      PriceListService priceListService,
      AppAccountService appAccountService,
      AnalyticMoveLineService analyticMoveLineService,
      AccountManagementAccountService accountManagementAccountService,
      PurchaseProductService purchaseProductService,
      ProductCompanyService productCompanyService,
      InvoiceLineRepository invoiceLineRepo,
      AppBaseService appBaseService) {
    super(
        currencyService,
        priceListService,
        appAccountService,
        analyticMoveLineService,
        accountManagementAccountService,
        purchaseProductService,
        productCompanyService,
        invoiceLineRepo,
        appBaseService);
  }

  @Inject private GstInvoiceLineService service;

  @Override
  public Map<String, Object> fillProductInformation(Invoice invoice, InvoiceLine invoiceLine)
      throws AxelorException {
	  if (!Beans.get(AppServiceImpl.class).isApp("gst")) {
	      return super.fillProductInformation(invoice, invoiceLine);
	    }
    Product product = invoiceLine.getProduct();
    InvoiceLine il =service.getGstAmounts(invoice, invoiceLine.getQty().multiply(product.getSalePrice()), product.getGstRate());

    System.out.println(product.getSalePrice().multiply(invoiceLine.getQty()));
    Map<String, Object> productInformation = super.fillProductInformation(invoice, invoiceLine);
    productInformation.put("gstRate", product.getGstRate());
    productInformation.put("hsbn", product.getHsbn());
    productInformation.put("igst", il.getIgst());
    productInformation.put("sgst", il.getSgst());
    productInformation.put("cgst", il.getCgst());
    System.out.println(product);
    return productInformation;
  }
}
