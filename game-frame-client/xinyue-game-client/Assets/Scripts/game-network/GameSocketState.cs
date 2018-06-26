using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Assets.Scripts.game_network
{
   public enum GameSocketState
    {
        CONNENT_BEGIN,
        CONNECT_SUCCESS,
        CONNECT_TIMEOUT,
        CONNECT_ERROR,
        SEND_TIMEOUT
    }
}
