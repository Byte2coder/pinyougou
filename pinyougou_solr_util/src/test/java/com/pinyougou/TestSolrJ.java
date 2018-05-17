package com.pinyougou;

import com.pinyougou.pojo.TbItem;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

public class TestSolrJ {

    @Test
    public void testAdd() throws IOException, SolrServerException {
        //创建连接
       /* SolrServer solrServer = new HttpSolrServer("http://192.168.25.133:8080/solr");
        //创建对象
        SolrInputDocument solrInputFields = new SolrInputDocument();
        //向文档中添加域
        solrInputFields.addField("id", "test02");

        solrInputFields.addField("name", "测试sorlj商品2222");
        solrServer.add(solrInputFields);
        solrServer.commit();*/

        SolrServer solrServer = new HttpSolrServer("http://192.168.25.133:8080/solr");
        TbItem tbItem = new TbItem();
        tbItem.setId(100l);
        tbItem.setTitle("field 注解商品");
        solrServer.addBean(tbItem);
        solrServer.commit();



    }

    @Test
    public  void testQuery() throws SolrServerException {
        HttpSolrServer solrServer = new HttpSolrServer("http://192.168.25.133:8080/solr");
        //创建查询对象
        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
        //执行查询
        QueryResponse response = solrServer.query(query);

        //获取结果集
        SolrDocumentList results = response.getResults();
        System.out.println("总数:" + results.getNumFound());

        //循环遍历结果集
        for (SolrDocument result : results) {
            System.out.println("商品名称:::"+result.get("name"));
        }


    }

    @Test
    public void testDelete() throws IOException, SolrServerException {
        HttpSolrServer solrServer = new HttpSolrServer("http://192.168.25.133:8080/solr");
        solrServer.deleteById("test02");
        solrServer.commit();

    }
}
