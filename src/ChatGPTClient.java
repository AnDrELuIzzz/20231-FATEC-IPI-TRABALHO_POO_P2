import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatGPTClient {
  public String criarPergunta(
      String OPENAI_API_KEY,
      String texto,
      int opcao) throws Exception {
    // Definir os prompts com base na opção escolhida
    String prompt;
    if (opcao == 1) {
      prompt = """
          Traduza o seguinte texto para o português %s.
          """.formatted(texto);
    } else if (opcao == 2) {
      prompt = """
          Me retorne três emoji para o seguinte filme %s.
          """.formatted(
          texto);
    } else if (opcao == 3) {
      prompt = """
          Forneça uma explicação, com até 30 palavras, adequada para uma criança de até 11 anos, para a seguinte pergunta: %s.
          """
          .formatted(
              texto);
    } else {
      throw new IllegalArgumentException("Opção inválida");
    }

    var requisicao = new ChatGPTRequest(
        "text-davinci-003",
        prompt,
        100);

    var gson = new Gson();

    var requisicaoJSON = gson.toJson(requisicao);

    RequestBody requestBody = RequestBody.create(
        requisicaoJSON,
        MediaType.parse("application/json"));

    OkHttpClient httpClient = new OkHttpClient();

    Request request = new Request.Builder()
        .url("https://api.openai.com/v1/completions")
        .addHeader("Media-Type", "application/json")
        .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
        .post(requestBody)
        .build();

    Response response = httpClient.newCall(request).execute();
    ChatGPTResponse chatGPTResponse = gson.fromJson(response.body().string(), ChatGPTResponse.class);
    String completion = chatGPTResponse.getChoices().get(0).getText();
    return completion;
  }
}