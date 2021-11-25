package com.axelor.apps.gst.service.accountManagement;

import com.axelor.apps.account.db.Tax;
import com.axelor.apps.account.db.TaxLine;
import com.axelor.apps.account.db.repo.TaxRepository;
import com.axelor.apps.account.service.AccountManagementServiceAccountImpl;
import com.axelor.apps.base.db.Company;
import com.axelor.apps.base.db.Product;
import com.axelor.apps.base.service.app.AppService;
import com.axelor.apps.base.service.tax.FiscalPositionService;
import com.axelor.apps.base.service.tax.TaxService;
import com.axelor.inject.Beans;
import com.google.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

public class AccountManagementGstServiceImpl extends AccountManagementServiceAccountImpl {

  @Inject
  public AccountManagementGstServiceImpl(
      FiscalPositionService fiscalPositionService, TaxService taxService) {
    super(fiscalPositionService, taxService);
  }

  @Override
  protected Tax getProductTax(
      Product product, Company company, boolean isPurchase, int configObject) {

    if (product.getGstRate() == null
        || product.getGstRate() == BigDecimal.ZERO
        || !Beans.get(AppService.class).isApp("gst")) {
      return super.getProductTax(product, company, isPurchase, configObject);
    } else {
      List<Tax> taxList = Beans.get(TaxRepository.class).all().fetch();
      double gst = product.getGstRate().doubleValue();
      Tax tax = null;
      for (Tax tx : taxList) {
        if (tx.getName().equalsIgnoreCase("GST")) {
          List<TaxLine> taxLineList = tx.getTaxLineList();
          for (TaxLine tl : taxLineList) {
            if (gst == tl.getValue().multiply(BigDecimal.valueOf(100)).doubleValue()) tax = tx;
          }
        }
      }
      return tax;
    }
  }
}
