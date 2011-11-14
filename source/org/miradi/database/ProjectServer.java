/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.json.JSONObject;
import org.martus.util.UnicodeReader;
import org.martus.util.UnicodeWriter;
import org.miradi.main.EAM;
import org.miradi.network.MiradiLocalFileSystem;
import org.miradi.utils.EnhancedJsonObject;

public class ProjectServer
{
	protected ProjectServer() throws IOException
	{
	}

	public Set<String> getListOfProjectsIn(String directory) throws Exception
	{
		return currentFileSystem.getListOfProjectsIn(directory);
	}

	protected int readLocalDataVersion(File projectDirectory) throws Exception
	{
		File dataDirectory = projectDirectory.getParentFile();
		String projectName = projectDirectory.getName();

		currentFileSystem = new MiradiLocalFileSystem();
		currentFileSystem.setDataLocation(dataDirectory.getAbsolutePath());
		return readProjectDataVersion(projectName);
	}

	private int readProjectDataVersion(String projectName) throws Exception, ParseException
	{
		File versionFile = getRelativeVersionFile();
		if(!currentFileSystem.doesFileExist(projectName, versionFile))
			throw new RuntimeException("No version file: " + versionFile);
		JSONObject version = readRelativeJsonFile(currentFileSystem, projectName, versionFile);
		int dataVersion = version.getInt(TAG_VERSION);
		return dataVersion;
	}

	protected static void writeLocalDataVersion(File projectDirectory, int versionToWrite) throws Exception
	{
		File dataDirectory = projectDirectory.getParentFile();
		String projectName = projectDirectory.getName();
		
		MiradiLocalFileSystem fileSystem = new MiradiLocalFileSystem();
		fileSystem.setDataLocation(dataDirectory.getAbsolutePath());
		
		EnhancedJsonObject version = createVersionJson(versionToWrite);
		writeRelativeJsonFile(fileSystem, projectName, getRelativeVersionFile(), version);
	}
	
	private static EnhancedJsonObject createVersionJson(int versionToWrite)
	{
		EnhancedJsonObject version = new EnhancedJsonObject();
		version.put(TAG_VERSION, versionToWrite);
		return version;
	}
	
	private static EnhancedJsonObject readRelativeJsonFile(MiradiLocalFileSystem fileSystem, String projectName, File relativeFile)
	throws Exception, ParseException
	{
		String contents = fileSystem.readFile(projectName, relativeFile);
		return new EnhancedJsonObject(contents);
	}

	private static void writeRelativeJsonFile(MiradiLocalFileSystem fileSystem, String projectName, File relativeObjectFile,
			EnhancedJsonObject json) throws Exception
	{
		String contents = json.toString();
		fileSystem.writeFile(projectName, relativeObjectFile, contents);
	}
	
	private static boolean isExistingProject(File projectDirectory) throws Exception
	{
		if(projectDirectory == null)
			return false;
		
		if(!projectDirectory.exists())
			return false;

		try
		{
			File versionFile = new File(projectDirectory, getRelativeVersionFile().getPath());
			return versionFile.exists();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}

	public static boolean isExistingLocalProject(File projectDirectory) throws Exception
	{
		if(projectDirectory == null)
			return false;
		return isExistingProject(projectDirectory);
	}

	public static String readLocalLastModifiedProjectTime(File projectDirectory) throws Exception
	{
		try
		{
			File lastModifiedTimeFile = getRelativeLastModifiedTimeFile();
			if (doesFileExist(projectDirectory, lastModifiedTimeFile))
				return readFile(projectDirectory, lastModifiedTimeFile);
			
			long lastModifiedMillisFromOperatingSystem = projectDirectory.lastModified();
			String lastModifiedTimeFromOperatingSystem = ProjectServer.timestampToString(lastModifiedMillisFromOperatingSystem);
			writeFile(projectDirectory, lastModifiedTimeFile, lastModifiedTimeFromOperatingSystem);
			return lastModifiedTimeFromOperatingSystem;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return EAM.text("Unknown");
		}
	}
	
	private static String readFile(File projectDirectory, File relativePath) throws Exception
	{
		File path = new File(projectDirectory, relativePath.getPath());
		UnicodeReader reader = new UnicodeReader(path);
		String contents = reader.readAll();
		reader.close();
		return contents;
	}

	private static void writeFile(File projectDirectory,File relativePath, String contents) throws Exception
	{
		if(!doesProjectDirectoryExist(projectDirectory))
			throw new FileNotFoundException("No project directory: " + projectDirectory);
		
		File path = new File(projectDirectory, relativePath.getPath());
		path.getParentFile().mkdirs();
		try
		{
			UnicodeWriter writer = new UnicodeWriter(path);
			try
			{
				writer.write(contents);
			}
			finally
			{
				writer.close();
			}
		}
		catch (Exception e)
		{
			EAM.handleWriteFailure(path, e);
		}
	}

	private static boolean doesProjectDirectoryExist(File projectDirectory) throws Exception
	{
		if(!projectDirectory.exists())
			return false;
		if(!projectDirectory.isDirectory())
			return false;
		return true;
	}

	private static boolean doesFileExist(File projectDirectory, File relativePath)
	{
		File file = new File(projectDirectory, relativePath.getPath());
		return file.exists();
	}
	
	public static String timestampToString(long lastModifiedMillis)
	{
		Date date = new Date(lastModifiedMillis);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(date);
	}

	private static File getRelativeLastModifiedTimeFile()
	{
		return new File(LAST_MODIFIED_FILE_NAME);
	}

	private static File getRelativeJsonDirectory()
	{
		return new File(JSON_DIRECTORY);
	}

	private static File getRelativeVersionFile()
	{
		return new File(getRelativeJsonDirectory(), VERSION_FILE);
	}

	public static final int DATA_VERSION = 61;
	public static final String LAST_MODIFIED_FILE_NAME = "LastModifiedProjectTime.txt";
	public static final String TAG_VERSION = "Version";
	private static final String JSON_DIRECTORY = "json";

	public static String PROJECTINFO_FILE = "project";
	private static String VERSION_FILE = "version";

	private MiradiLocalFileSystem currentFileSystem;
}
