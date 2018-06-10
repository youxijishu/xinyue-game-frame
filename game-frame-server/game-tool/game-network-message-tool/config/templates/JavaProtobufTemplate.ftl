option java_package = "${packageName}";
option java_outer_classname = "${className}";
<#list messages as message>
message ${message.messageName}{
	<#list message.fields as field>
	${field.attr} ${field.protoType} ${field.name} = ${field.index};
	</#list>
} 
</#list>