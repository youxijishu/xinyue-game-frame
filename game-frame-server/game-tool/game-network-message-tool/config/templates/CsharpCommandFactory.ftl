using Assets.Scripts.NewNetWork.Command;
using System;
namespace ${namespace}
{
	public class CommandFactory
	{
		public static ICommand getCommand(int commandId)
		{
			switch (commandId)
			{
				<#list commands as cmd>
				case ${cmd.id?c}:
					return new ${cmd.name?cap_first}();
				</#list>
			}
			throw new NullReferenceException("找不到commandId对应的command，" + commandId);
		}
	}
}