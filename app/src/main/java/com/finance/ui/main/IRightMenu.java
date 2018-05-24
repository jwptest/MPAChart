package com.finance.ui.main;

import com.finance.interfaces.IViewHandler;
import com.finance.model.ben.IssueEntity;
import com.finance.model.ben.ProductEntity;

/**
 * 右边菜单实现接口
 */
public interface IRightMenu extends IViewHandler {

    void updateProductIssue(ProductEntity productEntity, IssueEntity issueEntity);

}
