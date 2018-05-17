package com.pinyougou.sellergoods.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pinyougou.mapper.TbSpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojo.TbSpecificationOptionExample;
import com.pinyougou.pojogroup.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbSpecificationMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationExample;
import com.pinyougou.pojo.TbSpecificationExample.Criteria;
import com.pinyougou.sellergoods.service.SpecificationService;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private TbSpecificationMapper specificationMapper;

	@Autowired
	private TbSpecificationOptionMapper optionMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSpecification> findAll() {
		return specificationMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbSpecification> page=   (Page<TbSpecification>) specificationMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Specification specification) {
		//1.插入规格表
		TbSpecification tbSpecification = specification.getTbSpecification();
		specificationMapper.insert(tbSpecification);
		//2.获取到插入的规格表中的主键的ID 作为规格选项表的外键
//  mapper文件中做了配置

		//3.插入规格选项表 需要规格Id
		List<TbSpecificationOption> specificationOptionList=specification.getSpecificationOptionlList();
		for(TbSpecificationOption option:specificationOptionList){
			option.setSpecId(tbSpecification.getId());
			optionMapper.insert(option);
		}





	}


	/**
	 * 修改
	 */
	@Override
	public void update(Specification specification){
		//保存修改的規格1.

		specificationMapper.updateByPrimaryKey(specification.getTbSpecification());

		//刪除原有的規格選項2.
		TbSpecificationOptionExample example=new TbSpecificationOptionExample();
		TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(specification.getTbSpecification().getId());
		optionMapper.deleteByExample(example);

		//循環插入現在的規格選項3.
		for (TbSpecificationOption tbSpecificationOption : specification.getSpecificationOptionlList()) {
			tbSpecificationOption.setSpecId(specification.getTbSpecification().getId());
			optionMapper.insert(tbSpecificationOption);
		}
	}
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Specification findOne(Long id){
		TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);
		//构建Specification对象

		TbSpecificationOptionExample example=new TbSpecificationOptionExample();
		TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(tbSpecification.getId());
		List<TbSpecificationOption> specificationOptionList = optionMapper.selectByExample(example);
		Specification specification=new Specification(tbSpecification,specificationOptionList);
		return specification;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			specificationMapper.deleteByPrimaryKey(id);
			//刪除原有的規格選項
			TbSpecificationOptionExample example=new TbSpecificationOptionExample();
			TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
			criteria.andSpecIdEqualTo(id);

			optionMapper.deleteByExample(example);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbSpecificationExample example=new TbSpecificationExample();
		Criteria criteria = example.createCriteria();
		
		if(specification!=null){			
						if(specification.getSpecName()!=null && specification.getSpecName().length()>0){
				criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
			}
	
		}
		
		Page<TbSpecification> page= (Page<TbSpecification>)specificationMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public List<Map> selectOptionList() {
		List<TbSpecification> tbSpecifications = specificationMapper.selectByExample(null);
		List<Map> specificationList =new ArrayList<>();
		for (TbSpecification tbSpecification : tbSpecifications) {
			Map map=new HashMap();
			map.put("id",tbSpecification.getId());
			map.put("text",tbSpecification.getSpecName());
			specificationList.add(map);
		}
		return specificationList;
	}

}
