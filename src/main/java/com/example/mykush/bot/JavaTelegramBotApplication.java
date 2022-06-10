package com.example.mykush.bot;

import com.example.mykush.bot.service.UrlValidator;
import com.example.mykush.entity.dto.DeviceControlDTO;
import com.example.mykush.entity.dto.UserDTO;
import com.example.mykush.graph.LineChartEx;
import com.example.mykush.service.impl.TelegramBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.swing.event.HyperlinkEvent;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JavaTelegramBotApplication extends TelegramLongPollingBot {

    private final UrlValidator urlService;
    private final LineChartEx lineChartEx;
    private final TelegramBotService telegramBotService;

    @Value("${bot.username}")
    private String username;

    @Value("${bot.token}")
    private String token;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String userName = callbackQuery.getFrom().getUserName();
            String chatId = callbackQuery.getMessage().getChatId().toString();

            String data = update.getCallbackQuery().getData();
            sendChart(data, userName, chatId);
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            String chatId = update.getMessage().getChatId().toString();

            User user = update.getMessage().getFrom();

            if (message.equals("/start")) {
                sendMsg(chatId, "Вітаємо Вас в телеграм-боті Монітор." +"\uD83E\uDD73" +"\nДаний бот дає можливість робити підписки на будь-які гаджети " +
                        "(з магазину [Ябко](https://jabko.ua/iphone/)" +"\uD83D\uDCF1 " +
                        "та моніторити як змінювалась ціна на обраний вами товар з часом." + "\uD83D\uDCC8" +
                        "\n\nКожного дня ботом буде перевірятися ціна на обраний вами девайс." +
                        " Якщо вона зміниться - ви отримаєте повідомлення з актуальними цінами. \n\nДля того щоб здійснити підписку надішліть боту " +
                        "силку на будь-який гаджет із сайту зазначеного магазину");

            }

            //subscription
            if (urlService.isUrlValid(message)) {
                sendMsg(chatId, createSubscription(message, user, chatId));
            }

            if (message.equals("Переглянути підписки "+"\uD83D\uDD0D")) {
                sendMsgSubscription(chatId, user.getUserName(), "Ви маєте підписку на наступні девайси: ");
            }
            if (message.equals("Скасувати всі підписки "+"\uD83D\uDEAB")) {
                sendMsg(chatId, removeSubscription(user));
            }
        }

    }



    private void sendChart(String data, String username, String chatId) {
        List<DeviceControlDTO> deviceControlDTOs = getDeviceControlDTOs(username);

        for (int i = 0; i < deviceControlDTOs.size(); i++) {
            DeviceControlDTO deviceControlDTO = deviceControlDTOs.get(i);

            if (data.equals(deviceControlDTO.getModel() + " UA")) {
                byte[] chart = lineChartEx.gettingChartInUA(deviceControlDTO);
                try {
                    execute(sendImage(chatId, chart));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            } else if (data.equals(deviceControlDTO.getModel() + " USA")) {
                byte[] chartUSA = lineChartEx.gettingChartInUSA(deviceControlDTO);
                try {
                    execute(sendImage(chatId, chartUSA));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private synchronized void sendMsg(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        setButtons(sendMessage);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private synchronized void sendMsgReplyMarkup(String chatId, String message, String number) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        sendMessage.setReplyMarkup(setInline(number));
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public synchronized void setButtons(SendMessage sendMessage) {
        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add(new KeyboardButton("Переглянути підписки "+"\uD83D\uDD0D"));

        // Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardSecondRow.add(new KeyboardButton("Скасувати всі підписки "+"\uD83D\uDEAB"));

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    private String createSubscription(String message, User user, String chatId) {
        DeviceControlDTO subscribedDeviceInfo = telegramBotService.createSubscription(message, user, chatId);

        return String.format("Супер!" +"\uD83E\uDD73 "+ "Ви здійснили підписку на наступний гаджет"+"\uD83D\uDCF1"
                        + ": %s. На даний час його ціна %s, або %s." +"\uD83D\uDCB8 ",
                subscribedDeviceInfo.getModel(),
                subscribedDeviceInfo.getPriceUA(),
                subscribedDeviceInfo.getPriceUSA());
    }

    private List<DeviceControlDTO> getDeviceControlDTOs(String userName) {
        return telegramBotService.lookSubscription(userName);
    }

    private void sendMsgSubscription(String chatId, String userName, String returnMessage) {

        List<DeviceControlDTO> deviceControlDTOS = getDeviceControlDTOs(userName);
        if (!deviceControlDTOS.isEmpty()) {
            sendMsg(chatId, returnMessage);

            for (int i = 0; i < deviceControlDTOS.size(); i++) {
                DeviceControlDTO deviceControlDTO = deviceControlDTOS.get(i);
                String message = deviceControlDTO.toString();
                String model = deviceControlDTO.getModel();
                sendMsgReplyMarkup(chatId, message, model);
            }
        } else {
            sendMsg(chatId, "У вас немає жодної підписки." +"\uD83E\uDDD0");
        }
    }

    private String removeSubscription(User user) {
        String message = "Підписки успішно видалено "+"\uD83C\uDD97";
        try {
            telegramBotService.unsubscribe(user);
        } catch (Exception e) {
            message = "На даний час у вас немає жодної підписки."+"\uD83D\uDE40";
        }
        return message;
    }

    public void createDistribution() {
        List<UserDTO> userDTO = telegramBotService.createDistribution();
        userDTO.forEach(x -> sendMsg(x.getChatId(), "На підписані ваші девайси змінилась ціна:"));
        userDTO.forEach(x -> sendMsgSubscription(x.getChatId(), x.getUserName(), " "));
    }

    public SendPhoto sendImage(String chatId, byte[] chart) {
        SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(chatId);
        InputFile inputFile = new InputFile(new ByteArrayInputStream(chart), "graph");
        sendPhotoRequest.setPhoto(inputFile);
        return sendPhotoRequest;
    }

    private InlineKeyboardMarkup setInline(String number) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();

        InlineKeyboardButton inlineKeyboardButtonUA = new InlineKeyboardButton();
        inlineKeyboardButtonUA.setText("Монітор ціни UAH"+"\uD83C\uDDFA\uD83C\uDDE6");
        inlineKeyboardButtonUA.setCallbackData(number + " UA");

        InlineKeyboardButton inlineKeyboardButtonUSA = new InlineKeyboardButton();
        inlineKeyboardButtonUSA.setText("Монітор ціни USA"+"\uD83C\uDDFA\uD83C\uDDF8");
        inlineKeyboardButtonUSA.setCallbackData(number + " USA");

        buttons1.add(inlineKeyboardButtonUA);
        buttons1.add(inlineKeyboardButtonUSA);
        buttons.add(buttons1);

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        return markupKeyboard;
    }
}

