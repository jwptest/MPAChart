package com.finance.model.ben;

import java.util.ArrayList;

/**
 * 产品列表
 */
public class ProductsEntity extends ResponseEntity {

    private ArrayList<ProductEntity> Products;// 产品信息

    public ArrayList<ProductEntity> getProducts() {
        return Products;
    }

    public void setProducts(ArrayList<ProductEntity> products) {
        Products = products;
    }

}
