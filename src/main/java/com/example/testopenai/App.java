package com.example.testopenai;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletionContentPart;
import com.openai.models.chat.completions.ChatCompletionContentPartImage;
import com.openai.models.chat.completions.ChatCompletionContentPartImage.ImageUrl;
import com.openai.models.chat.completions.ChatCompletionContentPartText;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

public class App {
	public static String MODEL_NAME = "Qwen/Qwen2.5-VL-7B-Instruct";
	
	public boolean runQueries() {
		boolean isError = false;
    	OpenAIClient client = OpenAIOkHttpClient.builder().apiKey("MY_API_KEY").baseUrl("http://146.137.66.189:23333/v1").build();
    	//String queryText = "This image contains symbols or formulas representing radioactive isotopes, can you identify what they are?";
    	//String queryText = "Please transcribe all the text from the image and identify any symbols representing radioactive isotopes in the text.";
    	//String queryText = "Please transcribe all the text from the image.";
//    	String queryText = "This is an image taken from the screen of an isotope detector.  Can you tell me if device detected any isotopes and if so what they are?";
    	//Some of the possible isotopes are Cobalt-60 (Co60, Co-60, 60Co), Uranium-238 (238U, U238), Uranium-235 (235U, U235), Iodine-131 (I131, I-131, 131I), and Highly Enriched Uranium (HEU).";
//    	String queryText = "This is an image taken from the screen of an isotope detector.  Can you tell me if any isotopes were detected and if so what the isotope is?";
//    	String queryText = "This is an image taken from the screen of a radioactive element detector.  Can you determine if any radioactive isotopes were detected?  If any radioactive isotopes were detected please describe what they are.";
//    	String queryText = "Please list the radioactive isotopes found in the text of this image.";
//    	String queryText = "Please transcribe all the text from the image and identify any symbols representing radioactive isotopes in the text.";
    	//String queryText = "Please identify any radioactive isotopes in the text of this image. For reference WgPu stands for Weapons-Grade Plutonium, DU stands for Depleted Uranium and HEU stands for Highly Enriched Uranium";
    	
    	
    	//String queryText = "Please identify any words representing radioactive isotopes in the text of this image. For reference WgPu stands for Weapons-Grade Plutonium, DU stands for Depleted Uranium and HEU stands for Highly Enriched Uranium";
    	// You are a scientist looking at the screen of a chemical isotope detector and you are trying to determine if any isotopes or radioactive elements have been detected.
    	//String queryText = "Please transcribe all the text in this image.  Do any of the words or phrases represent chemical isotopes?";
    	String queryText1 = "You are a scientist looking at the screen of a chemical isotope detector and you are trying to determine if any isotopes or radioactive elements have been detected. Some of the possible text you might see and the matching isotopes are: Co60 is Cobalt-60, Co-60 is Cobalt-60, 60Co is Cobalt-60, CO60 is Cobalt-60, 238U is Uranium-238, U238 is Uranium-238, U-238 is Uranium-238, 238-U is Uranium-238, 235U is Uranium-235, U235 is Uranium-235, U-235 is Uranium-235 235-U is Uranium-235, I131 is Iodine-131, I-131 is Iodine-131, 131I is Iodine-131, 1131 is Iodine-131, HEU is Highly Enriched Uranium, Pu-239 is Plutonium-239, 239-Pu is Plutonium-239, Pu239 is Plutonium-239, Cs-137 is Cesium-137, Cs137 is Cesium-137, 137Cs is Cesium-137, WgPu is Weapons Grade Plutonium, WGPu is Weapons Grade Plutonium, K40 is Potassium-40, K-40 is Potassium-40, 40K is Potassium-40, 40-K is Potassium-40, Am-241 is Americium-241, Am241 is Americium-241, 241-Am is Americium-241, Ra-226 is Radium-226, 226-Ra is Radium-226, Ra226 is Radium-226, and DU is Depleted Uranium. But this list is not exhaustive and there may be other isotopes encountered.";
    	String queryText2 = "Please transcribe all the text in this image.  Identify all of the words or phrases that represent chemical isotopes or radioactive elements?";
//    	String queryText = "You are a scientist looking at the screen of a chemical isotope detector and you are trying to determine if any isotopes or radioactive elements have been detected. Please transcribe all the text in this image.  Identify all of the words or phrases that represent chemical isotopes or radioactive elements?";
    	String[] filePaths = { "/home/szymanski/visiondaq_images/device1_text_line.jpg", 
    			"/home/szymanski/visiondaq_images/device2_text_line.jpg",
    			"/home/szymanski/visiondaq_images/device3_text_line.jpg",
    			"/home/szymanski/visiondaq_images/device4_text_line.jpg",
    			"/home/szymanski/visiondaq_images/device5_area1.jpg",
    			"/home/szymanski/visiondaq_images/device6_text_line.jpg",
    			"/home/szymanski/visiondaq_images/device7_text_line.jpg",
    			"/home/szymanski/visiondaq_images/device8_text_1line.jpg",
    			"/home/szymanski/visiondaq_images/device8_text_2line.jpg",
    			"/home/szymanski/visiondaq_images/device8_area1.jpg"};

    	String[] directoryPaths = { "/home/szymanski/visiondaq_images/new_images/A401-1", 
    			"/home/szymanski/visiondaq_images/new_images/D3M-3",
    			"/home/szymanski/visiondaq_images/new_images/DR501-1",
    			"/home/szymanski/visiondaq_images/new_images/G501-BGO-1",
    			"/home/szymanski/visiondaq_images/new_images/G501-LaBr-1",
    			"/home/szymanski/visiondaq_images/new_images/G501-Nal-1",
//    			"/home/szymanski/visiondaq_images/new_images/graph",
    			"/home/szymanski/visiondaq_images/new_images/LoPro-1",
    			"/home/szymanski/visiondaq_images/new_images/R440_LaBr-1",
    			"/home/szymanski/visiondaq_images/new_images/RE-LaBr-1",
    			"/home/szymanski/visiondaq_images/new_images/SAM-950-1",
    			"/home/szymanski/visiondaq_images/new_images/SN12-1",
    			"/home/szymanski/visiondaq_images/orig_images"};

    	for (String dir : directoryPaths) {
    	Path new_images_dir_path = Paths.get(dir);
    	
    	try (DirectoryStream<Path> stream = Files.newDirectoryStream(new_images_dir_path)) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    System.out.println("File: " + new_images_dir_path + "/" + entry.getFileName());
    				ChatCompletionCreateParams createParams = createQueryParams(entry, queryText1, queryText2);
    				double startTime = System.currentTimeMillis();
    	    		client.chat().completions().create(createParams).choices().stream()
    	    			.flatMap(choice -> choice.message().content().stream())
    	    			.forEach(System.out::println);
    	    		System.out.println("Took " + (System.currentTimeMillis() - startTime) + "ms");
                } 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    	}
//    	for (String filePath : filePaths) {
//			try {
//				System.out.println("Querying image: " + filePath);
//				ChatCompletionCreateParams createParams = createQueryParams(filePath, queryText);
//				double startTime = System.currentTimeMillis();
//	    		client.chat().completions().create(createParams).choices().stream()
//	    			.flatMap(choice -> choice.message().content().stream())
//	    			.forEach(System.out::println);
//	    		System.out.println("Took " + (System.currentTimeMillis() - startTime) + "ms");
//			} catch (IOException e) {
//				e.printStackTrace();
//				isError = true;
//			}
//    	}
        System.out.println("End");
		return isError;
	}
	
    public static void main(String[] args) throws IOException {
    	App app = new App();
    	app.runQueries();
    }
    
    private ChatCompletionCreateParams createQueryParams(Path imageFilePath, String queryText1, String queryText2) throws IOException {
    	String imageBase64Url = getImageBase64(imageFilePath);
    	ImageUrl imageUrl = ChatCompletionContentPartImage.ImageUrl.builder()
                .url(imageBase64Url).build();
        ChatCompletionContentPart imageContentPart =
                ChatCompletionContentPart.ofImageUrl(ChatCompletionContentPartImage.builder()
                        .imageUrl(imageUrl).build());
        ChatCompletionContentPart questionContentPart1 =
                ChatCompletionContentPart.ofText(ChatCompletionContentPartText.builder()
                        .text(queryText1).build());
        ChatCompletionContentPart questionContentPart2 =
                ChatCompletionContentPart.ofText(ChatCompletionContentPartText.builder()
                        .text(queryText2).build());
        ChatCompletionCreateParams createParams = ChatCompletionCreateParams.builder()
                .model(MODEL_NAME)
                .maxCompletionTokens(2048)
                .addUserMessageOfArrayOfContentParts(List.of(questionContentPart1, questionContentPart2, imageContentPart))
                .build();
        
        return createParams;
    }
    
    private String getImageBase64(Path imageFilePath) throws IOException {
    	//Path imagePath = Paths.get(imageFilePath);
    	byte[] logoBytes = Files.readAllBytes(imageFilePath);
        String logoBase64Url = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(logoBytes);
    	return logoBase64Url;
    }



//  ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//  byte[] logoBytes = classloader.getResource("device1_area1.jpg").openStream().readAllBytes();
//    ResponseInputImage logoInputImage = ResponseInputImage.builder()
//            .detail(ResponseInputImage.Detail.AUTO)
//            .imageUrl(logoBase64Url)
//            .build();
//    ResponseInputItem messageInputItem = ResponseInputItem.ofMessage(ResponseInputItem.Message.builder()
//            .role(ResponseInputItem.Message.Role.USER)
//            .addInputTextContent("Please transcribe all the text from the image and identify any isotopes represented within the text.")
//            .addContent(logoInputImage)
//            .build());
//    ResponseCreateParams createParams = ResponseCreateParams.builder()
//            .inputOfResponse(List.of(messageInputItem))
//            .model("llava-hf/llava-interleave-qwen-7b-hf")
//            .build();
//
//    System.out.println("sending");
//    client.responses().create(createParams).output().stream()
//            .flatMap(item -> item.message().stream())
//            .flatMap(message -> message.content().stream())
//            .flatMap(content -> content.outputText().stream())
//            .forEach(outputText -> System.out.println(outputText.text()));


//	OpenAIClient client = OpenAIOkHttpClient.builder().apiKey("MY_API_KEY").baseUrl("http://146.137.66.189:23333/v1").build();
//
//	String queryText = "Please transcribe all the text from the image and identify any symbols representing radioactive isotopes in the text.";
////	String imageFilePath = "/home/szymanski/visiondaq_images/device1_area1.jpg";
//	String imageFilePath = "/home/szymanski/visiondaq_images/device8_text_1line.jpg";
//	String[] filePaths = { "/home/szymanski/visiondaq_images/device1_text_line.jpg", 
//			"/home/szymanski/visiondaq_images/device2_text_line.jpg",
//			"/home/szymanski/visiondaq_images/device3_text_line.jpg",
//			"/home/szymanski/visiondaq_images/device4_text_line.jpg",
//			"/home/szymanski/visiondaq_images/device5_text_line.jpg",
//			"/home/szymanski/visiondaq_images/device6_text_line.jpg",
//			"/home/szymanski/visiondaq_images/device7_text_line.jpg",
//			"/home/szymanski/visiondaq_images/device8_text_1line.jpg"};
//	Path imagePath = Paths.get(imageFilePath);
//	byte[] logoBytes = Files.readAllBytes(imagePath);
//	
//    String logoBase64Url = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(logoBytes);
//
//    ChatCompletionContentPart logoContentPart =
//            ChatCompletionContentPart.ofImageUrl(ChatCompletionContentPartImage.builder()
//                    .imageUrl(ChatCompletionContentPartImage.ImageUrl.builder()
//                            .url(logoBase64Url)
//                            .build())
//                    .build());
//    ChatCompletionContentPart questionContentPart =
//            ChatCompletionContentPart.ofText(ChatCompletionContentPartText.builder()
//                    .text("Please transcribe all the text from the image and identify any symbols representing radioactive isotopes in the text.")
//                    .build());
//    ChatCompletionCreateParams createParams = ChatCompletionCreateParams.builder()
//            .model("llava-hf/llava-interleave-qwen-7b-hf")
//            .maxCompletionTokens(2048)
//            .addUserMessageOfArrayOfContentParts(List.of(questionContentPart, logoContentPart))
//            .build();
//
//    client.chat().completions().create(createParams).choices().stream()
//            .flatMap(choice -> choice.message().content().stream())
//            .forEach(System.out::println);
//    System.out.println("got here");
}
