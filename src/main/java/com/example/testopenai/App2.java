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

public class App2 {
	public static String MODEL_NAME = "Qwen/Qwen2.5-VL-7B-Instruct";
//	public static String MODEL_NAME = "qwen2.5vl:7b";
	
	public boolean runQueries() {
		boolean isError = false;
		// attempt to use AskSage api
//    	OpenAIClient client = OpenAIOkHttpClient.builder().apiKey("sk-b8f49199307d4a33a1ff104561ed6217").baseUrl("https://janus.egs.anl.gov/v1").build();
		OpenAIClient client = OpenAIOkHttpClient.builder().apiKey("MY_API_KEY").baseUrl("http://146.137.66.189:23333/v1").build();
    	//String queryText = "Please transcribe all the text from the image and give all the words found in a comma separated value list and keep words and numbers connected by the '-' symbol if it is present";
//    	String queryText = "You are a scientist looking at the screen of a chemical isotope detector and you are trying to determine if any isotopes or radioactive elements have been detected. Please transcribe all the text in this image.  Identify all of the words or phrases that represent chemical isotopes or radioactive elements and please return only a comma separated value list with the identified isotopes.";
    	String queryText = "You are a scientist looking at the screen of a chemical isotope detector and you are trying to determine if any isotopes or radioactive elements have been detected. Please identify all of the words or phrases in the text of this image that represent chemical isotopes or radioactive elements and please return only a comma separated value list with the identified items.";

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
    				ChatCompletionCreateParams createParams = createQueryParams(entry, queryText);
    				double startTime = System.currentTimeMillis();
    	    		List<String> vals = client.chat().completions().create(createParams).choices().stream()
    	    			.flatMap(choice -> choice.message().content().stream()).toList();
//    	    			.forEach(System.out::println);
    	    		System.out.println(String.join(":", vals));
    	    		String listString = vals.get(0);
    	    		String[] elems = listString.split("\\s*,\\s*");
    	    		for (String elem : elems) {
    	    			String queryText2 = "Does " + elem + " represent a radioactive isotope? If so, which one? Answer with minimal explanation.";
        				ChatCompletionCreateParams createParams2 = createQueryParams2(queryText2);
        	    		List<String> vals2 = client.chat().completions().create(createParams2).choices().stream()
        	    			.flatMap(choice -> choice.message().content().stream()).toList();
//        	    			.forEach(System.out::println);
        	    		System.out.println(String.join(":", vals2));
    	    		}
    	    		System.out.println("Took " + (System.currentTimeMillis() - startTime) + "ms");
                } 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    	}
        System.out.println("End");
		return isError;
	}
	
    public static void main(String[] args) throws IOException {
    	App2 app = new App2();
    	app.runQueries();
    }
    
    private ChatCompletionCreateParams createQueryParams(Path imageFilePath, String queryText) throws IOException {
    	String imageBase64Url = getImageBase64(imageFilePath);
    	ImageUrl imageUrl = ChatCompletionContentPartImage.ImageUrl.builder()
                .url(imageBase64Url).build();
        ChatCompletionContentPart imageContentPart =
                ChatCompletionContentPart.ofImageUrl(ChatCompletionContentPartImage.builder()
                        .imageUrl(imageUrl).build());
        ChatCompletionContentPart questionContentPart2 =
                ChatCompletionContentPart.ofText(ChatCompletionContentPartText.builder()
                        .text(queryText).build());
        ChatCompletionCreateParams createParams = ChatCompletionCreateParams.builder()
                .model(MODEL_NAME)
                .maxCompletionTokens(2048)
                .addUserMessageOfArrayOfContentParts(List.of(questionContentPart2, imageContentPart))
                .build();
        
        return createParams;
    }
    
    private ChatCompletionCreateParams createQueryParams2(String queryText) throws IOException {
        ChatCompletionCreateParams createParams = ChatCompletionCreateParams.builder()
                .model(MODEL_NAME)
                .maxCompletionTokens(2048)
                .addUserMessage(queryText)
                .build();
        
        return createParams;
    }
    
    private String getImageBase64(Path imageFilePath) throws IOException {
    	//Path imagePath = Paths.get(imageFilePath);
    	byte[] logoBytes = Files.readAllBytes(imageFilePath);
        String logoBase64Url = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(logoBytes);
    	return logoBase64Url;
    }
}
