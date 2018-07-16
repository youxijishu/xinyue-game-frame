using Assets.Scripts.game_network;
using Assets.Scripts.game_network.game_message;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using UnityEngine;
/// <summary>
/// 这个类目前是测试类，当服务器框架测试完成之后，将会重新封装客户端的框架。
/// </summary>
public class GameNetworkClient {

    private GameSocket gameSocket = null;
    private GameMessageCodecFactory gameMessageCodeFactory = GameMessageCodecFactory.Instance();

    public GameNetworkClient()
    {
        gameSocket = new GameSocket();
    }
    public void Init()
    {
        Assembly assembly = Assembly.GetExecutingAssembly();
        Type[] types = assembly.GetTypes();
        List<Type> typeList = new List<Type>();
        foreach(Type type in types)
        {
            System.Object obj = type.GetCustomAttributes(typeof(GameMessageMeta), false).FirstOrDefault();
            if(obj != null)
            {
                GameMessageMeta meta = (GameMessageMeta)obj;
                if(meta.MessageType == EnumMessageType.RESPONSE)
                {
                    typeList.Add(type);
                }
                
            }

        }
        Debug.Log("添加命令数量：" + typeList.Count);
        gameMessageCodeFactory.RegisterGameMessageType(typeList);
    }
    public void OnConnectedToServer(string ip,int port)
    {
        gameSocket.Connect(ip, port);
        gameSocket.ReceiveMessageEvent += ReceiveGameMessage;
    }

    public void CloseSocket()
    {
        this.gameSocket.Close();
    }

    public void SendGameMessage(IGameMessage gameMessage)
    {
      
        byte[] bytes = gameMessageCodeFactory.EncodeGameMessage(gameMessage);
        gameSocket.SendMessage(bytes);
    }

    

    public void ReceiveGameMessage(ByteBuf byteBuf)
    {
        IGameMessage gameMessage = gameMessageCodeFactory.DecodeGameMessage(byteBuf);

        Debug.Log("收到服务器返回：" + gameMessage);
    }



}
