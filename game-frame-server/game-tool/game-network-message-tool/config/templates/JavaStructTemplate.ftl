package ${packageName};
<#list imports as item>
${item}
</#list>

public class ${className}{
	<#list fields as field>
	// ${field.desc}
	<#if field.arrayType == 1>
	private List<${field.javaType}> ${field.name};
	<#else>
	private ${field.javaType} ${field.name};
	</#if>
   	
	</#list>
	
	public ${className}(){
	
	}
	
	public ${className}(<#list fields as field><#if field.arrayType == 1>List<${field.javaType}> ${field.name}<#else>${field.javaType} ${field.name}</#if><#sep>, </#list>){
		<#list fields as field>
		this.${field.name } = ${field.name};		
		</#list>
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
	
	public ${builder}.${className}Model.Builder builder(){
		${builder}.${className}Model.Builder builder = ${builder}.${className}Model.newBuilder();
		<#list fields as field>
		<#if field.arrayType == 1>
		if(this.${field.name} == null){
			builder.addAll${field.name?cap_first}(Collections.emptyList());
		} else {
		<#if field.basicType == 1>
			builder.addAll${field.name?cap_first}(this.${field.name});
		}
		<#else>
			List<${builder}.${field.name?cap_first}Model> modelList = new ArrayList<>();
			for (${field.name?cap_first} item : this.${field.name}) {
				${builder}.${field.name?cap_first}Model.Builder builder2 = item.builder();
				modelList.add(builder2.build());
			}
			builder.addAll${field.name?cap_first}(modelList);
			
		}
		</#if>
		<#else>
		<#if field.basicType == 1>
		builder.set${field.name?cap_first}(this.${field.name});
		<#else>
		builder.set${field.name?cap_first}(this.${field.name}.builder());
		</#if>
		</#if>
		
		</#list>
		return builder;
	}
	
	public void build(${builder}.${className}Model entity){
		<#list fields as field>
		<#if field.arrayType == 1>
			<#if field.basicType == 1>
				this.${field.name} = entity.get${field.name?cap_first}List();
			<#else>
				this.${field.name} = new ArrayList<>();
				for(${builder}.${field.name?cap_first}Model model : entity.get${field.name?cap_first}List()){
					${field.name?cap_first} newModel = new ${field.name?cap_first}();
					newModel.build(model);
					this.${field.name}.add(newModel);
				}
			</#if>
			
		<#else>
			<#if field.basicType == 1>
				this.${field.name} = entity.get${field.name?cap_first}();
			<#else>
				this.${field.name} = new ${field.name?cap_first}();
				this.${field.name}.build(entity.get${field.name?cap_first}());			
			</#if>
		</#if>
		
		</#list>
	}
}
