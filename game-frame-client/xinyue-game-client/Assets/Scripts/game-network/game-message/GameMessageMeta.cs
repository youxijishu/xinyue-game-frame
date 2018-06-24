using Assets.Scripts.game_network.game_message_impl;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Assets.Scripts.game_network.game_message
{
    [AttributeUsage(AttributeTargets.Class)]
    class GameMessageMeta : Attribute
    {
        private EnumMessageType messageType;

        private short messageId;

        private EnumServerType serverType;

        public GameMessageMeta(short messageId,EnumMessageType messageType,EnumServerType serverType)
        {
            this.messageId = messageId;
            this.messageType = messageType;
            this.serverType = serverType;
        }

        public short MessageId
        {
            get
            {
                return messageId;
            }

           
        }

        internal EnumMessageType MessageType
        {
            get
            {
                return messageType;
            }

          
        }

        internal EnumServerType ServerType
        {
            get
            {
                return serverType;
            }

           
        }
    }
}
