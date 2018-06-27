using Assets.Scripts.game_network.game_message;
using ProtoBuf;
using System.IO;
using com.xinyue.network.message.impl.proto;

//河南心悦网络科技有限公司   王广帅
namespace com.xinyue.network.message.impl
{
	[GameMessageMeta(1001,EnumMessageType.RESPONSE,GAME_SERVER)]
	public class ConnectConfirmResponse : AbstractGameMessage
	{
	
		
		public ConnectConfirmResponse()
		{
		
		}
		
		public override byte[] EncodeBody()
		{
			
			return null;
		}
	
		public override void DecodeBody(byte[] bytes)
		{
		}
		public override int GetCommandId()
		{
			return commandId;
		}
	}
}
