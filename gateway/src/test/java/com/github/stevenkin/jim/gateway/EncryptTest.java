package com.github.stevenkin.jim.gateway;

import com.alibaba.fastjson.JSON;
import com.github.stevenkin.jim.gateway.encrypt.EncryptFrame;
import com.github.stevenkin.jim.gateway.service.EncryptKeyService;
import com.github.stevenkin.jim.gateway.utils.AES;
import com.github.stevenkin.jim.gateway.utils.Base64Sign;
import com.github.stevenkin.jim.gateway.utils.KeyPairGenUtil;

public class EncryptTest {
    public static void main(String[] args) throws Exception {
        test();
    }

    public static void test() throws Exception {
        String serverPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjjbCmpdURfGR645l8itoNF2GpggGnsuepFKqq1FyPzuyZXj/1+a7ZRWKST6HUpBvfknZE6NQPvz8PuFAdLdfAqEfvslpR15x5LBWVT7n+6ldTVF3a4EeZoBGqSCkOo74MSjs4fF+nz3z7T4G2U/N9i/MCJ033zGFGLIaI5s+04CUJrxG2QMEr88QQNJ0vL1eXcBcczgxsI0HjxDSIf8lcd2cis4JJSnb+hSc//CH63Vx+i/ZupAoNSdJPB5uJwJJQ68kOlqLO/jQNCn8T1/LkiYeSlaq9mH3tM5MaKkjDimu0dgTvUlIjGT3Q+nchwM5Q6oWVS0ssEKD7N+v2JoCEQIDAQAB";

        String aesKey = AES.genarateRandomKeyWithBase64();

        String data = "{\n" +
                "  \"header\": {\n" +
                "    \"command\": \"123\",\n" +
                "    \"channelId\": \"123\",\n" +
                "    \"sequence\": 0,\n" +
                "    \"flag\": 0,\n" +
                "    \"status\": 0,\n" +
                "    \"sender\": \"aaa\",\n" +
                "    \"receiver\": \"bbb\"\n" +
                "  },\n" +
                "  \"body\": \"aaabbbccc\"\n" +
                "}";
        String s = AES.encryptToBase64(data, aesKey);
        String encrypt = KeyPairGenUtil.encrypt(aesKey, serverPublicKey);
        EncryptFrame encryptFrame = new EncryptFrame();
        encryptFrame.setEncryptData(s);
        encryptFrame.setEncryptAesKey(encrypt);
        String jsonString = JSON.toJSONString(encryptFrame);
        System.out.println(jsonString);

    }
}
