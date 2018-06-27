package com.protocol.generate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentException;

import com.protocol.generate.common.BuilderProtobufFile;
import com.protocol.generate.common.ReadProtocolConfigFile;
import com.protocol.generate.csharps.BuildCSharpStructs;
import com.protocol.generate.csharps.BuildCsharpCommand;
import com.protocol.generate.csharps.BuilderCSharpProtobufFile;
import com.protocol.generate.model.ProtocolObject;

public class GenerateCSharpCode {
	public static void main(String[] args) {
		int i = 0;
		String outPath = "config/Csharp";
		File fileDir = new File("config/proto_config");
		File[] configfile = fileDir.listFiles();
		ReadProtocolConfigFile readProtocolConfigFile = new ReadProtocolConfigFile();
		List<ProtocolObject> protocolObjects = new ArrayList<>();
		String commandDirPath = null;
		for (File file : configfile) {
			if (!file.getName().endsWith(".xml")) {
				continue;
			}
			try {
				ProtocolObject protocolObject = readProtocolConfigFile.readFile(file);
				protocolObjects.add(protocolObject);
				//清空旧的数据
				commandDirPath = outPath;
				//File dir = new File(commandDirPath);
				//deleteDir(dir);
				String fileName = file.getName().substring(0, file.getName().length() - 4);
				protocolObject.setFileName(fileName);
				BuilderProtobufFile.build(protocolObject);
				BuilderCSharpProtobufFile.build(protocolObject.getPackageName(),commandDirPath, fileName);
			
				//生成structs
				BuildCSharpStructs.build(commandDirPath, protocolObject);
				//生成command
				BuildCsharpCommand.build(protocolObject, commandDirPath);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		//BuildCsharpCommandFactory.build(protocolObjects, commandDirPath);
		
	}

	/**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
