package com.lionxxw.seckill.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 暴露秒杀地址DTO
 * Package com.lionxxw.seckill.dto
 * Project seckill
 * Company www.baofoo.com
 * Author wangjian@baofoo.com
 * Created on 2017/2/28 15:55
 * version 1.0.0
 */
@Getter
@Setter
public class Exposer implements Serializable {
    private static final long serialVersionUID = -6410118652537907859L;

    private Boolean exposed;        // 是否开启秒杀
    private String md5;             // 一种加密措施
    private Long seckillId;         // 秒杀商品id
    private Long now;               // 系统当前时间(毫秒)
    private Long start;             // 秒杀开始时间
    private Long end;               // 秒杀结束时间

    public Exposer(Boolean exposed, String md5, Long seckillId) {
        this.exposed = exposed;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    public Exposer(Boolean exposed, Long seckillId, Long now, Long start, Long end) {
        this.exposed = exposed;
        this.seckillId = seckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public Exposer(Boolean exposed, Long seckillId) {
        this.exposed = exposed;
        this.seckillId = seckillId;
    }

    @Override
    public String toString() {
        return "Exposer{" +
                "exposed=" + exposed +
                ", md5='" + md5 + '\'' +
                ", seckillId=" + seckillId +
                ", now=" + now +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
