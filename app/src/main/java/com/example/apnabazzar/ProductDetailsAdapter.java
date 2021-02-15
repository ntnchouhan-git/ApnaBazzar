package com.example.apnabazzar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearSmoothScroller;

import java.util.List;

public class ProductDetailsAdapter extends FragmentPagerAdapter {

    private  int totalTabs;
    private String productDescription;
    private String productOtherDetails;
    private List<ProductSpecificationModel> productSpecificationModelList;

    public ProductDetailsAdapter(FragmentManager fm ,int totalTabs ,String productDescription , String productOtherDetails, List<ProductSpecificationModel> productSpecificationModelList){
        super(fm);
        this.productDescription= productDescription;
        this.productOtherDetails=productOtherDetails;
        this.productSpecificationModelList= productSpecificationModelList;
        this.totalTabs = totalTabs;

    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                ProductDesrciptionFragment productDesrciptionFragment1 = new ProductDesrciptionFragment();
                productDesrciptionFragment1.body=productDescription;
                return  productDesrciptionFragment1;
            case 1:
              ProductSpecificationFragment productSpecificationFragment = new ProductSpecificationFragment();
              productSpecificationFragment.productSpecificationModelList=productSpecificationModelList;
              return  productSpecificationFragment;
            case 2:
                ProductDesrciptionFragment productDesrciptionFragment2 = new ProductDesrciptionFragment();
                productDesrciptionFragment2.body= productOtherDetails;
                return  productDesrciptionFragment2;

            default:
                return  null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
