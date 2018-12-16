package cn.com.taiji.resdis;

import org.apache.commons.lang3.RandomStringUtils;
import redis.clients.jedis.Jedis;



public class mobileMessage {
    public static void main(String[] args) {
        //初始化
        Jedis jedis = new Jedis("localhost");
        System.out.println("redis本地服务连接成功！");
        //判断是否发过消息，如果没有则创建
        if (!jedis.exists("SmsMap")){
            //输出手机号
            String mobile = "17602260391";
            //生成验证码
            String code = RandomStringUtils.randomNumeric(6);
            //记录次数
            String count = "1";
            //存入redis
            jedis.hset("SmsMap","mobile",mobile);
            jedis.hset("SmsMap","code",code);
            jedis.hset("SmsMap","count",count);
            //设置过期时间
            jedis.expire("SmsMap",100);
            System.out.println("您的验证码为："+code+",100秒内有效");
        }else{
           String s =  jedis.hget("SmsMap","count");
            //获取次数，并判断是否超过3次
           int count =  Integer.parseInt(s);
           if(count<3){
               String code =   RandomStringUtils.randomNumeric(6);
               count++;
               jedis.hset("SmsMap","code",code);
               jedis.hset("SmsMap","count",Integer.toString(count));
               System.out.println("您的验证码为："+code+",100秒内有效");
           }else{
               System.out.println("请求次数过多，请100秒后再试");
           }
        }




    }
}
