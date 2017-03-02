package com.lionxxw.seckill.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 秒杀成功商品明细
 * Package com.lionxxw.seckill.entity
 * Project seckill
 * Company www.baofoo.com
 * Author wangjian@baofoo.com
 * Created on 2017/2/28 11:27
 * version 1.0.0
 */
@Getter
@Setter
public class SuccessKilled implements Serializable {
    private static final long serialVersionUID = -3616897200830505380L;
    private Long seckillId;
    private String userPhone;
    private Integer state;
    private Date createTime;
    private Seckill seckill;

    @Override
    public String toString() {
        return "SuccessKilled{" +
                "seckillId=" + seckillId +
                ", userPhone='" + userPhone + '\'' +
                ", state=" + state +
                ", createTime=" + createTime +
                ", seckill=" + seckill +
                '}';
    }
}