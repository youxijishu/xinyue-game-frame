using ProtoBuf;
using System.IO;
using System.Collections.Generic;
<#list imports as ns>
${ns}
</#list>

namespace Assets.Scripts.game_network.game_message_impl
{
	public class ${className}
	{
		<#list fields as field>
		// ${field.desc}
		<#if field.arrayType == 1>
		private List<${field.csharpType}> _${field.name};
		<#else>
		private ${field.csharpType} _${field.name};
		</#if>
		</#list>
		
		public ${className}()
		{
		
		}
		public ${className}(<#list fields as field><#if field.arrayType == 1>List<${field.csharpType}> ${field.name}<#else>${field.csharpType} ${field.name}</#if><#sep>, </#list>){
		<#list fields as field>
		this.${field.name } = ${field.name};		
		</#list>
		}
		
		<#list fields as field>
		
		<#if field.arrayType == 1>
		public List<${field.csharpType}> ${field.name}
		{
			get
			{
				return _${field.name};
			}
			
			set
			{
				_${field.name} = value;
			}
		}
		<#else>
		public ${field.csharpType} ${field.name}
		{
			get
			{
				return _${field.name};
			}
			
			set
			{
				_${field.name} = value;
			}
		}
		</#if>
		</#list>
		
		
		public void build(${namespace}.proto.${className}Model entity){
		<#list fields as field>
		<#if field.arrayType == 1>
			<#if field.basicType == 1>
				this._${field.name} = entity.${field.name};
			<#else>
				this._${field.name} = new List<${field.csharpType}>();
				foreach(proto.${field.name?cap_first}Model model in entity.${field.name}){
					${field.name?cap_first} newModel = new ${field.name?cap_first}();
					newModel.build(model);
					this._${field.name}.Add(newModel);
				}
			</#if>
			
		<#else>
			<#if field.basicType == 1>
				this._${field.name} = entity.${field.name};
			<#else>
				this._${field.name} = new ${field.name?cap_first}();
				this._${field.name}.build(entity.get${field.name?cap_first}());			
			</#if>
		</#if>
		
		</#list>

		}
		
		public ${namespace}.proto.${className}Model get${className}Model()
		{
			${namespace}.proto.${className}Model entity = new ${namespace}.proto.${className}Model();
		<#list fields as field>
		<#if field.arrayType == 1>
		
			<#if field.basicType == 1>
				
			foreach (${field.csharpType} item in this.${field.name}) {
				
				entity.${field.name}.Add(item);
			}
			
			<#else>
			
			foreach (${field.name?cap_first} item in this._${field.name}) {
				
				proto.${field.name?cap_first}Model model = item.get${field.name?cap_first}Model();
				entity.${field.name}.Add(model);
			}

		</#if>
		<#else>
		<#if field.basicType == 1>
			entity.${field.name}=this.${field.name};
			<#else>
			entity.${field.name} = this._${field.name}.get${field.name?cap_first}Model();
			</#if>
		</#if>
		
		</#list>

		return entity;
		}
	}
}
