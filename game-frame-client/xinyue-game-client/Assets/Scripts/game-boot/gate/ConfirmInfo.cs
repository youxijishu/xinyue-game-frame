using Assets.Scripts.game_boot.gate;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Assets.Scripts.game_boot
{
    class ConfirmInfo : GateMessage
    {
        public string token;
        public long userId;
        public long roleId;

    }
}
