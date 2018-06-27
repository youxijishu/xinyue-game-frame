using Assets.Scripts.game_network.game_message;
using ProtoBuf;
using System.IO;
using com.xinyue.network.message.impl.proto;

//河南心悦网络科技有限公司   王广帅
namespace com.xinyue.network.message.impl
{
	[GameMessageMeta(1002,EnumMessageType.RESPONSE,GAME_SERVER)]
	public class SignResponse : AbstractGameMessage
	{
	
		// true 签到成功
		public bool Result;
		
		public SignResponse()
		{
		
		}
		public SignResponse(bool Result){
		this.Result = Result;		
		}
		
		public override byte[] EncodeBody()
		{
			
			
			SignResponseModel entity = new SignResponseModel();
			
			entity.Result = this.Result;
			 MemoryStream ms = new MemoryStream();
			 use(ms){
             	Serializer.Serialize(ms, entity);
             	return ms.ToArray();
             }
		}
	
		public override void DecodeBody(byte[] bytes)
		{
			
			
			MemoryStream ms = new MemoryStream(bytes);
			using(ms){
				SignResponseModel entity = Serializer.Deserialize<SignResponseModel>(ms);
							this.Result = entity.Result;
			}
		}
		public override int GetCommandId()
		{
			return commandId;
		}
	}
}
