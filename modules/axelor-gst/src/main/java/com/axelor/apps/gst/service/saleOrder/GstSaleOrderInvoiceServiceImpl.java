package com.axelor.apps.gst.service.saleOrder;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.account.db.repo.InvoiceRepository;
import com.axelor.apps.account.service.invoice.InvoiceService;
import com.axelor.apps.base.service.app.AppBaseService;
import com.axelor.apps.businessproject.service.SaleOrderInvoiceProjectServiceImpl;
import com.axelor.apps.businessproject.service.app.AppBusinessProjectService;
import com.axelor.apps.gst.service.GstInvoiceLineService;
import com.axelor.apps.gst.service.GstInvoiceService;
import com.axelor.apps.sale.db.SaleOrder;
import com.axelor.apps.sale.db.SaleOrderLine;
import com.axelor.apps.sale.db.repo.SaleOrderRepository;
import com.axelor.apps.sale.service.saleorder.SaleOrderLineService;
import com.axelor.apps.sale.service.saleorder.SaleOrderWorkflowServiceImpl;
import com.axelor.apps.stock.db.repo.StockMoveRepository;
import com.axelor.apps.supplychain.service.app.AppSupplychainService;
import com.axelor.exception.AxelorException;
import com.google.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class GstSaleOrderInvoiceServiceImpl extends SaleOrderInvoiceProjectServiceImpl {

  @Inject private GstInvoiceLineService serviceInvoiceLine;

  @Inject private GstInvoiceService serviceInvoice;

  @Inject
  public GstSaleOrderInvoiceServiceImpl(
      AppBaseService appBaseService,
      AppSupplychainService appSupplychainService,
      SaleOrderRepository saleOrderRepo,
      InvoiceRepository invoiceRepo,
      InvoiceService invoiceService,
      AppBusinessProjectService appBusinessProjectService,
      StockMoveRepository stockMoveRepository,
      SaleOrderLineService saleOrderLineService,
      SaleOrderWorkflowServiceImpl saleOrderWorkflowServiceImpl) {
    super(
        appBaseService,
        appSupplychainService,
        saleOrderRepo,
        invoiceRepo,
        invoiceService,
        appBusinessProjectService,
        stockMoveRepository,
        saleOrderLineService,
        saleOrderWorkflowServiceImpl);
  }

  @Override
  public Invoice createInvoice(
      SaleOrder saleOrder,
      List<SaleOrderLine> saleOrderLineList,
      Map<Long, BigDecimal> qtyToInvoiceMap)
      throws AxelorException {

    Invoice invoice = super.createInvoice(saleOrder, saleOrderLineList, qtyToInvoiceMap);

    invoice.setNetCgst(serviceInvoice.getAmounts(invoice.getInvoiceLineList(), "cgst"));
    invoice.setNetSgst(serviceInvoice.getAmounts(invoice.getInvoiceLineList(), "sgst"));
    invoice.setNetIgst(serviceInvoice.getAmounts(invoice.getInvoiceLineList(), "igst"));

    return invoice;
  }

  @Override
  public List<InvoiceLine> createInvoiceLine(
      Invoice invoice, SaleOrderLine saleOrderLine, BigDecimal qtyToInvoice)
      throws AxelorException {
    List<InvoiceLine> invoiceLines = super.createInvoiceLine(invoice, saleOrderLine, qtyToInvoice);
    for (InvoiceLine invoiceLine : invoiceLines) {
      InvoiceLine il = serviceInvoiceLine.getGstAmounts(invoice, invoiceLine);
      if (saleOrderLine != null) {
        invoiceLine.setGstRate(saleOrderLine.getProduct().getGstRate());
        invoiceLine.setIgst(il.getIgst());
        invoiceLine.setSgst(il.getSgst());
        invoiceLine.setCgst(il.getCgst());
      }
    }
    return invoiceLines;
  }
}
