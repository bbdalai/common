package org.biswa.home.file.manager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FindDuplicateFiles {
	private Map<String,List<String>> fileMap;

	public FindDuplicateFiles() {
		super();
		fileMap = new HashMap<String, List<String>>();
	}
	
	public void search(File file){
		if(file.isDirectory()) {
			File[] listFiles = file.listFiles();
			for (File file2 : listFiles) {
				search(file2);
			}
		}else {
			addEntryInMap(file);
		}
	}

	private void addEntryInMap(File file) {
		List<String> list = fileMap.get(file.getName());
		if(list==null) {
			list=new ArrayList<>();
		}
		String absolutePath = file.getPath();
		list.add(absolutePath);
		fileMap.put(file.getName(), list);
	}
	
	public static void main(String[] args) {
		FindDuplicateFiles duplicateFiles = new FindDuplicateFiles();
		duplicateFiles.search(new File("/media/bbdalai/Elements"));
		duplicateFiles.deleteDuplicates();
	}

	public void deleteDuplicates() {
		List<String> deletelist = new ArrayList<>();
		List<String> uniquelist = new ArrayList<>();
		Set<String> keySet = fileMap.keySet();
		for (String key : keySet) {
			List<String> list = fileMap.get(key);
			if(list.size()>1) {
				List<String> ulist = new ArrayList<>();
				for (String entry : list) {
					if(!ulist.contains(entry)) {
						ulist.add(entry);
					}
				}
				uniquelist.add(ulist.get(0));
				for (int i = 1; i < ulist.size(); i++) {
					deletelist.add(ulist.get(i));
				}
			}
		}
		
		export(uniquelist, "/home/bbdalai/unique.txt");
		export(deletelist, "/home/bbdalai/deletemarked.txt");
	}
	
	private void export(List<String> list, String filePath) {
		StringBuffer buffer = new StringBuffer();
		for (String key : list) {
			buffer.append(key);
			buffer.append("\n");
		}
				
		try(FileOutputStream fos = new FileOutputStream(filePath);
                BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            //convert string to byte array
            byte[] bytes = buffer.toString().getBytes();
            //write byte array to file
            bos.write(bytes);
            bos.close();
            fos.close();
            System.out.print("Data written to file \""+filePath+"\" successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        } 
	}
	
	private void export() {
		StringBuffer buffer = new StringBuffer();
		Set<String> keySet = fileMap.keySet();
		for (String key : keySet) {
			List<String> list = fileMap.get(key);
			buffer.append(key);
			buffer.append(",");
			buffer.append(list.size());
			buffer.append(",");
			for (String v : list) {
				buffer.append(v);
				buffer.append(",");
			}
			buffer.append("\n");
		}
		
		try(FileOutputStream fos = new FileOutputStream("/home/bbdalai/filelist.csv");
                BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            //convert string to byte array
            byte[] bytes = buffer.toString().getBytes();
            //write byte array to file
            bos.write(bytes);
            bos.close();
            fos.close();
            System.out.print("Data written to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        } 
	}

	/*private void print() {
		Set<String> keySet = fileMap.keySet();
		for (String key : keySet) {
			List<String> list = fileMap.get(key);
			if(list.size()>1) {
				System.out.println(key+","+list.size());
			}
		}
		
	}*/
	
}
