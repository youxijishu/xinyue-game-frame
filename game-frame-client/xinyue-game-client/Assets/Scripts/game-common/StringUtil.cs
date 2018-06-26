using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Assets.Scripts.NewNetWork.common
{
    class StringUtil
    {
        /**
         * 将字节转化为utf8格式的字符串
         */
        public static string bytesToString(byte[] bytes)
        {
            return System.Text.Encoding.UTF8.GetString(bytes);
        }
        /**
         * 将字符串转为utf8的字节数组
         */
        public static byte[] stringToBytes(string value)
        {
            return System.Text.Encoding.UTF8.GetBytes(value);
        }
    }
}
