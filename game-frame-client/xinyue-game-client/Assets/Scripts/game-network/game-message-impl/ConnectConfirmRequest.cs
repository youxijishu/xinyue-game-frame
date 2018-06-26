using Assets.Scripts.game_network.game_message;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Assets.Scripts.game_network.game_message_impl
{
    [GameMessageMeta(1001,EnumMessageType.REQUEST,EnumServerType.GAME_SERVER)]
    class ConnectConfirmRequest : AbstractGameMessage
    {


    }
}
