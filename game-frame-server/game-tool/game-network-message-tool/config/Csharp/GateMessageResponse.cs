using Assets.Scripts.game_network.game_message;
using ProtoBuf;
using System.IO;
using com.xinyue.network.message.impl.proto;

//河南心悦网络科技有限公司   王广帅
namespace Assets.Scripts.game_network.game_message_impl
{
	[GameMessageMeta(1001,EnumMessageType.RESPONSE,EnumServerType.GATE)]
	public class GateMessageResponse : AbstractGameMessage
	{
	
		// 返回消息
		public string result;
		
		public GateMessageResponse()
		{
		
		}
		public GateMessageResponse(string result){
			this.result = result;		
		}
		
		public override byte[] EncodeBody()
		{
			
			
			GateMessageResponseModel entity = new GateMessageResponseModel();
			
			entity.result = this.result;
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
				GateMessageResponseModel entity = Serializer.Deserialize<GateMessageResponseModel>(ms);
							this.result = entity.result;
			}
		}
	}
}
