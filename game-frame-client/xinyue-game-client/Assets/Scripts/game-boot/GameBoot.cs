using Assets.Scripts.game_network.game_message_impl;
using System.Collections;
using System.Collections.Generic;
using System.Threading;
using UnityEngine;

public class GameBoot : MonoBehaviour {
    private GameNetworkClient client;
    private string token;
    private long userId = 100001;
    private long roleId = 20001;
    private string userName = "wgs";
	// Use this for initialization
	void Start () {
        client = new GameNetworkClient();
        client.Init();

        token = userId + "," + roleId + "," + (DateUtil.GetCurrentTimeUnix() + 203 * 1000);
        //StartCoroutine("Sign");

	}
	
	// Update is called once per frame
	void Update () {
		
	}

    public void ConnectConfirm()
    {
        client.CloseSocket();
        client.OnConnectedToServer("192.168.0.192", 8809);
        Thread.Sleep(3000);
        ConnectConfirmRequest request = new ConnectConfirmRequest();
        request.token = this.token;
        request.userId = this.userId;
        request.roleId = this.roleId;
        
        client.SendGameMessage(request);
        
    }

    public void Sign()
    {
        SignRequest request = new SignRequest();
        request.RoleId = 10001;
        
        client.SendGameMessage(request);
       
    }
}
