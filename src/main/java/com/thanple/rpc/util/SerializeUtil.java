package com.thanple.rpc.util;

import java.io.*;

/**
 * @author Liubsyy
 * @date 2023/12/31
 */
public class SerializeUtil {
    public static byte[] serializeObj(Object obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;

        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            out.flush();
            byte[] yourBytes = bos.toByteArray();

            return yourBytes;
            // 在此处使用 yourBytes 字节数组
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // 忽略关闭异常
            }
        }
    }
    public static <T> T unSerialize(byte[] data,Class<T> classz){
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInputStream in = null;

        try {
            in = new ObjectInputStream(bis);
            T obj = (T) in.readObject();
            return obj;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


}
