using Assets.Scripts.NewNetWork.common;
using System;
using System.Net;
using System.Net.Sockets;
using UnityEngine;


namespace Assets.Scripts.game_network
{
  public  delegate void GameSocketSateChange(GameSocket gameSocket,GameSocketState nowState);
  public  delegate void ReceiveMessage(ByteBuf buf);

   public class GameSocket
    {
        public event GameSocketSateChange GameSocketStateChangeEvent;
        public event ReceiveMessage ReceiveMessageEvent;
        //本地socket接收网络字节的缓冲区大小，默认是1M
        private ByteBuf localBytesBuffer = new ByteBuf(1024 * 1024);
        //每次从网络中读取的字节数，默认是512k
        private byte[] readBuffer = new byte[1024 * 512];
        private Socket socket;
        private IPEndPoint ipEndPoint = null;
        private string ip;
        private int port;
        private int sendTimeOut = 5000;
        private int connectTimeout = 5000;
        public GameSocket()
        {

        }
       
        public void Connect(string address, int port)
        {
            ip = address;
            this.port = port;
            if (ipEndPoint == null)
            {
                IPAddress ipAddress = IPAddress.Parse(address);
                ipEndPoint = new IPEndPoint(ipAddress, port);

            }
            try
            {
                Debug.Log("开始连接服务器....");
                InvokeConnectState(GameSocketState.CONNENT_BEGIN);
                //重新连接的时候，socket必须重新new才可以。
                socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
                socket.NoDelay = true;
                socket.SendTimeout = sendTimeOut;
                
                socket.BeginConnect(ipEndPoint, (IAsyncResult ar) =>
                {
                    if (socket.Connected)
                    {
                        Debug.Log("服务器连接成功，" + address + " " + port);
                        InvokeConnectState(GameSocketState.CONNECT_SUCCESS);
                        StartReceived();
                    } else
                    {
                        InvokeConnectState(GameSocketState.CONNECT_ERROR);
                    }
                }, null);
              

            }
            catch (Exception e)
            {
                Debug.LogError("服务器连接失败," + e.Message);
                Close();
                InvokeConnectState(GameSocketState.CONNECT_ERROR);
            }
        }
        public void ReConnect()
        {
            lock (this)
            {
                Debug.Log("重新连接");
                if (socket != null && !socket.Connected)
                {
                    socket.Close();
                    socket = null;
                    Connect(ip, port);
                }
                else
                {
                    Debug.Log("重新连接时，连接已成功");
                }
            }


        }
        public bool IsConnected
        {
            get
            {
                if (socket != null)
                    return socket.Connected;
                return false;
            }
        }
        private void InvokeConnectState(GameSocketState state)
        {
            if(this.GameSocketStateChangeEvent != null)
            {
                GameSocketStateChangeEvent.Invoke(this, state);
            }    
        }

        private void StartReceived()
        {
           
                try
                {
                    socket.BeginReceive(readBuffer, 0, readBuffer.Length, SocketFlags.None, (result =>
                    {

                        int readCount = socket.EndReceive(result);
                        if (readCount > 0)
                        {
                            Debug.Log("接收到消息");
                            localBytesBuffer.WriteBytes(readBuffer, 0, readCount);
                            while (true)
                            {
                                if (localBytesBuffer.ReadableBytes() <= 4)
                                {
                                    break;
                                }
                                int readables = localBytesBuffer.ReadableBytes();
                                localBytesBuffer.MarkReaderIndex();
                                int total = localBytesBuffer.ReadInt();

                               
                                if (total <= readables)
                                {
                                    localBytesBuffer.ResetReaderIndex();
                                    //当前接收到的字节数达到一个数据包的字节数量
                                    ByteBuf byteBuf = new ByteBuf(total);
                                    byte[] source = new byte[total];
                                    localBytesBuffer.ReadBytes(source, 0, total);
                                    byteBuf.WriteBytes(source);
                                    //整理一个缓存，再读取过的字节去掉，留下未
                                    if(this.ReceiveMessageEvent != null)
                                    {
                                        this.ReceiveMessageEvent.Invoke(byteBuf);
                                    }
                                    localBytesBuffer.DiscardReadBytes();
                                }
                                else
                                {
                                    //这里做断包粘包处理，如果当前接收到的字节数不足一个包的大小时，先缓存起来，等待下次收到字节时，再计算。
                                    localBytesBuffer.ResetReaderIndex();
                                    break;
                                }
                            }
                        }
                        StartReceived();
                    }), null);
                }
                catch (Exception ex)
                {
                    Debug.LogError(ex);
                    InvokeConnectState(GameSocketState.CONNECT_ERROR);
                }
            
        }

        public void SendMessage(byte[] bytes)
        {
            
            try
            {
                socket.BeginSend(bytes, 0, bytes.Length, SocketFlags.None, (_result) =>
                {
                    if (!_result.IsCompleted)
                    {
                        InvokeConnectState(GameSocketState.CONNECT_ERROR);
                    }
                   

                }, null);

            }
            catch (System.Exception ex)
            {
                Debug.LogError(ex);
                InvokeConnectState(GameSocketState.CONNECT_ERROR);
            }
           
        }

        public void Dispose()
        {
            Close();
            socket = null;
        }

        public void Close()
        {
            if (socket != null && IsConnected)
            {
                try
                {
                    socket.Shutdown(SocketShutdown.Both);
                }
                catch
                {
                    socket.Close();
                }
                finally
                {
                    socket = null;
                }
            }
        }
    }
}
