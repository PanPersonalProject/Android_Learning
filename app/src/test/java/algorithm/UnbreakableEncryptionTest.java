package algorithm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Random;

/**
 * 一次性密码本:
 * 一种基于异或(XOR)操作的简单加密算法。
 * 加密和解密过程都依赖于两个密钥之间的异或运算。
 * <p>
 * 加密原理如下:
 * <p>
 * - 给定原始数据 a 和随机生成的密钥 b
 * <p>
 * - 加密过程: c = a ^ b
 * <p>
 * - 解密过程: a = b ^ c
 * <p>
 * <p>
 * 注意: 本实现仅用于教学目的，并不安全。
 */
public class UnbreakableEncryptionTest {

    /**
     * 包含一对用于加密和解密的密钥。
     */
    public static final class KeyPair {
        /**
         * 第一个密钥。
         */
        public final byte[] key1;

        /**
         * 第二个密钥。
         */
        public final byte[] key2;

        /**
         * 构造一个新的密钥对。
         *
         * @param key1 第一个密钥
         * @param key2 第二个密钥
         */
        KeyPair(byte[] key1, byte[] key2) {
            this.key1 = key1;
            this.key2 = key2;
        }
    }

    /**
     * 生成指定长度的随机密钥。
     *
     * @param length 密钥的长度
     * @return 随机生成的密钥
     */
    private static byte[] randomKey(int length) {
        byte[] dummy = new byte[length];
        Random random = new Random();
        random.nextBytes(dummy);
        return dummy;
    }

    /**
     * 对原始数据进行加密。
     *
     * @param original 原始字符串
     * @return 包含加密所需密钥对的 KeyPair 对象
     */
    public KeyPair encrypt(String original) {
        byte[] originalBytes = original.getBytes();
        byte[] dummyKey = randomKey(originalBytes.length);
        byte[] encryptedKey = new byte[originalBytes.length];
        for (int i = 0; i < originalBytes.length; i++) {
            // XOR 每个字节
            encryptedKey[i] = (byte) (originalBytes[i] ^ dummyKey[i]);
        }
        return new KeyPair(dummyKey, encryptedKey);
    }

    /**
     * 使用给定的密钥对进行解密。
     *
     * @param kp 包含解密所需密钥的 KeyPair 对象
     * @return 解密后的字符串
     */
    public String decrypt(KeyPair kp) {
        byte[] decrypted = new byte[kp.key1.length];
        for (int i = 0; i < kp.key1.length; i++) {
            // XOR 每个字节
            decrypted[i] = (byte) (kp.key1[i] ^ kp.key2[i]);
        }
        return new String(decrypted);
    }

    /**
     * 测试加密和解密功能。
     */
    @Test
    public void test() {
        String words = "未加密的原始数据";
        KeyPair kp = encrypt(words);
        String result = decrypt(kp);
        System.out.println(result);
        assertEquals(words, result);
    }
}
