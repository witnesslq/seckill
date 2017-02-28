package com.lionxxw.seckill.exception;

/**
 * 重复秒杀异常
 * Package com.lionxxw.seckill.exception
 * Project seckill
 * Company www.baofoo.com
 * Author wangjian@baofoo.com
 * Created on 2017/2/28 16:55
 * version 1.0.0
 */
public class RepeatKillException extends SeckillException {
    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
