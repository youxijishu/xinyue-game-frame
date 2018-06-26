using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Assets.Scripts.game_network.game_message
{
    class GameMessageCodecFactory
    {
        private Dictionary<int, Type> gameMessageTypeMap = new Dictionary<int, Type>();

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

        public IGameMessage DecodeGameMessage(ByteBuf byteBuf)
        {

        }
    }
}
