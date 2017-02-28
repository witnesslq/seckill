package com.lionxxw.seckill.exception;

/**
 * 秒杀已关闭异常
 * Package com.lionxxw.seckill.exception
 * Project seckill
 * Company www.baofoo.com
 * Author wangjian@baofoo.com
 * Created on 2017/2/28 16:57
 * version 1.0.0
 */
public class SeckillCloseException extends SeckillException {
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
