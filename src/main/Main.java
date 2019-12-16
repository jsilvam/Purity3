package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utils.FileUtils;

public class Main {
	
	public static void main(String[] args) throws Exception {
		String repositoryUrl = null;
		String repositoryList = null;
		String refactoringFile = null;
		double timeLimit = 120;
		String outputFolder = null;
		String downloadFolder = null;
		boolean shouldLog = false;
		
		for(int i = 0; i<args.length; i++){
			switch(args[i]) {
			case "--url":
			case "-u":
				repositoryUrl = args[++i];
				break;
			case "--list":
			case "-l":
				repositoryList = args[++i];
				break;
			case "--refactorings":
			case "-r":
				refactoringFile = args[++i];
				break;
			case "--timeLimit":
			case "-t":
				timeLimit = Double.parseDouble(args[++i]);
				break;
			case "--output":
			case "-o":
				outputFolder = args[++i];
				break;
			case "--download":
			case "-d":
				downloadFolder = args[++i];
				break;
			case "-log":
				shouldLog = Boolean.parseBoolean(args[++i]);
				break;
			case "-help":
				help();
				break;
			default:
			}
		}
		
		if(outputFolder!=null && !outputFolder.isEmpty()) {
			FileUtils.setOutputFolder(outputFolder);
		}
		
		if(downloadFolder!=null && !downloadFolder.isEmpty()) {
			FileUtils.setDownloadFolder(downloadFolder);
		}
		
		
		if(repositoryList != null) {
			List<String> urls = getUrls(repositoryList);
			for(String url: urls) {
				Purity purity = new Purity(url,timeLimit);
				
				if(refactoringFile != null) {
					List<File> files = getRefactoringFiles(refactoringFile);
					for(File file: files)
						purity.analyse(file, shouldLog);
				}else
					throw new Exception("No input file provided!");
			}
			
		}else if (repositoryUrl != null) {
			Purity purity = new Purity(repositoryUrl, timeLimit);
			if(refactoringFile != null)
				purity.analyse(new File(refactoringFile), shouldLog);
			else
				throw new Exception("No input file provided!");
			
		}else
			throw new Exception("No url provided!");
	}
	
	
	private static void help() {
		System.out.println("-u, --url:            Repository's url *                      ");
		System.out.println("-l, --list:           File with list of repository's url *    ");
		System.out.println("-r, --refactorings:   File with refactorings **               ");
		System.out.println("-t, --timeLimit:      Time Limit                            (Default: 120)");
		System.out.println("-O, --output:         Output folder                         (Default: This directory)");
		System.out.println("-d, --download:       Download folder                       (Default: Temp directory)");
		System.out.println("-log:                 Log errors                            (Default: False)");
		System.out.println();
		System.out.println(" *   It is necessary a repository's urls or file with a list of repository's url");
		System.out.println(" **  It is necessary a file containg the commits with refactorings. If a list of repository's url is provided, then it is necessary a list of files as input.");
	}
	
	private static List<String> getUrls(String listFile) throws IOException{
		ArrayList<String> urls = new ArrayList<>();
		FileReader reader = new FileReader(listFile);
		BufferedReader buff = new BufferedReader(reader);
		
		String url = buff.readLine();
		while(url != null) {
			urls.add(url);
			url = buff.readLine();
		}
		buff.close();
		return urls;
	}
	
	private static List<File> getRefactoringFiles(String listFile) throws Exception{
		ArrayList<File> files = new ArrayList<>();
		FileReader reader = new FileReader(listFile);
		BufferedReader buff = new BufferedReader(reader);
		
		String file = buff.readLine();
		while(file != null) {
			File f = new File(file);
			if(!f.exists() || f.isDirectory())
				throw new Exception("invalid Input File: " + file);
			files.add(f);
			file = buff.readLine();
		}
		buff.close();
		return files;
	}
}
