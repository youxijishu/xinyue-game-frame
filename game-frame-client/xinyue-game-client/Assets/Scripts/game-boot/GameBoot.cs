using Assets.Scripts.game_network.game_message_impl;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameBoot : MonoBehaviour {
    private GameNetworkClient client;
	// Use this for initialization
	void Start () {
        client = new GameNetworkClient();
        client.OnConnectedToServer("192.168.0.192", 8809);
        ConnectConfirm();
	}
	
	// Update is called once per frame
	void Update () {
		
	}

    public void ConnectConfirm()
    {
        ConnectConfirmRequest request = new ConnectConfirmRequest();
        client.SendGameMessage(request);
        
    }

    public void Sign()
    {
        SignRequest request = new SignRequest();
        request.RoleId = 10001;
        client.SendGameMessage(request);
    }
}
