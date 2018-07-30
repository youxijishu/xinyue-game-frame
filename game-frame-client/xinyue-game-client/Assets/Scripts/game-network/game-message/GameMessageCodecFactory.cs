using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using UnityEngine;

namespace Assets.Scripts.game_network.game_message
{
   public class GameMessageCodecFactory
    {
        private static GameMessageCodecFactory _instance = new GameMessageCodecFactory();


        private Dictionary<int, Type> gameMessageTypeMap = new Dictionary<int, Type>();
        private int seqid = 0;

        public static GameMessageCodecFactory Instance()
        {
            return _instance;
        }

        public void RegisterGameMessageType(List<Type> gameMessageTypes)
        {
            if(gameMessageTypes == null)
            {
                return;
            }
            foreach(Type type in gameMessageTypes)
            {
                GameMessageHeader messageHeader = AbstractGameMessage.ReadGameMessageMeta(type);
                int key = AbstractGameMessage.CreateGameMessageUniqueId((int)messageHeader.ServerType, messageHeader.MessageId);
                gameMessageTypeMap.Add(key, type);
            }
        }
        /// <summary>
        /// 根据消息的唯一id，查找这个消息对应的Type，并创建这个消息的对象
        /// </summary>
        /// <param name="messageUniqueId"></param>
        /// <returns></returns>
        private IGameMessage NewGameMessage(int messageUniqueId)
        {
            Type messageType = gameMessageTypeMap[messageUniqueId];
            if(messageType == null)
            {
                Debug.Log("找不到消息" + messageUniqueId + "对应的消息Type，可能是没有注册这个消息Type");
                return null;
            }
            IGameMessage gameMessage = (IGameMessage)Activator.CreateInstance(messageType);

            return gameMessage;
        }
        /// <summary>
        /// 解码从服务器收到的消息(暂时不做加密和解密处理)，协议格式：total(4) + seqId(4) + uniqueMessageId(4) + errorCode(4) +  body
        /// </summary>
        /// <param name="byteBuf"></param>
        /// <returns></returns>
        public IGameMessage DecodeGameMessage(ByteBuf byteBuf)
        {
            int total = byteBuf.ReadInt();
            int seqId = byteBuf.ReadInt();
            int messageUniqueId = byteBuf.ReadInt();
            int errorCode = byteBuf.ReadInt();
          
            byte[] body = null;
            if (errorCode == 0)
            {
              
                int bodyLen = byteBuf.ReadableBytes();
                body = new byte[bodyLen];
                byteBuf.ReadBytes(body);    
            }
            IGameMessage gameMessage = this.NewGameMessage(messageUniqueId);
            if(gameMessage != null)
            {
                gameMessage.DecodeBody(body);
            }
            return gameMessage;
            
        }
        /// <summary>
        /// 编码客户端向服务器发送的请求消息，暂不加密，格式：total(4) + seqId(4）+ messageUniqueId(4) + body
        /// </summary>
        /// <param name="gameMessage"></param>
        public byte[] EncodeGameMessage(IGameMessage gameMessage)
        {
            seqid++;
            gameMessage.GetMessageHeader().SeqId = seqid;
            int total = 12;
            byte[] body = gameMessage.EncodeBody();
            if(body != null)
            {
                total += body.Length;
            }
            ByteBuf byteBuf = new ByteBuf(total);
            byteBuf.WriteInt(total);
            byteBuf.WriteInt(gameMessage.GetMessageHeader().SeqId);
            byteBuf.WriteInt(gameMessage.GetMessageUniqueId());
            if(body != null)
            {
                byteBuf.WriteBytes(body);
            }
            byte[] msg = byteBuf.ToArray();
            return msg;
        }
    }
}
