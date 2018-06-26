using Assets.Scripts.game_network.game_message_impl;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Assets.Scripts.game_network.game_message
{
    class GameMessageHeader
    {
        private short messageId;
        private int seqId;
        private EnumServerType serverType;
        private int errorCode;



        public short MessageId
        {
            get
            {
                return messageId;
            }

            set
            {
                messageId = value;
            }
        }

        public int SeqId
        {
            get
            {
                return seqId;
            }

            set
            {
                seqId = value;
            }
        }

        public EnumServerType ServerType
        {
            get
            {
                return serverType;
            }

            set
            {
                serverType = value;
            }
        }

        public int ErrorCode
        {
            get
            {
                return errorCode;
            }

            set
            {
                errorCode = value;
            }
        }
    }
}
