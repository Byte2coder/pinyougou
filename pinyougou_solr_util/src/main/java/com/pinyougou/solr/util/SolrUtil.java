package com.pinyougou.solr.util;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;

import java.util.List;
import java.util.Map;

public class SolrUtil {


    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private SolrTemplate solrTemplate;

    public  void importItemsIntoIndex(){
        //查询数据
        TbItemExample example=new TbItemExample();
        example.createCriteria().andStatusEqualTo("1");
        List<TbItem> itemList = tbItemMapper.selectByExample(example);
        //将查询出来的数据存入到索引库中

        //取出规格的数据存入到map中
        for (TbItem tbItem : itemList) {
            String spec = tbItem.getSpec();
            Map map = JSON.parseObject(spec, Map.class);
            tbItem.setSpecMap(map);
        }
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
        }

        public  void dele(){
        //查询数据
            SimpleQuery query = new SimpleQuery("*:*");
            solrTemplate.delete(query);
            solrTemplate.commit();
        }

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext*.xml");

        SolrUtil solrUtil = (SolrUtil)context.getBean("solrUtil");
        solrUtil.importItemsIntoIndex();
       // solrUtil.dele();
    }

}
