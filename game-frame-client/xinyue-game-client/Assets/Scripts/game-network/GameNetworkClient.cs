using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Assets.Scripts.game_network
{

    class GameNetworkClient
    {
        private GameSocket gameSocket;
        
        public GameNetworkClient()
        {
            gameSocket = new GameSocket();
        }
        public void ConnectServer(String ip,int port)
        {
            gameSocket.Connect(ip, port);
            gameSocket.ReceiveMessageEvent += ReceiveMessage;
        }

        public void AddConnectStateChanelListener(GameSocketSateChange stateChange)
        {
            if(gameSocket != null)
            {
                gameSocket.GameSocketStateChangeEvent += stateChange;
            }
        }

        public void RemveConnectStateChangeListener(GameSocketSateChange stateChange)
        {
            if(gameSocket != null)
            {
                gameSocket.GameSocketStateChangeEvent -= stateChange;
            }
        }
        
        public void ReceiveMessage(ByteBuf byteBuf)
        {

        }
        
    }
}
