package com.goldtek.main;

import java.io.IOException;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;

public class GeoAPI {
	GeoApiContext context;
	public GeoAPI() {
        //context = new GeoApiContext().setApiKey("AIzaSyAs71blnhxTVQj72XuGTgzkTIv5AqtDOlE");
		context = new GeoApiContext();
		context.setApiKey("AIzaSyAs71blnhxTVQj72XuGTgzkTIv5AqtDOlE");
	}
	
	public void testDistanceMatrix() {
		String[] depots = new String[] {
				"沅聖科技股份有限公司",
				"萊爾富中和連勝店",
				"萊爾富中和力德店",
				"萊爾富北縣員山店",
				"萊爾富北縣橋安店",//5
				"萊爾富北縣橋和二店",
				"美廉社中和民享店",
				"美廉社中和秀朗店",
				"美廉社中和壽德二店",
				"美廉社中和興南三店",//10
				//"美廉社中和華新店",
				};
		//String[] destinations = new String[] { "沅聖科技股份有限公司", "萊爾富中和連勝店", "萊爾富中和力德店, 萊爾富北縣員山店", "萊爾富北縣橋安店, 萊爾富北縣橋和二店" };
		try {
			DistanceMatrix matrix = DistanceMatrixApi.getDistanceMatrix(context, depots, depots).await();

			for (int i = 0; i < matrix.originAddresses.length; i++) {
				DistanceMatrixElement[] dm = matrix.rows[i].elements;
				for (int j = 0; j < dm.length; j++) {
					System.out.println(String.format("%s -> %s: %s", depots[i],
							depots[j], dm[j].distance.humanReadable));
				}
			}
		} catch (ApiException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
