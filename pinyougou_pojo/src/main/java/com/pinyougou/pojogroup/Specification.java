package com.pinyougou.pojogroup;

import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationOption;

import java.io.Serializable;
import java.util.List;

public class Specification implements Serializable{

    private TbSpecification tbSpecification;
    private List<TbSpecificationOption> specificationOptionlList;

    public Specification() {
    }

    public Specification(TbSpecification tbSpecification, List<TbSpecificationOption> specificationOptionlList) {
        this.tbSpecification = tbSpecification;//规格
        this.specificationOptionlList = specificationOptionlList;//规格选项列表
    }

    public TbSpecification getTbSpecification() {
        return tbSpecification;
    }

    public void setTbSpecification(TbSpecification tbSpecification) {
        this.tbSpecification = tbSpecification;
    }

    public List<TbSpecificationOption> getSpecificationOptionlList() {
        return specificationOptionlList;
    }

    public void setSpecificationOptionlList(List<TbSpecificationOption> specificationOptionlList) {
        this.specificationOptionlList = specificationOptionlList;
    }
}
