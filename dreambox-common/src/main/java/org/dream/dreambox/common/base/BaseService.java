package org.dream.dreambox.common.base;

import java.sql.Connection;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.dream.dreambox.common.page.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * Service的基础类
 * @author </a href="mailto:dujq@llyuntong.com.cn">dujq</a>
 * @version $Id: BaseService.java 206 2012-05-18 05:46:10Z djq $ 
 * @since 2.0
 */
public class BaseService {

    /**
     * session
     */
    @Autowired
    private SqlSession         session;

    /**
     * 保存
     */
    public static final String POSTFIX_INSERT             = ".insert";

    /**
     * 根据字段是否为空保存
     */
    public static final String POSTFIX_INSERT_SELECTIVE   = ".insertSelective";

    /**
     * 根据主键更新
     */
    public static final String POSTFIX_UPDATE_PRIAMARYKEY = ".updateByPrimaryKey";

    /**
     * 根据主键更新不为空字段
     */
    public static final String POSTFIX_UPDATE_SELECTIVE   = ".updateByPrimaryKeySelective";

    /**
     * 根据主键删除
     */
    public static final String POSTFIX_DELETE_PRIAMARYKEY = ".deleteByPrimaryKey";

    /**
     * 根据条件删除
     */
    public static final String POSTFIX_DELETE_EXAMPLE     = ".deleteByExample";

    /**
     * 根据主键查询
     */
    public static final String POSTFIX_SELECT_PRIAMARYKEY = ".selectByPrimaryKey";

    /**
     * 根据查询条件查询
     */
    public static final String POSTFIX_SELECT_EXAMPLE     = ".selectByExample";

    /**
     * 根据查询条件统计
     */
    public static final String POSTFIX_COUNT_EXAMPLE      = ".countByExample";


    /**
     * 插入业务对象
     * @param entity
     * @param session
     * @param mapClass
     * @return
     */
    public <T> T insert(T entity, Class<? extends Object> mapClass) {
        if (session.insert(mapClass.getName() + POSTFIX_INSERT, entity) > 0) {
            return entity;
        }
        return null;
    }

    /**
     * 插入不为null的值
     * @param entity
     * @param session
     * @param mapClass
     * @return
     */
    public <T> T insertSelective(T entity, Class<? extends Object> mapClass) {
        if (session.insert(mapClass.getName() + POSTFIX_INSERT_SELECTIVE, entity) > 0) {
            return entity;
        }
        return null;
    }
    
    /**
     * 回滚
     * @param session
     * @param conn
     */
    public void rollback(SqlSession session, Connection conn){
        try {
            if(null != session){
                session.rollback();
            }
            if(null != conn){
                conn.rollback();
            }
        } catch (Exception e) {
            getLogger().error("回滚异常："+e.getMessage(),e);
        }
    }
    /**
     * 关闭
     * @param session
     * @param conn
     */
    public void close(SqlSession session, Connection conn){
        try {
            if(null != session){
                session.close();
            }
            if(null != conn){
                conn.close();
            }
        } catch (Exception e) {
            getLogger().error("数据库关闭异常："+e.getMessage(),e);
        }
    }

    /**
     * 更新业务对象
     * @param entity
     * @param session
     * @param mapClass
     * @return
     */
    public int updateByPrimaryKey(Object entity, Class<? extends Object> mapClass) {
        return session.update(mapClass.getName() + POSTFIX_UPDATE_PRIAMARYKEY, entity);
    }

    /**
     * 更新不为null的字段
     * @param entity
     * @param session
     * @param mapClass
     * @return
     */
    public int updateByPrimaryKeySelective(Object entity, Class<? extends Object> mapClass) {
        return session.update(mapClass.getName() + POSTFIX_UPDATE_SELECTIVE, entity);
    }
    
    /**
     * 
     * 使用自己的更新方法
     * @param entity
     * @param mapClass
     * @param method
     * @return
     */
    public int updateByMethod(Object entity,Class<? extends Object> mapClass,String method){
        return session.update(mapClass.getName() + "." + method, entity);
    }

    /**
     * 根据主键删除对象
     * @param primaryKey
     * @param session
     * @param mapClass
     * @return
     */
    public int deleteByPrimaryKey(Object primaryKey, Class<? extends Object> mapClass) {
        return session.delete(mapClass.getName() + POSTFIX_DELETE_PRIAMARYKEY, primaryKey);
    }

    /**
     * 根据条件删除对象
     * @param example
     * @param session
     * @param mapClass
     * @return
     */
    public int deleteByExample(Object example, Class<? extends Object> mapClass) {
        return session.delete(mapClass.getName() + POSTFIX_DELETE_EXAMPLE, example);
    }


    /**
     * 根据查询条件查询
     * @param example
     * @param mapClass
     * @return
     */
    public <T> List<T> selectByExample(Object example, Class<? extends Object> mapClass) {
        return session.selectList(mapClass.getName() + POSTFIX_SELECT_EXAMPLE, example);
    }

    /**
     * 根据查询条件分页查询
     * @param example
     * @param session
     * @param mapClass
     * @param pageNo
     * @param pageSize
     * @return
     */
    public <T> List<T> selectByExample(Object example, SqlSession session,
                                       Class<? extends Object> mapClass, int pageNo, int pageSize) {
        int offset = (pageNo - 1) * pageSize;
        int limit = pageSize;
        return session.selectList(mapClass.getName() + POSTFIX_SELECT_EXAMPLE, example,
            new RowBounds(offset, limit));
    }


    /**
     * 根据条件统计数据
     * @param example
     * @param mapClass
     * @return
     */
    public int countByExample(Object example, Class<? extends Object> mapClass) {
        return (Integer) session.selectOne(mapClass.getName() + POSTFIX_COUNT_EXAMPLE, example);
    }

    /**
     * 分页查询{@link Page}对象
     * @param mapClass
     * @param example
     * @param pageNo
     * @param pageSize
     * @return {@link Page}
     */
    public <T> Page<T> queryPage(Class<? extends Object> mapClass, Object example, int pageNo,
                           int pageSize) {
        int startIndex = Page.getStartOfPage(pageNo, pageSize);
        Integer totalCount = countByExample(example, mapClass);

        if (totalCount == null)
            return new Page<T>();

        if (totalCount == 0) {
            return  new Page<T>();
        }

        List<T> list;

        if (pageSize > 0 && pageNo > 0) {
            list = selectByExample(example, session, mapClass, pageNo, pageSize);
        } else {
            pageSize = totalCount;
            list = selectByExample(example, mapClass);
        }
        return new Page<T>(startIndex, totalCount, pageSize, list);
    }

    public SqlSession getSession() {
        return session;
    }

    public void setSession(SqlSession session) {
        this.session = session;
    }
    
    public Logger getLogger() {
        return LoggerFactory.getLogger(BaseService.class);
    }
}
