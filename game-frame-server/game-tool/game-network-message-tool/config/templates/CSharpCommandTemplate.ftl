using Assets.Scripts.game_network.game_message;
using ProtoBuf;
using System.IO;
<#list imports as ns>
using ${ns};
</#list>
using ${namespace}.proto;

//河南心悦网络科技有限公司   王广帅
namespace Assets.Scripts.game_network.game_message_impl
{
	[GameMessageMeta(${id?c},${messageType},EnumServerType.${serverType})]
	public class ${className} : AbstractGameMessage
	{
	
		<#list fields as field>
		// ${field.desc}
		<#if field.arrayType == 1>
		public List<${field.csharpType}> ${field.name};
		<#else>
		public ${field.csharpType} ${field.name};
		</#if>
		</#list>
		
		public ${className}()
		{
		
		}
		<#if (fields?size > 0)>
		public ${className}(<#list fields as field><#if field.arrayType == 1>List<${field.csharpType}> ${field.name}<#else>${field.csharpType} ${field.name}</#if><#sep>, </#list>){
		<#list fields as field>
			this.${field.name } = ${field.name};		
		</#list>
		}
		</#if>
		
		public override byte[] EncodeBody()
		{
			
			<#if (fields? size == 0)>
			return null;
			<#else>
			
			${commandBody} entity = new ${commandBody}();
			
			<#list fields as field>
			<#if field.arrayType == 1>
			<#if field.basicType == 1>
			entity.${field.name}.AddRange(this.${field.name});
			<#else>
			foreach(${field.name?cap_first} item in this.${field.name})
			{
				entity.${field.name}.Add(item.get${field.name?cap_first}Model());
			}
			</#if>
			<#else>
			<#if field.basicType == 1>
			entity.${field.name} = this.${field.name};
			<#else>
			entity.${field.name} = this.${field.name}.get${field.name?cap_first}Model();
			</#if>
			</#if>
			</#list>
			 MemoryStream ms = new MemoryStream();
			 using(ms){
             	Serializer.Serialize(ms, entity);
             	return ms.ToArray();
             }
			</#if>
		}
	
		public override void DecodeBody(byte[] bytes)
		{
			<#if (fields?size > 0)>
			
			
			MemoryStream ms = new MemoryStream(bytes);
			using(ms){
				${commandBody} entity = Serializer.Deserialize<${commandBody}>(ms);
				<#list fields as field>
					<#if field.arrayType == 1>
						<#if field.basicType == 1>
							this.${field.name} = new List<${field.csharpType}>();
							foreach (${field.csharpType} item in entity.${field.name}) {
								this.${field.name}.Add(item);
							}
						<#else>
							this.${field.name} = new List<${field.name?cap_first}>();
							foreach (${field.name?cap_first}Model item in entity.${field.name}) {
								${field.name?cap_first} newItem = new ${field.name?cap_first}();
								newItem.build(item);
								this.${field.name}.Add(newItem);
							}
						</#if>
					<#else>
						<#if field.basicType == 1>
							this.${field.name} = entity.${field.name};
						<#else>
							this.${field.name} = new ${field.name?cap_first}();
							this.${field.name}.build(entity.${field.name});
						</#if>
					</#if>
				</#list>
			}
			</#if>
		}
	}
}
