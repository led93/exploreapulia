package com.example.ep;

import com.example.ep.domain.Difficulty;
import com.example.ep.domain.Region;
import com.example.ep.service.TourPackageService;
import com.example.ep.service.TourService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;

@SpringBootApplication
public class ExploreapuliaApplication implements CommandLineRunner {

	@Value("${ec.importfile}")
	private String importFile;
	@Autowired
	private TourPackageService tourPackageService;
	@Autowired
	private TourService tourService;

	public static void main(String[] args) {
		SpringApplication.run(ExploreapuliaApplication.class, args);
	}

	private void createTourPackages() {
		tourPackageService.createTourPackage("BA", "Backpack Apu");
		tourPackageService.createTourPackage("AC", "Apulia Calm");
		tourPackageService.createTourPackage("AH", "Apulia Hot springs");
		tourPackageService.createTourPackage("CA", "Cycle Apulia");
		tourPackageService.createTourPackage("FS", "From Forest to Sea");
		tourPackageService.createTourPackage("KA", "Kids Apulia");
		tourPackageService.createTourPackage("NW", "Nature Watch");
		tourPackageService.createTourPackage("RA", "Rafting Apulia");
		tourPackageService.createTourPackage("TA", "Taste of Apulia");
	}

	private void createTours(String fileToImport) throws IOException{
		TourFromFile.read(fileToImport).forEach(importedTour ->
				tourService.createTour(
						importedTour.getTitle(),
						importedTour.getDescription(),
						importedTour.getBlurp(),
						importedTour.getPrice(),
						importedTour.getLength(),
						importedTour.getBullets(),
						importedTour.getKeywords(),
						importedTour.getPackageType(),
						importedTour.getDifficulty(),
						importedTour.getRegion()));
	}

	@Override
	public void run(String... args) throws IOException {
		createTourPackages();
		long numOfPackages = tourPackageService.total();
		createTours(importFile);
		long numOfTours = tourService.total();
	}

	/**
	 * Helper class to import ExploreApulia.json
	 */
	private static class TourFromFile {
		//fields
		private String packageType, title, description, blurp, price, length,
				bullets, keywords, difficulty, region;
		//reader
		static List<TourFromFile> read(String fileToImport) throws IOException {
			return new ObjectMapper().setVisibility(FIELD, ANY)
					.readValue(new FileInputStream(fileToImport), new TypeReference<>() {
					});
		}

		protected TourFromFile(){}

		public String getPackageType() {
			return packageType;
		}

		public String getTitle() {
			return title;
		}

		public String getDescription() {
			return description;
		}

		public String getBlurp() {
			return blurp;
		}

		public Integer getPrice() {
			return Integer.parseInt(price);
		}

		public String getLength() {
			return length;
		}

		public String getBullets() {
			return bullets;
		}

		public String getKeywords() {
			return keywords;
		}

		public Difficulty getDifficulty() {
			return Difficulty.valueOf(difficulty);
		}

		public Region getRegion() {
			return Region.findByLabel(region);
		}
	}
}
