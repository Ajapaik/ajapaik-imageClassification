package ajapaik.imageClassificiation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.awt.image.BufferedImage;

public class ReadImage {
	public static final String OutdoorImageFoldUrl = "file:///disk1/git/ajapaik-imageClassification/src/main/resources/outdoortest/";
	public static final String indoorImageFoldUrl = "file:///disk1/git/ajapaik-imageClassification/src/main/resources/indoortest/";
	public static final String trainDataFile = "/disk1/git/ajapaik-imageClassification/src/main/resources/trainData.txt";
	public static final String testDataFile = "/disk1/git/ajapaik-imageClassification/src/main/resources/testData.txt";
	public static final boolean appendTrainData = true;
	public static final double trainPercent=0.1;
	
	public static String getGreyValuesFromImage(String urlString) {
		String greyString = "";

		BufferedImage bi;
		try {
			bi = ImageIO.read(new URL(urlString));

			int[] pixel;

			for (int y = 0; y < bi.getHeight(); y++) {
				for (int x = 0; x < bi.getWidth(); x++) {
					pixel = bi.getRaster().getPixel(x, y, new int[3]);
					greyString+=","+(pixel[0]+pixel[1]+pixel[2])/3;
					//System.out.print(pixel[0] + " - " + pixel[1] + " - " + pixel[2] + " - " + (bi.getWidth() * y + x)+"; ");
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return greyString.substring(1);
	}

	public static ArrayList<String> listFiles(String urlString) {
		ArrayList<String> fileNames= new ArrayList<String>();
		try {
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			InputStream inputStream = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

			String line = null;
			int i = 0;
			while ((line = reader.readLine()) != null) {
				fileNames.add(line);
				i++;
				System.out.println(line);
			}
			System.out.println("total files" + i);
			inputStream.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return fileNames;
	}

	public static void createTrainDataSet(String fileFold, String type) {
		//outdoor is 1
		ArrayList<String> outdoor_files=listFiles(fileFold);
		
		for (String file:outdoor_files) {
			String dataRow=getGreyValuesFromImage(fileFold+file);
			String[] pixels=dataRow.split(",");
			dataRow="";
			int i=1;
			for (int j=0;j<pixels.length;j++) {
				dataRow+=" "+(i++)+":"+pixels[j];
			}
			dataRow=type+" "+dataRow.substring(1);
			
			if (Math.random()<=trainPercent) {
				appendToFile(dataRow,trainDataFile);
				appendToFile(file,trainDataFile+".name.txt");
			} else {
				appendToFile(dataRow,testDataFile);
				appendToFile(file,testDataFile+".name.txt");
			}
			System.out.println(file+":"+dataRow);
		}
	}
	
	public static void readImagePixels(String fileFold) {
		
	}

	public static void appendToFile(String text, String fileName){
		if (text.isEmpty())
			return;
		try {
			File file = new File(fileName);

			FileWriter fw = new FileWriter(file, appendTrainData);
			fw.write(text+"\r\n");
			fw.close();

		} catch (IOException iox) {
			iox.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//getGreyValuesFromImage("file:///disk1/git/ajapaik-imageClassification/src/main/resources/outdoor/outdoors_400_013.jpg");
		//listFiles("file:///disk1/git/ajapaik-imageClassification/src/main/resources/outdoor/");
		createTrainDataSet(OutdoorImageFoldUrl, "1");
		createTrainDataSet(indoorImageFoldUrl, "2");
		//createFeatureTrainDataSet();
		//System.out.println("Done!");
	}

}
