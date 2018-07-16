using Assets.Scripts.game_network.game_message;
using ProtoBuf;
using System.IO;
using com.xinyue.network.message.impl.proto;

//河南心悦网络科技有限公司   王广帅
namespace Assets.Scripts.game_network.game_message_impl
{
	[GameMessageMeta(1001,EnumMessageType.RESPONSE,EnumServerType.GAME_SERVER)]
	public class ConnectConfirmResponse : AbstractGameMessage
	{
	
		// 是否成功
		public bool result;
		
		public ConnectConfirmResponse()
		{
		
		}
		public ConnectConfirmResponse(bool result){
			this.result = result;		
		}
		
		public override byte[] EncodeBody()
		{
			
			
			ConnectConfirmResponseModel entity = new ConnectConfirmResponseModel();
			
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
				ConnectConfirmResponseModel entity = Serializer.Deserialize<ConnectConfirmResponseModel>(ms);
							this.result = entity.result;
			}
		}
	}
}
