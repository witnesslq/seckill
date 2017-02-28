package com.lionxxw.seckill.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 秒杀库存
 * Package com.lionxxw.seckill.entity
 * Project seckill
 * Company www.baofoo.com
 * Author wangjian@baofoo.com
 * Created on 2017/2/28 11:24
 * version 1.0.0
 */
@Getter
@Setter
public class Seckill {
    private Long seckillId;
    private String name;
    private Integer num;
    private Date startTime;
    private Date endTime;
    private Date createTime;

    @Override
    public String toString() {
        return "Seckill{" +
                "seckillId=" + seckillId +
                ", name='" + name + '\'' +
                ", num=" + num +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", createTime=" + createTime +
                '}';
    }
}