package com.pinyougou.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.mapper.TbContentMapper;
import com.pinyougou.pojo.TbContent;
import com.pinyougou.pojo.TbContentExample;
import com.pinyougou.pojo.TbContentExample.Criteria;
import com.pinyougou.util.PinyougouConstants;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * 服务实现层
 * @author Administrator
 *
 * 在调用增删改的方法中要删除有变化后的redis缓存的数据------->方便缓存更新
 *
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;


	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbContent> findAll() {
		return contentMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbContent> page=   (Page<TbContent>) contentMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbContent content) {
		try {
			redisTemplate.boundHashOps(PinyougouConstants.TNCONTENT_REDIS_LUNBO_KEY).delete(content.getCategoryId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		contentMapper.insert(content);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbContent content){


		try {
			//获取原先传过去的对象
			TbContent tbContent = contentMapper.selectByPrimaryKey(content.getId());
			//修改前的categoryId值
			Long categoryId = tbContent.getCategoryId();
			//删除修改前的categoryId
			redisTemplate.boundHashOps(PinyougouConstants.TNCONTENT_REDIS_LUNBO_KEY).delete(categoryId);
			//删除修改后的categoryId
			redisTemplate.boundHashOps(PinyougouConstants.TNCONTENT_REDIS_LUNBO_KEY).delete(content.getCategoryId());
		} catch (Exception e) {
			e.printStackTrace();
		}


		contentMapper.updateByPrimaryKey(content);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbContent findOne(Long id){
		return contentMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			try {
				TbContent tbContent = contentMapper.selectByPrimaryKey(id);
				redisTemplate.boundHashOps(PinyougouConstants.TNCONTENT_REDIS_LUNBO_KEY).delete(tbContent.getCategoryId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			contentMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbContent content, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbContentExample example=new TbContentExample();
		Criteria criteria = example.createCriteria();
		
		if(content!=null){			
						if(content.getTitle()!=null && content.getTitle().length()>0){
				criteria.andTitleLike("%"+content.getTitle()+"%");
			}
			if(content.getUrl()!=null && content.getUrl().length()>0){
				criteria.andUrlLike("%"+content.getUrl()+"%");
			}
			if(content.getPic()!=null && content.getPic().length()>0){
				criteria.andPicLike("%"+content.getPic()+"%");
			}
			if(content.getContent()!=null && content.getContent().length()>0){
				criteria.andContentLike("%"+content.getContent()+"%");
			}
			if(content.getStatus()!=null && content.getStatus().length()>0){
				criteria.andStatusLike("%"+content.getStatus()+"%");
			}
	
		}
		
		Page<TbContent> page= (Page<TbContent>)contentMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Autowired
	private RedisTemplate redisTemplate;
	/**
	 * 根据广告分类ID查询广告列表
	 * @param categoryId
	 * @return
	 */
	@Override
	public List<TbContent> findContentListByCategoryId(Long categoryId) {

		//缓存目的,提高效率,缓存不可以影响正常逻辑
		//先从缓存中取,判断有数据,直接返回
		try {
			System.out.println("from 缓存...");
			List<TbContent> content = (List<TbContent>)redisTemplate.boundHashOps(PinyougouConstants.TNCONTENT_REDIS_LUNBO_KEY).get(categoryId);
			if (content!=null&&content.size()>0){
                return content;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}


		TbContentExample example=new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId).andStatusEqualTo("1");
		example.setOrderByClause("sort_order");//order by sort_order

		List<TbContent> content = contentMapper.selectByExample(example);

		try {
			System.out.println("from 数据库...");
			//第一次加载后将其存入到缓存中
			redisTemplate.boundHashOps(PinyougouConstants.TNCONTENT_REDIS_LUNBO_KEY).put(categoryId,content);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return content;
	}

}
