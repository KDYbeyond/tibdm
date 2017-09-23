package com.thit.tibdm.NettyTransmission.util;

/**
 * @author 匡东洋 E-mail:473948143@qq.com
 * @version 创建时间：2017年7月4日 上午11:00:52 类说明
 */
public interface Connection {
    /**
     * 添加连接 添加列车上的一个连接设备
     *
     * @param train 列车
     */

    void add(Train train);

    /**
     * 删除连接 根据连接的ID删除连接
     *
     * @param connectionID 连接设备
     */

    void delete(String connectionID);

    /**
     * 获得连接的数量
     *
     * @return 返回
     */

    int getConnectionNum();

    /**
     * 切换列车连接设备
     *
     * @param train 列车
     */

    void exchangeTrainConnection(Train train);

}
