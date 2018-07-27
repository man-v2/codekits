package org.codekit.utils;

import java.io.IOException;
import java.util.List;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
* @ClassName: CacheUtils
* @Description: memcache工具
* @author Administrator
* @date 2018年7月27日 下午5:18:08
*
 */
public class CacheUtils {
	private Logger log = LoggerFactory.getLogger(getClass());    
	private String mPrefix = "";
    private MemcachedClient mClient = null;
    private String mServerHost;

    /**
     * 有效时间，默认1个小时
     */
    private int expire = 1 * 60 * 60;

    public CacheUtils(List<String> mServerList, int expire, String prefix) {

        try {
            mPrefix = prefix;
            if (mServerList != null && mServerList.size() > 0) {
                mServerHost = mServerList.get(0);
            }
            MemcachedClientBuilder builder = new XMemcachedClientBuilder(
                    AddrUtil.getAddresses(mServerHost));
            // 宕机报警
            builder.setFailureMode(true);
            // 使用二进制文件
            builder.setCommandFactory(new BinaryCommandFactory());
            builder.setConnectionPoolSize(2);
            mClient = builder.build();
            mClient.setEnableHeartBeat(false);
        } catch (IOException e) {
            log.error("初始化Memecache连接失败(" + mServerHost + ")", e);
        }
    }

    public CacheUtils(List<String> mServerList, int expire) {
        try {
            if (mServerList != null && mServerList.size() > 0) {
                mServerHost = mServerList.get(0);
            }
            MemcachedClientBuilder builder = new XMemcachedClientBuilder(
                    AddrUtil.getAddresses(mServerHost));
            // 宕机报警
            builder.setFailureMode(true);
            // 使用二进制文件
            builder.setCommandFactory(new BinaryCommandFactory());
            builder.setConnectionPoolSize(2);
            mClient = builder.build();
        } catch (IOException e) {
            log.error("初始化Memecache连接失败(" + mServerHost + ")", e);
        }
    }

    public void addCache(String key, Object value) {
        try {
            mClient.set(key, expire, value);
        } catch (Exception e) {
            log.error("添加缓存失败:key=" + key + " , value=" + value, e);
        }
    }

    public void addCache(String key, int expire, Object value) {
        try {
            mClient.set(key, expire, value);
        } catch (Exception e) {
            log.error("添加缓存失败:key=" + key + " , value=" + value, e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Object> T getCache(String key) {
        try {
            return (T) mClient.get(key);
        } catch (Exception e) {
            log.error("获取缓存数据失败:key=" + key, e);
        }
        return null;
    }

    public Object delCache(String key) {
        try {
            return mClient.delete(key);
        } catch (Exception e) {
            log.error("移除缓存失败:key=" + key, e);
        }
        return null;
    }

    public void addCacheWithPrefix(String key, Object value) {
        addCache(mPrefix + key, value);
    }

    public void addCacheWithPrefix(String key, int expire, Object value) {
        addCache(mPrefix + key, expire, value);
    }

    public <T extends Object> T getCacheWithPrefix(String key) {
        return getCache(mPrefix + key);
    }

    public Object delCacheWithPrefix(String key) {
        return delCache(mPrefix + key);
    }

}
