using System;
using System.Collections.Generic;
using System.Linq;
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
            
        }

        public ByteBuf NewCapacity(int newCapacity)
        {
            int oldCapacity = this.capacity;
            byte[] oldArray = this.array;
            if(newCapacity > oldCapacity)
            {
                byte[] newArray = new byte[newCapacity];
                this.capacity = newCapacity;
                Array.Copy(oldArray, newArray, oldCapacity);
                this.array = newArray;
            } else if(newCapacity < oldCapacity)
            {

            }
            return this;
        }
    }
    
}
