package ir.mod.tavana.toranj.services.communication;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TopologyGenerator {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		int num = 4000;
		BufferedWriter writer = new BufferedWriter(new FileWriter("input_" + num + ".txt"));
		for(int i = 0; i < num; i++)
		{
			for(int j = 0; j < num; j++)
			{
				if( i == j)
					writer.append("0");
				else
					writer.append("1");
				if (j < num - 1)
					writer.append("\t");
			}
			writer.newLine();
		}
		writer.close();
	}

}
