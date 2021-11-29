package com.axelor.apps.gst.service.accountManagement;

import com.axelor.apps.account.db.Tax;
import com.axelor.apps.account.db.TaxLine;
import com.axelor.apps.account.db.repo.TaxLineRepository;
import com.axelor.apps.account.service.AccountManagementServiceAccountImpl;
import com.axelor.apps.base.db.Company;
import com.axelor.apps.base.db.Product;
import com.axelor.apps.base.service.app.AppService;
import com.axelor.apps.base.service.tax.FiscalPositionService;
import com.axelor.apps.base.service.tax.TaxService;
import com.axelor.inject.Beans;
import com.google.inject.Inject;
import java.math.BigDecimal;

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
        || product.getGstRate().doubleValue() == 0
        || !Beans.get(AppService.class).isApp("gst")) {
      return super.getProductTax(product, company, isPurchase, configObject);
    }
    TaxLine taxLine =
        Beans.get(TaxLineRepository.class)
            .all()
            .filter(
                "self.tax.name=? and self.value=?",
                "GST",
                product.getGstRate().divide(BigDecimal.valueOf(100)))
            .fetchOne();
    return taxLine.getTax();
  }
}
