package com.axelor.apps.gst.service.stockMoves;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.account.db.repo.InvoiceLineRepository;
import com.axelor.apps.account.db.repo.InvoiceRepository;
import com.axelor.apps.base.service.app.AppService;
import com.axelor.apps.businessproject.service.ProjectStockMoveInvoiceServiceImpl;
import com.axelor.apps.gst.service.GstInvoiceLineService;
import com.axelor.apps.gst.service.GstInvoiceService;
import com.axelor.apps.purchase.db.PurchaseOrder;
import com.axelor.apps.purchase.db.repo.PurchaseOrderRepository;
import com.axelor.apps.sale.db.SaleOrder;
import com.axelor.apps.sale.db.repo.SaleOrderRepository;
import com.axelor.apps.stock.db.StockMove;
import com.axelor.apps.stock.db.StockMoveLine;
import com.axelor.apps.stock.db.repo.StockMoveLineRepository;
import com.axelor.apps.supplychain.service.PurchaseOrderInvoiceService;
import com.axelor.apps.supplychain.service.SaleOrderInvoiceService;
import com.axelor.apps.supplychain.service.StockMoveLineServiceSupplychain;
import com.axelor.apps.supplychain.service.app.AppSupplychainService;
import com.axelor.apps.supplychain.service.config.SupplyChainConfigService;
import com.axelor.exception.AxelorException;
import com.axelor.inject.Beans;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class GstStockMovesInvoiceServiceImpl extends ProjectStockMoveInvoiceServiceImpl {

  @Inject private GstInvoiceService serviceInvoice;

  @Inject private GstInvoiceLineService serviceInvoiceLine;

  @Inject
  public GstStockMovesInvoiceServiceImpl(
      SaleOrderInvoiceService saleOrderInvoiceService,
      PurchaseOrderInvoiceService purchaseOrderInvoiceService,
      StockMoveLineServiceSupplychain stockMoveLineServiceSupplychain,
      InvoiceRepository invoiceRepository,
      SaleOrderRepository saleOrderRepo,
      PurchaseOrderRepository purchaseOrderRepo,
      StockMoveLineRepository stockMoveLineRepository,
      InvoiceLineRepository invoiceLineRepository,
      SupplyChainConfigService supplyChainConfigService,
      AppSupplychainService appSupplychainService) {
    super(
        saleOrderInvoiceService,
        purchaseOrderInvoiceService,
        stockMoveLineServiceSupplychain,
        invoiceRepository,
        saleOrderRepo,
        purchaseOrderRepo,
        stockMoveLineRepository,
        invoiceLineRepository,
        supplyChainConfigService,
        appSupplychainService);
  }

  @Override
  @Transactional(rollbackOn = {Exception.class})
  public Invoice createInvoiceFromSaleOrder(
      StockMove stockMove, SaleOrder saleOrder, Map<Long, BigDecimal> qtyToInvoiceMap)
      throws AxelorException {

    Invoice invoice = super.createInvoiceFromSaleOrder(stockMove, saleOrder, qtyToInvoiceMap);

    if (Beans.get(AppService.class).isApp("gst")) {
      invoice.setNetCgst(serviceInvoice.getAmounts(invoice.getInvoiceLineList(), "cgst"));
      invoice.setNetSgst(serviceInvoice.getAmounts(invoice.getInvoiceLineList(), "sgst"));
      invoice.setNetIgst(serviceInvoice.getAmounts(invoice.getInvoiceLineList(), "igst"));
      invoice = Beans.get(InvoiceRepository.class).save(invoice);
    }

    return invoice;
  }

  @Override
  @Transactional(rollbackOn = {Exception.class})
  public Invoice createInvoiceFromPurchaseOrder(
      StockMove stockMove, PurchaseOrder purchaseOrder, Map<Long, BigDecimal> qtyToInvoiceMap)
      throws AxelorException {

    Invoice invoice =
        super.createInvoiceFromPurchaseOrder(stockMove, purchaseOrder, qtyToInvoiceMap);

    if (Beans.get(AppService.class).isApp("gst")) {
      invoice.setNetCgst(serviceInvoice.getAmounts(invoice.getInvoiceLineList(), "cgst"));
      invoice.setNetSgst(serviceInvoice.getAmounts(invoice.getInvoiceLineList(), "sgst"));
      invoice.setNetIgst(serviceInvoice.getAmounts(invoice.getInvoiceLineList(), "igst"));
      invoice = Beans.get(InvoiceRepository.class).save(invoice);
    }

    return invoice;
  }

  @Override
  @Transactional(rollbackOn = {Exception.class})
  public Invoice createInvoiceFromOrderlessStockMove(
      StockMove stockMove, Map<Long, BigDecimal> qtyToInvoiceMap) throws AxelorException {

    Invoice invoice = super.createInvoiceFromOrderlessStockMove(stockMove, qtyToInvoiceMap);

    if (Beans.get(AppService.class).isApp("gst")) {
      invoice.setNetCgst(serviceInvoice.getAmounts(invoice.getInvoiceLineList(), "cgst"));
      invoice.setNetSgst(serviceInvoice.getAmounts(invoice.getInvoiceLineList(), "sgst"));
      invoice.setNetIgst(serviceInvoice.getAmounts(invoice.getInvoiceLineList(), "igst"));
      invoice = Beans.get(InvoiceRepository.class).save(invoice);
    }
    return invoice;
  }

  @Override
  public List<InvoiceLine> createInvoiceLines(
      Invoice invoice,
      StockMove stockMove,
      List<StockMoveLine> stockMoveLineList,
      Map<Long, BigDecimal> qtyToInvoiceMap)
      throws AxelorException {

    List<InvoiceLine> invoiceLineList =
        super.createInvoiceLines(invoice, stockMove, stockMoveLineList, qtyToInvoiceMap);
    if (Beans.get(AppService.class).isApp("gst")) {
      for (InvoiceLine invoiceLine : invoiceLineList) {

        InvoiceLine il =
            serviceInvoiceLine.getGstAmounts(
                invoice, invoiceLine.getExTaxTotal(), invoiceLine.getProduct().getGstRate());
        invoiceLine.setGstRate(invoiceLine.getProduct().getGstRate());
        invoiceLine.setIgst(il.getIgst());
        invoiceLine.setSgst(il.getSgst());
        invoiceLine.setCgst(il.getCgst());
      }
    }
    return invoiceLineList;
  }
}
