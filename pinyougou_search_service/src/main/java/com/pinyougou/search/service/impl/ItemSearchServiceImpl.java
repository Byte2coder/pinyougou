package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(timeout = 5000)
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public Map search(Map searchMap) {
        Map map=new HashMap();
        //1.根据高亮查询
        Map map1 = searchHighLightList(searchMap);
        map.putAll(map1);
        //2.根据关键字查询商品分类
        List categoryList = searchCategoryList(searchMap);
        map.put("categoryList",categoryList);

        //获取品牌列表 和规格的列表（判断 如果是被点击了分类 要根据被点击的分类来实现搜索，默认展示第一个）
        if(!"".equals(searchMap.get("category"))){
            //说明有人点击了某一个商品的分类 需要根据这个商品分类来实现查询
            Map map2 = searchBrandListAndSpecListByCategory((String)searchMap.get("category"));
            map.putAll(map2);
        }else {//展示默认的
            if (categoryList != null && categoryList.size() > 0) {
                Map map2 = searchBrandListAndSpecListByCategory((String) categoryList.get(0));
                map.putAll(map2);
            }
        }

      /*  //3.查询品牌和规格列表
        if(categoryList.size()>0){
            map.putAll(searchBrandListAndSpecListByCategory((String) categoryList.get(0)));
        }*/
        return map;
    }

    /**
     * 商品的分类的列表
     * @param searchMap 根据关键字查询
     * @return
     */
    public  List<String> searchCategoryList(Map searchMap){
         List<String> categoryList=new ArrayList<>();
        String keywords = (String) searchMap.get("keywords");

        SimpleQuery query = new SimpleQuery("*:*");
        
        //创建查询条件
        Criteria criteria = new Criteria("item_keywords");
        criteria.is(keywords);
        query.addCriteria(criteria);
        //设置分组查询条件
        GroupOptions groupOptions = new GroupOptions();
        groupOptions.addGroupByField("item_category");
        query.setGroupOptions(groupOptions);
        
        //分组查询
        GroupPage<TbItem> groupPage = solrTemplate.queryForGroupPage(query, TbItem.class);
        GroupResult<TbItem> groupResult = groupPage.getGroupResult("item_category");
        Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
        List<GroupEntry<TbItem>> content = groupEntries.getContent();
        for (GroupEntry<TbItem> tbItemGroupEntry : content) {
            //获取分组的值
            System.out.println("分组的值"+tbItemGroupEntry.getGroupValue());
            categoryList.add(tbItemGroupEntry.getGroupValue());
        }

        return categoryList;
    }

    //创建私有方法，用于返回查询列表的结果（高亮）
    private Map searchHighLightList(Map searchMap){
        Map resultMap=new HashMap();

        //创建查询 对象 主查询条件...
        SimpleHighlightQuery query = new SimpleHighlightQuery();
        //设置高亮域
        HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");
        highlightOptions.setSimplePrefix("<em style='color:red'>");
        highlightOptions.setSimplePostfix("</em>");

        //设置高亮选项
        query.setHighlightOptions(highlightOptions);
        //品牌,规格...过滤  高亮过滤

        //设置过滤条件  商品分类的过滤
        if (searchMap.get("category") != null && !"".equals(searchMap.get("category"))) {
            Criteria fitercriteria = new Criteria("item_category").is(searchMap.get("category"));
            FilterQuery filterquery = new SimpleFilterQuery(fitercriteria);
            query.addFilterQuery(filterquery);//
        }

        //设置品牌的过滤
        if (searchMap.get("brand") != null && !"".equals(searchMap.get("brand"))) {
            Criteria fitercriteria = new Criteria("item_brand").is(searchMap.get("brand"));
            FilterQuery filterquery = new SimpleFilterQuery(fitercriteria);
            query.addFilterQuery(filterquery);//
        }

        //设置规格的过滤条件

        if (searchMap.get("spec") != null) {
            Map<String,String> spec = (Map<String, String>) searchMap.get("spec");

            for (String key : spec.keySet()) {
                String value = spec.get(key);
                Criteria fitercriteria = new Criteria("item_spec_"+key).is(value);//item_spec_网络：3G
                FilterQuery filterquery = new SimpleFilterQuery(fitercriteria);
                query.addFilterQuery(filterquery);//
            }
        }


        //按照关键字查询
        Criteria criteria = new Criteria("item_title").is(searchMap.get("keywords"));
        query.addCriteria(criteria);


        //执行高亮查询
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);


        //循环高亮入口集合
        for (HighlightEntry<TbItem> highlightEntry : page.getHighlighted()) {
            TbItem item = highlightEntry.getEntity();//获原实体类
            List<HighlightEntry.Highlight> highlights = highlightEntry.getHighlights();
            if(highlights!=null && highlights.size()>0 && highlights.get(0)!=null &&  highlights.get(0).getSnipplets()!=null && highlights.get(0).getSnipplets().size()>0) {

                item.setTitle(highlights.get(0).getSnipplets().get(0));
            }

        }

        resultMap.put("rows",page.getContent());

        return resultMap;

    }
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 根据商品的分类的名称查询缓存中的品牌和规格的列表
     * @param categoryName
     * @return
     */
    public Map searchBrandListAndSpecListByCategory(String categoryName){
        Map map  = new HashMap();
        Long  typeTempldateId = (Long) redisTemplate.boundHashOps("itemCat").get(categoryName);
        List<Map> brandList = (List<Map>) redisTemplate.boundHashOps("brandList").get(typeTempldateId);
        List<Map> specList = (List<Map>) redisTemplate.boundHashOps("specList").get(typeTempldateId);
        map.put("brandList",brandList);
        map.put("specList",specList);
        return map;

    }


}
