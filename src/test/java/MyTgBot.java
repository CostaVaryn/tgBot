import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MyTgBot extends TelegramLongPollingBot {
    public static final String BOT_TOKEN = getValue("bot_token");
    public static final String BOT_USERNAME = getValue("bot_username");
    public static final String URI = getValue("uri");
    public static final String CHAT_ID = getValue("chat_id");

    public MyTgBot() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(this);
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            switch (update.getMessage().getText()) {
                case "/help":
                    sendMessage("Привет, я бот! Я высылаю ссылки на картинки по запросу.\n" +
                            "Напоминаю, что картинки на сайте NASA обновляются раз в сутки");
                    break;
                case "/give":
                    try {
                        sendMessage(Utils.getUrl(URI));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                default:
                    sendMessage("Что-то не так, но я не знаю что!)");
                    break;
            }
        }
    }

    private void sendMessage(String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(CHAT_ID);
        message.setText(messageText);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String key) {
        try (InputStream stream = ClassLoader.getSystemResourceAsStream("app.properties")) {
            if (stream == null) {
                throw new FileNotFoundException();
            }
            System.getProperties().load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return System.getProperty(key);
    }
}
