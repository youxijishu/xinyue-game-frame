using Assets.Scripts.game_network.game_message;
using ProtoBuf;
using System.IO;
using com.xinyue.network.message.impl.proto;

//河南心悦网络科技有限公司   王广帅
namespace Assets.Scripts.game_network.game_message_impl
{
	[GameMessageMeta(1001,EnumMessageType.REQUEST,EnumServerType.GAME_SERVER)]
	public class ConnectConfirmRequest : AbstractGameMessage
	{
	
		// token
		public string token;
		// userId
		public long userId;
		// 角色id
		public long roleId;
		
		public ConnectConfirmRequest()
		{
		
		}
		public ConnectConfirmRequest(string token, long userId, long roleId){
			this.token = token;		
			this.userId = userId;		
			this.roleId = roleId;		
		}
		
		public override byte[] EncodeBody()
		{
			
			
			ConnectConfirmRequestModel entity = new ConnectConfirmRequestModel();
			
			entity.token = this.token;
			entity.userId = this.userId;
			entity.roleId = this.roleId;
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
				ConnectConfirmRequestModel entity = Serializer.Deserialize<ConnectConfirmRequestModel>(ms);
							this.token = entity.token;
							this.userId = entity.userId;
							this.roleId = entity.roleId;
			}
		}
	}
}
