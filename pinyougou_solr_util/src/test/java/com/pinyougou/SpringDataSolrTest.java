package com.pinyougou;

import com.pinyougou.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-solr.xml")
public class SpringDataSolrTest {

    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public  void test01(){

       // for (int i=0;i<30 ;i++ ) {
            TbItem tbItem = new TbItem();
            tbItem.setId(1020l);
            tbItem.setTitle("钛灰色");
            solrTemplate.saveBean(tbItem);
      //  }

        solrTemplate.commit();

    }

    @Test
    public void testDelete(){
        solrTemplate.deleteById("100");
        solrTemplate.commit();
    }

    @Test
    public  void testQuery(){
        Query query=new SimpleQuery("*:*");
        Criteria criteria=new Criteria("item_title");
      //  criteria.is("测试");//item_title:"测试"
        criteria.contains("钛灰色");
       // criteria.is("钛灰色");
        query.addCriteria(criteria);

        ScoredPage<TbItem> tbItems = solrTemplate.queryForPage(query, TbItem.class);
        System.out.println(tbItems.getTotalElements() + "总数<<<");

        //tbItem已加注解
        List<TbItem> content = tbItems.getContent();
        for (TbItem tbItem : content) {
            System.out.println(tbItem.getId());
            System.out.println(tbItem.getTitle());

        }
        System.out.println(tbItems.getNumberOfElements() + "当前页显示行数");


    }

    @Test
    public  void testDeleteAll(){
        SimpleQuery query = new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }
}
