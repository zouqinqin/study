package com.zqq.logRecord.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * +----------------------------------------------------------------------
 * | CRMEB [ CRMEB赋能开发者，助力企业发展 ]
 * +----------------------------------------------------------------------
 * | Copyright (c) 2016~2023 https://www.crmeb.com All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed CRMEB并不是自由软件，未经许可不能去掉CRMEB相关版权
 * +----------------------------------------------------------------------
 * | Author: CRMEB Team <admin@crmeb.com>
 * +----------------------------------------------------------------------
 * redis工具类
 */

@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    private static final String LOCK_PREFIX = "lock:";
    private static final long DEFAULT_EXPIRE_TIME = 30000; // 默认锁过期时间，单位毫秒
    // ============================ common ============================

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return boolean
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效 失效时间为负数，说明该主键未设置失效时间（失效时间默认为-1）
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 获取锁
     *
     * @param lockKey   锁的名称
     * @param expire    锁的过期时间，单位毫秒
     * @return          是否成功获取锁
     */
    public boolean acquireLock(String lockKey, long expire) {
        String key = LOCK_PREFIX + lockKey;
        String value = String.valueOf(System.currentTimeMillis() + expire);
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value);
        if (result != null && result) {
            return true; // 成功获取锁
        }

        String currentValue = (String) redisTemplate.opsForValue().get(key);
        if (currentValue != null && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            // 当前锁已过期，尝试重新获取锁
            String oldValue = (String) redisTemplate.opsForValue().getAndSet(key, value);
            return oldValue != null && oldValue.equals(currentValue);
        }

        return false; // 获取锁失败
    }

    /**
     * 释放锁
     *
     * @param lockKey   锁的名称
     */
    public void releaseLock(String lockKey) {
        String key = LOCK_PREFIX + lockKey;
        redisTemplate.delete(key);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false 不存在
     */
    public Boolean exists(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                if (exists(key[0])) {
                    redisTemplate.delete(key[0]);
                }
            } else {
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    //  ============================ String =============================

    /**
     * 读取缓存
     *
     * @param key 键
     * @return T
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return key == null ? null : (T) redisTemplate.opsForValue().get(key);
    }

    /**
     * 写入缓存
     *
     * @param key   键
     * @param value 值
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 写入缓存带有效期(默认时间单位:秒)
     *
     * @param key        key
     * @param value      value
     * @param expireTime 过期时间 time要大于0 如果time小于等于0 将设置无限期
     * @return boolean
     */
    public boolean set(String key, Object value, Long expireTime) {
        try {
            if (expireTime > 0) {
                redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 写入缓存带有效期
     *
     * @param key        key
     * @param value      value
     * @param expireTime 过期时间
     * @param timeUnit   时间格式
     * @return boolean
     */
    public boolean set(String key, Object value, Long expireTime, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(key, value, expireTime, timeUnit);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key 键
     * @return long
     */
    public Long incrAndCreate(String key) {
        if (!exists(key)) {
            set(key, 1);
            return 1L;
        }
        return incr(key, 1L);
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return long
     */
    public Long incrAndCreate(String key, long delta) {
        if (!exists(key)) {
            set(key, delta);
            return delta;
        }
        return incr(key, delta);
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return long
     */
    public Long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return Long
     */
    public Long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    // ================================Map=================================

    /**
     * 哈希获取数据 HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为nul
     * @return 值
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmSet(String key, Map<Object, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 哈希删除
     *
     * @param key     键 不能为null
     * @param hashKey 项 不能为null
     */
    public void hmDelete(String key, Object hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hmDelete(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return 递增后的值
     */
    public long hincr(String key, String item, long by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少几(小于0)
     * @return 递减后的值
     */
    public long hdecr(String key, String item, long by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /**
     * 哈希数量
     *
     * @param key 键
     * @return 键下的数量
     */
    public Long getHashSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    // ===============================list=================================

    /**
     * 列表添加左边添加
     *
     * @param key   键
     * @param value 值
     */
    public boolean lPush(String key, Object value) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 列表添加左边添加
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从右边拿出来一个
     *
     * @param k string key
     * @param t Long 超时秒数
     */
    public Object getRightPop(String k, Long t) {
        return redisTemplate.opsForList().rightPop(k, t, TimeUnit.SECONDS);
    }

    /**
     * 列表获取数量
     *
     * @param key 键
     * @return Long
     */
    public Long getListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return list缓存的内容
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public Long lRemove(String key, long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return key对应的所有值
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public Boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0)
                expire(key, time);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return set缓存的长度
     */
    public Long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public Long setRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }


    /**
     * 有序集合添加    排行榜使用
     *
     * @param key   string key
     * @param value Object value
     * @param score double scoure
     */
    public void zAdd(String key, Object value, double score) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key, value, score);
    }


    /**
     * 有序集合获取    排行榜使用
     *
     * @param key   string key
     * @param score double scoure
     * @return Set<Object>
     */
    public Set<Object> rangeByScore(String key, double score, double score1) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, score, score1);
    }

}
