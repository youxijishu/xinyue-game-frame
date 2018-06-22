using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Assets.Scripts.NewNetWork.common
{
    public class ByteBuffer
    {
        //字节缓存区
        private byte[] buf;
        //读取索引
        private int readIndex = 0;
        //写入索引
        private int writeIndex = 0;
        //读取索引标记
        private int markReadIndex = 0;
        //写入索引标记
        private int markWirteIndex = 0;
        //缓存区字节数组的长度
        private int capacity;

        private object locker = new object();


        /**
         * 构造方法
         */
        private ByteBuffer(int capacity)
        {
            buf = new byte[capacity];
            this.capacity = capacity;
        }

        /**
         * 构造方法
         */
        private ByteBuffer(byte[] bytes)
        {
            buf = bytes;
            this.capacity = bytes.Length;
        }

        /**
         * 构建一个capacity长度的字节缓存区ByteBuffer对象
         */
        public static ByteBuffer Allocate(int capacity)
        {
            return new ByteBuffer(capacity);
        }

        /**
         * 构建一个以bytes为字节缓存区的ByteBuffer对象，一般不推荐使用
         */
        public static ByteBuffer Allocate(byte[] bytes)
        {
            return new ByteBuffer(bytes);
        }

        /**
         * 根据length长度，确定大于此leng的最近的2次方数，如length=7，则返回值为8
         */
        private int FixLength(int length)
        {
            int n = 2;
            int b = 2;
            while (b < length)
            {
                b = 2 << n;
                n++;
            }
            return b;
        }

        /**
         * 翻转字节数组，如果本地字节序列为低字节序列，则进行翻转以转换为高字节序列，C#字节默认是以小端处理的
         */
        private byte[] flip(byte[] bytes)
        {
            if (BitConverter.IsLittleEndian)
            {
                Array.Reverse(bytes);
            }
            return bytes;
        }

        /**
         * 确定内部字节缓存数组的大小
         */
        private int FixSizeAndReset(int currLen, int futureLen)
        {
            if (futureLen > currLen)
            {
                //以原大小的2次方数的两倍确定内部字节缓存区大小
                int size = FixLength(currLen) * 2;
                if (futureLen > size)
                {
                    //以将来的大小的2次方的两倍确定内部字节缓存区大小
                    size = FixLength(futureLen) * 2;
                }
                byte[] newbuf = new byte[size];
                Array.Copy(buf, 0, newbuf, 0, currLen);
                buf = newbuf;
                capacity = newbuf.Length;
            }
            return futureLen;
        }

        /**
         * 将bytes字节数组从startIndex开始的length字节写入到此缓存区
         */
        public ByteBuffer WriteBytes(byte[] bytes, int startIndex, int length)
        {
            lock (locker)
            {
                if (length <= 0) return null;
                //写入后的总长度
                int total = length + writeIndex;
                int len = buf.Length;
                FixSizeAndReset(len, total);
                //请写入的字节复制到buf中
                Array.Copy(bytes, startIndex, buf, writeIndex, length);
                writeIndex = total;
            }
            return this;
        }

        /**
         * 将字节数组中从0到length的元素写入缓存区
         */
        public ByteBuffer WriteBytes(byte[] bytes, int length)
        {
            WriteBytes(bytes, 0, length);
            return this;
        }

        /**
         * 将字节数组全部写入缓存区
         */
        public ByteBuffer WriteBytes(byte[] bytes)
        {
            WriteBytes(bytes, bytes.Length);
            return this;
        }

        /**
         * 将一个ByteBuffer的有效字节区写入此缓存区中
         */
        public ByteBuffer Write(ByteBuffer buffer)
        {
            if (buffer == null)
            {
                return this;
            }
            if (buffer.ReadableBytes() <= 0)
            {
                return this; ;
            }
            WriteBytes(buffer.ToArray());
            return this;
        }

        /**
         * 写入一个int16数据
         */
        public ByteBuffer WriteShort(short value)
        {
            WriteBytes(flip(BitConverter.GetBytes(value)));
            return this;
        }

        /**
         * 写入一个uint16数据
         */
        public ByteBuffer WriteUshort(ushort value)
        {
            WriteBytes(flip(BitConverter.GetBytes(value)));
            return this;
        }

        /**
         * 写入一个int32数据
         */
        public ByteBuffer WriteInt(int value)
        {
            WriteBytes(flip(BitConverter.GetBytes(value)));
            return this;
        }

        /**
         * 写入一个uint32数据
         */
        public ByteBuffer WriteUint(uint value)
        {
            WriteBytes(flip(BitConverter.GetBytes(value)));
            return this;
        }

        /**
         * 写入一个int64数据
         */
        public ByteBuffer WriteLong(long value)
        {
            WriteBytes(flip(BitConverter.GetBytes(value)));
            return this;
        }

        /**
         * 写入一个uint64数据
         */
        public ByteBuffer WriteUlong(ulong value)
        {
            WriteBytes(flip(BitConverter.GetBytes(value)));
            return this;
        }

        /**
         * 写入一个float数据
         */
        public ByteBuffer WriteFloat(float value)
        {
            WriteBytes(flip(BitConverter.GetBytes(value)));
            return this;
        }

        /**
         * 写入一个byte数据
         */
        public ByteBuffer WriteByte(byte value)
        {
            lock (locker)
            {
                int afterLen = writeIndex + 1;
                int len = buf.Length;
                FixSizeAndReset(len, afterLen);
                buf[writeIndex] = value;
                writeIndex = afterLen;
            }
            return this;
        }

        /**
         * 写入一个double类型数据
         */
        public ByteBuffer WriteDouble(double value)
        {
            WriteBytes(flip(BitConverter.GetBytes(value)));
            return this;
        }

        /**
         * 读取一个字节
         */
        public byte ReadByte()
        {
            byte b = buf[readIndex];
            readIndex++;
            return b;
        }

        /**
         * 从读取索引位置开始读取len长度的字节数组
         */
        private byte[] ReadBytes(int len)
        {
            byte[] bytes = new byte[len];
            Array.Copy(buf, readIndex, bytes, 0, len);
            if (BitConverter.IsLittleEndian)
            {
                Array.Reverse(bytes);
            }
            readIndex += len;
            return bytes;
        }

        /**
         * 读取一个uint16数据
         */
        public ushort ReadUshort()
        {
            return BitConverter.ToUInt16(ReadBytes(2), 0);
        }

        /**
         * 读取一个int16数据
         */
        public short ReadShort()
        {
            return BitConverter.ToInt16(ReadBytes(2), 0);
        }

        /**
         * 读取一个uint32数据
         */
        public uint ReadUint()
        {
            return BitConverter.ToUInt32(ReadBytes(4), 0);
        }

        /**
         * 读取一个int32数据
         */
        public int ReadInt()
        {
            return BitConverter.ToInt32(ReadBytes(4), 0);
        }

        /**
         * 读取一个uint64数据
         */
        public ulong ReadUlong()
        {
            return BitConverter.ToUInt64(ReadBytes(8), 0);
        }

        /**
         * 读取一个long数据
         */
        public long ReadLong()
        {
            return BitConverter.ToInt64(ReadBytes(8), 0);
        }

        /**
         * 读取一个float数据
         */
        public float ReadFloat()
        {
            return BitConverter.ToSingle(ReadBytes(4), 0);
        }

        /**
         * 读取一个double数据
         */
        public double ReadDouble()
        {
            return BitConverter.ToDouble(ReadBytes(8), 0);
        }

        /**
         * 从读取索引位置开始读取len长度的字节到disbytes目标字节数组中
         * @params disstart 目标字节数组的写入索引
         */
        public void ReadBytes(byte[] disbytes, int disstart, int len)
        {
            Array.Copy(buf, readIndex, disbytes, disstart, len);
            readIndex += len;
        }
        /**
         * 从缓存的读取索引开始读取数据，从disBytes的开始位置写入字节，写入的长主是这个数组的长度
         */
        public void ReadBytes(byte[] disBytes)
        {
            this.ReadBytes(disBytes, 0, disBytes.Length);
        }

        public string ReadString(int len)
        {
            byte[] bytes = new byte[len];
            ReadBytes(bytes);
            return StringUtil.bytesToString(bytes);
        }

        /**
         * 清除已读字节并重建缓存区
         */
        public void DiscardReadBytes()
        {
            if (readIndex <= 0) return;
            //剩余未读取的长度
            int len = buf.Length - readIndex;
            byte[] newbuf = new byte[len];
            Array.Copy(buf, readIndex, newbuf, 0, len);
            buf = newbuf;
            writeIndex -= readIndex;
            markReadIndex -= readIndex;
            if (markReadIndex < 0)
            {
                markReadIndex = readIndex;
            }
            markWirteIndex -= readIndex;
            if (markWirteIndex < 0 || markWirteIndex < readIndex || markWirteIndex < markReadIndex)
            {
                markWirteIndex = writeIndex;
            }
            readIndex = 0;
        }

        /**
         * 清空此对象
         */
        public void Clear()
        {
            buf = new byte[buf.Length];
            readIndex = 0;
            writeIndex = 0;
            markReadIndex = 0;
            markWirteIndex = 0;
        }

        /**
         * 设置开始读取的索引
         */
        public void SetReaderIndex(int index)
        {
            if (index < 0) return;
            readIndex = index;
        }

        /**
         * 标记读取的索引位置
         */
        public void MarkReaderIndex()
        {
            markReadIndex = readIndex;
        }

        /**
         * 标记写入的索引位置
         */
        public void MarkWriterIndex()
        {
            markWirteIndex = writeIndex;
        }

        /**
         * 将读取的索引位置重置为标记的读取索引位置
         */
        public void ResetReaderIndex()
        {
            readIndex = markReadIndex;
        }

        /**
         * 将写入的索引位置重置为标记的写入索引位置
         */
        public void ResetWriterIndex()
        {
            writeIndex = markWirteIndex;
        }

        /**
         * 可读的有效字节数
         */
        public int ReadableBytes()
        {
            return writeIndex - readIndex;
        }

        /**
         * 获取可读的字节数组
         */
        public byte[] ToArray()
        {
            byte[] bytes = new byte[writeIndex];
            Array.Copy(buf, 0, bytes, 0, bytes.Length);
            return bytes;
        }



        /**
         * 获取缓存区大小
         */
        public int GetCapacity()
        {
            return this.capacity;
        }
    }
}
