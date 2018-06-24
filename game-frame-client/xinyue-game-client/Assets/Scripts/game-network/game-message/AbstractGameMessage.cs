using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Assets.Scripts.game_network.game_message
{
    abstract class AbstractGameMessage : IGameMessage
    {
        private GameMessageHeader messageHeader;
        private int messageUniqueId;

        public AbstractGameMessage()
        {
            
           messageHeader =ReadGameMessageMeta();
            int serverType = (int)this.messageHeader.ServerType;
            int messageId = this.messageHeader.MessageId;
            this.messageUniqueId = CreateGameMessageUniqueId(serverType,messageId);
        }
        public static  int CreateGameMessageUniqueId(int serverType,int messageId)
        {
            return (serverType << 16) + messageId;
        }
        public static  GameMessageHeader ReadGameMessageMeta(Type objType)
        {
            GameMessageHeader  messageHeader = new GameMessageHeader();
            GameMessageMeta gameMessageMeta = (GameMessageMeta)objType.GetCustomAttributes(typeof(GameMessageMeta), false).FirstOrDefault();
            messageHeader.MessageId = gameMessageMeta.MessageId;
            messageHeader.ServerType = gameMessageMeta.ServerType;
            return messageHeader;

        }
        public abstract void DecodeBody(byte[] bytes);
        public abstract byte[] EncodeBody();
        public  GameMessageHeader GetMessageHeader()
        {
            return this.messageHeader;
        }
        public  int GetMessageUniqueId()
        {
            return this.messageUniqueId;
        }
    }
}
