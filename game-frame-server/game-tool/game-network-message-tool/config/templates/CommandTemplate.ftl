package ${packageName};
import com.xinyue.network.message.common.AbstractGameMessage;
import com.xinyue.network.message.common.GameMessageMetaData;
import com.xinyue.network.message.common.GameMessageType;
import com.xinyue.utils.JsonUtil;
import com.xinyue.network.EnumServerType;

<#list imports as pck>
import ${pck};
</#list>
//河南心悦网络科技有限公司   王广帅
//${desc}

@GameMessageMetaData(serverType = EnumServerType.${serverType},messageId = ${id?c}, messageType =${messageType})
public class ${className} extends AbstractGameMessage {
	<#list fields as field>
	//${field.desc}
   	<#if field.arrayType == 1>
	private List<${field.javaType}> ${field.name};
	<#else>
	private ${field.javaType} ${field.name};
	</#if>
	</#list>
	
	public ${className}(){
	}
	<#if (fields?size > 0)>
	public ${className}(<#list fields as field><#if field.arrayType == 1>List<${field.javaType}> ${field.name}<#else>${field.javaType} ${field.name}</#if><#sep>, </#list>){
		<#list fields as field>
		this.${field.name } = ${field.name};		
		</#list>
	}
	</#if>
	
	
	public ${newResponse} newResponse() {
		${newResponse} response = new ${newResponse}();
		copyMessageHead(response.getMessageHead());
		return response;
	}
	<#list fields as field>
	<#if field.arrayType == 1>
	public void set${field.name?cap_first} (List<${field.javaType}> ${field.name}){
		this.${field.name} = ${field.name};
	}
		
	public List<${field.javaType}> get${field.name?cap_first}(){
		return ${field.name};
	}
	<#else>
	
	public void set${field.name?cap_first} (${field.javaType} ${field.name}){
		this.${field.name} = ${field.name};
	}
		
	public ${field.javaType} get${field.name?cap_first}(){
		return ${field.name};
	}
	</#if>
	</#list>
	
	@Override
	public void decodeBody(byte[] bytes) throws Exception {
		<#if (fields?size > 0)>
		${commandBody} body = ${commandBody}.parseFrom(bytes);
		<#list fields as field>
		
		<#if field.basicType == 1>
		<#if field.arrayType == 1>
			this.${field.name} = body.get${field.name?cap_first}List();
		<#else>
			this.${field.name} = body.get${field.name?cap_first}();
		</#if>
		<#else>
		<#if field.arrayType == 1>
		this.${field.name} = new ArrayList<>();
		for (${builder}.${field.name?cap_first}Model itemModel : body.get${field.name?cap_first}List()) {
			${field.name?cap_first} item = new ${field.name?cap_first}();
			item.build(itemModel);
			this.${field.name}.add(item);
		}
		<#else>
			this.${field.name} = new ${field.name?cap_first}();
			this.${field.name}.build(body.get${field.name?cap_first}());	
		</#if>
	
		</#if>
		</#list>
		</#if>
	}
	
	@Override
	public byte[] encodeBody() {  
		 if(this.getMessageHead().getErrorCode() != 0){
		  	return null;
		 }
		 
		 <#if (fields?size == 0)>
		 return null;
		 <#else>
		 ${commandBody}.Builder builder = ${commandBody}.newBuilder();
		 <#list fields as field>
		 <#if field.basicType == 1>
		 <#if field.arrayType == 1>
		 builder.addAll${field.name?cap_first}(this.${field.name});
		 <#else>
		  builder.set${field.name?cap_first}(this.${field.name});
		 </#if>
		 <#else>
		 <#if field.arrayType == 1>
		 if(this.${field.name} == null){
		 	this.${field.name} = Collections.emptyList();
		 }
		 List<${builder}.${field.name?cap_first}Model> models = new ArrayList<>();
		 for(${field.name?cap_first} item : this.${field.name}){
		 	${builder}.${field.name?cap_first}Model.Builder itemBuilder = item.builder();
		 	models.add(itemBuilder.build());
		 }
		 builder.addAll${field.name?cap_first}(models);
		 <#else>
		 builder.set${field.name?cap_first}(this.${field.name}.builder());
		 </#if>
		 </#if>
		 </#list>
		 return builder.build().toByteArray();
		 </#if>
	}
	@Override
	public String toString(){
		String info = "${className}->" + JsonUtil.objToJson(this);
		return info;
	}
}
