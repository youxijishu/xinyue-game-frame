using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Assets.Scripts.game_network.game_message
{
   interface IGameMessage
    {
        int GetMessageUniqueId();
        GameMessageHeader GetMessageHeader();
        byte[] EncodeBody();

        void DecodeBody(byte[] bytes);
    }
}
