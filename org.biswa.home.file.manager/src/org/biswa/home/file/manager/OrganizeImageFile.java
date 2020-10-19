package org.biswa.home.file.manager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

public class OrganizeImageFile {
	public static void main(String[] args) {
		File source = new File("/media/bbdalai/Elements");
		File target = new File("/media/bbdalai/Elements/MyOrganizedAlbum");
		organizeMyAlbum(source, target);
		
	}
	
	private static void organizeMyAlbum(File source,File target) {
		
		if(source.isDirectory()) {
			File[] listFiles = source.listFiles();
			for (File file : listFiles) {
				if(file.isDirectory() && file.getName().equals("MyOrganizedAlbum")) {
					continue;
				}
				organizeMyAlbum(file, target);
			}
		}else {
			if(source.getName().endsWith(".jpg")
					||source.getName().endsWith(".png")
					||source.getName().endsWith(".jpeg")
					||source.getName().endsWith(".PNG")
					||source.getName().endsWith(".JPEG")
					||source.getName().endsWith(".JPG")
					||source.getName().endsWith(".THM")
					||source.getName().endsWith(".MPG")
					||source.getName().endsWith(".mpg")
					||source.getName().endsWith(".AVI")
					||source.getName().endsWith(".DAT")
					||source.getName().endsWith(".mp4")) {
				boolean status = isDateOnImage(source);
				if(status) {
					String monthYear = getImageMonthYearFromName(source);
					moveFile(source, target, monthYear);
				}else {
					String monthYear = getImageMonthYearFromMetaData(source);
					moveFile(source, target, monthYear);
				}
			}
		}
	}
	
	private static String getImageMonthYearFromMetaData(File file) {
		Metadata drewmetadata = null;
		try {
			drewmetadata = ImageMetadataReader.readMetadata(file);
		} catch (Exception ipx) {
			System.out.println(ipx.getLocalizedMessage());
		}
		if (drewmetadata != null) {
			for (Directory directory : drewmetadata.getDirectories()) {
				for (Tag tag : directory.getTags()) {
					if(tag.getTagName().contains("Date/Time")) {
						Pattern pattern1 = Pattern.compile("(\\d{4}):(\\d{2}):(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2})");
						Matcher matcher1 = pattern1.matcher(tag.getDescription());
						if(matcher1.find()) {
							String year = matcher1.group(1);
							String month = matcher1.group(2);
							return month+"-"+year;
						}
						return "Unknown";
					}else if(tag.getTagName().contains("File Modified Date")) {
				        SimpleDateFormat parser = new SimpleDateFormat("EEE MMM d HH:mm:ss +05:30 yyyy");
				        try {
							Date date = parser.parse(tag.getDescription());
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(date);
							return calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.YEAR);
						} catch (ParseException e) {
						}
					}
				}
			}
		}
		return "Unknown";
	}
	
	private static void moveFile(File file,File targetAlbum, String folderName) {
		if(!targetAlbum.exists()) {
			targetAlbum.mkdirs();
		}
		
		File targetFolder = new File(targetAlbum,folderName);
		if(!targetFolder.exists()) {
			targetFolder.mkdirs();
		}
		File targetFile = new File(targetFolder,file.getName());
		
		try {
			Files.move(Paths.get(file.toURI()), Paths.get(targetFile.toURI()),StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Moved "+file.toURI()+" file into "+targetFolder.toURI()+" folder successfully.");
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
	
	private static boolean isDateOnImage(File file) {
		Pattern pattern1 = Pattern.compile("IMG-([\\d]{8})-WA(\\d*)?.jpg");
		Matcher matcher1 = pattern1.matcher(file.getName());
		Pattern pattern2 = Pattern.compile("IMG-([\\d]{8})-(\\d*)?.jpg");
		Matcher matcher2 = pattern2.matcher(file.getName());
		Pattern pattern3 = Pattern.compile("([\\d]{8})_(\\d*)?.jpg");
		Matcher matcher3 = pattern3.matcher(file.getName());
		if(matcher1.find() || matcher2.find()||matcher3.find()) {
			return true;
		}
		return false;
	}
	
	private static String getImageMonthYearFromName(File file) {
		Pattern pattern1 = Pattern.compile("IMG-(\\d{4})(\\d{2})(\\d{2})-WA(\\d*)?.jpg");
		Matcher matcher1 = pattern1.matcher(file.getName());
		Pattern pattern2 = Pattern.compile("IMG-(\\d{4})(\\d{2})(\\d{2})-(\\d*)?.jpg");
		Matcher matcher2 = pattern2.matcher(file.getName());
		Pattern pattern3 = Pattern.compile("(\\d{4})(\\d{2})(\\d{2})_(\\d*)?.jpg");
		Matcher matcher3 = pattern3.matcher(file.getName());
		if(matcher1.find()) {
			String year = matcher1.group(1);
			String month = matcher1.group(2);
			return month+"-"+year;
		}else if(matcher2.find()) {
			String year = matcher2.group(1);
			String month = matcher2.group(2);
			return month+"-"+year;
		}else if(matcher3.find()) {
			String year = matcher3.group(1);
			String month = matcher3.group(2);
			return month+"-"+year;
		}

		return "Unknown";
	}
	
}
