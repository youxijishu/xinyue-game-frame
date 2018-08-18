using Assets.Scripts.game_network.game_message;
using ProtoBuf;
using System.IO;
using com.xinyue.network.message.impl.proto;

//河南心悦网络科技有限公司   王广帅
namespace Assets.Scripts.game_network.game_message_impl
{
	[GameMessageMeta(1001,EnumMessageType.REQUEST,EnumServerType.GATE)]
	public class GateMessageRequest : AbstractGameMessage
	{
	
		// 消息内容
		public string message;
		
		public GateMessageRequest()
		{
		
		}
		public GateMessageRequest(string message){
			this.message = message;		
		}
		
		public override byte[] EncodeBody()
		{
			
			
			GateMessageRequestModel entity = new GateMessageRequestModel();
			
			entity.message = this.message;
			 MemoryStream ms = new MemoryStream();
			 using(ms){
             	Serializer.Serialize(ms, entity);
             	return ms.ToArray();
             }
		}
	
		public override void DecodeBody(byte[] bytes)
		{
			
			
			MemoryStream ms = new MemoryStream(bytes);
			using(ms){
				GateMessageRequestModel entity = Serializer.Deserialize<GateMessageRequestModel>(ms);
							this.message = entity.message;
			}
		}
	}
}
