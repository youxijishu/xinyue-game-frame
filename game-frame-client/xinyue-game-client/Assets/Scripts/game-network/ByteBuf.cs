using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;

namespace Assets.Scripts.game_network
{
    /// <summary>
    /// 这个类的实现，是参考netty的Bytebuf实现的。
    /// </summary>
   class ByteBuf
    {
        private const int CALCULATE_THRESHOLD = 1048576 * 4;
        private int readerIndex;
        private int writerIndex;
        private int markedReaderIndex;
        private int markedWriterIndex;
        private int capacity;
        private byte[] array;

        public ByteBuf(int capacity)
        {
            this.capacity = capacity;
            this.array = new byte[capacity];
        }

        public int Capacity
        {
            get
            {
                return capacity;
            }
        }

        public ByteBuf Clear()
        {
            readerIndex = writerIndex = 0;
            return this;
        }

        public bool IsReadable()
        {
            return writerIndex > readerIndex;
        }

        public bool IsReadable(int numBytes)
        {
            return writerIndex - readerIndex >= numBytes;
        }

        public bool IsWriteable()
        {
            return this.capacity > writerIndex;
        }

        public bool IsWriteable(int numBytes)
        {
            return this.capacity - writerIndex >= numBytes;
        }

        public int ReadableBytes()
        {
            return writerIndex - readerIndex;
        }

        public int WriteableBytes()
        {
            return this.capacity - writerIndex;
        }

        public ByteBuf ResetReaderIndex()
        {
            this.readerIndex = markedReaderIndex;
            return this;
        }

        public ByteBuf ResetWriterIndex()
        {
            this.writerIndex = this.markedReaderIndex;
            return this;
        }

        public ByteBuf MarkReaderIndex()
        {
            this.markedReaderIndex = this.readerIndex;
            return this;
        }

        public ByteBuf MarkWriteIndex()
        {
            this.markedWriterIndex = this.writerIndex;
            return this;
        }

        public ByteBuf DiscardReadBytes()
        {
            if(readerIndex == 0)
            {
                return this;
            }
            if(readerIndex != writerIndex)
            {
                Array.Copy(this.array,this.readerIndex, this.array, 0, this.writerIndex - this.readerIndex);
                this.writerIndex = this.writerIndex - this.readerIndex;
                this.readerIndex = 0;

            } else
            {
                this.readerIndex = this.writerIndex = 0;
            }
            return this;
        }

        private void EnsureWriteable(int minWriteableBytes)
        {
            if(minWriteableBytes < 0)
            {
                throw new ArgumentException("minWriteableBytes 不能小于0");
            }
            int newCapacity = this.writerIndex + minWriteableBytes;
            if (newCapacity <= WriteableBytes())
            {
                return;
            }
           
            //如果新的buf大小大于4M，则直接增加4M
            if(newCapacity > CALCULATE_THRESHOLD)
            {
                newCapacity = newCapacity / CALCULATE_THRESHOLD * CALCULATE_THRESHOLD;
            } else
            {
                //找到第一个大于等于newCapacity的2的n次方数
                //从64开始
                int temp = 64;
                while(temp < newCapacity)
                {
                    temp <<= 1;
                }
                newCapacity = temp;
            }
            this.NewCapacity(newCapacity);
            
        }

        public ByteBuf NewCapacity(int newCapacity)
        {
            int oldCapacity = this.capacity;
            byte[] oldArray = this.array;
            byte[] newArray = new byte[newCapacity];
            if (newCapacity > oldCapacity)
            {
                this.capacity = newCapacity;
                Array.Copy(oldArray, newArray, oldCapacity);
            } else if(newCapacity < oldCapacity)
            {
                if(readerIndex < newCapacity)
                {
                    //如果readerIndex小于newCapacity，说明还有字节未读取完，如果写的索引大于newCapacity，则最多只能读取到newCapacity了。
                    if(writerIndex> newCapacity)
                    {
                        this.writerIndex = newCapacity;
                    }
                    Array.Copy(oldArray, this.readerIndex, newArray, readerIndex, writerIndex - readerIndex);
                } else
                {
                    //如果读取索引大于等于新的buf容量，则说明无法读取，也无法写了，这样把读和写索引变成newCapacity即可。
                    this.readerIndex = newCapacity;
                    this.writerIndex = newCapacity;
                }
                
            }
            this.array = newArray;
            return this;
        }

        private void CheckReadableBytes0(int minimumReadableBytes)
        {
            if(readerIndex > writerIndex - minimumReadableBytes)
            {
                throw new IndexOutOfRangeException("读取越界，当前可读取字节不足" + minimumReadableBytes);
            }
        }
        private byte GetByte0(int index)
        {
            return array[index];
        }

        public byte ReadByte()
        {
            this.CheckReadableBytes0(1);
            int i = readerIndex;
            byte b = GetByte0(i);
            readerIndex = i + 1;
            return b;
        }

        public bool ReadBool()
        {
            return this.ReadByte() != 0;
        }

        public short ReadShort()
        {
            CheckReadableBytes0(2);
            short v =(short)(array[readerIndex] << 8 | array[readerIndex + 1] & 0xFF);
            readerIndex += 2;
            return v;
        }

        public int ReadInt()
        {
            CheckReadableBytes0(4);
            int v = (array[this.readerIndex] & 0xff) << 24 | (array[this.readerIndex + 1] & 0xff) << 16 |
                (array[this.readerIndex + 2] & 0xff) << 8 | array[this.readerIndex + 3] & 0xff;
            this.readerIndex += 4;
            return v;
        }
        public long ReadLong()
        {
            CheckReadableBytes0(8);
            long v = ((long)array[this.readerIndex] & 0xff) << 56 |
                ((long)array[this.readerIndex + 1] & 0xff) << 48 |
                ((long)array[this.readerIndex + 2] & 0xff) << 40 |
                ((long)array[this.readerIndex + 3] & 0xff) << 32 |
                ((long)array[this.readerIndex + 4] & 0xff) << 24 |
                ((long)array[this.readerIndex + 5] & 0xff) << 16 |
                ((long)array[this.readerIndex + 6] & 0xff) << 8 |
                (long)array[this.readerIndex + 7] & 0xff;
            this.readerIndex += 8;
            return v;
        }

        public ByteBuf ReadBytes(byte[] dst,int dstIndex,int length)
        {
            Array.Copy(array, readerIndex, dst, dstIndex, length);
            this.readerIndex += length;
            return this;
        }

        public ByteBuf ReadBytes(byte[] dst)
        {
            ReadBytes(dst, 0, dst.Length);
            return this;
        }

        public ByteBuf WriteBool(bool value)
        {
            this.WriteByte(value ? 1 : 0);
            return this;
        }
        public ByteBuf WriteByte(int value)
        {
            this.EnsureWriteable(1);
            this.array[this.writerIndex] = (byte)value;
            this.writerIndex++;
            return this;
        }

        public ByteBuf WriteShort(int value)
        {
            EnsureWriteable(2);
            this.array[this.writerIndex] = (byte)(value >> 8);
            this.array[this.writerIndex + 1] = (byte) value;
            this.writerIndex += 2;
            return this;
        }

        public ByteBuf WriteInt(int value)
        {
            EnsureWriteable(4);
            this.array[this.writerIndex] = (byte)(value >> 24 );
            this.array[this.writerIndex + 1] = (byte)(value >> 16);
            this.array[this.writerIndex + 2] = (byte)(value >> 8);
            this.array[this.writerIndex + 3] = (byte)value;
            this.writerIndex += 4;
            return this;
        }

        public ByteBuf WriteLong(long value)
        {
            EnsureWriteable(8);
            this.array[this.writerIndex    ] = (byte)(value >> 56);
            this.array[this.writerIndex + 1] = (byte)(value >> 48);
            this.array[this.writerIndex + 2] = (byte)(value >> 40);
            this.array[this.writerIndex + 3] = (byte)(value >> 32);
            this.array[this.writerIndex + 4] = (byte)(value >> 24);
            this.array[this.writerIndex + 5] = (byte)(value >> 16);
            this.array[this.writerIndex + 6] = (byte)(value >> 8);
            this.array[this.writerIndex + 7] = (byte)value;
            this.writerIndex += 8;
            return this;

        }

        public ByteBuf WriteBytes(byte[] src,int srcIndex,int length)
        {
            EnsureWriteable(length);
            if (this.IsOutBounds(length))
            {
                throw new IndexOutOfRangeException("WriteBytes时，越界了");
            }
            Array.Copy(src,srcIndex,this.array,this.writerIndex,length);
            this.writerIndex += length;
            return this;
        }
        
        public ByteBuf WriteBytes(byte[] src)
        {
            this.WriteBytes(src,0,src.Length);
            IPAddress.HostToNetworkOrder();
            return this;
        }
        private  bool IsOutBounds(int length)
        {
            return (this.writerIndex | length | (this.writerIndex + length) | (this.capacity - (this.writerIndex + length))) < 0;
        }

        public byte[] IntToBytes(int value)
        {
            //把本地字节值转换成网络字节值
            value = IPAddress.HostToNetworkOrder(value);
            byte[] bytes = BitConverter.GetBytes(value);
            return bytes;
        }

        public int BytesToInt(byte[] bytes)
        {

            int value = BitConverter.ToInt32(bytes,0);
            //把网络字节值转化为本地字节值
            value = IPAddress.NetworkToHostOrder(value);
         
            return value;
        }

    }

   
    
}
