using Assets.Scripts.game_network.game_message;
using ProtoBuf;
using System.IO;
using com.xinyue.network.message.impl.proto;

//河南心悦网络科技有限公司   王广帅
namespace com.xinyue.network.message.impl
{
	[GameMessageMeta(1002,EnumMessageType.REQUEST,GAME_SERVER)]
	public class SignRequest : AbstractGameMessage
	{
	
		// 角色id
		public long RoleId;
		
		public SignRequest()
		{
		
		}
		public SignRequest(long RoleId){
		this.RoleId = RoleId;		
		}
		
		public override byte[] EncodeBody()
		{
			
			
			SignRequestModel entity = new SignRequestModel();
			
			entity.RoleId = this.RoleId;
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
				SignRequestModel entity = Serializer.Deserialize<SignRequestModel>(ms);
							this.RoleId = entity.RoleId;
			}
		}
		public override int GetCommandId()
		{
			return commandId;
		}
	}
}
